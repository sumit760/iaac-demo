package com.comcast.test.citf.common.cima.persistence.beans;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bean class for SSN data. This class is used to keep SSN specific data
 * of user.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="fresh_ssn_info")
public class FreshSSN {
	
	/**
	 * Default constructor
	 */
	public FreshSSN(){
		
	}
	
	/**
	 * Constructor to initialize SSN object.
	 * 
	 * @param ssn
	 * 			  The SSN.
	 * @param dob
	 * 		      Date of birth associated with the SSN.
	 * @param creationDate
	 * 			  Date of creation of the SSN. This is needed to auto-increment
	 * 			  the SSN month.
	 */
	public FreshSSN(String ssn, Date dob, Date creationDate){
		this.ssn = ssn;
		this.dob = dob;
		this.creationDate = creationDate;
	}

	
	@Id
	@Column(name = "ssn", unique = true, nullable = false, length = 15)
	private String ssn;
	
	@Column(name = "dob", nullable = false)
	private Date dob;

	@Column(name = "ssn_creation_date", nullable = false)
	private Date creationDate;
	
	/**
	 * Returns SSN of the user.
	 * 
	 * @return SSN associated with the user.
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
	  * Returns creationDate of the SSN.
	  * 
	  * @return SSN creation date.
	  */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets creationDate of the SSN.
	 * 
	 * @param creationDate SSN creation date.
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns date of birth of the user associated with the SSN.
	 * 
	 * @return Date of birth of the user associated with the SSN.
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets date of birth of the user associated with the SSN.
	 * 
	 * @param dob Date of birth of the user associated with the SSN.
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}
}