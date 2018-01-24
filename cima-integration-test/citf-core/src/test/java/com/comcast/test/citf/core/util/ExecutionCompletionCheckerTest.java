package com.comcast.test.citf.core.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.core.mq.CommonMessageProducer;
import com.comcast.test.citf.core.mq.MQConnectionHandler;

public class ExecutionCompletionCheckerTest extends CommonMessageProducer { 
	
	private ExecutionCompletionChecker checker;
	
	@Before
	public void setup() {
		
		checker = new ExecutionCompletionChecker();
		
	}
	
	@Test
	public void testIsExecutionCompleted() {
		
		//Expected No. of messages = 1 , Max execution time = 1
		//Expected true
		try{
			sendMessage("Test message", MQConnectionHandler.QueueNames.Async_Exec_Completion_Notification);
		}catch(InterruptedException e){
			Assert.fail("Exception occurred while sending message in queue " + e);
		}
		boolean isCompleted = checker.isExecutionCompleted(1, 1);
		
		assertThat(
				isCompleted,
				is(true));

		
		//Expected No. of messages = 0 , Max execution time = 1
		//Expected true
		isCompleted = checker.isExecutionCompleted(0, 1);
		
		assertThat(
				isCompleted,
				is(true));
		

		//Expected No. of messages = 1 , Max execution time = -1
		//Expected true
		isCompleted = checker.isExecutionCompleted(1, -1);
		
		assertThat(
				isCompleted,
				is(false));

		
	}

}
