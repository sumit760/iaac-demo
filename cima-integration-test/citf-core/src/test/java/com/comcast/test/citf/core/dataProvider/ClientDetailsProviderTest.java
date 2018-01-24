package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

/**
* @author Abhijit Rej
* 
* This is unit test class to test ClientDetailsProvider.
*/
public class ClientDetailsProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetClientDetails(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(ClientDetailsProvider.ClientDetailsPropKeys.LOGIN_CLIENT_ID.getValue(), "test", ICimaCommonConstants.ENVIRONMENT_QA);
			configCache.put(ClientDetailsProvider.ClientDetailsPropKeys.LOGIN_CLIENT_SECRET.getValue(), "test", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_CLIENT_DETAILS);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof ClientDetailsProvider);
		ClientDetailsProvider cdp = (ClientDetailsProvider)dpObj;
		
		ClientDetailsProvider.ClientDetails result = cdp.getClientDetails(ClientDetailsProvider.ClientType.LOGIN);
		Assert.assertTrue(result instanceof ClientDetailsProvider.ClientDetails);
		Assert.assertNotNull(result.getClientId());
		Assert.assertNotNull(result.getClientSecret());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
