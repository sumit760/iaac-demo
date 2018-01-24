package com.comcast.cima.test.api.cucumber.steps;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.Instant;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.jsonObjs.OAuthAccessTokenInfo;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.dataProvider.ClientDetailsProvider;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider;
import com.comcast.test.citf.core.helpers.OAuthHelper;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Valdas Sevelis
 */
public class OAuth_InternalAccountIdentifier_Flow {

	/** Log */
	private final Logger log = LoggerFactory.getLogger(getClass());

	private List<String> registeredScopes;
	private String clientId;
	private String clientSecret;
	private String expectedAccountNumber;
	private String expectedAccountInternalId;
	private Key tokenSigningKey;
	private TokenResponse internalAccountIdResponse;
	private IOException internalAccountIdError;
	private List<String> requestedScopes;
	
	@Autowired
	private OAuthDataProvider oauthDataProvider;
	
	@Autowired
	private ClientDetailsProvider clientDetailsProvider;
	
	@Autowired
	private FreshAccountProvider freshAccountProvider;
	
	@Autowired
	private OAuthHelper oAuthHelper;

	@Before
	public void setup() {
		registeredScopes = null;
		clientId = null;
		clientSecret = null;
		expectedAccountNumber = null;
		expectedAccountInternalId = null;
		internalAccountIdResponse = null;
		internalAccountIdError = null;
		tokenSigningKey = null;
		oAuthHelper.setTokenInfoUrl(oauthDataProvider.getTokenInfoUrl());
	}

	@Given("^OAuth client with registered scopes \"([^\"]*)\" and internal account identifier flow$")
	public void oauthClientWithRegisteredScopesAndInternalAccountIdentifierFlow(String scopes) {
		registeredScopes = oAuthHelper.parseScopeString(scopes);
		ClientDetailsProvider.ClientDetails client = clientDetailsProvider.getClientDetails(ClientDetailsProvider.ClientType.OAUTH);
		
		clientId = client.getClientId();
		clientSecret = client.getClientSecret();

		log.trace("Retrieved client info: client_id={}, client_secret={}, scopes={}",
		         clientId, clientSecret, registeredScopes);
	}

	@Given("^invalid OAuth client for internal account identifier flow$")
	public void invalidOAuthClientForInternalAccountIdentifierFlow() {
		registeredScopes = ImmutableList.of();
		clientId = "unregistered-oauth-client-id";
		clientSecret = "unregistered-oauth-client-secret";
	}

	@And("^active billing account$")
	public void activeBillingAccount() {
		
		Map<FreshAccountProvider.Filter, String> acFilters = ImmutableMap.of(FreshAccountProvider.Filter.ACCOUNT_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE);
		AccountCache.Account fAccount = freshAccountProvider.getDedicatedAccount(acFilters);
		if (fAccount == null) {
			fail("Could not retrieve valid account for test");
		}
		
		this.expectedAccountNumber = fAccount.getAccountId();
		this.expectedAccountInternalId = fAccount.getAuthGuid();

		log.trace("Retrieved account info: number={}, guid={}", expectedAccountNumber, expectedAccountInternalId);
	}

	@And("^invalid billing account$")
	public void invalidBillingAccount() {
		this.expectedAccountNumber = "invalid-billing-account-number-for-failure-test";
		this.expectedAccountInternalId = "invalid-account-guid-for-failure-test";
	}

	@When("^OAuth client requests access token for scopes \"([^\"]*)\" using internal account identifier flow$")
	public void oauthClientRequestsAccessTokenForScopesUsingInternalAccountIdentifierFlow(String scopes) {
		requestedScopes = oAuthHelper.parseScopeString(scopes);
		assertThat(registeredScopes,
		           hasItems(requestedScopes.toArray(new String[requestedScopes.size()])));

		Instant now = new Instant();
		Instant notBefore = now.minus(Minutes.minutes(5).toStandardDuration());
		Instant expTime = now.plus(Minutes.minutes(5).toStandardDuration());

		final String token = Jwts.builder()
				.setSubject(expectedAccountInternalId)
				.setIssuer(clientId)
				.setAudience("https://login.comcast.net/oauth")
				.setIssuedAt(now.toDate())
				.setExpiration(expTime.toDate())
				.setNotBefore(notBefore.toDate())
				.signWith(SignatureAlgorithm.HS256, tokenSigningKey)
				.compact();
		final TokenRequest request = new TokenRequest(
				new ApacheHttpTransport(),
				new JacksonFactory(),
				new GenericUrl(oauthDataProvider.getAccessTokenUrl()),
				"urn:comcast:oauth:internal-account-id")
				.setScopes(requestedScopes)
				.setClientAuthentication(new BasicAuthentication(clientId, clientSecret))
				.set("token", token);

		try {
			internalAccountIdResponse = request.execute();
		} catch (IOException e) {
			internalAccountIdError = e;
		}
	}

	Key buildSecretKey(String encodedKey) {
		return new SecretKeySpec(Base64.decodeBase64(encodedKey), "HmacSHA256");
	}

	@Then("^OAuth client receives successful internal account Id response$")
	public void oauthClientReceivesSuccessfulResponse() {
		assertThat(
				"Expected valid token response",
				internalAccountIdResponse, notNullValue());
	}

	@And("^OAuth access token in internal account Id response is valid$")
	public void oauthAccessTokenInResponseIsValid() {
		assertThat(internalAccountIdResponse.getAccessToken(), notNullValue());
		OAuthAccessTokenInfo accessTokenInfo = oAuthHelper.verifyAccessToken(internalAccountIdResponse.getAccessToken());
		assertThat(
				"Access token should belong to the client that requested it",
				accessTokenInfo.getAudience(), is(clientId));
		assertThat(
				"Access token scopes should include all requested scopes",
				oAuthHelper.parseScopeString(accessTokenInfo.getScope()),
				hasItems(Iterables.toArray(requestedScopes, String.class)));
	}

	@Then("^OAuth client receives \"([^\"]*)\" error response$")
	public void oauthClientReceivesErrorResponse(String errorCode) {
		assertThat(
				"Expected error response",
		        internalAccountIdError, notNullValue());
		assertThat(
				"Expected that error response is a valid OAuth error",
		        internalAccountIdError,
		        instanceOf(TokenResponseException.class));
		TokenResponseException e = (TokenResponseException) internalAccountIdError;
		assertThat(
				"Expected OAuth error code: " + errorCode,
		        e.getDetails().getError(),
		        is(errorCode));
	}

	@And("^valid internal account grant token signing key$")
	public void validInternalAccountGrantTokenSigningKey() {
		final String secretKey = oauthDataProvider.getInternalAccountIdGrantSignKey();
		log.trace("Retrieved grant data signing key '{}' for environment '{}'",
		          		secretKey, oauthDataProvider.getCurrentEnvironment());
		tokenSigningKey = buildSecretKey(secretKey);
		assertThat(tokenSigningKey, notNullValue());
	}

	@And("^invalid internal account grant token signing key$")
	public void invalidInternalAccountGrantTokenSigningKey() {
		tokenSigningKey = buildSecretKey("AAAAAAAAAAA=");
	}
}
