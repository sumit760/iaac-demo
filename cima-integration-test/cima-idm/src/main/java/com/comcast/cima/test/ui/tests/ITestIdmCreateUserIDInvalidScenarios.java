package com.comcast.cima.test.ui.tests;

import java.util.HashMap;
import java.util.HashSet;
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
import com.comcast.cima.test.ui.pageobject.InformationMismatch;
import com.comcast.cima.test.ui.pageobject.SMSExpired;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
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

public class ITestIdmCreateUserIDInvalidScenarios extends IdmTestDataProvider {
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
			
			String filterAddress = localParams.get(IdmFilterKeys.ADDRESS.getValue());
			String filterPhone = localParams.get(IdmFilterKeys.PHONE.getValue());
			
			filter = new HashMap<String, Object>();
			keySet = new HashSet<String>();

			if(filterAddress!=null) {
				keySet.add(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				filter.put(IdmFilterKeys.ADDRESS.getValue(), filterAddress);
			}
			if(filterPhone!=null) {
				keySet.add(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				filter.put(IdmFilterKeys.PHONE.getValue(), filterPhone);
			}
			phoneNumberType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE_TYPE);
			phoneNumber = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE);
			smsCodeType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE_TYPE);
			smsCode = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE);
			userNameType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_NAME_TYPE);
			userName = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_NAME);
			passwordType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE);
			password = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD);
			sqaType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER_TYPE);
			sqa = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER);
			identityType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_IDENTITY_TYPE);
			identity = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_IDENTITY_VALUE);
			alternateEmail = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_ALT_EMAIL);
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
	public void tryCreateUserWithVerificationByInvalidPhone(){
		String invalidPhoneNumberLengthErrorMessage = "This phone number is invalid.";
		String emptyPhoneNumberErrorMessage = "Please enter your phone number to proceed. It is the number associated with your Comcast account.";
		String invalidPhoneNumberErrorMessage = "This information doesn't match our records.";
		
		try
		{
			logger.info("Starting to create userId verified by invalid phone number : "+this.phoneNumber);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				
				if (phoneNumberType.equalsIgnoreCase("INVALIDLENGTH")) {
					String err = mobVerification.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(invalidPhoneNumberLengthErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number length", ITestIdmCreateUserIDInvalidScenarios.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with invalid phone number length", ITestIdmCreateUserIDInvalidScenarios.class));
					}
				} else if (phoneNumberType.equalsIgnoreCase("EMPTY")) {
					String err = mobVerification.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(emptyPhoneNumberErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty phone number", ITestIdmCreateUserIDInvalidScenarios.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with empty phone number", ITestIdmCreateUserIDInvalidScenarios.class));
					}
				} else if (phoneNumberType.equalsIgnoreCase("INVALID")) {
					if (obj != null && obj instanceof InformationMismatch) {
						InformationMismatch espInfMismatch = (InformationMismatch) obj;
						if (!espInfMismatch.getHeaderMessage().equalsIgnoreCase(invalidPhoneNumberErrorMessage)) {
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number", ITestIdmCreateUserIDInvalidScenarios.class));
						}
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithVerificationByInvalidPhone method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryCreateUserWithVerificationByInvalidSMSCode(){
		String emptySMSCodeErrorMessage = "Please enter verification code.";
		String invalidSMSCodeErrorMessage = "That wasn't quite right.";
		int i = 0;
		
		try
		{
			logger.info("Starting to create userId verified by invalid sms code : "+this.smsCode);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				
				if (this.smsCodeType.equalsIgnoreCase("EMPTY")) {
					smsConfirmationObj.enterSMSCode(this.smsCode);
					obj = smsConfirmationObj.Continue();
					String err = smsConfirmationObj.getErrorMessage();
					if (err!=null) {
						if (!err.equalsIgnoreCase(emptySMSCodeErrorMessage)) 
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty sms code", ITestIdmCreateUserIDInvalidScenarios.class));
					} else {
						produceError(sourceId, new TestExecutionException("No error message found with empty sms code", ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.smsCodeType.equalsIgnoreCase("INVALID")) {
					smsConfirmationObj.enterSMSCode(this.smsCode);
					obj = smsConfirmationObj.Continue();
					if (obj != null && obj instanceof SMSExpired) {
						SMSExpired invalidSMSCodePage = (SMSExpired) obj;
						String msg = invalidSMSCodePage.getHeaderMessage();
						if (msg == null)
							produceError(sourceId, new TestExecutionException("No SMS code invalid message found", ITestIdmCreateUserIDInvalidScenarios.class));
						else if (!msg.equalsIgnoreCase(invalidSMSCodeErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected SMS code invalid message not found", ITestIdmCreateUserIDInvalidScenarios.class));
					}
					if (obj != null && obj instanceof PageError) {
						produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
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
							produceError(sourceId, new TestExecutionException("Expected security check page not found after 3 consecutive invalid sms code", ITestIdmCreateUserIDInvalidScenarios.class));
						}
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithVerificationByInvalidSMSCode method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryCreateUserWithInvalidUserName(){
		String emptyOrInvalidUserNameErrorMessage = "Your username must contain 3 to 32 characters.";
		String userNameWithSpaceErrorMessage = "Your username cannot contain spaces.";
		
		try
		{
			logger.info("Starting to create userId by invalid user name : "+this.userName);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				accountVerification.enterAccountNumber(this.accountNo);
				accountVerification.enterStreetAddress(this.address);
				obj = accountVerification.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch) {
				produceError(sourceId, new TestExecutionException("Unable to identify account, check account details provided against ESD", ITestIdmCreateUserIDInvalidScenarios.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				createUserObj.enterUserName(this.userName);
				createUserObj.submitUserInformation();
				Map<String,String> errMap = createUserObj.getErrorMesssage();
				
				if (!this.userNameType.equalsIgnoreCase("WTHSPACE")) {
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("userNameError")) {
						String err = errMap.get("userNameError");
						if (!err.equalsIgnoreCase(emptyOrInvalidUserNameErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid user :"+this.userName, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.userNameType.equalsIgnoreCase("WTHSPACE")) {
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("userNameError")) {
						String err = errMap.get("userNameError");
						if (!err.equalsIgnoreCase(userNameWithSpaceErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid user :"+this.userName, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithInvalidUserName method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryCreateUserWithInvalidPassword(){
		String errorEmptyPassword = "Please enter a password.";
		String errorEmptyConfirmPasword = "Please repeat your new password to confirm you entered exactly what you wanted.";
		String errorPasswordLessThanEightMoreThanSixteenChar = "Your password must contain 8 to 16 characters.";
		String errorPasswordEqualToUsername = "Your password must not contain your username.";
		String errorPasswordEqualToFirstName = "Your password must not contain your first name.";
		String errorPasswordEqualToLastName = "Your password must not contain your last name.";
		String errorPasswordHasNoLetter = "Your password must contain at least one letter.";
		String errorPasswordHasNoNumber = "Your password must contain a number and/or special character";
		String errorPasswordHasSpaces = "Your password must not contain space(s).";	
		String errorPasswordEasyToGuess = "Your password must not be easily guessed.";	
		String errorPasswordMismatch = "Passwords don't match";
		
		try
		{
			logger.info("Starting to create userId by invalid password : "+this.password+" and password type : "+this.passwordType);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				accountVerification.enterAccountNumber(this.accountNo);
				accountVerification.enterStreetAddress(this.address);
				obj = accountVerification.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch) {
				produceError(sourceId, new TestExecutionException("Unable to identify account, check account details provided against ESD", ITestIdmCreateUserIDInvalidScenarios.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				
				if (this.passwordType.equalsIgnoreCase("EMPTY")) {
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError") && errMap.containsKey("passwordReTypeError")) {
						String errPassword = errMap.get("invalidPasswordError");
						String errConfPassword = errMap.get("passwordReTypeError");
						if (!errPassword.equalsIgnoreCase(errorEmptyPassword) && !errPassword.equalsIgnoreCase(errorEmptyConfirmPasword))
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty password. Got "+errPassword+" and "+errConfPassword, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("LESSTHAN8") || this.passwordType.equalsIgnoreCase("MORETHAN16")) {
					createUserObj.enterPassword(this.password);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordLessThanEightMoreThanSixteenChar))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("USERNAME")) {
					String user = getUserName();
					createUserObj.enterUserName(user);
					createUserObj.enterPassword(user);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordEqualToUsername))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("FIRSTNAME")) {
					createUserObj.enterPassword(this.firstName);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordEqualToFirstName))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("LASTNAME")) {
					createUserObj.enterPassword(this.lastName);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordEqualToLastName))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("NOLETTER")) {
					createUserObj.enterPassword(this.password);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordHasNoLetter))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("NONUMBER")) {
					createUserObj.enterPassword(this.password);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.contains(errorPasswordHasNoNumber))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("WITHSPACE")) {
					createUserObj.enterPassword(this.password);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordHasSpaces))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("EASYTOGUESS")) {
					createUserObj.enterPassword(this.password);
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPasswordError")) {
						String err = errMap.get("invalidPasswordError");
						if (!err.equalsIgnoreCase(errorPasswordEasyToGuess))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid password :"+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.passwordType.equalsIgnoreCase("MISMATCH")) {
					String pass = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
					createUserObj.enterPassword("Ext"+pass);
					createUserObj.reEnterPassword(pass);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("passwordReTypeError")) {
						String err = errMap.get("passwordReTypeError");
						if (!err.equalsIgnoreCase(errorPasswordMismatch))
							produceError(sourceId, new TestExecutionException("Expected error message not found with password mismatch: "+this.password+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithInvalidPassword method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	@Test
	public void tryCreateUserWithInvalidAltEmail(){
		
		String invalidAltEmailErrorMessage = "This email address is invalid.";
		
		try
		{
			logger.info("Starting to create userId by invalid user alternate email : "+this.alternateEmail);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				accountVerification.enterAccountNumber(this.accountNo);
				accountVerification.enterStreetAddress(this.address);
				obj = accountVerification.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch) {
				produceError(sourceId, new TestExecutionException("Unable to identify account, check account details provided against ESD", ITestIdmCreateUserIDInvalidScenarios.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				createUserObj.enterAlternateEmail(this.alternateEmail);
				createUserObj.submitUserInformation();
				Map<String,String> errMap = createUserObj.getErrorMesssage();
				
				if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("altEmailInvalidError")) {
					String err = errMap.get("altEmailInvalidError");
					if (!err.equalsIgnoreCase(invalidAltEmailErrorMessage))
						produceError(sourceId, new TestExecutionException("Expected error message not found with invalid alternate email :"+this.alternateEmail+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithInvalidAltEmail method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryCreateUserWithInvalidMobilePhone(){
		
		String invalidPhoneNumberLengthErrorMessage = "This phone number is invalid.";
		String invalidPhoneNumberErrorMessage = "An error occurred while sending SMS, please try again later.";
		
		try
		{
			logger.info("Starting to create userId by invalid user alternate email : "+this.alternateEmail);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				accountVerification.enterAccountNumber(this.accountNo);
				accountVerification.enterStreetAddress(this.address);
				obj = accountVerification.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch) {
				produceError(sourceId, new TestExecutionException("Unable to identify account, check account details provided against ESD", ITestIdmCreateUserIDInvalidScenarios.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				createUserObj.enterMobilePhoneNumber(this.phoneNumber);
				
				if (this.phoneNumberType.equalsIgnoreCase("INVALIDLENGTH")) {
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("phoneNumErrorMsg")) {
						String err = errMap.get("phoneNumErrorMsg");
						if (!err.equalsIgnoreCase(invalidPhoneNumberLengthErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number :"+this.phoneNumber+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.phoneNumberType.equalsIgnoreCase("INVALID")) {
					createUserObj.verifyMobilePhone();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("phoneNumErrorMsg")) {
						String err = errMap.get("phoneNumErrorMsg");
						if (!err.equalsIgnoreCase(invalidPhoneNumberErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid phone number :"+this.phoneNumber+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithInvalidMobilePhone method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryCreateUserWithInvalidSecurityAnswer(){
		
		String blankSecurityQuestionErrorMessage = "Choose a security question for password recovery.";
		String blankOrInvalidSecurityAnswerErrorMessage = "Your answer must be between 3 and 25 characters.";
		
		try
		{
			logger.info("Starting to create userId by invalid security question and answer type: "+this.sqaType+" and value: "+this.sqa);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				accountVerification.enterAccountNumber(this.accountNo);
				accountVerification.enterStreetAddress(this.address);
				obj = accountVerification.Continue();
			}
			
			if (obj != null && obj instanceof InformationMismatch) {
				produceError(sourceId, new TestExecutionException("Unable to identify account, check account details provided against ESD", ITestIdmCreateUserIDInvalidScenarios.class));
			}
			
			if (obj != null && obj instanceof CreateUser) {
				CreateUser createUserObj = (CreateUser) obj;
				if (this.sqaType.equalsIgnoreCase("EMPTY")) {
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("secretQuestionError") && errMap.containsKey("secretAnswerError")) {
						String errSecretQuestion = errMap.get("secretQuestionError");
						String errSecretAnswer = errMap.get("secretAnswerError");
						if (!errSecretQuestion.equalsIgnoreCase(blankSecurityQuestionErrorMessage) && 
								!errSecretQuestion.equalsIgnoreCase(blankOrInvalidSecurityAnswerErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with empty security question and answer :"+" Got "+errSecretQuestion+" "+errSecretAnswer, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.sqaType.equalsIgnoreCase("LESSTHAN3") || this.sqaType.equalsIgnoreCase("MORETHAN25")) {
					createUserObj.selectDefaultSecurityQuestion();
					createUserObj.setAnswer(this.sqa);
					createUserObj.submitUserInformation();
					Map<String,String> errMap = createUserObj.getErrorMesssage();
					
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("secretAnswerError")) {
						String err = errMap.get("secretAnswerError");
						if (!err.equalsIgnoreCase(blankOrInvalidSecurityAnswerErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error message not found with invalid security question answer :"+this.sqa+" Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryCreateUserWithInvalidSecurityAnswer method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	
	@Test
	public void tryVerifyUserWithInvalidAccountStreetOrPhone(){
		
		String emptyAccNumberErrorMessage = "Please enter your account number to proceed. It is the number located on the top right of your Comcast bill.";
		String emptyAddrPhoneErrorMessage = "Select street address or phone number.";
		String emptyStreetAddressErrorMessage = "This field is required.";
		String emptyPhoneNumberErrorMessage = "Please enter your phone number to proceed. It is the number associated with your Comcast account.";
		String invalidPhoneNumberErrorMessage = "This phone number is invalid.";
		
		try
		{
			logger.info("Starting to create userId by verifying identity with: "+this+" and value: "+this.sqa);
			System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);

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
				
				if (this.identityType.equalsIgnoreCase("EMPTYACCNOSTADDRPHONE")) {
					accountVerification.Continue();
					Map<String,String> errMap = accountVerification.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidAccountNumberError") &&
							errMap.containsKey("verificationError")) {
						
						String errAccountNumber = errMap.get("invalidAccountNumberError");
						String errVerification = errMap.get("verificationError");
						if (!errAccountNumber.equalsIgnoreCase(emptyAccNumberErrorMessage) && 
								!errVerification.equalsIgnoreCase(emptyAddrPhoneErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error messages not found for empty account no. and street address/phone number: Got "+errAccountNumber+" "+errVerification, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.identityType.equalsIgnoreCase("EMPTYSTADDR")) {
					accountVerification.enterAccountNumber(this.accountNo);
					accountVerification.enterStreetAddress(this.identity);
					accountVerification.Continue();
					Map<String,String> errMap = accountVerification.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidStreetAddressError")) {
						String err = errMap.get("invalidStreetAddressError");
						if (!err.equalsIgnoreCase(emptyStreetAddressErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error messages not found for empty street address: Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.identityType.equalsIgnoreCase("EMPTYPHONE")) {
					accountVerification.enterAccountNumber(this.accountNo);
					accountVerification.enterPhoneNumber(this.identity);
					accountVerification.Continue();
					Map<String,String> errMap = accountVerification.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPhoneNumberError")) {
						String err = errMap.get("invalidPhoneNumberError");
						if (!err.equalsIgnoreCase(emptyPhoneNumberErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error messages not found for empty phone number: Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.identityType.equalsIgnoreCase("INVALIDPHONE")) {
					accountVerification.enterAccountNumber(this.accountNo);
					accountVerification.enterPhoneNumber(this.identity);
					accountVerification.Continue();
					Map<String,String> errMap = accountVerification.getErrorMesssage();
					if (errMap!=null && !errMap.isEmpty() && errMap.containsKey("invalidPhoneNumberError")) {
						String err = errMap.get("invalidPhoneNumberError");
						if (!err.equalsIgnoreCase(invalidPhoneNumberErrorMessage))
							produceError(sourceId, new TestExecutionException("Expected error messages not found for invalid phone number lebgth: Got "+err, ITestIdmCreateUserIDInvalidScenarios.class));
					}
				}
				else if (this.identityType.equalsIgnoreCase("INVALIDACCNO,VALIDSTRADDR")) {
					accountVerification.enterAccountNumber(this.identity.split(ICommonConstants.COMMA)[0]);
					accountVerification.enterStreetAddress(this.address);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				else if (this.identityType.equalsIgnoreCase("INVALIDACCNO,VALIDPHONENO")) {
					accountVerification.enterAccountNumber(this.identity.split(ICommonConstants.COMMA)[0]);
					accountVerification.enterPhoneNumber(this.phoneNumber);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				else if (this.identityType.equalsIgnoreCase("VALIDACCNO,INVALIDSTRADDR")) {
					accountVerification.enterAccountNumber(this.accountNo);
					accountVerification.enterStreetAddress(this.identity.split(ICommonConstants.COMMA)[1]);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				else if (this.identityType.equalsIgnoreCase("VALIDACCNO,INVALIDPHONENO")) {
					accountVerification.enterAccountNumber(this.accountNo);
					accountVerification.enterPhoneNumber(this.identity.split(ICommonConstants.COMMA)[1]);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				else if (this.identityType.equalsIgnoreCase("INVALIDACCNO,INVALIDSTRADDR")) {
					accountVerification.enterAccountNumber(this.identity.split(ICommonConstants.COMMA)[0]);
					accountVerification.enterStreetAddress(this.identity.split(ICommonConstants.COMMA)[1]);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				else if (this.identityType.equalsIgnoreCase("INVALIDACCNO,INVALIDPHONE")) {
					accountVerification.enterAccountNumber(this.identity.split(ICommonConstants.COMMA)[0]);
					accountVerification.enterPhoneNumber(this.identity.split(ICommonConstants.COMMA)[1]);
					obj = accountVerification.Continue();
					if (obj!=null && !(obj instanceof InformationMismatch))
						produceError(sourceId, new TestExecutionException("Expected Information mismatch page not found", ITestIdmCreateUserIDInvalidScenarios.class));
				}
				testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			
			if (obj != null && obj instanceof PageError) {
				produceError(sourceId, new TestExecutionException("Expected page is not found", ITestIdmCreateUserIDInvalidScenarios.class));
			}

		} catch (Exception e) {
			produceError(sourceId, new TestExecutionException("Error occured while testing tryVerifyUserWithInvalidAccountStreetOrPhone method :"+e.getMessage(), ITestIdmCreateUserIDInvalidScenarios.class));
		}
		
	}

	
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
		if (account!=null && account.getAccountId()!=null)
			unlockResource(LockableResource.ACCOUNT,account.getAccountId(),sourceId);
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
	private String phoneNumberType = null;
	private String smsCode = null;
	private String smsCodeType = null;
	private String userName = null;
	private String userNameType = null;
	private String password = null;
	private String passwordType = null;
	private String identity = null;
	private String identityType = null;
	private String alternateEmail = null;
	private String sqa = null;
	private String sqaType = null;
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
	private FreshUsers user = null;
	private AccountCache.Account account = null;
	private String sessId = null;
	private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
	
	private void getTestData() throws Exception{

		if(testData==null){
			
			logger.debug("Calling data provider with keySet = "+keySet.toString());
			
			testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
			
			if(testData!=null && testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue())!=null){
				account = (AccountCache.Account)testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
				if(account!=null) {
					if (phoneNumber == null)
						phoneNumber = account.getPhoneNumber();
					accountNo = account.getAccountId();
					firstName = account.getFirstName();
					lastName = account.getLastName();
					address = account.getAddress();
				}
			}
		}
			
		logger.debug("Getting data from data provider :: Phone Number:  "+phoneNumber +" and alternate Email: "+alternateEmail);
		logger.info("Data provider has been called for fetching test data");
	}

	private String getUserName() throws Exception {
		UserNameGeneratorService userNameGen = ObjectInitializer.getUserNameGeneratorService();
		return userNameGen.next();
	}
	
	private static Logger logger = LoggerFactory.getLogger(ITestIdmCreateUserIDInvalidScenarios.class);

}
