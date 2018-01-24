package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.xerces.impl.dv.util.Base64;
import org.junit.Assert;
import org.junit.Test;

public class CodecUtilityTest {


	private final String environment = "STAGE";
	private final String payLoad="String12";
	private final String key="APP_KEY_SIGN_KEY";
	private final String Url="http:\\localhost:8079\\login.jsp";
	private final String ENCODING_UTF8 = "UTF-8";
	private final String expDigitalSignature="3evYgVfHyQYt3wE23QCZFSazxR8%3D";
	private final String expEncodedURL="http%3A%5Clocalhost%3A8079%5Clogin.jsp";


	@Test
	public void testGetDigitalSignature()
	{
		try{
			String s = CodecUtility.getDigitalSignature(payLoad, key, environment);
			assertEquals(s.getClass(),String.class);
			assertEquals(s,expDigitalSignature);
		}catch(Exception e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

	@Test
	public void testEncodeURL()
	{

		String encodedURL=CodecUtility.encodeURL(Url, ENCODING_UTF8);
		assertEquals(encodedURL.getClass(),String.class);
		assertEquals(expEncodedURL,encodedURL);
	}


	@Test
	public void testDecodeURL()
	{
		String decodeURL=CodecUtility.decodeURL(expEncodedURL, ENCODING_UTF8);
		assertEquals(Url,decodeURL);
	}


	@Test
	public void testGetBase64EncodedByteArray()
	{		
		byte[] output=CodecUtility.getBase64EncodedByteArray("Hello".getBytes(StandardCharsets.UTF_8));
		
		String decodedString = new String(org.apache.commons.codec.binary.Base64.decodeBase64(output),StandardCharsets.UTF_8);

		assertThat(decodedString, 
							is("Hello"));
	}


	@Test
	public void testGetBase64DecodedString(){
		try{
			String decodedString = CodecUtility.getBase64DecodedString(Base64.encode("Hello".getBytes(Charset.forName("UTF-8"))), ENCODING_UTF8);
			assertThat(decodedString, is("Hello"));
		}catch(Exception e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

}
