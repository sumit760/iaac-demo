package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigCacheTest {

	private ConfigCache objConfigCache;
	private String sValid;
	private String sEnviornment;

	private String sKey1="key1";
	private static final String EXCEPTION_INVALID_INPUT = "Inputs cannot be null or empty!!";

	@Before	
	public void setup()
	{
		objConfigCache = new ConfigCache();
		sValid="VALID";
		sEnviornment="QA";
	}


	@Test
	public void testPutWithInvalidParams() {
		
		//null params
		try {
			objConfigCache.put(null, null,sEnviornment);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}
		
		//null & empty params
		try {
			objConfigCache.put(sKey1, "",null);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}
	}

	
	@Test
	public void testConfigCache() {

		objConfigCache.put(sKey1, 
						   sValid,
						   sEnviornment);
		
		assertThat(
				objConfigCache.getString(sKey1, sEnviornment), 
				is(sValid));

	}
	
	
	@After
	public void tearDown() {
		
		objConfigCache.quit();
	}

}
