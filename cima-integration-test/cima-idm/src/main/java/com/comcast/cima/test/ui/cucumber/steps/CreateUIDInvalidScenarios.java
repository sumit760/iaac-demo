package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateUIDInvalidScenarios extends CitfTestInitializer {
	
	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmCreateUserIDInvalidScenarios";
	
	
	@Given("^the user is on xfinity Login page and clicks create userID link$")
	public void the_user_is_on_xfinity_Login_page_and_clicks_create_userID() throws Throwable {
	   
	}

	//Provide invalid phone number when verifying user identity
	
	@When("^the user provides phone number type: \\~([^\"]*)\\~ with value: \\~([^\"]*)\\~ when verifying his identity in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_invalidPhoneNumber(String phoneNumberType,String invalidPhoneNumber,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE_TYPE, phoneNumberType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE, invalidPhoneNumber);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid phone number error message$")
	public void the_user_gets_invalid_phonenumber_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify user by invalid phone number when create userID", TEST_CLASS_NAME, "tryCreateUserWithVerificationByInvalidPhone", parameters);
	}

	
	
	//Provide invalid sms code when verifying user identity
	
	@When("^the user provides smscode of type: \\~([^\"]*)\\~ with value: \\~([^\"]*)\\~ when verifying his identity in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_invalidSMSCode(String smsCodeType,String invalidSMSCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE_TYPE, smsCodeType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE, invalidSMSCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid sms code error message$")
	public void the_user_gets_invalid_smscode_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify user by invalid sms code when create userID", TEST_CLASS_NAME, "tryCreateUserWithVerificationByInvalidSMSCode", parameters);
	}

	
	
	//Provide invalid username when creating userID
	
	@When("^the user provides username of type: \\~([^\"]*)\\~ of value: \\~([^\"]*)\\~ when creating userID in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_UserName(String userNameType,String invalidUsername,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_NAME_TYPE, userNameType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_NAME, invalidUsername);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid username error message$")
	public void the_user_gets_invalid_username_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid username is checked", TEST_CLASS_NAME, "tryCreateUserWithInvalidUserName", parameters);
	}

	
	
	//Provide invalid password when creating userID
	
	@When("^the user provides invalid password of type: \\~([^\"]*)\\~ as: \\~([^\"]*)\\~ when creating userID in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_Password(String passwordType,String password,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD_TYPE, passwordType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PASSWORD, password);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid password error message$")
	public void the_user_gets_invalid_password_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid password is checked", TEST_CLASS_NAME, "tryCreateUserWithInvalidPassword", parameters);
	}

	
	
	//Provide invalid alternate email when creating userID
	
	@When("^the user provides invalid alternate email: \\~([^\"]*)\\~ when creating userID in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_AltEmail(String altEmail,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_ALT_EMAIL, altEmail);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid email error message$")
	public void the_user_gets_invalid_email_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid email is checked", TEST_CLASS_NAME, "tryCreateUserWithInvalidAltEmail", parameters);
	}

	
	
	//Provide invalid Mobile Phone when creating userID
	
	@When("^the user provides mobile phone number type: \\~([^\"]*)\\~ with value: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_MobilePhone(String phoneNumberType,String invalidPhoneNumber,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE_TYPE, phoneNumberType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE, invalidPhoneNumber);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid mobile phone error message$")
	public void the_user_gets_invalid_mobilePhone_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid phone is checked", TEST_CLASS_NAME, "tryCreateUserWithInvalidMobilePhone", parameters);
	}

	
	
	//Provide invalid answer to security question when creating userID
	
	@When("^the user provides invalid SQA type: \\~([^\"]*)\\~ of value: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_Security_Answer(String SQAType,String SQAValue,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER_TYPE, SQAType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER, SQAValue);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid answer error message$")
	public void the_user_gets_invalid_Security_Answer_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid security answer is checked", TEST_CLASS_NAME, "tryCreateUserWithInvalidSecurityAnswer", parameters);
	}

	
	
	//Provide invalid account number,street address and phone number when verifying user identity
	
	@When("^the user provides invalid identity type \\~([^\"]*)\\~ of value \\~([^\"]*)\\~ when verifying identity in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_provides_invalid_AccountNo_StreetAddress_Phone(String identityType,String identityValue,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_IDENTITY_TYPE, identityType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_IDENTITY_VALUE, identityValue);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid identity parameter error message$")
	public void the_user_gets_invalid_Identity_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify invalid account number,street address and phone number when verifying user identity", TEST_CLASS_NAME, "tryVerifyUserWithInvalidAccountStreetOrPhone", parameters);
	}

	
	
	

}
