package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Emerson College Login" web page.
 */
public class EmersonCollegeLogin extends SeleniumPageObject<EmersonCollegeLogin> {

	@FindBy(how = How.ID, using = "username")
	private WebElement usernameTextBox;

	@FindBy(how = How.ID, using = "password")
	private WebElement passwordTextBox;

	public EmersonCollegeLogin(WebDriver inDriver) {
		this.driver = inDriver;
		PageFactory.initElements(inDriver, this);
	}

	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.equals("Emerson College Login"));
		Assert.assertTrue(isElementPresent(this.usernameTextBox));
		Assert.assertTrue(isElementPresent(this.passwordTextBox));
	}

	public void setUsernameTextBox(String inValue) {
		this.usernameTextBox.sendKeys(inValue);
	}
	public void setPasswordTextBoxThenPressEnter(String inValue) {
		this.passwordTextBox.sendKeys(inValue);
		this.passwordTextBox.sendKeys(org.openqa.selenium.Keys.ENTER);
	}
}
