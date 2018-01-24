package com.comcast.cima.test.api.cucumber.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class OpenId_WebServerApplication_Flow {
	
	private Scenario testScenario;
	
	@Autowired
	private OAuthCommonSteps oAuthCommonSteps;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
	}
	
	
	@After
	public void tearDown() {
		try{
			WebDriver browser = oAuthCommonSteps.getBrowser();
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
			LOGGER.error("Error occured while performing clean up activities : ", e);
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenId_WebServerApplication_Flow.class);
}
