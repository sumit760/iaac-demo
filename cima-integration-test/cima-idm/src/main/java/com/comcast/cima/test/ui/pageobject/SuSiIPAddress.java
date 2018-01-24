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

public class SuSiIPAddress extends SeleniumPageObject<SuSiIPAddress> {
	private static Logger logger = LoggerFactory.getLogger(SuSiIPAddress.class);
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p[2]")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/form/div[1]/input")
	private WebElement deviceMakeText;
	
	@FindBy (how = How.XPATH, using = "//main/form/div[2]/input")
	private WebElement deviceModelText;	
	
	@FindBy (how = How.XPATH, using = "//main/form/div[3]/input")
	private WebElement macAddressText;	
	
	@FindBy (how = How.XPATH, using = "//main/form/div[4]/input")
	private WebElement ipAddressText;	
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div/button")
	private WebElement nextButton;
	
	public SuSiIPAddress(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while loading SuSiIPAddress Page: " + getURLToLoad(), e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.endsWith("/device-test"), "Failed to land on SuSi IP Address page");	
	}
	
	/* Action Methods */
	public void enterDeviceMake(String deviceMake) {
		if (isElementPresent(this.deviceMakeText)) {
			this.clearAndType(this.deviceMakeText, deviceMake);
		} else {
			throw new IllegalStateException("device make text field not found");
		}
	}
	
	public void enterDeviceModel(String deviceModel) {
		if (isElementPresent(this.deviceModelText)) {
			this.clearAndType(this.deviceModelText, deviceModel);
		} else {
			throw new IllegalStateException("device model text field not found");
		}
	}
	
	public void enterMacAddress(String macAddress) {
		if (isElementPresent(this.macAddressText)) {
			this.clearAndType(this.macAddressText, macAddress);
		} else {
			throw new IllegalStateException("Mac address text field not found");
		}
	}
	
	public void enterIpAddress(String ipAddress) {
		if (isElementPresent(this.ipAddressText)) {
			this.clearAndType(this.ipAddressText, ipAddress);
		} else {
			throw new IllegalStateException("IP address text field not found");
		}
	}
	
	/* Transition Method */
	public Object pageNext() {
		Object obj = null;
		if (isElementPresent(this.nextButton)) {
			this.nextButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering mobile page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	private String getURLToLoad() {
		try {
			return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).
					getString(IDataProviderEnums.IDMUrlPropKeys.SUSI_IP.getValue(), ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
		} catch (Exception e) {
			throw new RuntimeException("Failed to get the SuSi IP address page. PageFlowId: " + this.pageFlowId, e);
		}
	}
	
}
