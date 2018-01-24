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
 * This is Selenium Page Object for "Verify User after Creation" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */

public class VerifyUserAfterCreation extends SeleniumPageObject<VerifyUserAfterCreation> {
	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerGreetingMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement greeting;
	
	@FindBy (how = How.XPATH, using = "//main/p[2]")
	private WebElement emailSentConfirmation;
	
	@FindBy (how = How.XPATH, using = "//main/h2")
	private WebElement emailVerificationMessageHeader;
	
	@FindBy (how = How.XPATH, using = "//main/ol")
	private WebElement emailVerificationMessage;
	
	@FindBy (how = How.XPATH, using = "//main/section[1]/header/h2")
	private WebElement preferredEmailHeader;
	
	@FindBy (how = How.XPATH, using = "//main/section[1]/div/p[@class='li']")
	private WebElement preferredEmail;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/header/h2")
	private WebElement emailAccountHeader;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/div[@class='ul']")
	private WebElement emailAccounts;
	
	@FindBy (how = How.XPATH, using = "//main/section[2]/header/menu/menuitem/a")
	private WebElement changeEmailAccount;
	
	@FindBy (how = How.XPATH, using = "//a[@class='button secondary noprint']")
	private WebElement printThis;
	
	@FindBy (how = How.XPATH, using = "//a[@class='button submit noprint']")
	private WebElement continuePage;
	

	public VerifyUserAfterCreation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerGreetingMessage));
	}
	
	/**
	 * To validate page for any error
	 * 
	 * @param altEmail:
	 *                 Alternate email
	 * @return map which contains all the error message                
	 */
	public Map<String,String> validatePage(boolean altEmail) {
		Map<String,String> returnMap = new HashMap<String,String>();
		
		if (altEmail) {
			if (!isElementPresent(this.headerGreetingMessage)) {
				returnMap.put("HEADER GREETING", "NOT FOUND");
			}
			if (!isElementPresent(this.emailSentConfirmation)) {
				returnMap.put("EMAIL SENT MESSAGE(We have just sent an email to:)", "NOT FOUND");
			}
			if (!isElementPresent(this.emailVerificationMessageHeader)) {
				returnMap.put("EMAIL VERIFICATION MESSAGE HEADER(To verify <Alt Email> as your preferred email and XFINITY username)", "NOT FOUND");
			}
			if (!isElementPresent(this.emailVerificationMessage)) {
				returnMap.put("EMAIL VERIFICATION MESSAGE(Check your email/Check the link to verify your email. This link will expire in 72 hours)", "NOT FOUND");
			}
			if (!isElementPresent(this.preferredEmailHeader)) {
				returnMap.put("PREFERRED EMAIL HEADER MESSAGE(Your preferred email)", "NOT FOUND");
			}
			if (!isElementPresent(this.preferredEmail)) {
				returnMap.put("PREFERRED EMAIL(Your alternate email provided)", "NOT FOUND");
			}
			if (!isElementPresent(this.emailAccountHeader)) {
				returnMap.put("EMAIL ACCOUNT HEADER MESSAGE(Your comcast.net account)", "NOT FOUND");
			}
			if (!isElementPresent(this.emailAccounts)) {
				returnMap.put("EMAIL ACCOUNTS", "NOT FOUND");
			}
		}
		else {
			if (!isElementPresent(this.headerGreetingMessage)) {
				returnMap.put("HEADER GREETING", "NOT FOUND");
			}
			if (!isElementPresent(this.greeting)) {
				returnMap.put("GREETING", "NOT FOUND");
			}
			if (!isElementPresent(this.preferredEmailHeader)) {
				returnMap.put("PREFERRED EMAIL HEADER MESSAGE(Your email and XFINITY username)", "NOT FOUND");
			}
			if (!isElementPresent(this.preferredEmail)) {
				returnMap.put("PREFERRED EMAIL", "NOT FOUND");
			}
		}
		
		if (returnMap.size() > 0)
			returnMap.put(ICimaCommonConstants.IDM_USER_CREATE_STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
		else
			returnMap.put(ICimaCommonConstants.IDM_USER_CREATE_STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
		
		return returnMap;
		
	}
	
	/**
	 * To find cause of failure
	 * 
	 * @param failureDetails:
	 *                       Map of failure details
	 *                       
	 *@return String stating casue of failure                       
	 * 
	 */
	
	public String findCauseofFailure(Map<String,String> failureDetails) {
		
		StringBuilder causeString = new StringBuilder();
		if (failureDetails!=null && !failureDetails.isEmpty() && 
				failureDetails.get(ICimaCommonConstants.IDM_USER_CREATE_STATUS).equalsIgnoreCase(ICommonConstants.OPERATION_STATUS_ERROR)) {

			if (failureDetails.containsKey("HEADER GREETING"))					
				causeString.append(failureDetails.get("HEADER GREETING"));
			if (failureDetails.containsKey("EMAIL SENT MESSAGE(We have just sent an email to:)"))					
				causeString.append(failureDetails.get("EMAIL SENT MESSAGE(We have just sent an email to:)"));
			if (failureDetails.containsKey("EMAIL VERIFICATION MESSAGE HEADER(To verify <Alt Email> as your preferred email and XFINITY username)"))					
				causeString.append(failureDetails.get("EMAIL VERIFICATION MESSAGE HEADER(To verify <Alt Email> as your preferred email and XFINITY username)"));
			if (failureDetails.containsKey("EMAIL VERIFICATION MESSAGE(Check your email/Check the link to verify your email. This link will expire in 72 hours)"))					
				causeString.append(failureDetails.get("EMAIL VERIFICATION MESSAGE(Check your email/Check the link to verify your email. This link will expire in 72 hours)"));
			if (failureDetails.containsKey("PREFERRED EMAIL HEADER MESSAGE(Your preferred email)"))					
				causeString.append(failureDetails.get("PREFERRED EMAIL HEADER MESSAGE(Your preferred email)"));
			if (failureDetails.containsKey("PREFERRED EMAIL(Your alternate email provided)"))					
				causeString.append(failureDetails.get("PREFERRED EMAIL(Your alternate email provided)"));
			if (failureDetails.containsKey("EMAIL ACCOUNT HEADER MESSAGE(Your comcast.net account)"))					
				causeString.append(failureDetails.get("EMAIL ACCOUNT HEADER MESSAGE(Your comcast.net account)"));
			if (failureDetails.containsKey("EMAIL ACCOUNTS)"))					
				causeString.append(failureDetails.get("EMAIL ACCOUNTS"));
			if (failureDetails.containsKey("PREFERRED EMAIL HEADER MESSAGE(Your email and XFINITY username)"))					
				causeString.append(failureDetails.get("PREFERRED EMAIL HEADER MESSAGE(Your email and XFINITY username)"));
			if (failureDetails.containsKey("PREFERRED EMAIL"))					
				causeString.append(failureDetails.get("PREFERRED EMAIL"));
		}
		
		return causeString.toString();
	}
	
	/**
	 * To click continue
	 * 
	 * @return page object of next page based on application flow
	 */
	public Object clickContinue() throws Exception {
		if (isElementPresent(this.continuePage)) {
			this.continuePage.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	

}
