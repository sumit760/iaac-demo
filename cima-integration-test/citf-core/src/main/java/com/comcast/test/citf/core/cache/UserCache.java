package com.comcast.test.citf.core.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
 * User cache is for keeping user related data.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 */
@Service("userCache")
public class UserCache extends CacheAdapter implements ICitfCache{

	private static final String REGION = "USER_CACHE";
	private static final String ENV_REGION = "USER_ENV_CACHE";
	private static final String CHANNEL_REGION = "USER_CHANNELS_CACHE";
	private static final String LOB_REGION = "USER_LOB_CACHE";
	
	/**
	 * Constructor of UserAttributesCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	
	public UserCache(){
		try{
			userCache = JCS.getInstance(REGION);
			environmentCache = JCS.getInstance(ENV_REGION);
			channelSubscriptionCache = JCS.getInstance(CHANNEL_REGION);
			lobCache = JCS.getInstance(LOB_REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while initializing Cache", e);
		}
	}


	public enum InputMapKeys{
		USER_OBJECT,
		CHANNEL_SUBSCRIPTION_MAP,
		LOB
	}

	/**
	 * Populates the user detail into cache for any specific environment
	 * 
	 * @param key
	          Key to fetch the user value
	 * @param value
	          The value which is to be populated
	 * @param environment
	          Environment under which key is to be stored
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean put(String key, Object value, String environment) {
		LOGGER.debug("### Cache population input :: key = {} and value is {}", key, (value!=null?"not null.":"null."));
		User user;
		Map<String, String> channelSubscriptions;
		Set<String> lobs;
		boolean isSucceeded = false;

		try{
			//validation....
			if(StringUtility.isStringEmpty(key) || !(value instanceof Map) || AbstractExecutionController.isInEnvironment(environment) == null) {
				LOGGER.error("Not able process put request with key {} and value {} for environment {} due to any of these is(are) null or invalid",
						key, value, environment);
				return false;
			}

			Map<InputMapKeys, Object> inputMap = (Map<InputMapKeys, Object>)value;

			if(!(inputMap.get(InputMapKeys.USER_OBJECT) instanceof User) || (inputMap.get(InputMapKeys.CHANNEL_SUBSCRIPTION_MAP)!=null && 
					!(inputMap.get(InputMapKeys.CHANNEL_SUBSCRIPTION_MAP) instanceof Map))) {
				LOGGER.error("Input value object is invalid. It must contains User and may contain list of Channel Subscription map and LOB only!!!");
				return false;
			}

			user = 	(User)inputMap.get(InputMapKeys.USER_OBJECT);
			if(StringUtility.isStringEmpty(user.getUserId()) || StringUtility.isStringEmpty(user.getPassword()) || StringUtility.isStringEmpty(user.getCategory())) {
				LOGGER.error("User object should have all mandatory values!!");
				return false;
			}

			//populating user & environment cache
			userCache.put(key, user);
			Set<String> userIds = environmentCache.get(environment)!=null ? environmentCache.get(environment) : new HashSet<String>();
			userIds.add(key);
			environmentCache.put(environment, userIds);			
			
			//checking an populating LOB cache
			if(inputMap.get(InputMapKeys.LOB) instanceof Set){
				lobs = (Set<String>) inputMap.get(InputMapKeys.LOB);
				
				for(String lob : lobs){
					Set<String> lobUserIds = lobCache.get(lob)!=null ? lobCache.get(lob) : new HashSet<String>();
					lobUserIds.add(key);
					lobCache.put(lob, lobUserIds);
				}
			}

			//populating channel cache
			if(inputMap.get(InputMapKeys.CHANNEL_SUBSCRIPTION_MAP)!=null){
				channelSubscriptions = (Map<String, String>)inputMap.get(InputMapKeys.CHANNEL_SUBSCRIPTION_MAP);
				
				for(Map.Entry<String, String> entry : channelSubscriptions.entrySet()){
					String channelName = entry.getKey();
					String channelVal = entry.getValue();
					
					if(!StringUtility.isStringEmpty(channelName) && channelVal!=null){

						Map<String, Set<String>> subscriptions = channelSubscriptionCache.get(channelName)!=null ? channelSubscriptionCache.get(channelName) : new HashMap<String, Set<String>>();
						Set<String> subscribedUsers = (Set<String>) (subscriptions.get(channelVal)==null ? new HashSet<>() : subscriptions.get(channelVal));
						subscribedUsers.add(key);
						subscriptions.put(channelVal, subscribedUsers);
						channelSubscriptionCache.put(channelName, subscriptions);
					}
					else {
						LOGGER.warn("One empty or invalid map found in the list of Channel subscription maps");
					}
				}
			}
			isSucceeded = true;
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while populating Cache", e);
		}
		return isSucceeded;
	}

	@Override
	public Object getObject(String key){
		return !hasNullInputs(key)? userCache.get(key) : null;
	}

	/**
	 * To get list of user based on given filter conditions.
	 * 
	 * @param filterConditions
	 *        Map of filter conditions where a filter condition acts as a query parameter to fetch data. It can be any of the list of possible values given below
	 *        A. Exact value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS == 'ABCD'" SQL query
	 *        B. Not null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NOT NULL" SQL query
	 *        C. null value, which acts like "ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS IS NULL" SQL query
	 *        
	 *        The possible filter condition keys should be any of the given list. Other than ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, all are optional.
	 * 			1. ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT
     * 			2. ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV
     * 			3. ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME
     * 			5. ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION
     * 			6. ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV
     * 			7. ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED
     *  		8. ICommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS
     *  		9. ICommonConstants.CACHE_FLTR_CONDTN_CATEGORY
     *         10. ICommonConstants.CACHE_FLTR_CONDTN_TV_RATING
     *         12. ICommonConstants.CACHE_FLTR_CONDTN_MOVIE_RATING
     *         13. ICommonConstants.CACHE_FLTR_CONDTN_PRIMARY_ACCOUNT_ID
     *         14. ICommonConstants.CACHE_FLTR_CONDTN_SECONDARY_ACCOUNT_ID
     *         15. ICommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS
     *         16. ICommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS
	 *  
	 *  	P.S. The mandatory filter ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT should be given in the input along with optional filter conditions (if required).
	 *        
	 * @param noOfObjectsRequired 
	 *        	No of accounts required to be fetched
	 * 
	 * @return List of user objects
	 */
	@Override
	public List<Object> getFilteredObjects(Map<String, String> filterConditions, int noOfObjectsRequired){
		LOGGER.debug("### Starting to filter with filterConditions: {} and noOfObjectsRequired: {}", filterConditions, noOfObjectsRequired);

		List<Object> objList = null;
		Set<String> userIds;

		if(filterConditions==null || filterConditions.isEmpty() || filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT)==null) {
			LOGGER.error("Not able process getFilteredObjects request with filterConditions {} due to filterConditions is(are) null or invalid", filterConditions);
			return null;
		}

		//environment filtering
		userIds = new HashSet<String>(environmentCache.get(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT)));

		//User Id filtering
		if(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV)!=null){
			Set<String> uids = StringUtility.getTokensFromString(filterConditions.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV), ICommonConstants.COMMA);
			Set<String> filteredIds = new HashSet<>();
			for(String uid : uids){
				if(userIds.contains(uid)){
					filteredIds.add(uid);
				}
			}
			userIds = null;
			if(filteredIds.size()>0) {
				userIds = filteredIds;
			}
		}

		//channel subscription filtering (inclusive)
		if(userIds!=null && (filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME)!=null || filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION)!=null)){
			Map<String, Set<String>> subscriptions = channelSubscriptionCache.get(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME));
			if(subscriptions==null){
				LOGGER.warn("Channel Subscription list is empty for {}", filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME));
				userIds = null;
			}
			else{
				Set<String> subscribedUserIds = new HashSet<>();

				if(!userIds.isEmpty()){
					for(String uid: userIds) {
						if (subscriptions.get(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION)) != null && subscriptions.get(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION)).contains(uid)) {
							subscribedUserIds.add(uid);
						}
					}
				}

				userIds.clear();
				userIds.addAll(subscribedUserIds);
			}
		}


		//lob filtering
		if(userIds!=null && !userIds.isEmpty() && filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV)!=null){
			Set<String> inputLobs = StringUtility.getTokensFromString(filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV), ICimaCommonConstants.COMMA);
			Set<String> entitledUserIds = new HashSet<>();

			//Filtering with given lob options (inclusive)
			int lobIndex = 0;
			for(String lob : inputLobs) {
				if(lobCache.get(lob)==null || lobCache.get(lob).isEmpty()){
					entitledUserIds.clear();
					break;
				}
				else {
					if(lobIndex == 0){
						for(String uid: userIds){
							if(lobCache.get(lob).contains(uid)){
								entitledUserIds.add(uid);
							}
						}
					}
					else{
						if(!entitledUserIds.isEmpty()){
							Set<String> removableUserIds = new HashSet<>();
							for(String uid: entitledUserIds){
								if(!lobCache.get(lob).contains(uid)){
									removableUserIds.add(uid);
								}
							}
							if(!removableUserIds.isEmpty()){
								entitledUserIds.removeAll(removableUserIds);
							}
						}
						else{
							break;
						}
					}
					
				}
				lobIndex++;
			}

			userIds.clear();
			if(!entitledUserIds.isEmpty()){
				userIds.addAll(entitledUserIds);
			}
		}

		//user filtering (inclusive)
		if(userIds!=null && !userIds.isEmpty()){
			boolean unlockFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED)!=null;
			String loginStatusFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS);
			String categoryFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_CATEGORY);
			String tvRatingFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_TV_RATING);
			String movieRatingFilter = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_MOVIE_RATING);
			String primaryAccountFiler = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_PRIMARY_ACCOUNT_ID);
			String secondaryAccountFiler = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_SECONDARY_ACCOUNT_ID);
            String hasMultiplePrimaryAccountsFiler = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS);
            String hasMultipleSecondaryAccountsFiler = filterConditions.get(ICommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS);

			objList = new ArrayList<>();
			int count = 1;

			for(String key : userIds){
				User user = userCache.get(key);

				if(noOfObjectsRequired != ICommonConstants.QUANTITY_UNLIMITED && count>noOfObjectsRequired) {
					break;
				}

				if( 	(!unlockFilter || !user.isLocked()) &&
						(strictFilter(loginStatusFilter, user.getLoginStatus())) &&
						((categoryFilter!=null && (categoryFilter.equals(user.getCategory()) || ICimaCommonConstants.USER_CATEGORY_ALL.equalsIgnoreCase(user.getCategory()))) || categoryFilter==null) &&
						(strictFilter(tvRatingFilter, user.getTvRating())) &&
						(strictFilter(movieRatingFilter, user.getMovieRating())) &&
						(nonStrictLoopFilter(primaryAccountFiler, user.getPrimaryAccountIds())) &&
						(nonStrictLoopFilter(secondaryAccountFiler, user.getSecondaryAccountIds())) &&
                        (nonStrictLoopMultiValueCheckFilter(hasMultiplePrimaryAccountsFiler, user.getPrimaryAccountIds(), 2)) &&
                        (nonStrictLoopMultiValueCheckFilter(hasMultipleSecondaryAccountsFiler, user.getSecondaryAccountIds(), 2))){

					User cUser = user.clone();
					objList.add(cUser);
					count++;
				}
			}
		}

		LOGGER.info("### Cache filtration is over and output contains {} users.", (objList!=null?objList.size():"0"));
		return objList;
	}
	
	
	/**
	 * To change lock status of any user in Cache
	 * 
	 * @param key 
	 *        Key to get user object
	 * @param lockValue
	 *        true or false based on user to lock or unlock
	 * 
	 */	

	@Override
	public synchronized void changeLock(String key, boolean lockValue) throws LockException{
		Object obj = userCache.get(key);
		if(obj==null) {
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_FOUND, "User[key=" + key + "] not exist!");
		}

		User usr = (User)obj;

		if(lockValue && usr.isLocked()) {
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_ALREADY_LOCKED, "User[key=" + key + "] is already locked!");
		}
		else if(!lockValue && !usr.isLocked()) {
			throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_LOCKED, "User[key=" + key + "] is not locked!");
		}

		usr.setLocked(lockValue);
		LOGGER.info("User[key={}] has been {}", key, (lockValue?"locked":"unlocked"));
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
			Object obj = getObject(key);
			if (obj == null) {
				throw new LockException(LockException.EXCECPTION_TYPE_OBJECT_NOT_FOUND, "User[key=" + key + "] not exist!");
			}
			result = ((User)obj).isLocked();
		}
		return result;
	}
	
	/**
	 * To get password from cache
	 * 
	 * @param userId 
	 * 			User ID
	 * @return password 
	 */

	public String getPassword(String userId) {
		String result = null;
		if(!hasNullInputs(userId)){
			Object obj = getObject(userId);
			if (obj == null) {
				throw new IllegalStateException("User[key=" + userId + "] not exist!");
			}
			result = ((User)obj).getPassword();
		}
		return result;
	}

	/**
	 * To set password into cache
	 * @param userId
	 * 			User id
	 * @param password
	 * 			Password
	 */
	public void setPassword(String userId, String password){
		if(!hasNullInputs(userId)){
			Object obj = getObject(userId);
			if (obj == null) {
				throw new IllegalStateException("User[key=" + userId + "] not exist!");
			}

			((User)obj).setPassword(password);
		}
	}


	/**
	 * Cleans and destroys instances of channelSubscriptionCache , serviceEntitlementCache and userCache
	 * 
	 */
	@Override
	public void quit() {
		super.quit(channelSubscriptionCache);
		super.quit(lobCache);
		super.quit(environmentCache);
		super.quit(userCache);
	}
	
	/**
	 * Simple POJO class of user to keep cached values
	 *
	 */

	public class User implements Serializable
    {
        private static final long serialVersionUID = 6392376146163510146L;

        private String userId = null;
        private String password = null;
        private String category = null;
        private String pin = null;
        private String tvRating = null;
        private String movieRating = null;
        private String loginStatus = null;
        private Set<String> primaryAccountIds = null;
        private Set<String> secondaryAccountIds = null;
        private boolean isLocked = false;
        
        /**
         * Class constructor      
         */
        public User(String userId, String password, String category, String pin, String tvRating, String movieRating, String loginStatus,
					Set<String> primaryAccountIds, Set<String> secondaryAccountIds){
        	this.userId = userId;
        	this.password = password;
        	this.category = category;
        	this.pin = pin;
        	this.tvRating = tvRating;
        	this.movieRating = movieRating;
        	this.loginStatus = loginStatus;
        	this.primaryAccountIds = primaryAccountIds;
        	this.secondaryAccountIds = secondaryAccountIds;
        }

        /**
         * To get UserID from cache
         * @return userId
         */
		public String getUserId() {
			return userId;
		}
		
		/**
		 * To set user ID into cache
		 * @param userId 
		 * 			UserID
		 */

		public void setUserId(String userId) {
			this.userId = userId;
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
		 * To get category from cache
		 * @return category
		 */

		public String getCategory() {
			return category;
		}

		/**
		 * To set category
		 * @param category 
		 * 			Category
		 */
		public void setCategory(String category) {
			this.category = category;
		}
		/**
		 * To get pin from cache
		 * @return pin
		 */

		public String getPin() {
			return pin;
		}
		/**
		 * To set PIN into cache
		 * @param pin 
		 * 			PIN
		 */

		public void setPin(String pin) {
			this.pin = pin;
		}

		/**
		 * To get TV Rating from cache
		 * @return tvRating
		 */
		public String getTvRating() {
			return tvRating;
		}

		/**
		 * To set TV Rating into cache 
		 * @param tvRating 
		 * 			TV Rating
		 */
		public void setTvRating(String tvRating) {
			this.tvRating = tvRating;
		}

		/**
		 * To get Movie Rating from cache
		 * 
		 * @return movieRating
		 */
		public String getMovieRating() {
			return movieRating;
		}
		/**
		 * To set Movie rating into cache
		 * 
		 * @param movieRating  
		 * 			Rating of Movie
		 */

		public void setMovieRating(String movieRating) {
			this.movieRating = movieRating;
		}

		/**
		 * checks User lock status
		 * @return isLocked
		 */
		public boolean isLocked() {
			return isLocked;
		}

		/**
		 * Method to set lock for user into cache
		 * @param isLocked 
		 *        User lock status
		 */
		public void setLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}
		
		/**
		 * Method to get login status from cache
		 * @return loginStatus
		 */

		public String getLoginStatus() {
			return loginStatus;
		}
		
		/**
		 * To set login status into cache
		 * 
		 * @param loginStatus
		 *        	Login status of an USer
		 */

		public void setLoginStatus(String loginStatus) {
			this.loginStatus = loginStatus;
		}
		
		/**
		 * To get primary account IDs form cache
		 * 
		 * @return set of primaryAccountIds
		 */

		public Set<String> getPrimaryAccountIds() {
			return primaryAccountIds;
		}

		/**
		 * To set primary account IDs into cache
		 * 
		 * @param primaryAccountIds
		 * 			set of Primary account ids
		 */
		public void setPrimaryAccountIds(Set<String> primaryAccountIds) {
			this.primaryAccountIds = primaryAccountIds;
		}

		/**
		 * To get secondary account IDs form cache
		 * 
		 * @return set of secondaryAccountId
		 */
		public Set<String> getSecondaryAccountIds() {
			return secondaryAccountIds;
		}
		
		/**
		 * To set secondary Account IDs into cache
		 * @param secondaryAccountIds
		 * 			set of Secondary account id
		 */

		public void setSecondaryAccountIds(Set<String> secondaryAccountIds) {
			this.secondaryAccountIds = secondaryAccountIds;
		}

		/**
		 * To compare user ID
		 * 
		 * @return boolean true or false
		 */
		@Override
		public boolean equals(Object obj) {

			if(obj instanceof User){
				User user = (User)obj;

				if(this.userId.equals(user.getUserId()) && this.isLocked() == user.isLocked()) {
					return true;
				}
			}
			return false;
		}

		/**To clone account object to make some modification
		 * 
		 * @return instance of account
		 */
		
		@Override
		public int hashCode() {
			return this.getUserId().hashCode() + this.getPassword().hashCode() + this.getCategory().hashCode();
		}

		/**
		 * To get string representation of the object
		 * 
		 * @see  String java.lang.String.format
		 * @return Returns a string representation of the object
		 */
		
		@Override
        public String toString() {
            return String.format( "%s User which belongs to Category %s has primary accounts %s, secondary accounts %s, Login Status %s, TV Rating %s and Movie Rating %s. The user is %s",
                    userId, category, (primaryAccountIds!=null?primaryAccountIds.toString():null), (secondaryAccountIds!=null?secondaryAccountIds.toString():null), loginStatus, tvRating, movieRating, this.isLocked()?"locked":"not locked");
        }

		/**
		 * To clone user object to update information
		 * 
		 * @return instance of account
		 */
		
		@Override
		public User clone(){
			return new User(this.userId, this.password, this.category, this.pin, this.tvRating, this.movieRating, this.loginStatus,
					this.primaryAccountIds, this.secondaryAccountIds);
		}
    }

	private CacheAccess<String, User> userCache = null;
	private CacheAccess<String, Set<String>> environmentCache = null;
	private CacheAccess<String, Map<String, Set<String>>> channelSubscriptionCache = null;	
	private CacheAccess<String, Set<String>> lobCache = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserCache.class);
}
