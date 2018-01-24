package com.comcast.test.citf.common.exception;


/**
 * Class to handle lock exception.
 * 
 * @author arej001c
 * @see CITFException
 */
public class LockException extends CITFException{
	
	private static final long serialVersionUID = 1L;
	public static final String EXCECPTION_TYPE_OBJECT_NOT_FOUND = "ET_OBJECT_NOT_FOUND";
	public static final String EXCECPTION_TYPE_OBJECT_ALREADY_LOCKED = "ET_OBJECT_ALREADY_LOCKED";
	public static final String EXCECPTION_TYPE_OBJECT_NOT_LOCKED = "ET_OBJECT_NOT_LOCKED";
	
	private String exceptionType;

	/**
	 * Constructor to initialize Lock Exception.
	 * 
	 * @param exceptionType
	 * 					   The type of exception.
	 * @param message
	 * 					   The exception message.
	 */
	public LockException(String exceptionType, String message){
		super(message);
		this.exceptionType = exceptionType;
	}

	/**
	 * Returns the exception type.
	 * 
	 * @return exception type
	 */
	public String getExceptionType() {
		return exceptionType;
	}
	
}
