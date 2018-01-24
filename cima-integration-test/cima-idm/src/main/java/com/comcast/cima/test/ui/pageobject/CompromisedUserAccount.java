package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Compromised User Account" web page.
 * 
 * @author Sumit Pal
 *
 */

public class CompromisedUserAccount extends SeleniumPageObject<CompromisedUserAccount> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//div[@id='bd']/p[@id='reset-password']/a[@class='button']")
	private WebElement resetPasswordButton;

	public CompromisedUserAccount(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
	}

	/**
	 * Provides Selenium Page Object for Reset Password page.
	 * 
	 * @return Page Object for My Account Change Password page
	 * @throws Exception
	 */	
	public Object clickResetPasswordButton() throws Exception {
		this.resetPasswordButton.click();
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
	}
}
