package com.comcast.test.citf.common.cima.persistence.beans;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.cima.persistence.beans.BrowserCapabilities;

public class BrowserCapablitiesTest{
	
	private  BrowserCapabilities objBrowserCapabilitiesTest;
	private  String sValid="VALID";
	private  String sNull=null;
	private  String sBlank="";
	private  double dValid=2.45;
	private  double dZero=0.00;
	
	
	@Before	
	public void setup() {
		objBrowserCapabilitiesTest=new BrowserCapabilities();
	}
		
	
	@Test
	public void testId()
	{
		//valid
		objBrowserCapabilitiesTest.setId(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getId());	
		
		//invalid
		objBrowserCapabilitiesTest.setId(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getId());	
		
		//invalid
		objBrowserCapabilitiesTest.setId(sBlank);		
     	assertEquals(sBlank,objBrowserCapabilitiesTest.getId());	
	}
	
	
	@Test
	public void testType()
	{
		//valid
		objBrowserCapabilitiesTest.setType(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getType());			
		
		//invalid
		objBrowserCapabilitiesTest.setType(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getType());	
		
		//invalid
		objBrowserCapabilitiesTest.setType(sBlank);		
		assertEquals(sBlank,objBrowserCapabilitiesTest.getType());	
	}
	
	
	@Test
	public void testPlatFormName()
	{
		//valid
		objBrowserCapabilitiesTest.setPlatformName(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getPlatformName());			
		
		//invalid
		objBrowserCapabilitiesTest.setPlatformName(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getPlatformName());	
		
		//invalid
		objBrowserCapabilitiesTest.setPlatformName(sBlank);		
    	assertEquals(sBlank,objBrowserCapabilitiesTest.getPlatformName());		
	}
	
	
	@Test
	public void testPlatFormVersion()
	{
		//valid
		objBrowserCapabilitiesTest.setPlatformVersion(dValid);	
		assertTrue(dValid==objBrowserCapabilitiesTest.getPlatformVersion());			
		
		//invalid
		objBrowserCapabilitiesTest.setPlatformVersion(dZero);		
		assertTrue(dZero==objBrowserCapabilitiesTest.getPlatformVersion());	
	}
	
	
	@Test
	public void testBrowserName()
	{
		//valid
		objBrowserCapabilitiesTest.setBrowserName(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getBrowserName());			
		
		//invalid
		objBrowserCapabilitiesTest.setBrowserName(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getBrowserName());	
		
		//invalid
		objBrowserCapabilitiesTest.setBrowserName(sBlank);		
		 assertEquals(sBlank,objBrowserCapabilitiesTest.getBrowserName());		
	}
	
	
	@Test
	public void testBrowserVersion()
	{
		//valid
		objBrowserCapabilitiesTest.setBrowserVersion(dValid);	
		assertTrue(dValid==objBrowserCapabilitiesTest.getBrowserVersion());			
		
		//invalid
		objBrowserCapabilitiesTest.setBrowserVersion(dZero);		
		assertTrue(dZero==objBrowserCapabilitiesTest.getBrowserVersion());	
	}
	
	
	@Test
	public void testDeviceName()
	{
		//valid
		objBrowserCapabilitiesTest.setDeviceName(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getDeviceName());			
		
		//invalid
		objBrowserCapabilitiesTest.setDeviceName(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getDeviceName());	
		
		//invalid
		objBrowserCapabilitiesTest.setDeviceName(sBlank);		
		 assertEquals(sBlank,objBrowserCapabilitiesTest.getDeviceName());		
	}
	
	
	@Test
	public void testDeviceOrientation()
	{
		//valid
		objBrowserCapabilitiesTest.setDeviceOrientattion(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getDeviceOrientattion());			
		
		//invalid
		objBrowserCapabilitiesTest.setDeviceOrientattion(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getDeviceOrientattion());	
		
		//invalid
		objBrowserCapabilitiesTest.setDeviceOrientattion(sBlank);		
		 assertEquals(sBlank,objBrowserCapabilitiesTest.getDeviceOrientattion());		
	}
	
	
	@Test
	public void testStatus()
	{
		//valid
		objBrowserCapabilitiesTest.setStatus(sValid);		
		assertEquals(sValid,objBrowserCapabilitiesTest.getStatus());			
		
		//invalid
		objBrowserCapabilitiesTest.setStatus(sNull);		
		assertEquals(sNull,objBrowserCapabilitiesTest.getStatus());	
		
		//invalid
		objBrowserCapabilitiesTest.setStatus(sBlank);		
		 assertEquals(sBlank,objBrowserCapabilitiesTest.getStatus());		
	}

}
