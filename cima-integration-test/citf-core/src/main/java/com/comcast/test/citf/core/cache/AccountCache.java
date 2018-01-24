package com.comcast.test.citf.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.controller.AbstractExecutionController;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * Account cache is for keeping account related data.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 */
@Service("accountCache")
public class AccountCache extends CacheAdapter implements ICitfCache{

	private static final String REGION = "ACCOUNT_CACHE";
	private static final String ENV_REGION = "ACCOUNT_ENV_CACHE";
	
		
	/**
	 * Constructor of AccountCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	public AccountCache(){
		try{
			cache = JCS.getInstance(REGION);
			environmentCache = JCS.getInstance(ENV_REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while initializing Cache : ", e);
		}
	}
	
	
	/**
	 * Populates the account detail into cache for any specific environment
	 * 
	 * @param key
	          	Key to fetch the account value
	 * @param value
	          	The value which is to be populated
	 * @param environment
	          	Environment under which key is to be stored
	 */
	
	@Override
	public boolean put(String key, Object value, String environment) {
		LOGGER.debug("### Cache population input :: key = {} and value is {}", key, (value!=null?"not null.":"null."));
		boolean isSucceeded = false;
		try{
			//validation....
			if(StringUtility.isStringEmpty(key) || value == null || !(value instanceof Account) || AbstractExecutionController.isInEnvironment(environment)==null){
				LOGGER.error("Account cache not able process put request with key {} and value {} for environment {} due to any of these is(are) null or invalid",
								key, value, environment);
				return false;
			}

			Account account = (Account)value;
			cache.put(key, account);
			Set<String> accIds = environmentCache.get(environment);
			if(accIds == null){
				accIds = new HashSet<String>();
			}
			
			accIds.add(key);
			environmentCache.put(environment, accIds);
			isSucceeded = true;
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while putting new value in Account Cache:", e);
		}
		return isSucceeded;
	}
	
	
	/**
	 * To get list of accounts based on given filter conditions.
	 * 
	 * @param filterConditions
	 *        	Map of filter conditions where a filter condition acts as a query parameter to fetch data. It can be any of the list of possible values given below
	 *        	A. Exact value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS == 'ABCD'" SQL query
	 *        	B. Not null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NOT NULL" SQL query
	 *        	C. null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NULL" SQL query
	 *        
	 *        	The possible filter condition keys should be any of the given list. Other than ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, all are optional.
	 * 				1. ICommonConstants.CACHE_FLTR_CONDTN_PHONE
	 * 				2. ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS
	 * 				3. ICommonConstants.CACHE_FLTR_CONDTN_USER_ROLE
	 * 				5. ICommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG
	 * 				6. ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV
	 * 				7. ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS
	 *  			8. ICommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN
	 *  			9. ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED
	 *         	   10. ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT
	 *         	   11. ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV
	 *         	   12. ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_TYPE
	 *             13. ICommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID		
	 *  
	 *  		P.S. The mandatory filter ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT should be given in the input along with optional filter conditions (if required).
	 *        
	 * @param noOfObjectsRequired 
	 *        	No of accounts required to be fetched
	 * 
	 * @return List of Account objects
	 */
	@Override
	public List<Object> getFilteredObjects(Map<String, String> filterConditions, int noOfObjectsRequired){
		LOGGER.debug("### Starting to filter with filterConditions: {} and noOfObjectsRequired {}", filterConditions, noOfObjectsRequired);
		List<Object> objList = null;
		Set<String> accIds;

		if(filterConditions==null || filterConditions.isEmpty() || filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT)==null){
			LOGGER.error("Account cache not able process getFilteredObjects request with filter[keys] {} due to filterConditions is(are) null or not containing environment", 
					filterConditions!=null?Arrays.toString(filterConditions.keySet().toArray()):null);
			return null;
		}
		
		String phoneFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_PHONE);
		String statusFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS);
		String userRoleFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ROLE);
		Set<String> userIds = StringUtility.getTokensFromString(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV), ICommonConstants.COMMA);
		String transferFlagFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG);
		String addressFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS);
		String accountTypeFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_TYPE);
		String ssnFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN);
		boolean unlockFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED)!=null;
		String serviceAccountFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID);
		
		//environment filtering
		accIds = new HashSet<String>(environmentCache.get(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT)));
		
		if(!accIds.isEmpty()){
			objList = new ArrayList<Object>();
			int count = 1;
			
			for(String key : accIds){
				Account account = cache.get(key);
				
				if(noOfObjectsRequired != ICommonConstants.QUANTITY_UNLIMITED && count>noOfObjectsRequired){
					break;
				}
				
				String pickedUserId = null;
				//User id check
				if(userIds!=null || userRoleFilter!=null){
					boolean isValid = false;
					
					if(userIds != null){
						if(userRoleFilter!=null){
							List<String> uids = ICimaCommonConstants.UserRoles.SECONDARY.getValue().equals(userRoleFilter)? account.getSecondaryUserIds() : account.getPrimaryUserIds();
							if(uids!=null){
								for(String uid : uids){
									if(userIds.contains(uid)){
										isValid = true;
										pickedUserId = uid;
										break;
									}
								}
							}
						}
						else{
							if(account.getPrimaryUserIds()!=null){
								for(String uid : account.getPrimaryUserIds()){
									if(userIds.contains(uid)){
										isValid = true;
										pickedUserId = uid;
										break;
									}
								}
							}
						
							if(account.getSecondaryUserIds()!=null && !isValid){
								for(String uid : account.getSecondaryUserIds()){
									if(userIds.contains(uid)){
										isValid = true;
										pickedUserId = uid;
										break;
									}
								}
							}
						}
					}
					
					//without input user ids
					else{
						if(ICommonConstants.CACHE_FLTR_VALUE_NULL.equals(userRoleFilter)){
							if(account.getPrimaryUserIds()==null && account.getSecondaryUserIds()==null){
								isValid = true;
							}
						}
						else if((ICimaCommonConstants.UserRoles.PRIMARY.getValue().equals(userRoleFilter) && account.getPrimaryUserIds()!=null) || 
								(ICimaCommonConstants.UserRoles.SECONDARY.getValue().equals(userRoleFilter) && account.getSecondaryUserIds()!=null)){
							isValid = true;
						}
					}
						
					if(!isValid){
						continue;
					}
				}				
				
				//LOB filter
				if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV)!=null){
					if(account.getLobs()==null){
						continue;
					}
					Set<String> inputLobs = StringUtility.getTokensFromString(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV),
												ICimaCommonConstants.COMMA);
					for(String inLob : inputLobs){
						if(!account.getLobs().contains(inLob)){
							continue;
						}
					}
				}
				
				if( (strictFilter(statusFilter, account.getStatus())) &&
					(strictFilter(transferFlagFilter, account.getTransferFlag())) &&
					(nonStrictFilter(addressFilter, account.getAddress())) &&
					(nonStrictFilter(phoneFilter, account.getPhoneNumber())) && 
					(nonStrictFilter(ssnFilter, account.getFreshSsn())) &&
					(nonStrictFilter(accountTypeFilter, account.getAccountType())) &&
					(nonStrictFilter(serviceAccountFilter, account.getServiceAccountId())) &&
					(!unlockFilter || !account.isLocked())){
					
					Account acc = account.clone();
					if (account.isLocked) {
						acc.setLocked(true);
					}
					acc.setInputUserId(pickedUserId);
					objList.add(acc);
					count++;
				}
			}
		}
		
		LOGGER.info("### Cache filteration is over and output contains {} accounts.", (objList!=null?objList.size():"0"));
		return objList;
	}
	
	/**
	 * To change lock status of any account in Cache
	 * 
	 * @param key 
	 *        Key to get account object
	 * @param lockValue
	 *        true or false based on account to lock or unlock
	 * 
	 */
	
	@Override
	public synchronized void changeLock(String key, boolean lockValue) throws LockException{
		Object obj =  cache.get(key);
		if(obj==null){
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_FOUND, "Account[key="+key+"] not exist!");
		}
		Account account = (Account)obj;
		
		if(lockValue && account.isLocked()){
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_ALREADY_LOCKED, "Account[key="+key+"] is already locked!");
		}
		else if(!lockValue && !account.isLocked()){
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_LOCKED, "Account[key="+key+"] is not locked!");
		}
		account.setLocked(lockValue);
		LOGGER.info("Account[key={}] has been {}", key, (lockValue?"locked":"unlocked"));
	}

	
	/**
	 * Check lock status of any user
	 * 
	 * @param key
	 *        The key to fetch user
	 * 
	 */
	public boolean isLocked(String key) throws LockException{
		boolean result = false;
		if(!hasNullInputs(key)){
			Object obj = cache.get(key);
			if (obj == null) {
				throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_FOUND, "User[key=" + key + "] not exist!");
			}
			result = ((Account)obj).isLocked();
		}
		return result;
	}
	
	/**
	 * Cleans and destroys instances of environmentCache and cache
	 */
	@Override
	public void quit() {
		super.quit(environmentCache);
		super.quit(cache);
	}
	
	/**
	 * Simple POJO class of Account to keep cached values
	 *
	 */
	public class Account implements Serializable 
    {
        private static final long serialVersionUID = 6392376146163510145L;
        
        private String accountId;
        private String serviceAccountId;
        private String billingSystem;
        private String firstName;
        private String lastName;
        private String address;
        private String phoneNumber;
        private String authGuid;
        private String zip;
        private String status;
        private String transferFlag;
        private String physicalResourceLink;
        private List<String> primaryUserIds;
        private List<String> secondaryUserIds;
        private String inputUserId;
        private String freshSsn;
        private String freshDob;
        private Set<String> lobs;
        private String accountType;
        private boolean isLocked = false;
        
        /**
         * Class constructor
         */
        
        public Account(){
        	
        }
        
        /**
         * Class constructor with parameter
         * 
         * @param accountId Account ID
         * @param billingSystem BillingSystem
         * @param firstName FirstName
         * @param lastName LastName
         * @param address Address
         * @param phoneNumber PhoneNumber
         * @param authGuid AuthGuid
         * @param zip Zip
         * @param status Status
         * @param transferFlag TransferFlag
         * @param physicalResourceLink PhysicalResourceLink
         * @param freshSsn FreshSsn
         * @param freshDob FreshDob
         * @param primaryUserIds PrimaryUserIds
         * @param secondaryUserIds secondaryUserIds
         */
        
        public Account(	String accountId,
        				String serviceAccountId,
        				String billingSystem,
        				String firstName,
        				String lastName,
        				String address,
        				String phoneNumber,
        				String authGuid,
        				String zip,
        				String status,
        				String transferFlag,
        				String physicalResourceLink,
        				String freshSsn,
        				String freshDob,
        				Set<String> lobs,
						String accountType,
						List<String> primaryUserIds,
						List<String> secondaryUserIds){
        	
        	this.accountId = accountId;
        	this.serviceAccountId = serviceAccountId;
        	this.billingSystem = billingSystem;
        	this.firstName = firstName;
        	this.lastName = lastName;
        	this.address = address;
        	this.phoneNumber = phoneNumber;
        	this.authGuid = authGuid;
        	this.zip = zip;
        	this.status = status;
        	this.transferFlag = transferFlag;
        	this.physicalResourceLink = physicalResourceLink;
        	this.freshSsn = freshSsn;
        	this.freshDob = freshDob;
        	this.lobs = lobs;
        	this.accountType = accountType;
        	this.primaryUserIds = primaryUserIds;
        	this.secondaryUserIds = secondaryUserIds;
        }

        /**
         * Get account Id from cache
         * 
         * @return accountId
         */
        
		public String getAccountId() {
			return accountId;
		}
		
		/**
		 * Get BillingSystem from cache
		 * @return billingSystem
		 */

		public String getBillingSystem() {
			return billingSystem;
		}

		/**
		 * To get first name from cache
		 * @return firstName
		 */

		public String getFirstName() {
			return firstName;
		}
		
		/**
		 * To get Last name from cache
		 * @return LastName
		 */

		public String getLastName() {
			return lastName;
		}
		
		/**
		 * To get Address from cache
		 * 
		 * @return Address
		 */

		public String getAddress() {
			return address;
		}

		/**
		 * To get PhoneNumber from cache
		 * 
		 * @return PhoneNumber
		 */
		
		public String getPhoneNumber() {
			return phoneNumber;
		}
		
		/**
		 * To get AuthGuid from cache
		 * 
		 * @return authGuid
		 */

		public String getAuthGuid() {
			return authGuid;
		}
		
		/**
		 * To get zip from cache
		 * 
		 * @return zip
		 */

		public String getZip() {
			return zip;
		}
		

		/**
		 * To get Status from cache
		 * 
		 * @return Status
		 */

		public String getStatus() {
			return status;
		}

		/**
		 * To get TransferFlag from cache
		 * 
		 * @return TransferFlag
		 */
		
		
		public String getTransferFlag() {
			return transferFlag;
		}
		
		/**
		 *To get PhysicalResourceLink from cache
		 *
		 * @return physicalResourceLink
		 */

		public String getPhysicalResourceLink() {
			return physicalResourceLink;
		}

		/**
		 * To get PrimaryUserIds from cache
		 * 
		 * @return PrimaryUserIds list
		 */
		
		public List<String> getPrimaryUserIds() {
			return primaryUserIds;
		}
		
		/**
		 * To get SecondaryUserIds from cache
		 * 
		 * @return   list of SecondaryUserIds
		 */

		public List<String> getSecondaryUserIds() {
			return secondaryUserIds;
		}

		/**
		 * To get InputUserId cache
		 * 
		 * @return InputUserId
		 */
		
		public String getInputUserId() {
			return inputUserId;
		}
		
		/**
		 * To set InputUserId into cache
		 * 
		 * @param inputUserId Input User ID
		 */

		public void setInputUserId(String inputUserId) {
			this.inputUserId = inputUserId;
		}

		/**
		 * Checks lock status of account
		 * 
		 * @return true if account is locked, else false
		 */
		
		public boolean isLocked() {
			return isLocked;
		}
		
		/**
		 * To set lock status of any account
		 * 
		 * @param isLocked
		 *        	lock status
		 */

		public void setLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}
		
		
		/**
		 * To get Fresh SSN
		 *  
		 * @return freshSsn
		 */
		
		public String getFreshSsn() {
			return freshSsn;
		}

		/**
		 * To get fresh DOB
		 * 
		 * @return freshDob
		 */
		public String getFreshDob() {
			return freshDob;
		}
	
		
		/**
		 * @return the lob
		 */
		public Set<String> getLobs() {
			return lobs;
		}

		/**
		 * @return the accountType
		 */
		public String getAccountType() {
			return accountType;
		}

		/**
		 * @return the serviceAccountId
		 */
		public String getServiceAccountId() {
			return serviceAccountId;
		}

		/**
		 * To compare value of accountId
		 * 
		 * @param obj
		 *        An object instance of Account
		 * 
		 */
		@Override
		public boolean equals(Object obj) {
			
			if(obj instanceof Account){
				Account acc = (Account)obj;
				
				if(this.accountId.equals(acc.getAccountId())){
					return true;
				}
			}
			return false;
		}
		
        
		/**
		 * To get the hash code of an object
		 * 
		 * @return a hash code value for the object.
		 */
		
		@Override
		public int hashCode() {
			return this.getAccountId().hashCode() + this.getAuthGuid().hashCode() + this.getStatus().hashCode();
		}
		
		/**
		 * To get string representation of the object
		 * 
		 * @see  String java.lang.String.format()
		 * @return Returns a string representation of the object
		 */
		
		@Override
        public String toString() {
            return String.format( "%s Account with Service Account Id %s which belongs to billing system %s has Auth GUID %s, first name %s, last name %s, address %s, phone number %s, zip code %s, transfer flag %s, physical resource link %s, primary users %s, secondary users %s, fresh SSN %s and associated dob %s. The account status is %s and it is %s",
            						accountId, serviceAccountId, billingSystem, authGuid, firstName, lastName, address, phoneNumber, zip, transferFlag, physicalResourceLink, primaryUserIds, secondaryUserIds, freshSsn, freshDob, status, (isLocked?"locked.":"not locked."));
        }
		
		/**
		 * To clone account object to update information
		 * @return instance of Account
		 */
		
		@Override
		public Account clone(){
			return new Account(this.accountId, this.serviceAccountId, this.billingSystem, this.firstName, this.lastName, this.address, this.phoneNumber, this.authGuid, this.zip, this.status, 
					this.transferFlag, this.physicalResourceLink, this.freshSsn, this.freshDob, this.lobs, this.accountType, this.primaryUserIds, this.secondaryUserIds);
		}
    }
	
	
	private CacheAccess<String, Account> cache = null;
	private CacheAccess<String, Set<String>> environmentCache = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountCache.class);
}
