package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Xfinity Home Security" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityHomeSecurity extends SeleniumPageObject<XfinityHomeSecurity> {
	

	@FindBy(how = How.XPATH, using = "//li[@id='navigationplaceholder_0_ctl00_DesktopMenu_DesktopMenuListItem_1']/a")
	public WebElement myAccount;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement signIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signout;
	
	@FindBy(how = How.XPATH, using = "//div[@id='urlContentPanel']/div[1]/div[2]/div/div[1]/div/div[1]/h1/span")
	public WebElement headerMessage;
	

	public XfinityHomeSecurity(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity Home and  security page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	/**
	 * To click signin link
	 */
	
	public void clickSignin() {
		if (isElementPresent(this.signIn)) {
			this.signIn.click();
			waitForElementPresent(this.signout,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To click signin link
	 * 
	 * @return object of xfinity signin page or Next page object
	 * @throws Exception
	 */
	public Object getPageXfinitySignIn() throws Exception{
		if (isElementPresent(this.signIn)) {
			this.signIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * To check whether user is signed in
	 * 
	 * @return True if signed in else False
	 */
	public boolean isUserSignedIn()	{
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To check whether user is signed out
	 * @return True if signed out else False
	 */
	public boolean isUserSignedOut()	{
		return this.isSignInLinkPresent();
	}
	
	/**
	 * To logout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			this.signout.click();
			waitForElementPresent(this.signIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh xfinity Home and security page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	/**
	 * To check whether Header greeting of page is displayed
	 * @return True if present else False
	 */
	public boolean isHeaderGreetingDisplayed() {
		return isElementPresent(this.headerMessage);
	}
	
	/**
	 * To Click on My account link present on Home security Page
	 * 
	 * @return next page object
	 * @throws Exception
	 */
	public Object getPageMyAccount() throws Exception {
		if (isElementPresent(this.myAccount)) {
			this.myAccount.click();
			waitForLinkedText("Sign Out",ICommonConstants.WAIT_TIMEOUT);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}
	
	
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link present, else False
	 */
	private Boolean isSignoutLinkPresent() {
		return this.isElementPresent(signout);
	}
	
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signin link present, else False
	 */
	private Boolean isSignInLinkPresent() {
		return this.isElementPresent(signIn);
	}
	
	
	/**
	 * To get URL of Home and security page
	 * 
	 * @return URL of Home and security page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.HOME_SECURITY.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityHomeSecurity.class);
}

