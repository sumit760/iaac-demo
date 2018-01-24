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
 * This is Selenium Page Object for "Xfinity Email And Voice" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinityEmailAndVoice extends SeleniumPageObject<XfinityEmailAndVoice> {
	
		
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	private final static String frameId = "xcnavbar";
	
	
	public XfinityEmailAndVoice(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading xfinity email page. ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("XFINITY Connect") || title.startsWith("Sign in"));
	}
	
	/* Methods for actions */
	
	/**
	 * To check whether user is signed in
	 * 
	 * @return True if user is signed in, else False
	 */
	public boolean isUserSignedIn()	{
		switchToFrame(driver, frameId);
		
		return isElementPresent(this.Signout);
	}
	
	/**
	 * Check whether user is signed out
	 * 
	 * @return True if user is signed out, else False
	 */
	
	public boolean isUserSignedOut()	{
		switchToDefaultFrame(driver);
		switchToFrame(driver, frameId);
		
		return isElementPresent(this.SignIn);
	}
	
	/**
	 * To logout from application
	 */
	public void signOut(){
		if (isUserSignedIn()) {
			this.Signout.click();
			waitForLinkedText("Sign In",ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * To refresh xfinity email and invoice page.
	 * 
	 * @return Object of next page
	 */
	public Object refreshPage() throws Exception {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
		
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
	}
	
	
	/**
	 * To get URL of email and invoice page
	 * 
	 * @return URL of email and invoice page
	 * @throws Exception
	 */	
	
	private String getURLToLoad() throws Exception{
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.EMAIL.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityEmailAndVoice.class);
}

