package com.comcast.cima.test.ui.pageobject;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiUpdateSetting extends SeleniumPageObject<SuSiUpdateSetting> {
	
	private static final String UPDATING_YOUR_SETTINGS = "Updating Your Settings";
	
	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	public SuSiUpdateSetting(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiUpdateSetting page");	
	}
	
	public boolean isUpdatingPresent(){
		return isElementPresent(this.headerMessage) && this.headerMessage.getText().contains("Hang Tight");
	}
	
	/* Transition Method */
	public Object pageNext(String inSeconds) {
		try {
			TimeUnit.SECONDS.sleep(Integer.parseInt(inSeconds));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		} catch (Exception e) {
			throw new RuntimeException("Failed to get the SuSi all set page. PageFlowId: " + this.pageFlowId, e);
		}
	}

}
