package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Last step before facebook connect" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class LastStepBeforeFBConnect extends SeleniumPageObject<LastStepBeforeFBConnect> {

	/* Web Elements */
	
	/* Header Mesage */
	@FindBy (how = How.XPATH, using = "//*[@id='title']")
	private WebElement headerMessage;
	
	
	/*Login elements */
	@FindBy (how = How.ID, using = "user")
	private WebElement username;
	
	@FindBy (how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy (how = How.ID, using = "remember_me")
	private WebElement keepmeSignedIn;
	
	@FindBy (how = How.ID, using = "rm_label_learn_more")
	private WebElement learnMoreLink;
	
	@FindBy (how = How.ID, using = "sign_in")
	private WebElement signIn;
	
	
	/*weblink elements*/	
	@FindBy (how = How.XPATH, using = "//*[@id='signin']/p[1]/a/strong")
	private WebElement dontKnowUsernameLink;
	
	@FindBy (how = How.XPATH, using = "//*[@id='forgotPwdLink']/strong")
	private WebElement forgotPasswordLink;
	
	@FindBy (how = How.XPATH, using = "//*[@id='needaccess']/p/a")
	private WebElement createUsernameLink;
	
		
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public LastStepBeforeFBConnect(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	
	/*Methods to check presence of element*/
	
	/**
	 * To check whether Header is present
	 * 
	 * @return true if Header is present, else False
	 */
	public boolean isHeaderPresent()
	{
		return isElementPresent(headerMessage);		
	}
	
	/**
	 * To check whether Email Or Username Field is Present
	 * 
	 * @return true if Email Or Username is present, else False
	 */
	
	public boolean isEmailOrUsernameFieldPresent()
	{
		return isElementPresent(username);		
	}
	
	
	/**
	 * To check whether password Field is Present
	 * 
	 * @return true if password is present, else False
	 */		
	public boolean ispasswordFieldPresent()
	{
		return isElementPresent(password);		
	}
	
	/**
	 * To check whether signin button Present
	 * 
	 * @return true if signin button is present, else False
	 */	
	public boolean isSigninButtonPresent()
	{
		return isElementPresent(signIn);		
	}	
	
	/**
	 * To check whether "Don't Know Username" Link Present.
	 * 
	 * @return true if "Don't Know Username" link is present, else False
	 */		
	public boolean isDontKnowUsernameLinkPresent()
	{
		return isElementPresent(dontKnowUsernameLink);		
	}
	
	/**
	 * To check whether "Forgot Password" Link Present.
	 * 
	 * @return true if "Forgot Password" link is present, else False
	 */		
	public boolean isForgotPasswordLinkPresent()
	{
		return isElementPresent(forgotPasswordLink);		
	}
	
	/**
	 * To check whether "Create Username" Link Present.
	 * 
	 * @return true if "Create Username" link is present, else False
	 */		
	public boolean isCreateUsernameLinkPresent()
	{
		return isElementPresent(createUsernameLink);		
	}
	
	/**
	 * To check whether "Keep me signed in" checkbox Present.
	 * 
	 * @return true if "Keep me signed in" checkbox is present, else False
	 */	
	public boolean isKeepMeSignedInCheckboxPresent()
	{
		return isElementPresent(keepmeSignedIn);		
	}
	
	/**
	 * To check whether "Learn More" Link Present.
	 * 
	 * @return true if "Learn More" Link is present, else False
	 */	
	
	public boolean isLearnMoreLinkPresent()
	{
		return isElementPresent(learnMoreLink);		
	}
	
	/**
	 * To enter username 
	 * 	
	 * @param username :
	 *                  Username
	 */
	public void enterUsername(String username) {
		if (isElementPresent(this.username)) {
			this.clearAndType(this.username, username);
			}
		}
	
	/**
	 * To enter password 
	 * 	
	 * @param password 
	 *                  password
	 */	
	public void enterPassword(String password) {
		if (isElementPresent(this.password)) {
			this.clearAndType(this.password, password);
			}
		}
	
	/**
	 * To click on signin button and get the next page based on application navigation
	 * 
	 * @return page object of next page
	 * @throws Exception
	 */
	
	public Object clickSignin() {
		if (isElementPresent(signIn)) {
			this.signIn.click();
			try {
				return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get the Xfinity Home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return null;
	}
	
	/**
	 * To click create username link
	 * 
	 * @return next page of create username application flow
	 * @throws Exception
	 */
		
	public Object clickCreateUsername() throws Exception {
		if (isElementPresent(createUsernameLink)) {
			this.createUsernameLink.click();
			Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click forgot password link
	 * 
	 * @return next page of forgot password application flow.
	 * @throws Exception
	 */
	
	public Object clickforgotPassword() throws Exception {
		if (isElementPresent(forgotPasswordLink)) {
			this.forgotPasswordLink.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	/**
	 * To click username lookup link
	 * 
	 * @return next page of username lookup application flow.
	 * @throws Exception
	 */
	
	public Object clickUsernameLookup() {
		if (isElementPresent(dontKnowUsernameLink)) {
			this.dontKnowUsernameLink.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
}
