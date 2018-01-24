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
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * This is Selenium Page Object for "Xfinity Terms Of Service" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityTermsOfService extends SeleniumPageObject<XfinityTermsOfService>{

	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/h3")
	public WebElement headerMessage;
	
	@FindBy(how = How.LINK_TEXT, using = "Web Services Terms of Service")
	public WebElement wsTermsAndServices;
	
	@FindBy(how = How.LINK_TEXT, using = "Software License")
	public WebElement softwareLicense;
	
	@FindBy(how = How.LINK_TEXT, using = "Residential Services Policies")
	public WebElement residentialPolicies;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms-nav']/dl/dt/span/a[4]")
	public WebElement reportAbuse;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms-nav']/dl/dt/span/a[5]")
	public WebElement termsForFeedback;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms-nav']/dl/dt/span/a[6]")
	public WebElement mediaSharing;
	
	
	public XfinityTermsOfService(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
			
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity Terms of Service page ", e);
		}
	}


	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("XFINITY Terms Of Service"));
	}

	/**
	 * To Check whether pages is loaded successfully
	 * 
	 * @return True if Loaded else False
	 */
	public boolean isPageLoadedSuccessfully() {
		return (isElementPresent(this.headerMessage) && 
				isElementPresent(this.wsTermsAndServices) &&
				isElementPresent(this.softwareLicense) &&
				isElementPresent(this.residentialPolicies) &&
				isElementPresent(this.reportAbuse) &&
				isElementPresent(this.termsForFeedback) &&
				isElementPresent(this.mediaSharing));
	}
	
	
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
	 * To Click on Signin link
	 * 
	 * @return Next page Object
	 */
	public SignInToXfinity getPageXfinitySignIn() {
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			return (PageFactory.initElements(driver, SignInToXfinity.class));
		}
		return null;
	}

	/**
	 * To check whether User is Signed In
	 * 
	 * @return True if Signed in else False
	 */
	
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * To Check whether user is signed out
	 * 
	 * @return True if Signed out else False
	 */
	public boolean isUserSignedOut(){
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
	 * To regresh page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	/**
	 * To get URL of  Xfinity Terms Of Service page
	 * 
	 * @return URL of Xfinity Terms Of Service page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception{
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_TERMS_OF_SERVICE.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityTermsOfService.class);
}
