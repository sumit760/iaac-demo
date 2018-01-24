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
 * This is Selenium Page Object for "Reset Password By SMS" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class ResetPasswordBySMS extends SeleniumPageObject<ResetPasswordBySMS> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	@FindBy (how = How.ID, using = "fallback_option")
	private WebElement dontHaveAccesstoPhone;
	
	public ResetPasswordBySMS(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
		
	/**
	 * To get reset password method page.
	 * 
	 * @return page object of ResetPasswordMethods/ResetPasswordBySQA
	 */
	public Object noPhoneAccess() {
		if (isElementPresent(dontHaveAccesstoPhone)) {
			this.dontHaveAccesstoPhone.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click Continue
	 *  
	 * @return Page object of next page based on application navigation
	 */
	
	public Object continuePage() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId,ResetPasswordSMSConfirmation.class);
		}
		return null;
	}

	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}

}
