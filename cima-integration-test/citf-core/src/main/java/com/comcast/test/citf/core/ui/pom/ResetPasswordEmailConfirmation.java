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
 * This is Selenium Page Object for "Reset Password EmailConfirmation" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class ResetPasswordEmailConfirmation extends SeleniumPageObject<ResetPasswordEmailConfirmation> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/ol/li[1]")
	private WebElement emailConfirmationMessage1;
	
	@FindBy (how = How.XPATH, using = "//main/ol/li[2]")
	private WebElement emailConfirmationMessage2;
	
	@FindBy (how = How.ID, using = "fallback_option")
	private WebElement tryOtherMethods;
	
	
	public ResetPasswordEmailConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	/**
	 * To verify if email is send successfully
	 * 
	 * @return True is sent successfully, else False
	 */
	public boolean isEmailSentSuccessfully() {
		boolean sent = false;
		if (isElementPresent(this.headerMessage) && isElementPresent(this.emailConfirmationMessage1)
				&& isElementPresent(this.emailConfirmationMessage2)) {
			
			sent = this.headerMessage.getText().contains("We've just sent an email to") && 
			this.emailConfirmationMessage1.getText().contains("Check your email") &&
			this.emailConfirmationMessage2.getText().contains("Click the link to reset your password");
		}
		
		return sent;
	}
	
	
	/* Transition Method */
	
	/**
	 * To get page object of ResetPasswordMethods/ResetPasswordBySQA
	 * 
	 * @return page object of ResetPasswordMethods/ResetPasswordBySQA
	 */
	public Object continueWithOtherMethod() {
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

}
