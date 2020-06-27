package com.talend.se.platform.camel.tde;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;

public class BindingComponent extends RouteBuilder {

    private String name;
    private String routeId;
    private String fromUri;
    private String toUri;
    private Map<String, String> attributes;
    
    public BindingComponent() {
    }
    
    public BindingComponent(String name, String routeId, String from, String to, Map<String, String> attributes) {
        this.name = name;
        this.routeId = routeId;
        this.fromUri = from;
        this.toUri = to;
        this.attributes = attributes;
    }
    
    public static BindingComponentBuilder builder() {
    	return new BindingComponentBuilder();
    }

    public static class BindingComponentBuilder {
        private String name;
        private String routeId;
        private String fromUri;
        private String toUri;
        private Map<String, String> attributes;

        BindingComponentBuilder() {
        }

        public BindingComponentBuilder name(String name) {
        	this.name =  name;
        	return this;
        }
        
        public BindingComponentBuilder routeId(String routeId) {
        	this.routeId =  routeId;
        	return this;
        }

        public BindingComponentBuilder fromUri(String fromUri) {
        	this.fromUri =  fromUri;
        	return this;
        }
    
        public BindingComponentBuilder toUri(String toUri) {
        	this.toUri =  toUri;
        	return this;
        }
        
        public BindingComponentBuilder attribute(String attrKey, String attrValue) {
            if (this.attributes == null) {
            	this.attributes = new java.util.HashMap<>();
            	}
            this.attributes.put(attrKey, attrValue);
            return this;        	
        }

        public BindingComponentBuilder attributes(Map<String, String> attributes) {
            if (this.attributes == null) {
            	this.attributes = new java.util.HashMap<>();
            	}
        	this.attributes.putAll(attributes);
        	return this;
        }
        
        public BindingComponentBuilder clearAttributes() {
        	if (this.attributes != null) {
        		this.attributes.clear();
        	}
        	return this;
        }
    
        public BindingComponent build() {
        	return new BindingComponent(this.name, this.routeId, this.fromUri, this.toUri, this.attributes);
        }
        
    }
    
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
    	this.routeId = routeId;
    }
    
    @Override
    public void configure() throws Exception {
        from(this.fromUri)
                .routeId(this.getRouteId())
                .to(this.toUri);
    }

    
    
}

