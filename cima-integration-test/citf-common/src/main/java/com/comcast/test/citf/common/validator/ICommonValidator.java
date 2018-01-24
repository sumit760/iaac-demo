package com.comcast.test.citf.common.validator;

import java.util.Set;

/**
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 * This is a Validator interface which will be implements by all validator classes 
 */
public interface ICommonValidator {
	
	Set<String> check(Object input, String validationLogic);
	
}
