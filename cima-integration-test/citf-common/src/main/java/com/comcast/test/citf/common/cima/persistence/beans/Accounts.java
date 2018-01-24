package com.comcast.test.citf.common.cima.persistence.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * Bean class for Accounts entity.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="accounts")
public class Accounts {
	
	/**
	 * Default constructor
	 */
	public Accounts(){
		
	}
	
	/**
	 * Constructor for Accounts
	 * 
	 * @param billingAccountId 
	 * 							Billing account Id.
     * @param serviceAccountId
     * 							Service account Id.
	 * @param environment
	 * 							Environment.
	 * @param billingSystem
	 * 							Billing system.
	 * @param authGuid
	 * 							Authorization Guid.
	 * @param accountStatus
	 * 							Account status.
	 * @param firstName
	 * 							First name.
	 * @param lastName
	 * 							Last name.
	 * @param phoneNumber
	 * 							Phone number.
	 * @param address
	 * 							Address.
	 * @param zip
	 * 							Zip-code.
	 * @param transferFlag
	 * 							Transfer Flag.
	 * @param physicalResourceLink
	 * 							Physical resource link.
	 * @param lob
	 * 							Line of Business.
	 * @param accountType
	 * 							Type of account.
	 * @param freshSSN
	 * 							Fresh SSN.
	 */
	public Accounts(	String billingAccountId,
                        String serviceAccountId,
						String environment,
						String billingSystem,
						String authGuid, 
						String accountStatus, 
						String firstName, 
						String lastName,
						String phoneNumber,
						String address,
						String zip,
						String transferFlag,
						String physicalResourceLink,
						String lob,
						String accountType,
						FreshSSN freshSSN){
		
		this.billingAccountId = billingAccountId;
		this.environment = environment;
		this.billingSystem = billingSystem;
		this.authGuid = authGuid;
		this.accountStatus = accountStatus;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.zip = zip;
		this.transferFlag = transferFlag;
		this.physicalResourceLink = physicalResourceLink;
		this.lob = lob;
		this.accountType = accountType;
		this.freshSSN = freshSSN;
        this.serviceAccountId = serviceAccountId;
	}

	@Id
	@Column(name = "billing_account_id", unique = true, nullable = false, length = 25)
	private String billingAccountId;

	@Column(name = "service_account_id", nullable = true, length = 25)
	private String serviceAccountId;

	@Column(name = "environment", nullable = false, length = 10)
	private String environment;
	
	@Column(name = "billing_system", nullable = true, length = 10)
	private String billingSystem;
	
	//lob i.e. Line of Business can be any of the following - HSD/CDV/VID/combination of any added with a ',' separator
	@Column(name = "lob", nullable = true, length = 15)
	private String lob;
	
	//account types are COMMERCIAL and RESIDENTIAL
	@Column(name = "account_type", nullable = true, length = 15)
	private String accountType;
	
	@Column(name = "auth_guid", nullable = false, length = 100)
	private String authGuid;
	
	@Column(name = "account_status", nullable = false, length = 15)
	private String accountStatus;
	
	@Column(name = "first_name", nullable = true, length = 100)
	private String firstName;
	
	@Column(name = "last_name", nullable = true, length = 100)
	private String lastName;
	
	@Column(name = "phone", nullable = true, length = 15)
	private String phoneNumber;
	
	@Column(name = "address", nullable = true, length = 500)
	private String address;
	
	@Column(name = "zip_code", nullable = true, length = 10)
	private String zip;
	
	@Column(name = "transfer_flag", nullable = true, length = 1)
	private String transferFlag;
	
	@Column(name = "physical_resource_link", nullable = true, length = 100)
	private String physicalResourceLink;
	
	@OneToMany(mappedBy = "uaMapPrimaryKey.account")
	private Collection<AccountUserMap> userMap;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fresh_ssn", nullable = true)
	private FreshSSN freshSSN;
	
	/**
	 * Returns the billingAccountId of Accounts
	 * @return billingAccountId
	 */
	public String getBillingAccountId() {
		return billingAccountId;
	}

	/**
	 * Sets billingAccountId of Accounts
	 * @param billingAccountId
	 * 			Billing account Id
	 */
	public void setBillingAccountId(String billingAccountId) {
		this.billingAccountId = billingAccountId;
	}

	/**
	 * Returns billingSystem of Accounts
	 * @return billingSystem
	 */
	public String getBillingSystem() {
		return billingSystem;
	}

	/**
	 * Sets billingSystem of Accounts
	 * @param billingSystem
	 * 			Billing system
	 */
	public void setBillingSystem(String billingSystem) {
		if(!StringUtility.isStringEmpty(billingSystem)){
			this.billingSystem = billingSystem;
		}
	}

	/**
	 * Returns authGuid of Accounts
	 * @return authGuid
	 */
	public String getAuthGuid() {
		return authGuid;
	}

	/**
	 * Sets authGuid of Accounts
	 * @param authGuid
	 * 			Auth GUID
	 */
	public void setAuthGuid(String authGuid) {
		if(!StringUtility.isStringEmpty(authGuid)){
			this.authGuid = authGuid;
		}
	}

	/**
	 * Returns accountStatus of Accounts
	 * @return accountStatus
	 */
	public String getAccountStatus() {
		return accountStatus;
	}

	/**
	 * Sets accountStatus of Accounts
	 * @param accountStatus
	 * 			Status of Account
	 */
	public void setAccountStatus(String accountStatus) {
		if(!StringUtility.isStringEmpty(accountStatus)){
			this.accountStatus = accountStatus;
		}
	}

	/**
	 * Returns firstName of Accounts
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets firstName of Accounts
	 * @param firstName
	 * 			First Name
	 */
	public void setFirstName(String firstName) {
		if(!StringUtility.isStringEmpty(firstName)){
			this.firstName = firstName;
		}
	}

	/**
	 * Returns lastName of Accounts
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets lastName of Accounts
	 * @param lastName
	 * 			Last Name
	 */
	public void setLastName(String lastName) {
		if(!StringUtility.isStringEmpty(lastName)){
			this.lastName = lastName;
		}
	}

	/**
	 * Returns phoneNumber of Accounts
	 * @return phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets phoneNumber of Accounts
	 * @param phoneNumber
	 * 			Phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		if(!StringUtility.isStringEmpty(phoneNumber)){
			this.phoneNumber = phoneNumber;
		}
	}

	/**
	 * Returns address of Accounts
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets address of Accounts
	 * @param address
	 * 			Address
	 */
	public void setAddress(String address) {
		if(!StringUtility.isStringEmpty(address)){
			this.address = address;
		}
	}

	/**
	 * Returns zip of Accounts
	 * @return zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * Sets zip of Accounts
	 * @param zip
	 * 			Zip code
	 */
	public void setZip(String zip) {
		if(!StringUtility.isStringEmpty(zip)){
			this.zip = zip;
		}
	}

	/**
	 * Returns transferFlag of Accounts
	 * @return transferFlag
	 */
	public String getTransferFlag() {
		return transferFlag;
	}

	/**
	 * Sets transferFlag of Accounts
	 * @param transferFlag
	 * 			Transfer Flag
	 */
	public void setTransferFlag(String transferFlag) {
		if(!StringUtility.isStringEmpty(transferFlag)){
			this.transferFlag = transferFlag;
		}
	}

	/**
	 * Returns physicalResourceLink of Accounts
	 * @return physicalResourceLink
	 */
	public String getPhysicalResourceLink() {
		return physicalResourceLink;
	}

	/**
	 * Sets physicalResourceLink of Accounts
	 * @param physicalResourceLink
	 * 			Physical Resource Link
	 */
	public void setPhysicalResourceLink(String physicalResourceLink) {
		if(!StringUtility.isStringEmpty(physicalResourceLink)){
			this.physicalResourceLink = physicalResourceLink;
		}
	}

	/**
	 * Returns Collection of AccountUserMap of Accounts
	 * @return Collection of AccountUserMap
	 */
	public Collection<AccountUserMap> getUserMap() {
		return userMap;
	}

	/**
	 * Sets Collection of AccountUserMap of Accounts
	 * @param userMap
	 * 			Collection of AccountUserMap
	 */
	public void setUserMap(Collection<AccountUserMap> userMap) {
		this.userMap = userMap;
	}

	/**
	 * Returns environment of Accounts
	 * @return environment
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * Sets environment of Accounts
	 * @param environment
	 * 			Execution environment
	 */
	public void setEnvironment(String environment) {
		if(!StringUtility.isStringEmpty(environment)){
			this.environment = environment;
		}
	}
	
	/**
	 * Returns freshSSN of Accounts
	 * @return freshSSN
	 */
	public FreshSSN getFreshSSN() {
		return freshSSN;
	}

	/**
	 * Sets freshSSN of Accounts
	 * @param freshSSN
	 * 			Fresh SSN which is not associated with existing user/account
	 */
	public void setFreshSSN(FreshSSN freshSSN) {
		if(ssn!=null){
			this.freshSSN = freshSSN;
		}
	}

	
	/**
	 * @return the lob
	 */
	public Set<String> getLob() {
		return StringUtility.getTokensFromString(this.lob, ICimaCommonConstants.COMMA);
	}

	/**
	 * @param lob the lob to set
	 */
	public void setLob(String lob) {
		if(!StringUtility.isStringEmpty(lob)){
			this.lob = lob;
		}
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(String accountType) {
		if(!StringUtility.isStringEmpty(accountType)){
			this.accountType = accountType;
		}
	}

    public String getServiceAccountId() {
        return serviceAccountId;
    }

    public void setServiceAccountId(String serviceAccountId) {
    	if(!StringUtility.isStringEmpty(serviceAccountId)){
    		this.serviceAccountId = serviceAccountId;
    	}
    }

    /********************* Transient stuff *************************/
    
    @Transient
    private List<String> primaryUserIds = null;
    
    @Transient
    private List<String> secondaryUserIds = null;
    
    @Transient
    private String ssn = null;
    
    @Transient
    private String dob = null;

    @Transient
	public List<String> getPrimaryUserIds() {
		return primaryUserIds;
	}

    @Transient
	public void addPrimaryUserId(String primaryUserId) {
		if(primaryUserIds == null){
			primaryUserIds = new ArrayList<String>();
		}
		primaryUserIds.add(primaryUserId);
	}

    @Transient
	public List<String> getSecondaryUserIds() {
		return secondaryUserIds;
	}

    @Transient
	public void addSecondaryUserId(String secondaryUserId) {
    	if(secondaryUserIds == null){
    		secondaryUserIds = new ArrayList<String>();
    	}
    	secondaryUserIds.add(secondaryUserId);
	}

    @Transient
	public String getSsn() {
		return ssn;
	}

    @Transient
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

    @Transient
	public String getDob() {
		return dob;
	}

    @Transient
	public void setDob(String dob) {
		this.dob = dob;
	}
    
    
}