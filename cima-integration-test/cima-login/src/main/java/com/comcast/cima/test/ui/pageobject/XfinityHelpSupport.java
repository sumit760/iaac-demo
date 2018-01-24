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
 * This is Selenium Page Object for "Xfinity Help Support" web page.
 * 
 * @author Sumit Pal and shailesh suman
 */

public class XfinityHelpSupport extends SeleniumPageObject<XfinityHelpSupport> { 
		
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement signIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement signout;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-tv")
	public WebElement header;
	
	private final static String frameId = "polarisnav";
	
	public XfinityHelpSupport(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void load(){
		try{
			this.driver.get(getURLToLoad());
		
		}catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity help and support page ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error{
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Comcast Customer Service"));		
	}

	/**
	 * Check whether is signout link is present
	 * 
	 * @return True if user is signout link present, else False
	 */
	public Boolean isSignoutLinkPresent(){
		switchToDefaultFrame(driver);
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(signout);
	}
	
	/**
	 * Check whether is signin link is present
	 * 
	 * @return True if user is signin link present, else False
	 */
	public Boolean isSignInLinkPresent() {
		switchToFrame(driver, this.frameId);
		return this.isElementPresent(signIn);
	}
	
	/**
	 * To click signin link
	 * 
	 * @return object of xfinity signin page or object of xfinity Help Support
	 * @throws Exception
	 */
	public Object getPageXfinitySignIn() throws Exception{
		if (isElementPresent(this.signIn)) {
			switchToFrame(driver, this.frameId);
			this.signIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}
	
	
	/**
	 * To click signin link
	 */
	public void clickSignIn() {
		if (isElementPresent(signIn)) {
			switchToFrame(driver, this.frameId);
			this.signIn.click();
			waitForElementPresent(this.signout,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To check whether user is signed in
	 * @return True if signed in else False
	 */
	
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To check whether user is signed out
	 * @return True if signed out else False
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	/**
	 * To logout from application
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			switchToFrame(driver, this.frameId);
			this.signout.click();
			waitForElementPresent(this.signIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh xfinity Support page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	/**
	 * To get URL of Xfinity Help and Support page
	 * 
	 * @return URL of Xfinity Help and Support page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.HELP_AND_SUPPORT.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityHelpSupport.class);
}
