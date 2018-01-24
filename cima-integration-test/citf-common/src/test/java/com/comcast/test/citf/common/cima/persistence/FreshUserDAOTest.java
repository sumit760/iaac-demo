package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;

public class FreshUserDAOTest {
	
	private FreshUserDAO mockFreshUsersDAO;
	private Session mockSession;
	private Criteria mockCriteria;
	private Criterion mockCriterion;
	private Query mockQuery;
	
	@Before
	public void init() {
		mockFreshUsersDAO = Mockito.spy(new FreshUserDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriterion = Mockito.mock(Criterion.class, Mockito.RETURNS_DEEP_STUBS);
		mockQuery = Mockito.mock(Query.class,Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testFindUser() {
		
		List<FreshUsers> freshUserList = new ArrayList<FreshUsers>();
		FreshUsers freshUser = getFreshMockUsers();
		freshUserList.add(freshUser);
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockFreshUsersDAO)
			   .getSession();
		
		Mockito
			   .when(mockSession.createCriteria(Mockito.eq(FreshUsers.class)))
			   .thenReturn(mockCriteria);
		
		Mockito
			   .doReturn(mockCriteria)
			   .when(mockCriteria)
			   .add(mockCriterion);
		
		Mockito
			   .when(mockCriteria.list())
			   .thenReturn(freshUserList);
		
		Mockito
		       .when(mockSession.merge(Mockito.any(FreshUsers.class)))
		       .thenReturn(mockSession);
		
		List<FreshUserDAO.Query> queries = new ArrayList<FreshUserDAO.Query>();
		queries.add(new FreshUserDAO.Query(FreshUserDAO.Columns.FACEBOOK, FreshUserDAO.Conditions.IS_NOT_NULL, null));
		
		FreshUsers users = mockFreshUsersDAO.findUser(queries);
		
		assertThat(
				"Expected valid response",
				users, notNullValue());
		
		assertThat(users.getAlterEmailPassword(), is(alterEmailPassword));
		assertThat(users.getAlternativeEmail(), is(alternativeEmail));
		assertThat(users.getFbId(), is(fbId));
		assertThat(users.getFbPassword(), is(fbPassword));
		assertThat(users.getLockStatus(), is(lockStatus));
	}
	
	
	@Test
	public void testUnlockUser() {
		
		FreshUsers user = getFreshMockUsers();
		int primaryKey = 1;
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockFreshUsersDAO)
			   .getSession();
		
		Mockito
			   .when(mockSession.createCriteria(Mockito.eq(FreshUsers.class)))
			   .thenReturn(mockCriteria);
		
		Mockito
			   .doReturn(mockCriteria)
			   .when(mockCriteria)
			   .add(mockCriterion);
		
		Mockito
			   .when(mockCriteria.uniqueResult())
			   .thenReturn(user);
		
		Mockito
	       .when(mockSession.merge(Mockito.any(FreshUsers.class)))
	       .thenReturn(mockSession);
		
		boolean unlockStatus = mockFreshUsersDAO.unlockUser(primaryKey);
		
		assertThat(unlockStatus, is(true));
	}
	
	
	private FreshUsers getFreshMockUsers() {
		
		FreshUsers freshUser = new FreshUsers();
		
		freshUser.setAlterEmailPassword(alterEmailPassword);
		freshUser.setAlternativeEmail(alternativeEmail);
		freshUser.setFbId(fbId);
		freshUser.setFbPassword(fbPassword);
		freshUser.setLockStatus(lockStatus);
		
		return freshUser;
	}
	
	
	private String alterEmailPassword = "mockPassword";
	private String alternativeEmail = "mock@email.com";
	private String fbId = "mockFbId";
	private String fbPassword = "mockFBPassword";
	private String lockStatus = "Y";

}
