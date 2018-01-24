package com.comcast.test.citf.common.exception;


/**
 * Class to handle test execution exception.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@SuppressWarnings("serial")
public class TestExecutionException extends CITFException{

	/**
	 * Constructor to initialize TestExecutionException.
	 * 
	 * @param message
	 * 				 Test execution exception.
	 * @param sourceClass
	 * 				 The source class.
	 */
	public TestExecutionException(String message, Class sourceClass){
		super(message, sourceClass);
	}
	
}
