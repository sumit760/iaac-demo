package com.comcast.cima.test.ui.tests;

import java.util.Arrays;
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
import com.comcast.cima.test.ui.pageobject.CreateUserConfirmation;
import com.comcast.cima.test.ui.pageobject.InformationMismatch;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserLookupSignUp;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.UsernameLookupConfirmtaion;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.ui.router.PageError;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

public class ITestIdmUsernameLookup extends IdmTestDataProvider {
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
			pageOption = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION);
			pageOption2 = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION_TWO);
			
			String filterAlterMail = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
			filter =  new HashMap<String, Object>();
				
			if(filterAlterMail!=null)
				filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterMail);
			
			if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
				filter.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
				filter.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			
			saucePlatform = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM);
			sauceDeviceType = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE);
			sauceBrowser = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER);
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			this.getTestData();
			ldap = ObjectInitializer.getLdapService();
			pfId = PageNavigator.start("UserName_Lookup");
			browser = getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
			sessId = ((RemoteWebDriver)browser).getSessionId().toString();
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmUsernameLookup.class));
		}
	}

	
	@Test
	public void lookupUsingValidAddressAndOtherValidParam(){
		logger.info("Validate username lookup using valid account number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.lookupUser();
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByAccountNumber();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByAccountNumber) {
				UserByAccountNumber accountCheck = (UserByAccountNumber) obj;
				accountCheck.enterAccountNumber(this.account.getAccountId());
				
				if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
					accountCheck.enterStreetAddress(this.account.getAddress());
				else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
					accountCheck.enterPhoneNumber(this.account.getPhoneNumber());
				
				obj = accountCheck.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingValidAddressAndOtherValidParam method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingValidAddressAndOtherValidParam method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
		
	
	@Test
	public void lookupUsingValidPhone(){
		logger.info("Validate username lookup using valid phone number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.lookupUser();
			
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
				mobVerification.enterMobileNumber(this.account.getPhoneNumber());
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmUsernameLookup.class));
				}				
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
				}
				logger.info("SMS code found : "+smscode);
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			}
			
			if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingValidPhone method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingValidPhone method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupUsingFreshAccountWithValidDataByAccount(){
		logger.info("Validate username lookup using valid fresh account number with no-existing user by account number");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.account==null || this.pageOption==null || this.browser==null || this.pageOption2==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.lookupUser();
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByAccountNumber();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByAccountNumber) {
				UserByAccountNumber accountCheck = (UserByAccountNumber) obj;
				accountCheck.enterAccountNumber(this.account.getAccountId());
				if(pageOption2.equals(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS)) {
					accountCheck.enterStreetAddress(this.account.getAddress());
				}
				else if(pageOption2.equals(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE)) {
					accountCheck.enterPhoneNumber(this.account.getPhoneNumber());
				}
				obj = accountCheck.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UserLookupSignUp) {
				UserLookupSignUp createUser = (UserLookupSignUp) obj;
				
				if(createUser.isCreateUserPresent())
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				else	
					produceError(sourceId, new TestExecutionException("The create user id page is incomplete", ITestIdmUsernameLookup.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingFreshAccountWithValidDataByAccount method: {}",e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingFreshAccountWithValidDataByAccount method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupUsingFreshAccountWithValidDataByPhone(){
		logger.info("Validate username lookup using valid fresh account number with no-existing user by phone number");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.account==null || this.pageOption==null || this.browser==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.lookupUser();
			
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
				mobVerification.enterMobileNumber(this.account.getPhoneNumber());
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmUsernameLookup.class));
				}
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
				}
				logger.info("SMS code found : "+smscode);
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			}
			
			if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UserLookupSignUp) {
				UserLookupSignUp createUser = (UserLookupSignUp) obj;
				
				if(createUser.isCreateUserPresent())
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				else	
					produceError(sourceId, new TestExecutionException("The create user id page is incomplete", ITestIdmUsernameLookup.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingFreshAccountWithValidDataByPhone method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingFreshAccountWithValidDataByPhone method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupInCreateUIDUsingValidAddress(){
		logger.info("Validate username lookup in create UID using valid account number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
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
				UserByAccountNumber accountCheck = (UserByAccountNumber) obj;
				accountCheck.enterAccountNumber(this.account.getAccountId());
				
				if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
					accountCheck.enterStreetAddress(this.account.getAddress());
				else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
					accountCheck.enterPhoneNumber(this.account.getPhoneNumber());
				
				obj = accountCheck.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof CreateUserConfirmation) {
				CreateUserConfirmation confirm = (CreateUserConfirmation) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupInCreateUIDUsingValidAddress method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupInCreateUIDUsingValidAddress method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
		
	
	@Test
	public void lookupInCreateUIDUsingValidPhone(){
		logger.info("Validate username lookup in create UID using valid phone number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
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
				mobVerification.enterMobileNumber(this.account.getPhoneNumber());
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmUsernameLookup.class));
				}
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
				}
				logger.info("SMS code found : "+smscode);
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			}
			
			if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof CreateUserConfirmation) {
				CreateUserConfirmation confirm = (CreateUserConfirmation) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupInCreateUIDUsingValidPhone method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupInCreateUIDUsingValidPhone method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupThroughPasswdResetUsingValidAccount(){
		logger.info("Validate username lookup in fotgot password using valid account number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				obj = ResetPasswordGatewayobj.continueUserNameLookup();
			}
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByAccountNumber();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByAccountNumber) {
				UserByAccountNumber accountCheck = (UserByAccountNumber) obj;
				accountCheck.enterAccountNumber(this.account.getAccountId());
				
				if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
					accountCheck.enterStreetAddress(this.account.getAddress());
				else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
					accountCheck.enterPhoneNumber(this.account.getPhoneNumber());
				
				obj = accountCheck.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
			}
			this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupInForgotPWUsingValidAddress method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupInForgotPWUsingValidAddress method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
		
	
	@Test
	public void lookupInForgotPWUsingValidPhone(){
		logger.info("Validate username lookup in fotgot password using valid phone number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				obj = ResetPasswordGatewayobj.continueUserNameLookup();
			}
			
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
				mobVerification.enterMobileNumber(this.account.getPhoneNumber());
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmUsernameLookup.class));
				}
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
				}
				logger.info("SMS code found : "+smscode);
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			}
			
			if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
			}
			
			this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupInForgotPWUsingValidPhone method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupInForgotPWUsingValidPhone method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupUsingValidBusinessAddressAndOtherValidParam(){
		logger.info("Validate username lookup using valid business account number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			try{
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_ACCOUNT_TYPE.getValue(), "B");
			} catch (Exception e) {
				logger.error("Error occured while testing lookupUsingValidBusinessAddressAndOtherValidParam method: ", e);
				produceError(sourceId, new TestExecutionException("Failed to update account : "+account.getAccountId()+ " >> "+e.getMessage(), ITestIdmUsernameLookup.class));
			}
			Object obj = signInToXfinity.lookupUser();
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity identity = (VerifyIdentity) obj;
				obj = identity.verifyIdentityByAccountNumber();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck check = (SecurityCheck) obj;
				obj = check.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof UserByAccountNumber) {
				UserByAccountNumber accountCheck = (UserByAccountNumber) obj;
				accountCheck.enterAccountNumber(this.account.getAccountId());
				
				if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
					accountCheck.enterStreetAddress(this.account.getAddress());
				else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
					accountCheck.enterPhoneNumber(this.account.getPhoneNumber());
				
				obj = accountCheck.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingValidBusinessAddressAndOtherValidParam method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingValidBusinessAddressAndOtherValidParam method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	
	@Test
	public void lookupUsingValidBusinessPhone(){
		logger.info("Validate username lookup using valid business user phone number and choosen option: "+this.pageOption);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.pageOption==null || this.browser==null || this.account==null || this.userAttributes==null) {
				throw new IllegalStateException(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION + " PageOption: " + this.pageOption + " Browser: " + this.browser + " Account: " + this.account + 
						" UserAttributes: " + this.userAttributes);
			}
			
			try{
				ldap.updateAttributeOfAuthorizationObject(this.account.getAccountId(), LdapAttribute.AUTHORIZATION_ACCOUNT_TYPE.getValue(), "B");
			} catch (Exception e) {
				logger.error("Error occured while testing lookupUsingValidBusinessPhone method: ", e);
				produceError(sourceId, new TestExecutionException("Failed to update account : "+account.getAccountId()+ " >> "+e.getMessage(), ITestIdmUsernameLookup.class));
			}
			Object obj = signInToXfinity.lookupUser();
			
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
				mobVerification.enterMobileNumber(this.account.getPhoneNumber());
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				if (!smsConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No SMS sent confirmation message found in the page", ITestIdmUsernameLookup.class));
				}				
				String smscode=readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (smscode == null) {
					String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
				}
				logger.info("SMS code found : "+smscode);
				smsConfirmationObj.enterSMSCode(smscode);
				obj = smsConfirmationObj.Continue();
			}
			
			if (obj != null && obj instanceof PageError) {
				String err = readLog("USR_SMS_VERIFICATION_ERR", null, null);
				produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log. Error encountered : "+err, ITestIdmUsernameLookup.class));
			}
			
			if (obj != null && obj instanceof InformationMismatch)
				produceError(sourceId, new TestExecutionException("The information provided is either incorrect or it is possibly an ESP service Error!", ITestIdmUsernameLookup.class));
			
			if (obj != null && obj instanceof UsernameLookupConfirmtaion) {
				UsernameLookupConfirmtaion confirm = (UsernameLookupConfirmtaion) obj;
				String userName = confirm.getUserName();
				String email="";
				if(userAttributes.getAlterEmail()!=null) {
					email = confirm.getThirdPartyEmail();
				}
				if(StringUtility.isStringEmpty(userName))
					produceError(sourceId, new TestExecutionException("Not able to pick up user name from confirmation page or No username found!", ITestIdmUsernameLookup.class));
				
				if(StringUtility.isStringEmpty(email) && userAttributes.getAlterEmail()!=null) {
					produceError(sourceId, new TestExecutionException("Not able to pick up third party email from confirmation page or No email found!", ITestIdmUsernameLookup.class));
				}		
				obj = confirm.confirmUsername();
				if (obj != null && obj instanceof SignInToXfinity) {
					SignInToXfinity lastPage = (SignInToXfinity) obj;
					if(!lastPage.checkUserName(userName))
						produceError(sourceId, new TestExecutionException("User name not matched in the sign in page!", ITestIdmUsernameLookup.class));
					
					this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				}
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing lookupUsingValidBusinessPhone method: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while testing lookupUsingValidBusinessPhone method :"+e.getMessage(), ITestIdmUsernameLookup.class));
		} 
	}
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
		if(userAttributes!=null && userAttributes.getUserId()!=null)
			unlockResource(LockableResource.USER, userAttributes.getUserId(), sourceId);	
		
		if(IDMGeneralKeys.FRESH_ACCOUNT.getValue().equals(pageOption) && account!=null && account.getAccountId()!=null)
			unlockResource(LockableResource.ACCOUNT, account.getAccountId(), sourceId);
		try{
			ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_ACCOUNT_TYPE.getValue(), "R");
		} catch (Exception e) {
			logger.error("Error occured while testing cleanUp method: ", e);
			produceError(sourceId, new TestExecutionException("Failed to update account : "+account.getAccountId()+ " >> "+e.getMessage(), ITestIdmUsernameLookup.class));
		}
		try {
			updateSauceLabsTestStatus(sessId,testStatus);
		} catch (Exception e) {
			logger.error("Error occured while updating test execution status in SauceLabs: ", e);
			produceError(sourceId, new TestExecutionException("Error occured while updating test execution status in SauceLabs :"+e.getMessage(), ITestIdmUsernameLookup.class));
		}
	}
	
	//******************************** private methods & variables **********************************//*
	
		private String sourceId = null;
		private String pageOption = null;
		private String pageOption2 = null;
		private WebDriver browser;
		private String sessId = null;
		private SignInToXfinity signInToXfinity;
		private String pfId = null;
		private String saucePlatform = null;
		private String sauceDeviceType = null;
		private String sauceBrowser = null;
		private String currentTestMethod;
		private Map<String, Object> filter = null;
		private Map<String, Object> testData = null;
		private UserAttributesCache.Attribute userAttributes = null;
		private AccountCache.Account account = null;
		private LDAPInterface ldap = null;
		private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
		
		
		private void getTestData() throws Exception{
			Set<String> keySet = null;
				
			if(testData==null){
				if(IDMGeneralKeys.FRESH_ACCOUNT.getValue().equals(pageOption))
					keySet = new HashSet<String>(Arrays.asList(	IDMGeneralKeys.FRESH_ACCOUNT.getValue()));
				else
					keySet = new HashSet<String>(Arrays.asList(	IDMGeneralKeys.USER_DETAILS.getValue()));
				
				logger.debug("Calling data provider with keySet = "+keySet.toString());
				
				
				testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
				if(testData!=null){
					if(testData.get(IDMGeneralKeys.USER_DETAILS.getValue())!=null){
						List<Map<String, Object>> lst = (List<Map<String, Object>>)testData.get(IDMGeneralKeys.USER_DETAILS.getValue());
						if(lst!=null && lst.get(0)!=null){
							Map<String, Object> map = lst.get(0);
							if(map!=null && map.get(KEY_USER)!=null && map.get(KEY_ACCOUNT)!=null){
								this.account = (AccountCache.Account)map.get(KEY_ACCOUNT);
								this.userAttributes = (UserAttributesCache.Attribute)map.get(KEY_USER);
							}
						}
					}
					else if(testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue())!=null){
						account = (AccountCache.Account)testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
					}
				}
			}
				
			logger.debug("Getting data from data provider :: userAttributes: "+ this.userAttributes);
			logger.info("Data provider has been called for fetching test data");
		}
		
		private void unlockUser() {
			if(userAttributes!=null && userAttributes.getUserId()!=null)
				   unlockResource(LockableResource.USER, userAttributes.getUserId(), sourceId);
		}
		
		private static Logger logger = LoggerFactory.getLogger(ITestIdmUsernameLookup.class);
}
