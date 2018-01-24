package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

public class Welcome extends SeleniumPageObject<Welcome> {
	
	/*Element details*/
	@FindBy(how = How.XPATH, using = "//form/div[2]")
	private WebElement greeting;

	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.greeting), "Failed to land on authentication user page");
	}

	@Override
	protected void load() {
		
	}

	public Welcome(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public boolean isUserAuthenticated() {
		if (isElementPresent(this.greeting)) {
			return true;
		} else 
		{
			return false;
		}
	}

	private static Logger LOGGER = LoggerFactory.getLogger(Welcome.class);

}
