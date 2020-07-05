package com.talend.se.platform.camel.example.avro.dataformat;

import java.io.File;

import org.apache.avro.Schema;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelAvroDataFormatMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelAvroDataFormatMain.class);

	private static final String MARSHAL_URI = "direct:avro-dataformat";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-avro-dataformat/src/main/resources";
		final String filename = "person.avro";
		final String schemaFileName = "person.avro.avsc";

		final CamelAvroDataFormatMain main = new CamelAvroDataFormatMain();
		String schemaPath = main.getClass().getClassLoader().getResource(schemaFileName).getFile();
		final Schema schema = new org.apache.avro.Schema.Parser().parse(new File(schemaPath));

		CamelAvroDataFormat dataFormatRoute = new CamelAvroDataFormat(schema, MARSHAL_URI);
		CamelAvroDataFormatExample exampleRoute = new CamelAvroDataFormatExample(filepath, filename, dataFormatRoute.getSourceEndpoint());

		main.addRouteBuilder(dataFormatRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);
	}
}
