package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.UserCache.User;
import com.google.common.collect.ImmutableSet;

public class UserCacheTest {
	
	private ICitfCache userCache;
	private static final String EXCEPTION_INVALID_INPUT = "Input value object is invalid. It must contains User and may contain list of Channel Subscription map and LOB only!!!";
	
	
	@Before
	public void setup() {
		userCache = new UserCache();
	}

	@Test
	public void testPutWithInvalidParams()  {
		
		User cachedUser = getUser("1233",
				                  "password",
				                  "IDM", 
				                  "2221", 
				                  "TV-Y", 
				                  "R", 
				                  "A", 
				                  ImmutableSet.of("1163"), 
				                  ImmutableSet.of("1534"), 
				                  "QA");
		
		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);
		
		//null key
		Assert.assertFalse(userCache.put(null, ucMap, "QA"));
		
		//blank key
		Assert.assertFalse(userCache.put("", ucMap, "QA"));
		
		//value not of map type
		Assert.assertFalse(userCache.put("1233", "NotMapType", "QA"));

		//invalid environment
		Assert.assertFalse(userCache.put("1233", ucMap, "ABC"));
	}
	
	
	@Test
	public void testPutWithNullUserObject()  {
		
		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, null);
		
		//null key
		Assert.assertFalse(userCache.put("1234", ucMap, "QA"));
	}
	
	
	@Test
	public void testPutWithInvalidUserObjectType()  {
		
		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, "InvalidUserObjectType");
		
		//null key
		Assert.assertFalse(userCache.put("1234", ucMap, "QA"));
	}

	
	
	@Test
	public void testPut()  {
		
		populateUserCache("1233","password","IDM", "2221", "TV-Y", "R", "A", ImmutableSet.of("1163"), ImmutableSet.of("1534"), "QA");
		
		UserCache.User userRetrieved = (UserCache.User) userCache.getObject("1233");
		
		assertThat(
				userRetrieved.getUserId(),
				is("1233"));
		
		assertThat(
				userRetrieved.getPassword(),
				is("password"));
		
		assertThat(
				userRetrieved.getCategory(),
				is("IDM"));
		
		assertThat(
				userRetrieved.getPin(),
				is("2221"));
		
		assertThat(
				userRetrieved.getTvRating(),
				is("TV-Y"));
		
		assertThat(
				userRetrieved.getMovieRating(),
				is("R"));
		
		assertThat(
				userRetrieved.getLoginStatus(),
				is("A"));		
	}
	
	
	
	@Test
	public void testGetFilteredObjectsByEnvironment()  {
		
		
		populateUserCache("1234","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for LOGIN user in QA environment
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getUserId(),
				is("1234"));
		
	}

	
	
	@Test
	public void testGetFilteredObjectsByLockStatus()  {
		
		
		populateUserCache("1235","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for unlocked user
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_UNLOCKED, "true");
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getUserId(),
				is("1235"));
		
	}

	
	@Test
	public void testGetFilteredObjectsByLoginStatus()  {
		
		
		populateUserCache("1236","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user login status
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS, "A");
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getLoginStatus(),
				is("A"));
		
	}

	
	@Test
	public void testGetFilteredObjectsByCategory()  {
		
		
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user category
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_CATEGORY, ICimaCommonConstants.USER_CATEGORY_LOG_IN);
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getCategory(),
				is("LOGIN"));
		
	}

	
	@Test
	public void testGetFilteredObjectsByTVRating()  {
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user TV Rating
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TV_RATING, "TV-G");
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getTvRating(),
				is("TV-G"));
	}

	
	@Test
	public void testGetFilteredObjectsByMovieRating()  {
		
		
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user movie rating
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_MOVIE_RATING, "NR");
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				user.getMovieRating(),
				is("NR"));
		
	}

	
	@Test
	public void testGetFilteredObjectsByLob()  {
		
		
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1123"), ImmutableSet.of("2234"), "QA", 
								ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name()));
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user movie rating
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, ICimaCommonConstants.ENVIRONMENT_QA);
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV, ICimaCommonConstants.LOB.CDV.name());
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));		
	}
	
	@Test
	public void testChangeLock()  {
		
		populateUserCache("1235","password","LOGIN", "2227", "TV-PG", "MA", "A", ImmutableSet.of("1129"), ImmutableSet.of("2034"), "QA");
		
		//Lock success
		try{
			userCache.changeLock("1235", true);
		}catch(LockException e){
			Assert.fail("Exception occurred while locking the account " + e);
		}
		
		UserCache.User userRetrieved = (UserCache.User) userCache.getObject("1235");
		
		assertThat("Expected user to be locked", 
				userRetrieved.isLocked(), is(true));
		
		
		//Lock failure
		try{
			userCache.changeLock("1235", false);
		}catch(LockException e){
			Assert.fail("Exception occurred while unlocking the account " + e);
		}
		assertThat("Expected user to be unlocked", 
				userRetrieved.isLocked(), is(false));
		
	}
	
	@Test
	public void testGetFilteredObjectsByMultiplePrimaryAccountsFilter()  {
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1111","2222"), ImmutableSet.of("1234"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user TV Rating
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TV_RATING, "TV-G");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				(user.getPrimaryAccountIds()!=null && user.getPrimaryAccountIds().size()>1),
				is(true));
	}
	
	
	@Test
	public void testGetFilteredObjectsByMultipleSecondaryAccountsFilter()  {
		populateUserCache("1237","password","LOGIN", "2222", "TV-G", "NR", "A", ImmutableSet.of("1234"), ImmutableSet.of("1111","2222"), "QA");
		
		Map<String, String> userFilterConditions = new HashMap<String, String>();
		
		//Filter for user TV Rating
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TV_RATING, "TV-G");
		userFilterConditions.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);
		
		//Get user with the filter
		List<Object> usrObjList = userCache.getFilteredObjects(userFilterConditions, 1);
		
		assertThat(
				usrObjList,
				notNullValue());
		
		assertThat(
				usrObjList.size() > 0,
				is(true));
		
		UserCache.User user = (UserCache.User) usrObjList.get(0);
		
		assertThat(
				(user.getSecondaryAccountIds()!=null && user.getSecondaryAccountIds().size()>1),
				is(true));
	}
	
	
	@After
	public void tearDown()  {
		
		userCache.quit();
	}
	
	
	private void populateUserCache(String userID,
								   String password,
								   String category,
								   String pin,
								   String tvRating,
								   String movieRating,
								   String loginStatus,
								   Set<String> primaryAccountIds,
								   Set<String> secondaryAccountIds,
								   String environment)  {
		
		User cachedUser = ((UserCache)userCache).new User(userID, password, category, pin,
				tvRating, movieRating, loginStatus, primaryAccountIds, secondaryAccountIds);
		
		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<UserCache.InputMapKeys, Object>();
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);
		
		userCache.put(userID, ucMap, environment);
		
	}
	
	private void populateUserCache(	String userID,
			   						String password,
			   						String category,
			   						String pin,
			   						String tvRating,
			   						String movieRating,
			   						String loginStatus,
			   						Set<String> primaryAccountIds,
			   						Set<String> secondaryAccountIds,
			   						String environment,
			   						Set<String> lobs)  {

		User cachedUser = ((UserCache)userCache).new User(userID, password, category, pin,
				tvRating, movieRating, loginStatus, primaryAccountIds, secondaryAccountIds);

		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);
		ucMap.put(UserCache.InputMapKeys.LOB, lobs);

		userCache.put(userID, ucMap, environment);

	}
	
	private User getUser(String userID,
			   String password,
			   String category,
			   String pin,
			   String tvRating,
			   String movieRating,
			   String loginStatus,
			   Set<String> primaryAccountIds,
			   Set<String> secondaryAccountIds,
			   String environment)  {
		
		return ((UserCache)userCache).new User(userID, password, category, pin,
				tvRating, movieRating, loginStatus, primaryAccountIds, secondaryAccountIds);
	}

}