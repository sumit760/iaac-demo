package com.comcast.test.citf.common.cima.persistence.beans;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Bean class for channel user association.
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="channel_user_map")
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.user",
        joinColumns = @JoinColumn(name = "user_id")),
    @AssociationOverride(name = "primaryKey.channel",
        joinColumns = @JoinColumn(name = "channel_id")) })
public class ChannelUserMap {
	
	@EmbeddedId
	private UserChannelId primaryKey;
	
	@Column(name = "subscription_value", nullable = false, length = 100)
	private String subscription;
	
	/**
	 * Default constructor
	 */
	public ChannelUserMap(){
		
	}
	
	/**
	 * Constructor to initialize ChannelUserMap.
	 * 
	 * @param primaryKey
	 * 					The primary key of user channel mapping.
	 * @param subscription
	 * 					The subscription.
	 */
	public ChannelUserMap(UserChannelId primaryKey, String subscription){
		this.primaryKey = primaryKey;
		this.subscription = subscription;
	}

	/**
	 * Returns the user channel association primary key.
	 * 
	 * @return The user channel association primary key.
	 */
	public UserChannelId getPrimaryKey() {
		return primaryKey;
	}

	/**
     * Sets the user channel association primary key.
     * 
     * @param primaryKey 
     * 			User Channel Id
     */
	public void setPrimaryKey(UserChannelId primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Returns subscription.
	 * 
	 * @return User channel subscript
	 */
	public String getSubscription() {
		return subscription;
	}

	/**
	 * Sets subscription.
	 * 
	 * @param subscription 
	 * 			User channel subscription.
	 */
	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}
	
	@Embeddable
	public static class UserChannelId implements Serializable { 
		
		@ManyToOne(cascade = CascadeType.ALL)
	    private Users user;
		
		@ManyToOne(cascade = CascadeType.ALL)
	    private Channels channel;
		
		/**
		 * Default constructor
		 */
		public UserChannelId(){
			
		}
	    
		/**
		 * Constructor specifying Users and Channels.
		 * 
		 * @param user
		 * 				User object @see com.comcast.test.citf.common.cima.persistence.beans.Users
		 * @param channel
		 * 				Channel object @see com.comcast.test.citf.common.cima.persistence.beans.Channels
		 */
	    public UserChannelId(Users user, Channels channel){
	    	this.user = user;
	    	this.channel = channel;
	    }
	   
	    /**
	     * Returns User of user channel association.
	     * 
	     * @return User
	     * 
	     * @see com.comcast.test.citf.common.cima.persistence.beans.Users
	     */
	    public Users getUser() {
	        return user;
	    }
	 
	    /**
	     * Sets Users of user channel association.
	     * 
	     * @param user
	     * 			User object  @see com.comcast.test.citf.common.cima.persistence.beans.Users
	     */
	    public void setUser(Users user) {
	        this.user = user;
	    }
	   
	    /**
	     * Returns Channels of user channel association.
	     * 
	     * @return Channel object
	     * 
	     * @see com.comcast.test.citf.common.cima.persistence.beans.Channels
	     */
	    public Channels getChannel() {
	        return channel;
	    }
	 
	    /**
	     * Sets Channels of user channel association.
	     * 
	     * @param channel 
	     * 			Channel to set in user channel association.
	     * 
	     * @see com.comcast.test.citf.common.cima.persistence.beans.Channels
	     */
	    public void setChannel(Channels channel) {
	        this.channel = channel;
	    }
	}
	
}