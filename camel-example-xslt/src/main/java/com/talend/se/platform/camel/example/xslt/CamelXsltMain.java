package com.talend.se.platform.camel.example.xslt;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelXsltMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(CamelXsltMain.class);

	private static final String MARSHAL_URI = "direct:xslt";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/camel-example-xml-dataformat/src/test/resources";
		final String filename = "shiporder.xml";
		final String xsltUri="xml-to-json.xsl";

		CamelXsltMain main = new CamelXsltMain();

		CamelXslt xsltRoute = new CamelXslt(xsltUri, MARSHAL_URI);
		CamelXsltExample exampleRoute = new CamelXsltExample(filepath, filename, xsltRoute.getSourceEndpoint());

		main.addRouteBuilder(xsltRoute);
		main.addRouteBuilder(exampleRoute);

		main.run(args);
	}
}
