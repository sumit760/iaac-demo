package com.comcast.test.citf.common.cima.jsonObjs;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Access token information
 *
 * @author Valdas Sevelis
 */
public class OAuthAccessTokenInfo {
	/**
	 * The application that is the intended target of the token.
	 */
	private final String audience;

	/**
	 * The space-delimited set of scopes that the user consented to.
	 */
	private final String scope;

	/**
	 * This field is only present if the {@code profile} scope was present in the request.
	 * The value of this field is an immutable identifier for the logged-in user,
	 * and may be used when creating and managing user sessions in your application.
	 * ability to correlate profile information across multiple applications in the same organization.
	 */
	private final String userId;

	/**
	 * The number of seconds left in the lifetime of the token.
	 */
	private final long expiresIn;


	/**
	 * Constructor of OAuthAccessTokenInfo
	 * 
	 * @param audience 
	 * 					Access token audience.
	 * @param scopes
	 * 					Access token scopes. 
	 * @param userId
	 * 					Access token userId.
	 * @param expiresIn
	 * 					Access token expires in value.
	 */
	public OAuthAccessTokenInfo(
			@JsonProperty("audience") String audience,
			@JsonProperty("scope") String scopes,
			@JsonProperty("user_id") String userId,
			@JsonProperty("expires_in") long expiresIn) {
		this.audience = audience;
		this.scope = scopes;
		this.userId = userId;
		this.expiresIn = expiresIn;
	}

	/**
	 * Gets the audience of the token.
	 * 
	 * @return The audience of the token.
	 */
	@JsonGetter("audience")
	public String getAudience() {
		return audience;
	}

	/**
	 * Gets the scope of the token.
	 * 
	 * @return The scope of the token.
	 */
	@JsonGetter("scope")
	public String getScope() {
		return scope;
	}

	/**
	 * Gets the userId of the token.
	 * 
	 * @return The userId of the token.
	 */
	@JsonGetter("user_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the expires in of the token.
	 * 
	 * @return The expires in of the token.
	 */
	@JsonGetter("expires_in")
	public long getExpiresIn() {
		return expiresIn;
	}
}
