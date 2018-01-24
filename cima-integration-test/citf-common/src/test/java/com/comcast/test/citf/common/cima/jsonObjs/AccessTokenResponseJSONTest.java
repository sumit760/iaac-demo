package com.comcast.test.citf.common.cima.jsonObjs;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import com.comcast.test.citf.common.cima.jsonObjs.AccessTokenResponseJSON;
import org.junit.Before;
import org.junit.Test;


public class AccessTokenResponseJSONTest {

	private AccessTokenResponseJSON objAccessTokenResponseJSON;
	private String sValid="VALID";
	private String sNull=null;
	private String sBlank="";


	@Before	
	public void setup() {
		objAccessTokenResponseJSON=new AccessTokenResponseJSON();
	}

	@Test
	public void testAccessToken() {

		objAccessTokenResponseJSON.setAccess_token(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getAccess_token());

		objAccessTokenResponseJSON.setAccess_token(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getAccess_token());

		objAccessTokenResponseJSON.setAccess_token(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getAccess_token());
	}

	@Test
	public void testTokenType() {

		objAccessTokenResponseJSON.setToken_type(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getToken_type());

		objAccessTokenResponseJSON.setToken_type(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getToken_type());

		objAccessTokenResponseJSON.setToken_type(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getToken_type());
	}

	@Test
	public void testRefreshToken() {

		objAccessTokenResponseJSON.setRefresh_token(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getRefresh_token());

		objAccessTokenResponseJSON.setRefresh_token(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getRefresh_token());

		objAccessTokenResponseJSON.setRefresh_token(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getRefresh_token());
	}


	@Test
	public void testExpiresIn() {

		objAccessTokenResponseJSON.setExpires_in(3599);
		assertEquals(3599,objAccessTokenResponseJSON.getExpires_in());

		objAccessTokenResponseJSON.setExpires_in(0);
		assertEquals(0,objAccessTokenResponseJSON.getExpires_in());
	}

	@Test
	public void testScope() {

		objAccessTokenResponseJSON.setScope(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getScope());

		objAccessTokenResponseJSON.setScope(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getScope());

		objAccessTokenResponseJSON.setScope(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getScope());
	}

	@Test
	public void testIdToken() {

		objAccessTokenResponseJSON.setId_token(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getId_token());

		objAccessTokenResponseJSON.setId_token(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getId_token());

		objAccessTokenResponseJSON.setId_token(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getId_token());
	}

	@Test
	public void testError() {

		objAccessTokenResponseJSON.setError(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getError());

		objAccessTokenResponseJSON.setError(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getError());

		objAccessTokenResponseJSON.setError(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getError());
	}

	@Test
	public void testErrorDescription() {

		objAccessTokenResponseJSON.setError_description(sValid);
		assertEquals(sValid,objAccessTokenResponseJSON.getError_description());

		objAccessTokenResponseJSON.setError_description(sNull);
		assertEquals(sNull,objAccessTokenResponseJSON.getError_description());

		objAccessTokenResponseJSON.setError_description(sBlank);
		assertEquals(sBlank,objAccessTokenResponseJSON.getError_description());
	}

	@Test
	public void testAdditionalProperties() {
		String arg0="123";
		Object arg1=new String();
		Map<String, Object> map1=new HashMap<String, Object>();
		map1.put(arg0, arg1);
		
		objAccessTokenResponseJSON.setAdditionalProperty(arg0,arg1);
		assertEquals(map1,objAccessTokenResponseJSON.getAdditionalProperties());
	}

}
