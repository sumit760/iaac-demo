package com.comcast.test.citf.core.util;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class ObjectDestroyerTest extends ObjectInitializer {
	
	private final static String KEY = "key";
	private final static String VALUE = "Value";
	private final static String ENVIRONMENT = "QA";
	private ICitfCache userCache;
	private ICitfCache stringCache;
	private ICitfCache configCache;
	private ICitfCache testErrorCache;
	private ICitfCache mapCache;
	private ICitfCache userAttributeCache;
	private ICitfCache accountCache;
	private String filePath;
	
	@Before
	public void setup() {
		
		CoreContextInitilizer.initializeContext();
		
		userCache = getCache(ICimaCommonConstants.CACHE_USERS);
		stringCache = getCache(ICimaCommonConstants.CACHE_STRING);
		configCache = getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
		testErrorCache = getCache(ICimaCommonConstants.CACHE_TEST_ERROR);
		mapCache = getCache(ICimaCommonConstants.CACHE_MAP);
		userAttributeCache = getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES);
		accountCache = getCache(ICimaCommonConstants.CACHE_ACCOUNT);
		filePath = System.getProperty("user.dir");
	}
	
	
	@Test
	public void testDestroyAllCachesAndClearMqTempFiles() {
		
		//Populate all the caches
		populateAllCaches();
		
		//Now destroy all the caches
		ObjectDestroyer.destroyAllCaches();

		//Check the cache now
		assertThat(
				userCache.getObject(KEY),
				nullValue());

		assertThat(
				stringCache.getObject(KEY),
				nullValue());
		
		assertThat(
				configCache.getObject(KEY),
				nullValue());

		assertThat(
				testErrorCache.getObject(KEY),
				nullValue());

		assertThat(
				mapCache.getObject(KEY),
				nullValue());

		assertThat(
				userAttributeCache.getObject(KEY),
				nullValue());

		assertThat(
				accountCache.getObject(KEY),
				nullValue());
		
		
		ObjectDestroyer.destroyAllCaches();
		CoreContextInitilizer.destroyContext();
		
		try{
			Set<String> files = FileUtility.findFileNamesInDirectory(filePath + "/target/activemq-data");
			assertThat(files, nullValue());
		}catch(IOException e){
			Assert.fail("Exception occurred while finding file names in directory " + e);
		}
	}
	
	
	@After
	public void tearDown() {
		CoreContextInitilizer.destroyContext();
	}

	
	private void populateAllCaches() {
		userCache.put(KEY, VALUE, ENVIRONMENT);
		stringCache.put(KEY, VALUE);
		configCache.put(KEY, VALUE);
		testErrorCache.put(KEY, VALUE);
		mapCache.put(KEY, VALUE);
		userAttributeCache.put(KEY, VALUE);
		accountCache.put(KEY, VALUE, ENVIRONMENT);
	}
}
