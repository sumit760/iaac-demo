package com.comcast.test.citf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This class provides methods to clean/destroy/release resources which have been used during CITF execution.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 */
public class ObjectDestroyer{

	/**
	 * Cleans temporary files created by all caches 
	 */
	public static void destroyAllCaches(){
		try{
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_STRING).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_TEST_ERROR).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_MAP).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES).quit();
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT).quit();
			
			LOGGER.debug("Destroyed all caches.");
		}catch(Exception e){
			LOGGER.error("Error occurred while destroying caches : ", e);
		}
	}

	private static Logger LOGGER = LoggerFactory.getLogger(ObjectDestroyer.class);
}
