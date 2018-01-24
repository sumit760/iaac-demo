package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.assertEquals;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.persistence.beans.TempParameters;


public class TempParametersTest {

	TempParameters objTempParameters;
	private  String sValid="VALID";
	private String sNull=null;
	private String sBlank="";


	Calendar calendar = Calendar.getInstance();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());


	@Before
	public void setup() {
		objTempParameters=new TempParameters();
	}

	@Test
	public void testKey() {

		objTempParameters.setKey(sValid);
		assertEquals(sValid,objTempParameters.getKey());

		objTempParameters.setKey(sNull);
		assertEquals(sNull,objTempParameters.getKey());

		objTempParameters.setKey(sBlank);
		assertEquals(sBlank,objTempParameters.getKey());
	}

	@Test
	public void testValue() {
		objTempParameters.setValue(sValid);
		assertEquals(sValid,objTempParameters.getValue());

		objTempParameters.setValue(sNull);
		assertEquals(sNull,objTempParameters.getValue());

		objTempParameters.setValue(sBlank);
		assertEquals(sBlank,objTempParameters.getValue());
	}


	@Test
	public void testAddVal1() {
		objTempParameters.setAddVal1(sValid);
		assertEquals(sValid,objTempParameters.getAddVal1());

		objTempParameters.setAddVal1(sNull);
		assertEquals(sNull,objTempParameters.getAddVal1());

		objTempParameters.setAddVal1(sBlank);
		assertEquals(sBlank,objTempParameters.getAddVal1());
	}


	@Test
	public void testAddVal2() {
		objTempParameters.setAddVal2(sValid);
		assertEquals(sValid,objTempParameters.getAddVal2());

		objTempParameters.setAddVal2(sNull);
		assertEquals(sNull,objTempParameters.getAddVal2());

		objTempParameters.setAddVal2(sBlank);
		assertEquals(sBlank,objTempParameters.getAddVal2());
	}

	@Test
	public void testStatus() {
		objTempParameters.setStatus(sValid);
		assertEquals(sValid,objTempParameters.getStatus());

		objTempParameters.setStatus(sNull);
		assertEquals(sNull,objTempParameters.getStatus());

		objTempParameters.setStatus(sBlank);
		assertEquals(sBlank,objTempParameters.getStatus());
	}


	@Test
	public void testModifiedOn() {
		objTempParameters.setModifiedOn(currentTimestamp);
		assertEquals(currentTimestamp,objTempParameters.getModifiedOn());
	}

	@Test
	public void modifiedByTest() {

		objTempParameters.setModifiedBy(sValid);
		assertEquals(sValid,objTempParameters.getModifiedBy());

		objTempParameters.setModifiedBy(sNull);
		assertEquals(sNull,objTempParameters.getModifiedBy());

		objTempParameters.setModifiedBy(sBlank);
		assertEquals(sBlank,objTempParameters.getModifiedBy());
	}

}
