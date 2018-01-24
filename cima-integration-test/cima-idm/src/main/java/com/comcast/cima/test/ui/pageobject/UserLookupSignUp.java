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
 * This is Selenium Page Object for "User Lookup SignUp" web page.
 * 
 *@author sumit pal
 */
public class UserLookupSignUp extends SeleniumPageObject<UserLookupSignUp> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "createUID")
	private WebElement createUserNameButton;
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public UserLookupSignUp(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To check whether create username button is present
	 * 
	 * @return True if create username button present, else False
	 */
	public boolean isCreateUserPresent(){
		return isElementPresent(createUserNameButton);
	}
	
	public Object continueCreatingUsername() {
		if (isElementPresent(createUserNameButton)) {
			this.createUserNameButton.click();
			waitForPageLoaded(driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;	
	}
}
