package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

/**
 * This is Selenium Page Object for "Xfinity IDM Privacy Policy" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class XfinityIDMPrivacyPolicy extends SeleniumPageObject<XfinityIDMPrivacyPolicy> {

	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave0']/h1")
	public WebElement privacyPolicyHeader;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[1]")
	public WebElement privacyPolicy1;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[2]")
	public WebElement privacyPolicy2;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[3]")
	public WebElement privacyPolicy3;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[4]")
	public WebElement privacyPolicy4;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[5]")
	public WebElement privacyPolicy5;
	
	@FindBy(how = How.XPATH, using = "//div[@id='DFCMSave1']/h3[6]")
	public WebElement privacyPolicy6;
	

	public XfinityIDMPrivacyPolicy(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}

	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(privacyPolicyHeader));
	}

	
	/**
	 * To verify Xfinity Privacy Policy 
	 * 
	 * @return True or false based on validation status
	 */
	public boolean verifyXfinityPrivacyPolicy() {
		boolean validated = false;
		if (isElementPresent(privacyPolicyHeader) &&
			isElementPresent(privacyPolicy1) &&
			isElementPresent(privacyPolicy2) &&
			isElementPresent(privacyPolicy3) &&
			isElementPresent(privacyPolicy4) &&
			isElementPresent(privacyPolicy5) &&
			isElementPresent(privacyPolicy6) ) {
			validated = true;
		}
		return validated;
	}

	private static Logger logger = LoggerFactory.getLogger(XfinityIDMPrivacyPolicy.class);
}
