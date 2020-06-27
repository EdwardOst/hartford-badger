package com.talend.se.platform.camel.example.json.dataformat;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelJsonDataFormatMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelJsonDataFormatMain.class);

	private static final String MARSHAL_URI = "direct:json-dataformat";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-json-dataformat/src/test/resources";
		final String filename = "sample-person-message.json";
//		final String filename = "sample-person-message-invalid-extra-field.json";
//		final String filename = "sample-person-message-invalid-missing-last-name.json";
//		final String filename = "sample-person-message-invalid-non-numeric-age.json";

		CamelJsonDataFormatMain main = new CamelJsonDataFormatMain();

		CamelJsonDataFormat dataFormatRoute = new CamelJsonDataFormat(MARSHAL_URI);
		CamelJsonDataFormatExample exampleRoute = new CamelJsonDataFormatExample(filepath, filename, dataFormatRoute.getSourceEndpoint());

		main.addRouteBuilder(dataFormatRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);
	}
}
