package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Bean class for IDM user credential validation response.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
/*@JsonPropertyOrder({
        "uid",
        "available",
        "alternateEmail",
        "mobilePhoneNumber",
        "resetCode",
        "errorCode",
        "errorDescription",
        "status",
        "message",   
})*/

@JsonIgnoreProperties(ignoreUnknown=true)
public class IDMUserCredentialValidationResponseJSON {

	@JsonProperty("uid")
    private String uid;

	@JsonProperty("available")
    private String available;
	
	@JsonProperty("alternateEmail")
    private String alternateEmail;
	
	@JsonProperty("mobilePhoneNumber")
    private String mobilePhoneNumber;
	
	@JsonProperty("resetCode")
    private String resetCode;
	
	@JsonProperty("errorCode")
    private String errorCode;
	
	@JsonProperty("errorDescription")
    private String errorDescription;
	
	@JsonProperty("status")
    private String status;
	
	@JsonProperty("message")
    private String message;
	
	
	@JsonProperty("billingAccountId")
    private String billingAccountId;
	
	@JsonProperty("customerGuid")
	private String customerGuid;		

	@JsonProperty("userName")
    private String userName;
	
	@JsonProperty("firstName")
    private String firstName;
	
	@JsonProperty("lastName")
    private String lastName;
	
	@JsonProperty("contactEmail")
    private String contactEmail;
	
	@JsonProperty("issuedAt")
    private String issuedAt;
	
	@JsonProperty("validationUrlTTLHours")
    private String validationUrlTTLHours;
	
	@JsonProperty("prefferedEmail")
    private String prefferedEmail;
	
	@JsonProperty("videoOnlyCustomer")
    private String videoOnlyCustomer;
	
	
	@JsonProperty("custGuid")
    private String custGuid;
	
	
	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	/**
	 * Returns the uid.
	 * 
	 * @return The uid.
	 */
	@JsonProperty("uid")
	public String getUid() {
		return uid;
	}
	
	/**
	 * Sets the uid.
	 * 
	 * @param uid uid to set.
	 */
	@JsonProperty("uid")
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * Returns userId availability.
	 *  
	 * @return The userId availability.
	 */
	@JsonProperty("available")
	public String getAvailable() {
		return available;
	}

	/**
	 * Sets user availability.
	 * 
	 * @param available User availability.
	 */
	@JsonProperty("available")
	public void setAvailable(String available) {
		this.available = available;
	}

	/**
	 * Returns alternative email.
	 * 
	 * @return The alternative email of user.
	 */
	@JsonProperty("alternateEmail")
	public String getAlternateEmail() {
		return alternateEmail;
	}

	/**
	 * Sets the alternative email.
	 * 
	 * @param alternateEmail Alternative email to set.
	 */
	@JsonProperty("alternateEmail")
	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	/**
	 * Returns mobile phone number of user.
	 * 
	 * @return The mobile phone number.
	 */
	@JsonProperty("mobilePhoneNumber")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * Sets the mobile phone number.
	 * 
	 * @param mobilePhoneNumber Mobile phone number to set.
	 */
	@JsonProperty("mobilePhoneNumber")
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	/**
	 * Returns the reset code.
	 * 
	 * @return Reset code.
	 */
	@JsonProperty("resetCode")
	public String getResetCode() {
		return resetCode;
	}

	/**
	 * Sets the reset code.
	 * 
	 * @param resetCode Reset code to set.
	 */
	@JsonProperty("resetCode")
	public void setResetCode(String resetCode) {
		this.resetCode = resetCode;
	}

	/**
	 * Returns the error code.
	 * 
	 * @return The error code.
	 */
	@JsonProperty("errorCode")
	public String getErrorCode() {
		return errorCode;
	}

	
	/**
	 * Sets the error code.
	 * 
	 * @param errorCode The error code to set.
	 */
	@JsonProperty("errorCode")
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Returns the error description.
	 * 
	 * @return The error description.
	 */
	@JsonProperty("error_description")
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Sets the error description.
	 * 
	 * @param errorDescription Error description to set.
	 */
	@JsonProperty("error_description")
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Returns the user status.
	 * 
	 * @return User status.
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the user status.
	 * 
	 * @param status Status to set.
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the message.
	 * 
	 * @return The message to return.
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * 
	 * @param message The message to set.
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the billing account Id.
	 * 
	 * @return The billing account Id.
	 */
	@JsonProperty("billingAccountId")
	public String getBillingAccountId() {
		return billingAccountId;
	}

	/**
	 * Sets the billing account Id.
	 * 
	 * @param billingAccountId The billing account Id to set.
	 */
	@JsonProperty("billingAccountId")
	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	/**
	 * Returns the customer Guid.
	 * 
	 * @return Customer Guid.
	 */
	@JsonProperty("customerGuid")		
	public String getCustomerGuid() {		
		return customerGuid;		
	}		
		 		
	/**		
	* Sets the customer guid.		
	* 		
	* @param customerGuid Customer Guid to set.		
	*/		
	@JsonProperty("customerGuid")		
	public void setCustomerGuid(String customerGuid) {		
		this.customerGuid = customerGuid;		
	}		


	/**
	 * Returns the user name.
	 *  
	 * @return User name.
	 */
	@JsonProperty("userName")
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 * 
	 * @param userName User name to set.
	 */
	@JsonProperty("userName")
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Returns the first name of user.
	 * 
	 * @return First name of user.
	 */
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name of the user.
	 * 
	 * @param firstName First name to set.
	 */
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	/**
	 * Returns the last name of the user.
	 * 
	 * @return Last name of user.
	 */
	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name of the user.
	 * 
	 * @param lastName Last name to set.
	 */
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns contact email of user.
	 * 
	 * @return Contact email of user.
	 */
	@JsonProperty("contactEmail")
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * Sets contact email of user.
	 * 
	 * @param contactEmail Contact email to set.
	 */
	@JsonProperty("contactEmail")
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	
	/**
	 * Returns issued At.
	 * 
	 * @return Issued At.
	 */
	@JsonProperty("issuedAt")
	public String getIssuedAt() {
		return issuedAt;
	}

	
	/**
	 * Sets issued at attribute.
	 * 
	 * @param issuedAt issued at attribute to set.
	 */
	@JsonProperty("issuedAt")
	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	/**
	 * Returns validation URL TTL in hours.
	 * 
	 * @return Validation URL TTL in hours
	 */
	@JsonProperty("validationUrlTTLHours")
	public String getValidationUrlTTLHours() {
		return validationUrlTTLHours;
	}

	/**
	 * Sets validation URL TTL in hours.
	 * 
	 * @param validationUrlTTLHours Validation URL TTL hour to set.
	 */
	@JsonProperty("validationUrlTTLHours")
	public void setValidationUrlTTLHours(String validationUrlTTLHours) {
		this.validationUrlTTLHours = validationUrlTTLHours;
	}

	/**
	 * Returns the preferred email of user.
	 * 
	 * @return The Preferred email of user.
	 */
	@JsonProperty("prefferedEmail")
	public String getPrefferedEmail() {
		return prefferedEmail;
	}

	/**
	 * Sets the preferred email of user.
	 * 
	 * @param prefferedEmail Preferred email to set.
	 */
	@JsonProperty("prefferedEmail")
	public void setPrefferedEmail(String prefferedEmail) {
		this.prefferedEmail = prefferedEmail;
	}

	/**
	 * Returns whether customer is video only.
	 * 
	 * @return The video only status of customer.
	 */
	@JsonProperty("videoOnlyCustomer")
	public String getVideoOnlyCustomer() {
		return videoOnlyCustomer;
	}

	/**
	 * Sets the customer as video only.
	 * 
	 * @param videoOnlyCustomer Video only customer.
	 */
	@JsonProperty("videoOnlyCustomer")
	public void setVideoOnlyCustomer(String videoOnlyCustomer) {
		this.videoOnlyCustomer = videoOnlyCustomer;
	}

	/**
	 * Returns customer Guid.
	 * 
	 * @return Customer guid.
	 */
	@JsonProperty("custGuid")
	public String getCustGuid() {
		return custGuid;
	}

	/**
	 * Sets customer guid.
	 * 
	 * @param custGuid Customer Guid to set.
	 */
	@JsonProperty("custGuid")
	public void setCustGuid(String custGuid) {
		this.custGuid = custGuid;
	}
	

	/**
	 * Returns any additional properties associated.
	 * 
	 * @return Additional properties associated.
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	/**
	 * Sets any additional properties of the user.
	 *  
	 * @param additionalProperties Additional properties to set.
	 */
	@JsonAnyGetter
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
