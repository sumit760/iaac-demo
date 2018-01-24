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
 * This is Selenium Page Object for "Facebook login" popup.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class FacebookLoginPopUp extends SeleniumPageObject<FacebookLoginPopUp> {
	
	/*FB Popup element details*/
	@FindBy(how = How.ID, using = "email")
	private WebElement FBEmaailOrPhone;
	
	@FindBy(how = How.ID, using = "pass")
	private WebElement FBPassword;
	
	@FindBy(how = How.ID, using = "u_0_2")
	private WebElement FBLogin;
	
	@FindBy(how = How.ID, using = "homelink")
	private WebElement FBPopUpHeader;
	
	@FindBy(how = How.ID, using = "popup_message_descr")
	private WebElement FBLoginBlocked;
	
	@FindBy(how = How.LINK_TEXT, using = "Forgotten password?")
	private WebElement forgotPassword;
	
	@FindBy(how = How.ID, using = "persist_box")
	private WebElement keepMeLoggedIn ;
	
	@FindBy(how = How.NAME, using = "cancel")
	private WebElement cancelButton ;
	
	
	public FacebookLoginPopUp(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	
	@Override
	protected void load() {}
	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(FBPopUpHeader));
	}
		
	
/**
 * To enter email or phone if field is present
 * 
 * @param email :
 *              Email to login into facebook
 */
	
	public void setEmailORPhone(String email) {
		if (isElementPresent(this.FBEmaailOrPhone))
			this.clearAndType(this.FBEmaailOrPhone, email);
	}
	
	/**
	 * To check whether email or phone is present
	 * 
	 * @return True if email/phone field is present, else False.
	 */
	
	public boolean isEmailOrPhoneTextFieldDisplayed()
	{
		return isElementPresent(FBEmaailOrPhone);		
	}
	
	/**
	 * To enter password
	 * 
	 * @param email :
	 *               Email
	 * 
	 */
	public void setPassword(String email) {
		if (isElementPresent(this.FBPassword))
			this.clearAndType(FBPassword, email);
	}
	
	/**
	 * To check whether password field is present 
	 * 
	 * @return True if password field is present, else False.
	 */
	
	public boolean isPasswordTextFieldDisplayed()
	{
		return isElementPresent(FBPassword);		
	}
	
	
	/*FB Login*/
	
	/**
	 * To click FbLogin button
	 */
	
	private void ClickFbLogin() {
		if (isElementPresent(this.FBLogin))
			FBLogin.click();
	}
	
	
	/**
	 * To check whether FBLogin Button is present 
	 * 
	 * @return True if password field is present, else False.
	 */
	
	public boolean isFBLoginButtonDisplayed()
	{
		return isElementPresent(FBLogin);		
	}
		
	/**
	 * To check whether Forgot Password Link is present 
	 * 
	 * @return True if password field is present, else False.
	 */
	
	public boolean isForgotPasswordLinkDisplayed()
	{
		return isElementPresent(forgotPassword);		
	}
	
	/**
	 * To check whether keep me signed in checkbox is present 
	 * 
	 * @return True if keep me signed  checkbox is present, else False.
	 */
	public boolean isKeepMeLoggedInCheckBoxDisplayed()
	{
		return isElementPresent(keepMeLoggedIn);		
	}
	
	/**
	 * To check whether cancel button is present 
	 * 
	 * @return True if cancel button is present, else False.
	 */
	public boolean isCancelButtonDisplayed()
	{
		return isElementPresent(cancelButton);		
	}
	
	/**
	 * To get Last Step Before FBConnect Page
	 * @param email :
	 *             Email
	 * @param password :
	 *                 Password
	 *              
	 * @throws Exception
	 *  @return Last Step Before FBConnect Page
	 */
	public Object getPageLastStepBeforeFBConnect(String email,String password) throws Exception {
		this.setEmailORPhone(email);
		this.setPassword(password);
		this.ClickFbLogin();
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
	}
	

	/*Transition Methods*/
	
	public Object login(String email,String password) throws Exception {
		this.setEmailORPhone(email);
		this.setPassword(password);
		this.ClickFbLogin();
		return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
	}
	
	public Object login(String email,String password, String parentWindow) {
		this.setEmailORPhone(email);
		this.setPassword(password);
		this.ClickFbLogin();
		this.driver = switchToParentWindow(this.driver, parentWindow);
		try {
			return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get the Last step before FB connect page or Xfinity Home page. PageFlowId: " + this.pageFlowId, e);
		}
	}

}
