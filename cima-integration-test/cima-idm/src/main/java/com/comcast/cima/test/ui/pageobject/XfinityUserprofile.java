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
 * This is Selenium Page Object for "Xfinity User profile" web page.
 * 
 * @author Sumit Pal
 *
 */

public class XfinityUserprofile extends SeleniumPageObject<XfinityUserprofile> {


	/*Login page elements */
	
	@FindBy(how = How.ID, using = "user")
	private WebElement username;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement SignIn;
	
	@FindBy(how = How.ID, using = "sign_in_fb")
	private WebElement ConnectUsingFacebook;
	
	@FindBy(how = How.ID, using = "fbLogin")
	private WebElement ConnectFacebook;
	
	
	
	/*User profile page elements*/
	
	@FindBy(how = How.XPATH, using = "//*[local-name() = 'button' and text() = 'Block Access']")
	private WebElement BlockAccess;
	
	@FindBy(how = How.XPATH, using = "//button[@data-action='unbindthirdpartyprofile']")
	private WebElement DisconnectFB;
	
	@FindBy(how = How.ID, using = "thirdpartyprofile-unbind-confirmation-dialog-submit")
	private WebElement DisconnectFBConfirmation;
	
	@FindBy(how = How.ID, using = "thirdpartyprofile-blockaccess-confirmation-dialog-submit")
	private WebElement BlockAcessConfirmation;

	@FindBy(how = How.XPATH, using = "//*[local-name() = 'button' and text() = 'Allow Access']")
	private WebElement AllowAcess;

	private String parentWindowHandler;
	
	
	public XfinityUserprofile(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.endsWith("/userprofile"));
	}

	@Override
	protected void load() {}
	
	
	/**
	 * To check whether Connect Facebook is Present
	 * 
	 * @return True if Connect Facebook link is Present, else False
	 */
	
	public boolean isConnectFacebookPresent()
	{
		return isElementPresent(ConnectFacebook);
	}
	
	public boolean isDisconnectFacebookPresent()
	{
		return isElementPresent(DisconnectFB);
	}
	
	/**
	 * To check whether Allow Access is Present
	 * 
	 * @return True if Allow Access is Present, else False
	 */	
	public boolean isAllowAccessPresent()
	{
		return isElementPresent(AllowAcess);
	}
	
	/**
	 * To check whether Block Access is Present
	 * 
	 * @return True if Block Access is Present, else False
	 */	
	public boolean isBlockAccessPresent()
	{
		return isElementPresent(BlockAccess);
	}
	
	/**
	 * To set username if username field is present
	 *
	 */
	
	private void setUserName(String name) {
		this.clearAndType(username, name);
	}
	
	/**
	 * To set password if password field is present
	 *
	 */
	
	private void setPassword(String passwd) {
		this.clearAndType(password, passwd);
	}
	
	/**
	 * Click Signin button
	 */
	private void clickSignin() {
		SignIn.click();
	}
	
	/**
	 * To sigin into application
	 */
	public void siginUserprofile(String user, String pass) {
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		}
	
	
	/**
	 * To click facebook connect to get facebook login popup
	 * 
	 * @return Object of facebook login popup
	 * 
	 */

	public Object getFacebookLoginPopup() {
		if (isElementPresent(ConnectFacebook)) {
			this.ConnectFacebook.click();
			this.parentWindowHandler = this.driver.getWindowHandle(); 		// Store your parent window
			this.driver = switchToChildWindow(this.driver);
			try {
				return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get the FB Login popup page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return null;
	}
	
	public void disconnectFacebook() {
		if(isElementPresent(this.DisconnectFB)) {
			this.DisconnectFB.click();
			waitForElementPresent(this.DisconnectFBConfirmation,ICommonConstants.WAIT_TIMEOUT);
			this.DisconnectFBConfirmation.click();
		} else {
			throw new IllegalStateException("Facebook disconnect button not found");
		}
	}
	
	public void blockAccess() {
		if(isElementPresent(this.BlockAccess)) {
			this.BlockAccess.click();
			waitForElementPresent(this.BlockAcessConfirmation,ICommonConstants.WAIT_TIMEOUT);
			this.BlockAcessConfirmation.click();
		} else {
			throw new IllegalStateException("Facebook Block Access button not found");
		}
	}
	
	public void allowAccess() {
		if(isElementPresent(this.AllowAcess)) {
			this.AllowAcess.click();
		} else {
			throw new IllegalStateException("Facebook Allow Access button not found");
		}
	}
	
	public String getParentWindowHandler() {
		return parentWindowHandler;
	}
	
}
