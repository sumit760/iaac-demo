package com.comcast.test.citf.core.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.wink.client.RestClient;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.OAuthAccessTokenInfo;
import com.comcast.test.citf.common.cima.jsonObjs.OAuthIDTokenInfo;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.cima.satrXmlSuccess.AccessTokenResponse;
import com.comcast.test.citf.common.parser.JSONParserHelper;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.dataProvider.ClientDetailsProvider;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.comcast.test.citf.core.dataProvider.TveDataProvider;
import com.comcast.test.citf.core.helpers.OAuthHelper;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.OAuthRequestLink;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.auth.oauth2.RefreshTokenRequest;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.Iterables;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OAuthCommonSteps{
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pageFlowId;
	private WebDriver browser;
	private String browserName;
	private String platform;
	private AuthorizationCodeFlow oauthAuthorizationFlow;
	private List<String> requestedScopes;
	private List<NameValuePair> params;
	private String clientId;
	private String clientSecret;
	private String authorizationCode;
	private String redirectUrl;
	private String responseType;
	private Object pageObject;
	private String authTokenUrl;
	private TokenResponse accessTokenResponse;
	private DeviceActivationResponseJSON deviceActivationResponse;
	private String serviceTokenResponse;
	private AccessTokenResponse serviceToken;
	private String exceptionMessage;
	private Scenario testScenario;
	private String accessToken;
	private String overrideAccessToken;
	private Map<String, String> headerMap;
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private OAuthDataProvider oauthDataProvider;
	
	@Autowired
	private ClientDetailsProvider clientDetailsProvider;
	
	@Autowired
	private TveDataProvider tveDataProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private OAuthHelper oAuthHelper;
	
	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.pageFlowId = null;
		this.requestedScopes = null;
		this.clientId = null;
		this.redirectUrl = null;
		this.responseType = null;
		this.pageObject = null;
		this.authTokenUrl = null;
		this.accessToken=null;
		this.serviceTokenResponse = null;
		this.serviceToken = null;
		this.exceptionMessage = null;
		this.overrideAccessToken = null;
		this.headerMap = new HashMap<String, String>();
		oAuthHelper.setTokenInfoUrl(oauthDataProvider.getTokenInfoUrl());
	}
	
	
	@Given("^OAuth client with registered scope \"(.*?)\"$")
	public void oauthClientWithRegisteredScope(String scopes) {
		
		oauthClientWithRegisteredScopeAndHTTPRedirectUrl(scopes);
	}

	
	@And("^OAuth client with registered scope keys \"(.*?)\" and HTTP redirect url$")
	public void oauthClientWithRegisteredScopeKeysAndHTTPRedirectUrl(String scope) {
		try {
			getOAuthClientWithScopeKeys(scope);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get the OAuth client with scope: " + scope, e);
		}
	}
	
	
	@And("^OAuth client with registered scope \"(.*?)\" and HTTP redirect url$")
	public void oauthClientWithRegisteredScopeAndHTTPRedirectUrl(String scopes) {
		try {
			getOAuthClientWithScope(scopes);
		} catch (Exception e) {
			logger.debug("Retrieved client info: client_id={}, client_secret={}, scopes={}, redirect_url={}, response_type={}",
			         clientId, clientSecret, requestedScopes, redirectUrl, responseType);
			throw new IllegalStateException("Failed to get the OAuth client with scope: " + scopes, e);
		}
	}
	
	@When("^OAuth client requests authorization code with state \"(.*?)\" in web browser$")
	public void oauthClientRequestsForAuthorizationCodeWithState(String state) {

		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCodeWithState");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
					BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
					BrowserCapabilityDAO.Types.COMPUTER, 
					getBrowserName(), 
					false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				null,
				getClientId(),
				getOAuthAuthorizationUrl())
				.setScopes(getRegisteredScopes())
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setResponseTypes(Collections.singletonList(getResponseType()))
						.setState(state)
						.setRedirectUri(getRedirectUrl())
						.setScopes(getRegisteredScopes())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
		}
		
		logger.debug("Authorization Code request URL: Authorization_Code_Request_URL={}", url);
	}

	
	@Then("^OAuth client receives an authorization code in the authorization response$")
	public void getAuthorizationCodeInAuthorizationResponse()  {

		if (this.coreCucumberSteps != null && 
				this.coreCucumberSteps.getResponseUrl() != null) {
			this.authTokenUrl = this.coreCucumberSteps.getResponseUrl();
		}
		try{
			params = URLEncodedUtils.parse(new URI(getAuthTokenUrl()), ICommonConstants.ENCODING_UTF8);
		}catch(Exception e){
			logger.error("Error occurred while parsing response : ", e);
			fail("Not able to parse response due to: "+e.getMessage());
		}
		
		if (params == null || params.isEmpty()) {
			fail("Could not retrieve authorization code");
		}
		
		for (NameValuePair param : params) {
			if (param.getName().equals("code")) {
				authorizationCode = param.getValue();
				break;
			}
		}
		
		assertThat(authorizationCode, notNullValue());
		
		logger.debug("Retrieved authorization code: Authorization_Code={}", authorizationCode);
	}
	
	@When("^OAuth client requests for new access token by passing the refresh token$")
	public void oauthClientRequestsForAccessTokenWithRefreshToken() {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				ICommonConstants.GRANT_TYPE_REFRESH_TOKEN)
				.setClientAuthentication(new BasicAuthentication(getClientId(), getClientSecret()))
				.set("refresh_token", accessTokenResponse.getRefreshToken());

		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	}

	@Then("^OAuth client receives an access token response$")
	public void validAccessTokenResponse() {

		assertThat(
				"Expected valid token response",
				accessTokenResponse, notNullValue());
	}

	@And("^response contains a valid OAuth access token$")
	public void oAuthAccessTokenIsValid() {

		assertThat(accessTokenResponse.getAccessToken(), notNullValue());
		
		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(accessTokenResponse.getAccessToken());
		
		assertThat(
				"Access token should belong to the client that requested it",
				accessTokenInfo.getAudience(), is(this.clientId));
		
		assertThat(
				"Access token scopes should include all requested scopes",
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(Iterables.toArray(this.requestedScopes, String.class)));
		
		assertThat(
				"Access token expires in time should be > 0",
				accessTokenInfo.getExpiresIn() > 0,
		        is(true));
		
		
		logger.debug("Retrieved access token: Access_Token={}, Expires_In={}, Scope={}, Refresh_Token={}", 
					  accessTokenResponse.getAccessToken(), accessTokenResponse.getExpiresInSeconds(),
					  accessTokenResponse.getScope(),
					  accessTokenResponse.getRefreshToken());
		
	}

	
	@And("^response contains a valid OAuth Id token$")
	public void oAuthIdTokenIsValid() {

		Map<String,Object> idTokenMap = accessTokenResponse.getUnknownKeys();
		
		OAuthIDTokenInfo idTokenInfo = oAuthHelper.verifyIdToken((String)idTokenMap.get("id_token"));
		assertThat(
				"Unexpected id token audience",
				idTokenInfo.getAudience(),
				is(oauthAuthorizationFlow.getClientId()));
		assertThat(
				"Unexpected id token authorized party",
				idTokenInfo.getAuthorizedParty(),
		        is(oauthAuthorizationFlow.getClientId()));
		assertThat(
				"User id is required in id token",
				idTokenInfo.getSubjectIdentifier(),
		        notNullValue());
		assertThat(
				"Login identifier is required in id token",
				idTokenInfo.getLoginIdentifier().contains(coreCucumberSteps.getUserId()),
		        is(true));
		assertThat(
				"Tenant id is required in id token",
				idTokenInfo.getTenantIdentifier(),
		        notNullValue());

	}

	
	@And("^response contains a valid OAuth access token with refresh token$")
	public void oAuthAccessTokenIsValidWithRefreshToken() {

		assertThat(accessTokenResponse.getAccessToken(), notNullValue());
		
		assertThat(accessTokenResponse.getRefreshToken(), notNullValue());
		
		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(accessTokenResponse.getAccessToken());
		
		assertThat(
				"Access token should belong to the client that requested it",
				accessTokenInfo.getAudience(), is(this.clientId));
		
		assertThat(
				"Access token scopes should include all requested scopes",
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(Iterables.toArray(this.requestedScopes, String.class)));
		
		assertThat(
				"Access token expires in time should be > 0",
				accessTokenInfo.getExpiresIn() > 0,
		        is(true));
		
		
		logger.debug("Retrieved access token: Access_Token={}, Expires_In={}, Scope={}, Refresh_Token={}", 
					  accessTokenResponse.getAccessToken(), accessTokenResponse.getExpiresInSeconds(),
					  accessTokenResponse.getScope(),
					  accessTokenResponse.getRefreshToken());
		
	}


	@When("^OAuth client requests a device activation code$")
	public void oauthClientRequestsForDeviceActivationCode() {
		
		
		String deviceActivationJson = new RestClient()
		.resource(getDeviceActivationURL())
		.accept(MediaType.APPLICATION_JSON_TYPE)
		.queryParam("client_id", this.clientId)
		.post(String.class, null);
		
		deviceActivationResponse = (DeviceActivationResponseJSON) JSONParserHelper.parseJSON(deviceActivationJson, DeviceActivationResponseJSON.class);
		
		logger.debug("Retrieved device activation response: device_code={}, user_code={}, verification_uri={}, expires_in={}, interval={}", 
				deviceActivationResponse.getDevice_code(), deviceActivationResponse.getUser_code(),
				deviceActivationResponse.getVerification_uri(), deviceActivationResponse.getExpires_in(),
				deviceActivationResponse.getInterval());
	}
	
	@Then("^OAuth client receives a device activation response$")
	public void oauthClientReceivesDeviceActivationResponse() {
		
		assertThat(
				"Expected valid device activation response",
				deviceActivationResponse, notNullValue());
	}
	
	
	@And("^response contains device code user code and verification url$")
	public void validDeviceActivationResponse() {
		
		UrlValidator urlValidator = new UrlValidator();
		assertThat(
				"Expected valid device code",
				deviceActivationResponse.getDevice_code(), notNullValue());
		
		assertThat(
				"Expected valid user code",
				deviceActivationResponse.getUser_code(), notNullValue());
		
		assertThat(
				"Expected valid verification uri",
				urlValidator.isValid(deviceActivationResponse.getVerification_uri()),
				is(true));
	}
	
	
	@When("^OAuth client requests access token by passing device code$")
	public void oauthClientRequestsForAccessTokenByDeviceCode() {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				ICommonConstants.GRANT_TYPE_DEVICE_CODE)
				.setScopes(getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(getClientId(), getClientSecret()))
				.set("code", deviceActivationResponse.getDevice_code());

		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	}
	
	@When("^OAuth client requests access token in client side application flow$")
	public void oauthClientRequestsForAccessTokenInClientSideAppFlow() {
		
		pageFlowId = PageNavigator.start("FetchOAuthAccessTokenInClientSideApplicationFlow");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
				                     BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
						             BrowserCapabilityDAO.Types.COMPUTER, 
						             getBrowserName(), 
						             false);
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		String url =
				new BrowserClientRequestUrl(
						getOAuthAuthorizationUrl(),
						getClientId())
						.setResponseTypes(Collections.singletonList("token"))
						.setRedirectUri(getRedirectUrl())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
		}
	}

	@Then("^OAuth client is redirected to a response url$")
	public void oauthClientRedirectedToResponseURL() {
		
		assertThat("Expected a valid response URL", 
				   coreCucumberSteps.getResponseUrl(), 
				   notNullValue());
	}
	
	
	@Then("^response url contains a valid OAuth access token$")
	public void responseURLContainsValidOAuthAccessToken() {
		
		String accessToken = null;
		try{
			params = URLEncodedUtils.parse(new URI(coreCucumberSteps.getResponseUrl().replace("#", "?")), 
									   ICommonConstants.ENCODING_UTF8);
		}catch(Exception e){
			logger.error("Error occurred while pasring response : ", e);
			fail("Not able to parse response due to: "+e.getMessage());
		}
		
		if (params == null || params.isEmpty()) {
			fail("Could not retrieve access token code");
		}
		
		for (NameValuePair param : params) {
			if (param.getName().equals("access_token")) {
				accessToken = param.getValue();
				break;
			}
		}
		
		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(accessToken);
		
		assertThat(
				"Access token should belong to the client that requested it",
				accessTokenInfo.getAudience(), is(this.clientId));
		
		assertThat(
				"Access token scopes should include all requested scopes",
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(Iterables.toArray(this.requestedScopes, String.class)));
		
		assertThat(
				"Access token expires in time should be > 0",
				accessTokenInfo.getExpiresIn() > 0,
		        is(true));
		
	}
	
	
	@When("^OAuth client requests access token in client credential flow$")
	public void oauthClientRequestsForAccessTokenInClientCredentialFlow() {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS)
				.setScopes(getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(getClientId(), getClientSecret()))
				.set("redirect_uri", getRedirectUrl());

		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	}
	
	@And("^response does not contain any valid OAuth access token$")
	public void noOAuthAccessTokenInResponse() {
		
		assertThat("Expected no access token in the response", 
				this.exceptionMessage.contains("access_token"), 
				is(false));
	}
	
	@When("^OAuth client requests authorization code with invalid scope in web browser$")
	public void oauthClientRequestsForAuthorizationCodeWithInvalidScope() {

		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCodeWithInvalidScope");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
				                     BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
						             BrowserCapabilityDAO.Types.COMPUTER, 
						             getBrowserName(), 
						             false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				null,
				getClientId(),
				getOAuthAuthorizationUrl())
				.setScopes(getRegisteredScopes())
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setResponseTypes(Collections.singletonList(getResponseType()))
						.setRedirectUri(getRedirectUrl())
						.setScopes(getRegisteredScopes())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
			this.coreCucumberSteps.setBrowser(this.browser);
		}
		
		logger.debug("Authorization Code request URL: Authorization_Code_Request_URL={}", url);
	}

	
	@When("^OAuth client requests authorization code with invalid clientId in web browser$")
	public void oauthClientRequestsForAuthorizationCodeWithInvalidClientId() {

		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCodeWithInvalidClientId");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
					BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
					BrowserCapabilityDAO.Types.COMPUTER, 
					getBrowserName(), 
					false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				null,
				getClientId(),
				getOAuthAuthorizationUrl())
				.setScopes(getRegisteredScopes())
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						ICimaCommonConstants.INVALID_CLIENT_ID)
						.setResponseTypes(Collections.singletonList(getResponseType()))
						.setRedirectUri(getRedirectUrl())
						.setScopes(getRegisteredScopes())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
			this.coreCucumberSteps.setBrowser(this.browser);
		}
		
		logger.debug("Authorization Code request URL: Authorization_Code_Request_URL={}", url);
	}

	@And("^OAuth client with registered scope \"(.*?)\" and HTTP redirect url for TVE$")
	public void oauthClientWithRegisteredScopeAndHTTPRedirectUrlForTve(String scopes) {
		try {
			getOAuthClientWithScopeForTve();
		} catch (Exception e) {
			logger.debug("Retrieved client info: client_id={}, client_secret={}, scopes={}, redirect_url={}, response_type={}",
			         clientId, clientSecret, requestedScopes, redirectUrl, responseType);
			throw new IllegalStateException("Failed to get the OAuth client with scope: " + scopes, e);
		}
	}
	
	
	@When("^OAuth client requests authorization code with invalid response type in web browser$")
	public void oauthClientRequestsForAuthorizationCodeWithInvalidResponseType() {

		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCodeWithInvalidResponseType");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
					BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
					BrowserCapabilityDAO.Types.COMPUTER, 
					getBrowserName(), 
					false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				null,
				getClientId(),
				oauthDataProvider.getAuthorizationUrl())
				.setScopes(getRegisteredScopes())
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setResponseTypes(Collections.singletonList(ICimaCommonConstants.INVALID_RESPONSE_TYPE))
						.setRedirectUri(getRedirectUrl())
						.setScopes(getRegisteredScopes())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
			this.coreCucumberSteps.setBrowser(this.browser);
		}
		
		logger.debug(LOG_STATEMENT_AUTH_CODE_REQ_URL, url);
	}


	
	@When("^OAuth client requests authorization code with invalid redirect URL in web browser$")
	public void oauthClientRequestsForAuthorizationCodeWithInvalidRedirectURL() {

		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCodeWithInvalidRedirectURL");
		if (coreCucumberSteps != null && 
			coreCucumberSteps.getBrowserType() != null &&
			coreCucumberSteps.getBrowserPlatform() != null) {
			
			this.browserName = coreCucumberSteps.getBrowserType();
			this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
					BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
					BrowserCapabilityDAO.Types.COMPUTER, 
					getBrowserName(), 
					false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(getOAuthTokenUrl()),
				null,
				getClientId(),
				getOAuthAuthorizationUrl())
				.setScopes(getRegisteredScopes())
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setResponseTypes(Collections.singletonList(getResponseType()))
						.setRedirectUri(ICimaCommonConstants.INVALID_REDIRECT_URL)
						.setScopes(getRegisteredScopes())
						.build();
		
		pageObject = browserInstance.open(url);
		
		if (this.coreCucumberSteps != null) {
			this.coreCucumberSteps.setPageObject(pageObject);
			this.coreCucumberSteps.setBrowser(this.browser);
		}
		
		logger.debug("Authorization Code request URL: Authorization_Code_Request_URL={}", url);
	}
	
	
	
	@When("^OAuth client requests a service token for \"(.*?)\" by passing the access token$")
	public void oAuthClientRequestsServiceToken(String service) {
		
		String requestEntity = "svc=" + service;
		this.serviceTokenResponse = new RestClient()
						  .resource(coreCucumberSteps.getServiceAccessURL())
						  .header("Authorization", "Bearer " + getAccessTokenResponse().getAccessToken())
						  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
						  .post(requestEntity)
						  .getEntity(String.class);
		
		logger.debug("Service token POST response: {}", this.serviceTokenResponse);
	}
	
	
	@Then("^OAuth client receives an service token response$")
	public void oAuthClientReceivesServiceTokenResponse() {
		
		assertThat(
				"Expected valid service token response",
				this.serviceTokenResponse, notNullValue());
		
		assertThat(
				"Expected valid service token" +
				" Received servcice token :: " + this.serviceTokenResponse,
				this.serviceTokenResponse.contains("ServiceToken"), 
				is(true));
		
		try{
			this.serviceToken = (AccessTokenResponse) JAXBContext
					.newInstance(AccessTokenResponse.class)
					.createUnmarshaller()
					.unmarshal(IOUtils
							.toInputStream(removenameSpace(this.serviceTokenResponse)));
		}catch(JAXBException e){
			logger.error("Error occurred while parsing service token response : ", e);
			fail("Not able to parse service token response due to : "+e.getMessage());
		}
	}
	
	
	@And("^response contains a valid service token$")
	public void responseContainsValidServiceToken() {
		
		assertThat(
				"Expected valid service token response",
				this.serviceToken.getServiceToken().getAuthResponse().getAuthToken(), 
				notNullValue());
		
		assertThat(
				"Expected valid lifetime within service token",
				this.serviceToken.getServiceToken().getAuthResponse().getLifetime(), 
				notNullValue());
		
		assertThat(
				"Expected valid status in access token",
				this.serviceToken.getStatus().getCode(), 
				containsString("Success"));
		
	}
	
	
	@When("^OAuth client requests a service token for \"(.*?)\" by passing an expired access token$")
	public void oAuthClientRequestsServiceTokenWithExpiredAccessToken(String service) {
		
		String requestEntity = "svc=" + service;
		this.serviceTokenResponse = new RestClient()
						  .resource(coreCucumberSteps.getServiceAccessURL())
						  .header("Authorization", "Bearer " + ICimaCommonConstants.EXPIRED_ACCESS_TOKEN)
						  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
						  .post(requestEntity)
						  .getEntity(String.class);
		
		logger.debug("Service token POST response: {}", this.serviceTokenResponse);
	}
	
	@Then("^OAuth client receives an service token response with error$")
	public void oAuthClientReceivesServiceTokenResponseWithError() {
		
		assertThat(
				"Expected valid service token response",
				this.serviceTokenResponse, notNullValue());
		
		assertThat(
				"Unexpected error",
				this.serviceTokenResponse.contains("UnknownFailure"), 
				is(true));
		
		assertThat(
				"Unexpected error",
				this.serviceTokenResponse.contains("Missing login token param"), 
				is(true));
		
	}
	
	
	@And("^response does not contain a valid service token$")
	public void responseWihNoServiceToken() {
		
		assertThat(
				"Expected valid service token",
				this.serviceTokenResponse.contains("authToken"), 
				is(false));
	}

	
	
	@Then("^OAuth client not able to receive an authorization code in the authorization response$")
	public void notGetAuthorizationCodeInAuthorizationResponse() throws URISyntaxException {
		params = URLEncodedUtils.parse(new URI(this.authTokenUrl), ICommonConstants.ENCODING_UTF8);
		if (params == null || params.isEmpty()) {
			fail("Could not retrieve authorization code");
		}
		
		for (NameValuePair param : params) {
			if (param.getName().equals("code")) {
				authorizationCode = param.getValue();
				break;
			}
		}
		assertThat(authorizationCode, nullValue());
		logger.debug("Retrieved authorization code: Authorization_Code={}", this.authorizationCode);
	}

	@When("^OAuth client requests access token for scope \"(.*?)\" using client_credential flow$")
	public void oauthClientRequestsAccessTokenForScopesUsingClientCredentailFlow(String scope) {
		TokenRequest request = new TokenRequest(
	        new ApacheHttpTransport(),
	        new JacksonFactory(),
	        new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
	        ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS)
	        .setScopes(this.requestedScopes)
	        .setClientAuthentication(new BasicAuthentication(this.clientId, this.clientSecret))
	        .set("redirect_uri", this.redirectUrl);
	
		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	}
	
	@When("^OAuth client requests expired access token for scope \"(.*?)\" using client_credential flow$")
	public void oauthClientRequestsExpiredAccessTokenForScopesUsingClientCredentailFlow(String scope) {
		this.accessToken = ICimaCommonConstants.EXPIRED_ACCESS_TOKEN;
		TokenRequest request = new TokenRequest(
		        new ApacheHttpTransport(),
		        new JacksonFactory(),
		        new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
		        ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS)
		        .setScopes(this.requestedScopes)
		        .setClientAuthentication(new BasicAuthentication(this.clientId, this.clientSecret))
		        .set("redirect_uri", this.redirectUrl);
		
		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	   accessTokenResponse.setAccessToken(accessToken);
	}
	

	@When("^OAuth client requests an access token by passing the authorization code$")
	public void oauthClientRequestsForAccessToken() {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_AUTHORIZATION_CODE)
				.setScopes(getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(getClientId(), getClientSecret()))
				.set("code", authorizationCode)
				.set("redirect_uri", getRedirectUrl());

		try{
			accessTokenResponse = request.execute();
		}catch(Exception e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN+e.getMessage());
		}
	
	}
	
	@Then("^renew OAuth access token using refresh token$")
	public void getTveOauthAccessTokenUsingRefreshCode() {
		//Reference - https://developers.google.com/api-client-library/java/google-oauth-java-client/
		//  reference/1.20.0/com/google/api/client/auth/oauth2/RefreshTokenRequest
		TokenResponse response = null;
		try{
			response =
					new RefreshTokenRequest(
							new NetHttpTransport(),
							new JacksonFactory(),
							new GenericUrl(getOAuthTokenUrl()),
							accessTokenResponse.getRefreshToken())
					.setClientAuthentication(
							new BasicAuthentication(
									this.clientId, this.clientSecret)
							).execute();
		}catch(IOException e){
			logger.error("Error occurred while executing token request : ", e);
			fail("Not able to execute token request due to : "+e.getMessage());
		}
		this.accessTokenResponse = response;
	}
	
	@Then("^an expired TVE OAuth access token$")
	public void useExpiredTveOauthAccessToken() {
		this.overrideAccessToken = tveDataProvider.getOauthExpiredAccessToken();
	}
	
	@Then("^an invalid TVE OAuth access token$")
	public void useInvalidTveOauthAccessToken() {
		this.overrideAccessToken = tveDataProvider.getOauthInvalidAccessToken();
	}
	
	@Then("^OAuth access token in response is valid$")	
	public void oauthAccessTokenInResponseIsValid() {
		
		this.accessToken=accessTokenResponse.getAccessToken();
		assertThat(accessTokenResponse.getAccessToken(), notNullValue());
		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(accessTokenResponse.getAccessToken());

		assertThat(
				"Access token should belong to the client that requested it",
				accessTokenInfo.getAudience(), is(this.clientId));

		assertThat(
				"Access token scopes should include all requested scopes",
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(Iterables.toArray(this.requestedScopes, String.class)));

		assertThat(
				"Access token expires in time should be > 0",
				accessTokenInfo.getExpiresIn() > 0,
				is(true));


		logger.debug("Retrieved access token: Access_Token={}, Expires_In={}, Scope={}, Refresh_Token={}", 
				accessTokenResponse.getAccessToken(), accessTokenResponse.getExpiresInSeconds(),
				accessTokenResponse.getScope(),
				accessTokenResponse.getRefreshToken());

	}
	
	@When("^OAuth client requests authorization code in web browser$")
	public void oauthClientRequestsForAuthorizationCode() {
		platform = this.coreCucumberSteps.getBrowserPlatform();
		browserName = coreCucumberSteps.getBrowserType();
		pageFlowId = PageNavigator.start("FetchOAuthAuthorizationCode1");
		
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
					BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
					BrowserCapabilityDAO.Types.COMPUTER, 
					this.browserName, 
					false);
		}catch(IOException e){
			logger.error(LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER, e);
			fail(ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER+e.getMessage());
		}
		
		OAuthRequestLink browserInstance = new OAuthRequestLink(browser);
		browserInstance.setPageFlowId(pageFlowId);

		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				null,
				this.clientId,
				oauthDataProvider.getAuthorizationUrl())
				.setScopes(this.requestedScopes)
				.build();
		
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setResponseTypes(Collections.singletonList(this.responseType))
						.setRedirectUri(this.redirectUrl)
						.setScopes(this.requestedScopes)
						.build();
		
		pageObject = browserInstance.open(url);
		this.coreCucumberSteps.setPageObject(pageObject);
		
		logger.debug(LOG_STATEMENT_AUTH_CODE_REQ_URL, url);
	}
	

	@When("^OAuth access token is set in a HTTP request header$")
	public void oauthAccessTokenIsSetInTheRequestHeader() {
		headerMap.put("Authorization", "Bearer " + accessTokenResponse.getAccessToken());
	}
	
	@Then("^OAuth client receives successful response$")
	public void oauthClientReceivesSuccessfulResponse() {
		assertThat(
				"Expected valid token response",
				this.accessTokenResponse, notNullValue());
	}

	private void getOAuthClientWithScopeKeys(String scope) {
		requestedScopes = oAuthHelper.parseScopeKeysToString(scope);
		
		ClientDetailsProvider.ClientDetails client = clientDetailsProvider.getClientDetails(ClientDetailsProvider.ClientType.LOGIN);
		if(client == null){
			Assert.fail("Not able to get client details");
		}
		
		clientId = client.getClientId();
		clientSecret = client.getClientSecret();
		
		redirectUrl = oauthDataProvider.getRequestTokenRedirectUrl();
		responseType = ICommonConstants.RESPONSE_TYPE_CODE;
	}
	
	private void getOAuthClientWithScope(String scopes) {
		scopes = scopes.replaceAll("'", "");
		requestedScopes = oAuthHelper.parseScopeString(scopes);
		
		ClientDetailsProvider.ClientDetails client = clientDetailsProvider.getClientDetails(ClientDetailsProvider.ClientType.LOGIN);
		if(client == null){
			Assert.fail("Not able to get client details");
		}
		
		clientId = client.getClientId();
		clientSecret =client.getClientSecret();
		
		redirectUrl = oauthDataProvider.getRequestTokenRedirectUrl();
		responseType = ICommonConstants.RESPONSE_TYPE_CODE;
	}
	
	private String getDeviceActivationURL() {
		return oauthDataProvider.getDeviceActivationRequestUrl();
	}

	private void getOAuthClientWithScopeForTve() {
		requestedScopes = oAuthHelper.parseScopeString(tveDataProvider.getOauthRequestedScopes());
		clientId = tveDataProvider.getOauthClientId();
		clientSecret = tveDataProvider.getOauthClientSecret();
		redirectUrl = tveDataProvider.getOauthRedirectUrl();
		responseType = ICommonConstants.RESPONSE_TYPE_CODE;
	}
	
	public String getOAuthTokenUrl() {
		return oauthDataProvider.getAccessTokenUrl();
	}
	
	protected String getConfigString(String cacheKey) {
		return oauthDataProvider.getCurrentEnvironment();
	}
	
	public String getOAuthAuthorizationUrl() {
		return oauthDataProvider.getAuthorizationUrl();
	}
	
	public String getOAuthOobUrl() {
		return oauthDataProvider.getOobUrl();
	}

	public String getOAuthTokenInfoUrl() {
		return oauthDataProvider.getTokenInfoUrl();
	}
	
	@After
	public void tearDown() {
		try{
			if (browser != null && citfTestInitializer.isRemoteBrowser(browser) && 
					((RemoteWebDriver) browser).getSessionId() != null) {
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
			logger.error("Error occurred while performing clean up activities : ", e);
		}
	}
	
	//Getter Methods
	public Object getPageObject() {
		return this.pageObject;
	}
	
	public WebDriver getBrowser() {
		return this.browser;
	}
	
	public String getPlatform() {
		return this.platform!=null? this.platform : coreCucumberSteps.getBrowserPlatform();
	}
	
	public String getBrowserName() {
		return this.browserName;
	}

	public String getClientId() {
		return this.clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public AuthorizationCodeFlow getAuthorizationCodeFlow() {
		return this.oauthAuthorizationFlow;
	}
	
	public String getAuthTokenUrl() {
		return this.authTokenUrl;
	}
	
	public List<String> getRegisteredScopes() {
		return this.requestedScopes;
	}
	
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	public String getResponseType() {
		return this.responseType;
	}

	public String getPageFlowId() {
		return this.pageFlowId;
	}

	public List<NameValuePair> getParams() {
		return params;
	}


	public String getOverrideAccessToken() {
		return this.overrideAccessToken;
	}

	public TokenResponse getAccessTokenResponse() {
		return accessTokenResponse;
	}
	
	public DeviceActivationResponseJSON getDeviceActivationResponse() {
		return this.deviceActivationResponse;
	}

	
	
	public String getAuthorizationCode() {
		return this.authorizationCode;
	}


	//Setter methods
	
	public void setAccessTokenResponse(TokenResponse accessTokenResponse) {
		this.accessTokenResponse = accessTokenResponse;
	}
	
	public void setAuthTokenUrl(String authTokenUrl) {
		this.authTokenUrl = authTokenUrl;
	}
	
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public void setRegisteredScopes(List<String> requestedScopes) {
		this.requestedScopes = requestedScopes;
	}

	public List<String> getRequestedScopes() {
		return requestedScopes;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public void setCoreCucumberSteps(CoreCucumberSteps coreCucumberSteps) {
		this.coreCucumberSteps = coreCucumberSteps;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}
	
	//Message constants
	private static final String LOG_STATEMENT_AUTH_CODE_REQ_URL = "Authorization Code request URL: Authorization_Code_Request_URL={}";
	
	private String removenameSpace(String token) {
		token = token.replaceAll("cima:", "")
			     .replaceAll(":cima", "")
			     .replaceAll("xmlns=\"[a-z]+\\:[a-zA-Z]+\"", "")
				 .replaceAll("xmlns.*?\\d\\.\\d\"", "");
		return token;
	}
	
	private static final String LOG_MESSAGE_ERR_OCCURRED_IN_TOKEN_REQUEST = "Error occurred while requesting token : ";
	private static final String LOG_MESSAGE_ERR_OCCURRED_WHILE_FETCHING_BROWSER = "Error occurred while getting browser instance : ";
	private static final String ASSERT_MESSAGE_UNABLE_TO_GET_BROWSER = "Not able to get browser instance due to : ";
	private static final String ASSERT_MESSAGE_UNABLE_TO_REQUEST_TOKEN = "Not able to request token due to: ";
}
