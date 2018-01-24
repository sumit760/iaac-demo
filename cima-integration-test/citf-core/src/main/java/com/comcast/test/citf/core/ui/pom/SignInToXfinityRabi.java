package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

//import com.comcast.cima.test.ui.transition.VerifyIdentity;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for Sign In to Xfinity web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class SignInToXfinityRabi extends SeleniumPageObject<SignInToXfinityRabi> {

	/* Login related object */
	
	@FindBy(how = How.ID, using = "user")
	private WebElement userName;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement signIn;
	
	@FindBy(how = How.ID, using = "sign_in_fb")
	private WebElement connectUsingFacebook;
	
	@FindBy(how = How.XPATH, using = "//div[@id='signin']/p[@class='logged_in']")
	private WebElement loggedInUser;
	
	
	/* Various other Links */	
	
	@FindBy(how = How.ID, using = "rm_label_learn_more")
	private WebElement learnMoreLink;
	
	@FindBy(how = How.ID, using = "fb_learn_more_text")
	private WebElement learnMoreLinkFb;
	
	@FindBy(how = How.XPATH, using = "//*[@id='needaccess']/p/a")
	private WebElement createUsernameLink;
	
	@FindBy(how = How.ID, using = "forgotPwdLink")
	private WebElement forgotPwdLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Don't know your email or username?")
	private WebElement lookUpUID;
	
	@FindBy(how = How.ID, using = "remember_me")
	private WebElement checkBoxKeepMeSignedIn;
	
	@FindBy(how = How.ID, using = "forgotPwdLink")
	private WebElement forgotPassword;
	
	@FindBy(how = How.XPATH, using = "//*[@id='needaccess']/p/a")
	private WebElement createUsername;
	
	private String parentWindowHandler;
	
	public SignInToXfinityRabi(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(this.userName), "Failed to land on Sign in to Xfinity Rabi page");	
	}

	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Xfinity SignIn Page. ", e);
		}
	}
	
	
	/* Methods to check presence of element on Xfinity SignIn Page */
	
	/**
	 * Checks if user name field present
	 * 
	 * @return TRUE if user name field is present, else FALSE
	 */
	public boolean isUsernameFieldPresent(){
		return this.isElementPresent(userName);
	}	

	/**
	 * Checks if password field present
	 * 
	 * @return TRUE if password field is present, else FALSE
	 */
	public boolean isPasswordFieldPresent(){
		return this.isElementPresent(password);
	}
	
	/**
	 * Checks if Sign In button present
	 * 
	 * @return TRUE if Sign In button is present, else FALSE
	 */
	public boolean isSignInButtonPresent(){
		return this.isElementPresent(signIn);
	}	

	/**
	 * Checks if Connect using Facebook element present
	 * 
	 * @return TRUE if Connect using Facebook element is present, else FALSE
	 */
	public boolean isConnectUsingFBPresent(){
		return this.isElementPresent(connectUsingFacebook);
	}
	
	/**
	 * Checks if Connect using Facebook element present
	 * 
	 * @return TRUE if Connect using Facebook element is present, else FALSE
	 */
	public boolean isLearnMoreLinkPresent(){
		return this.isElementPresent(learnMoreLink);
	}
	
	/**
	 * Checks if Learn more Facebook link present
	 * 
	 * @return TRUE if Learn more Facebook link is present, else FALSE
	 */
	public boolean isLearnMoreFBLinkPresent(){
		return this.isElementPresent(learnMoreLinkFb);
	}
	
	/**
	 * Checks if Learn more Facebook link present
	 * 
	 * @return TRUE if Learn more Facebook link is present, else FALSE
	 */
	public boolean isUserLookUpLinkPresent(){
		return this.isElementPresent(lookUpUID);
	}
	
	/**
	 * Checks if Learn more Facebook link present
	 * 
	 * @return TRUE if Learn more Facebook link is present, else FALSE
	 */
	public boolean isForgotPasswordLinkPresent(){
		return this.isElementPresent(forgotPwdLink);
	}
	
	/**
	 * Checks if Create UserName link present
	 * 
	 * @return TRUE if Create UserName link is present, else FALSE
	 */
	public boolean isCreateUsernameLinkPresent(){
		return this.isElementPresent(createUsernameLink);
	}
	
	
	/* Actions related to page object */
	
	/**
	 * Sets user name
	 * 
	 * @param name
	 * 			User name
	 */
	private void setUserName(String name) {
		this.clearAndType(userName, name);
	}
	
	/**
	 * Sets password
	 * 
	 * @param passwd
	 * 			Password
	 */
	private void setPassword(String passwd) {
		this.clearAndType(password, passwd);
	}
	
	/**
	 * Gets logged in user name
	 * 
	 * @return Logged in user name
	 */
	public String getLoggedInUserName() {
		if (isElementPresent(this.loggedInUser)) {
			return this.loggedInUser.getText();
		}
		return null;
	}
	
	/**
	 * Clicks on sign in button
	 */
	private void clickSignin() {
		this.signIn.click();
	}
	
	public void clickConnectUsingFacebookLink() {
		this.connectUsingFacebook.click();
	}

	/**
	 * Signs in with username and password
	 * 
	 * @param user
	 * 			User Name
	 * @param pass
	 * 			Password
	 * @return Next page object
<<<<<<< HEAD
	 * @throws Exception
=======
>>>>>>> master
	 */
	public Object signin(String user, String pass) {
		Object obj = null;
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		try {
			obj = ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
		} catch (Exception e) {
			throw new IllegalStateException("Failed to get Xfinity home page or RABI Check option page. PageFlowId: " + this.pageFlowId, e);
		}
		return obj;
	}
	
	/**
	 * Signs in and remember
	 * 
	 * @param user
	 * 			User Name
	 * @param pass
	 * 			Password
	 * @return Next page object
	 */
	public Object signInAndRemember(String user, String pass){
		this.setUserName(user);
		this.setPassword(pass);
		this.keepMesignedIn();
		this.clickSignin();
		waitForLinkedText("Sign Out", ICommonConstants.WAIT_TIMEOUT);
		
		return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
	}
	
	/**
	 * Signs in with frame
	 * 
	 * @param user
	 * 			User Name
	 * @param pass
	 * 			Password
	 * @param frameId
	 * 			Frame Id
	 * @return Next page object
	 */
	public Object signinWithFrame(String user, String pass, String frameId){
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		waitForLinkedTextInFrame("Sign Out", frameId, ICommonConstants.WAIT_TIMEOUT);
		
		return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
	}
	
	/**
	 * Signs in with password
	 * 
	 * @param pass
	 * 			Password
	 * @return Next page object
	 */
	public Object signin(String pass){
		this.setPassword(pass);
		this.clickSignin();
		
		return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
	}
	
	/**
	 * Signs in to get response URL
	 * 
	 * @param user
	 * 			User Name
	 * @param pass
	 * 			Password
	 * @return Response URL
	 */
	public String signInToGetResponseURL(String user, String pass){
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		
		return this.driver.getCurrentUrl();
	}
	
	/**
	 * Signs in for device activation
	 * 
	 * @param user
	 * 			User Name
	 * @param pass
	 * 			Password
	 * @return Next page object
	 */
	public Object signinForDeviceActivation(String user, String pass){
		this.setUserName(user);
		this.setPassword(pass);
		this.clickSignin();
		
		String pageSource = this.driver.getPageSource();
		pageSource = pageSource.replaceAll("\\s+", ICommonConstants.BLANK_SPACE_STRING);
		if (pageSource.contains("THIS APP REQUESTS PERMISSION TO")){
				waitForElementPresent(this.driver.findElement(new By.ByXPath("//form[@id='confirmationForm']/button")), ICommonConstants.WAIT_TIMEOUT);
		}
		
		return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.getPageFlowId());
	}
	
	/**
	 * Selects keep me sign in check box
	 */
	public void keepMesignedIn(){
		if (isElementPresent(this.checkBoxKeepMeSignedIn)){
			this.selectCheckboxByID(this.checkBoxKeepMeSignedIn);
		}
	}
	
	/**
	 * Get Reset Password Gateway page
	 * 
	 * @return Reset Password Gateway page object
	 */
	public Object getPageResetPasswordGateway() {
		if (isElementPresent(forgotPassword)) {
			this.forgotPassword.click();
			return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * Creates User id
	 * 
	 * @return Next page object
	 */
	public Object createUserID() {
		if (isElementPresent(createUsername)) {
			this.createUsername.click();
			waitForElementByXPATH("//button[@id='submitButton']",ICommonConstants.WAIT_TIMEOUT);
			return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
		}
		return null;
	}
	

	/**
	 * Provides Facebook login popup page
	 * 
	 * @return Facebook login popup page object
	 */
	public Object getFacebookLoginPopup() {
		if (isElementPresent(connectUsingFacebook)) {
			this.connectUsingFacebook.click();
			// Store your parent window
			this.parentWindowHandler = this.driver.getWindowHandle();
			this.driver = switchToChildWindow(this.driver);
			try {
				return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to get the FB Login popup page. PageFlowId: " + this.pageFlowId, e);
			}
		}
		return null;
	}
	
	/**
	 * Clicks on Learn more Facebook 
	 */
	public void clickLearnMoreFb(){		
		this.learnMoreLinkFb.click();		
	}
	
	/**
	 * Looks up user
	 * 
	 * @return Next page object 
	 */
	public Object lookupUser() {
		if (isElementPresent(lookUpUID)) {
			this.lookUpUID.click();
			return ObjectInitializer.getPageNavigator().getNextPage(this.driver, this, this.pageFlowId);
		}
		return null;
	}
	
	/**
	 * Checks user name
	 * 
	 * @param user
	 * 			User Name
	 * @return TURE if user contains this user name, else FALSE
	 */
	public boolean checkUserName(String user){
		return user.contains(this.userName.getText()) ? true : false;
	}
	
	
	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(EndPoinUrlProvider.IDMUrlPropKeys.SIGN_IN_XFINITY_RABI_URL.getValue(), ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static Logger LOGGER = LoggerFactory.getLogger(SignInToXfinity.class);

	public String getParentWindowHandler() {
		return parentWindowHandler;
	}
}
