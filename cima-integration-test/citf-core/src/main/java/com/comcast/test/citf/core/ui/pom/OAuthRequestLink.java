package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for OAuth Request page.
 * 
 * @author Sumit Pal
 *
 */
public class OAuthRequestLink extends SeleniumPageObject<OAuthRequestLink> {

	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
	}
	
	public OAuthRequestLink(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Provides SignInToXfinity page
	 * 
	 * @param url
	 * 			OAuth Auth Token request URL 
	 * @return Page Object for SignInToXfinity page
	 * 
	 * @see com.comcast.test.citf.core.ui.pom.SignInToXfinity
	 */
	public Object open(String url) {
		if (url != null) {
			this.driver.get(url);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
}
