package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * This is Selenium Page Object for "User Lookup Success" web page.
 * 
 * @author Sumit Pal
 *
 */

public class UserLookupSuccess extends SeleniumPageObject<UserLookupSuccess> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/h1")
	private WebElement headerMessage;
	
	
	/* Rest of the elements */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/section/header/menu/menuitem/a")
	private WebElement notMyUsername;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/section/div/p")
	private WebElement username;
	
	
	/* Transition Elements */ 
	@FindBy (how = How.ID, using = "signIn")
	private WebElement signIn;
	
	@FindBy (how = How.ID, using = "forgotPassword")
	private WebElement forgotPassword;
	
	
	public UserLookupSuccess(WebDriver driver) {
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
	 * To get text of username text box
	 * 
	 * @return returns text of username field
	 */
	public String getUsername() {
		if (isElementPresent(this.username)) {
			return this.username.getText();
		}
		return null;
	}
	
	
	/**
	 * To click not my username link
	 * 
	 * @return next page to set username
	 */
	public Object notMyUserName() throws Exception {
		if (isElementPresent(this.notMyUsername)) {
			this.notMyUsername.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, VerifyIdentity.class);
		}
		return null;
	}
	
	/**
	 * To click sign page and get the next page based on application flow
	 * 
	 * @return returns the selenium page object of next page of application flow
	 */
	public Object signIn() throws Exception {
		if (isElementPresent(this.signIn)) {
			this.signIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, SignInToXfinity.class);
		}
		return null;
	}
	
	/**
	 * To get forgot password page
	 * 
	 * @return selenium page object of Forgot password page
	 */
	public Object forgotPassword() throws Exception {
		if (isElementPresent(this.forgotPassword)) {
			this.forgotPassword.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId, ResetPasswordGateway.class);
		}
		return null;
	}
	

}
