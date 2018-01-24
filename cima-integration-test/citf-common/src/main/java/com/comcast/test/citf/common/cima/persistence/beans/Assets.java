package com.comcast.test.citf.common.cima.persistence.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Bean class for Assets.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="assets")
public class Assets {
	
	/**
	 * Default constructor
	 */
	public Assets(){
		
	}
	
	/**
	 * Constructor specifying asset name, channelId, TV rating, Movie rating, asset seriesName, 
	 * asset seriesId, asset programId and asset status
	 *
	 * @param name 
	 * 				Name of the asset.
	 * @param channelId
	 * 				Name of the channel.
	 * @param tvRating
	 * 				TV rating of the asset if the asset is of type TV series.
	 * @param movieRating
	 * 				Movie rating if the asset is of type movie.
	 * @param seriesName
	 * 				Name of the asset series.
	 * @param seriesId
	 * 				ID of the series.
	 * @param programId
	 * 				ID of the program.
	 * @param status
	 * 				Asset status.
	 */
	public Assets(String name, Channels channelId, String tvRating, String movieRating, String seriesName, String seriesId, String programId, String status){
		this.name = name;
		this.channelId = channelId;
		this.tvRating = tvRating;
		this.movieRating = movieRating;
		this.seriesName = seriesName;
		this.seriesId = seriesId;
		this.programId = programId;
		this.status = status;
	}

	@Id
	@Column(name="name", unique = true, nullable = false, length = 250)
	private String name;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "channel_id", nullable = true)
	private Channels channelId;
	
	@Column(name="tv_rating", nullable=true, length = 25)
	private String tvRating;
	
	@Column(name = "movie_rating", nullable = true, length = 25)
	private String movieRating;
	
	@Column(name="series_name", nullable=true, length = 200)
	private String seriesName;
	
	@Column(name="series_id", nullable=true, length = 20)
	private String seriesId;
	
	@Column(name="program_id", nullable=true, length = 25)
	private String programId;
	
	@Column(name="status", nullable=false, length = 10)
	private String status;
	
	
	/**
	  * Returns name of Assets
	  * 
	  * @return Asset name.
	  */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of Assets
	 * 
	 * @param name Asset name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * Returns channel Id associated with the asset.
     * 
     * @return Channel Id.
     */
	public Channels getChannelId() {
		return channelId;
	}

	/**
	  * Sets Channel Id of Assets.
	  * 
	  * @param channelId Channel Id to set.
	  */
	public void setChannelId(Channels channelId) {
		this.channelId = channelId;
	}

	/**
	 * Returns tvRating of Assets.
	 * 
	 * @return TV rating of the asset.
	 */
	public String getTvRating() {
		return tvRating;
	}

	/**
	 * Sets tvRating of Assets.
	 * 
	 * @param tvRating TV Rating of the asset to set.
	 */
	public void setTvRating(String tvRating) {
		this.tvRating = tvRating;
	}

	/**
	 * Returns movieRating of Assets.
	 * 
	 * @return Movie rating if the asset.
	 */
	public String getMovieRating() {
		return movieRating;
	}

	/**
	 * Sets movieRating of Assets.
	 * 
	 * @param movieRating Movie rating to set.
	 */
	public void setMovieRating(String movieRating) {
		this.movieRating = movieRating;
	}

	/**
	 * Returns seriesName of Assets.
	 * 
	 * @return Series name of the asset.
	 */
	public String getSeriesName() {
		return seriesName;
	}

	/**
	 * Sets seriesName of Assets.
	 * 
	 * @param seriesName Series name to set.
	 */
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	/**
	 * Returns seriesId of Assets.
	 * 
	 * @return Series Id of the asset.
	 */
	public String getSeriesId() {
		return seriesId;
	}

	/**
	 * Sets seriesId of Assets.
	 * 
	 * @param seriesId Series Id to set.
	 */
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	/**
	 * Returns programId of Assets.
	 * 
	 * @return Program Id of assets.
	 */
	public String getProgramId() {
		return programId;
	}

	/**
	 * Sets programId of Assets.
	 * 
	 * @param programId Program Id to set.
	 */
	public void setProgramId(String programId) {
		this.programId = programId;
	}

	/**
	 * Returns status of Assets.
	 * 
	 * @return Asset status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status of Assets.
	 *  
	 * @param status Set asset status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
}