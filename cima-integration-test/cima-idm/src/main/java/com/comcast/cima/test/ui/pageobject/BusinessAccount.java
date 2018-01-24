package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;


/**
 * This is Selenium Page Object for Username Lookup Confirmation web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class BusinessAccount extends SeleniumPageObject<BusinessAccount> {

	/* Header Message */
	@FindBy (how = How.ID, using = "fallback")
	private WebElement imResidentialCustomer;
	
	public BusinessAccount(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(imResidentialCustomer), "Failed to land on Business account page");
	}
	
	/**
	 * Checks whether greeting Header of create user page is present
	 * 
	 * @return True if present, else False
	 */
	
	public boolean isImResedentailCustomerLinkPresent() {
		return isElementPresent(imResidentialCustomer);
	}
	
	
	


}
