package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiMobileSent extends SeleniumPageObject<SuSiMobileSent> {
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.ID, using = "verificationCodeEntered")
	private WebElement verificationCodeText;
	
	@FindBy (how = How.XPATH, using = "//main/form/section/p/button")
	private WebElement resendTextLink;
	
	@FindBy (how = How.XPATH, using = "//main/form/section/button")
	private WebElement signInWithUsernameLink;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[1]/button")
	private WebElement backButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[2]/button")
	private WebElement nextButton;

	public SuSiMobileSent(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiMobileSent page");	
	}
	
	/* Action Methods */
	public void enterVerificationCode(String code) {
		if (isElementPresent(this.verificationCodeText)) {
			this.clearAndType(this.verificationCodeText, code);
		} else {
			throw new IllegalStateException("SMS verification code text field not found");
		}
	}
	
	public void resendVerificationCode() {
		if (isElementPresent(this.resendTextLink)) {
			this.resendTextLink.click();
		} else {
			throw new IllegalStateException("resend SMS verification code link not found");
		}
	}
	
	public void signInWithUsernameAndPassword() {
		if (isElementPresent(this.signInWithUsernameLink)) {
			this.signInWithUsernameLink.click();
		} else {
			throw new IllegalStateException("sign in with username link not found");
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
				throw new RuntimeException("Failed to get the SuSi your account page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
}
