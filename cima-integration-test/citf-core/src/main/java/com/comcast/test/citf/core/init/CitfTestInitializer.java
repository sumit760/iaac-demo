package com.comcast.test.citf.core.init;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.cima.persistence.ServersDAO;
import com.comcast.test.citf.common.cima.persistence.beans.LogFinders;
import com.comcast.test.citf.common.cima.persistence.beans.Servers;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.reader.SplunkReader;
import com.comcast.test.citf.common.testng.ExecuteApiTests;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.MapCache;
import com.comcast.test.citf.core.cache.StringCache;
import com.comcast.test.citf.core.cache.TestErrorCache;
import com.comcast.test.citf.core.cache.TestErrorCache.TestError;
import com.comcast.test.citf.core.controller.AbstractExecutionController;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider.SauceLabCredentials;
import com.saucelabs.saucerest.SauceREST;

/**
 * This abstract class provides a few common methods which need to be used in all step definition classes across CITF.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
@Service("citfTestInitializer")
public class CitfTestInitializer{
	public static final String TEST_PARAM_MAP = "TEST_PARAM_MAP";

	/**
	 * Provides to Class to execute TestNG tests from Cucumber Step Definition
	 * 
	 * @param testClassName
	 * 			Name of the TestNG test class
	 * @param testName
	 * 			Name of the Cucumber test scenario
	 * @param testMethodName
	 * 			Name of test method inside estNG test class
	 * @return Instance of ExecuteApiTests. @see com.comcast.test.citf.common.testng.ExecuteApiTests
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public ExecuteApiTests getExecuteApiTests(String testClassName, String testName, String testMethodName) throws Exception{
		ExecuteApiTests ept = (ExecuteApiTests)CoreContextInitilizer.getBean(EXECUTE_API_TEST_BEAN_NAME);
		ept.setTestClassName(testClassName);
		ept.setTestName(testName);
		ept.setParameter(ICimaCommonConstants.BLANK_STRING);
		ept.addMethodNames(testMethodName);

		return ept;
	}

	/**
	 * Generates unique class id
	 * 
	 * @param className
	 * 			Name of the class
	 * @return unique class id
	 */
	public synchronized String generateUniqueClassId(String className){
		StringBuilder sbf = new StringBuilder();
		sbf.append(className);
		sbf.append(ICimaCommonConstants.HASH_SIGN);
		sbf.append(MiscUtility.generateUniqueId());
		return sbf.toString();
	}


	/**
	 * Runs TestNG tests from Cucumber step definition methods
	 * 
	 * @param classId
	 * 			Class Id
	 * @param testName
	 * 			Name of the test
	 * @param testClassName
	 * 			Name of the test class
	 * @param testMethodName
	 * 			Name of the test method
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public void runTest(String classId, String testName, String testClassName, String testMethodName) throws Exception{
		getExecuteApiTests(testClassName, testName, testMethodName).executeTests(classId, null);

		TestError errCache = consumeError(classId);
		if(errCache!=null && errCache.getException()!=null){
			throw errCache.getException();
		}
	}

	/**
	 * Runs TestNG tests from Cucumber step definition methods
	 * 
	 * @param classId
	 * 			Class Id
	 * @param testName
	 * 			Name of the test
	 * @param testClassName
	 * 			Name of the test class
	 * @param testMethodName
	 * 			Name of the test method
	 * @param params
	 * 			Additional parameters
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public void runTest(String classId, String testName, String testClassName, String testMethodName,  Map<String, String> params) throws Exception{
		params.put(ICimaCommonConstants.CURRENT_TEST_METHOD, testMethodName);
		getExecuteApiTests(testClassName, testName, testMethodName).executeTests(classId, params);

		TestError errCache = consumeError(classId);
		if(errCache!=null && errCache.getException()!=null){
			throw errCache.getException();
		}
	}

	
	/**
	 * Creates exception from TestNG test class which will be consumed by Cucumber step definition
	 * 
	 * @param methodId
	 * 			Method Id
	 * @param exception
	 * 			TestExecutionException object @see com.comcast.test.citf.common.exception.TestExecutionException
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public void produceError(String methodId, TestExecutionException exception){
		try{
			TestError err = ((TestErrorCache)testErrorCache).new TestError(exception);
			testErrorCache.put(methodId, err);

			Logger logger = LoggerFactory.getLogger(exception.getSourceClass());
			logger.error(exception.getMessage());

			Assert.fail(exception.getMessage());
		}
		catch(Exception e){
			LOGGER.error("Error occurred while producing test error", e);
		}
	}

	/**
	 * Consumes exception in Cucumber step definition which has been created in TestNG test class
	 * 
	 * @param methodId
	 * 			Method Id
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public TestError consumeError(String methodId){
		Object obj = null;
		try{
			obj = testErrorCache.getObject(methodId);
		}
		catch(Exception e){
			LOGGER.error("Error occurred while consuming test error", e);
		}
		return obj!=null?(TestError)obj:null;
	}

	/**
	 * Puts test value in cache which will be used by other methods in TestNG test class
	 * 
	 * @param classId
	 * 			Class Id
	 * @param key
	 * 			Cache key
	 * @param value
	 * 			Cache value
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public void putTestValue(String classId, String key, Object value) throws Exception{
		if(StringUtility.isStringEmpty(classId) || StringUtility.isStringEmpty(key) || value==null){
			throw new Exception(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
		}

		Map<String, Object> cacheEle = (Map<String, Object>) ((mapCache.getObject(classId)!=null) ? (Map<String, Object>)mapCache.getObject(classId) : new HashMap<String, Object>());
		cacheEle.put(key, value);
		mapCache.put(classId, cacheEle);
	}

	/**
	 * Gets test value from cache which have been populated by any previously executed methods in TestNG test class
	 * 
	 * @param classId
	 * 			Class Id
	 * @param key
	 * 			Cache key
	 * @return Cached object (value)
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public Object getTestValue(String classId, String key) throws Exception{
		Object value = null;

		if(StringUtility.isStringEmpty(classId) || StringUtility.isStringEmpty(key)){
			throw new Exception(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
		}
		
		Map<String, Object> cacheEle = mapCache.getObject(classId)!=null? (Map<String, Object>)mapCache.getObject(classId) : null;
		if(cacheEle!=null && cacheEle.get(key)!=null){
			value = cacheEle.get(key);
		}

		return value;
	}

	/**
	 * Removes test value from cache which have been populated by any previously executed methods in TestNG test class
	 * 
	 * @param classId
	 * 			Class Id
	 * @param key
	 * 			Cache key
	 * @return Object
	 * @throws Exception
	 * 
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 */
	@Deprecated
	public Object removeTestValue(String classId, String key) throws Exception{
		Object value = null;

		if(StringUtility.isStringEmpty(classId) || StringUtility.isStringEmpty(key)){
			throw new Exception(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
		}
		Map<String, Object> cacheEle = mapCache.getObject(classId)!=null? (Map<String, Object>)mapCache.getObject(classId) : null;

		if(cacheEle!=null && cacheEle.get(key)!=null){
			value = cacheEle.remove(key);
		}
		return value;
	}

	/**
	 * Provides current environment
	 * 
	 * @return current environment
	 * @throws Exception
	 */
	public String getCurrentEnvironment() {
		String environment = null;
		try {
			environment = stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT);
		}catch(Exception e){
			LOGGER.error("Not able to fetch current environment, so going with default environment {}",ICimaCommonConstants.ENVIRONMENT_QA);
		}
		return environment!=null? environment : ICimaCommonConstants.ENVIRONMENT_QA;
	}

	/**
	 * Provides current domain
	 * 
	 * @return current domain
	 * @throws Exception
	 */
	public String getCurrentDomain() {
		return stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN);
	}

	/**
	 * Provides current UI execution environment to decide whether UI test cases will be executed on Sauce Labs or local
	 * 
	 * @return current UI execution environment
	 * @throws Exception
	 */
	public String getUIExecutionEnvironment() {
		return stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_UI_TEST_EXEC_ENV);
	}

	/**
	 * Checks whether input environment matches current environment
	 * 
	 * @param environment
	 * 			Environment
	 * @return TRUE if input environment is equals to current environment or 'ALL', otherwise FALSE
	 * @throws Exception
	 */
	public boolean isEnvironmentValid(String environment){
		return environment == null || (getCurrentEnvironment().equalsIgnoreCase(environment) || ICimaCommonConstants.ENVIRONMENT_ALL.equalsIgnoreCase(environment));
	}

	/**
	 * Provides Web browser instance based on given input parameters
	 * 
	 * @param currentTest
	 * 			Name of the current test 
	 * @param platform
	 * 			Platform/Operating System where the browser will run. @see com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO#Platforms
	 * @param type
	 * 			Type of device. @see com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO#Types
	 * @param browserName
	 * 			Name of the browser as described inside browser_capabilities table
	 * @param isSauceConnectEnabled
	 * 			Boolean field to decide whether the browser will run on local system or Sauce Labs
	 * @return	WebDriver. @see org.openqa.selenium.WebDriver
	 * @throws Exception
	 */

	public WebDriver getBrowserInstance(String currentTest, Platforms platform, Types type, String browserName, boolean isSauceConnectEnabled, Object profileDtls) throws IOException {
		WebDriver browser = null;
		LOGGER.info("Start[getBrowserInstance] with currentTest={}, Platforms={}, Types={}, browserName={}, isSauceConnectEnabled={} and profile={}", 
				currentTest, platform, type, browserName, isSauceConnectEnabled, profileDtls);
		

		String uiEnvironment = getUIExecutionEnvironment();
		if (uiEnvironment==null || uiEnvironment.equalsIgnoreCase(ICommonConstants.ENVIRONMENT_UI_REMOTE)) {
			SauceLabCredentials sauceLab = configDataProvider.getSauceLabCredentials();
			String onDemandUrl = sauceLab.getOnDemandUrlWithoutSauceConnect();
			if(isSauceConnectEnabled){
				onDemandUrl = sauceLab.getOnDemandUrlWithSauceConnect();
			}

			String sauceURL = StringUtility.appendStrings(	ICimaCommonConstants.URL_HTTP_PREFIX,
															sauceLab.getUserName(),										
															ICimaCommonConstants.COLON,
															sauceLab.getAccessKey(),										
															ICimaCommonConstants.AT_THE_RATE,
															onDemandUrl);
			LOGGER.info("Sauce URL created {} for test {}", sauceURL, currentTest);
			
			DesiredCapabilities Capability = ObjectInitializer.getBrowserCapabilityDAO().findCapability(platform, type, browserName, profileDtls);
			if(Capability==null){
				throw new IllegalStateException("DesiredCapabilities not found!!!");
			}
			Capability.setCapability("name", currentTest+"_"+MiscUtility.getCurrentTimeInMillis());
			browser = new RemoteWebDriver(new URL(sauceURL), Capability);
		} else {
			if(profileDtls instanceof FirefoxProfile ){
				browser = new FirefoxDriver((FirefoxProfile)profileDtls);
			}
			else{
				browser = new FirefoxDriver();
			}
		}
		LOGGER.info("End[getBrowserInstance] completed with result browser {}", browser);
		return browser;
	}
	
	public WebDriver getBrowserInstance(String currentTest, Platforms platform, Types type, String browserName, boolean isSauceConnectEnabled) throws IOException {
		return getBrowserInstance(currentTest, platform, type, browserName, isSauceConnectEnabled, null);
	}

	/**
	 * Returns FirefoxDriver
	 * 
	 * @return WebDriver (type FirefoxDriver). @see org.openqa.selenium.WebDriver
	 * @throws Exception
	 */
	public WebDriver getBrowserInstance() {
		return new FirefoxDriver();
	}
	
	
	public boolean isRemoteBrowser(WebDriver browser) {
		
		return browser instanceof RemoteWebDriver && 
			   getUIExecutionEnvironment().equalsIgnoreCase(ICommonConstants.ENVIRONMENT_UI_REMOTE);
	}

	/**
	 * Switches between different windows in a browser
	 * 
	 * @param browser
	 * 			Web Browser, i.e. WebDriver
	 * @param handle
	 * 			Name of the handle
	 * @return WebDriver
	 * 
	 * @see org.openqa.selenium.WebDriver
	 */
	public WebDriver switchWindow(WebDriver browser, String handle) {
		browser.switchTo().window(handle);
		return browser;
	}


	/**
	 * Reads specific phrase from logs
	 * 
	 * @param dbLogIdentifier
	 * 			log identifier string as described inside log_finders table in database  
	 * @param argumentType
	 * 			Type/key of argument which will be searched in log. @see com.comcast.test.citf.common.util.LogReader#RegexArgument
	 * @param argumentVal
	 * 			Value of argument
	 * @return Value found in log
	 * @throws Exception
	 */
	public synchronized String readLog(String dbLogIdentifier, LogReader.RegexArgument argumentType, String argumentVal){
		String result = null;
		String currentEnvironment = getCurrentEnvironment();
		
		LogFinders searchDtls = ObjectInitializer.getLogFinderDAO().findLogChecker(dbLogIdentifier, currentEnvironment);
		if(searchDtls!=null){
			ServersDAO serverDao = ObjectInitializer.getServersDAO();
			String logReaderString = configDataProvider.getLogReadingMechanism();

			//file reader
			if(logReaderString!=null && LogReader.LOG_READER_FILE.equalsIgnoreCase(logReaderString)){
				String projDir = System.getProperty(ICimaCommonConstants.SYSTEM_PROPERTY_USER_DIRECTORY);
				projDir = projDir.replaceAll(Matcher.quoteReplacement(ICimaCommonConstants.BACKSLASH), ICimaCommonConstants.SLASH);
				String pkFilePath = projDir.replace(StringUtility.getLastTokenFromString(projDir, ICimaCommonConstants.SLASH), ICimaCommonConstants.FILE_PATH_CIMA_SERVER_ACCESS_PRIVATE_KEY);

				List<Servers> servers = serverDao.findServerByTypeAndEnvironment(ICimaCommonConstants.SERVER_TYPE_CIMA_LOG, currentEnvironment);
				if(servers!=null && servers.size()>=1){
					for(Servers server : servers){
						int port = LogReader.DEFAULT_FILE_PORT;
						try{
							port = Integer.parseInt(server.getPort());
						}catch(NumberFormatException nfe){}

						String regex = searchDtls.getRegex();
						LOGGER.info("Before changing regex argument name: {}, val: {} and REGEX: {}", argumentType.getValue(), argumentVal, regex);

						regex = this.perpareLogSearchString(regex, argumentType, argumentVal, currentEnvironment);
						result = logReader.readFromFile(searchDtls.getLogPath().replace(ICimaCommonConstants.DOLLAR_SIGN, String.valueOf(server.getServerPK().getPriority())), server.getHost(), port,
												server.getUserId(), server.getPassword(), regex, pkFilePath);

						if(result!=null){
							break;
						}
					}
				}
			}

			//Splunk
			else{
				List<Servers> servers = ObjectInitializer.getServersDAO().findServerByTypeAndEnvironment(ICimaCommonConstants.SERVER_TYPE_SPLUNK, currentEnvironment);
				if(servers!=null && servers.size()>0 && servers.get(0)!=null){
					Servers server = servers.get(0);

					String qry = searchDtls.getSplunkQry();
					LOGGER.info("Before changing splunk query argument name: {}, val: {} and Query: {}", argumentType.getValue(), argumentVal, qry);

					qry = this.perpareLogSearchString(qry, argumentType, argumentVal, currentEnvironment);

					int port = LogReader.DEFAULT_SPLUNK_PORT;
					try{
						port = Integer.parseInt(server.getPort());
					}catch(NumberFormatException nfe){}

					String splunkTimeZone = SplunkReader.SPLUNK_SERVER_DEFAULT_TIMEZONE;
					String logPath = System.getProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_LOG_PATH);
					if(logPath!=null && logPath.contains(ICimaCommonConstants.LOG_FILE_NAME_CIMA_IDM)) {
						splunkTimeZone = configDataProvider.getIdmServerTimeZone();
					}

					result = logReader.readFromSplunk(server.getHost(), port, server.getUserId(), server.getPassword(), qry, searchDtls.getSplunkKey(), splunkTimeZone);
				}
				else {
					throw new IllegalStateException("No server details found from database!");
				}
			}
		}

		return result;
	}


	/**
	 * Updates test execution status in Sauce Labs
	 * 
	 * @param sessionId
	 * 			Sauce Connect session Id
	 * @param testStatus
	 * 			Test Status
	 * @throws Exception
	 */
	public void updateSauceLabsTestStatus(String sessionId,String testStatus) throws Exception {
		if (sessionId!=null && !sessionId.isEmpty() && ICommonConstants.ENVIRONMENT_UI_REMOTE.equalsIgnoreCase(getUIExecutionEnvironment())) {
			SauceLabCredentials sauceLab = configDataProvider.getSauceLabCredentials();
			SauceREST sauceClient = new SauceREST(sauceLab.getUserName(), sauceLab.getAccessKey());

			if (ICommonConstants.TEST_EXECUTION_STATUS_PASSED.equals(testStatus)) {
				sauceClient.jobPassed(sessionId);
			}
			else if (ICommonConstants.TEST_EXECUTION_STATUS_FAILED.equals(testStatus)) {
				sauceClient.jobFailed(sessionId);
			}
		}
	}
	

	
	protected Map<String, Object> parameters = null;
	
	/**
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 * 
	 * @param mapName
	 * 			Name of the map
	 * @param key
	 * 			Key
	 * @param value
	 * 			Value
	 * @param isMapExist
	 * 			Boolean parameter to decide whether the map is already created
	 * @throws Exception
	 */
	@Deprecated
	public void populateTestMap(String mapName, String key, Object value, boolean isMapExist) throws Exception{
		putValuesInThreadMap(mapName, null, key, value, isMapExist);	
	}
	
	/**
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 * 
	 * @param mapName
	 * 			Name of the map
	 * @param childMap
	 * 			Child Map
	 * @param isMapExist
	 * 			Boolean parameter to decide whether the map is already created
	 * @throws Exception
	 */
	@Deprecated
	public void populateTestMap(String mapName, Map<String, Object> childMap, boolean isMapExist) throws Exception{
		putValuesInThreadMap(mapName, childMap, null, null, isMapExist);
	}
	
	/**
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 * 
	 * @param mapName
	 * 			Name of the map
	 * @return Map value
	 * @throws Exception
	 */
	@Deprecated
	public Map<String, Object> getTestMap(String mapName) throws Exception{
		String threadName = Thread.currentThread().getName();
		return getTestValue(threadName, mapName)!=null ? (Map<String, Object>)getTestValue(threadName, mapName) : null;
	}
	
	/**
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 * 
	 * @param mapName
	 * 			Name of the map
	 * @throws Exception
	 */
	@Deprecated
	public void removeTestMap(String mapName) throws Exception{
		removeTestValue(TEST_PARAM_MAP, mapName);
	}
	
	public String getPageName(Object pageObject) {
		return pageObject!=null?pageObject.getClass().getName():null;
	}
	
	/**
	 * @deprecated Will be removed soon once removing TestNG test layer.
	 * 
	 * @param mapName
	 * 			Name of the map
	 * @param childMap
	 * 			Child Map
	 * @param key
	 * 			Key
	 * @param value
	 * 			Value
	 * @param isMapExist
	 * 			Boolean parameter to decide whether the map is already created
	 * @throws Exception
	 */
	@Deprecated
	private void putValuesInThreadMap(String mapName, Map<String, Object> childMap, String key, Object value, boolean isMapExist) throws Exception{
		String threadName = Thread.currentThread().getName();
		
		if(isMapExist){
			Map<String, Object> parameters = getTestValue(threadName, mapName)!=null ? (Map<String, Object>)getTestValue(threadName, mapName) : null;
			if(parameters==null)
				throw new Exception(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
		}
		else {
			parameters = new HashMap<String, Object>();
		}
		if(childMap!=null){
			parameters.putAll(childMap);
		} else {
			parameters.put(key, value);
		}
		putTestValue(threadName, mapName, parameters);	
	}
	
	/**
	 * An utility method to prepare search string which will be searched in log
	 * 
	 * @param searchStr
	 * 			Search string as fetched from database
	 * @param argumentType
	 * 			Type/key of argument. @see com.comcast.test.citf.common.util.LogReader#RegexArgument
	 * @param argumentVal
	 * 			Value of argument
	 * @param environment
	 * 			Log environment 
	 * @return Prepared log search string
	 * @throws Exception
	 */
	private String perpareLogSearchString(String inputSearchStr, LogReader.RegexArgument argumentType, String argumentVal, String environment){
		String searchStr = inputSearchStr;
		if (searchStr != null) {
			if (argumentType != null && argumentVal != null) {
				String value = argumentVal;
				if (searchStr.contains(ICimaCommonConstants.SPLUNK_PARAM_NAME_DOMAIN) && LogReader.RegexArgument.PHONE_NUMBER.equals(argumentType) && 
					!argumentVal.contains("-") && argumentVal.length() >= 10) {
					value = StringUtility.appendStrings(argumentVal.substring(0, 3), ICommonConstants.DASH, argumentVal.substring(3, 6), 
							ICommonConstants.DASH,argumentVal.substring(6));
				}
				searchStr = searchStr.replace(argumentType.getValue(), value);
			}
			
			if(searchStr.contains(LogReader.RegexArgument.ENVIRONMENT.getValue())) {
				searchStr = searchStr.replace(LogReader.RegexArgument.ENVIRONMENT.getValue(), environment);
			}

			if(searchStr.contains(LogReader.RegexArgument.DOMAIN.getValue())){
				String domain = ICimaCommonConstants.SPLUNK_PARAM_VALUE_DOMAIN_COMCAST;
				if(AbstractExecutionController.Domain.XFINITY.getValue().equals(getCurrentDomain())) {
					domain = ICimaCommonConstants.SPLUNK_PARAM_VALUE_DOMAIN_XFINITY;
				}
				searchStr = searchStr.replace(LogReader.RegexArgument.DOMAIN.getValue(), domain);
			}
		}
		return searchStr;
	}

	
	@Autowired
	private TestErrorCache testErrorCache;
	
	@Autowired
	private MapCache mapCache;

	@Autowired
	private StringCache stringCache;
	
	@Autowired
	private LogReader logReader;	
	
	@Autowired
	private ConfigurationDataProvider configDataProvider;
	
	
	private static final String EXECUTE_API_TEST_BEAN_NAME = "apiTestExecuter";
	private static final Logger LOGGER = LoggerFactory.getLogger(CitfTestInitializer.class);
}
