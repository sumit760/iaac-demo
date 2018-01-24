package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap.AccountUserId;
import com.comcast.test.citf.common.cima.persistence.beans.Users;

public class AccountsUserMapTest{

	private  AccountUserMap objAccountUserMapTest;
	private   String sValid="VALID";
	private   String sNull=null;
	private   String sBlank="";
	private   Collection<AccountUserMap> userMap;
	private   List<String> userIds;
	private   AccountUserId uaMapPrimaryKey;
	private   Users users;
	private   Accounts accounts;
	private   AccountUserId objaccountUserId;

	@Before	
	public  void setup()
	{
		objAccountUserMapTest=new AccountUserMap();
		userIds= new ArrayList<String>();
		uaMapPrimaryKey=new AccountUserId();
		users=new Users();
		accounts=new Accounts();
		objaccountUserId=new AccountUserId();
	}


	@Test
	public void testRole()
	{
		//valid
		objAccountUserMapTest.setRole((sValid));		
		assertEquals(sValid,objAccountUserMapTest.getRole());	

		//invalid
		objAccountUserMapTest.setRole((sNull));		
		assertEquals(sNull,objAccountUserMapTest.getRole());		

		//invalid
		objAccountUserMapTest.setRole((sBlank));		
		assertEquals(sBlank,objAccountUserMapTest.getRole());		
	}



	@Test
	public void testuaMapPrimaryKey()
	{
		//valid
		objAccountUserMapTest.setUaMapPrimaryKey((uaMapPrimaryKey));		
		assertTrue(objAccountUserMapTest.getUaMapPrimaryKey() instanceof AccountUserId);

		objAccountUserMapTest.setUaMapPrimaryKey((uaMapPrimaryKey));		
		assertFalse(objAccountUserMapTest.getUaMapPrimaryKey().equals(sNull));
	}


	@Test
	public void testUsers()
	{
		//valid
		objaccountUserId.setUser(users);		
		assertTrue(objaccountUserId.getUser() instanceof Users);

		objaccountUserId.setUser(users);		
		assertFalse(objaccountUserId.getUser().equals(sNull));
	}


	@Test
	public void testAccounts()
	{
		//valid
		objaccountUserId.setAccount(accounts);		
		assertTrue(objaccountUserId.getAccount()instanceof Accounts);

		objaccountUserId.setAccount(accounts);		
		assertFalse(objaccountUserId.getAccount().equals(sNull));
	}

}
