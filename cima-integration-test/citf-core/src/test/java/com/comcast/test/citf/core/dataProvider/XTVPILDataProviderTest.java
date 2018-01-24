package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums.XfinityTVPartnerIntegrationLayerPropKeys;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

/**
* @author Abhijit Rej
* 
* This is unit test class to test XTVPILDataProvider.
*/
public class XTVPILDataProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testTheMethodWhichReturnsString(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(XfinityTVPartnerIntegrationLayerPropKeys.XTV_PIL_AUTHN_SELECT_ACCOUNT.getValue(), "testSelectAccount", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_XTV_PIL);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof XTVPartnerIntegrationLayerDataProvider);
		XTVPartnerIntegrationLayerDataProvider xtvdp = (XTVPartnerIntegrationLayerDataProvider)dpObj;
		Assert.assertNotNull(xtvdp.getAuthSelectAccount());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
