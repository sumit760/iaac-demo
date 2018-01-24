package com.comcast.cima.test.ui.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IDMGeneralKeys;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.IdmFilterKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UsernameLookup extends CitfTestInitializer {

	private String classId = null;
	private Map<String, String> parameters = new HashMap<String, String>();
	private String validEnvironment = null;
	private static final String TEST_CLASS_NAME="com.comcast.cima.test.ui.tests.ITestIdmUsernameLookup";
	
	@Given("^account number \\~([^\"]*)\\~ alternative email and \\~([^\"]*)\\~ after clicking the link in sign in page and selecting account number in method selection page$")
	public void givenWithValidAccountAndOtherParam(String alternateEmailStatus, String secondAttribute) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, secondAttribute);
	}

	@When("^the user is going to lookup his username with account number and another attribute in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithValidAccountAndOtherParam(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out his email using account number and continues by selecting email$")
	public void thenWithValidAccountAndOtherParam() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid account number and address/phone", TEST_CLASS_NAME, "lookupUsingValidAddressAndOtherValidParam", parameters);
	}
	
	
	
	
	@Given("^phone number \\~([^\"]*)\\~ alternative email after clicking the link in sign in page and selecting phone number in method selection page$")
	public void givenWithValidPhone(String alternateEmailStatus) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
	}

	@When("^the user is going to lookup his username with phone number in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithValidPhone(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out his email using phone number and continues by selecting email$")
	public void thenWithValidPhone() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid phone number", TEST_CLASS_NAME, "lookupUsingValidPhone", parameters);
	}
	
	
	
	
	@Given("^account number and \\~([^\"]*)\\~ after clicking the link in sign in page and selecting account number in method selection page$")
	public void givenWithFreshAccount(String secondAttribute) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, IDMGeneralKeys.FRESH_ACCOUNT.getValue());
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION_TWO, secondAttribute);
	}

	@When("^the non-exist user is going to lookup his username with account number and another attribute in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithFreshAccount(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user has been redirected to create user page using account number$")
	public void thenWithFreshAccount() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid fresh account without any user by account number", TEST_CLASS_NAME, "lookupUsingFreshAccountWithValidDataByAccount", parameters);
	}
	
	
	
	
	@Given("^phone number after clicking the link in sign in page and selecting phone number in method selection page$")
	public void givenWithFreshAccount_phone() throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, IDMGeneralKeys.FRESH_ACCOUNT.getValue());
	}

	@When("^the non-exist user is going to lookup his username with phone number in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithFreshAccount_phone(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user has been redirected to create user page using phone number$")
	public void thenWithFreshAccount_phone() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid fresh account without any user by phone number", TEST_CLASS_NAME, "lookupUsingFreshAccountWithValidDataByPhone", parameters);
	}
	
	
	
	
	@Given("^account number \\~([^\"]*)\\~ alternative email and \\~([^\"]*)\\~ after clicking the create username in sign in page and selecting account number in method selection page$")
	public void givenUserExistWithValidAccountAndOtherParam(String alternateEmailStatus, String secondAttribute) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, secondAttribute);
	}

	@When("^the user is going to create username with account number and another attribute in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenUserExistWithValidAccountAndOtherParam(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out username and email using account number and continues by selecting email$")
	public void thenUserExistWithValidAccountAndOtherParam() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup in create UID using valid account number and address/phone", TEST_CLASS_NAME, "lookupInCreateUIDUsingValidAddress", parameters);
	}
	
	

	
	@Given("^phone number \\~([^\"]*)\\~ alternative email after clicking the create username in sign in page and selecting phone number in method selection page$")
	public void givenUserExistWithValidPhone(String alternateEmailStatus) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
	}

	@When("^the user is going to create username with phone number in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenUserExistWithValidPhone(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out username and email using phone number and continues by selecting email$")
	public void thenUserExistWithValidPhone() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup in create UID using valid phone number", TEST_CLASS_NAME, "lookupInCreateUIDUsingValidPhone", parameters);
	}
	
	
	
	
	@Given("^account number \\~([^\"]*)\\~ alternative email and \\~([^\"]*)\\~ after clicking the forgot password in sign in page and selecting account number in method selection page$")
	public void givenLookupInForgotPwAccountAndOtherParam(String alternateEmailStatus, String secondAttribute) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, secondAttribute);
	}

	@When("^the user is going to forgot password with account number and another attribute in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenLookupInForgotPwAccountAndOtherParam(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out username with or without email through password reset flow$")
	public void thenLookupInForgotPwAccountAndOtherParam() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup in forgot password using valid account number and address/phone", TEST_CLASS_NAME, "lookupThroughPasswdResetUsingValidAccount", parameters);
	}
	
	

	
	@Given("^phone number \\~([^\"]*)\\~ alternative email after clicking the forgot password in sign in page and selecting phone number in method selection page$")
	public void givenLookupInForgotPwPhone(String alternateEmailStatus) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
	}

	@When("^the user is going to forgot password with phone number in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenLookupInForgotPwPhone(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out username and email in forgot password using phone number and continues by selecting email$")
	public void thenLookupInForgotPwPhone() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup in forgot password using valid phone number", TEST_CLASS_NAME, "lookupInForgotPWUsingValidPhone", parameters);
	}
	
	
	
	
	@Given("^business account number \\~([^\"]*)\\~ alternative email and \\~([^\"]*)\\~ after clicking the link in sign in page and selecting account number in method selection page$")
	public void givenWithValidBusinessAccountAndOtherParam(String alternateEmailStatus, String secondAttribute) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, secondAttribute);
	}

	@When("^the user is going to lookup his username with business account number and another attribute in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithValidBusinessAccountAndOtherParam(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out his email using business account number and continues by selecting email$")
	public void thenWithValidBusinessAccountAndOtherParam() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid business account number and address/phone", TEST_CLASS_NAME, "lookupUsingValidBusinessAddressAndOtherValidParam", parameters);
	}
	
	
	
	
	@Given("^business user phone number \\~([^\"]*)\\~ alternative email after clicking the link in sign in page and selecting phone number in method selection page$")
	public void givenWithValidBusinessPhone(String alternateEmailStatus) throws Throwable {
		classId = generateUniqueClassId(TEST_CLASS_NAME);
		parameters.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), 
				(alternateEmailStatus!=null && "WITHOUT".equals(alternateEmailStatus)) ? ICimaCommonConstants.CACHE_FLTR_VALUE_NULL : ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		parameters.put(ICimaCommonConstants.TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION, "PHONE");
	}

	@When("^the user is going to lookup his username with business user phone number in Environment : \\~([^\"]*)\\~ , Platform \\~([^\"]*)\\~ , device type : \\~([^\"]*)\\~ and Browser :\\~([^\"]*)\\~$")
	public void whenWithValidBusinessPhone(String environment, String platform, String type, String browser) throws Throwable {
		
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_PLATFORM, platform);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_DEVICE_TYPE, type);
		parameters.put(ICimaCommonConstants.SAUCELABS_EXECUTION_BROWSER, browser);
		
		validEnvironment = environment;
	}

	@Then("^the user is able to find out his email using business user phone number and continues by selecting email$")
	public void thenWithValidBusinessPhone() throws Throwable {
		if(isEnvironmentValid(this.validEnvironment))
			runTest(classId, "Validate username lookup using valid business user phone number", TEST_CLASS_NAME, "lookupUsingValidBusinessPhone", parameters);
	}
	
}
