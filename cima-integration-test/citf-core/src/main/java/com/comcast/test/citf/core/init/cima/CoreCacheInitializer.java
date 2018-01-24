package com.comcast.test.citf.core.init.cima;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.cima.persistence.AccountsDAO;
import com.comcast.test.citf.common.cima.persistence.UserAttributesDAO;
import com.comcast.test.citf.common.cima.persistence.UserChannelDAO;
import com.comcast.test.citf.common.cima.persistence.UserDAO;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.UserAttributes;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.reader.PropertyReader;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.AccountCache.Account;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.cache.UserCache.User;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This class is used for populating caches with the data fetched from database and configuration properties files.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 */

public class CoreCacheInitializer{

	private ICitfCache configCache = null;
	private ICitfCache userCache = null;
	private ICitfCache userAttributesCache = null;
	private ICitfCache stringCache = null;
	private ICitfCache accountCache = null;

	private UserDAO userDAO = null;
	private UserChannelDAO channelSubsDAO = null;
	private UserAttributesDAO userAttrsDAO = null;
	private AccountsDAO accountDAO = null;
	
	private Map<String, Set<String>> lobMap = null; 

	/**
	 * Only public method in this class to populate all caches
	 * 
	 * @param commonStringCacheParams
	 * 			A few Strings values which have come as input and will be used frequently by other classes, like current environment
	 */
	public void initialize(Map<String, String> commonStringCacheParams) throws IOException{

		configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
		userCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS);
		stringCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_STRING);
		userAttributesCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES);
		accountCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT);

		userDAO = ObjectInitializer.getUserDAO();
		channelSubsDAO = ObjectInitializer.getUserChannelDAO();
		userAttrsDAO = ObjectInitializer.getUserAttributeDAO();
		accountDAO = ObjectInitializer.getAccountsDAO();

		String domain = commonStringCacheParams.get(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN);
		String environment = commonStringCacheParams.get(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT);

		//loading common parameters (not any environment specific) and properties params
		loadConfigParamsAndTestDataFromProperties(environment, domain);
		loadStringCache(commonStringCacheParams);

		//loading parameters for specific environment
		loadAccountsFromDB(environment);
		loadUsersFromDB(environment);
	}


	/**
	 * Reads configuration properties files and populate ConfigCache
	 * 
	 * @param environment
	 * 			Test environment
	 * @param domain
	 * 			Test domain
	 */
	private void loadConfigParamsAndTestDataFromProperties(String environment, String domain) throws IOException{
		//loading core config data
		loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_CORE_CONFIGURATION, ICimaCommonConstants.ENVIRONMENT_ALL);
		
		//loading environmental config data
		switch(environment){
			case ICimaCommonConstants.ENVIRONMENT_STAGE:
				loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_GENERAL_STAGE_CONFIGURATION, environment);
				if(ICimaCommonConstants.URI_DOMAIN_XFINITY.equalsIgnoreCase(domain)){
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_XFINITY_STAGE_CONFIGURATION, environment);
				}else{
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_COMCAST_STAGE_CONFIGURATION, environment);
				}
				break;
				 
			case ICimaCommonConstants.ENVIRONMENT_PROD:
				loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_GENERAL_PROD_CONFIGURATION, environment);
				if(ICimaCommonConstants.URI_DOMAIN_XFINITY.equalsIgnoreCase(domain)){
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CIT_XFINITYF_PROD_CONFIGURATION, environment);
				}else{
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_COMCAST_PROD_CONFIGURATION, environment);
				}
				break;
				 
			default:
				loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_GENERAL_QA_CONFIGURATION, environment);
				if(ICimaCommonConstants.URI_DOMAIN_XFINITY.equalsIgnoreCase(domain)){
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_XFINITY_QA_CONFIGURATION, environment);
				}else{
					loadPropertyFileInCache(ICimaCommonConstants.PROPERTY_FILE_CITF_COMCAST_QA_CONFIGURATION, environment);
				}
				break;
		}		
	}

	/**
	 * Fetches Accounts table from database and populate AccountCache
	 * 
	 * @param environment
	 * 			Test environment
	 */
	private void loadAccountsFromDB(String environment) {

//TODO: Due to lack of test accounts currently PROD is running with STAGE data and similar for DEV which is using QA data. 
//		But this should be changed while separate set of data will be available for all environments.
		String accEnvironment = environment;
		switch (environment) {
			case ICimaCommonConstants.ENVIRONMENT_PROD:
				accEnvironment = ICimaCommonConstants.ENVIRONMENT_STAGE;
				break;
			case ICimaCommonConstants.ENVIRONMENT_DEV:
				accEnvironment = ICimaCommonConstants.ENVIRONMENT_QA;
				break;
		}

		List<Accounts> accountList = accountDAO.findAccountsWithDtlsByEnvironment(accEnvironment);

		if(accountList == null || accountList.isEmpty()) {
			LOGGER.warn("No account found from database for {} environment", environment);
		}
		else{
			lobMap = new HashMap<String, Set<String>>();
			for(Accounts account : accountList){
				if(account!=null && account.getBillingAccountId()!=null && account.getAuthGuid()!=null){
					Account cachedService = ((AccountCache)accountCache).new Account(account.getBillingAccountId(), account.getServiceAccountId(), account.getBillingSystem(), 
							account.getFirstName(), account.getLastName(), account.getAddress(), account.getPhoneNumber(), account.getAuthGuid(), 
							account.getZip(), account.getAccountStatus(), account.getTransferFlag(), account.getPhysicalResourceLink(), account.getSsn(), 
							account.getDob(), account.getLob(), account.getAccountType(), account.getPrimaryUserIds(), account.getSecondaryUserIds());
					accountCache.put(account.getBillingAccountId(), cachedService, environment);
					
					if(account.getLob()!=null){
						lobMap.put(account.getBillingAccountId(), account.getLob());
					}
				}
			}
			LOGGER.info("Accounts loaded for {} environment.", environment);
		}
	}
	

	/**
	 * Fetches Users, User_Attributes, User_Service_Map and User_Account_Map tables from database and populate UserCache and UserAttributeCache
	 * 
	 * @param environment
	 * 			Test environment
	 */
	private void loadUsersFromDB(String environment){
		String userEnvironment = environment;
		switch (environment) {
			case ICimaCommonConstants.ENVIRONMENT_PROD:
				userEnvironment = ICimaCommonConstants.ENVIRONMENT_STAGE;
				break;
			case ICimaCommonConstants.ENVIRONMENT_DEV:
				userEnvironment = ICimaCommonConstants.ENVIRONMENT_QA;
				break;
		}

		List<Users> userList = userDAO.findUsersWithDtlsByEnvironment(userEnvironment);
		if(userList == null || userList.isEmpty()) {
			LOGGER.warn("No users found from database for {} environment", environment);
		}

		else{
			for(Users user : userList){
				if(user!=null && user.getUserId()!=null && user.getEnvironment()!=null && userEnvironment.equals(user.getEnvironment())){
					String userId = user.getUserId();
					
					User cachedUser = ((UserCache)userCache).new User(userId, user.getPassword(), user.getCategory(), user.getPin(),
							user.getTvRating(), user.getMovieRating(), user.getLoginStatus(), user.getPrimaryAccountIds(), user.getSecondaryAccountIds());
					Map<String, String> channelSubscriptionMap = channelSubsDAO.findChannelSubscriptionByUserId(user);
					
					Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
					ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);

					if(channelSubscriptionMap!=null) {
						ucMap.put(UserCache.InputMapKeys.CHANNEL_SUBSCRIPTION_MAP, channelSubscriptionMap);
					}
					if(lobMap!=null && user.getPrimaryAccountIds()!=null){
						Set<String> lobs = null;
						for(String accountId : user.getPrimaryAccountIds()){
							if(lobMap.get(accountId)!=null){
								if(lobs==null){
									lobs = new HashSet<String>();
								}	
								lobs.addAll(lobMap.get(accountId));
							}
						}
						
						if(lobs!=null){
							ucMap.put(UserCache.InputMapKeys.LOB, lobs);
						}
					}
							
					userCache.put(user.getUserId(), ucMap, environment);
					
					//Fetching user attributes
					UserAttributes usrAttr = userAttrsDAO.findUserAttributesByUser(user);
					if(usrAttr!=null && usrAttr.getGuid()!=null){
						UserAttributesCache.Attribute attr = ((UserAttributesCache)userAttributesCache).new Attribute(usrAttr.getGuid(), user.getUserId(),
								usrAttr.getEmail(), usrAttr.getAlterEmail(), usrAttr.getAlterEmailPassword(), usrAttr.getSecretQuestion(),
								usrAttr.getSecretAnswer(), usrAttr.getFbId(), usrAttr.getFbPassword(), usrAttr.getDob(), usrAttr.getSsn());
						userAttributesCache.put(user.getUserId(), attr);
					}
				}
			}

			LOGGER.info("Users/attributes loaded for {} environment.", environment);
		}
	}
	

	/**
	 * Populate StringCache with the values coming as inputs of build command
	 * 
	 * @param params
	 * 			Parameter map
	 */
	private void loadStringCache(Map<String, String> params) {
		if(params!=null && !params.isEmpty()){
			stringCache.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, params.get(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT));
			stringCache.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN, params.get(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN));

			if (params.containsKey(ICimaCommonConstants.STRING_CACHE_KEY_UI_TEST_EXEC_ENV)) {
				stringCache.put(ICimaCommonConstants.STRING_CACHE_KEY_UI_TEST_EXEC_ENV, params.get(ICimaCommonConstants.STRING_CACHE_KEY_UI_TEST_EXEC_ENV));
			}
		}
	}
	
	/**
	 * Utility method to read individual properties file and then populates ConfigCache
	 * 
	 * @param propertyFileName
	 * 			Name of the property file
	 * @param environment
	 * 			Test environment
	 * @throws IOException
	 */
	private void loadPropertyFileInCache(String propertyFileName, String environment) throws IOException{
		Properties file = PropertyReader.loadPropertyFile(propertyFileName);
		Set<Object> propObjects = PropertyReader.getPropertyKeySet(file);
		
		for(Object keyObj : propObjects){
			if(keyObj != null && !PropertyReader.getProperty(file, keyObj.toString()).isEmpty()){
				String key = keyObj.toString();
				configCache.put(key, PropertyReader.getProperty(file, key), environment);
			}
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CoreCacheInitializer.class);
}