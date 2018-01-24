package com.comcast.test.citf.core.runtime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.helpers.FileModifier;
import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.util.AsyncExecutor;

/**
 * This class calculates the No. of threads to be run and No. of cucumber test scenarios for each thread based on the input parameters. It then configures 
 * the executable test scenarios by putting the specific thread annotation in the scenario. It creates and loads the Cucumber runner classes using ClassGenerator
 * class. Finally it calls the AsyncExecutor to execute all the threads.
 * 
 * It supports parallel as well as sequential execution of test scenarios.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
*/
public class ExecutionEngine {

	private Map<String, Integer> fileScenarioCountMap = null;
	private Map<String, Integer> sequentialScenarioCountMap = null;
	private Map<String, String> srcFilePathWithNameMap = null;
	private Map<String, String> threadWithRunnerMap = null;
	private List<String> threadAnnos = null;
	String rootDirectory = null;

	private int scenarioCount = 0;
	private int threadCount = 0;
	private String srcIdentifierAnno = null;

	private String featureFileDirectory = null;
	private String runnerClassFileDirectory = null;

/**
 * This is the starting point to calculate, configure and run tests. 
 * This returns the No of threads forked to run all the valid test scenarios as per given inputs.
 * 
 * @param category 	
 * 		  	The category as given in the input of CITF execution
 * @param testType 	
 * 			The test type as given in the input of CITF execution
 * @param maxAllowedThreads 	
 * 			Maximum No. of threads to be created for test case execution
 * @param tempExecutionFilePath		
 * 			This is a directory path where modified cucumber files and the runner classes will be generated and kept temporarily
 * @param executor	
 * 			Instance of AsyncExecutor 
 * 			@see  com.comcast.test.citf.core.util.AsyncExecutor
 * @param fileModifier 
 * 			Instance of FileModifier 
 * 			@see  com.comcast.test.citf.core.util.AsyncExecutor
 * @return No. of threads generated for executing test scenarios
 * @throws IOException
 */
	public int executeTests(	String category,
								String testType,
								int maxAllowedThreads,
								String tempExecutionFilePath,
								AsyncExecutor executor,
								FileModifier fileModifier) throws IOException{

		if(StringUtility.isStringEmpty(category) || StringUtility.isStringEmpty(testType) || maxAllowedThreads<=0 || StringUtility.isStringEmpty(tempExecutionFilePath) || executor == null){
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}

		srcIdentifierAnno = ICommonConstants.AT_THE_RATE + category;

		rootDirectory = tempExecutionFilePath + MiscUtility.getCurrentTimeInMillis();
		featureFileDirectory = rootDirectory + CUCUMBER_FILEPATH_SUFFIX;
		runnerClassFileDirectory = rootDirectory + RUNNER_FILEPATH_SUFFIX;
		FileUtility.createDirectory(featureFileDirectory);
		FileUtility.createDirectory(runnerClassFileDirectory);

		Set<String> directoryPaths = new HashSet<>();
		List<String> glues = new ArrayList<>();

		directoryPaths.add(ICimaCommonConstants.CUCUMBER_FEATURE_PACKAGE_COMMON_PATH);
		glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_COMMON);

		if(ICimaCommonConstants.TEST_TYPE_API.equalsIgnoreCase(testType)){
			directoryPaths.add(ICimaCommonConstants.CUCUMBER_FEATURE_PACKAGE_API_PATH);
			glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_API);
		}
		else if(ICimaCommonConstants.TEST_TYPE_UI.equalsIgnoreCase(testType)){
			directoryPaths.add(ICimaCommonConstants.CUCUMBER_FEATURE_PACKAGE_UI_PATH);
			glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_UI);
		}
		else if(ICimaCommonConstants.TEST_TYPE_ALL.equalsIgnoreCase(testType)){
			directoryPaths.add(ICimaCommonConstants.CUCUMBER_FEATURE_PACKAGE_API_PATH);
			directoryPaths.add(ICimaCommonConstants.CUCUMBER_FEATURE_PACKAGE_UI_PATH);
			
			glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_API);
			glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_UI);
		}
		else
			throw new IllegalArgumentException("Input testType is invalid!");

		this.calculate(directoryPaths, maxAllowedThreads);
		this.configure(fileModifier);
		this.run(glues, executor);

		if(sequentialScenarioCountMap != null){
			threadCount = threadCount + 1;
		}

		return threadCount;
	}


	/**
	 * 
	 * @return set of thread names generated for test execution
	 */
	public Set<String> getThreads(){
		Set<String> threads = null;

		if(threadAnnos!=null && threadWithRunnerMap!=null){
			threads = new HashSet<>();

			for(String key: threadAnnos){
				threads.add(threadWithRunnerMap.get(key));
			}
		}

		return threads;
	}


	/**
	 * 
	 * @return set of thread names generated for test execution
	 */
	public String getTempFilesRootDirectory(){
		return rootDirectory;
	}



	//********************************************** Private variables & methods ********************************************

	/**
	 * Calculates the No of test scenarios need to be executed by reading all the cucumber feature files. It also considers sequential test scenarios if presents. 
	 * 
	 * @param directoryPaths	
	 * 			Directory path/package of cucumber feature files
	 * @param maxThreadCount	
	 * 			No. of threads generated for executing test scenarios
	 * @return calculation status
	 */
	private boolean calculate(Set<String> directoryPaths, int maxThreadCount){
		boolean result = false;
		this.scenarioCount = 0;
		this.threadCount = 0;
		int sequentialLoad = 0;

		try{
			for(String directoryPath : directoryPaths){
				if(!StringUtility.isStringEmpty(directoryPath)){
					Set<String> fileNames = FileUtility.findFileNamesInDirectory(directoryPath);
					if(fileNames!=null && !fileNames.isEmpty()){
						for(String fileName: fileNames){
							if(!StringUtility.isStringEmpty(fileName)){
								String filePath = StringUtility.appendStrings(directoryPath, ICommonConstants.SLASH, fileName);

								int count = FileUtility.countOccuranceInFile(filePath, false, srcIdentifierAnno);
								if(count>0){
									int sequentialCount = FileUtility.countOccuranceInFile(filePath, true, srcIdentifierAnno, ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL);

									if(sequentialCount>0){
										if(sequentialScenarioCountMap == null){
											sequentialScenarioCountMap = new HashMap<>();
										}

										sequentialScenarioCountMap.put(filePath, sequentialCount);
										count = count - sequentialCount;
										sequentialLoad = sequentialLoad + sequentialCount;
									}

									if(fileScenarioCountMap == null){
										fileScenarioCountMap = new HashMap<>();
									}

									if(count>0){
										fileScenarioCountMap.put(filePath, count);
										this.scenarioCount = this.scenarioCount + count;
									}
									else if(sequentialCount>0){
										fileScenarioCountMap.put(filePath, -1);
									}

									if(srcFilePathWithNameMap == null){
										srcFilePathWithNameMap = new HashMap<>();
									}

									srcFilePathWithNameMap.put(filePath, fileName);
								}
							}
						}
					}
				}
			}

			if(this.scenarioCount<=0 && sequentialScenarioCountMap ==null){
				throw new IllegalStateException("No scenario found while screening feature directory paths["+directoryPaths.toString()+"]!");
			}

			if(sequentialScenarioCountMap != null){
				maxThreadCount = maxThreadCount - 1;
				LOGGER.info("There is a SEQUENTIAL THREAD which will run with load {}", sequentialLoad);
			}

			this.threadCount = this.scenarioCount < maxThreadCount ? this.scenarioCount : maxThreadCount;

			if(this.scenarioCount>0){
				LOGGER.info("There are {} PARALLEL THREADS which will be run for {} scenarios with an average load {}",
						this.threadCount, this.scenarioCount, this.scenarioCount / this.threadCount);
			}

			result = true;
		}
		catch(Exception e){
			LOGGER.error("### Engine Error :: Not able to calculate", e);
		}

		LOGGER.info("Engine calculation is {}", result ? "Successfully completed." : "completed with Error!");
		return result;
	}


	/**
	 * Copies all the required test scenarios and write those with appropriate thread annotation (i.e. a test scenario should have an annotation
	 * with name of the thread which is going to execute it. 
	 * 
	 * @param fileModifier 
	 * 		  	The instance of FileModifer class which is responsible to perform the file modification operation 
	 * 		  	@see com.comcast.test.citf.common.helpers.FileModifier
	 * @return status of engine configuration
	 */
	private boolean configure(FileModifier fileModifier){
		boolean isConfigured = false;

		threadAnnos = new ArrayList<>();
		threadWithRunnerMap = new HashMap<>();

		try{
			int count = 1;
			while(count<=this.threadCount){
				String threadName = FEATURE_TAG_PREFIX_FOR_PARALLEL_THREAD + count;
				threadAnnos.add(threadName);
				threadWithRunnerMap.put(threadName, StringUtility.appendStrings(PARALLEL_THREAD_RUNNER_NAME_PREFIX, String.valueOf(count), RUNNER_NAME_SUFFIX));
				count++;
			}

			if(sequentialScenarioCountMap != null){
				String threadName = FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD;
				threadAnnos.add(threadName);
				threadWithRunnerMap.put(threadName, StringUtility.appendStrings(SEQUENTIAL_THREAD_RUNNER_NAME_PREFIX, RUNNER_NAME_SUFFIX));
			}

			int annoIndex = 0;
			for(Map.Entry<String, Integer> entry: fileScenarioCountMap.entrySet()){
				String feature = entry.getKey();
				int scenarios = entry.getValue();
				String destFilePath = StringUtility.appendStrings(featureFileDirectory, srcFilePathWithNameMap.get(feature));
				LinkedList<FileModifier.ModifyInstruction> modifyInstructions = new LinkedList<>();

				//For Sequential flow
				if(sequentialScenarioCountMap !=null && sequentialScenarioCountMap.get(feature)!=null){
					FileModifier.ModifyInstruction instruction = fileModifier.new ModifyInstruction( ICommonConstants.BLANK_SPACE_STRING + FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD,
																									 FileModifier.OperationType.ADD,
																								 	 sequentialScenarioCountMap.get(feature),
																									 new String[]{StringUtility.appendStrings(srcIdentifierAnno, ICommonConstants.COMMA, ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL)},
																									 null);
					modifyInstructions.add(instruction);
				}

				//For parallel flow
				if(scenarios>0){
					int threadCapacity = this.getCurrentThreadCapacity(annoIndex);
					if(scenarios<threadCapacity){
						threadCapacity = threadCapacity - scenarios;
						FileModifier.ModifyInstruction instruction = fileModifier.new ModifyInstruction( ICommonConstants.BLANK_SPACE_STRING + threadAnnos.get(annoIndex),
																									 FileModifier.OperationType.ADD,
																									 FileModifier.MAX_COUNT_UNLIMITED,
																									 new String[]{srcIdentifierAnno},
																									 new String[]{ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL},
																									 fileModifier.getNextExecutionSequence());

						modifyInstructions.add(instruction);
						fileModifier.modifyFile(feature, destFilePath, modifyInstructions, false);
					}

					else{
						while(scenarios>=0){
							if(scenarios>=threadCapacity){
								FileModifier.ModifyInstruction instruction = fileModifier.new ModifyInstruction( ICommonConstants.BLANK_SPACE_STRING + threadAnnos.get(annoIndex),
									 																		 FileModifier.OperationType.ADD,
									 																		 threadCapacity,
									 																		 new String[]{srcIdentifierAnno},
									 																		 new String[]{ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL},
									 																		 fileModifier.getNextExecutionSequence());
								modifyInstructions.add(instruction);

								scenarios = scenarios - threadCapacity;
								annoIndex++;
								if(annoIndex<this.threadCount) {
									threadCapacity = this.getCurrentThreadCapacity(annoIndex);
								} else {
									break;
								}
							}
							else{
								threadCapacity = threadCapacity - scenarios;
								FileModifier.ModifyInstruction instruction = fileModifier.new ModifyInstruction( ICommonConstants.BLANK_SPACE_STRING + threadAnnos.get(annoIndex),
									 																		 FileModifier.OperationType.ADD,
									 																		 scenarios,
									 																		 new String[]{srcIdentifierAnno},
									 																		 new String[]{ICommonConstants.TEST_EXECUTION_TYPE_SEQUENTIAL},
									 																		 fileModifier.getNextExecutionSequence());
								modifyInstructions.add(instruction);

								scenarios = 0;
								break;
							}
						}
						fileModifier.modifyFile(feature, destFilePath, modifyInstructions, false);
					}
				}
				else{	//when there is only sequential flow, no parallel flow.
					if(modifyInstructions.size()>0) {
						fileModifier.modifyFile(feature, destFilePath, modifyInstructions, false);
					}
				}
			}

			isConfigured = true;
		}
		catch(Exception e){
			LOGGER.error("### Engine Error :: Not able to configure", e);
			isConfigured = false;
		}

		LOGGER.info("Engine configuration is {}", threadAnnos.size()>0 ? "Successful." : "Failed.");
		return isConfigured;
	}



	/**
	 * Initiates test case execution.
	 * It first generate runners classes for all test executer threads and load those classes. Then it call AsyncExecutor to execute a asynchronous thread
	 * for each parallel execution.
	 * 
	 * @param glues 
	 * 			List of glues
	 * @param executor 
	 * 			Instance of AsyncExecutor 
	 * 			@see com.comcast.test.citf.core.util.AsyncExecutor
	 */
	@SuppressWarnings("rawtypes")
	protected void run(List<String> glues, AsyncExecutor executor){
		List<String> features = new ArrayList<>();
		features.add(featureFileDirectory);

		try{
			//For sequential thread execution
			if(threadWithRunnerMap.get(FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD)!=null){
				String fileName = threadWithRunnerMap.get(FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD);
				String fullPath = StringUtility.appendStrings(runnerClassFileDirectory, fileName, ICommonConstants.EXTN_JAVA);

				Class runner = ClassGenerator.generateRunnerClass(fileName, runnerClassFileDirectory, FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD, features, glues);
				if(runner == null){
					throw new IllegalStateException("Not able to load sequential class "+fullPath);
				}

				LOGGER.info("{} has been loaded successfully and its ready to run.", fullPath);

				executor.execute(runner);

				LOGGER.info("{} has been pushed for sequential execution", fullPath);
			}

			//For parallel thread execution
			for(String threadAnno : threadAnnos){

				if(!FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD.equals(threadAnno)){	//excluding the sequential one

					String fileName = threadWithRunnerMap.get(threadAnno);
					String fullPath = StringUtility.appendStrings(runnerClassFileDirectory, fileName, ICommonConstants.EXTN_JAVA);

					Class runner = ClassGenerator.generateRunnerClass(fileName, runnerClassFileDirectory, threadAnno, features, glues);
					if(runner == null){
						throw new IllegalStateException("Not able to load parallel class "+fullPath);
					}

					LOGGER.info("{} has been loaded successfully and its ready to run.", fullPath);
					executor.execute(runner);
					LOGGER.info("{} has been pushed for parallel execution", fullPath);
				}
			}
		}
		catch(Exception e){
			LOGGER.error("### Engine Error :: Not able to run", e);
		}
	}

	/**
	 * Calculates maximum No. of tests to be executed by a single thread
	 * 
	 * @param currentIndex 
	 * 			This is the No. of current thread, it helps to calculate the remainder test scenarios
	 * @return calculated thread capacity
	 */
	private int getCurrentThreadCapacity(int currentIndex){
		int reminder = this.scenarioCount % this.threadCount;
		int actual = (this.scenarioCount - reminder) / this.threadCount;

		return (currentIndex+1 <=reminder) ? actual+1 : actual;
	}

	private static final String FEATURE_TAG_PREFIX_FOR_PARALLEL_THREAD = "@ParallelThread";
	private static final String PARALLEL_THREAD_RUNNER_NAME_PREFIX = "ParallelThread";
	public static final String RUNNER_NAME_SUFFIX = "Runner";

	private static final String CUCUMBER_FILEPATH_SUFFIX = "/Cucumber/";
	private static final String RUNNER_FILEPATH_SUFFIX = "/Runner/";

	public static final String FEATURE_TAG_PREFIX_FOR_SEQUENTIAL_THREAD = "@SequentialThread";
	private static final String SEQUENTIAL_THREAD_RUNNER_NAME_PREFIX = "SequentialThread";

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionEngine.class);
}
