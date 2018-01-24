package com.comcast.cima.test.ui.pageobject;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class RABICheckOptions extends SeleniumPageObject<UserLookupSignUp> {
	
	private static final String WAIT_SECONDS = "2";

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "contactEmail")
	private WebElement emailText;
	
	@FindBy (how = How.ID, using = "phoneNumber")
	private WebElement phoneText;
	
	@FindBy (how = How.ID, using = "sendSmsValidationCode")
	private WebElement verifyButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/fieldset[2]/div[2]/div/h1")
	private WebElement consentMessage;
	
	@FindBy (how = How.XPATH, using = "//main/form/fieldset[2]/div[2]/div/button[1]")
	private WebElement agreeButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/fieldset[2]/div[1]/input")
	private WebElement smsCode;
	
	@FindBy (how = How.XPATH, using = "//main/form/fieldset[2]/div[1]/button")
	private WebElement verifySMSCode;
	
	@FindBy (how = How.XPATH, using = "//main/form/fieldset[2]/span")
	private WebElement phoneVerified;
	
	@FindBy (how = How.ID, using = "dk0-combobox")
	private WebElement secretQuestionDropdown;
	
	@FindBy (how = How.ID, using = "dk0-")
	private WebElement chooseSecretQuestion;
	
	@FindBy(how = How.ID, using = "dk0-What-is your favorite beverage?")
	private WebElement defaultSecretQuestion;
	
	@FindBy(how = How.ID, using = "dk0-What-was your first car (make and model)?")
	private WebElement secondSecretQuestion;
	
	@FindBy (how = How.ID, using = "secretAnswer")
	private WebElement secretAnswerTextBox;
	
	@FindBy (how = How.ID, using = "continue")
	private WebElement pageContinue;
	
	@FindBy (how = How.ID, using = "askMeLater")
	private WebElement askMeLater;

	public RABICheckOptions(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage), "Failed to land on RABICheckOptions page");
	}
	
	public boolean isVerifyButtonPresent(){
		return isElementPresent(this.verifyButton);
	}
	
	/* Action Methods */
	/* Alternate Email */
	public void enterEmail(String email) {
		if (isElementPresent(this.emailText)) {
			this.clearAndType(this.emailText, email);
		} else {
			throw new IllegalStateException("email text field not found");
		}
	}
	
	/* Mobile Phone */ 
	public void enterMobilePhone(String mobilePhoneNumber) {
		if(isElementPresent(this.phoneText)) {
			this.clearAndType(this.phoneText, mobilePhoneNumber);
		} else {
			throw new IllegalStateException("mobile number text field not found");
		}
	}
	
	public void verifyMobilePhone(String mobilePhoneNumber) {
		if (mobilePhoneNumber.length() == 10) {
			if (isElementPresent(this.phoneText)) {
				this.clearAndType(this.phoneText, mobilePhoneNumber);
				try {
					TimeUnit.SECONDS.sleep(Integer.parseInt(WAIT_SECONDS));
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.verifyButton.click();
				waitForElementPresent(this.agreeButton,ICommonConstants.WAIT_TIMEOUT);
				this.agreeButton.click();
				waitForElementPresent(this.smsCode,ICommonConstants.WAIT_TIMEOUT);
			} else {
				throw new IllegalStateException("mobile number text field not found");
			}
		} else {
			throw new IllegalStateException("mobile number length is not valid.");
		}
	}
	
	public void verifyMobilePhone() {
		if (isElementPresent(this.verifyButton)) {
			this.verifyButton.click();
		} else {
			throw new IllegalStateException("verify mobile number button not found");
		}
	}
	
	
	public void enterSMSCode(String smsCode) {
		if (smsCode != null && isElementPresent(this.smsCode)) {
			this.clearAndType(this.smsCode, smsCode);
			this.verifySMSCode.click();
			waitForElementVisible(this.phoneVerified,ICommonConstants.WAIT_TIMEOUT);
		} else {
			throw new IllegalStateException("No SMS verification code found in server log or SMS verification code text field not found");
		}
	}
	
	/* Secret Questions */ 
	public void selectSecurityQuestion(String question) {
		if (isElementPresent(this.secretQuestionDropdown)) {
			this.selectDropDownOptionByVisibleText(this.secretQuestionDropdown, question);
		} else {
			throw new IllegalStateException("secret question dropdown not found");
		}
	}
	
	public void selectDefaultSecurityQuestion() {
		if (isElementPresent(this.secretQuestionDropdown)) {
			this.selectDropDownOptionByElement(this.secretQuestionDropdown, this.defaultSecretQuestion);
		} else {
			throw new IllegalStateException("secret question dropdown not found");
		}
	}
	
	public void selectSecondSecurityQuestion() {
		if (isElementPresent(this.secretQuestionDropdown)) {
			this.selectDropDownOptionByElement(this.secretQuestionDropdown, this.secondSecretQuestion);
		} else {
			throw new IllegalStateException("secret question dropdown not found");
		}
	}
	
	public void setAnswer(String answer) {
		if (isElementPresent(this.secretAnswerTextBox)) {
			this.clearAndType(this.secretAnswerTextBox, answer);
		} else {
			throw new IllegalStateException("secret answer dropdown not found");
		}
	}
	
	public void setDefaultAnswer() {
		if (isElementPresent(this.secretAnswerTextBox)) {
			this.clearAndType(this.secretAnswerTextBox, ICimaCommonConstants.USER_SECURITY_QUESTION_ANSWER_DEFAULT);
		} else {
			throw new IllegalStateException("secret answer dropdown not found");
		}
	}
	
	
	/* Transition Method */
	public Object continueAskMeLater() {
		Object obj = null;
		if (isElementPresent(this.askMeLater)) {
			this.askMeLater.click();
			try {
				obj =  ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get Xfinity home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public String continueAskMeLaterToGetResponseURL() {
		if (isElementPresent(this.askMeLater)) {
			this.askMeLater.click();
			return driver.getCurrentUrl();
		} else {
			throw new RuntimeException("Ask me later link not found. PageFlowId: " + this.pageFlowId);
		}
	}
	
	public Object continueConfirmation() {
		Object obj = null;
		if (isElementPresent(this.pageContinue)) {
			this.pageContinue.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get RABI check confirmation page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
}
