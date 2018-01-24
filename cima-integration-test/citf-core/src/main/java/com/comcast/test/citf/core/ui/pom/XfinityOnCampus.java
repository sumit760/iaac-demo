package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Xfinity TV Partner Integration Layer" web page.
 */
public class XfinityOnCampus extends SeleniumPageObject<XfinityOnCampus> {

	private static final Logger LOGGER = LoggerFactory.getLogger(XfinityOnCampus.class);

	private String targetUrl = null;

	@FindBy(how = How.ID, using = "picker-input")
	private WebElement pickerInputTextBox;

	public XfinityOnCampus(String inTargetUrl, WebDriver inDriver) {
		this.targetUrl = inTargetUrl;
		this.driver = inDriver;
		PageFactory.initElements(inDriver, this);
	}

	@Override
	protected void load() {
		String url = null;
		try {
			url = getURLToLoad();
			this.driver.get(url);
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading \"Xfinity On Campus\" web using URL (" + url + "). ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.equals("XFINITY On Campus"));
		Assert.assertTrue(isElementPresent(this.pickerInputTextBox));
	}

	public void setPickerInputTextBoxThenPressEnter(String inValue) {
		this.pickerInputTextBox.sendKeys(inValue);
		this.pickerInputTextBox.sendKeys(org.openqa.selenium.Keys.ENTER);
	}

	private String getURLToLoad() {
		return this.targetUrl;
	}
}
