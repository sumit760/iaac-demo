package com.comcast.test.citf.common.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.cima.jsonObjs.AccessTokenResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.IDMGetAccountsResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;
import com.comcast.test.citf.common.service.ParentalControl;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * JSON parser class to parse a json string into objects.
 * 
 * @author spal004c, arej001c
 * @since July 2015
 *
 */
public class JSONParserHelper {

	/**
	 * Method to parse json string into objects. The object type is required to pass
	 * to cast the string into appropriate object.
	 * 
	 * @param jsonString
	 * 			JSON input string
	 * @param ObjectType
	 * 			Type of object
	 * @return Parsed object
	 */
	public static Object parseJSON(String jsonString, Class<?> ObjectType) {
		
		ObjectMapper jsonMapper = new ObjectMapper();	
		Object parsedJSONObj = null;    	
		jsonString = jsonString.replaceAll(REPLACEABLE_STRING, ICommonConstants.BLANK_STRING);
    	
    	try
    	{	        
	        if ( ObjectType == AccessTokenResponseJSON.class ) {	        
	        	parsedJSONObj = jsonMapper.readValue(jsonString, new TypeReference<AccessTokenResponseJSON>() {});
	        } else if ( ObjectType == ParentalControl.class) {
	        	parsedJSONObj = jsonMapper.readValue(jsonString, new TypeReference<ParentalControl>() {});
	        } else if ( ObjectType == DeviceActivationResponseJSON.class) {
	        	parsedJSONObj = jsonMapper.readValue(jsonString, new TypeReference<DeviceActivationResponseJSON>() {});
	        } else if ( ObjectType == IDMUserCredentialValidationResponseJSON.class) {
	        	parsedJSONObj = jsonMapper.readValue(jsonString, new TypeReference<IDMUserCredentialValidationResponseJSON>() {});
	        } else if ( ObjectType == IDMGetAccountsResponseJSON.class) {
	        	parsedJSONObj = jsonMapper.readValue(jsonString, new TypeReference<IDMGetAccountsResponseJSON>() {});
	        }
	        
    	}catch (Exception e) {
    		LOGGER.error("Exception Occurred while parsing JSON String [{}] :", jsonString, e);
    	}
    	
    	LOGGER.debug("Parsed JSON String from input [{}]: to POJO {}", jsonString, parsedJSONObj);
    	return parsedJSONObj;
	}
	
	private static final String REPLACEABLE_STRING = "\\r\\n|\\r|\\n";
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONParserHelper.class);
}
