package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.thoughtworks.selenium.webdriven.commands.IsVisible;

public class AWSManagementConsole extends SeleniumPageObject<AWSManagementConsole> {
	
	/* Header Message */
	@FindBy (how = How.ID, using = "gwt-debug-button-launch-instance")
	private WebElement  headerGreetingMessage;
	
	/* Logout controls */
	@FindBy(how = How.XPATH, using = "//*[@id='nav-usernameMenu']/div[1]")
	private WebElement signOutLink;
	
	@FindBy(how = How.ID, using = "aws-console-logout")
	private WebElement signOut;
	

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerGreetingMessage), "Failed to land on CreateUser page");
		
	}

	@Override
	protected void load() {
			
	}

	public AWSManagementConsole(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void signOut() {
		if (this.signOutLink != null) {
			this.signOutLink.click();
			waitForElementPresent(this.signOut, ICimaCommonConstants.WAIT_TIME_UI);
			this.signOut.click();
		}
	}
	
	
}
