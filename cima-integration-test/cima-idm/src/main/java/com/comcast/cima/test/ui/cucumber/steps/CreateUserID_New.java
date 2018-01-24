package com.comcast.cima.test.ui.cucumber.steps;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.BusinessAccount;
import com.comcast.cima.test.ui.pageobject.CreateUser;
import com.comcast.cima.test.ui.pageobject.CreateUserConfirmation;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.TokenGenerator;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserBySSN;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.LDAPInterface.SearchType;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.dataProvider.DataProviderToCreateUser;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateUserID_New {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private LDAPInterface ldap;
	private Scenario testScenario;
	private WebDriver browser;
	private String sessionId;
	private AccountCache.Account account;
	private UserAttributesCache.Attribute userAttributes;
	private Object pageObject;
	private FreshUsers freshUser;
	private String mobileNumber;
	
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
	private UserNameGeneratorService userNameGenerator;

	private String emailToken;

	private String userId;
	
		
	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.sessionId = null;
		this.account = null;
		this.userAttributes = null;
		this.pageObject = null;
		this.freshUser = null;
		this.mobileNumber = null;
	}
	
	@After
	public void tearDown() {
		try{
			PageNavigator.close(commonSteps.getPfId());
			this.browser = commonSteps.getBrowser();
			this.sessionId = commonSteps.getSessionId();
			this.ldap = commonSteps.getLdap();
			this.account = commonSteps.getAccount();
			this.userAttributes = commonSteps.getUserAttributes();
			this.freshUser = commonSteps.getFreshUser();
			this.pageObject = commonSteps.getPageObject();
			logger.info("restore the mobile number attribute of account2");
			if(commonSteps.getAccount2() != null && commonSteps.getMobile2() != null) {
				ldap.updateAttributeOfAuthorizationObject(commonSteps.getAccount2().getAccountId(), LdapAttribute.AUTHORIZATION_HOME_PHONE.getValue(), commonSteps.getMobile2());
			}
			logger.info("user started user deletion process");
			if(freshUser!=null && freshUser.getPrimaryKey()>0) {
				LDAPAuthorization auth = ldap.getAuthorizationData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID,account.getAccountId()));
				if (auth.getCstAccountManagerGuid() != null) {
					LDAPCustomer cust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_GUID,auth.getCstAccountManagerGuid()));
					List<String> users = new ArrayList<String>(); 
					userId = cust.getUid();
					users.add(userId); 
					ldap.purgeUsers(users);
				}
				
			}
			logger.info("unlock fresh accounts and fresh users");
			if(userAttributes!=null && userAttributes.getUserId()!=null) {
				userProvider.unlockUser(userAttributes.getUserId());
			}
			if (account!=null && account.getAccountId()!=null && userAttributes==null) {
				freshAccountProvider.unlockAccount(account.getAccountId());
			}
			if(freshUser!=null && freshUser.getPrimaryKey()>0) {
				freshUserProvider.unlockFreshUser(freshUser.getPrimaryKey());
			}
			if (browser != null && browser instanceof RemoteWebDriver &&
					((RemoteWebDriver) browser).getSessionId() != null) {
				final String status = testScenario.getStatus().equals("passed")
					? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
					: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
				citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
			}
			if (this.browser != null) {
				browser.quit();
			}
		}
		catch(Exception e){
			logger.error("Error occured while performing clean up activities : ", e );
		}
	}
	

	@When("^user starts user id creation flow$")
	public void userClickOnCreateUsernameLink() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof SignInToXfinity)){
	            throw new IllegalStateException("Expected page not found after user opens Xfinity Login page " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        SignInToXfinity signInPage = (SignInToXfinity)pageObject;
	        pageObject = signInPage.createUserID();
	        commonSteps.setPageObject(pageObject);
	 
	}
	
	@When("^user verifies his identity by mobile number$")
	public void userSelectMobileAsIdentityVerificationMethodAndContinue() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof VerifyIdentity)){
	            throw new IllegalStateException("Expected page not found after user clicks create username link " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        VerifyIdentity verifyIdentity = (VerifyIdentity)pageObject;
	        pageObject = verifyIdentity.verifyIdentityByMobile();
	        commonSteps.setPageObject(pageObject);
	}
	
	@Then("^user is asked to provide his mobile details$")
	public void userSuccessfullyLandsOnEnterMobileNumberOfYourAccountPage() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof UserByMobile)){
	            throw new IllegalStateException("Expected page not found after security check for user by mobile " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	       
	}
	
	@Then("^user is asked to provide his SSN details$")
	public void userSuccessfullyLandsOnEnterSSNDetailOfYourAccountPage() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof UserBySSN)){
	            throw new IllegalStateException("Expected page not found after security check for user by SSN " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	       
	}

	@When("^user provides the mobile number$")
	public void userEnterMobileNumberAndContinue() throws Exception {
	   
		    UserByMobile userByMobile = (UserByMobile)pageObject;
	        userByMobile.enterMobileNumber(commonSteps.getMobile());
	        pageObject = userByMobile.Continue();
	        commonSteps.setPageObject(pageObject);
	}	
	
	@When("^user provides the one time verification code$")
	public void userEntersOneTimeCodeAndContinue() throws Exception {
		UserSMSConfirmation userSMSConfirmation = (UserSMSConfirmation)pageObject;
		userSMSConfirmation.enterSMSCode(commonSteps.getSmscode());
        pageObject = userSMSConfirmation.Continue();
        commonSteps.setPageObject(pageObject);
		
	  
	}
	
	@When("^user proceeds with creating username$")
	public void userClicksUsernameLookupLinkOnLastStepBeforeFBConnectPage() throws Exception {
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof LastStepBeforeFBConnect)){
			throw new IllegalStateException("Last step before FB connect page not found " + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		LastStepBeforeFBConnect lastStepBeforeFBConnect = (LastStepBeforeFBConnect)pageObject;
		pageObject = lastStepBeforeFBConnect.clickCreateUsername();
		commonSteps.setPageObject(pageObject);
	}

	@Then("^user gets user id existense confirmation$")
	public void userGetsUserLookupSuccesspage() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof CreateUserConfirmation)){
	            throw new IllegalStateException("Expected page not found after verification of mobile number of user " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	}
	
	@Then("^user gets business account error message$")
	public void userGetsBusinessAccountErrorMessage() throws Exception {
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof BusinessAccount)){
	            throw new IllegalStateException("Business account page is not found after navigating thourgh business account" + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }	   
	    }
	
	

	@When("^user verifies his identity by SSN$")
	public void userSelectSSNIdentityVerificationMethodAndContinue() throws Exception {
		
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof VerifyIdentity)){
	            throw new IllegalStateException("Expected page not found after user clicks create username link " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        VerifyIdentity verifyIdentity = (VerifyIdentity)pageObject;
	        pageObject = verifyIdentity.verifyIdentityBySSN();
	        commonSteps.setPageObject(pageObject);
	}

	@When("^user provides SSN details$")
	public void userEnterSSNDOBAndMobileNumberAndContinue() throws Exception {
		
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof UserBySSN)){
	            throw new IllegalStateException("User by SSN page not found" + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        
	        account=commonSteps.getAccount();
	        userAttributes=commonSteps.getUserAttributes();
	        if(account==null){
	            throw new IllegalStateException("Unable to read account from database for creating user by SSN");
	        }	 
	        
	        UserBySSN userBySSN = (UserBySSN)pageObject;
	        if(userAttributes != null && userAttributes.getSsn() != null) {
	        	userBySSN.enterSocialSecurityNumber(userAttributes.getSsn());
	        }
	        if(userAttributes != null && userAttributes.getDob() != null) {
	        	userBySSN.enterDateOfBirth(MiscUtility.getFormattedDate(ICimaCommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME,userAttributes.getDob()));
	        }
	        if(account.getPhoneNumber() != null) {
	        	userBySSN.enterPhoneNumber(account.getPhoneNumber());
	        }
	        pageObject=userBySSN.Continue();	  
	        commonSteps.setPageObject(pageObject);
	   
	}
	
	@When("^user provides fresh SSN details$")
	public void userProvidesFreshSSNDetails() throws Exception {
		
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof UserBySSN)){
	            throw new IllegalStateException("User by SSN page not found" + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        
	        account=commonSteps.getAccount();
	        if(account==null){
	            throw new IllegalStateException("Unable to read account from database for creating user by SSN");
	                    
	        }	        
	        UserBySSN userBySSN = (UserBySSN)pageObject;
	        userBySSN.enterSocialSecurityNumber(account.getFreshSsn());
	        userBySSN.enterDateOfBirth(account.getFreshDob());
	        userBySSN.enterPhoneNumber(account.getPhoneNumber());
	        pageObject=userBySSN.Continue();	  
	        commonSteps.setPageObject(pageObject);
	   
	}
	
	@When("^user enter SSN DOB and mobile number and continue for fresh account$")
	public void userEnterSSNDOBAndMobileNumberAndContinueForFreshAccount() throws Exception {
		
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof UserBySSN)){
	            throw new IllegalStateException("User by SSN page not found" + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	        }
	        
	        account=commonSteps.getAccount();
	        if(account==null){
	            throw new IllegalStateException("Unable to read account from database for creating user by SSN");  
	        }	        
	        UserBySSN userBySSN = (UserBySSN)pageObject;
	        userBySSN.enterSocialSecurityNumber(account.getFreshSsn());
	        userBySSN.enterDateOfBirth(account.getFreshDob());
	        userBySSN.enterPhoneNumber(account.getPhoneNumber());
	        pageObject=userBySSN.Continue();	  
	        commonSteps.setPageObject(pageObject);
	   
	}

	
	@Then("^user sucessfully lands on token generation tool page$")
	public void userSucessfullyLandOnTokenGenerationToolPage(){
		 pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof TokenGenerator)){
	            throw new IllegalStateException("Expected page not found after user opens TokenGenerator page " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	            }	        
	    
	}
	
	
	@When("^user requests to generate an email token with true$")
	public void userSelectEmailAndTokenVerficationAsTrueAndClickGenerate(){
		  pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof TokenGenerator)){
	            throw new IllegalStateException("Expected page not found after user opens TokenGenerator page " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	            }	        
	        TokenGenerator tokenGenerator = (TokenGenerator)pageObject;
	       emailToken=tokenGenerator.createEmailTokenTrue(commonSteps.getFreshUser().getAlternativeEmail(), "true", null, null);
	       commonSteps.setEmailToken(emailToken);
	       PageNavigator.close(commonSteps.getPfId());
	       commonSteps.getBrowser().quit();
	}
	
	
	@When("^user requests to generate an email token with false$")
	public void userSelectEmailAndTokenVerficationAsFalseAndClickGenerate(){
		  pageObject = commonSteps.getPageObject();
	        if(!(pageObject instanceof TokenGenerator)){
	            throw new IllegalStateException("Expected page not found after user opens TokenGenerator page " + 
	                    MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
	            }	        
	        TokenGenerator tokenGenerator = (TokenGenerator)pageObject;
	       emailToken=tokenGenerator.createEmailTokenTrue(commonSteps.getFreshUser().getAlternativeEmail(), "false", null, null);
	       commonSteps.setEmailToken(emailToken);
	       PageNavigator.close(commonSteps.getPfId());
	       commonSteps.getBrowser().quit();
	}
	
	@Then("^user gets email token$")
	public void userGetsEmailToken() {
			
		if(this.emailToken!=null)
       {
    	   logger.info("Email token generated sucessfully");
       }
	}
	
	@Then("^user is prompted to create his userId$")
	public void usersuccessfullyLandsOnCreateuserPage() {
			
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
	
	}
	
	@When("^user provides username$")
	public void userEntersUsernameOnCreateUserPage() throws Exception {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.enterUserName(getUserName());
	}
	
	@When("^user provides password while creating userId$")
	public void userEntersPasswordAndConfirmsPassword() throws InterruptedException {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.enterPassword(commonSteps.getFreshUser().getAlterEmailPassword());
		Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
		createUser.reEnterPassword(commonSteps.getFreshUser().getAlterEmailPassword());
	}
	
	@When("^user provides alternative email$")
	public void userEntersThirdPartyEmailOnCreateUserPage() throws Exception {
		
		this.mobileNumber = commonSteps.getAccount().getPhoneNumber();
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
		createUser.enterAlternateEmail(commonSteps.getFreshUser().getAlternativeEmail());
	}
	
	@When("^user provides his phone number$")
	public void userVerifiesPhoneNumberAndEntersSMSCodeOnCreateUserPage() throws Exception {
		
		this.mobileNumber = commonSteps.getAccount().getPhoneNumber();
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.enterMobilePhoneNumber(this.mobileNumber);
	}
	
	@When("^user provides password and security question answer$")
	public void userEntersPasswordAndSecurityQuestionanswerandClickContinue() throws InterruptedException {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.enterPassword(commonSteps.getFreshUser().getAlterEmailPassword());
		createUser.reEnterPassword(commonSteps.getFreshUser().getAlterEmailPassword());
		createUser.selectDefaultSecurityQuestion();
		createUser.setDefaultAnswer();
		createUser.agreeToTermOfServiceAndPrivacyPolicy();
		pageObject = createUser.submitUserInformation();
		commonSteps.setPageObject(pageObject);
      
	}
	
	@When("^user provides secret question and answers$")
	public void userProvidesSecretQuestionAndAnswers() throws InterruptedException {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.selectDefaultSecurityQuestion();
		createUser.setDefaultAnswer();
	}
	
	@When("^user agrees to the terms and condition before verifying mobile number$")
	public void userAgreesToTheTermsAndConditionsBeforeVerifyingMobileNumber() throws InterruptedException {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.agreeToTermOfServiceAndPrivacyPolicy();
		pageObject = createUser.submitUserInformation();
	}
	
	@When("^user agrees to the terms and conditions$")
	public void userAgreesToTheTermsAndConditions() throws InterruptedException {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		createUser.agreeToTermOfServiceAndPrivacyPolicy();
		pageObject = createUser.submitUserInformation();
		commonSteps.setPageObject(pageObject);
	}

	
	@When("^user verifies his phone number$")
	public void userVerifiesAnAccountByEnteringSMSCode() throws Exception {
	
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUser)){
			throw new IllegalStateException(MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		CreateUser createUser = (CreateUser)pageObject;
		String smscode=citfTestInitializer.readLog("USR_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, this.mobileNumber);
		if (smscode == null) {
			throw new IllegalStateException("No SMS verification code found in server log for this mobile number: " + this.mobileNumber);
		}
		pageObject = createUser.enterSMSCodeAndContinue(smscode);
		commonSteps.setPageObject(pageObject);
	}
	
	@Then("^userId should be created successfully for the user$")
	public void userIdCreationConfirmationPageShouldgetDisplay() throws Exception {

		if(!(pageObject instanceof CreateUserConfirmation || pageObject instanceof VerifyUserAfterCreation)){
			throw new IllegalStateException("Create User Confirmation is not opening" + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}	   
	}

	@Then("^user email should be marked as verified$")
	public void userEmailShouldMarkedAsVerified() throws Exception {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof CreateUserConfirmation)){
			throw new IllegalStateException("Create User Confirmation is not opening" + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
		
		CreateUserConfirmation createUserConfirmation = (CreateUserConfirmation)pageObject;
		if(createUserConfirmation.isEmailVerified()){
			logger.info("Preferred email is marked as verified verified");
			
		}
		else{
			throw new IllegalStateException("Prefered email is not marked as verfified on user creation confirmation page");						
		}
	}

	@When("^user confirms his userId$")
	public void userClickContinueOnConfirmation() throws Exception {
		if(pageObject instanceof CreateUserConfirmation) {
			CreateUserConfirmation createUserConfirmation = (CreateUserConfirmation)pageObject;
			pageObject=createUserConfirmation.clickContinue();
			commonSteps.setPageObject(pageObject);
		} else {
			VerifyUserAfterCreation createUserConfirmation = (VerifyUserAfterCreation)pageObject;
			pageObject=createUserConfirmation.clickContinue();
			commonSteps.setPageObject(pageObject);
		}
	}

	@Then("^User lands on VerifyUserAfterCreation page$")
	public void userlandsonVerifyUserAfterCreationpage() throws Exception {
		
		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof VerifyUserAfterCreation)){
			throw new IllegalStateException("verify user after creation page is not opening" + 				
					MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + (pageObject!=null?pageObject.getClass().getName():null));
		}
			   
	}
	
	private String getUserName() throws Exception {
		return userNameGenerator.next();
	}
		
	private static final String MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS = "Page has been identified as ";
	private static final String MESSAGE_CREATE_USER_PAGE_TO_ENTER_DETAILS_IS_NOT_OPENING = "Create User page to enter details is not opening";
}