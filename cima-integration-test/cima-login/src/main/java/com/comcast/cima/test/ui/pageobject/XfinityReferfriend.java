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

/**
 * This is Selenium Page Object for "Xfinity Refer friend" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityReferfriend extends SeleniumPageObject<XfinityReferfriend> {
	
	@FindBy(how = How.ID, using = "dialog-comcast-employees-content")
	public WebElement EmployeePopup;
	
	@FindBy(how = How.ID, using = "dialog-comcast-employees-button-close")
	public WebElement EmployeePopupClose;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;


	public XfinityReferfriend(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
				
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity refer friend page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.endsWith("Refer-A-Friend Program by Comcast") || title.startsWith("Sign in"));
	}
	
	/**
	 * To check whether User is Signed In
	 * 
	 * @return True if Signed in else False
	 */
	public boolean isUserSignedIn()	{
		this.EmployeePopupClose.click();		
		return this.isElementPresent(Signout);
	}
	
	/**
	 * To get URL of Xfinity Refer friend page
	 * 
	 * @return URL of Xfinity Refer friend page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception{
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_REFER_A_FRIEND.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityReferfriend.class);
}

