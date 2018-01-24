package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PasswordReset extends CitfTestInitializer {

	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmPasswordReset";
	
	
	@Given("^the user is on xfinity Login page$")
	public void the_user_is_on_xfinity_Login_page() throws Throwable {
	   
	}

	//Inactive user (check whether user can login, password reset isn't possible for this user)
	@Then("^the inactive user \\~([^\"]*)\\~ is not able to login because this user has a \\~([^\"]*)\\~ value for the 'cstLoginStatus' attribute in ESD$")
	public void the_inactive_user_is_not_able_to_login(String inUsername, String inCstLoginStatus) throws Throwable {
		parameters.put(ICimaCommonConstants.TEST_CASE_USERNAME, inUsername);
		parameters.put(ICimaCommonConstants.ESD_FLAG_CST_LOGIN_STATUS, inCstLoginStatus);
		if(isEnvironmentValid(this.validEnvironment)) {
			runTest(classId, "Validate inactive user is not able to login", TEST_CLASS_NAME, "validatePasswordResetUsingInactiveUser", parameters);
		}
	}

	//Compromised user password reset
	@When("^the compromised user attempts to login in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_compromised_user_attempts_to_login(String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		validEnvironment = environment;
	}
	@Then("^the compromised user is able to reset the password$")
	public void the_compromised_user_is_able_to_reset_the_password() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment)) {
			runTest(classId, "Validate Password reset for compromised user", TEST_CLASS_NAME, "validatePasswordResetUsingCompromisedUser", parameters);
		}
	}

	//Password reset with only SQA recovery option
	
	@When("^the user with recovery option as SQA chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_SQA(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using SQA recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_SQA_recovery_options_method() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with SQA", TEST_CLASS_NAME, "validatePasswordResetUsingSQA", parameters);
	}
	
	
	
	//Password reset with SMS recovery option
	
	@When("^the user with recovery option as SMS chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_SMS_chooses_SMS(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using SMS recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_SMS_recovery_options_method() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with SMS", TEST_CLASS_NAME, "validatePasswordResetUsingSMS", parameters);
	}

	
	
	//Password reset with Email recovery option
	
	@When("^the user with recovery option as Email chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_Email_chooses_EMAIL(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
	   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using Email recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_Email_recovery_options_method() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with SMS", TEST_CLASS_NAME, "validatePasswordResetUsingEmail", parameters);
	}

	
		
	//Password reset with SMS and SQA recovery option
	
	@When("^the user with recovery option as SMS and SQA chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_SMS_and_SQA(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable  {
	 
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using SMS and SQA recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_SMS_and_SQA() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with SQA and SMS", TEST_CLASS_NAME, "validatePasswordResetUsingSMSAndSQA", parameters);
	}
	
	
	
	//Password reset with Email and SQA recovery option
	
	@When("^the user with recovery option as Email and SQA chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_Email_and_SQA(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
			classId = generateUniqueClassId(TEST_CLASS_NAME);
			parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

			
			parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
			parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
			parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
			parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
			
			validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using Email and SQA recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_Email_and_SQA_recovery_options_method() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with Email and SQA", TEST_CLASS_NAME, "validatePasswordResetUsingEmailAndSQA", parameters);
	}
	
	
	
	//Password reset with Email, SMS and SQA recovery option
	
	@When("^the user with recovery option as Email, SMS and SQA chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_Email_and_SMS(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password using Email and SMS recovery options method$")
	public void the_user_is_able_to_reset_the_password_using_Email_and_SMS_recovery_options_method() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset for using with Email, SMS and SQA", TEST_CLASS_NAME, "validatePasswordResetUsingEmailSMSAndSQA", parameters);
	}
	
	
	
	//Password reset with the help of field agents
	
	@When("^the user forgots his recovery option as Email, SMS and SQA chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_with_recovery_option_as_None(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password with the help of the agents$")
	public void the_user_is_able_to_reset_the_password_with_the_help_of_agents() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset with the help of agents", TEST_CLASS_NAME, "validatePasswordResetUsingAgents", parameters);
	}

	
	
	//Password reset with SQA or SMS after selecting Email
	
	@When("^the user does not have email access after selecting email and chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void user_chooses_sqa_sms_after_email(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password with either of SQA or SMS$")
	public void the_user_is_able_to_reset_the_password_either_with_SQA_SMS() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset either with SQA or SMS after Selecting Email", TEST_CLASS_NAME, "validatePasswordResetUsingSQAOrSMSAfterSelectingEmail", parameters);
	}
	
	
	
	//Password reset with SQA or Email after selecting SMS
	
	@When("^the user does not have phone access after selecting SMS and chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void user_chooses_sqa_email_after_sms(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password with either of SQA or Email$")
	public void the_user_is_able_to_reset_the_password_either_with_SQA_Email() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset either with SQA or Email after Selecting SMS", TEST_CLASS_NAME, "validatePasswordResetUsingSQAOrEmailAfterSelectingSMS", parameters);
	}

	
	
	//Password reset with SQA or SMS after generating recovery link by Email
	
	@When("^the user does not have email access after generating recovery link by email and chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void user_chooses_sqa_sms_after_generating_recoverylinkby_email(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password with either of SQA or SMS even after generating the recovery link by email$")
	public void the_user_is_able_to_reset_the_password_either_with_SQA_SMS_After_RecoveryLinkGeneration_byEmail() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset either with SQA or SMS after generating recovery link by email", TEST_CLASS_NAME, "validatePasswordResetUsingSQAOrSMSAfterRecoveryLinkGenByEmail", parameters);
	}

	
	
	//Password reset with SQA or Email after generating recovery code by SMS
	
	@When("^the user does not have phone access after generating recovery code by SMS and chooses \\~([^\"]*)\\~ as recovery option in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void user_chooses_sqa_email_after_generating_recoveryCodeby_sms(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
		   
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.USER_ROLE.getValue(), ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to reset the password with either of SQA or Email even after generating the recovery code by SMS$")
	public void the_user_is_able_to_reset_the_password_either_with_SQA_Email_After_RecoveryCodeGeneration_bySMS() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate Password reset either with SQA or Email after generating recovery code by SMS", TEST_CLASS_NAME, "validatePasswordResetUsingSQAOrEmailAfterRecoveryCodeGenBySMS", parameters);
	}



}
