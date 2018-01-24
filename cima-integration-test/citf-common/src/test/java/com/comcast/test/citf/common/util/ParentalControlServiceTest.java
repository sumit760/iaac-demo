package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LDAPInstruction;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.service.ParentalControl;
import com.comcast.test.citf.common.service.ParentalControlService;



public class ParentalControlServiceTest {
 
	private ParentalControlService parentalControlService;
	private LDAPInterface mockLdapService;
	private LDAPCustomer mockCustomer;
	private HttpGet mockHttpGet;
	private HttpClient mockHttpClient;
	private HttpResponse mockHttpResponse;

	private final String uid = "mockuid";
	String username = "1234";
	String password = "1234";
	private final String mockUrl = "https://mockurl/";
	private final String mockRes = "{"
				  + "\"parentalPin\":\"1234\","
				  + "\"protectedRatings\":[\"TV-G\", \"TV-Y\"],"
				  + "\"protectedChannels\":[\"NBC\", \"FOX\"],"
				  + "\"protectedNetworks\":[\"ESPN\"]"
				  + "}";
 

	@Before
	public void setup()
	{
		parentalControlService = Mockito.spy(new ParentalControlService());
		mockLdapService = Mockito.mock(LDAPInterface.class, Mockito.RETURNS_DEEP_STUBS);
		mockCustomer = Mockito.mock(LDAPCustomer.class, Mockito.RETURNS_DEEP_STUBS);
		mockHttpGet = Mockito.mock(HttpGet.class, Mockito.RETURNS_DEEP_STUBS);
		mockHttpClient = Mockito.mock(HttpClient.class, Mockito.RETURNS_DEEP_STUBS);
		mockHttpResponse = Mockito.mock(HttpResponse.class, Mockito.RETURNS_DEEP_STUBS);
	}
 
 
 
	@Test
	public void testGetParentalControlDetails(){  
		LDAPCustomer customer = new LDAPCustomer();
		customer.setCstAuthGuid("1234.AuthGuid");
		BufferedReader rd = new BufferedReader(new StringReader(mockRes));
		
		//Stub
		Mockito
			   .doReturn(mockLdapService)
			   .when(parentalControlService)
			   .getLdapService();
		
		Mockito
		       .when(mockLdapService.getCustomerData(Mockito.any(LDAPInstruction.class)))
		       .thenReturn(customer);
		
		Mockito
	       .when(mockCustomer.getCstAuthGuid())
	       .thenReturn("1234.AuthGuid");
		
		Mockito
		       .doReturn(mockHttpClient)
		       .when(parentalControlService)
		       .getHttpClient();
		
		Mockito
		       .doReturn(mockHttpGet)
		       .when(parentalControlService)
		       .getHttpGetRequest(Mockito.anyString());
		
		try{
			Mockito
			.doReturn(mockHttpResponse)
			.when(mockHttpClient)
			.execute(Mockito.any(HttpGet.class));

			Mockito
			.doReturn(rd)
			.when(parentalControlService)
			.getBufferedReader(Mockito.any(HttpResponse.class));
		
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
		
		ParentalControl pc = parentalControlService.getParentalControlDetails(uid, mockUrl);
		
		assertThat(pc.getParentalPin(), 
				  is("1234"));
		
		assertThat(pc.getProtectedChannels(), 
				  hasItems("NBC","FOX"));
		
		assertThat(pc.getProtectedRatings(), 
				  hasItems("TV-G","TV-Y"));
		
		assertThat(pc.getProtectedNetworks(), 
				  hasItems("ESPN"));

	} 
	
	
}