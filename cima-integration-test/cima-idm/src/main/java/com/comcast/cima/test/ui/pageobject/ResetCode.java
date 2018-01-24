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

/**
 * This is Selenium Page Object for reset code web page.
 * 
 * @author Sumit Pal
 *
 */

public class ResetCode extends SeleniumPageObject<ResetCode> {
	
	/* Web Elements */
	
	/* Header */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "dk0-combobox")
	private WebElement selectOpType;
	
	@FindBy (how = How.ID, using = "dk0-readresetcode")
	private WebElement readResetCode;
	
	@FindBy (how = How.ID, using = "dk0-createresetcode")
	private WebElement createResetCode;
	
	@FindBy (how = How.ID, using = "dk0-deleteresetcode")
	private WebElement deleteResetCode;
	
	@FindBy (how = How.XPATH, using = "//div[@id='resetCodeFields']/label")
	private WebElement usernameLabel;
	
	@FindBy (how = How.XPATH, using = "//div[@id='resetCodeFields']/input")
	private WebElement usernameText;
	
	@FindBy (how = How.ID, using = "resetcodeoperation")
	private WebElement continueButton;
	
	@FindBy (how = How.ID, using = "resetCode")
	private WebElement resetCode;

	@Override
	protected void load() {
		try{
			String url = getURLToLoad();
			this.driver.get(url);
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Reset Code page. ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(headerMessage));
	}
	

	public ResetCode(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	/**
	 * To  create reset code
	 * 
	 * @param userId 
	 *                User ID
	 * @return reset codes               
	 */
	public String createResetCode(String userId) {
		String code = null;
		String[] text = null;
		
		if (isElementPresent(this.selectOpType)) {
			selectDropDownOptionByElement(selectOpType,createResetCode);
			waitForElementPresent(usernameText,ICommonConstants.WAIT_TIMEOUT);
			if (userId.contains("@")) {
				String[] tokens = userId.split("@");
				userId = tokens[0];
			}
			this.clearAndType(this.usernameText, userId+"@comcast.net");
			this.continueButton.click();
			waitForElementPresent(this.resetCode,ICommonConstants.WAIT_TIMEOUT);
			text = resetCode.getText().split(" ")[4].split("R");
			code = text[0];
			
		}
		closeWindow();
		
		return code;
	}
	
	/**
	 * To close current window
	 */
	public void closeWindow() {
		this.driver.close();
	}
	
	/**
	 * To get Url of reset code page
	 * 
	 * @return  URL of the reset code
	 * 
	 * @throws Exception
	 */
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.IDMUrlPropKeys.RESET_CODE_UI.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(ResetCode.class);

}
