package com.talend.se.platform.camel.tde;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.camel.builder.RouteBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

@Builder(toBuilder=true)
@Value
@With
@JsonDeserialize(builder = BindingComponent.BindingComponentBuilder.class)
public class BindingComponent {

    private final String name;
    private final String routeId;
    private final String fromUri;
    private final String toUri;
    @Singular private Map<String, String> attributes;
    @JsonIgnore private final RouteBuilder routeBuilder = new RouteBuilder() {
	    @Override
	    public void configure() throws Exception {
	        from(fromUri)
	                .routeId(routeId)
	                .to(toUri);
	    }
    };
    
    private static ObjectMapper objectMapper = null;
    private static ObjectMapper getObjectMapper() {
    	if (BindingComponent.objectMapper == null) {
    		BindingComponent.objectMapper = new ObjectMapper();
    	}
    	return BindingComponent.objectMapper;
    }
    
    public static BindingComponent from(String json) throws JsonParseException, JsonMappingException, IOException {
    	return from(json, BindingComponent.getObjectMapper());
    }
    
    public static BindingComponent from(String json, ObjectMapper mapper) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, BindingComponent.class);
    }
    
    public static BindingComponent from(File jsonFile) throws JsonParseException, JsonMappingException, IOException {
    	return from(jsonFile, BindingComponent.getObjectMapper());
    }
    
    public static BindingComponent from(File jsonFile, ObjectMapper mapper) throws JsonParseException, JsonMappingException, IOException {
    	return mapper.readValue(jsonFile, BindingComponent.class);
    }
    
    public String toString() {
    	ObjectMapper mapper = BindingComponent.getObjectMapper();
		String result;
		try {
			result = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new  RuntimeException(e);
		}
		return result;
    }
    
    @JsonPOJOBuilder(withPrefix = "")
    public static class BindingComponentBuilder {
    	
    	// exclude routeBuilder from Builder and exclude it from Jackson serialization
    	@JsonIgnore 
    	private BindingComponentBuilder routeBuilder(RouteBuilder routeBuilder) {
    		return this;
    	}
    }

}
