package com.comcast.test.citf.common.cima.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings;
import com.comcast.test.citf.common.cima.persistence.beans.Assets;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * DAO class for CRACr assets. The asset could be movies, TV shows etc. 
 * This class is accountable for all the asset related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@Repository("assetDao")
public class AssetDAO extends AbstractDAO{
	

	/**
	 * Persists the asset details in database. This method is used to load asset data into the 
	 * database.
	 * 
	 * @param assetName The name of the asset.
	 * @param channelId The channel Id of the asset.
	 * @param tvRating The TV Rating of the asset.
	 * @param movieRating The movie rating of the asset.
	 *                    The asset will be having either a tv rating
	 *                    or a movie rating. The other parameter will
	 *                    be passed as null that time.
	 * @param seriesName The name of the series if asset is of type
	 *                   tv series.
	 * @param seriesId The series Id.
	 * @param programId The program Id.
	 */
	@Transactional
	public void populateAsset(String assetName, Channels channelId, String tvRating, String movieRating, String seriesName, String seriesId, String programId){
		Assets asset = getAssets(assetName, channelId, tvRating, movieRating, seriesName, seriesId, programId);
		getSession().merge(asset);
    }
	
	/**
	 * Gets the asset details by rating.
	 * 
	 * @param type Asset Rating types, i.e. tv rating or movie rating.
	 * @param rating The rating string.
	 * @param channelId The channel to look for the asset.
	 * 
	 * @return The details of the asset.
	 */
	@Transactional(readOnly=true)
	public Assets findAssetByRatingName(ICimaCommonConstants.AssetRatingTypes type, String rating, String channelId){
		Criteria criteria = getSession().createCriteria(Assets.class);
	    criteria.add(Restrictions.eq(ICimaCommonConstants.AssetRatingTypes.MOVIE_RATING.equals(type)? ASSET_RATING_COLUMN_FOR_MOVIE : ASSET_RATING_COLUMN_FOR_TV, rating));
	    criteria.add(Restrictions.eq("channelId.channelId", channelId));
	    List<Assets> result = criteria.list();
	    
	    return (result!=null && result.get(0)!=null)? result.get(0) : null;
    }
	
	/**
	 * Gets the asset details by rating priority. The method is used to retrieve all the assets with
	 * rating either lower than or greater than the current rating provided.  
	 * <p>
	 * deltaPriority controls how lower or how higher the rating would be in searching for assets. 
	 * For example: if the current rating is TV-G and deltaPriority is 1 along with 'isPriorityHigher' true 
	 * then it'll look for all the assets with rating TV-PG or if deltaPriority is 1 along with 'isPriorityHigher' false  
	 * then it'll look for all the assets with rating TV-FV.
	 * <p>
	 * Similar logic applies for movie assets as well.
	 *  
	 * @param type The asset rating type.
	 * @param currentRating The current rating.
	 * @param deltaPriority The delta priority, could be positive and negative.
	 * @param isPriorityHigher The boolean flag to determine the direction of search.
	 * @param channelId The channel Id to look under.
	 * 
	 * @return The asset object.
	 */
	@Transactional(readOnly=true)
	public Assets findAssetByRatingPriority(ICimaCommonConstants.AssetRatingTypes type, String currentRating, int deltaPriority, boolean isPriorityHigher, String channelId) throws IllegalStateException{
		String ratingType = ICimaCommonConstants.AssetRatingTypes.MOVIE_RATING.equals(type)? RatingDAO.RATING_TYPE_MOVIE : RatingDAO.RATING_TYPE_TV;
		
		ratingDB = getRatingDAO();
		AssetRatings curRating = ratingDB.findRatingByName(currentRating, ratingType);
		if(curRating==null){
			throw new IllegalStateException("Invalid rating value in currentRating!");
		}
		
		int ratingPriority = isPriorityHigher? curRating.getPriority()-Math.abs(deltaPriority) : curRating.getPriority()+Math.abs(deltaPriority);
		if(ratingPriority<0){
			throw new IllegalStateException("Invalid delta rating priority["+deltaPriority+"] and isPriorityHigher["+isPriorityHigher+"] combination. The result priority is coming -ve!");
		}
		
		AssetRatings targetRating = ratingDB.findRatingByPriority(ratingPriority, ratingType);
		if(targetRating==null){
			LOGGER.warn("Invalid delta rating priority["+deltaPriority+"] and isPriorityHigher["+isPriorityHigher+"] combination. There is no such rating!");
			return null;
		}
		
		return findAssetByRatingName(type, targetRating.getId().getName(), channelId);
    }
	
	private static final String ASSET_RATING_COLUMN_FOR_TV = "tvRating";
	private static final String ASSET_RATING_COLUMN_FOR_MOVIE = "movieRating";
	
	@Autowired
	private RatingDAO ratingDB;
	
	protected Assets getAssets(String assetName, Channels channelId, String tvRating, String movieRating, String seriesName, String seriesId, String programId) {
		return new Assets(assetName, channelId, tvRating, movieRating, seriesName, seriesId, programId, ICimaCommonConstants.DB_STATUS_ACTIVE);
	}
	
	protected RatingDAO getRatingDAO() {
		return new RatingDAO();
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetDAO.class);
}
