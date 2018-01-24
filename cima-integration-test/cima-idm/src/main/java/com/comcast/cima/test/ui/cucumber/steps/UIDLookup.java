package com.comcast.cima.test.ui.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.CreateUser;
import com.comcast.cima.test.ui.pageobject.InformationMismatch;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserBySSN;
import com.comcast.cima.test.ui.pageobject.UserLookupSignUp;
import com.comcast.cima.test.ui.pageobject.UsernameLookupConfirmtaion;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByEmail;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySQA;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UIDLookup {
	
	private final Logger logger = LoggerFactory.getLogger(UIDLookup.class);
	
	private LDAPInterface ldap;
	private Scenario testScenario;
	private String username;
	private String userLookedUp;
	private String userAltEmail;
	private Object pageObject;
	private UsernameLookupConfirmtaion lookUpSuccessPage;
	
	@Autowired
	private IdmCommonSteps commonSteps;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private UserNameGeneratorService userNameGenerator;
	

	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.pageObject = null;
		this.username = null;
		this.userLookedUp = null;
		this.userAltEmail = null;
	}
	
	
	@And("^user tries to access his username$")
	public void userTriesToAccessUsernameFromXfinitySignIn() {
		
		pageObject = commonSteps.getPageObject();
        if(!(pageObject instanceof SignInToXfinity)){
        	logAndFail(SignInToXfinity.class.getSimpleName());
        }
        SignInToXfinity signInPage = (SignInToXfinity)pageObject;
        pageObject = signInPage.lookupUser();
	}
	
	
	@And("^user selects account number to verify his identity$")
	public void userSelectsAccountNumberToVerifyIdentity() throws Exception {
		
		if (!(pageObject instanceof VerifyIdentity)) {
			logAndFail(VerifyIdentity.class.getSimpleName());
		}
		
		VerifyIdentity identityPage = (VerifyIdentity) pageObject;
		pageObject = identityPage.verifyIdentityByAccountNumber();
		commonSteps.setPageObject(pageObject);
	}
	
	@And("^user selects phone number to verify his identity$")
	public void userSelectsPhoneNumberToVerifyIdentity() {
		
		if (!(pageObject instanceof VerifyIdentity)) {
			logAndFail(VerifyIdentity.class.getSimpleName());
		}
		
		VerifyIdentity identityPage = (VerifyIdentity) pageObject;
		pageObject = identityPage.verifyIdentityByMobile();
		commonSteps.setPageObject(pageObject);
	}
	
	
	@And("^user selects SSN to verify his identity$")
	public void userSelectsSSNToVerifyIdentity() {
		
		if (!(pageObject instanceof VerifyIdentity)) {
			logAndFail(VerifyIdentity.class.getSimpleName());
		}
		
		VerifyIdentity identityPage = (VerifyIdentity) pageObject;
		pageObject = identityPage.verifyIdentityBySSN();
		commonSteps.setPageObject(pageObject);
	}

	
	
	@Then("^user receives his Xfinity username$")
	public void userGetsXfinityUsername() {
		
		pageObject = commonSteps.getPageObject();
		if (!(pageObject instanceof UsernameLookupConfirmtaion)) {
			logAndFail(UsernameLookupConfirmtaion.class.getSimpleName());
		}
		this.lookUpSuccessPage = (UsernameLookupConfirmtaion) pageObject;
		this.userLookedUp = lookUpSuccessPage.getUserName();
	}

	
	@Then("^user receives multiple Xfinity usernames$")
	public void userGetsMultipleXfinityUsername() {
		
		pageObject = commonSteps.getPageObject();
		if (!(pageObject instanceof UsernameLookupConfirmtaion)) {
			logAndFail(UsernameLookupConfirmtaion.class.getSimpleName());
		}
		this.lookUpSuccessPage = (UsernameLookupConfirmtaion) pageObject;
		this.userLookedUp = lookUpSuccessPage.getUserName();
		this.userAltEmail = lookUpSuccessPage.getThirdPartyEmail();
	}
	
	@And("^user validates the username$")
	public void userValidatesXfinityUsername() {
		
		assertThat(
				"Username validation error",
				this.userLookedUp, 
				containsString(commonSteps.getUserId()));
	}
	
	@And("^user validates all the usernames$")
	public void userValidatesXfinityUsernames() {
		
		assertThat(
				"Username validation error",
				this.userLookedUp, 
				containsString(commonSteps.getUserId()
				.split(ICommonConstants.AT_THE_RATE)[0]));
		
		assertThat(
				"User alternate email validation error",
				this.userAltEmail,
				containsString(commonSteps.getAlternateEmail()));
	}
	
	@When("^user continues with the username received$")
	public void userContinuesWithUsernameReceived() {
		
		this.pageObject = getLookUpSuccessPage().confirmUsername();
	}
	
	@Then("^user is redirected to sign in with username populated$")
	public void usernameIsPopulatedinXfinitySignIn() {
		
		if (!(pageObject instanceof SignInToXfinity)) {
			logAndFail(SignInToXfinity.class.getSimpleName());
		}
		
		assertThat(
				"Invalid username populated in Sign In page",
				 this.userLookedUp, 
				 containsString(((SignInToXfinity)pageObject)
			     .getUserName()));
	}
	
	
	@Then("^user is redirected to reset his password by security questions$")
	public void userIsRedirectedToResetPasswordBySQA() {
		
		if (!(pageObject instanceof ResetPasswordBySQA)) {
			logAndFail(ResetPasswordBySQA.class.getSimpleName());
		}
		
	}
	
	@Then("^user is redirected to reset his password by alternate email$")
	public void userIsRedirectedToResetPasswordByAltEmail() {
		
		if (!(pageObject instanceof ResetPasswordByEmail)) {
			logAndFail(ResetPasswordByEmail.class.getSimpleName());		}
		
	}
	
	
	@And("^user chooses to reset his password but forgots username$")
	public void userTriesToResetPasswordAndForgotsUsername()  {
		
		this.pageObject = commonSteps.getPageObject();
		
		if (!(pageObject instanceof SignInToXfinity)) {
			logAndFail(SignInToXfinity.class.getSimpleName());
		}
		
		SignInToXfinity signIn = (SignInToXfinity) pageObject;
		this.pageObject = signIn.getPageResetPasswordGateway();
		
	}
	
	@And("^user tries to retrieve his username$")
	public void userTriesToRetrieveUsername() {
		
		if (!(pageObject instanceof ResetPasswordGateway)) {
			logAndFail(ResetPasswordGateway.class.getSimpleName());
		}
		
		ResetPasswordGateway resetPassword = (ResetPasswordGateway) pageObject;
		this.pageObject = resetPassword.continueUserNameLookup();
	}

	@When("^user chooses to lookup his username$")
	public void userChoosesToLookupUsername() {

		this.pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof LastStepBeforeFBConnect)){
			logAndFail(LastStepBeforeFBConnect.class.getSimpleName());
		}
		
		LastStepBeforeFBConnect lastStepBeforeFBConnect = (LastStepBeforeFBConnect)pageObject;
		this.pageObject = lastStepBeforeFBConnect.clickUsernameLookup();
		commonSteps.setPageObject(pageObject);
	}


	@Then("^user is asked to create username$")
	public void userIsAskedToCreateUsername() {
		
		pageObject = commonSteps.getPageObject();
		if (!(pageObject instanceof UserLookupSignUp)) {
			logAndFail(UserLookupSignUp.class.getSimpleName());
		}
		UserLookupSignUp userlookUpSignUp = (UserLookupSignUp) pageObject;
		
		assertThat(
				"Expected create user page to come up",
				userlookUpSignUp.isCreateUserPresent(), 
				is(true));
	}
	
	
	@When("^user continues creating username$")
	public void userProceedsWithCreatingUsername() {
		
		UserLookupSignUp userlookUpSignUp = (UserLookupSignUp) pageObject;
		pageObject = userlookUpSignUp.continueCreatingUsername();
	}
	
	
	@And("^user provides the username and password$")
	public void userProvidesUsernameAndPassword() {
		
		if (!(pageObject instanceof CreateUser)) {
			logAndFail(CreateUser.class.getSimpleName());
		}
		CreateUser createUser = (CreateUser)pageObject;
	    username = getUserName();
		createUser.enterUserName(username);
		String password = getPassword();
		createUser.enterPassword(password);
		createUser.reEnterPassword(password);
	}
	
	
	@And("^user sets security question and answer$")
	public void userSetsSecurityQuestionAndAnswer() {
		
		CreateUser createUser = (CreateUser)pageObject;
		try {
			createUser.selectDefaultSecurityQuestion();
			createUser.setDefaultAnswer();
		} catch (InterruptedException e) {
			logger.error("Exception in setting security question and answer ");
        	fail("Exception in setting security question and answer ");
		}
		
	}
	
	@And("^user agrees to the terms of service$")
	public void userAgreesToTermsAndConditions() throws InterruptedException {
		
		CreateUser createUser = (CreateUser)pageObject;
		createUser.agreeToTermOfServiceAndPrivacyPolicy();
		pageObject = createUser.submitUserInformation();
	}
	
	@Then("^username should be created for the user$")
	public void usernameIsCreated()  {
		if(!(pageObject instanceof VerifyUserAfterCreation)){
			logAndFail(VerifyUserAfterCreation.class.getSimpleName());
		}
	}
	
	@Then("^user receives information mismatch error$")
	public void userReceivesInformationMismatch()  {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof InformationMismatch)){
			logAndFail(InformationMismatch.class.getSimpleName());
		}
		
		InformationMismatch infMismatch = (InformationMismatch) pageObject;
		assertThat(
				infMismatch.getHeaderMessage(), 
				is("This information doesn't match our records."));
	}
	
	@Then("^user is prompted to provide the account number$")
	public void userIsPromptedForAccountNumber()  {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByAccountNumber)){
			logAndFail(UserByAccountNumber.class.getSimpleName());
		}
		
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		
		assertThat(
				"Expected an account number prompt to user",
				userByAccountNumber.getErrorMesssage(), 
				notNullValue());
		
		assertThat(
				userByAccountNumber.getErrorMesssage().get("invalidAccountNumberError"), 
				is("Please enter your account number to proceed. It is the number located "
						+ "on the top right of your Comcast bill."));
	}
	
	@Then("^user is prompted to provide street address or phone number$")
	public void userIsPromptedForAddressOrPhoneNumber()  {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByAccountNumber)){
			logAndFail(UserByAccountNumber.class.getSimpleName());
		}
		
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		
		assertThat(
				"Expected street address or phone number prompt to user",
				userByAccountNumber.getErrorMesssage(), 
				notNullValue());
		
		assertThat(
				userByAccountNumber.getErrorMesssage().get("verificationError"), 
				is("Select street address or phone number."));
	}

	
	@Then("^user is prompted to provide a valid phone number$")
	public void userIsPromptedForPhoneNumber()  {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByMobile)){
			logAndFail(UserByMobile.class.getSimpleName());
		}
		
		UserByMobile userByPhoneNumber = (UserByMobile)pageObject;
		
		assertThat(
				userByPhoneNumber.getErrorMessage(), 
				is("This phone number is invalid."));
	}

	@When("^user provides account details by invalid account number and street address$")
	public void userEntersInValidAccountNumberAndStreetAddressOnUserAccountNumberPage() {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByAccountNumber)){
			logAndFail(UserByAccountNumber.class.getSimpleName());
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if (commonSteps.getAccount() != null && 
		   commonSteps.getAccount().getAccountId() != null && 
		   commonSteps.getAccount().getAddress() != null) {
			userByAccountNumber.enterAccountNumber(INVALID_ACCOUNT_NO);
			userByAccountNumber.enterStreetAddress(commonSteps.getAccount().getAddress());
		}  else {
			logger.error(DATA_NOT_FOUND);
        	fail(DATA_NOT_FOUND);
		}
		pageObject = userByAccountNumber.Continue();
		commonSteps.setPageObject(this.pageObject);
	}
	
	@When("^user provides account details by invalid account number and phone number$")
	public void userEntersInValidAccountNumberAndPhoneNumberOnUserAccountNumberPage() {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByAccountNumber)){
			logAndFail(UserByAccountNumber.class.getSimpleName());
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if(commonSteps.getAccount() != null && commonSteps.getAccount().getPhoneNumber() != null) {
			userByAccountNumber.enterAccountNumber(INVALID_ACCOUNT_NO);
			userByAccountNumber.enterPhoneNumber(commonSteps.getAccount().getPhoneNumber());
		} else {
			logger.error(DATA_NOT_FOUND);
        	fail(DATA_NOT_FOUND);
		}
		pageObject = userByAccountNumber.Continue();
		commonSteps.setPageObject(this.pageObject);
	}


	@When("^user provides account details by account number and invalid address$")
	public void userEntersValidAccountNumberAndInvalidAddress() {
		pageObject = commonSteps.getPageObject();
		if(!(pageObject instanceof UserByAccountNumber)){
			logAndFail(UserByAccountNumber.class.getSimpleName());
		}
		UserByAccountNumber userByAccountNumber = (UserByAccountNumber)pageObject;
		if(commonSteps.getAccount() != null && commonSteps.getAccount().getAccountId() != null) {
			userByAccountNumber.enterAccountNumber(commonSteps.getAccount().getAccountId());
			userByAccountNumber.enterStreetAddress(INVALID_ADDRESS);
		} else {
			logger.error(DATA_NOT_FOUND);
        	fail(DATA_NOT_FOUND);
		}
		pageObject = userByAccountNumber.Continue();
		commonSteps.setPageObject(this.pageObject);
	}

	
	@When("^user provides an incorrect phone number$")
	public void userProvidesIncorrectPhoneNumber()  {
		
		pageObject = commonSteps.getPageObject();
		UserByMobile userByMobileNumber = (UserByMobile) pageObject;
		userByMobileNumber.enterMobileNumber(INCORRECT_TN);
		pageObject = userByMobileNumber.Continue();
		commonSteps.setPageObject(this.pageObject);
	}
	
	@When("^user provides an invalid phone number$")
	public void userProvidesInvalidPhoneNumber()  {
		
		pageObject = commonSteps.getPageObject();
		UserByMobile userByMobileNumber = (UserByMobile) pageObject;
		userByMobileNumber.enterMobileNumber(INVALID_TN);
		userByMobileNumber.Continue();
	}
	
	@When("^user provides incorrect social security number$")
	public void userProvidesIncorrectSocialSecurityNumber() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterSocialSecurityNumber(INCORRECT_SSN);
	}

	@When("^user provides invalid social security number$")
	public void userProvidesInvalidSocialSecurityNumber() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterSocialSecurityNumber(INVALID_SSN);
		userBySSN.Continue();
	}

	@And("^user provides incorrect date of birth$")
	public void userProvidesIncorrectDateOfBirth() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterDateOfBirth(INCORRECT_DOB);
	}

	@And("^user provides invalid date of birth$")
	public void userProvidesInvalidDateOfBirth() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterDateOfBirth(INVALID_DOB);
	}

	
	@And("^user provides incorrect TN$")
	public void userProvidesIncorrectTN() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterPhoneNumber(INCORRECT_TN);
		pageObject = userBySSN.Continue();
		commonSteps.setPageObject(this.pageObject);
	}

	@And("^user provides invalid TN$")
	public void userProvidesInvalidTN() {
		
		pageObject = commonSteps.getPageObject();
		UserBySSN userBySSN = (UserBySSN) pageObject;
		userBySSN.enterPhoneNumber(INVALID_TN);
		userBySSN.Continue();
	}

	@Then("^user is prompted to provide valid SSN$")
	public void userIsPromptedForValidSSN()  {

		UserBySSN userBySSN = (UserBySSN)pageObject;
		
		assertThat(
				"Expected prompt for SSN to user",
				userBySSN.getErrorMesssage(), 
				hasItem(PROMPT_FOR_VALID_SSN));
	}
	
	@Then("^user is prompted to provide valid date of birth$")
	public void userIsPromptedForValidDOB()  {

		UserBySSN userBySSN = (UserBySSN)pageObject;
		
		assertThat(
				"Expected prompt for date of birth to user",
				userBySSN.getErrorMesssage(), 
				hasItem(PROMPT_FOR_VALID_DOB));
	}

	@Then("^user is prompted to provide valid TN$")
	public void userIsPromptedForValidTN()  {

		UserBySSN userBySSN = (UserBySSN)pageObject;
		
		assertThat(
				"Expected prompt for TN to user",
				userBySSN.getErrorMesssage(), 
				hasItem(PROMPT_FOR_VALID_TN));
	}

	public Object getPageObject() {
		return this.pageObject;
	}
	
	public UsernameLookupConfirmtaion getLookUpSuccessPage() {
		return lookUpSuccessPage;
	}
	
	@After
	public void tearDown() {
		try {
			PageNavigator.close(commonSteps.getPfId());
			WebDriver browser = commonSteps.getBrowser();
			if (browser != null && citfTestInitializer.isRemoteBrowser(browser) && 
					((RemoteWebDriver) browser).getSessionId() != null) {
				final String sessionId = ((RemoteWebDriver) browser).getSessionId().toString();
				final String status = testScenario.getStatus().equals("passed")
				                      ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
				                      : ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
				citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
			}
			if (browser != null) {
				browser.quit();
			}
			List<String> users = new ArrayList<String>(); 
			users.add(username);
			ldap.purgeUsers(users);
		} catch (Exception e) {
			logger.error("Error occured while performing clean up activities : ", e );
		}
		
	}

	private String getUserName() {
		return userNameGenerator.next();
	}
	
	private String getPassword() {
		return PasswordGenerator.generatePassword(8, 10, 2, 1, 1);
	}
	
	private void logAndFail(String classname) {
		
		logger.error(classname + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS + 
    			citfTestInitializer.getPageName(getPageObject()));
		
    	fail(classname + MESSAGE_PAGE_NOT_FOUND + MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS +  
    			citfTestInitializer.getPageName(getPageObject()));
	}
	
	

	private static final String MESSAGE_PAGE_HAS_BEEN_IDENTIFIED_AS = " Page has been identified as ";
	private static final String MESSAGE_PAGE_NOT_FOUND = " page not found";
	private static final String INCORRECT_TN = "2158001234";
	private static final String INVALID_TN = "0000000000";
	private static final String INVALID_DOB = "15/40/0000";
	private static final String INCORRECT_DOB = "12/20/1976";
	private static final String INVALID_SSN = "0000";
	private static final String INCORRECT_SSN = "1000";
	private static final String INVALID_ACCOUNT_NO = "000000000";
	private static final String INVALID_ADDRESS = "invalid address";
	private static final String PROMPT_FOR_VALID_SSN = "Please enter only the last 4 digits of your social security number.";
	private static final String PROMPT_FOR_VALID_DOB = "This date is invalid.";
	private static final String PROMPT_FOR_VALID_TN = "This phone number is invalid.";
	private static final String DATA_NOT_FOUND = "Unable to retrieve data from data provider";
	
}
