package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Xfinity Device Activation" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityDeviceActivation extends SeleniumPageObject<XfinityDeviceActivation> {

	public XfinityDeviceActivation(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public XfinityDeviceActivation(WebDriver driver, String verficationUrl){
		this.driver = driver;
		this.verficationUrl = verficationUrl;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.endsWith("Activate Device"));		
	}

	@Override
	protected void load() {
		this.driver.get(this.verficationUrl);
	}

	/**
	 * To activate device using user code
	 * 
	 * @param userCode :
	 *                  Usercode
	 * @return Object of next page
	 * @throws Exception
	 */
	public Object activateDevice(String userCode) throws Exception{
		Object result = null;
		
		if(isElementPresent(activateDeviceButton) && isElementPresent(activationCodeText)){
			activationCodeText.sendKeys(userCode);
			activateDeviceButton.click();
			result = (isElementPresent(title) && isElementPresent(error)) ? error.getText() : 
						ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		
		return result;
	}
	
	
	@FindBy(how = How.ID, using = "user_code")
	private WebElement activationCodeText;
	
	@FindBy(how = How.XPATH, using = "//form/button")
	private WebElement activateDeviceButton;
	
	@FindBy(how = How.ID, using = "user-code-error")
	private WebElement error;
	
	@FindBy(how = How.XPATH, using = "//main/h1")
	private WebElement title;
	
	private String verficationUrl = null;
}
