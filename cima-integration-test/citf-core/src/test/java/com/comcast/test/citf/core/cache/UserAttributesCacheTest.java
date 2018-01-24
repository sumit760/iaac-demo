package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserAttributesCache.Attribute;

public class UserAttributesCacheTest {

	private ICitfCache userAttributeCache;
	private static final String EXCEPTION_INVALID_INPUT = "Invalid input(s)!!";
	private static final String EXCEPTION_INVALID_ATTRIBUTE = "Input User Attributes object should have all mandatory values!!";

	@Before
	public void setup()
	{
		userAttributeCache = new UserAttributesCache();
	}

	@Test
	public void testPutWithInvalidParameters() 
	{
	
		Attribute cachedUserAttribute = getAttribute("guid",
										   "userId",
										   "email@gmail.com",
										   "alterEmail@gmail.com",
										   "alterEmailPassword",
										   "secretQuestion",
										   "secretAnswer",
										   "fbId",
										   "fbPassword",
										   getDate(),
										   "ssn");
		//null key
		try {
			userAttributeCache.put(null, cachedUserAttribute);
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}
		

		//blank key
		try {
			userAttributeCache.put("", cachedUserAttribute);
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}


		//null value
		try {
			userAttributeCache.put("guid", null);
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}

		//value not instance of Attribute
		try {
			userAttributeCache.put("guid", "InvalidValueType");
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_INPUT));
		}

	}

	
	@Test
	public void testPutWithInvalidGuid() 
	{
	
		Attribute cachedUserAttribute = getAttribute("",
										   "userId",
										   "email@gmail.com",
										   "alterEmail@gmail.com",
										   "alterEmailPassword",
										   "secretQuestion",
										   "secretAnswer",
										   "fbId",
										   "fbPassword",
										   getDate(),
										   "ssn");
		//blank guid in Attribute
		try {
			userAttributeCache.put("guid", cachedUserAttribute);
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_ATTRIBUTE));
		}

	}

	
	@Test
	public void testPutWithInvalidUserId() 
	{
	
		Attribute cachedUserAttribute = getAttribute("guid",
										   "",
										   "email@gmail.com",
										   "alterEmail@gmail.com",
										   "alterEmailPassword",
										   "secretQuestion",
										   "secretAnswer",
										   "fbId",
										   "fbPassword",
										   getDate(),
										   "ssn");
		//blank guid in Attribute
		try {
			userAttributeCache.put("guid", cachedUserAttribute);
		} catch (Exception e) {
			assertThat(
				e.getMessage(),
				is(EXCEPTION_INVALID_ATTRIBUTE));
		}

	}

	

	@Test
	public void testGetFilteredObjectsByEmail()  {
		
		//Populate user
		populateUserAttributeCache("guid",
								   "userId",
								   "email@gmail.com",
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "ssn");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Filter by User Email
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());

		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify email is not null
		assertThat(
				attributeRetrieved.getEmail(),
				is("email@gmail.com"));

	}

	
	@Test
	public void testGetFilteredObjectsByAlternateEmail()  {
		
		//Populate user
		populateUserAttributeCache("guid",
								   "userId",
								   "email@gmail.com",
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "ssn");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Filter by user alternate Email
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());

		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify alternate email is not null
		assertThat(
				attributeRetrieved.getAlterEmail(),
				is("alterEmail@gmail.com"));

	}


	@Test
	public void testGetFilteredObjectsBySecretQuestions()  {
		
		//Populate user
		populateUserAttributeCache("guid",
								   "userId",
								   "email@gmail.com",
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "ssn");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Filter by secret question
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());

		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify secret question is not null
		assertThat(
				attributeRetrieved.getSecretQuestion(),
				is("secretQuestion"));

	}

	
	@Test
	public void testGetFilteredObjectsByFacebookId()  {
		
		//Populate user
		populateUserAttributeCache("guid",
								   "userId",
								   "email@gmail.com",
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "ssn");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Filter by Facebook Id
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());

		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify facebook Id is not null
		assertThat(
				attributeRetrieved.getFbId(),
				is("fbId"));

	}


	@Test
	public void testGetFilteredObjectsBySSN()  {
		
		//Populate user
		populateUserAttributeCache("guid",
								   "userId",
								   "email@gmail.com",
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "1101");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Filter by SSN
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());

		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify SSN is not null
		assertThat(
				attributeRetrieved.getSsn(),
				is("1101"));

	}

	
	@Test
	public void testGetFilteredObjectsByWrongFilter()  {
		
		//Populate user so that user email is null
		populateUserAttributeCache("guid",
								   "userId",
								   null,
								   "alterEmail@gmail.com",
								   "alterEmailPassword",
								   "secretQuestion",
								   "secretAnswer",
								   "fbId",
								   "fbPassword",
								   getDate(),
								   "ssn");

		Map<String, String> userFilters = new HashMap<String, String>();
		
		//Set the filter as USER_ATTR_EMAIL = NOT NULL
		//This should not return any thing
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL,ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL );
		List<Object> usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());
		
		assertThat(
				usrAttributeObjList.size() == 0,
				is(true));

		//Now set the filter as USER_ATTR_EMAIL = NULL
		//This should return the user attribute
		
		userFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL,ICimaCommonConstants.CACHE_FLTR_VALUE_NULL );
		usrAttributeObjList = userAttributeCache.getFilteredObjects(userFilters, 1);

		assertThat(
				usrAttributeObjList,
				notNullValue());
		
		assertThat(
				usrAttributeObjList.size() > 0,
				is(true));

		UserAttributesCache.Attribute attributeRetrieved = (UserAttributesCache.Attribute) usrAttributeObjList.get(0);

		//Verify email is null
		assertThat(
				attributeRetrieved.getEmail(),
				nullValue());

	}
	
	

	@After
	public void tearDown()  {

		userAttributeCache.quit();
	}


	private void populateUserAttributeCache(String guid, 
			String userId,
			String email, 
			String alterEmail, 
			String alterEmailPassword,
			String secretQuestion,
			String secretAnswer,
			String fbId,
			String fbPassword,
			Date dob,
			String ssn
			)  {

		Attribute cachedUserAttribute = ((UserAttributesCache)userAttributeCache).new Attribute(guid,
																								userId,
																								email,
																								alterEmail,
																								alterEmailPassword,
																								secretQuestion,
																								secretAnswer,
																								fbId,
																								fbPassword,
																								dob,
																								ssn);
		userAttributeCache.put(guid, cachedUserAttribute);

	}

	private Attribute getAttribute(String guid, 
		String userId,
		String email, 
		String alterEmail, 
		String alterEmailPassword,
		String secretQuestion,
		String secretAnswer,
		String fbId,
		String fbPassword,
		Date dob,
		String ssn)  {

		return ((UserAttributesCache)userAttributeCache).new Attribute(guid,
																									userId,
																									email,
																									alterEmail,
																									alterEmailPassword,
																									secretQuestion,
																									secretAnswer,
																									fbId,
																									fbPassword,
																									dob,
																									ssn);
	}

	
	private Date getDate() {
		
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentDate = calendar.getTime();

		return new java.sql.Date(currentDate.getTime());
		
	}

}
