package com.comcast.cima.test.validator;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.validator.ICommonValidator;

@Service("cimaTveTestValidator")
public class TveTestValidator implements ICommonValidator{
	
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

	
	public Set<String> check(Object input, String validationLogic) {
		Set<String> mismatches = null;
		
		if(input==null || !isValidationLogicValid(validationLogic)){
			throw new IllegalArgumentException("Input or validationLogic either null or invalid!");
		}
	
		
		//TODO: write all data fetching logic here.
		
		return mismatches;
	}
	
	
		
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
