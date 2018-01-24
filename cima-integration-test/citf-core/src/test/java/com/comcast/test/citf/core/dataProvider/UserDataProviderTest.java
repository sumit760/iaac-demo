package com.comcast.test.citf.core.dataProvider;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserAttributesCache.Attribute;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.cache.UserCache.User;
import com.comcast.test.citf.core.dataProvider.UserDataProvider.Filter;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
*
* @author Abhijit Rej
* 
* This is unit test class to test UserDataProvider.
*
*/
public class UserDataProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testSimpleFetchUser(){
		Object dpObj = null;
	
		try{
			AddUserInUserCache("usr1", "1234", UserDataProvider.UserCategory.LOGIN.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		UserCache.User usr = udp.fetchUser(null, false, UserDataProvider.UserCategory.LOGIN);
		
		Assert.assertTrue(usr instanceof UserCache.User);
	}
	
	@Test
	public void testFetchUserWithReusingSameUser(){
		Object dpObj = null;
	
		try{
			AddUserInUserCache("usr1", "1234", UserDataProvider.UserCategory.LOGIN.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			AddUserInUserCache("usr2", "1234", UserDataProvider.UserCategory.LOGIN.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		UserCache.User usr = udp.fetchUser(null, false, UserDataProvider.UserCategory.LOGIN);
		
		Assert.assertTrue(usr instanceof UserCache.User);
		
		UserCache.User usr2 = udp.fetchUser(null, true, UserDataProvider.UserCategory.LOGIN);
		
		Assert.assertTrue(usr2 instanceof UserCache.User);
		Assert.assertArrayEquals(new UserCache.User[]{usr}, new UserCache.User[]{usr2});
	}
	
	
	@Test
	public void testFetchUserWithValidFilter(){
		Object dpObj = null;
	
		try{
			AddUserInUserCache("usr1", "1234", UserDataProvider.UserCategory.LOGIN.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		Map<Filter, String> filters = ImmutableMap.of(Filter.LOGIN_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE);
		UserCache.User usr = udp.fetchUser(filters, false, UserDataProvider.UserCategory.LOGIN);
		
		Assert.assertTrue(usr instanceof UserCache.User);
	}
	
	
	@Test
	public void testFetchUserWithInvalidFilter(){
		Object dpObj = null;
	
		try{
			AddUserInUserCache("usr1", "1234", UserDataProvider.UserCategory.LOGIN.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		Map<Filter, String> filters = ImmutableMap.of(Filter.LOGIN_STATUS, ICimaCommonConstants.DB_STATUS_DELETED);
		UserCache.User usr = udp.fetchUser(filters, false, UserDataProvider.UserCategory.LOGIN);
		
		Assert.assertNull(usr);
	}
	
	
	@Test
	public void testSimpleFetchUserDetails(){
		Object dpObj = null;
		String userId = "user1";
		String accountId= "acc1";
	
		try{
			AddAccountInAccountCache(accountId, null, "DST", "fName", "lName", null, null, "aguid123", null, ICimaCommonConstants.DB_STATUS_ACTIVE, 
					null, null, null, null, null, null, ImmutableList.of(userId), null, ICimaCommonConstants.ENVIRONMENT_QA);
			AddUserInUserCache(userId, "1234", UserDataProvider.UserCategory.IDM.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			AddUserAttributeInUserAttributeCache("guid1", userId, null, null, null, null, null, null, null, null, null);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		UserDataProvider.UserObjects uObjs = udp.fetchUserDetails(null, false, UserDataProvider.UserCategory.IDM, false);
		
		Assert.assertTrue(uObjs instanceof UserDataProvider.UserObjects);
		Assert.assertNotNull(uObjs.getUser());
		Assert.assertNotNull(uObjs.getUserAttr());
		Assert.assertNotNull(uObjs.getAccount());
	}
	
	@Test
	public void testFetchUserDetailsWithLock(){
		Object dpObj = null;
		String userId = "userWithLock";
		String accountId= "accWithLock";
	
		try{
			AddAccountInAccountCache(accountId, null, "DST", "fName", "lName", null, null, "aguid123", null, ICimaCommonConstants.DB_STATUS_ACTIVE, 
					null, null, null, null, null, null, ImmutableList.of(userId), null, ICimaCommonConstants.ENVIRONMENT_QA);
			AddUserInUserCache(userId, "1234", UserDataProvider.UserCategory.IDM.getValue(), null, null, null, 
					ICimaCommonConstants.DB_STATUS_ACTIVE, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			AddUserAttributeInUserAttributeCache("guid1", userId, null, null, null, null, null, null, null, null, null);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_USER);
			
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS)
				.put(ConfigurationDataProvider.ConfigurationPropKeys.MAX_WAITING_TIME_TO_ACQUIRE_LOCK.getValue(), "1000", ICimaCommonConstants.ENVIRONMENT_QA);
		}catch(Exception e){
			Assert.fail("Not able to fetch UserDataPrivider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof UserDataProvider);
		
		UserDataProvider udp = (UserDataProvider)dpObj;
		udp.setLockWaitingTime(1000);
		UserDataProvider.UserObjects uObjs = udp.fetchUserDetails(null, false, UserDataProvider.UserCategory.IDM, true);
		Assert.assertTrue(uObjs instanceof UserDataProvider.UserObjects);
		
		//after locking
		UserDataProvider.UserObjects uObjs2 = udp.fetchUserDetails(null, false, UserDataProvider.UserCategory.IDM, false);
		Assert.assertNull(uObjs2);
		
		try{
			ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS).changeLock(uObjs.getUser().getUserId(), false); 
		}catch(Exception e){
			Assert.fail("Not able to unlock user due to "+e.getMessage());
		}
		
		//after unlocking
		UserDataProvider.UserObjects uObjs3 = udp.fetchUserDetails(null, false, UserDataProvider.UserCategory.IDM, false);
		Assert.assertTrue(uObjs3 instanceof UserDataProvider.UserObjects);
		Assert.assertNotNull(uObjs3.getUser());
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
	
	
	private void AddUserInUserCache(	String userID,
			   							String password,
			   							String category,
			   							String pin,
			   							String tvRating,
			   							String movieRating,
			   							String loginStatus,
			   							Set<String> primaryAccountIds,
			   							Set<String> secondaryAccountIds,
			   							String environment) {
		try{
			ICitfCache userCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USERS);

			User cachedUser = ((UserCache)userCache).new User(userID, password, category, pin,
						tvRating, movieRating, loginStatus, primaryAccountIds, secondaryAccountIds);

			Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<UserCache.InputMapKeys, Object>();
			ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);

			userCache.put(userID, ucMap, environment);
		}catch(Exception e){
			Assert.fail("Not able to populate UserCache due to "+e.getMessage());
		}

	}
	
	
	private void AddAccountInAccountCache(	String accountId,
												String serviceAccountId,
												String billingSystem,
												String firstName,
												String lastName,
												String address,
												String phoneNumber,
												String authGuid,
												String zip,
												String status,
												String transferFlag,
												String physicalResourceLink,
												String freshSsn,
												String freshDob,
												Set<String> lob,
												String accountType,
												List<String> primaryUserIds,
												List<String> secondaryUserIds,
												String environment) {
		try{
			ICitfCache accountCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT);
			
			AccountCache.Account acc = ((AccountCache)accountCache).new Account(accountId, serviceAccountId, billingSystem, firstName, lastName, address, phoneNumber, 
					authGuid, zip, status, transferFlag, physicalResourceLink, freshSsn, freshDob, lob, accountType, primaryUserIds, secondaryUserIds);
			
			accountCache.put(accountId, acc, environment);
		}catch(Exception e){
			Assert.fail("Not able to populate AccountCache due to "+e.getMessage());
		}
	}
	
	
	private void AddUserAttributeInUserAttributeCache(	String guid, 
														String userId,
														String email, 
														String alterEmail, 
														String alterEmailPassword,
														String secretQuestion,
														String secretAnswer,
														String fbId,
														String fbPassword,
														Date dob,
														String ssn) {

		try{
			ICitfCache userAttributeCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_USER_ATTRIBUTES);
			
			Attribute cachedUserAttribute = ((UserAttributesCache)userAttributeCache).new Attribute(guid, userId, email, alterEmail, alterEmailPassword, 
				secretQuestion, secretAnswer, fbId, fbPassword, dob, ssn);
			userAttributeCache.put(guid, cachedUserAttribute);
		}catch(Exception e){
			Assert.fail("Not able to populate userAttributeCache due to "+e.getMessage());
		}
	}
}
