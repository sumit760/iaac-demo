package com.comcast.cima.test.ui.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityHome;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UICommonSteps{
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	private Scenario testScenario;
	private String pageFlowId;
	private String sessionId;
	private String browserName;
	private String platform;
	private WebDriver browser;
	private XfinityHome xfinityHome;
	private Set<Cookie> cookies;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
		this.pageFlowId = null;
		this.sessionId = null;
		this.browser = null;
		this.platform = null;
	}
	
	
	@When("^customer opens Xfinity portal in browser$")
	public void customerOpensXfinityPortal() throws Exception {
		
		pageFlowId = PageNavigator.start("ValidateXfinityPortalLinksAndFeatures");
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
		
		sessionId = ((RemoteWebDriver)browser).getSessionId().toString();
		
		System.out.println("SauceOnDemandSessionID="+ sessionId +" job-name="+testScenario.getName());
		
		xfinityHome = new XfinityHome(browser);
		xfinityHome.setPageFlowId(pageFlowId);
		xfinityHome.setWindowHandle(this.browser.getWindowHandle());
		xfinityHome.get();
	}
	
	
	@And("^customer signs in from xfinity portal$")
	public void customerSignsInFromXfinityPortal() throws Exception {
		
		Object obj = xfinityHome.signIn();
		if (obj instanceof SignInToXfinity) {
			SignInToXfinity signIn = (SignInToXfinity) obj;
			signIn.signin(coreCucumberSteps.getUserId(), coreCucumberSteps.getUserPassword());
				
			logger.info("User is signed in from Xfinity portal");
		}
	}
	
	@And("^customer signs in from xfinity portal with remember me$")
	public void customerSignsInFromXfinityPortalWithRememberMe() throws Exception {
		
		Object obj = xfinityHome.signIn();
		if (obj instanceof SignInToXfinity) {
			SignInToXfinity signIn = (SignInToXfinity) obj;
			signIn.signInAndRemember(coreCucumberSteps.getUserId(), coreCucumberSteps.getUserPassword());
				
			logger.info("User is signed in from Xfinity portal with remember me");
		}
	}

	@When("^customer signs out from Xfinity portal$")
	public void customerSignsOutFromXfinityPortal() throws Exception {

		citfTestInitializer.switchWindow(browser,xfinityHome.getWindowHandle());
		xfinityHome.signOut();
		
		logger.info("User is signed out from Xfinity portal");
	}
	
	@Then("^sign in link is present in xfinity portal$")
	public void signInLinkInXfinityPortal() throws Exception {

		assertThat(
				"Sign In link expected in Xfinity portal",
				xfinityHome.isSignInLinkPresent(),
				is(true));
		logger.info("Signin link located in Xfinity portal");
	}
	
	@Then("^sign out link is present in xfinity portal$")
	public void signOutLinkPresentInXfinityPortal() throws Exception {
		
		assertThat(
				"Sign Out link expected in Xfinity portal",
				xfinityHome.isSignOutLinkPresent(),
				is(true));
		
		logger.info("Sign out link located in Xfinity portal");
	}
	
	@When("^customer closes the browser$")
	public void customerClosesBrowser() throws Exception {
		
		cookies = xfinityHome.getAllCookies();
		this.browser.close();
	}

	@And("^reopens Xfinity portal within a month$")
	public void customerReopensXfinityPortal() throws Exception {
		
		browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
                 BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
	             BrowserCapabilityDAO.Types.COMPUTER, 
	             getBrowserName(), 
	             false);
		
		xfinityHome = new XfinityHome(browser);
		xfinityHome.setPageFlowId(pageFlowId);
		xfinityHome.setWindowHandle(this.browser.getWindowHandle());
		xfinityHome.get();
		xfinityHome.setAllCookies(cookies);
		xfinityHome.refreshPage();
	}

	@When("^customer opens Xfinity portal in browser after a month$")
	public void customerOpensXfinityPortalAfterAMonth() throws Exception {
		
		xfinityHome.clearAllCookies();
		browser.close();
		
		browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), 
                 BrowserCapabilityDAO.Platforms.valueOf(getPlatform()), 
	             BrowserCapabilityDAO.Types.COMPUTER, 
	             getBrowserName(), 
	             false);

		xfinityHome = new XfinityHome(browser);
		xfinityHome.setPageFlowId(pageFlowId);
		xfinityHome.setWindowHandle(this.browser.getWindowHandle());
		xfinityHome.get();
	}

	
	@After
	public void tearDown() throws Exception {
		try{
			if (browser != null && citfTestInitializer.isRemoteBrowser(browser) && 
					((RemoteWebDriver) browser).getSessionId() != null) {
				sessionId = ((RemoteWebDriver) browser).getSessionId().toString();
				final String status = testScenario.getStatus().equals("passed")
						? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
								: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
				citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
			}
			if (browser != null) {
				browser.quit();
			}
		}catch(Exception e){
			logger.error("Error occured while performing clean up activities : ", e);
		}
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

	public String getSessionId() {
		return sessionId;
	}

	public String getPageFlowId() {
		return pageFlowId;
	}

	public XfinityHome getXfinityHome() {
		return xfinityHome;
	}
	
}
