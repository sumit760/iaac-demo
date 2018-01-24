package com.comcast.test.citf.common.http;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.comcast.test.citf.common.http.RestHandler.WriteRequestMethod;


@RunWith(MockitoJUnitRunner.class)
public class RestHandlerTest {
	
	private RestHandler handler;
	private RestClient mockClient;
	private Resource mockResource;
	private ClientResponse mockResponse;
	
	@Before
	public void init() {
		handler = Mockito.spy(new RestHandler());
		mockClient = Mockito.mock(RestClient.class,Mockito.RETURNS_DEEP_STUBS); 
		mockResource = Mockito.mock(Resource.class,Mockito.RETURNS_DEEP_STUBS);
		mockResponse = Mockito.mock(ClientResponse.class,Mockito.RETURNS_DEEP_STUBS);
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteReadRequest() {
		String response = "Mock Response";
		
		// Stub for executeReadRequest
		Mockito
		       .doReturn(mockClient)
		       .when(handler)
		       .getClient();
		
		Mockito
		       .when(mockClient.resource(Mockito.anyString()))
		       .thenReturn(mockResource);
		
		Mockito
		       .when(mockResource.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON).get(Mockito.any(Class.class)))
		       .thenReturn(response);
		
		handler.setRequestType(MediaType.APPLICATION_FORM_URLENCODED);
		handler.setResponseType(MediaType.APPLICATION_JSON);
		assertEquals(response,handler.executeReadRequest("mockURL"));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteWriteRequestMethodOne() {
		String postResponse = "Mock Post Response";
		
		// Stub for executeWriteRequest with four parameters
		Mockito
		       .doReturn(mockClient)
		       .when(handler)
		       .getClient();
		
		Mockito
		       .when(mockClient.resource(Mockito.anyString()))
		       .thenReturn(mockResource);
		
		Mockito
			   .when(mockResource.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON))
			   .thenReturn(mockResource);
		
		Mockito
		       .when(mockResource.post(Mockito.anyString()))
		       .thenReturn(mockResponse);
		
		Mockito
		       .when(mockResource.header(Mockito.anyString(), Mockito.anyString()))
		       .thenReturn(mockResource);
		
		Mockito
		       .when(mockResponse.getEntity(Mockito.any(Class.class)))
		       .thenReturn(postResponse);
		
		Mockito
		       .when(mockResponse.getStatusCode())
		       .thenReturn(Mockito.anyInt());
		
		handler.setRequestType(MediaType.APPLICATION_FORM_URLENCODED);
		handler.setResponseType(MediaType.APPLICATION_JSON);
		
		Map<String,String> parameters = new HashMap<String,String>();
		parameters.put("mockParamKey", "mockParamValue");
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("mockHeaderKey", "mockHeaderValue");
		
		assertEquals(postResponse,handler.executeWriteRequest("mockURL", parameters, headers, WriteRequestMethod.POST));
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testExecuteWriteRequestMethodTwo() {
		String postResponse = "Mock Write Response";
		
		// Stub for executeWriteRequest with three parameters
		Mockito
		       .doReturn(mockClient)
		       .when(handler)
		       .getClient();
		
		Mockito
		       .when(mockClient.resource(Mockito.anyString()))
		       .thenReturn(mockResource);
		
		Mockito
		       .when(mockResource.contentType(MediaType.APPLICATION_FORM_URLENCODED).accept(MediaType.APPLICATION_JSON))
		       .thenReturn(mockResource);
		
		Mockito
		       .when(mockResource.post(Mockito.anyString()))
		       .thenReturn(mockResponse);
		
		Mockito
		       .when(mockResponse.getEntity(Mockito.any(Class.class)))
		       .thenReturn(postResponse);
		
		Mockito
		       .when(mockResponse.getStatusCode())
		       .thenReturn(Mockito.anyInt());
		
		handler.setRequestType(MediaType.APPLICATION_FORM_URLENCODED);
		handler.setResponseType(MediaType.APPLICATION_JSON);
		
		assertEquals(postResponse,handler.executeWriteRequest("mockURL", "mockBody", WriteRequestMethod.POST));
		
	}
	
	
	@Test
	public void testgetStringInput() {
		String output = "mockParamKey=mockParamValue";
		
		Map<String,String> parameters = new HashMap<String,String>();
		parameters.put("mockParamKey", "mockParamValue");
		
		assertEquals(output,handler.getStringInput(parameters));
	}
	
	
	
	@Test
	public void testgetJsonInput() {
		String output = "{\"mockParamKey\" : \"mockParamValue\"}";
		
		Map<String,String> parameters = new HashMap<String,String>();
		parameters.put("mockParamKey", "mockParamValue");
		
		assertEquals(output,handler.getJsonInput(parameters));
	}
}
