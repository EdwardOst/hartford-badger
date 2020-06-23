package com.talend.se.hartfoder.dynamic_endpoint;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EndpointFactoryRoute extends RouteBuilder {

	@Override
    public void configure() {
        from("timer://world?fixedRate=true&period=1000")
            .log("${{mymessage}}");
    }
}
