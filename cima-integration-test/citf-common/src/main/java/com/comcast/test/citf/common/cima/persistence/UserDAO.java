package com.comcast.test.citf.common.cima.persistence;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * DAO class for user. This class is accountable for
 * all the user related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */

@Repository("userDao")
public class UserDAO extends AbstractDAO{	
	
	/**
	 * Gets user by user Id.
	 * 
	 * @param userId User Id.
	 * 
	 * @return User object.
	 */
	@Transactional(readOnly=true)
	public Users findUserById(String userId){
		Users user = null;
		
        Criteria criteria = getSession().createCriteria(Users.class);
        criteria.add(Restrictions.eq("userId",userId));
        Object usrObj = criteria.uniqueResult();
        
        if(usrObj!=null)
        	user = (Users)usrObj;
        
        return user;
    }
	
	
	/**
	 * Gets user by environment.
	 * 
	 * @param environment The environment.
	 * 
	 * @return List of users in the environment.
	 */
	@Transactional(readOnly=true)
	public List<Users> findUsersByEnvironment(String environment){
		List<Users> usrList = null;
		
		Criteria criteria = getSession().createCriteria(Users.class);
	    criteria.add(Restrictions.eq("environment", environment));
	    Object usrsObj = criteria.list();
	    
	    if(usrsObj!=null)
	    	usrList = (List<Users>)usrsObj;
        
        return usrList;
    }
	
	
	/**
	 * Gets users and user details by environment.
	 * 
	 * @param environment The environment to look for.
	 * 
	 * @return List of users in the environment.
	 */
	@Transactional(readOnly=true)
	public List<Users> findUsersWithDtlsByEnvironment(String environment){
		List<Users> usrList = null;
		Object resObj = findUsersByEnvironment(environment);
		
		if(resObj!=null){
			usrList = (List<Users>)resObj;
        
			for(Users usr : usrList){
				if(usr.getAccoutMap()!=null && !usr.getAccoutMap().isEmpty()){
					Collection<AccountUserMap> uaMaps = usr.getAccoutMap();
					Set<String> primaryAccIds = new HashSet<String>();
					Set<String> secondaryAccIds = new HashSet<String>();
						
					for(AccountUserMap uaMap: uaMaps){
						if(uaMap!=null && uaMap.getUaMapPrimaryKey()!=null && uaMap.getUaMapPrimaryKey().getAccount()!=null){
							if(ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_SECONDARY.equals(uaMap.getRole())){
								secondaryAccIds.add(uaMap.getUaMapPrimaryKey().getAccount().getBillingAccountId());
							}
							else{
								primaryAccIds.add(uaMap.getUaMapPrimaryKey().getAccount().getBillingAccountId());
							}
						}
					}
					if(!primaryAccIds.isEmpty()){
						usr.setPrimaryAccountIds(primaryAccIds);
					}
					if(!secondaryAccIds.isEmpty()){
						usr.setSecondaryAccountIds(secondaryAccIds);
					}
				}
			}
		}
		
        return usrList;
    }
	
	
	/**
	 * Loads users in the database.
	 * 
	 * @param userId User Id.
	 * @param password User password.
	 * @param category User category.
	 * @param pin User PIN.
	 * @param tvRating User TV-Rating.
	 * @param movieRating User Movie Rating.
	 * @param environment The environment of the user.
	 * @param loginStatus User login status.
	 */
	@Transactional
	public void populateUser(	String userId,
							 	String password,
								String category, 
								String pin, 
								String tvRating, 
								String movieRating, 
								String environment, 
								String loginStatus){
		
		Users user = getUsers(userId, password, category, pin, tvRating, movieRating, environment, loginStatus);
		getSession().merge(user);
    }
	
	/**
	 * Loads user in the database and returns that.
	 * 
	 * @param userId User Id.
	 * @param password User password.
	 * @param category User category.
	 * @param pin User PIN.
	 * @param tvRating User TV-Rating.
	 * @param movieRating User Movie Rating.
	 * @param environment The environment of the user.
	 * 
	 * @return User loaded.
	 * 
	 * @throws InvalidUserException
	 */
	@Transactional
	public Users populateAndGetUser(	String userId,
										String password,
										String category, 
										String pin, 
										String tvRating, 
										String movieRating, 
										String environment) throws InvalidUserException{
		Users user;
		
		Object obj = findEntityByRequiredField(Users.class, "userId", userId);
		user = obj!=null? (Users)obj : null;
		if(user!=null){
			if(!environment.equalsIgnoreCase(user.getEnvironment()))
				throw new InvalidUserException(new MessageFormat("{1} is already present for {2}  environment. So same user cannot exist for {3}").format(
												new String[] {userId, user.getEnvironment(), environment}));
			
			if(!category.equalsIgnoreCase(user.getCategory())){
				user.setCategory(ICimaCommonConstants.USER_CATEGORY_ALL);
				logger.info(new MessageFormat("{1} is already present in {2} category. So this is going to be avaialble for ALL categories.").format(
									new String[] {userId, user.getCategory()}));
			}
			
			user.setPin(pin);
			user.setTvRating(tvRating);
			user.setMovieRating(movieRating);
		}
		else
			user = new Users(userId, password, category, pin, tvRating, movieRating, environment);
		
		getSession().merge(user);
		return user;
    }
	
	
	/**
	 * Loads user in database and gets the user.
	 * 
	 * @param userId User Id.
	 * @param password User password.
	 * @param category User category.
	 * @param loginStatus User login status.
	 * @param environment The environment of the user.
	 * 
	 * @return The user populated.
	 * 
	 * @throws InvalidUserException
	 */
	@Transactional
	public Users populateAndGetUser(	String userId,
										String password,
										String category, 
										String loginStatus, 
										String environment) throws InvalidUserException{
		Users user;
		
		Object obj = findEntityByRequiredField(Users.class, "userId", userId);
		user = obj!=null? (Users)obj : null;
		if(user!=null){
			if(!environment.equalsIgnoreCase(user.getEnvironment()))
				throw new InvalidUserException(new MessageFormat("{1} is already present for {2}  environment. So same user cannot exist for {3}").format(
						new String[] {userId, user.getEnvironment(), environment}));
			
			if(!category.equalsIgnoreCase(user.getCategory())){
				user.setCategory(ICimaCommonConstants.USER_CATEGORY_ALL);
				logger.info(new MessageFormat("{1} is already present in {2} category. So this is going to be avaialble for ALL categories.").format(
								new String[] {userId, user.getCategory()}));
			}
			
			user.setLoginStatus(loginStatus);
		}
		else
			user = new Users(userId, password, category, environment, loginStatus);
		
		getSession().merge(user);
		return user;
    }
	
	protected Users getUsers(String userId,
							 String password,
						   	 String category, 
							 String pin, 
							 String tvRating, 
							 String movieRating, 
							 String environment, 
							 String loginStatus) {
		
		return new Users(userId, password, category, pin, tvRating, movieRating, environment, loginStatus);	
	}
		
	
	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
}
