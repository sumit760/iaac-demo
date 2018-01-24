package com.comcast.test.citf.common.cima.persistence.beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Bean class for asset rating.
 * 
 * @author arej001c
 */
@Entity
@Table(name="asset_rating")
public class AssetRatings {
	
	/**
	 * Default Constructor
	 */
	public AssetRatings(){
		
	}

	/**
	 * Class constructor specifying id and priority
	 * @param id
	 * 			Rating Id
	 * @param priority
	 * 			Rating priority
	 */
	public AssetRatings(RatingId id, int priority){
		this.id = id;
		this.priority = priority;
	}
	
	@EmbeddedId
    private RatingId id;
	
	@Column(name = "priority", nullable = false)
	private int priority;

	/**
	 * Returns id of AssetRatings
	 * @see com.comcast.test.citf.common.cima.persistence.beans.AssetRatings.RatingId
	 */
	public RatingId getId() {
		return id;
	}

	/**
	 * Sets id of AssetRatings
	 * @param id
	 * 			Rating Id
	 * @see com.comcast.test.citf.common.cima.persistence.beans.AssetRatings.RatingId
	 */
	public void setId(RatingId id) {
		this.id = id;
	}

	/**
	 * Returns priority of AssetRatings
	 * @return priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets priority of AssetRatings
	 * @param priority
	 * 			Rating priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Embeddable
	public static class RatingId implements Serializable{
		
		/**
		 * Default constructor
		 */
		public RatingId(){
			
		}
		
		/**
		 * Class constructor specifying String name, String type
		 * @param name
		 * 			Rating name
		 * @param type
		 * 			Rating type
		 */
		public RatingId(String name, String type){
			this.name = name;
			this.type = type;
		}

		@Column(name = "rating_name", length = 25)
	    private String name;
		
		@Column(name = "rating_type", length = 10)
	    private String type;

		/**
		 * Returns name of RatingId
		 * @return name
		 */ 
		public String getName() {
			return name;
		}

		/**
		 * Sets name of RatingId
		 * @param name
		 * 			Rating name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Returns type of RatingId
		 * @return type
		 */
		public String getType() {
			return type;
		}

		/**
         * Sets type of RatingId
         * @param type
         * 			Rating type
         */
		public void setType(String type) {
			this.type = type;
		}
	}
}