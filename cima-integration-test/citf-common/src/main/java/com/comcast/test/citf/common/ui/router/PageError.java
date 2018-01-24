package com.comcast.test.citf.common.ui.router;

import java.util.ArrayList;
import java.util.List;

/**
* Placeholder class for errors that might be encountered in UI navigation.
* 
* @author Abhijit Rej (arej001c)
* @since October 2015
* 
*/
public class PageError {
	
	public static final String ERROR_SYSTEM_ERROR = "System Error";
	public static final String ERROR_PAGE_ERROR = "Page Error";
	
	public static final String ERROR_DESCRIPTION_INVALID_NULL_INPUT = "Invalid/null input";
	
	
	/**
	 * Constructor for PageError with list of error descriptions
	 * 
	 * @param error The error string
	 * @param errorDescriptions The list of error descriptions
	 * @param sourcePage The source page where the error is encountered
	 * 
	 */
	public PageError(String error, List<String> errorDescriptions, String sourcePage){
		this.error = error;
		this.errorDescriptions = errorDescriptions;
		this.sourcePage = sourcePage;
	}
	
	/**
	 * Constructor for PageError with a error descriptions
	 * 
	 * @param error The error string
	 * @param errorDescription The error description string
	 * @param sourcePage The source page where the error is encountered
	 * 
	 */
	public PageError(String error, String errorDescription, String sourcePage){
		this.error = error;
		this.sourcePage = sourcePage;
		
		errorDescriptions = new ArrayList<String>();
		errorDescriptions.add(errorDescription);
	}

	private String error;
	private List<String> errorDescriptions;
	private String sourcePage;
	
	
	/**
	 * Method to get the error
	 * 
	 * @return The error
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * Method to set the error
	 * 
	 * @param error The error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	
	/**
	 * Method to get list of error descriptions
	 * 
	 * @return The list of error descriptions
	 */
	public List<String> getErrorDescriptions() {
		return errorDescriptions;
	}
	
	/**
	 * Method to set the list of error descriptions
	 * 
	 * @param errorDescriptions The list of error descriptions to set
	 */
	public void setErrorDescription(List<String> errorDescriptions) {
		this.errorDescriptions = errorDescriptions;
	}
	
	/**
	 * Method to get the source page where the error is encountered
	 * 
	 * @return The source page
	 */
	public String getSourcePage() {
		return sourcePage;
	}
	
	/**
	 * Method to set the source page where the error is encountered
	 * 
	 * @param sourcePage The source page
	 */
	public void setSourcePage(String sourcePage) {
		this.sourcePage = sourcePage;
	}
	
	
}
