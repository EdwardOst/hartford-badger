package com.talend.se.platform.camel.example.json;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.camel.CamelContext;
import org.apache.camel.main.Main;
import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelJsonMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelJsonMain.class);

	private static final String VALIDATION_URI = "direct:json-schema-validation";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-json/src/test/resources";
		final String filename = "sample-person-message.json";
//		final String filename = "sample-person-message-invalid-extra-field.json";
//		final String filename = "sample-person-message-invalid-missing-last-name.json";
//		final String filename = "sample-person-message-invalid-non-numeric-age.json";
		final String schema = "sample-person-schema.json";

		CamelJsonMain main = new CamelJsonMain();

		CamelJsonValidator validatorRoute = new CamelJsonValidator(schema, VALIDATION_URI);
		CamelJsonValidatorExample exampleRoute = new CamelJsonValidatorExample(filepath, filename, validatorRoute.getSourceEndpoint());

		main.addRouteBuilder(validatorRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);

	}

}
