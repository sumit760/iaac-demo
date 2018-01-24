package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class RABICheckConfirmation extends SeleniumPageObject<UserLookupSignUp> {
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement emailSentText;
	
	@FindBy (how = How.XPATH, using = "//main/ol/li[1]")
	private WebElement emailConfirmationMessage1;
	
	@FindBy (how = How.XPATH, using = "//main/ol/li[2]")
	private WebElement emailConfirmationMessage2;
	
	@FindBy (how = How.XPATH, using = "//main/section[1]/div/p")
	private WebElement emailConfirm;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/div/p")
	private WebElement phoneConfirmWithEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[1]/div/p")
	private WebElement phoneConfirmWithoutEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[3]/div/p[1]")
	private WebElement questionConfirmWithEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/div/p[1]")
	private WebElement questionConfirmWithoutEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[3]/div/p[2]")
	private WebElement answerConfirmWithEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/div/p[2]")
	private WebElement answerConfirmWithoutEmail;
	
	@FindBy (how = How.XPATH, using = "//main/a")
	private WebElement pageContinue;
	
	public RABICheckConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on RABICheckConfirmation page");
	}
	
	public boolean isEmailSentSuccessfully() {
		boolean sent = false;
		if (isElementPresent(this.emailSentText) && isElementPresent(this.emailConfirmationMessage1)
				&& isElementPresent(this.emailConfirmationMessage2)) {
			
			sent = this.emailSentText.getText().contains("We've just sent an email to") && 
			this.emailConfirmationMessage1.getText().contains("Check your email") &&
			this.emailConfirmationMessage2.getText().contains("Click the link to verify your email");
		}
		return sent;
	}

	public String getEmailConfirm() {
		return this.emailConfirm.getText();
	}

	public String getPhoneConfirmWithEmail() {
		String part1 = this.phoneConfirmWithEmail.getText().substring(1, 4);
		String part2 = this.phoneConfirmWithEmail.getText().substring(5, 8);
		String part3 = this.phoneConfirmWithEmail.getText().substring(9);
		return part1 + part2 + part3;
	}
	
	public String getPhoneConfirmWithoutEmail() {
		String part1 = this.phoneConfirmWithoutEmail.getText().substring(1, 4);
		String part2 = this.phoneConfirmWithoutEmail.getText().substring(5, 8);
		String part3 = this.phoneConfirmWithoutEmail.getText().substring(9);
		return part1 + part2 + part3;
	}

	public String getQuestionConfirmWithEmail() {
		return this.questionConfirmWithEmail.getText();
	}
	
	public String getQuestionConfirmWithoutEmail() {
		return this.questionConfirmWithoutEmail.getText();
	}

	public String getAnswerConfirmWithEmail() {
		return this.answerConfirmWithEmail.getText();
	}
	
	public String getAnswerConfirmWithoutEmail() {
		return this.answerConfirmWithoutEmail.getText();
	}

	/* Transition Method */
	public Object continueXfinity() {
		Object obj = null;
		if (isElementPresent(this.pageContinue)) {
			this.pageContinue.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get Xfinity home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
}
