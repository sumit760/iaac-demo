package com.comcast.test.citf.common.cima.persistence.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bean class for fresh users to be used in Facebook association.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="fresh_users")
public class FreshUsers {
	
	/**
	 * Default constructor
	 */
	public FreshUsers(){
		
	}
	
	/**
	 * Constructor to initialize fresh user.
	 * 
	 * @param primaryKey
	 * 					User Id as primary key of DB table.
	 * @param alternativeEmail
	 * 					User alternative email other than Comcast.
	 * @param alterEmailPassword
	 * 					Alternative email password.
	 * @param fbId
	 * 					Facebook Id of the user.
	 * @param fbPassword
	 * 					Facebook pasword.
	 * @param lastUserId
	 * 					Last created user Id.
	 * @param lockStatus
	 * 					The lock status of the user. This determines whether the user can be
	 * 					used in tests.
	 */
	public FreshUsers(int primaryKey, String alternativeEmail, String alterEmailPassword, String fbId, String fbPassword, String lastUserId, String lockStatus){
		this.primaryKey = primaryKey;
		this.alternativeEmail = alternativeEmail;
		this.alterEmailPassword = alterEmailPassword;
		this.fbId = fbId;
		this.fbPassword = fbPassword;
		this.lastUserId = lastUserId;
		this.lockStatus = lockStatus;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "sys_id", unique = true, nullable = false)
    private int primaryKey;
	
	@Column(name = "alternate_email", nullable = true, length = 150)
    private String alternativeEmail;
	
	@Column(name = "alternate_email_password", nullable = true, length = 20)
    private String alterEmailPassword;
	
	@Column(name = "facebook_id", nullable = true, length = 100)
    private String fbId;
	
	@Column(name = "facebook_password", nullable = true, length = 20)
    private String fbPassword;
	
	@Column(name = "last_generated_user_id", nullable = true, length = 25)
    private String lastUserId;
	
	@Column(name = "last_modified_on", nullable = true)
    private Timestamp lastModifiedOn;
	
	@Column(name = "lock_status", nullable = false, length = 1)
    private String lockStatus;

	/**
	 * Returns primaryKey i.e. userId of the fresh user table. 
	 * 
	 * @return User Id
	 */
	public int getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets primaryKey.
	 * 
	 * @param primaryKey The user Id.
	 */
	public void setPrimaryKey(int primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Returns alternativeEmail of the user.
	 * 
	 * @return Alternative email of user.
	 */
	public String getAlternativeEmail() {
		return alternativeEmail;
	}

	/**
	 * Sets alternativeEmail of user.
	 * 
	 * @param alternativeEmail Alternative email to set.
	 */
	public void setAlternativeEmail(String alternativeEmail) {
		this.alternativeEmail = alternativeEmail;
	}

	/**
	 * Returns alternate email password of user.
	 * 
	 * @return Alternate email password.
	 */
	public String getAlterEmailPassword() {
		return alterEmailPassword;
	}

	/**
	 * Sets alternate email password of user.
	 * 
	 * @param alterEmailPassword Alternate email password to set.
	 */
	public void setAlterEmailPassword(String alterEmailPassword) {
		this.alterEmailPassword = alterEmailPassword;
	}
	
	/**
	 * Returns facebook Id of user.
	 * 
	 * @return Facebook Id of user.
	 */
	public String getFbId() {
		return fbId;
	}

	/**
	 * Sets facebook Id of user.
	 * 
	 * @param fbId Facebook Id to set.
	 */
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	/**
	 * Returns facebook password of the user.
	 * 
	 * @return The facebook password.
	 */
	public String getFbPassword() {
		return fbPassword;
	}

	/**
	 * Sets facebook password of the user.
	 * 
	 * @param fbPassword The facebook password to set.
	 */
	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}

	/**
	 * Returns last created userId.
	 * 
	 * @return Last created userId.
	 */
	public String getLastUserId() {
		return lastUserId;
	}

	/**
	 * Sets last created userId.
	 * 
	 * @param lastUserId Last created userId to set.
	 */
	public void setLastUserId(String lastUserId) {
		this.lastUserId = lastUserId;
	}

	/**
	 * Returns last modification time stamp of the user.
	 * 
	 * @return The last user modification time stamp.
	 */
	public Timestamp getLastModifiedOn() {
		return lastModifiedOn;
	}

	/**
	 * Sets modification time stamp of the user.
	 * 
	 * @param lastModifiedOn Modification time stamp of the user to set.
	 */
	public void setLastModifiedOn(Timestamp lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

	/**
	 * Returns lockStatus of the user.
	 * 
	 * @return Lock status.
	 */
	public String getLockStatus() {
		return lockStatus;
	}

	/**
	 * Sets lockStatus of the user.
	 * 
	 * @param lockStatus Lock status to set.
	 */
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
}