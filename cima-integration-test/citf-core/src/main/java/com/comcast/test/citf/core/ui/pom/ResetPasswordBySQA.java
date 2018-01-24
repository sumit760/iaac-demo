package com.comcast.test.citf.core.ui.pom;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Reset Password By SQA" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class ResetPasswordBySQA extends SeleniumPageObject<ResetPasswordBySQA> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	
	/* Security Questions */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/span")
	private WebElement securityQuestion;
	
	@FindBy (how = How.ID, using = "secretAnswer")
	private WebElement securityQuestionAnswer;
	
	@FindBy (how = How.ID, using = "fallback_option")
	private WebElement dontRememberAnswer;
	
	
	/* Zip Code */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[2]/legend/span")
	private WebElement zipCodeTitleMessage;
	
	@FindBy (how = How.ID, using = "zipCode")
	private WebElement zipCode;
	
	
	/* Transition Element */
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	@FindBy (how = How.ID, using = "nucaptcha-answer")
	private WebElement nucaptchaAnswer;
	
	/* Error Elements */
	@FindBy (how = How.ID, using = "secretAnswer-error")
	private WebElement secretAnswerError;
	
	@FindBy (how = How.ID, using = "zipCode-error")
	private WebElement zipCodeError;
	
	public ResetPasswordBySQA(WebDriver driver) {
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
	 * To set answer of security question
	 * @param ans :
	 *             Answer of security question
	 */
	
	public void answerSecretQuestion(String ans) {
		if (isElementPresent(securityQuestion) && isElementPresent(securityQuestionAnswer)){
			this.clearAndType(this.securityQuestionAnswer, ans);
		}
	}
	
	/**
	 * To enter Zip code 
	 * 
	 * @param zip :
	 *             ZIP code
	 */
	
	public void enterZipCode(String zip) {
		if (isElementPresent(zipCodeTitleMessage) && isElementPresent(zipCode)){
			this.clearAndType(this.zipCode, zip);
		}
	}
	
	/**
	 * To enter captcha text
	 * 
	 * @param inNucaptchaAnswer :
	 *                           Captcha text
	 */
	public void enterNucaptchaAnswer(String inNucaptchaAnswer) {
		if (isElementPresent(this.nucaptchaAnswer)){
			this.clearAndType(this.nucaptchaAnswer, inNucaptchaAnswer);
		}
		
	}
	
	/**
	 * To get to next page after entering security captcha text
	 * 
	 * @return ResetPassword page if security answer is correct and Stays on the same page and throws error if answer is not correct
	 */
	public Object continueResettingPassword() {
		if (isElementPresent(this.continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, ResetPassword.class);
		}
		return null;
	}
	
	
	/**
	 * To click on don't remember field
	 * 
	 * @return page object of ResetPasswordByAgent
	 */
	public Object dontRememberAnswer() {
		if (isElementPresent(this.dontRememberAnswer)) {
			this.dontRememberAnswer.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId,ResetPasswordByAgent.class);
		}
		return null;
	}
	
	/**
	 * To get error message of page reset password by SQA
	 * 
	 * @return MAP of error message
	 */
	
		public Map<String,String> getErrorMesssage() {
		Map<String,String> errors = new HashMap<String,String>();
		
		if (isElementPresent(secretAnswerError)) {
			errors.put("secretAnswerError".toUpperCase(), secretAnswerError.getText());
		}
		if (isElementPresent(zipCodeError)) {
			errors.put("zipCodeError".toUpperCase(), zipCodeError.getText());
		}

		return errors;
	}	

}
