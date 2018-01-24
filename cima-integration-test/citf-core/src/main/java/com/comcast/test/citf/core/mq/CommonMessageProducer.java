package com.comcast.test.citf.core.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides utility methods related with producing MQ message.
 * This class should be extended by all MQ producers.
 * 
 * @author Abhijit Rej and Valdas Sevelis
 * @since June 2015
 *
 */
public class CommonMessageProducer {

	/**
	 * This method push new message in MQ 
	 * 
	 * @param message 
	 * 			Message String
	 * @param queueName 
	 * 			Name of the Queue
	 * @throws InterruptedException
	 */
	public static void sendMessage(String message, MQConnectionHandler.QueueNames queueName) throws InterruptedException {
		queueName.getBackingQueue().put(message);
		LOGGER.info("Message sent [{}] on {} queue.", message, queueName);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonMessageProducer.class);
}