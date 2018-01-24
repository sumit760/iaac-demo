package com.comcast.cima.test.ui.tests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;

import com.comcast.cima.test.dataProvider.IdmTestDataProvider;
import com.comcast.cima.test.ui.pageobject.CreateUser;
import com.comcast.cima.test.ui.pageobject.FacebookLoginPopUp;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IDMGeneralKeys;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;

public class ITestIdmFacebookConnect extends IdmTestDataProvider {
	
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
			currentTestMethod = localParams.get(ICimaCommonConstants.CURRENT_TEST_METHOD);
			executionEnvironment = getCurrentEnvironment();
			this.getTestData();
			pfId = PageNavigator.start("ValidatepasswordResetFeature");
			browser = getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
			signInToXfinity = new SignInToXfinity(browser);
			signInToXfinity.setPageFlowId(pfId);
			signInToXfinity.setWindowHandle(this.browser.getWindowHandle());
			signInToXfinity.get();
			
			
		}catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIdmPasswordReset.class));
		}
	}
	
	
	@Test
	public void createUsernameHavingAllRecoveryOptionUsingFBConnect(){
		
		try
		{
			
			Object obj=signInToXfinity.getFacebookLoginPopup();
	
			if (obj != null && obj instanceof FacebookLoginPopUp) {
				FacebookLoginPopUp FacebookLoginPopUpobj=(FacebookLoginPopUp)obj;
				
				if (!FacebookLoginPopUpobj.isEmailOrPhoneTextFieldDisplayed()) {
					produceError(sourceId, new TestExecutionException("Email or phone filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Password filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isFBLoginButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Login button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Forgot FB password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isCancelButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Cancel button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isKeepMeLoggedInCheckBoxDisplayed()) {
					produceError(sourceId, new TestExecutionException("Keep me signed in checkbox is not displayed", ITestIdmFacebookConnect.class));
				}
				
				obj=FacebookLoginPopUpobj.getPageLastStepBeforeFBConnect("Email", "pwd");
			}
				
				
				
			if (obj != null && obj instanceof LastStepBeforeFBConnect) {
				LastStepBeforeFBConnect LastStepBeforeFBConnectobj=(LastStepBeforeFBConnect)obj;
				
				if (!LastStepBeforeFBConnectobj.isHeaderPresent()) {
					produceError(sourceId, new TestExecutionException("Last Steps befor FB connect page header is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isDontKnowUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Userlookup link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isForgotPasswordLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Forgot password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isCreateUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Create Username link is not displayed", ITestIdmFacebookConnect.class));
				}				
				obj=LastStepBeforeFBConnectobj.clickCreateUsername();
				
			}
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity VerifyIdentityobj=(VerifyIdentity)obj;
				obj=VerifyIdentityobj.verifyIdentityByAccountNumber();
			}
			
	  	   if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("securityCode");
			}
			
	  	   
	  	 if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	 
	  	if (obj != null && obj instanceof CreateUser) {
	  		CreateUser CreateUserobj=(CreateUser)obj;	
	  		
	  		if (!CreateUserobj.isHeaderGreetingDisplayed()) {
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		CreateUserobj.enterUserName("username");
	  		CreateUserobj.enterPassword("password");
	  		CreateUserobj.reEnterPassword("pasword");
	  		CreateUserobj.enterAlternateEmail("altEmail");
	  		CreateUserobj.verifyMobilePhone("mobilePhoneNumber");
	  		CreateUserobj.selectSecurityQuestion("asdasda");
	  		CreateUserobj.setAnswer("asdasda");
	  		CreateUserobj.agreeToTermOfServiceAndPrivacyPolicy();	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
		}
	  	
	  	
			
	  	if (obj != null && obj instanceof VerifyUserAfterCreation) {
	  		VerifyUserAfterCreation VerifyUserrobj=(VerifyUserAfterCreation)obj;	
			
	  		if(!VerifyUserrobj.validatePage(true).get("STATUS").equalsIgnoreCase("SUCCESS"))
	  		{
	  			produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
	  		}
	  		
	  		obj=VerifyUserrobj.clickContinue();
	  	}
	  	
	  	
	  	
		} catch (Exception e) {
			logger.error("Error occured while testing Username creation using FB connect:"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying create username using Facebook connect method :"+e.getMessage(), ITestIdmFacebookConnect.class));
		  
		}
		
	}
	
	
	
	@Test
	public void createUsernameHavingMobileAndSQARecoveryOptionUsingFBConnect(){
		
		try
		{
			
			Object obj=signInToXfinity.getFacebookLoginPopup();
	
			if (obj != null && obj instanceof FacebookLoginPopUp) {
				FacebookLoginPopUp FacebookLoginPopUpobj=(FacebookLoginPopUp)obj;
				
				if (!FacebookLoginPopUpobj.isEmailOrPhoneTextFieldDisplayed()) {
					produceError(sourceId, new TestExecutionException("Email or phone filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Password filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isFBLoginButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Login button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Forgot FB password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isCancelButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Cancel button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isKeepMeLoggedInCheckBoxDisplayed()) {
					produceError(sourceId, new TestExecutionException("Keep me signed in checkbox is not displayed", ITestIdmFacebookConnect.class));
				}
				
				obj=FacebookLoginPopUpobj.getPageLastStepBeforeFBConnect("Email", "pwd");
			}
				
				
				
			if (obj != null && obj instanceof LastStepBeforeFBConnect) {
				LastStepBeforeFBConnect LastStepBeforeFBConnectobj=(LastStepBeforeFBConnect)obj;
				
				if (!LastStepBeforeFBConnectobj.isHeaderPresent()) {
					produceError(sourceId, new TestExecutionException("Last Steps befor FB connect page header is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isDontKnowUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Userlookup link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isForgotPasswordLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Forgot password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isCreateUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Create Username link is not displayed", ITestIdmFacebookConnect.class));
				}				
				obj=LastStepBeforeFBConnectobj.clickCreateUsername();
				
			}
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity VerifyIdentityobj=(VerifyIdentity)obj;
				obj=VerifyIdentityobj.verifyIdentityByAccountNumber();
			}
			
	  	   if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("securityCode");
			}
			
	  	   
	  	 if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	 
	  	if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	
	  	if (obj != null && obj instanceof CreateUser) {
	  		CreateUser CreateUserobj=(CreateUser)obj;	
	  		
	  		if (!CreateUserobj.isHeaderGreetingDisplayed()) {
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("userNameError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("invalidPasswordError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("passwordReTypeError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("altEmailInvalidError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretQuestionError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretAnswerError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("termsofServiceErrorMsg").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		
	  		CreateUserobj.enterUserName("username");
	  		CreateUserobj.enterPassword("password");
	  		CreateUserobj.reEnterPassword("pasword");
	  		//CreateUserobj.enterAlternateEmail("altEmail");
	  		CreateUserobj.verifyMobilePhone("mobilePhoneNumber");
	  		CreateUserobj.selectSecurityQuestion("asdasda");
	  		CreateUserobj.setAnswer("asdasda");
	  		CreateUserobj.agreeToTermOfServiceAndPrivacyPolicy();	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
		}
	  	
	  	
			
	  	if (obj != null && obj instanceof VerifyUserAfterCreation) {
	  		VerifyUserAfterCreation VerifyUserrobj=(VerifyUserAfterCreation)obj;	
			
	  		if(!VerifyUserrobj.validatePage(true).get("STATUS").equalsIgnoreCase("SUCCESS"))
	  		{
	  			produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
	  		}
	  		
	  		obj=VerifyUserrobj.clickContinue();
	  	}
	  	
	  	
	  	
		} catch (Exception e) {
			logger.error("Error occured while testing Username creation using FB connect:"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying create username using Facebook connect method :"+e.getMessage(), ITestIdmFacebookConnect.class));
		  
		}
		
	}
	
	
	
	@Test
	public void createUsernameHavingEmailAndSQARecoveryOptionUsingFBConnect(){
		
		try
		{
			
			Object obj=signInToXfinity.getFacebookLoginPopup();
	
			if (obj != null && obj instanceof FacebookLoginPopUp) {
				FacebookLoginPopUp FacebookLoginPopUpobj=(FacebookLoginPopUp)obj;
				
				if (!FacebookLoginPopUpobj.isEmailOrPhoneTextFieldDisplayed()) {
					produceError(sourceId, new TestExecutionException("Email or phone filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Password filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isFBLoginButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Login button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Forgot FB password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isCancelButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Cancel button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isKeepMeLoggedInCheckBoxDisplayed()) {
					produceError(sourceId, new TestExecutionException("Keep me signed in checkbox is not displayed", ITestIdmFacebookConnect.class));
				}
				
				obj=FacebookLoginPopUpobj.getPageLastStepBeforeFBConnect("Email", "pwd");
			}
				
				
				
			if (obj != null && obj instanceof LastStepBeforeFBConnect) {
				LastStepBeforeFBConnect LastStepBeforeFBConnectobj=(LastStepBeforeFBConnect)obj;
				
				if (!LastStepBeforeFBConnectobj.isHeaderPresent()) {
					produceError(sourceId, new TestExecutionException("Last Steps befor FB connect page header is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isDontKnowUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Userlookup link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isForgotPasswordLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Forgot password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isCreateUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Create Username link is not displayed", ITestIdmFacebookConnect.class));
				}				
				obj=LastStepBeforeFBConnectobj.clickCreateUsername();
				
			}
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity VerifyIdentityobj=(VerifyIdentity)obj;
				obj=VerifyIdentityobj.verifyIdentityByAccountNumber();
			}
			
	  	   if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("securityCode");
			}
			
	  	   
	  	 if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	 
	  	if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	
	  	if (obj != null && obj instanceof CreateUser) {
	  		CreateUser CreateUserobj=(CreateUser)obj;	
	  		
	  		if (!CreateUserobj.isHeaderGreetingDisplayed()) {
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("userNameError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("invalidPasswordError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("passwordReTypeError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("altEmailInvalidError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretQuestionError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretAnswerError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("termsofServiceErrorMsg").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		
	  		CreateUserobj.enterUserName("username");
	  		CreateUserobj.enterPassword("password");
	  		CreateUserobj.reEnterPassword("pasword");
	  		CreateUserobj.enterAlternateEmail("altEmail");
	  		CreateUserobj.selectSecurityQuestion("asdasda");
	  		CreateUserobj.setAnswer("asdasda");
	  		CreateUserobj.agreeToTermOfServiceAndPrivacyPolicy();	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
		}
	  	
	  	
			
	  	if (obj != null && obj instanceof VerifyUserAfterCreation) {
	  		VerifyUserAfterCreation VerifyUserrobj=(VerifyUserAfterCreation)obj;	
			
	  		if(!VerifyUserrobj.validatePage(true).get("STATUS").equalsIgnoreCase("SUCCESS"))
	  		{
	  			produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
	  		}
	  		
	  		obj=VerifyUserrobj.clickContinue();
	  	}
	  	
	  	
	  	
		} catch (Exception e) {
			logger.error("Error occured while testing Username creation using FB connect:"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying create username using Facebook connect method :"+e.getMessage(), ITestIdmFacebookConnect.class));
		  
		}
		
	}
	
	
	
	@Test
	public void createUsernameHavingSQARecoveryOptionUsingFBConnect(){
		
		try
		{
			
			Object obj=signInToXfinity.getFacebookLoginPopup();
	
			if (obj != null && obj instanceof FacebookLoginPopUp) {
				FacebookLoginPopUp FacebookLoginPopUpobj=(FacebookLoginPopUp)obj;
				
				if (!FacebookLoginPopUpobj.isEmailOrPhoneTextFieldDisplayed()) {
					produceError(sourceId, new TestExecutionException("Email or phone filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Password filed is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isFBLoginButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Login button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isForgotPasswordLinkDisplayed()) {
					produceError(sourceId, new TestExecutionException("Forgot FB password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isCancelButtonDisplayed()) {
					produceError(sourceId, new TestExecutionException("Cancel button is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!FacebookLoginPopUpobj.isKeepMeLoggedInCheckBoxDisplayed()) {
					produceError(sourceId, new TestExecutionException("Keep me signed in checkbox is not displayed", ITestIdmFacebookConnect.class));
				}
				
				obj=FacebookLoginPopUpobj.getPageLastStepBeforeFBConnect("Email", "pwd");
			}
				
				
				
			if (obj != null && obj instanceof LastStepBeforeFBConnect) {
				LastStepBeforeFBConnect LastStepBeforeFBConnectobj=(LastStepBeforeFBConnect)obj;
				
				if (!LastStepBeforeFBConnectobj.isHeaderPresent()) {
					produceError(sourceId, new TestExecutionException("Last Steps befor FB connect page header is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isDontKnowUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Userlookup link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isForgotPasswordLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Forgot password link is not displayed", ITestIdmFacebookConnect.class));
				}
				
				if (!LastStepBeforeFBConnectobj.isCreateUsernameLinkPresent()) {
					produceError(sourceId, new TestExecutionException("Create Username link is not displayed", ITestIdmFacebookConnect.class));
				}				
				obj=LastStepBeforeFBConnectobj.clickCreateUsername();
				
			}
			
			if (obj != null && obj instanceof VerifyIdentity) {
				VerifyIdentity VerifyIdentityobj=(VerifyIdentity)obj;
				obj=VerifyIdentityobj.verifyIdentityByAccountNumber();
			}
			
	  	   if (obj != null && obj instanceof SecurityCheck) {
				SecurityCheck SecurityCheckobj=(SecurityCheck)obj;
				obj=SecurityCheckobj.provideSecurityCodeAndContinue("securityCode");
			}
			
	  	   
	  	 if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	 
	  	if (obj != null && obj instanceof UserByAccountNumber) {
	  		UserByAccountNumber UserByAccountNumberobj=(UserByAccountNumber)obj;
			
	  		UserByAccountNumberobj.enterAccountNumber("accountNo");
	  		UserByAccountNumberobj.enterPhoneNumber("phoneNo");
	  		obj=UserByAccountNumberobj.Continue();
	  		
			}
	  	
	  	if (obj != null && obj instanceof CreateUser) {
	  		CreateUser CreateUserobj=(CreateUser)obj;	
	  		
	  		if (!CreateUserobj.isHeaderGreetingDisplayed()) {
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("userNameError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("invalidPasswordError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("passwordReTypeError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("altEmailInvalidError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretQuestionError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("secretAnswerError").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}	  		
	  		
	  		if (!(CreateUserobj.getErrorMesssage().get("termsofServiceErrorMsg").equalsIgnoreCase("Your username must contain 3 to 32 characters")))	  		
	  		{
				produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
			}
	  		
	  		
	  		CreateUserobj.enterUserName("username");
	  		CreateUserobj.enterPassword("password");
	  		CreateUserobj.reEnterPassword("pasword");
	  		CreateUserobj.enterAlternateEmail("altEmail");
	  		CreateUserobj.verifyMobilePhone("mobilePhoneNumber");
	  		CreateUserobj.selectSecurityQuestion("asdasda");
	  		CreateUserobj.setAnswer("asdasda");
	  		CreateUserobj.agreeToTermOfServiceAndPrivacyPolicy();	  		
	  		obj=CreateUserobj.submitUserInformation();
	  		
		}
	  	
	  	
			
	  	if (obj != null && obj instanceof VerifyUserAfterCreation) {
	  		VerifyUserAfterCreation VerifyUserrobj=(VerifyUserAfterCreation)obj;	
			
	  		if(!VerifyUserrobj.validatePage(true).get("STATUS").equalsIgnoreCase("SUCCESS"))
	  		{
	  			produceError(sourceId, new TestExecutionException("Create Username Header is not displayed", ITestIdmFacebookConnect.class));
	  		}
	  		
	  		obj=VerifyUserrobj.clickContinue();
	  	}
	  	
	  	
	  	
		} catch (Exception e) {
			logger.error("Error occured while testing Username creation using FB connect:"+MiscUtility.getStackTrace(e));
			produceError(sourceId, new TestExecutionException("Error occured while trying create username using Facebook connect method :"+e.getMessage(), ITestIdmFacebookConnect.class));
		  
		}
		
	}
	
	
	
	
	
	@AfterTest
	public void cleanUp() {
		PageNavigator.close(pfId);
		browser.quit();
	}
	
	//******************************** private methods & variables **********************************//*
	
	private String sourceId = null;
	private UserAttributesCache.Attribute userAttributes = null;
	private AccountCache.Account account = null;
	private String chosenResetMethod = null;
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

	
	
	private static Logger logger = LoggerFactory.getLogger(ITestIdmFacebookConnect.class);



}
