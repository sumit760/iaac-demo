package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Xfinity Customer Agreement" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityCustomerAgreement extends SeleniumPageObject<XfinityCustomerAgreement> {
	
	
	@FindBy(how = How.LINK_TEXT, using = "Sign In")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	@FindBy(how = How.XPATH, using = "//div[@id='urlContentPanel']/div[3]/div/div[1]/div/h1")
	public WebElement headerMessage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='urlContentPanel']/div[3]/div/div[1]/div/p[1]")
	public WebElement headerContent1;
	
	@FindBy(how = How.XPATH, using = "//div[@id='urlContentPanel']/div[3]/div/div[1]/div/p[2]")
	public WebElement headerContent2;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-1']/div[1]/div/a/h3")
	public WebElement overView;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-1']/div[1]/div/a/span")
	public WebElement overViewExpand;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-1']/div[2]/div/h4[1]")
	public WebElement networkPractice;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-1']/div[2]/div/h4[2]")
	public WebElement performanceCharacteristics;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-1']/div[2]/div/h4[3]")
	public WebElement commercialTerms;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[1]/a/h3")
	public WebElement xfinityResidentialServices;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div[1]/div/a/span")
	public WebElement xfinityResidentialServicesExpand;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[1]")
	public WebElement xfinityRSGeneral;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[2]")
	public WebElement xfinityRSXTV;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[3]")
	public WebElement xfinityRSXfVoice;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[4]")
	public WebElement xfinityRSXfInternet;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[5]")
	public WebElement xfinityRSXfWiFi;
	
	@FindBy(how = How.XPATH, using = "//div[@id='section-2']/div/div[2]/div/h4[6]")
	public WebElement xfinityRSXfHome;
	
	
	public XfinityCustomerAgreement(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void isLoaded() throws Error {		
	}

	@Override
	protected void load() {
	}
	
	/**
	 * Check whether page is loaded successfully
	 * 
	 * @return True if page loaded successfully, else False
	 */
	public boolean isPageLoadedSuccessfully() {
		boolean pageLoaded = false;
		if (!isElementPresent(this.headerMessage) ||
			!isElementPresent(this.headerContent1) ||
			!isElementPresent(this.headerContent2))
			return pageLoaded;
		
		if (isElementPresent(this.overView) && isElementPresent(this.overViewExpand) &&
			isElementPresent(this.xfinityResidentialServices) && isElementPresent(this.xfinityResidentialServicesExpand)) {
			this.overViewExpand.click();
			this.xfinityResidentialServicesExpand.click();
			if (isElementPresent(this.networkPractice) &&
				isElementPresent(this.performanceCharacteristics) &&
				isElementPresent(this.commercialTerms) &&
				isElementPresent(this.xfinityRSGeneral) &&
				isElementPresent(this.xfinityRSXfHome) &&
				isElementPresent(this.xfinityRSXfInternet) &&
				isElementPresent(this.xfinityRSXfVoice) &&
				isElementPresent(this.xfinityRSXfWiFi) &&
				isElementPresent(this.xfinityRSXTV)){
				
				pageLoaded = true;
			}
		}
		
		return pageLoaded;
	}
	
	/**
	 * Checks whether Signout Link is present
	 * 
	 * @return True if present, else False
	 */
	 
	public Boolean isSignoutLinkPresent() {
		return this.isElementPresent(Signout);
	}
	
	/**
	 * Checks whether SignIn Link is present
	 * 
	 * @return True if present, else False
	 */	
	public Boolean isSignInLinkPresent() {
		return this.isElementPresent(SignIn);
	}
	
	/**
	 * To click on signIn link to get SignIn xfinity page
	 * 
	 * @return Page object of Xfinity sign in page
	 */	
	public Object getPageXfinitySignIn()  throws Exception{
		if (isElementPresent(this.SignIn)) {
			this.SignIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}

	/**
	 * Checks whether user is Signed in.
	 * 
	 * @return True if signed in, else False
	 */	
	
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * Checks whether user is SignedOut.
	 * 
	 * @return True if SignedOut, else False
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
	
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
}
