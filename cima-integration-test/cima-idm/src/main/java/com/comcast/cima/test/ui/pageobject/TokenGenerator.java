package com.comcast.cima.test.ui.pageobject;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Token Generator" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */


public class TokenGenerator extends SeleniumPageObject<TokenGenerator> {
	
	/* Web Elements */
	
	/* Header */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "dk0-combobox")
	private WebElement selectEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Family-Connect Encoder")
	private WebElement familyConnectEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Facebook-Connect Encoder")
	private WebElement facebookConnectEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Pre-Activation-Encoder")
	private WebElement preActivationEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Commercial-Automatic Account Creation Encoder")
	private WebElement commercialAccountEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Restart/Transfer-Token Encoder")
	private WebElement restartTransferEncoder;
	
	@FindBy (how = How.ID, using = "dk0-Verified-Email Encoder")
	private WebElement verifiedEmailEncoder;
	
	@FindBy (how = How.ID, using = "dk0-IDM-Portal - Password Reset Email Token Encoder")
	private WebElement idmPasswordResetEmailToken;
	
	@FindBy (how = How.ID, using = "dk0-Einstein-- Password Reset Email Token Encoder")
	private WebElement einsteinPasswordResetEmailToken;
	
	
	/* Web Elements  */
	
	
	@FindBy (how = How.NAME, using = "emailAddress")
	private WebElement emailField;
		
	@FindBy (how = How.NAME, using = "verified")
	private WebElement verifyEmail;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[1]")
	private WebElement field1;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[2]")
	private WebElement field2;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[3]")
	private WebElement field3;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[4]")
	private WebElement field4;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[5]")
	private WebElement field5;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[6]")
	private WebElement field6;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[7]")
	private WebElement field7;
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/input[8]")
	private WebElement field8;
	
	
	@FindBy (how = How.XPATH, using = "//div[@id='tokenfields']/select[@class='valid']")
	private WebElement field9;
	
	
	/* Generate Token */
	@FindBy (how = How.ID, using = "GENERATE_TOKEN")
	private WebElement generateToken;
	
	@FindBy (how = How.ID, using = "token")
	private WebElement tokenGenerated;
	
	
	@Override
	protected void load() {
		try{
			String url = getURLToLoad();
			this.driver.get(url);
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading token generator page. ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	
	public TokenGenerator(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * To create family connect token
	 * 
	 * @param firstName:
	 *                 FirstName
	 * @param lastName :
	 *                 LastName
	 * @param inviteMethod :
	 *                     Invite Method
	 * @param accountGuid :
	 *                    Account ID
	 * @param contactEmail :
	 *                     Contact Email ID
	 * @param phone :
	 *              Phone
	 * @param notBefore :
	 *                  Not Before
	 * @param notOnOrAfter :-
	 *                     Not After
	 * @return family connect token
	 */
	public String createFamilyConnectToken(String firstName, String lastName, 
										   String inviteMethod, String accountGuid, 
										   String contactEmail, String phone, 
										   String notBefore, String notOnOrAfter) {
		
		String token = null;
		if (!StringUtils.isEmpty(firstName) && !StringUtils.isEmpty(lastName) && !StringUtils.isEmpty(inviteMethod) && !StringUtils.isEmpty(accountGuid)) {
			
			if (isElementPresent(this.selectEncoder)) {
				selectDropDownOptionByElement(selectEncoder,familyConnectEncoder);
				waitForElementPresent(this.field1,ICommonConstants.WAIT_TIMEOUT);
			}
			
			if (isElementPresent(this.field1) && isElementPresent(this.field2) && isElementPresent(this.field3) &&
					isElementPresent(this.field4)) {
				
				this.clearAndType(this.field1, firstName);
				this.clearAndType(this.field2, lastName);
				this.clearAndType(this.field3, inviteMethod);
				this.clearAndType(this.field4, accountGuid);
				if (notBefore != null && notOnOrAfter != null &&
						!notBefore.isEmpty() && !notOnOrAfter.isEmpty()) {
					
					this.clearAndType(this.field5, notBefore);
					this.clearAndType(this.field6, notOnOrAfter);
				}
				if (contactEmail!=null && phone!=null &&
						!contactEmail.isEmpty() && !phone.isEmpty()) {
					
					this.clearAndType(this.field7, contactEmail);
					this.clearAndType(this.field8, phone);
				}
				
				this.generateToken.click();
				waitForElementPresent(this.tokenGenerated,ICommonConstants.WAIT_TIMEOUT);
				if (isElementPresent(this.tokenGenerated)) {
					token = this.tokenGenerated.getAttribute("value");
				}
			}
		}
		
		return token;
		
	}
	
	
	/**
	 * To create family connect token
	 * 
	 * @param email:
	 *                 email
	
	 * @param contactEmail :
	 *                     Contact Email ID
	 * @param notBefore :
	 *                  Not Before
	 * @param notOnOrAfter :-
	 *                     Not After
	 * @return family connect token
	 */
	public String createEmailTokenTrue(String email, String verificationFlag ,String notBefore, String notOnOrAfter) {
		
		String token = null;
		if (email!=null && verificationFlag!=null) {
			
			if (isElementPresent(this.selectEncoder)) {
				selectDropDownOptionByElement(selectEncoder,verifiedEmailEncoder);
				waitForElementPresent(this.emailField,ICommonConstants.WAIT_TIMEOUT);
			}
			
			if (isElementPresent(this.emailField) && isElementPresent(this.verifyEmail)){
							
				this.clearAndType(this.emailField, email);
				selectDropDownOptionByVisibleText(verifyEmail,verificationFlag);
				if (notBefore != null && notOnOrAfter != null &&
						!notBefore.isEmpty() && !notOnOrAfter.isEmpty()) {
					
					this.clearAndType(this.field5, notBefore);
					this.clearAndType(this.field6, notOnOrAfter);
				}
				
				this.generateToken.click();
				waitForElementPresent(this.tokenGenerated,ICommonConstants.WAIT_TIMEOUT);
				if (isElementPresent(this.tokenGenerated)) {
					token = this.tokenGenerated.getAttribute("value");
				}
			}
		}
		
		return token;
		
	}

	
	

	/**
	 * To create preactivation token
	 * 
	 * @param billingAccountId :
	 *                          Billing Account ID
	 * @param accountPhoneNumber :
	 *                            Account Phone Number
	 * @param firstName :
	 *                   First Name
	 * @param lastName :
	 *                 Last Name                 
	 * @param contactEmail:
	 *                    Contact Email
	 * @param notBefore :
	 *                   Not Before
	 * @param notOnOrAfter:
	 *                    Not After
	 * @param userName : 
	 *                 UserName
	 * @return preactivation token
	 */
	
	public String createPreActivationToken(String billingAccountId, String accountPhoneNumber, 
			   							   String firstName, String lastName, 
			   							   String contactEmail, String notBefore, 
			   							   String notOnOrAfter, String userName) {

		String token = null;
		if (billingAccountId!=null && accountPhoneNumber!=null && firstName!=null && lastName!=null && contactEmail!=null &&
				!billingAccountId.isEmpty() && !accountPhoneNumber.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !contactEmail.isEmpty()) {

			if (isElementPresent(this.selectEncoder)) {
				selectDropDownOptionByElement(selectEncoder,preActivationEncoder);
				waitForElementPresent(this.field1,ICommonConstants.WAIT_TIMEOUT);
			}

			if (isElementPresent(this.field1) && isElementPresent(this.field2) && isElementPresent(this.field3) &&
					isElementPresent(this.field4) && isElementPresent(this.field5)) {

				this.clearAndType(this.field1, billingAccountId);
				this.clearAndType(this.field2, accountPhoneNumber);
				this.clearAndType(this.field3, firstName);
				this.clearAndType(this.field4, lastName);
				this.clearAndType(this.field5, contactEmail);
				if (notBefore != null && notOnOrAfter != null &&
						!notBefore.isEmpty() && !notOnOrAfter.isEmpty()) {

					this.clearAndType(this.field6, notBefore);
					this.clearAndType(this.field7, notOnOrAfter);
				}
				if (userName!=null && !userName.isEmpty()) {
					this.clearAndType(this.field8, userName);
				}

				this.generateToken.click();
				waitForElementPresent(this.tokenGenerated,ICommonConstants.WAIT_TIMEOUT);
				if (isElementPresent(this.tokenGenerated)) {
					token = this.tokenGenerated.getText();
				}
			}
		}
		return token;
	}
	
	/**
	 * To create transfer token
	 * 
	 * @param uid :
	 *            UID
	 * @param cstCustGuid :
	 *                    Customer GUID
	 * @param oldBillingAccountId :
	 *                           Old Billing ID
	 * @param rememberMe : 
	 *                   Remember me
	 * @param continueUrl :
	 *                    Continue URL
	 * @param notBefore :
	 *                  Not Before
	 * @param notOnOrAfter :
	 *                     Not on or after
	 * @return restart transfer token
	 */
	
	public String createRestartTransferToken(String uid, String cstCustGuid, 
			   							     String oldBillingAccountId, String rememberMe, 
			   							     String continueUrl, String notBefore, 
			   							     String notOnOrAfter) {

		String token = null;
		if (uid!=null && cstCustGuid!=null && oldBillingAccountId!=null && continueUrl!=null && rememberMe!=null &&
			!uid.isEmpty() && !cstCustGuid.isEmpty() && !oldBillingAccountId.isEmpty() && !continueUrl.isEmpty()) {

			if (isElementPresent(this.selectEncoder)) {
				selectDropDownOptionByElement(selectEncoder,restartTransferEncoder);
				waitForElementPresent(this.field1,ICommonConstants.WAIT_TIMEOUT);
			}

			if (isElementPresent(this.field1) && isElementPresent(this.field2) && isElementPresent(this.field3) &&
				isElementPresent(this.field9) && isElementPresent(this.field4) && isElementPresent(this.field5)) {

				this.clearAndType(this.field1, uid);
				this.clearAndType(this.field2, cstCustGuid);
				this.clearAndType(this.field3, oldBillingAccountId);
				selectDropDownOptionByVisibleText(this.field9,rememberMe);
				this.clearAndType(this.field4, continueUrl);
				if (notBefore != null && notOnOrAfter != null &&
						!notBefore.isEmpty() && !notOnOrAfter.isEmpty()) {

					this.clearAndType(this.field5, notBefore);
					this.clearAndType(this.field6, notOnOrAfter);
				}

				this.generateToken.click();
				waitForElementPresent(this.tokenGenerated,ICommonConstants.WAIT_TIMEOUT);
				if (isElementPresent(this.tokenGenerated)) {
					token = this.tokenGenerated.getText();
				}
			}
		}
		return token;
	}
	
	/**
	 * To close current window
	 * 
	 */
	public void closeWindow() {
		this.driver.close();
	}
	
	/**
	 * To get URL of token generator page from database based on current environment
	 * 
	 * @return URL of token generator page
	 * @throws Exception
	 */
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.IDMUrlPropKeys.TOKEN_GENERATOR.getValue(), 
												ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(TokenGenerator.class);
}
