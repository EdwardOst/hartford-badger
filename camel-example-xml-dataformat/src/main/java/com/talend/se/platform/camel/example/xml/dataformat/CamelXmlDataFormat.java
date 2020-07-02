package com.talend.se.platform.camel.example.xml.dataformat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import java.util.Map;
import java.util.Map.Entry;


public class CamelXmlDataFormat extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelXmlDataFormat.class);

	private String sourceEndpoint;
	
	public CamelXmlDataFormat(final String sourceEndpoint) {
		this.sourceEndpoint = Optional.ofNullable(sourceEndpoint).orElseThrow( ()-> new NullArgumentException("sourceEndpoint") );
	}
	
	@Override
	public void configure() throws Exception {
		from(sourceEndpoint)
		.convertBodyTo(String.class)
		.log(LoggingLevel.WARN, logger, "message: ${body}")
		.unmarshal().jacksonxml()
		.setBody().simple("$simple{body[shipto][name]}")
		.to("log:xml-validation?showHeaders=true");	
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

}
