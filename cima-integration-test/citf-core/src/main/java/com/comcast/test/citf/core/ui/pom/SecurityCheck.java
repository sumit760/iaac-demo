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
 * This is Selenium Page Object for "SecurityCheck" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class SecurityCheck extends SeleniumPageObject<SecurityCheck> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "nucaptcha-answer")
	private WebElement captcha;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage));
	}
	
	public SecurityCheck(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To click continue after entering security code
	 * 
	 * @param securityCode
	 * 			Security code
	 * @return This can return to any of the following pages
	 * 			1. UserByAccountNumber (Create UID Flow)
	     		2. UserBySSN (Create UID Flow)
		  		3. UserByMobile (Create UID Flow)
		  		4. ResetPasswordMethods (IDM Reset Password Flow)
		  		5. ResetPasswordByEmail (IDM Reset Password Flow)
		  		5. Error page in case of any errors
	 */
	public Object provideSecurityCodeAndContinue(String securityCode) {
		if (securityCode != null && isElementPresent(captcha) && isElementPresent(continuePage)) {
			this.clearAndType(captcha, securityCode);
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}

}
