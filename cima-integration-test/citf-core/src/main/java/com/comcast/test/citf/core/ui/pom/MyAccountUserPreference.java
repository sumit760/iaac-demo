package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for My Account User Preference web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class MyAccountUserPreference extends SeleniumPageObject<MyAccountUserPreference>  {

	@FindBy (how = How.ID, using = "main_1_pageheader_0_PageHeaderText")
	private WebElement headerMessage;
	
	@FindBy (how = How.ID, using = "main_1_usersettingsmain_0_userbasicsettingslist_0_changePasswordLink")
	private WebElement changePasswordLink;
	
	@FindBy (how = How.ID, using = "main_1_usersettingsmain_0_userbasicsettingslist_0_changeSecretquestionLink")
	private WebElement changeSQLink;
	
	
	
	@Override
	protected void load() {}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(this.isElementPresent(headerMessage));
	}
	
	public MyAccountUserPreference(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Provides Page Object for My Account Change Password page.
	 * 
	 * @return Page Object for My Account Change Password page
	 */
	public Object getChangePasswordPage() {
		if (isElementPresent(changePasswordLink)){
			this.changePasswordLink.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
}
