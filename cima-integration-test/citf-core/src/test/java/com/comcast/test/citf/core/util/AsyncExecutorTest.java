package com.comcast.test.citf.core.util;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.CoreContextInitilizer;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

public class AsyncExecutorTest {
	
	private AsyncExecutor executor;
	
	
	@Before
	public void setup() {
		CoreContextInitilizer.initializeContext();
		executor = (AsyncExecutor)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_ASYNC_EXECUTER);
	}
	
	@Test
	public void testExecute() {
		executor.execute(SampleEmptyRunner.class);
		try{
			TimeUnit.SECONDS.sleep(1);
		}catch(InterruptedException e){
			Assert.fail("Exception occurred while running test " + e);
		}
		boolean result = executor.getSuccessStatus();
		Assert.assertEquals(result, true);
	}

	@RunWith(Cucumber.class)
	@CucumberOptions ()
	public class SampleEmptyRunner{
		
	}
	
	@After
	public void tearDown() {
		CoreContextInitilizer.destroyContext();
	}
}
