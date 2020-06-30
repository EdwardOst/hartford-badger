package com.talend.se.platform.camel.tde;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;

public class RouteFactory {

	public void addRoute(Exchange e) throws Exception {
		e.getContext().addRoutes(e.getIn().getBody(RoutesBuilder.class));
	}
	
}
