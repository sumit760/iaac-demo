package com.comcast.test.citf.common.cima.jsonObjs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuthIDTokenInfoTest {
	
	private OAuthIDTokenInfo OAuthIDTokenInfoObj;
	private String idTokenJson = "{"
								+ "\"iss\":\"login-qa4.comcast.net\","
							    + "\"sub\":\"298522361903062009Comcast.USR3JR\","
							    + "\"aud\":\"cimatest\","
								+ "\"azp\":\"cimatest1\","
								+ "\"iat\":1436211155,"
								+ "\"exp\":1436214708,"
								+ "\"auth_time\":1436210977,"
								+ "\"prev_auth_time\":1436210977,"
								+ "\"amr\":[\"urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport\"],"
								+ "\"user_type\":\"Residential\","
								+ "\"rm_state\":false"
								+ "}";
	
	private ObjectMapper objectMapper = null;
	
	@Before
	public  void setup() throws JsonParseException, JsonMappingException, IOException {
		
		objectMapper = new ObjectMapper();
		OAuthIDTokenInfoObj = objectMapper.readValue(idTokenJson, OAuthIDTokenInfo.class);
	}
	
	@Test
	public void testGetIssuer() {
		
		assertThat(OAuthIDTokenInfoObj.getIssuer(), is("login-qa4.comcast.net"));
		
	}
	
	
	@Test
	public void testGetSubjectIdentifier() {
		
		assertThat(OAuthIDTokenInfoObj.getSubjectIdentifier(), is("298522361903062009Comcast.USR3JR"));
		
	}
	
	
	@Test
	public void testGetAudience() {
		
		assertThat(OAuthIDTokenInfoObj.getAudience(), is("cimatest"));
		
	}
	
	
	@Test
	public void testGetAuthorizedParty() {
		
		assertThat(OAuthIDTokenInfoObj.getAuthorizedParty(), is("cimatest1"));
		
	}
	
	
	@Test
	public void testGetIssueInstant() {
		
		assertThat(OAuthIDTokenInfoObj.getIssueInstant(), notNullValue());
		
	}
	
	@Test
	public void testGetExpirationInstant() {
		
		assertThat(OAuthIDTokenInfoObj.getExpirationInstant(), notNullValue());
		
	}
	
	
	@Test
	public void testGetAuthnInstant() {
		
		assertThat(OAuthIDTokenInfoObj.getAuthnInstant(), notNullValue());
		
	}
	
	@Test
	public void testGetPrevAuthnInstant() {
		
		assertThat(OAuthIDTokenInfoObj.getPrevAuthnInstant(), notNullValue());
		
	}
	

}
