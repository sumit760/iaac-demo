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
 * This is Selenium Page Object for "Xfinity Publish" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityPublish extends SeleniumPageObject<XfinityPublish> {
	
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	@FindBy(how = How.LINK_TEXT, using = "Click here")
	public WebElement ClickToLogin;
	
	@FindBy(how = How.ID, using = "user")
	private WebElement Username;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement Password;
	
		
	public XfinityPublish(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
		
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity Publish page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("PWP") || title.startsWith("Sign in"));
	}

	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */
	public Boolean isSignoutLinkPresent() {
		return this.isElementPresent(Signout);
	}
	
	/**
	 * To Enter password
	 * 
	 * @param passwd:
	 *              Password
	 */
	public void setPassword(String passwd) {
		this.clearAndType(Password, passwd);
	}
	
	/**
	 * To Navigate to home page
	 */
	public void navigateToHomepage(){
		this.ClickToLogin.click();
	}
	
	/**
	 * To check whether User is Signed In
	 * 
	 * @return True if Signed in else False
	 */
	public boolean isUserSignedIn() {
		return this.isElementPresent(Signout);
	}
	
	/**
	 * To Signout from application
	 */
	public void signOut() {
		this.Signout.click();
	}
	
	/**
	 * To get URL of Xfinity Publish page
	 * 
	 * @return URL of Xfinity Publish page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_PUBLISH.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityPublish.class);
	
}

