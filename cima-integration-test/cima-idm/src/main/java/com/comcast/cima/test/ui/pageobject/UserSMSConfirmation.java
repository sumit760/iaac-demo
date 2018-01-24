package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "User SMS Confirmation" web page.
 * 
 * @author Sumit Pal
 *
 */

public class UserSMSConfirmation extends SeleniumPageObject<UserSMSConfirmation> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/ol/li[1]")
	private WebElement smsConfirmationMessage1;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/ol/li[2]")
	private WebElement smsConfirmationMessage2;
	
	@FindBy (how = How.ID, using = "smsCodeEntered")
	private WebElement smsCode;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continueButton;
	
	@FindBy (how = How.ID, using = "fallback")
	private WebElement verifyOtherWay;
	
	
	/* Error Elements */
	@FindBy (how = How.ID, using = "smsCodeEntered-error")
	private WebElement smsCodeError;
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage));
	}
	
	
	public UserSMSConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To verify whether SMS is sent successfully
	 * 
	 * @return Confirmation message of SMS sent
	 */
	
	public boolean isSMSSentSuccessfully() {
		boolean sent = false;
		if (isElementPresent(this.headerMessage) && isElementPresent(this.smsConfirmationMessage1)
				&& isElementPresent(this.smsConfirmationMessage2)) {
			
			sent = this.headerMessage.getText().contains("We've just sent a text to") && 
			this.smsConfirmationMessage1.getText().contains("Check your text messages") &&
			this.smsConfirmationMessage2.getText().contains("Return here to enter the verification code, which will expire in 15 minutes");
		}

		return sent;
	}
	
	/**
	 * To enter SMS code into code field
	 * 
	 * @param code :
	 *             SMS Code
	 *             
	 *             
	 */
	public void enterSMSCode(String code) {
		if (isElementPresent(this.smsCode)) {
			this.clearAndType(this.smsCode, code);
		}
	}
	
	/**
	 * Click continue button on user SMS confirmation page
	 * 
	 * @return returns next page selenium object or error
	 *           
	 *             
	 *             
	 */
	public Object Continue() {
		if (isElementPresent(this.continueButton)) {
			this.continueButton.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click "Verify other way " Link.
	 * 
	 * @return returns selenium page object of the next page according to application flow
	 */
	public Object verifyOtherWay() {
		if (isElementPresent(this.verifyOtherWay)) {
			this.verifyOtherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId,VerifyIdentity.class);
		}
		return null;
	}
	
	/**
	 * To get error message
	 * 
	 * @return retruns error message
	 */
	public String getErrorMessage() {
		if (isElementPresent(this.smsCodeError)) {
			return this.smsCodeError.getText();
		}
		return null;
	}

}
