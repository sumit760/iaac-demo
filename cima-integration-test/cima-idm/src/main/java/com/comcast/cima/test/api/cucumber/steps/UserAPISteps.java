package com.comcast.cima.test.api.cucumber.steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.jsonObjs.IDMGetAccountsResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.IDMGetAccountsResponseJSON.Authorization;
import com.comcast.test.citf.common.cima.jsonObjs.IDMGetAccountsResponseJSON.AuthorizationRole;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
import com.comcast.test.citf.common.http.RestHandler;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.parser.JSONParserHelper;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.dataProvider.DataProviderToCreateUser;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UserAPISteps {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Scenario testScenario;
	private AccountCache.Account account;
	private UserAttributesCache.Attribute userAttributes;
	private String userAPIResponse;
	private Map<String, String> headerMap;
	private Map<String, String> paramMap;
	private String userCredUrl;
	private String resetCode;
	private String resetCodeNotFound;
	private TokenResponse accessTokenResponse;
	private String message;
	private String[] emailVerificationToken;
	private String emailLinkForVerification;
	private String verificationToken;
	private Object paresdObject;
	private String invalidInputMessage;
	private String homePhone;
	private String unregisteredUserID;
	private String unregisteredAlterEmail;
	private String unregisteredMobile;
	private IDMUserCredentialValidationResponseJSON parsedResponse;
	private List<Authorization> authorizations;
	private List<AuthorizationRole> authorizationRoles;
	private WebDriver browser;
	private String errorDescription;
    private FreshUsers freshUserAttributes;
	private String idmBaseUrl;
	private String cdvStatus;
	private String hsdStatus;
	private String vidStatus;
	private String userId;
	private String password;
	private String secretQuestion;
	private long timeTOGetResetCodeGetExpired;
	private String secretAnswer;
	private String unregisteredUserIdNew;
	private String invalidEmail;
	private static final String FLOW_URL="https://login-qa4.comcast.net/myaccount/create-uid";
	private static final String SUCCESS_MSG = "success";
	private static final String GENERIC_FAILURE_MSG = "create_primary_uid_generic_failure";
	private static final String UNSUCCESSFUL_USER_REGISTERATION_STATUS="create_secondary_uid_max_accounts_reached";
	private static final CharSequence RESETCODE_NOT_FOUND_ERROR_DESC = "No code found for the specified user";	
	private static final String RESETCODE_EXPIRED = "reset_code_expired";	
	private static final String INVALID_RESETCODE="ABCD";	
	private static final String RESETCODE_STRING = "resetCode/userName:";	
	
	@Autowired
	private UserAPICommonSteps commonSteps;
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private FreshAccountProvider freshAccountProvider;
	
	@Autowired
	private DataProviderToCreateUser freshUserProvider;
	
	@Autowired
	private ConfigurationDataProvider configDataProvider;
	
	@Autowired
	private LDAPInterface ldap;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private UserNameGeneratorService userNameGenerator;

	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
		this.account = null;
		this.userAttributes = null;
		this.userAPIResponse = null;
		this.userCredUrl = null;
		this.errorDescription = null;
		this.resetCode = null;
		this.idmBaseUrl = null;
		this.accessTokenResponse = null;
		this.emailLinkForVerification = null;
		this.emailVerificationToken = null;
		this.verificationToken = null;
		this.freshUserAttributes=null;
		this.invalidEmail="123456";
		this.message = ICimaCommonConstants.SUCCESS_MESSAGE;
		this.invalidInputMessage = ICimaCommonConstants.INVALID_INPUT_MESSAGE;
		this.paramMap = new HashMap<String, String>();
		this.resetCodeNotFound = ICimaCommonConstants.RESET_CODE_NOT_FOUND;
		this.homePhone = null;
		this.unregisteredUserID = null;
		this.unregisteredAlterEmail = null;
		this.unregisteredMobile = null;
		this.headerMap = new HashMap<String, String>();
		this.paresdObject = null;
		this.parsedResponse = null;
		this.authorizations = null;
		this.authorizationRoles = null;
		this.browser = null;
		this.cdvStatus = null;
		this.hsdStatus = null;
		this.vidStatus = null;
		this.password = null;
		this.browser = null;
		this.unregisteredUserIdNew=null;
		this.secretQuestion="What is your favorite beverage?";
		this.secretAnswer="COKE";
	}

	@After
	public void tearDown() {
		try{
			this.browser = commonSteps.getBrowser();
			if (this.account != null && this.account.getPhoneNumber() == null) {
					ldap.updateAttributeOfAuthorizationObject(account.getAccountId(), LdapAttribute.AUTHORIZATION_HOME_PHONE.getValue(), null);
			}
			if (this.browser != null) {
				browser.close();
			}
			final String status = testScenario.getStatus().equals("passed") ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
					: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
			citfTestInitializer.updateSauceLabsTestStatus(null, status);
		}catch(Exception e){
			logger.error("Error occured while performing clean up activities : ", e );
		}
	}

	@When("^care agent sends the request to create a reset code for the user$")
	public void requestToCreateResetCode()  {

		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		userAttributes = commonSteps.getUserAttributes();

		if (userAttributes == null) {
			throw new IllegalStateException("Unable to fetch user attributes from database for creating reset code");
		}
		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.userAttributes.getUserId();

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.POST);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response to create a reset code with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to create reset code with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	}

	@When("^care agent sends the request to read reset code for the user$")
	public void requestToCReadResetCode()  {

		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		userAttributes = commonSteps.getUserAttributes();

		if (userAttributes == null) {
			throw new IllegalStateException("Unable to fetch user attributes from database to read reset code");
		}

		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.userAttributes.getUserId();

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to read reset with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	}

	@When("^care agent sends the request to delete a reset code for the user$")
	public void deleteResetCode()  {

		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		userAttributes = commonSteps.getUserAttributes();

		if (userAttributes == null) {
			throw new IllegalStateException("Unable to fetch user attributes from database to delete reset code");
		}
		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.userAttributes.getUserId() + "/"
				+ this.resetCode;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.DELETE);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to delete reset code with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}

	}

	@Then("^response contains deleted reset code$")
	public void validateConfirmationOfDeleteResetCode() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.warn(this.errorDescription);
				throw new IllegalStateException(this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object for response of delete confirmation: ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object or response of delete confirmation");
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (parsedResponse.getResetCode().equals(this.resetCode)) {
					logger.info("reset code deleted successully " + parsedResponse.getResetCode());
				} else {
					logger.error(LOG_MESSAGE_UNABLE_TO_DELETE_RESET_CODE);
					throw new IllegalStateException(LOG_MESSAGE_UNABLE_TO_DELETE_RESET_CODE);
				}
			}

			else {
				logger.error(LOG_MESSAGE_JSON_PARSER_IS_RETURNING_NULL);
				throw new IllegalStateException(
						"JSON parser is returning null for the API response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during validation of reset code delete", e);
			throw new IllegalStateException("Exception occured during validation of reset code delete", e);
		}

	}

	@Then("^Response contains reset code not found$")
	public void validateResponseForDeletedResetCode() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.warn(this.errorDescription);
				throw new IllegalStateException(this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object for delete confirmation response: ", e);
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				
					if (this.resetCodeNotFound.equalsIgnoreCase(parsedResponse.getErrorCode())) {
						logger.info("reset code deletion confrimatin is validated " + parsedResponse.getResetCode());
					} else {
						logger.error(LOG_MESSAGE_UNABLE_TO_DELETE_RESET_CODE);
						throw new IllegalStateException(LOG_MESSAGE_UNABLE_TO_DELETE_RESET_CODE);
					}
				} 
		
			else {
				logger.error("Unable to parse User API Response");
				throw new IllegalStateException(
						"JSON parser is returning null for the API response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during the validation of reset code deletion", e);
			throw new IllegalStateException("Exception occured during the validation of reset code deletion", e);
		}
	}

	@Then("^response contains a valid reset code$")
	public void responseContainsValidResetCode() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				
				logger.warn("Exception occured while parsing the JSON format to object for delete confirmation response: ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object for delete confirmation response " + e);
			}

			if (paresdObject != null) {
				 parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (parsedResponse.getResetCode() != null) {
					logger.info("reset code generated successully " + parsedResponse.getResetCode());
					this.resetCode = parsedResponse.getResetCode();
				} else {
					throw new IllegalStateException("Response doesnot contains valid reset code");
				}
			}

			else {
				logger.error(LOG_MESSAGE_JSON_PARSER_IS_RETURNING_NULL);
				throw new IllegalStateException(
						"JSON parser is returning null for the response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during reset code validation", e);
			throw new IllegalStateException("Exception occured during reset code validation", e);
		}
	}

	@When("^application sends the request to send verification email for the user$")
	public void applicationSendsRequestToSendverificationEmail() {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();

		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verification email", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verification email" + this.userAttributes
					+ " userId: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getLastName());

		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting for sending verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting for sending verification email");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@Then("^response confirms message submission$")
	public void validateEmailSubmissionMessage() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object for email submission response: ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object for email submission response:");
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (message.contentEquals(parsedResponse.getMessage())) {
					logger.info("Verification email submitted successfully");
				} else {
					logger.error("verification email submission is not successful={} ", parsedResponse);
					throw new IllegalStateException(
							"verification email submission is not successful: " + parsedResponse);
				}

			}

			else {
				logger.error(LOG_MESSAGE_JSON_PARSER_IS_RETURNING_NULL);
				throw new IllegalStateException(
						"JSON parser is returning null for the response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.error("Exception occured while validating response of verification email", e.getMessage());
			throw new IllegalStateException(
					"Exception occured while validating response of verification email : " + this.userAPIResponse);
		}

	}

	@Then("^response contains valid account details of user$")
	public void responseContainsValidAccountDetailsOfUser() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object: ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object:");
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (parsedResponse.getBillingAccountId().equals(account.getAccountId())) {
					logger.info("Post email validation response contains valid billing account id");
				} else {
					logger.error("Post email validation response does not contain valid billing account id");
					throw new IllegalStateException(
							"Post email validation response does not contain valid billing account id");
				}

				if (parsedResponse.getFirstName().equals(account.getFirstName())) {
					logger.info("Post email validation response contains user first name");
				} else {
					logger.error("Post email validation response contains first name");
					throw new IllegalStateException("Post email validation response does not contain user first name");
				}

				if (parsedResponse.getLastName().equals(account.getLastName())) {
					logger.info("Post email validation response contains user Last name");
				} else {
					logger.error("Post email validation response contains Last name");
					throw new IllegalStateException("Post email validation response does not contain user Last name");
				}

			}

			else {
				logger.error(LOG_MESSAGE_JSON_PARSER_IS_RETURNING_NULL);
				throw new IllegalStateException(
						"JSON parser is returning null for the response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.error("Exception occured while validation user information from response of email validation",
					e.getMessage());
			throw new IllegalStateException(
					"Exception occured while validation user information from response of email validation"
							+ this.userAPIResponse);
		}
	}

	@Then("^email verification link is sent to the log$")
	public void validateEmailLinkInSplunkLog() {
		try {
			this.emailLinkForVerification = citfTestInitializer.readLog("VERIFICATION_EMAIL", LogReader.RegexArgument.EMAIL,
					this.userAttributes.getAlterEmail());
			if (emailLinkForVerification != null) {
				logger.info("verification link is retrieved from splunk log :" + emailLinkForVerification);
			} else {
				logger.error("verification mail could not be retrieved from splunk log");
				throw new IllegalStateException("verification mail could not be retrieved from splunk log");
			}
		} catch (Exception e) {
			logger.error("Email verification link is not verfied from splunk log ", e);
			throw new IllegalStateException("Email verification link is not verfied from splunk log :", e);
		}
	}

	@When("^email verification token is extracted from the log$")
	public void emailVerificationTokenIsExtractedFromTheLog() {

		if (emailLinkForVerification != null) {
			emailVerificationToken = this.emailLinkForVerification.split("=");
			verificationToken = emailVerificationToken[1];
		} else {
			logger.error("Email verification link could not be retrieved  from splunk log ");
			throw new IllegalStateException("Email verification link could not be retrieved from splunk log");
		}
	}

	@When("^application sends the request appending the verification token to validate verification email$")
	public void applicationSendsRequestAppendingVerificationTokenValidateVerificationEmail(){

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;

		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to validate verfifcation email", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to validate verfifcation email" + this.userAttributes
					+ " userId to send verification email " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}

		userCredUrl = this.idmBaseUrl + "validateVerificationEmail/?verify=" + verificationToken;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to send verification email with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}

	}

	@Then("^response confirms invalid input receieved$")
	public void responseConfirmsInvalidInputReceieved() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				
				logger.warn("Exception occured while parsing the JSON format to object for invalid input response: ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object invalid input response:");
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (invalidInputMessage.contentEquals(parsedResponse.getMessage())) {
					logger.info("verification email is not submitted with invalid parameter");
				} else {
					logger.error("verification email submitted with invalid paraemter={} ", parsedResponse);
					throw new IllegalStateException(
							"verification email submitted with invalid parameter" + parsedResponse);
				}

			}

			else {
				logger.error("JSON parser is returning null for invalid input receieved");
				throw new IllegalStateException(
						"JSON parser is returning null for invalid input receieved" + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.error("Exception occured while validating response of verification email for invalid parameter",
					e.getMessage());
			throw new IllegalStateException(
					"Exception occured while validating response of verification email  invalid parameter "
							+ this.userAPIResponse);
		}

	}

	@When("^application sends the request to send verification email for the user without billing account ID$")
	public void applicationSendsRequestToSendverificationEmailWithInvalidBillingAccountID(){

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without billing account id", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without billing account id" + this.userAttributes
					+ " userId to send verification email without billing account ID: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getLastName());

		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^application sends the request to send verification email for the user without GUID parameter$")
	public void applicationSendsRequestToSendverificationEmailWithoutGUID() {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();

		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without GUID", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without GUID" + this.userAttributes
					+ " userId for verfication email without GUID parameter: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getLastName());

		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException(
					"Exception occured while requesting to send verification email with invalid parameter");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@Given("^an unregistered user with alternate email$")
	public void givenAnUnregisteredUserWithAlternateEmail() {
		this.unregisteredUserID = ICimaCommonConstants.UNREGISTERED_USERID;
		this.unregisteredAlterEmail = ICimaCommonConstants.UNREGISTERED_ALTEREMAIL;
	}

	@Given("^an unregistered user with invalid alternate email$")
	public void givenAnUnregisteredUserWithInvalidAlternateEmail() {
		this.unregisteredUserID = ICimaCommonConstants.UNREGISTERED_USERID;
		this.unregisteredAlterEmail = ICimaCommonConstants.UNREGISTERED_INVALID_ALTEREMAIL;
	}

	@Given("^an unregistered user with mobile phone number$")
	public void givenAnUnregisteredUserWithMobileNumber() {
		this.unregisteredUserID = ICimaCommonConstants.UNREGISTERED_USERID;
		this.unregisteredMobile = ICimaCommonConstants.UNREGISTERED_MOBILE;
	}

	@Given("^an unregistered user with invalid mobile phone number$")
	public void givenAnUnregisteredUserWithInvalidMobileNumber() {
		this.unregisteredUserID = ICimaCommonConstants.UNREGISTERED_USERID;
		this.unregisteredMobile = ICimaCommonConstants.UNREGISTERED_INVALID_MOBILE;
	}
	
	@Given("^an active user with alternate email$")
	public void anActiveUserWithAlternateEmail() throws Exception {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
																	UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
		}
	}

	@Given("^an active user with mobile phone number$")
	public void anActiveUserWithPhoneNumber() throws Exception {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
																		UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
		}
	}
	
	@Given("^an active user with multiple billing accounts with X1 service$")
	public void anActiveUserWithMultipleBillingAccountsWithX1Service() throws Exception {
		String currentEnvironment = citfTestInitializer.getCurrentEnvironment();
		if(currentEnvironment.equalsIgnoreCase(ICimaCommonConstants.ENVIRONMENT_STAGE) || currentEnvironment.equalsIgnoreCase(ICimaCommonConstants.ENVIRONMENT_PROD)) {
			
			Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
													UserDataProvider.Filter.HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
													UserDataProvider.Filter.SERVICE_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			
			UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
			if (userObjects!=null) {
				this.account = userObjects.getAccount();
				this.userAttributes = userObjects.getUserAttr();
			}
			
			commonSteps.setAccount(this.account);
			commonSteps.setUserAttributes(this.userAttributes);
		} else {
			throw new IllegalStateException("This step is not valid in current environment: " + currentEnvironment);	
		}
	}
	
	@Given("^an active user with xbo service account$")
	public void anActiveUserWithXboServiceAccount() throws Exception {
		String currentEnvironment = citfTestInitializer.getCurrentEnvironment();
		if(currentEnvironment.equalsIgnoreCase(ICimaCommonConstants.ENVIRONMENT_STAGE) || currentEnvironment.equalsIgnoreCase(ICimaCommonConstants.ENVIRONMENT_PROD)) {
			
			Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
					UserDataProvider.Filter.SERVICE_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

			UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
			if (userObjects!=null) {
				this.account = userObjects.getAccount();
				this.userAttributes = userObjects.getUserAttr();
			}
			
			commonSteps.setAccount(this.account);
			commonSteps.setUserAttributes(this.userAttributes);
		} else {
			throw new IllegalStateException("This step is not valid in current environment: " + currentEnvironment);	
		}
	}

	@Given("^an active homePhone$")
	public void anActiveHomePhone() throws Exception {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
														UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null && userObjects.getAccount()!=null) {
			this.homePhone = userObjects.getAccount().getPhoneNumber();
		}
	}

	@Given("^an active user with the same home phone number$")
	public void anActiveUserWithSameHomePhoneNumber() throws Exception {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
														UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null && userObjects.getAccount()!=null) {
			this.account = userObjects.getAccount();
			try {
				ldap.updateAttributeOfAuthorizationObject(account.getAccountId(),
						LdapAttribute.AUTHORIZATION_HOME_PHONE.getValue(), this.homePhone);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to update the home phone of the accountId: " + account.getAccountId(), e);
			}
		}
	}

	@Given("^an active user with secret questions$")
	public void anActiveUserWithSecretQuestions() throws Exception {
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
				UserDataProvider.Filter.SECRET_QUESTION, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
		}
	}

	@When("^the application sends the request to validate unavailability of the userId$")
	public void requestForAPIFireForValidatingUnavailUid() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();
		this.userAttributes = commonSteps.getUserAttributes();
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.userAttributes == null || this.userAttributes.getUserId() == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [userAttributes={}, accessTokenResponse={}] ", this.userAttributes,
					this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with userAttributes: " + this.userAttributes
					+ " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "uid/" + this.userAttributes.getUserId();
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate unavailability of the user id with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the userId userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate availability of the userId$")
	public void requestForAPIFireForValidatingAvailUid() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (commonSteps.getUnregisteredUserId() == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [unregisteredUserID={}, accessTokenResponse={}] ",
					this.commonSteps.getUnregisteredUserId(), this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with unregisteredUserID: "
					+ this.commonSteps.getUnregisteredUserId() + " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "uid/" + commonSteps.getUnregisteredUserId();
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate availablity of user id with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate availablity of user id with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate unavailability of the alternateEmail$")
	public void requestForAPIFireForValidatingUnavailEmail() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.userAttributes == null || this.userAttributes.getAlterEmail() == null
				|| this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [userAttributes={}, accessTokenResponse={}] ", this.userAttributes,
					this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with userAttributes: " + this.userAttributes
					+ " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "alternateEmail/" + this.userAttributes.getAlterEmail();
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate unavailability of the alternateEmail with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the alternateEmail with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate availability of the alternateEmail$")
	public void requestForAPIFireForValidatingAvailEmail() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.unregisteredAlterEmail == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [unregisteredAlterEmail={}, accessTokenResponse={}] ",
					this.unregisteredAlterEmail, this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with unregisteredAlterEmail: "
					+ this.unregisteredAlterEmail + " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "alternateEmail/" + this.unregisteredAlterEmail;
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response  for a request to validate availability of the alternateEmail with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the alternateEmail with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate unavailability of the invalid alternateEmail$")
	public void requestForAPIFireForValidatingUnavailInvalidEmail() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.unregisteredAlterEmail == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [unregisteredAlterEmail={}, accessTokenResponse={}] ",
					this.unregisteredAlterEmail, this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with unregisteredAlterEmail: "
					+ this.unregisteredAlterEmail + " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "alternateEmail/" + this.unregisteredAlterEmail;
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate unavailability of the invalid alternateEmail with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the invalid alternateEmail with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate unavailability of the mobileNumber$")
	public void requestForAPIFireForValidatingUnavailMobile() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.account == null || this.account.getPhoneNumber() == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [account={}, accessTokenResponse={}] ", this.account,
					this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with account: " + this.account
					+ " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "mobilePhoneNumber/" + this.account.getPhoneNumber();
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate unavailability of the mobileNumber with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the mobileNumber with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate availability of the mobileNumber$")
	public void requestForAPIFireForValidatingAvailMobile() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.unregisteredMobile == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [unregisteredMobile={}, accessTokenResponse={}] ",
					this.unregisteredMobile, this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with unregisteredMobile: " + this.unregisteredMobile
					+ " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "mobilePhoneNumber/" + this.unregisteredMobile;
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate availability of the mobileNumber with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate availability of the mobileNumber with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate unavailability of the invalid mobileNumber$")
	public void requestForAPIFireForValidatingUnavailInvalidMobile() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.unregisteredMobile == null || this.accessTokenResponse == null
				|| this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [unregisteredMobile={}, accessTokenResponse={}] ",
					this.unregisteredMobile, this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with unregisteredMobile: " + this.unregisteredMobile
					+ " accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "mobilePhoneNumber/" + this.unregisteredMobile;
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate unavailability of the invalid mobileNumber  with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate unavailability of the invalid mobileNumber with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate availability of secret questions$")
	public void theAppSendsTheRequestToValidateAvailOfSecretQuestions() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns [accessTokenResponse={}] ", this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "secretquestions";
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate availability of secret questions with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate availability of secret questions with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate billing accounts associated with homePhone$")
	public void theAppSendsTheRequestToValidateBillingAccountsAssociatedWithHomePhone() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null
				|| this.homePhone == null) {
			logger.error("Null value returns [accessTokenResponse={}, homePhone={} ] ", this.accessTokenResponse,
					this.homePhone);
			throw new IllegalStateException("Null value returns with accessTokenResponse: " + this.accessTokenResponse
					+ " and homePhone: " + this.homePhone);
		}
		userCredUrl = this.idmBaseUrl + "accounts?homePhone=" + this.homePhone;
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate billing accounts associated with homePhone with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate billing accounts associated with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@When("^the application sends the request to validate billing accounts associated with a user via roles$")
	public void theAppSendsTheRequestToValidateBillingAccountsAssociatedWithAUserViaRoles() {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("Null value returns accessTokenResponse={} ", this.accessTokenResponse);
			throw new IllegalStateException("Null value returns with accessTokenResponse: " + this.accessTokenResponse);
		}
		userCredUrl = this.idmBaseUrl + "accounts?";
		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
		} catch (Exception e) {
			logger.error("Unable to fetch the user API response for a request to validate billing accounts associated with a user via roles [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to fetch the user API response for a request to validate billing accounts associated with a user via roles with userCredUrl: "
					+ this.userCredUrl + " and headerMap: " + this.headerMap);
		}
		commonSteps.setUserAPIResponse(userAPIResponse);
	}

	@Then("^response validates unavailability of the userId$")
	public void userUidUnavailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the userId: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the userId: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getUid() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [Uid={}, Availability={}] ", this.parsedResponse.getUid(),
						this.parsedResponse.getAvailable());
				throw new IllegalStateException("Null value returns with Uid: " + this.parsedResponse.getUid()
						+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if (parsedResponse.getAvailable().contains("false")) {
				logger.info("Unavailability of registered userId is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Unavailability of registered userId is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException("Unavailability of registered userId is not validated with userId= "
						+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate unavailability of registered userId with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate unavailability of registered userId with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates availability of the userId$")
	public void userUidAvailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for availability of the userId: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for availability of the userId: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getUid() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [Uid={}, Availability={}] ", this.parsedResponse.getUid(),
						this.parsedResponse.getAvailable());
				throw new IllegalStateException("Null value returns with Uid: " + this.parsedResponse.getUid()
						+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if (parsedResponse.getAvailable().contains("true")) {
				logger.info("Availability of unregistered userId is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Availability of unregistered userId is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException("Availability of unregistered userId is not validated with userId= "
						+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate availability of unregistered userId with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate availability of unregistered userId with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates unavailability of the alternateEmail$")
	public void userAlternateEmailUnavailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the alternateEmail: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the alternateEmail ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getAlternateEmail() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [AlternateEmail={}, Availability={}] ",
						this.parsedResponse.getAlternateEmail(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with AlternateEmail: " + this.parsedResponse.getAlternateEmail()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if ((parsedResponse.getAvailable().equals("false"))
					&& (parsedResponse.getErrorCode() == null || parsedResponse.getErrorCode().equals(""))) {
				logger.info("Unavailability of registered alternateEmail is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Unavailability of registered alternateEmail is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException(
						"Unavailability of registered alternateEmail is not validated with userId= "
								+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate unavailability of registered alternateEmail with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate unavailability of registered alternateEmail with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates availability of the alternateEmail$")
	public void userAlternateEmailAvailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for availability of the alternateEmail: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for availability of the alternateEmail: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getAlternateEmail() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [AlternateEmail={}, Availability={}] ",
						this.parsedResponse.getAlternateEmail(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with AlternateEmail: " + this.parsedResponse.getAlternateEmail()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if ((parsedResponse.getAvailable().contains("true"))) {
				logger.info("Availability of unregistered alternateEmail is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Availability of unregistered alternateEmail is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException(
						"Availability of unregistered alternateEmail is not validated with userId= "
								+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate availability of unregistered alternateEmail with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate availability of unregistered alternateEmail with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates unavailability of the invalid alternateEmail$")
	public void userInvalidAlternateEmailUnavailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the invalid alternateEmail: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the invalid alternateEmail: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getAlternateEmail() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [AlternateEmail={}, Availability={}] ",
						this.parsedResponse.getAlternateEmail(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with AlternateEmail: " + this.parsedResponse.getAlternateEmail()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if (parsedResponse.getAvailable().contains("false") && parsedResponse.getErrorCode().contains("1000")
					&& parsedResponse.getErrorDescription().contains("Invalid email address")) {
				logger.info("Unavailability of invalid alternateEmail is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Unavailability of invalid alternateEmail is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException(
						"Unavailability of invalid alternateEmail is not validated with userId= "
								+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate unavailability of invalid alternateEmail with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate unavailability of invalid alternateEmail with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates unavailability of the mobileNumber$")
	public void userMobileNumberUnavailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the mobileNumber : ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the mobileNumber: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getMobilePhoneNumber() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [Phone Number={}, Availability={}] ",
						this.parsedResponse.getMobilePhoneNumber(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with MobileNumber: " + this.parsedResponse.getMobilePhoneNumber()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if ((parsedResponse.getAvailable().equals("false"))
					&& (parsedResponse.getErrorCode() == null || parsedResponse.getErrorCode().equals(""))) {
				logger.info("Unavailability of registered mobileNumber is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Unavailability of registered mobileNumber is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException(
						"Unavailability of registered mobileNumber is not validated with userId= "
								+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate unavailability of registered mobileNumber with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate unavailability of registered mobileNumber with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates availability of the mobileNumber$")
	public void userMobileNumberAvailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the mobileNumber: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the mobileNumber: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getMobilePhoneNumber() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [Phone Number={}, Availability={}] ",
						this.parsedResponse.getMobilePhoneNumber(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with MobileNumber: " + this.parsedResponse.getMobilePhoneNumber()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if (parsedResponse.getAvailable().contains("true")) {
				logger.info("Availability of unregistered mobileNumber is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Availability of unregistered mobileNumber is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException(
						"Availability of unregistered mobileNumber is not validated with userId= "
								+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate availability of unregistered mobileNumber with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate availability of unregistered mobileNumber with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates unavailability of the invalid mobileNumber$")
	public void userInvalidMobileNumberUnavailabilityValidated() {
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMUserCredentialValidationResponseJSON.class);
			this.parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for unavailability of the invalid mobileNumber: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for unavailability of the invalid mobileNumber: ", e);
		}

		if (this.parsedResponse != null) {
			if (parsedResponse.getMobilePhoneNumber() == null || parsedResponse.getAvailable() == null) {
				logger.error("Null value returns [Phone Number={}, Availability={}] ",
						this.parsedResponse.getMobilePhoneNumber(), this.parsedResponse.getAvailable());
				throw new IllegalStateException(
						"Null value returns with MobileNumber: " + this.parsedResponse.getMobilePhoneNumber()
								+ " and Avalability: " + this.parsedResponse.getAvailable());
			}
			if (parsedResponse.getAvailable().contains("false") && parsedResponse.getErrorCode().contains("2000")
					&& parsedResponse.getErrorDescription().contains("Invalid mobile phone number")) {
				logger.info("Unavailability of invalid mobileNumber is validated with userId= {}",
						this.parsedResponse.getUid());
			} else {
				logger.error("Unavailability of invalid mobileNumber is not validated with userId= {}",
						this.parsedResponse.getUid());
				throw new IllegalStateException("Unavailability of invalid mobileNumber is not validated with userId= "
						+ this.parsedResponse.getUid());
			}
		} else {
			logger.error("Failed to validate unavailability of invalid mobileNumber with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate unavailability of invalid mobileNumber with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@SuppressWarnings("rawtypes")
	@Then("^response validates availability of secret questions$")
	public void responseValidatesAvailabilityOfSecretQuestions() {
		Map sequrityQuestions = parseIDMSecurityQuestionResponse(userAPIResponse);
		Iterator entries = sequrityQuestions.entrySet().iterator();
		if (!sequrityQuestions.isEmpty()) {
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				if ((key.contains("FAVORITE_BEVERAGE") || key.contains("FIRST_CAR") || key.contains("FIRST_JOB")
						|| key.contains("NIECE_NEPHEW") || key.contains("HONEYMOON") || key.contains("SPORTS_TEAM")
						|| key.contains("BEST_FRIEND") || key.contains("FAVORITE_TEACHER") || key.contains("PET_NAME")
						|| key.contains("FAVORITE_MOVIE")) && (value == null || value.equals(""))) {
					logger.error("Test case failed due to no value presence against a Security question={} ", key);
					throw new IllegalStateException(
							"Test case failed due to no value presence against a Security question: " + key);
				} else {
					logger.info(
							"Security Question validation Test case executed successfully with [question={}, answer={}] ",
							key, value);
				}
			}
		} else {
			logger.error(
					"Test case for Security question validation failed as no security question set returned with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Test case for Security question validation failed as no security question set returned with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates billing accounts associated with homePhone$")
	public void responseValidatesBillingAccountsAssociatedWithHomePhone() {
		IDMGetAccountsResponseJSON parsedAccountResponse = null;
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMGetAccountsResponseJSON.class);
			parsedAccountResponse = (IDMGetAccountsResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for billing accounts associated with homePhone: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for billing accounts associated with homePhone: ", e);
		}
		if (parsedAccountResponse != null) {
			if (parsedAccountResponse.getHomePhone() == null || parsedAccountResponse.getAuthorizations() == null) {
				logger.error("Null value returns [Home Phone={}, Authorizations={}] ",
						parsedAccountResponse.getHomePhone(), parsedAccountResponse.getAuthorizations());
				throw new IllegalStateException(
						"Null value returns with Home Phone: " + parsedAccountResponse.getHomePhone()
								+ " and Authorizations: " + parsedAccountResponse.getAuthorizations());
			}
			authorizations = parsedAccountResponse.getAuthorizations();
			for (int i = 0; i < authorizations.size(); i++) {
				if (authorizations.get(i).getBillingAccountId().length() != 13) {
					logger.error("Billing accounts are not validated with homePhone= {}",
							parsedAccountResponse.getHomePhone());
					throw new IllegalStateException("Billing accounts are not validated with homePhone= "
							+ parsedAccountResponse.getHomePhone());
				}
			}
			logger.info("Billing accounts are successfully validated associated with homePhone= {}",
					parsedAccountResponse.getHomePhone());
		} else {
			logger.error("Failed to validate billing accounts associated with homePhone with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate billing accounts associated with homePhone with userAPIResponse: "
							+ this.userAPIResponse);
		}
	}

	@Then("^response validates billing accounts associated with a user via roles$")
	public void responseValidatesBillingAccountsAssociatedWithAUserViaRoles() {
		IDMGetAccountsResponseJSON parsedAccountResponse = null;
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMGetAccountsResponseJSON.class);
			parsedAccountResponse = (IDMGetAccountsResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for billing accounts associated with a user via roles: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for billing accounts associated with a user via roles: ", e);
		}
		if (parsedAccountResponse != null) {
			if (parsedAccountResponse.getUsername() == null || parsedAccountResponse.getAuthorizationRoles() == null) {
				logger.error("Null value returns [Username={}, Authorization Roles={}] ",
						parsedAccountResponse.getUsername(), parsedAccountResponse.getAuthorizationRoles());
				throw new IllegalStateException(
						"Null value returns with Username: " + parsedAccountResponse.getUsername()
								+ " and Authorization Roles: " + parsedAccountResponse.getAuthorizationRoles());
			}
			authorizationRoles = parsedAccountResponse.getAuthorizationRoles();
			for (int i = 0; i < authorizationRoles.size(); i++) {
				if (authorizationRoles.get(i).getAuthorization().getBillingAccountId().length() != 13) {
					logger.error("Billing accounts are not validated with a user via username= {}",
							parsedAccountResponse.getUsername());
					throw new IllegalStateException("Billing accounts are not validated with a user via username= "
							+ parsedAccountResponse.getUsername());
				}
			}
			logger.info("Billing accounts are successfully validated with a user via username= {}",
					parsedAccountResponse.getUsername());
		} else {
			logger.error(
					"Failed to validate billing accounts associated with a user via roles with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate billing accounts associated with a user via roles with userAPIResponse: "
					+ this.userAPIResponse);
		}
	}
	
	@Then("^response validates billing accounts and xbo service accounts associated with a user via roles$")
	public void responseValidatesBillingAccountXboServiceAccountAssociatedWithAUserViaRoles() {
		IDMGetAccountsResponseJSON parsedAccountResponse = null;
		try {
			this.paresdObject = JSONParserHelper.parseJSON(userAPIResponse,
					IDMGetAccountsResponseJSON.class);
			parsedAccountResponse = (IDMGetAccountsResponseJSON) paresdObject;
		} catch (Exception e) {
			logger.error("Exception occured while parsing the JSON format to object for billing accounts associated with a user via roles: ", e);
			throw new IllegalStateException("Exception occured while parsing the JSON format to object for billing accounts associated with a user via roles: ", e);
		}
		if (parsedAccountResponse != null) {
			if (parsedAccountResponse.getUsername() == null || parsedAccountResponse.getAuthorizationRoles() == null) {
				logger.error("Null value returns [Username={}, Authorization Roles={}] ",
						parsedAccountResponse.getUsername(), parsedAccountResponse.getAuthorizationRoles());
				throw new IllegalStateException(
						"Null value returns with Username: " + parsedAccountResponse.getUsername()
								+ " and Authorization Roles: " + parsedAccountResponse.getAuthorizationRoles());
			}
			authorizationRoles = parsedAccountResponse.getAuthorizationRoles();
			for (int i = 0; i < authorizationRoles.size(); i++) {
				if (authorizationRoles.get(i).getAuthorization().getBillingAccountId().length() != 13) {
					logger.error("Billing accounts are not validated with a user via username= {}",
							parsedAccountResponse.getUsername());
					throw new IllegalStateException("Billing accounts are not validated with a user via username= "
							+ parsedAccountResponse.getUsername());
				}
				if(authorizationRoles.get(i).getXboServiceAccountId().length() != 14) {
					logger.error("XBO service accounts are not validated with a user via username= {}",
							parsedAccountResponse.getUsername());
					throw new IllegalStateException("XBO service accounts are not validated with a user via username= "
							+ parsedAccountResponse.getUsername());
				}
			}
			logger.info("Billing accounts and xbo service accounts are successfully validated with a user via username= {}",
					parsedAccountResponse.getUsername());
		} else {
			logger.error(
					"Failed to validate billing accounts and xbo service accounts associated with a user via roles with userAPIResponse: {} ",
					this.userAPIResponse);
			throw new IllegalStateException(
					"Failed to validate billing accounts and xbo service accounts associated with a user via roles with userAPIResponse: "
					+ this.userAPIResponse);
		}
	}

	public Map<String, String> parseIDMSecurityQuestionResponse(String response) {
		String response1 = response;
		Map<String,String> sequrityQuestions = new HashMap<String,String>();
		response1 = response1.replace("[", "");
		response1 = response1.replace("]", "");
		List<String> aList = new ArrayList<String>(Arrays.asList(response1.split(",")));
		for (int i = 0; i < aList.size(); i++) {
			List<String> bList = new ArrayList<String>(Arrays.asList(aList.get(i).toString().split(",")));
			for (int j = 0; j < bList.size(); j++) {
				String[] myList = bList.get(j).toString().split(":");
				sequrityQuestions.put(myList[0], myList[1]);
			}
		}
		return sequrityQuestions;
	}

	@Given("^a fresh user with alternative email and mobile$")
	public void getFreshUserwithEmailandMobile() throws Exception {
		
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if(fAccount!=null){
			account = fAccount;
		}
		
		Map<DataProviderToCreateUser.Filter, String> frUsrFilters = ImmutableMap.of(DataProviderToCreateUser.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		FreshUsers freshUser = freshUserProvider.getFreshUser(frUsrFilters);
		if(freshUser!=null){
			freshUserAttributes = freshUser;
		}
	}
	

	@When("^the application sends the request to register a fresh user$")
	public void applicationSendRequestRegisterFreshUser() throws Exception {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;		
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.freshUserAttributes == null || this.account==null ) {
			logger.error("Required user attributes not found", this.freshUserAttributes);
			throw new IllegalStateException("Null value returns with userAttributes: " + this.freshUserAttributes);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}
		
		
		this.userId = userNameGenerator.next();
		this.password = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);

		userCredUrl = this.idmBaseUrl + "registerUser";
		
		 paramMap.put("firstName", this.account.getFirstName());
		 paramMap.put("lastName",  this.account.getLastName()); 
		 paramMap.put("displayName", this.userId.split(ICommonConstants.UNDER_SCORE)[0] +" "+ this.userId.split(ICommonConstants.UNDER_SCORE)[1]);
		 paramMap.put("phone",  this.account.getPhoneNumber()); 
		 paramMap.put("mobilePhone", "");
		 paramMap.put("uid", this.userId);
		 paramMap.put("password", (String) this.password);
		 paramMap.put("secretQuestion", this.secretQuestion);
		 paramMap.put("secretAnswer", this.secretAnswer);
		 paramMap.put("account", this.account.getAccountId());
		 paramMap.put("linesOfBusiness", getLineofBusiness());
		 paramMap.put("createEmailFlag", "true");
		 paramMap.put("isPrimary","true" );
		 paramMap.put("parentGUID", "");


		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);		
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap, RestHandler.WriteRequestMethod.POST);
		
		} catch (Exception e) {
			logger.error(
					"Exception occured requesting to regsiter user with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured requesting to regsiter user");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	
	}

	@Then("^response validates successful registration of fresh user$")
	public void responseValidatesSuccessfulRegistrationFreshUser() throws Exception {
		
		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				
				logger.warn("Exception occured while parsing the JSON format to object of register user ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object: " + e);
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				if (SUCCESS_MSG.equalsIgnoreCase(parsedResponse.getStatus()) && parsedResponse.getCustGuid()!=null && !parsedResponse.getCustGuid().isEmpty()) {
					logger.info("user is registered successfully and the custGuid" + parsedResponse.getCustGuid());
				} else {
					throw new IllegalStateException("user is not registered successfully");
				}
			}

			else {
				logger.error("JSON parser is returning null while parsing API response of register user");
				throw new IllegalStateException(
						"JSON parser is returning null while parsing API response of register user" + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during validation of register user API response", e);
			throw new IllegalStateException("Exception occured during validation of register user API response", e);
		}
	 
	 
	}

	@When("^application deletes registerd user from LDAP$")
	public void applicationDeletesRegisterdUserFromLDAP() throws Exception {

		logger.info("user started user deletion process");
		try{
			List<String> users = new ArrayList<String>(); 
			users.add(this.userId); 
			ldap.purgeUsers(users);
		}
		catch(Exception e)
		{
			logger.error("Exception occured while deleting the created user from LDAP: " +this.userId );
			throw new IllegalStateException("Exception occured while deleting the created user from LDAP :" + this.userId + " with exception" + e);			 
		}
	}

	@Then("^register user gets deleted successfully$")
	public void registerUserGetsDeletedSuccessfully() throws Exception {

			String user = this.userId;
			 if(ldap.isUserDeleted(user)){
				 logger.info("User is deleted successfully from LDAP");		 
			}
			 else {
				 if (this.hsdStatus!=null) {
					 logger.info("User is not deleted successfully from LDAP");	
				 }
				 throw new IllegalStateException("User is not deleted successfully from LDAP");
			 }
	 
	}
	

	@Given("^an active user with alternate email and phone$")
	public void anActiveUserWithAlternateEmailAndPhone() throws Exception {
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE,
													UserDataProvider.Filter.ALTERNATE_MAIL, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL,
													UserDataProvider.Filter.PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
		}
	}
	
	
	
	@When("^the application sends the request to register user with invalid account details$")
	public void applicationSendsRequestRegisterUserWithInvalidAccount() throws Exception {
		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;		
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.account==null ) {
			logger.error("Required user attributes not found", this.userAttributes);
			throw new IllegalStateException("Null value returns with userAttributes: " + this.userAttributes);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email");
		}
		
		
		this.userId = userNameGenerator.next();
		this.password = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);

		userCredUrl = this.idmBaseUrl + "registerUser";
		
		 paramMap.put("firstName", this.account.getFirstName());
		 paramMap.put("lastName",  this.account.getLastName()); 
		 paramMap.put("displayName", this.userId.split(ICommonConstants.UNDER_SCORE)[0] +" "+ this.userId.split(ICommonConstants.UNDER_SCORE)[1]);
		 paramMap.put("phone",  this.account.getPhoneNumber()); 
		 paramMap.put("mobilePhone", "");
		 paramMap.put("uid", this.userId);
		 paramMap.put("password", (String) this.password);
		 paramMap.put("secretQuestion", this.secretQuestion);
		 paramMap.put("secretAnswer", this.secretAnswer);
		 paramMap.put("account", this.account.getAccountId());
		 paramMap.put("linesOfBusiness", getLineofBusiness());
		 paramMap.put("createEmailFlag", "true");
		 paramMap.put("isPrimary","true" );
		 paramMap.put("parentGUID", "");


		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);		
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap, RestHandler.WriteRequestMethod.POST);
		
		} catch (Exception e) {
			logger.error(
					"Exception occured requesting to regsiter user with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured requesting to regsiter user");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	   
	}

	
	@Then("^response validates unsuccessful registration of user with existing account$")
	public void responseValidatesUnsuccessfulRegistrationOfFreshUser() throws Exception {
		
		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.error(LOG_MESSAGE_USER_API_UNAVAILABLE, this.errorDescription);
				throw new IllegalStateException(
						LOG_MESSAGE_USER_API_UNAVAILABLE2 + this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object of register user ", e);
				throw new IllegalStateException("Exception occured while parsing the JSON format to object of register user: " + e);
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
				
				if (UNSUCCESSFUL_USER_REGISTERATION_STATUS.equalsIgnoreCase(parsedResponse.getStatus()) || GENERIC_FAILURE_MSG.equalsIgnoreCase(parsedResponse.getStatus())) {
					logger.info("user registration is unsuccessful with invalid account details");
				} else {
					throw new IllegalStateException("User registration is successful with invalid account details.Userid is :"+ this.userId);
				}
			}

			else {
				logger.error("JSON parser is returning null while parsing API response of register user for invalid account");
				throw new IllegalStateException(
						"JSON parser is returning null while parsing API response of register user for invalid account detail" + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during validation of register user API response with invalid account detail", e);
			throw new IllegalStateException("Exception occured during validation of register user API response with invalid account detail", e);
		}
	 
	 
	}
	
	
	@When("^care agent sends the request to read reset code for the user who does not have Reset Code$")
	public void requestToCReadResetCodeForUserWithoutResetCode() throws Exception {

		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.unregisteredUserID=commonSteps.getUnregisteredUserId();
		
		if (this.unregisteredUserID == null) {
			throw new IllegalStateException("Unable to fetch user");
		}

		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.unregisteredUserID;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for deleting reset code for user who doesnot have one with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	}
	
	@Then("^Response contains reset code not found for the user$")
	public void validateResponseToReadResetCodeForUserWhoDoesntHaveResetCode() {

		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.warn(this.errorDescription);
				throw new IllegalStateException(this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object for invalid reset code: ", e);
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;

				//No code found for the specified user.
					if (this.resetCodeNotFound.equalsIgnoreCase(parsedResponse.getErrorCode()) && parsedResponse.getErrorDescription().contains(RESETCODE_NOT_FOUND_ERROR_DESC) ) {
						logger.info("reset code not found for user");
					} else {
						logger.error("reset code found for the user who doesnot have reset code");
						throw new IllegalStateException("reset code found for the user who doesnot have reset code");
					}
				} 
		
			else {
				logger.error("Unable to parse User API Response for user having no Reset code");
				throw new IllegalStateException(
						"Unable to parse User API Response for user having no Reset code the API response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during the validation of read reset code for invalid user", e);
			throw new IllegalStateException("Exception occured during the validation of read reset code for invalid user", e);
		}
	}


	@When("^care agent sends the request to create a reset code for the unregistered user$")
	public void careAgentSendsRequestToCreateResetCodeForTheUnregisteredUser() {
		
		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
			
		if (unregisteredUserIdNew == null) {
			throw new IllegalStateException("Unable to fetch user attributes");
		}
		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.unregisteredUserIdNew;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.POST);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to create reset code for unregistered user with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	 
	}
	
	
	@When("^Wait to get Reset Code expired$")
	public void waitToGetResetCodeExpired() {
		try {
			timeTOGetResetCodeGetExpired = configDataProvider.getResetCodeExpirationTime();
			TimeUnit.MILLISECONDS.sleep(timeTOGetResetCodeGetExpired);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to wait to get reset code expired", e);
		}
	}

	
	@When("^care agent sends the request to read expired reset code$")
	public void careAgentSendsRequestReadExpiredResetCode() {
		
		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
				
		if (this.unregisteredUserIdNew == null) {
			throw new IllegalStateException("Unable to fetch user");
		}

		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.unregisteredUserIdNew;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.GET);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for request to read expired code with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	}
		
	
	@Then("^Response contains message as reset code is expired for the user$")
	public void responseContainsMessageResetCodeIsExpiredForTheUser() {
		
		try {
			if (this.userAPIResponse.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)) {
				this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				logger.warn(this.errorDescription);
				throw new IllegalStateException(this.errorDescription);
			}
			try {
				paresdObject = JSONParserHelper.parseJSON(this.userAPIResponse,
						IDMUserCredentialValidationResponseJSON.class);
			} catch (Exception e) {
				logger.warn("Exception occured while parsing the JSON format to object for expired reset code: ", e);
			}

			if (paresdObject != null) {
				parsedResponse = (IDMUserCredentialValidationResponseJSON) paresdObject;
                 
				
					if (UserAPISteps.RESETCODE_EXPIRED.equalsIgnoreCase(parsedResponse.getErrorCode())) {
						logger.info("reset code is expired for the user");
					} else {
						logger.error("reset code is not expired for the user");
						throw new IllegalStateException("reset code is not expired for the user");
					}
				} 
		
			else {
				logger.error("Unable to parse User API Response for user having no Reset code");
				throw new IllegalStateException(
						"Unable to parse User API Response for user having no Reset code the API response: " + this.userAPIResponse);
			}
		}

		catch (Exception e) {
			logger.warn("Exception occured during the validation of read reset code for invalid user", e);
			throw new IllegalStateException("Exception occured during the validation of read reset code for invalid user", e);
		}
	  
	}
	
	
	@Given("^an unregistered new user$")
	public void unregisteredNewUser() throws Exception {
		this.unregisteredUserIdNew = userNameGenerator.next();
		 
	}
	
	
	@When("^care agent sends the request to delete an invalid reset code$")
	public void careAgentRequestToDeleteAnInvalidResetcode()  {
		
		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
				
		if (this.unregisteredUserIdNew == null) {
			throw new IllegalStateException("Unable to fetch user attributes from database");
		}
		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.unregisteredUserIdNew + "/"
				+ INVALID_RESETCODE;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.DELETE);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for a request to delete a invalid code with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	   
	}

	
	@When("^care agent sends the request to Delete expired reset code$")
	public void careagentSendsRequestToDeleteExpiredResetcode()  {
		
		headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		
		if (this.unregisteredUserIdNew == null) {
			throw new IllegalStateException("Unable to fetch user attributes from database");
		}
		userCredUrl = commonSteps.getIdmBaseUrl() + RESETCODE_STRING + this.unregisteredUserIdNew + "/"
				+ this.resetCode;

		try {
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, null, headerMap,
					RestHandler.WriteRequestMethod.DELETE);
			commonSteps.setUserAPIResponse(userAPIResponse);
		} catch (Exception e) {
			logger.error("Unable to get the user API response with [userCredUrl={}, headerMap={}] ", this.userCredUrl,
					this.headerMap, e);
			throw new IllegalStateException("Unable to get the user API response for rquest to delete expired reset code with userCredUrl: " + this.userCredUrl
					+ " and headerMap: " + this.headerMap);
		}
	   
	}
	
	
	
	@When("^application sends the request to send verification email for the user without First Name$")
	public void applicationSendsRequestToSendverificationEmailWithoutFirstName()  {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email withoutfirst name", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without first name" + this.userAttributes
					+ " userId to send verification email without billing account ID: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email without first name");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("lastName", this.account.getLastName());
		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email without first name");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	@When("^application sends the request to send verification email for the user without Last Name$")
	public void applicationSendsRequestToSendverificationEmailWithoutLastName()  {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without billing account id", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without last name" + this.userAttributes
					+ " userId to send verification email without billing account ID: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email without last name");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email without last name");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	@When("^application sends the request to send verification email for the user without Username$")
	public void applicationSendsRequestToSendverificationEmailWithoutusername()  {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without username", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without username" + this.userAttributes
					+ " userId to send verification email without username: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email without username");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email without username");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	@When("^application sends the request to send verification email for the user without email$")
	public void applicationSendsRequestToSendverificationEmailWithoutEmail()  {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without email", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without Email" + this.userAttributes
					+ " userId to send verification email without email: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email without email");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getFirstName());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email without email");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	@When("^application sends the request to send verification email for the user without Flow URL$")
	public void applicationSendsRequestToSendverificationEmailWithoutFlowURL() {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without Flow URL", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email without username" + this.userAttributes
					+ " userId to send verification email without Flow URL: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email without Flow URL");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getFirstName());
		if (this.userAttributes.getUserId().contains("@")) {
			String[] userIdStringArray = this.userAttributes.getUserId().split("@");
			paramMap.put("userName", userIdStringArray[0]);
		} else {
			paramMap.put("userName", this.userAttributes.getUserId());
		}
		paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
		paramMap.put("prefferedEmail", "true");
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email without Flow URL");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	@When("^application sends the request to send verification email for the user with invalid email$")
	public void applicationSendsRequestToSendverificationEmailWithInvalidEmail() {

		this.idmBaseUrl = commonSteps.getIdmBaseUrl();
		this.headerMap = commonSteps.getOauthCommonSteps().getHeaderMap();;
		this.accessTokenResponse = commonSteps.getOauthCommonSteps().getAccessTokenResponse();
		
		if (this.userAttributes == null || this.userAttributes.getUserId() == null) {
			logger.error("Required user attributes not found to send verfication email without Flow URL", this.userAttributes,
					this.userAttributes != null ? this.userAttributes.getUserId() : null);
			throw new IllegalStateException("Required user attributes not found to send verfication email with invalid email" + this.userAttributes
					+ " userId to send verification email with invalid email: " + this.userAttributes != null ? this.userAttributes.getUserId() : null);
		}

		if (this.accessTokenResponse == null || this.accessTokenResponse.getAccessToken() == null) {
			logger.error("access token not found to send the request", this.accessTokenResponse,
					this.accessTokenResponse != null ? this.accessTokenResponse.getAccessToken() : null);
			throw new IllegalStateException("access Token not found to send the request to send verifictaion email with invalid email");
		}

		userCredUrl = this.idmBaseUrl + "sendVerificationEmail";

		paramMap.put("billingAccountId", this.account.getAccountId());
		paramMap.put("customerGuid", this.userAttributes.getGuid());
		paramMap.put("firstName", this.account.getFirstName());
		paramMap.put("lastName", this.account.getFirstName());
		paramMap.put("contactEmail", invalidEmail);
		paramMap.put("prefferedEmail", "true");
		paramMap.put("flowUrl", UserAPISteps.FLOW_URL);
		paramMap.put("validationUrlTTLHours", "72");

		try {
			RestHandler handler = restHandler;
			handler.setRequestType(MediaType.APPLICATION_JSON);
			userAPIResponse = restHandler.executeWriteRequest(userCredUrl, paramMap, headerMap,
					RestHandler.WriteRequestMethod.POST);
		} catch (Exception e) {
			logger.error(
					"Exception occured while requesting to send verification email with [paramMap={},userCredUrl={}, headerMap={}] ",
					this.paramMap, this.userCredUrl, this.headerMap, e);
			throw new IllegalStateException("Exception occured while requesting to send verification email with invalid email");
		}

		commonSteps.setUserAPIResponse(userAPIResponse);
	}
	
	
	private String getLineofBusiness() throws Exception {
		
		LDAPAuthorization auth = ldap.getAuthorizationData(ldap.new LDAPInstruction(LDAPInterface.SearchType.EQUAL_TO, 
				LDAPInterface.LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID, this.account.getAccountId()));
		
		if (auth.getDigitalVoiceStatus() != null && (auth.getDigitalVoiceStatus().equalsIgnoreCase("C") ||
													 auth.getDigitalVoiceStatus().equalsIgnoreCase("A"))) {
			this.cdvStatus = "CDV";
		}
		
		if (auth.getHsdStatus() != null && (auth.getHsdStatus().equalsIgnoreCase("C") ||
				 auth.getHsdStatus().equalsIgnoreCase("A"))) {
			this.hsdStatus = "HSI";
		}
		
		if (auth.getVidStatus() != null && (auth.getVidStatus().equalsIgnoreCase("C") ||
				 auth.getVidStatus().equalsIgnoreCase("A"))) {
			this.vidStatus = "VID";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("[\"");
		if (this.cdvStatus!=null)
		{
			sb.append(this.cdvStatus).append("\"");
		}
		if (this.hsdStatus!=null)
		{
			sb.append(",\"").append(this.hsdStatus).append("\"");
		}
		if (this.vidStatus!=null)
		{
			sb.append(",\"").append(this.vidStatus).append("\"");
		}
		
		sb.append("]");
		
		return sb.toString();
	}


	/**
	 * @return the resetCode
	 */
	public String getResetCode() {
		return this.resetCode;
	}

	/**
	 * @param resetCode
	 *            the resetCode to set
	 */
	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}
	
	private static final String LOG_MESSAGE_UNABLE_TO_DELETE_RESET_CODE = "Unable to delete reset code";
	private static final String LOG_MESSAGE_JSON_PARSER_IS_RETURNING_NULL = "JSON parser is returning null";
	private static final String LOG_MESSAGE_USER_API_UNAVAILABLE = "USER API Server Unavailable with error Description ={} ";
	private static final String LOG_MESSAGE_USER_API_UNAVAILABLE2 = "USER API Server Unavailable with error Description =";

}
