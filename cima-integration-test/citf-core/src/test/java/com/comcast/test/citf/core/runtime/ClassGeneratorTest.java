package com.comcast.test.citf.core.runtime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

public class ClassGeneratorTest {
	
	private String rootDirectory;
	private String featureFileDirectory;
	private String runnerClassFileDirectory;
	private String fullClassPath;
	
	private static final String TEMP_DIRECTORY = "java.io.tmpdir";
	private static final String CLASS_NAME = "ParallelThread1Runner";
	private static final String THREAD_NAME = "ParallelThread1";
	private static final String FILE_CONTENT_PART_ONE = "import org.junit.runner.RunWith;import cucumber.api.CucumberOptions;import cucumber.api.junit.Cucumber;"
									           + "@RunWith(Cucumber.class)@CucumberOptions( features={\"";
	
	private static final String FILE_CONTENT_PART_TWO = "/Cucumber/\"} ,"
									           + "glue={\"com/comcast/cima/test/api/cucumber/steps\"} ,tags={\"ParallelThread1\"} ,plugin = {\"pretty\", "
									           + "\"html:target/cucumberParallelThread1Runner\", \"json:target/cucumberParallelThread1Runner/cucumber-result.json\"} ,"
									           + "strict=true)public class ParallelThread1Runner {}";
	
	@Before
	public void setup() {
		
		rootDirectory = System.getProperty(TEMP_DIRECTORY);
		featureFileDirectory = rootDirectory + "/Cucumber/";
		runnerClassFileDirectory = rootDirectory + "/Runner/";
	}

	@Test
	public void testGenerateRunnerClass() {
		
		List<String> features = new ArrayList<>();
		features.add(featureFileDirectory);
		
		List<String> glues = new ArrayList<>();
		glues.add(ICimaCommonConstants.CUCUMBER_STEP_DEFINATION_PACKAGE_API);
		
		try{
			FileUtility.createDirectory(runnerClassFileDirectory);
		}catch(IOException e){
			Assert.fail("Exception occurred while deleting temp files " + e);
		}
		
		ClassGenerator.generateRunnerClass(CLASS_NAME, runnerClassFileDirectory, THREAD_NAME, features, glues);
		
		fullClassPath = StringUtility.appendStrings(runnerClassFileDirectory, CLASS_NAME, ICommonConstants.EXTN_JAVA);
		
		assertThat(
				Files.exists(Paths.get(fullClassPath)),
				is(true));
		
		try{
			String fileContent = FileUtility.getFileContentAsString(fullClassPath);
			String fileContentToCompare = FILE_CONTENT_PART_ONE + rootDirectory + FILE_CONTENT_PART_TWO;
			assertThat(
					fileContent, 
					is(fileContentToCompare));
		}catch(IOException e){
			Assert.fail("Exception occurred while doing IO operations " + e);
		}
	}
	
	@After
	public void tearDown()  {
		try{
			Path filePath = Paths.get(fullClassPath);
			Files.delete(filePath);
			Files.delete(filePath.getParent());
		}catch(IOException e){
			Assert.fail("Exception occurred while deleting temp files " + e);
		}
	}
}
