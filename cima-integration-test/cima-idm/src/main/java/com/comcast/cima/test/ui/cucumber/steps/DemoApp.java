package com.comcast.cima.test.ui.cucumber.steps;

import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import com.comcast.cima.test.ui.pageobject.Welcome;
import com.comcast.cima.test.ui.pageobject.openDemoApp;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class DemoApp {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pfId;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private Object pageObject;
	private String appURL;
	private String username;
	private String password;
	private String sauceBrowser;
    private String saucePlatform;
    //private final String appURLFilePath = "C:\\work\\DevOps\\URLPath\\appPath.properties";
    private final String appURLFilePath = "/var/tmp/web.properties";
	
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
		this.appURL = null;
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
	
	
	@Given("^Demo App URL and user credentials$")
	public void givenAppURLAndUserCredentials() {
		try {
			FileReader reader=new FileReader(appURLFilePath);  
			Properties p=new Properties();  
		    p.load(reader); 
		    //this.appURL = p.getProperty("path");
		    this.appURL = p.getProperty("URL");
		    this.username = "admin";
		    this.password = "admin";
		} catch (Exception e) {
			
		}
		
		//this.appURL = "http://139.59.83.201:9080/Presentation-0.0.1-SNAPSHOT/";
		

	}

	
	@When("^user opens the app$")
	public void userOpensApp() {
		
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
		openDemoApp openConsole = new openDemoApp(this.browser);
		openConsole.setPageFlowId(this.pfId);
		openConsole.setWindowHandle(this.browser.getWindowHandle());
		openConsole.setAppURL(appURL);
		pageObject = openConsole.get();
		
		commonSteps.setPfId(this.pfId);
		commonSteps.setBrowser(this.browser);
		commonSteps.setSessionId(this.sessionId);
		
	}
	
	
	@And("^user provides username password and submits$")
	public void userProvidesCredentials() {
		if(!(pageObject instanceof openDemoApp)){
        	logAndFail(openDemoApp.class.getSimpleName());
        }
		
		openDemoApp signInPage = (openDemoApp)pageObject;
		pageObject = signInPage.signin(this.username, this.password);
		
	}
	
	
	@Then("^user should be able to able to login to the demo app$")
	public void userShouldLogin() {
		
		if(!(pageObject instanceof Welcome)){
        	logAndFail(Welcome.class.getSimpleName());
        }
		
		Welcome welcomePage = (Welcome)pageObject;
		Assert.assertTrue(welcomePage.isUserAuthenticated(),"Failed to land on CreateUser page");
	}
	
	public Object getPageObject() {
		return this.pageObject;
	}
	
	private void logAndFail(String classname) {
		
		logger.error(classname + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
    			citfTestInitializer.getPageName(getPageObject()));
		
    	fail(classname + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
    			citfTestInitializer.getPageName(getPageObject()));
	}
	
	private static final String MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS = " Page has been identified as ";
	private static final String MESSAGE_PAGE_NOT_FOUND = " page not found";

}
