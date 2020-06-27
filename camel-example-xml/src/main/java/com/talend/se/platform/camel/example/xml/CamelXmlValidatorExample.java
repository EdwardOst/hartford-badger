package com.talend.se.platform.camel.example.xml;

import java.util.Optional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelXmlValidatorExample extends RouteBuilder {

	private static Logger logger = LoggerFactory.getLogger(CamelXmlValidatorExample.class);

	private final String data_dir;
	private final String filename;
	private final String targetEndpoint;
	
	public CamelXmlValidatorExample(final String filepath, final String filename, final String targetEndpoint) {
		this.data_dir = Optional.of(filepath).orElseThrow( ()-> new NullArgumentException("filepath") );
		this.filename = Optional.of(filename).orElseThrow( ()-> new NullArgumentException("filename") );
		this.targetEndpoint = Optional.of(targetEndpoint).orElseThrow( ()-> new NullArgumentException("targetEndpoint") );
		logger.info("Camel XmlValidator listening on '" + filepath + "' for file '" + filename + "'");
	}

	@Override
	public void configure() throws Exception {
		from("file://" + this.data_dir + "?noop=true&fileName=" + this.filename)
		.to(targetEndpoint);
	}

}
