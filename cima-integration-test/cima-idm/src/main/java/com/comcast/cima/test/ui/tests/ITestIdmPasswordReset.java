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
import com.comcast.cima.test.ui.pageobject.CompromisedUserAccount;
import com.comcast.cima.test.ui.pageobject.EmailConfirmationLink;
import com.comcast.cima.test.ui.pageobject.RecoveryOptions;
import com.comcast.cima.test.ui.pageobject.ResetCode;
import com.comcast.cima.test.ui.pageobject.ResetPasswordSuccess;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.mq.CommonMessageProducer;
import com.comcast.test.citf.core.mq.MQConnectionHandler.QueueNames;
import com.comcast.test.citf.core.ui.pom.ResetPassword;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByAgent;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByEmail;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySMS;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySQA;
import com.comcast.test.citf.core.ui.pom.ResetPasswordEmailConfirmation;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.ResetPasswordMethods;
import com.comcast.test.citf.core.ui.pom.ResetPasswordSMSConfirmation;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityHome;

public class ITestIdmPasswordReset extends IdmTestDataProvider{
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){
		try{
			Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
			sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
			chosenResetMethod = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN);
			
			String filterPhone = localParams.get(IdmFilterKeys.PHONE.getValue());
			String filterSQA = localParams.get(IdmFilterKeys.SECRET_QUESTION.getValue());
			String filterAlterEmail = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
			String filterUserRole = localParams.get(IdmFilterKeys.USER_ROLE.getValue());
			
			if(filterPhone!=null || filterSQA!=null || filterAlterEmail!=null || filterUserRole!=null){
				filter =  new HashMap<String, Object>();
				
				if(filterPhone!=null)
					filter.put(IdmFilterKeys.PHONE.getValue(), filterPhone);
				if(filterSQA!=null)
					filter.put(IdmFilterKeys.SECRET_QUESTION.getValue(), filterSQA);
				if(filterAlterEmail!=null)
					filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterEmail);
				if(filterUserRole!=null)
					filter.put(IdmFilterKeys.USER_ROLE.getValue(), filterUserRole);
			}
			
			saucePlatform = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM);
			sauceDeviceType = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE);
			sauceBrowser = localParams.get(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER);
			testCastUsername = localParams.get(ICimaCommonConstants.TEST_CASE_USERNAME);
			esdFlagCstLoginStatus = localParams.get(ICimaCommonConstants.ESD_FLAG_CST_LOGIN_STATUS);
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			executionEnvironment = getCurrentEnvironment();
			this.getTestData();
			pfId = PageNavigator.start("ValidatepasswordResetFeature_"+currentTestMethod+"_"+MiscUtility.getCurrentTimeInMillis());
			logger.info("Page Flow Id : " + pfId);
			browser = getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
			sessId = ((RemoteWebDriver)browser).getSessionId().toString();
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
			ldap = ObjectInitializer.getLdapService();
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
	}
	
	@Test
	public void validatePasswordResetUsingInactiveUser() throws Exception {
		//STEP 1 - Make sure that the user account isn't inactive, want to confirm that the user's account exists by logging in first
		ldap.updateAttributeOfCustomerObject(testCastUsername, "cstLoginStatus", "A");
		//STEP 2 - Login as the user to confirm that the user's account does exist
		signInToXfinity.get();
		Object nextPageObj;
		nextPageObj = signInToXfinity.signin(testCastUsername, "Comcast1");
		if (!(nextPageObj instanceof RecoveryOptions)) {
			throw new Exception(
					"After logging in with the user's account in the active state, resulting page was '" + nextPageObj.getClass().getName()
					+ "' instead of '" + RecoveryOptions.class.getName() + "'");
		}
		//STEP 3 - Make sure that the user account is inactive
		ldap.updateAttributeOfCustomerObject(testCastUsername, "cstLoginStatus", esdFlagCstLoginStatus);
		//STEP 4 - Try login with inactive account, confirm that the user cannot login
		signInToXfinity.get();
		nextPageObj = signInToXfinity.signin(testCastUsername, "Comcast1");
		if (!(nextPageObj instanceof SignInToXfinity)) {
			throw new Exception(
					"After logging in with the user's account in the inactive state, resulting page was '" + nextPageObj.getClass().getName()
					+ "' instead of '" + SignInToXfinity.class.getName() + "'");
		}
	}
	
	@Test
	public void validatePasswordResetUsingCompromisedUser() throws Exception {
		try {
			//
			//STEP 1 - Make sure that the user account isn't compromised, because the reset password attempt (STEP 2) requires this
			ldap.updateAttributeOfCustomerObject(
					"xcima_test_account_compromised_1", "cstCompromisedFlag", "N");
			//
			//
			//STEP 2 - Since the last step below resets the user's password and the steps before it require that this test know the password,
			//  this step resets the password to a known value ('Comcast1')
			Object obj = signInToXfinity.getPageResetPasswordGateway();
			
			if (obj != null && obj instanceof ResetPasswordGateway) {
				ResetPasswordGateway ResetPasswordGatewayobj=(ResetPasswordGateway)obj;
				ResetPasswordGatewayobj.enterUsername("xcima_test_account_compromised_1");
				obj=ResetPasswordGatewayobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof SecurityCheck) {			
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;			
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("answer");
			}
			
			
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion("honda");
				ResetPasswordBySQAobj.enterZipCode("19311");
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			
			if (obj != null && obj instanceof ResetPassword) {	
				//String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword("Comcast1");
				ResetPasswordobj.reEnterPassword("Comcast1");
			    obj = ResetPasswordobj.continuePage();
			}
			//
			//STEP 3 - Compromise the user's account
			ldap.updateAttributeOfCustomerObject(
					"xcima_test_account_compromised_1", "cstCompromisedFlag", "Y");
			//
			//STEP 4 - Login as the user, traverse compromised pages to reset password, login with newly-created password
			signInToXfinity.get();
			//
			Object nextPageObj;
			nextPageObj = signInToXfinity.signin("xcima_test_account_compromised_1", "Comcast1");
			if (nextPageObj instanceof CompromisedUserAccount) {
				CompromisedUserAccount cua = ((CompromisedUserAccount)nextPageObj);
				nextPageObj = cua.clickResetPasswordButton();
				if (nextPageObj instanceof ResetPasswordBySQA) {
					ResetPasswordBySQA rpbsqa = ((ResetPasswordBySQA)nextPageObj);
					rpbsqa.answerSecretQuestion("honda");
					rpbsqa.enterZipCode("19311");
					rpbsqa.enterNucaptchaAnswer("answer");
					nextPageObj = rpbsqa.continueResettingPassword();
					if (nextPageObj instanceof ResetPassword) {
						ResetPassword rp = ((ResetPassword)nextPageObj);
						//'updatePassword' string length totals 16 characters - the upper limit (8 to 16 characters)
						String updatePassword = "ComcastUpdated2";
						rp.enterPassword(updatePassword);
						rp.reEnterPassword(updatePassword);
						nextPageObj = rp.continuePage();
						if (!(nextPageObj instanceof ResetPasswordSuccess)) {
							throw new Exception(
									"After resetting password, resulting page was '" + nextPageObj.getClass().getName()
									+ "' instead of '" + ResetPasswordSuccess.class.getName() + "'");
						}
						signInToXfinity.get();
						nextPageObj = signInToXfinity.signin("xcima_test_account_compromised_1", updatePassword);
						if (!(nextPageObj instanceof RecoveryOptions)) {
							throw new Exception(
									"After logging in with new password, resulting page was '" + nextPageObj.getClass().getName()
									+ "' instead of '" + RecoveryOptions.class.getName() + "'");
						}
						//If test reaches this point, the user has successfully logged in with newly-created password
						//  after resetting the password in the compromised user web flow
					} else {
						throw new Exception(
								"After submitting Secret Q&A information, resulting page was '" + nextPageObj.getClass().getName()
								+ "' instead of '" + ResetPassword.class.getName() + "'");
					}
				} else {
					throw new Exception(
							"After selecting 'Reset Password' button, resulting page was '" + nextPageObj.getClass().getName()
							+ "' instead of '" + ResetPasswordBySQA.class.getName() + "'");
				}
			} else {
				throw new Exception(
						"After signing in, resulting page was '" + nextPageObj.getClass().getName()
						+ "' instead of '" + CompromisedUserAccount.class.getName() + "'");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/* Password Recovery using SQA */
	
	@Test
	public void validatePasswordResetUsingSQA(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try{
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
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
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
			    obj = ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + this.userAttributes.getUserId());
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPasswordWith SQA method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with SQA options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}


	
	/* Password Recovery using SMS */
	
	@Test
	public void validatePasswordResetUsingSMS(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
				
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
				if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
				}
				String resetcode=readLog("RESET_PASSWORD_SMS", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
				if (resetcode == null) {
					produceError(sourceId, new TestExecutionException("Unable to retrieve SMS code from server log", ITestIdmPasswordReset.class));
				}
				ResetPasswordSMSConfirmationObj.enterResetCode(resetcode);
				obj=ResetPasswordSMSConfirmationObj.continueResettingPassword();
			}
				
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPasswordWith SMS method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with SMS options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}

	
	/* Password Recovery using Email */
	
	@Test
	public void validatePasswordResetUsingEmail(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);		
				
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
			
			if (obj != null && obj instanceof ResetPasswordByEmail) {	
				ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
				obj=ResetPasswordByEmailobj.continuePage();				
			}
				
			if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
			{
				ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
				if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
				}
				String resetLink=readLog("RESET_PASSWORD_EMAIL", LogReader.RegexArgument.EMAIL, this.userAttributes.getAlterEmail());
				EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
				EmailConfirmationLinkobj.setPageFlowId(this.pfId);
				if (resetLink != null)
					obj=EmailConfirmationLinkobj.open(resetLink);
				else
					produceError(sourceId, new TestExecutionException("Unable to retrieve password reset email from server log", ITestIdmPasswordReset.class));
			}
				
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPasswordWith Email method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with Email options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
	}

	
	
	/* Password Recovery using SMS and SQA */
	
	@Test
	public void validatePasswordResetUsingSMSAndSQA(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
					
			String securityAnswer = this.userAttributes.getSecretAnswer();
				
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
			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("SMS"))
			{	
			
				if (obj != null && obj instanceof ResetPasswordBySMS) {	
					ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
					obj=ResetPasswordBySMSobj.continuePage();				
				}
				
				
				if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
				{
					ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
					if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetcode=readLog("RESET_PASSWORD_SMS", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
					if (resetcode == null) {
						produceError(sourceId, new TestExecutionException("No SMS reset code found in server log", ITestIdmPasswordReset.class));
					}
					ResetPasswordSMSConfirmationObj.enterResetCode(resetcode);
					obj=ResetPasswordSMSConfirmationObj.continueResettingPassword();
				}
				
			}
			else if((this.chosenResetMethod.equalsIgnoreCase("SQA")))
			{				
				if (obj != null && obj instanceof ResetPasswordBySMS) {	
					ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
					obj=ResetPasswordBySMSobj.noPhoneAccess();				
				}
			
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(this.account.getZip().substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}
				
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPasswordWith SQA method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with SQASMS options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}
	
	
	

	@Test
	public void validatePasswordResetUsingEmailAndSQA(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("EMAIL"))
			{	
			
				if (obj != null && obj instanceof ResetPasswordByEmail) {	
					ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
					obj=ResetPasswordByEmailobj.continuePage();				
				}
					
				if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
				{
					ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
					if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetLink=readLog("RESET_PASSWORD_EMAIL", LogReader.RegexArgument.EMAIL, this.userAttributes.getAlterEmail());
					EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
					EmailConfirmationLinkobj.setPageFlowId(this.pfId);
					if (resetLink != null)
						obj=EmailConfirmationLinkobj.open(resetLink);
					else
						produceError(sourceId, new TestExecutionException("Unable to retrieve password reset email from server log", ITestIdmPasswordReset.class));
				}
					
			}
			else if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{				
				if (obj != null && obj instanceof ResetPasswordByEmail) {	
					ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
					obj=ResetPasswordByEmailobj.noEmailAccess();				
				}
					
			
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}
				
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
		}
										
		
		catch(Exception e){
			 logger.error("Error occured while testing resetPasswordWith SQA method :"+MiscUtility.getStackTrace(e));
			 produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with SQA options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}
	
	
	
	
	@Test
	public void validatePasswordResetUsingEmailSMSAndSQA(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("EMAIL"))
			{	
			
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetByEmail();
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				
				if (obj != null && obj instanceof ResetPasswordByEmail) {	
					ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
					obj=ResetPasswordByEmailobj.continuePage();				
				}
					
				if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
				{
					ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
					if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetLink=readLog("RESET_PASSWORD_EMAIL", LogReader.RegexArgument.EMAIL, this.userAttributes.getAlterEmail());
					EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
					EmailConfirmationLinkobj.setPageFlowId(this.pfId);
					if (resetLink != null)
						obj=EmailConfirmationLinkobj.open(resetLink);
					else
						produceError(sourceId, new TestExecutionException("Unable to retrieve password reset email from server log", ITestIdmPasswordReset.class));
				}
					
			}
			else if(this.chosenResetMethod.equalsIgnoreCase("SMS"))
			{				
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetBySMS();		
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySMS) {	
					ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
					obj=ResetPasswordBySMSobj.continuePage();				
				}
				
				if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
				{
					ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
					if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetcode=readLog("RESET_PASSWORD_SMS", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
					if (resetcode == null) {
						produceError(sourceId, new TestExecutionException("No SMS reste code found in server log", ITestIdmPasswordReset.class));
					}
					ResetPasswordSMSConfirmationObj.enterResetCode(resetcode);
					obj=ResetPasswordSMSConfirmationObj.continueResettingPassword();
				}
					
			}
			else if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{
			
			if (obj != null && obj instanceof ResetPasswordMethods) {	
				ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
				obj=ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
			}
				
			if (obj != null && obj instanceof ResetPasswordBySQA) {	
				ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
				ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
				ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
			    obj=ResetPasswordBySQAobj.continueResettingPassword();
			}
			
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With EmailSMSAndSQA method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with EmailSMSAndSQA options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}
	
	
	
	
	@Test
	public void validatePasswordResetUsingAgents(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			//Choosing the agent for recovery
			if(this.chosenResetMethod.equalsIgnoreCase("AGENT"))
			{	
			
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					obj=ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
				}
					
				
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					obj=ResetPasswordBySQAobj.dontRememberAnswer();		
				}
					
				if (obj != null && obj instanceof ResetPasswordByAgent)
				{
					ResetPasswordByAgent resetpasswordByAgent = (ResetPasswordByAgent) obj;
					if (!resetpasswordByAgent.verifyPage()) {
						produceError(sourceId, new TestExecutionException("Expected messages not found in reset password by agent page", ITestIdmPasswordReset.class));
					}
					String code = getResetCode(userId);
					switchWindow(browser,resetpasswordByAgent.getWindowHandle());
					obj = resetpasswordByAgent.provideResetCodeAndContinue(code);
				}
					
				if (obj != null && obj instanceof ResetPassword) {	
					String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
					ResetPassword ResetPasswordobj=(ResetPassword)obj;
					if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
						produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
					}
					ResetPasswordobj.enterPassword(pwd);
					ResetPasswordobj.reEnterPassword(pwd);
					obj=ResetPasswordobj.continuePage();
				}
			}
			
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With EmailSMSAndSQA method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying to reset password with EmailSMSAndSQA options method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}

	
	
	@Test
	public void validatePasswordResetUsingSQAOrSMSAfterSelectingEmail(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			if (obj != null && obj instanceof ResetPasswordMethods) {	
				ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
				ResetPasswordMethodsobj.selectPasswordResetByEmail();
				obj=ResetPasswordMethodsobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof ResetPasswordByEmail) {	
				ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
				obj=ResetPasswordByEmailobj.noEmailAccess();		
			}

			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{	
				if (obj != null && obj instanceof ResetPasswordMethods) {
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					obj = ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}

			}
			else if(this.chosenResetMethod.equalsIgnoreCase("SMS"))
			{				
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetBySMS();		
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySMS) {	
					ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
					obj=ResetPasswordBySMSobj.continuePage();				
				}
				
				if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
				{
					ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
					if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetcode=readLog("RESET_PASSWORD_SMS", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
					if (resetcode == null) {
						produceError(sourceId, new TestExecutionException("No SMS reste code found in server log", ITestIdmPasswordReset.class));
					}
					ResetPasswordSMSConfirmationObj.enterResetCode(resetcode);
					obj=ResetPasswordSMSConfirmationObj.continueResettingPassword();
				}
					
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With SQAOrSMS method after selecting Email as password recovery method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With SQAOrSMS method after selecting Email as password recovery method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}

	
	
	@Test
	public void validatePasswordResetUsingSQAOrEmailAfterSelectingSMS(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
				
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			if (obj != null && obj instanceof ResetPasswordMethods) {	
				ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
				ResetPasswordMethodsobj.selectPasswordResetBySMS();
				obj=ResetPasswordMethodsobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof ResetPasswordBySMS) {	
				ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
				obj=ResetPasswordBySMSobj.noPhoneAccess();	
			}

			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{	
				if (obj != null && obj instanceof ResetPasswordMethods) {
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					obj = ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}

			}
			else if(this.chosenResetMethod.equalsIgnoreCase("EMAIL"))
			{				
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetByEmail();
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				if (obj != null && obj instanceof ResetPasswordByEmail) {	
					ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
					obj=ResetPasswordByEmailobj.continuePage();				
				}
				
				if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
				{
					ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
					if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetLink=readLog("RESET_PASSWORD_EMAIL", LogReader.RegexArgument.EMAIL, this.userAttributes.getAlterEmail());
					EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
					EmailConfirmationLinkobj.setPageFlowId(this.pfId);
					if (resetLink != null)
						obj=EmailConfirmationLinkobj.open(resetLink);
					else
						produceError(sourceId, new TestExecutionException("Unable to retrieve password reset email from server log", ITestIdmPasswordReset.class));
				}
				
			}

			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With SQAOrEmail method after selecting SMS as password recovery method :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With SQAOrEmail method after selecting SMS as password recovery method :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}

	
	
	@Test
	public void validatePasswordResetUsingSQAOrSMSAfterRecoveryLinkGenByEmail(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			if (obj != null && obj instanceof ResetPasswordMethods) {	
				ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
				ResetPasswordMethodsobj.selectPasswordResetByEmail();
				obj=ResetPasswordMethodsobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof ResetPasswordByEmail) {	
				ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
				obj=ResetPasswordByEmailobj.continuePage();				
			}
				
			if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
			{
				ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
				if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
				}
				obj = ResetPasswordEmailConfirmationObj.continueWithOtherMethod();
			}

			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{	
				if (obj != null && obj instanceof ResetPasswordMethods) {
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					obj = ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}

			}
			else if(this.chosenResetMethod.equalsIgnoreCase("SMS"))
			{				
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetBySMS();		
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySMS) {	
					ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
					obj=ResetPasswordBySMSobj.continuePage();				
				}
				
				if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
				{
					ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
					if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetcode=readLog("RESET_PASSWORD_SMS", LogReader.RegexArgument.PHONE_NUMBER, this.account.getPhoneNumber());
					if (resetcode == null) {
						produceError(sourceId, new TestExecutionException("No SMS reste code found in server log", ITestIdmPasswordReset.class));
					}
					ResetPasswordSMSConfirmationObj.enterResetCode(resetcode);
					obj=ResetPasswordSMSConfirmationObj.continueResettingPassword();
				}
					
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With SQAOrSMS method after generating recovery link by Email :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With SQAOrSMS method after generating recovery link by Email :"+e.getMessage(), ITestIdmPasswordReset.class));
		} finally {
			unlockUser();
		}
		
	}

	
	
	@Test
	public void validatePasswordResetUsingSQAOrEmailAfterRecoveryCodeGenBySMS(){
		logger.info("Starting to reset password with chosen option: "+this.chosenResetMethod);
		System.out.println("SauceOnDemandSessionID="+sessId+" job-name="+currentTestMethod);
		
		try {
				
			if(this.chosenResetMethod==null || this.userAttributes==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
				
			String securityAnswer = this.userAttributes.getSecretAnswer();
			String zip = this.account.getZip();
				
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
			
			if (obj != null && obj instanceof ResetPasswordMethods) {	
				ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
				ResetPasswordMethodsobj.selectPasswordResetBySMS();
				obj=ResetPasswordMethodsobj.continueResettingPassword();
			}
			
			if (obj != null && obj instanceof ResetPasswordBySMS) {	
				ResetPasswordBySMS ResetPasswordBySMSobj=(ResetPasswordBySMS)obj;
				obj=ResetPasswordBySMSobj.continuePage();				
			}
				
			if (obj != null && obj instanceof ResetPasswordSMSConfirmation)
			{
				ResetPasswordSMSConfirmation ResetPasswordSMSConfirmationObj=(ResetPasswordSMSConfirmation)obj;
				if (!ResetPasswordSMSConfirmationObj.isSMSSentSuccessfully()) {
					produceError(sourceId, new TestExecutionException("No password reset SMS sent confirmation message found", ITestIdmPasswordReset.class));
				}
				obj = ResetPasswordSMSConfirmationObj.continueWithOtherWay();
			}

			
			//Choosing the flow
			if(this.chosenResetMethod.equalsIgnoreCase("SQA"))
			{	
				if (obj != null && obj instanceof ResetPasswordMethods) {
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					obj = ResetPasswordMethodsobj.continueResettingPasswordByOtherWay();
				}
					
				if (obj != null && obj instanceof ResetPasswordBySQA) {	
					ResetPasswordBySQA ResetPasswordBySQAobj=(ResetPasswordBySQA)obj;
					ResetPasswordBySQAobj.answerSecretQuestion(securityAnswer);
					ResetPasswordBySQAobj.enterZipCode(zip.substring(0, 5));
				    obj=ResetPasswordBySQAobj.continueResettingPassword();
				}

			}
			else if(this.chosenResetMethod.equalsIgnoreCase("EMAIL"))
			{				
				if (obj != null && obj instanceof ResetPasswordMethods) {	
					ResetPasswordMethods ResetPasswordMethodsobj=(ResetPasswordMethods)obj;
					ResetPasswordMethodsobj.selectPasswordResetByEmail();		
					obj=ResetPasswordMethodsobj.continueResettingPassword();
				}
					
				if (obj != null && obj instanceof ResetPasswordByEmail) {	
					ResetPasswordByEmail ResetPasswordByEmailobj=(ResetPasswordByEmail)obj;
					obj=ResetPasswordByEmailobj.continuePage();				
				}
				
				if (obj != null && obj instanceof ResetPasswordEmailConfirmation)
				{
					ResetPasswordEmailConfirmation ResetPasswordEmailConfirmationObj=(ResetPasswordEmailConfirmation)obj;
					if (!ResetPasswordEmailConfirmationObj.isEmailSentSuccessfully()) {
						produceError(sourceId, new TestExecutionException("No password reset email sent confirmation message found", ITestIdmPasswordReset.class));
					}
					String resetLink=readLog("RESET_PASSWORD_EMAIL", LogReader.RegexArgument.EMAIL, this.userAttributes.getAlterEmail());
					EmailConfirmationLink EmailConfirmationLinkobj=new EmailConfirmationLink(browser);
					EmailConfirmationLinkobj.setPageFlowId(this.pfId);
					if (resetLink != null)
						obj=EmailConfirmationLinkobj.open(resetLink);
					else
						produceError(sourceId, new TestExecutionException("Unable to retrieve password reset email from server log", ITestIdmPasswordReset.class));
				}
					
			}
			
			if (obj != null && obj instanceof ResetPassword) {	
				String pwd = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				ResetPassword ResetPasswordobj=(ResetPassword)obj;
				if (!ResetPasswordobj.isPageLoadedSuccessfully()) {
					produceError(sourceId, new TestExecutionException("Reset password page is not successfully loaded", ITestIdmPasswordReset.class));
				}
				ResetPasswordobj.enterPassword(pwd);
				ResetPasswordobj.reEnterPassword(pwd);
				obj=ResetPasswordobj.continuePage();
			}
			
			if (obj != null && !(obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				produceError(sourceId, new TestExecutionException("Password reset not successful", ITestIdmPasswordReset.class));
			} else if (obj != null && (obj instanceof XfinityHome || obj instanceof ResetPasswordSuccess)) {
				this.testStatus = ICommonConstants.TEST_EXECUTION_STATUS_PASSED;
				logger.info("Password has been reset successfully for user :: " + userId);
			} else {
				produceError(sourceId, new TestExecutionException("Something went wrong, unexpected error", ITestIdmPasswordReset.class));
			}
			
		}
		catch(Exception e){
			logger.error("Error occured while testing resetPassword With SQAOrEmail method after generating recovery code by SMS :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while testing resetPassword With SQAOrEmail method after generating recovery code by SMS :"+e.getMessage(), ITestIdmPasswordReset.class));
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
			StringBuilder sbf = new StringBuilder();
			sbf.append(this.userId);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.password);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.alternateEmail);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.mobile);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.secretAnswer);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.zipCode);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.currentTestMethod);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.saucePlatform);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.sauceDeviceType);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.sauceBrowser);
			sbf.append(ICommonConstants.COMMA);
			sbf.append(this.pfId);
			CommonMessageProducer.sendMessage(sbf.toString(), QueueNames.Persist_User_Password_In_Database);
		} catch (Exception e) {
			logger.error("Error occured while updating test execution status in SauceLabs: {}", e);
		}
	}
	
	//******************************** private methods & variables **********************************//*
	
	private String sourceId = null;
	private UserAttributesCache.Attribute userAttributes = null;
	private AccountCache.Account account = null;
	private String chosenResetMethod = null;
	private String userId = null;
	private String password = null;
	private String alternateEmail = null;
	private String mobile = null;
	private String secretAnswer = null;
	private String zipCode = null;
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
	private String testCastUsername = null;
	private String esdFlagCstLoginStatus = null;
	private Map<String, Object> testData = null;
	private Map<String, Object> filter = null;
	private String sessId = null;
	private String testStatus = ICommonConstants.TEST_EXECUTION_STATUS_FAILED;
	private LDAPInterface ldap = null;
	
	
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
							this.userId = userAttributes.getUserId();
							this.password = userAttributes.getPassword();
							this.alternateEmail = userAttributes.getAlterEmail();
							this.mobile = account.getPhoneNumber();
							this.secretAnswer = userAttributes.getSecretAnswer();
							this.zipCode = account.getZip().substring(0, 5);
						}
					}
						
				}
			}
			
			logger.debug("Getting data from data provider :: userAttributes: "+ this.userAttributes);
			logger.info("Data provider has been called for fetching test data");
	}
	
	
	private String getResetCode(String userId) {
		String code = null;
		
		try
		{
			WebDriver newBrowser = getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
			ResetCode resetCode = new ResetCode(newBrowser);
			resetCode.setPageFlowId(this.pfId);
			resetCode.setWindowHandle(newBrowser.getWindowHandle());
			resetCode.get();
			code = resetCode.createResetCode(userId);
			
		} catch (Exception e) {
			logger.error("Error occured opening reset code page :"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured opening reset code page :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
		return code;
	}
	
	private void unlockUser() {
		if(userAttributes!=null && userAttributes.getUserId()!=null)
			   unlockResource(LockableResource.USER, userAttributes.getUserId(), sourceId);
	}
	
	private static Logger logger = LoggerFactory.getLogger(ITestIdmPasswordReset.class);
}
