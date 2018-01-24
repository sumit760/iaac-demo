package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.Objects;
import java.util.Set;

import org.joda.time.Instant;

import com.comcast.test.citf.common.cima.jsonObjs.ser.InstantAsUnixSecondsDeserializer;
import com.comcast.test.citf.common.cima.jsonObjs.ser.InstantAsUnixSecondsSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * Bean class for OAuth ID Token. This holds the ID token claim parameters.
 * 
 * @author Valdas Sevelis
 *
 */
public class OAuthIDTokenInfo {

	public static final String ISSUER_CLAIM_NAME = "iss";
	public static final String SUBJECT_CLAIM_NAME = "sub";
	public static final String AUDIENCE_CLAIM_NAME = "aud";
	public static final String AUTHORIZED_PARTY_CLAIM_NAME = "azp";
	public static final String ISSUED_AT_CLAIM_NAME = "iat";
	public static final String EXPIRES_AT_CLAIM_NAME = "exp";
	public static final String AUTHENTICATION_TIME_CLAIM_NAME = "auth_time";
	public static final String PREVIOUS_AUTHENTICATION_TIME_CLAIM_NAME = "prev_auth_time";
	public static final String AUTHENTICATION_METHODS_REFERENCES_CLAIM_NAME = "amr";
	public static final String AUTHENTICATION_CONTEXT_CLASS_REFERENCE_CLAIM_NAME = "acr";
	public static final String USER_TYPE_CLAIM_NAME = "user_type";
	public static final String REMEMBER_ME_STATE_CLAIM_NAME = "rm_state";
	public static final String LOGIN_ID_CLAIM_NAME = "login_id";
	public static final String TRACKING_ID_CLAIM_NAME = "tid";

	/**
	 * REQUIRED. Issuer Identifier for the Issuer of the response.
	 */
	@JsonProperty(ISSUER_CLAIM_NAME)
	private String issuer;

	/**
	 * REQUIRED. Subject identifier. A locally unique and never reassigned
	 * identifier within the Issuer for the End-User, which is intended to be
	 * consumed by the Client. e.g. 24400320 or
	 * AItOawmwtWwcT0k51BayewNvutrJUqsvl6qs7A4. It MUST NOT exceed 255 ASCII
	 * characters in length.
	 */
	@JsonProperty(SUBJECT_CLAIM_NAME)
	private String subjectIdentifier;

	/**
	 * REQUIRED. Audience that this ID Token is intended for. It MUST contain
	 * the OAuth 2.0 client_id of the Client.
	 */
	@JsonProperty(AUDIENCE_CLAIM_NAME)
	private String audience;

	/**
	 * OPTIONAL. Authorized Party. This member identifies an OAuth 2.0 client
	 * authorized to use this ID Token as an OAuth access token, if different
	 * than the Client that requested the ID Token. It MUST contain the
	 * client_id of the authorized party.
	 */
	@JsonProperty(AUTHORIZED_PARTY_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String authorizedParty;

	/**
	 * REQUIRED. Time at which the JWT was issued. The value is the number of
	 * seconds from 1970-01-01T0:0:0Z as measured in UTC until the desired
	 * date/time. See RFC 3339 [RFC3339] for details regarding date/times in
	 * general and UTC in particular.
	 */
	@JsonProperty(ISSUED_AT_CLAIM_NAME)
	@JsonSerialize(using = InstantAsUnixSecondsSerializer.class)
	@JsonDeserialize(using = InstantAsUnixSecondsDeserializer.class)
	private Instant issueInstant;

	/**
	 * REQUIRED. Expiration time on or after which the ID Token MUST NOT be
	 * accepted for processing. The processing of this parameter requires that
	 * the current date/time MUST be before the expiration date/time listed in
	 * the value. Implementers MAY provide for some small leeway, usually no
	 * more than a few minutes, to account for clock skew. The value is the
	 * number of seconds from 1970-01-01T0:0:0Z as measured in UTC until the
	 * desired date/time. See RFC 3339 [RFC3339] for details regarding
	 * date/times in general and UTC in particular.
	 */
	@JsonProperty(EXPIRES_AT_CLAIM_NAME)
	@JsonSerialize(using = InstantAsUnixSecondsSerializer.class)
	@JsonDeserialize(using = InstantAsUnixSecondsDeserializer.class)
	private Instant expirationInstant;

	/**
	 * OPTIONAL. Time when the End-User authentication occurred, specified as
	 * the number of seconds from 1970-01-01T0:0:0Z as measured in UTC.}
	 */
	@JsonProperty(AUTHENTICATION_TIME_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonSerialize(using = InstantAsUnixSecondsSerializer.class)
	@JsonDeserialize(using = InstantAsUnixSecondsDeserializer.class)
	private Instant authnInstant;

	/**
	 * OPTIONAL. Time when the End-User previously authenticated, specified as
	 * the number of seconds from 1970-01-01T0:0:0Z as measured in UTC.}
	 */
	@JsonProperty(PREVIOUS_AUTHENTICATION_TIME_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonSerialize(using = InstantAsUnixSecondsSerializer.class)
	@JsonDeserialize(using = InstantAsUnixSecondsDeserializer.class)
	private Instant prevAuthnInstant;

	/**
	 * OPTIONAL. Authentication Methods References. JSON array of strings that
	 * are identifiers for authentication methods used in the authentication.
	 */
	@JsonProperty(AUTHENTICATION_METHODS_REFERENCES_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Set<String> authenticationMethodsReferences;

	/**
	 * OPTIONAL. Authentication Context Class Reference. A string that
	 * identifies the Authentication Context Class that was satisfied by the
	 * authentication process.
	 */
	@JsonProperty(AUTHENTICATION_CONTEXT_CLASS_REFERENCE_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String authenticationContextClassReference;

	/**
	 * OPTIONAL. String claim indicating authenticated user type.
	 */
	@JsonProperty(USER_TYPE_CLAIM_NAME)
	private String userType;

	/**
	 * OPTIONAL. Boolean claim, indicating whether user elected to be kept
	 * signed in across browser/application restarts
	 */
	@JsonProperty(REMEMBER_ME_STATE_CLAIM_NAME)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Boolean rememberMeState;

	/**
	 * OPTIONAL. String claim indicating logged in user name.
	 */
	@JsonProperty(LOGIN_ID_CLAIM_NAME)
	private String loginIdentifier;
	
	/**
	 * OPTIONAL. String claim indicating tenant Id.
	 */
	@JsonProperty(TRACKING_ID_CLAIM_NAME)
	private String tenantIdentifier;

	/**
	 * Creates new ID Token
	 */
	public OAuthIDTokenInfo() {
	}

	/**
	 * @return the issuer
	 */
	public String getIssuer() {
		return issuer;
	}

	/**
	 * @return the subjectIdentifier
	 */
	public String getSubjectIdentifier() {
		return subjectIdentifier;
	}

	/**
	 * @return the audience
	 */
	public String getAudience() {
		return audience;
	}

	/**
	 * @return the authorizedParty
	 */
	public String getAuthorizedParty() {
		return authorizedParty;
	}

	/**
	 * @return the issueInstant
	 */
	public Instant getIssueInstant() {
		return issueInstant;
	}

	/**
	 * @return the expirationInstant
	 */
	public Instant getExpirationInstant() {
		return expirationInstant;
	}

	/**
	 * @return the authnInstant
	 */
	public Instant getAuthnInstant() {
		return authnInstant;
	}

	/**
	 * @return the prevAuthnInstant
	 */
	public Instant getPrevAuthnInstant() {
		return prevAuthnInstant;
	}

	/**
	 * @return the authenticationMethodsReferences
	 */
	public Set<String> getAuthenticationMethodsReferences() {
		return authenticationMethodsReferences;
	}

	/**
	 * @return the authenticationContextClassReference
	 */
	public String getAuthenticationContextClassReference() {
		return authenticationContextClassReference;
	}

	/**
	 * @return user's type
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @return remember me status flag
	 */
	public Boolean getRememberMeState() {
		return rememberMeState;
	}

	/**
	 * @return optional login id
	 */
	public String getLoginIdentifier() {
		return loginIdentifier;
	}
	
	/**
	 * @return optional login id
	 */
	public String getTenantIdentifier() {
		return tenantIdentifier;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				issuer, subjectIdentifier, audience,
				authorizedParty, issueInstant, expirationInstant,
				authnInstant, prevAuthnInstant, authenticationMethodsReferences,
				authenticationContextClassReference, userType, rememberMeState, loginIdentifier);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof OAuthIDTokenInfo)) {
			return false;
		}

		OAuthIDTokenInfo t = (OAuthIDTokenInfo)o;
		return Objects.equals(issuer, t.issuer)
				&& Objects.equals(subjectIdentifier, t.subjectIdentifier)
				&& Objects.equals(audience, t.audience)
				&& Objects.equals(authorizedParty, t.authorizedParty)
				&& Objects.equals(issueInstant, t.issueInstant)
				&& Objects.equals(expirationInstant, t.expirationInstant)
				&& Objects.equals(authnInstant, t.authnInstant)
				&& Objects.equals(prevAuthnInstant, t.prevAuthnInstant)
				&& Objects.equals(authenticationMethodsReferences, t.authenticationMethodsReferences)
				&& Objects.equals(authenticationContextClassReference, t.authenticationContextClassReference)
				&& Objects.equals(userType, t.userType)
				&& Objects.equals(rememberMeState, t.rememberMeState)
				&& Objects.equals(loginIdentifier, t.loginIdentifier);
	}
}
