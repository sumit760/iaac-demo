package com.comcast.test.citf.common.cima.persistence;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.AssetRatings;
import com.comcast.test.citf.common.orm.AbstractDAO;

/**
 * DAO class for asset rating. This class is accountable for
 * all the asset rating operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@Repository("ratingDao")
public class RatingDAO extends AbstractDAO{

	/**
	 * Loads the asset rating into the database.
	 * 
	 * @param ratingName The rating name.
	 * @param ratingType The rating type.
	 * @param priority The priority of the rating.
	 */
	@Transactional
	public void populateAssetRating(String ratingName, String ratingType, int priority){
		
		AssetRatings.RatingId id = getRatingId(ratingName, ratingType);
		AssetRatings rating = getAssetRatings(id, priority);
		getSession().merge(rating);
    }
	
	/**
	 * Gets all the ratings.
	 * 
	 * @return List of rating objects.
	 */
	@Transactional(readOnly=true)
	public List<AssetRatings> findAllRatings(){
        Criteria criteria = getSession().createCriteria(AssetRatings.class);
        return (List<AssetRatings>) criteria.list();
    }
	
	/**
	 * Gets the rating by rating name.
	 * 
	 * @param name Name of the rating.
	 * @param ratingType Type of the rating.
	 * 
	 * @return The asset rating object.
	 */
	@Transactional(readOnly=true)
	public AssetRatings findRatingByName(String name, String ratingType){
        Criteria criteria = getSession().createCriteria(AssetRatings.class);
        AssetRatings.RatingId id = getRatingId(name, ratingType);
        criteria.add(Restrictions.eq("id",id));
        Object result = criteria.uniqueResult();   
        return result!=null?(AssetRatings)result:null ;
    }
	
	/**
	 * Gets the rating by priority.
	 * 
	 * @param priority The rating priority.
	 * @param ratingType The type of the rating.
	 * 
	 * @return The asset rating object.
	 */
	@Transactional(readOnly=true)
	public AssetRatings findRatingByPriority(int priority, String ratingType){
        Criteria criteria = getSession().createCriteria(AssetRatings.class);
        criteria.add(Restrictions.eq("id.type",ratingType));
        criteria.add(Restrictions.eq("priority",priority));
        Object result = criteria.uniqueResult();   
        return result!=null?(AssetRatings)result:null ;
    }
	
	
	protected AssetRatings.RatingId getRatingId(String ratingName, String ratingType) {
		
		return new AssetRatings.RatingId(ratingName, ratingType);
	}
	
	protected AssetRatings getAssetRatings(AssetRatings.RatingId id, int priority) {
		
		return new AssetRatings(id, priority);
	}
	
	public static final String RATING_TYPE_TV = "TV";
	public static final String RATING_TYPE_MOVIE = "MOVIE";
}
