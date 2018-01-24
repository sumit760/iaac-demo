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
 * This is Selenium Page Object for "SMS Expired" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class SMSExpired extends SeleniumPageObject<SMSExpired> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/p")
	private WebElement letsTryAgain;
	
	@FindBy (how = How.ID, using = "smsCodeEntered")
	private WebElement smsCode;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement tryAgain;
	
	@FindBy (how = How.ID, using = "fallback")
	private WebElement anotherWay;
	
	
	/* Error Elements */
	
	@FindBy (how = How.ID, using = "smsCodeEntered-error")
	private WebElement errorMessage;
	
	
	public SMSExpired(WebDriver driver) {
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
	 * To enter SMS code if SMS code field is present
	 * @param code:
	 *            SMS code
	 */
	public void enterSMSCode(String code) {
		if (code != null && isElementPresent(this.smsCode)) {
			this.clearAndType(this.smsCode, code);
		}
	}
	
	
	/**
	 * To click try again button to re send the SMS code
	 * 
	 * @return next page object based on application navigation
	 * @throws Exception
	 */
	
	public Object tryAgain() throws Exception {
		if (isElementPresent(this.tryAgain)) {
			this.tryAgain.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click on verify with alternative way
	 * 
	 * @return next page object based on application navigation
	 * @throws Exception
	 */
	public Object verifyOtherway() throws Exception {
		if (isElementPresent(this.anotherWay)) {
			this.anotherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, VerifyIdentity.class);
		}
		return null;
	}
	
	/**
	 * To get error message of SMS expired page
	 * 
	 * @return error message text
	 */
	public String getErrorMessage() {
		if (isElementPresent(this.errorMessage)) {
			return this.errorMessage.getText();
		}
		return null;
	}
	
	/**
	 * To get Header message text of SMS expired page  
	 * 
	 * @return text of header of SMS expired
	 */
	public String getHeaderMessage() {
		if (isElementPresent(this.headerMessage)) {
			return this.headerMessage.getText();
		}
		return null;
	}
	

}
