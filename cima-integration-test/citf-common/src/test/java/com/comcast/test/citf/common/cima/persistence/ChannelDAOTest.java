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

import com.comcast.test.citf.common.cima.persistence.beans.Channels;

public class ChannelDAOTest {
	
	private ChannelDAO mockChannelDAO;
	private Channels mockChannel;
	private Session mockSession;
	private Criteria mockCriteria;

	
	@Before
	public void init() {
		mockChannelDAO = Mockito.spy(new ChannelDAO());
		mockChannel = Mockito.mock(Channels.class, Mockito.RETURNS_DEEP_STUBS);
		mockSession = Mockito.mock(Session.class,Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testFindCapability() {
		
		//Stub
		Mockito
			  .doReturn(mockChannel)
			  .when(mockChannelDAO)
			  .getChannels(Mockito.anyString(), 
						   Mockito.anyString(), 
						   Mockito.anyString(), 
						   Mockito.anyString(), 
						   Mockito.anyString(), 
						   Mockito.anyString(), 
						   Mockito.anyString());
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockChannelDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(Class.class)))
		       .thenReturn(mockSession);
		
		mockChannelDAO.populateChannel(channelId, name, description, stationId, contentType, strategy);
		
		Mockito.verify(mockChannelDAO, VerificationModeFactory.times(1)).getChannels(Mockito.anyString(), Mockito.anyString(), 
												Mockito.anyString(), 
												Mockito.anyString(), 
												Mockito.anyString(), 
												Mockito.anyString(), 
												Mockito.anyString());
		
		Mockito.verify(mockChannelDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(Class.class));
	}
	
	
	@Test
	public void testFindChannelByName() {
		
		Channels channels = getMockChannels();
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockChannelDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Channels.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(channels);
		
		Channels ch = mockChannelDAO.findChannelByName(name);
		
		assertThat(
				"Expected valid response",
				ch, notNullValue());
		
		assertThat(ch.getChannelId(), is(channelId));
		assertThat(ch.getName(), is(name));
		assertThat(ch.getDescription(), is(description));
		assertThat(ch.getStationId(), is(stationId));
		assertThat(ch.getContentType(), is(contentType));
		assertThat(ch.getStatus(), is(status));
		assertThat(ch.getStrategy(), is(strategy));
	}
	
	
	
	@Test
	public void testFindAllChannels() {
		
		List<Channels> channelList = new ArrayList<Channels>();
		Channels channel = getMockChannels();
		channelList.add(channel);

		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockChannelDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Channels.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
			   .when(mockCriteria.list())
			   .thenReturn(channelList);
		
		List<Channels> actualChannelList = mockChannelDAO.findAllChannels();
		
		Channels actualChannel = actualChannelList.get(0);

		assertThat(
				actualChannelList, notNullValue());
		
		assertThat(
				actualChannelList.size(), is(1));
		
		assertThat(
				actualChannel, notNullValue());
		
		assertThat(actualChannel.getChannelId(), is(channelId));
		assertThat(actualChannel.getName(), is(name));
		assertThat(actualChannel.getDescription(), is(description));
		assertThat(actualChannel.getStationId(), is(stationId));
		assertThat(actualChannel.getContentType(), is(contentType));
		assertThat(actualChannel.getStatus(), is(status));
		assertThat(actualChannel.getStrategy(), is(strategy));

	}
	
	
	
	private Channels getMockChannels() {
		
		Channels channels = new Channels();
		
		channels.setChannelId(channelId);
		channels.setContentType(contentType);
		channels.setDescription(description);
		channels.setName(name);
		channels.setStationId(stationId);
		channels.setStatus(status);
		channels.setStrategy(strategy);
		
		return channels;
	}
	
	
	private final String channelId = "123";
	private final String name = "mockChannelName";
	private final String description = "mockDescription";
	private final String stationId = "mockStationId";
	private final String contentType = "mockContentType";
	private final String status = "mockStatus";
	private final String strategy = "mockStrategy";
	

}
