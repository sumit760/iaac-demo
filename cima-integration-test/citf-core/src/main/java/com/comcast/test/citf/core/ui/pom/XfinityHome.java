package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for Xfinity Home web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class XfinityHome extends SeleniumPageObject<XfinityHome> {
	
	/* Header */
	
	@FindBy(how = How.XPATH, using = "//div[@class='search-area']/h1")
	public WebElement xfinityLogo;
	
	/* Navigation Menu Items */
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-authenticated-upgrade']/span")
	public WebElement navUpgradeSignedIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-recognized-upgrade']/span")
	public WebElement navUpgradeNoSignIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-authenticated-support']/span")
	public WebElement navSupportSignedIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-recognized-support']/span")
	public WebElement navSupportNoSignIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-authenticated-myaccount']/span")
	public WebElement navMyAccountSignedIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-recognized-myaccount']/span")
	public WebElement navMyAccountNoSignIn;
	
	@FindBy(how = How.ID, using = "polaris-header-main-navigation-tv")
	public WebElement navTV;
	
	@FindBy(how = How.ID, using = "polaris-header-personal-information-email")
	public WebElement navEmail;
	
	@FindBy(how = How.ID, using = "polaris-header-main-navigation-more")
	public WebElement navMore;
	
	/* Items within More */
	@FindBy(how = How.ID, using = "polaris-header-more-bill")
	public WebElement navPayBillsInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-guide")
	public WebElement navGuideInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-saved")
	public WebElement navSavedInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-ond")
	public WebElement navOnDemandInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-constantguard")
	public WebElement navConstantGuardInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-storelocator")
	public WebElement storeLocatorInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-toolbar")
	public WebElement navToolbarInMore;
	
	@FindBy(how = How.ID, using = "polaris-header-more-button")
	public WebElement navMoreFromXfn;
	
	
	/* Sign In and Sign out */
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-header']/div/ul[2]/li[5]/a")
	public WebElement signIn;
	
	@FindBy(how = How.XPATH, using = "//a[@id='polaris-header-main-navigation-profile']/div/span[4]")
	public WebElement signInGreeting;
	
	@FindBy(how = How.ID, using = "polaris-header-main-navigation-signout-standalone")
	public WebElement signout;
	
	
	/* Help & Support Section */
	/* This section tests the validity of the links by opening them and does not verify SSO/SLO */
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[1]/ul/li[1]/a")
	public WebElement setUpRemoteControl;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[1]/ul/li[2]/a")
	public WebElement setUpWireless;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[1]/ul/li[3]/a")
	public WebElement comcastAlerts;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[1]/ul/li[4]/a")
	public WebElement dashboardForXfnTv;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[1]/ul/li[5]/a")
	public WebElement changeOrResetPassword;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[1]/a")
	public WebElement payBill;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[2]/a")
	public WebElement findServiceCenter;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[3]/a")
	public WebElement checkForOutages;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[4]/a")
	public WebElement resetPassword;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[5]/a")
	public WebElement findUsername;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[2]/ul/li[6]/a")
	public WebElement askComcastCommunity;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[3]/ul/li[1]/a")
	public WebElement selfService;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[3]/ul/li[2]/a")
	public WebElement internet;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[3]/ul/li[3]/a")
	public WebElement billing;
	
	@FindBy(how = How.XPATH, using = "//section[@id='support-band']/div/div[2]/div[3]/ul/li[4]/a")
	public WebElement tv;
	
	
	/* I want To Section */
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[1]/a")
	private WebElement viewAndPayMyBill;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[5]/a")
	private WebElement watchTVOnline;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[9]/a")
	private WebElement manageParentalControl;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[13]/a")
	private WebElement findMyAccountNumber;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[17]/a")
	private WebElement manageMyDVR;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[21]/a")
	private WebElement programMyRemote;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[2]/a")
	public WebElement checkEmailAndVoiceMail;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[6]/a")
	private WebElement contactCustomerSupport;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[10]/a")
	private WebElement downloadConstantGuard;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[14]/a")
	private WebElement monitorHomeSecurityControl;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[18]/a")
	private WebElement upgradeMyService;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[22]/a")
	private WebElement submitFeedback;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[3]/a")
	private WebElement manageMyAccount;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[7]/a")
	private WebElement getApps;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[11]/a")
	private WebElement manageUsersAndAlerts;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[15]/a")
	private WebElement purchaseAccessories;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[4]/a")
	private WebElement checkTVListings;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[8]/a")
	private WebElement checkLocalNewsAndWeather;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[12]/a")
	private WebElement resetMyPassword;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[16]/a")
	private WebElement purchaseAMovie;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[20]/a")
	private WebElement getHelpAndSupport;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[1]/div[1]/ul/li[24]/a")
	private WebElement findXfinityStore;
	
	
	
	/* Footer Section */
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[2]/ul/li[2]/a")
    public WebElement privacyPolicy;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[2]/ul/li[3]/a")
	public WebElement termsOfService;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[1]/div/a/img")
	public WebElement footerComcastImage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[3]/ul/li[1]/a/img")
	public WebElement footerTwitterImage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[3]/ul/li[2]/a/img")
	public WebElement footerYouTubeImage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-footer']/div/div[2]/div/div[3]/ul/li[3]/a/img")
	public WebElement footerFacebookImage;
	
	
	public XfinityHome(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Access My Account | Email | Online News"));
	}

	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Xfinity home. ", e);
		}
	}
	

	/* Check presence of Elements in top nav */
	
	/**
	 * Checks whether Xfinity logo present
	 * 
	 * @return TRUE if page contains Xfinity logo, else FALSE
	 */
	public boolean isXfinityLogoPresent() {
		return isElementPresent(this.xfinityLogo);
	}
	
	/**
	 * Checks whether upgrade link present
	 * 
	 * @return TRUE if page contains upgrade link, else FALSE
	 */
	public boolean isUpgradeLinkPresentInNav() {
		boolean present = false;
		if (isElementPresent(this.signIn)){
			present = isElementPresent(this.navUpgradeNoSignIn);
		} else if (isElementPresent(this.signInGreeting)){
			present = isElementPresent(this.navUpgradeSignedIn);
		}
		return present;
	}
	
	/**
	 * Checks whether support link present
	 * 
	 * @return TRUE if support link is present, else FALSE
	 */
	public boolean isSupportLinkPresentInNav() {
		boolean present = false;
		if (isElementPresent(this.signIn)){
			present = isElementPresent(this.navSupportNoSignIn);
		} else if (isElementPresent(this.signInGreeting)){
			present = isElementPresent(this.navSupportSignedIn);
		}		
		return present;
	}
	
	/**
	 * Checks whether My Account link present
	 * 
	 * @return TRUE if My Account link is present, else FALSE
	 */
	public boolean isMyAccountLinkPresentInNav() {
		boolean present = false;
		if (isElementPresent(this.signIn)){
			present = isElementPresent(this.navMyAccountNoSignIn);
		} else if (isElementPresent(this.signInGreeting)){
			present = isElementPresent(this.navMyAccountSignedIn);
		}
		return present;
	}
	
	/**
	 * Checks whether TV Go link present
	 * 
	 * @return TRUE if TV Go link is present, else FALSE
	 */
	public boolean isTVGoLinkPresentInNav() {
		return isElementPresent(this.navTV);
	}
	
	/**
	 * Checks whether Email link present
	 * 
	 * @return TRUE if Email link is present, else FALSE
	 */
	public boolean isEmailLinkPresentInNav() {
		return isElementPresent(this.navEmail);
	}
	
	/**
	 * Checks whether More link present
	 * 
	 * @return TRUE if More link is present, else FALSE
	 */
	public boolean isMoreLinkPresentInNav() {
		return isElementPresent(this.navMore);
	}
	
	/**
	 * Checks whether Pay Bill link present
	 * 
	 * @return TRUE if Pay Bill link is present, else FALSE
	 */
	public boolean isPayBillsLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navPayBillsInMore);
	}
	
	/**
	 * Checks whether Guide link present
	 * 
	 * @return TRUE if Guide link is present, else FALSE
	 */
	public boolean isGuideLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navGuideInMore);
	}
	
	/**
	 * Checks whether Save link present
	 * 
	 * @return TRUE if Save link is present, else FALSE
	 */
	public boolean isSavedLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navSavedInMore);
	}
	
	/**
	 * Checks whether On Demand link present
	 * 
	 * @return TRUE if On Demand link is present, else FALSE
	 */
	public boolean isOnDemandLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navOnDemandInMore);
	}
	
	/**
	 * Checks whether Constant Guard link present
	 * 
	 * @return TRUE if Constant Guard link is present, else FALSE
	 */
	public boolean isConstantGuardLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navConstantGuardInMore);
	}
	
	/**
	 * Checks whether Toolbar link present
	 * 
	 * @return TRUE if Toolbar link is present, else FALSE
	 */
	public boolean isStoreLocatorPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.storeLocatorInMore);
	}
	
	/**
	 * Checks whether Toolbar link present
	 * 
	 * @return TRUE if Toolbar link is present, else FALSE
	 */
	public boolean isToolbarLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navToolbarInMore);
	}
	
	/**
	 * Checks whether More from Xfinity link present
	 * 
	 * @return TRUE if More from Xfinity link is present, else FALSE
	 */
	public boolean isMoreFromXfinityLinkPresentInMore() {
		Actions actions = new Actions(driver);
		actions.moveToElement(this.navMore);
		actions.perform();
		return isElementPresent(this.navMoreFromXfn);
	}
	
	/**
	 * Checks whether Sign In link present
	 * 
	 * @return TRUE if Sign In link is present, else FALSE
	 */
	public boolean isSignInLinkPresent() {
		return isElementPresent(this.signIn);
	}
	
	/**
	 * Checks whether Sign Out link present
	 * 
	 * @return TRUE if Sign Out link is present, else FALSE
	 */
	public boolean isSignOutLinkPresent() {
		return isElementPresent(this.signInGreeting);
	}
	
	/**
	 * Checks whether User has Signed In
	 * 
	 * @return TRUE if Sign Out link is present, else FALSE
	 */
	public boolean isUserSignedIn(){
		return this.isSignOutLinkPresent();
	}
	
	/**
	 * Checks whether User has Signed out
	 * 
	 * @return TRUE if Sign In link is present, else FALSE
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	
	/* Check presence of Elements in Help & Support */
	
	/**
	 * Checks whether Setup remote control link present
	 * 
	 * @return TRUE if Setup remote control link is present, else FALSE
	 */
	public boolean isSetUpRemoteControlLinkPresent() {
		return isElementPresent(this.setUpRemoteControl);
	}
	
	/**
	 * Checks whether Accessing and SetUp WG link present
	 * 
	 * @return TRUE if Accessing and SetUp WG link is present, else FALSE
	 */
	public boolean isAccessingAndSetUpWGLinkPresent() {
		return isElementPresent(this.setUpWireless);
	}
	
	/**
	 * Checks whether Stay informed with Comcast alerts link present
	 * 
	 * @return TRUE if Stay informed with Comcast alerts link is present, else FALSE
	 */
	public boolean isStayInformedWithComcastAlertsLinkPresent() {
		return isElementPresent(this.comcastAlerts);
	}
	
	/**
	 * Checks whether dashboard for Xfinity TV link present
	 * 
	 * @return TRUE if dashboard for Xfinity TV link is present, else FALSE
	 */
	public boolean isDashboardForXfinityTVLinkPresent() {
		return isElementPresent(this.dashboardForXfnTv);
	}
	
	/**
	 * Checks whether Change Reset Password TV link present
	 * 
	 * @return TRUE if Change Reset Password link is present, else FALSE
	 */
	public boolean isChangeResetPasswordLinkPresent() {
		return isElementPresent(this.changeOrResetPassword);
	}
	
	/**
	 * Checks whether Pay Bill link present
	 * 
	 * @return TRUE if Pay Bill link is present, else FALSE
	 */
	public boolean isPayBillLinkPresent() {
		return isElementPresent(this.payBill);
	}
	
	/**
	 * Checks whether find a service link present
	 * 
	 * @return TRUE if find a service link is present, else FALSE
	 */
	public boolean isFindAServiceCenter() {
		return isElementPresent(this.findServiceCenter);
	}
	
	/**
	 * Checks whether Check for Outages link present
	 * 
	 * @return TRUE if Check for Outages link is present, else FALSE
	 */
	public boolean isCheckForOutagesLinkPresent() {
		return isElementPresent(this.checkForOutages);
	}
	
	/**
	 * Checks whether Check for Outages link present
	 * 
	 * @return TRUE if Check for Outages link is present, else FALSE
	 */
	public boolean isResetYourPasswordLinkPresent() {
		return isElementPresent(this.resetPassword);
	}
	
	/**
	 * Checks whether Find username link present
	 * 
	 * @return TRUE if  Find username link is present, else FALSE
	 */
	public boolean isFindUsernameLinkPresent() {
		return isElementPresent(this.findUsername);
	}
	
	/**
	 * Checks whether Ask Comcast Community link present
	 * 
	 * @return TRUE if Ask Comcast Community link is present, else FALSE
	 */
	public boolean isAskComcastCommunityLinkPresent() {
		return isElementPresent(this.askComcastCommunity);
	}
	
	/**
	 * Checks whether Ask Comcast Community link present
	 * 
	 * @return TRUE if Ask Comcast Community link is present, else FALSE
	 */
	public boolean isSelfServiceLinkPresent() {
		return isElementPresent(this.selfService);
	}
	
	/**
	 * Checks whether Internet link present
	 * 
	 * @return TRUE if Internet link is present, else FALSE
	 */
	public boolean isInternetLinkPresent() {
		return isElementPresent(this.internet);
	}
	
	/**
	 * Checks whether Internet link present
	 * 
	 * @return TRUE if Internet link is present, else FALSE
	 */
	public boolean isBillingLinkPresent() {
		return isElementPresent(this.billing);
	}
	
	/**
	 * Checks whether TV link present
	 * 
	 * @return TRUE if TV link is present, else FALSE
	 */
	public boolean isTVLinkPresent() {
		return isElementPresent(this.tv);
	}
	
	
	/* Check presence of Elements in I want To */
	
	/**
	 * Checks whether Watch TV Online link present
	 * 
	 * @return TRUE if Watch TV Online is present, else FALSE
	 */
	public boolean isWatchTVOnlineLinkPresent() {
		return isElementPresent(this.watchTVOnline);
	}
	
	/**
	 * Checks view and pay my bill link is present
	 * 
	 * @return TRUE if view and pay my bill link is present, else FALSE
	 */
	public boolean isViewAndPayMyBillLinkPresent() {
		return isElementPresent(this.viewAndPayMyBill);
	}
	
	/**
	 * Checks whether Manage Parental Control link present
	 * 
	 * @return TRUE if Manage Parental Control link is present, else FALSE
	 */
	public boolean isManageParentalControlLinkPresent() {
		return isElementPresent(this.manageParentalControl);
	}
	
	/**
	 * Checks whether Find My Account Number link present
	 * 
	 * @return TRUE if Find My Account Number link is present, else FALSE
	 */
	public boolean isFindMyAccountNumberLinkPresent() {
		return isElementPresent(this.findMyAccountNumber);
	}
	
	/**
	 * Checks whether Program My Remote link present
	 * 
	 * @return TRUE if Program My Remote link is present, else FALSE
	 */
	public boolean isProgramMyRemoteLinkPresent() {
		return isElementPresent(this.programMyRemote);
	}
	
	/**
	 * Checks whether Manage My DVR link present
	 * 
	 * @return TRUE if Manage My DVR link is present, else FALSE
	 */
	public boolean isManageMyDVRLinkPresent() {
		return isElementPresent(this.manageMyDVR);
	}
	
	/**
	 * Checks whether Email And Voice Mail link present
	 * 
	 * @return TRUE if Email And Voice Mail link is present, else FALSE
	 */
	public boolean isEmailAndVoiceMailLinkPresent() {
		return isElementPresent(this.checkEmailAndVoiceMail);
	}
	
	/**
	 * Checks whether Contact Customer Support link present
	 * 
	 * @return TRUE if Contact Customer Support link is present, else FALSE
	 */
	public boolean isContactCustomerSupportLinkPresent() {
		return isElementPresent(this.contactCustomerSupport);
	}
	
	/**
	 * Checks whether Download Constant Guard link present
	 * 
	 * @return TRUE if Download Constant Guard link is present, else FALSE
	 */
	public boolean isDownloadConstantGuardLinkPresent() {
		return isElementPresent(this.downloadConstantGuard);
	}
	
	/**
	 * Checks whether Monitor Home Security And Control link present
	 * 
	 * @return TRUE if Monitor Home Security And Control link is present, else FALSE
	 */
	public boolean isMonitorHomeSecurityAndControlLinkPresent() {
		return isElementPresent(this.monitorHomeSecurityControl);
	}
	
	/**
	 * Checks whether Upgrade My Service link present
	 * 
	 * @return TRUE if Upgrade My Service link is present, else FALSE
	 */
	public boolean isUpgradeMyServiceLinkPresent() {
		return isElementPresent(this.upgradeMyService);
	}
	
	/**
	 * Checks whether submit feedback link present
	 * 
	 * @return TRUE if submit feedback link is present, else FALSE
	 */
	public boolean isSubmitFeedbackLinkPresent() {
		return isElementPresent(this.submitFeedback);
	}
	
	/**
	 * Checks whether Manage My Account link present
	 * 
	 * @return TRUE if Manage My Account link is present, else FALSE
	 */
	public boolean isManageMyAccountLinkPresent() {
		return isElementPresent(this.manageMyAccount);
	}
	
	/**
	 * Checks whether Get Apps link present
	 * 
	 * @return TRUE if Get Apps link is present, else FALSE
	 */
	public boolean isGetAppsLinkPresent() {
		return isElementPresent(this.getApps);
	}
	
	/**
	 * Checks whether Manage Users And Alerts link present
	 * 
	 * @return TRUE if Manage Users And Alerts link is present, else FALSE
	 */
	public boolean isManageUsersAndAlertsLinkPresent() {
		return isElementPresent(this.manageUsersAndAlerts);
	}
	
	/**
	 * Checks whether Purchase accessories link present
	 * 
	 * @return TRUE if Purchase accessories link is present, else FALSE
	 */
	public boolean isPurchaseAccessoriesLinkPresent() {
		return isElementPresent(this.purchaseAccessories);
	}
	
	/**
	 * Checks whether TV Listings link present
	 * 
	 * @return TRUE if TV Listings link is present, else FALSE
	 */
	public boolean isTVListingsLinkPresent() {
		return isElementPresent(this.checkTVListings);
	}
	
	/**
	 * Checks whether local new and weather link present
	 * 
	 * @return TRUE if local new and weather link is present, else FALSE
	 */
	public boolean isLocalNewsAndWeatherLinkPresent() {
		return isElementPresent(this.checkLocalNewsAndWeather);
	}
	
	/**
	 * Checks whether reset my password link present
	 * 
	 * @return TRUE if reset my password link is present, else FALSE
	 */
	public boolean isResetMyPasswordLinkPresent() {
		return isElementPresent(this.resetMyPassword);
	}
	
	/**
	 * Checks whether purchase a movie link present
	 * 
	 * @return TRUE if purchase a movie link is present, else FALSE
	 */
	public boolean isPurchaseAMovieLinkPresent() {
		return isElementPresent(this.purchaseAMovie);
	}
	
	/**
	 * Checks whether Get Help And Support link present
	 * 
	 * @return TRUE if Get Help And Support link is present, else FALSE
	 */
	public boolean isGetHelpAndSupportLinkPresent() {
		return isElementPresent(this.getHelpAndSupport);
	}
	
	/**
	 * Checks whether find xfinity store link present
	 * 
	 * @return TRUE if find xfinity storet link is present, else FALSE
	 */
	public boolean isFindXfinityStoreLinkPresent() {
		return isElementPresent(this.findXfinityStore);
	}
	
	
	/* Check presence of Elements in the footer */
	/**
	 * Checks whether Privacy Policy link present
	 * 
	 * @return TRUE if Privacy Policy link is present, else FALSE
	 */
	public boolean isPrivacyPolicyLinkPresent() {
		return isElementPresent(this.privacyPolicy);
	}
	
	/**
	 * Checks whether Terms of Service link present
	 * 
	 * @return TRUE if Terms of Service link is present, else FALSE
	 */
	public boolean isTermsofServiceLinkPresent() {
		return isElementPresent(this.termsOfService);
	}
	
	/**
	 * Checks whether footer Comcast image is present
	 * 
	 * @return TRUE if footer Comcast image is present, else FALSE
	 */
	public boolean isFooterComcastLinkPresent() {
		return isElementPresent(this.footerComcastImage);
	}
	
	/**
	 * Checks whether footer twitter image is present
	 * 
	 * @return TRUE if footer twitter image is present, else FALSE
	 */
	public boolean isFooterTwitterLinkPresent() {
		return isElementPresent(this.footerTwitterImage);
	}
	
	/**
	 * Checks whether footer YouTube image is present
	 * 
	 * @return TRUE if footer YouTube image is present, else FALSE
	 */
	public boolean isFooterYouTubeLinkPresent() {
		return isElementPresent(this.footerYouTubeImage);
	}
	
	/**
	 * Checks whether footer Facebook image is present
	 * 
	 * @return TRUE if footer Facebook image is present, else FALSE
	 */
	public boolean isFooterFacebookLinkPresent() {
		return isElementPresent(this.footerFacebookImage);
	}
	
	/* validation methods to verify section wise page load status */
	
	/**
	 * Checks if top navigation loaded successfully
	 * 
	 * @return TRUE if top navigation loaded successfully, else FALSE
	 */
	public boolean isTopNavigationLoadedSuccessfully() {
		boolean otherLinksStatus = isUpgradeLinkPresentInNav() && 
								   isSupportLinkPresentInNav() &&
								   isMyAccountLinkPresentInNav() &&
								   isTVGoLinkPresentInNav() &&
								   isEmailLinkPresentInNav() &&
								   isXfinityLogoPresent();
		boolean moreElementsLocated = false;
		if (isMoreLinkPresentInNav()) {
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore);
			actions.perform();
			moreElementsLocated = isPayBillsLinkPresentInMore() &&
								  isGuideLinkPresentInMore() &&
								  isSavedLinkPresentInMore() &&
								  isOnDemandLinkPresentInMore() &&
								  isConstantGuardLinkPresentInMore() &&
								  isStoreLocatorPresentInMore() &&
								  isMoreFromXfinityLinkPresentInMore();
		}
		
		return otherLinksStatus && moreElementsLocated && (isSignInLinkPresent() || isSignOutLinkPresent());
		
	}
	
	/**
	 * Checks if Help And Support loaded successfully
	 * 
	 * @return TRUE if Help And Support loaded successfully, else FALSE
	 */
	public boolean isHelpAndSupportLoadedSuccessfully() {
		return 	isSetUpRemoteControlLinkPresent() && 
				isAccessingAndSetUpWGLinkPresent() &&
				isStayInformedWithComcastAlertsLinkPresent() && 
				isDashboardForXfinityTVLinkPresent() &&
				isChangeResetPasswordLinkPresent() && 
				isPayBillLinkPresent() && 
				isFindAServiceCenter() &&
				isCheckForOutagesLinkPresent() &&
				isResetYourPasswordLinkPresent() &&
				isFindUsernameLinkPresent() &&
				isAskComcastCommunityLinkPresent() &&
				isSelfServiceLinkPresent() &&
				isInternetLinkPresent() &&
				isBillingLinkPresent() &&
				isTVLinkPresent();
	}
	
	
	/**
	 * Checks if I want to section is loaded successfully
	 * 
	 * @return TRUE I want to section is loaded successfully, else FALSE
	 */
	public boolean isIWantToSectionLoadedSuccessfully() {
		return 	isWatchTVOnlineLinkPresent() &&
				isViewAndPayMyBillLinkPresent() &&
				isManageParentalControlLinkPresent() &&
				isFindMyAccountNumberLinkPresent() &&
				isManageMyDVRLinkPresent() &&
				isProgramMyRemoteLinkPresent() &&
				isEmailAndVoiceMailLinkPresent() &&
				isContactCustomerSupportLinkPresent() &&
				isDownloadConstantGuardLinkPresent() &&
				isMonitorHomeSecurityAndControlLinkPresent() &&
				isUpgradeMyServiceLinkPresent() &&
				isSubmitFeedbackLinkPresent() &&
				isManageMyAccountLinkPresent() &&
				isGetAppsLinkPresent() &&
				isManageUsersAndAlertsLinkPresent() &&
				isPurchaseAccessoriesLinkPresent() &&
				isTVListingsLinkPresent() &&
				isLocalNewsAndWeatherLinkPresent() &&
				isResetMyPasswordLinkPresent() &&
				isPurchaseAMovieLinkPresent() &&
				isGetHelpAndSupportLinkPresent();
	}
	
	
	public boolean isFooterSectionLoadedSuccessfully() {
		return isPrivacyPolicyLinkPresent() &&
			   isTermsofServiceLinkPresent() &&
			   isFooterTwitterLinkPresent() &&
			   isFooterYouTubeLinkPresent() &&
			   isFooterFacebookLinkPresent();
	}
	
	
	
	/* getPages methods for the transition elements */
	
	/* Action methods for elements in top nav */
	
	/**
	 * Provides Page Upgrade page object
	 * 
	 * @return Page object for Xfinity Page Upgrade
	 */
	public Object getPageUpgradeFromNav(){
		boolean signedIn = false;
		if (isUpgradeLinkPresentInNav()){
			if (isUserSignedIn()) {
				signedIn = true;
				this.navUpgradeSignedIn.click();
			} else {
				this.navUpgradeNoSignIn.click();
			}
			switchToChildWindow(this.driver);
			if (signedIn){
				waitForLinkedText("Sign Out",ICommonConstants.WAIT_TIMEOUT);
			} else{
				waitForLinkedText("Sign In", ICommonConstants.WAIT_TIMEOUT);
			}
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Help and Support page object
	 * 
	 * @return Page Object for Xfinity Help and Support
	 */
	public Object getPageSupportFromNav(){
		if (isSupportLinkPresentInNav()){
			if (isUserSignedIn()) {
				this.navSupportSignedIn.click();
			} else {
				this.navSupportNoSignIn.click();
			}
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	
	/**
	 * Provides My Account page object
	 * 
	 * @return Page Object for Xfinity My Account
	 */
	public Object getPageMyaccount(){
		if(isMyAccountLinkPresentInNav()){
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,-250)", "");
			if (isUserSignedIn()) {
				this.navMyAccountSignedIn.click();
			} else {
				this.navMyAccountNoSignIn.click();
			}
			
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides TV Go page object
	 * 
	 * @return Page Object for Xfinity TV Go
	 */
	public Object getPageTVGo(){
		boolean signedIn = false;
		if (isTVGoLinkPresentInNav()){
			if (isUserSignedIn()){
				signedIn = true;
			}
			
			this.navTV.click();
			switchToChildWindow(this.driver);
			
			if (signedIn){
				waitForLinkedText("Sign Out", ICommonConstants.WAIT_TIMEOUT);
			} else{
				waitForLinkedText("Sign In", ICommonConstants.WAIT_TIMEOUT);
			}
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}

		return null;
	}
	
	/**
	 * Provides Email page object
	 * 
	 * @return Page Object for Xfinity Email and Voice
	 */
	public Object getPageEmailNav(){
		if (isEmailLinkPresentInNav())	{
			this.navEmail.click();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			waitForFrame("xcnavbar", ICommonConstants.WAIT_TIMEOUT);
			waitForElementByXPATH("//a[@id='galactic-header-personal-information-email']/span[1]", 
								   ICommonConstants.WAIT_TIMEOUT);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Signs In
	 * 
	 * @return Next Page Object
	 */
	public Object signIn(){
		if (isElementPresent(this.signIn) && !isElementPresent(this.signInGreeting))	{
			this.signIn.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}

		return null;
	}
	
	/**
	 * Provides Page Guide from more page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageGuideFromMoreInNav() {
		if (isGuideLinkPresentInMore()){
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore).moveToElement(this.navGuideInMore).click().perform();
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Page Saved from more page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageSavedFromMoreInNav(){
		if (isSavedLinkPresentInMore()){
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore).moveToElement(this.navSavedInMore).click().perform();
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Page On Demand from more page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageOnDemandFromMoreInNav(){
		if (isOnDemandLinkPresentInMore()){
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore).moveToElement(this.navOnDemandInMore).click().perform();
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Page Constant Guard from more page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageConstantGuardFromMoreInNav(){
		if (isConstantGuardLinkPresentInMore()){
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore).moveToElement(this.navConstantGuardInMore).click().perform();
			waitForPageLoaded(this.driver);

			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Page Toolbar from more page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageStoreLocator(){
		if (isStoreLocatorPresentInMore()){
			Actions actions = new Actions(driver);
			actions.moveToElement(this.navMore).moveToElement(this.storeLocatorInMore).click().perform();
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	
	
	/* Action methods for elements under "I Want To" */
	
	/**
	 * Provides Watch TV Online page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageWatchTVOnline(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isWatchTVOnlineLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.watchTVOnline).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.watchTVOnline).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);

			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Parental Control page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageParentalControl(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isManageParentalControlLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.manageParentalControl).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.manageParentalControl).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);

			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Find Account Number page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageFindAccountNumber(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isFindMyAccountNumberLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.findMyAccountNumber).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.findMyAccountNumber).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides DVR Manager page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageManageDVR(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isManageMyDVRLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.manageMyDVR).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.manageMyDVR).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		
		return null;
	}
	
	/**
	 * Provides Email and Voice Mail page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageEmailAndVoiceMail(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isEmailAndVoiceMailLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.checkEmailAndVoiceMail).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.checkEmailAndVoiceMail).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Customer Support page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageCustomerSupport(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isContactCustomerSupportLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.contactCustomerSupport).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.contactCustomerSupport).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}

	/**
	 * Provides Constant Guard page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageConstantGuard(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isDownloadConstantGuardLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.downloadConstantGuard).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.downloadConstantGuard).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Home Security And Control page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageHomeSecurityAndControl(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isMonitorHomeSecurityAndControlLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.monitorHomeSecurityControl).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.monitorHomeSecurityControl).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Upgrade My Service page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageUpgradeMyService(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isUpgradeMyServiceLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.upgradeMyService).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.upgradeMyService).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Manage My Account page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageManageMyAccount(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isManageMyAccountLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.manageMyAccount).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.manageMyAccount).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Apps page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageApps(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isGetAppsLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.getApps).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.getApps).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Users And Alerts page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageUsersAndAlerts(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isManageUsersAndAlertsLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.manageUsersAndAlerts).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.manageUsersAndAlerts).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides TV Listings page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageTVListings(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isTVListingsLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.checkTVListings).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.checkTVListings).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Purchase Movie page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPagePurchaseMovie(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isPurchaseAMovieLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.purchaseAMovie).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.purchaseAMovie).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Help And Support page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageHelpAndSupport(){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		if (isGetHelpAndSupportLinkPresent()){
			Actions act = new Actions(driver);
			act.moveToElement(this.getHelpAndSupport).perform();
			jse.executeScript("window.scrollBy(0,-250)", "");
			act.contextClick(this.getHelpAndSupport).perform();
			act.sendKeys("w").perform();
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Privacy Policy page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPagePrivacyPolicy(){
		if (isPrivacyPolicyLinkPresent()){
			Actions act = new Actions(driver);
			act.contextClick(this.privacyPolicy).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Provides Terms Of Service page object
	 * 
	 * @return Next Page Object
	 */
	public Object getPageTermsOfService(){
		if (isTermsofServiceLinkPresent()){
			Actions act = new Actions(driver);
			act.contextClick(this.termsOfService).perform();
			act.sendKeys("w").perform();
			switchToChildWindow(this.driver);
			waitForPageLoaded(this.driver);
			
			return ObjectInitializer.getPageNavigator().getNextPage(switchToChildWindow(driver), this, this.getPageFlowId());
		}
		return null;
	}
	
	/**
	 * Signs out
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			Actions actions = new Actions(driver);
			actions.moveToElement(this.signInGreeting).moveToElement(this.signout).click().perform();
			
			waitForLinkedText("Sign In",ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * Refreshes page
	 */
	public void refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
	}
	
	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(EndPoinUrlProvider.LoginUrlPropKeys.XFINITY_HOME.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityHome.class);
}
