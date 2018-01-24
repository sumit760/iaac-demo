package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "User By Mobile" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class UserByMobile extends SeleniumPageObject<UserByMobile> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "phoneNumber")
	private WebElement mobilePhone;
	
	@FindBy (how = How.ID, using = "continue")
	private WebElement continuePage;
	
	
	/* Continue other way */ 
	@FindBy (how = How.ID, using = "fallback")
	private WebElement continueOtherWay;
	
	
	/* Elements in Error scenarios */
	@FindBy (how = How.ID, using = "phoneNumber-error")
	private WebElement phoneNoError;
	
	/* The consent message */
	
	@FindBy (how = How.ID, using = "inlineConsent")
	private WebElement consentMessage;
	
	
	public UserByMobile(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	/**
	 * To enter mobile number
	 * 
	 * @param number:
	 *              Mobile Number
	 */
	public void enterMobileNumber(String number) {
		if (number != null && isElementPresent(this.mobilePhone)) {
			this.clearAndType(this.mobilePhone, number);
		}
	}
	
	
	/**
	 * To click continue to get to create user page or information mismatch page
	 * 
	 * @return object of create user page or information mismatch page
	 * @throws Exception
	 */
	
	public Object Continue() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			waitForPageLoaded(driver);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click continue other way to create user
	 * 
	 * @return object of next page according to application flow
	 * @throws Exception
	 */
	
	public Object continueOtherWay() throws Exception {
		if (isElementPresent(continueOtherWay)) {
			this.continueOtherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId,VerifyIdentity.class);
		}
		return null;
	}
	
	/**
	 * To verify consent message
	 * 
	 * @return True if consent message matched else false
	 */
	public boolean verifyConsentMessage() {
		
		String expectedMessage = "By pressing the \"Continue\" button, I give my consent to receive text messages on behalf of Comcast via automated technology to my wireless phone number regarding my XFINITY username and password. I understand that I am not required to provide this consent to make a purchase from Comcast.";
		if (isElementPresent(this.consentMessage)) {
			return this.consentMessage.getText().equalsIgnoreCase(expectedMessage);
		}
		return false;
	}
	
	/**
	 * To get error message
	 * 
	 * @return text of error message
	 */
	public String getErrorMessage() {
		if (isElementPresent(this.phoneNoError)) {
			return this.phoneNoError.getText();
		}
		return null;
	}

	

}
