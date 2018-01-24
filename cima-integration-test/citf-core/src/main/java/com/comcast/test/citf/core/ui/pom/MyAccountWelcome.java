package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for My Account Welcome web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class MyAccountWelcome extends SeleniumPageObject<MyAccountWelcome> {

	@FindBy (how = How.XPATH, using = "//div[@id='tos-main-wrapper']/div/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "main_1_accepttos")
	private WebElement acceptButton;
	
	@FindBy (how = How.ID, using = "Ecobillverify")
	private WebElement cancelButton;
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public MyAccountWelcome(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Clicks on accept button on My Account Welcome page and returns next page object
	 * 
	 * @return Page object for next page
	 */
	public Object accept() {
		if (isElementPresent(acceptButton)){
			this.acceptButton.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
}
