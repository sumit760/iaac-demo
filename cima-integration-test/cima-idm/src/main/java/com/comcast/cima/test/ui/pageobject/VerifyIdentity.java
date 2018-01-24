package com.comcast.cima.test.ui.pageobject;

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
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * This is Selenium Page Object for "Verify Identity" web page.
 * 
 * @author Sumit Pal
 *
 */

public class VerifyIdentity extends SeleniumPageObject<VerifyIdentity> {

	
	/* Web Elements */
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	/*User identity types elements*/
	
	@FindBy (how = How.ID, using = "byPhone_radio")
	private WebElement byPhone;
	
	@FindBy (how = How.ID, using = "byPhone_label")
	private WebElement byPhoneLabel;
	
	@FindBy (how = How.ID, using = "bySSN_radio")
	private WebElement bySSN;
	
	@FindBy (how = How.ID, using = "bySSN_label")
	private WebElement bySSNLabel;
	
	@FindBy (how = How.ID, using = "byAccountNumber_radio")
	private WebElement byAccount;
	
	@FindBy (how = How.ID, using = "byAccountNumber_label")
	private WebElement byAccountLabel;
	
	@FindBy (how = How.ID, using = "submitButton")
	private WebElement submit;
	
	private String customUrl;
	
	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public VerifyIdentity(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public VerifyIdentity(String inCustomUrl, WebDriver driver) {
		this.customUrl = inCustomUrl;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading verifiy identity page. ", e);
		}
	}
	
	
	/**
	 * To verify identity by account number
	 * 
	 * Note: This usually returns the SecurityCheck page, but not always.
	 *
	 * @return page object of security check page or page object of the next page according to application flow
	 * @throws Exception
	 */
	public Object verifyIdentityByAccountNumber() {
		if (isElementPresent(byAccount) && isElementPresent(byAccountLabel) && isElementPresent(submit)){
			this.byAccount.click();
			this.submit.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * To verify identity by SSN
	 * Note: This usually returns the SecurityCheck page, but not always.
	 *
	 * @return page object of security check page or page object of the next page according to application flow
	 * @throws Exception
	 */
	public Object verifyIdentityBySSN() {
		if (isElementPresent(bySSN) && isElementPresent(bySSNLabel) && isElementPresent(submit)){
			this.bySSN.click();
			this.submit.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}

	/**
	 * To verify identity by Mobile
	 * Note: This usually returns the SecurityCheck page, but not always.
	 *
	 * @return page object of security check page or page object of the next page according to application flow
	 * @throws Exception
	 */
	
	public Object verifyIdentityByMobile() {
		if (isElementPresent(byPhone) && isElementPresent(byPhoneLabel) && isElementPresent(submit)){
			this.byPhone.click();
			this.submit.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	private String getURLToLoad() throws Exception {
		String outValue;
		if (this.customUrl != null) {
			outValue = this.customUrl;
		} else {
			outValue = ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_SIGNIN.getValue(), ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
		}
		return outValue;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignInToXfinity.class);
	
}
