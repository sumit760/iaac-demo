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
 * This is Selenium Page Object for "Xfinity Shop And Upgrade" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityShopAndUpgrade extends SeleniumPageObject<XfinityShopAndUpgrade> {
	
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement signIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signout;
	
	@FindBy(how = How.XPATH, using = "//div[@id='urlContentPanel']/div[1]/div[1]/div/a[@class='button secondary']")
	public WebElement signInForDeals;
	
	
	public XfinityShopAndUpgrade(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading xfinity shop and upgrade page ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Comcast Deals"));
	}
	
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */
	public boolean isSignoutLinkPresent() {
		return this.isElementPresent(signout);
	}
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signin link is present, else False
	 */
	public boolean isSignInLinkPresent() {
		return this.isElementPresent(signIn);
	}
	
	
	/* getPages methods for the transition elements */
	/**
	 * To click on signin link
	 * 
	 * @return Next page object
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
	 * To Click signin for deals link
	 * 
	 * @return object of next page
	 * @throws Exception
	 */
	
	public Object getPageXfinitySignInForDeals() throws Exception {
		if (isElementPresent(this.signInForDeals)) {
			this.signInForDeals.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId(),XfinityUpgrade.class);
		}
		return null;
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
	 * @return True if Signed out else False
	 */
	public boolean isUserSignedOut() {
		return this.isSignInLinkPresent();
	}

	/**
	 * To Signout from application
	 */
	public void signOut() {
		if (isElementPresent(signout) && !isElementPresent(signIn)) {
			this.signout.click();
			waitForElementPresent(this.signIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To Refresh Xfinity Shop And Upgrade
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
	}
	
	/**
	 * To get URL of Xfinity Shop And Upgrade page
	 * 
	 * @return URL of Xfinity Shop And Upgrade page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.SHOP_OR_UPGRADE.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityShopAndUpgrade.class);
}

