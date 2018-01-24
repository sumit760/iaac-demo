package com.comcast.test.citf.core.cache;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * ConfigCache is for keeping Config related data.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
@Service("configCache")
public class ConfigCache  extends CacheAdapter implements ICitfCache{
	
	private static final String REGION = "CONFIG_CACHE";

	/**
	 * Constructor of ConfigCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
		
	public ConfigCache(){
		try{
			cache = JCS.getInstance(REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while initializing Cache : ", e);
		}
	}
	
	
	/**
	 * Populates the configuration detail into cache for any specific environment
	 * 
	 * @param key
	          Key to fetch the configuration value
	 * @param value
	          The value which is to be populated
	 * @param environment
	          Environment under which key is to be stored
	 */
	
		
	@Override
	public boolean put(String key, String value, String environment) {
		LOGGER.debug("### Cache population input :: key = {}, value: {} and environment: {}", key, value, environment);
		boolean isSucceeded = false;
		
		try{
			if(!hasNullInputs(key, value, environment)){
				
				Map<String, String> map = cache.get(environment);
				if(map == null){
					map = new HashMap<String, String>();
				}
			
				map.put(key, value);
				cache.put(environment, map);
				isSucceeded = true;
			}
			else{
				LOGGER.error("Not able process put request with key {} and value {} for environment {} due to any of these is(are) null or invalid",
						key, value, environment);
			}
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while putting new value :", e);
		}
		return isSucceeded;
	}
	
	/**
	 * To get string value based on input key and input environment
	 * 
	 * @param key
	 *        Key by which value is to be fetched
	 * @param environment
	 *        Environment from which value is to be fetched
	 * 
	 * @return String The value from cache
	 */
	
	@Override
	public String getString(String key, String environment) {
		String value = null;
		
		try{
			if(!hasNullInputs(key, environment)){
			
				Map<String, String> map = cache.get(environment);
				if(map!=null && map.get(key)!=null){
					value = map.get(key);
				}
			
				//This will check the key in default 'ALL' environment in case not found in specific environment 
				if(value == null && !ICommonConstants.ENVIRONMENT_ALL.equals(environment)){
					map = cache.get(ICommonConstants.ENVIRONMENT_ALL);
					if(map!=null && map.get(key)!=null){
						value = map.get(key);
					}
				}
			}
			else{
				LOGGER.error("Not able process getString request with key {} and environment {} due to any of these is(are) null or invalid",
						key, environment);
			}
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while fetching value :", e);
		}
		
		LOGGER.debug("### Cache output :: key = {}, value: {} and environment: {}", key, value, environment);
		return value;
	}
	
	/**
	 * Cleans and destroys cache instance
	 */
	
	@Override
	public void quit() {
		super.quit(cache);
	}


	private CacheAccess<String, Map<String, String>> cache = null; 
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigCache.class);
}
