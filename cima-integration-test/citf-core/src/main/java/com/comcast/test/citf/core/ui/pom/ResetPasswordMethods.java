package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Reset Password Methods" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class ResetPasswordMethods extends SeleniumPageObject<ResetPasswordMethods> {

	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement recoverPasswordTitleMessage;
	
	/* Elements for password reset */
	
	@FindBy (how = How.ID, using = "EMAIL_radio")
	private WebElement sendEmailRadio;
	
	@FindBy (how = How.ID, using = "EMAIL_label")
	private WebElement sendEmailLabel;
	
	@FindBy (how = How.ID, using = "SMS_radio")
	private WebElement sendSMSRadio;
	
	@FindBy (how = How.ID, using = "SMS_label")
	private WebElement sendSMSLabel;
	
	
	/* Transition Elements */
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	@FindBy (how = How.ID, using = "fallback_option")
	private WebElement tryOtherWay;
	
	
	public ResetPasswordMethods(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To select password reset method as email
	 */
	
	public void selectPasswordResetByEmail() {
		if (isElementPresent(sendEmailLabel) && isElementPresent(sendEmailRadio)){
			this.selectRadioByID(this.sendEmailRadio);
		}
	}
	
	/**
	 * To select password reset method as SMS
	 */
	
	public void selectPasswordResetBySMS() {
		if (isElementPresent(sendSMSLabel) && isElementPresent(sendSMSRadio)){
			this.selectRadioByID(this.sendSMSRadio);
		}
	}
	
	/**
	 * To continue password resetting
	 * 
	 * @return page object of ResetPasswordByEmailor ResetPasswordBySMS page
	 */
	public Object continueResettingPassword() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To get other option of password recovery like ResetPasswordBySQA
	 * 
	 * @return object of ResetPasswordBySQA
	 */
	public Object continueResettingPasswordByOtherWay() {
		if (isElementPresent(tryOtherWay)) {
			this.tryOtherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}

}
