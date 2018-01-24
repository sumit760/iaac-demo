package com.comcast.test.citf.common.ldap;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * Service class for LDAP operations. This class supports querying LDAP for customers, 
 * authorizations/accounts, update fields in accounts or customer in LDAP and purge
 * users.
 * 
 * @author spal004c
 * @since 7/16/2015
 *
 */

@Service("ldapService")
public class LDAPInterface {

	private static final String O_COMCAST = ",o=Comcast";
	
	public enum SearchType{
		BEGINNING_WITH,
		NOT_BEGINNING_WITH,
		CONTAINING,
		NOT_CONTAINING,
		EQUAL_TO,
		NOT_EQUAL_TO,
		ENDING_WITH,
		NOT_ENDING_WITH
	}
	
	/**
	 * Enumeration for LDAP attributes to search for in customer and authorization.
	 */
	public enum LdapAttribute{
		CUSTOMER_GUID("cstCustGuid"),
		CUSTOMER_LOGIN_STATUS("cstLoginStatus"),
		CUSTOMER_CONTACT_EMAIL("cstContactEmail"),
		CUSTOMER_CONTACT_EMAIL_STATUS("cstContactEmailStatus"),
		CUSTOMER_PREFERRED_COMM_EMAIL("cstPreferredCommunicationEmail"),
		CUSTOMER_SECRET_QUESTION("cstPwdHint"),
		CUSTOMER_SECRET_ANSWER("cstPwdHintAnswer"),
		CUSTOMER_COMPROMISED_FLAG("cstCompromisedFlag"),
		CUSTOMER_UID_TIMESTAMP("uidTimeStamp"),
		CUSTOMER_MAIL("mail"),
		CUSTOMER_FIRST_NAME("givenName"),
		CUSTOMER_LAST_NAME("sn"),
		CUSTOMER_GUIDS("cstAuthGuids"),
		CUSTOMER_MANAGEE_GUIDS("cstManageeGuids"),
		CUSTOMER_MANAGER_GUIDS("cstManagerGuids"),
		CUSTOMER_ROLE("cstRole"),
		CUSTOMER_UID("uid"),
		CUSTOMER_MOBILE("mobile"),
		
		AUTHORIZATION_GUID("cstAuthGuid"),
		AUTHORIZATION_ACCOUNT_STATUS("cstAccountStatus"),
		AUTHORIZATION_ACCOUNT_TYPE("cstAccountType"),
		AUTHORIZATION_ACCOUNT_TIMESTAMP("cstBillingAccountTimestamp"),
		AUTHORIZATION_CDV_STATUS("cstDigitalVoiceStatus"),
		AUTHORIZATION_VID_STATUS("cstVIDStatus"),
		AUTHORIZATION_HSD_STATUS("cstHSDStatus"),
		AUTHORIZATION_TRANSFER_FLAG("cstTransferFlag"),
		AUTHORIZATION_TRANSFER_FROM_ACCOUNT_NO("cstTransferFromAccountNumber"),
		AUTHORIZATION_RANSFER_FROM_AUTH_GUID("cstTransferFromAuthGuid"),
		AUTHORIZATION_RANSFER_TIMESTAMP("cstTransferTimeStamp"),
		AUTHORIZATION_WIFI_STATUS("cstWifiStatus"),
		AUTHORIZATION_ACCOUNT_MANAGER_GUID("cstAccountManagerGuid"),
		AUTHORIZATION_ACCOUNT_PRIMARY_GUID("cstAccountPrimaryGuid"),
		AUTHORIZATION_BILLING_ACCOUNT_ID("cstBillingAccountId"),
		AUTHORIZATION_BILLING_SYSTEM_ID("cstBillingSystemId"),
		AUTHORIZATION_BILL_TO_FIRST_NAME("cstBillToFirstName"),
		AUTHORIZATION_BILL_TO_LAST_NAME("cstBillToLastName"),
		AUTHORIZATION_HOME_PHONE("cstHomePhone"),
		
		LOGIN_ID("loginId");
		
		private final String value;
        LdapAttribute(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Inner class to prepare LDAP instructions to be used in searching and updating LDAP.
	 */
	public class LDAPInstruction{
		
		/**
		 * Constructor for Search instruction
		 * 
		 * @param searchType The LDAP search type.
		 * @param searchKey The LDAP search attributes.
		 * @param searchValue The search string.
		 * 
		 * @see SearchType, LdapAttribute
		 */
		public LDAPInstruction(	SearchType searchType, 
				LdapAttribute searchKey, 
				String searchValue){

			this.searchType = searchType;
			this.searchKey = searchKey;
			this.searchValue = searchValue;
		}
		
		/**
		 * Constructor for Modify instruction.
		 * 
		 * @param searchType The LDAP search type.
		 * @param searchKey The LDAP search attributes.
		 * @param searchValue The search string.
		 * @param modifyList The list of LDAP attributes to modify.
		 * 
		 * @see SearchType, LdapAttribute
		 */
		public LDAPInstruction(	SearchType searchType, 
								LdapAttribute searchKey, 
								String searchValue, 
								Map<LdapAttribute, String> modifyList){
			
			this.searchType = searchType;
			this.searchKey = searchKey;
			this.searchValue = searchValue;
			this.modifyList = modifyList;
		}
		
		
		/**
		 * Gets the LDAP query filter.
		 * 
		 * @return The LDAP query filter.
		 * 
		 */
		public String getFilter(){
			String filter = null;
			
			if(searchType!=null && searchKey!=null && searchValue!=null){
				
				if(SearchType.EQUAL_TO.equals(searchType) || SearchType.NOT_EQUAL_TO.equals(searchType)){
					filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, searchKey.getValue(), ICommonConstants.EQUALS_SIGN, searchValue, ICommonConstants.END_ROUND_BRACE);
				
					if(SearchType.NOT_EQUAL_TO.equals(searchType)){
						filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, ICommonConstants.NOT_SIGN, filter, ICommonConstants.END_ROUND_BRACE);
					}
				}
				
				else if(SearchType.CONTAINING.equals(searchType) || SearchType.NOT_CONTAINING.equals(searchType)){
					filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, searchKey.getValue(), ICommonConstants.EQUALS_SIGN, 
							ICommonConstants.ASTERIX, searchValue, ICommonConstants.ASTERIX, ICommonConstants.END_ROUND_BRACE);
				
					if(SearchType.NOT_CONTAINING.equals(searchType)){
						filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, ICommonConstants.NOT_SIGN, filter, ICommonConstants.END_ROUND_BRACE);
					}
				}
				
				else if(SearchType.BEGINNING_WITH.equals(searchType) || SearchType.NOT_BEGINNING_WITH.equals(searchType)){
					filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, searchKey.getValue(), ICommonConstants.EQUALS_SIGN, 
							searchValue, ICommonConstants.ASTERIX, ICommonConstants.END_ROUND_BRACE);
				
					if(SearchType.NOT_BEGINNING_WITH.equals(searchType)){
						filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, ICommonConstants.NOT_SIGN, filter, ICommonConstants.END_ROUND_BRACE);
					}
				}
				
				else if(SearchType.ENDING_WITH.equals(searchType) || SearchType.NOT_ENDING_WITH.equals(searchType)){
					filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, searchKey.getValue(), ICommonConstants.EQUALS_SIGN, 
							ICommonConstants.ASTERIX, searchValue, ICommonConstants.END_ROUND_BRACE);
				
					if(SearchType.NOT_ENDING_WITH.equals(searchType)){
						filter = StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, ICommonConstants.NOT_SIGN, filter, ICommonConstants.END_ROUND_BRACE);
					}
				}
			}
			
			return filter;
		}
		
		/**
		 * Gets the LDAP modify list containing the modification items.
		 * 
		 * @return The LDAP modify list.
		 * 
		 */
		public Map<LdapAttribute, String> getModifyList() {
			return modifyList;
		}
		
		@Override
        public String toString() {
			boolean isModify = modifyList != null;
			return StringUtility.appendObjects("LDAP ",isModify?"Modify":"Search" ," Instruction has filter: ",this.getFilter(),isModify?(" and modifyList: "+modifyList):".");
        } 

		private final SearchType searchType;
		private final LdapAttribute searchKey;
		private final String searchValue;
		private Map<LdapAttribute, String> modifyList;
	}
	
	
	
	/**
	 * Returns a LDAPCustomer object after querying LDAP by cstCustGuid.
	 * 
	 * @param instruction The LDAP instruction for search.
	 * 
	 * @return The LDAPCustomer bean object.
	 * @see LDAPCustomer
	 */
	public synchronized LDAPCustomer getCustomerData(LDAPInstruction instruction) {
		if(!this.isOldRequest){
			LOGGER.info("Starting to fetch customer data from LDAP with instruction: {}", instruction);
		}
		
		DirContext ctx = null;
		@SuppressWarnings("rawtypes")
		NamingEnumeration results = null;
        LDAPCustomer cust = getLDAPCustomer();
        boolean found = false;
        
        try{
        	if(instruction==null){
        		throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
        	}
        	
        	ctx = getInitialDirContext();
        	SearchControls controls = getSearchControls();
        	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        	results = ctx.search(ICommonConstants.BLANK_STRING, instruction.getFilter(), controls);
        		
        	while (results.hasMore()) {
        		found = true;
        		SearchResult searchResult = (SearchResult) results.next();
        		Attributes attributes = searchResult.getAttributes();
        		Attribute attr = attributes.get(LdapAttribute.CUSTOMER_GUID.getValue());
        		if (attr != null && cust.getCstCustGuid() == null) {
        			cust.setCstCustGuid((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_GUIDS.getValue());
        		if (attr != null && cust.getCstAuthGuid() == null) {
        			cust.setCstAuthGuid((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_CONTACT_EMAIL.getValue());
        		if (attr != null && cust.getCstContactEmail() == null) {
        			cust.setCstContactEmail((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_CONTACT_EMAIL_STATUS.getValue());
        		if (attr != null && cust.getCstContactEmailStatus() == null) {
        			cust.setCstContactEmailStatus((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_PREFERRED_COMM_EMAIL.getValue());
        		if (attr != null && cust.getCstPreferredCommunicationEmail() == null) {
        			cust.setCstPreferredCommunicationEmail((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_SECRET_QUESTION.getValue());
        		if (attr != null && cust.getCstPwdHint() == null) {
        			cust.setCstPwdHint((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_SECRET_ANSWER.getValue());
        		if (attr != null && cust.getCstPwdHintAnswer() == null) {
        			cust.setCstPwdHintAnswer((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_COMPROMISED_FLAG.getValue());
        		if (attr != null && cust.getCstCompromisedFlag() == null) {
        			cust.setCstCompromisedFlag((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_MANAGEE_GUIDS.getValue());
        		if (attr != null) {
        			for (int ix = 0 ; ix < attr.size() ; ix++) {
        				String manageeGuid = (String) attr.get(ix);
        				cust.setCstManageeGuids(manageeGuid);
        			}
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_MANAGER_GUIDS.getValue());
        		if (attr != null) {
        			for (int ix = 0 ; ix < attr.size() ; ix++) {
        				String managerGuid = (String)attr.get(ix);
        				cust.setCstManagerGuids(managerGuid);
        			}
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_FIRST_NAME.getValue());
        		if (attr != null && cust.getFirstName() == null) {
        			cust.setFirstName((String) attr.get());
        		}       			
        		attr = attributes.get(LdapAttribute.CUSTOMER_LAST_NAME.getValue());
        		if (attr != null && cust.getLastName() == null) {
        			cust.setLastName((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_ROLE.getValue());
        		if (attr != null && cust.getRole() == null) {
        			cust.setRole((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_MAIL.getValue());
        		if (attr != null && cust.getEmail() == null) {
        			cust.setEmail((String) attr.get());
        		}
        		attr = attributes.get(LdapAttribute.CUSTOMER_UID.getValue());
        		if (attr != null && cust.getUid() == null) {
        			cust.setUid((String) attr.get());
        		}
        		
        		attr = attributes.get(LdapAttribute.CUSTOMER_MOBILE.getValue());
        		if (attr != null && cust.getMobile() == null) {
        			cust.setMobile((String) attr.get());
        		}
        	}
           	
        	if (!found) {
        		cust = null;
        	}
        }catch(NamingException e){
			LOGGER.error("Error occurred while fetching customer data: ", e);
		}
        finally{
        	if (results != null) {
    			try {
    				results.close();
    			} catch (Exception e) {
    				LOGGER.warn("Error occurred while trying to close NamingEnumeration: ", e);
    			}
    		}
    		if (ctx != null) {
    			try {
    				ctx.close();
    			} catch (Exception e) {
    				LOGGER.warn("Error occurred while trying to close Context: ", e);
    			}
    		}
        }
        if(!this.isOldRequest){
        	LOGGER.info("Customer data fetch from LDAP is over with result: {}", cust);
        }
        
        return cust;
       }


	/**
	 * Returns a LDAPAuthorization object after querying LDAP by cstAuthGuid.
	 * 
	 * @param instruction The LDAP instruction for search.
	 * 
	 * @return The LDAPAuthorization bean object.
	 * @see LDAPAuthorization
	 */
	public synchronized LDAPAuthorization getAuthorizationData(LDAPInstruction instruction){
		if(!this.isOldRequest){
			LOGGER.info("Starting to fetch authorization data from LDAP with instruction: {}", instruction);
		}	
		DirContext ctx = null;
        @SuppressWarnings("rawtypes")
		NamingEnumeration results = null;
        LDAPAuthorization auth = null;
        
        try{
        	if(instruction==null){
        		throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
        	}
        	ctx = getInitialDirContext();
          	SearchControls controls = getSearchControls();
           	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
           	
           	results = ctx.search(ICommonConstants.BLANK_STRING, instruction.getFilter(), controls);

           	while (results.hasMore()) {
           		auth = getLDAPAuthorization();
           		SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                
                Attribute attr = attributes.get(LdapAttribute.AUTHORIZATION_GUID.getValue());
                if (attr != null && auth.getCstAuthGuid() == null) {
                	auth.setCstAuthGuid((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_ACCOUNT_MANAGER_GUID.getValue());
                if (attr != null && auth.getCstAccountManagerGuid() == null) {
                	auth.setCstAccountManagerGuid((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_ACCOUNT_PRIMARY_GUID.getValue());
                if (attr != null && auth.getCstAccountPrimaryGuid() == null) {
                	auth.setCstAccountPrimaryGuid((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_ACCOUNT_STATUS.getValue());
                if (attr != null && auth.getAccountStatus() == null) {
                	auth.setAccountStatus((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID.getValue());
                if (attr != null && auth.getBillingId() == null) {
                	auth.setBillingId((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_BILLING_SYSTEM_ID.getValue());
                if (attr != null && auth.getBillingSystem() == null) {
                	auth.setBillingSystem((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_BILL_TO_FIRST_NAME.getValue());
                if (attr != null && auth.getFirstName() == null) {
                	auth.setFirstName((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_BILL_TO_LAST_NAME.getValue());
                if (attr != null && auth.getLastName() == null) {
                	auth.setLastName((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_CDV_STATUS.getValue());
                if (attr != null && auth.getDigitalVoiceStatus() == null) {
                	auth.setDigitalVoiceStatus((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_HSD_STATUS.getValue());
                if (attr != null && auth.getHsdStatus() == null) {
                	auth.setHsdStatus((String) attr.get());
                }
                attr = attributes.get(LdapAttribute.AUTHORIZATION_VID_STATUS.getValue());
                if (attr != null && auth.getVidStatus() == null) {
                	auth.setVidStatus((String) attr.get());
                }
           	}
        }catch(NamingException e){
			LOGGER.error("Error occurred while fetching authorization data: ", e);
		}finally{
        	if (results != null) {
    			try {
    				results.close();
    			} catch (Exception e) {
    				LOGGER.warn("Error occurred while trying to close NamingEnumeration: ", e);
    			}
    		}
    		if (ctx != null) {
    			try {
    				ctx.close();
    			} catch (Exception e) {
    				LOGGER.warn("Error occurred while trying to close Context: ", e);
    			}
    		}
        }
        if(!this.isOldRequest){
        	LOGGER.info("Authorization data fetch from LDAP is over with result: {}", auth);
        }
        return auth;
	}
	
	
	/**
	 * Purges the users provided in the list.
	 * 
	 * @param users The list of users to purge in LDAP.
	 */
	public synchronized void purgeUsers(List<String> users) throws NamingException {
		LOGGER.info("Starting to purge users with users: {}", users);
		this.isOldRequest = true;
		
		if (users != null && !users.isEmpty()) {
			for (String user : users ) {
				purgeUser(user+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);
			}
		}
	}
	

	/**
	 * Updates attribute of LDAP customer object.
	 *  
	 * @param cstContactEmail cstContactEmail.
	 */
	@SuppressWarnings("rawtypes")
	public synchronized void deleteUserLoginObject(String cstContactEmail) throws NamingException{

		if (cstContactEmail != null) {
			DirContext ctx = getInitialDirContext();
            SearchControls controls = getSearchControls();
           	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(ICommonConstants.BLANK_STRING, StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, LdapAttribute.LOGIN_ID.getValue(), ICommonConstants.EQUALS_SIGN,
           			cstContactEmail, ICommonConstants.END_ROUND_BRACE), controls);
           	
           	while (results != null && results.hasMore()) {
           		SearchResult searchResult = (SearchResult) results.next();
           		String nameSpaceToDelete = searchResult.getName();
				NamingEnumeration enumeration = ctx.listBindings(nameSpaceToDelete);
           		while (enumeration.hasMore()) {
           			Binding childEntry = (Binding)enumeration.next();
           			LdapName childName = getLdapName(nameSpaceToDelete);
           			childName.add(childEntry.getName());
           			ctx.destroySubcontext(childName);
           		}
           		if (nameSpaceToDelete.contains("AuxData") || nameSpaceToDelete.contains("Customer") || nameSpaceToDelete.contains("identityrole")) {
           			ctx.destroySubcontext(nameSpaceToDelete);
           		} 
           	}
           	
		}
	}
	

	public void updateAttributeOfCustomerObject(
			String inUserId, String inAttributeName, String inAttributeValue) throws NamingException, IllegalStateException {
		DirContext ctx = getInitialDirContext();
		SearchControls controls = getSearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		LDAPInstruction li = getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_UID, inUserId);
		@SuppressWarnings("rawtypes")
		NamingEnumeration results = ctx.search("ou=Customer", li.getFilter(), controls);
		SearchResult searchResult;
		if (results.hasMore()) {
			searchResult = (SearchResult) results.next();
		} else {
			throw new IllegalStateException("No user found with ESD (LDAP) uid = '" + inUserId + "'");
		}
		ModificationItem[] mods = getModificationItem(1);
		Attribute mod = getAttribute(inAttributeName, inAttributeValue);
		mods[0] = getModificationItem(DirContext.REPLACE_ATTRIBUTE, mod);
		ctx.modifyAttributes(searchResult.getNameInNamespace().replace(O_COMCAST, 
				ICommonConstants.BLANK_STRING), mods);
	}
	
	public void updateAttributeOfAuthorizationObject(String inAccountId, String inAttributeName, String inAttributeValue) throws NamingException, IllegalStateException{
		DirContext ctx = getInitialDirContext();
		SearchControls controls = getSearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		LDAPInstruction li = getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID, inAccountId);
		@SuppressWarnings("rawtypes")
		NamingEnumeration results = ctx.search("ou=Authorization", li.getFilter(), controls);
		SearchResult searchResult;
		if (results.hasMore()) {
			searchResult = (SearchResult) results.next();
		} else {
			throw new IllegalStateException("No account found with ESD (LDAP) account id = '" + inAccountId + "'");
		}
		ModificationItem[] mods = getModificationItem(1);
		Attribute mod = getAttribute(inAttributeName, inAttributeValue);
		mods[0] = getModificationItem(DirContext.REPLACE_ATTRIBUTE, mod);
		ctx.modifyAttributes(searchResult.getNameInNamespace().replace(O_COMCAST, ICommonConstants.BLANK_STRING), mods);
	}
	
	
	/**
	 * Update LDAP attributes based on the update LDAP instruction.
	 * 
	 * @param instructions The list of LDAP update instructions.
	 */
	@SuppressWarnings("rawtypes")
	public void update(List<LDAPInstruction> instructions) throws NamingException{
		DirContext ctx = null;
		LOGGER.info("Starting to update LDAP with instructions : {}", instructions);
		
		try{
			if (instructions != null && instructions.size()>0) {
				for(LDAPInstruction instruction : instructions){
					ctx = getInitialDirContext();
					SearchControls controls = getSearchControls();
					controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
					NamingEnumeration results = ctx.search(ICommonConstants.BLANK_STRING, instruction.getFilter(), controls);
					while (results.hasMore()) {
						SearchResult searchResult = (SearchResult) results.next();
						if(searchResult.getName().contains("ou=identityrole")){
							continue;
						}
						String NameSpaceToUpdate = searchResult.getNameInNamespace();
						NameSpaceToUpdate = NameSpaceToUpdate.replace(O_COMCAST, ICommonConstants.BLANK_STRING);
	           		
	           			Map<LdapAttribute, String> modifyList = instruction.getModifyList();
	           			ModificationItem[] mods = getModificationItem(modifyList.size());
	           		
	           			int index = 0;
	           			for(Map.Entry<LdapAttribute, String> entry : modifyList.entrySet()){
	           				LdapAttribute attr = entry.getKey();
	           				
	           				Attribute mod = getAttribute(attr.getValue(), entry.getValue());
	           				mods[index] = getModificationItem(DirContext.REPLACE_ATTRIBUTE, mod);
	           				index++;
	           			}
	           			ctx.modifyAttributes(NameSpaceToUpdate, mods);
	           			LOGGER.info("LDAP has been successfully updated for instruction : {}", instruction);
					}
				}
			}
		}finally{
			try {
				if (ctx != null) {
					ctx.close();
				}
			}catch(NamingException ne){
				LOGGER.warn("Error occurred while trying to close Context: ", ne);
			}
		}
	}
	
	
	//********************************************** Private methods **********************************************************************
	
	static final String  TREE_DELETE_CONTROL_OID = "1.2.840.113556.1.4.805";
	
	private Hashtable<String,String> env = new Hashtable<String,String>();
	private boolean isOldRequest = false;
	
	/**
	 * Constructor for initializing LDAP connection
	 */
	LDAPInterface() {
		loadDefaultEnv();
	}
	
	protected LDAPCustomer getLDAPCustomer() {
		return new LDAPCustomer();
	}
	
	protected LDAPAuthorization getLDAPAuthorization() {
		return new LDAPAuthorization();
	}
	
	protected DirContext getInitialDirContext() throws NamingException {
		return new InitialDirContext(env);
	}
	
	protected SearchControls getSearchControls() {
		return new SearchControls();
	}
	
	protected LdapName getLdapName(String nameSpace) throws InvalidNameException {
		return new LdapName(nameSpace);
	}
	
	protected ModificationItem[] getModificationItem(int size) {
		return new ModificationItem[size];
	}
	
	protected Attribute getAttribute(String key, String value) {
		return new BasicAttribute(key,value);
	}
	
	protected ModificationItem getModificationItem(int mod_op,Attribute attr) {
		return new ModificationItem(mod_op, attr);
	}
	
	protected LDAPInstruction getLDAPInstruction(SearchType st, LdapAttribute ldapAttribute, String userId) {
		return new LDAPInstruction(st, ldapAttribute, userId);
	}
	
	protected void purgeUser(String user) throws NamingException {
		if (user != null) {
			LDAPAuthorization auth = getLDAPAuthorization();
			LDAPCustomer cust = getCustomerData(	getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_MAIL, user));
			if (cust != null){
				auth = getAuthorizationData(getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.AUTHORIZATION_GUID, cust.getCstAuthGuid()));
			}
			if (cust != null && auth!= null && cust.getCstManageeGuids().size() == 1 && cust.getCstManageeGuids().get(0).equals(cust.getCstCustGuid())) {
				//No secondary user
				deleteUserAssociations(cust.getCstCustGuid());
				resetAuthorization(auth.getCstAuthGuid());
				LOGGER.info("{} user successfully purged.", cust.getCstCustGuid());
			}
		}
		
	}
	
	
	public boolean isUserDeleted(String user) {
		boolean result = false;
		if (user != null) {
			LDAPCustomer cust = getCustomerData(	new LDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_MAIL, user));
			if(cust == null){
				result = true;
			}
		}
		return result;
	}

	
	@SuppressWarnings("rawtypes")
	protected void deleteUserAssociations(String cstCustGuid) throws NamingException {
		if (cstCustGuid != null) {
			DirContext ctx = getInitialDirContext();
            SearchControls controls = getSearchControls();
           	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(ICommonConstants.BLANK_STRING, StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, LdapAttribute.CUSTOMER_GUID.getValue(), ICommonConstants.EQUALS_SIGN,
           			cstCustGuid, ICommonConstants.END_ROUND_BRACE), controls);
           	
           	while (results!=null && results.hasMore()) {
           		SearchResult searchResult = (SearchResult) results.next();
           		String NameSpaceToDelete = searchResult.getName();
				NamingEnumeration enumeration = ctx.listBindings(NameSpaceToDelete);
           		while (enumeration.hasMore()) {
           			Binding childEntry = (Binding)enumeration.next();
           			LdapName childName = getLdapName(NameSpaceToDelete);
           			childName.add(childEntry.getName());
           			ctx.destroySubcontext(childName);
           		}
           		if (NameSpaceToDelete.contains("AuxData") || NameSpaceToDelete.contains("Customer") || NameSpaceToDelete.contains("identityrole")) {
           			ctx.destroySubcontext(NameSpaceToDelete);
           		} 
           	}
           	
		}
	}
	
	
	protected void resetAuthorization(String cstAuthGuid) throws NamingException {

		if (cstAuthGuid != null) {
			DirContext ctx = getInitialDirContext();
           	SearchControls controls = getSearchControls();
           	controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(ICommonConstants.BLANK_STRING, StringUtility.appendStrings(ICommonConstants.START_ROUND_BRACE, LdapAttribute.AUTHORIZATION_GUID.getValue(), ICommonConstants.EQUALS_SIGN,
           			cstAuthGuid, ICommonConstants.END_ROUND_BRACE), controls);
           	while (results.hasMore()) {
           		SearchResult searchResult = (SearchResult) results.next();
           		String NameSpaceToUpdate = searchResult.getNameInNamespace();
           		NameSpaceToUpdate = NameSpaceToUpdate.replace(O_COMCAST, "");
           		ModificationItem[] mods = getModificationItem(2);
           		Attribute mod0 = getAttribute(LdapAttribute.AUTHORIZATION_ACCOUNT_MANAGER_GUID.getValue(), ICommonConstants.BLANK_SPACE_STRING);
           	    Attribute mod1 = getAttribute(LdapAttribute.AUTHORIZATION_ACCOUNT_PRIMARY_GUID.getValue(), ICommonConstants.BLANK_SPACE_STRING);
           	    mods[0] = getModificationItem(DirContext.REPLACE_ATTRIBUTE, mod0);
           	    mods[1] = getModificationItem(DirContext.REPLACE_ATTRIBUTE, mod1);
           	    ctx.modifyAttributes(NameSpaceToUpdate, mods);
           	}
		}
	}
	
	private void loadDefaultEnv() {
		env.put(Context.INITIAL_CONTEXT_FACTORY, ICimaCommonConstants.LDAP_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, "ldap://" + ICimaCommonConstants.LDAP_HOSTNAME + ":" + ICimaCommonConstants.LDAP_PORT + "/o=" + ICimaCommonConstants.LDAP_ORG);
		env.put(Context.SECURITY_AUTHENTICATION, ICimaCommonConstants.LDAP_SECURITY_AUTHENTICATION);
		env.put(Context.SECURITY_PRINCIPAL, ICimaCommonConstants.LDAP_SECURITY_PRINCIPAL);
		env.put(Context.SECURITY_CREDENTIALS, ICimaCommonConstants.LDAP_SECURITY_CREDENTIALS);

	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LDAPInterface.class);
	
}
