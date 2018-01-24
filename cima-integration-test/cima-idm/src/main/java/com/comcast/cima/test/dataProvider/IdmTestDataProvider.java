package com.comcast.cima.test.dataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.cima.persistence.FreshUserDAO;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.dataProvider.ICommonDataProvider;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.mq.CommonMessageProducer;
import com.comcast.test.citf.core.mq.MQConnectionHandler;

/**
 * @deprecated This class will be removed when the new data providers will be implemented in IDM test cases (XCIMA-3982).
 * 
 * @author arej001c
 *
 */

@Deprecated
public abstract class IdmTestDataProvider extends CitfTestInitializer implements ICommonDataProvider, IDataProviderEnums{

	public Map<String, Object> getTestData(Set<String> keySet, Map<String, Object> optionalFilter, String environment, String sourceId) throws Exception {
		LOGGER.debug("Enter Data provider with keySet={}, optionalFilter={} and environment={}", keySet, optionalFilter, environment);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(keySet==null || keySet.isEmpty() || StringUtility.isStringEmpty(environment)){
			throw new Exception(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}
		
		userCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS);
		accountCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT);
		configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
		
		for(String key : keySet){
		
			if(key.equals(IDMGeneralKeys.USER_DETAILS.getValue())){
				this.fetchUserDetails(optionalFilter, environment, IDMGeneralKeys.USER_DETAILS.getValue(), sourceId, resultMap);
			}
			else if(key.equals(IDMGeneralKeys.FRESH_ACCOUNT.getValue())){
				ICitfCache accountCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT);
				
				Map<String, String> accountFilters = getRequiredFilter(optionalFilter, new String[]{FilterPrefix.ACCOUNT.getValue(), FilterPrefix.GENERAL.getValue()});
				if(accountFilters == null){
					accountFilters = new HashMap<String, String>();
				}
				
				accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE, ICommonConstants.CACHE_FLTR_VALUE_NULL);
				accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, environment);
				accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
				
				if(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN==null){
					accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
				}
				
				List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);
				
				if(accountObjs!=null && accountObjs.size()>0){
					Collections.shuffle(accountObjs);
					for(Object obj : accountObjs){
						AccountCache.Account acc = (AccountCache.Account)obj;
						try{
							accountCache.changeLock(acc.getAccountId(), true);
							resultMap.put(key, acc);
							break;
						}catch(LockException ale){
							LOGGER.warn("Not able to lock user due to : ", ale);
						}
					}
				}
			}
			else if(key.equals(IDMGeneralKeys.FRESH_USER.getValue())){
				FreshUserDAO fuDao = ObjectInitializer.getFreshUserDAO();
				List<FreshUserDAO.Query> queries = null;
				
				if(optionalFilter!=null && (optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL)!=null || 
						optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB)!=null || optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID)!=null)){
					
					queries = new ArrayList<FreshUserDAO.Query>();
					if(optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL)!=null){
						queries.add(this.prepareQueryToFetchFreshUser(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL, optionalFilter, fuDao));
					}
					if(optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB)!=null){
						queries.add(this.prepareQueryToFetchFreshUser(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB, optionalFilter, fuDao));
					}
					if(optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID)!=null){
						queries.add(this.prepareQueryToFetchFreshUser(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID, optionalFilter, fuDao));
					}
				}
				
				if(optionalFilter==null || optionalFilter.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID)==null){
					if(queries == null){
						queries = new ArrayList<FreshUserDAO.Query>();
					}
					
					queries.add(new FreshUserDAO.Query(FreshUserDAO.Columns.FACEBOOK, FreshUserDAO.Conditions.IS_NULL, null));
				}
				
				FreshUsers user = fuDao.findUser(queries);
				if(user!=null){
					resultMap.put(key, user);
				}
			}
			else if(key.equals(IDMGeneralKeys.CLIENT_ID_CLIENT_SECRET.getValue())){
				resultMap.put(KEY_CLIENT_ID, configCache.getString(IDataProviderEnums.ClientDetailsPropKeys.LOGIN_CLIENT_ID.getValue(), environment));
				resultMap.put(KEY_CLIENT_SECRET, configCache.getString(IDataProviderEnums.ClientDetailsPropKeys.LOGIN_CLIENT_SECRET.getValue(), environment));
			}
			else if(IDMGeneralKeys.SCOPE.getValue().equals(key) && optionalFilter.get(key)!=null){
				resultMap.put(key, fetchScope(optionalFilter.get(key).toString()));
			}
		}
		
		LOGGER.debug("Exit Data provider with resultMap={} for environment={}", resultMap, environment);
		return resultMap;
	}
	
	
	public String fetchScope(String key) throws Exception{
		String configKey = null;
		if(key!=null){
			configKey = IdmScopePropKeys.valueOf(key.toUpperCase()).getValue();
		}
		
		return configKey!=null? getConfigString(configKey) : null;
	}	
	
	protected void unlockResource(LockableResource resource, Object id, String sourceId){
		
		try{
			if(LockableResource.USER.equals(resource)){
				String userId = id.toString();
				ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS).changeLock(userId, false);
				
				if(getTestValue(sourceId, userId)!=null){
					removeTestValue(sourceId, userId);
				}
			}
			else if(LockableResource.FRESH_USER.equals(resource)){
				ObjectInitializer.getFreshUserDAO().unlockUser(Integer.parseInt(id.toString()));
			}
			else if(LockableResource.ACCOUNT.equals(resource)){
				String accId = id.toString();
				ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT).changeLock(accId, false);
			}
		}
		catch(LockException le){
			LOGGER.error("Not able to unlock {} due to : ",resource.name(), le);
		}
		catch(Exception e){
			LOGGER.error("Error occured while trying to unlock {}; error : ", resource.name(), e);
		}
	}
	
	
	protected void resetPassword(String userId, String password){
		try{
			((UserCache)ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS)).setPassword(userId, password);
			
			class ResetDBPassword extends CommonMessageProducer {
				public void sendResetMessage(String userId, String password) throws Exception{
					sendMessage(StringUtility.appendStrings(userId, ICimaCommonConstants.COMMA, password), MQConnectionHandler.QueueNames.Persist_User_Password_In_Database);
					LOGGER.info("Message sent for update database with user {}", userId);
				}
			}

			new ResetDBPassword().sendResetMessage(userId, password);
		}
		catch(Exception e){
			LOGGER.error("Error occured while trying to reset password for [user: {}, password: {}] :: ",userId, password, e);
		}
	}
	
	protected List<Map<String, Object>> fetchUser(Map<String, Object> filter, String environment, String sourceId) {
		String currentEnvironment = null;
		Map<String, Object> testData = null;
		Set<String> keySet = new HashSet<String>(Arrays.asList(IDMGeneralKeys.USER_DETAILS.getValue()));
		if (filter!=null) {
			try {
				currentEnvironment = getCurrentEnvironment();
				testData = getTestData(keySet, filter, currentEnvironment, sourceId);
			} catch (Exception e) {
				throw new RuntimeException("Falied to get test data. KeySet: " + keySet + " Filter: " + filter + " Environment: " + currentEnvironment + " SourceId: " + sourceId, e);
			}
		}
		if(testData!=null && testData.get(IDMGeneralKeys.USER_DETAILS.getValue())!=null){
			List<Map<String, Object>> lst = (List<Map<String, Object>>)testData.get(IDMGeneralKeys.USER_DETAILS.getValue());
			return lst;
		}
		
		return null;
	}			
	
	protected String getConfigString(String cacheKey) throws Exception {
		ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
		return configCache.getString(cacheKey, getCurrentEnvironment());
	}
	
	/*********************************** Private stuff ***********************************/
	
	ICitfCache userCache = null;
	ICitfCache accountCache = null;
	ICitfCache configCache = null;
	
	private void fetchUserDetails(	Map<String, Object> optionalFilter, 
									String environment, 
									String userKey,
									String sourceId, 
									Map<String, Object> result) throws Exception{
		
		if(getTestValue(sourceId, userKey)!=null){
			LOGGER.info("User details have been already fetched for same test. It'll going to use same user details.");
			result.put(userKey, getTestValue(sourceId, userKey));
			return;
		}
		
		int quantity = 0;
		Map <String, UserAttributesCache.Attribute> userAttrMap = null;
		String userIdsCsv = null;
		
		Map<String, String> userFilters = getRequiredFilter(optionalFilter, new String[]{FilterPrefix.USER.getValue(), FilterPrefix.GENERAL.getValue()});
		Map<String, String> userAttrFilters = getRequiredFilter(optionalFilter, new String[]{FilterPrefix.USER_ATTR.getValue(), FilterPrefix.GENERAL.getValue()});
		Map<String, String> accountFilters = getRequiredFilter(optionalFilter, new String[]{FilterPrefix.ACCOUNT.getValue(), FilterPrefix.GENERAL.getValue()});
		
		if(optionalFilter!=null && optionalFilter.get(ICommonConstants.CACHE_FLTR_CONDTN_QUANTITY)!=null){
			quantity = Integer.parseInt(optionalFilter.get(ICommonConstants.CACHE_FLTR_CONDTN_QUANTITY).toString());
		}
		
		if(quantity <= 0){
			quantity = 1;
		}
		
		//fetching user data
		if(userFilters == null){
			userFilters = new HashMap<String, String>();
		}
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, environment);
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_CATEGORY, ICimaCommonConstants.USER_CATEGORY_IDM);
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		List<Object> usrObjList = userCache.getFilteredObjects(userFilters, ICommonConstants.QUANTITY_UNLIMITED);
		
		if(usrObjList == null || usrObjList.size() == 0){
			return;
		}
		
		StringBuilder sbf = new StringBuilder();
		for(Object usrObj : usrObjList){
			if(sbf.length()>0){
				sbf.append(ICimaCommonConstants.COMMA);
			}
			
			UserCache.User usr = (UserCache.User)usrObj;
			sbf.append(usr.getUserId());
		}
		userIdsCsv = sbf.toString();
		
		if(userAttrFilters == null){
			userAttrFilters = new HashMap<String, String>();
		}
		userAttrFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV, userIdsCsv);
		
		if(userAttrFilters.get(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID)==null){
			userAttrFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);
		}
		
		List<Object> usrAttrObjs = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES).getFilteredObjects(userAttrFilters, ICommonConstants.QUANTITY_UNLIMITED);
		
		if(usrAttrObjs == null || usrAttrObjs.size() == 0){
			return;
		}
		
		sbf = new StringBuilder();
		userAttrMap =  new HashMap<String, UserAttributesCache.Attribute>();
		for(Object usrObj : usrAttrObjs){
			if(sbf.length()>0){
				sbf.append(ICimaCommonConstants.COMMA);
			}
			
			UserAttributesCache.Attribute attr = (UserAttributesCache.Attribute)usrObj;
			sbf.append(attr.getUserId());
			userAttrMap.put(attr.getUserId(), attr);
		}
		userIdsCsv = sbf.toString();
		
		
		if(accountFilters == null){
			accountFilters = new HashMap<String, String>();
		}
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV, userIdsCsv);
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, environment);
		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, ICommonConstants.QUANTITY_UNLIMITED);
		
		if(accountObjs == null || accountObjs.size() == 0){
			return;
		}
		
		List<Map<String, Object>> usrDtlsList = null;
		long maxLockWaitingTime = 300000;
		long deltaTime = 60000;
		try{		
			maxLockWaitingTime = Long.parseLong(configCache.getString(IDataProviderEnums.ConfigurationPropKeys.MAX_WAITING_TIME_TO_ACQUIRE_LOCK.getValue(), ICimaCommonConstants.ENVIRONMENT_ALL));
		}catch(Exception e){}
			
		while(deltaTime<=maxLockWaitingTime){
			boolean dataFound = false;
			usrDtlsList = getUserDtlsList(accountObjs, userAttrMap, quantity);
			if(usrDtlsList!= null){
				if(usrDtlsList.size()>=quantity){
					dataFound = true;
				}else{
					for(Map<String, Object> map : usrDtlsList){
						if(map!=null && map.get(KEY_USER_ID)!=null){
							try{
								userCache.changeLock(map.get(KEY_USER_ID).toString(), false);
							}catch(LockException ale){
								LOGGER.error("Not able to unlock user due to : ", ale);
							}
						}
					}
				}
			}
			
			if(dataFound)
				break;
			else{
				Thread.sleep(60000);
				deltaTime = deltaTime + 60000;
			}
				
		}
		
		if(usrDtlsList.size()>0){
			putTestValue(sourceId, userKey, usrDtlsList);
			result.put(userKey, usrDtlsList);
		}
	}
	
	
	private List<Map<String, Object>> getUserDtlsList(	List<Object> accountObjs, 
														Map<String, UserAttributesCache.Attribute> userAttrMap, 
														int quantity) throws Exception{
		int count = 0;
		List<Map<String, Object>> resultList = null;
		for(Object obj : accountObjs){
			if(obj!=null){
				AccountCache.Account account = (AccountCache.Account)obj;
				String usrId = account.getInputUserId();
				boolean isUserValid = false;
				try{
					userCache.changeLock(usrId, true);
					isUserValid = true;
				}catch(LockException ale){
					LOGGER.warn("Not able to lock user due to : ", ale);
				}
				
				if(isUserValid){
					if(resultList==null){
						resultList = new ArrayList<Map<String, Object>>();
					}
						
					UserAttributesCache.Attribute attr = userAttrMap.get(account.getInputUserId());
					attr.setPassword(((UserCache)userCache).getPassword(usrId));
				
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(KEY_USER, attr);
					map.put(KEY_ACCOUNT, account);
					map.put(KEY_USER_ID, usrId);
					resultList.add(map);
					count++;
				}
			}
			
			if(count==quantity){
				break;	
			}
		}
		return resultList;
	}
	
	private FreshUserDAO.Query prepareQueryToFetchFreshUser(String key, Map<String, Object> filter, FreshUserDAO fuDao){
		FreshUserDAO.Conditions condition = FreshUserDAO.Conditions.EXACT_VALUE;
		FreshUserDAO.Columns column = null;
		String value = null;
		
		if(ICimaCommonConstants.CACHE_FLTR_VALUE_NULL.equals(filter.get(key).toString())){
			condition = FreshUserDAO.Conditions.IS_NULL;
		} else if(ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL.equals(filter.get(key).toString())){
			condition = FreshUserDAO.Conditions.IS_NOT_NULL;
		} else{
			value = filter.get(key).toString();
		}
			
		if(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID.equals(key)){
			column = FreshUserDAO.Columns.FACEBOOK;
		} else if(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB.equals(key)){
			column = FreshUserDAO.Columns.DOB;
		} else if(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL.equals(key)){
			column = FreshUserDAO.Columns.ALTERNATE_EMAIL;
		}
		return new FreshUserDAO.Query(column, condition, value);
	}
	
	private static Map<String, String> getRequiredFilter(Map<String, Object> allFilters, String... prefixes) throws Exception{
		Map<String, String> filter = null;

		if(allFilters == null || prefixes == null || prefixes.length == 0) {
			return null;
		}

		filter = new HashMap<>();

		if(prefixes.length==1){
			String prefix = prefixes[0];
			for(Map.Entry<String, Object> entry: allFilters.entrySet()){
				String key = entry.getKey();
				if(key.contains(prefix)) {
					filter.put(key, entry.getValue().toString());
				}
			}
		}
		else{
			for(Map.Entry<String, Object> entry: allFilters.entrySet()){
				String key = entry.getKey();
				for(String prefix: prefixes) {
					if (key.contains(prefix)) {
						filter.put(key, entry.getValue().toString());
						break;
					}
				}

			}
		}

		if(filter.size() == 0) {
			filter = null;
		}

		return filter;
	}

	private static Logger LOGGER = LoggerFactory.getLogger(IdmTestDataProvider.class);
}