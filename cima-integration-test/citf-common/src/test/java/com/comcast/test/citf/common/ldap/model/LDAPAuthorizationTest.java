package com.comcast.test.citf.common.ldap.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;

public class LDAPAuthorizationTest{

	private  LDAPAuthorization objLDAPAuthorization;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";

	@Before	
	public  void setup()
	{

		objLDAPAuthorization=new LDAPAuthorization();
	}



	@Test
	public void testCustAuthGuid()
	{

		//Valid 
		objLDAPAuthorization.setCstAuthGuid(sValid);
		assertThat(objLDAPAuthorization.getCstAuthGuid(), is(sValid));

		//Null
		objLDAPAuthorization.setCstAuthGuid(sNull);
		assertThat(objLDAPAuthorization.getCstAuthGuid(), nullValue());

		//Blank
		objLDAPAuthorization.setCstAuthGuid(sBlank);
		assertThat(objLDAPAuthorization.getCstAuthGuid(), is(sBlank));

	}


	@Test
	public void testCstAccountManagerGuid()
	{

		//Valid 
		objLDAPAuthorization.setCstAccountManagerGuid(sValid);
		assertThat(objLDAPAuthorization.getCstAccountManagerGuid(), is(sValid));

		//Null
		objLDAPAuthorization.setCstAccountManagerGuid(sNull);
		assertThat(objLDAPAuthorization.getCstAccountManagerGuid(), nullValue());

		//Blank
		objLDAPAuthorization.setCstAccountManagerGuid(sBlank);
		assertThat(objLDAPAuthorization.getCstAccountManagerGuid(), is(sBlank));

	}



	@Test
	public void testCstAccountPrimaryGuid()
	{

		//Valid 
		objLDAPAuthorization.setCstAccountPrimaryGuid(sValid);
		assertThat(objLDAPAuthorization.getCstAccountPrimaryGuid(), is(sValid));

		//Null
		objLDAPAuthorization.setCstAccountPrimaryGuid(sNull);
		assertThat(objLDAPAuthorization.getCstAccountPrimaryGuid(), nullValue());

		//Blank
		objLDAPAuthorization.setCstAccountPrimaryGuid(sBlank);
		assertThat(objLDAPAuthorization.getCstAccountPrimaryGuid(), is(sBlank));

	}


	@Test
	public void testsetAccountStatus()
	{

		//Valid 
		objLDAPAuthorization.setAccountStatus(sValid);
		assertThat(objLDAPAuthorization.getAccountStatus(), is(sValid));

		//Null
		objLDAPAuthorization.setAccountStatus(sNull);
		assertThat(objLDAPAuthorization.getAccountStatus(), nullValue());

		//Blank
		objLDAPAuthorization.setAccountStatus(sBlank);
		assertThat(objLDAPAuthorization.getAccountStatus(), is(sBlank));

	}



	@Test
	public void testBillingId()
	{

		//Valid 
		objLDAPAuthorization.setBillingId(sValid);
		assertThat(objLDAPAuthorization.getBillingId(), is(sValid));

		//Null
		objLDAPAuthorization.setBillingId(sNull);
		assertThat(objLDAPAuthorization.getBillingId(), nullValue());

		//Blank
		objLDAPAuthorization.setBillingId(sBlank);
		assertThat(objLDAPAuthorization.getBillingId(), is(sBlank));

	}



	@Test
	public void testFirstName()
	{

		//Valid 
		objLDAPAuthorization.setFirstName(sValid);
		assertThat(objLDAPAuthorization.getFirstName(), is(sValid));

		//Null
		objLDAPAuthorization.setFirstName(sNull);
		assertThat(objLDAPAuthorization.getFirstName(), nullValue());

		//Blank
		objLDAPAuthorization.setFirstName(sBlank);
		assertThat(objLDAPAuthorization.getFirstName(), is(sBlank));

	}


	@Test
	public void testLastName()
	{

		//Valid 
		objLDAPAuthorization.setLastName(sValid);
		assertThat(objLDAPAuthorization.getLastName(), is(sValid));

		//Null
		objLDAPAuthorization.setLastName(sNull);
		assertThat(objLDAPAuthorization.getLastName(), nullValue());

		//Blank
		objLDAPAuthorization.setLastName(sBlank);
		assertThat(objLDAPAuthorization.getLastName(), is(sBlank));

	}



	@Test
	public void testDigitalVoiceStatus()
	{

		//Valid 
		objLDAPAuthorization.setDigitalVoiceStatus(sValid);
		assertThat(objLDAPAuthorization.getDigitalVoiceStatus(), is(sValid));

		//Null
		objLDAPAuthorization.setDigitalVoiceStatus(sNull);
		assertThat(objLDAPAuthorization.getDigitalVoiceStatus(), nullValue());

		//Blank
		objLDAPAuthorization.setDigitalVoiceStatus(sBlank);
		assertThat(objLDAPAuthorization.getDigitalVoiceStatus(), is(sBlank));

	}


	@Test
	public void testHsdStatus()
	{

		//Valid 
		objLDAPAuthorization.setHsdStatus(sValid);
		assertThat(objLDAPAuthorization.getHsdStatus(), is(sValid));

		//Null
		objLDAPAuthorization.setHsdStatus(sNull);
		assertThat(objLDAPAuthorization.getHsdStatus(), nullValue());

		//Blank
		objLDAPAuthorization.setHsdStatus(sBlank);
		assertThat(objLDAPAuthorization.getHsdStatus(), is(sBlank));

	}


	@Test
	public void testVidStatus()
	{

		//Valid 
		objLDAPAuthorization.setVidStatus(sValid);
		assertThat(objLDAPAuthorization.getVidStatus(), is(sValid));

		objLDAPAuthorization.setVidStatus(sNull);
		assertThat(objLDAPAuthorization.getVidStatus(), nullValue());

		//Blank
		objLDAPAuthorization.setVidStatus(sBlank);
		assertThat(objLDAPAuthorization.getVidStatus(), is(sBlank));

	}





}
