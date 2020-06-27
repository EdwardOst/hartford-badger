package com.talend.se.platform.camel.example.xml;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;


public class CamelXmlValidator extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelXmlValidator.class);

	private final String schemaPath;
	
	private String sourceEndpoint;
	
	public CamelXmlValidator(final String schema, final String sourceEndpoint) {
		this.schemaPath = Optional.of(schema).orElseThrow( ()-> new NullArgumentException("schema_path") );
		this.sourceEndpoint = Optional.of(sourceEndpoint).orElseThrow( ()-> new NullArgumentException("sourceEndpoint") );
	}
	
	@Override
	public void configure() throws Exception {
		from(sourceEndpoint)
		.convertBodyTo(String.class)
		.log(LoggingLevel.WARN, logger, "message: ${body}")
		.to("validator:" + schemaPath)
		.to("log:xml-validation?showHeaders=true");

//		.log(LoggingLevel.WARN, logger, "validation: ${body}");
	}

	public String getSchemaPath() {
		return schemaPath;
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

}
