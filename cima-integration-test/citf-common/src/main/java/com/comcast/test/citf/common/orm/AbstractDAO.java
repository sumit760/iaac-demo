package com.comcast.test.citf.common.orm;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract class for Hibernate Data Access Objects.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */

public abstract class AbstractDAO {

	@Autowired
	@Qualifier("sessionFactory")
    private SessionFactory sessionFactory;
 
	/**
	 * Method to get a hibernate session from the session factory.
	 * 
	 * @return The hibernate session.
	 * 
	 */
    public Session getSession() {
		return sessionFactory.getCurrentSession().isOpen() ? sessionFactory.getCurrentSession() : sessionFactory.openSession();
    }

    
    /**
     * Method to get a hibernate session factory.
     * 
     * @return The hibernate session factory instance.
     * 
     */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Method to set the hibernate session factory.
	 * 
	 * @param sessionFactory The hibernate session factory.
	 * 
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	/**
	 * Method to find the entity bean based on the criteria in terms of name, value pair.
	 * 
	 * @param beanCls The hibernate entity bean class.
	 * @param fieldName The field name to look for.
	 * @param fieldValue The field value to look for.
	 * 
	 * @return The entity bean object instance.
	 * 
	 */
	@Transactional(readOnly=true)
	public Object findEntityByRequiredField(Class beanCls, String fieldName, String fieldValue){
		Criteria criteria = getSession().createCriteria(beanCls);
        criteria.add(Restrictions.eq(fieldName, fieldValue));
        return criteria.uniqueResult();
    }
}
