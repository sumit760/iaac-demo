package com.comcast.test.citf.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for exception handling.
 * 
 * @author arej001c, spal004c
 *
 */
@SuppressWarnings("serial")
public class CITFException extends Exception{
	
	/**
	 * Constructor to initialize CITFException.
	 * 
	 * @param message
	 * 				   The exception message to set.
	 * @param sourceClass
	 * 				   The source class that throws the exception.
	 */
	
	@SuppressWarnings("rawtypes")
	public CITFException(String message, Class sourceClass){
		super(message);
		logger = LoggerFactory.getLogger(sourceClass);
		logger.error(EXCEPTION_LOG+message);
		this.sourceClass = sourceClass;
		sourceClassName = this.sourceClass!=null?this.sourceClass.getName():null;
	}
	
	/**
	 * Overloaded constructor to initialize CITFException.
	 * 
	 * @param message
	 * 				  The exception message to set.
	 */
	public CITFException(String message){
		super(message);
	}
	
	/**
	 * Returns the sourceClassName.
	 * 
	 * @return Source class name.
	 */
	public String getSourceClassName(){
		return sourceClassName;
	}
	
	/**
	 * Returns sourceClass.
	 * 
	 * @return Class.
	 */
	public Class getSourceClass(){
		return sourceClass;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	@Override
	public String toString() {
		StringBuilder sbf = new StringBuilder();
		sbf.append(TO_STRING_PART1);
		sbf.append(sourceClassName);
		sbf.append(TO_STRING_PART2);
		sbf.append(super.getMessage());
		
		return sbf.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CITFException){
			CITFException exception = (CITFException)obj;
			
			if(this.getMessage().equals(exception.getMessage())){
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	
	private Logger logger = null;
	private String sourceClassName = null;
	private Class sourceClass = null;
	
	private static final String EXCEPTION_LOG = "Exception occured :";
	private static final String TO_STRING_PART1 = "Exception occured in :";
	private static final String TO_STRING_PART2 = " class : ";
}
