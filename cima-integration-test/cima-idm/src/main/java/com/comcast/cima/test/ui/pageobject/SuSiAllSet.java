package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;

public class SuSiAllSet extends SeleniumPageObject<SuSiAllSet> {
	private static final String ALL_SET = "All Set";

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/div[2]/section[1]/div")
	private WebElement wifiName;
	
	@FindBy (how = How.XPATH, using = "//main/div[2]/section[2]/div")
	private WebElement wifiPasswd;
	
	@FindBy (how = How.ID, using = "redirectSmartInternet")
	private WebElement viewWifiSettingButton;
	
	@FindBy (how = How.ID, using = "connectionHelp")
	private WebElement helpConnectingLink;
	
	public SuSiAllSet(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isAllSetPresent(), "Error occurred while loading SuSiAllSet Page");
	}
	
	public boolean isAllSetPresent(){
		return isElementPresent(this.headerMessage) && this.headerMessage.getText().equals(ALL_SET);
	}
	
	public boolean isWiFiNamePresent(){
		return isElementPresent(this.wifiName);
	}
	
	public boolean isWiFiPasswdPresent(){
		return isElementPresent(this.wifiPasswd);
	}
	
	/* Action Methods */
	public void viewYourWiFiSettings() {
		if (isElementPresent(this.viewWifiSettingButton)) {
			this.viewWifiSettingButton.click();
		} else {
			throw new IllegalStateException("view WiFi setting button not found");
		}
	}
	
	public void needHelpConnectingYourDevices() {
		if (isElementPresent(this.helpConnectingLink)) {
			this.helpConnectingLink.click();
		} else {
			throw new IllegalStateException("need help connecting your devices link not found");
		}
	}

}
