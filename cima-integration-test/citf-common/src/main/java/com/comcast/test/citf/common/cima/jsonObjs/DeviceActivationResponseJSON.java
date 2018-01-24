package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Bean class for Device Activation Response.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "device_code",
        "user_code",
        "verification_uri",
        "expires_in",
        "interval",
        "error",
        "error_description"
})

public class DeviceActivationResponseJSON {

	@JsonProperty("device_code")
    private String device_code;

	@JsonProperty("user_code")
    private String user_code;
	
	@JsonProperty("verification_uri")
    private String verification_uri;
	
	@JsonProperty("expires_in")
    private long expires_in;
	
	@JsonProperty("interval")
    private long interval;
	
	@JsonProperty("error")
    private String error;
	
	@JsonProperty("error_description")
    private String error_description;
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Gets the device code.
	 * 
	 * @return The device code.
	 */
	@JsonProperty("device_code")
	public String getDevice_code() {
		return device_code;
	}

	/**
	 * Sets the device code.
	 * 
	 * @param device_code The device code to set.
	 */
	@JsonProperty("device_code")
	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	/**
	 * Gets the user code.
	 * 
	 * @return The user code.
	 */
	@JsonProperty("user_code")
	public String getUser_code() {
		return user_code;
	}

	/**
	 * Sets the user code.
	 * 
	 * @param user_code The user code to set.
	 */
	@JsonProperty("user_code")
	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	/**
	 * Gets the verification URL.
	 * 
	 * @return The verification URL.
	 */
	@JsonProperty("verification_uri")
	public String getVerification_uri() {
		return verification_uri;
	}

	/**
	 * Sets the verification URL.
	 * 
	 * @param verification_uri The verification URL to set.
	 */
	@JsonProperty("verification_uri")
	public void setVerification_uri(String verification_uri) {
		this.verification_uri = verification_uri;
	}

	/**
	 * Gets expires In.
	 * 
	 * @return The expires In attribute of the response.
	 */
	@JsonProperty("expires_in")
	public long getExpires_in() {
		return expires_in;
	}

	/**
	 * Sets expires In.
	 * 
	 * @param expires_in The expires In attribute to set.
	 */
	@JsonProperty("expires_in")
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
	}

	/**
	 * Gets the interval value.
	 * 
	 * @return The interval.
	 */
	@JsonProperty("interval")
	public long getInterval() {
		return interval;
	}

	/**
	 * Sets the interval value.
	 * 
	 * @param interval The interval to set.
	 */
	@JsonProperty("interval")
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
	/**
	 * Gets the error string.
	 * 
	 * @return The error string.
	 */
	@JsonProperty("error")
	public String getError() {
		return error;
	}

	/**
	 * Sets the error string.
	 * 
	 * @param error The error string to set.
	 */
	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Gets the error description.
	 * 
	 * @return The error description string.
	 */
	@JsonProperty("error_description")
	public String getError_description() {
		return error_description;
	}

	/**
	 * Sets the error description.
	 * 
	 * @param error_description The error description to set.
	 */
	@JsonProperty("error_description")
	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	/**
	 * Gets additional attributes of the response.
	 * 
	 * @return Any additional attributes in the response.
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	/**
	 * Sets additional attributes to the response.
	 * 
	 * @param additionalProperties The additional attributes to set.
	 */
	@JsonAnyGetter
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
