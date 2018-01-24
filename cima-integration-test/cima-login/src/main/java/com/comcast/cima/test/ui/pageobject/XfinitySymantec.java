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
 * This is Selenium Page Object for "Xfinity Symantec" web page.
 * 
 * @author Sumit Pal and shailesh suman
 *
 */
public class XfinitySymantec extends SeleniumPageObject<XfinitySymantec> {
	
	@FindBy(how = How.ID, using = "MainContent_MainContent_lblProductName")
	public WebElement NortonProductLabel;
	
	public XfinitySymantec(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
		} catch (Exception e){
			LOGGER.error("Error occurred while loading xfinity Norton downlaod page. ", e);
		}
	}
		

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();		
		Assert.assertTrue( title.startsWith("Norton Download")  || title.startsWith("Sign in") );
	}
	
	/**
	 * To Check whether user is logged in
	 * @return True if loogedin else False
	 */
	public boolean isLoggedin() {
		return this.isElementPresent(NortonProductLabel);
	}
	
	/**
	 * TO get URL of Xfinity Symantec Page
	 * 
	 * @return URL of Xfinity Symantec
	 * @throws Exception
	 */
	private String getURLToLoad() throws Exception {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.XFINITY_SYMANTEC.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(XfinitySymantec.class);
}

