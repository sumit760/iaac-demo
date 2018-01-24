package com.comcast.test.citf.common.dataProvider;

import java.util.Map;
import java.util.Set;

/**
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 * 
 * This interface will be implemented by all Data Provider abstract classes.
 *
 * @deprecated It will be removed while implementing new data providers
 */
@Deprecated
public interface ICommonDataProvider {
	
	/**
	 * @param keys 
	 * 			This will contains the set of all keys which will be the key set of result map
	 * @param optionalFilter 
				If more than one data is required for any key, then those need to be mentioned along with the
	 * 			filter conditions in the map under optionalQuantitySet which will come along with the key.
	 * 			As this is optional, so it may comes null in the request.
	 * @param environment 	
	 * 			Execution environment
	 * @param sourceId
	 * 			Unique test class id			
	 * 		e.g.
	 * 			Any test class required 3 sets of USERID_PASSWORD with status ACTIVE from QA environment
	 * 			would be like
	 * 
	 * 			keySet = <Set>{"USER_PSWD", ....}
	 * 			optionalQuantitySet = <Map>{("USER_PSWD",<Map>{("FILTER_CONDITION_STATUS", "ACTIVE"),("QUANTITY", 3)}),....}
	 * 			environment = "QA"
	 */
	public Map<String, Object> getTestData(Set<String> keys, Map<String, Object> optionalFilter, String environment, String sourceId) throws Exception;
	
	String INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION = "Required input data not found from Data provider!";
}
