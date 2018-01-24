package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.RABICheckOptions;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cucumber.steps.OAuthCommonSteps;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * @author jxu004c
 *
 */
public class UserAPICommonSteps {
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private List<String> requestedScopes;
	private String clientId;
	private String clientSecret;
	private Map<String, Object> filter;
	private AccountCache.Account account;
	private UserAttributesCache.Attribute userAttributes;
	private String unregisteredUserId;
	private TokenResponse accessTokenResponse;
	private Map<String, String> headerMap;
	private String userAPIResponse;
	private String idmBaseUrl;
	private String scope;
	private WebDriver browser;
	private String authTokenUrl;
	private Object pageObject;
	
	@Autowired
	private OAuthCommonSteps oauthCommonSteps;
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private EndPoinUrlProvider urlProvider;

	@Before
	public void setup(Scenario scenario) throws Exception {
		this.idmBaseUrl = null;
		this.requestedScopes = null;
		this.clientId = null;
		this.clientSecret = null;
		this.filter = null;
		this.account = null;
		this.userAttributes = null;
		this.unregisteredUserId = null;
		this.accessTokenResponse = null;
		this.userAPIResponse = null;
		this.browser = null;
		this.pageObject = null;
		this.authTokenUrl = null;
		this.headerMap = new HashMap<String, String>();
	}
	
	@Given("^an unregistered user$")
	public void givenAnUnregisteredUser() {
		this.unregisteredUserId = ICimaCommonConstants.UNREGISTERED_USERID;
	}
	
	@Given("^an active user$")
	public void givenUserToValidateWithScopeCredential() throws Exception {
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICommonConstants.USER_STATUS_ACTIVE);
		UserDataProvider.UserObjects userObjects = userProvider.fetchUserDetails(filters, false, UserDataProvider.UserCategory.IDM, true);
		if (userObjects!=null) {
			this.account = userObjects.getAccount();
			this.userAttributes = userObjects.getUserAttr();
		}
	}
	
	@And("^the service endpoint$")
	public void theServiceEndpoint() {
		idmBaseUrl = urlProvider.getIdmUrl(EndPoinUrlProvider.IDMUrlPropKeys.IDMBASEURL.name());
	}
	
	@And("^the service v2 endpoint$")
	public void theServiceV2Endpoint() {
		idmBaseUrl = urlProvider.getIdmUrl(EndPoinUrlProvider.IDMUrlPropKeys.IDMBASEURL_V2.name());
	}
	
	@Then("^the application receives a successful response$")
	public void applicationReceivesASuccessfulResponse() {
		assertThat(
				"HTTP response should not return null value",
				this.userAPIResponse, notNullValue());		
		assertThat(
				"Expected valid HTTP response",
				this.userAPIResponse, is(not(ICommonConstants.IDM_API_RESPONSE_TYPE_UNAUTHORIZED)));
	}
	
	@Then("^the application receives an unauthorized access response$")
	public void applicationReceivesAnUnauthorizedAccessResponse() {
		assertThat(
				"HTTP response should not return null value",
				this.userAPIResponse, notNullValue());		
		assertThat(
				"Expected valid HTTP response",
				this.userAPIResponse, is(ICommonConstants.IDM_API_RESPONSE_TYPE_UNAUTHORIZED));
	}
	
	@Then("^care agent receives a successful response$")
	public void careAgentReceivesASuccessfulResponse() {
		assertThat(
				"HTTP response should not return null value",
				this.userAPIResponse, notNullValue());		
		assertThat(
				"Expected valid HTTP response",
				this.userAPIResponse, is(not(ICommonConstants.IDM_API_RESPONSE_TYPE_UNAUTHORIZED)));
	}
	
	@And("^user is redirected after sign in and get auth response url$")
	public void userGetsRedirectedAfterSignIn() {
		pageObject = this.oauthCommonSteps.getPageObject();
		browser = this.oauthCommonSteps.getBrowser();
		assertThat(pageObject, instanceOf(SignInToXfinity.class));
		
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		if(this.userAttributes != null && this.userAttributes.getUserId() != null && this.userAttributes.getPassword() != null) {
			pageObject = signInPage.signin(this.userAttributes.getUserId(), this.userAttributes.getPassword());
		} else {
			throw new IllegalStateException("Failed to sign in with userAttributes: " + this.userAttributes);	
		}
		if(pageObject instanceof RABICheckOptions) {
			RABICheckOptions rabiCheckOptions = (RABICheckOptions)pageObject;
			authTokenUrl = rabiCheckOptions.continueAskMeLaterToGetResponseURL();
		} else {
			authTokenUrl = this.browser.getCurrentUrl();
		} 
		oauthCommonSteps.setAuthTokenUrl(authTokenUrl);
		logger.debug("User redirected to the URl after sign in={}", authTokenUrl);
	}
	
	public List<String> getRequestedScopes() {
		return requestedScopes;
	}

	public void setRequestedScopes(List<String> requestedScopes) {
		this.requestedScopes = requestedScopes;
	}

	public String getclientId() {
		return clientId;
	}

	public void setclientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public AccountCache.Account getAccount() {
		return account;
	}

	public void setAccount(AccountCache.Account account) {
		this.account = account;
	}

	public UserAttributesCache.Attribute getUserAttributes() {
		return userAttributes;
	}

	public void setUserAttributes(UserAttributesCache.Attribute userAttributes) {
		this.userAttributes = userAttributes;
	}

	public TokenResponse getAccessTokenResponse() {
		return accessTokenResponse;
	}

	public void setAccessTokenResponse(TokenResponse accessTokenResponse) {
		this.accessTokenResponse = accessTokenResponse;
	}

	public Logger getLogger() {
		return logger;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public String getUserAPIResponse() {
		return userAPIResponse;
	}

	public void setUserAPIResponse(String userAPIResponse) {
		this.userAPIResponse = userAPIResponse;
	}

	public String getIdmBaseUrl() {
		return idmBaseUrl;
	}

	public void setIdmBaseUrl(String idmBaseUrl) {
		this.idmBaseUrl = idmBaseUrl;
	}
	
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnregisteredUserId() {
		return unregisteredUserId;
	}

	public void setUnregisteredUserId(String unregisteredUserId) {
		this.unregisteredUserId = unregisteredUserId;
	}

	public WebDriver getBrowser() {
		return browser;
	}

	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}

	public OAuthCommonSteps getOauthCommonSteps() {
		return oauthCommonSteps;
	}

	public void setOauthCommonSteps(OAuthCommonSteps oauthCommonSteps) {
		this.oauthCommonSteps = oauthCommonSteps;
	}
	
	
	
}
