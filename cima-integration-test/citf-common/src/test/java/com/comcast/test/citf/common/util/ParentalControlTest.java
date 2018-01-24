package com.comcast.test.citf.common.util;

import  static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.comcast.test.citf.common.cima.jsonObjs.AccessTokenResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;
import com.comcast.test.citf.common.exception.CITFException;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.service.ParentalControl;

public class ParentalControlTest{

	private ParentalControl objParentalControl;
	private String sValid = "VALID";
	private String sNull = null;
	private String sBlank = "";
	private List<String> dummyList ;



	@Before	
	public void setup()
	{
		objParentalControl = new ParentalControl();
		dummyList = new ArrayList();
		dummyList.add(sValid);
		dummyList.add(sNull);
		dummyList.add(sBlank);
	}



	@Test
	public void testProtectRating()
	{
		objParentalControl.setProtectedRatings(dummyList);
		assertEquals(dummyList,objParentalControl.getProtectedRatings());
	}	


	@Test
	public void testProtectedChannels()
	{

		objParentalControl.setProtectedChannels(dummyList);
		assertEquals(dummyList,objParentalControl.getProtectedChannels());

	}	


	@Test
	public void testProtectedNetworks()
	{

		//Valid 
		objParentalControl.setProtectedNetworks(dummyList);
		assertEquals(dummyList,objParentalControl.getProtectedNetworks());

	}	

}
