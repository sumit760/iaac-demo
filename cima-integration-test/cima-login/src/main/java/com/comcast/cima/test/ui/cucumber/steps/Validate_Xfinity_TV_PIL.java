package com.comcast.cima.test.ui.cucumber.steps;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.ws.rs.core.UriBuilder;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.dataProvider.XTVPartnerIntegrationLayerDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityOnCampus;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;

/**
 * @author Anonymous
 */
public class Validate_Xfinity_TV_PIL {

	@Autowired
	private CoreCucumberSteps ccs;
	
	@Autowired
	private XTVPartnerIntegrationLayerDataProvider xtvPilDataProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;

	private enum LOGIN_TYPE{comcast, university}
	private Scenario testScenario;

	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
	}

	@Then("^user attempts to go to the XTV PIL page as partner '(.*)' with redirect url '(.*)'$")
	public void attemptToGoToTheXfinityTvPartnerIntegrationLayerLoginPageWithPostLoginRedirectTo(
			String inLoginType, String inRedirectUrl) throws Exception {
		//
		final String loginUrl = getXtvPilAuthnSelectAccountUrl(inLoginType, inRedirectUrl, null);
		final boolean loginAlreadyAttempted = getCoreCucumberSteps().isLoginAlreadyAttempted();
		if (!loginAlreadyAttempted && LOGIN_TYPE.comcast.toString().equals(inLoginType)) {
			SignInToXfinity page = new SignInToXfinity(loginUrl, getCoreCucumberSteps().getBrowser());
			getCoreCucumberSteps().setPageObject(page.get());
		} else if (!loginAlreadyAttempted && LOGIN_TYPE.university.toString().equals(inLoginType)) {
			XfinityOnCampus page = new XfinityOnCampus(
					loginUrl, getCoreCucumberSteps().getBrowser());
			getCoreCucumberSteps().setPageObject(page.get());
		} else {
			getCoreCucumberSteps().getBrowser().navigate().to(loginUrl);
			getCoreCucumberSteps().setPageObject(null);
		}
	}
	@Then("^user attempts to go to the XTV PIL page as partner '(.*)' using forced authentication with redirect url '(.*)'$")
	public void attemptToGoToTheXfinityTvPartnerIntegrationLayerLoginPageUsingForcedAuthenticationWithPostLoginRedirectTo(
			String inLoginType, String inRedirectUrl) throws Exception {
		//
		final String loginUrl = getXtvPilAuthnSelectAccountUrl(inLoginType, inRedirectUrl, true);
		SignInToXfinity page = new SignInToXfinity(loginUrl, getCoreCucumberSteps().getBrowser());
		getCoreCucumberSteps().setPageObject(page.get());
	}
	@Then("^attempt to go to the XfinityTV Partner Integration Layer logout page with post-login redirect to '(.*)'$")
	public void attemptToGoToTheXfinityTvPartnerIntegrationLayerLoginPageWithPostLoginRedirectTo(
			String inRedirectUrl) throws Exception {
		//
		final String logoutUrl = getXtvPilAuthnLogoutUrl(inRedirectUrl);
		getCoreCucumberSteps().getBrowser().navigate().to(logoutUrl);
		getCoreCucumberSteps().setPageObject(null);
		getCoreCucumberSteps().setLoginAlreadyAttempted(false);
	}
	//
	//
	//
	private String getXtvPilAuthnSelectAccountUrl(String inLoginType, String inRedirectUrl, Boolean inForcedAuthn) throws Exception {
		UriBuilder outValue = UriBuilder.fromUri(getXtvPilAuthnSelectAccountUrl());
		try {
			outValue.queryParam("partnerId", LOGIN_TYPE.valueOf(inLoginType).toString());
		} catch (IllegalArgumentException iae) {
			throw new IllegalArgumentException(
					"The login type '" + inLoginType + "' is invalid, it should be one of " + Arrays.toString(LOGIN_TYPE.values()), iae);
		}
		try {
			outValue.queryParam("continue", (new URL(inRedirectUrl)).toString());
		} catch (MalformedURLException murle) {
			throw new IllegalArgumentException("Redirect URL '" + inRedirectUrl + "' is invalid", murle);
		}
		if (inForcedAuthn != null) {
			outValue.queryParam("forced_authn", inForcedAuthn.toString());
		}
		return outValue.build().toString();
	}
	private String getXtvPilAuthnSelectAccountUrl() throws Exception {
		return xtvPilDataProvider.getAuthSelectAccount();
	}
	private String getXtvPilAuthnLogoutUrl(String inRedirectUrl) throws Exception {
		UriBuilder outValue = UriBuilder.fromUri(getXtvPilAuthnLogoutUrl());
		try {
			outValue.queryParam("continue", (new URL(inRedirectUrl)).toString());
		} catch (MalformedURLException murle) {
			throw new IllegalArgumentException("Redirect URL '" + inRedirectUrl + "' is invalid", murle);
		}
		return outValue.build().toString();
	}
	private String getXtvPilAuthnLogoutUrl() throws Exception {
		return xtvPilDataProvider.getAuthLogOut();
	}
	//
	private CoreCucumberSteps getCoreCucumberSteps() {
		return this.ccs;
	}
	
	@After
	public void tearDown() {
		try{
			if (this.testScenario.getName().contains("XTV")) {
				WebDriver browser = getCoreCucumberSteps().getBrowser();
				if (citfTestInitializer.isRemoteBrowser(browser)) {
					final String status = testScenario.getStatus().equals("passed")
				                      ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
				                      : ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
					citfTestInitializer.updateSauceLabsTestStatus(((RemoteWebDriver)browser).getSessionId().toString(), status);
				}
				if (browser != null) {
					browser.quit();
				}
			}
		}catch(Exception e){
			LOGGER.error("Error occured while performing clean up activities : ", e);
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Validate_Xfinity_TV_PIL.class);
}
