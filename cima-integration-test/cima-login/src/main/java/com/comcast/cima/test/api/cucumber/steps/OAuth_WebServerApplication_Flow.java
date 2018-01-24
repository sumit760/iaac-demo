package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OAuth_WebServerApplication_Flow {
	
	private List<NameValuePair> params;
	private Scenario testScenario;
	private String exceptionMessage;
	private TokenResponse accessTokenResponse;
	
	@Autowired
	private OAuthCommonSteps oAuthCommonSteps;
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private OAuthDataProvider oauthDataProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
		this.params = null;
		this.exceptionMessage = null;
	}


	@And("^state \"(.*?)\" is present in the authorization response$")
	public void stateIsPresentInAuthorizationResponse(String state) throws Exception {

		String actualState = null;
		
		params = oAuthCommonSteps.getParams();
		
		for (NameValuePair param : params) {
			if (param.getName().equals("state")) {
				actualState = param.getValue();
				break;
			}
		}
		
		assertThat("Expected state to be not null", 
				    actualState, notNullValue());
		
		assertThat(actualState, 
				   is(state));
		
	}
	
	
	@When("^OAuth client requests an access token with invalid client id by passing the authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidClientId() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(ICimaCommonConstants.INVALID_CLIENT_ID, 
															     oAuthCommonSteps.getClientSecret()))
				.set(CODE, oAuthCommonSteps.getAuthorizationCode())
				.set(REDIRECT_URI, oAuthCommonSteps.getRedirectUrl());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	
	@Then("^OAuth client receives client not found in the response$")
	public void oauthClientReceivesClientNotFoundMessage() throws Exception {
		
		assertThat("Expected unauthorized message", 
					this.exceptionMessage, 
				    containsString("unauthorized"));
		
		assertThat("Expected Client not found message", 
				this.exceptionMessage, 
				anyOf(containsString("No client with requested id: "+ICimaCommonConstants.INVALID_CLIENT_ID),  
			    	  containsString("Client not found: "+ICimaCommonConstants.INVALID_CLIENT_ID)));
	}

	
	@When("^OAuth client requests an access token with invalid client secret by passing the authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidClientSecret() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
															     ICimaCommonConstants.INVALID_CLIENT_SECRET))
				.set(CODE, oAuthCommonSteps.getAuthorizationCode())
				.set(REDIRECT_URI, oAuthCommonSteps.getRedirectUrl());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	
	@Then("^OAuth client receives bad client credentials in the response$")
	public void oauthClientReceivesBadClientCredentialsMessage() throws Exception {
		
		assertThat("Expected unauthorized message", 
				this.exceptionMessage, 
			    containsString("unauthorized"));
		
		assertThat("Expected Bad credentials message", 
				this.exceptionMessage, 
			    containsString("Bad credentials"));
	}

	
	@When("^OAuth client requests an access token with invalid scope by passing the authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidScope() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(Arrays.asList(ICimaCommonConstants.INVALID_SCOPE))
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
																 oAuthCommonSteps.getClientSecret()))
				.set(CODE, oAuthCommonSteps.getAuthorizationCode())
				.set(REDIRECT_URI, oAuthCommonSteps.getRedirectUrl());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	
	@Then("^OAuth client receives invalid scope in the response$")
	public void oauthClientReceivesInvalidScopeMessage() throws Exception {
		
		assertThat("Expected invalid_scope message", 
				this.exceptionMessage, 
			    containsString("Invalid scope: "+ICimaCommonConstants.INVALID_SCOPE));
	}

	
	@When("^OAuth client requests an access token with invalid grant type by passing the authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidGrantType() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICimaCommonConstants.INVALID_GRANT_TYPE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
																 oAuthCommonSteps.getClientSecret()))
				.set(CODE, oAuthCommonSteps.getAuthorizationCode())
				.set(REDIRECT_URI, oAuthCommonSteps.getRedirectUrl());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	
	@Then("^OAuth client receives unsupported grant type in the response$")
	public void oauthClientReceivesUnsupportedGrantTypeMessage() throws Exception {
		
		assertThat("Expected invalid_scope message", 
				this.exceptionMessage, 
				containsString("Unsupported grant type: "+ICimaCommonConstants.INVALID_GRANT_TYPE));
	}

	
	
	@When("^OAuth client requests an access token with invalid redirect URL by passing the authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidRedirectURL() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
																 oAuthCommonSteps.getClientSecret()))
				.set(CODE, oAuthCommonSteps.getAuthorizationCode())
				.set(REDIRECT_URI, ICimaCommonConstants.INVALID_REDIRECT_URL);

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	
	@Then("^OAuth client receives redirect url mismatch in the response$")
	public void oauthClientReceivesRedirectURLResponse() throws Exception {
		
		assertThat("Expected redirect_uri_mismatch message", 
				this.exceptionMessage, 
				containsString("redirect_uri_mismatch"));
	}

	
	@When("^OAuth client requests an access token with an invalid authorization code$")
	public void oauthClientRequestsForAccessTokenWithInvalidAuthCode() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
																 oAuthCommonSteps.getClientSecret()))
				.set(CODE, ICimaCommonConstants.INVALID_AUTH_CODE)
				.set(REDIRECT_URI, oAuthCommonSteps.getRedirectUrl());

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}
	
	@Then("^OAuth client receives invalid authorization code in the response$")
	public void oauthClientReceivesIncorrectAuthCodeResponse() throws Exception {
		
		assertThat("Expected Invalid authorization code error in the response", 
				   this.exceptionMessage, 
				   containsString("Invalid authorization code: "+ICimaCommonConstants.INVALID_AUTH_CODE));
	}

	
	
	@Then("^OAuth client receives an unrecognized scope message$")
	public void oauthClientReceivesUnrecognizedScopeMessage() throws Exception {
		
		String responseURL = coreCucumberSteps.getResponseUrl();
		params = URLEncodedUtils.parse(new URI(responseURL), ICommonConstants.ENCODING_UTF8);
		
		if (params == null || params.isEmpty()) {
			fail("Could not retrieve errors from the response URL");
		}
		
		for (NameValuePair param : params) {
			if (param.getName().equals("error")) {
				
				assertThat("Expected invalid_scope message", 
						param.getValue(), 
						containsString("invalid_scope"));
			} else if (param.getName().equals("error_description")) {
				assertThat("Expected Unrecognized scope message", 
						param.getValue(), 
						containsString("Unrecognized scope"));
			}
		}
	}

	
	@Then("^OAuth client receives an invalid client message$")
	public void oauthClientReceivesInvalidClientMessage() throws Exception {
		
		String pageSource = coreCucumberSteps.getPageSource();
		
		assertThat("Expected invalid_client message", 
				pageSource, 
				containsString("invalid_client"));
		
		assertThat("Expected Bad client credentials message", 
				pageSource, 
				containsString("Bad client credentials"));
	}

	
	@Then("^OAuth client receives unsupported response type message$")
	public void oauthClientReceivesUnsupportedResponseTypeMessage() throws Exception {
		
		String responseURL = coreCucumberSteps.getResponseUrl();
		params = URLEncodedUtils.parse(new URI(responseURL), ICommonConstants.ENCODING_UTF8);
		
		if (params == null || params.isEmpty()) {
			fail("Could not retrieve errors from the response URL");
		}
		
		for (NameValuePair param : params) {
			if (param.getName().equals("error")) {
				
				assertThat("Expected unsupported_response_type message", 
						param.getValue(), 
						containsString("unsupported_response_type"));
			} else if (param.getName().equals("error_description")) {
				assertThat("Expected Unsupported response types message", 
						param.getValue(), 
						containsString("Unsupported response types"));
			}
		}
	}
	
	@After
	public void tearDown() {
		try{
			WebDriver browser = oAuthCommonSteps.getBrowser();
			if (citfTestInitializer.isRemoteBrowser(browser)) {
				final String sessionId = ((RemoteWebDriver) browser).getSessionId().toString();
				final String status = testScenario.getStatus().equals("passed")
			                      ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
			                      : ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
				citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
			}
			if (browser != null) {
				browser.quit();
			}
		}catch(Exception e){
			LOGGER.error("Error occured while performing clean up activities : ", e);
		}
	}

	@Then("^OAuth client receives redirect URI mismatch message$")
	public void oauthClientReceivesRedirectURIMismatchMessage() throws Exception {
		
		String pageSource = coreCucumberSteps.getPageSource();
		
		assertThat("Expected redirect_uri_mismatch message", 
				pageSource, 
				containsString("redirect_uri_mismatch"));
	}
	
	protected TokenResponse getAccessTokenResponse() {
		return this.accessTokenResponse;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth_WebServerApplication_Flow.class);
	
	private static final String CODE = "code";
	private static final String REDIRECT_URI = "redirect_uri";
}