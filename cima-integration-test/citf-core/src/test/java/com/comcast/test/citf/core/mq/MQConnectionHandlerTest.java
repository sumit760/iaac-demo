package com.comcast.test.citf.core.mq;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.concurrent.BlockingQueue;

import org.junit.Test;

public class MQConnectionHandlerTest {
	
	@Test
	public void testGetBlockingQueue() {
		
		BlockingQueue<Object> bq = MQConnectionHandler.QueueNames.Async_Exec_Completion_Notification.getBackingQueue();
		
		assertThat(
				bq,
				notNullValue());
		
	}

}
