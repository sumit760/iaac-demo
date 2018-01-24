package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;

public class UserDAOTest {
	
	private UserDAO mockUserDAO;
	private Session mockSession;
	private Criteria mockCriteria;
	
	@Before
	public void init() {
		mockUserDAO = Mockito.spy(new UserDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	@Test
	public void testFindUserById() {
		
		Users users = getUsers();
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockUserDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Users.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(users);
		
		Users actualUsers = mockUserDAO.findUserById(userId);
		
		assertThat(
				"Expected valid response",
				actualUsers, notNullValue());
		
		assertThat(actualUsers.getUserId(), is(userId));
		assertThat(actualUsers.getCategory(), is(category));
		assertThat(actualUsers.getEnvironment(), is(environment));
		assertThat(actualUsers.getLoginStatus(), is(loginStatus));
		assertThat(actualUsers.getMovieRating(), is(movieRating));
		assertThat(actualUsers.getTvRating(), is(tvRating));
	}
	
	
	@Test
	public void testFindUsersByEnvironment() {
		
		List<Users> userList = new ArrayList<Users>();
		userList.add(getUsers());
		
		//Stub
		Mockito
	       .doReturn(mockSession)
	       .when(mockUserDAO)
	       .getSession();
	
		Mockito
	       .when(mockSession.createCriteria(Mockito.eq(Users.class)))
	       .thenReturn(mockCriteria);
	
		Mockito
	       .when(mockCriteria.add(Mockito.any(Criterion.class)))
	       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(userList);
		
		List<Users> usrList = mockUserDAO.findUsersByEnvironment(environment);
		
		assertThat(
				"Expected valid response",
				usrList, notNullValue());
		
		assertThat(
				"Expected valid response",
				usrList.get(0), notNullValue());
		
		assertThat(usrList.get(0).getUserId(), is(userId));
		assertThat(usrList.get(0).getCategory(), is(category));
		assertThat(usrList.get(0).getEnvironment(), is(environment));
		assertThat(usrList.get(0).getLoginStatus(), is(loginStatus));
		assertThat(usrList.get(0).getMovieRating(), is(movieRating));
		assertThat(usrList.get(0).getTvRating(), is(tvRating));
		
	}
	
	
	
	@Test
	public void testPopulateUser() {
		
		Users user = getUsers();
		
		Mockito
		       .doReturn(user)
		       .when(mockUserDAO)
		       .getUsers(Mockito.anyString(), 
		    		     Mockito.anyString(),
		    		     Mockito.anyString(), 
		    		     Mockito.anyString(), 
		    		     Mockito.anyString(), 
		    		     Mockito.anyString(), 
		    		     Mockito.anyString(), 
		    		     Mockito.anyString());
		Mockito
	       .doReturn(mockSession)
	       .when(mockUserDAO)
	       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(Users.class)))
		       .thenReturn(mockSession);
		       
		mockUserDAO.populateUser(userId, password, category, pin, tvRating, movieRating, environment, loginStatus);
		
		Mockito.verify(mockUserDAO, VerificationModeFactory.times(1)).getUsers(Mockito.anyString(), 
   		     Mockito.anyString(),
   		     Mockito.anyString(), 
   		     Mockito.anyString(), 
   		     Mockito.anyString(), 
   		     Mockito.anyString(), 
   		     Mockito.anyString(), 
   		     Mockito.anyString());
		
		Mockito.verify(mockUserDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(user);
		
	}
	
	
	@Test
	public void testPopulateAndGetUser() {
		
		Users users = getUsers();
		
		//Stub
		Mockito
	       .doReturn(mockSession)
	       .when(mockUserDAO)
	       .getSession();
	
		Mockito
	       .when(mockSession.createCriteria(Mockito.eq(Users.class)))
	       .thenReturn(mockCriteria);
	
		Mockito
	       .when(mockCriteria.add(Mockito.any(Criterion.class)))
	       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(users);
		
		Users actualUser = null;
		try{
			actualUser = mockUserDAO.populateAndGetUser(userId, 
				                                          password, 
				                                          category, 
				                                          pin, 
				                                          tvRating, 
				                                          movieRating, 
				                                          environment);
		}catch(InvalidUserException e){
			Assert.fail("Error occurred while populating User "+e.getMessage());
		}
		
		assertThat(
				"Expected valid response",
				actualUser, notNullValue());
		
		assertThat(actualUser.getUserId(), is(userId));
		assertThat(actualUser.getCategory(), is(category));
		assertThat(actualUser.getEnvironment(), is(environment));
		assertThat(actualUser.getLoginStatus(), is(loginStatus));
		assertThat(actualUser.getTvRating(), is(tvRating));
		assertThat(actualUser.getMovieRating(), is(movieRating));
		
	}
	
	
	private Users getUsers() {
		Users user = new Users();
		
		user.setUserId(userId);
		user.setCategory(category);
		user.setEnvironment(environment);
		user.setLoginStatus(loginStatus);
		user.setMovieRating(movieRating);
		user.setTvRating(tvRating);
		
		return user;
	}
	
	private static final String userId = "mockId";
	private static final String password = "mockPassword";
	private static final String category = "mockCategory";
	private static final String environment = "QA";
	private static final String loginStatus = "A";
	private static final String movieRating = "NR";
	private static final String tvRating = "TV-G";
	private static final String pin = "1234";
}
