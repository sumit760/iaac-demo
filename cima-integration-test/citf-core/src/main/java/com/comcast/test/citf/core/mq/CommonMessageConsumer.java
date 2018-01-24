package com.comcast.test.citf.core.mq;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a few utility methods related with consuming MQ message.
 * This class should be extended by all MQ consumers.
 * 
 * @author Abhijit Rej and Valdas Sevelis
 * @since June 2015
 *
 */
public abstract class CommonMessageConsumer {

	/**
	 * Checks No. of messages in a Specific queue 
	 * 
	 * @param queueName
	 * 			Name of the queue @see com.comcast.test.citf.core.mq.MQConnectionHandler.QueueNames
	 * @return No. of messages in queue
	 */
	protected static int getNoOfMessagesInQueue(MQConnectionHandler.QueueNames queueName) {
		final int messageCount = queueName.getBackingQueue().size();

		LOGGER.info("No. of messages in {} queue is {}", queueName, messageCount);
		return messageCount;
	}

	/**
	 * Receives message from MQ
	 * 
	 * @param queueName 
	 * 			Name of the queue  @see com.comcast.test.citf.core.mq.MQConnectionHandler.QueueNames
	 * @return message Object
	 */
	protected static Object receiveMessage(MQConnectionHandler.QueueNames queueName) {
		Object message = null;
		try {
			message = queueName.getBackingQueue().poll(MQConnectionHandler.MESSAGE_WAITING_TIME_MILLIS, TimeUnit.MILLISECONDS);
			LOGGER.info("Message received from {}:: [{}]", queueName, message);
		} catch (InterruptedException e) {
			LOGGER.warn("No message has been received from {}", queueName);
		}
		return message;
	}

	private static Logger LOGGER = LoggerFactory.getLogger(CommonMessageConsumer.class);
}