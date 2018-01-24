package com.comcast.test.citf.common.cima.persistence;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * DAO class for User-Account mapping.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */

@Repository("userAccountDao")
public class UserAccountDAO extends AbstractDAO{
	
	/**
	 * Loads the user-account mapping in the database.
	 * 
	 * @param user User object
	 * @param account Account object
	 * @param role User role.
	 * @throws InvalidUserException
	 */
	@Transactional
	public void populateAccountUserMap(Users user, Accounts account, ICimaCommonConstants.UserRoles role) throws InvalidUserException{
		String uRole = ICimaCommonConstants.UserRoles.PRIMARY.getValue();
		if(role!=null && ICimaCommonConstants.UserRoles.SECONDARY.getValue().equals(role.getValue()))
			uRole = ICimaCommonConstants.UserRoles.SECONDARY.getValue();
		
		AccountUserMap.AccountUserId pk = getAccountUserId(user, account);
		AccountUserMap uaMap = getAccountUserMap(pk, uRole);
		getSession().merge(uaMap);
		
    }
	
	
	protected AccountUserMap.AccountUserId getAccountUserId(Users user, Accounts account) {
		
		return new AccountUserMap.AccountUserId(user, account);
	}
	
	protected AccountUserMap getAccountUserMap(AccountUserMap.AccountUserId pk, String uRole) {
		
		return new AccountUserMap(pk, uRole);
	}
}
