package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.LogFinders;

public class LogFinderDAOTest {
	
	private LogFinderDAO mockLogFinderDAO;
	private LogFinders.LogFindersPrimaryKeys mockLogFindersPrimaryKeys;
	private LogFinders mockLogFinders;
	private Session mockSession;
	private Criteria mockCriteria;
	private Criterion mockCriterion;
	
	@Before
	public void init() {
		mockLogFinderDAO = Mockito.spy(new LogFinderDAO());
		mockLogFindersPrimaryKeys = Mockito.mock(LogFinders.LogFindersPrimaryKeys.class, Mockito.RETURNS_DEEP_STUBS);
		mockLogFinders = Mockito.mock(LogFinders.class, Mockito.RETURNS_DEEP_STUBS);
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriterion = Mockito.mock(Criterion.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testPopulateLogFinder() {
		
		String id = "123";
		String environment = "QA";
		String logPath = "mockLogPath";
		String regex = "mockRegex";
		String splunkQry = "mocksplunkQry";
		String splunkKey = "mocksplunkKey";
		
		//Stub
		Mockito
			   .doReturn(mockLogFindersPrimaryKeys)
			   .when(mockLogFinderDAO)
			   .getLogFindersPrimaryKeys(Mockito.anyString(), Mockito.anyString());
		
		Mockito
		       .doReturn(mockLogFinders)
		       .when(mockLogFinderDAO)
		       .getLogFinders(Mockito.any(LogFinders.LogFindersPrimaryKeys.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockLogFinderDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(LogFinders.class)))
		       .thenReturn(mockSession);
		
		mockLogFinderDAO.populateLogFinder(id, environment, logPath, regex, splunkQry, splunkKey);

		Mockito.verify(mockLogFinderDAO, VerificationModeFactory.times(1))
					.getLogFindersPrimaryKeys(Mockito.anyString(), Mockito.anyString());
		
		Mockito.verify(mockLogFinderDAO, VerificationModeFactory.times(1))
					.getLogFinders(Mockito.any(LogFinders.LogFindersPrimaryKeys.class), Mockito.anyString(), 
							Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		Mockito.verify(mockLogFinderDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(LogFinders.class));
		     
	}
	
	
	@Test
	public void testFindLogChecker() {
		
		String id = "123";
		String environment = "QA";
		
		//Stub
		Mockito
			   .doReturn(mockSession)
			   .when(mockLogFinderDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(LogFinders.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(mockLogFinders);
		
		LogFinders lf = mockLogFinderDAO.findLogChecker(id, environment);
		
		assertThat(
				lf, notNullValue());
		
		assertThat(
				lf, is(mockLogFinders));
		
	}

}
