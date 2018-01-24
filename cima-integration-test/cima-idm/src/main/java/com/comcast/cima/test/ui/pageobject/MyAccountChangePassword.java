package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "My account change password" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class MyAccountChangePassword extends SeleniumPageObject<MyAccountChangePassword>  {

	@FindBy (how = How.XPATH, using = "//h1[@data-sel='change-password-header']")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "main_0_currentPassword")
	private WebElement currentPasswordText;
	
	@FindBy (how = How.ID, using = "main_0_newPassword")
	private WebElement newPasswordText;
	
	@FindBy (how = How.ID, using = "main_0_confirmPassword")
	private WebElement confirmNewPasswordText;
	
	@FindBy (how = How.ID, using = "main_0_btnConfirm")
	private WebElement savePasswordButton;
	
	@FindBy (how = How.ID, using = "main_0_errorList_SingleErrorContainer")
	private WebElement errorMessage;
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public MyAccountChangePassword(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * 
	 * To to change password
	 * 
	 * Note :For valid test newPassword and confirmPassword should be same
	 * 
	 * @param oldPassword:
	 *                    Old Password
	 * @param newPassword:
	 *                  New Password      
	 *                  
	 * @return : Return password changed confirmation page or error                         
	 */
	
	public Object changePassword(String oldPassword, String newPassword, String confirmPassword) throws Exception{
		if (isElementPresent(currentPasswordText) && isElementPresent(newPasswordText) && isElementPresent(confirmNewPasswordText)){
			this.setCurrentPassword(oldPassword);
			this.setNewPassword(newPassword);
			this.confirmNewPassword(confirmPassword);
			this.savePasswordButton.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		return null;
	}

	/**
	 * To set current password
	 * 
	 * @param password :
	 *                 Password
	 */
	
	private void setCurrentPassword(String password) {
		this.clearAndType(currentPasswordText, password);
	}
	
	/**
	 * To set New password
	 * 
	 * @param password :
	 *                 Password
	 */
	
	private void setNewPassword(String password) {
		this.clearAndType(newPasswordText, password);
	}
	
	/**
	 * To set confirm password
	 * 
	 * @param password :
	 *                Password
	 */
	private void confirmNewPassword(String password) {
		this.clearAndType(confirmNewPasswordText, password);
	}
}
