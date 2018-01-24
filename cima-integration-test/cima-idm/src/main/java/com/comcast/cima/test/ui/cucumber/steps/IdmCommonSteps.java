package com.comcast.cima.test.ui.cucumber.steps;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.naming.NamingException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.EmailConfirmationLink;
import com.comcast.cima.test.ui.pageobject.EmailValidationSuccess;
import com.comcast.cima.test.ui.pageobject.FacebookLoginPopUp;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.TokenGenerator;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserBySSN;
import com.comcast.cima.test.ui.pageobject.UserLookupSignUp;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.LDAPInterface.SearchType;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.WebDriverUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.dataProvider.DataProviderToCreateUser;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityHome;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author jxu004c
 *
 */

public class IdmCommonSteps {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pfId;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private AccountCache.Account account;
	private AccountCache.Account account2;
	private UserCache.User user;
	private UserAttributesCache.Attribute userAttributes;
	private String userId;
	private String userPassword;
	private String billingAccountId;
	private String StreetAddress;
	private String phoneNumber;
	private String ssn;
	private String dateofbirth;
	private Object pageObject;
	private String alternateEmail;
	private String emailLinkForVerification;
	private String smscode;
	private String mobile;
	private String mobile2;
	private String businessAccount;
	private FreshUsers freshUser;
	private String fbId;
	private String fbPassword;
    private String emailToken;
    private String sauceBrowser;
    private String saucePlatform;
    
    @Autowired
	private CoreCucumberSteps coreCucumberSteps;

	@Before
	public void setup() {
		this.emailToken=null;
		this.pfId = null;
		this.browser = null;
		this.sessionId = null;
		this.account = null;
		this.account2 = null;
		this.user = null;
		this.userAttributes = null;
		this.userId = null;
		this.userPassword = null;
		this.billingAccountId = null;
		this.StreetAddress = null;
		this.pageObject = null;
		this.alternateEmail = null;
		this.emailLinkForVerification = null;
		this.smscode=null;
		this.mobile=null;
		this.mobile2 = null;
		this.freshUser = null;
		this.fbId = null;
		this.fbPassword = null;
		this.phoneNumber = null;
		this.ssn = null;
		this.dateofbirth = null;
	}
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private FreshAccountProvider freshAccountProvider;
	
	@Autowired
	private DataProviderToCreateUser freshUserProvider;
	
	@Autowired
	private EndPoinUrlProvider urlProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private LDAPInterface ldap;
	
	@Autowired
	private ConfigurationDataProvider configDataProvider;
	

	@And("^user opens Xfinity Login page in a browser$")
	public void userLaunchBrowser() {
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), 
															 Platforms.valueOf(saucePlatform), 
															 Types.COMPUTER, 
															 sauceBrowser, 
															 true);
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}

		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		SignInToXfinity signInToXfinity = new SignInToXfinity(this.browser);
		signInToXfinity.setPageFlowId(this.pfId);
		signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
		pageObject = signInToXfinity.get();
	}
	
	@Given("^a fresh account with mobile number$")
	public void aFreshAccountWithMobileNumber() {
		
		// Filter for fresh residential account with mobile phone number and address
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																FreshAccountProvider.Filter.ADDRESS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																FreshAccountProvider.Filter.ACCOUNT_TYPE, ICimaCommonConstants.AccountType.RESIDENTIAL.name());
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
			mobile = account.getPhoneNumber();
			logger.info("Fresh IDM account Id with mobile number = {}", this.account.getAccountId());
		} else {
			logger.error("Error occurred while fetching the fresh account with the account filters : ", acFilters);
            fail("Unable to fetch the fresh account with the account filters: " + acFilters);
		}
	}
	
	@Given("^another fresh account with the same mobile number$")
	public void anotherFreshAccountWithTheSameMobileNumber() {
		
		// Filter for fresh account with mobile phone number and address
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																FreshAccountProvider.Filter.ADDRESS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																FreshAccountProvider.Filter.ACCOUNT_TYPE, ICimaCommonConstants.AccountType.RESIDENTIAL.name());
		
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account2 = fAccount;
			mobile2 = account2.getPhoneNumber();
			if(account != null && account.getPhoneNumber() != null) {
				try {
					ldap.updateAttributeOfAuthorizationObject(account2.getAccountId(), LdapAttribute.AUTHORIZATION_HOME_PHONE.getValue(), this.mobile);
				} catch (IllegalStateException | NamingException e) {
					logger.error("Error occurred while updating ldap attribute of authorization object : ", e);
		            fail("Not able to update ldap attribute of authorization object due to : "+e.getMessage());
				}
				logger.info("Fresh IDM account Id = {}", account.getAccountId());	
			}
		} else {
			logger.error("Error occurred while fetching the fresh account with the account filters : ", acFilters);
            fail("Unable to fetch the fresh account with the account filters: " + acFilters);
		}
	}
	
	@Given("^a fresh SSN account with mobile number$")
	public void aFreshAccountWithSSN() {
		
		// Filter for fresh account with SSN and mobile phone number
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				FreshAccountProvider.Filter.SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
			mobile = account.getPhoneNumber();
			logger.info("Fresh SSN account Id with mobile number = {}", this.account.getAccountId());	
		} else {
			logger.error("Error occurred while fetching the fresh account with the account filters : ", acFilters);
            fail("Unable to fetch the fresh account with the account filters: " + acFilters);
		}
	}
	
	@Given("^a fresh user with alternate email$")
	public void aFreshUserWithAlternateEmail() {
		
		// Filter for fresh user with alternate email
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers fUser = freshUserProvider.getFreshUser(frUsrFilters);
		if(fUser!=null){
			freshUser = fUser;
			alternateEmail = fUser.getAlternativeEmail();
			logger.info("Alternate email = {}", this.alternateEmail);
		} else {
			logger.error("Error occurred while fetching the fresh user with the user filters : ", frUsrFilters);
            fail("Unable to fetch the fresh user with the user filters: " + frUsrFilters);
		}
	}
	
	@Given("^a fresh facebook account$")
	public void aFreshFacebookAccount() {
		
		// Filter for fresh facebook account with alternate email
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.FACE_BOOK_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																	DataProviderToCreateUser.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers fUser = freshUserProvider.getFreshUser(frUsrFilters);
		if(fUser!=null){
			freshUser = fUser;
			fbId = fUser.getFbId();
			fbPassword = fUser.getFbPassword();
			alternateEmail = fUser.getAlternativeEmail();
			logger.info("Fresh facebook account Id = {} and alternate email = {}", this.fbId,this.alternateEmail);	
		} else {
			logger.error("Error occurred while fetching the fresh facebook account with the account filters : ", frUsrFilters);
            fail("Unable to fetch the fresh facebook account with the account filters: " + frUsrFilters);
		}
	}
	
	
	@Given("^a user with a facebook account$")
	public void userWithFacebookAccount() {
		
		// Filter for active user with facebookId, address and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
																   	   UserDataProvider.Filter.FACE_BOOK_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																   	   UserDataProvider.Filter.ADDRESS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
																   	   UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null && userObjects.getUserAttr() != null) {
			this.fbId = userObjects.getUserAttr().getFbId();
			this.fbPassword = userObjects.getUserAttr().getFbPassword();
			logger.info("existing FB Id = {} FB Password = {} ", this.fbId, this.fbPassword);	
		} else {
			logger.error("Error occurred while fetching facebook account with the user filters : ", filters);
            fail("Unable to fetch the fresh facebook account with the user filters: " + filters);
		}
		
	}
	

	@Given("^a business account$")
    public void aBusinessAccount() {
		
		// Filter for a business account
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.ACCOUNT_TYPE, 
				                                                             ICimaCommonConstants.AccountType.BUSINESS.name());
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
			this.businessAccount=account.getAccountId();
		} else {
			logger.error("Error occurred while fetching business account with the account filters : ", acFilters);
            fail("Unable to fetch the business account with the account filters: " + acFilters);
		}
    }

	@Given("^an existing user with SSN$")
	public void anExistingUserWithSSN() {
		
		// Filter for active user associated with SSN and mobile phone 
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
													UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
													UserDataProvider.Filter.SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
			this.mobile=account.getPhoneNumber();
			logger.info("existing account Id = {} user Id = {} and SSN = {}", this.account.getAccountId(), this.userAttributes.getUserId(), this.userAttributes.getSsn());	
		} else {
			logger.error("Error occurred while fetching user with SSN using user filters : ", filters);
            fail("Unable to fetch user with SSN using user filters: " + filters);
		}
	}
	
	@Given("^a fresh user with alternate email and mobile number$")
	public void getFreshUserwithEmailandMobile() {
		
		// Filter for fresh account associated with mobile phone number
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, 
				                                                             ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
			mobile = fAccount.getPhoneNumber();
		} else {
			logger.error("Error occurred while fetching fresh account using account filters : ", acFilters);
            fail("Unable to fetch fresh account using account filters: " + acFilters);
		}
		
		// Filter for fresh user associated with alternate email
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.ALTERNATE_MAIL, 
				                                                                    ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers fuser = freshUserProvider.getFreshUser(frUsrFilters);
		if(fuser!=null){
			freshUser = fuser;
			alternateEmail=fuser.getAlternativeEmail();
		} else {
			logger.error("Error occurred while fetching fresh user using user filters : ", frUsrFilters);
            fail("Unable to fetch fresh user using user filters: " + frUsrFilters);
		}

		logger.info("fresh account Id = {} and fresh email address = {}", this.account.getAccountId(), this.freshUser.getAlternativeEmail());	
	}
	
	@Given("^an existing user with mobile number$")
	public void anExistingUserWithMobileNumber() {
		
		// Filter for active primary user with mobile phone number 
		// and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, 
															 		   ICommonConstants.USER_STATUS_ACTIVE,
															 		   UserDataProvider.Filter.PHONE, 
															 		   ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
															 		   UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, 
															 		   ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
			this.mobile=account.getPhoneNumber();
			logger.info("existing account Id = {} and user Id = {}", this.account.getAccountId(), this.userAttributes.getUserId());
		} else {
			logger.error("Error occurred while fetching user with mobile number using user filters : ", filters);
            fail("Unable to fetch user with mobile number using user filters: " + filters);
		}
	}
	
	
	@Given("^a fresh facebook and user account$")
	public void aFreshFacebookAndUserAccount() {
		
		// Filter for fresh user account associated with facebookId 
		// and having an alternate email
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.FACE_BOOK_ID, 
				                                                                    ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				                                                                    DataProviderToCreateUser.Filter.ALTERNATE_MAIL, 
				                                                                    ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers fUser = freshUserProvider.getFreshUser(frUsrFilters);
		if(fUser!=null){
			freshUser = fUser;
			fbId = fUser.getFbId();
			fbPassword = fUser.getFbPassword();
			alternateEmail = fUser.getAlternativeEmail();
		} else {
			logger.error("Error occurred while fetching fresh user with facebook account using user filters : ", frUsrFilters);
            fail("Unable to fetch fresh user with facebook account using user filters: " + frUsrFilters);
		}
		
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, 
				                                                             ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
		} else {
			logger.error("Error occurred while fetching fresh account using account filters : ", acFilters);
            fail("Unable to fetch fresh account using account filters: " + acFilters);
		}
	}
	
	@And("^user opens Xfinity Login page in browser (.*?) over (.*?)$")
	public void userOpensXfinitySignInPage(String sauceBrowser, String saucePlatform) {
		
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), 
					                                         Platforms.valueOf(saucePlatform), 
					                                         Types.COMPUTER, 
					                                         sauceBrowser, 
					                                         true);
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		SignInToXfinity signInToXfinity = new SignInToXfinity(this.browser);
		signInToXfinity.setPageFlowId(this.pfId);
		signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
		pageObject = signInToXfinity.get();
	}
	
	
	@When("^user requests to create an username by passing the email token$")
	public void userCreateUsernameUrlByAppendingEmailTokenAndLaunch() {
		String urlWithToken;
		String url = urlProvider.getLoginUrl(EndPoinUrlProvider.LoginUrlPropKeys.XFINITY_CREATEUSERNAME_URL.name());
		urlWithToken=url+ "token=" + this.emailToken ;
		
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), 
					                                         Platforms.valueOf(saucePlatform), 
					                                         Types.COMPUTER, 
					                                         sauceBrowser, 
					                                         true);
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		VerifyIdentity verifyIdentity = new VerifyIdentity(urlWithToken + this.getEmailToken(),this.browser);
		verifyIdentity.setPageFlowId(this.pfId);
		verifyIdentity.setWindowHandle(this.browser.getWindowHandle());
		pageObject = verifyIdentity.get();
		

	}
	
	@When("^user opens token generation tool in a browser$")
	public void userOpensTokenGenerationTool() {
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), 
					                                         Platforms.valueOf(saucePlatform), 
					                                         Types.COMPUTER, 
					                                         sauceBrowser, 
					                                         true);
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		TokenGenerator tokenGenerator = new TokenGenerator(this.browser);
		tokenGenerator.setPageFlowId(this.pfId);
		tokenGenerator.setWindowHandle(this.browser.getWindowHandle());
		pageObject = tokenGenerator.get();
	   
	}
	
	@And("^user opens Xfinity Login page in a Spanish browser$")
	public void userOpensXfinitySignInPageInSpanish() {
		sauceBrowser = configDataProvider.getFireFoxBrowser();
        saucePlatform = configDataProvider.getWindowsPlatform();
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), 
					                                         Platforms.valueOf(saucePlatform), 
					                                         Types.COMPUTER, sauceBrowser, 
					                                         true, 
					                                         WebDriverUtility.getFireFoxBrowserToSpanish());
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		SignInToXfinity signInToXfinity = new SignInToXfinity(this.browser);
		signInToXfinity.setPageFlowId(this.pfId);
		signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
		pageObject = signInToXfinity.get();
	}
	
	@When("^user enters username and password and clicks sign in button$")
	public void userEntersUsernamePasswdAndNext() {
		if(!(pageObject instanceof SignInToXfinity)){
			logger.error(SignInToXfinity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
					citfTestInitializer.getPageName(getPageObject()));
			
            fail(SignInToXfinity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
					citfTestInitializer.getPageName(getPageObject()));
		}
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		if(user != null && user.getUserId() != null && user.getPassword() != null) {
			pageObject = signInPage.signin(user.getUserId(), user.getPassword());
		} else {
			logger.error("user is not fetching from database or this user doesn't have UID or password. user: " + user);
            fail("user is not fetching from database or this user doesn't have UID or password. user: " + user);
		}
	}
	
	
	@When("^third party email is verified in the log$")
    public void userValidatesVerificationEmailLink() {
        if (this.alternateEmail!=null) {
            emailLinkForVerification = citfTestInitializer.readLog("VERIFICATION_EMAIL", LogReader.RegexArgument.EMAIL, this.alternateEmail);    
        } else {
			logger.error("this user doesn't have alternate email address. alternateEmail: " + this.alternateEmail);
            fail("this user doesn't have alternate email address. alternateEmail: " + this.alternateEmail);
        }
        if (this.emailLinkForVerification != null) {
            EmailConfirmationLink emailConfirmationLinkobj=new EmailConfirmationLink(this.browser);
            emailConfirmationLinkobj.setPageFlowId(this.pfId);
            pageObject = emailConfirmationLinkobj.verify(emailLinkForVerification);
            if (!(pageObject instanceof EmailValidationSuccess)) {
            	logger.error(EmailValidationSuccess.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
            			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
            			citfTestInitializer.getPageName(getPageObject()));
            	
            	fail(EmailValidationSuccess.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
            			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
            			citfTestInitializer.getPageName(getPageObject()));
            }
        } else {
        	logger.error("Email validation link not found");
        	fail("Email validation link not found");
        }
    }
	
	@When("^user logs in by using facebook account$")
	public void userLogsInByFacebook() {
		if(!(pageObject instanceof SignInToXfinity)){
        	logger.error(SignInToXfinity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(SignInToXfinity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
        			citfTestInitializer.getPageName(getPageObject()));
		}
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		pageObject = signInPage.getFacebookLoginPopup();
		String parentWindowHandler = signInPage.getParentWindowHandler();
		if(!(pageObject instanceof FacebookLoginPopUp)) {
        	logger.error(FacebookLoginPopUp.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(FacebookLoginPopUp.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		FacebookLoginPopUp facebookLoginPage = (FacebookLoginPopUp)pageObject;
		if(this.fbId != null && this.fbPassword != null) {
			pageObject = facebookLoginPage.login(this.fbId, this.fbPassword, parentWindowHandler);
		} else {
			logger.error("Facebook account not fetching from database or this user doesn't have FbId or FbPassword.");
			fail("Facebook account not fetching from database or this user doesn't have FbId or FbPassword.");
		}
	}
	
	
	@Then("^user is prompted to provide xfinity user name$")
	public void userLandsOnLastStepBeforeFBConnectPage() {
		if (!(pageObject instanceof LastStepBeforeFBConnect)) {
        	logger.error(LastStepBeforeFBConnect.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(LastStepBeforeFBConnect.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND 
        			+ MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}
	
	@Then("^user lands on Xfnity portal page$")
	public void userLandsOnXfinityHomePage() {
		if (!(pageObject instanceof XfinityHome)) {
        	logger.error(XfinityHome.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(XfinityHome.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}
	
	@Then("^user is asked to verify his identity$")
	public void userLandsOnVerifyIdentityPage() {
		
		if (!(this.pageObject instanceof VerifyIdentity)) {
        	logger.error(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}
	
	@When("^user verifies his identity by account number$")
	public void userSelectsEnteringAccountNumberOptioin() {
		if(!(pageObject instanceof VerifyIdentity)){
        	logger.error(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		VerifyIdentity verifyIdentity = (VerifyIdentity)pageObject;
		pageObject = verifyIdentity.verifyIdentityByAccountNumber();
	}
	
	@Then("^user is challenged with a security check$")
	public void userSuccessfullyLandsOnSecurityCheckPage() {
		if (!(pageObject instanceof SecurityCheck)) {
        	logger.error(SecurityCheck.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
        	
        	fail(SecurityCheck.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}
	
	@When("^user clears the security check by providing the CAPTCHA$")
	public void userEntersCAPTCHACodeAndContinue() {
		if(!(pageObject instanceof SecurityCheck)){
			logger.error(SecurityCheck.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(SecurityCheck.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		SecurityCheck securityCheckPage = (SecurityCheck)pageObject;
		pageObject = securityCheckPage.provideSecurityCodeAndContinue("answer");
	}
	
	@Then("^user is asked to provide his account details$")
	public void userSuccessfullyLandsOnUserByAccountNumberPage() {
		if (!(pageObject instanceof UserByAccountNumber)) {
			logger.error(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}
	
	@When("^user provides account details by account number and street address$")
	public void userEntersValidAccountNumberAndStreetAddressOnUserAccountNumberPage() {
		if(!(pageObject instanceof UserByAccountNumber)){
			logger.error(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if(account != null && account.getAccountId() != null && account.getAddress() != null) {
			userByAccountNumber.enterAccountNumber(account.getAccountId());
			userByAccountNumber.enterStreetAddress(account.getAddress());
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
		}
		pageObject = userByAccountNumber.Continue();
	}

	
	@When("^user provides account details by street address only$")
	public void userProvidesStreetAddressOnly() {
		if(!(pageObject instanceof UserByAccountNumber)){
			logger.error(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if(account != null && account.getAccountId() != null && account.getAddress() != null) {
			userByAccountNumber.enterStreetAddress(account.getAddress());
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
		}
		userByAccountNumber.Continue();
	}
	
	@When("^user provides account details by account number only$")
	public void userProvidesAccountNumberOnly() {
		if(!(pageObject instanceof UserByAccountNumber)){
			logger.error(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +   
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if(account != null && account.getAccountId() != null && account.getAddress() != null) {
			userByAccountNumber.enterAccountNumber(account.getAccountId());
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
		}
		userByAccountNumber.Continue();
	}
	
		
	@Then("^user is prompted for one time verification code$")
	public void userSuccessfullyLandsOnOneTimeVerificationCodePage() {
        if(!(pageObject instanceof UserSMSConfirmation)){
			logger.error(UserSMSConfirmation.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserSMSConfirmation.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
        			citfTestInitializer.getPageName(getPageObject()));
        }
		this.smscode=citfTestInitializer.readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER,this.mobile);
		if (smscode!= null) {
			logger.info("SMS verification code found in server log for this mobile number: " + this.mobile);			
		}
		else {
			logger.error("No SMS verification code found in server log for this mobile number: " + this.mobile);
			fail("No SMS verification code found in server log for this mobile number: " + this.mobile);
		}
	}
	
	@Then("^third party email is verified in back office$")
    public void userVerifiesThirdPartyEmailInLDAP() {
	  
	   	LDAPAuthorization auth = ldap.getAuthorizationData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID,account.getAccountId()));
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_GUID,auth.getCstAccountManagerGuid()));
        if (this.alternateEmail != null) {
            if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
            	logger.error("the value of cstContact Email is null or not updated in ESD");
            	fail("the value of cstContact Email is null or not updated in ESD");
            }
            if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) {
            	logger.error("the value of cstContact Email status is not updated to Delivered in ESD");
            	fail("the value of cstContact Email status is not updated to Delivered in ESD");
            }
            if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) {
            	logger.error("the value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
            	fail("the value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
            }
        }
        else {
            if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
            	logger.error("the value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
            	fail("the value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
            }
        }
    }
	  
	@Then("^user mobile number and third party email is verified in the back office$")
	public void userVerifiesMobileNumberThirdPartyEmailInLDAP() {
		
		LDAPAuthorization auth = ldap.getAuthorizationData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID,account.getAccountId()));
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_GUID,auth.getCstAccountManagerGuid()));
		if (this.mobile != null && (cust.getMobile()==null || !cust.getMobile().replaceAll(ICimaCommonConstants.DASH, ICimaCommonConstants.BLANK_STRING).equals(this.mobile))) {
        	logger.error("the value of Mobile is null or not updated in ESD");
        	fail("the value of Mobile is null or not updated in ESD");
		}
        if(this.fbId!= null) {
            this.alternateEmail = fbId;
        }
		if (this.alternateEmail != null) {
			if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
            	logger.error("Value of cstContact Email is null or not updated in ESD");
            	fail("Value of cstContact Email is null or not updated in ESD");
			}
			if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) {
            	logger.error("Value of cstContact Email status is not updated to Delivered in ESD");
            	fail("Value of cstContact Email status is not updated to Delivered in ESD");
			}
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) {
            	logger.error("Value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
            	fail("Value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
			}
		}
		else {
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
            	logger.error("Value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
            	fail("Value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
			}
		}
	}	
	
	@When("^user provides account details by account number and phone number$")
	public void userEntersValidAccountNumberAndPhoneNumberOnUserAccountNumberPage() {
		if(!(pageObject instanceof UserByAccountNumber)){
			logger.error(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
					citfTestInitializer.getPageName(getPageObject()));
			
			fail(UserByAccountNumber.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
					citfTestInitializer.getPageName(getPageObject()));
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		userByAccountNumber.enterAccountNumber(account.getAccountId());
		userByAccountNumber.enterPhoneNumber(account.getPhoneNumber());
		pageObject = userByAccountNumber.Continue();
	}
	
	
	@Given("^an active user with no alternate email$")
	public void anActiveUserWithNoAlternateEmail() {
		
		// Filter for active user account with no alternate email 
		// and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null) {
			this.userId = userObjects.getUser().getUserId();
			this.userPassword = userObjects.getUser().getPassword();
			this.account = userObjects.getAccount();

			logger.info("User retrieved having no alternate email with userId = {} user password = {} ", 
					this.userId, this.userPassword);		
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_USER_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_USER_INFO);
		}
		
	}
	
	
	@Given("^an active user with an alternate email$")
	public void anActiveUserWithAlternateEmail() throws Exception {
		
		// Filter for active user account with alternate email, no mobile
		// phone number and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null && userObjects.getUser() != null 
				&& userObjects.getUserAttr() != null) {
			this.userId = userObjects.getUser().getUserId();
			this.userPassword = userObjects.getUser().getPassword();
			this.alternateEmail = userObjects.getUserAttr().getAlterEmail();
			this.account = userObjects.getAccount();

			logger.info("User retrieved having alternate email with userId = {} user password = {} alternate email = {}", 
					this.userId, this.userPassword, this.alternateEmail);		
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_USER_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_USER_INFO);
		}
	}
	
	
	
	@Given("^an active user with registered phone number on billing account$")
	public void anActiveUserWithPhoneNumber() throws Exception {
		
		// Filter for active primary user account associated with mobile phone,  
		// no alternate email and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.SECONDARY_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null) {
			this.userId = userObjects.getUser().getUserId();
			this.userPassword = userObjects.getUser().getPassword();
			this.account = userObjects.getAccount();

			logger.info("User retrieved having phone number with userId = {} user password = {} phone number = {}", 
					this.userId, this.userPassword, this.account.getPhoneNumber());	
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_USER_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_USER_INFO);
		}
	}


	@Given("^an active user with social security number$")
	public void anActiveUserWithSSN() throws Exception {
		
		// Filter for active primary user account associated with SSN, mobile phone 
		// and not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.SECONDARY_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null && userObjects.getUser() != null
				&& userObjects.getUserAttr() != null && userObjects.getAccount() != null) {
			this.userId = userObjects.getUser().getUserId();
			this.ssn = userObjects.getUserAttr().getSsn();
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			this.dateofbirth = df.format(userObjects.getUserAttr().getDob());
			this.phoneNumber = userObjects.getAccount().getPhoneNumber();

			logger.info("User retrieved having SSN and phone number with userId = {} SSN = {} phone number = {}", 
					this.userId, this.ssn, this.phoneNumber);	
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_USER_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_USER_INFO);
		}
	}

	
	
	@Given("^an active user with phone and no alternate email$")
	public void anActiveUserWithPhoneAndNoAlternateEmail() throws Exception {
		
		// Filter for active primary user account associated with mobile phone, no alternate email and 
		// not mapped to multiple accounts
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				UserDataProvider.Filter.SECONDARY_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
				UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, 
				                                                                 false, 
				                                                                 UserDataProvider.UserCategory.IDM, 
				                                                                 true);
		if (userObjects!=null) {
			this.userId = userObjects.getUser().getUserId();
			this.userPassword = userObjects.getUser().getPassword();
			this.account = userObjects.getAccount();

			logger.info("User retrieved having phone number with userId = {} user password = {} phone number = {}", 
					this.userId, this.userPassword, this.account.getPhoneNumber());	
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_USER_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_USER_INFO);
		}
		
	}
	
	
	@Given("^a fresh user account$")
	public void aFreshAccount() {
		
		// Filter for fresh residential user account associated with mobile phone and address
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				FreshAccountProvider.Filter.ADDRESS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
				FreshAccountProvider.Filter.ACCOUNT_TYPE, ICimaCommonConstants.AccountType.RESIDENTIAL.name());
		
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
			mobile = account.getPhoneNumber();
			logger.info("Fresh IDM account Id with mobile number = {}", this.account.getAccountId());
		} else {
			logger.error(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
            fail(MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO);
		}
	}
	
	
	@And("^billing account and address of the user$")
	public void billingAccountAndAddressOfUser() {
		
		if (this.account != null) {
			this.billingAccountId = this.account.getAccountId();
			this.StreetAddress = this.account.getAddress();
		} else {
			logger.error("No account information is found from data provider");
        	fail("No account information is found from data provider");
		}
		
	}
	
	@And("^billing account of the user$")
	public void billingAccountOfUser() {
		if (this.account == null) {
			logger.error("No account information is found from data provider");
        	fail("No account information is found from data provider");
		}
	}

	
	@Then("^user is asked to provide the mobile phone number to verify identity$")
	public void userIsAskedToProvidePhoneNumberPage() {
		if (!(pageObject instanceof UserByMobile)) {
			logger.error(UserByMobile.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserByMobile.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}

	
	@Then("^user is asked to provide his SSN$")
	public void userIsAskedToProvideSSN() {
		if (!(pageObject instanceof UserBySSN)) {
			logger.error(UserBySSN.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserBySSN.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
        			citfTestInitializer.getPageName(getPageObject()));
		}
	}

	
	@When("^user provides the phone number$")
	public void userProvidesPhoneNumberPage()  {
		
		UserByMobile userByMobileNumber = (UserByMobile) pageObject;
		userByMobileNumber.enterMobileNumber(this.account.getPhoneNumber());
		pageObject = userByMobileNumber.Continue();
	}

	
	@When("^user provides the social security number$")
	public void userProvidesSocialSecurityNumber() {
		
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterSocialSecurityNumber(this.ssn);
	}


	@And("^user provides the date of birth$")
	public void userProvidesDateOfBirth() {
		
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterDateOfBirth(this.dateofbirth);
	}
	
	@And("^user provides the phone number associated with the account$")
	public void userProvidesPhoneNumberAssociatedWithSSN() {
		
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterPhoneNumber(this.phoneNumber);
		pageObject = userBySSN.Continue();
	}

	
	@And("^user verifies the number by applying the verification code$")
	public void userVerifiesPhoneNumber() {
		
		if (pageObject instanceof UserByAccountNumber) {
			UserByAccountNumber accountInfoPage = (UserByAccountNumber) pageObject;
			accountInfoPage.enterAccountNumber(this.account.getAccountId());
			accountInfoPage.enterStreetAddress(this.account.getAddress());
			pageObject = accountInfoPage.Continue();
			return;
		}

		if(!(pageObject instanceof UserSMSConfirmation)){
			logger.error(UserSMSConfirmation.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(UserSMSConfirmation.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		
		UserSMSConfirmation smsConfirmation = (UserSMSConfirmation) pageObject;
		String smscode = citfTestInitializer.readLog("USR_SMS_VERIFICATION", 
				               LogReader.RegexArgument.PHONE_NUMBER, 
				               this.account.getPhoneNumber());
		if (smscode == null) {
			logger.error("SMS verification code not found");
        	fail("SMS verification code not found");
		}
		logger.info("Found SMS code {}", smscode);
		smsConfirmation.enterSMSCode(smscode);
		pageObject = smsConfirmation.Continue();
	}

	
	@When("^user verifies his identity by phone number$")
	public void userSelectsPhoneNumberOption() {
		if(!(pageObject instanceof VerifyIdentity)){
			logger.error(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
			
        	fail(VerifyIdentity.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
        			citfTestInitializer.getPageName(getPageObject()));
		}
		VerifyIdentity verifyIdentity = (VerifyIdentity)pageObject;
		pageObject = verifyIdentity.verifyIdentityByMobile();
	}
	
	
	@Then("^user is able to lookup his username$")
	public void userSuccessfullyLandsOnUserLookupSignUpPage() {
		if (!(pageObject instanceof UserLookupSignUp)) {
			logger.error(UserLookupSignUp.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
					citfTestInitializer.getPageName(getPageObject()));
			
			fail(UserLookupSignUp.class.getSimpleName() + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +
					citfTestInitializer.getPageName(getPageObject()));
		}
		UserLookupSignUp userLookupSignUp = (UserLookupSignUp)pageObject;
		if(!userLookupSignUp.isCreateUserPresent()) {
			logger.error("Create username button not found on UserLookupSignUp page");
			fail("Create username button not found on UserLookupSignUp page");
		}
	}
	
	private static final String ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE = "Error occurred while getting browser instance : ";
	private static final String NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO = "Not able to get browser instance due to : ";
	private static final String MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS = " Page has been identified as ";
	private static final String MESSAGE_PAGE_NOT_FOUND = " page not found";
	private static final String MESSAGE_ERROR_RETRIEVE_USER_INFO = "Error occurred while retrieving user information";
	private static final String MESSAGE_ERROR_RETRIEVE_ACCOUNT_INFO = "Error occurred while retrieving account information";
	
//-----------------------------------------------------------------------------------------------------------------	

	public String getBusinessAccount() {
		return businessAccount;
	}

	public void setBusinessAccount(String businessAccount) {
		this.businessAccount = businessAccount;
	}
	
	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}
	
	public LDAPInterface getLdap() {
		return ldap;
	}

	public Object getPageObject() {
		return pageObject;
	}
	
	public void setPageObject(Object pageObject) {
		this.pageObject = pageObject;
	}

	public AccountCache.Account getAccount() {
		return this.account;
	}
	
	public void setAccount(AccountCache.Account account) {
		this.account = account;
	}
	
	public UserAttributesCache.Attribute getUserAttributes() {
		return userAttributes;
	}
	
	public void setUserAttributes(UserAttributesCache.Attribute userAttributes) {
		this.userAttributes = userAttributes;
	}
	
	public Scenario getTestScenario() {
		return testScenario;
	}
	
	public void setTestScenario(Scenario testScenario) {
		this.testScenario = testScenario;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public WebDriver getBrowser() {
		return browser;
	}

	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}

	public String getPfId() {
		return pfId;
	}

	public void setPfId(String pfId) {
		this.pfId = pfId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getFbPassword() {
		return fbPassword;
	}

	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}

	public FreshUsers getFreshUser() {
		return freshUser;
	}

	public void setFreshUser(FreshUsers freshUser) {
		this.freshUser = freshUser;
	}

	public String getSmscode() {
		return smscode;
	}

	public void setSmscode(String smscode) {
		this.smscode = smscode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	public AccountCache.Account getAccount2() {
		return account2;
	}

	public void setAccount2(AccountCache.Account account2) {
		this.account2 = account2;
	}

	public UserCache.User getUser() {
		return user;
	}

	public void setUser(UserCache.User user) {
		this.user = user;
	}

	public String getMobile2() {
		return mobile2;
	}

	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getBillingAccountId() {
		return billingAccountId;
	}

	public String getStreetAddress() {
		return StreetAddress;
	}

	public String getSsn() {
		return ssn;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
}