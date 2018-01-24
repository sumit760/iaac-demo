package com.comcast.test.citf.common.testng;

/**
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 * This class will be used to initialize and execute TestNG based API test cases.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;

/**
 * Class to execute TestNg tests invoked dynamically from Cucumber step definition file.
 * 
 * @author spal004c
 *
 */
public class ExecuteApiTests {
	
	/**
	 * Returns the TestNg instance.
	 * 
	 * @return TestNg instance.
	 */
	public TestNG getTestNg() {
		return testNg;
	}

	/**
	 * Sets the TestNg instance.
	 * 
	 * @param testNg TestNg instance.
	 */
	public void setTestNg(TestNG testNg) {
		this.testNg = testNg;
	}

	/**
	 * Sets test class name to execute.
	 * 
	 * @param testClassName Test class name.
	 */
	public void setTestClassName(String testClassName) {
		this.testClassName = testClassName;
	}

	/**
	 * Sets test name.
	 * 
	 * @param testName Test name.
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * Sets test parameter.
	 * 
	 * @param parameter Test parameter.
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Add test methods to execute.
	 * 
	 * @param methodName Test methods.
	 */
	public void addMethodNames(String methodName) {
		if(methodNames == null)
			methodNames = new ArrayList<String>();
		
		methodNames.add(methodName);
	}
	
	/**
	 * Executes TestNG tests.
	 * 
	 * @param classId
	 * 					The unique class identifier.
	 * @param classParams
	 * 					The class test class parameters.
	 */
	public void executeTests(String classId, Map<String, String> classParams){
		logger.info("Start executing TestNG tests for '"+testName+"'["+testClassName+"] with "+parameter);
		
		try{
			   
			Map<String, String> testngParams = new HashMap<String, String>();
	        testngParams.put("prams", parameter);
	        
	        initializeXmlSuite();
	        initializeXmlTest(this.testName);
	        xmlTest.setParameters(testngParams);
	        
	        XmlClass tstCls = new XmlClass(testClassName);
	        
	        if(methodNames!=null && !methodNames.isEmpty()){
	        	List<XmlInclude> includes = new ArrayList<XmlInclude>();
	        	for(String mName : methodNames)
	        		includes.add(new XmlInclude(mName));
	        		
	        	tstCls.setIncludedMethods(includes);
	        	logger.info("Methods included for testing are "+methodNames);
	        }
	        
	        if(classId != null){
	        	Map<String, String> paramMap = null;
	        	
	        	if(classParams!=null && !classParams.isEmpty())
	        		paramMap = classParams;
	        	else
	        		paramMap = new HashMap<String, String>();
	        	
	        	paramMap.put(ICommonConstants.TEST_PARAMETER_CLASS_ID, classId);
	        	tstCls.setParameters(paramMap);
	        }
	        	
	        List<XmlClass> testClasses = new ArrayList<XmlClass>();
	        testClasses.add(tstCls);
	        xmlTest.setXmlClasses(testClasses);
	        
	        List<XmlTest> listOfTests = new ArrayList<XmlTest>();
	        listOfTests.add(xmlTest);
	        
			xSuite.setTests(listOfTests);
			xSuite.setAllowReturnValues(true);

			List<XmlSuite> listOfSuites = new ArrayList<XmlSuite>();
			listOfSuites.add(xSuite);
		    this.getTestNg().setXmlSuites(listOfSuites);
		     
		    this.getTestNg().run();
	        
		}
		catch(Exception e){
			logger.error("Error occured while initializing Test Objects : "+MiscUtility.getStackTrace(e));
			e.printStackTrace();
		}
		finally{
			methodNames = null;
			xSuite = null;
			xmlTest = null;
			parameter = null;
		}
		
		logger.info("Execution of TestNG tests for '"+testName+"'["+testClassName+"] is over.");
		logger.info("==================================================================================\n\n\n");
	}
	
	
	
	/************************** Private methods & variables ********************************/
	
	//Spring dependencies
	private TestNG testNg = null;
	
	private static int suiteCounter = 1;
	private static int testCounter = 1;
	
	private XmlTest xmlTest = null;
	private XmlSuite xSuite = null;
	private String testClassName = null;
	private String testName = null;
	private String parameter = null;
	private List<String> methodNames = null;
	
	private Map<String, String> classParams = null;
	private String classId = null;
	
	private synchronized void initializeXmlTest(String testName) {
		xmlTest = new XmlTest(xSuite);
		xmlTest.setName(testName+testCounter);
		testCounter++;
	}

	private void initializeXmlSuite() {
		if(xSuite == null){
			synchronized(this){
				xSuite = new XmlSuite();
				xSuite.setName("Suite"+suiteCounter);
				suiteCounter++;
			}
		}
	}
	
	private List<String> getMethodNames() {
		return methodNames;
	}

	private static Logger logger = LoggerFactory.getLogger(ExecuteApiTests.class);
}
