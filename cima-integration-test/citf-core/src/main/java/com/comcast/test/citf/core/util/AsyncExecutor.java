package com.comcast.test.citf.core.util;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.mq.CommonMessageProducer;
import com.comcast.test.citf.core.mq.MQConnectionHandler;
import com.comcast.test.citf.core.runtime.ExecutionEngine;

/**
 * Creates asynchronous threads and start executing Cucumber Runner file.
 * 
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 */

public class AsyncExecutor extends CommonMessageProducer {
	
	/**
	 * Creates asynchronous threads and start executing Cucumber Runner file. 
	 * Also it pushes a message in MQ to notify completion of test executer thread when the execution completed.
	 * 
	 * @param runableCls
	 * 			Cucumber Runner class which will be executed by this asynchronous thread
	 * 			@see java.lang.Class
	 */
	@SuppressWarnings("rawtypes")
	@Async("executor")
	public void execute(Class runableCls){
		this.isSucceed = true;
		JUnitCore junit = null;
		Result res = null;
		LOGGER.info("Start executing AsyncExecutor for class {}", runableCls);
		
		try{
			String threadName = StringUtility.getLastTokenFromString(runableCls.getName(), ICommonConstants.DOT);
			if(threadName!=null){
				threadName = threadName.replace(ExecutionEngine.RUNNER_NAME_SUFFIX, ICommonConstants.BLANK_STRING);
				Thread.currentThread().setName(threadName);
			}
			
			junit = (JUnitCore)CoreContextInitilizer.getBean("junitCore");
			res = junit.run(runableCls);
			
			if(res.getFailures()!=null && res.getFailures().size()>0){
				for(Failure failure : res.getFailures()){
					LOGGER.error("Failed to run {} due to {}", runableCls, failure);
					this.isSucceed = false;
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Error occurred while executing runner thread ", e);
			this.isSucceed = false;
		}
		finally{
			res = null;
			junit = null;
			try{
				sendMessage("Excecution processed AsyncExecutor for class "+runableCls, MQConnectionHandler.QueueNames.Async_Exec_Completion_Notification);
			}catch(Exception mse){
				LOGGER.error("Error occurred while sending message - ", mse);
			}
		}
		LOGGER.info("AsyncExecutor execution finished for class {}", runableCls);
	}
	
	public boolean getSuccessStatus(){
		return this.isSucceed;
	}
	
	private boolean isSucceed;
	private static Logger LOGGER = LoggerFactory.getLogger(AsyncExecutor.class);
}