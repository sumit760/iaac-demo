package com.comcast.test.citf.common.exception;

/**
 * Class to handle User specific exception.
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 *
 */
@SuppressWarnings("serial")
public class InvalidUserException extends CITFException{

	public InvalidUserException(String message){
		super(message);
	}
	
}
