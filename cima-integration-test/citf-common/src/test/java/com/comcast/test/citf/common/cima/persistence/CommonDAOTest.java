package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CommonDAOTest {
	
	private CommonDAO mockCommonDAO;
	private Session mockSession;
	private Query mockQuery;
	
	@Before
	public void init() {
		mockCommonDAO = Mockito.spy(new CommonDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockQuery = Mockito.mock(Query.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testUpdateSingleTable() {
		
		int updateQuantity = 1;
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockCommonDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createQuery(Mockito.anyString()))
		       .thenReturn(mockQuery);
		
		Mockito
		       .when(mockQuery.setString(Mockito.anyString(), Mockito.anyString()))
		       .thenReturn(mockQuery);
		
		Mockito
		       .when(mockQuery.executeUpdate())
		       .thenReturn(updateQuantity);
		
		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("password", "mockPassword");
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("userId", "mockUserId");
		
		int updateQ = mockCommonDAO.updateSingleTable(updateMap, conditionMap, "Users");
		
		assertThat(
				updateQ,
				is(updateQuantity));
		
	}
	
	
	

}
