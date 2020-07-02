package com.talend.se.platform.camel.example.json.dataformat;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import java.util.Map;


public class CamelJsonDataFormat extends RouteBuilder {

	private static final Logger logger = LoggerFactory.getLogger(CamelJsonDataFormat.class);

	private String sourceEndpoint;
	
	public CamelJsonDataFormat(final String sourceEndpoint) {
		this.sourceEndpoint = Optional.ofNullable(sourceEndpoint).orElseThrow( ()-> new NullArgumentException("sourceEndpoint") );
	}
	
	@Override
	public void configure() throws Exception {
		from(sourceEndpoint)
		.convertBodyTo(String.class)
		.log(LoggingLevel.WARN, logger, "message: ${body}")
		.unmarshal().json(JsonLibrary.Jackson)
		.process(new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("**** body.class = " + exchange.getIn().getBody().getClass().getName());
				Map<String, Object> bodyMap = exchange.getIn().getBody(Map.class);
				for (Map.Entry<String, Object> e : bodyMap.entrySet()) {
					System.out.println(e.getKey() + ": " + e.getValue().toString());
				}
			}
			
		})
		.setBody().simple("$simple{body[lastName]}")
		.to("log:xml-validation?showHeaders=true");	
	}

	public String getSourceEndpoint() {
		return sourceEndpoint;
	}

}
