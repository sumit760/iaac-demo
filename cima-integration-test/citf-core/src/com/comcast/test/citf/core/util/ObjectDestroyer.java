package com.comcast.test.citf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 */
public class ObjectDestroyer extends ObjectInitializer {

	public static void destroyAllCaches() {
		try {
			getCache(ICimaCommonConstants.CACHE_SERVICES).quit();
			getCache(ICimaCommonConstants.CACHE_USERS).quit();
			getCache(ICimaCommonConstants.CACHE_STRING).quit();
			getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS).quit();
			getCache(ICimaCommonConstants.CACHE_TEST_ERROR).quit();
			getCache(ICimaCommonConstants.CACHE_MAP).quit();
			getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES).quit();
			getCache(ICimaCommonConstants.CACHE_ACCOUNT).quit();

			LOGGER.debug("Destroyed all caches.");
		} catch (Exception e) {
			LOGGER.error("Error occurred while destroying caches", e);
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ObjectDestroyer.class);
}
