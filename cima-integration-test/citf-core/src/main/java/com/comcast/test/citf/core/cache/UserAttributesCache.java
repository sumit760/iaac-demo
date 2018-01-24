package com.comcast.test.citf.core.cache;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * User attribute cache is for keeping user attribute related data.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 */
@Service("userAttributesCache")
public class UserAttributesCache extends CacheAdapter implements ICitfCache{

	private static final String ATTRIBUTES_CACHE = "USER_ATTRIBUTES_CACHE";
	
	/**
	 * Constructor of UserAttributesCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	
	public UserAttributesCache(){
		try{
			cache = JCS.getInstance(ATTRIBUTES_CACHE);
		}
		catch(CacheException e){
			LOGGER.error("Error occured while initializing Cache : ", e);
		}
	}
	
	/**
	 * Populates the user attributes detail into cache
	 * 
	 * @param key
	          Key to fetch the user attributes value
	 * @param value
	          The value which is to be populated
	 */
	
	@Override
	public boolean put(String key, Object value) {
		LOGGER.debug("### Cache population input :: key = {} and value is {}", key, (value!=null?"not null.":"null."));
		boolean isSucceeded = false;
		
		try{
			if(StringUtility.isStringEmpty(key) || value == null || !(value instanceof Attribute)){
				LOGGER.error("Not able process getString request with key {} and value {} due to any of these is(are) null or inavlid", 
						key, value);
				return false;
			}
			
			Attribute attrs = (Attribute)value;
			if(StringUtility.isStringEmpty(attrs.getGuid()) || StringUtility.isStringEmpty(attrs.getUserId())){
				LOGGER.error("Input User Attributes object should have all mandatory values!!");
			}
				
			cache.put(key, attrs);
			isSucceeded = true;
		}
		catch(CacheException e){
			LOGGER.error("Error occured while populating Cache : ", e);
		}
		return isSucceeded;
	}
	
	/**
	 * To get list of user attributes based on given filter conditions.
	 * 
	 * @param filterConditions
	 *        Map of filter conditions where a filter condition acts as a query parameter to fetch data. It can be any of the list of possible values given below
	 *        A. Exact value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS == 'ABCD'" SQL query
	 *        B. Not null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NOT NULL" SQL query
	 *        C. null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NULL" SQL query
	 *        
	 *        The possible filter condition keys should be any of the given list. Other than ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, all are optional.
	 * 			1. ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL
	 * 			2. ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL
	 * 			3. ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION
	 * 			5. ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID
	 * 			6. ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN
	 * 			7. ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV
	 *  
	 *  	   P.S. The mandatory filter ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT should be given in the input along with optional filter conditions (if required).
	 *        
	 * @param noOfObjectsRequired 
	 *        No of user attributes required to be fetched
	 * 
	 * @return List of user attributes objects
	 */
	
	@Override
	public List<Object> getFilteredObjects(Map<String, String> filterConditions, int noOfObjectsRequired){
		LOGGER.debug("### Starting to filter with filterConditions : {} and noOfObjectsRequired {}", filterConditions, noOfObjectsRequired);
		
		List<Object> objList = null;
		String emailFilter = null;
		String alterEmailFilter = null;
		String secQAFilter = null;
		String facebookFilter = null;
		String ssnFilter = null;
		String fbSameAsAlterEmailFilter = null;
		String fbNotSameAsAlterEmailFilter = null;
		Set<String> userIds = null;
		
		if(filterConditions!=null){
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL)!=null){
				emailFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL);
			}		
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL)!=null){
				alterEmailFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION)!=null){
				secQAFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID)!=null){
				facebookFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN)!=null){
				ssnFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV)!=null){
				userIds = StringUtility.getTokensFromString(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV), ICommonConstants.COMMA);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL)!=null) {
				fbSameAsAlterEmailFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL);
			}
			if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL)!=null) {
				fbNotSameAsAlterEmailFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL);
			}
		}
		
		int count = 0;
		for(String uid : cache.getCacheControl().getKeySet()){
			Attribute attr = cache.get(uid); 
			
			if(objList == null)
				objList = new ArrayList<Object>();
			
			if(fbSameAsAlterEmailFilter != null || fbNotSameAsAlterEmailFilter != null){
				boolean isCheckPassed = false;
				if(attr.getAlterEmail()!=null && attr.getFbId()!=null){
					String alterEmail = attr.getAlterEmail();
					String fbId = attr.getFbId();
					
					if((fbSameAsAlterEmailFilter != null && alterEmail.equalsIgnoreCase(fbId)) ||
							(fbNotSameAsAlterEmailFilter != null && !alterEmail.equalsIgnoreCase(fbId))){
						isCheckPassed = true;
					}
				}
				
				if(!isCheckPassed){
					continue;
				}
			}
			
			if( 	(nonStrictFilter(emailFilter, attr.getEmail())) &&
					(nonStrictFilter(alterEmailFilter, attr.getAlterEmail())) &&
					(nonStrictFilter(secQAFilter, attr.getSecretQuestion())) &&
					(nonStrictFilter(facebookFilter, attr.getFbId())) &&
					(nonStrictFilter(ssnFilter, attr.getSsn())) && 
					((userIds!=null && userIds.contains(attr.getUserId())) || userIds == null)){
				
				Attribute attribute = attr.clone();
				objList.add((Object)attribute);
				count++;
			}
			
			if(noOfObjectsRequired!=ICommonConstants.QUANTITY_UNLIMITED && count>=noOfObjectsRequired){
				break;
			}
		}
		
		LOGGER.info("### Cache filteration is over and output contains {} user attributes.", (objList!=null?objList.size():"0"));
		return objList;
	}
	
	
	/**
	 * To get object from cache
	 * 
	 * @param key
	 *         Key to fetch the object
	 * 
	 * @return object
	 */
	
	
	@Override
	public Object getObject(String key){
		return !hasNullInputs(key)? cache.get(key) : null;
	}
	
	/**
	 * Cleans and destroys instance of cache 
	 */	
	
	@Override
	public void quit() {
		super.quit(cache);
	}
	
	/**
	 * Simple POJO class of user attributes to keep cached values
	 *
	 */
	
	
	public class Attribute implements Serializable 
    {
        private static final long serialVersionUID = 6392376146163510145L;
        
        private String userId;
        private String guid;
        private String email;
        private String alterEmail;
        private String alterEmailPassword;
        private String secretQuestion;
        private String secretAnswer;
        private String fbId;
        private String fbPassword;
        private String ssn;
        private String password;
        private Date dob;
        
        /**
         * Class constructor
         */
        
        public Attribute(	String guid, 
        					String userId,
        					String email, 
        					String alterEmail, 
        					String alterEmailPassword,
        					String secretQuestion,
        					String secretAnswer,
        					String fbId,
        					String fbPassword,
        					Date dob,
        					String ssn){
        	
        	this.guid = guid;
        	this.userId = userId;
        	this.email = email;
        	this.alterEmail = alterEmail;
        	this.alterEmailPassword = alterEmailPassword;
        	this.secretQuestion = secretQuestion;
        	this.secretAnswer = secretAnswer;
        	this.fbId = fbId;
        	this.fbPassword = fbPassword;
        	this.dob = dob;
        	this.ssn = ssn;
        }
        
        /**
         * To get Guid from cache
         * 
         * @return GUID
         */
        
		public String getGuid() {
			return guid;
		}
		
		/**
		 * To set Guid into cache
		 * 
		 * @param guid 
		 * 			GUID
		 */

		public void setGuid(String guid) {
			this.guid = guid;
		}

		/**
		 * To get user id from cache
		 * 
		 * @return user id
		 */
		
		public String getUserId() {
			return userId;
		}
		
		/**
		 * To set userid into cache
		 * @param userId User ID
		 */

		public void setUserId(String userId) {
			this.userId = userId;
		}

		/**
		 * To get Email from cache
		 * @return email
		 */
		public String getEmail() {
			return email;
		}
		
		/**
		 * To set Email into cache
		 * 
		 *@param email 
		 *			Email
		 */

		public void setEmail(String email) {
			this.email = email;
		}
		/**
		 * To get AlterEmail from cache
		 * 
		 * @return ALternate Email
		 */

		public String getAlterEmail() {
			return alterEmail;
		}
		
		/**
		 * To set alternate Email  into cache
		 * 
		 * @param alterEmail 
		 * 			ALternate Email
		 */

		public void setAlterEmail(String alterEmail) {
			this.alterEmail = alterEmail;
		}
		
		/**
		 * To get alternate Email Password from cache
		 * @return Password of Alternate Email
		 */

		public String getAlterEmailPassword() {
			return alterEmailPassword;
		}

		/**
		 * To set alternate Email password into cache
		 * 
		 * @param alterEmailPassword 
		 * 			Password of Alternate Email
		 */
		public void setAlterEmailPassword(String alterEmailPassword) {
			this.alterEmailPassword = alterEmailPassword;
		}

		/**
		 * To get secret question from cache
		 * 
		 * @return Secret question
		 */
		public String getSecretQuestion() {
			return secretQuestion;
		}

		/**
		 * To set secret question into cache
		 * 
		 * @param secretQuestion 
		 * 			Secret question
		 */
		
		public void setSecretQuestion(String secretQuestion) {
			this.secretQuestion = secretQuestion;
		}

		/**
		 *To get secret answer from cache
		 *
		 * @return Answer of secret question
		 */
		public String getSecretAnswer() {
			return secretAnswer;
		}

		/**
		 * To set secret Answer
		 * 
		 * @param secretAnswer 
		 * 			Answer of secret question
		 */
		public void setSecretAnswer(String secretAnswer) {
			this.secretAnswer = secretAnswer;
		}

		/**
		 * To get Facebook user id from cache
		 * @return Facebook user id
		 */
		public String getFbId() {
			return fbId;
		}
		
		/**
		 * To set Facebook user id into cache
		 * @param fbId facebook user id
		 */

		public void setFbId(String fbId) {
			this.fbId = fbId;
		}

		/**
		 * To get Facebook password from cache
		 * @return Facebook Password
		 */
		public String getFbPassword() {
			return fbPassword;
		}

		/**
		 * To set facebook password into cache
		 * 
		 * @param fbPassword 
		 * 			Facebook Password
		 */
		public void setFbPassword(String fbPassword) {
			this.fbPassword = fbPassword;
		}
		
		/**
		 *To get SSN from cache
		 * @return SSN
		 */

		public String getSsn() {
			return ssn;
		}

		/**
		 * To set SSN into cache
		 * @param ssn 
		 * 			SSN
		 */
		public void setSsn(String ssn) {
			this.ssn = ssn;
		}
		
		/**
		 * To get password from cache
		 * @return password
		 */
		
		public String getPassword() {
			return password;
		}
		
		/**
		 * To set password into cache
		 * @param password 
		 * 			Password
		 */

		public void setPassword(String password) {
			this.password = password;
		}
		
		/**
		 * To get DOB form cache
		 * @return dob
		 */
		public Date getDob() {
			return dob;
		}

		/**
		 * To set DOB into cache
		 * @param dob
		 * 			Date of Birth
		 */
		
		public void setDob(Date dob) {
			this.dob = dob;
		}

		/**
		 * To compare userid
		 * @param obj
		 *        Instance of an object
		 */
		
		@Override
		public boolean equals(Object obj) {
			
			if(obj instanceof Attribute){
				Attribute attr = (Attribute)obj;
				
				if(this.guid.equals(attr.getGuid()) && this.userId.equals(attr.getUserId())){
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
			return this.getGuid().hashCode() + this.getUserId().hashCode();
		}
		
		
		/**
		 * To get string representation of the object
		 * 
		 * @see  String java.lang.String.format
		 * @return a string representation of the object
		 */
		
		@Override
        public String toString() {
            return String.format( "User Attributes[guid %s] belongs to user %s and it has email %s, alterEmail %s, date of birth %s, SSN %s, secret question %s and facbook Id %s", 
            		this.guid, this.userId, this.email, this.alterEmail, this.dob, this.ssn, this.secretQuestion, this.fbId);
        }
		
		/**To clone account object to update information
		 * @return instance of account
		 */
		
		@Override
		public Attribute clone(){
			return new Attribute(this.guid, this.userId, this.email, this.alterEmail, this.alterEmailPassword, this.secretQuestion, 
					this.secretAnswer, this.fbId, this.fbPassword, this.dob, this.ssn);
		}
    }
	
	private CacheAccess<String, Attribute> cache = null;
	
	private static Logger LOGGER = LoggerFactory.getLogger(UserAttributesCache.class);
}
