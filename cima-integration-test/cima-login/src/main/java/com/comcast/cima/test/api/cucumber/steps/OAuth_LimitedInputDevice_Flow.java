package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.MediaType;

import org.apache.wink.client.RestClient;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivation;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationConfirmation;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationSuccess;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class OAuth_LimitedInputDevice_Flow {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pageFlowId;
	private String browserName;
	private String platform;
	private Object pageObj;
	private String deviceActivationResponse;
	private WebDriver browser;
	private String errorString;
	private String exceptionMessage;
	private Scenario testScenario;
	private XfinityDeviceActivation activationPage;
	
	@Autowired
	private OAuthCommonSteps oAuthCommonSteps;
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private OAuthDataProvider oAuthDataProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
		this.browserName = null;
		this.platform = null;
		this.deviceActivationResponse = null;
		this.browser = null;
		this.errorString = null;
		this.exceptionMessage = null;
		this.pageObj = null;
	}
	
	@When("^user opens the verification url$")
	public void userOpenVerificationURL() throws Exception {
		
		pageFlowId = PageNavigator.start("Activate Device with user code");
		if (coreCucumberSteps != null && 
				coreCucumberSteps.getBrowserType() != null &&
				coreCucumberSteps.getBrowserPlatform() != null) {
				
				this.browserName = coreCucumberSteps.getBrowserType();
				this.platform = coreCucumberSteps.getBrowserPlatform();
		}
		
		browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
                 BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
	             BrowserCapabilityDAO.Types.COMPUTER, 
	             getBrowserName(), 
	             false);

		activationPage = new XfinityDeviceActivation(browser, 
													 oAuthCommonSteps
													 .getDeviceActivationResponse()
													 .getVerification_uri());
		
		activationPage.setPageFlowId(this.pageFlowId);
		activationPage.get();
	}
	
	
	@And("^provides the user code$")
	public void userProvidesUserCode() throws Exception {
		
		this.pageObj = activationPage.activateDevice(oAuthCommonSteps
				                                   .getDeviceActivationResponse()
				                                   .getUser_code());
		coreCucumberSteps.setPageObject(this.pageObj);
	}

	
	@And("^provides an invalid user code$")
	public void userProvidesInvalidUserCode() throws Exception {
		
		this.errorString = (String) activationPage.activateDevice(ICimaCommonConstants.INVALID_USER_CODE);
	}

	
	@And("^permission is granted by user$")
	public void userGrantsPermission() throws Exception {
		
		this.pageObj = coreCucumberSteps.getPageObject();
		
		if (this.pageObj instanceof XfinityDeviceActivationConfirmation) {
			XfinityDeviceActivationConfirmation confirmation = (XfinityDeviceActivationConfirmation)this.pageObj;
			    this.pageObj = confirmation.goToApp();
		}
	}
	
	
	@Then("^user receives the device activation success message$")
	public void userReceivesDeviceActivationSuccessMessage() throws Exception {
		
		assertThat(
				"Problem in activating device",
				this.pageObj, instanceOf(XfinityDeviceActivationSuccess.class));
		
		XfinityDeviceActivationSuccess deviceActivationSuccess = (XfinityDeviceActivationSuccess) this.pageObj;
		
		assertThat(
				"Expected the device activation congratulation message",
				deviceActivationSuccess.isCongratulationMessagePresent(),
				is(true));
		
		assertThat(
				"Expected the device activation confirmation message",
				deviceActivationSuccess.isDeviceActivationSuccessMessagePresent(),
				is(true));
	}
	
	@Then("^user receives unrecognized device code message$")
	public void userReceivesUnrecognizedDeviceCodeMessage() throws Exception {
		
		assertThat(
				"Expected error - Unrecognized device code",
				this.errorString, is("Unrecognized device code"));
	}
	
	
	@When("^OAuth client requests a device activation code using invalid client id$")
	public void oauthClientRequestsForDeviceActivationCodeWithInvalidClientId() throws Exception {
		
		deviceActivationResponse = new RestClient()
										      .resource(getDeviceActivationURL())
										      .accept(MediaType.APPLICATION_JSON_TYPE)
										      .queryParam("client_id", ICimaCommonConstants.INVALID_CLIENT_ID)
										      .post(String.class, null);
		
		logger.debug("Retrieved device activation response with invalid clientId: {}", 
				deviceActivationResponse);
		
	}

	
	@Then("^OAuth client receives an error message$")
	public void oauthClientDeviceActivationCodeRequestWithInvalidClientIdError() throws Exception {
		
		assertThat("Expected error message when oauth client requests device activation code "
				+ "with invalid clientId", 
				deviceActivationResponse, 
				containsString("Error 500 - Internal Error"));
	}

	
	@When("^OAuth client requests access token by passing invalid device code$")
	public void oauthClientRequestsForAccessTokenByInvalidDeviceCode() throws Exception {
		
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oAuthDataProvider.getAccessTokenUrl()),
				ICommonConstants.GRANT_TYPE_DEVICE_CODE)
				.setScopes(oAuthCommonSteps.getRegisteredScopes())
				.setClientAuthentication(new BasicAuthentication(oAuthCommonSteps.getClientId(), 
						                                         oAuthCommonSteps.getClientSecret()))
				.set("code", ICimaCommonConstants.INVALID_DEVICE_CODE);

		try {
			request.execute();
		}  catch (TokenResponseException t) {
			this.exceptionMessage = t.getMessage();
			oAuthCommonSteps.setExceptionMessage(this.exceptionMessage);
		}
	}

	
	@Then("^OAuth client receives invalid authorization code response$")
	public void oauthClientReceivesInvalidAuthorizationCodeMessage() throws Exception {
		
		assertThat("Expected a invalid grant message", 
					this.exceptionMessage, 
				    containsString("invalid_grant"));
		
		assertThat("Expected a invalid authorization code message", 
				this.exceptionMessage, 
			    containsString("Invalid authorization code: "+ICimaCommonConstants.INVALID_DEVICE_CODE));
	}
	
	
	public WebDriver getBrowser() {
		return this.browser;
	}
	
	public String getPlatform() {
		return this.platform;
	}
	
	public String getBrowserName() {
		return this.browserName;
	}
	
	private String getDeviceActivationURL() throws Exception {
		return oAuthDataProvider.getDeviceActivationRequestUrl();
	}
}
