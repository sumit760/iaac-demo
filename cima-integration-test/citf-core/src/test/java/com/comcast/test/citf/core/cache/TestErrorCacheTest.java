package com.comcast.test.citf.core.cache;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.core.cache.TestErrorCache;
import com.comcast.test.citf.core.cache.TestErrorCache.TestError;

import static org.hamcrest.CoreMatchers.is;


public class TestErrorCacheTest{

	private TestErrorCache objTestErrorCache;
	private TestError objTestError;

	private final static String KEY_CACHE = "Key";
	private final static String EXCEPTION_INVALID_INPUT = "Invalid input(s)!!";
	private final static String EXCEPTION_NULL_EXECUTION_EXCEPTION = "Input TestError object should have all mandatory values!!";
	private final static String EXCEPTION_MESSAGE = "Test Exception";
	private TestExecutionException exception;


	@Before	
	public void setup()
	{
		objTestErrorCache = new TestErrorCache();
		exception = new TestExecutionException(EXCEPTION_MESSAGE,TestErrorCache.class);
		objTestError = objTestErrorCache.new TestError(exception);

	}


	@Test
	public void testObjectPutGetValidValues() 
	{

		/* Sunny day scenario */
		objTestErrorCache.put(KEY_CACHE, objTestError);	
		assertThat(
				(TestError)objTestErrorCache.getObject((KEY_CACHE)),
				is(objTestError));

		assertThat(
				   objTestError.getException().getMessage(), 
				   is(EXCEPTION_MESSAGE));
	}
	
	
	@Test
	public void testObjectPutGetInValidValues() 
	{
		
		
		/* Rainy day scenarios */
		// Empty key
		try
		{
			objTestErrorCache.put("", objTestError);	
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}
		
		//null object to put
		try
		{
			objTestErrorCache.put(KEY_CACHE, null);	
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}

		
		//Object instance not type of TestError
		try
		{
			objTestErrorCache.put(KEY_CACHE, "InvalidObjectType");	
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}
		
		//null TestExecutionException
		objTestError = objTestErrorCache.new TestError(null);
		try
		{
			objTestErrorCache.put(KEY_CACHE, objTestError);	
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_NULL_EXECUTION_EXCEPTION));
		}

	}
	
	
	@After
	public void tearDown()  {

		objTestErrorCache.quit();
	}

}
