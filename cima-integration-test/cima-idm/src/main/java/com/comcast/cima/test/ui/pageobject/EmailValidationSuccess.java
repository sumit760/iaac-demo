package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Email Validation success" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class EmailValidationSuccess extends SeleniumPageObject<EmailValidationSuccess> {
	
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement congratulations;
	
	@FindBy (how = How.XPATH, using = "//main/p[1]")
	private WebElement validationMessage;

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.validationMessage));
	}
	
	public EmailValidationSuccess(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To checks whether email is validated 
	 * 
	 * @return true if email id validated, else False.
	 */
	
	public boolean isEmailValidated() {
		return isElementPresent(this.congratulations) && this.congratulations.getText().contains("Congratulations") &&
			   isElementPresent(this.validationMessage) && this.validationMessage.getText().contains("Your alternate email address has been successfully validated");
				
	}

}
