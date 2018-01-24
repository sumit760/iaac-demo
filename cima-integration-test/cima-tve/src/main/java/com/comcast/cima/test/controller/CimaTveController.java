package com.comcast.cima.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.controller.AbstractExecutionController;
import com.comcast.test.citf.core.init.cima.CoreCacheInitializer;
import com.comcast.test.citf.core.util.ExecutionCompletionChecker;

/**
 * This is main class to start test execution in cima-tve.
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 *
 */
public class CimaTveController extends AbstractExecutionController{
	
	/**
     * To initialize and perform pre test execution setup. eg. initializing Spring contexts, initializing cache and initializing data.
     * 
	 * @param environment
	 *        	The Name of environment where test suite to execute (eg. QA or STAGE or PROD)
	 * @param domain
	 *        	Name of the domain under which test to execute 
	 * @param uiexecEnv
	 *        	Execution environment as LOCAL or REMOTE
	 * 
	 * <br> Note that if uiexecEnv is selected as REMOTE then the computer need Sauce tunnel enabled. </br>
	 * 
	 * @throws Exception
	 * 
	 */
	private static void initialize(String environment, String domain, String uiexecEnv) throws Exception{
		//System param initialization
		String tempDirectory = MiscUtility.setEnvironmentVariables(ICimaCommonConstants.LOG_FILE_NAME_CIMA_TVE);
		System.out.println("######### Temporary file path: "+tempDirectory+"\n\n\n");
		Thread.currentThread().setName("Cima-TVE-Main");
		
		//Context initialization
		CimaTveContextInitializer.initializeContext();
		
		//Data initialization (for with LOCAL mode)
		loadData();
		
		//Cache Initialization
		Map<String, String> commonStringCacheParams = new HashMap<String, String>();
		commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, environment);
		commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN, domain);
				
		if (uiexecEnv != null) {
			commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_UI_TEST_EXEC_ENV, uiexecEnv);
		}
		((CoreCacheInitializer)CimaTveContextInitializer.getBean(ICimaCommonConstants.SPRING_BEAN_CACHE_INITIALIZER)).initialize(commonStringCacheParams);
				
		LOGGER.info("All initializations done");
	}
			
	/**
     * 
     * Method is the starting point of CITF execution in LOGIN. It calls test execution , track execution status using MQ listener and also perform  post execution cleanup activities.
	 
	 * @param args:: [0] : environment
	 *                     The Name of environment where test suite to execute (eg. QA or STAGE or PROD)
	 *               [1] : test category
	 *                     The category of test (SMOKE or INTEGRATION)
	 *               [2] : test Type
	 *                     Type of Test (UI or API)
	 *               [3] : domain
	 *                     The name of domain under which test suite is going to execute
	 *               [4] : UI execution environment
	 *                     Execution environment as LOCAL or REMOTE
	 *               The input arguments are not case-sensitive.
	 */
	public static void main(String[] args) {
		int noOfAsyncExecutions = 1;
		boolean isDestroyerCalled = false;
		
		String testType = null;
		String testCategory = null;
		String environment = null;
		String domain = null;
		String uiExecEnv = null;
		
		try{				
			
			if(args==null || args.length<4){
				throw new Exception(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			
			environment = isInEnvironment(args[0]);
			if(environment == null){
				throw new Exception("Input Environment(1st argument) is invalid!");
			}
			
			testCategory = isInTestCategory(args[1]);
			if(testCategory == null){
				throw new Exception("Input Test Category(2nd argument) is invalid!");
			}
			
			testType = isInTestType(args[2]);
			if(testType == null){
				throw new Exception("Input Test Type(3rd argument) is invalid!");
			}
			
			domain = isInDomain(args[3]);
			if(domain == null){
				throw new Exception("Input Domain(4th argument) is invalid!");
			}
			
			if(args.length==5 && TestType.UI.getValue().equals(testType)){
				uiExecEnv = isInUIExecutionEnv(args[4]);
				if(uiExecEnv == null){
					throw new Exception("Input UI execution environment(5th argument) is invalid!");
				}
			}
			
			LOGGER.info("Starting execution with environment: {}, testCategory: {}, testType: {}, domain: {} and UI test execution env: {}",
					new String [] {
							environment, testCategory, testType,
							domain, uiExecEnv

					});
			initialize(environment, domain, uiExecEnv);
			
			ICitfCache configCache = (ICitfCache)CimaTveContextInitializer.getBean(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			
			noOfAsyncExecutions = execute(testCategory, testType, configCache);
			
			if(noOfAsyncExecutions<=0)
				throw new Exception("System error. Engine is not working porperly may be due some invalid data!!!");
			
			long totalExecTime = Long.parseLong(configCache.getString(IDataProviderEnums.ConfigurationPropKeys.MAX_CITF_EXECUTION_TIME.getValue(), ICimaCommonConstants.ENVIRONMENT_ALL));
				
			ExecutionCompletionChecker executionChecker = (ExecutionCompletionChecker)CimaTveContextInitializer.getBean("execChecker");
			boolean isCompleted = executionChecker.isExecutionCompleted(noOfAsyncExecutions, totalExecTime);
			if(isCompleted){
				LOGGER.info("All test case executions are over.");
				destroy();
				isDestroyerCalled = true;
			}
			
		}catch(Exception e){
			LOGGER.error("Error occurred while executing controller", e);
			e.printStackTrace();
		}
		finally{
			try{
				if(!isDestroyerCalled)
					destroy();
			}
			catch(Exception de){}
		}
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(CimaTveController.class);
}