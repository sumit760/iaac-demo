package com.comcast.test.citf.common.cima.persistence.beans;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Bean class for Channels.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="channels")
public class Channels {
	
	/**
	 * Default constructor
	 */
	public Channels(){
		
	}
	
	/**
	 * Constructor specifying all instance variables.
	 * 
	 * @param channelId
	 * 					The Channel Id.
	 * @param name
	 * 					The name of the channel.
	 * @param description
	 * 					The channel description.
	 * @param stationId
	 * 					The station Id.
	 * @param contentType
	 * 					The channel content type.
	 * @param strategy
	 * 					The channel strategy.
	 * @param status
	 * 					The channel status.
	 */
	public Channels(String channelId, String name, String description, String stationId, String contentType, String strategy, String status){
		this.channelId = channelId;
		this.name = name;
		this.description = description;
		this.stationId = stationId;
		this.contentType = contentType;
		this.strategy = strategy;
		this.status = status;
	}

	@Id
	@Column(name = "channel_id", unique = true, nullable = false, length = 50)
    private String channelId;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "description", nullable = true, length = 500)
	private String description;
	
	@Column(name = "station_id", nullable = false, length = 50)
	private String stationId;
	
	@Column(name = "content_type", nullable = true, length = 10)
	private String contentType;
	
	@Column(name = "test_strategy", nullable = true, length = 25)
	private String strategy;
	
	@Column(name = "status", nullable = false, length = 10)
	private String status;
	
	@OneToMany(mappedBy = "primaryKey.channel")
	private Collection<ChannelUserMap> userMap;
	
	/**
     * Returns channelId of Channels.
     * 
     * @return ChannleId of the channel.
     */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * Sets channelId of Channels.
	 * 
	 * @param channelId Channel Id to set.
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * Returns name of Channels.
	 * 
	 * @return Name of the channel.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of Channels.
	 * 
	 * @param name Channel name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns description of Channels.
	 * 
	 * @return Channel description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description of Channels.
	 * 
	 * @param description Channel description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns status of Channels.
	 * 
	 * @return The status of the channel.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status of Channels.
	 * 
	 * @param status The channel status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns userMap of Channels.
	 * 
	 * @return User association map of the channel.
	 */
	public Collection<ChannelUserMap> getUserMap() {
		return userMap;
	}

	/**
	 * Sets userMap of Channels.
	 * 
	 * @param userMap The user association map of the channel to set.
	 */
	public void setUserMap(Collection<ChannelUserMap> userMap) {
		this.userMap = userMap;
	}

	/**
	 * Returns stationId of Channels.
	 * 
	 * @return StaionId of the channel.
	 */
	public String getStationId() {
		return stationId;
	}

	/**
	 * Sets stationId of Channels.
	 * 
	 * @param stationId station Id to set.
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	/**
	 * Returns contentType of Channels.
	 * 
	 * @return The content type of the channel.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
     * Sets contentType of Channels.
     * 
     * @param contentType The content type to set.
     */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns strategy of Channels.
	 * 
	 * @return The channel strategy.
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * Sets strategy of Channels.
	 * 
	 * @param strategy The channel strategy to set.
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	
}