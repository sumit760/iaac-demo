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

/**
 * This is Selenium Page Object for "Xfinity Toolbar" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityToolbar extends SeleniumPageObject<XfinityToolbar> {
	
	
	@FindBy(how = How.XPATH, using = "//div[@id='header']/div/h2/span")
	public WebElement header;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	public XfinityToolbar(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void load(){}

	@Override
	protected void isLoaded() throws Error{
		Assert.assertTrue(isElementPresent(this.header));		
	}

	/* Method to check presense of elements*/
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */
	public boolean isSignoutLinkPresent(){
		return this.isElementPresent(Signout);
	}
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signinlink is present, else False
	 */
	public boolean isSignInLinkPresent() {
		return this.isElementPresent(SignIn);
	}
	
	/**
	 * To click on signin link
	 * 
	 * @return Next page object
	 * @throws Exception
	 */
	public Object getPageXfinitySignIn() throws Exception{
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * To check whether User is Signed In
	 * 
	 * @return True if Signed in else False
	 */
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To Check whether user is signed out
	 * 
	 * @return True if Signed out else False
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	/**
	 * To signout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			this.Signout.click();
			waitForElementPresent(this.SignIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForElementPresent(header,ICommonConstants.WAIT_TIMEOUT);
	}
	
	private static Logger logger = LoggerFactory.getLogger(XfinityToolbar.class);

}
