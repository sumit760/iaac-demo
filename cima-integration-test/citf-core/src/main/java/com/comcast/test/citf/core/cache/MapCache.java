package com.comcast.test.citf.core.cache;

import java.util.Map;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.StringUtility;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * Map cache is for keeping simple Map values.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */
@Service("mapCache")
public class MapCache extends CacheAdapter implements ICitfCache{

	private static final String REGION = "MAP_CACHE";
	

	/**
	 * Constructor of MapCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	
	public MapCache(){
		try{
			cache = JCS.getInstance(REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while initializing Cache : ", e);
		}
	}
	
	/**
	 * Populates the map detail into cache for any specific environment
	 * 
	 * @param key
	          Key to fetch the value
	 * @param value
	          The value which is to be populated
	 */
	
	
	@Override
	public boolean put(String key, Object value) {
		LOGGER.debug("### Cache population input :: key = {} and value is {}", key, (value!=null?"not null.":"null."));
		boolean isSucceeded = false;
		try{
			//validation....
			if(StringUtility.isStringEmpty(key) || value == null || !(value instanceof Map)){
				LOGGER.error("Not able process put request with key {} and value {} due to any of these is(are) null or invalid",
						key, value);
			}
			else{
				Map<String, Object> map = (Map<String, Object>)value;
				cache.put(key, map);
				isSucceeded = true;
			}
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while putting new value :", e);
		}
		return isSucceeded;
	}
	
	
	/**
	 * To get object from cache
	 * 
	 * @param key
	 *        The key to fetch the value.
	 *  
	 * @return object
	 */
	
	
	@Override
	public Object getObject(String key){
		return !hasNullInputs(key)?cache.get(key):null;
	}
	
	/**
	 * To remove value from cache
	 * 
	 * @param key
	 *        The key to fetch the value.
	 */
	
	@Override
	public void remove(String key){
		if(key!=null && cache.get(key)!=null){
			cache.remove(key);
		}
	}
	
	/**
	 * To clean and destroy instance of cache
	 */
	
	@Override
	public void quit() {
		super.quit(cache);
	}
	

	private CacheAccess<String, Map<String, Object>> cache = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(MapCache.class);
}
