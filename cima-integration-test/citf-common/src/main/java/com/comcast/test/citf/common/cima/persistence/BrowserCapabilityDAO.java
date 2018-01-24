package com.comcast.test.citf.common.cima.persistence;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.BrowserCapabilities;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * DAO class for Browser Capability. This class is accountable for
 * all the browser related operation in the database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 *
 */

@Repository("browserCapabilityDAO")
public class BrowserCapabilityDAO extends AbstractDAO{
	
	/**
	 * Enumeration for browser types.
	 * 
	 * @author spal004c
	 *
	 */
	public enum Types{
		COMPUTER(TYPE_COMPUTER),
		MOBILE(TYPE_MOBILE);
		
		private final String value;
        Types(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Enumeration for browser platforms where they operate.
	 * 
	 * @author spal004c
	 *
	 */
	public enum Platforms{
		WINDOWS("Windows"),
		LINUX("Linux"),
		OSX("OS X"),
		OSX10_10("OS X 10.10");
		
		private final String value;
        Platforms(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	
	/**
	 * Gets all the browsers versions under a platform, type, browser and platform version.
	 * 
	 * @param platform The browser platform.
	 * @param type The browser type.
	 * @param browser The name of the browser.
	 * @param platformVersion The platform version.
	 * 
	 * @return DesiredCapabilities which contains browser version information.
	 *
	 * @see Platforms, Types, DesiredCapabilities
	 */
	@Transactional(readOnly=true)
	public DesiredCapabilities findCapability(Platforms platform, Types type, String browser, double platformVersion, Object profileDtls){
		Criteria criteria = getSession().createCriteria(BrowserCapabilities.class)
        								.add(Restrictions.eq("status", ICommonConstants.DB_STATUS_ACTIVE))
        								.add(Restrictions.eq("platformName", platform.getValue()))
        								.add(Restrictions.eq("browserName", browser))
        								.add(Restrictions.eq("platformVersion", platformVersion))
        								.addOrder(Order.desc("browserVersion"))
        								.setMaxResults(1);
        
        Object res = criteria.uniqueResult();
        if(res!=null){
        	return this.prepareBrowserCapability((BrowserCapabilities)res, profileDtls);
        }
		return null;
    }
	
	
	/**
	 * Gets all the browsers versions under a platform, type and browser.
	 * 
	 * @param platform The browser platform.
	 * @param type The browser type.
	 * @param browser The name of the browser.
	 * 
	 * @return DesiredCapabilities which contains browser version information.
	 * @see Platforms, Types, DesiredCapabilities
	 */
	@Transactional(readOnly=true)
	public DesiredCapabilities findCapability(Platforms platform, Types type, String browser, Object profileDtls) {
		Criteria criteria = getSession().createCriteria(BrowserCapabilities.class)
        								.add(Restrictions.eq("status", ICommonConstants.DB_STATUS_ACTIVE))
        								.add(Restrictions.eq("platformName", platform.getValue()))
        								.add(Restrictions.eq("browserName", browser))
        								.addOrder(Order.desc("browserVersion"))
        								.addOrder(Order.desc("platformVersion"))
        								.setMaxResults(1);
        
        Object res = criteria.uniqueResult();
        if(res!=null){
        	LOGGER.info("Browser capability found from database");
        	return this.prepareBrowserCapability((BrowserCapabilities)res, profileDtls);
        }
		return null;
    }
	

	/**
	 * Gets all the browser capabilities under a platform and type.
	 * 
	 * @param platform The browser platform.
	 * @param type The platform type.
	 * 
	 * @return The list of browser capabilities.
	 */
	@Transactional(readOnly=true)
	public List<DesiredCapabilities> findAllCapabilities(Platforms platform, Types type, Object profileDtls) {
		Criteria criteria = getSession().createCriteria(BrowserCapabilities.class)
        								.add(Restrictions.eq("status", ICommonConstants.DB_STATUS_ACTIVE))
        								.add(Restrictions.eq("platformName", platform.getValue()))
        								.add(Restrictions.eq("type", type.getValue()));
        return prepareCapList((List<BrowserCapabilities>) criteria.list(), profileDtls);
    }
	
	
	/**
	 * Gets all the browser capabilities under a type.
	 * 
	 * @param type The browser type.
	 * 
	 * @return The list of browser capabilities.
	 */
	@Transactional(readOnly=true)
	public List<DesiredCapabilities> findAllCapabilities(Types type, Object profileDtls){
		Criteria criteria = getSession().createCriteria(BrowserCapabilities.class)
        								.add(Restrictions.eq("status", ICommonConstants.DB_STATUS_ACTIVE))
        								.add(Restrictions.eq("type", type.getValue()));
        return prepareCapList((List<BrowserCapabilities>) criteria.list(), profileDtls);
    }
	
	
	
	/************************** Private variables & methods **********************************/
	
	private List<DesiredCapabilities> prepareCapList(List<BrowserCapabilities> dbTabList, Object profileDtls){
		List<DesiredCapabilities> capList = null;
		
		if(dbTabList!=null){
        	for(BrowserCapabilities tab : dbTabList){
        		DesiredCapabilities caps = this.prepareBrowserCapability(tab, profileDtls);
        		
        		if(caps!=null){
        			if(capList==null){
        				capList = new ArrayList<DesiredCapabilities>();
        			}
        			capList.add(caps);
        		}
        	}
        }
		return capList;
	}
	
	private DesiredCapabilities prepareBrowserCapability(BrowserCapabilities tab, Object profileDtls){
		
		DesiredCapabilities caps = null;
		
		if(tab!=null){
			switch(tab.getType()){
				case TYPE_COMPUTER:
					if(BROWSER_NAME_CHROME.equals(tab.getBrowserName())){
						caps = DesiredCapabilities.chrome();
						if(profileDtls instanceof ChromeOptions){
							caps.setCapability(ChromeOptions.CAPABILITY, profileDtls);
						}
					}
					else if(BROWSER_NAME_FIREFOX.equals(tab.getBrowserName())){
						caps = DesiredCapabilities.firefox();
						if(profileDtls instanceof FirefoxProfile ){
							caps.setCapability(FirefoxDriver.PROFILE, profileDtls);
						}
					}
					else if(BROWSER_NAME_INTERNET_EXPLORER.equals(tab.getBrowserName())){
						caps = DesiredCapabilities.internetExplorer();
					}else if(BROWSER_NAME_SAFARI.equals(tab.getBrowserName())){
						caps = DesiredCapabilities.safari();
					}else if(BROWSER_NAME_OPERA.equals(tab.getBrowserName())){
						caps = DesiredCapabilities.operaBlink();
					}else {
						LOGGER.warn(new MessageFormat("Browser name [{1}] is invalid for desktop capability. Will ignore it while preparing result list!").format(
								new String[] {tab.getBrowserName()}));
						return null;
					}
					
					String platformVersion = tab.getPlatformVersion()>0 ? String.valueOf(tab.getPlatformVersion()) : null;
					if(platformVersion!=null && ".0".equals(platformVersion.substring(platformVersion.length()-2))){
						platformVersion = platformVersion.substring(0, platformVersion.length()-2);
					}
					caps.setCapability("platform", platformVersion!=null ? tab.getPlatformName()+ICommonConstants.BLANK_SPACE_STRING+platformVersion : String.valueOf(tab.getPlatformName()));
					caps.setCapability("version", String.valueOf(tab.getBrowserVersion()));
					break;
					
				case TYPE_MOBILE:
					break;
			}
		}
		return caps;
	}
	
	public static final String TYPE_COMPUTER = "COMPUTER";
	public static final String TYPE_MOBILE = "MOBILE";

	public static final String BROWSER_NAME_CHROME = "Chrome";
	public static final String BROWSER_NAME_FIREFOX = "FF";
	public static final String BROWSER_NAME_INTERNET_EXPLORER = "IE";
	public static final String BROWSER_NAME_OPERA = "Opera";
	public static final String BROWSER_NAME_SAFARI = "Safari";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BrowserCapabilityDAO.class);
}
