package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.And;

public class Rest_AccessToken_Flow {
	
	@Autowired
	private RestCommonSteps restCommon;
	
	@And("^response contains a valid service token for smartzone service$")
	public void responseContainsValidServiceToken() throws Exception {
		
		com.comcast.test.citf.common.cima.satrXmlSuccess.AccessTokenResponse accessToken = restCommon.getAccessResponse();

		assertThat(
					"Expected valid auth token within service token",
					accessToken.getServiceToken().getAuthResponse().getAuthToken(), 
					notNullValue());
			
		assertThat(
					"Expected valid lifetime within service token",
					accessToken.getServiceToken().getAuthResponse().getLifetime(), 
					notNullValue());
			
		assertThat(
					"Expected valid status in access token",
					accessToken.getStatus().getCode(), 
					containsString("Success"));
		
	}

	
	@And("^response does not contain any valid access token$")
	public void responseDoesNotContainValidAccessToken() throws Exception {
		
		String accessTokenResponse = restCommon.getAccessTokenResponse();
		
		assertThat(
				"Expected valid status in access token",
				accessTokenResponse.contains("authToken"), 
				is(false));
	}
	
	@And("^response does not contain any valid Login token$")
	public void responseDoesNotContainValidLoginToken() throws Exception {
		
		String loginTokenResponse = restCommon.getLoginTokenResponse();
		
		assertThat(
				"Unexpected Error",
				loginTokenResponse.contains("LoginToken"), 
				is(false));
	}

}
