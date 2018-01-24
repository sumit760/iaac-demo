package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CreateUID extends CitfTestInitializer {
	
	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmCreateUserID";
	
	
	@Given("^the user is on xfinity Login page and wants to create his userID$")
	public void the_user_is_on_xfinity_Login_page_and_want_to_create_userID() throws Exception {
	   
	}

	//Create UserID verified by phone number
	
	@When("^the user identifies himself with phone number and provides userId: \\~([^\"]*)\\~ and password recovery option as: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_phone(String uid,String recoveryOption,String environment,String platform,String type,String browser) throws Exception {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		if (recoveryOption.equalsIgnoreCase("Email") || recoveryOption.equalsIgnoreCase("UIDEmail")) {
			parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		}
		parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_NEW_USER_ID, uid);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}
	
	
	@Then("^the user is able to create his Comcast userID verified by mobile$")
	public void the_user_is_able_to_create_userID_VERIFIED_BY_MOBILE() throws Exception {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Create userID using Phone", TEST_CLASS_NAME, "createUserIDByMobileNumber", parameters);
	}

	
	//Create UserID verified by account number and street address
	
	@When("^the user identifies himself with account number and identifier: \\~([^\"]*)\\~ and provides userId: \\~([^\"]*)\\~ and password recovery option as: \\~([^\"]*)\\~ in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~ .$")
	public void the_user_identifiedBy_AccountNo_StreetAddress_Phone(String identifierType,String uid,String recoveryOption,String environment,String platform,String type,String browser) throws Exception {
		classId = generateUniqueClassId(TEST_CLASS_NAME);

		if (recoveryOption.contains("Email"))
			parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		if (recoveryOption.contains("Phone"))
			parameters.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		parameters.put(IdmFilterKeys.ADDRESS.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_IDENTIFIER_TYPE, identifierType);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_NEW_USER_ID, uid);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN, recoveryOption);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	
	@Then("^the user is able to create his Comcast userID verified by account number$")
	public void the_user_is_able_to_create_userID_VERIFIED_BY_ACCOUNT_NUMBER() throws Exception {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Create userID using Account Number", TEST_CLASS_NAME, "createUserIDByAccountNumberAndStreetAddressOrPhone", parameters);
	}
	
	

}
