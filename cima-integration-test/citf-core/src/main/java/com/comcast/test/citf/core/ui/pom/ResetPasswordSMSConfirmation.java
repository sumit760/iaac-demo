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
 * This is Selenium Page Object for "Reset Password SMS Confirmation" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class ResetPasswordSMSConfirmation extends SeleniumPageObject<ResetPasswordSMSConfirmation> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/ol/li[1]")
	private WebElement smsConfirmationMessage1;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/ol/li[2]")
	private WebElement smsConfirmationMessage2;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset/legend/span")
	private WebElement enterResetCodeTitle;
	
	@FindBy (how = How.ID, using = "resetCodeEntered")
	private WebElement resetCode;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement contnuePage;
	
	@FindBy (how = How.ID, using = "fallback_option")
	private WebElement tryOtherMethods;
	
	/* Error elements */
	@FindBy (how = How.ID, using = "resetCodeEntered-error")
	private WebElement errorResetCode;
	
	
	public ResetPasswordSMSConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To check whether SMS is sent successfully
	 * 
	 * @return True if SMS sent successfully, else False
	 */
	public boolean isSMSSentSuccessfully() {
		boolean sent = false;
		if (isElementPresent(this.headerMessage) && isElementPresent(this.smsConfirmationMessage1)
				&& isElementPresent(this.smsConfirmationMessage2)) {
			
			sent = this.headerMessage.getText().contains("We've just sent a text message to") && 
			this.smsConfirmationMessage1.getText().contains("Check your text messages") &&
			this.smsConfirmationMessage2.getText().contains("Return here to enter the reset code below");
		}

		return sent;
	}
	
	/**
	 * To enter reset code code
	 * @param code:
	 *            Reset Code
	 */
	public void enterResetCode(String code) {
		if (isElementPresent(resetCode))
			this.clearAndType(this.resetCode, code);
	}
	
	
	
	/**
	 * To continue resetting password
	 * 
	 * @return object of ResetPassword page
	 */
	public Object continueResettingPassword() {
		if (isElementPresent(contnuePage)) {
			this.contnuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, ResetPassword.class);
		}
		return null;
	}
	
	/**
	 * To continue password reset by other way
	 * 
	 * @return object of ResetPasswordMethods/ResetPasswordBySQA
	 */
	public Object continueWithOtherWay() {
		if (isElementPresent(tryOtherMethods)) {
			this.tryOtherMethods.click();
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
	
	/**
	 * To get error message of Reset Password SMS confirmation page
	 * 
	 * @return text of error message
	 */
	public String getErrorMessage() {
		if (isElementPresent(this.errorResetCode)) {
			return this.errorResetCode.getText();
		}
		return null;
	}

}
