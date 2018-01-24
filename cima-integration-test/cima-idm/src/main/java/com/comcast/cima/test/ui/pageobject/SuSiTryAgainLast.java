package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiTryAgainLast extends SeleniumPageObject<SuSiTryAgainLast> {
	private static final String TRY_THAT_AGAIN = "Let's Try That Again";
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/div[2]/section[1]/div")
	private WebElement wifiName;
	
	@FindBy (how = How.XPATH, using = "//main/div[2]/section[2]/div")
	private WebElement wifiPasswd;
	
	@FindBy (how = How.ID, using = "retryButton")
	private WebElement tryAgainButton;
	
	public SuSiTryAgainLast(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isTryAgainPresent(), "Failed to land on SuSiTryAgainLast page");	
	}
	
	public boolean isTryAgainPresent(){
		return isElementPresent(this.headerMessage) && this.headerMessage.getText().equals(TRY_THAT_AGAIN);
	}
	
	public boolean isWiFiNamePresent(){
		return isElementPresent(this.wifiName);
	}
	
	public boolean isWiFiPasswdPresent(){
		return isElementPresent(this.wifiPasswd);
	}

	/* Transition Method */
	public Object tryAgain() {
		Object obj = null;
		if (isElementPresent(this.tryAgainButton)) {
			this.tryAgainButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi all set page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
}
