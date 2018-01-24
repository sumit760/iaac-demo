package com.comcast.test.citf.common.cima.persistence.beans;
//import static org.junit.Assert.assertEquals;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;

public class AccountsTest{

	private  Accounts objAccountsTest;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";
	private  Collection<AccountUserMap> userMap;
	private  List<String> userIds;

	@Before	
	public  void setup()
	{
		objAccountsTest=new Accounts();
		userIds = new ArrayList<String>();
		userMap = new ArrayList<AccountUserMap>();
	}


	@Test
	public void testBillingAccountId()
	{
		//valid
		objAccountsTest.setBillingAccountId(sValid);		
		assertEquals(sValid,objAccountsTest.getBillingAccountId());	

		//invalid
		objAccountsTest.setBillingAccountId(sNull);		
		assertEquals(sNull,objAccountsTest.getBillingAccountId());	

		//invalid
		objAccountsTest.setBillingAccountId(sBlank);		
		assertEquals(sBlank,objAccountsTest.getBillingAccountId());	
	}


	@Test
	public void testBillingSystem()
	{
		//valid
		objAccountsTest.setBillingSystem(sValid);		
		assertEquals(sValid,objAccountsTest.getBillingSystem());	

	}


	@Test
	public void testAuthGuid()
	{
		//valid
		objAccountsTest.setAuthGuid(sValid);		
		assertEquals(sValid,objAccountsTest.getAuthGuid());	
	}


	@Test
	public void testAccountStatus()
	{
		//valid
		objAccountsTest.setAccountStatus(sValid);		
		assertEquals(sValid,objAccountsTest.getAccountStatus());	

	}



	@Test
	public void testFirstName()
	{
		//valid
		objAccountsTest.setFirstName(sValid);		
		assertEquals(sValid,objAccountsTest.getFirstName());	

	}


	@Test
	public void testLastName()
	{
		//valid
		objAccountsTest.setLastName(sValid);		
		assertEquals(sValid,objAccountsTest.getLastName());	
	}

	@Test
	public void testPhoneNumber()
	{
		//valid
		objAccountsTest.setPhoneNumber(sValid);		
		assertEquals(sValid,objAccountsTest.getPhoneNumber());	

	}


	@Test
	public void testAddress()
	{
		//valid
		objAccountsTest.setAddress(sValid);		
		assertEquals(sValid,objAccountsTest.getAddress());	

	}


	@Test
	public void testZip()
	{
		//valid
		objAccountsTest.setZip(sValid);		
		assertEquals(sValid,objAccountsTest.getZip());	

	}


	@Test
	public void testTransferFlag()
	{
		//valid
		objAccountsTest.setTransferFlag(sValid);		
		assertEquals(sValid,objAccountsTest.getTransferFlag());	

	}


	@Test
	public void testPhysicalResourceLink()
	{
		//valid
		objAccountsTest.setPhysicalResourceLink(sValid);		
		assertEquals(sValid,objAccountsTest.getPhysicalResourceLink());

	}


	@Test
	public void testEnvironment()
	{
		//valid
		objAccountsTest.setEnvironment(sValid);		
		assertEquals(sValid,objAccountsTest.getEnvironment());	

	}


	@Test
	public void testPrimaryUserIds()
	{

		userIds.add(sValid);
		userIds.add(sNull);
		userIds.add(sBlank);

		//valid
		objAccountsTest.addPrimaryUserId(sValid);
		assertTrue(objAccountsTest.getPrimaryUserIds().contains(sValid));

	}


	@Test
	public void testSecondryUserIds()
	{
		userIds.clear();
		userIds.add(sValid);
		userIds.add(sNull);
		userIds.add(sBlank);

		//valid
		objAccountsTest.addSecondaryUserId(sValid);
		assertTrue(objAccountsTest.getSecondaryUserIds().contains(sValid));	

	}


	@Test
	public void testSSN()
	{

		//valid
		objAccountsTest.setSsn(sValid);		
		assertEquals(sValid,objAccountsTest.getSsn());	

	}


	@Test
	public void testDOB()
	{

		//valid
		objAccountsTest.setDob(sValid);		
		assertEquals(sValid,objAccountsTest.getDob());	

	}


	@Test
	public void testUserMap()
	{
		//valid
		AccountUserMap userMap1=new AccountUserMap();
		AccountUserMap userMap2=new AccountUserMap();
		AccountUserMap userMap3=new AccountUserMap();
		userMap.add(userMap1);
		userMap.add(userMap2);
		userMap.add(userMap3);

		objAccountsTest.setUserMap(userMap)	;
		assertTrue(objAccountsTest.getUserMap().contains(userMap1));
		assertTrue(objAccountsTest.getUserMap().contains(userMap2));
		assertTrue(objAccountsTest.getUserMap().contains(userMap3));
	}
}
