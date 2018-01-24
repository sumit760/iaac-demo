package com.comcast.test.citf.common.ldap.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import  static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.ldap.model.LDAPCustomer;

public class LDAPCustomerTest{

	private  LDAPCustomer objLDAPCustomerTest;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";

	private   List<String> custMangerGuids;
	private   List<String> custMangeeGuids;


	@Before	
	public  void setup()
	{
		objLDAPCustomerTest=new LDAPCustomer();
	}



	@Test
	public void testCstCustGuid()
	{

		//Valid 		
		objLDAPCustomerTest.setCstCustGuid(sValid);
		assertThat(objLDAPCustomerTest.getCstCustGuid(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstCustGuid(sNull);
		assertThat(objLDAPCustomerTest.getCstCustGuid(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstCustGuid(sBlank);
		assertThat(objLDAPCustomerTest.getCstCustGuid(), is(sBlank));

	}


	@Test
	public void testCstAuthGuid()
	{

		//Valid 

		objLDAPCustomerTest.setCstAuthGuid(sValid);
		assertThat(objLDAPCustomerTest.getCstAuthGuid(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstAuthGuid(sNull);
		assertThat(objLDAPCustomerTest.getCstAuthGuid(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstAuthGuid(sBlank);
		assertThat(objLDAPCustomerTest.getCstAuthGuid(), is(sBlank));

	}


	@Test
	public void testCstContactEmail()
	{

		//Valid 

		objLDAPCustomerTest.setCstContactEmail(sValid);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstContactEmail(sNull);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstContactEmail(sBlank);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), is(sBlank));
	}



	@Test
	public void testCstContactEmailStatus()
	{

		//Valid 		
		objLDAPCustomerTest.setCstContactEmail(sValid);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstContactEmail(sNull);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstContactEmail(sBlank);
		assertThat(objLDAPCustomerTest.getCstContactEmail(), is(sBlank));

	}


	@Test
	public void testCstPreferredCommunicationEmail()
	{
		//Valid 		
		objLDAPCustomerTest.setCstPreferredCommunicationEmail(sValid);
		assertThat(objLDAPCustomerTest.getCstPreferredCommunicationEmail(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstPreferredCommunicationEmail(sNull);
		assertThat(objLDAPCustomerTest.getCstPreferredCommunicationEmail(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstPreferredCommunicationEmail(sBlank);
		assertThat(objLDAPCustomerTest.getCstPreferredCommunicationEmail(), is(sBlank));
	}



	@Test
	public void testCstPwdHint()
	{

		//Valid 		
		objLDAPCustomerTest.setCstPwdHint(sValid);
		assertThat(objLDAPCustomerTest.getCstPwdHint(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstPwdHint(sNull);
		assertThat(objLDAPCustomerTest.getCstPwdHint(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstPwdHint(sBlank);
		assertThat(objLDAPCustomerTest.getCstPwdHint(), is(sBlank));

	}



	@Test
	public void testCstPwdHintAnswer()
	{

		//Valid 		
		objLDAPCustomerTest.setCstPwdHintAnswer(sValid);
		assertThat(objLDAPCustomerTest.getCstPwdHintAnswer(), is(sValid));

		//Null
		objLDAPCustomerTest.setCstPwdHintAnswer(sNull);
		assertThat(objLDAPCustomerTest.getCstPwdHintAnswer(), nullValue());

		//Blank
		objLDAPCustomerTest.setCstPwdHintAnswer(sBlank);
		assertThat(objLDAPCustomerTest.getCstPwdHintAnswer(), is(sBlank));
	}


	@Test
	public void testFirstName()
	{

		//Valid 

		objLDAPCustomerTest.setFirstName(sValid);
		assertThat(objLDAPCustomerTest.getFirstName(), is(sValid));

		//Null
		objLDAPCustomerTest.setFirstName(sNull);
		assertThat(objLDAPCustomerTest.getFirstName(), nullValue());

		//Blank
		objLDAPCustomerTest.setFirstName(sBlank);
		assertThat(objLDAPCustomerTest.getFirstName(), is(sBlank));
	}


	@Test
	public void testLastName()
	{

		//Valid 

		objLDAPCustomerTest.setLastName(sValid);
		assertThat(objLDAPCustomerTest.getLastName(), is(sValid));

		//Null
		objLDAPCustomerTest.setLastName(sNull);
		assertThat(objLDAPCustomerTest.getLastName(), nullValue());

		//Blank
		objLDAPCustomerTest.setLastName(sBlank);
		assertThat(objLDAPCustomerTest.getLastName(), is(sBlank));
	}



	@Test
	public void testRole()
	{

		//Valid 

		objLDAPCustomerTest.setRole(sValid);
		assertThat(objLDAPCustomerTest.getRole(), is(sValid));

		//Null
		objLDAPCustomerTest.setRole(sNull);
		assertThat(objLDAPCustomerTest.getRole(), nullValue());

		//Blank
		objLDAPCustomerTest.setRole(sBlank);
		assertThat(objLDAPCustomerTest.getRole(), is(sBlank));
	}


	@Test
	public void testEmail()
	{

		//Valid 

		objLDAPCustomerTest.setEmail(sValid);
		assertThat(objLDAPCustomerTest.getEmail(), is(sValid));

		//Null
		objLDAPCustomerTest.setEmail(sNull);
		assertThat(objLDAPCustomerTest.getEmail(), nullValue());

		//Blank
		objLDAPCustomerTest.setEmail(sBlank);
		assertThat(objLDAPCustomerTest.getEmail(), is(sBlank));
	}


	@Test
	public void testUid()
	{
		//Valid 		
		objLDAPCustomerTest.setUid(sValid);
		assertThat(objLDAPCustomerTest.getUid(), is(sValid));


		//Null
		objLDAPCustomerTest.setUid(sNull);
		assertThat(objLDAPCustomerTest.getUid(), nullValue());

		//Blank
		objLDAPCustomerTest.setUid(sBlank);
		assertThat(objLDAPCustomerTest.getUid(), is(sBlank));
	}


	@Test
	public void testCstManagerGuids()
	{
		//Valid 		
		objLDAPCustomerTest.setCstManagerGuids(sValid);
		custMangerGuids=objLDAPCustomerTest.getCstManagerGuids();
		assertEquals(true,custMangerGuids.contains(sValid));


		objLDAPCustomerTest.setCstManagerGuids(sBlank);
		custMangerGuids=objLDAPCustomerTest.getCstManagerGuids();
		assertEquals(true,custMangerGuids.contains(sBlank));

		objLDAPCustomerTest.setCstManagerGuids(sNull);
		custMangerGuids=objLDAPCustomerTest.getCstManagerGuids();
		assertEquals(true,custMangerGuids.contains(sNull));
	}



	@Test
	public void testCstManageeGuids()
	{
		//Valid 		
		objLDAPCustomerTest.setCstManageeGuids(sValid);
		custMangeeGuids=objLDAPCustomerTest.getCstManageeGuids();
		assertEquals(true,custMangeeGuids.contains(sValid));


		objLDAPCustomerTest.setCstManageeGuids(sBlank);
		custMangeeGuids=objLDAPCustomerTest.getCstManageeGuids();
		assertEquals(true,custMangeeGuids.contains(sBlank));

		objLDAPCustomerTest.setCstManageeGuids(sNull);
		custMangeeGuids=objLDAPCustomerTest.getCstManageeGuids();
		assertEquals(true,custMangeeGuids.contains(sNull));
	}





}
