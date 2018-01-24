package com.comcast.test.citf.common.cima.persistence.beans;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class ChannelTest{

	private  Channels objChannelsTest;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";
	private  double dValid=2.45;
	private  double dZero=0.00;


	@Before
	public void setup() {
		objChannelsTest=new Channels();
	}


	@Test
	public void testChannelId()
	{
		//valid
		objChannelsTest.setChannelId(sValid);		
		assertEquals(sValid,objChannelsTest.getChannelId());		

		//invalid
		objChannelsTest.setChannelId(sNull);		
		assertEquals(sNull,objChannelsTest.getChannelId());

		//invalid
		objChannelsTest.setChannelId(sBlank);		
		assertEquals(sBlank,objChannelsTest.getChannelId());
	}

	@Test
	public void testName()
	{
		//valid
		objChannelsTest.setName(sValid);		
		assertEquals(sValid,objChannelsTest.getName());		

		//invalid
		objChannelsTest.setName(sNull);		
		assertEquals(sNull,objChannelsTest.getName());

		//invalid
		objChannelsTest.setName(sBlank);		
		assertEquals(sBlank,objChannelsTest.getName());
	}


	@Test
	public void testDescription()
	{
		//valid
		objChannelsTest.setDescription(sValid);		
		assertEquals(sValid,objChannelsTest.getDescription());		

		//invalid
		objChannelsTest.setDescription(sNull);		
		assertEquals(sNull,objChannelsTest.getDescription());

		//invalid
		objChannelsTest.setDescription(sBlank);		
		assertEquals(sBlank,objChannelsTest.getDescription());
	}



	@Test
	public void testStatus()
	{
		//valid
		objChannelsTest.setStatus(sValid);		
		assertEquals(sValid,objChannelsTest.getStatus());		

		//invalid
		objChannelsTest.setStatus(sNull);		
		assertEquals(sNull,objChannelsTest.getStatus());

		//invalid
		objChannelsTest.setStatus(sBlank);		
		assertEquals(sBlank,objChannelsTest.getStatus());
	}


	@Test
	public void testStationID()
	{
		//valid
		objChannelsTest.setStationId(sValid);		
		assertEquals(sValid,objChannelsTest.getStationId());		

		//invalid
		objChannelsTest.setStationId(sNull);		
		assertEquals(sNull,objChannelsTest.getStationId());

		//invalid
		objChannelsTest.setStationId(sBlank);		
		assertEquals(sBlank,objChannelsTest.getStationId());
	}



	@Test
	public void testContentType()
	{
		//valid
		objChannelsTest.setContentType(sValid);		
		assertEquals(sValid,objChannelsTest.getContentType());		

		//invalid
		objChannelsTest.setContentType(sNull);		
		assertEquals(sNull,objChannelsTest.getContentType());

		//invalid
		objChannelsTest.setContentType(sBlank);		
		assertEquals(sBlank,objChannelsTest.getContentType());
	}


	@Test
	public void testStartgey()
	{
		//valid
		objChannelsTest.setStrategy(sValid);		
		assertEquals(sValid,objChannelsTest.getStrategy());		

		//invalid
		objChannelsTest.setStrategy(sNull);		
		assertEquals(sNull,objChannelsTest.getStrategy());

		//invalid
		objChannelsTest.setStrategy(sBlank);		
		assertEquals(sBlank,objChannelsTest.getStrategy());
	}

}
