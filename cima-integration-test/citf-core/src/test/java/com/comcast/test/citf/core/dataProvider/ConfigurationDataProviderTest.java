package com.comcast.test.citf.core.dataProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider.ConfigurationPropKeys;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

/**
* @author Abhijit Rej
* 
* This is unit test class to test ConfigurationDataProvider.
*/
public class ConfigurationDataProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	
	@Test
	public void testTheMethodWhichReturnsObjects(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(ConfigurationPropKeys.CITF_KEYSTORE_ALIAS.getValue(), "testAlias", ICimaCommonConstants.ENVIRONMENT_QA);
			configCache.put(ConfigurationPropKeys.CITF_KEYSTORE_ENTRY_PASSWORD.getValue(), "testEntryPass", ICimaCommonConstants.ENVIRONMENT_QA);
			configCache.put(ConfigurationPropKeys.CITF_KEYSTORE_KEYSTORE_PASSWORD.getValue(), "testKeystorePass", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_CONFIGURATION);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof ConfigurationDataProvider);
		
		ConfigurationDataProvider cdp = (ConfigurationDataProvider)dpObj;
		ConfigurationDataProvider.JavaKeyStoreCredentials jks = cdp.getCitfKeystoreCredentials();
		
		Assert.assertTrue(jks instanceof ConfigurationDataProvider.JavaKeyStoreCredentials);
		Assert.assertNotNull(jks.getAlias());
		Assert.assertNotNull(jks.getEntryPassword());
		Assert.assertNotNull(jks.getKeyStorePassword());
	}
	
	
	@Test
	public void testTheMethodWhichReturnsString(){
		Object dpObj = null;
		
		try{
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			configCache.put(ConfigurationPropKeys.CITF_RUN_MODE.getValue(), "LOCAL", ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_CONFIGURATION);
			
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof ConfigurationDataProvider);
		ConfigurationDataProvider cdp = (ConfigurationDataProvider)dpObj;
		Assert.assertNotNull(cdp.getCitfRunMode());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
