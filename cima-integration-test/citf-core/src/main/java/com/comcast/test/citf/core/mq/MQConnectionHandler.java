package com.comcast.test.citf.core.mq;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Manages MQ connection
 * 
 * @author Abhijit Rej and Valdas Sevelis
 * @since June 2015
 *
 */
public class MQConnectionHandler {

	/** Private constructor for utility class */
	private MQConnectionHandler() {}

	public static final long MESSAGE_WAITING_TIME_MILLIS = 1000;

	/**
	 * Name of the MQ queues
	 */
	public enum QueueNames{
		Async_Exec_Completion_Notification("Q1"),
		Persist_User_Password_In_Database("Q2");

		private final String queueName;

		private final BlockingQueue<Object> backingQueue;

        QueueNames(final String value) {
            this.queueName = value;
	        backingQueue = new LinkedBlockingDeque<>();
        }

		public String getQueueName() {
			return queueName;
		}

		public BlockingQueue<Object> getBackingQueue() {
			return backingQueue;
		}
	}
}