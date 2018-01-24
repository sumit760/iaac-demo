package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class InvalidUsernameLookup extends CitfTestInitializer {
	
	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmInvalidUsernameLookup";
	
	@Given("^the user is on xfinity Login page and clicks username lookup link$")
	public void the_user_is_on_xfinity_Login_page_and_clicks_username_lookup() throws Throwable {
	   
	}

	//Provide invalid phone number when verifying user identity
	
	@When("^the user provides phone number type: \\~([^\"]*)\\~ with value: \\~([^\"]*)\\~ when verifying his identity for username lookup in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_invalidPhoneNumber(String phoneNumberType,String invalidPhoneNumber,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE_TYPE, phoneNumberType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_PHONE, invalidPhoneNumber);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid phone number error message for username lookup$")
	public void the_user_gets_invalid_phonenumber_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify user by invalid phone number when username lookup", TEST_CLASS_NAME, "tryUsernameLookupByInvalidPhone", parameters);
	}
	
	
	//Provide invalid sms code when verifying user identity
	
	@When("^the user provides smscode of type: \\~([^\"]*)\\~ with value: \\~([^\"]*)\\~ when verifying his identity for username lookup in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_invalidSMSCode(String smsCodeType,String invalidSMSCode,String environment,String platform,String type,String browser) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
		
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE_TYPE, smsCodeType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_SMS_CODE, invalidSMSCode);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user gets the invalid sms code error message for username lookup$")
	public void the_user_gets_invalid_smscode_error() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Verify user by invalid sms code when username lookup", TEST_CLASS_NAME, "tryUsernameLookupByInvalidSMSCode", parameters);
	}

}
