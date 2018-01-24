package com.comcast.cima.test.ui.cucumber.steps;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.AWSManagementConsole;
import com.comcast.cima.test.ui.pageobject.OpenAWSConsole;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AWS {

	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pfId;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private Object pageObject;
	private String awsURL;
	private String username;
	private String password;
	private String sauceBrowser;
    private String saucePlatform;
	
	@Autowired
	private IdmCommonSteps commonSteps;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;

	@Autowired
	private ConfigurationDataProvider configDataProvider;
	
	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.pfId = null;
		this.sessionId = null;
		this.pageObject = null;
		this.awsURL = null;
		this.username = null;
		this.password = null;
		this.sauceBrowser = null;
		this.saucePlatform = null;
	}
	
	@After
	public void tearDown() {
		try {
				if (browser instanceof RemoteWebDriver) {
					final String status = testScenario.getStatus().equals("passed")
							? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
							: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
					citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
				}
				if (this.browser != null) {
					browser.quit();
				}
		}
		catch(Exception e) {
			logger.error("Error occured while performing clean up activities : ", e );
			fail("Not able to perform clean up activities due to : "+e.getMessage());
		}
	}
	
	
	@Given("^AWS URL and user credentials$")
	public void givenUserWithFBIdSameEmailId() {
		this.awsURL = "https://console.aws.amazon.com/ec2/v2/home?region=us-east-1#Instances:sort=instanceId";
		this.username = "Barun.Mandal@in.pwc.com";
		this.password = "Password@2409";

	}

	
	@When("^user opens AWS console$")
	public void userOpensAWSConsole() {
		
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), Platforms.valueOf(saucePlatform), Types.COMPUTER, sauceBrowser, false);
		} catch (IOException e) {
			logger.error("Error occurred while getting browser instance : ", e);
			fail("Not able to get browser instance due to : "+e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		OpenAWSConsole openConsole = new OpenAWSConsole(this.browser);
		openConsole.setPageFlowId(this.pfId);
		openConsole.setWindowHandle(this.browser.getWindowHandle());
		pageObject = openConsole.get();
		
		commonSteps.setPfId(this.pfId);
		commonSteps.setBrowser(this.browser);
		commonSteps.setSessionId(this.sessionId);
		
		assertThat(pageObject, instanceOf(OpenAWSConsole.class));
		
	}
	
	@And("^user provides username and password$")
	public void userProvidesCredentials() {
		
		OpenAWSConsole console = (OpenAWSConsole) pageObject;
		pageObject = console.signin(this.username, this.password);
	}
	
	
	@Then("^user should be able to login to AWS Console$")
	public void userShouldLogin() {
		
		assertThat(pageObject, instanceOf(AWSManagementConsole.class));
		
	}
	
	@When("^user clicks logout from AWS Console$")
	public void userClicksLogoutFromAWSConsole() {
		
		AWSManagementConsole ec2Console = (AWSManagementConsole) pageObject;
		ec2Console.signOut();
	}
	
	@Then("^user should be able to logout from AWS Console$")
	public void userShouldLogout() {
		
	}
	
}
