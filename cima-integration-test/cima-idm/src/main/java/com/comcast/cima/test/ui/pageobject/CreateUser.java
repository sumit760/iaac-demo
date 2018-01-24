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
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "create User" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class CreateUser extends SeleniumPageObject<CreateUser>{
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement  headerGreetingMessage;
	
	/* Username */
	@FindBy (how = How.XPATH, using = "//legend[@id='usernameContainer_legend']/span[@class='required']")
	private WebElement  usernameTitle;
	
	@FindBy (how = How.XPATH, using = "//main/h1/span")
	private WebElement  userFirstName;
	
	@FindBy (how = How.ID, using = "userName")
	private WebElement  usernameTextBox;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='usernameContainer_legend']/button")
	private WebElement  usernameHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//div[@class='tooltip']/div[@class='content']")
	private WebElement  usernameHelpIconToolTipText;
	
	@FindBy (how = How.XPATH, using = "//div[@class='tooltip']/div[@class='content']/button[@class='close']")
	private WebElement  usernameHelpIconClose;
	
	@FindBy (how = How.ID, using = "usePersonalEmail")
	private WebElement  usePersonalEmail;
	
	@FindBy (how = How.ID, using = "primaryEmail")
	private WebElement  userEmailTextBox;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='personalEmailContainer_legend']/button")
	private WebElement  userEmailHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='personalEmailContainer_legend']/div/div[@class='content']")
	private WebElement  userEmailHelpIconToolTipText;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='personalEmailContainer_legend']/div/div/button[@class='close']")
	private WebElement  userEmailHelpIconClose;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='personalEmailContainer_legend']/span")
	private WebElement  emailTitle;
	
	@FindBy (how = How.LINK_TEXT, using = "I'd rather create my username another way")
	private WebElement  createUserName;
	
	
	/* Password */
	@FindBy (how = How.XPATH, using = "//legend[@id='passwordFieldset_legend']/span[@class='required']")
	private WebElement  passwordTitle;
	
	@FindBy(how = How.ID, using = "password")
	private WebElement passwordTextBox;
	
	@FindBy(how = How.ID, using = "passwordRetype")
	private WebElement reEnterpasswordTextBox;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='passwordFieldset_legend']/button")
	private WebElement  passwordHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//li[@id='lengthStatus']/div")
	private WebElement  passwordHelpIconToolText1;
	
	@FindBy (how = How.XPATH, using = "//li[@id='hasLetterStatus']/div")
	private WebElement  passwordHelpIconToolText2;
	
	@FindBy (how = How.XPATH, using = "//li[@id='hasNumOrSpecialCharStatus']/div")
	private WebElement  passwordHelpIconToolText3;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noSpacesStatus']/div")
	private WebElement  passwordHelpIconToolText4;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noInvalidCharStatus']/div")
	private WebElement  passwordHelpIconToolText5;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noFirstNameStatus']/div")
	private WebElement  passwordHelpIconToolText6;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noLastNameStatus']/div")
	private WebElement  passwordHelpIconToolText7;
	
	@FindBy (how = How.XPATH, using = "//li[@id='noUsernameStatus']/div")
	private WebElement  passwordHelpIconToolText8;
	
	@FindBy (how = How.XPATH, using = "//li[@id='notGuessableStatus']/div")
	private WebElement  passwordHelpIconToolText9;
	
	@FindBy (how = How.XPATH, using = "//div[@id='passwordEssentials']/div/button[@class='close']")
	private WebElement  passwordHelpIconClose;
	
	@FindBy (how = How.XPATH, using = "//label[@for='rememberMe']/span[@class='content']")
	private WebElement  keepMeSignedInTitle;
	
	@FindBy (how = How.XPATH, using = "//span[@id='rememberMe_checkbox']")
	private WebElement  rememberPassword;
	
	
	/* Alternate Email */
	@FindBy(how = How.XPATH, using = "//legend[@id='recoveryEmail_legend']/span")
	private WebElement alternateEmailTitle;
	
	@FindBy(how = How.ID, using = "alternateEmail")
	private WebElement alternateEmailTextBox;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryEmail_legend']/button")
	private WebElement  altEmailHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryEmail_legend']/div[@class='tooltip']/div[@class='content']")
	private WebElement  altEmailHelpIconText;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryEmail_legend']/div[@class='tooltip']/div[@class='content']/button[@class='close']")
	private WebElement  altEmailHelpIconClose;
	
	
	/* Mobile Phone */
	@FindBy(how = How.XPATH, using = "//legend[@id='recoveryPhone_legend']/span")
	private WebElement phoneNumberTitle;
	
	@FindBy(how = How.ID, using = "phoneNumber")
	private WebElement phoneNumberTextBox;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryPhone_legend']/button")
	private WebElement  mobilePhoneHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryPhone_legend']/div[@class='tooltip']/div[@class='content']")
	private WebElement  mobilePhoneHelpIconText;
	
	@FindBy (how = How.XPATH, using = "//legend[@id='recoveryPhone_legend']/div[@class='tooltip']/div[@class='content']/button[@class='close']")
	private WebElement  mobilePhoneHelpIconClose;
	
	@FindBy(how = How.XPATH, using = "//div[@id='consentBlock']/div[@class='content']/h1")
	private WebElement consentMessageHeader;
	
	@FindBy(how = How.XPATH, using = "//div[@id='consentBlock']/div[@class='content']/p")
	private WebElement consentMessage;
	
	@FindBy(how = How.XPATH, using = "//div[@id='consentBlock']/div/button[@class='close']")
	private WebElement consentMessageClose;
	
	@FindBy(how = How.ID, using = "agree")
	private WebElement giveConsentForSMS;
	
	@FindBy(how = How.ID, using = "sendSmsValidationCode")
	private WebElement verifyMobile;
	
	@FindBy(how = How.ID, using = "smsValidationCodeEntered")
	private WebElement smsCode;
	
	@FindBy(how = How.ID, using = "verifySMSVerificationCode")
	private WebElement verifySMSCode;
	
	@FindBy(how = How.XPATH, using = "//fieldset[@id='recoveryPhone']/span")
	private WebElement phoneVerified;
	
	
	/* Secret Question */
	@FindBy(how = How.XPATH, using = "//form[@id='mainForm']/fieldset[5]/legend/span")
	private WebElement secretQuestionTitle;

	@FindBy(how = How.ID, using = "dk0-combobox")
	private WebElement secretQuestionDropdown;
	
	@FindBy(how = How.ID, using = "dk0-What-is your favorite beverage?")
	private WebElement defaultSecretQuestion;
	
	@FindBy(how = How.ID, using = "secretAnswer")
	private WebElement secretAnswerTextBox;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[5]/legend/button")
	private WebElement  secretQuestionHelpIcon;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[5]/legend/div/div")
	private WebElement  secretQuestionHelpIconText;
	
	@FindBy (how = How.XPATH, using = "//form[@id='mainForm']/fieldset[5]/legend/div/div/button")
	private WebElement  secretQuestionHelpClose;
	
	
	/* Agreement for Terms & Conditions */
	@FindBy(how = How.ID, using = "cimTcAccepted_checkbox")
	private WebElement termsOfServiceCheck;
	
	@FindBy(how = How.LINK_TEXT, using = "Terms of Service")
	private WebElement termsOfService;
	
	@FindBy(how = How.LINK_TEXT, using = "Privacy Policy")
	private WebElement privacyPolicy;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement  continueButton;
				
	
	/* Error messages */
	
	@FindBy(how = How.ID, using = "userName-error")
	private WebElement userNameError;
	
	@FindBy(how = How.ID, using = "password-error")
	private WebElement invalidPasswordError;
	
	@FindBy(how = How.ID, using = "passwordRetype-error")
	private WebElement passwordReTypeError;
	
	@FindBy(how = How.ID, using = "passwordRetype-error")
	private WebElement passwordNotMatchedError;
	
	@FindBy(how = How.ID, using = "alternateEmail-error")
	private WebElement altEmailInvalidError;
	
	@FindBy(how = How.ID, using = "secretQuestion-error")
	private WebElement secretQuestionError;
	
	@FindBy(how = How.ID, using = "secretAnswer-error")
	private WebElement secretAnswerError;
	
	@FindBy(how = How.ID, using = "cimTcAccepted-error")
	private WebElement termsofServiceErrorMsg;
	
	@FindBy(how = How.ID, using = "phoneNumber-error")
	private WebElement phoneNumErrorMsg;	
		
	public CreateUser(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
	}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerGreetingMessage), "Failed to land on CreateUser page");
	}
	
	
	/*Action elements*/
	
	/**
	 * Checks whether greeting Header of create user page is present
	 * 
	 * @return True if present, else False
	 */
	
	public boolean isHeaderGreetingDisplayed() {
		return isElementPresent(headerGreetingMessage);
	}
	
	/**
	 * Provides first name of user
	 * @return first name of user
	 */
	public String getUserFirstName() {
		return this.userFirstName.getText();
	}

	/**
	 * Enter username on create user page
	 * 
	 * @param username
	 *                 User name
	 */	
	public void enterUserName(String username) {
		if (isElementPresent(usernameTitle) && isElementPresent(usernameTextBox)) {
			this.clearAndType(usernameTextBox, username);
		} else {
			throw new IllegalStateException("username title or username text box not found");
		}
	}
	
	/**
	 * Get User Name HelpIcon Text of create username page
	 * 
	 * @return Text of user name icon.
	 */
	
	public String getUserNameHelpIconText() {
		String text = null;
		if (isElementPresent(usernameHelpIcon)) {
			this.usernameHelpIcon.click();
			waitForElementPresent(usernameHelpIconToolTipText,10);
			if (isElementPresent(usernameHelpIconToolTipText)) {
				text = this.usernameHelpIconToolTipText.getText();
			}
			if (isElementPresent(usernameHelpIconClose)) {
				this.usernameHelpIconClose.click();
			}
		}
		return text;
	}
	
	/**
	 * To click on usepersonalEmail Link 
	 */
	public void usePersonalEmailAsUserName() {
		if (isElementPresent(usePersonalEmail)) {
			this.usePersonalEmail.click();
		} else {
			throw new IllegalStateException("use personal email link not found");
		}
	}
	
	/**
	 * To Enter email ID to create user
	 * 
	 * @param email 
	 * 			Email
	 */
	public void enterEmail(String email) {
		if (isElementPresent(userEmailTextBox)) {
			this.clearAndType(userEmailTextBox, email);
		} else {
			throw new IllegalStateException("user email text box not found");			
		}
	}
	
	/**
	 * Get Email Help Icon Text of create user
	 * 
	 * @return Text of email icon.
	 */
	
	public String getEmailHelpIconText() {
		String text = null;
		if (isElementPresent(userEmailHelpIcon)) {
			this.userEmailHelpIcon.click();
			waitForElementPresent(userEmailHelpIconToolTipText,10);
			if (isElementPresent(userEmailHelpIconToolTipText)) {
				text = this.userEmailHelpIconToolTipText.getText();
			}
			if (isElementPresent(userEmailHelpIconClose)) {
				this.userEmailHelpIconClose.click();
			}
		}
		return text;
	}
	
	
	/**
	 * To Enter Password if password field is present
	 * 
	 * @param pasword  
	 * 			Password
	 */
	public void enterPassword(String pasword) {
		if (isElementPresent(passwordTitle) && isElementPresent(passwordTextBox)) {
			this.clearAndType(passwordTextBox, pasword);
		} else {
			throw new IllegalStateException("password title or password text box not found");			
		}
	}
	
	/**
	 * To Enter Password if Re password field is present
	 * 
	 * @param pasword  
	 * 			Password
	 */
	
	public void reEnterPassword(String pasword) {
		if (isElementPresent(passwordTitle) && isElementPresent(reEnterpasswordTextBox)) {
			this.clearAndType(reEnterpasswordTextBox, pasword);
		} else {
			throw new IllegalStateException("password title or reenter password text box not found");			
		} 
	}
	
	/**
	 * To check the keep me signed in checkbox present on create user page
	 */
	
	public void keepSignedIn() {
		if (isElementPresent(keepMeSignedInTitle) && isElementPresent(rememberPassword)) {
			this.selectCheckboxByID(rememberPassword);
		} else {
			throw new IllegalStateException("keep me signed in title or remember me check box not found");			
		} 
	}
	
	
	/**
	 * Get password Help Icon Text
	 * 
	 * @return Text of password icon.
	 */
	
	
	public String getPasswordHelpIconText() {
		String text = "";
		if (isElementPresent(passwordHelpIcon)) {
			this.passwordHelpIcon.click();
			waitForElementPresent(passwordHelpIconToolText1,10);
			if (isElementPresent(passwordHelpIconToolText1)) {
				text = text + this.passwordHelpIconToolText1.getText() + ","
						    + this.passwordHelpIconToolText2.getText() + ","
						    + this.passwordHelpIconToolText3.getText() + ","
						    + this.passwordHelpIconToolText4.getText() + ","
						    + this.passwordHelpIconToolText5.getText() + ","
						    + this.passwordHelpIconToolText6.getText() + ","
						    + this.passwordHelpIconToolText7.getText() + ","
						    + this.passwordHelpIconToolText8.getText() + ","
						    + this.passwordHelpIconToolText9.getText();
			}
				
			if (isElementPresent(passwordHelpIconClose)) {
				this.passwordHelpIconClose.click();
			}
		}
		return text;
	}
	
	
	/**
	 * To enter alternate email
	 * 
	 * @param altEmail 
	 *          Alternate email
	 * */ 
	
	public void enterAlternateEmail(String altEmail) {
		if (isElementPresent(alternateEmailTitle) && isElementPresent(alternateEmailTextBox)) {
			this.clearAndType(alternateEmailTextBox, altEmail);
		} else {
			throw new IllegalStateException("alternate email title or alternate email text box not found");			
		}
	}
	
	/**
	 * Get alternate email Help Icon Text
	 * 
	 * @return Text of alternate email icon.
	 */
	
	
	public String getAltEmailHelpIconText() {
		String text = null;
		if (isElementPresent(altEmailHelpIcon)) {
			this.altEmailHelpIcon.click();
			waitForElementPresent(altEmailHelpIconText,10);
			if (isElementPresent(altEmailHelpIconText)) {
				text = this.altEmailHelpIconText.getText();
			}
			if (isElementPresent(altEmailHelpIconClose)) {
				this.altEmailHelpIconClose.click();
			}
		}
		return text;
	}
	
	
	
	/**
	 * To enter mobile number if mobile number field is present
	 *  
	 * @param mobilePhoneNumber 
	 *        	Mobile number
	 * @throws InterruptedException 
	 */
	public void enterMobilePhoneNumber(String mobilePhoneNumber) throws InterruptedException {
		
		Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
		if (isElementPresent(phoneNumberTextBox)) {
			this.clearAndType(phoneNumberTextBox, mobilePhoneNumber);
		} else {
			throw new IllegalStateException("phone number title or phone number text box not found");			
		} 
	}
	
	/**
	 * Get MobilePhone Help Icon Text
	 * 
	 * @return Text of MobilePhone icon.
	 */
	
	
	public String getMobilePhoneHelpIconText() {
		String text = null;
		if (isElementPresent(mobilePhoneHelpIcon)) {
			this.mobilePhoneHelpIcon.click();
			waitForElementPresent(mobilePhoneHelpIconText,10);
			if (isElementPresent(mobilePhoneHelpIconText)) {
				text = this.mobilePhoneHelpIconText.getText();
			}
			if (isElementPresent(mobilePhoneHelpIconClose)) {
				this.mobilePhoneHelpIconClose.click();
			}
		}
		return text;
	}
	
	/**
	 * 
	 * @param mobilePhoneNumber
	 *        	Mobile Number
	 * @return map of string containing Header and message of consent popup
	 */
	
	public Map<String,String> getMobilePhoneConsentHeaderAndMessage(String mobilePhoneNumber) {
		Map<String,String> consentMessageMap = new HashMap<String,String>();
		if (mobilePhoneNumber.length() == 10) {
			if (isElementPresent(phoneNumberTitle) && isElementPresent(phoneNumberTextBox)) {
				this.clearAndType(phoneNumberTextBox, mobilePhoneNumber);
				waitForElementPresent(verifyMobile,10);
				if (isElementPresent(verifyMobile)) {
					this.verifyMobile.click();
					waitForElementPresent(consentMessage,10);
					consentMessageMap.put("HEADER", consentMessageHeader.getText());
					consentMessageMap.put("MESSAGE", consentMessage.getText());
					this.consentMessageClose.click();
				}
			}
		} else {
			consentMessageMap.put("Error", "Invalid Phone Number");
		}
		return consentMessageMap;
	}
	
	
	/**
	 * To verify mobile number
	 * 
	 * @param mobilePhoneNumber
	 * Mobile number
	 * We will remove this method when we finish reconstruction	IDM tese cases
	 */
	@Deprecated
	public void verifyMobilePhone(String mobilePhoneNumber) {
		if (mobilePhoneNumber.length() == 10) {
			this.clearAndType(phoneNumberTextBox, mobilePhoneNumber);
			waitForElementPresent(verifyMobile,ICommonConstants.WAIT_TIMEOUT);
			if (isElementPresent(verifyMobile)) {
				this.verifyMobile.click();
				waitForElementPresent(giveConsentForSMS,ICommonConstants.WAIT_TIMEOUT);
				this.giveConsentForSMS.click();
				waitForElementPresent(this.smsCode,ICommonConstants.WAIT_TIMEOUT);
			}
		}
	}
	
	
	/**
	 * To verify Mobile Phone
	 */
	public void verifyMobilePhone() {
		if (isElementPresent(this.verifyMobile)) {
			this.verifyMobile.click();
		} else {
			throw new IllegalStateException("verify mobile button not found");			
		} 
	}
	
	/**
	 * To enter SMS code
	 * 
	 * @param smsCode
	 * 			SMS code
	 * We will remove this method when we finish reconstruction	IDM tese cases
	 */
	@Deprecated
	public void enterSMSCode(String smsCode) {
		if (smsCode != null && isElementPresent(this.smsCode)) {
			this.clearAndType(this.smsCode, smsCode);
			this.verifySMSCode.click();
			waitForElementVisible(this.phoneVerified,ICommonConstants.WAIT_TIMEOUT);
		} else {
			throw new IllegalStateException("SMS Code is null or SMS Code text field not found");			
		} 
	}
	
	/**
	 * To enter SMS code and continue
	 * 
	 * @param smsCode
	 * 			SMS code
	 */
	public Object enterSMSCodeAndContinue(String smsCode) {
		Object obj = null;
		waitForElementPresent(this.smsCode,ICommonConstants.WAIT_TIMEOUT);
		if (smsCode != null && isElementPresent(this.smsCode)) {
			this.clearAndType(this.smsCode, smsCode);
		} else {
			throw new IllegalStateException("SMS Code is null or SMS Code text field not found");			
		}
		this.verifySMSCode.click();
		try {
			obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get User id Creation confirmation page. PageFlowId: " + this.pageFlowId, e);
		}
		return obj;
	}
	
	/**
	 * To select Security question
	 * 
	 * @param question
	 * 			Security Question
	 */

	public void selectSecurityQuestion(String question) {
		if (isElementPresent(this.secretQuestionDropdown)) {
			this.selectDropDownOptionByVisibleText(this.secretQuestionDropdown, question);
		} else {
			throw new IllegalStateException("secret question dropdown not found");			
		} 
	}
	
	/**
	 * To Select Default security Question
	 * @throws InterruptedException 
	 */
	
	public void selectDefaultSecurityQuestion() throws InterruptedException {
		if (isElementPresent(this.secretQuestionDropdown)) {
			Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
			this.selectDropDownOptionByElement(this.secretQuestionDropdown, this.defaultSecretQuestion);
		} else {
			throw new IllegalStateException("default secret question not found");			
		} 
	}
	
	/**
	 * To enter Security question answer
	 * 
	 * @param answer
	 * 			Answer of the secret question
	 */
	
	public void setAnswer(String answer) throws InterruptedException {
		if (isElementPresent(this.secretAnswerTextBox)) {
			Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
			this.clearAndType(this.secretAnswerTextBox, answer);
		} else {
			throw new IllegalStateException("secret answer text box not found");			
		} 
	}
	
	/**
	 * To set default security answer
	 * @throws InterruptedException 
	 */
	
	public void setDefaultAnswer() throws InterruptedException {
		if (isElementPresent(this.secretAnswerTextBox)) {
			Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
			this.clearAndType(this.secretAnswerTextBox, ICimaCommonConstants.USER_SECURITY_QUESTION_ANSWER_DEFAULT);
		} else {
			throw new IllegalStateException("secret answer text box not found");			
		} 
	}
	
	/**
	 * To get secret Question HelpIcon Text 
	 * 
	 * @return Text of secret Question HelpIcon Text 
	 */
	
	public String getSecretQuestionHelpIconText() {
		String text = null;
		if (isElementPresent(this.secretQuestionHelpIcon)) {
			this.secretQuestionHelpIcon.click();
			waitForElementPresent(this.secretQuestionHelpIconText,10);
			if (isElementPresent(secretQuestionHelpIconText)) {
				text = this.secretQuestionHelpIconText.getText();
			}
			if (isElementPresent(this.secretQuestionHelpClose)) {
				this.secretQuestionHelpClose.click();
			}
		}
		return text;
	}
	
	/**
	 * To select agree term of service and privacy policy checkbox.
	 */
	public void agreeToTermOfServiceAndPrivacyPolicy() {
		if (isElementPresent(termsOfServiceCheck)) {
			this.selectCheckboxByID(termsOfServiceCheck);		
		} else {
			throw new IllegalStateException("agree to term and policy checkbox not found");			
		} 
	}
	

	/**
	 * To get "Term of service page"
	 * 
	 * @return Selenium page object of "Term of service"
	 */
	public Object getPageTermsOfService() {
		Object obj = null;
		if (isElementPresent(termsOfService)) {
			this.termsOfService.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get terms of service page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	/**
	 * To get privacy policy page
	 * 
	 * @return Selenium page object of privacy policy page"
	 */
	public Object getPagePrivacyPolicy() {
		Object obj = null;
		if (isElementPresent(privacyPolicy)) {
			this.privacyPolicy.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get privacy policy page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	
	/**
	 * To get collection of Error message  of create user page
	 * 
	 * @return map of errors of create user page
	 */
	
	public Map<String,String> getErrorMesssage() {
		Map<String,String> errors = new HashMap<String,String>();
		
		if (isElementPresent(userNameError)) {
			errors.put("userNameError",userNameError.getText());
		}
		if (isElementPresent(invalidPasswordError)) {
			errors.put("invalidPasswordError",invalidPasswordError.getText());
		}
		if (isElementPresent(passwordReTypeError)) {
			errors.put("passwordReTypeError",passwordReTypeError.getText());
		}
		if (isElementPresent(passwordNotMatchedError)) {
			errors.put("passwordNotMatchedError",passwordNotMatchedError.getText());
		}
		if (isElementPresent(altEmailInvalidError)) {
			errors.put("altEmailInvalidError",altEmailInvalidError.getText());
		}
		if (isElementPresent(phoneNumErrorMsg)) {
			errors.put("phoneNumErrorMsg",phoneNumErrorMsg.getText());
		}
		if (isElementPresent(secretQuestionError)) {
			errors.put("secretQuestionError",secretQuestionError.getText());
		}
		if (isElementPresent(secretAnswerError)) {
			errors.put("secretAnswerError",secretAnswerError.getText());
		}
		if (isElementPresent(termsofServiceErrorMsg)) {
			errors.put("termsofServiceErrorMsg",termsofServiceErrorMsg.getText());
		}
		
		return errors;
	}
	
	/**
	 * To submit user information from create user page
	 * 
	 * @return page object based on next page navigation
	 */
	public Object submitUserInformation() {
		Object obj = null;
		if (isElementPresent(continueButton)) {
			this.continueButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
				Thread.sleep(ICimaCommonConstants.WAIT_TIME_UI);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get Xfinity home page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
}
