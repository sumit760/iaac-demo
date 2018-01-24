package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class StringUtilityTest{

	private String sValidString="VALID";	
	private String sEmptystring="";
	private String regularExpressionPattern="^h.*";
	private String inputStringValid="high pass h3";
	private String inputStringInvalid="str jabsfsf h3fsdfsdf";
	private String xml="<root xmlns:h=\"http://www.w3.org/TR/html4/\" xmlns:f=\"http://www.w3schools.com/furniture\"><h:table><h:tr><h:td>Apples</h:td><h:td>Bananas</h:td></h:tr></h:table></root>";
	private String xmlAfter="<root  ><h:table><h:tr><h:td>Apples</h:td><h:td>Bananas</h:td></h:tr></h:table></root>";
	private String tokenString="this is a token <abcde and this is another token <324234";
	private String lastToken="324234";
	private String firstToken="abcde";
	private List<String> custMangerGuids;
	
	@Test
	public void testIsStringEmpty() throws InterruptedException
	{	
		assertTrue(StringUtility.isStringEmpty(sEmptystring));
		assertFalse(StringUtility.isStringEmpty(sValidString));
	}	


	@Test
	public void testRegularExpressionChecker() throws InterruptedException
	{	
		assertEquals(StringUtility.regularExpressionChecker(inputStringValid, regularExpressionPattern),inputStringValid);
		assertEquals(StringUtility.regularExpressionChecker(inputStringInvalid, regularExpressionPattern),null);
	}	

	@Test
	public void testRegularExpressionCheckerWithElementNumber() throws InterruptedException
	{	
		assertEquals(StringUtility.regularExpressionChecker("This is an order of $300 OK", "(.*?)(\\d+)(.*?)",2),"300");
		assertEquals(StringUtility.regularExpressionChecker("This is an order of $300 OK", "(.*?)(\\d+)(\\s+[A-Z]+)",3)," OK");
	}	


	@Test
	public void testRemoveAllNameSpaces() throws InterruptedException
	{	
		assertEquals(StringUtility.removeAllNameSpaces(xml, "\\s","\\>"),xmlAfter);
		assertFalse(StringUtility.removeAllNameSpaces(xml, "\\s","\\>").contentEquals(xml));
	}	


	@Test
	public void testListToStringArray() throws InterruptedException
	{	
		custMangerGuids = new ArrayList<String>();
		custMangerGuids.add("VALID");
		custMangerGuids.add("");
		
		String[] ret = StringUtility.ListToStringArray(custMangerGuids);
		
		assertThat(
				ret[0], is("VALID"));
		
		assertThat(
				ret[1], is(""));
		
		assertThat(
				ret.length, is(2));
	}	



	@Test
	public void testgetTokensFromString() throws InterruptedException
	{	
		String token;
		
		Set<String> tokens = StringUtility.getTokensFromString(tokenString, "<");
		Iterator<String> it = tokens.iterator();
		token = it.next();
		
		assertThat(
				token, is("this is a token "));
		
		token = it.next();
		assertThat(
				token, is("abcde and this is another token "));
		
		token = it.next();
		assertThat(
				token, is("324234"));
	}	

	@Test
	public void testgetLastTokensFromString() throws InterruptedException
	{	
		assertTrue(StringUtility.getLastTokenFromString(tokenString, "<").equalsIgnoreCase(lastToken));
	}	

	@Test
	public void testAppendStrings() throws InterruptedException
	{	
		assertEquals(StringUtility.appendStrings(firstToken,lastToken),firstToken + lastToken);
	}	


	@Test
	public void testAppendObjects() throws InterruptedException
	{	
		assertEquals(StringUtility.appendStrings(firstToken,lastToken),firstToken + lastToken);
	}	

	@Test
	public void tesReplaceSeparatorsInString() throws InterruptedException
	{	
		assertEquals(StringUtility.replaceSeparatorsInString("<", ">", tokenString),"this is a token >abcde and this is another token >324234");
	}	



}
