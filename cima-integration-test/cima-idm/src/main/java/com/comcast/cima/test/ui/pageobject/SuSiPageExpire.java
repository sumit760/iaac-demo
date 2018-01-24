package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class SuSiPageExpire extends SeleniumPageObject<SuSiPageExpire> {

	/* Header Message */
	@FindBy (how = How.XPATH, using = "//main/h1")
	private WebElement headerMessage;
	
	@FindBy (how = How.XPATH, using = "//main/p")
	private WebElement description;
	
	@FindBy (how = How.XPATH, using = "//main/p/a")
	private WebElement tryAgainLink;
	
	public SuSiPageExpire(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.headerMessage), "Failed to land on SuSiPageExpire page");	
	}
	
	/* Transition Method */
	public Object tryAgain() {
		Object obj = null;
		if (isElementPresent(this.tryAgainLink)) {
			this.tryAgainLink.click();
			try {
				obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new RuntimeException("Failed to get the SuSi entering mobile page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return obj;
	}

}
