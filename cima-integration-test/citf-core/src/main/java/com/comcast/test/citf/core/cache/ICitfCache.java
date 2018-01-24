package com.comcast.test.citf.core.cache;

import com.comcast.test.citf.common.exception.LockException;

import java.util.List;
import java.util.Map;

/**
 * This interface provides all the common cache methods which should be implemented by all cache classes.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
public interface ICitfCache {
	
	/**
	 * Puts Object in cache
	 * 
	 * @param key
	 * 			cache key
	 * @param value
	 * 			Object to store in the cache 
	 */
	boolean put(String key, Object value);
	
	/**
	 * Puts String in cache
	 * 
	 * @param key
	 * 			cache key
	 * @param value
	 * 			String value to store in the cache 
	 * @return boolean
	 */
	boolean put(String key, String value);
	
	/**
	 * Puts String in cache for any specific environment
	 * 
	 * @param key
	 * 			cache key
	 * @param value
	 * 			String to store in the cache 
	 * @param environment
	 * 			Current environment
	 * @return boolean
	 */
	boolean put(String key, String value, String environment);
	
	/**
	 * Puts Object in cache for any specific environment
	 * 
	 * @param key
	 * 			cache key
	 * @param value
	 * 			Object to store in the cache 
	 * @param environment
	 * 			Current environment
	 * @return boolean
	 */
	boolean put(String key, Object value, String environment);
	
	/**
	 * Provides Object value associated with the key
	 * 
	 * @param key
	 * 			cache key
	 * @return Object
	 */
	Object getObject(String key);
	
	/**
	 * Provides list of objects based on input filter conditions
	 * 
	 * @param filterConditions
	 * 			Filter map to identify the objects to be fetched
	 * @param noOfObjectsRequired
	 * 			No. of Objects need to be fetched
	 * @return List of Objects
	 */
	List<Object> getFilteredObjects(Map<String, String> filterConditions, int noOfObjectsRequired);
	
	/**
	 * Provides String value associated with the key
	 * 
	 * @param key
	 * 			Cache key
	 * @return String value
	 */
	String getString(String key);
	
	/**
	 * Provides String value associated with the key and environment
	 * 
	 * @param key
	 * 			Cache key
	 * @param environment
	 * 			Current environment
	 * @return String value
	 */
	String getString(String key, String environment);
	
	/**
	 * Removes cache entry
	 * 
	 * @param key
	 * 			Cache key
	 */
	void remove(String key);
	
	/**
	 * Removes cache entry
	 * 
	 * @param key
	 * 			Cache key
	 * @param environment
	 * 			Current environment
	 */
	void remove(String key, String environment);
	
	
	/**
	 * Changes lock status for any lockable cached Object
	 * 
	 * @param key
	 * 			Cache key
	 * @param lockValue
	 * 			Lock Value, TRUE to lock and FALSE to unlock an already locked Object
	 * @throws LockException
	 */
	void changeLock(String key, boolean lockValue) throws LockException;
	
	/**
	 * Checks lock status for any lockable cached Object
	 * 
	 * @param key
	 * 			Cache key
	 * @return TRUE if Object is locked, else FALSE
	 * @throws LockException
	 */
	boolean isLocked(String key) throws LockException;
	
	/**
	 * Quit cache instance.
	 * This is a mandatory method which needs to be implemented by all cached classes.
	 */
	void quit();
}
