package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;

import org.apache.commons.io.IOUtils;
import org.apache.wink.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError;
import com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse;
import com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError;
import com.comcast.test.citf.common.cima.satrXmlSuccess.AccessTokenResponse;
import com.comcast.test.citf.common.util.CodecUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.dataProvider.KeysDataProvider;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.api.client.util.DateTime;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RestCommonSteps {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String serviceName;
	private String appKey;
	private String signKey;
	private String loginEndPointURL;
	private String accessEndPointURL;
	private String userId;
	private String password;
	private String loginTokenResponse;
	private String accessTokenResponse;
	private String loginToken;
	private AuthnResponse authResponse;
	private AuthnResponseError authResponseError;
	private AccessTokenResponse accessResponse;
	private AccessTokenResponseError accessResponseError;


	@Autowired
	private CoreCucumberSteps coreCucumberSteps;

	@Autowired
	private EndPoinUrlProvider urlProvider;
	
	@Autowired
	private KeysDataProvider keyProvider;

	@Before
	public void setup(Scenario scenario) throws Exception {
		this.appKey = null;
		this.signKey = null;
		this.loginEndPointURL = null;
		this.accessEndPointURL = null;
		this.userId = null;
		this.password = null;
		this.serviceName=null;
	}



	@Given("^a REST service \"(.*?)\"$")
	public void aRESTservice(String service) throws Exception {
		this.serviceName = service;
		this.appKey = keyProvider.getAppKey();
		this.signKey = keyProvider.getSignKey();
		this.loginEndPointURL = urlProvider.getServiceEndPointUrl(EndPoinUrlProvider.ServiceEndpointPropKeys.LOGIN.name());
		this.accessEndPointURL = urlProvider.getServiceEndPointUrl(EndPoinUrlProvider.ServiceEndpointPropKeys.ACCESS.name());
	}

	@When("^rest service requests a login token$")
	public void restServiceRequestsLoginToken() throws Exception {

		this.userId = coreCucumberSteps.getUserId();
		this.password = coreCucumberSteps.getUserPassword();
		checksRequiredParamsIncludingUser();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
														.on(" ")
				                                        .join(this.appKey, this.password, this.userId), 
				                                        this.signKey, 
				                                        urlProvider.getCurrentEnvironment()); 
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put("u", this.userId);
		map.put("p", this.password);
		map.put(DSIG, dsig);
		
		String requestEntity = getStringInput(map);
		loginTokenResponse = new RestClient()
								.resource(loginEndPointURL)
								.post(requestEntity)
								.getEntity(String.class);

		logger.debug(LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE, this.loginTokenResponse);
	}

	@Then("^rest service receives a login token response$")
	public void restServiceReceivesValidResponse() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE,
				loginTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE,
				loginTokenResponse.contains("LoginToken"), 
				is(true));

		this.loginTokenResponse = this.loginTokenResponse.replace(ICimaCommonConstants.LOGIN_TOKEN_XML_NAMESPACE, "");
		
		authResponse = (AuthnResponse) JAXBContext
				                        .newInstance(AuthnResponse.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils.toInputStream(this.loginTokenResponse));
	}

	@And("^response contains a valid login token$")
	public void responseContainsValidLoginToken() throws Exception {
		
		assertThat(
				"Expected valid login token",
				authResponse.getLoginToken().getValue(), 
				notNullValue());
		
		assertThat(
				"Expected valid expires on value in login token",
				 new DateTime(authResponse.getLoginToken().getExpiresOn()).getValue()
				 > System.currentTimeMillis(), 
				is(true));
		
		String decodedLoginToken = CodecUtility.getBase64DecodedString(authResponse.getLoginToken().getValue(), 
				                                                       ICimaCommonConstants.ENCODING_UTF8);
		assertThat(
				"Expected userId within login token",
				decodedLoginToken.contains(coreCucumberSteps.getUserId()), 
				is(true));
	}

	
	@When("^rest service requests access token by passing login token$")
	public void restServiceRequestsAccessTokenByPassingLoginToken() throws Exception {
		checksRequiredParamsExcludingUser();

		loginToken = authResponse.getLoginToken().getValue();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
															 .on(" ")
															 .join(this.appKey, this.loginToken, this.serviceName), 
															 this.signKey, 
															 urlProvider.getCurrentEnvironment()); 

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put(LOGIN_TOKEN, URLEncoder.encode(loginToken, ICommonConstants.ENCODING_UTF8));
		map.put(SVC, this.serviceName);
		map.put(DSIG, dsig);
		
		String requestEntity = getStringInput(map);
		accessTokenResponse = new RestClient()
							  .resource(this.accessEndPointURL)
							  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
							  .accept(MediaType.APPLICATION_JSON)
		                      .post(requestEntity)
		                      .getEntity(String.class);

		logger.debug(LOG_MESSAGE_ACCESS_TOKEN_POST_RESPONSE, this.accessTokenResponse);

	}
	
	
	@Then("^rest service receives an access token response$")
	public void restServiceReceivesAccessTokenResponse() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_ACCESS_TOKEN_RESPONSE,
				accessTokenResponse, notNullValue());
		
		assertThat(
				"Expected a service token in the access token response"
				+ " Access Token received = " + accessTokenResponse,
				accessTokenResponse.contains("ServiceToken"), 
				is(true));
		
		accessResponse = (AccessTokenResponse) JAXBContext
				                        .newInstance(AccessTokenResponse.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils
				                        .toInputStream(removenameSpace(this.accessTokenResponse)));
	}
	
	
	@When("^rest service requests access token with invalid appkey passing the login token$")
	public void restServiceRequestsAccessTokenWithInvalidAppKey() throws Exception {
		checksRequiredParamsExcludingUser();

		loginToken = authResponse.getLoginToken().getValue();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
															 .on(" ")
															 .join(ICimaCommonConstants.INVALID_APP_KEY, 
		    											     this.loginToken, this.serviceName), 
															 this.signKey, 
															 urlProvider.getCurrentEnvironment()); 

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, ICimaCommonConstants.INVALID_APP_KEY);
		map.put(LOGIN_TOKEN, URLEncoder.encode(loginToken, ICommonConstants.ENCODING_UTF8));
		map.put(SVC, this.serviceName);
		map.put(DSIG, dsig);
				
		String requestEntity = getStringInput(map);
		accessTokenResponse = new RestClient()
							  .resource(this.accessEndPointURL)
							  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
							  .accept(MediaType.APPLICATION_JSON)
		                      .post(requestEntity)
		                      .getEntity(String.class);

		logger.debug(LOG_MESSAGE_ACCESS_TOKEN_POST_RESPONSE, this.accessTokenResponse);

	}

	
	@Then("^rest service receives a response with invalid application key message$")
	public void restServiceReceivesInvalidAppKeyResponse() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_ACCESS_TOKEN_RESPONSE,
				accessTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessTokenResponse.contains("Failure"), 
				is(true));
		
		accessResponseError = (AccessTokenResponseError) JAXBContext
				                        .newInstance(AccessTokenResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils
				                        .toInputStream(removenameSpace(this.accessTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessResponseError.getStatus().getMessage(), 
				containsString("Can't find application"));
	}


	@When("^rest service requests access token with invalid service passing the login token$")
	public void restServiceRequestsAccessTokenWithInvalidService() throws Exception {
		checksRequiredParamsExcludingUser();
		
		loginToken = authResponse.getLoginToken().getValue();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
															 .on(" ")
															 .join(this.appKey, this.loginToken, this.serviceName), 
															 this.signKey, 
															 urlProvider.getCurrentEnvironment()); 

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put(LOGIN_TOKEN, URLEncoder.encode(loginToken, ICommonConstants.ENCODING_UTF8));
		map.put(SVC, this.serviceName);
		map.put(DSIG, dsig);
				
		String requestEntity = getStringInput(map);
		accessTokenResponse = new RestClient()
							  .resource(this.accessEndPointURL)
							  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
							  .accept(MediaType.APPLICATION_JSON)
		                      .post(requestEntity)
		                      .getEntity(String.class);

		logger.debug(LOG_MESSAGE_ACCESS_TOKEN_POST_RESPONSE, this.accessTokenResponse);

	}

	
	@Then("^rest service receives a response with invalid service message$")
	public void restServiceReceivesInvalidServiceResponse() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_ACCESS_TOKEN_RESPONSE,
				accessTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessTokenResponse.contains("SvcNotValid"), 
				is(true));
		
		accessResponseError = (AccessTokenResponseError) JAXBContext
				                        .newInstance(AccessTokenResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils
				                        .toInputStream(removenameSpace(this.accessTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessResponseError.getStatus().getMessage(), 
				containsString("Invalid service requested"));
	}

	
	@When("^rest service requests access token by passing invalid login token$")
	public void restServiceRequestsAccessTokenWithInvalidLoginToken() throws Exception {
		checksRequiredParamsExcludingUser();
		
		String dsig =  CodecUtility.getDigitalSignature(Joiner
															 .on(" ")
															 .join(this.appKey, 
															   ICimaCommonConstants.INVALID_LOGIN_TOKEN, 
															   this.serviceName), 
															   this.signKey, 
															   urlProvider.getCurrentEnvironment()); 

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put(LOGIN_TOKEN, URLEncoder.encode(ICimaCommonConstants.INVALID_LOGIN_TOKEN, 
						                                 ICommonConstants.ENCODING_UTF8));
		map.put(SVC, this.serviceName);
		map.put(DSIG, dsig);
				
		String requestEntity = getStringInput(map);
		accessTokenResponse = new RestClient()
							  .resource(this.accessEndPointURL)
							  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
							  .accept(MediaType.APPLICATION_JSON)
		                      .post(requestEntity)
		                      .getEntity(String.class);

		logger.debug(LOG_MESSAGE_ACCESS_TOKEN_POST_RESPONSE, this.accessTokenResponse);

	}

	
	@Then("^rest service receives a response with invalid login token message$")
	public void restServiceReceivesInvalidLoginTokenResponse() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_ACCESS_TOKEN_RESPONSE,
				accessTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessTokenResponse.contains("UnknownFailure"), 
				is(true));
		
		accessResponseError = (AccessTokenResponseError) JAXBContext
				                        .newInstance(AccessTokenResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils
				                        .toInputStream(removenameSpace(this.accessTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				accessResponseError.getStatus().getMessage(), 
				containsString("Invalid token"));
	}

	
	@When("^rest service requests a login token by passing invalid appkey$")
	public void restServiceRequestsLoginTokenWithInvalidAppKey() throws Exception {

		this.userId = coreCucumberSteps.getUserId();
		this.password = coreCucumberSteps.getUserPassword();
		checksRequiredParamsIncludingUser();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
														.on(" ")
				                                        .join(ICimaCommonConstants.INVALID_APP_KEY, 
				                                        	  this.password, this.userId), 
				                                        this.signKey, 
				                                        urlProvider.getCurrentEnvironment()); 
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, ICimaCommonConstants.INVALID_APP_KEY);
		map.put("u", this.userId);
		map.put("p", this.password);
		map.put(DSIG, dsig);
		
		String requestEntity = getStringInput(map);
		loginTokenResponse = new RestClient()
								.resource(loginEndPointURL)
								.post(requestEntity)
								.getEntity(String.class);

		logger.debug(LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE, this.loginTokenResponse);
	}

	
	@Then("^rest service receives response with invalid application key$")
	public void restServiceReceivesLoginTokenResponseWithInvalidAppKey() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE,
				loginTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				loginTokenResponse.contains("KeyNotValid"), 
				is(true));
		
		authResponseError = (AuthnResponseError) JAXBContext
				                        .newInstance(AuthnResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils.toInputStream(
				                        removenameSpace(this.loginTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				authResponseError.getStatus().getMessage(), 
				containsString("Invalid application key"));
	}

	
	
	@When("^rest service requests a login token by passing invalid userId$")
	public void restServiceRequestsLoginTokenWithInvalidUserId() throws Exception {

		this.userId = coreCucumberSteps.getUserId();
		this.password = coreCucumberSteps.getUserPassword();
		checksRequiredParamsIncludingUser();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
														.on(" ")
				                                        .join(this.appKey, 
				                                        	  this.password, ICimaCommonConstants.INVALID_USERID), 
				                                        this.signKey, 
				                                        urlProvider.getCurrentEnvironment()); 
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put("u", ICimaCommonConstants.INVALID_USERID);
		map.put("p", this.password);
		map.put(DSIG, dsig);
		
		String requestEntity = getStringInput(map);
		loginTokenResponse = new RestClient()
								.resource(loginEndPointURL)
								.post(requestEntity)
								.getEntity(String.class);

		logger.debug(LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE, this.loginTokenResponse);
	}

	
	@Then("^rest service receives response with invalid user credentials$")
	public void restServiceReceivesLoginTokenResponseWithInvalidUser() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE,
				loginTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				loginTokenResponse.contains("Incorrect"), 
				is(true));
		
		authResponseError = (AuthnResponseError) JAXBContext
				                        .newInstance(AuthnResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils.toInputStream(
				                        removenameSpace(this.loginTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				authResponseError.getStatus().getMessage(), 
				containsString(""));
	}

	
	@When("^rest service requests a login token by passing invalid user password$")
	public void restServiceRequestsLoginTokenWithInvalidUserPassword() throws Exception {

		this.userId = coreCucumberSteps.getUserId();
		this.password = coreCucumberSteps.getUserPassword();
		checksRequiredParamsIncludingUser();

		String dsig =  CodecUtility.getDigitalSignature(Joiner
														.on(" ")
				                                        .join(this.appKey, 
				                                        	  ICimaCommonConstants.INVALID_USER_PASSWORD, 
				                                        	  this.userId), 
				                                        this.signKey, 
				                                        urlProvider.getCurrentEnvironment()); 
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put("u", this.userId);
		map.put("p", ICimaCommonConstants.INVALID_USER_PASSWORD);
		map.put(DSIG, dsig);
		
		String requestEntity = getStringInput(map);
		loginTokenResponse = new RestClient()
								.resource(loginEndPointURL)
								.post(requestEntity)
								.getEntity(String.class);

		logger.debug(LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE, this.loginTokenResponse);
	}

	
	@When("^rest service requests a login token by passing invalid digital signature$")
	public void restServiceRequestsLoginTokenWithInvalidDigitalSignature() throws Exception {

		this.userId = coreCucumberSteps.getUserId();
		this.password = coreCucumberSteps.getUserPassword();
		checksRequiredParamsIncludingUser();

		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(APPKEY, this.appKey);
		map.put("u", this.userId);
		map.put("p", this.password);
		map.put(DSIG, ICimaCommonConstants.INVALID_DIGITAL_SIGNATURE);
		
		String requestEntity = getStringInput(map);
		loginTokenResponse = new RestClient()
								.resource(loginEndPointURL)
								.post(requestEntity)
								.getEntity(String.class);

		logger.debug(LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE, this.loginTokenResponse);
	}

	
	@Then("^rest service receives response with invalid digital signature$")
	public void restServiceReceivesLoginTokenResponseWithInvalidDS() throws Exception {

		assertThat(
				ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE,
				loginTokenResponse, notNullValue());
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				loginTokenResponse.contains("SigNotValid"), 
				is(true));
		
		authResponseError = (AuthnResponseError) JAXBContext
				                        .newInstance(AuthnResponseError.class)
				                        .createUnmarshaller()
				                        .unmarshal(IOUtils.toInputStream(
				                        removenameSpace(this.loginTokenResponse)));
		
		assertThat(
				ASSERTION_MESSAGE_UNEXPECTED_ERROR,
				authResponseError.getStatus().getMessage(), 
				containsString("Invalid request signature"));
	}

	
	
	public String getLoginTokenResponse() {
		return loginTokenResponse;
	}

	public String getAccessTokenResponse() {
		return accessTokenResponse;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getAppKey() {
		return appKey;
	}

	public String getSignKey() {
		return signKey;
	}

	public String getLoginEndPointURL() {
		return loginEndPointURL;
	}
	
	public String getAccessEndPointURL() {
		return accessEndPointURL;
	}

	public String getLoginToken() {
		return loginToken;
	}
	
	public AccessTokenResponse getAccessResponse() {
		return accessResponse;
	}
	
	public AccessTokenResponseError getAccessResponseError() {
		return accessResponseError;
	}
	
	public AuthnResponse getAuthResponse() {
		return authResponse;
	}

	public AuthnResponseError getAuthResponseError() {
		return authResponseError;
	}


	private String getStringInput(Map<String, String> paramerters){
		StringBuilder sbf = null;
		String result = ICommonConstants.BLANK_STRING;
		
		if(paramerters!=null){
			for(Map.Entry<String, String> entry : paramerters.entrySet()){
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
			
			result = sbf.toString();
			logger.debug("Prepared String input to execute the request : {}", result);
		}
		
		return result;
	}
	
	private String removenameSpace(String token) {
		return token.replaceAll("cima:", "")
			     .replaceAll(":cima", "")
			     .replaceAll("xmlns=\"[a-z]+\\:[a-zA-Z]+\"", "")
				 .replaceAll("xmlns.*?\\d\\.\\d\"", "");
	}
	
	private void checksRequiredParamsIncludingUser(){
		if(this.appKey==null || this.signKey==null) {
			throw new IllegalStateException("App key/Sign key not found from data provider");
		}
		if(this.userId==null || this.password==null) {
			throw new IllegalStateException("userId/password not found from data provider");
		}
		if(this.loginEndPointURL==null) {
			throw new IllegalStateException("loginEndPointURL not found from data provider");
		}
	}
	
	private void checksRequiredParamsExcludingUser(){
		if(this.appKey==null || this.signKey==null) {
			throw new IllegalStateException("App key/Sign key not found from data provider");
		}
		if(this.serviceName==null) {
			throw new IllegalStateException("service name not found from data provider");
		}
		if(this.loginEndPointURL==null || this.accessEndPointURL==null) {
			throw new IllegalStateException("loginEndPointURL/accessEndPointURL not found from data provider");
		}
	}
	
	private static final String LOG_MESSAGE_LOGIN_TOKEN_POST_RESPONSE = "Login token POST response: {}";
	private static final String LOG_MESSAGE_ACCESS_TOKEN_POST_RESPONSE = "Access token POST response: {}";
	private static final String ASSERTION_MESSAGE_EXPECTED_VALID_LOGIN_TOKEN_RESPONSE = "Expected valid login token response";
	private static final String ASSERTION_MESSAGE_UNEXPECTED_ERROR = "Unexpected Error";
	private static final String ASSERTION_MESSAGE_EXPECTED_VALID_ACCESS_TOKEN_RESPONSE = "Expected valid access token response";
	private static final String APPKEY = "appkey";
	private static final String DSIG = "dsig";
	private static final String LOGIN_TOKEN = "login_token";
	private static final String SVC = "svc";
}
