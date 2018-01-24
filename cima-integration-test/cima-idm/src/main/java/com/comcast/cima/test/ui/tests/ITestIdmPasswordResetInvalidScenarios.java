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
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.ui.pom.ResetPassword;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByAgent;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySMS;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySQA;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.ResetPasswordSMSConfirmation;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

public class ITestIdmPasswordResetInvalidScenarios extends IdmTestDataProvider {
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID); 
			
			String filterPhone = localParams.get(IdmFilterKeys.PHONE.getValue());
			String filterSQA = localParams.get(IdmFilterKeys.SECRET_QUESTION.getValue());
			String filterAlterEmail = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
			
			if(filterPhone!=null || filterSQA!=null || filterAlterEmail!=null){
				filter =  new HashMap<String, Object>();
				
				if(filterPhone!=null)
					filter.put(IdmFilterKeys.PHONE.getValue(), filterPhone);
				if(filterSQA!=null)
					filter.put(IdmFilterKeys.SECRET_QUESTION.getValue(), filterSQA);
				if(filterAlterEmail!=null)
					filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterEmail);
			}
			
			recoveryOption = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN);
			saucePlatform = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM);
			sauceDeviceType = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE);
			sauceBrowser = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER);
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			executionEnvironment = getCurrentEnvironment();
			this.getTestData();
			pfId = PageNavigator.start("ValidatepasswordResetErrorScenarios"+currentTestMethod+"_"+MiscUtility.getCurrentTimeInMillis());
			browser = getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
			sessId = ((RemoteWebDriver)browser).getSessionId().toString();
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
	}
	
	
	
	/* Password Recovery using invalid userId */
	
	@Test
	public void tryPasswordResetInvalidUserId(){
		logger.info("Starting to reset password with userId :: " + user);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		ResetPasswordGateway ResetPasswordGatewayobj = null;
		String messageExpectedWithInvalidUserId = "We could not find this username. Please try again and make sure it is correctly spelled.";
		String messageExpectedWithblankUserId = "Please enter a username to proceed.";
		
		user = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_USERID);
		
		try{
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(user);
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (user.trim().length() > 0) {
				if (obj != null && obj instanceof SecurityCheck) {			
					SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
					obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
				}
				
				if (obj != null && obj instanceof ResetPasswordGateway) {
					ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
					String actualMessageWithInvalidUserId = ResetPasswordGatewayobj.getErrorMesssage();
					if (!actualMessageWithInvalidUserId.equalsIgnoreCase(messageExpectedWithInvalidUserId))
						produceError(sourceId, new TestExecutionException("Invalid error message. Expected : "+messageExpectedWithInvalidUserId+" and got "+actualMessageWithInvalidUserId, ITestIdmPasswordResetInvalidScenarios.class));
				}
			}
			else {
				String actualMessageWithBlankUserId = ResetPasswordGatewayobj.getErrorMesssage();
				if (!actualMessageWithBlankUserId.equalsIgnoreCase(messageExpectedWithblankUserId))
					produceError(sourceId, new TestExecutionException("Invalid error message. Expected : "+messageExpectedWithblankUserId+" and got "+actualMessageWithBlankUserId, ITestIdmPasswordResetInvalidScenarios.class));
			}
			this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			
		} catch(Exception e){
			logger.error("Error occured while testing resetPassword With invalid userId :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With invalid userId :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
	}

	
	
	/* Password Recovery using invalid Security answer and ZipCode */
	
	@Test
	public void tryPasswordResetInvalidSQAAndZipCode(){
		logger.info("Starting to reset password with a user having SQA recovery method and invalid security answer and zipcode");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		ResetPasswordGateway ResetPasswordGatewayobj = null;
		String messageExpectedWithInvalidSecurityAnswer = "This answer does not match your security question. Please try again.";
		String messageExpectedWithInvalidZipCode = "This zip code does not match your account records. Please try again.";
		
		invalidSecurityAnswer = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER);
		invalidZipCode = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_ZIPCODE);
		
		try{
			
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {
				
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.invalidSecurityAnswer);
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			    Map<String,String> errorMap = ResetPasswordBySQAobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    if (errorMap.containsKey("secretAnswerError".toUpperCase())) {
			    	if (!errorMap.get("secretAnswerError".toUpperCase()).equalsIgnoreCase(messageExpectedWithInvalidSecurityAnswer)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for invalid secret answer", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    if (errorMap.containsKey("zipCodeError".toUpperCase())) {
			    	produceError(sourceId, new TestExecutionException("Unexpected zip code error message found for invalid security answer", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.invalidZipCode);
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			    errorMap = ResetPasswordBySQAobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			  
			    if (errorMap.containsKey("zipCodeError".toUpperCase())) {
			    	if (!errorMap.get("zipCodeError".toUpperCase()).equalsIgnoreCase(messageExpectedWithInvalidZipCode)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for invalid zipcode", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    if (errorMap.containsKey("secretAnswerError".toUpperCase())) {
			    	produceError(sourceId, new TestExecutionException("Unexpected secret answer error message found for invalid zipcode", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			else {
				produceError(sourceId, new TestExecutionException("Error in opening password reset by Security QA page", ITestIdmPasswordResetInvalidScenarios.class));
			}
			
			
		} catch(Exception e){
			logger.error("Error occured while testing resetPassword With invalid SQA and zipcode :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With invalid SQA and zipcode :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	
	/* Password Recovery using empty Security answer and ZipCode */
	
	@Test
	public void tryPasswordResetEmptySQAAndZipCode(){
		logger.info("Starting to reset password with a user having SQA recovery method and empty security answer and zipcode");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		ResetPasswordGateway ResetPasswordGatewayobj = null;
		String messageExpectedWithBlankSecurityAnswer = "Please enter the answer to your security question.";
		String messageExpectedWithBlankZipCode = "Please enter the zip code associated with your Comcast account to proceed.";
		
		try{
			
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {
				
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.invalidSecurityAnswer);
				ResetPasswordBySQAobj.enterZipCode(this.invalidZipCode);
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			    Map<String,String> errorMap = ResetPasswordBySQAobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    if (errorMap.containsKey("secretAnswerError".toUpperCase())) {
			    	if (!errorMap.get("secretAnswerError".toUpperCase()).equalsIgnoreCase(messageExpectedWithBlankSecurityAnswer)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for blank secret answer", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    if (errorMap.containsKey("zipCodeError".toUpperCase())) {
			    	if (!errorMap.get("zipCodeError".toUpperCase()).equalsIgnoreCase(messageExpectedWithBlankZipCode)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for blank zipcode", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
			else {
				produceError(sourceId, new TestExecutionException("Error in opening password reset by Security QA page", ITestIdmPasswordResetInvalidScenarios.class));
			}
			
			
		} catch(Exception e){
			logger.error("Error occured while testing resetPassword With empty SQA and zipcode :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With empty SQA and zipcode :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	/* Password Recovery using SQA with empty new password */
	
	@Test
	public void tryPasswordResetEmptyNewPassword(){
		logger.info("Starting to reset password for SQA users with empty new password");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		String messageExpectedWithBlankPassword = "Please enter a password.";
		String messageExpectedWithBlankRepeatPassword = "Please repeat your new password to confirm you entered exactly what you wanted.";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(" ");
				ResetPasswordobj.reEnterPassword(" ");
			    ResetPasswordobj.continuePage();
			    Map<String,String> errorMap = ResetPasswordobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (errorMap.containsKey("passwordError".toUpperCase())) {
			    	if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(messageExpectedWithBlankPassword)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for blank password", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    if (errorMap.containsKey("passwordReTypeError".toUpperCase())) {
			    	if (!errorMap.get("passwordReTypeError".toUpperCase()).equalsIgnoreCase(messageExpectedWithBlankRepeatPassword)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for blank re type password", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With empty new password :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With empty new password :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	

	
	/* Password Recovery using SQA with non matching password and confirm password */
	
	@Test
	public void tryPasswordResetConflictingPassword(){
		logger.info("Starting to reset password for SQA users with empty new password");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		userPassword = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD);
		userConfirmPassword = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_RETYPE_PASSWORD);
		
		String expectedMessage = "Passwords don't match";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(userPassword);
				ResetPasswordobj.reEnterPassword(userConfirmPassword);
			    ResetPasswordobj.continuePage();
			    Map<String,String> errorMap = ResetPasswordobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (errorMap.containsKey("passwordReTypeError".toUpperCase())) {
			    	if (!errorMap.get("passwordReTypeError".toUpperCase()).equalsIgnoreCase(expectedMessage)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for non matching passwords", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With non matching passwords :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With non matching passwords :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	/* Password Recovery using SQA with password less than 8 chars or more than 16 chars */
	
	@Test
	public void tryPasswordResetIncorrectPasswordLength(){
		logger.info("Starting to reset password for SQA users with empty new password");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		userPassword = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD);
		
		String expectedMessage = "Your password must contain 8 to 16 characters.";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(userPassword);
				ResetPasswordobj.reEnterPassword(userPassword);
			    ResetPasswordobj.continuePage();
			    Map<String,String> errorMap = ResetPasswordobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (errorMap.containsKey("passwordError".toUpperCase())) {
			    	if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessage)) {
				    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password less than 8 or more than 16 chars", ITestIdmPasswordResetInvalidScenarios.class));
				    }
			    }
			    
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With incorrect password length :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With incorrect password length :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	/* Password Recovery using SQA with password same as user name or last name or first name */
	
	@Test
	public void tryPasswordResetWithPasswordAsUserDetails(){
		logger.info("Starting to reset password for SQA users with password same as user name or last name or first name");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		userPasswordType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE);
		
		String expectedMessagePasswordAsUsername = "Your password must not contain your username.";
		String expectedMessagePasswordAsFirstname = "Your password must not contain your first name.";
		String expectedMessagePasswordAsLastname = "Your password must not contain your last name.";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				if (userPasswordType.equalsIgnoreCase("USERNAME")) {
					ResetPasswordobj.enterPassword(this.userAttributes.getUserId());
					ResetPasswordobj.reEnterPassword(this.userAttributes.getUserId());
				}
				else if (userPasswordType.equalsIgnoreCase("FIRSTNAME")) {
					ResetPasswordobj.enterPassword(this.account.getFirstName());
					ResetPasswordobj.reEnterPassword(this.account.getFirstName());
				}
				else if (userPasswordType.equalsIgnoreCase("LASTNAME")) {
					ResetPasswordobj.enterPassword(this.account.getLastName());
					ResetPasswordobj.reEnterPassword(this.account.getLastName());
				}
			    ResetPasswordobj.continuePage();
			    Map<String,String> errorMap = ResetPasswordobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (errorMap.containsKey("passwordError".toUpperCase())) {
			    	if (userPasswordType.equalsIgnoreCase("USERNAME")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordAsUsername)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password equal to username", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	} else if (userPasswordType.equalsIgnoreCase("FIRSTNAME")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordAsFirstname)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password equal to firstname", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	} else if (userPasswordType.equalsIgnoreCase("LASTNAME")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordAsLastname)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password equal to lastname", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	}
			    	
			    }
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With password as username/firstname/lastname :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With password as username/firstname/lastname :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	

	/* Password Recovery using SQA with invalid password formats */
	
	@Test
	public void tryPasswordResetWithInvalidPasswordFormats(){
		logger.info("Starting to reset password for SQA users with invalid password formats");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		userPasswordType = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE);
		userPassword = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD);
		
		String expectedMessagePasswordcontainsNoLetter = "Your password must contain at least one letter.";
		String expectedMessagePasswordcontainsNoNumber = "Your password must contain a number and/or special character";
		String expectedMessagePasswordcontainsSpaces = "Your password must not contain space(s).";
		String expectedMessagePasswordEasyToGuess = "Your password must not be easily guessed.";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(this.userAttributes.getSecretAnswer());
				ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(userPassword);
				ResetPasswordobj.reEnterPassword(userPassword);
			    ResetPasswordobj.continuePage();
			    Map<String,String> errorMap = ResetPasswordobj.getErrorMesssage();
			    if (errorMap == null || errorMap.isEmpty()) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (errorMap.containsKey("passwordError".toUpperCase())) {
			    	if (userPasswordType.equalsIgnoreCase("NOLETTER")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordcontainsNoLetter)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password containing no letter", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	} else if (userPasswordType.equalsIgnoreCase("NONUMBER")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).contains(expectedMessagePasswordcontainsNoNumber)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password containing no number", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	} else if (userPasswordType.equalsIgnoreCase("WITHSPACE")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordcontainsSpaces)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for password containing spaces", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	} else if (userPasswordType.equalsIgnoreCase("EASYTOGUESS")) {
			    		if (!errorMap.get("passwordError".toUpperCase()).equalsIgnoreCase(expectedMessagePasswordEasyToGuess)) {
					    	produceError(sourceId, new TestExecutionException("Expected error message is not found for a easy to guess password", ITestIdmPasswordResetInvalidScenarios.class));
					    }
			    	}
			    	
			    }
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With invalid password format :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With invalid password format :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	/* Password Recovery using SQA with invalid agent reset code */
	
	@Test
	public void tryPasswordResetWithInvalidAgentResetCode(){
		logger.info("Starting to reset password for SQA users with invalid agent reset code");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		agentResetCode = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_AGENT_RESET_CODE);
		
		String expectedMessageInvalidAgentResetCode = "Reset code did not match.";
		String expectedMessageBlankAgentResetCode = "Please enter a valid code to proceed.";
		
		try{
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(this.userAttributes.getUserId());
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
			    obj=ResetPasswordBySQAobj.dontRememberAnswer();
			}
			
			
			if (obj != null && obj instanceof ResetPasswordByAgent) {	
				ResetPasswordByAgent ResetPasswordAgentobj=(ResetPasswordByAgent)obj;
				ResetPasswordAgentobj.provideResetCodeAndContinue(agentResetCode);
			    String error = ResetPasswordAgentobj.getErrorMessage();
			    if (error == null) {
			    	produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
			    }
			    
			    if (agentResetCode.trim().length() > 0) {
			    	if (!error.equalsIgnoreCase(expectedMessageInvalidAgentResetCode)) {
			    		produceError(sourceId, new TestExecutionException("Expected error message not found for invalid agent reset code", ITestIdmPasswordResetInvalidScenarios.class));
			    	}
			    } else {
			    	if (!error.equalsIgnoreCase(expectedMessageBlankAgentResetCode)) {
			    		produceError(sourceId, new TestExecutionException("Expected error message not found for blank agent reset code", ITestIdmPasswordResetInvalidScenarios.class));
			    	}
			    }
			    this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With incorrect agent reset code :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With incorrect agent reset code :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}
	
	
	
	/* Password Recovery using SQA with invalid SMS code */
	
	@Test
	public void tryPasswordResetWithInvalidSMSCode(){
		logger.info("Starting to reset password for SMS users with invalid SMS code");
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		smsResetCode = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE);
		
		String expectedMessageInvalidSMSResetCode = "Reset code did not match.";
		String expectedMessageBlankSMSResetCode = "Please enter reset code you have received.";
		
		try {
				
			if(this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String userId = this.userAttributes.getUserId();	
				
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername(userId);
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			if (obj != null && obj instanceof ResetPasswordBySMS) {	
				ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
				obj=ResetPasswordBySMSobj.continuePage();				
			}
				
				
			if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
			{
				ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
				ResetPasswordSMSConfirmationObj.enterResetCode(smsResetCode);
				ResetPasswordSMSConfirmationObj.continueResettingPassword();
				String error = ResetPasswordSMSConfirmationObj.getErrorMessage();
				if (error == null) {
					produceError(sourceId, new TestExecutionException("Expected error messages are not found", ITestIdmPasswordResetInvalidScenarios.class));
				}
				
				if (smsResetCode.trim().length() > 0) {
			    	if (!error.equalsIgnoreCase(expectedMessageInvalidSMSResetCode)) {
			    		produceError(sourceId, new TestExecutionException("Expected error message not found for invalid SMS reset code", ITestIdmPasswordResetInvalidScenarios.class));
			    	}
			    } else {
			    	if (!error.equalsIgnoreCase(expectedMessageBlankSMSResetCode)) {
			    		produceError(sourceId, new TestExecutionException("Expected error message not found for blank SMS reset code", ITestIdmPasswordResetInvalidScenarios.class));
			    	}
			    }
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPasswordWith incorrect SMS code :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPasswordWith incorrect SMS code :"+e.getMessage(), ITestIdmPasswordReset.class));
	   } finally {
			unlockUser();
		}
		
	}


	
	
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
		try {
			updateSauceLabsTestStatus(sessId,testStatus);
		} catch (Exception e) {
			logger.error("Error occured while updating test execution status in SauceLabs : "+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while updating test execution status in SauceLabs :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
	}

	
	
	
	//******************************** private methods & variables **********************************//*
	
	private String sourceId = null;
	private UserAttributesCache.Attribute userAttributes = null;
	private AccountCache.Account account = null;
	private String user = null;
	private String recoveryOption = null;
	private String invalidSecurityAnswer = null;
	private String invalidZipCode = null;
	private String userPassword = null;
	private String userConfirmPassword = null;
	private String userPasswordType = null;
	private String agentResetCode = null;
	private String smsResetCode = null;
	private String userId = null;
	private String password = null;
	private String serviceName = null;
	private String entitlementKey="";
	private WebDriver browser;
	private SignInToXfinity signInToXfinity;
	private String executionEnvironment = null;
	private String pfId = null;
	private String errorDetails = null;
	private String saucePlatform = null;
	private String sauceDeviceType = null;
	private String sauceBrowser = null;
	private String currentTestMethod;		
	private Map<String, Object> testData = null;
	private Map<String, Object> filter = null;
	private Map<String, String> localParams = null;
	private String sessId = null;
	private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
	
	private void getTestData() throws Exception{
		Set<String> keySet = null;
			
		if(testData==null){
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
						}
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
	
	
	private static Logger logger = LoggerFactory.getLogger(ITestIdmPasswordResetInvalidScenarios.class);


}
