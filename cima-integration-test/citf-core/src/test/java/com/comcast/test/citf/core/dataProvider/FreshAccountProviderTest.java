package com.comcast.test.citf.core.dataProvider;

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
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.dataProvider.FreshAccountProvider.Filter;
import com.comcast.test.citf.core.init.CoreContextInitilizer;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.util.ObjectDestroyer;

public class FreshAccountProviderTest {

	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void testFreshAccountWithoutFilter(){
		Object dpObj = null;
	
		try{
			AddAccountInAccountCache("1", null, "DST", "fName", "lName", null, null, "aguid123", null, ICimaCommonConstants.DB_STATUS_ACTIVE, 
					null, null, null, null, null, null, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_FRESH_ACCOUNT);
		}catch(Exception e){
			Assert.fail("Not able to fetch FreshAccountProvider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof FreshAccountProvider);
		
		FreshAccountProvider fadp = (FreshAccountProvider)dpObj;
		AccountCache.Account account = fadp.getDedicatedAccount(null);
		
		Assert.assertTrue(account instanceof AccountCache.Account);
	}
	
	
	@Test
	public void testFreshAccountWithFilter(){
		Object dpObj = null;
	
		try{
			AddAccountInAccountCache("2", null, "DST", "fName", "lName", null, null, "aguid123", null, ICimaCommonConstants.DB_STATUS_ACTIVE, 
					null, null, null, null, null, null, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_FRESH_ACCOUNT);
		}catch(Exception e){
			Assert.fail("Not able to fetch FreshAccountProvider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof FreshAccountProvider);
		
		FreshAccountProvider fadp = (FreshAccountProvider)dpObj;
		Map<FreshAccountProvider.Filter, String> filters =  new HashMap<Filter, String>();
		filters.put(FreshAccountProvider.Filter.ACCOUNT_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE);
		
		AccountCache.Account account = fadp.getDedicatedAccount(filters);
		Assert.assertTrue(account instanceof AccountCache.Account);
	}
	
	
	@Test
	public void testFreshAccountLock(){
		Object dpObj = null;
		String accountId = "3";
	
		try{
			AddAccountInAccountCache(accountId, null, "DST", "fName", "lName", null, null, "aguid123", null, ICimaCommonConstants.DB_STATUS_ACTIVE, 
					null, null, null, null, null, null, null, null, ICimaCommonConstants.ENVIRONMENT_QA);
			
			dpObj = CoreContextInitilizer.getBean(ICimaCommonConstants.SPRING_BEAN_DATA_PROVIDER_FRESH_ACCOUNT);
		}catch(Exception e){
			Assert.fail("Not able to fetch FreshAccountProvider object due to "+e.getMessage());
		}
		
		Assert.assertTrue(dpObj instanceof FreshAccountProvider);
		
		FreshAccountProvider fadp = (FreshAccountProvider)dpObj;
		AccountCache.Account account = fadp.getDedicatedAccount(null);
		Assert.assertTrue(account instanceof AccountCache.Account);
		
		try{
			Assert.assertTrue(fadp.getAccountCache().isLocked(accountId));
			fadp.unlockAccount(accountId);
			Assert.assertFalse(fadp.getAccountCache().isLocked(accountId));
		}catch(LockException le){
			Assert.fail("Not able to check lock "+le.getMessage());
		}
	}
	
	
	private void AddAccountInAccountCache(String accountId, String serviceAccountId, String billingSystem,
			String firstName, String lastName, String address, String phoneNumber, String authGuid, String zip,
			String status, String transferFlag, String physicalResourceLink, String freshSsn, String freshDob,
			Set<String> lob, String accountType, List<String> primaryUserIds, List<String> secondaryUserIds,
			String environment) {
		try {
			ICitfCache accountCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_ACCOUNT);

			AccountCache.Account acc = ((AccountCache) accountCache).new Account(accountId, serviceAccountId,
					billingSystem, firstName, lastName, address, phoneNumber, authGuid, zip, status, transferFlag,
					physicalResourceLink, freshSsn, freshDob, lob, accountType, primaryUserIds, secondaryUserIds);

			accountCache.put(accountId, acc, environment);
		} catch (Exception e) {
			Assert.fail("Not able to populate AccountCache due to " + e.getMessage());
		}
	}
	
	
	@After
	public void tearDown() {
		try{
			ObjectDestroyer.destroyAllCaches();
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}
}
