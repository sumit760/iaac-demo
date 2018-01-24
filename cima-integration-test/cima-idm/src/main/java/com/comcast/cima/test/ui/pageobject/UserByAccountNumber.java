package com.comcast.cima.test.ui.pageobject;

import java.util.HashMap;
import java.util.Map;

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
 * This is Selenium Page Object for "User By Account Number" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class UserByAccountNumber extends SeleniumPageObject<UserByAccountNumber> {

	/* Web Elements */
	
	/* Header Mesage */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMesage;
	
	
	/* Account */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/span")
	private WebElement accountNumberTitleMessage;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[1]/legend/button")
	private WebElement accountNumberHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//div[@id='accountNumberInfo']/div[@class='content']")
	private WebElement accountNumberHelpText;
	
	@FindBy (how = How.XPATH, using = "//div[@id='accountNumberInfo']/div[@class='content']/button[@class='close']")
	private WebElement accountNumberHelpClose;
	
	@FindBy (how = How.ID, using = "billingAccountId")
	private WebElement accountNumber;
	
	
	
	/* Street Address or Phone */
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[2]/legend/span")
	private WebElement streetAddressOrPhoneNumberTitleMessage;
	
	@FindBy (how = How.ID, using = "by-StreetAddress_radio")
	private WebElement streetAddressRadio;
	
	@FindBy (how = How.ID, using = "streetAddress")
	private WebElement streetAddress;
	
	@FindBy (how = How.ID, using = "by-PhoneNumber_radio")
	private WebElement phoneNumberRadio;
	
	@FindBy (how = How.ID, using = "phoneNumber")
	private WebElement phoneNumber;
	
	
	/* Continue */
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement continuePage;
	
	@FindBy (how = How.LINK_TEXT, using = "I'd rather verify another way")
	private WebElement continueOtherWay;
	
	
	/* Error Conditions */
	
	@FindBy (how = How.ID, using = "billingAccountId-error")
	private WebElement invalidAccountNumberError;
	
	@FindBy (how = How.ID, using = "streetAddress-error")
	private WebElement invalidStreetAddressError;
	
	@FindBy (how = How.ID, using = "phoneNumber-error")
	private WebElement invalidPhoneNumberError;
	
	@FindBy (how = How.ID, using = "verificationSubType-error")
	private WebElement verificationError;
	
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMesage));
	}
	
	
	public UserByAccountNumber(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To enter account number
	 * 
	 * @param accountNo:
	 *                 Account Number
	 */
	public void enterAccountNumber(String accountNo) {
		if (isElementPresent(this.accountNumberTitleMessage) && isElementPresent(this.accountNumber)) {
			this.clearAndType(this.accountNumber, accountNo);
		}
	}
	
	/**
	 * To get help icon text of account number
	 * 
	 * @return text of help icon of account number
	 */
	public String getAccountNumberHelpIconText() {
		String text = null;
		if (isElementPresent(accountNumberHelpIcon)) {
			this.accountNumberHelpIcon.click();
			waitForElementPresent(accountNumberHelpText,ICommonConstants.WAIT_TIMEOUT);
			if (isElementPresent(accountNumberHelpText)){
				text = this.accountNumberHelpText.getText();
			}
			if (isElementPresent(accountNumberHelpClose)) {
				this.accountNumberHelpClose.click();
			}
		}
		return text;
	}
	
	
	/**
	 * To enter street address
	 * 
	 * @param streetAddress:
	 *                     Street address
	 */
	public void enterStreetAddress(String streetAddress) {
		if (isElementPresent(this.streetAddressRadio)) {
			this.streetAddressRadio.click();
			waitForElementPresent(this.streetAddress,ICommonConstants.WAIT_TIMEOUT);
			if (isElementPresent(this.streetAddress)) {
				this.clearAndType(this.streetAddress, streetAddress);
			}
		}
	}
	
	/**
	 * To enter phone number
	 * 
	 * @param phoneNo :
	 *                 Phone Number
	 */
	public void enterPhoneNumber(String phoneNo) {
		if (isElementPresent(this.streetAddressOrPhoneNumberTitleMessage) && isElementPresent(this.phoneNumberRadio)) {
			this.phoneNumberRadio.click();
			waitForElementPresent(this.phoneNumber,10);
			if (isElementPresent(this.phoneNumber)) {
				this.clearAndType(this.phoneNumber, phoneNo);
			}
		}
	}
	
	
	/**
	 * To click continue to get to create user or information mismatch page
	 * 
	 * @return object of CreateUser or information mismatch
	 * @throws Exception
	 */
	public Object Continue() {
		if (isElementPresent(continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To click continue other way to get select another method to create user
	 * 
	 * @return object of CreateUser or information mismatch
	 * @throws Exception
	 */
	
	public Object continueOtherWay() throws Exception {
		if (isElementPresent(continueOtherWay)) {
			this.continueOtherWay.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	
/**
 * To get error message list
 * 
 * @return list of error message
 */
	public Map<String,String> getErrorMesssage() {
		Map<String,String> errors = new HashMap<String,String>();
		
		if (isElementPresent(invalidAccountNumberError)){
			errors.put("invalidAccountNumberError",invalidAccountNumberError.getText());
		}
		if (isElementPresent(invalidStreetAddressError)){
			errors.put("invalidStreetAddressError",invalidStreetAddressError.getText());
		}
		if (isElementPresent(invalidPhoneNumberError)){
			errors.put("invalidPhoneNumberError",invalidPhoneNumberError.getText());
		}
		if (isElementPresent(verificationError)){
			errors.put("verificationError",verificationError.getText());
		}
		
		return errors;
	}
	
	

}
