package com.comcast.cima.test.validator;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.validator.ICommonValidator;

/**
 * This class provides methods to validate IDM tests.
 * 
 * @author arej001c
 *
 */
@Service("cimaIdmTestValidator")
public class IdmTestValidator implements ICommonValidator{
	
	/**
	 * Enumeration contains all validation types
	 */
	public enum ValidationLogic{
		SAMPLE_VALIDATION("SAMPLE_VALIDATION");		//Please remove this line when you are writing business logic in this class
		
		private final String value;
        ValidationLogic(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Method to validate data based on input logic name.
	 * Not fully implemented.
	 * 
	 * @param validationLogic
	 *        	Validation Logic Name
	 * @param input
	 *        	Map of data set
	 * @return set of mismatched data
	 */
	public Set<String> check(Object input, String validationLogic) {
		Set<String> mismatches = null;
		
		if(input==null || !isValidationLogicValid(validationLogic)){
			throw new IllegalArgumentException("Input is either null or invalid!");
		}
	
		
		//TODO: write all data fetching logic here.
		
		return mismatches;
	}
	
	
	/** 
	 * Utility method to check whether input validation logic is valid
	 * 
	 * @param input
	 * 			input validation logic
	 * @return TRUE if input validation logic is valid, else FALSE
	 */
	private boolean isValidationLogicValid(String input){
		
		if(input!=null)
			for(ValidationLogic val : ValidationLogic.values()){
				if(val.getValue().equals(input)){
					return true;
				}
			}
		
		return false;
	}
	
}
