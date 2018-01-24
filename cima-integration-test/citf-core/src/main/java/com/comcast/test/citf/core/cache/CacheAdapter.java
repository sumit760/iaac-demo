package com.comcast.test.citf.core.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.jcs.access.CacheAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * This is adapter abstract class which should be used by all cache implemented class
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 * 
 */
public class CacheAdapter implements ICitfCache{
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public boolean put(String key, Object value) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return false;
	}

	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public boolean put(String key, String value) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return false;
	}

	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public boolean put(String key, String value, String environment){
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return false;
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public boolean put(String key, Object value, String environment){
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return false;
	}

	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	
	@Override
	public Object getObject(String key){
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return null;
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public List<Object> getFilteredObjects(Map<String, String> filterConditions, int noOfObjectsRequired){
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return null;
	}

	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public String getString(String key) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return null;
	}

	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public String getString(String key, String environment) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return null;
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public void changeLock(String key, boolean lockValue) throws LockException{
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public boolean isLocked(String key) throws LockException{
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
		return false;
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public void quit(){
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public void remove(String key) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
	}
	
	/**
	 * This is dummy method.It overrides by cached classes method whenever required.
	 */
	@Override
	public void remove(String key, String environment) {
		LOGGER.warn(NOT_IMPLEMENTED_WARNING);
	}
	
	/**
	 * To clean and destroy instances of CacheAccess 
	 * 
	 * @param cache
	 *        Instance of CacheAccess
	 * @see org.apache.commons.jcs.access
	 */
	public void quit(CacheAccess cache) {
		if(cache != null){
			cache.clear();
			cache.dispose();
		}
	}
	
	/**
	 * To check whether a string is null
	 * @param param1 
	 *        The string which is to be checked
	 */
	protected boolean hasNullInputs(String param1) {
		return StringUtility.isStringEmpty(param1);
	}
	
	/**
	 * To check either of string is null
	 * 
	 * @param param1
	 *        The string which need to be checked
	 * @param param2
	 *        The string which need to be checked
	 */
	protected boolean hasNullInputs(String param1, String param2) {
		return StringUtility.isStringEmpty(param1) || StringUtility.isStringEmpty(param2);
	}
	
	
	/**
	 * To check either of string is null or not
	 * 
	 * @param param1
	 *        The string which need to be checked
	 * @param param2
	 *        The string which need to be checked
	 * @param param3 
	 *        The string which need to be checked
	 */
	protected boolean hasNullInputs(String param1, String param2, String param3) {
		 return StringUtility.isStringEmpty(param1) || StringUtility.isStringEmpty(param2) || StringUtility.isStringEmpty(param3);
	}
	
	/**
	 * To check filter is strict or non strict.
	 * 
	 * Strict filter will have specific value for filtering but non strict filter will have generic conditions like NULL or Not NULL
	 * 
	 * @param filterValue
	 *        This is filter condition.The possible filter condition keys should be any of the given list. Other than ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, all are optional.
	 * 			1. ICommonConstants.CACHE_FLTR_CONDTN_PHONE
	 * 			2. ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS
	 * 			3. ICommonConstants.CACHE_FLTR_CONDTN_USER_ROLE
	 *          5. ICommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG
	 * 			6. ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV
	 * 			7. ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS
	 *  		8. ICommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN
	 *  		9. ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED
	 *         10. ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT
	 * @param cacheValue
	 *        This is cached value of the data. Eg. Address or phone or email etc.
	 * 
	 * @return boolean based on strict or non strict filter
	 */
	protected boolean nonStrictFilter(String filterValue, String cacheValue){
		boolean result = false;
		
		if(filterValue==null){
			return true;
		}
		
		if(ICommonConstants.CACHE_FLTR_VALUE_NOT_NULL.equals(filterValue) && cacheValue!=null){
			result = true;
		} else if(ICommonConstants.CACHE_FLTR_VALUE_NULL.equals(filterValue) && cacheValue==null){
			result = true;
		} else if(filterValue.equals(cacheValue)){
			result = true;
		}
		return result;
	}

    /**
     * To check filter is strict or non strict.
     *
     * Strict filter will have specific value for filtering but non strict filter will have generic conditions like NULL or Not NULL
     */
     protected boolean nonStrictLoopFilter(String filterValue, Collection<String> cacheValue){
		boolean result = false;

		if(filterValue==null){
			return true;
		}

		if(ICommonConstants.CACHE_FLTR_VALUE_NOT_NULL.equals(filterValue) && cacheValue!=null){
			result = true;
		} else if(ICommonConstants.CACHE_FLTR_VALUE_NULL.equals(filterValue) && cacheValue==null){
			result = true;
		} else if(cacheValue!=null && cacheValue.contains(filterValue)){
			result = true;
		}
		return result;
	}


    protected boolean nonStrictLoopMultiValueCheckFilter(String filterValue, Collection<String> cacheValue, int minimumCacheQunatity){
        boolean result = false;

        if(filterValue==null){
            return true;
        }

        if(ICommonConstants.CACHE_FLTR_VALUE_NOT_NULL.equals(filterValue) && cacheValue!=null && cacheValue.size()>=minimumCacheQunatity){
            result = true;
        } else if(ICommonConstants.CACHE_FLTR_VALUE_NULL.equals(filterValue) && (cacheValue==null || cacheValue.size()<minimumCacheQunatity)){
            result = true;
        }

        return result;
    }
	
	/**
	 * To check filter is strict or non strict.
	 * 
	 * Strict filter will have specific value for filtering but non strict filter will have generic conditions like NULL or Not NULL.
	 * 
	 * @param filterValue
	 *        This is filter condition.The possible filter condition keys should be any of the given list. Other than ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, all are optional.
	 * 			1. ICommonConstants.CACHE_FLTR_CONDTN_PHONE
	 * 			2. ICommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS
	 * 			3. ICommonConstants.CACHE_FLTR_CONDTN_USER_ROLE
	 *          5. ICommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG
	 * 			6. ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV
	 * 			7. ICommonConstants.CACHE_FLTR_CONDTN_ADDRESS
	 *  		8. ICommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN
	 *  		9. ICommonConstants.CACHE_FLTR_CONDTN_UNLOCKED
	 *         10. ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT
	 * @param cacheValue
	 *        This is cached value of the data. Eg. Address or phone or email etc.
	 * 
	 * @return boolean based on strict or non strict filter
	 */
	protected boolean strictFilter(String filterValue, String cacheValue){
		if(filterValue==null){
			return true;
		} else if(filterValue.equals(cacheValue)){
			return true;
		}
		return false;
	}
	
	private static final String NOT_IMPLEMENTED_WARNING = "This method has not been implemented here. Please use appropriate cache class.";
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAdapter.class);
}
