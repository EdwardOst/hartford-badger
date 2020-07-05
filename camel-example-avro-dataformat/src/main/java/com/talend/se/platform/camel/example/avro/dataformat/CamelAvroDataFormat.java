package com.talend.se.platform.camel.example.avro.dataformat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.avro.AvroDataFormat;
import org.apache.avro.Schema;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

import org.apache.camel.LoggingLevel;


public class CamelAvroDataFormat extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelAvroDataFormat.class);

	private final Schema schema;
	private final String sourceEndpoint;
	
	public CamelAvroDataFormat(final Schema schema, final String sourceEndpoint) {
		this.schema = Optional.ofNullable(schema).orElseThrow( ()-> new NullArgumentException("schema") );
		this.sourceEndpoint = Optional.ofNullable(sourceEndpoint).orElseThrow( ()-> new NullArgumentException("sourceEndpoint") );
	}
	
	@Override
	public void configure() throws Exception {
		AvroDataFormat format = new AvroDataFormat(schema);

		from(sourceEndpoint)
		.marshal(format)
		.to("log:avro?showHeaders=true")
		.unmarshal(format)
		.log(LoggingLevel.WARN, logger, "firstName: ${body.firstName}, lastName: ${body.lastName}, age: ${body.age}");

//		This route would be used to consume a file in avro format, but it does not work and throws an error.
//			org.apache.avro.AvroRuntimeException: Malformed data. Length is negative: -40
//
//		AvroDataFormat format = new AvroDataFormat(schema);
//		from(sourceEndpoint)
//		.convertBodyTo(String.class)
//		.unmarshal(format)
//		.log(LoggingLevel.WARN, logger, "firstName: ${body.firstName}, lastName: ${body.lastName}, age: ${body.age}")
//		.marshal(format)
//		.to("log:avro?showHeaders=true");
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

}
