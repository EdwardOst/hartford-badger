package com.talend.se.platform.camel.example.avro.dataformat;

import java.io.File;
import java.util.Optional;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.avro.AvroDataFormat;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelAvroDataFormatExample extends RouteBuilder {

	private static Logger logger = LoggerFactory.getLogger(CamelAvroDataFormatExample.class);

	private final String data_dir;
	private final String filename;
	private final String targetEndpoint;
	
	public CamelAvroDataFormatExample(final String filepath, final String filename, final String targetEndpoint) {
		this.data_dir = Optional.ofNullable(filepath).orElseThrow( ()-> new NullArgumentException("filepath") );
		this.filename = Optional.ofNullable(filename).orElseThrow( ()-> new NullArgumentException("filename") );
		this.targetEndpoint = Optional.ofNullable(targetEndpoint).orElseThrow( ()-> new NullArgumentException("targetEndpoint") );
		logger.info("Camel Avro data format listening on '" + filepath + "' for file '" + filename + "'");
	}

	@Override
	public void configure() throws Exception {
		Person person = Person.newBuilder().setFirstName("Edward").setLastName("Ost").setAge(53).build();
		from("timer:mytimer?repeatCount=1")
			.setBody(() -> person)
			.to(targetEndpoint);

		
		// Read an Avro file
		from("file://" + this.data_dir + "?noop=true&fileName=" + this.filename)
			.process( new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					DatumReader<Person> personDatumReader = new SpecificDatumReader<Person>(Person.class);
					DataFileReader<Person> personDataFileReader = new DataFileReader<Person>(exchange.getIn().getBody(File.class), personDatumReader);
					exchange.getIn().setBody(personDataFileReader);
				}
			})
		.split().simple("${body}")
		.log(LoggingLevel.WARN, logger, "person: ${body}")
		.to("direct:avro-output");
//		.to("direct:json-output");
//		.to("direct:avro-marshal-output");

		AvroDataFormat format = new AvroDataFormat(Person.SCHEMA$);
		
		// Convert body to a string, the result will be a json file
		from("direct:json-output")
		.convertBodyTo(String.class)
		.to("file://" + this.data_dir + "/out?fileName=person.json");

		// Write marshalled content, but this is NOT a valid avro file
//		from("direct:avro-marshal-output")
//		.marshal(format)
//		.to("file://" + this.data_dir + "/out?fileName=person.marshalled.avro");
		
		// Use Avro DataWriter to write as valid Avro file
		// While this shows the principle of using DataFileWriter, it does not adequately
		// handle resource management since it closes the DataFileWriter after just one message.
		DatumWriter<Person> personDatumWriter = new SpecificDatumWriter<Person>(Person.class);
		DataFileWriter<Person> personDataFileWriter = new DataFileWriter<Person>(personDatumWriter);
		personDataFileWriter.create(Person.SCHEMA$, new File(this.data_dir + "/out/person.avro"));
		from("direct:avro-output")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				personDataFileWriter.append(exchange.getIn().getBody(Person.class));
				personDataFileWriter.close();
				}
		});
//		.to("file://" + this.data_dir + "/out?fileName=person.avro");
	}

}
