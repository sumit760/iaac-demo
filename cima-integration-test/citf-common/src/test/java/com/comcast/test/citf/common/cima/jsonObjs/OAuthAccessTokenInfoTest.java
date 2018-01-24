package com.comcast.test.citf.common.cima.jsonObjs;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class OAuthAccessTokenInfoTest {
	
	private OAuthAccessTokenInfo OAuthAccessTokenInfoObj;
	private String audience = "AUD";
	private String scopes = "https://login.comcast.net/login";
	private String userId = "mockUserId";
	private long expiresIn = 3599;
	
	@Before
	public  void setup() {
		
		OAuthAccessTokenInfoObj = new OAuthAccessTokenInfo(audience, scopes, userId, expiresIn);
	}
	
	
	@Test
	public void testGetAudience() {
		
		assertThat(OAuthAccessTokenInfoObj.getAudience(), is(audience));
	}
	
	@Test
	public void testGetScope() {
		
		assertThat(OAuthAccessTokenInfoObj.getScope(), is(scopes));
	}
	
	@Test
	public void testGetUserId() {
		
		assertThat(OAuthAccessTokenInfoObj.getUserId(), is(userId));
	}
	
	@Test
	public void testGetExpiresIn() {
		
		assertThat(OAuthAccessTokenInfoObj.getExpiresIn(), is(expiresIn));
	}
	

}
