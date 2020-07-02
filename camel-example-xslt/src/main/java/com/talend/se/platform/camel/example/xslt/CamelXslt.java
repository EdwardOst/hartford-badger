package com.talend.se.platform.camel.example.xslt;

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

public class CamelXslt extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelXslt.class);

	private String sourceEndpoint;
	private String xsltUri;

	public CamelXslt(final String xsltUri, final String sourceEndpoint) {
		this.xsltUri = Optional.ofNullable(xsltUri)
				.orElseThrow(() -> new NullArgumentException("xsltUri"));
		this.sourceEndpoint = Optional.ofNullable(sourceEndpoint)
				.orElseThrow(() -> new NullArgumentException("sourceEndpoint"));
	}

	@Override
	public void configure() throws Exception {
		logger.warn("READING FROM " + sourceEndpoint);
		from(sourceEndpoint)
			.removeHeaders("*")
			.to("xslt:" + xsltUri + "?transformerFactoryClass=net.sf.saxon.TransformerFactoryImpl")
			.log(LoggingLevel.WARN, logger, "message: ${body}");
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

}
