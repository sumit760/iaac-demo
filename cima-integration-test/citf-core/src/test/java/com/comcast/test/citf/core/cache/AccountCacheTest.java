package com.comcast.test.citf.core.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.exception.LockException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache.Account;
import com.google.common.collect.ImmutableSet;


public class AccountCacheTest {

	private ICitfCache accountCache;
	private List<String> primaryUserIds;
	private List<String> secondaryUserIds;


	@Before
	public void setup() 
	{
		accountCache=new AccountCache();
		
		primaryUserIds=new ArrayList<>();
		primaryUserIds.add("puid1");
		primaryUserIds.add("puid2");
		secondaryUserIds=new ArrayList<>();
		secondaryUserIds.add("suid1");
		secondaryUserIds.add("suid2");
	}


	@Test
	public void testPutWithInvalidValues() {
		
		//with blank key
		Account cachedAccount = getAccount("1234",
				 "123",
				 "POS",
				 "Harry",
				 "Potter",
				 "4 Privet Drive",
				 "0744878576",
				 "authId",
				 "Ne81YH",
				 "valid",
				 "N",
				 "physicalResourceLink",
				 "2078",
				 "21/12/1997",
				 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
				 "RESIDENTIAL",
				 primaryUserIds,
				 secondaryUserIds);
		
		Assert.assertFalse(accountCache.put("", cachedAccount, "QA"));
		Assert.assertFalse(accountCache.put("1234", null, "QA"));
		Assert.assertFalse(accountCache.put("1234", "InvalidType", "QA"));
		Assert.assertFalse(accountCache.put("1234", cachedAccount, null));		
	}
	
	
	@Test
	public void testGetFilteredObjectsInvalidValues() {
		//null filter
		Assert.assertNull(accountCache.getFilteredObjects(null, 1));
		
		Map<String, String> accountFilters = new HashMap<>();
		
		//Empty filter
		Assert.assertNull(accountCache.getFilteredObjects(accountFilters, 1));
		
		//null environment in filter condition
		Assert.assertNull(accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, null));
	}
	
	
	@Test
	public void testGetFilteredObjectsByPhone() {


		populateAccountCache("1234",
							 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "Ne81YH",
							 "valid",
							 "N",
							 "physicalResourceLink",
							 "2078",
							 "21/12/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_PHONE, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getPhoneNumber(),
				is("0744878576"));

	}

	
	@Test
	public void testGetFilteredObjectsByAccountStatus() {


		populateAccountCache("1235",
							 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "19103",
							 "Active",
							 "N",
							 "physicalResourceLink",
							 "2078",
							 "12/21/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS, "Active");

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getStatus(),
				is("Active"));

	}


	
	@Test
	public void testGetFilteredObjectsByUserRolePrimary() {


		populateAccountCache("puid1",
				 			 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "19103",
							 "Active",
							 "Y",
							 "physicalResourceLink",
							 "2078",
							 "12/21/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE, ICimaCommonConstants.UserRoles.PRIMARY.toString());
		accountFilters.put(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV,"puid1,puid2");

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getAccountId(),
				is("puid1"));


	}


	
	@Test
	public void testGetFilteredObjectsByUserRoleSecondary() {


		populateAccountCache("suid1",
				 			 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "19103",
							 "Active",
							 "Y",
							 "physicalResourceLink",
							 "2078",
							 "12/21/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE, ICimaCommonConstants.UserRoles.SECONDARY.getValue());
		accountFilters.put(ICommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV,"suid1,suid2");

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getAccountId(),
				is("suid1"));


	}

	@Test
	public void testGetFilteredObjectsByTransferFlag() {


		populateAccountCache("1236",
							 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "19103",
							 "Active",
							 "Y",
							 "physicalResourceLink",
							 "2078",
							 "12/21/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG, "Y");

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getTransferFlag(),
				is("Y"));


	}

	
	@Test
	public void testGetFilteredObjectsByAddress() {
		
		populateAccountCache("1237",
				 "123",
				 "POS",
				 "Harry",
				 "Potter",
				 "4 Privet Drive",
				 "0744878576",
				 "authId",
				 "19103",
				 "Active",
				 "Y",
				 "physicalResourceLink",
				 "2078",
				 "12/21/1997",
				 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
				 "RESIDENTIAL",
				 primaryUserIds,
				 secondaryUserIds,
				 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ADDRESS, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getAddress(),
				is("4 Privet Drive"));

	}

	
	
	@Test
	public void testGetFilteredObjectsBySSN() {
		
		populateAccountCache("1238",
				 "123",
				 "POS",
				 "Harry",
				 "Potter",
				 "4 Privet Drive",
				 "0744878576",
				 "authId",
				 "19103",
				 "Active",
				 "Y",
				 "physicalResourceLink",
				 "2078",
				 "12/21/1997",
				 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
				 "RESIDENTIAL",
				 primaryUserIds,
				 secondaryUserIds,
				 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getFreshSsn(),
				is("2078"));

	}

	
	@Test
	public void testGetFilteredObjectsWithWrongFilter() {
		
		populateAccountCache("1241",
				 "123",
				 "POS",
				 "Harry",
				 "Potter",
				 "4 Privet Drive",
				 "0744878576",
				 "authId",
				 "19103",
				 "Active",
				 "Y",
				 "physicalResourceLink",
				 null,
				 "12/21/1997",
				 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
				 "RESIDENTIAL",
				 primaryUserIds,
				 secondaryUserIds,
				 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		
		//Use wrong filter of SSN not null
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(false));

		//Now use the null filter
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN, ICimaCommonConstants.CACHE_FLTR_VALUE_NULL);

		accountObjs = accountCache.getFilteredObjects(accountFilters, 1);
		
		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getFreshSsn(),
				nullValue());

	}

	@Test
	public void testGetFilteredObjectsByLob() {


		populateAccountCache("1234",
				 			 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "Ne81YH",
							 "valid",
							 "N",
							 "physicalResourceLink",
							 "2078",
							 "21/12/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV, ICimaCommonConstants.LOB.CDV.name());

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				account.getLobs()!=null && account.getLobs().contains(ICimaCommonConstants.LOB.CDV.name()),
				is(true));
	}
	
	@Test
	public void testLockAccount(){
		populateAccountCache("1239",
				 			 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "Ne81YH",
							 "valid",
							 "Y",
							 "physicalResourceLink",
							 "2078",
							 "02/12/1980",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "PROD");

		try{
			accountCache.changeLock("1239", true);
		}catch(LockException e){
			Assert.fail("Exception occurred while locking the account " + e);
		}

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "PROD");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG, "Y");
		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);
		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);
		assertThat("Expected account to be locked", 
				account.isLocked(), is(true));

	}

	
	
	@Test
	public void testUnLockAccount()
	{
		populateAccountCache("1240",
				 			 "123",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "Ne81YH",
							 "valid",
							 "Y",
							 "physicalResourceLink",
							 "2078",
							 "02/12/1980",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name(), ICimaCommonConstants.LOB.HSD.name(), ICimaCommonConstants.LOB.VID.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "PROD");

		//Lock the account first
		try{
			accountCache.changeLock("1240", true);
		}catch(LockException e){
			Assert.fail("Exception occurred while locking the account " + e);
		}

		Map<String, String> accountFilters = new HashMap<>();
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "PROD");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_TRANSFER_FLAG, "Y");
		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);
		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);
		assertThat("Expected account to be locked", 
				account.isLocked(), is(true));
		
		//Unlock now
		try{
			accountCache.changeLock("1240", false);
		}catch(LockException e){
			Assert.fail("Exception occurred while unlocking the account " + e);
		}
		accountObjs = accountCache.getFilteredObjects(accountFilters, 1);
		account = (AccountCache.Account) accountObjs.get(0);
		
		assertThat("Expected account to be unlocked", 
				account.isLocked(), is(false));
	}
	
	@Test
	public void testGetFilteredObjectsByServiceAccountId() {
		populateAccountCache("1234",
							 "11111111",
							 "POS",
							 "Harry",
							 "Potter",
							 "4 Privet Drive",
							 "0744878576",
							 "authId",
							 "Ne81YH",
							 "valid",
							 "N",
							 "physicalResourceLink",
							 "2078",
							 "21/12/1997",
							 ImmutableSet.of(ICimaCommonConstants.LOB.CDV.name()),
							 "RESIDENTIAL",
							 primaryUserIds,
							 secondaryUserIds,
							 "QA");

		Map<String, String> accountFilters = new HashMap<>();

		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_ENVIRONMENT, "QA");
		accountFilters.put(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID, ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL);

		List<Object> accountObjs = accountCache.getFilteredObjects(accountFilters, 1);

		assertThat(
				accountObjs,
				notNullValue());

		assertThat(
				accountObjs.size() > 0,
				is(true));

		AccountCache.Account account = (AccountCache.Account) accountObjs.get(0);

		assertThat(
				(account.getServiceAccountId()!=null),
				is(true));

	}


	@After
	public void tearDown() {

		accountCache.quit();
	}

	private void populateAccountCache(String accountId,
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

		Account cachedAccount = ((AccountCache)accountCache).new Account(accountId, serviceAccountId, billingSystem, firstName, lastName, address, phoneNumber, authGuid, 
				zip, status, transferFlag, physicalResourceLink, freshSsn, freshDob, lob, accountType, primaryUserIds, secondaryUserIds);

		accountCache.put(accountId, cachedAccount, environment);

	}
	
	private Account getAccount(String accountId,
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
			List<String> secondaryUserIds) {
		
		return ((AccountCache)accountCache).new Account(accountId, serviceAccountId, billingSystem, firstName, lastName, address, phoneNumber, authGuid, zip, status, 
				transferFlag, physicalResourceLink, freshSsn, freshDob, lob, accountType, primaryUserIds, secondaryUserIds);
	}

}
