package com.talend.se.platform.camel.tde;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Processor;

public class TalendDynamicEndpointMain extends Main {

	private static final String TDE_BINDING_COMPONENT_JSON_FILE = "/tde/binding_component.json";

	private static Logger logger = LoggerFactory.getLogger(TalendDynamicEndpointMain.class);

	private static final String MARSHAL_URI = "direct:json-dataformat";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/talend-dynamic-endpoint/src/main/resources";
		final String filename = "sample-person-message.json";

		logger.info("starting");
		TalendDynamicEndpointMain main = new TalendDynamicEndpointMain();

		TalendDynamicEndpointExampleUnmarshal dataFormatRoute = new TalendDynamicEndpointExampleUnmarshal(MARSHAL_URI);

		main.addRouteBuilder(dataFormatRoute);

//		final BindingComponent.BindingComponentBuilder builder = BindingComponent.builder()
//				.name("bc-name")
//				.routeId("dynamicEndpoint")
//				.fromUri("file://" + filepath + "?noop=true&fileName=" + filename)
//				.toUri(MARSHAL_URI);
//		logger.info(builder.toString());
//		final BindingComponent bindingComponent = builder.build();

//		ObjectMapper mapper = new ObjectMapper();

//		System.out.println("*** BindingComponent ***");
//		System.out.println("bindingComponent = " + bindingComponent.toString());
//		String bindingComponentJson = mapper.writeValueAsString(bindingComponent);
//		mapper.writeValue(Paths.get(TDE_BINDING_COMPONENT_JSON_FILE).toFile(), bindingComponent);

//		System.out.println("********* bc ***********");
//		BindingComponent bc;
//		bc = BindingComponent.from(Paths.get(TDE_BINDING_COMPONENT_JSON_FILE).toFile());
//		System.out.println("bc from JsonFile = " + bc.toString());
//		
//		System.out.println("******* bc2 ************");
//		BindingComponent bc2;
//		bc2 = BindingComponent.from(bindingComponentJson);
//		System.out.println("bc2 from JsonString = " + bc2.toString());
//		
//		System.out.println("************************");

		RouteBuilder loadBindingComponent = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("timer:startBindingComponent?repeatCount=1&delay=3000")
					.pollEnrich("file://" + Path.ofNullable(TDE_BINDING_COMPONENT_JSON_FILE).getParent() + "?noop=true&fileName=" + Path.ofNullable(TDE_BINDING_COMPONENT_JSON_FILE).getFileName())
					.bean(BindingComponent.class, "from(String)")
					.setBody(simple("${body.getRouteBuilder}"))
					.bean(RouteFactory.class);
				
//				from("timer:readResource?repeatCount=1")
//					.setBody(simple("resource:classpath:sample-person-messagejson"))
//					.log("${body}");
			}
		};
		
		main.addRouteBuilder(loadBindingComponent);
		
		main.run(args);
		logger.info("FINISHED");
	}
}
