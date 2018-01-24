package com.comcast.test.citf.common.ui.router;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

//import com.comcast.cima.test.ui.transition.VerifyIdentity;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

public class SignInToXfinity extends SeleniumPageObject<SignInToXfinity> {

	public SignInToXfinity(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.endsWith("/login"));
	}

	@Override
	protected void load() {
		try	{
			this.driver.get("https://login-qa4.comcast.net/login");
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			logger.error("Error occurred while loading Xfinity SignIn Page. " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	private static Logger logger = LoggerFactory.getLogger(SignInToXfinity.class);
}
