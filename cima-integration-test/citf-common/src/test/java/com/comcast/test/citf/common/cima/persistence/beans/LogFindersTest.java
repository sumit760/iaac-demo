package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.persistence.beans.LogFinders;
import com.comcast.test.citf.common.cima.persistence.beans.LogFinders.LogFindersPrimaryKeys;


public class LogFindersTest {


	LogFinders objLogFinders;
	LogFindersPrimaryKeys objLogFindersPrimaryKeys;
	private  String sValid="VALID";
	private String sNull=null;
	private String sBlank="";


	@Before
	public void setup()
	{
		objLogFinders=new LogFinders();
		objLogFindersPrimaryKeys=new LogFindersPrimaryKeys();
	}


	@Test
	public void testPrimaryKey()
	{

		objLogFinders.setPrimaryKey(objLogFindersPrimaryKeys);
		assertEquals(objLogFindersPrimaryKeys,objLogFinders.getPrimaryKey());

	}


	@Test
	public void testLogPath()
	{
		objLogFinders.setLogPath(sValid);
		assertEquals(sValid,objLogFinders.getLogPath());

		objLogFinders.setLogPath(sNull);
		assertEquals(sNull,objLogFinders.getLogPath());

		objLogFinders.setLogPath(sBlank);
		assertEquals(sBlank,objLogFinders.getLogPath());

	}

	@Test
	public void testRegex()
	{
		objLogFinders.setRegex(sValid);
		assertEquals(sValid,objLogFinders.getRegex());

		objLogFinders.setRegex(sNull);
		assertEquals(sNull,objLogFinders.getRegex());

		objLogFinders.setRegex(sBlank);
		assertEquals(sBlank,objLogFinders.getRegex());

	}

	@Test
	public void testSplunkQry()
	{
		objLogFinders.setSplunkQry(sValid);
		assertEquals(sValid,objLogFinders.getSplunkQry());

		objLogFinders.setSplunkQry(sNull);
		assertEquals(sNull,objLogFinders.getSplunkQry());

		objLogFinders.setSplunkQry(sBlank);
		assertEquals(sBlank,objLogFinders.getSplunkQry());

	}


	@Test
	public void testSplunkKey()
	{
		objLogFinders.setSplunkKey(sValid);
		assertEquals(sValid,objLogFinders.getSplunkKey());

		objLogFinders.setSplunkKey(sNull);
		assertEquals(sNull,objLogFinders.getSplunkKey());

		objLogFinders.setSplunkKey(sBlank);
		assertEquals(sBlank,objLogFinders.getSplunkKey());

	}

}
