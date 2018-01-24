package com.comcast.cima.test.ui.cucumber.steps;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.jsonObjs.OAuthAccessTokenInfo;
import com.comcast.test.citf.common.cima.jsonObjs.OAuthIDTokenInfo;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.dataProvider.ClientDetailsProvider;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.helpers.OAuthHelper;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.OAuthOutOfBoundsPage;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author Valdas Sevelis
 */
public class OAuthIntegrationTestSteps {

	/** Log */
	private final Logger log = LoggerFactory.getLogger(getClass());

	private String oauthRedirectUrl;
	private String userId;
	private String userPassword;
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private OAuthDataProvider oauthDataProvider;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private ClientDetailsProvider clientProvider;
	
	@Autowired
	private OAuthHelper oAuthHelper;

	private AuthorizationCodeFlow oauthAuthorizationFlow;
	private WebDriver browser;
	private Scenario testScenario;

	@Before
	public void setup(Scenario scenario)  {
		this.testScenario = scenario;
		oauthAuthorizationFlow = null;
		browser = null;
		oAuthHelper.setTokenInfoUrl(oauthDataProvider.getTokenInfoUrl());
	}

	@After
	public void tearDown() {
		try{
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
		}catch(Exception e){
			log.error("Error occurred while performing clean up activities : ", e);
		}
	}

	@And("^Unregistered OAuth Client$")
	public void unregisteredOAuthClient()  {
		expectOAuthClientWithRedirectUrl("unregistered-oauth-client-id", "http://www.example.com", Collections.<String>emptyList());
	}

	@When("^OAuth client requests access token for scopes \"(.*?)\" using implicit grant flow$")
	public void executeImplicitGrantRequest(String scopes) {
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setRedirectUri(oauthRedirectUrl)
						.setScopes(oAuthHelper.parseScopeString(scopes))
						.build();
		browser.navigate().to(url);
	}

	@When("^OAuth client requests access token for scopes \"(.*?)\" using implicit grant flow and passive authentication$")
	public void oauthClientRequestsAccessTokenForScopesScopesUsingImplicitGrantFlowAndPassiveAuthentication(String scopes)  {
		String url =
				new BrowserClientRequestUrl(
						oauthAuthorizationFlow.getAuthorizationServerEncodedUrl(),
						oauthAuthorizationFlow.getClientId())
						.setRedirectUri(oauthRedirectUrl)
						.setScopes(oAuthHelper.parseScopeString(scopes))
						.set("prompt", "none")
						.build();

		browser.navigate().to(url);
	}

	@And("^User signs-in at Login Page$")
	public void signsInAtLoginPage()  {
		SignInToXfinity loginPage = new SignInToXfinity(browser);
		assertThat(
				"Username field is not visible",
				loginPage.isUsernameFieldPresent(),
				is(true));
		assertThat(
				"Password field is not visible",
				loginPage.isPasswordFieldPresent(),
				is(true));
		assertThat(
				"Sign In button is not visible",
				loginPage.isSignInButtonPresent(),
				is(true));
		loginPage.signInToGetResponseURL(userId, userPassword);
	}

	@Then("^Browser navigates to page (.*?)$")
	public void browserNavigatesToExpectedUrl(String url) {
		assertThat(
				"Unexpected url: " + browser.getCurrentUrl(),
				browser.getCurrentUrl().startsWith(url),
				is(true));
	}

	@Then("^Browser navigates to OAuth OOB URL$")
	public void browserNavigatesToOAuthOOBUrl()  {
		browserNavigatesToExpectedUrl(oauthDataProvider.getOobUrl());
	}

	@And("^OAuth access token is present in the url fragment$")
	public void oauthAccessTokenIsPresentInTheUrlFragment() {
		final GenericUrl url = new GenericUrl(browser.getCurrentUrl());
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(url.getFragment());
		assertThat(
				"Can't find access token in the URL: " + url,
				fragmentData.keySet(),
				hasItem("access_token"));
	}

	@And("^OAuth access token in the url fragment is valid$")
	public void oauthAccessTokenInTheUrlFragmentIsValid()  {
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(new GenericUrl(browser.getCurrentUrl()).getFragment());
		final String accessToken = fragmentData.get("access_token");

		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(accessToken);
		assertThat(
				"Unexpected access token audience",
				accessTokenInfo.getAudience(),
				is(oauthAuthorizationFlow.getClientId()));
		final Collection<String> requestedScopes = oauthAuthorizationFlow.getScopes();
		String[] expectedScopes = requestedScopes.toArray(new String[requestedScopes.size()]);
		assertThat(
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(expectedScopes));
		assertThat(
				accessTokenInfo.getExpiresIn() > 0,
		        is(true));
		if (requestedScopes.contains("profile")) {
			assertThat(
					"Expected user id in token info response, since requested scopes include 'profile'",
					accessTokenInfo.getUserId(),
					notNullValue());
		} else {
			assertThat(
					"Unexpected user id in token info response, since requested scopes do not include 'profile'",
					accessTokenInfo.getUserId(),
					nullValue());
		}
	}

	@And("^id_token in the url fragment is valid$")
	public void id_tokenInTheUrlFragmentIsValid()  {
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(new GenericUrl(browser.getCurrentUrl()).getFragment());
		final String idToken = fragmentData.get("id_token");

		OAuthIDTokenInfo idTokenInfo = oAuthHelper.verifyIdToken(idToken);
		assertThat(
				"Unexpected id token audience",
				idTokenInfo.getAudience(),
				is(oauthAuthorizationFlow.getClientId()));
		assertThat(
				"Unexpected id token authorized party",
				idTokenInfo.getAuthorizedParty(),
		        is(oauthAuthorizationFlow.getClientId()));
		assertThat(
				"User id is required in id token",
				idTokenInfo.getSubjectIdentifier(),
		        notNullValue());
	}

	@And("^id_token is not present in the url fragment$")
	public void idTokenIsNotPresentInTheUrlFragment() {
		final GenericUrl url = new GenericUrl(browser.getCurrentUrl());
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(url.getFragment());
		assertThat(
				"Found unexpected id token in the URL: " + url,
				fragmentData.keySet(),
				not(hasItem("id_token")));
	}

	@And("^id_token is present in the url fragment$")
	public void id_tokenIsPresentInTheUrlFragment() {
		final GenericUrl url = new GenericUrl(browser.getCurrentUrl());
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(url.getFragment());
		assertThat(
				"Can't find id token in the URL: " + url,
				fragmentData.keySet(),
				hasItem("id_token"));
	}

	@And("^OAuth access token is present in the OOB Page$")
	public void oauthAccessTokenIsPresentInTheOOBPage() {
		OAuthOutOfBoundsPage oobPage = new OAuthOutOfBoundsPage(browser);
		assertThat(
				oobPage.getEmbeddedAccessToken().isPresent(),
				is(true));
	}

	@And("^id_token is not present in the OOB Page$")
	public void id_tokenIsNotPresentInTheOOBPage() {
		OAuthOutOfBoundsPage oobPage = new OAuthOutOfBoundsPage(browser);
		assertThat(
				oobPage.getEmbeddedIdToken().isPresent(),
				is(false));
	}

	@And("^id_token is present in the OOB Page$")
	public void id_tokenIsPresentInTheOOBPage() {
		OAuthOutOfBoundsPage oobPage = new OAuthOutOfBoundsPage(browser);
		assertThat(
				oobPage.getEmbeddedIdToken().isPresent(),
				is(true));
	}

	@Then("^\"([^\"]*)\" OAuth error page is displayed$")
	public void oauthErrorPageIsDisplayed(String errorCode)  {
		browserNavigatesToExpectedUrl(oauthDataProvider.getAuthorizationUrl());

		assertThat(
				"Unexpected error content:" + browser.getPageSource(),
				browser.getPageSource(),
				containsString(String.format("error=\"%s\"", errorCode)));
	}

	@And("^\"([^\"]*)\" OAuth error is present in the url fragment$")
	public void oauthErrorIsPresentInTheUrlFragment(String errorCode)  {
		final GenericUrl url = new GenericUrl(browser.getCurrentUrl());
		final Map<String, String> fragmentData = oAuthHelper.parseOAuthUrlFragment(url.getFragment());
		assertThat(
				"Can't find expected error code in the URL: " + url,
				fragmentData.get("error"),
				is(errorCode));
	}

	@Given("^Web browser named \"([^\"]*)\" for platform \"([^\"]*)\"$")
	public void setupBrowser(String browserName, String platform)  {
		setupBrowser(browserName, null, platform);
	}

	@Given("^Web browser named \"([^\"]*)\" with version \"([^\"]*)\" for platform \"([^\"]*)\"$")
	public void setupBrowser(String browserName, String version, String platform)  {
		try{
			browser = citfTestInitializer.getBrowserInstance(testScenario.getName(), BrowserCapabilityDAO.Platforms.valueOf(platform), BrowserCapabilityDAO.Types.COMPUTER, browserName, false);
		}catch(IOException e){
			log.error("Error occurred while getting browser instance : ", e);
			fail("Not able to get browser instance due to : "+e.getMessage());
		}
	}

	@And("^OAuth client with registered scopes \"([^\"]*)\" and HTTP redirect url$")
	public void registeredOAuthClientWithRegisteredScopesAndHTTPRedirectUrl(String scopes)  {
		List<String> requestedScopes = oAuthHelper.parseScopeString(scopes);
		ClientDetailsProvider.ClientDetails loginClient = clientProvider.getClientDetails(ClientDetailsProvider.ClientType.LOGIN);
		
		final String clientId = loginClient.getClientId();
		final String redirectUrl = oauthDataProvider.getHttepRedirectUrlForLogin();
		expectOAuthClientWithRedirectUrl(clientId, redirectUrl, requestedScopes);
	}

	@And("^OAuth client with registered scopes \"([^\"]*)\" and out-of-bounds redirect url$")
	public void registeredOAuthClientWithRegisteredScopesAndOutOfBoundsRedirectUrl(String scopes)  {
		List<String> requestedScopes = oAuthHelper.parseScopeString(scopes);
		ClientDetailsProvider.ClientDetails loginClient = clientProvider.getClientDetails(ClientDetailsProvider.ClientType.LOGIN);
		
		final String clientId = loginClient.getClientId();
		final String redirectUrl = oauthDataProvider.getOobUrlForLogin();
		expectOAuthClientWithRedirectUrl(clientId, redirectUrl, requestedScopes);
	}

	@And("^Active user$")
	public void activeUser()  {
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE);
		UserCache.User user = userProvider.fetchUser(filters, false, UserDataProvider.UserCategory.LOGIN);
		
		if (user == null) {
			fail("Could not retrieve valid user for test");
		}
		
		userId = user.getUserId();
		userPassword = user.getPassword();
	}

	private void expectOAuthClientWithRedirectUrl(String clientId, String redirectUrl, List<String> scopes)  {
		log.debug("Initializing OAuth client [clientId={}, redirectUrl={}, scopes={}",
				clientId, redirectUrl, scopes);

		this.oauthRedirectUrl = redirectUrl;
		oauthAuthorizationFlow = new AuthorizationCodeFlow.Builder(
				BearerToken.authorizationHeaderAccessMethod(),
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				null,
				clientId,
				oauthDataProvider.getAuthorizationUrl())
				.setScopes(scopes)
				.build();
	}

	@Then("^Browser navigates to OAuth client's redirect url$")
	public void browserNavigatesToOAuthClientSRedirectUrl()  {
		browserNavigatesToExpectedUrl(oauthRedirectUrl);
	}
}
