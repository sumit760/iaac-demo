package com.comcast.cima.test.ui.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;

import com.comcast.cima.test.dataProvider.IdmTestDataProvider;
import com.comcast.cima.test.ui.pageobject.CreateUser;
import com.comcast.cima.test.ui.pageobject.EmailConfirmationLink;
import com.comcast.cima.test.ui.pageobject.EmailValidationSuccess;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.LDAPInterface.SearchType;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.ui.router.PageError;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

public class ITestIdmCreateUserID extends IdmTestDataProvider {
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
			
			String filterPhone = localParams.get(IdmFilterKeys.PHONE.getValue());
			String filterAlterEmail = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
			String filterAddress = localParams.get(IdmFilterKeys.ADDRESS.getValue());
			
			filter = new HashMap<String, Object>();
			keySet = new HashSet<String>();
			if(filterPhone!=null) {
				keySet.add(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				filter.put(IdmFilterKeys.PHONE.getValue(), filterPhone);
			}
			if(filterAlterEmail!=null) {
				keySet.add(IDMGeneralKeys.FRESH_USER.getValue());
				filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterEmail);
			}
			if(filterAddress!=null) {
				keySet.add(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				filter.put(IdmFilterKeys.ADDRESS.getValue(), filterAddress);
			}
			uid = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_NEW_USER_ID);
			if (uid.equalsIgnoreCase("GENERATE")) {
				UserNameGeneratorService userNameGen = ObjectInitializer.getUserNameGeneratorService();
				this.userId = userNameGen.next();
			} else {
				this.userId = uid;
			}
			userIdentifierType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_IDENTIFIER_TYPE);
			keepMeSignedIn = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_NEW_USER_KEEP_SIGNEDIN);
			additionalRecoveryOption = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN);
			saucePlatform = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM);
			sauceDeviceType = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE);
			sauceBrowser = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER);
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			executionEnvironment = getCurrentEnvironment();
			this.getTestData();
			ldap = ObjectInitializer.getLdapService();
			pfId = PageNavigator.start("createUserID");
			browser = getBrowserInstance(currentTestMethod,Platforms.valueOf(saucePlatform),Types.valueOf(sauceDeviceType),sauceBrowser,true);
			sessId = ((RemoteWebDriver)browser).getSessionId().toString();
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmCreateUserID.class));
		}
	}
	
	
	@Test
	public void createUserIDByMobileNumber(){
		
		try
		{
			logger.info("Starting to create userId verified by phone number with recovery option : "+this.additionalRecoveryOption);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
			
			if(this.additionalRecoveryOption==null || this.phoneNumber==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);

			try{
				//Setting user's first and last name to "Wrong" in ESD is to verify that first and last name values which are populated the Step 2 page are indeed returned from the DAS instead of ESD.
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_FIRST_NAME.getValue(), "Wrong");
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_LAST_NAME.getValue(), "Wrong");
			} catch (Exception e) {
				produceError(sourceId, new TestExecutionException("Failed to update account : "+account.getAccountId()+ " >> "+e.getMessage(), ITestIdmCreateUserID.class));
			}
			
			Object obj = signInToXfinity.createUserID();
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByMobile();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByMobile) {
				UserByMobile mobVerification = (UserByMobile) obj;
				mobVerification.enterMobileNumber(this.phoneNumber);
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmCreateUserID.class));
				}
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.phoneNumber);
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmCreateUserID.class));
				}
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			} else if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmCreateUserID.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				
				if(!createUserObj.getUserFirstName().equals(firstName)) {
					produceError(sourceId, new TestExecutionException("First Name value returned from the DAS not populate the Step 2 page", ITestIdmCreateUserID.class));
				}
				
				if (!this.userId.equalsIgnoreCase("NA")) {
					createUserObj.enterUserName(this.userId);
				}
				else {
					createUserObj.usePersonalEmailAsUserName();
					if (this.alternateEmail==null)
						produceError(sourceId, new TestExecutionException("null alternate email from data provider", ITestIdmCreateUserID.class));
					createUserObj.enterEmail(this.alternateEmail);
				}
				
				createUserObj.enterPassword(this.password);
				createUserObj.reEnterPassword(this.password);
				if (keepMeSignedIn!=null && keepMeSignedIn.equalsIgnoreCase("YES"))
					createUserObj.keepSignedIn();
				if (this.additionalRecoveryOption.equalsIgnoreCase("EMAIL")) {
					if (this.alternateEmail==null)
						produceError(sourceId, new TestExecutionException("null alternate email from data provider", ITestIdmCreateUserID.class));
					createUserObj.enterAlternateEmail(this.alternateEmail);
				}
				createUserObj.selectDefaultSecurityQuestion();
				createUserObj.setDefaultAnswer();
				createUserObj.agreeToTermOfServiceAndPrivacyPolicy();
				obj = createUserObj.submitUserInformation();
			}
			
			if (obj != null && obj instanceof VerifyUserAfterCreation) {
				VerifyUserAfterCreation verifyUserObj = (VerifyUserAfterCreation) obj;
				if (this.additionalRecoveryOption.toUpperCase().contains("EMAIL")) {
					userVerificationDetails = verifyUserObj.validatePage(true);
				} 
				else {
					userVerificationDetails = verifyUserObj.validatePage(false);
				}
				if (!userVerificationDetails.get(ICimaCommonConstants.IDM_USER_CREATE_STATUS).equalsIgnoreCase(ICommonConstants.OPERATION_STATUS_SUCCESS)) {
					String causeOfFailure = verifyUserObj.findCauseofFailure(userVerificationDetails);
					produceError(sourceId, new TestExecutionException("Post user creation verification has failed. "+causeOfFailure, ITestIdmCreateUserID.class));
				}
				if (this.alternateEmail!=null){
					emailLinkForVerification = readLog("VERIFICATION_EMAIL", LogReader.RegexArgument.EMAIL, this.alternateEmail);
				}
				obj = verifyUserObj.clickContinue();
			}
			
			/*if (obj != null && !(obj instanceof XfinityHome)) {
				produceError(sourceId, new TestExecutionException("User has failed to land to Xfinity portal page after userId creation", ITestIdmCreateUserID.class));
			}*/
			
			/* Verify LDAP before click the email link */
			LDAPCustomer cust1 = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.BEGINNING_WITH,LdapAttribute.CUSTOMER_MAIL,this.userId));
			if (this.additionalRecoveryOption.contains("Email")) {
				if (cust1.getCstContactEmail() !=null) {
					produceError(sourceId, new TestExecutionException("cstContact Email is wrongly set in ESD", ITestIdmCreateUserID.class));
				}
				if (cust1.getCstContactEmailStatus() !=null) 
					produceError(sourceId, new TestExecutionException("cstContact Email status is wrongly set to Delivered in ESD", ITestIdmCreateUserID.class));
				
				if (cust1.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) 
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is wrongly set to contactEmail in ESD", ITestIdmCreateUserID.class));
			}
			else {
				if (!cust1.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to comcastEmail in ESD", ITestIdmCreateUserID.class));
				}
			}
			
			if (this.emailLinkForVerification != null) {
				EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
				EmailConfirmationLinkobj.setPageFlowId(this.pfId);
				obj = EmailConfirmationLinkobj.verify(emailLinkForVerification);
				if (obj != null && !(obj instanceof EmailValidationSuccess)) {
					produceError(sourceId, new TestExecutionException("Unable to validate user alternate email address", ITestIdmCreateUserID.class));
				}
				
			} else if (this.alternateEmail!=null && this.emailLinkForVerification==null) {
				produceError(sourceId, new TestExecutionException("Email validation link not found", ITestIdmCreateUserID.class));
			}
			
			/* Verify LDAP */
			LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.BEGINNING_WITH,LdapAttribute.CUSTOMER_MAIL,this.userId));
			if (this.alternateEmail != null) {
				if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
					produceError(sourceId, new TestExecutionException("cstContact Email null or not set in ESD", ITestIdmCreateUserID.class));
				}
				if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) 
					produceError(sourceId, new TestExecutionException("cstContact Email status is not set to Delivered in ESD", ITestIdmCreateUserID.class));
				
				if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) 
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to contactEmail in ESD", ITestIdmCreateUserID.class));
			}
			else {
				if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to comcastEmail in ESD", ITestIdmCreateUserID.class));
				}
			}
			this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	@Test
	public void createUserIDByAccountNumberAndStreetAddressOrPhone(){
		
		try
		{
			logger.info("Starting to create userId verified by account number and street address with recovery option : "+this.additionalRecoveryOption);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
			
			if(this.additionalRecoveryOption==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			try{
				final String WRONG = "Wrong";
				//This is 
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_FIRST_NAME.getValue(), WRONG);
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_LAST_NAME.getValue(), WRONG);
			} catch (Exception e) {
				produceError(sourceId, new TestExecutionException("Failed to update account : "+account.getAccountId()+ " >> "+e.getMessage(), ITestIdmCreateUserID.class));
			}
			
			Object obj = signInToXfinity.createUserID();
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByAccountNumber();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByAccountNumber) {
				UserByAccountNumber accountVerification = (UserByAccountNumber) obj;
				accountVerification.enterAccountNumber(accountNo);
				if (this.userIdentifierType.toUpperCase().equals("STADDRESS"))
					accountVerification.enterStreetAddress(address);
				else if (this.userIdentifierType.toUpperCase().equals("PHONE"))
					accountVerification.enterPhoneNumber(this.phoneNumber);
				obj = accountVerification.Continue();
			}

			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				
				if(!createUserObj.getUserFirstName().equals(firstName)) {
					produceError(sourceId, new TestExecutionException("First Name value returned from the DAS not populate the Step 2 page", ITestIdmCreateUserID.class));
				}
				
				if (!this.userId.equalsIgnoreCase("NA")) {
					createUserObj.enterUserName(this.userId);
				}
				else {
					createUserObj.usePersonalEmailAsUserName();
					if (this.alternateEmail==null)
						produceError(sourceId, new TestExecutionException("null alternate email from data provider", ITestIdmCreateUserID.class));
					createUserObj.enterEmail(this.alternateEmail);
				}
				
				createUserObj.enterPassword(this.password);
				createUserObj.reEnterPassword(this.password);
				if (keepMeSignedIn!=null && keepMeSignedIn.equalsIgnoreCase("YES"))
					createUserObj.keepSignedIn();
				if (this.additionalRecoveryOption.contains("Email")) {
					if (this.alternateEmail==null)
						produceError(sourceId, new TestExecutionException("null alternate email from data provider", ITestIdmCreateUserID.class));
					createUserObj.enterAlternateEmail(this.alternateEmail);
				}
				if (this.additionalRecoveryOption.contains("Phone")) {
					if (this.phoneNumber==null)
						produceError(sourceId, new TestExecutionException("null phone number from data provider", ITestIdmCreateUserID.class));
					createUserObj.verifyMobilePhone(phoneNumber);
					String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.phoneNumber);
					if (smscode == null) {
						String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
						produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmCreateUserID.class));
					}
					createUserObj.enterSMSCode(smscode);
				}
				createUserObj.selectDefaultSecurityQuestion();
				createUserObj.setDefaultAnswer();
				createUserObj.agreeToTermOfServiceAndPrivacyPolicy();
				obj = createUserObj.submitUserInformation();
			}
			
			
			if (obj != null && obj instanceof VerifyUserAfterCreation) {
				VerifyUserAfterCreation verifyUserObj = (VerifyUserAfterCreation) obj;
				if (this.additionalRecoveryOption.toUpperCase().contains("EMAIL")) {
					userVerificationDetails = verifyUserObj.validatePage(true);
				} 
				else {
					userVerificationDetails = verifyUserObj.validatePage(false);
				}
				if (!userVerificationDetails.get(ICimaCommonConstants.IDM_USER_CREATE_STATUS).equalsIgnoreCase(ICommonConstants.OPERATION_STATUS_SUCCESS)) {
					String causeOfFailure = verifyUserObj.findCauseofFailure(userVerificationDetails);
					produceError(sourceId, new TestExecutionException("Post user creation verification has failed. "+causeOfFailure, ITestIdmCreateUserID.class));
				}
				if (this.alternateEmail!=null){
					emailLinkForVerification = readLog("VERIFICATION_EMAIL", LogReader.RegexArgument.EMAIL, this.alternateEmail);
				}
				obj = verifyUserObj.clickContinue();
			}
			
			/*if (obj != null && !(obj instanceof XfinityHome)) {
				produceError(sourceId, new TestExecutionException("User has failed to land to Xfinity portal page after userId creation", ITestIdmCreateUserID.class));
			}*/
			
			/* Verify LDAP before click the email link */
			LDAPCustomer cust1 = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.BEGINNING_WITH,LdapAttribute.CUSTOMER_MAIL,this.userId));
			if (this.additionalRecoveryOption.contains("Email")) {
				if (cust1.getCstContactEmail() !=null) {
					produceError(sourceId, new TestExecutionException("cstContact Email is wrongly set in ESD", ITestIdmCreateUserID.class));
				}
				if (cust1.getCstContactEmailStatus() !=null) 
					produceError(sourceId, new TestExecutionException("cstContact Email status is wrongly set to Delivered in ESD", ITestIdmCreateUserID.class));
				
				if (cust1.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) 
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is wrongly set to contactEmail in ESD", ITestIdmCreateUserID.class));
			}
			else {
				if (!cust1.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to comcastEmail in ESD", ITestIdmCreateUserID.class));
				}
			}
			
			if (this.emailLinkForVerification != null) {
				EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
				EmailConfirmationLinkobj.setPageFlowId(this.pfId);
				obj = EmailConfirmationLinkobj.verify(emailLinkForVerification);
				if (obj != null && !(obj instanceof EmailValidationSuccess)) {
					produceError(sourceId, new TestExecutionException("Unable to validate user alternate email address", ITestIdmCreateUserID.class));
				}
			} else if (this.alternateEmail!=null && this.emailLinkForVerification==null) {
				produceError(sourceId, new TestExecutionException("Email validation link not found", ITestIdmCreateUserID.class));
			}
			
			/* Verify LDAP */
			LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.BEGINNING_WITH,LdapAttribute.CUSTOMER_MAIL,this.userId));
			if (this.alternateEmail != null) {
				if (cust.getCstContactEmail()==null || !cust.getCstContactEmail().equals(this.alternateEmail) ) {
					produceError(sourceId, new TestExecutionException("cstContact Email null or not set in ESD", ITestIdmCreateUserID.class));
				}
				if (!cust.getCstContactEmailStatus().equalsIgnoreCase("Delivered")) 
					produceError(sourceId, new TestExecutionException("cstContact Email status is not set to Delivered in ESD", ITestIdmCreateUserID.class));
				
				if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("contactEmail")) 
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to contactEmail in ESD", ITestIdmCreateUserID.class));
			}
			else {
				if (!cust.getCstPreferredCommunicationEmail().equalsIgnoreCase("comcastEmail")) {
					produceError(sourceId, new TestExecutionException("cstPreferredCommunicationEmail is not set to comcastEmail in ESD", ITestIdmCreateUserID.class));
				}
			}
			this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
		if(user!=null && user.getPrimaryKey()>0)
			unlockResource(LockableResource.FRESH_USER, user.getPrimaryKey(), sourceId);
		
		if (account!=null && account.getAccountId()!=null)
			unlockResource(LockableResource.ACCOUNT,account.getAccountId(),sourceId);
		
		List<String> users = new ArrayList<String>();
		if (!this.userId.equalsIgnoreCase("NA"))
			users.add(this.userId);
		else if (this.userId.equalsIgnoreCase("NA")) {
			users.add(this.alternateEmail.substring(0, this.alternateEmail.indexOf("@")));
		}
		try {
			ldap.purgeUsers(users);
			ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_FIRST_NAME.getValue(), account.getFirstName());
			ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_BILL_TO_LAST_NAME.getValue(), account.getLastName());
		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Failed to purge user : "+users+ " >> "+e.getMessage(), ITestIdmPasswordReset.class));
		}
		try {
			updateSauceLabsTestStatus(sessId,testStatus);
		} catch (Exception e) {
			logger.error("Error occured while updating test execution status in SauceLabs : "+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while updating test execution status in SauceLabs :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
		
	}
	
	//******************************** private methods & variables **********************************//*
	
	private String sourceId = null;
	private String phoneNumber = null;
	private String alternateEmail = null;
	private String uid = null;
	private String userId = null;
	private String password = null;
	private String accountNo = null;
	private String address = null;
	private String userIdentifierType = null;
	private String emailLinkForVerification = null;
	private String keepMeSignedIn = null;
	private String additionalRecoveryOption = null;
	private LDAPInterface ldap = null;
	private WebDriver browser;
	private SignInToXfinity signInToXfinity;
	private String executionEnvironment = null;
	private String pfId = null;
	private String saucePlatform = null;
	private String sauceDeviceType = null;
	private String sauceBrowser = null;
	private String currentTestMethod;		
	private Map<String, Object> testData = null;
	private Map<String, Object> filter = null;
	private Map<String,String> userVerificationDetails = null;
	Set<String> keySet = null;
	private FreshUsers user = null;
	private AccountCache.Account account = null;
	private String sessId = null;
	private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
	private String firstName = null;
	private String lastName = null;
	
	private void getTestData() throws Exception{

		if(testData==null){
			
			logger.debug("Calling data provider with keySet = "+keySet.toString());
			
			testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
			
			if(testData!=null && testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue())!=null){
				account = (AccountCache.Account)testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				if(account!=null) {
					firstName = account.getFirstName();
					lastName = account.getLastName();
					phoneNumber = account.getPhoneNumber();
					accountNo = account.getAccountId();
					address = account.getAddress();
				}
			}
			
			if(testData!=null && testData.get(IDMGeneralKeys.FRESH_USER.getValue())!=null){
				user = (FreshUsers)testData.get(IDMGeneralKeys.FRESH_USER.getValue());
				if (user!=null)
					alternateEmail = user.getAlternativeEmail();
			}
			
			password = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
		}		
		
		logger.debug("Getting data from data provider :: Phone Number:  "+phoneNumber +" and alternate Email: "+alternateEmail);
		logger.info("Data provider has been called for fetching test data");
	}

	
	
	private static Logger logger = LoggerFactory.getLogger(ITestIdmCreateUserID.class);



}
