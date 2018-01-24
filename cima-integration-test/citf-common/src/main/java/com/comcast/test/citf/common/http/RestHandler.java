package com.comcast.test.citf.common.http;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * Class for handling all REST based operations i.e. GET, POST,
 * PUT and DELETE.
 * 
 * @author arej001c
 *
 */

@Service("restHandler")
public class RestHandler {
	
	private static final String LINESOFBUSINESS = "linesOfBusiness";
	/**
	 * Sets requestType
	 * @param requestType
	 * 			Type of request
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
    /**
     * Sets responseType
     * @param responseType
     * 			Response type
     */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * Enumeration for REST methods.
	 */
	public enum WriteRequestMethod{
		POST,
		PUT,
		DELETE,
		GET
	}
	
	/**
	 * Returns HTTP status code.
	 * 
	 * @return HTTP status.
	 */
	public int getStatusCode() {
		return this.statusCode;
	}
	
	/**
	 * Constructor to be called from unit test
	 * 
	 * @return instance of RestClient
	 */
	public RestClient getClient() {
		return new RestClient();
	}
	
	/**
	 * Performs a web service Get request in the specified url and returns result.
	 * 
	 * @param url 
	 * 				GET request URL.
	 * @return
	 * 				GET request Response.
	 */
	public String executeReadRequest(String url){
		LOGGER.debug("Start executing GET request on {}", url);

		RestClient client = getClient();
		Resource res = client.resource(url);
		String result = res.contentType(this.requestType).accept(this.responseType).get(String.class);

		LOGGER.info("GET request execution is over with result : {}", result);
		return result;
	}
	
	/**
	 * Performs web service Post request with the body and header in the provided url and returns the result.
	 * 
	 * @param url 
	 * 				POST request URL.
	 * @param parameters
	 * 				Body of the request.
	 * @param headers 
	 * 				Header of the request.
	 * @param method 
	 * 				Could have POST/PUT
	 * @return
	 * 				POST request response.
	 */
	public String executeWriteRequest(String url, Map<String, String> parameters, Map<String, String> headers, WriteRequestMethod method){
		
		String result = null;
		String inputParams = null;
		ClientResponse response = null;
		
		LOGGER.info("Start executing {} request on {} with {} parameters and header {}", method, url, parameters, headers);

		if(MediaType.APPLICATION_FORM_URLENCODED.equalsIgnoreCase(this.requestType)) {
			inputParams = this.getStringInput(parameters);
		} else if (MediaType.APPLICATION_JSON.equalsIgnoreCase(this.requestType)) {
			inputParams = this.getJsonInput(parameters);
		}
		LOGGER.info("Input parameters {}", inputParams);
		RestClient client = getClient();
		Resource res = client.resource(url);

		if(headers!=null && !headers.isEmpty()){
			for(Map.Entry<String, String> entry : headers.entrySet()){
				res = res.header(entry.getKey(), entry.getValue());
			}
		}

		res = res.contentType(this.requestType).accept(this.responseType);	

		if(WriteRequestMethod.POST.equals(method)) {
			response = res.post(inputParams);
		}
		else if(WriteRequestMethod.PUT.equals(method)) {
			response = res.put(inputParams);
		}
		else if(WriteRequestMethod.DELETE.equals(method)) {
			response = res.delete();
		}
		else if(WriteRequestMethod.GET.equals(method)) {
			response = res.get();
		}

		if(response!=null) {
			result = response.getEntity(String.class);
			statusCode = response.getStatusCode();
		}

		LOGGER.info("{} request execution is over with result : {}", method, result);
		return result;
	}
	
	/**
	 * Performs web service Post request with the body in the specified url and returns result.
	 * 
	 * @param url 
	 * 				POST request URL.
	 * @param body
	 * 				Body of the request.
	 * @param method 
	 * 				POST/PUT.
	 * @return
	 * 				POST request response.
	 */
	public String executeWriteRequest(String url, String body, WriteRequestMethod method){
		String result = null;
		ClientResponse response = null;
		LOGGER.debug("Start executing {} request on {} with {} body", method, url, body);
		
		RestClient client = getClient();
		Resource res = client.resource(url);	
		res = res.contentType(this.requestType).accept(this.responseType);	

		if(WriteRequestMethod.POST.equals(method)) {
			response = res.post(body);
		}
		else if(WriteRequestMethod.PUT.equals(method)) {
			response = res.put(body);
		}

		if(response!=null){
			result = response.getEntity(String.class);
			statusCode = response.getStatusCode();
		}

		LOGGER.info("{} request execution is over with result : {}", method, result);
		return result;
	}
	
	
	/**
	 * Utility method to format string
	 * 
	 * @param parameters
	 * 			Map of parameters
	 * @return formatted string
	 */
	public String getStringInput(Map<String, String> parameters){
		StringBuilder sbf = null;
		String result = ICommonConstants.BLANK_STRING;
		
		if(parameters!=null){
			for(Map.Entry<String, String> entry : parameters.entrySet()){
				String key = entry.getKey();
				
				if(sbf != null) {
					sbf.append(ICommonConstants.AMPERSAND_SIGN);
				} else {
					sbf = new StringBuilder();
				}
				sbf.append(key);
				sbf.append(ICommonConstants.EQUALS_SIGN);
				sbf.append(entry.getValue());
			}
			
			result = sbf!=null ? sbf.toString() : null;
			LOGGER.debug("Prepared String input to execute the request : {}", result);
		}
		
		return result;
	}
	
	/**
	 * Utility method to generate JSON input
	 * 
	 * @param parameters
	 * 			Map of parameters
	 * @return formatted String
	 */
	public String getJsonInput(Map<String, String> parameters) {
		StringBuilder sbf = null;
		String result = ICommonConstants.START_CURLY_BRACE;
		
		if(parameters!=null){
			for(Map.Entry<String, String> entry : parameters.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				
				if(sbf != null) {
					sbf.append(ICommonConstants.COMMA);
				} else {
					sbf = new StringBuilder();
				}
				sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
				sbf.append(key);
				sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
				
				sbf.append(ICommonConstants.BLANK_SPACE_STRING);
				sbf.append(ICommonConstants.COLON);
				sbf.append(ICommonConstants.BLANK_SPACE_STRING);
				
				if (!StringUtils.isNumeric(value) && !(ICommonConstants.BOOLEAN_VALUE_TRUE.equalsIgnoreCase(value) || ICommonConstants.BOOLEAN_VALUE_FALSE.equalsIgnoreCase(value)) && !key.equalsIgnoreCase(LINESOFBUSINESS)) {
					sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
					sbf.append(value);
					sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
				}
				
				else {
					sbf.append(value);
				}
			}
			if(sbf != null) {
				sbf.append(ICommonConstants.END_CURLY_BRACE);
				result = result + sbf.toString();
			}
			//result= result
			LOGGER.debug("Prepared String input to execute the request : {}", result);
		}
		
		return result;
	}
	
	/****************************** Private variables & methods ********************************/
	
	private String requestType = MediaType.APPLICATION_FORM_URLENCODED;
	private String responseType = MediaType.APPLICATION_JSON;
	private int statusCode = 0;
	private static final Logger LOGGER = LoggerFactory.getLogger(RestHandler.class);
}
