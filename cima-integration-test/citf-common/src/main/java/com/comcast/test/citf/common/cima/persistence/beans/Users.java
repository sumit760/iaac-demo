package com.comcast.test.citf.common.cima.persistence.beans;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * Bean class for users. The class holds user specific data.
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="users")
public class Users {

	/**
	 * Default constructor
	 */
	public Users(){

	}

	/**
	 * Constructor for Users. 
	 * 
	 * @param userId
	 * 				User Id.
	 * @param password
	 * 				User password.
	 * @param category
	 * 				User category.
	 * @param environment
	 * 				User environment.
	 * @param loginStatus
	 * 				User login status.
	 */
	public Users(	String userId,
					String password,
					String category, 
					String environment, 
					String loginStatus){
		this.userId = userId;
		this.password = password;
		this.category = category;
		this.environment = environment;
		this.loginStatus = loginStatus;
	}

	/**
	 * Overloaded constructor for Users.
	 * 
	 * @param userId
	 * 				User Id.
	 * @param password
	 * 				User password.
	 * @param category
	 * 				User category.
	 * @param pin
	 * 				User PIN.
	 * @param tvRating
	 * 				User parental control TV rating.
	 * @param movieRating
	 * 				User parental control movie rating.
	 * @param environment
	 * 				User environment.
	 */
	public Users(		String userId,
						String password,
						String category, 
						String pin, 
						String tvRating, 
						String movieRating, 
						String environment){
		this.userId = userId;
		this.password = password;
		this.category = category;
		this.pin = pin;
		this.tvRating = tvRating;
		this.movieRating = movieRating;
		this.environment = environment;
		this.loginStatus = ICommonConstants.DB_STATUS_ACTIVE;
	}

	/**
	 * Overloaded constructor for Users.
	 * 
	 * @param userId
	 * 				User Id.
	 * @param password
	 * 				User password.
	 * @param category
	 * 				User category.
	 * @param pin
	 * 				User PIN.
	 * @param tvRating
	 * 				User parental control TV rating.
	 * @param movieRating
	 * 				User parental control movie rating.
	 * @param environment
	 * 				User environment.
	 * @param loginStatus
	 * 				User login status.
	 */
	public Users(		String userId,
						String password,
						String category, 
						String pin, 
						String tvRating, 
						String movieRating, 
						String environment, 
						String loginStatus){
		this.userId = userId;
		this.password = password;
		this.category = category;
		this.pin = pin;
		this.tvRating = tvRating;
		this.movieRating = movieRating;
		this.environment = environment;
		this.loginStatus = loginStatus;
	}

	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 25)
    private String userId;

	@Column(name = "password", nullable = false, length = 25)
	private String password;

	@Column(name = "category", nullable = true, length = 15)
	private String category;

	@Column(name = "pin", nullable = true, length = 5)
	private String pin;

	@Column(name = "tv_rating", nullable = true, length = 50)
	private String tvRating;

	@Column(name = "movie_rating", nullable = true, length = 50)
	private String movieRating;

	@Column(name = "environment", nullable = false, length = 10)
	private String environment;

	@Column(name = "login_status", nullable = false, length = 15)
	private String loginStatus;

	@OneToMany(mappedBy = "primaryKey.user")
	private Collection<ChannelUserMap> channelMap;

	@OneToMany(mappedBy = "uaMapPrimaryKey.user")
	private Collection<AccountUserMap> accoutMap;


	/**
	 * Returns userId.
	 * 
	 * @return userId.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets userId.
	 * 
	 * @param userId 
	 * 			UserId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Returns user password.
	 * 
	 * @return User password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets user password.
	 * 
	 * @param password 
	 * 			User password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns user category.
	 * 
	 * @return User category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets user category.
	 * 
	 * @param category 
	 * 			User category to set.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Returns environment.
	 * 
	 * @return User environment.
	 */
	public String getEnvironment() {
		return environment;
	}

	/**
	 * Sets user environment.
	 * 
	 * @param environment 
	 * 			Execution environment to set.
	 */
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	/**
	 * Returns user loginStatus.
	 * 
	 * @return User login status.
	 */
	public String getLoginStatus() {
		return loginStatus;
	}

	/**
	 * Sets user loginStatus.
	 * 
	 * @param loginStatus 
	 * 			User loginStatus to set.
	 */
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * Returns PIN associated with user.
	 * 
	 * @return User PIN.
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * Sets user pin.
	 * 
	 * @param pin 
	 * 			User PIN to set.
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * Returns User parental control tvRating.
	 * 
	 * @return User parental control tvRating
	 */
	public String getTvRating() {
		return tvRating;
	}

	/**
	 * Sets User parental control tvRating.
	 * 
	 * @param tvRating 
	 * 			User parental control tvRating to set.
	 */
	public void setTvRating(String tvRating) {
		this.tvRating = tvRating;
	}

	/**
	 * Returns User parental control movieRating.
	 * 
	 * @return User parental control movieRating.
	 */
	public String getMovieRating() {
		return movieRating;
	}

	/**
	 * Sets User parental control movieRating.
	 *  
	 * @param movieRating 
	 * 			User parental control movieRating to set.
	 */
	public void setMovieRating(String movieRating) {
		this.movieRating = movieRating;
	}

	/**
	 * Returns List of user channel Map.
	 * 
	 * @return Collection of user channel Map.
	 */
	public Collection<ChannelUserMap> getChannelMap() {
		return channelMap;
	}

	/**
	 * Sets List of user channel Map.
	 * 
	 * @param channelMap 
	 * 			Collection of user channel Map
	 */
	public void setChannelMap(Collection<ChannelUserMap> channelMap) {
		this.channelMap = channelMap;
	}

	/**
	 * Returns Collection of user account Map.
	 * 
	 * @return Collection of user account Map.
	 */
	public Collection<AccountUserMap> getAccoutMap() {
		return accoutMap;
	}

	/**
	 * Sets Collection of user account Map.
	 * 
	 * @param accoutMap 
	 * 			Collection of user account Map to set.
	 */
	public void setAccoutMap(Collection<AccountUserMap> accoutMap) {
		this.accoutMap = accoutMap;
	}

	@Override
    public int hashCode() {
		return Objects.hash(userId, password, environment, loginStatus);
    }

    @Override
    public boolean equals(Object obj) {
	    if (obj instanceof Users) {
		    Users user = (Users)obj;
		    return Objects.equals(this.getUserId(), user.getUserId()) &&
				    Objects.equals(this.getEnvironment(), user.getEnvironment());
	    }

       return false;
    }

    @Override
    public String toString() {
	    return MoreObjects
			    .toStringHelper(this)
			    .add("userId",userId)
			    .add("environment",environment)
			    .add("login status",loginStatus)
			    .add("password", password!=null?"******":"null")
			    .add("pin",pin)
			    .add("TV Rating",tvRating)
			    .add("Movie Rating",movieRating)
	            .toString();
    }

    /********************* Transient stuff *************************/

    
    @Transient
    private Set<String> primaryAccountIds = null;

    @Transient
    private Set<String> secondaryAccountIds = null;

    /**
     * Returns primaryAccountIds
     * @return primaryAccountIds
     */
    @Transient
	public Set<String> getPrimaryAccountIds() {
		return primaryAccountIds;
	}

    /**
     * Sets primaryAccountIds
     * @param primaryAccountIds
     * 			Primary account ids
     */
    @Transient
	public void setPrimaryAccountIds(Set<String> primaryAccountIds) {
		this.primaryAccountIds = primaryAccountIds;
	}

    /**
     * Returns secondaryAccountIds
     * @return secondaryAccountIds
     */
    @Transient
	public Set<String> getSecondaryAccountIds() {
		return secondaryAccountIds;
	}

    /**
     * Sets secondaryAccountIds
     * @param secondaryAccountIds
     * 			Secondary account ids
     */
    @Transient
	public void setSecondaryAccountIds(Set<String> secondaryAccountIds) {
		this.secondaryAccountIds = secondaryAccountIds;
	}

}