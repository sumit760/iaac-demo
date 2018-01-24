package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class InvalidPasswordReset extends CitfTestInitializer {
	
	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmPasswordResetInvalidScenarios";
	

	@Given("^the user is on xfinity SignIn page$")
	public void the_user_is_on_xfinity_SignIn_page() throws Throwable {
	   
	}

	
	//	Password reset for an invalid user
	
	@When("^the invalid user: \\~([^\"]*)\\~ continue to recover password in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_invalid_userId(String userID,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_USERID, userID);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays error message for the user$")
	public void error_displayed_invalid_userId() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for invalid userId", TEST_CLASS_NAME, "tryPasswordResetInvalidUserId", parameters);
	}
	
	
	
	//	Password reset for an user with invalid SQA and zipcode
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides wrong answer: \\~([^\"]*)\\~ and wrong zipcode: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_invalid_SQA_ZipCode(String recoveryOption,String wrongAnswer,String wrongZipCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER, wrongAnswer);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_ZIPCODE, wrongZipCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays error message for invalid SQA  and Invalid ZIP$")
	public void error_displayed_invalid_SQA_ZipCode() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for invalid SQA and ZipCode", TEST_CLASS_NAME, "tryPasswordResetInvalidSQAAndZipCode", parameters);
	}

	
	
	//	Password reset for an user with empty SQA and zipcode
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides empty answer: \\~([^\"]*)\\~ and empty zipcode: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_empty_SQA_ZipCode(String recoveryOption,String wrongAnswer,String wrongZipCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
				
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER, wrongAnswer);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_ZIPCODE, wrongZipCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity display error message for empty SQA and empty ZIP$")
	public void error_displayed_empty_SQA_ZipCode() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for empty SQA and ZipCode", TEST_CLASS_NAME, "tryPasswordResetEmptySQAAndZipCode", parameters);
	}


	
	//	Password reset for an user with empty new password
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides empty password in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_empty_NewPassword(String recoveryOption,String environment,String platform,String type,String browser) throws Throwable {
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

	@Then("^Xinfity display empty password error message$")
	public void error_displayed_empty_password() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for empty SQA and ZipCode", TEST_CLASS_NAME, "tryPasswordResetEmptyNewPassword", parameters);
	}

	
	//	Password reset for an user with password and retype password not matching
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides non matching \\~([^\"]*)\\~ and \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_nonmatching_password(String recoveryOption,String password,String retypePassword,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD, password);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_RETYPE_PASSWORD, retypePassword);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity display error message for invalid confirm passwords$")
	public void error_displayed_nonmatching_password() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for nonmatching password and confirm password", TEST_CLASS_NAME, "tryPasswordResetConflictingPassword", parameters);
	}

	
	
	//	Password reset for an user with password less than 8 char or more than 16 char
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides password: \\~([^\"]*)\\~ less than eight or more than sixteen chars in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_password_less_than_8_or_more_than_16_chars(String recoveryOption,String password,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD, password);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity display invalid password error message$")
	public void error_displayed_incorrect_password_length() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for password less than eight or more than sixteen chars", TEST_CLASS_NAME, "tryPasswordResetIncorrectPasswordLength", parameters);
	}

	

	//	Password reset for an user with password same as username or last name or first name
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides password as: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_password_username_firstname_lastname(String recoveryOption,String passwordType,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE, passwordType);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays invalid password error message stating password can not be username or last name or first name$")
	public void error_displayed_password_cannot_contain_username_fname_lname() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for password as username firstname or lastname", TEST_CLASS_NAME, "tryPasswordResetWithPasswordAsUserDetails", parameters);
	}

	
	
	//	Password reset for an user with invalid password format
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides invalid password of type: \\~([^\"]*)\\~ as: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_invalid_password_format(String recoveryOption,String passwordType,String password,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE, passwordType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD, password);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays invalid password format error message$")
	public void error_displayed_invalid_password_format() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for invalid password format", TEST_CLASS_NAME, "tryPasswordResetWithInvalidPasswordFormats", parameters);
	}


	
	//	Password reset for an user with invalid reset code from the agent
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides invalid reset codde: \\~([^\"]*)\\~ from the agent in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_invalid_reset_code(String recoveryOption,String resetCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.SECRET_QUESTION.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_AGENT_RESET_CODE, resetCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays invalid reset code error message$")
	public void error_displayed_invalid_resetcode() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for invalid reset code", TEST_CLASS_NAME, "tryPasswordResetWithInvalidAgentResetCode", parameters);
	}
	
	

	//	Password reset for an user with invalid SMS code
	
	@When("^the user with RecoveryOption: \\~([^\"]*)\\~ provides invalid SMS codde: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser : \\~([^\"]*)\\~$")
	public void the_user_with_invalid_sms_code(String recoveryOption,String smsCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE, smsCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^Xinfity displays invalid SMS code error message$")
	public void error_displayed_invalid_smscode() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate error message for invalid sms code", TEST_CLASS_NAME, "tryPasswordResetWithInvalidSMSCode", parameters);
	}




}
