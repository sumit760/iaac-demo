package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.service.UserNameGeneratorService;

public class UserNameGeneratorServiceTest {

	private UserNameGeneratorService objUserNameGeneratorServiceTest1;
	private UserNameGeneratorService objUserNameGeneratorServiceTest2;

	@Test
	public void testNext() {
		
		objUserNameGeneratorServiceTest1 = new UserNameGeneratorService(10);
		String userNameFirst = objUserNameGeneratorServiceTest1.next();
		
		assertThat(userNameFirst, notNullValue());
		
		objUserNameGeneratorServiceTest2 = new UserNameGeneratorService(20);
		String userNameSecond = objUserNameGeneratorServiceTest2.next();
		
		assertThat(userNameSecond, notNullValue());
		
		assertNotEquals(userNameFirst, userNameSecond);
		
	}


}
