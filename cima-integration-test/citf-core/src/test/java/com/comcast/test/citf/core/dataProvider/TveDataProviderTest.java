package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.TveDataProvider.TvePropKeys;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

public class TveDataProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testTveDataProvider(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(TvePropKeys.REQUEST_URL.getValue(), "testUrl", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_TVE);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch TveDataProvider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof TveDataProvider);
		TveDataProvider tdp = (TveDataProvider)dpObj;
		Assert.assertNotNull(tdp.getRequestUrl());
	}
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
