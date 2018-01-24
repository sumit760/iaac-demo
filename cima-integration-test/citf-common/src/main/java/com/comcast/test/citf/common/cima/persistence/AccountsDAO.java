package com.comcast.test.citf.common.cima.persistence;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.FreshSSN;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * DAO class for user accounts. This class is accountable for
 * all the account related operation in the database.
 *  
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */

@Repository("accountDao")
public class AccountsDAO extends AbstractDAO{

	/**
	 * Gets the account object by account Id.
	 * 
	 * @param accId The account Id associated with the account.
	 * @return The Account object.
	 * @see Accounts
	 */
	@Transactional(readOnly=true)
	public Accounts findAccountById(String accId){
        Criteria criteria = getSession().createCriteria(Accounts.class);
        criteria.add(Restrictions.eq("billingAccountId",accId));
        return (Accounts) criteria.uniqueResult();
    }
	
	
	/**
	 * Gets the account object by user associated with the account.
	 * 
	 * @param user The user object.
	 * @return The Account object.
	 * @see Accounts, Users
	 */
	@Transactional(readOnly=true)
	public Accounts findAccountByUser(Users user){
		Accounts result = null;
		
		Criteria criteria = getSession().createCriteria(AccountUserMap.class);
	    criteria.add(Restrictions.eq("uaMapPrimaryKey.user", user));
	    List<AccountUserMap> resultset = (List<AccountUserMap>)criteria.list();
	   
	    if(resultset!=null && resultset.size()>0 && resultset.get(0)!=null && resultset.get(0).getUaMapPrimaryKey()!=null){
	    	result = resultset.get(0).getUaMapPrimaryKey().getAccount();
	    }
	    return result;
    }
	
	
	/**
	 * Gets the account details by environment.
	 * 
	 * @param environment The environment like "QA", "PROD" etc.
	 * @return The list of Accounts object in that environment.
	 */
	@Transactional(readOnly=true)
	public List<Accounts> findAccountsWithDtlsByEnvironment(String environment){
		List<Accounts> accList = null;
		Criteria criteria = getSession().createCriteria(Accounts.class);
	    criteria.add(Restrictions.eq("environment", environment));
	    Object objList = criteria.list();
		
		if(objList!=null){
			accList = (List<Accounts>)objList;
        
			for(Accounts acc : accList){
				
				//check and set primary and secondary user ids
				this.checkAndSetUserIds(acc);

				if(acc.getFreshSSN()!=null){
					FreshSSN ssnDtls = acc.getFreshSSN();
					if(ssnDtls.getSsn()!=null && ssnDtls.getDob()!=null && ssnDtls.getCreationDate()!=null){
						acc.setDob(MiscUtility.getDateOfBirthForSSN(ssnDtls.getDob(), ssnDtls.getCreationDate()));
						acc.setSsn(ssnDtls.getSsn());
					}
				}
			}
		}
		
        return accList;
    }
	
	
	/**
	 * Persists the account details in database. This method is used to load account data into the 
	 * database.
	 * 
	 * @param billingAccountId The billing accountID.
	 * @param serviceAccountId The service accountID.
	 * @param environment The environment.
	 * @param billingSystem The billing system like CSG, DST etc.
	 * @param authGuid The cstAuthGuid of the account.
	 * @param accountStatus The account status.
	 * @param firstName The first name of the account holder.
	 * @param lastName The last name of the account holder.
	 * @param phoneNumber The Phone number associated with the account.
	 * @param address The address associated with the account.
	 * @param zip The zipcode associated with the account.
	 * @param transferFlag The transfer flag to determine whether the user has been transferred
	 *                     from another account.
	 * @param physicalResourceLink The physical resources like STB, Cable Modem associated with the
	 *                             account. 
	 * @param freshSSN The SSN associated with the account.
	 * @param freshDob The date of birth associated with the SSN.
	 * @param ssnCreationDate SSN creation date.
	 */
	@Transactional
	public void populateAccount(	String billingAccountId,
									String serviceAccountId,
									String environment,
									String billingSystem,
									String authGuid, 
									String accountStatus, 
									String firstName, 
									String lastName,
									String phoneNumber,
									String address,
									String zip,
									String transferFlag,
									String physicalResourceLink,
									String freshSSN,
									Date freshDob,
									Date ssnCreationDate,
									String lob,
									String accountType){
		
		Object obj = findEntityByRequiredField(Accounts.class, "billingAccountId", billingAccountId);
		Accounts account = obj!=null ? (Accounts)obj : null;
		FreshSSN ssn = null;
		if(!StringUtility.isStringEmpty(freshSSN) && freshDob!=null){
			ssn = new FreshSSN(freshSSN, freshDob, ssnCreationDate);
		}
		
		if(account!=null){	
			account.setBillingSystem(billingSystem);
			account.setAuthGuid(authGuid);
			account.setAccountStatus(accountStatus);
			account.setEnvironment(environment);
			account.setServiceAccountId(serviceAccountId);
			account.setFirstName(firstName);
			account.setLastName(lastName);
			account.setAddress(address);
			account.setPhoneNumber(phoneNumber);
			account.setZip(zip);
			account.setTransferFlag(transferFlag);
			account.setPhysicalResourceLink(physicalResourceLink);
			account.setLob(lob);
			account.setAccountType(accountType);
			account.setFreshSSN(ssn);
			
		}else{
			account = new Accounts(billingAccountId, serviceAccountId, environment, billingSystem, authGuid, accountStatus, firstName, lastName, phoneNumber,
									address, zip, transferFlag, physicalResourceLink, lob, accountType, ssn);
		}
		getSession().merge(account);
    }
	
	
	private void checkAndSetUserIds(Accounts account){
		 if(account.getUserMap()!=null) {
             for (AccountUserMap uaMap : account.getUserMap()) {
                 if (uaMap != null && uaMap.getUaMapPrimaryKey() != null && uaMap.getUaMapPrimaryKey().getUser() != null) {
                     if (ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_SECONDARY.equals(uaMap.getRole())) {
                    	 account.addSecondaryUserId(uaMap.getUaMapPrimaryKey().getUser().getUserId());
                     } else {
                    	 account.addPrimaryUserId(uaMap.getUaMapPrimaryKey().getUser().getUserId());
                     }
                 }
             }
         }
	}
	
}
