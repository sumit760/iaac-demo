package com.comcast.test.citf.common.cima.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.Servers;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * DAO class for Servers. This class is accountable for
 * all the server related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since October 2015
 *
 */

@Repository("serversDao")
public class ServersDAO extends AbstractDAO{

	/**
	 * Loads server details in the database.
	 * 
	 * @param serverType The type of the server.
	 * @param environment The environment.
	 * @param host Server host.
	 * @param port Server port.
	 * @param userId UserId to login in the server.
	 * @param password User password to login.
	 * @param priority Rating priority.
	 * @param status Rating status.
	 */
	@Transactional
	public void populateServer(		String serverType, 
									String environment, 
									String host, 
									String port, 
									String userId, 
									String password, 
									int priority, 
									String status){
		if(status==null)
			status = ICommonConstants.DB_STATUS_ACTIVE;
		if(priority<=0)
			priority = 1;
		
		Servers.ServerPrimaryKeys spk = new Servers.ServerPrimaryKeys(serverType, environment, priority);
		Servers server = new Servers(spk, host, port, userId, password, status);
		getSession().merge(server);
    }
	
	
	/**
	 * Gets server details by type and environment.
	 * 
	 * @param serverType The type of the server.
	 * @param environment The environment.
	 * 
	 * @return List of Server objects.
	 */
	@Transactional(readOnly=true)
	public List<Servers> findServerByTypeAndEnvironment(String serverType, String environment){
        Criteria criteria = getSession().createCriteria(Servers.class)
        								.add(Restrictions.eq("serverPK.serverType", serverType))
        								.add(Restrictions.in("serverPK.environment", new String[]{ICommonConstants.ENVIRONMENT_ALL, environment}))
        								.addOrder(Order.asc("serverPK.priority"));
        
        List<Servers> result = criteria.list();   
        return result;
    }
}
