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
 * This is Selenium Page Object for "Xfinity Insider" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */

public class XfinityInsider extends SeleniumPageObject<XfinityInsider> {
	
	@FindBy(how = How.ID, using = "accountBtn")
	public WebElement MyAccount;
		
		
	public XfinityInsider(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try	{
				this.driver.get(getURLToLoad());
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity Insider page. ", e);
		}
	}
		
	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.endsWith("XFINITY Insider"));
	}
	
	
	/**
	 * To check whether user is signed in
	 * 
	 * @return True if signed in else False
	 */
	
	public boolean isUserSignedIn() {
		return this.isElementPresent(MyAccount);
	}
	
	
	/**
	 * To get URL of Xfinity Insider page
	 * 
	 * @return URL of Xfinity Insider page
	 * @throws Exception
	 */	
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_INSIDER.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinityInsider.class);
}

