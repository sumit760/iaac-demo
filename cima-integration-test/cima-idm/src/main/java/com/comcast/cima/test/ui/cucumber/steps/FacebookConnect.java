package com.comcast.cima.test.ui.cucumber.steps;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.fail;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.FacebookLoginPopUp;
import com.comcast.cima.test.ui.pageobject.FacebookXfinityLogin;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.XfinityUserprofile;
import com.comcast.cima.test.ui.pageobject.XfinityUserprofileSpanish;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.WebDriverUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.dataProvider.DataProviderToCreateUser;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.XfinityHome;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FacebookConnect {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pfId;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private AccountCache.Account account;
	private UserCache.User user;
	private UserAttributesCache.Attribute userAttributes;
	private Object pageObject;
	private FreshUsers freshUser;
	private String fbId;
	private String fbPassword;
    private String sauceBrowser;
    private String saucePlatform;
    
	@Autowired
	private IdmCommonSteps commonSteps;
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private FreshAccountProvider freshAccountProvider;
	
	@Autowired
	private DataProviderToCreateUser freshUserProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;

	@Autowired
	private ConfigurationDataProvider configDataProvider;
	
	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.pfId = null;
		this.sessionId = null;
		this.account = null;
		this.user = null;
		this.userAttributes = null;
		this.pageObject = null;
		this.freshUser = null;
		this.fbId = null;
		this.fbPassword = null;
	}
	
	@After
	public void tearDown() {
		try {
			if(fbId != null) {
				PageNavigator.close(commonSteps.getPfId());
				this.browser = commonSteps.getBrowser();
				this.sessionId = commonSteps.getSessionId();
				if(commonSteps.getFreshUser() != null) {
					freshUser = commonSteps.getFreshUser();
				}
				if(user!=null && user.getUserId()!=null && user.isLocked()) {
					userProvider.unlockUser(user.getUserId());
				}
				if (account!=null && account.getAccountId()!=null && userAttributes==null) {
					freshAccountProvider.unlockAccount(account.getAccountId());
				}
				if(freshUser!=null && freshUser.getPrimaryKey()>0) {
					freshUserProvider.unlockFreshUser(freshUser.getPrimaryKey());
				}
				if (browser instanceof RemoteWebDriver) {
					final String status = testScenario.getStatus().equals("passed")
							? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
							: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
					citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
				}
				if (this.browser != null) {
					browser.quit();
				}
			}
		}
		catch(Exception e) {
			logger.error("Error occured while performing clean up activities : ", e );
			fail("Not able to perform clean up activities due to : "+e.getMessage());
		}
	}
	
	@Given("^an existing user with facebook Id same as alternate email Id$")
	public void givenUserWithFBIdSameEmailId() {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.FACE_BOOK_ID_SAME_AS_ALTER_EMAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			account = userObjects.getAccount();
			user =userObjects.getUser();
			userAttributes = userObjects.getUserAttr();
			fbId = userAttributes.getFbId();
			fbPassword = userAttributes.getFbPassword();
			commonSteps.setFbId(fbId);
			commonSteps.setFbPassword(fbPassword);
			logger.info("existing account Id = {} and user Id = {}", this.account.getAccountId(), this.user.getUserId());
		}
		//Set in Idm common steps
		commonSteps.setAccount(account);
		commonSteps.setUser(user);
		commonSteps.setUserAttributes(userAttributes);
	}
	
	@Given("^an existing user with facebook Id diff from alternate email Id$")
	public void givenUserWithFBIdDiffEmailId() {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			account = userObjects.getAccount();
			user =userObjects.getUser();
			userAttributes = userObjects.getUserAttr();
			fbId = userAttributes.getFbId();
			fbPassword = userAttributes.getFbPassword();
			commonSteps.setFbId(fbId);
			commonSteps.setFbPassword(fbPassword);
			logger.info("existing account Id = {} and user Id = {}", this.account.getAccountId(), this.user.getUserId());
		}
		//Set in Idm common steps
		commonSteps.setAccount(account);
		commonSteps.setUser(user);
		commonSteps.setUserAttributes(userAttributes);
	}
	
	@When("^user opens FB XfinityURL page in a browser$")
	public void userOpensXfinitySignInPage() {
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), Platforms.valueOf(saucePlatform), Types.COMPUTER, sauceBrowser, true);
		} catch (IOException e) {
			logger.error("Error occurred while getting browser instance : ", e);
			fail("Not able to get browser instance due to : "+e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		FacebookXfinityLogin facebookXfinityLogin = new FacebookXfinityLogin(this.browser);
		facebookXfinityLogin.setPageFlowId(this.pfId);
		facebookXfinityLogin.setWindowHandle(this.browser.getWindowHandle());
		pageObject = facebookXfinityLogin.get();
		
		commonSteps.setPfId(this.pfId);
		commonSteps.setBrowser(this.browser);
		commonSteps.setSessionId(this.sessionId);
	}
	
	@When("^user opens FB XfinityURL page in a Spanish browser$")
	public void userOpensXfinitySignInPageInSpanish() {
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), Platforms.valueOf(saucePlatform), Types.COMPUTER, sauceBrowser, true, WebDriverUtility.getFireFoxBrowserToSpanish());
		} catch (IOException e) {
			logger.error("Error occurred while getting browser instance : ", e);
			fail("Not able to get browser instance due to : "+e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		FacebookXfinityLogin facebookXfinityLogin = new FacebookXfinityLogin(this.browser);
		facebookXfinityLogin.setPageFlowId(this.pfId);
		facebookXfinityLogin.setWindowHandle(this.browser.getWindowHandle());
		pageObject = facebookXfinityLogin.get();
		
		commonSteps.setPfId(this.pfId);
		commonSteps.setBrowser(this.browser);
		commonSteps.setSessionId(this.sessionId);
	}
	
	@Then("^user should be blocked by facebook connect$")
	public void userBlockedByFBConnect() {
		if(pageObject instanceof XfinityHome || pageObject instanceof LastStepBeforeFBConnect) {
			logger.error("Facebook connect has not been blocked after blocking Facebook access at Xfinity user profile page");
			fail("Facebook connect has not been blocked after blocking Facebook access at Xfinity user profile page");
		} else {
			commonSteps.getBrowser().quit();
		}
	}
	
	@When("^user connects facebook account$")
	public void userLogsInByFacebookAtUserProfilePage() {
		if(!(pageObject instanceof XfinityUserprofile)){
			logger.error(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		pageObject = userProfile.getFacebookLoginPopup();
		String parentWindowHandler = userProfile.getParentWindowHandler();
		if(!(pageObject instanceof FacebookLoginPopUp)) {
			logger.error("Page FacebookLoginPopUp not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail("FacebookLoginPopUp page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		FacebookLoginPopUp facebookLoginPage = (FacebookLoginPopUp)pageObject;
		if(userAttributes != null && userAttributes.getFbId() != null && userAttributes.getFbPassword() != null) {
			pageObject = facebookLoginPage.login(userAttributes.getFbId(), userAttributes.getFbPassword(), parentWindowHandler);
		} else {
			logger.error("userAttributes are not fetching from database or this user doesn't have FbId or FbPassword.");
			fail("userAttributes are not fetching from database or this user doesn't have FbId or FbPassword.");
		}
	}
	
	@When("^user connects facebook account in Spanish$")
	public void userLogsInByFacebookAtUserProfileSpanishPage() {
		if(!(pageObject instanceof XfinityUserprofileSpanish)){
			logger.error(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		pageObject = userProfile.getFacebookLoginPopup();
		String parentWindowHandler = userProfile.getParentWindowHandler();
		if(!(pageObject instanceof FacebookLoginPopUp)) {
			logger.error("Page FacebookLoginPopUp not get. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail("FacebookLoginPopUp page not get. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		FacebookLoginPopUp facebookLoginPage = (FacebookLoginPopUp)pageObject;
		if(userAttributes != null && userAttributes.getFbId() != null && userAttributes.getFbPassword() != null) {
			pageObject = facebookLoginPage.login(userAttributes.getFbId(), userAttributes.getFbPassword(), parentWindowHandler);
		} else {
			logger.error("userAttributes cannot be fetched from database or this user doesn't have FbId or FbPassword.");
			fail("userAttributes cannot be fetched from database or this user doesn't have FbId or FbPassword.");
		}
	}
	
	@When("^user disconnects facebook account$")
	public void userDisconnectsFacebookAtUserProfilePage() {
		if(!(pageObject instanceof XfinityUserprofile)){
			logger.error(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		userProfile.disconnectFacebook();
	}
	
	@When("^user disconnects facebook account in Spanish$")
	public void userDisconnectsFacebookAtUserProfileSpanishPage() {
		if(!(pageObject instanceof XfinityUserprofileSpanish)){
			logger.error(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		userProfile.disconnectFacebook();
	}
	
	@When("^user blocks facebook account$")
	public void userBlocksFacebookAtUserProfilePage() {
		if(!(pageObject instanceof XfinityUserprofile)){
			logger.error(XFINITY_USERPROFILE_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		userProfile.blockAccess();
	}
	
	@When("^user blocks facebook account in Spanish$")
	public void userBlocksFacebookAtUserProfileSpanishPage() {
		if(!(pageObject instanceof XfinityUserprofileSpanish)){
			logger.error(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		userProfile.blockAccess();
	}
	
	@When("^user unblocks facebook account$")
	public void userUnblocksFacebookAtUserProfilePage() {
		if(!(pageObject instanceof XfinityUserprofile)){
			logger.error(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		userProfile.allowAccess();
	}
	
	@When("^user unblocks facebook account in Spanish$")
	public void userUnblocksFacebookAtUserProfileSpanishPage() {
		if(!(pageObject instanceof XfinityUserprofileSpanish)){
			logger.error(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		userProfile.allowAccess();
	}
	
	@When("^user logs in at Facebook Xfinity URL page$")
	public void userEntersUsernamePasswdAndNextAtFBXfinityURL() {
		if(!(pageObject instanceof FacebookXfinityLogin)){
			logger.error("FacebookXfinityLogin page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail("FacebookXfinityLogin page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		FacebookXfinityLogin facebookXfinityLogin = (FacebookXfinityLogin)pageObject;
		if(user != null && user.getUserId() != null && user.getPassword() != null) {
			pageObject = facebookXfinityLogin.signin(user.getUserId(), user.getPassword());
		} else {
			logger.error("user are not fetching from database or this user doesn't have UID or password.");
			fail("user are not fetching from database or this user doesn't have UID or password.");
		}
	}
	
	@When("^user proceeds with username lookup$")
	public void userClicksUsernameLookupLinkOnLastStepBeforeFBConnectPage() {
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof LastStepBeforeFBConnect)){
			logger.error("LastStepBeforeFBConnect page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
			fail("LastStepBeforeFBConnect page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		LastStepBeforeFBConnect lastStepBeforeFBConnect = (LastStepBeforeFBConnect)pageObject;
		pageObject = lastStepBeforeFBConnect.clickUsernameLookup();
		commonSteps.setPageObject(pageObject);
	}
	
	@Then("^user is disconnected from Facebook$")
	public void userLandsOnXfinityUserProfilePageWithConnecting() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
			fail(XFINITY_USERPROFILE_PAGE_NOT_FOUND + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isConnectFacebookPresent()) {
			logger.error("Expected page not found after user signs in from Facebook Xfinity URL page or disconnects Facebook from Xfinity user profile page");
			fail("Expected page not found after user signs in from Facebook Xfinity URL page or disconnects Facebook from Xfinity user profile page");
		}
	}
	
	@Then("^user is disconnected from Facebook in Spanish$")
	public void userLandsOnXfinityUserProfileSpanishPageWithConnecting() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error("XfinityUserprofileSpanish page not found after user signs in from Facebook Xfinity URL page or disconnects Facebook from Xfinity user profile page "
					+ PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail("XfinityUserprofileSpanish page not found after user signs in from Facebook Xfinity URL page or disconnects Facebook from Xfinity user profile page "
					+ PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isConnectFacebookPresent()) {
			logger.error("Expected page not found after user signs in from FacebookXfinityURL page or disconnects Facebook from Xfinity user profile page");
			fail("Expected page not found after user signs in from FacebookXfinityURL page or disconnects Facebook from Xfinity user profile page");
		}
	}
	
	@Then("^user is connected from Facebook$")
	public void userLandsOnXfinityUserProfilePageWithDisconnecting() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error("XfinityUserprofile page not found after user connects Facebook from Facebook Xfinity user profile page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail("XfinityUserprofile page not found after user connects Facebook from Facebook Xfinity user profile page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isDisconnectFacebookPresent()) {
			logger.error("Expected page not found after user connects Facebook from Facebook Xfinity user profile page");
			fail("Expected page not found after user connects Facebook from Facebook Xfinity user profile page");
		}
	}
	
	@Then("^user is connected from Facebook in Spanish$")
	public void userLandsOnXfinityUserProfileSpanishPageWithDisconnecting() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error("Expected page not found after user connects Facebook from Facebook Xfinity user profile page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail("Expected page not found after user connects Facebook from Facebook Xfinity user profile page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isDisconnectFacebookPresent()) {
			logger.error("Expected page not get after user connects Facebook from Facebook Xfinity user profile page");
			fail("Expected page not get after user connects Facebook from Facebook Xfinity user profile page");
		}
	}
	
	@Then("^user is unblocked from Facebook$")
	public void userLandsOnXfinityUserProfilePageWithBlockingAccess() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isBlockAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
		}
	}
	
	@Then("^user is unblocked from Facebook in Spanish$")
	public void userLandsOnXfinityUserProfileSpanishPageWithBlockingAccess() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isBlockAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
		}
	}
	
	@Then("^user is unblocked from Facebook and close browser$")
	public void userLandsOnXfinityUserProfilePageWithBlockingAccessAndCloseBrowser() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isBlockAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
		}
		else {
			this.browser.quit();
		}
	}
	
	@Then("^user is unblocked from Facebook in Spanish and close browser$")
	public void userLandsOnXfinityUserProfileSpanishPageWithBlockingAccessAndCloseBrowser() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isBlockAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS);
		}
		else {
			this.browser.quit();
		}
	}
	
	@Then("^user is blocked from Facebook and close browser$")
	public void userLandsOnXfinityUserProfilePageWithAllowingAccessAndCloseBrowser() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isAllowAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
		}
		else {
			this.browser.quit();
		}
	}
	
	@Then("^user is blocked from Facebook in Spanish and close browser$")
	public void userLandsOnXfinityUserProfileSpanishPageWithAllowingAccessAndCloseBrowser() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isAllowAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
		}
		else {
			this.browser.quit();
		}
	}
	
	@Then("^user is blocked from Facebook$")
	public void userLandsOnXfinityUserProfilePageWithAllowingAccess() {
		if (!(pageObject instanceof XfinityUserprofile)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofile userProfile = (XfinityUserprofile)pageObject;
		if(!userProfile.isAllowAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
		}
	}
	
	@Then("^user is blocked from Facebook in Spanish$")
	public void userLandsOnXfinityUserProfileSpanishPageWithAllowingAccess() {
		if (!(pageObject instanceof XfinityUserprofileSpanish)) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		XfinityUserprofileSpanish userProfile = (XfinityUserprofileSpanish)pageObject;
		if(!userProfile.isAllowAccessPresent()) {
			logger.error(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
			fail(EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE);
		}
	}

	@When("^user signs in at Last step before FB connect page$")
	public void userSignsInAtLastStepBeforeFBConnectPage() {
		if (!(pageObject instanceof LastStepBeforeFBConnect)) {
			logger.error("Expected page not found after user logs in by using facebook account from Xfinity Login page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
			fail("Expected page not found after user logs in by using facebook account from Xfinity Login page " + 
					PAGE_HAS_BEEN_IDENTIFIED + (pageObject!=null?pageObject.getClass().getName():null));
		}
		LastStepBeforeFBConnect lastStepBeforeFBConnectPage = (LastStepBeforeFBConnect)pageObject;
		if(user != null && user.getUserId() != null && user.getPassword() != null) {
			lastStepBeforeFBConnectPage.enterUsername(user.getUserId());
			lastStepBeforeFBConnectPage.enterPassword(user.getPassword());
			pageObject = lastStepBeforeFBConnectPage.clickSignin();
		} else {
			logger.error("Expected page not found after user logs in by using facebook account from Xfinity Login page");
			fail("Expected page not found after user logs in by using facebook account from Xfinity Login page");
		}
		commonSteps.setPageObject(pageObject);
	}
	
	private static final String XFINITY_USERPROFILE_PAGE_NOT_FOUND = "XfinityUserprofile page not found. Page has been identified as ";
	private static final String XFINITY_USERPROFILE_SPANISH_PAGE_NOT_FOUND = "XfinityUserprofileSpanish page not found. Page has been identified as ";
	private static final String PAGE_HAS_BEEN_IDENTIFIED = "Page has been identified as ";
	private static final String EXPECTED_PAGE_NOT_FOUND_AFTER_USER_SIGNS_IN_FROM_FACEBOOK_XFINITY_URL_PAGE_OR_ALLOWS = "Expected page not found after user signs in from Facebook Xfinity URL page or allows access Facebook from Xfinity user profile page ";
	private static final String EXPECTED_PAGE_NOT_FOUND_AFTER_USER_BLOCKS_FACEBOOK_FROM_FACEBOOK_XFINITY_USER_PROFILE_PAGE = "Expected page not found after user blocks Facebook from Facebook Xfinity user profile page ";
	
}
