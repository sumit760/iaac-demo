package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiWelcomeUsername extends SeleniumPageObject<SuSiWelcomeUsername> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//form/div/h1/span")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//form/div/div[1]/p")
	private WebElement description;
	
	@FindBy (how = How.ID, using = "user")
	private WebElement usernameText;
	
	@FindBy (how = How.ID, using = "passwd")
	private WebElement passwordText;
	
	@FindBy (how = How.XPATH, using = "//form/div/p/a[1]")
	private WebElement forgotUsernameLink;
	
	@FindBy (how = How.XPATH, using = "//form/div/p/a[2]")
	private WebElement forgotPasswordLink;
	
	@FindBy (how = How.XPATH, using = "//form/div/div[4]/p/a")
	private WebElement createUsernameLink;
	
	@FindBy (how = How.XPATH, using = "//form/div/nav/div[1]/button")
	private WebElement backButton;
	
	@FindBy (how = How.ID, using = "sign_in")
	private WebElement nextButton;
	
	public SuSiWelcomeUsername(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiWelcomeUsername page");	
	}
	
	/* Action Methods */
	public void enterUsername(String username) {
		if (isElementPresent(this.usernameText)) {
			this.clearAndType(this.usernameText, username);
		} else {
			throw new IllegalStateException("SuSi username text field not found");
		}
	}
	
	public void enterPassword(String password) {
		if (isElementPresent(this.passwordText)) {
			this.clearAndType(this.passwordText, password);
		} else {
			throw new IllegalStateException("SuSi password text field not found");
		}
	}
	
	/* Transition Method */
	public Object forgotUsername() {
		Object obj = null;
		if (isElementPresent(this.forgotUsernameLink)) {
			this.forgotUsernameLink.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the username lookup page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object forgotPassword() {
		Object obj = null;
		if (isElementPresent(this.forgotPasswordLink)) {
			this.forgotPasswordLink.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the password reset page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object pageBack() {
		Object obj = null;
		if (isElementPresent(this.backButton)) {
			this.backButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering mobile page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object pageNext() {
		Object obj = null;
		if (isElementPresent(this.nextButton)) {
			this.nextButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi your account page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}

}
