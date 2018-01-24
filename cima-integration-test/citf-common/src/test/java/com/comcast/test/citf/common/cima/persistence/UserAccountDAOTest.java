package com.comcast.test.citf.common.cima.persistence;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

public class UserAccountDAOTest {
	
	private UserAccountDAO mockUserAccountDAO;
	private AccountUserMap.AccountUserId mockAccountUserId;
	private AccountUserMap mockAccountUserMap;
	private Session mockSession;
	
	@Before
	public void init() {
		
		mockUserAccountDAO = Mockito.spy(new UserAccountDAO());
		mockAccountUserId = Mockito.mock(AccountUserMap.AccountUserId.class,Mockito.RETURNS_DEEP_STUBS);
		mockAccountUserMap = Mockito.mock(AccountUserMap.class, Mockito.RETURNS_DEEP_STUBS);
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testPopulateAccountUserMap(){
		
		//Stub
		Mockito
		       .doReturn(mockAccountUserId)
		       .when(mockUserAccountDAO)
		       .getAccountUserId(Mockito.any(Users.class), Mockito.any(Accounts.class));
		
		Mockito
		       .doReturn(mockAccountUserMap)
		       .when(mockUserAccountDAO)
		       .getAccountUserMap(Mockito.any(AccountUserMap.AccountUserId.class), Mockito.anyString());
		
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserAccountDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(AccountUserMap.class)))
		       .thenReturn(mockSession);
		
		try{
			mockUserAccountDAO.populateAccountUserMap(getUsers(), getAccount(), ICimaCommonConstants.UserRoles.PRIMARY);
		}catch(InvalidUserException e){
			Assert.fail("Not able to populate AccountUserMap due to "+e.getMessage());
		}
		
		Mockito.verify(mockUserAccountDAO, VerificationModeFactory.times(1))
					.getAccountUserId(Mockito.any(Users.class), Mockito.any(Accounts.class));
		
		Mockito.verify(mockUserAccountDAO, VerificationModeFactory.times(1))	
				    .getAccountUserMap(Mockito.any(AccountUserMap.AccountUserId.class), Mockito.anyString());
		
		Mockito.verify(mockUserAccountDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(AccountUserMap.class));
	}
	
	
	private Users getUsers() {
		
		Users user = new Users();
		user.setCategory(category);
		user.setEnvironment(environment);
		user.setLoginStatus(loginStatus);
		
		return user;
	}
	
	private Accounts getAccount() {
	
		Accounts account = new Accounts();
		
		account.setAccountStatus(accountStatus);
		account.setAddress(address);
		account.setAuthGuid(authGuid);
		account.setBillingAccountId(billingAccountId);
		
		return account;
	}
	
	private final String category = "mockCategory";
	private final String environment = "mockEnvironment";
	private final String loginStatus = "A";

	private final String accountStatus = "A";
	private final String address = "mockAddress";
	private final String authGuid = "mockAuthGuid";
	private final String billingAccountId = "mockBillingAccountId";
	
}
