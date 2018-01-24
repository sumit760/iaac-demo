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
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings;

public class RatingDAOTest {
	
	private RatingDAO mockRatingDAO;
	private AssetRatings.RatingId mockRatingId;
	private AssetRatings mockAssetRatings;
	private Session mockSession;
	private Criteria mockCriteria;
	private Criterion mockCriterion;
	
	@Before
	public void init() {
		mockRatingDAO = Mockito.spy(new RatingDAO());
		mockRatingId = Mockito.mock(AssetRatings.RatingId.class, Mockito.RETURNS_DEEP_STUBS);
		mockAssetRatings = Mockito.mock(AssetRatings.class, Mockito.RETURNS_DEEP_STUBS);
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriterion = Mockito.mock(Criterion.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	
	@Test
	public void testPopulateAssetRating() {
		
		String ratingName = "mockRating";
		String ratingType = "mockRatingType";
		int priority = 1;
		
		//Stub
		Mockito
		       .doReturn(mockRatingId)
		       .when(mockRatingDAO)
		       .getRatingId(Mockito.anyString(), Mockito.anyString());
		
		Mockito
		       .doReturn(mockAssetRatings)
		       .when(mockRatingDAO)
		       .getAssetRatings(Mockito.any(AssetRatings.RatingId.class), Mockito.anyInt());
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockRatingDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(AssetRatings.class)))
		       .thenReturn(mockSession);
		
		mockRatingDAO.populateAssetRating(ratingName, ratingType, priority);
		
		Mockito.verify(mockRatingDAO, VerificationModeFactory.times(1)).getRatingId(Mockito.anyString(), Mockito.anyString());
		Mockito.verify(mockRatingDAO, VerificationModeFactory.times(1)).getAssetRatings(Mockito.any(AssetRatings.RatingId.class), Mockito.anyInt());
		Mockito.verify(mockRatingDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(AssetRatings.class));
	}
	
	
	@Test
	public void testFindAllRatings() {
		
		List<AssetRatings> assetRatingList = new ArrayList<AssetRatings>();
		AssetRatings assetRatings = getMockAssetRatings();
		assetRatingList.add(assetRatings);
		
		//Stub
		Mockito
	       .doReturn(mockSession)
	       .when(mockRatingDAO)
	       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(AssetRatings.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(assetRatingList);
		
		List<AssetRatings> actualAssetRatings = mockRatingDAO.findAllRatings();
		
		assertThat(
				"Expected valid response",
				actualAssetRatings, notNullValue());
		
		assertThat(
				"Expected valid response",
				actualAssetRatings.get(0), notNullValue());
		
		assertThat(actualAssetRatings.get(0).getPriority(), is(priority));
		assertThat(actualAssetRatings.get(0).getId().getName(), is(ratingName));
		assertThat(actualAssetRatings.get(0).getId().getType(), is(ratingType));
		
	}
	
	
	@Test
	public void testFindRatingByName() {
		
		AssetRatings.RatingId ratingId = getMockRatingId();
		
		AssetRatings assetRating = getMockAssetRatings();
		
		//Stub
		Mockito
	       .doReturn(mockSession)
	       .when(mockRatingDAO)
	       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(AssetRatings.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .doReturn(ratingId)
		       .when(mockRatingDAO)
		       .getRatingId(Mockito.anyString(), Mockito.anyString());
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(assetRating);
		
		AssetRatings ar = mockRatingDAO.findRatingByName(ratingName, ratingType);
		
		assertThat(
				"Expected valid response",
				ar, notNullValue());
		
		assertThat(ar.getId().getName(), is(ratingName));
		assertThat(ar.getId().getType(), is(ratingType));
		assertThat(ar.getPriority(), is(priority));
		
	}
	
	
	@Test
	public void testFindRatingByPriority() {
		
		AssetRatings assetRating = getMockAssetRatings();
		
		//Stub
		Mockito
	       .doReturn(mockSession)
	       .when(mockRatingDAO)
	       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(AssetRatings.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
	       .when(mockCriteria.add(Mockito.any(Criterion.class)))
	       .thenReturn(mockCriteria);
	
		Mockito
	       .when(mockCriteria.uniqueResult())
	       .thenReturn(assetRating);
		
		AssetRatings ar = mockRatingDAO.findRatingByPriority(priority, ratingType);
		
		assertThat(
				"Expected valid response",
				ar, notNullValue());
		
		assertThat(ar.getId().getName(), is(ratingName));
		assertThat(ar.getId().getType(), is(ratingType));
		assertThat(ar.getPriority(), is(priority));
		
	}
	
	
	private AssetRatings getMockAssetRatings() {
		
		AssetRatings assetRatings = new AssetRatings();
		assetRatings.setPriority(priority);
		AssetRatings.RatingId ratingId = new AssetRatings.RatingId();
		ratingId.setName(ratingName);
		ratingId.setType(ratingType);
		assetRatings.setId(ratingId);
		
		return assetRatings;
	}
	
	
	private AssetRatings.RatingId getMockRatingId() {
		
		AssetRatings.RatingId ratingId = new AssetRatings.RatingId();
		ratingId.setName(ratingName);
		ratingId.setType(ratingType);
		
		return ratingId;
	}
	
	private int priority = 1;
	private String ratingName = "movieRating";
	private String ratingType = "Movies";
	
	

}
