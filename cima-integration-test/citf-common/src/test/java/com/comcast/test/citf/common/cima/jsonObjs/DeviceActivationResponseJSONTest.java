package com.comcast.test.citf.common.cima.jsonObjs;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;

public class DeviceActivationResponseJSONTest {

	private DeviceActivationResponseJSON objDeviceActivationResponseJSON;
	private final static String sValid="VALID";
	private final static String sNull=null;
	private final static String sBlank="";
	private final static long lValid=123;	

	@Before

	public void setup()
	{
		objDeviceActivationResponseJSON=new DeviceActivationResponseJSON();
	}



	@Test
	public void testDeviceCode()
	{
		//Valid 
		objDeviceActivationResponseJSON.setDevice_code(sValid);
		assertEquals(sValid,objDeviceActivationResponseJSON.getDevice_code());


		//Null
		objDeviceActivationResponseJSON.setDevice_code(sNull);
		assertEquals(sNull,objDeviceActivationResponseJSON.getDevice_code());

		//Blank
		objDeviceActivationResponseJSON.setDevice_code(sBlank);
		assertEquals(sBlank,objDeviceActivationResponseJSON.getDevice_code());


	}

	@Test
	public void testUserCode()
	{
		//Valid 
		objDeviceActivationResponseJSON.setUser_code(sValid);
		assertEquals(sValid,objDeviceActivationResponseJSON.getUser_code());


		//Null
		objDeviceActivationResponseJSON.setUser_code(sNull);
		assertEquals(sNull,objDeviceActivationResponseJSON.getUser_code());

		//Blank
		objDeviceActivationResponseJSON.setUser_code(sBlank);
		assertEquals(sBlank,objDeviceActivationResponseJSON.getUser_code());


	}

	@Test
	public void testVerificationUrl()
	{
		//Valid 
		objDeviceActivationResponseJSON.setVerification_uri(sValid);
		assertEquals(sValid,objDeviceActivationResponseJSON.getVerification_uri());


		//Null
		objDeviceActivationResponseJSON.setVerification_uri(sNull);
		assertEquals(sNull,objDeviceActivationResponseJSON.getVerification_uri());

		//Blank
		objDeviceActivationResponseJSON.setVerification_uri(sBlank);
		assertEquals(sBlank,objDeviceActivationResponseJSON.getVerification_uri());


	}

	@Test
	public void testExpiresIn()
	{
		//Valid 
		objDeviceActivationResponseJSON.setExpires_in(lValid);
		assertEquals(lValid,objDeviceActivationResponseJSON.getExpires_in());


	}

	@Test
	public void testInterval()
	{
		//Valid 
		objDeviceActivationResponseJSON.setInterval(lValid);
		assertEquals(lValid,objDeviceActivationResponseJSON.getInterval());


	}

	@Test
	public void testError()
	{
		//Valid 
		objDeviceActivationResponseJSON.setError(sValid);
		assertEquals(sValid,objDeviceActivationResponseJSON.getError());


		//Null
		objDeviceActivationResponseJSON.setError(sNull);
		assertEquals(sNull,objDeviceActivationResponseJSON.getError());

		//Blank
		objDeviceActivationResponseJSON.setError(sBlank);
		assertEquals(sBlank,objDeviceActivationResponseJSON.getError());


	}


	@Test
	public void testErrorDescription()
	{
		//Valid 
		objDeviceActivationResponseJSON.setError_description(sValid);
		assertEquals(sValid,objDeviceActivationResponseJSON.getError_description());


		//Null
		objDeviceActivationResponseJSON.setError_description(sNull);
		assertEquals(sNull,objDeviceActivationResponseJSON.getError_description());

		//Blank
		objDeviceActivationResponseJSON.setError_description(sBlank);
		assertEquals(sBlank,objDeviceActivationResponseJSON.getError_description());


	}

	@Test
	public void testAdditionalProperties()
	{
		String arg0="123";
		Object arg1=new String();
		Map<String, Object> map1=new HashMap<String, Object>();
		map1.put(arg0, arg1);
		objDeviceActivationResponseJSON.setAdditionalProperties(map1);
		assertEquals(map1,objDeviceActivationResponseJSON.getAdditionalProperties());

	}










}
