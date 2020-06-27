package com.talend.se.platform.camel.example.xml.dataformat;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelXmlDataFormatMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelXmlDataFormatMain.class);

	private static final String MARSHAL_URI = "direct:xml-dataformat";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-xml-dataformat/src/test/resources";
//		final String filename = "shiporder.xml";
		final String filename = "shiporder-invalid-missing-order-id.xml";
		final String schema = "classpath:shiporder.xsd";

		CamelXmlDataFormatMain main = new CamelXmlDataFormatMain();

		CamelXmlDataFormat dataFormatRoute = new CamelXmlDataFormat(MARSHAL_URI);
		CamelXmlDataFormatExample exampleRoute = new CamelXmlDataFormatExample(filepath, filename, dataFormatRoute.getSourceEndpoint());

		main.addRouteBuilder(dataFormatRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);
	}
}
