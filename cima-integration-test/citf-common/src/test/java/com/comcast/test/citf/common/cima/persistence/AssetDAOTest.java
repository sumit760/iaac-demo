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

import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings;
import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings.RatingId;
import com.comcast.test.citf.common.cima.persistence.beans.Assets;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants.AssetRatingTypes;

public class AssetDAOTest {
	
	private AssetDAO mockAssetDAO;
	private Session mockSession;
	private Criteria mockCriteria;
	private RatingDAO mockRatingDB;
	
	@Before
	public void init() {
		mockAssetDAO = Mockito.spy(new AssetDAO());
		mockSession = Mockito.mock(Session.class,Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockRatingDB = Mockito.mock(RatingDAO.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	@Test
	public void testPopulateAsset() {
		
		Channels channels = getMockChannels();
		String assetName = "mockAssetName";
		String tvRating = "TV-G";
		String movieRating = "R";
		String seriesName = "mockSeriesName";
		String seriesId = "mockSeriesId";
		String programId = "mockProgramId";
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockAssetDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(Assets.class)))
		       .thenReturn(mockSession);
		
		mockAssetDAO.populateAsset(assetName, 
								   channels, 
								   tvRating, 
								   movieRating, 
								   seriesName, 
								   seriesId, 
								   programId);
		
		Mockito.verify(mockAssetDAO, VerificationModeFactory.times(1)).getAssets(Mockito.anyString(), 
														Mockito.any(Channels.class), 
														Mockito.anyString(), 
														Mockito.anyString(), 
														Mockito.anyString(), 
														Mockito.anyString(), 
														Mockito.anyString());
		
		Mockito.verify(mockAssetDAO, VerificationModeFactory.times(1)).getSession();
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(Assets.class));
	}
	
	
	@Test
	public void testFindAssetByRatingName() {
		
		List<Assets> mockAssets = new ArrayList<Assets>();
		mockAssets.add(getMockAssets());
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockAssetDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Assets.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(mockAssets);
		
		Assets asset = mockAssetDAO.findAssetByRatingName(AssetRatingTypes.MOVIE_RATING, "NR", "123");
		
		assertThat(
				"Expected valid response",
				asset, notNullValue());
		
		assertThat(asset.getChannelId().getChannelId(), is("123"));
		assertThat(asset.getChannelId().getContentType(), is("mockContentType"));
		assertThat(asset.getChannelId().getDescription(), is("mockDescription"));
		assertThat(asset.getChannelId().getName(), is("mockName"));
		assertThat(asset.getChannelId().getStationId(), is("mockStationId"));
		assertThat(asset.getChannelId().getStatus(), is("mockStatus"));
		assertThat(asset.getChannelId().getStrategy(), is("mockStrategy"));
		assertThat(asset.getMovieRating(), is("NR"));
		assertThat(asset.getName(), is("MockChName"));
		assertThat(asset.getProgramId(), is("mockPrgId"));
		assertThat(asset.getSeriesId(), is("mockSeriesId"));
		assertThat(asset.getSeriesName(), is("mockSeriesName"));
		assertThat(asset.getStatus(), is("mockStatus"));
		assertThat(asset.getTvRating(), is("TV-G"));
	}
	
	
	@Test
	public void testFindAssetByRatingPriority() {
		
		String ratingName = "mockRating";
		String ratingType = "mockRatingType";		
		int ratingPriority = 1;
		
		AssetRatings assetRating = new AssetRatings();
		RatingId rId = new RatingId();
		rId.setName(ratingName);
		rId.setType(ratingType);
		assetRating.setId(rId);
		assetRating.setPriority(ratingPriority);
		
		Assets assets = getMockAssets();
		
		List<Assets> mockAssets = new ArrayList<Assets>();
		mockAssets.add(getMockAssets());
		
		//Stub
		Mockito
		       .doReturn(mockRatingDB)
		       .when(mockAssetDAO)
		       .getRatingDAO();
		
		Mockito
		       .when(mockRatingDB.findRatingByName(Mockito.anyString(), Mockito.anyString()))
		       .thenReturn(assetRating);
		
		Mockito
		       .when(mockRatingDB.findRatingByPriority(Mockito.anyInt(), Mockito.anyString()))
		       .thenReturn(assetRating);
		
		Mockito
		       .doReturn(assets)
		       .when(mockAssetDAO)
		       .findAssetByRatingName(AssetRatingTypes.MOVIE_RATING, "NR", "123");
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockAssetDAO)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Assets.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(mockAssets);
		
		Assets asst = mockAssetDAO.findAssetByRatingPriority(AssetRatingTypes.MOVIE_RATING, "NR", 0, false, "123");
		
		assertThat(
				"Expected valid response",
				asst, notNullValue());
		
		assertThat(asst.getChannelId().getChannelId(), is("123"));
		assertThat(asst.getChannelId().getContentType(), is("mockContentType"));
		assertThat(asst.getChannelId().getDescription(), is("mockDescription"));
		assertThat(asst.getChannelId().getName(), is("mockName"));
		assertThat(asst.getChannelId().getStationId(), is("mockStationId"));
		assertThat(asst.getChannelId().getStatus(), is("mockStatus"));
		assertThat(asst.getChannelId().getStrategy(), is("mockStrategy"));
		assertThat(asst.getMovieRating(), is("NR"));
		assertThat(asst.getName(), is("MockChName"));
		assertThat(asst.getProgramId(), is("mockPrgId"));
		assertThat(asst.getSeriesId(), is("mockSeriesId"));
		assertThat(asst.getSeriesName(), is("mockSeriesName"));
		assertThat(asst.getStatus(), is("mockStatus"));
		assertThat(asst.getTvRating(), is("TV-G"));
		
	}
	
	
	private Assets getMockAssets() {
		
		String movieRating = "NR";
		String name = "MockChName";
		String programId = "mockPrgId";
		String seriesId = "mockSeriesId";
		String seriesName = "mockSeriesName";
		String status = "mockStatus";
		String tvRating = "TV-G";
		
		Assets asset = new Assets();
		
		asset.setChannelId(getMockChannels());
		asset.setMovieRating(movieRating);
		asset.setName(name);
		asset.setProgramId(programId);
		asset.setSeriesId(seriesId);
		asset.setSeriesName(seriesName);
		asset.setStatus(status);
		asset.setTvRating(tvRating);
		
		return asset;
	}
	
	private Channels getMockChannels() {
		
		String channelId = "123";
		String contentType = "mockContentType";
		String description = "mockDescription";
		String name = "mockName";
		String stationId = "mockStationId";
		String status = "mockStatus";
		String strategy = "mockStrategy";
		
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

}
