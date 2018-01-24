package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.ICommonConstants;

public class MapCacheTest {

	private static final String MAPKEY = "arg0";
	private static final String VALUEKEY = "Value0";
	private static final String CACHEKEY = "cacheKey";
	
	private MapCache objMapCache;

	@Before
	public void setup() {
		objMapCache = new MapCache();
	}

	@Test
	public void testPutWithInvalidParams() {
		
		Map<String, Object> map = new HashMap<>();
		map.put(MAPKEY, VALUEKEY);
		
		//null key
		try {
			objMapCache.put(null, map);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(ICommonConstants.EXCEPTION_INVALID_INPUT));
		}
		
		//blank key
		try {
			objMapCache.put("", map);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(ICommonConstants.EXCEPTION_INVALID_INPUT));
		}

		//null value
		try {
			objMapCache.put(CACHEKEY, null);
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(ICommonConstants.EXCEPTION_INVALID_INPUT));
		}

		//value not instance of map
		try {
			objMapCache.put(CACHEKEY, "InvalidValue");
		} catch (Exception e) {
			assertThat(
				e.getMessage(), 
				is(ICommonConstants.EXCEPTION_INVALID_INPUT));
		}

	}
	
	
	@Test
	public void testPut() {
		Map<String, Object> map = new HashMap<>();
		map.put(MAPKEY, VALUEKEY);

		objMapCache.put(CACHEKEY, map);
		
		@SuppressWarnings("unchecked")
		Map<String,Object> retMap = (Map<String,Object>)objMapCache.getObject(CACHEKEY);
		
		assertThat((String)retMap.get(MAPKEY), is(VALUEKEY));

	}


	@Test
	public void testRemove() {
		
		Object obj1 = new HashMap<>();
		Object obj2 = new HashMap<>();
		
		objMapCache.put("key1", obj1);
		objMapCache.put("key2", obj2);
		
		objMapCache.remove("key1");
		
		assertThat(
			objMapCache.getObject("key1"), 
			is(nullValue()));

	}

	
	@After
	public void tearDown() {
	
		objMapCache.quit();
	}



}
