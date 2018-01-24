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
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class OpenAWSConsole extends SeleniumPageObject<OpenAWSConsole> {
	
/* Login related object */
	
	@FindBy(how = How.ID, using = "resolving_input")
	private WebElement userName;

	@FindBy(how = How.ID, using = "next_button")
	private WebElement next;
	
	@FindBy(how = How.ID, using = "ap_password")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "signInSubmit-input")
	private WebElement signIn;
	

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
		Assert.assertTrue(path.startsWith("/") || url.contains("console.aws.amazon.com"),
				"Current page URL should contain '/login' or 'console.aws.amazon.com', but the actual value is '"
						+ url + "'");
		
	}

	@Override
	protected void load() {
		try	{
			this.driver.get("https://console.aws.amazon.com/ec2/v2/home?region=us-east-1#Instances:sort=instanceId");
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Xfinity SignIn Page. ", e);
		}
		
	}

	public OpenAWSConsole(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/* Actions related to page object */
	private void setUserName(String name) {
		this.clearAndType(userName, name);
	}
	
	private void clickNext() {
		this.next.click();
	}
	
	private void setPassword(String passwd) {
		this.clearAndType(password, passwd);
	}
	
	private void clickSignin() {
		signIn.click();
	}
	
	public Object signin(String user, String pass) {
		Object obj = null;
		this.setUserName(user);
		this.clickNext();
		waitForPageTitle("Amazon.com Sign In", ICimaCommonConstants.WAIT_TIME_UI);
		this.setPassword(pass);
		this.clickSignin();
		waitForPageTitle("EC2 Management Console", ICimaCommonConstants.WAIT_TIME_UI);
		try {
			obj = ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
		} catch (Exception e) {
			throw new IllegalStateException ("Failed to get the Xfinity Sign in page. PageFlowId: " + this.pageFlowId, e);
		}
		return obj;
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(OpenAWSConsole.class);
	
}
