package com.comcast.test.citf.common.cima.persistence.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bean class for browser capabilities. The class holds different
 * platform browser combinations along with their version.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="browser_capabilities")
public class BrowserCapabilities {
	
	/**
	 * Default constructor
	 */
	public BrowserCapabilities(){
		
	}

	@Id
	@Column(name="cap_key", unique = true, nullable = false, length = 100)
	private String id;
	
	@Column(name="type", nullable=false, length = 15)
	private String type;
	
	@Column(name="platform", nullable=false, length = 20)
	private String platformName;
	
	@Column(name="platform_version", nullable=true)
	private double platformVersion;
	
	@Column(name="browser", nullable=true, length = 20)
	private String browserName;
	
	@Column(name="browser_version", nullable=true)
	private double browserVersion;
	
	@Column(name="device", nullable=true, length = 100)
	private String deviceName;
	
	@Column(name="device_orientattion", nullable=true, length = 20)
	private String deviceOrientattion;
	
	@Column(name="status", nullable=false, length = 15)
	private String status;

	
	/**
	 * Returns id.
	 * 
	 * @return The Id.
	 */
	public String getId() {
		return id;
	}

	/**
     * Sets id.
     * 
     * @param id Id to set.
     */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns browser type.
	 * 
	 * @return Browser type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets browser type.
	 * 
	 * @param type Browser type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns browser platform name.
	 *  
	 * @return Platform name of browser.
	 */
	public String getPlatformName() {
		return platformName;
	}

	/**
	 * Sets platformName of browser.
	 * 
	 * @param platformName Platform name to set.
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	/**
	 * Returns platformVersion of the browser.
	 * 
	 * @return Platform version of the browser.
	 */
	public double getPlatformVersion() {
		return platformVersion;
	}

	/**
	 * Sets platformVersion of the browser.
	 * 
	 * @param platformVersion Platform version to set.
	 */
	public void setPlatformVersion(double platformVersion) {
		this.platformVersion = platformVersion;
	}

	/**
	 * Returns browserName.
	 * 
	 * @return Browser name.
	 */
	public String getBrowserName() {
		return browserName;
	}

	/**
	 * Sets browserName.
	 * 
	 * @param browserName Browser name to set.
	 */
	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	/**
	 * Returns browserVersion. 
	 * 
	 * @return The browser version.
	 */
	public double getBrowserVersion() {
		return browserVersion;
	}

	/**
	 * Sets browserVersion.
	 * 
	 * @param browserVersion The browser version to set.
	 */
	public void setBrowserVersion(double browserVersion) {
		this.browserVersion = browserVersion;
	}

	/**
	 * Returns deviceName.
	 * 
	 * @return Device name that supports the browser.
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * Sets deviceName.
	 * 
	 * @param deviceName Device name to set.
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * Returns deviceOrientattion.
	 *  
	 * @return Device orientation of the device.
	 */
	public String getDeviceOrientattion() {
		return deviceOrientattion;
	}

	/**
	 * Sets deviceOrientattion.
	 * 
	 * @param deviceOrientattion Sets device orientation of the device.
	 */
	public void setDeviceOrientattion(String deviceOrientattion) {
		this.deviceOrientattion = deviceOrientattion;
	}

	/**
	 * Returns status.
	 * 
	 * @return Browser status (Active/Inactive)
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status.
	 * 
	 * @param status Sets browser status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}