package com.comcast.test.citf.core.cucumber.steps;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.UserCache;
import com.comcast.test.citf.core.dataProvider.ConfigurationDataProvider;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.dataProvider.UserDataProvider;
import com.comcast.test.citf.core.dataProvider.XTVPartnerIntegrationLayerDataProvider;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.EmersonCollegeLogin;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityLoginStatus;
import com.comcast.test.citf.core.ui.pom.XfinityOnCampus;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

public class CoreCucumberSteps {
	
	/** Log */
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreCucumberSteps.class);
	
	private WebDriver browser;
	
	private String userId;
	private String userPassword;
	private String browserType;
	private String browserPlatform;
	private String responseUrl;
	private String serviceAccessURL;
	private Object pageObject;
	private String pageSource;
	private Scenario testScenario;
	
	private boolean loginAlreadyAttempted = false;
	
	@Autowired
	private UserDataProvider userProvider;
	
	@Autowired
	private XTVPartnerIntegrationLayerDataProvider xtvPilDataProvider;
	
	@Autowired
	private EndPoinUrlProvider urlProvider; 
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	@Autowired
	private ConfigurationDataProvider configDataProvider;
	
	@Before
	public void setup(Scenario scenario) {
		this.testScenario = scenario;
		this.userId = null;
		this.userPassword = null;
		this.responseUrl = null;
		this.serviceAccessURL = null;
		this.pageObject = null;
		this.pageSource = null;
		this.browser = null;
	}

	@After
	public void tearDown() {
		try{
			if (this.browser instanceof RemoteWebDriver) {
				final String sessionId = ((RemoteWebDriver) browser).getSessionId().toString();
				final String status = testScenario.getStatus().equals("passed") ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
						: ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
				citfTestInitializer.updateSauceLabsTestStatus(sessionId, status);
			}
			if (this.browser != null) {
				this.browser.quit();
			}
		}catch(Exception e){
			LOGGER.error("Error occurred while performing clean up activities : ", e);
		}
	}

	
	@Given("^an active customer$")
	public void anActiveCustomer() {
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE);
		UserCache.User user = userProvider.fetchUser(filters, false, UserDataProvider.UserCategory.LOGIN);
		
		if (user == null) {
			fail("Could not retrieve valid user for test");
		}
		
		this.userId = user.getUserId();
		this.userPassword = user.getPassword();
		
		LOGGER.debug("Retrieved user info: userId={}, user_Password={}",
					userId, userPassword);
		
	}
	
	@Given("^an active \"(.*?)\" customer$")
	public void anActiveCustomerWithLOB(String lob) {
		
		Map<UserDataProvider.Filter, String> filters = ImmutableMap.of(UserDataProvider.Filter.LOGIN_STATUS, ICimaCommonConstants.DB_STATUS_ACTIVE,
																		UserDataProvider.Filter.LOBS_CSV, lob);
		UserCache.User user = userProvider.fetchUser(filters, false, UserDataProvider.UserCategory.LOGIN);
		
		if (user == null) {
			fail("Could not retrieve valid user for test");
		}
		
		this.userId = user.getUserId();
		this.userPassword = user.getPassword();
		
		LOGGER.debug("Retrieved user info: userId={}, user_Password={}",
					userId, userPassword);
		
	}

	
	
	@And("^a firefox web browser on windows$")
	public void ffOnWindowsWebBrowserSelection() {
		this.browserType = configDataProvider.getFireFoxBrowser();
        this.browserPlatform = configDataProvider.getWindowsPlatform();
	}
	

	public void setUserIdAndPassword(String inUserId, String inUserPassword) {
		this.userId = inUserId;
		this.userPassword = inUserPassword;
	}
	
	@And("^a web browser$")
	public void defaultWebBrowserSelection() {
		
		ffOnWindowsWebBrowserSelection();
	}
	
	
	@And("^user signs in at Login Page$")
	public void userSignsIn()  {

		assertThat(pageObject, instanceOf(SignInToXfinity.class));
		
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		pageObject = signInPage.signin(this.getUserId(), this.getUserPassword());
		this.loginAlreadyAttempted = true;
		
		LOGGER.debug("User is signed in.");
	}
	
	@And("^user signs in at Login Page with just a password$")
	public void userSignsInWithJustPassword()  {

		assertThat(pageObject, instanceOf(SignInToXfinity.class));
		
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		pageObject = signInPage.signin(this.getUserPassword());
		this.loginAlreadyAttempted = true;
		
		LOGGER.debug("User is signed in.");
	}
	
	
	@And("^user is redirected after sign in$")
	public void userGetsRedirectedAfterSignIn()  {

		assertThat(pageObject, instanceOf(SignInToXfinity.class));
		
		SignInToXfinity signInPage = (SignInToXfinity)pageObject;
		responseUrl = signInPage.signInToGetResponseURL(this.getUserId(), this.getUserPassword());
		if (this.browser != null) {
			pageSource = this.browser.getPageSource();
		}
		this.loginAlreadyAttempted = true;
		
		LOGGER.debug("User redirected to the URl after sign in={}",
				responseUrl);
	}

	@And("^on the 'XFINITY On Campus' page, select '(.*)'$")
	public void onTheXfinityOnCampusPageSelect(String inCollegeName) {

		validatePageObjectClass(XfinityOnCampus.class);
		XfinityOnCampus currentPage = ((XfinityOnCampus)getPageObject());
		currentPage.setPickerInputTextBoxThenPressEnter(inCollegeName);

		if (("Emerson College").equals(inCollegeName)) {
			EmersonCollegeLogin nextCurrentPage = new EmersonCollegeLogin(currentPage.driver);
			//Note - The following call ensures that important page components are loaded ('isLoaded()' indirectly called)
			nextCurrentPage.get();
			setPageObject(nextCurrentPage);
		}
	}

	@And("^on the 'Emerson College Login' page, login using a student account$")
	public void onTheEmersonCollegeLoginPageLoginUsingAStudentAccount() {

		validatePageObjectClass(EmersonCollegeLogin.class);
		EmersonCollegeLogin currentPage = ((EmersonCollegeLogin)getPageObject());

		final String xtvPilUniversityUsername = getXtvPilUniversityAccount1Username();
		final String xtvPilUniversityPassword = getXtvPilUniversityAccount1Password();

		currentPage.setUsernameTextBox(xtvPilUniversityUsername);
		currentPage.setPasswordTextBoxThenPressEnter(xtvPilUniversityPassword);
		this.loginAlreadyAttempted = true;
	}

	@And("^confirm that '(.*)' is the title of the web page$")
	public void confirmThatIsTheTitleOfTheWebPage(String inTitle) {
		getBrowser();
		WebDriverWait wait = new WebDriverWait(browser, ICommonConstants.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.titleContains(inTitle));
		assertEquals(browser.getTitle(), inTitle);
	}

	@And("^confirm that parameters '(.*)' and '(.*)' appear in the browser's URL$")
	public void confirmThatParametersAndAppearInTheBrowsersUrl(String inParameterNameOne, String inParameterNameTwo) {
		getBrowser();
		String currentUrl = browser.getCurrentUrl();
		assertNotNull("The browser's current URL is Null", currentUrl);

		boolean paramOneFound = false;
		boolean paramTwoFound = false;
		
		URIBuilder urlb = getUriBuilder(currentUrl);		
		List<NameValuePair> paramNameValueList = urlb.getQueryParams();
		for (NameValuePair nextNvp : paramNameValueList) {
			paramOneFound |= nextNvp.getName().equals(inParameterNameOne);
			paramTwoFound |= nextNvp.getName().equals(inParameterNameTwo);
		}
		String message = "Neither the {1} parameter nor the {2} parameter can be found in the browser's current URL - {3}";
		assertFalse(new MessageFormat(message).format(new String[] {inParameterNameOne, inParameterNameTwo, currentUrl}),
					!paramOneFound && !paramTwoFound);
		assertFalse(new MessageFormat(MESSAGE_PARAM_NOT_FOUND_IN_URL).format(new String[] {inParameterNameOne, currentUrl}),
					!paramOneFound);
		assertFalse(new MessageFormat(MESSAGE_PARAM_NOT_FOUND_IN_URL).format(new String[] {inParameterNameTwo, currentUrl}),
					!paramTwoFound);
	}
	
	@And("^confirm that parameter '([^\\']*)' appears in the browser's URL$")
	public void confirmThatParametersAppearsInTheBrowsersUrl(String inParameterNameOne) {
		getBrowser();
		String currentUrl = browser.getCurrentUrl();
		assertNotNull("The browser's current URL is Null", currentUrl);

		boolean paramOneFound = false;
		URIBuilder urlb = getUriBuilder(currentUrl);
		List<NameValuePair> paramNameValueList = urlb.getQueryParams();
		for (NameValuePair nextNvp : paramNameValueList) {
			paramOneFound |= nextNvp.getName().equals(inParameterNameOne);
		}
		assertFalse(new MessageFormat(MESSAGE_PARAM_NOT_FOUND_IN_URL).format(new String[] {inParameterNameOne, currentUrl}),
						!paramOneFound);
	}
	
	@And("^confirm that parameter '([^\\']*)' with value '([^\\']*)' appears in the browser's URL$")
	public void confirmThatParametersAppearsInTheBrowsersUrl(
			String inParameterNameOne, String inParameterValueOne) {
		getBrowser();
		String currentUrl = browser.getCurrentUrl();
		assertNotNull("The browser's current URL is Null", currentUrl);

		boolean paramOneWithCorrectValueFound = false;
		URIBuilder urlb = getUriBuilder(currentUrl);
		List<NameValuePair> paramNameValueList = urlb.getQueryParams();
		for (NameValuePair nextNvp : paramNameValueList) {
			paramOneWithCorrectValueFound |=
					nextNvp.getName().equals(inParameterNameOne)
					&& nextNvp.getValue() != null
					&& nextNvp.getValue().equals(inParameterValueOne);
		}
		assertFalse(new MessageFormat("The '{1}' parameter with value '{2}' cannot be found in the browser's current URL - {3}").format(
				new String[] {inParameterNameOne, inParameterValueOne, currentUrl}), !paramOneWithCorrectValueFound);
	}
	
	@And("^confirm user has signed out$")
	public void confirmUserHasSignedOut() {
		getBrowser();
		XfinityLoginStatus page = new XfinityLoginStatus(browser);
		setPageObject(page.get());
		assertFalse("User is logged in but shouldn't be at this point", page.isLoggedIn());
	}
	
	@And("^a service access Url$")
	public void fetchServiceAccessURL() {
		this.serviceAccessURL = urlProvider.getServiceEndPointUrl(EndPoinUrlProvider.ServiceEndpointPropKeys.ACCESS.name());
	}


	public WebDriver getBrowser() {
		if (browser == null) {
			try{
				browser = citfTestInitializer.getBrowserInstance(
						this.testScenario.getName(),
						BrowserCapabilityDAO.Platforms.valueOf(getBrowserPlatform()),
						BrowserCapabilityDAO.Types.COMPUTER,
						getBrowserType(),
						false);

			}catch(IOException e){
				LOGGER.error("Error occurred while getting browser instance : ", e);
				fail("Not able to get browser instance due to : "+e.getMessage());
			}
		}
		return browser;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getBrowserType() {
		return browserType;
	}

	public String getBrowserPlatform() {
		return browserPlatform;
	}
	
	public String getResponseUrl() {
		return responseUrl;
	}

	public Object getPageObject() {
		return pageObject;
	}
	
	public String getPageSource() {
		return pageSource;
	}

	public boolean isLoginAlreadyAttempted() {
		return this.loginAlreadyAttempted;
	}
	public void setLoginAlreadyAttempted(boolean inLoginAlreadyAttempted) {
		this.loginAlreadyAttempted = inLoginAlreadyAttempted;
	}

	public String getServiceAccessURL() {
		return serviceAccessURL;
	}
	
	@SuppressWarnings("rawtypes")
	public void validatePageObjectClass(Class inClass) {
		if (!(inClass.isInstance(getPageObject()))) {
			throw new IllegalStateException("The current page object is of class '"
					+ ((getPageObject() != null) ? getPageObject().getClass().getName() : null)
					+ "' but it's expected to be '" + inClass.getName() + "'");
		}
	}

	private String getXtvPilUniversityAccount1Username() {
		return xtvPilDataProvider.getUniversityAccount1Username();
	}
	private String getXtvPilUniversityAccount1Password() {
		return xtvPilDataProvider.getUniversityAccount1Password();
	}

	public void setPageObject(Object pageObject) {
		this.pageObject = pageObject;
	}
	
	public void setUserPassword(String password) {
		this.userPassword = password;
	}

	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}
	
	private URIBuilder getUriBuilder(String currentUrl){
		URIBuilder urlb = null;
		try{
			urlb = new URIBuilder(currentUrl);
		}catch(URISyntaxException e){
			LOGGER.error("Error occurred while creating URIBuilder : ", e);
			fail("Not able to create URIBuilder : "+e.getMessage());
		}
		return urlb;
	}
	
	//Message constants
	private static final String MESSAGE_PARAM_NOT_FOUND_IN_URL = "The '{1}' parameter cannot be found in the browser's current URL - {2}";
}
