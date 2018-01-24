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
 * This is Selenium Page Object for "Reset Password Gateway" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class ResetPasswordGateway extends SeleniumPageObject<ResetPasswordGateway>  {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	
	/* Username */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset/legend/span")
	private WebElement promptForXfinityUsernameTitle;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/button[@class='info icon']")
	private WebElement xfinityUsernameHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset/legend/div/div")
	private WebElement xfinityUsernameHelpIconText;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset/legend/div/div/button")
	private WebElement xfinityUsernameHelpIconClose;
	
	@FindBy (how = How.ID, using = "userName")
	private WebElement xfinityUsername;
	
	
	/* Transition Elements */
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/a")
	private WebElement forgotUsername;
	
	
	/* Error messages */
	
	@FindBy(how = How.ID, using = "userName-error")
	private WebElement userNameError;
	
	
	
	public ResetPasswordGateway(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	public boolean isForgotUsernamePresent() {
		return isElementPresent(this.forgotUsername);
	}
	
	
	/**
	 * To Enter Username
	 * @param username :
	 *                  Username
	 */
	
	public void enterUsername(String username) {
		if (isElementPresent(xfinityUsername)) {
			this.clearAndType(this.xfinityUsername, username);
		}
	}
	
	
	/**
	 * To continue reseting password
	 * 
	 * @return usually returns the SecurityCheck page object
	 */
	public Object continueResettingPassword() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To get Username Lookup Page object
	 * @return Username Lookup Page object
	 */
	public Object continueUserNameLookup() {
		if (isElementPresent(forgotUsername)) {
			this.forgotUsername.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To get error message of Reset password gateway page
	 * 
	 * @return error message
	 */
	
	public String getErrorMesssage() {
		if (isElementPresent(userNameError)) {
			return userNameError.getText();
		} 
		
		return null;
	}
	
	
	/**
	 * TO get help icon text
	 * 
	 * @return text of help icon
	 */
	public String getUserNameHelpIconText() {
		String text = null;
		if (isElementPresent(this.xfinityUsernameHelpIcon)) {
			this.xfinityUsernameHelpIcon.click();
			waitForElementPresent(xfinityUsernameHelpIconText,10);
			if (isElementPresent(xfinityUsernameHelpIconText)){
				text = this.xfinityUsernameHelpIconText.getText();
			}
			if (isElementPresent(this.xfinityUsernameHelpIconClose)) {
				this.xfinityUsernameHelpIconClose.click();
			}
		}
		return text;
	}
	
	/**
	 * To validate User Name Help Icon Text
	 * 
	 * @return true if help icon text matches, else False
	 */
	public boolean validateUserNameHelpIconText() {
		String textToBePresent = "Your username was created as part of your Comcast ID. "
				+ "You can also use your @comcast.net email or any other email "
				+ "linked to this account, including Gmail, Yahoo, AOL, etc.";
		
		String  actualText = getUserNameHelpIconText();
		return actualText != null ? actualText.equalsIgnoreCase(textToBePresent) : false;
	}


}
