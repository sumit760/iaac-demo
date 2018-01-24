package com.comcast.test.citf.common.cima.persistence;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * DAO class for fresh users. This class is accountable for
 * all fresh user related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */

@Repository("freshUserDao")
public class FreshUserDAO extends AbstractDAO{

	/**
	 * Gets a fresh user based on the query. The query sets the 
	 * search attribute such as Facebook Id, alternate Email etc. 
	 * along with the search type like EQUAL, NOT EQUAL or NULL.
	 * 
	 * @param queries The search query.
	 * @return Fresh user details.
	 * @see Query
	 */
	@Transactional
	public synchronized FreshUsers findUser(List<Query> queries){
		FreshUsers result = null;		
		
		Criteria criteria = getSession().createCriteria(FreshUsers.class);
		if(queries!=null && queries.size()>0){
			for(Query query: queries){
				if(Conditions.IS_NOT_NULL.equals(query.getCondition())){
					criteria.add(Restrictions.isNotNull(query.getColumn()));
				}
				else if(Conditions.IS_NULL.equals(query.getCondition())){
					criteria.add(Restrictions.isNull(query.getColumn()));
				}
				else if(Conditions.EXACT_VALUE.equals(query.getCondition())){
					criteria.add(Restrictions.eq(query.getColumn(), query.getValue()));
				}
			}
		}
		criteria.add(Restrictions.eq("lockStatus", LockStatus.UNLOCKED.getValue()));

		Object resultset = criteria.list();
	    if(resultset!=null && ((List<FreshUsers>)resultset).size()>0){
	    	List<FreshUsers> users = (List<FreshUsers>)resultset;
	    	if(users.size()>1){
	    		Collections.shuffle(users);
	    	}
	    	if(users.get(0)!=null){
	    		result = users.get(0);
	    		result.setLockStatus(LockStatus.LOCKED.getValue());
	    		result.setLastModifiedOn(MiscUtility.getCurrentSqlDate());
	    		getSession().merge(result);
	    	}
	    }
	    
	    LOGGER.info("Fresh user request for queries: {} has been completed with result: {}", queries, result);
	    return result;
    }
	
	
	/**
	 * Unlocks an user that gets locked when fetched. Ideally this method
	 * should be called to unlock an user at the end as part of cleanup.
	 * 
	 * @param primaryKey User Id.
	 */
	@Transactional
	public synchronized boolean unlockUser(int primaryKey){
		Criteria criteria = getSession().createCriteria(FreshUsers.class);
		criteria.add(Restrictions.eq("primaryKey", primaryKey));
		Object usr = criteria.uniqueResult();
		
		if(usr!=null){
			FreshUsers fu = (FreshUsers)usr;
			fu.setLockStatus(LockStatus.UNLOCKED.getValue());
			fu.setLastModifiedOn(MiscUtility.getCurrentSqlDate());
			getSession().merge(fu);
			return true;
		}
		return false;
    }
	
	
	/**
	 * Enumeration for lock status.
	 * 
	 * @author arej001c
	 *
	 */
	public enum LockStatus{
		
		LOCKED("Y"),
		UNLOCKED("N");
		
		private final String value;
        private LockStatus(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Enumeration for search criteria.
	 * 
	 * @author arej001c
	 *
	 */
	public enum Columns{
		FACEBOOK,
		DOB,
		ALTERNATE_EMAIL;
	}
	
	/**
	 * Enumeration for search type.
	 * 
	 * @author arej001c
	 *
	 */
	public enum Conditions{
		IS_NULL,
		IS_NOT_NULL,
		EXACT_VALUE;
	}
	
	/**
	 * Class to handle and buildup a search query based upon search criteria and serach type.
	 * 
	 * @author arej001c
	 *
	 */
	public static class Query{
		private Columns column;
		private Conditions condition;
		private Object value;
		
		public Query(Columns column, Conditions condition, Object value){
			this.column = column;
			this.condition = condition;
			this.value = value;
		}
		
		public String getColumn() {
			if(Columns.FACEBOOK.equals(this.column)){
				return "fbId";
			} else if(Columns.DOB.equals(this.column)) {
				return "dob";
			} else if(Columns.ALTERNATE_EMAIL.equals(this.column)) {
				return "alternativeEmail";
			} else {
				return null;
			}
		}
		
		public Conditions getCondition() {
			return condition;
		}
		
		public Object getValue() {
			return value;
		}
		
		@Override
        public String toString() {
            return StringUtility.appendObjects("FreshUserDao Query [Column: ",column,", Conditions: ",condition," and value: ",value,"]");
        }
	}
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FreshUserDAO.class);
}
