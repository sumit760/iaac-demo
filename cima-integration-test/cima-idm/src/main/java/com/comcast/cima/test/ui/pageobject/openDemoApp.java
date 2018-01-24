package com.comcast.cima.test.ui.pageobject;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class openDemoApp extends SeleniumPageObject<openDemoApp> {

private String appURL;
	
	/*FB Popup element details*/
	@FindBy(how = How.XPATH, using = "//form[1]/div[2]/input[1]")
	private WebElement username;
	
	@FindBy(how = How.XPATH, using = "//form[1]/div[2]/input[2]")
	private WebElement password;
	
	@FindBy(how = How.XPATH, using = "//form[1]/div[2]/button")
	private WebElement login;
	
	@Override
	protected void isLoaded() throws Error {
		String url = this.driver.getCurrentUrl();
		String path = "";
		try {
			URL u = new URL(url);
			path = u.getPath();
		} catch (MalformedURLException mue) {
			Assert.fail("Unable to constuct URL from value '" + url + "'");
		}
		Assert.assertTrue(path.startsWith("http://") || url.contains("Presentation"),
				"Current page URL should contain '/Presentation/', but the actual value is '"
						+ url + "'");
	}

	@Override
	protected void load() {
		try	{
			this.driver.get(getAppURL());
			Thread.sleep(5000);
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Xfinity SignIn Page. ", e);
		}
		
	}

	public openDemoApp(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		this.appURL = null;
	}
	
	
	
	public String getAppURL() {
		return appURL;
	}

	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}

	private void setUserName(String name) {
		this.clearAndType(username, name);
	}
	
	private void setPassword(String passwd) {
		this.clearAndType(password, passwd);
	}


	private void clickSignin() {
		this.login.click();
	}
	
	
	public Object signin(String user, String pass) {
		Object obj = null;
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		try {
			obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get application home page. PageFlowId: " + this.pageFlowId, e);
		}
		return obj;
	}


	private static Logger LOGGER = LoggerFactory.getLogger(openDemoApp.class);
	
}
