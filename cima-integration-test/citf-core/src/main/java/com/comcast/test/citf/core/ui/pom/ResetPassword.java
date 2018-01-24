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
 * This is Selenium Page Object for "ResetPassword" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class ResetPassword extends SeleniumPageObject<ResetPassword> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	

	/* Password Elements */
	@FindBy (how = How.XPATH, using = "//legend[@id='passwordFieldset_legend']/span")
	private WebElement createPasswordTitle;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='passwordFieldset_legend']/button")
	private WebElement createPasswordHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//li[@id='lengthStatus']/div")
	private WebElement  passwordHelpIconToolText1;
	
	@FindBy (how = How.XPATH, using = "//li[@id='hasLetterStatus']/div")
	private WebElement  passwordHelpIconToolText2;
	
	@FindBy (how = How.XPATH, using = "//li[@id='hasNumOrSpecialCharStatus']/div")
	private WebElement  passwordHelpIconToolText3;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noSpacesStatus']/div")
	private WebElement  passwordHelpIconToolText4;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noInvalidCharStatus']/div")
	private WebElement  passwordHelpIconToolText5;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noFirstNameStatus']/div")
	private WebElement  passwordHelpIconToolText6;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noLastNameStatus']/div")
	private WebElement  passwordHelpIconToolText7;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noUsernameStatus']/div")
	private WebElement  passwordHelpIconToolText8;
	
	@FindBy (how = How.XPATH, using = "//li[@id='notGuessableStatus']/div")
	private WebElement  passwordHelpIconToolText9;
	
	@FindBy (how = How.XPATH, using = "//div[@id='passwordEssentials']/div/button[@class='close']")
	private WebElement  passwordHelpIconClose;
	
	@FindBy (how = How.ID, using = "password")
	private WebElement  password;
	
	@FindBy (how = How.ID, using = "passwordRetype")
	private WebElement  reTypePassword;
	
	
	/* Password Remember */
	@FindBy (how = How.ID, using = "rememberMe_checkbox")
	private WebElement  rememberMe;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/label/span[2]")
	private WebElement  rememberMeTitle;
	
	
	/* Continue to Next Page */
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement  ContinuePage;
	
	
	/* Error Elements */
	@FindBy (how = How.ID, using = "password-error")
	private WebElement  passwordError;
	
	@FindBy (how = How.ID, using = "passwordRetype-error")
	private WebElement  passwordReTypeError;
	
	
	/* Success page elements, since the success page doesn't live and redirected to Xfinity Home */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement  successTitle;
	
	@FindBy (how = How.XPATH, using = "//main/p[1]")
	private WebElement  successMessage;
	
	
	
	public ResetPassword(WebDriver driver) {
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
	 * To verify whether reset password page is loaded successfully.
	 * 
	 * @return true if page is loaded successfully, else False
	 */
	/* Verification Method */
	public boolean isPageLoadedSuccessfully() {
		return (isElementPresent(createPasswordTitle) &&
				isElementPresent(password) &&
				isElementPresent(reTypePassword) &&
				isElementPresent(rememberMe) &&
				isElementPresent(rememberMeTitle) &&
				isElementPresent(ContinuePage));
	}
	
	
	/**
	 * To enter password if password field present
	 * 
	 * @param password :
	 *                 Password
	 */
	public void enterPassword(String password) {
		if (isElementPresent(this.password)){
			this.clearAndType(this.password, password);
		}
	}
	
	/**
	 * To Re enter password
	 * 
	 * @param password :
	 *                 Password
	 */	
	public void reEnterPassword(String password) {
		if (isElementPresent(this.reTypePassword)){
			this.clearAndType(this.reTypePassword, password);
		}
	}
	
	/**
	 * To select remember password checkbox
	 * 
	 */
	public void rememberPassword() {
		if (isElementPresent(this.rememberMe)){
			this.selectCheckboxByID(this.rememberMe);
		}
	}
	
	/**
	 * To click continue button present on reset password page
	 * 
	 * @return next page object based on application flow
	 */
	
	public Object continuePage() {
		if (isElementPresent(this.ContinuePage)) {
			this.ContinuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	/**
	 * To get text of password help icon 
	 * 
	 * @return text of password help icon  
	 */
	public String getPasswordHelpIconText() {
		String text = "";
		if (isElementPresent(createPasswordHelpIcon)) {
			this.createPasswordHelpIcon.click();
			waitForElementPresent(passwordHelpIconToolText1,10);
			if (isElementPresent(passwordHelpIconToolText1)) {
				text = text + this.passwordHelpIconToolText1.getText() + ","
						    + this.passwordHelpIconToolText2.getText() + ","
						    + this.passwordHelpIconToolText3.getText() + ","
						    + this.passwordHelpIconToolText4.getText() + ","
						    + this.passwordHelpIconToolText5.getText() + ","
						    + this.passwordHelpIconToolText6.getText() + ","
						    + this.passwordHelpIconToolText7.getText() + ","
						    + this.passwordHelpIconToolText8.getText() + ","
						    + this.passwordHelpIconToolText9.getText();
			}
				
			if (isElementPresent(passwordHelpIconClose)) {
				this.passwordHelpIconClose.click();
			}
		}
		return text;
	}
	
	
	/**
	 * To get map of error message
	 * 
	 * @return map of error message
	 */
	
	public Map<String,String> getErrorMesssage() {
		Map<String,String> errors = new HashMap<String,String>();
		
		if (isElementPresent(passwordError)){
			errors.put("passwordError".toUpperCase(), passwordError.getText());
		}
		if (isElementPresent(passwordReTypeError)){
			errors.put("passwordReTypeError".toUpperCase(), passwordReTypeError.getText());
		}

		return errors;
	}	


}
