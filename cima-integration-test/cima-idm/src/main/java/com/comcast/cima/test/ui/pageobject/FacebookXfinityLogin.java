package com.comcast.cima.test.ui.pageobject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class FacebookXfinityLogin extends SeleniumPageObject<FacebookXfinityLogin> {
	
	/* Login related object */
	
	@FindBy(how = How.ID, using = "user")
	private WebElement userName;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement signIn;
	
	/* Various other Links */	
	
	@FindBy(how = How.ID, using = "rm_label_learn_more")
	private WebElement learnMoreLink;
	
	@FindBy(how = How.XPATH, using = "//*[@id='needaccess']/p/a")
	private WebElement createUsernameLink;
	
	@FindBy(how = How.ID, using = "forgotPwdLink")
	private WebElement forgotPwdLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Don't know your email or username?")
	private WebElement lookUpUID;
	
	public FacebookXfinityLogin(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Facebook Xfinity Login page. ", e);
		}
	}

	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.userName), "Failed to land on Facebook Xfinity Login page");	
	}
	
	/*Methods to check presence of element on Xfinity SignIn Page */
	public boolean isUsernameFieldPresent(){
		return this.isElementPresent(userName);
	}	

	public boolean isPasswordFieldPresent(){
		return this.isElementPresent(password);
	}
	
	public boolean isSignInButtonPresent(){
		return this.isElementPresent(signIn);
	}	
	
	public boolean isLearnMoreLinkPresent(){
		return this.isElementPresent(learnMoreLink);
	}

	public boolean isUserLookUpLinkPresent(){
		return this.isElementPresent(lookUpUID);
	}
	
	public boolean isForgotPasswordLinkPresent(){
		return this.isElementPresent(forgotPwdLink);
	}
	
	public boolean isCreateUsernameLinkPresent(){
		return this.isElementPresent(createUsernameLink);
	}
	
	
	/* Actions related to page object */
	private void setUserName(String name) {
		this.clearAndType(userName, name);
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
		this.setPassword(pass);
		this.clickSignin();
		try {
			obj = ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
		} catch (Exception e) {
			throw new IllegalStateException ("Failed to get the Xfinity Sign in page. PageFlowId: " + this.pageFlowId, e);
		}
		return obj;
	}
	
	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(EndPoinUrlProvider.IDMUrlPropKeys.FB_XFINITY_URL.getValue(), ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookXfinityLogin.class);

}
