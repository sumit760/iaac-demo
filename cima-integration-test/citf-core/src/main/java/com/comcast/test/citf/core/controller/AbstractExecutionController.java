package com.comcast.test.citf.core.controller;


import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.helpers.FileModifier;
import com.comcast.test.citf.common.reader.JsonReader;
import com.comcast.test.citf.common.reader.PropertyReader;
import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.runtime.ExecutionEngine;
import com.comcast.test.citf.core.setup.cima.InitialDataLoader;
import com.comcast.test.citf.core.util.AsyncExecutor;
import com.comcast.test.citf.core.util.ObjectDestroyer;


/**
 * This abstract class should be inherited by other Controller classes in CITF to have the common controller functionality.
 * 
* @author Sumit Pal (spal004c)
* @since June 2015
*
*/
public abstract class AbstractExecutionController {

	protected static AsyncExecutor asyncExecutor = null;
	protected static ExecutionEngine engine = null;
	protected static FileModifier fileModifier = null;
	protected static Set<String> threads = null;

	/**
	 * Populates database when CITF is running in LOCAL mode.
	 * 
	 * @throws 	IOException
	 */
	protected static void loadData() throws IOException{
		String runMode = PropertyReader.getProperty(ICimaCommonConstants.PROPERTY_FILE_CITF_CORE_CONFIGURATION, ConfigurationDataProvider.ConfigurationPropKeys.CITF_RUN_MODE.getValue());
		
		if(ICimaCommonConstants.CITF_RUN_MODE_LOCAL.equalsIgnoreCase(runMode)){
			InitialDataLoader.load();
		}
	}


	/**
	 * Cleans all initialized resources.
	 */
	protected static void destroy() {
		generateCucumberReport();
		FileUtility.deleteAll(engine.getTempFilesRootDirectory());
		ObjectDestroyer.destroyAllCaches();
		CoreContextInitilizer.destroyContext();
	}

	/**
	 * Runs CITF engine after initializing required classes
	 * 
	 * @param category
	 * 			Test category (as given in Maven build command)  
	 * @param type
	 * 			Test type (as given in Maven build command) 
	 * @param configCache
	 * 			Instance of Configuration Cache  @see com.comcast.test.citf.core.cache.ConfigCache
	 * @return No. of threads created for executing test scenarios
	 */
	protected static int execute(String category, String type, ICitfCache configCache){
		int threadCount = 0;
		try{
			asyncExecutor = (AsyncExecutor)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_ASYNC_EXECUTER);
			engine = (ExecutionEngine)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_EXECUTION_ENGINE);
			fileModifier = (FileModifier)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_FILE_MODIFIER);

			int maxAllowedThreads = Integer.parseInt(configCache.getString(ConfigurationDataProvider.ConfigurationPropKeys.NUMBER_OF_MAX_PARALLEL_THREADS.getValue(), ICimaCommonConstants.ENVIRONMENT_ALL));
			threadCount = engine.executeTests(category, type, maxAllowedThreads, System.getProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_TEMP_FILE_PATH_CITF), asyncExecutor, fileModifier);
			threads = engine.getThreads();

		}catch(Exception t){
			LOGGER.error("Error occurred while executing async runner from controller ", t);
		}
		return threadCount;
	}

	
	/**
	 *  Generates accumulated Cucumber test report by merging all individual runner thread's report
	 */
	private static void generateCucumberReport(){
		try{
			String targetRootDirectory = System.getProperty(ICommonConstants.SYSTEM_PROPERTY_USER_DIRECTORY)+ICommonConstants.DIRECTORY_NAME_TARGET;

			String[] inputs = new String[threads.size()];
			int index = 0;
			for(String thread : threads){
				String filePath = StringUtility.appendStrings(targetRootDirectory, DIRECTORY_NAME_PREFIX_CUCUMBER, thread, JSON_FILE_NAME);
				String fileContent = null;
				
				try{
					fileContent = FileUtility.getFileContentAsString(filePath);
				}catch(IOException ioe){
					LOGGER.error("{} content is not readable while generating report due to : ",filePath, ioe);
				}

				if(!StringUtility.isStringEmpty(fileContent)){
					inputs[index] = fileContent;
					index++;
				}
			}

			if(inputs.length>0){
				String fullReportContent = JsonReader.generateMergerdCucumberReport(inputs);

				if(fullReportContent!=null){
					String destRoot = StringUtility.appendStrings(targetRootDirectory, DIRECTORY_NAME_PREFIX_CUCUMBER , ICommonConstants.SLASH);
					FileUtility.createDirectory(destRoot);
					FileUtility.createFile(destRoot + JSON_FILE_NAME, fullReportContent);
					LOGGER.info("Successfully generated full cucumber report JSON file.");
				}
			}
		}
		catch(Exception e){
			LOGGER.error("### Post run Error :: Not able to generate cucumber report due to ", e);
		}
	}

	/**
	 * Test type enumeration
	 */
	public enum TestType{
		API(ICommonConstants.TEST_TYPE_API),
		UI(ICommonConstants.TEST_TYPE_UI),
		ALL(ICommonConstants.TEST_TYPE_ALL);

		private final String value;
        TestType(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Test category enumeration
	 */
	public enum TestCategory{
		SMOKE(ICommonConstants.TEST_CATEGORY_SMOKE),
		INTEGRATION(ICommonConstants.TEST_CATEGORY_INTEGRATION),
		WIP(ICommonConstants.TEST_CATEGORY_WIP),
		TEST(ICommonConstants.TEST_CATEGORY_TEST);

		private final String value;
        TestCategory(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Test environment enumeration
	 */
	public enum Environment{
		QA(ICommonConstants.ENVIRONMENT_QA),
		DEV(ICommonConstants.ENVIRONMENT_DEV),
		PERF(ICommonConstants.ENVIRONMENT_PERF),
		STAGE(ICommonConstants.ENVIRONMENT_STAGE),
		PROD(ICommonConstants.ENVIRONMENT_PROD);

		private final String value;
        Environment(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }

		public static Environment fromString(String input) {
			for (Environment e : values()) {
				if (e.getValue().equalsIgnoreCase(input)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * UI execution environment enumeration
	 */
	protected enum UIExecutionEnvironment{
		LOCAL(ICommonConstants.ENVIRONMENT_UI_LOCAL),
		REMOTE(ICommonConstants.ENVIRONMENT_UI_REMOTE);

		private final String value;
        UIExecutionEnvironment(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Domain enumeration
	 */
	public enum Domain{
		COMCAST(ICommonConstants.URI_DOMAIN_COMCAST),
		XFINITY(ICommonConstants.URI_DOMAIN_XFINITY);

		private final String value;
        Domain(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Checks whether input parameter belongs to TestType enumeration
	 * 
	 * @param input
	 * 			Input test type
	 * @return TestType enumeration value if matches, otherwise null
	 */
	public static String isInTestType(String input){
		if(input!=null){
			for(TestType val : TestType.values()){
				if(val.getValue().equalsIgnoreCase(input)){
					return val.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether input parameter belongs to TestCategory enumeration
	 * 
	 * @param input
	 * 			Input test category
	 * @return TestCategory enumeration value if matches, otherwise null
	 */
	public static String isInTestCategory(String input){
		if(input!=null){
			for(TestCategory val : TestCategory.values()){
				if(val.getValue().equalsIgnoreCase(input)){
					return val.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether input parameter belongs to Environment enumeration
	 * 
	 * @param input
	 * 			Input environment
	 * @return Environment enumeration value if matches, otherwise null
	 */
	public static String isInEnvironment(String input){
		Environment environment = Environment.fromString(input);
		return environment == null ? null : environment.getValue();
	}

	/**
	 * Checks whether input parameter belongs to Environment enumeration
	 * 
	 * @param 	input
	 * 				Input UI execution environment
	 * @return 	Environment enumeration value if matches, otherwise null
	 */
	public static String isInUIExecutionEnv(String input){
		if(input!=null){
			for(UIExecutionEnvironment val : UIExecutionEnvironment.values()){
				if(val.getValue().equalsIgnoreCase(input)){
					return val.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Checks whether input parameter belongs to Domain enumeration
	 * 
	 * @param input
	 * 			Input domain
	 * @return Domain enumeration value if matches, otherwise null
	 */
	public static String isInDomain(String input){
		if(input!=null){
			for(Domain val : Domain.values()){
				if(val.getValue().equalsIgnoreCase(input)){
					return val.getValue();
				}
			}
		}
		return null;
	}

	private static final String DIRECTORY_NAME_PREFIX_CUCUMBER = "cucumber";
	private static final String JSON_FILE_NAME = "/cucumber-result.json";

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecutionController.class);
}
