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

/**
 * This is Selenium Page Object for "Xfinity Contact Customer Support" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityContactCustomerSupport extends SeleniumPageObject<XfinityContactCustomerSupport> {
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	@FindBy(how = How.ID, using = "main_2_responsivecontent_0_pageintro_0_imgIntro")
	public WebElement contactUs;
	
	private final static String frameId = "polarisnav";
	
	public XfinityContactCustomerSupport(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void isLoaded() throws Error { 
		Assert.assertTrue(isElementPresent(this.contactUs));
	}

	@Override
	protected void load() {}
	
	/**
	 * Checks whether Signout Link is present
	 * 
	 * @return True if present, else False
	 */
	private boolean isSignoutLinkPresent() {
		switchToDefaultFrame(driver);
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(Signout);
	}
	
	private boolean isSignInLinkPresent() {
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(SignIn);
	}
	
	/**
	 * To get xfinity SignIn page
	 * 
	 * @return Page object of xfinity signIn page
	 */
	
	public Object getPageXfinitySignIn() throws Exception{
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}

	/**
	 * Checks whether user is signed in
	 * 
	 * @return True if signed in, else False
	 */
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * Checks whether user is signed out
	 * 
	 * @return True if signed out, else False
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	/**
	 * Checks whether page is loaded successfully
	 * 
	 * @return True if Loaded, else False
	 */
	public boolean isPageLoadedSuccessfully() {
		return isElementPresent(this.contactUs);
	}
	
	/**
	 * To click Signin Link
	 */
	public void clickSignIn() {
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			waitForElementPresent(this.Signout,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	/**
	 * To Signout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			this.Signout.click();
			waitForElementPresent(this.SignIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
}
