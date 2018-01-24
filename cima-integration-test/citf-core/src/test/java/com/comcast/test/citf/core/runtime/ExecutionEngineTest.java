package com.comcast.test.citf.core.runtime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.helpers.FileModifier;
import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.core.util.AsyncExecutor;

public class ExecutionEngineTest {

	private static final String TEMP_DIRECTORY = "java.io.tmpdir";
	
	private ExecutionEngine engine;
	private AsyncExecutor asyncExecutor;
	private FileModifier fileModifier;
	private String rootDirectory;
	private static final String EXCEPTION_MESSAGE = "Input(s) is(are) null or invalid!";
	
	@Before
	public void setup() {
		
		engine = new ExecutionEngine();
		asyncExecutor = new AsyncExecutor();
		fileModifier = new FileModifier();
		rootDirectory = System.getProperty(TEMP_DIRECTORY) + "/unittest/";
		try{
			FileUtility.createDirectory(rootDirectory);
		}catch(IOException e){
			Assert.fail("Exception occurred while running test " + e);
		}
	}
	
	
	@Test
	public void testExecuteTestsWithEmptyCategory() {
		
		//Empty category
		try {
			engine.executeTests("", "api", 1, rootDirectory, asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}

	}
	
	
	@Test
	public void testExecuteTestsWithEmptyTestType() {
		
		//Empty test type
		try {
			engine.executeTests("Integration", "", 1, rootDirectory, asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}
		
	}
	
	
	@Test
	public void testExecuteTestsWithInvalidTestType() {
		
		//Invalid test type
		try {
			engine.executeTests("Integration", "invalid", 1, rootDirectory, asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Input testType is invalid!"));
		}
		
	}
	
	
	@Test
	public void testExecuteTestsWithZeroThreads() {
		
		//maxAllowedThreads = 0
		try {
			engine.executeTests("Integration", "api", 0, rootDirectory, asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}

	}
	
	
	@Test
	public void testExecuteTestsWithNegativeNoOfThreads() {
	
		//maxAllowedThreads < 0
		try {
			engine.executeTests("Integration", "api", -1, rootDirectory, asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}
	}
	
	@Test
	public void testExecuteTestsWithBlankExecutionPath() {
		
		//Empty temp file execution path
		try {
			engine.executeTests("Integration", "api", 1, "", asyncExecutor, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}
	}
	
	
	@Test
	public void testExecuteTestsWithNullExecutor() {
		
		//null AsyncExecutor
		try {
			engine.executeTests("Integration", "api", 1, rootDirectory, null, fileModifier);
		} catch (Exception e) {
			assertThat(e.getMessage(), is(EXCEPTION_MESSAGE));
		}

		
	}
	
	
	
	@Test
	public void testExecuteTestsTestTypeAPI() {
		 
		//TEST_TYPE = api
		try{
			int ret = engine.executeTests("Integration", "api", 1, rootDirectory, asyncExecutor, fileModifier);
		
			assertThat(
				ret,
				is(0));
		}catch(IOException e){
			Assert.fail("Exception occurred while running test " + e);
		}
	}
	
	@Test
	public void testExecuteTestsTestTypeUI() {
		
		//TEST_TYPE = ui
		try{
			int ret = engine.executeTests("Integration", "ui", 1, rootDirectory, asyncExecutor, fileModifier);
		
			assertThat(
				ret,
				is(0));
		}catch(IOException e){
			Assert.fail("Exception occurred while running test " + e);
		}
	}
	
	
	@Test
	public void testExecuteTestsTestTypeAll() {
		
		try{
		//TEST_TYPE = all
		int ret = engine.executeTests("Integration", "all", 1, rootDirectory, asyncExecutor, fileModifier);
		
		assertThat(
				ret,
				is(0));
		}catch(IOException e){
			Assert.fail("Exception occurred while running test " + e);
		}
	}
	
	
	
	@After
	public void tearDown() {
		
		try{
		Path directory = Paths.get(rootDirectory);
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   Files.delete(file);
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				   Files.delete(dir);
				   return FileVisitResult.CONTINUE;
			   }

		   });
		}catch(IOException e){
			Assert.fail("Exception occurred while running test " + e);
		}
	}
}
