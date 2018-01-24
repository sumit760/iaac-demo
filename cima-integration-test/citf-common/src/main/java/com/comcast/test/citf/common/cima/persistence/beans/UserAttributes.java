package com.comcast.test.citf.common.cima.persistence.beans;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Bean class for user attributes. 
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="user_attributes")
public class UserAttributes {

	/**
	 * Default constructor
	 */
	public UserAttributes(){
		
	}
	
	/**
	 * Constructor to initialize UserAttributes.
	 * 
	 * @param guid
	 * 				User guid (csrCustGuid)
	 * @param user
	 * 				User object.
	 * @param email
	 * 				User email.
	 * @param alterEmail
	 * 				User alternate email.
	 * @param alterEmailPassword
	 * 				User alternate email password.
	 * @param secretQuestion
	 * 				User secret question.
	 * @param secretAnswer
	 * 				User secret answer.
	 * @param fbId
	 * 				User facebook Id.
	 * @param fbPassword
	 * 				User facebook password.
	 * @param dob
	 * 				User date of birth.
	 * @param ssn
	 * 				User SSN.
	 */
	public UserAttributes(	String guid,
							Users user,
							String email,
							String alterEmail,
							String alterEmailPassword,
							String secretQuestion,
							String secretAnswer,
							String fbId,
							String fbPassword,
							Date dob,
							String ssn){
		
		this.guid = guid;
		this.user = user;
		this.email = email;
		this.alterEmail = alterEmail;
		this.alterEmailPassword = alterEmailPassword;
		this.secretQuestion = secretQuestion;
		this.secretAnswer = secretAnswer;
		this.fbId = fbId;
		this.fbPassword = fbPassword;
		this.dob = dob;
		this.ssn = ssn;
	}
	
	@Id
	@Column(name = "guid", unique = true, nullable = false, length = 100)
    private String guid;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", nullable = false)
	private Users user;
	
	
	@Column(name = "email", nullable = true, length = 150)
	private String email;
	
	@Column(name = "alternate_email", nullable = true, length = 150)
	private String alterEmail;
	
	@Column(name = "alternate_email_password", nullable = true, length = 20)
	private String alterEmailPassword;
	
	@Column(name = "secret_question", nullable = true, length = 500)
	private String secretQuestion;
	
	@Column(name = "secret_answer", nullable = true, length = 200)
	private String secretAnswer;

	@Column(name = "facebook_id", nullable = true, length = 100)
	private String fbId;
	
	@Column(name = "dob", nullable = true)
	private Date dob;
	
	@Column(name = "ssn", nullable = true, length = 15)
	private String ssn;
	
	@Column(name = "facebook_password", nullable = true, length = 20)
	private String fbPassword;

	/**
	 * Returns user cstCustGuid.
	 * 
	 * @return User cstCustGuid.
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Sets user cstCustGuid.
	 * 
	 * @param guid User cstCustGuid to set.
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * Returns Users.
	 * 
	 * @return User object.
	 */
	public Users getUser() {
		return user;
	}

	/**
	 * Sets Users.
	 * 
	 * @param user User to set.
	 */
	public void setUser(Users user) {
		this.user = user;
	}

	/**
	 * Returns user email.
	 * 
	 * @return Email of user.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email of the user.
	 * 
	 * @param email Email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns secretQuestion of the user.
	 * 
	 * @return Secret Question.
	 */
	public String getSecretQuestion() {
		return secretQuestion;
	}

	/**
	 * Sets secretQuestion of the user.
	 * 
	 * @param secretQuestion 
	 * 			Secret Question to set.
	 */
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	/**
	 * Returns secretAnswer of the user.
	 * 
	 * @return secretAnswer to set.
	 */
	public String getSecretAnswer() {
		return secretAnswer;
	}

	/**
	 * Sets secretAnswer of the user.
	 * 
	 * @param secretAnswer Secret answer to set.
	 */
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	/**
	 * Returns alternate email of the user.
	 * 
	 * @return The alternate email.
	 */
	public String getAlterEmail() {
		return alterEmail;
	}

	/**
	 * Sets alternate email of the user.
	 * 
	 * @param alterEmail The alternate email to set.
	 */
	public void setAlterEmail(String alterEmail) {
		this.alterEmail = alterEmail;
	}

	/**
	 * Returns alternate email password of the user.
	 * 
	 * @return The alternate email password.
	 */
	public String getAlterEmailPassword() {
		return alterEmailPassword;
	}

	/**
	 * Sets alternate email password of the user.
	 * 
	 * @param alterEmailPassword The alternate email password to set.
	 */
	public void setAlterEmailPassword(String alterEmailPassword) {
		this.alterEmailPassword = alterEmailPassword;
	}

	/**
	 * Returns Facebook Id of the user.
	 * 
	 * @return The Facebook Id of the user.
	 */
	public String getFbId() {
		return fbId;
	}

	/**
	 * Sets Facebook Id of the user.
	 * 
	 * @param fbId Facebook Id of the user to set.
	 */
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	
	/**
	 * Returns Date of birth of the user.
	 * 
	 * @return Date of birth.
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets Date of birth of the user.
	 * 
	 * @param dob Date of birth to set.
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Returns SSN of the user.
	 * 
	 * @return SSN.
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * Sets SSN of the user.
	 * 
	 * @param ssn SSN to set.
	 */
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	/**
	 * Returns Facebook password of user.
	 * 
	 * @return Facebook password.
	 */
	public String getFbPassword() {
		return fbPassword;
	}

	/**
	 * Sets Facebook password of user.
	 * 
	 * @param fbPassword Facebook password to set.
	 */
	public void setFbPassword(String fbPassword) {
		this.fbPassword = fbPassword;
	}
}