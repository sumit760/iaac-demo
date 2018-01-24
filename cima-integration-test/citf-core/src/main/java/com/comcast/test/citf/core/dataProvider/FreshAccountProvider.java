package com.comcast.test.citf.core.dataProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;

/**
*
* @author Abhijit Rej
* @since May 2016
*
* This is data provider to provide fresh account which does not have any user.
*
*/
@Service("freshAccountDP")
public class FreshAccountProvider extends AbstractDataProvider{

	 public enum Filter{
	    	ACCOUNT_TYPE(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_TYPE),
	    	ACCOUNT_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS),
	    	ADDRESS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ADDRESS),
	    	ENVIRONMENT(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT),
	        SSN(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN),
	        LOBS(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV),
	        PHONE(ICimaCommonConstants.CACHE_FLTR_CONDTN_PHONE),
	        ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID),
	        TRANSFER_FLAG(ICimaCommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG),
	        UNLOCKED(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED);

	        private final String value;
	        Filter(final String value) {
	            this.value = value;
	        }
	        public String getValue(){
	            return this.value;
	        }
	    }
	 
	 /**
	  * Provides dedicated (i.e. which will be locked) fresh account
	  * 
	  * @param filters
	  * @return
	  */
	 public AccountCache.Account getDedicatedAccount(Map<Filter, String> filters){
		LOGGER.debug("Starting to fetch fresh account with filter {}", filters!=null?Arrays.toString(filters.entrySet().toArray()):null);
		AccountCache.Account account = null;
		 
		Map<String, String> accountFilters = new HashMap<String, String>();
	    populateFilterMap(filters, accountFilters);
		 
	    accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE, ICommonConstants.CACHE_FLTR_VALUE_NULL);
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, getCurrentEnvironment());
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
			
		if(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN==null){
			accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		}
	     
		List<Object> accountObjs = getAccountCache().getFilteredObjects(accountFilters, 10);
		if(accountObjs!=null && accountObjs.size()>0){
			Collections.shuffle(accountObjs);
			account = (AccountCache.Account)accountObjs.get(0);
			try{
				getAccountCache().changeLock(account.getAccountId(), true);
			}catch(LockException ale){
				LOGGER.warn("Not able to lock account due to : ", ale);
			}
		}
		LOGGER.debug("Fresh account fetched: {}", account);
		return account;
	 }
	 
	 /**
	  * Unlocks a locked account
	  * 
	  * @param accountId
	  * @throws LockException
	  */
	 public void unlockAccount(String accountId) throws LockException{
		 getAccountCache().changeLock(accountId, false);
	 }
	 
	 private void populateFilterMap(Map<Filter, String> filters, Map<String, String> filterConditions){
		 if(filters != null){
			 for(Map.Entry<Filter, String> filer : filters.entrySet()){
				 filterConditions.put(filer.getKey().getValue(), filer.getValue());
	         }
	     }
	 }
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(FreshAccountProvider.class);
}
