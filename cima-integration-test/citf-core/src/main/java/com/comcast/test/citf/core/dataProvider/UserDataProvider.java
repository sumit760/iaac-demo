package com.comcast.test.citf.core.dataProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;

/**
 *
 * @author Abhijit Rej
 * @since April 2016
 *
 * This is data provider to provide User and related data.
 *
 */
@Service("userDP")
public class UserDataProvider extends AbstractDataProvider{

    public enum Filter{
    	ACCOUNT_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS),
    	ADDRESS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ADDRESS),
    	ALTERNATE_MAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL),
        CHANNEL_SUBSCRIPTION(ICimaCommonConstants.CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION),
        CHANNEL_NAME(ICimaCommonConstants.CACHE_FLTR_CONDTN_CHANNEL_NAME),
        DOB(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB),
        EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL),
        FACE_BOOK_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID),
        FACE_BOOK_ID_SAME_AS_ALTER_EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL),
        FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL),
        FRESH_ACCOUNT_SSN(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN),
        HAS_MULTIPLE_PRIMARY_ACCOUNTS(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS),
        HAS_MULTIPLE_SECONDARY_ACCOUNTS(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS),
        LOBS_CSV(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV),
        LOGIN_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS),
        MOVIE_RATING(ICimaCommonConstants.CACHE_FLTR_CONDTN_MOVIE_RATING),
        PRIMARY_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_PRIMARY_ACCOUNT_ID),
        PHONE(ICimaCommonConstants.CACHE_FLTR_CONDTN_PHONE),
        SECONDARY_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_SECONDARY_ACCOUNT_ID),
        SECRET_QUESTION(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION),
        SERVICE_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID),
        SSN(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN),
        TV_RATING(ICimaCommonConstants.CACHE_FLTR_CONDTN_TV_RATING),
		USER_IDS_CSV(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV),
		USER_ROLE(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE);

        private final String value;
        Filter(final String value) {
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    public enum UserCategory{
        LOGIN(ICimaCommonConstants.USER_CATEGORY_LOG_IN),
        IDM(ICimaCommonConstants.USER_CATEGORY_IDM);

        private final String value;
        UserCategory(final String value) {
            this.value = value;
        }
        public String getValue(){
            return this.value;
        }
    }

    /**
     * This provides User object from UserCache based on the inputs. 
     * <b>IMPORTANT</b>: The fetched user may not be a dedicated one, i.e. <b>the user will not be locked</>.
     * 
     * @param filters
     * 			Map of filter condition which will be used to filter out the valid user, <b>This can be null</b>
     * @param reuseOldUser
     * 			Boolean flag, TRUE means it'll check and return same user(if any) which has been fetched earlier by same thread
     * 						  FALSE means it'll always go to fetch new user
     * @param userCategory
     * 			The category of user
     * @return user
     */
    public UserCache.User fetchUser(Map<Filter, String> filters, boolean reuseOldUser, UserCategory userCategory){
        LOGGER.debug("Starting to fetch user with filter {}, reuse {} for category {}", filters!=null? 
        										Arrays.toString(filters.entrySet().toArray()):null, reuseOldUser, userCategory);
        UserCache.User user = null;

        if(reuseOldUser && LAST_USER.get()!=null){
            LOGGER.info("Last fetched user {} found, will use the same.", LAST_USER.get().getUserId());
            user = LAST_USER.get();
        }
        else if(userCategory != null){
            try{
            	Map<String, String> userFilterConditions = new HashMap<String, String>();
                userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, getCurrentEnvironment());
                userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_CATEGORY, userCategory.getValue());

                populateFilterMap(filters, userFilterConditions);

                //This will fetch 10 users and shuffles to select a random one
                List<Object> usrObjList = getUserCache().getFilteredObjects(userFilterConditions, 10);
                if(usrObjList!=null && !usrObjList.isEmpty()){
                    Collections.shuffle(usrObjList);
                    user = (UserCache.User)usrObjList.get(0);
                    LAST_USER.set(user);
                }
            } catch(Exception e){
                LOGGER.error("Error occurred while fetching user ", e);
            }
        }
        else{
            LOGGER.error("Can not fetch user as the user category is null.");
        }
        LOGGER.debug("User fetched: {}", user);
        return user;
    }

    /**
     * This provides UserDetails (which might contains User, Account and UserAttribute) based on the inputs. This should be typically used when a test needs
     * to fetch user along with associated account and user attribute details
     * <b>IMPORTANT</b>: The fetched user may be a dedicated one if and only if needToLockUser is TRUE, i.e. <b>the user may be locked</>.
     * 
     * @param filters
     * 			Map of filter condition which will be used to filter out the valid user, <b>This can be null</b>
     * @param reuseOldUser
     * 			Boolean flag, TRUE means it'll check and return same user(if any) which has been fetched earlier by same thread
     * 						  FALSE means it'll always go to fetch new user
     * @param userCategory
     * 			The category of user
     * @param needToLockUser
     * 			Boolean flag, TRUE means it'll lock the user, i.e. it can not be used by any other test same time until and unless 
     * 								it'll be explicitly unlocked by the invoked test case
     * 						  FALSE means it'll not lock the fetched user
     * @return user details (which might contains User, Account and UserAttribute)
     */
    public UserObjects fetchUserDetails(Map<Filter, String> filters, boolean reuseOldUser, UserCategory userCategory, boolean needToLockUser){
    	  LOGGER.debug("Starting to fetch user details with filter {}, reuse {} for category {} and lock needed {}", 
    			  						filters!=null? Arrays.toString(filters.entrySet().toArray()):null, reuseOldUser, userCategory, needToLockUser);
        if(reuseOldUser && LAST_USER_DTLS.get()!=null){
            LOGGER.info("Last fetched user details with user id  {} found, will use the same.", 
            					LAST_USER_DTLS.get().getUser()!=null?LAST_USER_DTLS.get().getUser().getUserId():null);
            return LAST_USER_DTLS.get();
        }
        UserObjects userDtls = null;

        Map<String, String> filterConditions = new HashMap<String, String>();
        populateFilterMap(filters, filterConditions);
        filterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, getCurrentEnvironment());
        filterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_CATEGORY, userCategory!=null ? userCategory.getValue() : UserCategory.IDM.getValue());
        //By default it'll check for unlocked user
        filterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

        long maxLockWaitingTime = 180000;
        try {
            maxLockWaitingTime = configDataProvider.getMaxWaitingTimeToAcquireLock();
        } catch (Exception e) {
            LOGGER.warn("Not able to fetch MAX_WAITING_TIME_TO_ACQUIRE_LOCK value from configuration cache due to ", e);
        }

        long totalWaitingTime = 0;
        while(totalWaitingTime<=maxLockWaitingTime) {
            userDtls = this.getUser(filterConditions, needToLockUser);
            if(userDtls!=null){
                break;
            }
            else{
                try {
                    TimeUnit.MILLISECONDS.sleep(delteLockWaitingTime);
                } catch (InterruptedException e) {
                	LOGGER.debug("InterruptedException occured while waiting ", e);
                }
                totalWaitingTime = totalWaitingTime + delteLockWaitingTime;
            }
        }
        LOGGER.debug("User details fetched: {}", userDtls);
        return userDtls;
    }
    
    /**
     * Unlocks user which has been locked
     * @param userId
     * @throws LockException
     */
    public void unlockUser(String userId) throws LockException{
    	getUserCache().changeLock(userId, false);
    }


    public static class UserObjects{
        private UserCache.User user;
        private UserAttributesCache.Attribute userAttr;
        private AccountCache.Account account;

        protected UserObjects(UserCache.User user, UserAttributesCache.Attribute userAttr, AccountCache.Account account){
            this.user = user;
            this.userAttr = userAttr;
            this.account = account;
        }

        public UserCache.User getUser() {
            return user;
        }

        public UserAttributesCache.Attribute getUserAttr() {
            return userAttr;
        }

        public AccountCache.Account getAccount() {
            return account;
        }
    }
    

    private static final ThreadLocal<UserCache.User> LAST_USER = new ThreadLocal<UserCache.User>();
    private static final ThreadLocal<UserObjects> LAST_USER_DTLS = new ThreadLocal<UserObjects>();
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDataProvider.class);


    private void populateFilterMap(Map<Filter, String> filters, Map<String, String> userFilterConditions){
        if(filters != null){
            for(Map.Entry<Filter, String> filer : filters.entrySet()){
                userFilterConditions.put(filer.getKey().getValue(), filer.getValue());
            }
        }
    }

    private synchronized UserObjects getUser(Map<String, String> filter, boolean needToLockUser) {
        UserObjects result = null;
        try {
        	Map<String, UserAttributesCache.Attribute> userAttrMap;
            String userIdsCsv;

            //fetching user data
            List<Object> usrObjList = getUserCache().getFilteredObjects(filter, ICommonConstants.QUANTITY_UNLIMITED);
            if (isListEmpty(usrObjList, "No user found from User cache!")) {
               return null;
            }

            StringBuilder sbf = new StringBuilder();
            for (Object usrObj : usrObjList) {
                if (sbf.length() > 0) {
                    sbf.append(ICimaCommonConstants.COMMA);
                }

                UserCache.User usr = (UserCache.User) usrObj;
                sbf.append(usr.getUserId());
            }
            userIdsCsv = sbf.toString();
            filter.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV, userIdsCsv);
            checkFaceBookFilter(filter);

            List<Object> usrAttrObjs = getUserAttributesCache().getFilteredObjects(filter, ICommonConstants.QUANTITY_UNLIMITED);
            if (isListEmpty(usrAttrObjs, "No user found from User Attributes cache!")) {
                return null;
            }

            sbf = new StringBuilder();
            userAttrMap = new HashMap<String, UserAttributesCache.Attribute>();
            for (Object usrObj : usrAttrObjs) {
                if (sbf.length() > 0) {
                    sbf.append(ICimaCommonConstants.COMMA);
                }

                UserAttributesCache.Attribute attr = (UserAttributesCache.Attribute) usrObj;
                sbf.append(attr.getUserId());
                userAttrMap.put(attr.getUserId(), attr);
            }
            userIdsCsv = sbf.toString();

            filter.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV, userIdsCsv);
            List<Object> accountObjs = getAccountCache().getFilteredObjects(filter, ICommonConstants.QUANTITY_UNLIMITED);
            if (isListEmpty(accountObjs, "No user found from Accounts!")) {
                return null;
            }

            result = chooseAndLockUserObjects(accountObjs, userAttrMap, needToLockUser);
        } catch (Exception e) {
        	 LOGGER.error("Not able to fetch user due to : ", e);
        }
        return result;
    }


     private UserObjects chooseAndLockUserObjects(  List<Object> accountObjs,
                                                    Map<String, UserAttributesCache.Attribute> userAttrMap,
                                                    boolean needToLockUser) {
         UserObjects usrObjs = null;

         Collections.shuffle(accountObjs);
         for (Object obj : accountObjs) {
             if (obj instanceof AccountCache.Account) {
                 AccountCache.Account account = (AccountCache.Account) obj;
                 String usrId = account.getInputUserId();

                 boolean isUserValid = true;
                 if(needToLockUser) {
                     isUserValid = false;
                     try {
                         getUserCache().changeLock(usrId, true);
                         isUserValid = true;
                     } catch (LockException ale) {
                         LOGGER.warn("Not able to lock user {} due to : ", usrId, ale);
                     }
                 }

                 if (isUserValid) {
                     usrObjs = new UserObjects((UserCache.User)getUserCache().getObject(usrId),  userAttrMap.get(usrId), account);
                     break;
                 }
             }
         }
         return usrObjs;
     }  
     
     
     private boolean isListEmpty(List<Object> list, String failWarningMessage){
    	 boolean isEmpty = false;
    	 if (list == null || list.isEmpty()) {
             LOGGER.warn(failWarningMessage);
             isEmpty = true;
         }
    	 return isEmpty;
     }
     
     private void checkFaceBookFilter(Map<String, String> filter){
    	//By default data provider will not provide any user who has valid Facebook account (due to lack of FB accounts) until and unless it has been mentioned here
         if (filter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID) == null && 
        		 filter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL) == null && 
        		 filter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL) == null) {
             filter.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
         }
     }
     
     @Autowired
     private ConfigurationDataProvider configDataProvider;
     
     private long delteLockWaitingTime = 60000;
     
     //This method has been created only to facilitate unit testing
     public void setLockWaitingTime(long delteLockWaitingTime){
    	 this.delteLockWaitingTime = delteLockWaitingTime;
     }
}
