package com.comcast.test.citf.common.cima.persistence;

import java.sql.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.UserAttributes;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * DAO class for user attributes. This class is accountable for
 * all user attributes related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since October 2015
 *
 */

@Repository("userAttributes")
public class UserAttributesDAO extends AbstractDAO{
	
	/**
	 * Gets user attributes by user.
	 * 
	 * @param user User object.
	 * 
	 * @return User attributes associated with the user.
	 */
	@Transactional(readOnly=true)
	public UserAttributes findUserAttributesByUser(Users user){
		UserAttributes usrAttr = null;
		
        Criteria criteria = getSession().createCriteria(UserAttributes.class);
        criteria.add(Restrictions.eq("user",user));
        Object usrObj = criteria.uniqueResult();
        
        if(usrObj!=null)
        	usrAttr = (UserAttributes)usrObj;
        
        return usrAttr;
    }
	
	
	/**
	 * Gets user attributes by user Id.
	 * 
	 * @param userId The user Id.
	 * 
	 * @return User attributes associated with the user.
	 */
	@Transactional(readOnly=true)
	public UserAttributes findUserAttributesByUserId(String userId){
		Object obj = findEntityByRequiredField(UserAttributes.class, "user.userId", userId);
        return obj!=null ? (UserAttributes)obj : null;
    }
	
	@Transactional(readOnly=true)
	public UserAttributes findUserAttributesByGuid(String guid){
		Object obj = findEntityByRequiredField(UserAttributes.class, "guid", guid);
		return obj!=null ? (UserAttributes)obj : null;
    }
	
	
	/**
	 * Loads user attributes in the database.
	 * 
	 * @param guid User cstCustGuid.
	 * @param user User object.
	 * @param email User email.
	 * @param alternativeEmail User alternate email if any.
	 * @param alterEmailPassword User alternate email password if alternate email exists.
	 * @param secretQuestion User secret question.
	 * @param secretAnswer USer secret answer.
	 * @param fbId User Facebook Id if any.
	 * @param fbPassword User Facebook password if FB Id exists.
	 * @param ssn User SSN if exists.
	 * @param dob User date of birth.
	 * 
	 * @return The user attribute loaded.
	 * 
	 * @throws InvalidUserException
	 */
	@Transactional
	public UserAttributes populateAndGetUserAttributes(	String guid, 
														Users user,
														String email,
														String alternativeEmail,
														String alterEmailPassword,
														String secretQuestion,
														String secretAnswer,
														String fbId,
														String fbPassword,
														String ssn,
														Date dob) throws InvalidUserException{
		UserAttributes usrAttrs = null;
		usrAttrs = findUserAttributesByGuid(guid);
		if(usrAttrs!=null){			
			usrAttrs.setUser(user);
			
			if(email!=null)
				usrAttrs.setEmail(email);
			if(!StringUtility.isStringEmpty(alternativeEmail))
				usrAttrs.setAlterEmail(alternativeEmail);
			if(!StringUtility.isStringEmpty(alterEmailPassword))
				usrAttrs.setAlterEmailPassword(alterEmailPassword);
			if(!StringUtility.isStringEmpty(secretQuestion))
				usrAttrs.setSecretQuestion(secretQuestion);
			if(!StringUtility.isStringEmpty(secretAnswer))
				usrAttrs.setSecretAnswer(secretAnswer);
			if(!StringUtility.isStringEmpty(fbId))
				usrAttrs.setFbId(fbId);
			if(!StringUtility.isStringEmpty(fbPassword))
				usrAttrs.setFbPassword(fbPassword);
			if(!StringUtility.isStringEmpty(ssn))
				usrAttrs.setSsn(ssn);
			if(dob!=null)
				usrAttrs.setDob(dob);
		}
		else
			usrAttrs = new UserAttributes(guid, user, email, alternativeEmail, alterEmailPassword, secretQuestion, secretAnswer, fbId, fbPassword, dob, ssn);
		
		getSession().merge(usrAttrs);
		return usrAttrs;
    }

}
