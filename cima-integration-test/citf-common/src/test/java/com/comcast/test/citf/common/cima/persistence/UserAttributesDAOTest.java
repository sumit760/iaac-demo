package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.testng.Assert;

import com.comcast.test.citf.common.cima.persistence.beans.UserAttributes;
import com.comcast.test.citf.common.cima.persistence.beans.Users;

public class UserAttributesDAOTest {
	
	private UserAttributesDAO mockUserAttributesDAO;
	private Session mockSession;
	private Criteria mockCriteria;
	
	
	@Before
	public void init() {		
		mockUserAttributesDAO = Mockito.spy(new UserAttributesDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);		
	}
	
	
	@Test
	public void testFindUserAttributesByUser() {
		
		UserAttributes uattr = getUserAttributes();
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserAttributesDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(UserAttributes.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(uattr);
		
		UserAttributes actualUserAttr = mockUserAttributesDAO.findUserAttributesByUser(getUsers());
		
		assertThat(
				"Expected valid response",
				actualUserAttr, notNullValue());
		
		assertThat(actualUserAttr.getAlterEmail(), is(alterEmail));
		assertThat(actualUserAttr.getAlterEmailPassword(), is(alterEmailPassword));
		assertThat(actualUserAttr.getEmail(), is(email));
		assertThat(actualUserAttr.getFbId(), is(fbId));
		assertThat(actualUserAttr.getFbPassword(), is(fbPassword));
		assertThat(actualUserAttr.getGuid(), is(guid));
	}
	
	
	@Test
	public void testFindUserAttributesByUserId() {
		
		UserAttributes uattr = getUserAttributes();
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserAttributesDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(UserAttributes.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(uattr);
		
		UserAttributes userattr = mockUserAttributesDAO.findUserAttributesByUserId(userId);
		
		assertThat(
				"Expected valid response",
				userattr, notNullValue());
		
		assertThat(userattr.getAlterEmail(), is(alterEmail));
		assertThat(userattr.getAlterEmailPassword(), is(alterEmailPassword));
		assertThat(userattr.getEmail(), is(email));
		assertThat(userattr.getFbId(), is(fbId));
		assertThat(userattr.getFbPassword(), is(fbPassword));
		assertThat(userattr.getGuid(), is(guid));
	}
	
	
	@Test
	public void testFindUserAttributesByGuid() {
		
		UserAttributes uattr = getUserAttributes();
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserAttributesDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(UserAttributes.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(uattr);
		
		UserAttributes userattr = mockUserAttributesDAO.findUserAttributesByGuid(guid);
		
		assertThat(
				"Expected valid response",
				userattr, notNullValue());
		
		assertThat(userattr.getAlterEmail(), is(alterEmail));
		assertThat(userattr.getAlterEmailPassword(), is(alterEmailPassword));
		assertThat(userattr.getEmail(), is(email));
		assertThat(userattr.getFbId(), is(fbId));
		assertThat(userattr.getFbPassword(), is(fbPassword));
		assertThat(userattr.getGuid(), is(guid));
		
	}
	
	
	@Test
	public void testPopulateAndGetUserAttributes()  {
		
		UserAttributes uattr = getUserAttributes();
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserAttributesDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(UserAttributes.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(uattr);
		
		Mockito
		       .when(mockSession.merge(Mockito.any(UserAttributes.class)))
		       .thenReturn(mockSession);
		
		UserAttributes userattr = null;
		try{
			userattr = mockUserAttributesDAO.populateAndGetUserAttributes(guid, 
														   getUsers(), 
				                                           alterEmail, 
				                                           alternativeEmail, 
				                                           alterEmailPassword, 
				                                           secretQuestion, 
				                                           secretAnswer, 
				                                           fbId, 
				                                           fbPassword, 
				                                           ssn, 
				                                           getDate());
		}catch(Exception e){
			Assert.fail("Error occurred while populating UserAttributes "+e.getMessage());
		}
		
		assertThat(
				"Expected valid response",
				userattr, notNullValue());
		
		assertThat(userattr.getAlterEmail(), is(alterEmail));
		assertThat(userattr.getAlterEmailPassword(), is(alterEmailPassword));
		assertThat(userattr.getEmail(), is(email));
		assertThat(userattr.getFbId(), is(fbId));
		assertThat(userattr.getFbPassword(), is(fbPassword));
		assertThat(userattr.getGuid(), is(guid));
	}
	
	
	private UserAttributes getUserAttributes() {
		
		UserAttributes uattr = new UserAttributes();
		
		uattr.setAlterEmail(alterEmail);
		uattr.setAlterEmailPassword(alterEmailPassword);
		uattr.setEmail(email);
		uattr.setFbId(fbId);
		uattr.setFbPassword(fbPassword);
		uattr.setGuid(guid);
		
		return uattr;
	}
	
	private Users getUsers() {
		
		Users user = new Users();
		
		user.setCategory(category);
		user.setEnvironment(environment);
		user.setLoginStatus(loginStatus);
		
		return user;
	}
	
	private Date getDate() throws ParseException {
		
		String strDate = "2016-02-10 00:00:00";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        java.util.Date date = sdf.parse(strDate);
        return new Date(date.getTime());
		
	}
	
	
	private final String alterEmail = "abc@email.com";
	private final String alterEmailPassword = "mockpassword";
	private final String email = "abc@email.com";
	private final String fbId = "abc@fb.com";
	private final String fbPassword = "mockFBpassword";
	private final String guid = "mockGuid";
	
	private final String category = "mockCategory";
	private final String environment = "QA";
	private final String loginStatus = "A";

	private final String userId = "mockUserId";
	
	private final String alternativeEmail = "abc@email.com";
	private final String secretQuestion = "mockQuestion";
	private final String secretAnswer = "mockAnswer";
	private final String ssn = "mockSSN";
	
	
}
