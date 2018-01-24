package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.ChannelUserMap.UserChannelId;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.cima.persistence.beans.Users;

public class UserChannelDAOTest {
	
	private UserChannelDAO mockUserChannelDAO;
	private ChannelUserMap.UserChannelId mockUserChannelId;
	private ChannelUserMap mockChannelUserMap;
	private Session mockSession;
	private Criteria mockCriteria;
	private Criterion mockCriterion;
	
	@Before
	public void init() {
		mockUserChannelDAO = Mockito.spy(new UserChannelDAO());
		mockUserChannelId = Mockito.mock(ChannelUserMap.UserChannelId.class, Mockito.RETURNS_DEEP_STUBS);
		mockChannelUserMap = Mockito.mock(ChannelUserMap.class,Mockito.RETURNS_DEEP_STUBS);
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriterion = Mockito.mock(Criterion.class, Mockito.RETURNS_DEEP_STUBS);
	}

	
	@Test
	public void testPopulateMap() {
		
		Users users = getUsers();
		Channels channels = getChannels();
		
		//Stub
		Mockito
		       .doReturn(mockUserChannelId)
		       .when(mockUserChannelDAO)
		       .getUserChannelId(Mockito.any(Users.class), Mockito.any(Channels.class));
		
		Mockito
		       .doReturn(mockChannelUserMap)
		       .when(mockUserChannelDAO)
		       .getChannelUserMap(Mockito.any(ChannelUserMap.UserChannelId.class), Mockito.anyString());
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockUserChannelDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(ChannelUserMap.class)))
		       .thenReturn(mockSession);
		
		mockUserChannelDAO.populateMap(users, channels, subscription);
		
		Mockito.verify(mockUserChannelDAO, VerificationModeFactory.times(1))
					.getUserChannelId(Mockito.any(Users.class), Mockito.any(Channels.class));
		
		Mockito.verify(mockUserChannelDAO, VerificationModeFactory.times(1))
					.getChannelUserMap(Mockito.any(ChannelUserMap.UserChannelId.class), Mockito.anyString());
		
		Mockito.verify(mockUserChannelDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(ChannelUserMap.class));
	}
	
	
	@Test
	public void testFindChannelSubscriptionByUserId() {
		
		List<ChannelUserMap> channelUserList = new ArrayList<ChannelUserMap>();
		ChannelUserMap chMap = new ChannelUserMap();
		UserChannelId Id = new UserChannelId();
		Id.setChannel(getChannels());
		Id.setUser(getUsers());
		chMap.setPrimaryKey(Id);
		chMap.setSubscription(subscription);
		channelUserList.add(chMap);
		
		Mockito
			   .doReturn(mockSession)
			   .when(mockUserChannelDAO)
			   .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(ChannelUserMap.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(channelUserList);
		
		Map<String, String> res = mockUserChannelDAO.findChannelSubscriptionByUserId(getUsers());
		
		assertThat(
				res, notNullValue());
		
		assertThat(
				res.get(name), is(subscription));
		
	}
	
	
	
	private Users getUsers() {
		Users user = new Users();
		
		user.setCategory(category);
		user.setEnvironment(environment);
		user.setLoginStatus(loginStatus);
		user.setMovieRating(movieRating);
		user.setTvRating(tvRating);
		
		return user;
	}
	
	private Channels getChannels() {
		Channels channel = new Channels();
		
		channel.setChannelId(channelId);
		channel.setContentType(contentType);
		channel.setDescription(description);
		channel.setName(name);
		channel.setStationId(stationId);
		
		return channel;
	}
	
	private String category = "mockCategory";
	private String environment = "QA";
	private String loginStatus = "A";
	private String movieRating = "NR";
	private String tvRating = "TV-G";
	
	private String channelId = "123";
	private String contentType = "mockContentType";
	private String description = "mockDescription";
	private String name = "mockName";
	private String stationId = "1";
	
	private String subscription = "Triple";
}
