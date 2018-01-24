package com.comcast.test.citf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.core.mq.CommonMessageConsumer;
import com.comcast.test.citf.core.mq.MQConnectionHandler;

/**
 * This class acts a listener to check completion of all test(thread) executions.
 * 
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 */
public class ExecutionCompletionChecker extends CommonMessageConsumer{
	
	private static final long THREAD_WAITING_TIME = 60000; //1 min
	
	/**
	 * Checks completion of all test(thread) executions
	 * 
	 * @param expectedNoOfMessages
	 * 			No. of messages in MQ to decide completion of execution
	 * @param maxExecutionTime
	 * 			Maximum waiting time in milliseconds
	 * @return	true if execution is complete, else false
	 */
	public boolean isExecutionCompleted(int expectedNoOfMessages, long maxExecutionTime){
		boolean isCompleted = false;
		int totalMsgCount = 0;
		
		try{
			long exeTime = 0;
			while(exeTime<=maxExecutionTime){
				LOGGER.info("Entering loop[total waiting time till now: {} min] to fetch No. of messages in {} queue.", 
									(exeTime/60000), MQConnectionHandler.QueueNames.Async_Exec_Completion_Notification);
				
				totalMsgCount = getNoOfMessagesInQueue(MQConnectionHandler.QueueNames.Async_Exec_Completion_Notification);
				
				if(totalMsgCount >= expectedNoOfMessages){
					isCompleted = true;
					break;
				}
				Thread.sleep(THREAD_WAITING_TIME);
				exeTime=exeTime+THREAD_WAITING_TIME;
			}
		}catch(Exception e){
			LOGGER.error("Error occurred while checking No of messages in queue : ", e);
		}
		
		LOGGER.info("Exiting isExecutionCompleted with execution completion status: {}", (isCompleted?"Complete":"InComplete"));
		return isCompleted;
	}

	
	private static Logger LOGGER = LoggerFactory.getLogger(ExecutionCompletionChecker.class);
}
