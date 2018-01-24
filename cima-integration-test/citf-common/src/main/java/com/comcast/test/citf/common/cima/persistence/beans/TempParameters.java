package com.comcast.test.citf.common.cima.persistence.beans;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bean class for temporary parameters to be used in test execution. This class is out dated now
 * and will be deprecated shortly.
 * 
 * @author arej001c, spal004c
 *
 */
@Entity
@Table(name="temp_parameters")
public class TempParameters {

	@Id
	@Column(name = "param_key", unique = true, nullable = false, length = 50)
    private String key;
	
	@Column(name = "param_value", nullable = true, length = 500)
    private String value;
	
	@Column(name = "additional_val1", nullable = true, length = 200)
    private String addVal1;
	
	@Column(name = "additional_val2", nullable = true, length = 200)
    private String addVal2;
	
	@Column(name = "last_modified_on", nullable = false)
    private Timestamp modifiedOn;
	
	@Column(name = "last_modified_by", nullable = true, length = 20)
    private String modifiedBy;
	
	@Column(name = "status", nullable = false, length = 10)
    private String status;
	
	/**
	 * Deafult constructor
	 */
	public TempParameters(){
		
	}
	
	/**
	 * Constructor for TempParameters.
	 * 
	 * @param key
	 * 			Key 
	 * @param value
	 * 			Primary value
	 * @param addVal1
	 * 			Additional value 1
	 * @param addVal2
	 * 			Additional value 2
	 * @param modifiedOn
	 * 			Last modification timestamp
	 * @param modifiedBy
	 * 			Last modified by
	 * @param status
	 * 			Status
	 */
	public TempParameters(String key, String value, String addVal1, String addVal2, Timestamp modifiedOn, String modifiedBy, String status){
		this.key = key;
		this.value = value;
		this.addVal1 = addVal1;
		this.addVal2 = addVal2;
		this.modifiedOn = modifiedOn;
		this.modifiedBy = modifiedBy;
		this.status = status;
	}

	/**
	 * Returns key
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets key
	 * @param key
	 * 			Key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns value
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets value
	 * @param value
	 * 			Value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Returns additional value 1
	 * @return addVal1
	 */
	public String getAddVal1() {
		return addVal1;
	}

	/**
	 * Sets additional value 1
	 * @param addVal1
	 * 			Additional value 1
	 */
	public void setAddVal1(String addVal1) {
		this.addVal1 = addVal1;
	}

	/**
	 * Returns additional value 2
	 * @return addVal2
	 */
	public String getAddVal2() {
		return addVal2;
	}

	/**
	 * Sets additional value 2
	 * @param addVal2
	 * 			Additional value 2
	 */
	public void setAddVal2(String addVal2) {
		this.addVal2 = addVal2;
	}

	/**
	 * Returns status
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	 /**
     * Sets status
     * @param status
     * 			Status
     */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns Timestamp
	 * @return Timestamp
	 */
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * Sets Timestamp
	 * @param modifiedOn
	 * 			Last modification timestamp
	 */
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * Returns modifiedBy
	 * @return modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/** Sets modifiedBy
	 * 
	 * @param modifiedBy
	 * 			Last modified by
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
}