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
import com.comcast.cima.test.ui.pageobject.InformationMismatch;
import com.comcast.cima.test.ui.pageobject.SMSExpired;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.ui.router.PageError;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

public class ITestIdmInvalidUsernameLookup extends IdmTestDataProvider {
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
			pageOption = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION);

			String filterAlterMail = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
			
			filter = new HashMap<String, Object>();
			keySet = new HashSet<String>();

			if(filterAlterMail!=null)
				filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterMail);
			if(ICimaCommonConstants.PAGE_ATTRIBUTE_ADDRESS.equals(pageOption))
				filter.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			else if(ICimaCommonConstants.PAGE_ATTRIBUTE_PHONE.equals(pageOption))
				filter.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			
			phoneNumberType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE_TYPE);
			invalidPhoneNumber = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE);
			smsCodeType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE_TYPE);
			smsCode = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE);
			
			saucePlatform = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM);
			sauceDeviceType = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE);
			sauceBrowser = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER);
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			executionEnvironment = getCurrentEnvironment();
			this.getTestData();
			pfId = PageNavigator.start("createUserIDInvalidScenarios");
			browser = getBrowserInstance(currentTestMethod,Platforms.valueOf(saucePlatform),Types.valueOf(sauceDeviceType),sauceBrowser,true);
			sessId = ((RemoteWebDriver)browser).getSessionId().toString();
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
	}
	
	
	@Test
	public void tryUsernameLookupByInvalidPhone(){
		final String invalidPhoneNumberLengthErrorMessage = "This phone number is invalid.";
		final String emptyPhoneNumberErrorMessage = "Please enter your phone number to proceed. It is the number associated with your Comcast account.";
		final String invalidPhoneNumberErrorMessage = "This information doesn't match our records.";
		
		try
		{
			logger.info("Starting to username lookup verified by invalid phone number : "+this.invalidPhoneNumber);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				mobVerification.enterMobileNumber(this.invalidPhoneNumber);
				obj = mobVerification.Continue();
				
				if (phoneNumberType.equalsIgnoreCase("INVALIDLENGTH")) {
					String err = mobVerification.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(invalidPhoneNumberLengthErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number length", ITestIdmInvalidUsernameLookup.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with invalid phone number length", ITestIdmInvalidUsernameLookup.class));
					}
				} else if (phoneNumberType.equalsIgnoreCase("EMPTY")) {
					String err = mobVerification.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(emptyPhoneNumberErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty phone number", ITestIdmInvalidUsernameLookup.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with empty phone number", ITestIdmInvalidUsernameLookup.class));
					}
				} else if (phoneNumberType.equalsIgnoreCase("INVALID")) {
					if (obj != null && obj instanceof InformationMismatch) {
						InformationMismatch espInfMismatch = (InformationMismatch) obj;
						if (!espInfMismatch.getHeaderMessage().equalsIgnoreCase(invalidPhoneNumberErrorMessage)) {
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number", ITestIdmInvalidUsernameLookup.class));
						}
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && !(obj instanceof PageError) && !(obj instanceof InformationMismatch)) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmInvalidUsernameLookup.class));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void tryUsernameLookupByInvalidSMSCode(){
		String emptySMSCodeErrorMessage = "Please enter verification code.";
		String invalidSMSCodeErrorMessage = "That wasn't quite right.";
		int i = 0;
		
		try
		{
			logger.info("Starting to username lookup verified by invalid sms code : "+this.smsCode);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				mobVerification.enterMobileNumber(this.phoneNumber);
				obj = mobVerification.Continue();
			}
			
			if (obj != null && obj instanceof UserSMSConfirmation) {
				UserSMSConfirmation smsConfirmationObj = (UserSMSConfirmation) obj;
				
				if (this.smsCodeType.equalsIgnoreCase("EMPTY")) {
					smsConfirmationObj.enterSMSCode(this.smsCode);
					obj = smsConfirmationObj.Continue();
					String err = smsConfirmationObj.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(emptySMSCodeErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty sms code", ITestIdmInvalidUsernameLookup.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with empty sms code", ITestIdmInvalidUsernameLookup.class));
					}
				}
				else if (this.smsCodeType.equalsIgnoreCase("INVALID")) {
					smsConfirmationObj.enterSMSCode(this.smsCode);
					obj = smsConfirmationObj.Continue();
					if (obj != null && obj instanceof SMSExpired) {
						SMSExpired invalidSMSCodePage = (SMSExpired) obj;
						String msg = invalidSMSCodePage.getHeaderMessage();
						if (msg == null)
							produceError(sourceId, new TestExecutionException("No SMS code invalid message found", ITestIdmInvalidUsernameLookup.class));
						else if (!msg.equalsIgnoreCase(invalidSMSCodeErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected SMS code invalid message not found", ITestIdmInvalidUsernameLookup.class));
					}
					if (obj != null && obj instanceof PageError) {
						produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmInvalidUsernameLookup.class));
					}
				}
				else if (this.smsCodeType.equalsIgnoreCase("INVALIDTHRICE")) {
					String[] sms = this.smsCode.split(",");
					smsConfirmationObj.enterSMSCode(sms[i++]);
					obj = smsConfirmationObj.Continue();
					if (obj != null && obj instanceof SMSExpired) {
						SMSExpired invalidSMSCodePage = (SMSExpired) obj;
						while (i <= 2) {
							invalidSMSCodePage.enterSMSCode(sms[i]);
							obj = invalidSMSCodePage.tryAgain();
							i++;
						}
						if (obj != null && !(obj instanceof SecurityCheck)) {
							produceError(sourceId, new TestExecutionException("Expected security check page not found after 3 consecutive invalid sms code", ITestIdmInvalidUsernameLookup.class));
						}
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
		if (account!=null && account.getAccountId()!=null)
			unlockResource(LockableResource.ACCOUNT,account.getAccountId(),sourceId);
		if(userAttributes!=null && userAttributes.getUserId()!=null)
			unlockResource(LockableResource.USER, userAttributes.getUserId(), sourceId);
		try {
			updateSauceLabsTestStatus(sessId,testStatus);
		} catch (Exception e) {
			logger.error("Error occured while updating test execution status in SauceLabs : "+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while updating test execution status in SauceLabs :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
	}
	
	//******************************** private methods & variables **********************************//*
	
		private String sourceId = null;
		private String phoneNumber = null;
		private String invalidPhoneNumber = null;
		private String phoneNumberType = null;
		private String smsCode = null;
		private String smsCodeType = null;
		private String accountNo = null;
		private String address = null;
		private String firstName = null;
		private String lastName = null;
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
		private AccountCache.Account account = null;
		private String sessId = null;
		private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
		private String pageOption = null;
		private UserAttributesCache.Attribute userAttributes = null;
		
		private void getTestData() throws Exception{

			
			if(testData==null){
				if("ADDRESS".equals(pageOption) || "PHONE".equals(pageOption))
					keySet = new HashSet<String>(Arrays.asList(	IDMGeneralKeys.USER_DETAILS.getValue()));
				
				logger.debug("Calling data provider with keySet = "+keySet.toString());
				
				testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
				
				if(testData!=null && testData.get(IDMGeneralKeys.USER_DETAILS.getValue())!=null){
					List<Map<String, Object>> lst = (List<Map<String, Object>>)testData.get(IDMGeneralKeys.USER_DETAILS.getValue());
					if(lst!=null && lst.get(0)!=null){
						Map<String, Object> map = lst.get(0);
						if(map!=null && map.get(KEY_USER)!=null && map.get(KEY_ACCOUNT)!=null){
							this.account = (AccountCache.Account)map.get(KEY_ACCOUNT);
							this.userAttributes = (UserAttributesCache.Attribute)map.get(KEY_USER);
							this.phoneNumber = this.account.getPhoneNumber();
						}
					}
				}
			}
				
			logger.debug("Getting data from data provider :: Phone Number:  "+phoneNumber);
			logger.info("Data provider has been called for fetching test data");
		}

		private String getUserName() throws Exception {
			UserNameGeneratorService userNameGen = ObjectInitializer.getUserNameGeneratorService();
			return userNameGen.next();
		}
		
		private static Logger logger = LoggerFactory.getLogger(ITestIdmCreateUserIDInvalidScenarios.class);

}
