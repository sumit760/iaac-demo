package com.comcast.test.citf.common.cima.persistence.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Bean class for log finding methodologies. The class holds two ways of finding
 * application logs.
 * 
 * 1. Finding log from the server. This logs in to the CIMA server and gets the log
 *    from the application log.
 *   
 * 2. Finding log from Splunk. This gets the log from Splunk and the splunk query
 *    needs to be provided for that.
 *    
 * CITF recommends to Splunk as it is more reliable and robust than getting server log 
 * directly.
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="log_finders")
public class LogFinders {

	/**
	 * Default constructor
	 */
	public LogFinders(){
		
	}
	
	/**
	 * Constructor to initialize log finder object.
	 * 
	 * @param primaryKey
	 * 					The id that identifies the log type. Example 
	 * 					'RESET_PASSWORD_EMAIL' is the id for the log
	 * 					type that gets the email link when a password
	 * 					is reset by email.
	 * @param logPath
	 * 					The server log path.
	 * @param regex
	 * 					The regular expression to apply over the server log.
	 * @param splunkQry
	 * 					The Splunk query.
	 * @param splunkKey
	 * 					The key to extract in Splunk query.
	 */
	public LogFinders(LogFindersPrimaryKeys primaryKey, String logPath, String regex, String splunkQry, String splunkKey){
		this.primaryKey = primaryKey;
		this.logPath = logPath;
		this.regex = regex;
		this.splunkQry = splunkQry;
		this.splunkKey = splunkKey;
	}
	
	
	@EmbeddedId
	private LogFindersPrimaryKeys primaryKey;
	
	@Column(name = "log_path", nullable = false, length = 200)
    private String logPath;
	
	@Column(name = "regex", nullable = false, length = 150)
    private String regex;
	
	@Column(name = "splunk_query", nullable = false, length = 500)
    private String splunkQry;
	
	@Column(name = "splunk_query_key", nullable = false, length = 50)
    private String splunkKey;
	
	/**
	 * Returns the log type id.
	 * 
	 * @return The log type id.
	 */
	public LogFindersPrimaryKeys getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * sets the log type id.
	 * 
	 * @param primaryKey 
	 * 			The log type id to set.
	 */
	public void setPrimaryKey(LogFindersPrimaryKeys primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Returns the server log path.
	 * 
	 * @return The server log path.
	 */
	public String getLogPath() {
		return logPath;
	}

	/**
	 * Sets server log path.
	 * 
	 * @param logPath 
	 * 			The server log path to set.
	 */
	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	/**
	 * Returns the regular expression to apply on the server log to extract the 
	 * token looking for.
	 * 
	 * @return The regular expression.
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * Sets the regular expression to apply on the server log to extract the 
	 * token looking for.
	 * 
	 * @param regex 
	 * 			The regular expression to set.
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	/**
	 * Returns the Splunk query to apply.
	 * 
	 * @return The Splunk query.
	 */
	public String getSplunkQry() {
		return splunkQry;
	}

	/**
	 * Sets the Splunk query.
	 * 
	 * @param splunkQry 
	 * 			The Splunk query to set.
	 */
	public void setSplunkQry(String splunkQry) {
		this.splunkQry = splunkQry;
	}

	/**
	 * Returns the Splunk key associated with the query.
	 * 
	 * @return The Splunk key.
	 */
	public String getSplunkKey() {
		return splunkKey;
	}

	/**
	 * Sets the Splunk key associated with the query.
	 * 
	 * @param splunkKey 
	 * 			The Splunk key to set.
	 */
	public void setSplunkKey(String splunkKey) {
		this.splunkKey = splunkKey;
	}




	@Embeddable
	public static class LogFindersPrimaryKeys  implements Serializable{
		private static final long serialVersionUID = 1L;

		/**
		 * Deafult constructor
		 */
		public LogFindersPrimaryKeys(){
		}
		
		/**
		 * Constructor
		 * 
		 * @param id
		 * 			Log finder Id
		 * @param environment
		 * 			Execution environment
		 */
		public LogFindersPrimaryKeys(String id, String environment){
			this.id = id;
			this.environment = environment;
		}
		
		@Column(name = "id", length = 50)
	    private String id;
		
		@Column(name="environment", length = 10)
		private String environment;
		
		/**
         * Returns id
         * @return id
         */
		public String getId() {
			return id;
		}

		/**
		 * Sets id
		 * @param id
		 * 			Log finder Id
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
         * Returns environment
         * @return environment
         */
		public String getEnvironment() {
			return environment;
		}

		/**
		 * Sets environment
		 * @param environment
		 * 			Execution environment
		 */
		public void setEnvironment(String environment) {
			this.environment = environment;
		}
	}
	
}