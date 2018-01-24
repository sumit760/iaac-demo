package com.comcast.test.citf.common.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.service.Dictionary;

public class DictionaryTest {


	private Dictionary objDictionary;


	@Before
	public  void setup() 	
	{
		objDictionary=new Dictionary();
	}


	@Test
	public void testSize()
	{
		assertEquals(1535578,objDictionary.size());
	}

	@Test
	public void testGetPrime()
	{
		assertEquals(3263443,objDictionary.getPrime());
	}


	@Test
	public void testWord()
	{
		String s=objDictionary.word(10);
		assertEquals(s,"active_abbey");
	}


}
