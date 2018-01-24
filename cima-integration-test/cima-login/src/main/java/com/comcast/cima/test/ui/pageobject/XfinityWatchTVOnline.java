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
 * This is Selenium Page Object for "Xfinity Watch TV Online" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */


public class XfinityWatchTVOnline extends SeleniumPageObject<XfinityWatchTVOnline>  {

	/*Elements definitions */
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	@FindBy(how = How.XPATH, using = "//body[@id='cmshome']/nav/ul/li[1]/a")
	public WebElement watchOnlineTab;
		
	
	public XfinityWatchTVOnline(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading xfinity online TV page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Watch TV Online"));
				
	}
	
	
	
	/*methods to check presence of element on shop and upgrade page  */
	
	
	/**
	 * Check whether signout link is present
	 * 
	 * @return True if signout link is present, else False
	 */
	
	public boolean isSignoutLinkPresent() {
		return this.isElementPresent(Signout);
	}
	
	
	/**
	 * Check whether signin link is present
	 * 
	 * @return True if signinlink is present, else False
	 */
	public boolean isSignInLinkPresent() {
		return this.isElementPresent(SignIn);
	}
	
	/**
	 * To Click on SignIn Link
	 * 
	 * @return Next page object
	 * @throws Exception
	 */
	public Object getPageXfinitySignIn() throws Exception{
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
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
	 * 
	 * @return True if Signed out else False
	 */
	
	public boolean isUserSignedOut() {
		return this.isSignInLinkPresent();
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
	
	/**
	 * To Refresh page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	/**
	 * To get URL of  Xfinity Watch TV Online page
	 * 
	 * @return URL of Xfinity Watch TV Online page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.TV.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityWatchTVOnline.class);
}
