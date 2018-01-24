package com.comcast.test.citf.common.cima.persistence.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Bean class for server information. The class keeps all server
 * related data including host, port, userId, password and the 
 * server SSH key to connect.
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="servers")
public class Servers {

	/**
	 * Default constructor
	 */
	public Servers(){
	}
	
	/**
	 * Constructor to initialize server object.
	 * 
	 * @param serverPK
	 * 				   The server.pk file for SSH connection.
	 * @param host
	 * 				   The host name of the server.
	 * @param port
	 * 				   The port name.
	 * @param userId
	 * 				   The userId.
	 * @param password
	 * 				   The password.
	 * @param status
	 * 				   Server status.
	 */
	public Servers(ServerPrimaryKeys serverPK, String host, String port, String userId, String password, String status){
		this.serverPK = serverPK;
		this.port = port;
		this.host = host;
		this.userId = userId;
		this.password = password;
		this.status = status;
	}
	
	@EmbeddedId
	private ServerPrimaryKeys serverPK;
	
	@Column(name = "host", nullable = false, length = 100)
	private String host;
	
	@Column(name = "port", nullable = false, length = 4)
	private String port;
	
	@Column(name = "user_id", nullable = false, length = 50)
	private String userId;
	
	@Column(name = "password", nullable = true, length = 20)
	private String password;
	
	@Column(name = "status", nullable = false, length = 10)
	private String status;
	
	
	/**
	 *  Returns the server .pk file path.
	 * 
	 * @return the server .pk file path.
	 */
	public ServerPrimaryKeys getServerPK() {
		return serverPK;
	}

	/**
	 * Sets server .pk file path.
	 * 
	 * @param serverPK 
	 * 			Server .pk file path to set.
	 */
	public void setServerPK(ServerPrimaryKeys serverPK) {
		this.serverPK = serverPK;
	}

	/**
	 * Returns hostname of the server.
	 * 
	 * @return hostname of the server.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets host name of the server.
	 * 
	 * @param host 
	 * 			Host name to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Returns port name of the server.
	 * 
	 * @return Port name.
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets port name of the server.
	 * 
	 * @param port 
	 * 			Port name to set.
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Returns userId to be used in logging to the server.
	 * 
	 * @return userId for log in to the server.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets userId to be used in logging to the server.
	 * 
	 * @param userId 
	 * 			userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns user password to be used in logging to the server.
	 * 
	 * @return user password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets user password to be used in logging to the server.
	 * 
	 * @param password 
	 * 			User password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns status Server status.
	 * 
	 * @return the status of the server.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status of the server.
	 * 
	 * @param status 
	 * 			Server status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
	
	@Embeddable
	public static class ServerPrimaryKeys  implements Serializable{
		private static final long serialVersionUID = 1L;

		/**
		 * Default constructor
		 */
		public ServerPrimaryKeys(){
		}
		
		/**
		 * Constructor
		 * 
		 * @param serverType
		 * 			Type of server
		 * @param environment
		 * 			Execution environment
		 * @param priority
		 * 			Priority
		 */
		public ServerPrimaryKeys(String serverType, String environment, int priority){
			this.serverType = serverType;
			this.environment = environment;
			this.priority = priority;
		}
		
		@Column(name = "server_type", length = 20)
		private String serverType;
		
		@Column(name="environment", length = 10)
		private String environment;
		
		@Column(name = "priority")
		private int priority;

		/**
		 * Returns serverType
		 * @return serverType
		 */
		public String getServerType() {
			return serverType;
		}

		/**
		 * Sets serverType
		 * @param serverType
		 * 			Type of server
		 */
		public void setServerType(String serverType) {
			this.serverType = serverType;
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
		
		/**
		 * Returns priority
		 * @return priority
		 */
		public int getPriority() {
			return priority;
		}

		/**
		 * Sets priority
		 * @param priority
		 * 			Priority
		 */
		public void setPriority(int priority) {
			this.priority = priority;
		}
	}
}