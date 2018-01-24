package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Email Confirmation" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class EmailConfirmationLink extends SeleniumPageObject<EmailConfirmationLink> {
	
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;

	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	public EmailConfirmationLink(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To launch email confirmation page
	 * 
	 * @param url  :
	 *              URL of email confirmation page
	 * @return Get Next page Object
	 * @throws Exception
	 */
	
	public Object open(String url) throws Exception {
		if (url != null) {
			this.driver.get(url);
			waitForElementByXPATH("//input[@id='password']", ICommonConstants.WAIT_TIMEOUT);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To verify alternate email address
	 * 
	 * @param url :
	 *            URL
	 * @return Next page selenium object based on page navigation
	 * @throws Exception
	 */
	
	public Object verify(String url) {
		if (url != null) {
			this.driver.get(url);
			waitForTextPresent("//main/p[1]", "Your alternate email address has been successfully validated", ICommonConstants.WAIT_TIMEOUT);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}

}
