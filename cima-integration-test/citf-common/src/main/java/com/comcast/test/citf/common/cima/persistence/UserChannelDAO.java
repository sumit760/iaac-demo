package com.comcast.test.citf.common.cima.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.orm.AbstractDAO;

/**
 * DAO class for User and Channel mapping.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@Repository("userChannelDao")
public class UserChannelDAO extends AbstractDAO{
	
	/**
	 * Loads the user-channel mapping in the database.
	 * 
	 * @param user User object.
	 * @param channel Channel object.
	 * @param subscription The user subscription to the channel.
	 */
	@Transactional
	public void populateMap(Users user, Channels channel, String subscription){
		
		ChannelUserMap.UserChannelId pKey = getUserChannelId(user, channel);
		ChannelUserMap map = getChannelUserMap(pKey, subscription);
		getSession().merge(map);
	}
	
	/**
	 * Gets channel subscription by user.
	 * 
	 * @param user User object.
	 * 
	 * @return Map of channel names and subscriptions for user
	 */
	@Transactional(readOnly=true)
	public Map<String, String> findChannelSubscriptionByUserId(Users user){
		Map<String, String> resultMap = null;
		
		Criteria criteria = getSession().createCriteria(ChannelUserMap.class);
	    criteria.add(Restrictions.eq("primaryKey.user", user));
	    List<ChannelUserMap> dbMaps = (List<ChannelUserMap>)criteria.list();
	   
	    if(dbMaps!=null){
	    	resultMap = new HashMap<String, String>();
	    	for(ChannelUserMap dbMap: dbMaps){
	    		if(dbMap!=null && dbMap.getPrimaryKey().getChannel()!=null)
	    			resultMap.put(dbMap.getPrimaryKey().getChannel().getName(), dbMap.getSubscription());
	    	}
	    }
	    
	    return resultMap;
    }
	
	
	protected ChannelUserMap.UserChannelId getUserChannelId(Users user, Channels channel) {
		
		return new ChannelUserMap.UserChannelId(user, channel);
		
	}
	
	
	protected ChannelUserMap getChannelUserMap(ChannelUserMap.UserChannelId pKey, String subscription) {
	
		return new ChannelUserMap(pKey, subscription);
	}

}
