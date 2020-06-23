package com.talend.se.platform.camel.example.json;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Endpoint;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.networknt.schema.ValidationMessage;

import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;

public class TestJsonSchemaValidation extends CamelTestSupport {

	private static Logger logger = LoggerFactory.getLogger(TestJsonSchemaValidation.class);
	
	private Endpoint testEndpoint;
	private final String filepath = "/Users/eost/git/hartford-badger/camel-example-json/src/test/resources";
	
	@Override
	protected RoutesBuilder createRouteBuilder() throws Exception {
		final String schema = "sample-person-schema.json";

		CamelContext camelContext = context();
		testEndpoint = camelContext.getComponent("direct").createEndpoint("direct:test-json-schema-vaidation");
		return new CamelJsonValidator(schema, testEndpoint);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testMissingRequiredField() throws IOException, CamelExecutionException {
		final String filename = "sample-person-message-invalid-missing-last-name.json";
		String message = Files.readString(Path.of(filepath + "/" + filename));

		thrown.expect(CamelExecutionException.class);
		thrown.expectCause(new JsonValidationMatches("required", "1028", "lastName"));
		
		try {
			String result = fluentTemplate()
								.withBody(message)
								.to(testEndpoint)
								.request(String.class);
			logger.debug(result);
		} catch (CamelExecutionException e) {
			if (e.getCause() instanceof JsonValidationException) {
				JsonValidationException validationError = (JsonValidationException) e.getCause();
				validationError.getErrors().forEach(error -> printJsonValidationMessage(System.out, error));
			}
			throw e;
		}
		
	}

	@Test
	public void testExtraField() throws IOException, CamelExecutionException {
		final String filename = "sample-person-message-invalid-extra-field.json";
		String message = Files.readString(Path.of(filepath + "/" + filename));

		thrown.expect(CamelExecutionException.class);
		thrown.expectCause(new JsonValidationMatches("additionalProperties", "1001", "extra_field"));
		
		try {
			String result = fluentTemplate()
								.withBody(message)
								.to(testEndpoint)
								.request(String.class);
			logger.debug(result);
		} catch (CamelExecutionException e) {
			if (e.getCause() instanceof JsonValidationException) {
				JsonValidationException validationError = (JsonValidationException) e.getCause();
				validationError.getErrors().forEach(error -> printJsonValidationMessage(System.out, error));
			}
			throw e;
		}
		
	}

	@Test
	public void testNonNumericField() throws IOException, CamelExecutionException {
		final String filename = "sample-person-message-invalid-non-numeric-age.json";
		String message = Files.readString(Path.of(filepath + "/" + filename));

		thrown.expect(CamelExecutionException.class);
		thrown.expectCause(new JsonValidationMatches("type", "1029", "string"));
		
		try {
			String result = fluentTemplate()
								.withBody(message)
								.to(testEndpoint)
								.request(String.class);
			logger.debug(result);
		} catch (CamelExecutionException e) {
			if (e.getCause() instanceof JsonValidationException) {
				JsonValidationException validationError = (JsonValidationException) e.getCause();
				validationError.getErrors().forEach(error -> printJsonValidationMessage(System.out, error));
			}
			throw e;
		}
		
	}
	
	@Test
	public void testValidMessage() throws IOException, CamelExecutionException {
		final String filename = "sample-person-message.json";
		String message = Files.readString(Path.of(filepath + "/" + filename));

		String result = fluentTemplate()
							.withBody(message)
							.to(testEndpoint)
							.request(String.class);
		logger.debug(result);
		assertEquals(message, result);
	}

	private static class JsonValidationMatches extends TypeSafeMatcher<JsonValidationException> {
		private String type;
		private String code;
		private String argument;
	  
	    public JsonValidationMatches(String type, String code, String argument) {
	        this.type = type;
	        this.code = code;
	        this.argument = argument;
	    }
	  
	    @Override
	    protected boolean matchesSafely(JsonValidationException item) {
	    	Set<ValidationMessage> errors = item.getErrors();
	    	if (errors.size() != 1) return false;
	    	ValidationMessage error = errors.iterator().next();
	    	if (error == null) return false;
	    	return error.getType().contentEquals(type)
	    		&& error.getCode().contentEquals(code)
	    		&& error.getArguments()[0].contentEquals(argument);
	    }
	  
	    @Override
	    public void describeTo(Description description) {
	        description
	        	.appendText("JsonValidationException ")
	        	.appendText("expects type ")
	        	.appendValue(type)
	        	.appendText("expects code ")
	        	.appendValue(code)
	        	.appendText("expects argument ")
	        	.appendValue(argument);
	    }
	  
	    @Override
	    protected void describeMismatchSafely(JsonValidationException item, Description mismatchDescription) {
	    	Set<ValidationMessage> errors = item.getErrors();
	    	if (errors.size() != 1) {
		        mismatchDescription
		        	.appendText("exactly one Error expected, actual size ")
		        	.appendValue(errors.size());
		        return;
	    	}
	    	ValidationMessage error = errors.iterator().next();
	    	if (error == null) {
		        mismatchDescription
	        	.appendText("unexpected empty ValidationMessage list");
	    		return;
	    	}
	        mismatchDescription
	        	.appendText("type was ")
	        	.appendValue(error.getType())
	        	.appendText("code was ")
	        	.appendValue(error.getCode())
	        	.appendText("argument was ")
	        	.appendValue(error.getArguments()[0]);
	    }
	}
	
	public static void printJsonValidationMessage(PrintStream pstream, ValidationMessage validationMessage) {
		pstream.printf("type: %s\n", validationMessage.getType());
		pstream.printf("code: %s\n", validationMessage.getCode());
		pstream.printf("message: %s\n", validationMessage.getMessage());
		pstream.printf("path: %s\n", validationMessage.getPath());
		pstream.printf("arguments: ");
		Arrays.asList(validationMessage.getArguments()).forEach(item -> pstream.printf("%s ",item));
		pstream.println();
		if (validationMessage.getDetails() != null) {
			validationMessage.getDetails().forEach((String key, Object value) -> pstream.printf("    %s: %s\n", key, value.toString()));
		}
	}
	
}
