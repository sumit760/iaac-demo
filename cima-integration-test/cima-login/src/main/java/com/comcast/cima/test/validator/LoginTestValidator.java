package com.comcast.cima.test.validator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.common.validator.ICommonValidator;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This class provides methods to validate Login tests.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */

@Service("cimaLoginApiTestValidator")
public class LoginTestValidator implements ICommonValidator{
	
	private LDAPInterface ldap = null;
	
	/**
	 * Enumeration contains all validation types
	 */
	public enum ValidationLogic{
		VALIDATE_LOGIN_TOKEN(ENUM_VAL_VALIDATE_LOGIN_TOKEN);
		
		private final String value;
        ValidationLogic(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Method to validate data based on input logic name
	 * 
	 * @param validationLogic
	 *        	Validation Logic Name
	 * @param input
	 *        	Map of data set
	 * @return set of mismatched data
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> check(Object input, String validationLogic) {
		Set<String> mismatches = null;
		
		if(input==null || !isValidationLogicValid(validationLogic))
			throw new IllegalArgumentException("Input or validationLogic is either null or invalid!");
		
		switch(validationLogic){
			
			case ENUM_VAL_VALIDATE_LOGIN_TOKEN:
					
					Map<String, String> fieldsMap = (Map<String, String>) input;
					LOGGER.debug("Enter validation check with input : {} and validationLogic: {}", fieldsMap, validationLogic);
					
					if(		fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_CUSTOMER_ID)!=null && 
							fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_FIRST_NAME)!=null &&
							fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_LAST_NAME)!=null &&
							fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_EMAIL)!=null){
						ldap = ObjectInitializer.getLdapService();
					
						LDAPCustomer customer = ldap.getCustomerData(ldap.new LDAPInstruction(LDAPInterface.SearchType.EQUAL_TO, LDAPInterface.LdapAttribute.CUSTOMER_GUID, fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_CUSTOMER_ID)));
						
						if(customer!=null && customer.getFirstName()!=null && customer.getLastName()!=null && customer.getEmail()!=null){
							mismatches = new HashSet<String>();
							if(!fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_FIRST_NAME).equalsIgnoreCase(customer.getFirstName()))
								mismatches.add("First Name");
							
							if(!fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_LAST_NAME).equalsIgnoreCase(customer.getLastName()))
								mismatches.add("Last Name");
							
							if(!fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_EMAIL).equalsIgnoreCase(customer.getEmail()))
								mismatches.add("Email Address");
							
							LOGGER.info("Validation checking is okay for customer : {}", fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_CUSTOMER_ID));
						}
						else
							throw new IllegalStateException("Customer data missing from LDAP for customerId : "+fieldsMap.get(ICimaCommonConstants.VALIDATION_KEY_CUSTOMER_ID));
					}
					else{
						throw new IllegalStateException("Data missing from SAML response!");
					}
					break;
		}
		
		if(mismatches!=null && mismatches.size()==0) {
			mismatches = null;
		}
		return mismatches;
	}
	
	
	
	//****************************** Private variables & methods ****************************

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
				if(val.getValue().equals(input)) {
					return true;
				}
			}
		
		return false;
	}
	
	private static final String ENUM_VAL_VALIDATE_LOGIN_TOKEN = "VALIDATE_LOGIN_TOKEN";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginTestValidator.class);
}
