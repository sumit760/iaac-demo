package com.comcast.test.citf.core.cache;

import java.io.Serializable;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;
import org.apache.commons.jcs.access.exception.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * Cache is a temporary storage for keeping useful data through out CITF execution.
 * Test Error cache is for keeping test errors.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */
@Service("testErrorCache")
public class TestErrorCache extends CacheAdapter implements ICitfCache{

	private static final String REGION = "TEST_ERROR_CACHE";
	
	/**
	 * Constructor of TestErrorCache to initialize JCS cache
	 * 
	 * @see org.apache.commons.jcs.JCS
	 */
	
	public TestErrorCache(){
		try{
			cache = JCS.getInstance(REGION);
		}
		catch(CacheException e){
			LOGGER.error("Error occured while initializing Cache : ", e);
		}
	}
	
	/**
	 *  Populates the TestError detail into cache
	 * 
	 * @param key
	          Key to fetch the TestError
	 * @param value
	          The TestError which is to be populated
	 */
	
	
	@Override
	public boolean put(String key, Object value){
		LOGGER.debug("### Cache population input :: key = {} and value is {}", key, (value!=null?"not null.":"null."));
		boolean isSucceeded = false;
		
		try{
			if(StringUtility.isStringEmpty(key) || value == null || !(value instanceof TestError)){
				LOGGER.error("Not able process put request with key {} and value {} due to any of these is(are) null or invalid", key, value);
			}
			else{
				TestError error = (TestError)value;
				if(error.getException()==null){
					LOGGER.error("Input TestError object should have all mandatory values!!");
				}
				
				cache.put(key, error);
				isSucceeded = true;
			}
		}
		catch(CacheException e){
			LOGGER.error("Error occurred while populating Cache : ", e);
		}
		return isSucceeded;
	}
	
	
	/**
	 *To get object from cache
	 * 
	 * @param key 
	 *        Key to fetch the TestError
	 * 
	 * @return object
	 */
	@Override
	public Object getObject(String key){
		return !hasNullInputs(key)? cache.get(key) : null;
	}
	
	/**
	 * Cleans and destroys cache instance
	 */
	
	@Override
	public void quit() {
		super.quit(cache);
	}
	
	
	/**
	 * Simple POJO class of TestError to keep cached values
	 *
	 */
	public class TestError implements Serializable 
    {
        private static final long serialVersionUID = 6392376146163510146L;
        
        private TestExecutionException exception = null;
        
        /**
         * Class constructor
         * @param exception
         * 			Exception object @see com.comcast.test.citf.common.exception.TestExecutionException
         */
        public TestError(TestExecutionException exception){
        	this.exception = exception;
        }

        /**
         * To get exception from cache
         * 
         * @return exception
         */
        public TestExecutionException getException() {
			return exception;
		}
        
        /**
         * To set exception into cache
         * @param exception
         * 			Exception object @see com.comcast.test.citf.common.exception.TestExecutionException
         */

		public void setException(TestExecutionException exception) {
			this.exception = exception;
		}

		/**
		 * To compare exception
		 * 
		 * @param obj
		 *        An object instance of TestError
		 * 
		 */
		
		@Override
		public boolean equals(Object obj) {
			
			if(obj instanceof TestError){
				TestError error = (TestError)obj;
				
				if(this.exception.equals(error.getException())){
					return true;
				}
			}
			return false;
		}
		
		/**
		 * To get the hash code of an object
		 * 
		 * @return a hash code value for the object.
		 */
        
		@Override
		public int hashCode() {
			return this.getException().hashCode();
		}
		
		
		/**
		 * To get string representation of the object
		 * 
		 * @see String java.lang.String.format
		 * @return Returns a string representation of the object
		 */		
		
		@Override
        public String toString() {
            return String.format( "%s error occurred in %s", exception.getMessage(), exception.getSourceClassName());
        }
    }
	
	private CacheAccess<String, TestError> cache = null;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestErrorCache.class);
}
