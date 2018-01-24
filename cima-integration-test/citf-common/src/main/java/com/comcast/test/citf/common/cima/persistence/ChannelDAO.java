package com.comcast.test.citf.common.cima.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * DAO class for Channels. This class is accountable for
 * all the channel related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@Repository("channelDao")
public class ChannelDAO extends AbstractDAO{

	/**
	 * Persists the channel details in database. This method is used to load channel data into the 
	 * database.
	 * 
	 * @param channelId The Channel Id.
	 * @param name The name of the channel.
	 * @param description The channel optional description.
	 * @param stationId The stationId.
	 * @param contentType The content Type.
	 * @param strategy The channel strategy.
	 */
	@Transactional
	public void populateChannel(String channelId, String name, String description, String stationId, String contentType, String strategy){
		Channels channel = getChannels(channelId, name, description, stationId, contentType, strategy, ICommonConstants.DB_STATUS_ACTIVE);
		getSession().merge(channel);
    }
	
	/**
	 * Gets channel details by name.
	 * 
	 * @param channelName The name of the channel.
	 * 
	 * @return The Channel object.
	 * @see Channels
	 */
	@Transactional(readOnly=true)
	public Channels findChannelByName(String channelName){
        Criteria criteria = getSession().createCriteria(Channels.class);
        criteria.add(Restrictions.eq("channel_id",channelName));
        return (Channels) criteria.uniqueResult();
    }
	
	/**
	 * Gets all the channels.
	 * 
	 * @return The list of Channel objects.
	 */
	@Transactional(readOnly=true)
	public List<Channels> findAllChannels(){
        Criteria criteria = getSession().createCriteria(Channels.class);
        criteria.add(Restrictions.eq("status",ICommonConstants.DB_STATUS_ACTIVE));
        return (List<Channels>) criteria.list();
    }
	
	protected Channels getChannels(String channelId, String name, String description, String stationId, String contentType, String strategy,String status) {
		
		return new Channels(channelId, name, description, stationId, contentType, strategy, ICommonConstants.DB_STATUS_ACTIVE);
	}
}
