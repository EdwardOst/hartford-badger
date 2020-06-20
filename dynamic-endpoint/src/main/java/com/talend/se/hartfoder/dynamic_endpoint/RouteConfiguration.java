package com.talend.se.hartfoder.dynamic_endpoint;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RouteConfiguration {

//	  @Bean
	  RouteBuilder myRouter() {
	    return new RouteBuilder() {

//	      @Override
	      public void configure() throws Exception {
	        from("timer://earth?fixedRate=true&period=3000")
	        .log("hello earth");
	      }

	    };
	  }
}
