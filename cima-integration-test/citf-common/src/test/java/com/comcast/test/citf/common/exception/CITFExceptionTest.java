package com.comcast.test.citf.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.exception.CITFException;

public class CITFExceptionTest{
	
	private  CITFException objCITFExceptionValid;
	private  CITFException objCITFExceptionNull;
	private  CITFException objCITFExceptionBlank;
	
	@Before
	public  void Setup()
	{
		objCITFExceptionValid=new CITFException("message",CITFExceptionTest.class);
		objCITFExceptionNull=new CITFException(null,CITFExceptionTest.class);
		objCITFExceptionBlank=new CITFException("",CITFExceptionTest.class);
		
	}
	
	@Test
	public void testGetSourceClass()
	{
		assertEquals(CITFExceptionTest.class,objCITFExceptionValid.getSourceClass());
	
	}
	
	@Test
	public void testGetSourceClassName()
	{
		assertEquals(CITFExceptionTest.class.getName(),objCITFExceptionValid.getSourceClassName());
	}

	@Test
	public void testGetMessage()
	{
		assertThat(objCITFExceptionValid.getMessage(), is("message"));
		assertThat(objCITFExceptionBlank.getMessage(), is(""));
		assertThat(objCITFExceptionNull.getMessage(), nullValue());
	}	

}
