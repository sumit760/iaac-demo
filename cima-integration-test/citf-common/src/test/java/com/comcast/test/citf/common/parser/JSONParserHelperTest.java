package com.comcast.test.citf.common.parser;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.comcast.test.citf.common.cima.jsonObjs.AccessTokenResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;
import com.comcast.test.citf.common.service.ParentalControl;

public class JSONParserHelperTest {
	
	@Test
	public void parseAccessTokenResponseJSON() {
		
		String jsonToParse = "{"
							+ "\"access_token\": \"CgNPQVQQkIToQZI5Af8QzIVw**\","
							+ "\"token_type\": \"Bearer\","
							+ "\"refresh_token\": \"5Xx9ClxYOv_d69ggeNTdEaM*\","
				            + "\"expires_in\": 3599,"
				            + "\"scope\": \"https://login.comcast.net/pdp/tve\""
				            + "}";
		
		Object obj = JSONParserHelper.parseJSON(jsonToParse, AccessTokenResponseJSON.class);
		
		assertThat(obj, notNullValue());
		
		assertThat(obj, instanceOf(AccessTokenResponseJSON.class));
		
		AccessTokenResponseJSON resp = (AccessTokenResponseJSON) obj;
		
		assertThat(resp.getAccess_token(), 
				is("CgNPQVQQkIToQZI5Af8QzIVw**"));
		
		assertThat(resp.getToken_type(), 
				is("Bearer"));
		
		assertThat(resp.getRefresh_token(), 
				is("5Xx9ClxYOv_d69ggeNTdEaM*"));
		
		assertThat(Long.toString(resp.getExpires_in()), 
				is("3599"));
		
		assertThat(resp.getScope(), 
				is("https://login.comcast.net/pdp/tve"));
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void parseParentalControlResponseJSON() {
		
		String jsonToParse = "{"
							  + "\"parentalPin\": \"1234\","
							  + "\"protectedRatings\": [\"TV-G\"],"
							  + "\"protectedChannels\": [\"NBC\",\"ABC\"],"
							  + "\"protectedNetworks\": [\"Fox\"]"
							  + "}";
		
		Object obj = JSONParserHelper.parseJSON(jsonToParse, ParentalControl.class);
		
		assertThat(obj, notNullValue());
		
		assertThat(obj, instanceOf(ParentalControl.class));
		
		ParentalControl resp = (ParentalControl) obj;
		
		assertThat(resp.getParentalPin(), 
				is("1234"));
		
		assertThat(resp.getProtectedRatings(), 
						(Matcher) hasItems("TV-G"));
		
		assertThat(resp.getProtectedChannels(), 
				(Matcher) hasItems("NBC","ABC"));
		
		assertThat(resp.getProtectedNetworks(), 
				(Matcher) hasItems("Fox"));
	}

	
	@Test
	public void parseDeviceActivationResponseJSON() {
		
		String jsonToParse = "{"
							   + "\"device_code\": \"0.ddc.PDLw9lpOsZcB\","
							   + "\"user_code\": \"citc0rbv\","
							   + "\"verification_uri\": \"https://login-qa4.comcast.net/oauth/device/activate\","
							   + "\"expires_in\": 599, "
							   + "\"interval\": 5"
							   + "}";
		
		Object obj = JSONParserHelper.parseJSON(jsonToParse, DeviceActivationResponseJSON.class);
		
		assertThat(obj, notNullValue());
		
		assertThat(obj, instanceOf(DeviceActivationResponseJSON.class));
		
		DeviceActivationResponseJSON resp = (DeviceActivationResponseJSON) obj;
		
		assertThat(resp.getDevice_code(), 
				is("0.ddc.PDLw9lpOsZcB"));
		
		assertThat(resp.getUser_code(), 
				is("citc0rbv"));
		
		assertThat(resp.getVerification_uri(), 
				is("https://login-qa4.comcast.net/oauth/device/activate"));
		
		assertThat(Long.toString(resp.getExpires_in()), 
				is("599"));
		
		assertThat(Long.toString(resp.getInterval()), 
				is("5"));
	}

	
	@Test
	public void parseIDMUserCredentialValidationResponseJSON() {
		
		String jsonToParse = "{"
								+ "\"uid\": \"mockUid\","
								+ "\"available\": \"true\","
								+ "\"alternateEmail\": \"altemail@comcast.net\","
								+ "\"mobilePhoneNumber\": \"2157652234\","
								+ "\"resetCode\": \"1234\","
								+ "\"errorCode\":\"100\","
								+ "\"errorDescription\":\"mock error\","
								+ "\"status\":\"active\","
								+ "\"message\":\"mock message\""
								+ "}";
		
		Object obj = JSONParserHelper.parseJSON(jsonToParse, IDMUserCredentialValidationResponseJSON.class);
		
		assertThat(obj, notNullValue());
		
		assertThat(obj, instanceOf(IDMUserCredentialValidationResponseJSON.class));
		
		IDMUserCredentialValidationResponseJSON resp = (IDMUserCredentialValidationResponseJSON) obj;
		
		assertThat(resp.getUid(), 
				is("mockUid"));
		
		assertThat(resp.getAvailable(), 
				is("true"));
		
		assertThat(resp.getAlternateEmail(), 
				is("altemail@comcast.net"));
		
		assertThat(resp.getMobilePhoneNumber(), 
				is("2157652234"));
		
		assertThat(resp.getResetCode(), 
				is("1234"));
		
		assertThat(resp.getErrorCode(), 
				is("100"));
		
		assertThat(resp.getErrorDescription(), 
				is("mock error"));
		
		assertThat(resp.getStatus(), 
				is("active"));
		
		assertThat(resp.getMessage(), 
				is("mock message"));
	}


	
}
