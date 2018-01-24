package com.comcast.test.citf.common.cima.persistence;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.LogFinders;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;


/**
 * DAO class for retrieving application logs. 
 * 
 * @author Abhijit Rej (arej001c)
 * @since October 2015
 *
 */

@Repository("logFinderDao")
public class LogFinderDAO extends AbstractDAO{
	
	/**
	 * Persists the log finder details in database. 
	 * This method is used to load log finder into the database.
	 * 
	 * @param id The unique Id to identify the log query.
	 * @param environment The environment.
	 * @param logPath The server log path.
	 * @param regex The regular expression to retrive logs from server log.
	 * @param splunkQry The Splunk query.
	 * @param splunkKey The Splunk key to look for.
	 */
	@Transactional
	public void populateLogFinder(	String id, 
									String environment, 
									String logPath, 
									String regex,
									String splunkQry, 
									String splunkKey){
		
		LogFinders.LogFindersPrimaryKeys pk = getLogFindersPrimaryKeys(id, environment);
		LogFinders lf = getLogFinders(pk, logPath, regex, splunkQry, splunkKey);
		getSession().merge(lf);
    }

	/**
	 * Gets the log finder associated with the log query Id and environment.
	 * 
	 * @param id The log query Id.
	 * @param environment The environment.
	 * 
	 * @return The log finder object.
	 */
	@Transactional(readOnly=true)
	public LogFinders findLogChecker(String id, String environment){
        Criteria criteria = getSession().createCriteria(LogFinders.class)
        								.add(Restrictions.eq("primaryKey.id", id))
        								.add(Restrictions.in("primaryKey.environment", new String[]{ICommonConstants.ENVIRONMENT_ALL, environment}));
        
        Object result = criteria.uniqueResult();   
        return result!=null?(LogFinders)result:null ;
    }
	
	
	protected LogFinders.LogFindersPrimaryKeys getLogFindersPrimaryKeys(String id, String environment) {
		return new LogFinders.LogFindersPrimaryKeys(id, environment);
	}
	
	protected LogFinders getLogFinders(LogFinders.LogFindersPrimaryKeys pk,
									   String logPath,
									   String regex,
									   String splunkQry,
									   String splunkKey) {
		return new LogFinders(pk, logPath, regex, splunkQry, splunkKey);
	}
}
