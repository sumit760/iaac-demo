package com.comcast.test.citf.core.init.cima;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.AccountCache.Account;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.cache.UserCache.User;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public class CoreCacheInitializerTest {

	
	@Before
	public void setup() {
		MiscUtility.setEnvironmentVariables("core-log");
		CoreContextInitilizer.initializeContext();
	}
	
	@Test
	public void testInitialize() {
		
		Map<String, String> commonStringCacheParams = new HashMap<String, String>();
		commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT, ICommonConstants.ENVIRONMENT_QA);
		commonStringCacheParams.put(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN, "comcast");
		try{
			((CoreCacheInitializer)CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_CACHE_INITIALIZER)).initialize(commonStringCacheParams);	
		}catch(IOException e){
			Assert.fail("Exception occurred while initializing cache initializer bean " + e);
		}
		
		ICitfCache stringCache = (ICitfCache)CoreContextInitilizer.getBean(ICimaCommonConstants.CACHE_STRING);
		assertThat(
				stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT),
				is("QA"));
		assertThat(
				stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_TEST_DOMAIN),
				is("comcast"));
		
		ICitfCache configCache = (ICitfCache)CoreContextInitilizer.getBean(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
		assertThat(
				configCache.getString(ConfigurationDataProvider.ConfigurationPropKeys.CITF_RUN_MODE.getValue(), ICimaCommonConstants.ENVIRONMENT_ALL)!=null,
				is(true));
		
		ICitfCache userCache = (ICitfCache)CoreContextInitilizer.getBean(ICimaCommonConstants.CACHE_USERS);
		Map<UserCache.InputMapKeys, Object> ucMap = new HashMap<>();
		User cachedUser = ((UserCache)userCache).new User("mockId", "mockPassword", "Login", "1234",
				"TV-G", "PG-13", "Active", ImmutableSet.of("1000Primary"), ImmutableSet.of("2000Secondary"));
		ucMap.put(UserCache.InputMapKeys.USER_OBJECT, cachedUser);
		userCache.put("mockId", ucMap, ICimaCommonConstants.ENVIRONMENT_QA);
		
		assertThat(
				userCache.getFilteredObjects(ImmutableMap.of(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, ICimaCommonConstants.ENVIRONMENT_QA), 1)!=null,
				is(true));
		
		ICitfCache userAttributesCache = (ICitfCache)CoreContextInitilizer.getBean(ICimaCommonConstants.CACHE_USER_ATTRIBUTES);
		
		UserAttributesCache.Attribute attr = null;
		try{
			attr = ((UserAttributesCache)userAttributesCache).new Attribute("mockGuid", 
								"mockId", "email@comcast.net", "altEmail@gamil.com", "abcd1", "What's your favourite beverage?", 
								"coke", "fbId@gmail.com", "abcd1", MiscUtility.getSqlDate("10/10/1980", "MM/dd/yyyy"), "3456");
		}catch(ParseException e){
			Assert.fail("Exception occurred while getting user attribute " + e);
		}
		
		userAttributesCache.put("mockUserId", attr);
		
		assertThat(
				userAttributesCache.getFilteredObjects(ImmutableMap.of(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL), 1)!=null,
				is(true));
		
		ICitfCache accountCache = (ICitfCache)CoreContextInitilizer.getBean(ICimaCommonConstants.CACHE_ACCOUNT);
		Set<String> lobs = new HashSet<>();
		lobs.add("HSD");
		List<String> primaryUserIds = new ArrayList<>();
		List<String> secondaryUserIds = new ArrayList<>();
		primaryUserIds.add("4444.userId");
		secondaryUserIds.add("3333.userId");
		Account cachedService = ((AccountCache)accountCache).new Account("98001", "1234", "CSG", 
				"Greg", "Stewart", "1700 Ben Franklin Pkwy", "2152006312", "56700.auth", 
				"19103", "active", "N", "3342.auth", "3456", 
				"12/10/1978", lobs, "mockAccountType", primaryUserIds, secondaryUserIds);
		
		accountCache.put("98001", cachedService, ICimaCommonConstants.ENVIRONMENT_QA);
		
		assertThat(
				accountCache.getFilteredObjects(ImmutableMap.of(ICommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, ICimaCommonConstants.ENVIRONMENT_QA), 1)!=null,
				is(true));
	}
	
	
	@After
	public void tearDown() {
		CoreContextInitilizer.destroyContext();
	}
}