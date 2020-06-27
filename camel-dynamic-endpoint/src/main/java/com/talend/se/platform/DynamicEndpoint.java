package com.talend.se.platform;

import org.apache.camel.builder.RouteBuilder;

public class DynamicEndpoint extends RouteBuilder {

	private String name;
	private String routeId;
	private String fromUri;
	private String toUri;

	public DynamicEndpoint() {
	}

	public DynamicEndpoint(String name, String routeId, String fromUri, String toUri) {
		this.name = name;
		this.routeId = routeId;
		this.fromUri = fromUri;
		this.toUri = toUri;
	}
	
	@Override
	public void configure() throws Exception {
		from(fromUri)
			.routeId(routeId)
			.to(toUri);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getFromUri() {
		return fromUri;
	}

	public void setFromUri(String fromUri) {
		this.fromUri = fromUri;
	}

	public String getToUri() {
		return toUri;
	}

	public void setToUri(String toUri) {
		this.toUri = toUri;
	}

}
