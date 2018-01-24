package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class CreateUserConfirmation extends SeleniumPageObject<UsernameLookupConfirmtaion>{

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/form/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//form/section/div/p")
	private WebElement singleUsernameMatch;
	
	@FindBy (how = How.XPATH, using = "//label[1]/span")
	private WebElement multipleUsernameMatch;
	
	@FindBy (how = How.XPATH, using = "//label[1]/div")
	private WebElement multipleUsernameMatchContent;
	
	@FindBy (how = How.XPATH, using = "//label[2]/div")
	private WebElement thirdPartyEmailContent;
	
	@FindBy (how = How.ID, using = "forgotPassword")
	private WebElement forgotPassword;
	
	@FindBy (how = How.XPATH, using = "//form/section/header/menu/menuitem/a")
	private WebElement notMyUsername;
	
	@FindBy (how = How.XPATH, using = "//main/p/a")
	private WebElement userNameNotListed;
	
	@FindBy (how = How.ID, using = "signIn")
	private WebElement continueButton;
	
	@FindBy (how = How.LINK_TEXT, using = "Continue")
	private WebElement continueButtonLink;
	
	@FindBy (how = How.XPATH, using = "//menuitem/span[2]")
	private WebElement preferredEmailFLag;


	public CreateUserConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	@Override
	protected void load() {}
	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage), "Failed to land on CreateUserConfirmation page");
	}
	
	public String getUserName(){
		if(isElementPresent(multipleUsernameMatchContent)) {
			return this.multipleUsernameMatchContent.getText();
		}
		if(isElementPresent(singleUsernameMatch)) {
			return this.singleUsernameMatch.getText();
		}
		return null;
	}
	
	public String getThirdPartyEmail() {
		if(isElementPresent(multipleUsernameMatchContent)) {
			return this.thirdPartyEmailContent.getText();
		}
		return null;
	}
	
	public Object confirmUsername() {
		Object obj = null;
		if ((isElementPresent(singleUsernameMatch) || isElementPresent(multipleUsernameMatch)) && isElementPresent(continueButton)) {
			if(isElementPresent(multipleUsernameMatch)) {
				this.multipleUsernameMatch.click();
			}
			this.continueButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get Xfinity home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	
	public Object clickContinue() {
		Object obj = null;
		if (isElementPresent(continueButtonLink)) {
			this.continueButtonLink.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get Xfinity home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
	
		return obj;
	}
	

	public boolean isEmailVerified() {
		
		if(isElementPresent(preferredEmailFLag)) {
			return this.preferredEmailFLag.getText().equalsIgnoreCase("verified");
		}
		else
		{
			return false;
		}
		
	}

	
}
