package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The bean class for access token response.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access_token",
        "token_type",
        "refresh_token",
        "expires_in",
        "scope",
        "id_token",
        "error",
        "error_description"
})

public class AccessTokenResponseJSON {
	
	@JsonProperty("access_token")
    private String access_token;
	
	@JsonProperty("token_type")
    private String token_type;
	
	@JsonProperty("refresh_token")
    private String refresh_token;
	
	@JsonProperty("expires_in")
    private long expires_in;
	
	@JsonProperty("scope")
    private String scope;
	
	@JsonProperty("id_token")
	private String id_token;
	
	@JsonProperty("error")
	private String error;
	
	@JsonProperty("error_description")
	private String error_description;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the access token.
	 * 
	 * @return The access token.
	 */
	@JsonProperty("access_token")
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * Sets the access token.
	 * 
	 * @param access_token The access token to set.
	 */
	@JsonProperty("access_token")
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * Gets the token type.
	 * 
	 * @return The token type i.r 'Bearer' etc.
	 */
	@JsonProperty("token_type")
	public String getToken_type() {
		return token_type;
	}

	/**
	 * Sets the token type.
	 * 
	 * @param token_type The token type i.r 'Bearer' etc.
	 */
	@JsonProperty("token_type")
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * Gets the refresh token.
	 * 
	 * @return The refresh token.
	 */
	@JsonProperty("refresh_token")
	public String getRefresh_token() {
		return refresh_token;
	}

	/**
	 * Sets the refrersh token.
	 * 
	 * @param refresh_token The refresh token to set.
	 */
	@JsonProperty("refresh_token")
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	/**
	 * Gets the expires in of the access token.
	 * 
	 * @return Expires In of the access token.
	 */
	@JsonProperty("expires_in")
	public long getExpires_in() {
		return expires_in;
	}

	/**
	 * Sets the expires in of the access token.
	 * 
	 * @param expires_in Expires In of the access token to set.
	 */
	@JsonProperty("expires_in")
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * Gets the access token scope.
	 * 
	 * @return The access token scope.
	 */
	@JsonProperty("scope")
	public String getScope() {
		return scope;
	}

	/**
	 * Sets the access token scope.
	 * 
	 * @param scope The access token scope.
	 */
	@JsonProperty("scope")
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * Gets the id token attribute of the access token.
	 * 
	 * @return The Id token.
	 */
	@JsonProperty("id_token")
	public String getId_token() {
		return id_token;
	}

	/**
	 * Sets the id token attribute of the access token.
	 * 
	 * @param id_token The Id token.
	 */
	@JsonProperty("id_token")
	public void setId_token(String id_token) {
		this.id_token = id_token;
	}

	/**
	 * Gets the error if access token is associated with errors.
	 * 
	 * @return The error string.
	 */
	@JsonProperty("error")
	public String getError() {
		return error;
	}

	/**
	 * Sets the error string of the access token.
	 * 
	 * @param error The error string.
	 */
	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets the error description of the access token.
	 * 
	 * @return The error description string.
	 */
	@JsonProperty("error_description")
	public String getError_description() {
		return error_description;
	}
	

	/**
	 * Sets the error description of the access token.
	 * 
	 * @param error_description The error description string.
	 */
	@JsonProperty("error_description")
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	/**
	 * Gets other additional properties.
	 * 
	 * @return The additional properties.
	 */
	@JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

	/**
	 * Sets other additional properties.
	 * 
	 * @param name The name of the property.
	 * @param value The value of the property.
	 */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }	
}
