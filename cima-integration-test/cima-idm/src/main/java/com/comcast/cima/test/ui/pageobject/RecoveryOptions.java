package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Recovery option" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class RecoveryOptions extends SeleniumPageObject<RecoveryOptions> {
	
	public RecoveryOptions(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		//Assert.assertTrue(isElementPresent(headerMessage));
	}

}
