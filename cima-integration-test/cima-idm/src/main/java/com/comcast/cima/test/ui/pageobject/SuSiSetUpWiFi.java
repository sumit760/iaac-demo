package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiSetUpWiFi extends SeleniumPageObject<SuSiSetUpWiFi> {
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.ID, using = "existingWifi_radio")
	private WebElement existingWifiRadio;
	
	@FindBy (how = How.ID, using = "newWifiAccount_radio")
	private WebElement newWifiRadio;
	
	@FindBy (how = How.ID, using = "wifiSSID24")
	private WebElement wifi24IdText;
	
	@FindBy (how = How.ID, using = "wifiPassword24")
	private WebElement wifi24PasswordText;
	
	@FindBy (how = How.ID, using = "showPassword_checkbox")
	private WebElement showPasswordCheckBox;
	
	@FindBy (how = How.ID, using = "moreSettings")
	private WebElement moreSettingsLink;
	
	@FindBy (how = How.ID, using = "dualConfigCheckBox_checkbox")
	private WebElement sameSettingCheckBox;
	
	@FindBy (how = How.ID, using = "wifiSSID50")
	private WebElement wifi50IdText;
	
	@FindBy (how = How.ID, using = "wifiPassword50")
	private WebElement wifi50PasswordText;
	
	@FindBy (how = How.ID, using = "textMsgAgreementAccepted_checkbox")
	private WebElement receiveMessageCheckBox;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[1]/button")
	private WebElement backButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div[2]/button")
	private WebElement nextButton;
	
	public SuSiSetUpWiFi(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiSetUpWiFi page");	
	}
	
	/* Action Methods */
	public void selectExistingWifiOption() {
		if (isElementPresent(this.existingWifiRadio)) {
			this.existingWifiRadio.click();
		} else {
			throw new IllegalStateException("Existing Wifi Option radio button not found");
		}
	}
	
	public void selectNewWifiOption() {
		if (isElementPresent(this.newWifiRadio)) {
			this.newWifiRadio.click();
		} else {
			throw new IllegalStateException("New Wifi Option radio button not found");			
		}
	}
	
	public void enterWifi24Id(String wifi24Id) {
		if (isElementPresent(this.wifi24IdText)) {
			this.clearAndType(this.wifi24IdText, wifi24Id);
		} else {
			throw new IllegalStateException("2.4GHz WiFi name text field not found");
		}
	}
	
	public void enterWifi24Passwd(String wifi24Passwd) {
		if (isElementPresent(this.wifi24PasswordText)) {
			this.clearAndType(this.wifi24PasswordText, wifi24Passwd);
		} else {
			throw new IllegalStateException("2.4GHz WiFi password text field not found");
		}
	}
	
	public void showPassword() {
		if (isElementPresent(this.showPasswordCheckBox)) {
			this.showPasswordCheckBox.click();
		} else {
			throw new IllegalStateException("show password link not found");
		}
	}
	
	public void showMoreSettings() {
		if (isElementPresent(this.moreSettingsLink)) {
			this.moreSettingsLink.click();
		} else {
			throw new IllegalStateException("show more settings link not found");
		}
	}
	
	public void useSeparateWifiSettings() {
		if (isElementPresent(this.sameSettingCheckBox)) {
			this.sameSettingCheckBox.click();	
		} else {
			throw new IllegalStateException("use separate WiFi settings link not found");
		}
	}
	
	public void enterWifi50Id(String wifi50Id) {
		if (isElementPresent(this.wifi50IdText)) {
			this.clearAndType(this.wifi50IdText, wifi50Id);
		} else {
			throw new IllegalStateException("5GHz WiFi name text field not found");
		}
	}
	
	public void enterWifi50Passwd(String wifi50Passwd) {
		if (isElementPresent(this.wifi50PasswordText)) {
			this.clearAndType(this.wifi50PasswordText, wifi50Passwd);
		} else {
			throw new IllegalStateException("5GHz WiFi password text field not found");
		}
	}
	
	public void agreeReceivingMessage() {
		if (isElementPresent(this.receiveMessageCheckBox)) {
			this.receiveMessageCheckBox.click();
		} else {
			throw new IllegalStateException("agree to receive text message check box not found");
		}
	}
	
	/* Transition Method */
	public Object pageBack() {
		Object obj = null;
		if (isElementPresent(this.backButton)) {
			this.backButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi your account page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object pageNext() {
		Object obj = null;
		if (isElementPresent(this.nextButton)) {
			this.nextButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the updating your settings page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
}
