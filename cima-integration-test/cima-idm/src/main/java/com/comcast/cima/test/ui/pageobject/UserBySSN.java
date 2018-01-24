package com.comcast.cima.test.ui.pageobject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;
/**
 * This is Selenium Page Object for "User By SSN" web page.
 * 
 * @author Sumit Pal
 *
 */

public class UserBySSN extends SeleniumPageObject<UserBySSN> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	
	/* SSN Details */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/span")
	private WebElement ssnTitleMessage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/button[@class='info icon']")
	private WebElement ssnTitleMessageHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/div[@class='tooltip']/div[@class='content']")
	private WebElement ssnTitleMessageHelpIconText;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/div[@class='tooltip']/div/button[@class='close']")
	private WebElement ssnTitleMessageHelpIconClose;
	
	@FindBy (how = How.ID, using = "ssn")
	private WebElement socialSecurityNumber;
	
	
	/* Date of Birth and phone number */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[2]/legend/span")
	private WebElement dobAndPhoneNoTitleMessage;
	
	@FindBy (how = How.ID, using = "dateOfBirth")
	private WebElement dob;
	
	@FindBy (how = How.ID, using = "phoneNumber")
	private WebElement phoneNumber;
	
	
	/* Transition Elements */ 
	@FindBy (how = How.ID, using = "continue")
	private WebElement continuePage;
	
	@FindBy (how = How.ID, using = "fallback")
	private WebElement continueOtherWay;
	
	
	/* Elements in Error scenarios */
	@FindBy (how = How.ID, using = "ssn-error")
	private WebElement ssnError;
	
	@FindBy (how = How.ID, using = "dateOfBirth-error")
	private WebElement dobError;
	
	@FindBy (how = How.ID, using = "phoneNumber-error")
	private WebElement phoneNumberError;
	
	public UserBySSN(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	/**
	 * To enter SSN
	 * 
	 * @param ssn :
	 *            Social Security number
	 */
	
	public void enterSocialSecurityNumber(String ssn) {
		if (isElementPresent(ssnTitleMessage) && isElementPresent(socialSecurityNumber)) 
			this.clearAndType(socialSecurityNumber, ssn);
	}
	
	/**
	 * To get SSN help Icon text
	 * 
	 * @return Text of SSN help icon
	 */
	public String getSSNHelpIconText() {
		String text = null;
		if (isElementPresent(ssnTitleMessageHelpIcon)) {
			this.ssnTitleMessageHelpIcon.click();
			waitForElementPresent(ssnTitleMessageHelpIconText,10);
			if (isElementPresent(ssnTitleMessageHelpIconText)){
				text = this.ssnTitleMessageHelpIconText.getText();
			}
			if (isElementPresent(ssnTitleMessageHelpIconClose)) {
				this.ssnTitleMessageHelpIconClose.click();
			}
		}
		return text;
	}
	
	/**
	 * To enter date of birth
	 * 
	 * @param dateOfBirth:
	 *                    Date of birth
	 */
	
	public void enterDateOfBirth(String dateOfBirth) {
		if (isElementPresent(dobAndPhoneNoTitleMessage) && isElementPresent(dob)) {
			this.clearAndType(dob, dateOfBirth);
		}
	}
	
	/**
	 * To enter Phone
	 * 
	 * @param phone
	 *             Phone
	 */
	
	public void enterPhoneNumber(String phone) {
		if (isElementPresent(dobAndPhoneNoTitleMessage) && isElementPresent(phoneNumber)) {
			this.clearAndType(phoneNumber, phone);
		}
	}
	
	
	/**
	 * To click continue
	 * 
	 * @return next page of user by SSN in application flow
	 * @throws Exception
	 */
	
	public Object Continue() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			waitForPageLoaded(driver);
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	/**
	 * To click continueOtherWay
	 * 
	 * @return next page of user by SSN in application flow
	 * @throws Exception
	 */
	
	public Object continueOtherWay() {
		if (isElementPresent(continueOtherWay)) {
			this.continueOtherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId,VerifyIdentity.class);
		}
		return null;
	}
	
	
	/**
	 * To get errors of user by SSN page
	 * 
	 * @return list of error of "user by SSN" page
	 */
	public List<String> getErrorMesssage() {
		List<String> errors = new ArrayList<String>();
		
		if (isElementPresent(ssnError)) {
			errors.add(ssnError.getText());
		} else if (isElementPresent(dobError)) {
			errors.add(dobError.getText());
		} else if (isElementPresent(phoneNumberError)) {
			errors.add(phoneNumberError.getText());
		}
		
		return errors;
	}

}
