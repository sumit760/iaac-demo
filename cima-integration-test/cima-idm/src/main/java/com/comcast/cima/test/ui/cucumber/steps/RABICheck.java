package com.comcast.cima.test.ui.cucumber.steps;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.RABICheckConfirmation;
import com.comcast.cima.test.ui.pageobject.RABICheckOptions;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.LDAPInterface.SearchType;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.dataProvider.DataProviderToCreateUser;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinityRabi;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class RABICheck {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private String pfId;
	private LDAPInterface ldap;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private AccountCache.Account account;
	private UserCache.User user;
	private UserAttributesCache.Attribute userAttributes;
	private Object pageObject;
	private AccountCache.Account accountForRABI;
	private FreshUsers userForRABI;
	private String phoneNumber;
	private String alternateEmail;
	private String PwdHintAns;
	private String PwdHint;
	private String cstPreferredCommunicationEmail;
	
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
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.pfId = null;
		this.testScenario = scenario;
		commonSteps.setTestScenario(scenario);
		this.sessionId = null;
		this.account = null;
		this.user = null;
		this.userAttributes = null;
		this.pageObject = null;
		this.accountForRABI = null;
		this.userForRABI = null;
		this.phoneNumber = null;
		this.alternateEmail = null;
		this.cstPreferredCommunicationEmail = null;
		this.PwdHint = ICimaCommonConstants.SECOND_SECRET_QUESTION;
		this.PwdHintAns = ICimaCommonConstants.SECOND_SECRET_ANSWER;
	}
	
	@After
	public void tearDown() {
		try{
			if (accountForRABI != null) {
				PageNavigator.close(pfId);
				if (accountForRABI!=null && accountForRABI.getAccountId()!=null && userForRABI==null) {
					freshAccountProvider.unlockAccount(accountForRABI.getAccountId());
				}
				if(userForRABI!=null && userForRABI.getPrimaryKey()>0) {
					freshUserProvider.unlockFreshUser(userForRABI.getPrimaryKey());
				}
				if(userAttributes != null){
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_MOBILE.getValue(), null);
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_CONTACT_EMAIL.getValue(), null);
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_CONTACT_EMAIL_STATUS.getValue(), null);
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_PREFERRED_COMM_EMAIL.getValue(), null);
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_SECRET_QUESTION.getValue(), 
					this.userAttributes.getSecretQuestion());
					ldap.updateAttributeOfCustomerObject(userAttributes.getUserId(), LdapAttribute.CUSTOMER_SECRET_ANSWER.getValue(), 
					this.userAttributes.getSecretAnswer());
					ldap.deleteUserLoginObject(alternateEmail);
				}	
				if (browser instanceof RemoteWebDriver) {
					final String status = testScenario.getStatus().equals("passed")
							? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
							: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
					citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
				}
				if (browser != null) {
					browser.quit();
				}
			}
		}
		catch(Exception e){
			logger.error("Error occured while performing clean up activities : ", e );
			fail("Not able to perform clean up activities due to : "+e.getMessage());
		}
	}
	
	@Given("^an existing user with only SQA as recovery option$")
	public void givenUser() throws Exception {
		ldap = commonSteps.getLdap();
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
														UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
														UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL,
														UserDataProvider.Filter.SECRET_QUESTION, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			account = userObjects.getAccount();
			user = userObjects.getUser();
			userAttributes = userObjects.getUserAttr();
		}
		else {
			throw new IllegalStateException("This user is null or this user doesn't have either user account: " + 
					account + "or user attribute: " + userAttributes);
		}
		//Set in Idm common steps
		commonSteps.setAccount(account);
		commonSteps.setUser(user);
		commonSteps.setUserAttributes(userAttributes);
		
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			accountForRABI = fAccount;
		}
		
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers freshUser = freshUserProvider.getFreshUser(frUsrFilters);
		if(freshUser!=null){
			userForRABI = freshUser;
		}
		
		if(accountForRABI!=null && accountForRABI.getPhoneNumber()!=null) {
			phoneNumber = accountForRABI.getPhoneNumber();
		} else {
			throw new IllegalStateException("account is not fetching from database or this account doesn't have phone number.");
		}
		if (userForRABI!=null && userForRABI.getAlternativeEmail()!=null) {
			alternateEmail = userForRABI.getAlternativeEmail();
		} else {
			throw new IllegalStateException("user is not fetching from database or this user doesn't have alternate email.");
		}
	}
	
	@And("^user opens Xfinity RABI Login page in browser (.*?) over (.*?)$")
	public void userOpensXfinitySignInPage(String sauceBrowser, String saucePlatform) {
		
		pfId = PageNavigator.start(testScenario.getName()+MiscUtility.getCurrentTimeInMillis());
		try {
			browser = citfTestInitializer.getBrowserInstance(getClass().getSimpleName(), Platforms.valueOf(saucePlatform), Types.COMPUTER, sauceBrowser, true);
		} catch (IOException e) {
			logger.error(ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE, e);
            fail(NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO + e.getMessage());
		}
		sessionId = ((RemoteWebDriver)this.browser).getSessionId().toString();
		SignInToXfinityRabi signInToXfinity = new SignInToXfinityRabi(this.browser);
		signInToXfinity.setPageFlowId(this.pfId);
		signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
		pageObject = signInToXfinity.get();
		commonSteps.setPfId(this.pfId);
		commonSteps.setBrowser(this.browser);
	}
	
	@When("^user signs in Xfinity for RABI$")
	public void userEntersUsernamePasswdAndNext() {
		user = commonSteps.getUser();
		if(!(pageObject instanceof SignInToXfinityRabi)){
			logger.error("Sign in to Xfinity page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
            fail("Sign in to Xfinity page not found. Page has been identified as " + 
					(pageObject!=null?pageObject.getClass().getName():null));
		}
		SignInToXfinityRabi signInPage = (SignInToXfinityRabi)pageObject;
		if(user != null && user.getUserId() != null && user.getPassword() != null) {
			pageObject = signInPage.signin(user.getUserId(), user.getPassword());
		} else {
			logger.error("user is not fetching from database or this user doesn't have UID or password. user: " + user);
            fail("user is not fetching from database or this user doesn't have UID or password. user: " + user);
		}
	}
	
	@Then("^user successfully lands on Update your password recovery options page$")
	public void userLandsOnRABICheckOptionsPage() throws Exception {
		if (!(pageObject instanceof RABICheckOptions)) {
			throw new Exception("Expected page not found after user enters username & password and clicks sign in from Xfinity Login page" + 
				"Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
	}
	
	@When("^user verifies a phone number and enters SMS code and clicks Continue button$")
	public void userVerifiesPhoneEntersSMSAndNext() throws Exception {
		if (!(pageObject instanceof RABICheckOptions)) {
			throw new Exception("Expected page not found after user enters username & password and clicks sign in from Xfinity Login page" + 
				"Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
		RABICheckOptions rabiCheckOptions = (RABICheckOptions)pageObject;
		rabiCheckOptions.verifyMobilePhone(phoneNumber);
		String smscode=citfTestInitializer.readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, phoneNumber);
		if (smscode == null) {
			throw new Exception("No SMS verification code found in server log for this mobile number: " + phoneNumber);
		}
		rabiCheckOptions.enterSMSCode(smscode);
		pageObject = rabiCheckOptions.continueConfirmation();
	}
	
	@When("^user enters third party email and clicks Continue button$")
	public void userEntersEmailAndNext() throws Exception {
		if (!(pageObject instanceof RABICheckOptions)) {
			throw new Exception("Expected page not found after user enters username & password and clicks sign in from Xfinity Login page" + 
				"Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
		RABICheckOptions rabiCheckOptions = (RABICheckOptions)pageObject;
		rabiCheckOptions.enterEmail(alternateEmail);
		pageObject = rabiCheckOptions.continueConfirmation();
		commonSteps.setAlternateEmail(alternateEmail);
	}
	
	@When("^user enters third party email and verifies a phone number and enters SMS code and clicks Continue button$")
	public void userVerifiesPhoneEntersSMSEntersEmailAndNext() throws Exception {
		if (!(pageObject instanceof RABICheckOptions)) {
			throw new Exception("Expected page not found after user enters username & password and clicks sign in from Xfinity Login page" + 
				"Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
		RABICheckOptions rabiCheckOptions = (RABICheckOptions)pageObject;
		rabiCheckOptions.enterEmail(alternateEmail);
		rabiCheckOptions.verifyMobilePhone(phoneNumber);
		String smscode=citfTestInitializer.readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, phoneNumber);
		if (smscode == null) {
			throw new Exception("No SMS verification code found in server log for this mobile number: " + phoneNumber);
		}
		rabiCheckOptions.enterSMSCode(smscode);
		pageObject = rabiCheckOptions.continueConfirmation();
		commonSteps.setAlternateEmail(alternateEmail);
	}
	
	@When("^user enters third party email and verifies a phone number and enters SMS code also changes SQA and clicks Continue button$")
	public void userVerifiesPhoneEntersSMSEntersEmailChangesSQAAndNext() throws Exception {
		if (!(pageObject instanceof RABICheckOptions)) {
			throw new Exception("Expected page not found after user enters username & password and clicks sign in from Xfinity Login page" + 
				"Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
		RABICheckOptions rabiCheckOptions = (RABICheckOptions)pageObject;
		rabiCheckOptions.enterEmail(alternateEmail);
		rabiCheckOptions.verifyMobilePhone(phoneNumber);
		String smscode=citfTestInitializer.readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, phoneNumber);
		if (smscode == null) {
			throw new Exception("No SMS verification code found in server log for this mobile number: " + phoneNumber);
		}
		rabiCheckOptions.enterSMSCode(smscode);
		rabiCheckOptions.selectSecondSecurityQuestion();
		rabiCheckOptions.setAnswer(this.PwdHintAns);
		pageObject = rabiCheckOptions.continueConfirmation();
		commonSteps.setAlternateEmail(alternateEmail);
	}
	
	@Then("^user successfully lands on Review your password recovery options page$")
	public void userLandsOnRABICheckConfirmationPage() throws Exception {
		if (!(pageObject instanceof RABICheckConfirmation)) {
			throw new Exception("Expected page not found after user adds other recovery option and clicks Continue button from RABICheck Option page"
				+ "Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
	}
	
	
	
	@When("^user clicks Continue button$")
	public void userAndNext() throws Exception {
		if (!(pageObject instanceof RABICheckConfirmation)) {
			throw new Exception("Expected page not found after user adds other recovery option and clicks Continue button from RABICheck Option page"
				+ "Page has been identified as " + (pageObject!=null?pageObject.getClass().getName():null));
		}
		RABICheckConfirmation rabiCheckConfirmation = (RABICheckConfirmation)pageObject;
		pageObject = rabiCheckConfirmation.continueXfinity();
	}
	
	/* Verify LDAP */
	@Then("^user verifies mobile number has set in LDAP for RABI check$")
	public void userVerifiesMobileNumberInLDAP() throws Exception {
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,userAttributes.getUserId()));
		if (this.phoneNumber != null) {
			if (cust.getMobile()==null || !cust.getMobile().equals(this.phoneNumber) ) {
				throw new Exception("the value of mobile is null or not updated in ESD");
			}
		}
	}
	
	@Then("^third party email status is checked in the log before test$")
	public void emailStatusIsCheckedInTheLogBeforeTest() throws Exception {
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,user.getUserId()));
		if (cust.getCstContactEmail()!=null) {
			throw new Exception("there is already an alternate email associated with this user: " + cust.getUid());
		}
		if (cust.getCstContactEmailStatus()!=null) {
			throw new Exception("cstContactEmailStatus is not empty for this user: " + cust.getUid());
		}
		cstPreferredCommunicationEmail = cust.getCstPreferredCommunicationEmail();
	}
	
	@Then("^user verifies third party email has set in LDAP for RABI check$")
	public void userVerifiesThirdPartyEmailInLDAP() throws Exception {
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,user.getUserId()));
		if (this.alternateEmail != null) {
			if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
				throw new Exception("the value of cstContact Email is null or not updated in ESD");
			}
			if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) {
				throw new Exception("the value of cstContact Email status is not updated to Delivered in ESD");
			}
			if (!cust.getCstPreferredCommunicationEmail().equals(cstPreferredCommunicationEmail)) {
				throw new Exception("the value of cstPreferredCommunicationEmail is changed in ESD");
			}
		}
		else {
			throw new Exception("Failed to get the alternateEmail");
		}
	}
	
	@Then("^user verifies mobile number and third party email have set in LDAP for RABI check$")
	public void userVerifiesMobileNumberThirdPartyEmailInLDAP() throws Exception {
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,userAttributes.getUserId()));
		if (this.phoneNumber != null) {
			if (cust.getMobile()==null || !cust.getMobile().equals(this.phoneNumber) ) {
				throw new Exception("the value of Mobile is null or not updated in ESD");
			}
		}
		if (this.alternateEmail != null) {
			if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
				throw new Exception("the value of cstContact Email is null or not updated in ESD");
			}
			if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) {
				throw new Exception("the value of cstContact Email status is not updated to Delivered in ESD");
			}
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) {
				throw new Exception("the value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
			}
		}
		else {
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
				throw new Exception("the value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
			}
		}
	}
	
	@Then("^user verifies mobile number and third party email also updated SQA have set in LDAP for RABI check$")
	public void userVerifiesMobileNumberThirdPartyEmailSQAInLDAP() throws Exception {
		LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,userAttributes.getUserId()));
		if(cust.getCstPwdHint()!= null && cust.getCstPwdHintAnswer()!= null && 
				(!cust.getCstPwdHint().equals(PwdHint)  || !cust.getCstPwdHintAnswer().equals(PwdHintAns))) {
				throw new Exception("the value of SQA is not updated in ESD");
		}
		if (this.phoneNumber != null && (cust.getMobile()==null || !cust.getMobile().equals(this.phoneNumber))) {
				throw new Exception("the value of Mobile is null or not updated in ESD");
		}
		if (this.alternateEmail != null) {
			if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
				throw new Exception("the value of cstContact Email is null or not updated in ESD");
			}
			if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) {
				throw new Exception("the value of cstContact Email status is not updated to Delivered in ESD");
			}
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) {
				throw new Exception("the value of cstPreferredCommunicationEmail is not updated to contactEmail in ESD");
			}
		}
		else {
			if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
				throw new Exception("the value of cstPreferredCommunicationEmail is not set to comcastEmail in ESD");
			}
		}
	}
	
	
	private static final String ERROR_OCCURRED_WHILE_GETTING_BROWSER_INSTANCE = "Error occurred while getting browser instance : ";
	private static final String NOT_ABLE_TO_GET_BROWSER_INSTANCE_DUE_TO = "Not able to get browser instance due to : ";
}
