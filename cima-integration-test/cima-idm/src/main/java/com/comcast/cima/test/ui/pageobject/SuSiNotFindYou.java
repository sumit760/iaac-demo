package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiNotFindYou extends SeleniumPageObject<SuSiNotFindYou> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/form/div/button")
	private WebElement signInButton;
	
	@FindBy (how = How.ID, using = "createUidButton")
	private WebElement createUIDButton;
	
	@FindBy (how = How.XPATH, using = "//main/form/nav/div/button")
	private WebElement backButton;
	
	
	public SuSiNotFindYou(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiNotFindYou page");	
	}

	/* Transition Method */
	public Object signIn() {
		Object obj = null;
		if (isElementPresent(this.signInButton)) {
			this.signInButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering credential page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object createUID() {
		Object obj = null;
		if (isElementPresent(this.createUIDButton)) {
			this.createUIDButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the IDM creating UID page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
	public Object pageBack() {
		Object obj = null;
		if (isElementPresent(this.backButton)) {
			this.backButton.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering mobile page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}
	
}
