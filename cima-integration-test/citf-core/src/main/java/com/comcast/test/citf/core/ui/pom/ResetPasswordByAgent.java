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
 * This is Selenium Page Object for "Reset Password By Agent" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class ResetPasswordByAgent extends SeleniumPageObject<ResetPasswordByAgent> {
	
	/* Web Elements */
	
	/* Header */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p[1]")
	private WebElement subHeaderMessage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset/legend/span")
	private WebElement resetCodeMessage;
	
	@FindBy (how = How.ID, using = "oneTimeResetCodeEntered")
	private WebElement resetCodeTextBox;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continueButton;
	
	
	/* error Elements */
	@FindBy (how = How.ID, using = "oneTimeResetCodeEntered-error")
	private WebElement resetCodeError;
	

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	public ResetPasswordByAgent(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * TO verify Reset password by agent page
	 * 
	 * @return True if header message and sub header message is present, else False
	 */
	public boolean verifyPage() {
		return (isElementPresent(headerMessage) && isElementPresent(subHeaderMessage));
	}
	
	/**
	 * To continue to reset code
	 * 
	 * @param code :
	 *               Reset Code
	 * @return Next page object based on application flow
	 */
	
	public Object provideResetCodeAndContinue(String code) {
		if (isElementPresent(this.resetCodeTextBox) && code != null) {
			this.clearAndType(this.resetCodeTextBox, code);
			this.continueButton.click();
			
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To get error message text
	 * 
	 * @return Error message text
	 */
	
	public String getErrorMessage() {
		if (isElementPresent(this.resetCodeError)) {
			return this.resetCodeError.getText();
		}
		return null;
	}

}
