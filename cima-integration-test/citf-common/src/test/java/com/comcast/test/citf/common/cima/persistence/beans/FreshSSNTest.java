package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;


import org.junit.Before;
import org.junit.Test;


import com.comcast.test.citf.common.cima.persistence.beans.FreshSSN;


public class FreshSSNTest {

	FreshSSN objFreshSSN;
	private  String sValid="VALID";
	private String sNull=null;
	private String sBlank="";


	@Before
	public void setup() {
		objFreshSSN=new FreshSSN();
	}


	@Test
	public void testSSN() {

		objFreshSSN.setSsn(sValid);
		assertEquals(sValid,objFreshSSN.getSsn());

		objFreshSSN.setSsn(sNull);
		assertEquals(sNull,objFreshSSN.getSsn());

		objFreshSSN.setSsn(sBlank);
		assertEquals(sBlank,objFreshSSN.getSsn());
	}


	@Test
	public void testCreationDate() {
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		objFreshSSN.setCreationDate(date);
		assertEquals(date,objFreshSSN.getCreationDate());
	}


	@Test
	public void testDob() {
		java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
		objFreshSSN.setDob(date);
		assertEquals(date,objFreshSSN.getDob());
	}

}
