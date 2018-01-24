package com.comcast.test.citf.core.dataProvider;

import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.persistence.FreshUserDAO;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.ConfigCache;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.cache.StringCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is an abstract class to provide required resource to all data providers.
*
*/
public abstract class AbstractDataProvider {
	
	public ICitfCache getConfigCache() {
		return configCache;
	}
	
	public ICitfCache getUserCache() {
		return userCache;
	}
	
	public ICitfCache getUserAttributesCache() {
		return userAttributesCache;
	}
	
	public ICitfCache getAccountCache() {
		return accountCache;
	}
	
	public FreshUserDAO getFreshUserDAO(){
		if(freshUserDao == null){
			freshUserDao = ObjectInitializer.getFreshUserDAO();
		}
		return freshUserDao;
	}
	
	public String getCurrentEnvironment() {
		String environment = stringCache.getString(ICimaCommonConstants.STRING_CACHE_KEY_TEST_ENVIRONMENT);
		return environment!=null ? environment : ICimaCommonConstants.ENVIRONMENT_QA;
	}
	
	protected String getConfigString(String cacheKey) {
		return getConfigCache()!=null ? getConfigCache().getString(cacheKey, getCurrentEnvironment()) : null;
	}
	
	@Autowired
	private ConfigCache configCache;
	
	@Autowired
	private UserCache userCache;
	
	@Autowired
    private UserAttributesCache userAttributesCache;
	
	@Autowired
    private AccountCache accountCache;
	
	@Autowired
	private StringCache stringCache;
	
    private FreshUserDAO freshUserDao;
}
