package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.TempParameters;

public class TempParameterDAOTest {

	private TempParameterDAO mockTempParameterDAO;
	private Session mockSession;
	private Criteria mockCriteria;
	private Criterion mockCriterion;
	
	@Before
	public void init() {
		mockTempParameterDAO = Mockito.spy(new TempParameterDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriterion = Mockito.mock(Criterion.class, Mockito.RETURNS_DEEP_STUBS);
	}

	
	@Test
	public void testFindParameterByKey() {
		
		TempParameters tempParam = getTempParameters();
		
		Mockito
		   .doReturn(mockSession)
		   .when(mockTempParameterDAO)
		   .getSession();
	
		Mockito
	       .when(mockSession.createCriteria(Mockito.eq(TempParameters.class)))
	       .thenReturn(mockCriteria);
	
		Mockito
	       .when(mockCriteria.add(Mockito.any(Criterion.class)))
	       .thenReturn(mockCriteria);
	
		Mockito
	       .when(mockCriteria.uniqueResult())
	       .thenReturn(tempParam);
		
		TempParameters actualTempParam = mockTempParameterDAO.findParameterByKey(key);
		
		assertThat(
				"Expected valid response",
				actualTempParam, notNullValue());
		
		assertThat(actualTempParam.getAddVal1(), is(addVal1));
		assertThat(actualTempParam.getAddVal2(), is(addVal2));
		assertThat(actualTempParam.getKey(), is(key));
		assertThat(actualTempParam.getModifiedBy(), is(modifiedBy));
		assertThat(actualTempParam.getStatus(), is(status));
		assertThat(actualTempParam.getValue(), is(value));
		
	}
	
	
	@Test
	public void testPopulateResponse() {
		
		TempParameters tempParam = getTempParameters();
		
		//Stub
		Mockito
		       .doReturn(tempParam)
		       .when(mockTempParameterDAO)
		       .findParameterByKey(key);
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockTempParameterDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(TempParameters.class)))
		       .thenReturn(mockSession);
		
		mockTempParameterDAO.populateResponse(key, value, addVal1, addVal2);
		
		Mockito.verify(mockTempParameterDAO, VerificationModeFactory.times(1)).findParameterByKey(key);
		Mockito.verify(mockTempParameterDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(TempParameters.class));
	}
	
	
	@Test
	public void testDeactivateParameter() {
		
		TempParameters tempParam = getTempParameters();
		
		//Stub
		Mockito
	       .doReturn(tempParam)
	       .when(mockTempParameterDAO)
	       .findParameterByKey(key);
		
		Mockito
	       .doReturn(mockSession)
	       .when(mockTempParameterDAO)
	       .getSession();
		
		Mockito
	       .when(mockSession.merge(Mockito.any(TempParameters.class)))
	       .thenReturn(mockSession);
		
		mockTempParameterDAO.deactivateParameter(key);
		
		Mockito.verify(mockTempParameterDAO, VerificationModeFactory.times(1)).findParameterByKey(key);
		Mockito.verify(mockTempParameterDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(TempParameters.class));
	}
	
	private TempParameters getTempParameters() {
		
		TempParameters temp = new TempParameters();
		
		temp.setAddVal1(addVal1);
		temp.setAddVal2(addVal2);
		temp.setKey(key);
		temp.setModifiedBy(modifiedBy);
		temp.setStatus(status);
		temp.setValue(value);
		
		return temp;
		
	}
	
	private String addVal1 = "mockVal1";
	private String addVal2 = "mockVal2";
	private String key = "mockKey";
	private String modifiedBy = "mockAgent";
	private String status = "mockStatus";
	private String value = "mockValue";
}
