package com.comcast.test.citf.core.setup.cima;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.CoreContextInitilizer;

public class AccountExcelLoaderTest {

	private static final String FILE_NAME_ACCOUNTS_FOR_QA = "Test_Accounts_QA.csv";
	private static final int accountsToBeLoaded = 150;
	
	@Before
	public void setup() {
		CoreContextInitilizer.initializeContext();
	}
	
	
	@Test
	public void testLoadAccounts() {
		
		int noOfAccountsLoaded = AccountExcelLoader.loadAccounts(FILE_NAME_ACCOUNTS_FOR_QA, ICommonConstants.ENVIRONMENT_QA);
	
		assertThat(
				noOfAccountsLoaded,
				is(accountsToBeLoaded));
	}
	
	@After
	public void tearDown() {
		CoreContextInitilizer.destroyContext();
	}
	
}
