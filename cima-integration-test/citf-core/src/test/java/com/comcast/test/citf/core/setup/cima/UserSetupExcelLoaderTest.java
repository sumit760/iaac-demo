package com.comcast.test.citf.core.setup.cima;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CoreContextInitilizer;

public class UserSetupExcelLoaderTest {
	
	private static final String FILE_NAME_LOGIN_USERS_FOR_QA = "Test_Users_Login_QA.csv";
	private static final int entriesToBeLoaded = 99;
	
	@Before
	public void setup() {
		CoreContextInitilizer.initializeContext();
	}
	
	
	@Test
	public void testLoadAccounts() {
		
		int noOfentries = UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_LOGIN_USERS_FOR_QA, 
				                                 ICommonConstants.ENVIRONMENT_QA,
				                                 ICimaCommonConstants.USER_CATEGORY_LOG_IN);
		
		assertThat(
				noOfentries,
				is(entriesToBeLoaded));	
	}

	@After
	public void tearDown() {
		CoreContextInitilizer.destroyContext();
	}
}
