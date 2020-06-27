package com.talend.se.platform.camel.example.xml;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelXmlMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelXmlMain.class);

	private static final String VALIDATION_URI = "direct:xml-schema-validation";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-xml/src/test/resources";
//		final String filename = "shiporder.xml";
		final String filename = "shiporder-invalid-missing-order-id.xml";
		final String schema = "classpath:shiporder.xsd";

		CamelXmlMain main = new CamelXmlMain();

		CamelXmlValidator validatorRoute = new CamelXmlValidator(schema, VALIDATION_URI);
		CamelXmlValidatorExample exampleRoute = new CamelXmlValidatorExample(filepath, filename, validatorRoute.getSourceEndpoint());

		main.addRouteBuilder(validatorRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);
	}
}
