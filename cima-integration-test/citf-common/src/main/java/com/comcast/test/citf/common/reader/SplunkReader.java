package com.comcast.test.citf.common.reader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.splunk.Job;
import com.splunk.JobArgs;
import com.splunk.ResultsReaderXml;
import com.splunk.Service;
import com.splunk.ServiceArgs;

/**
 * Utility class for extracting application logs from splunk.
 * 
 * @author arej001c, spal004c
 *
 */

@org.springframework.stereotype.Service("splunkReader")
public class SplunkReader {
	// should put these values into property files, since these values may vary by environment 
	private static final int WAIT_TIME_SPLUNK = 10;		
	private static final int MAXIMUM_WAIT_TIMER = 5;

	/**
	 * Initializes the splunk.
	 * 
	 * @param host
	 * 				Host name of splunk server.
	 * @param port 
	 * 				Port of splunk server.
	 * @param user
	 * 				User Id to to connect splunk server.
	 * @param password 
	 * 				Password to connect splunk server.
	 * @param scheme 
	 * 				HTTP scheme (HTTPS in this case)
	 * @return	
	 * 				The Splunk service instance.
	 * @see Service
	 */
	public Service initializeSplunk(	String host, 
										int port, 
										String user,
										String password, 
										String scheme) {
		ServiceArgs loginArgs = new ServiceArgs();

		loginArgs.setHost(host);

		if (port > 0){
			loginArgs.setPort(port);
		}

		loginArgs.setUsername(user);
		loginArgs.setPassword(password);
		loginArgs.setScheme(scheme);

		Service service = Service.connect(loginArgs);
		LOGGER.debug("Splunk server connected!");
		return service;
	}

	/**
	 * Perform search query with the Service and return the result.
	 * 
	 * @param service
	 * 					The Splunk Service.
	 * @param searchQuery
	 * 					The specified splunk query.
	 * @param key 		
	 * 					The key element searched for in the query.
	 * @return
	 * 					Splunk query result.
	 */
	public String read(Service service, String searchQuery, String key) {
		String result = null;
		int counter = 0;
		try {
			TimeUnit.SECONDS.sleep(WAIT_TIME_SPLUNK);
		} catch (InterruptedException e) {
			LOGGER.warn("InterruptedException occured while waiting to fetch splunk log: ", e);
		}
		JobArgs jobArgs = new JobArgs();
		jobArgs.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
		Job job = service.getJobs().create(searchQuery, jobArgs);

		while (!job.isDone() && counter < MAXIMUM_WAIT_TIMER) {
			counter++;
			if (counter >= MAXIMUM_WAIT_TIMER) {
				throw new RuntimeException("The maximum waiting time: " + (counter * WAIT_TIME_SPLUNK) + 
					" has reached when waiting to fetch splunk log using this splunk query: " + searchQuery);
			}
			try {
				TimeUnit.SECONDS.sleep(WAIT_TIME_SPLUNK);
			} catch (InterruptedException e) {
				LOGGER.warn("InterruptedException occured while waiting to fetch splunk log: ", e);
			}
		}

		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("count", 0);
		InputStream resultsNormalSearch = job.getResults(arguments);
		try {
			ResultsReaderXml resultsReaderNormalSearch = new ResultsReaderXml(resultsNormalSearch);

			HashMap<String, String> event;
			while ((event = resultsReaderNormalSearch.getNextEvent()) != null){
				if (event.get(key) != null) {
					LOGGER.debug("Splunk key match found[Key: {}, Value: {}, Time: {}]", key, event.get(key), event.get(EVENT_KEY_TIMESTAMP));
					result = event.get(key);
					break;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while trying to fetch/check splunk log[key: {}, search query: {}] exception: ", 
							searchQuery, key, e);
		}

		LOGGER.info("Splunk serach completed for query: {} with result: {}", searchQuery, result);
		return result;
	}

	public static final String EVENT_KEY_TIMESTAMP = "timestamp";
	public static final String SPLUNK_SERVER_DEFAULT_TIMEZONE = "GMT";
	public static final String SPLUNK_TIME_SUFFIX_MINUTE = "m";

	private static Logger LOGGER = LoggerFactory.getLogger(SplunkReader.class);
}