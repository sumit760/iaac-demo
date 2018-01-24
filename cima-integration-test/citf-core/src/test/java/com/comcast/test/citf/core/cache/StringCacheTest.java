package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringCacheTest {

	private StringCache objStringCache;
	private final String KEY_STRING_CACHE = "Key";
	private final String VALUE_STRING_CACHE = "Value";
	private final static String EXCEPTION_INVALID_INPUT = "Inputs cannot be null or empty!!";

	@Before
	public void setup() {
		objStringCache = new StringCache();
	}
	
	
	@Test
	public void testPutWithInvalidValues() {
		
		//null key
		try {
			objStringCache.put(null, VALUE_STRING_CACHE);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}
		
		//blank key
		try {
			objStringCache.put("", VALUE_STRING_CACHE);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}
		
		//null value
		try {
			objStringCache.put(KEY_STRING_CACHE, null);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}

		//blank value
		try {
			objStringCache.put(KEY_STRING_CACHE, "");
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(EXCEPTION_INVALID_INPUT));
		}
	}

	
	@Test
	public void testPut() {
		objStringCache.put(KEY_STRING_CACHE, VALUE_STRING_CACHE);
		
		assertThat(
			objStringCache.getString(KEY_STRING_CACHE), 
			is(VALUE_STRING_CACHE));

	}


	@After
	public void tearDown() {

		objStringCache.quit();
	}

}
