package com.comcast.test.citf.core.cache;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * StringCache is for keeping simple String values.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 * 
 */
@Service("stringCache")
public class StringCache extends CacheAdapter implements ICitfCache {
	
	private static final String REGION = "STRING_CACHE";
	
	
	/**
	 * Constructor of StringCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	
	public StringCache(){
		try{
			cache = JCS.getInstance(REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while initializing Cache : ", e);
		}
	}

	/**
	 * Populates the string detail into cache
	 * 
	 * @param key
	          Key to fetch the string value
	 * @param value
	          The value which is to be populated
	 */
	
	
	@Override
	public boolean put(String key, String value) {
		boolean isSucceeded = false;
		if(!hasNullInputs(key, value)){
			cache.put(key, value);
			isSucceeded = true;
		}
		else{
			LOGGER.error("Not able process put request with key {} and value {} due to any of these is(are) null or invalid", key, value);
		}
		return isSucceeded;
	}

	/**
	 * To get string detail from cache
	 * 
	 * @param key
	          Key to fetch the string value
	 * @return  value
	 */
		
	@Override
	public String getString(String key) {
		return !hasNullInputs(key)? cache.get(key) : null;
	}
	
	
	/**
	 * Cleans and destroys cache instances
	 */
	
	@Override
	public void quit(){
		super.quit(cache);
	}
	

	private CacheAccess<String, String> cache = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(StringCache.class);
}
