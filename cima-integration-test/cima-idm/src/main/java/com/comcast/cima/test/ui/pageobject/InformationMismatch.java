package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Information Mismatch" web page.
 * 
 * @author Sumit Pal
 *
 */

public class InformationMismatch extends SeleniumPageObject<InformationMismatch> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public InformationMismatch(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	/**
	 * To get header message of information mismatched page
	 * 
	 * @return Text of header of information mismatched page
	 */
	public String getHeaderMessage() {
		if (isElementPresent(this.headerMessage)) {
			return this.headerMessage.getText();
		}
		return null;
	}
}
