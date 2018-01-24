package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiYourAccount extends SeleniumPageObject<SuSiYourAccount> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/form/div/div[1]/section[1]/div")
	private WebElement nameText;
	
	@FindBy (how = How.XPATH, using = "//main/form/div/div[1]/section[2]/div")
	private WebElement mobileNumberText;
	
	@FindBy (how = How.XPATH, using = "//main/form/div/div[2]/section[1]/div")
	private WebElement addressText;
	
	@FindBy (how = How.XPATH, using = "//main/form/div/div[2]/section[2]/div")
	private WebElement emailText;
	
	@FindBy (how = How.ID, using = "webServiceAgreementAccepted_checkbox")
	private WebElement acceptTermCheckBox;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[1]/button")
	private WebElement backButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[2]/button")
	private WebElement nextButton;
	
	public SuSiYourAccount(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage), "Failed to land on SuSiYourAccount page");	
	}
	
	public String getName(){
		return this.nameText.getText();
	}
	
	public String getMobileNumber() {
		return this.mobileNumberText.getText();
	}
	
	public String getAddress(){
		return this.addressText.getText();
	}
	
	public String getEmail(){
		return this.emailText.getText();
	}
	
	/* Action Methods */
	public void acceptTerms() {
		if (isElementPresent(this.acceptTermCheckBox)) {
			this.acceptTermCheckBox.click();
		} else {
			throw new IllegalStateException("accept terms check box not found");
		}
	}

	/* Transition Method */
	public Object pageBack() {
		Object obj = null;
		if (isElementPresent(this.backButton)) {
			this.backButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering mobile page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object pageNext() {
		Object obj = null;
		if (isElementPresent(this.nextButton)) {
			this.nextButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi set up WiFi page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
}
