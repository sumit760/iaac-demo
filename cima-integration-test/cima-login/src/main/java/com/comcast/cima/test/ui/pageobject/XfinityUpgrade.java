package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * This is Selenium Page Object for "Xfinity Upgrade" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityUpgrade extends SeleniumPageObject<XfinityShopAndUpgrade> {
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement signIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signout;
	
	@FindBy(how = How.XPATH, using = "//div[@id='current-services-hero']/div/div[1]/h1")
	public WebElement greetingHeader;

	
	public XfinityUpgrade(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error { 
		Assert.assertTrue(isElementPresent(greetingHeader));
	}
	
	
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */
	public boolean isSignoutLinkPresent() {
		return isElementPresent(this.signout);
	}
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signinlink is present, else False
	 */
	public boolean isSignInLinkPresent() {
		return isElementPresent(this.signIn);
	}
	
	/**
	 * To click on signin link
	 * 
	 * @return True if Header Greeting is displayed, else False
	 */
	public boolean isHeaderGreetingDisplayed() {
		return isElementPresent(this.greetingHeader);
	}
	
	/**
	 * To check whether User is Signed In
	 * 
	 * @return True if Signed in else False
	 */
	public boolean isUserSignedIn() {
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To Check whether user is signed out
	 * 
	 * @return True if Signed out else False
	 */
	public boolean isUserSignedOut() {
		return this.isSignInLinkPresent();
	}

	/**
	 * To signout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			this.signout.click();
			waitForElementPresent(this.signIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh page
	 * 
	 * @throws Exception
	 */
	public Object refreshPage() throws Exception {
		this.driver.navigate().refresh();
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
	}
	
}
