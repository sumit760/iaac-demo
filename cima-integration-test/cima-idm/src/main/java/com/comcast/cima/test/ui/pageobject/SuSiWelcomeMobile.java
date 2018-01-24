package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiWelcomeMobile extends SeleniumPageObject<SuSiWelcomeMobile> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.ID, using = "mobilePhoneNumber")
	private WebElement mobileNumberText;
	
	@FindBy (how = How.XPATH, using = "//main/form/button")
	private WebElement signInWithUsernameLink;
	
	@FindBy (how = How.ID, using = "tncAccepted_checkbox")
	private WebElement acceptTermCheckBox;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div/button")
	private WebElement nextButton;
	
	public SuSiWelcomeMobile(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiWelcomeMobile page");	
	}
	
	/* Action Methods */
	public void enterMobileNumber(String mobileNumber) {
		if (isElementPresent(this.mobileNumberText)) {
			this.clearAndType(this.mobileNumberText, mobileNumber);
		} else {
			throw new IllegalStateException("mobile number text field not found");
		}
	}
	
	public void acceptTerms() {
		if (isElementPresent(this.acceptTermCheckBox)) {
			this.acceptTermCheckBox.click();
		} else {
			throw new IllegalStateException("accept terms check box not found");
		} 
	}
	
	/* Transition Method */
	public Object pageNext() {
		Object obj = null;
		if (isElementPresent(this.nextButton)) {
			this.nextButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering verification code page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	
	public Object signInWithUsernameAndPassword() {
		Object obj = null;
		if (isElementPresent(this.signInWithUsernameLink)) {
			this.signInWithUsernameLink.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering credential page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
}
