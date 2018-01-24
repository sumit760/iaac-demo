package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.AnyOf;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OAuth_RenewAccessToken_WebServerApp_Flow {

	private String exceptionMessage;
	
	@Autowired
	private OAuthCommonSteps oAuthCommonSteps;
	
	@Autowired
	private OAuthDataProvider oAuthDataProvider;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.exceptionMessage = null;
	}
	
	@When("^OAuth client requests for access token using invalid refresh token$")
	public void oauthClientRequestsForAccessTokenWithInvalidRefreshToken() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oAuthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_REFRESH_TOKEN)
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
															     oAuthCommonSteps.getClientSecret()))
				.set(REFRESH_TOKEN, ICimaCommonConstants.INVALID_REFRESH_TOKEN);

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}

	
	@When("^OAuth client requests for access token by passing refresh token with invalid grant type$")
	public void oauthClientRequestsForAccessTokenWithInvalidGrantType() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oAuthDataProvider.getAccessTokenUrl()),
				ICimaCommonConstants.INVALID_GRANT_TYPE)
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
															     oAuthCommonSteps.getClientSecret()))
				.set(REFRESH_TOKEN, oAuthCommonSteps
									  .getAccessTokenResponse()
									  .getRefreshToken());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}

	
	@Then("^OAuth client receives invalid refresh token response$")
	public void oauthClientReceivesInvalidRefreshTokenMessage() throws Exception {
		
		assertThat("Expected invalid grant message", 
					this.exceptionMessage, 
				    containsString("invalid_grant"));
		
		assertThat("Expected invalid refresh token message", 
				this.exceptionMessage, 
			    containsString("Invalid refresh token: "+ICimaCommonConstants.INVALID_REFRESH_TOKEN));
	}

	
	@Then("^OAuth client receives invalid grant type response$")
	public void oauthClientReceivesInvalidGrantTypeMessage() throws Exception {
		
		assertThat("Expected unsupported grant type message", 
				this.exceptionMessage, 
			    containsString("Unsupported grant type: "+ICimaCommonConstants.INVALID_GRANT_TYPE));
	}

	
	@When("^OAuth client requests for access token by passing refresh token with invalid clientId$")
	public void oauthClientRequestsForAccessTokenWithInvalidClientId() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oAuthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_REFRESH_TOKEN)
				.setClientAuthentication(new BasicAuthentication(ICimaCommonConstants.INVALID_CLIENT_ID, 
															     oAuthCommonSteps.getClientSecret()))
				.set(REFRESH_TOKEN, oAuthCommonSteps
									  .getAccessTokenResponse()
									  .getRefreshToken());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}

	
	@Then("^OAuth client receives unauthorized client response$")
	public void oauthClientReceivesInvalidClientIdMessage() throws Exception {
		
		assertThat("Expected unauthorized message", 
				this.exceptionMessage, 
			    containsString("unauthorized"));
		
		assertThat("Expected Client not found message", 
				this.exceptionMessage, 
				anyOf(containsString("No client with requested id: "+ICimaCommonConstants.INVALID_CLIENT_ID),  
			    	  containsString("Client not found: "+ICimaCommonConstants.INVALID_CLIENT_ID)));
	}

	
	@When("^OAuth client requests for access token by passing refresh token with invalid client secret$")
	public void oauthClientRequestsForAccessTokenWithInvalidClientSecret() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oAuthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_REFRESH_TOKEN)
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
															     ICimaCommonConstants.INVALID_CLIENT_SECRET))
				.set(REFRESH_TOKEN, oAuthCommonSteps
									  .getAccessTokenResponse()
									  .getRefreshToken());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}

	
	@Then("^OAuth client receives bad client credentials response$")
	public void oauthClientReceivesInvalidClientSecretMessage() throws Exception {
		
		assertThat("Expected unauthorized message", 
				this.exceptionMessage, 
			    containsString("unauthorized"));
		
		assertThat("Expected Bad credentials message", 
				this.exceptionMessage, 
			    containsString("Bad credentials"));
	}

	private static final String REFRESH_TOKEN = "refresh_token";
}
