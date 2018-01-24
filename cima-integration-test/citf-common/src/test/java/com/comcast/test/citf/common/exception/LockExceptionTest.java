package com.comcast.test.citf.common.exception;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class LockExceptionTest {

	private LockException objLockExceptionTest;
	private String exceptionType = "LockException";
	private String message = "message";

	@Before	
	public  void setup()
	{
		objLockExceptionTest = new LockException(exceptionType,message);

	}


	@Test
	public void getExceptionTypeTest() {
		assertThat(objLockExceptionTest.getExceptionType(), is(exceptionType));
		assertThat(objLockExceptionTest.getMessage(), is(message));
	}


}
