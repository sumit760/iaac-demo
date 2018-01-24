package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.OAuthDataProvider.OAuthUrlPropKeys;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

/**
* @author Abhijit Rej
* 
* This is unit test class to test OAuthDataProvider.
*/
public class OAuthDataProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testDataProvider(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(OAuthUrlPropKeys.ACCESS_TOKEN_URL.getValue(), "testAccessTokenURL", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_OAUTH_DATA);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof OAuthDataProvider);
		OAuthDataProvider odp = (OAuthDataProvider)dpObj;
		Assert.assertNotNull(odp.getAccessTokenUrl());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
