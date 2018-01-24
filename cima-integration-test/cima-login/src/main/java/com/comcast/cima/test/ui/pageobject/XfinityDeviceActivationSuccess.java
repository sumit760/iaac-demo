package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Xfinity Device Activation Success" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityDeviceActivationSuccess extends SeleniumPageObject<XfinityDeviceActivationSuccess>{

	public XfinityDeviceActivationSuccess(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void isLoaded() throws Error {
	}

	@Override
	protected void load() {
	}
	
	@FindBy(how = How.XPATH, using = "//main/h1")
	private WebElement congratulationMessage;
	
	@FindBy(how = How.XPATH, using = "//main/p[1]")
	private WebElement deviceActivationSuccessMessage;
	
	public boolean isCongratulationMessagePresent() {
		return isElementPresent(this.congratulationMessage);
	}
	
	public boolean isDeviceActivationSuccessMessagePresent() {
		return isElementPresent(this.deviceActivationSuccessMessage);
	}
}
