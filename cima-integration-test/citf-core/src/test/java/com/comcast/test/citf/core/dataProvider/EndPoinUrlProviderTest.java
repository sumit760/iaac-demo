package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider.LoginUrlPropKeys;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider.OtherUrlPropKeys;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

/**
* @author Abhijit Rej
* 
* This is unit test class to test EndPoinUrlProvider.
*/
public class EndPoinUrlProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testValueUsingEnumKey(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(LoginUrlPropKeys.EMAIL.getValue(), "testEmail", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_URLS);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof EndPoinUrlProvider);
		EndPoinUrlProvider udp = (EndPoinUrlProvider)dpObj;
		Assert.assertNotNull(udp.getLoginUrl(LoginUrlPropKeys.EMAIL.name()));
	}
	
	
	@Test
	public void testDirectValue(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(OtherUrlPropKeys.PARENTAL_CONTROL_SERVICE_URL.getValue(), "testURL", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_URLS);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof EndPoinUrlProvider);
		EndPoinUrlProvider udp = (EndPoinUrlProvider)dpObj;
		Assert.assertNotNull(udp.getParentalControlServiceUrl());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
