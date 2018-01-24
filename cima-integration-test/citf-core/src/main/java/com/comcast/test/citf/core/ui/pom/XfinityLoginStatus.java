package com.comcast.test.citf.core.ui.pom;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is Selenium Page Object for "Xfinity Login Status" web page.
 *   Prod Page (April 2016) - https://login.comcast.net/api/usersessioninfo
 */
public class XfinityLoginStatus extends SeleniumPageObject<XfinityLoginStatus> {

	private static final Logger LOGGER = LoggerFactory.getLogger(XfinityLoginStatus.class);

	private boolean loggedIn;
	private boolean persistentSessionToken;

	public XfinityLoginStatus(WebDriver inDriver) {
		this.driver = inDriver;
		PageFactory.initElements(inDriver, this);
	}

	public boolean isLoggedIn() {
		return this.loggedIn;
	}
	public boolean isPersistentSessionTokenPresent() {
		return this.persistentSessionToken;
	}

	@Override
	protected void load() {
		String url = null;
		try {
			url = getURLToLoad();
			this.driver.get(url);
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading \"Xfinity Login Status\" web using URL (" + url + "). ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		try {
			ObjectMapper om = new ObjectMapper();
			String pageSource = this.driver.getPageSource();
			if (pageSource.indexOf('{') != -1) {
				pageSource = pageSource.substring(pageSource.indexOf('{'));
			} else {
				throw new IllegalStateException(
						"No starting '{' found in page source - " + pageSource);
			}
			if (pageSource.lastIndexOf('}') != -1) {
				pageSource = pageSource.substring(0, pageSource.indexOf('}') + 1);
			} else {
				throw new IllegalStateException(
						"No ending '}' found in page source - " + this.driver.getPageSource());
			}
			Map<String, Object> map = om.readValue(
					pageSource, new TypeReference<Map<String, String>>(){});
			Assert.assertTrue(map.containsKey("isLoggedIn"));
			Assert.assertTrue(map.containsKey("hasPersistentSessionToken"));
			this.loggedIn = Boolean.valueOf(map.get("isLoggedIn") + "");
			this.persistentSessionToken =
					Boolean.valueOf(map.get("hasPersistentSessionToken") + "");
		} catch (Exception e) {
			Assert.fail("'Xfinity Login Status' page didn't have correct content", e);
		}
	}

	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(
				EndPoinUrlProvider.LoginUrlPropKeys.XFINITY_LOGIN_STATUS.getValue(),
				ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
}
