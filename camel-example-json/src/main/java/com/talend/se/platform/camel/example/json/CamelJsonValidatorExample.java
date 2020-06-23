package com.talend.se.platform.camel.example.json;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelJsonValidatorExample extends RouteBuilder {

	private static Logger logger = LoggerFactory.getLogger(CamelJsonValidatorExample.class);

	private final String filepath;
	private final String filename;
	private final Endpoint targetEndpoint;
	
	public CamelJsonValidatorExample(final String filepath, final String filename, Endpoint targetEndpoint) {
		this.filepath = filepath;
		this.filename = filename;
		this.targetEndpoint = targetEndpoint;
		logger.info("Camel JsonValidator listening on '" + filepath + "' for file '" + filename + "'");
	}

	@Override
	public void configure() throws Exception {
		from("file://" + this.filepath + "?noop=true&fileName=" + this.filename)
		.to(targetEndpoint);
	}

}
