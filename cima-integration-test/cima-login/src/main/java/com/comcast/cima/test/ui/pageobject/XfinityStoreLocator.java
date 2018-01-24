package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class XfinityStoreLocator extends SeleniumPageObject<XfinityStoreLocator> {
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement signIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signout;
	
	private final static String frameId = "polarisnav";
	
	public XfinityStoreLocator(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load(){
	}

	@Override
	protected void isLoaded() throws Error{
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Find Locations, Service and Payment Centers"));		
	}

	/**
	 * Check whether is signout link is present
	 * 
	 * @return True if user is signout link present, else False
	 */
	public Boolean isSignoutLinkPresent(){
		switchToDefaultFrame(driver);
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(signout);
	}
	
	/**
	 * Check whether is signin link is present
	 * 
	 * @return True if user is signin link present, else False
	 */
	public Boolean isSignInLinkPresent() {
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(signIn);
	}
	
	/**
	 * To check whether user is signed in
	 * @return True if signed in else False
	 */
	
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To check whether user is signed out
	 * @return True if signed out else False
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	public Object signIn() throws Exception {
		if (isElementPresent(this.signIn)) {
			switchToFrame(driver, this.frameId);
			this.signIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * To logout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			switchToFrame(driver, this.frameId);
			this.signout.click();
			waitForElementPresent(this.signIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh xfinity Support page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityHelpSupport.class);


}
