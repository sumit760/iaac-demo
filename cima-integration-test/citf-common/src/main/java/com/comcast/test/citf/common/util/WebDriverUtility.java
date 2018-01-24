package com.comcast.test.citf.common.util;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Utility class for Web driver profile generation.
 * 
 * @author Jimmy Xu
 *
 */

public class WebDriverUtility {
	
	public WebDriverUtility() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Generate profile for Firefox web driver in Spanish 
	 * @return FirefoxProfile
	 */
	public static Object getFireFoxBrowserToSpanish() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages","es");
		return profile;
	}
	
	/**
	 * Generate options for Chrome web driver in Spanish 
	 * @return ChromeOptions
	 */
	public static Object getChromeBrowserToSpanish() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--lang=es");
		return options;
	}
	
}
