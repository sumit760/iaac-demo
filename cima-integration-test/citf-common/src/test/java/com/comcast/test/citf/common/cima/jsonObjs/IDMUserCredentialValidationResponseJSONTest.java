package com.comcast.test.citf.common.cima.jsonObjs;

import  static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;

public class IDMUserCredentialValidationResponseJSONTest{

	private  IDMUserCredentialValidationResponseJSON objIDMUserCredentialValidationResponseJSON;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";
	private  long lValid=123;	

	@Before
	public  void setup()
	{
		objIDMUserCredentialValidationResponseJSON=new IDMUserCredentialValidationResponseJSON();
	}



	@Test
	public void testUid()
	{
		//Valid 		
		objIDMUserCredentialValidationResponseJSON.setUid(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getUid());


		//Null
		objIDMUserCredentialValidationResponseJSON.setUid(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getUid());


		//Blank

		objIDMUserCredentialValidationResponseJSON.setUid(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getUid());


	}

	@Test
	public void testAvailable()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setAvailable(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getAvailable());


		//Null

		objIDMUserCredentialValidationResponseJSON.setAvailable(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getAvailable());


		//Blank

		objIDMUserCredentialValidationResponseJSON.setAvailable(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getAvailable());


	}

	@Test
	public void testAlternateEmail()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setAlternateEmail(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getAlternateEmail());


		//Null

		objIDMUserCredentialValidationResponseJSON.setAlternateEmail(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getAlternateEmail());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setAlternateEmail(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getAlternateEmail());


	}


	@Test
	public void testMobileNumber()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setMobilePhoneNumber(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getMobilePhoneNumber());


		//Null

		objIDMUserCredentialValidationResponseJSON.setMobilePhoneNumber(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getMobilePhoneNumber());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setMobilePhoneNumber(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getMobilePhoneNumber());


	}


	@Test
	public void testResetCode()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setResetCode(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getResetCode());


		//Null

		objIDMUserCredentialValidationResponseJSON.setResetCode(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getResetCode());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setResetCode(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getResetCode());


	}


	@Test
	public void testErrorCode()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setErrorCode(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getErrorCode());


		//Null

		objIDMUserCredentialValidationResponseJSON.setErrorCode(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getErrorCode());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setErrorCode(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getErrorCode());


	}


	@Test
	public void testErrorDescription()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setErrorDescription(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getErrorDescription());


		//Null

		objIDMUserCredentialValidationResponseJSON.setErrorDescription(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getErrorDescription());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setErrorDescription(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getErrorDescription());


	}


	@Test
	public void testStaus()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setStatus(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getStatus());


		//Null

		objIDMUserCredentialValidationResponseJSON.setStatus(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getStatus());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setStatus(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getStatus());


	}



	@Test
	public void testMessage()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setMessage(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getMessage());


		//Null

		objIDMUserCredentialValidationResponseJSON.setMessage(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getMessage());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setMessage(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getMessage());


	}


	@Test
	public void testBillingAccount()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setBillingAccountId(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getBillingAccountId());


		//Null

		objIDMUserCredentialValidationResponseJSON.setBillingAccountId(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getBillingAccountId());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setBillingAccountId(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getBillingAccountId());


	}


	@Test
	public void testCustomerID()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setCustomerGuid(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getCustomerGuid());


		//Null

		objIDMUserCredentialValidationResponseJSON.setCustomerGuid(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getCustomerGuid());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setCustomerGuid(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getCustomerGuid());


	}



	@Test
	public void testUserName()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setUserName(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getUserName());


		//Null

		objIDMUserCredentialValidationResponseJSON.setUserName(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getUserName());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setUserName(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getUserName());


	}



	@Test
	public void testFirstName()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setFirstName(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getFirstName());


		//Null

		objIDMUserCredentialValidationResponseJSON.setFirstName(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getFirstName());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setFirstName(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getFirstName());


	}


	@Test
	public void testLastName()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setLastName(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getLastName());


		//Null

		objIDMUserCredentialValidationResponseJSON.setLastName(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getLastName());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setLastName(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getLastName());


	}



	@Test
	public void testContactEmail()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setContactEmail(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getContactEmail());


		//Null

		objIDMUserCredentialValidationResponseJSON.setContactEmail(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getContactEmail());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setContactEmail(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getContactEmail());


	}


	@Test
	public void testIssuedAt()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setIssuedAt(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getIssuedAt());


		//Null

		objIDMUserCredentialValidationResponseJSON.setIssuedAt(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getIssuedAt());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setIssuedAt(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getIssuedAt());


	}


	@Test
	public void testValidationUrlTTLHours()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setValidationUrlTTLHours(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getValidationUrlTTLHours());


		//Null

		objIDMUserCredentialValidationResponseJSON.setValidationUrlTTLHours(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getValidationUrlTTLHours());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setValidationUrlTTLHours(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getValidationUrlTTLHours());


	}


	@Test
	public void testPrefferedEmail()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setPrefferedEmail(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getPrefferedEmail());


		//Null

		objIDMUserCredentialValidationResponseJSON.setPrefferedEmail(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getPrefferedEmail());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setPrefferedEmail(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getPrefferedEmail());


	}


	@Test
	public void testVideoOnlyCustomer()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setVideoOnlyCustomer(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getVideoOnlyCustomer());


		//Null

		objIDMUserCredentialValidationResponseJSON.setVideoOnlyCustomer(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getVideoOnlyCustomer());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setVideoOnlyCustomer(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getVideoOnlyCustomer());


	}



	@Test
	public void testCustID()
	{
		//Valid 

		objIDMUserCredentialValidationResponseJSON.setCustGuid(sValid);
		assertEquals(sValid,objIDMUserCredentialValidationResponseJSON.getCustGuid());


		//Null

		objIDMUserCredentialValidationResponseJSON.setCustGuid(sNull);
		assertEquals(sNull,objIDMUserCredentialValidationResponseJSON.getCustGuid());


		//Blank
		objIDMUserCredentialValidationResponseJSON.setCustGuid(sBlank);
		assertEquals(sBlank,objIDMUserCredentialValidationResponseJSON.getCustGuid());


	}





}
