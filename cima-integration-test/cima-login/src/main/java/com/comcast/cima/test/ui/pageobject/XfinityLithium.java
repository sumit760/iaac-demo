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
 * This is Selenium Page Object for "Xfinity Lithium" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityLithium extends SeleniumPageObject<XfinityLithium> {
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signoutLink;
	
	@FindBy(how = How.XPATH, using = "//img[@class='lia-user-avatar-message']")
	public WebElement myAccountLink;	
	
		
	public XfinityLithium(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
		} catch (Exception e)	{
			LOGGER.error("Error occurred while loading xfinity lithium support forrum page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.endsWith("Support Forums") || title.startsWith("Sign in"));
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
	 * To check whether user is signed in
	 * 
	 * @return True if signed in else False
	 */	
	public boolean isUserSignedIn() {
		return this.isElementPresent(myAccountLink);
	}
	
	/**
	 * To logout from application
	 */
	public void SignOut() {
		this.myAccountLink.click();
		this.signoutLink.click();
	}
	
	/**
	 * To get URL of Xfinity Lithium page
	 * 
	 * @return URL of Xfinity Lithium page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_LITHIUM.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityLithium.class);
}

