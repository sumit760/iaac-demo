package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.comcast.test.citf.common.http.SeleniumHelper;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for "Xfinity Device Activation Confirmation" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityDeviceActivationConfirmation extends SeleniumPageObject<XfinityDeviceActivationConfirmation> {

	public XfinityDeviceActivationConfirmation(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void isLoaded() throws Error {
	}

	@Override
	protected void load() {
	}
	
	/**
	 * To go to Application
	 * 
	 * @return object of next page
	 * @throws Exception
	 */
	public Object goToApp() throws Exception{
		if(isElementPresent(goToAppButton)){
			goToAppButton.click();
			waitForPageLoaded(this.driver);
			SeleniumHelper.waitForElementByXpath(driver, "//main/p[1]", ICommonConstants.WAIT_TIMEOUT);

			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		}
		
		return null;
	}
	
	@FindBy(how = How.XPATH, using = "//form[@id='confirmationForm']/button")
	private WebElement goToAppButton;
	
}
