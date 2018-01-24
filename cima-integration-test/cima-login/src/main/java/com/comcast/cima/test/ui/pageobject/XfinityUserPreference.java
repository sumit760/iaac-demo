package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Xfinity User Preference" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityUserPreference extends SeleniumPageObject<XfinityUserPreference> {
	
	private LoadableComponent<?> parent;

	@FindBy(how = How.LINK_TEXT, using = "Email")
	public WebElement emailLink;
	
	@FindBy(how = How.ID, using = "navigationplaceholder_0_ctl00_aSignLink")
	public WebElement signInLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signoutLink;
	
	@FindBy(how = How.LINK_TEXT, using = "My Account")
	public WebElement myAccountLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Click here")
	public WebElement clickHereForLoginToComcastAccount;
	
	@FindBy(how = How.ID, using = "disabled_message")
	public WebElement ComcastLoginPopup;
	
	@FindBy(how = How.ID, using = "user")
	private WebElement username;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement SignIn;
	
	@FindBy(how = How.ID, using = "main_1_pageheader_0_PageHeaderText")
	private WebElement userPreferenceHeader;
	
	
		
	public XfinityUserPreference(WebDriver driver, LoadableComponent<?> parent)  {
		this.driver = driver;
		this.parent = parent;
		PageFactory.initElements(driver, this);
	}
	
	public XfinityUserPreference(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		this.driver.get("https://idp.comcast.net/idp/startSSO.ping?PartnerSpId=publish.comcast.net");
	}

	@Override
	protected void isLoaded() throws Error {
				
		Assert.assertTrue(this.isElementPresent(ComcastLoginPopup));
		
	}
	
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */

	public boolean isSignoutLinkPresent() {
		return this.isElementPresent(signoutLink);
	}
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signinlink is present, else False
	 */

	public boolean isSignInLinkPresent() {
		return this.isElementPresent(signInLink);
	}
	
	
	/**
	 * To click LoginToComcastAccount link
	 */
	public void clickHereToLoginIntoComcast() {
			this.clickHereForLoginToComcastAccount.click();
					
	}
	
	/**
	 * To Enter password
	 * @param passwd
	 * 			Password
	 */

	public void setPassword(String passwd) {
		this.clearAndType(password, passwd);
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


}

