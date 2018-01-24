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

public class XfinityUserprofileSpanish extends SeleniumPageObject<XfinityUserprofileSpanish> {


	/*Login page elements */
	
	@FindBy(how = How.ID, using = "user")
	private WebElement username;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement signIn;
	
	@FindBy(how = How.ID, using = "fbLogin")
	private WebElement connectFacebook;
	
	
	/*User profile page elements*/
	
	@FindBy(how = How.XPATH, using = "//*[local-name() = 'button' and text() = 'Bloquear acceso']")
	private WebElement blockAccess;
	
	@FindBy(how = How.XPATH, using = "//button[@data-action='unbindthirdpartyprofile']")
	private WebElement disconnectFB;
	
	@FindBy(how = How.ID, using = "thirdpartyprofile-unbind-confirmation-dialog-submit")
	private WebElement disconnectFBConfirmation;
	
	@FindBy(how = How.ID, using = "thirdpartyprofile-blockaccess-confirmation-dialog-submit")
	private WebElement blockAcessConfirmation;

	@FindBy(how = How.XPATH, using = "//*[local-name() = 'button' and text() = 'Permitir acceso']")
	private WebElement allowAcess;

	private String parentWindowHandler;
	
	
	public XfinityUserprofileSpanish(WebDriver driver) {
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
		return isElementPresent(connectFacebook);
	}
	
	public boolean isDisconnectFacebookPresent()
	{
		return isElementPresent(disconnectFB);
	}
	
	/**
	 * To check whether Allow Access is Present
	 * 
	 * @return True if Allow Access is Present, else False
	 */	
	public boolean isAllowAccessPresent()
	{
		return isElementPresent(allowAcess);
	}
	
	/**
	 * To check whether Block Access is Present
	 * 
	 * @return True if Block Access is Present, else False
	 */	
	public boolean isBlockAccessPresent()
	{
		return isElementPresent(blockAccess);
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
		signIn.click();
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
		if (isElementPresent(connectFacebook)) {
			this.connectFacebook.click();
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
		if(isElementPresent(this.disconnectFB)) {
			this.disconnectFB.click();
			waitForElementPresent(this.disconnectFBConfirmation,ICommonConstants.WAIT_TIMEOUT);
			this.disconnectFBConfirmation.click();
		} else {
			throw new IllegalStateException("Facebook disconnect button not found");
		}
	}
	
	public void blockAccess() {
		if(isElementPresent(this.blockAccess)) {
			this.blockAccess.click();
			waitForElementPresent(this.blockAcessConfirmation,ICommonConstants.WAIT_TIMEOUT);
			this.blockAcessConfirmation.click();
		} else {
			throw new IllegalStateException("Facebook Block Access button not found");
		}
	}
	
	public void allowAccess() {
		if(isElementPresent(this.allowAcess)) {
			this.allowAcess.click();
		} else {
			throw new IllegalStateException("Facebook Allow Access button not found");
		}
	}
	
	public String getParentWindowHandler() {
		return parentWindowHandler;
	}
	
}
