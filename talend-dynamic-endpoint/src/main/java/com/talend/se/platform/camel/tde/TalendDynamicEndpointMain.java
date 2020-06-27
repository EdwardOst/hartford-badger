package com.talend.se.platform.camel.tde;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.camel.Processor;

public class TalendDynamicEndpointMain extends Main {

	private static Logger logger = LoggerFactory.getLogger(TalendDynamicEndpointMain.class);

	private static final String MARSHAL_URI = "direct:json-dataformat";
	
	public static void main(String[] args) throws Exception {
		final String filepath = "/Users/eost/git/hartford-badger/talend-dynamic-endpoint/src/main/resources";
		final String filename = "sample-person-message.json";

		logger.info("starting");
		TalendDynamicEndpointMain main = new TalendDynamicEndpointMain();

		TalendDynamicEndpointExampleUnmarshal dataFormatRoute = new TalendDynamicEndpointExampleUnmarshal(MARSHAL_URI);

		main.addRouteBuilder(dataFormatRoute);

		BindingComponent bindingComponent = new BindingComponent.BindingComponentBuilder()
				.name("bc-name")
				.routeId("dynamicEndpoint")
				.fromUri("file://" + filepath + "?noop=true&fileName=" + filename)
				.toUri(MARSHAL_URI)
				.build();
		
		RouteBuilder loadBindingComponent = new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("timer:startBindingComponent?repeatCount=1&delay=3000")
					.process(new Processor() {

						@Override
						public void process(Exchange exchange) throws Exception {
							// TODO Auto-generated method stub
							exchange.getContext().addRoutes(bindingComponent);
						}
						
					});
			}
		};
		
		main.addRouteBuilder(loadBindingComponent);
		
		main.run(args);
		logger.info("FINISHED");
	}
}
