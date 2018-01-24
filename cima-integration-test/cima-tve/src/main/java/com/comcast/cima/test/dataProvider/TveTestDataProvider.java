package com.comcast.cima.test.dataProvider;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.dataProvider.ICommonDataProvider;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.core.init.CitfTestInitializer;

public abstract class TveTestDataProvider extends CitfTestInitializer implements ICommonDataProvider, IDataProviderEnums{

	public Map<String, Object> getTestData(Set<String> keySet, Map<String, Object> optionalFilter, String environment, String sourceId) throws Exception {
		logger.debug("Enter Data provider with keySet="+keySet+", optionalFilter="+optionalFilter+" and environment="+environment);
		Map<String, Object> resultMap = null;
		
		if(keySet==null || keySet.isEmpty() || StringUtility.isStringEmpty(environment))
			throw new Exception(ICommonConstants.EXCEPTION_INVALID_INPUT);
		
		
		
		//TODO: write all data fetching logic here.
		

		
		
		logger.debug("Exit Data provider with resultMap="+resultMap+" for environment="+environment);
		return resultMap;
	}

	private static Logger logger = LoggerFactory.getLogger(TveTestDataProvider.class);
}
