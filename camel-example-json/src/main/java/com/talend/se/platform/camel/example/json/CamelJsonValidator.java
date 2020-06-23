package com.talend.se.platform.camel.example.json;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;

public class CamelJsonValidator extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelJsonValidator.class);

	private final String schema;
	
	private Endpoint sourceEndpoint;
	
	public CamelJsonValidator(final String schema, final Endpoint sourceEndpoint) {
		this.schema = schema;
		this.sourceEndpoint = Optional.of(sourceEndpoint).orElseThrow( ()-> new NullArgumentException("sourceEndpoint") );
	}
	
	@Override
	public void configure() throws Exception {
		from(sourceEndpoint)
		.convertBodyTo(String.class)
		.log(LoggingLevel.WARN, logger, "message: ${body}")
		.to("json-validator:" + schema)
		.to("log:json-validation?showHeaders=true");

//		.log(LoggingLevel.WARN, logger, "validation: ${body}");
	}

	public String getSchema() {
		return schema;
	}

	public Endpoint getSourceEndpoint() {
		return sourceEndpoint;
	}

}
