package com.comcast.test.citf.common.cima.persistence.beans;

//import   org.junit.Assert.assertEquals;

import  static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap.UserChannelId;


public class ChannelUserMapTest{

	private   ChannelUserMap objChannelUserMapTest;
	private   String sValid="VALID";
	private   String sNull=null;
	private   String sBlank="";
	private   UserChannelId userChannelId;

	@Before	
	public void setup() {
		objChannelUserMapTest=new ChannelUserMap();
		userChannelId=new UserChannelId();
	}


	@Test
	public void testPrimaryKey() {
		//valid
		objChannelUserMapTest.setPrimaryKey(userChannelId);		
		assertTrue(objChannelUserMapTest.getPrimaryKey() instanceof UserChannelId);

		objChannelUserMapTest.setPrimaryKey(userChannelId);		
		assertFalse(objChannelUserMapTest.getPrimaryKey().equals(sNull));
	}



	@Test
	public void testContentType() {
		//valid
		objChannelUserMapTest.setSubscription(sValid);	
		assertEquals(sValid,objChannelUserMapTest.getSubscription());		

		//invalid
		objChannelUserMapTest.setSubscription(sNull);		
		assertEquals(sNull,objChannelUserMapTest.getSubscription());


		//invalid
		objChannelUserMapTest.setSubscription(sBlank);		
		assertEquals(sBlank,objChannelUserMapTest.getSubscription());
	}

}
