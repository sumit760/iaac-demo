package com.comcast.cima.test.ui.cucumber.steps;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.comcast.cima.test.dataProvider.IdmTestDataProvider;
import com.comcast.cima.test.ui.pageobject.SuSiAllSet;
import com.comcast.cima.test.ui.pageobject.SuSiIPAddress;
import com.comcast.cima.test.ui.pageobject.SuSiMobileSent;
import com.comcast.cima.test.ui.pageobject.SuSiNotFindYou;
import com.comcast.cima.test.ui.pageobject.SuSiSetUpWiFi;
import com.comcast.cima.test.ui.pageobject.SuSiTryAgainLast;
import com.comcast.cima.test.ui.pageobject.SuSiUpdateSetting;
import com.comcast.cima.test.ui.pageobject.SuSiWelcomeMobile;
import com.comcast.cima.test.ui.pageobject.SuSiWelcomeUsername;
import com.comcast.cima.test.ui.pageobject.SuSiYourAccount;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.google.common.collect.ImmutableMap;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;


public class SusiFlow extends IdmTestDataProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Scenario testScenario;
	private WebDriver browser;
	
	@Before
	public void setup(Scenario scenario) throws Exception {
		this.testScenario = scenario;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ICimaCommonConstants.SCENARIO_NAME, testScenario.getName());
		populateTestMap(TEST_PARAM_MAP, map, false);
	}
	
	@After
	public void tearDown() throws Exception {
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		browser = (WebDriver) paramMap.get(ICimaCommonConstants.BROWSER);
		
		String sessionId = paramMap.get(ICimaCommonConstants.SESSIONID)!=null ? paramMap.get(ICimaCommonConstants.SESSIONID).toString() : null;
		if (sessionId!=null) {
			if(browser instanceof RemoteWebDriver) {
					final String status = testScenario.getStatus().equals("passed")
                    ? ICommonConstants.TEST_EXECUTION_STATUS_PASSED
                    : ICimaCommonConstants.TEST_EXECUTION_STATUS_FAILED;
					updateSauceLabsTestStatus(sessionId, status);
			}
			if (browser != null) {
//				browser.quit();
			}
			removeTestMap(TEST_PARAM_MAP);
			if(testScenario.getStatus().equals("passed")) {
//				purgeConfig(ICimaCommonConstants.SERVICE_ID);
			}
		}
	}
	
	@Given("^an existing user in environment (.*?)$")
	public void givenUser() throws Exception {
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Map<String, Object> filter = ImmutableMap.<String, Object>builder()
				.build();
		
		List<Map<String, Object>> userList = fetchUser(filter,getCurrentEnvironment(),getClass().getName());
		if (userList!=null && userList.size() > 0) {
			Map<String,Object> user = userList.get(0);
			if(user!=null && user.get(KEY_USER)!=null && user.get(KEY_ACCOUNT)!=null) {
				paramMap.put(ICimaCommonConstants.ACCOUNT, (AccountCache.Account)user.get(KEY_ACCOUNT));
				paramMap.put(ICimaCommonConstants.USERATTR, (UserAttributesCache.Attribute)user.get(KEY_USER));
			} else {
				throw new IllegalStateException("This user is null or this user doesn't have either user account: " + 
						(user!=null?user.get(KEY_ACCOUNT):null) + "or user attribute: " + (user!=null?user.get(KEY_USER):null));
			}
		} else {
			throw new IllegalStateException("No user found. Filter: " + filter + " Environment: " + getCurrentEnvironment() + " SourceId: " + getClass().getName());
		}
		populateTestMap(TEST_PARAM_MAP, paramMap, true);		
	}
	
	@Given("^an existing user with phone number$")
	public void givenUserWithPhoneNumber() throws Exception {
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Map<String, Object> filter = ImmutableMap.<String, Object>builder()
				.put(IdmFilterKeys.PHONE.getValue(), ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL)
				.build();
		
		List<Map<String, Object>> userList = fetchUser(filter,getCurrentEnvironment(),getClass().getName());
		if (userList!=null && userList.size() > 0) {
			Map<String,Object> user = userList.get(0);
			if(user!=null && user.get(KEY_USER)!=null && user.get(KEY_ACCOUNT)!=null) {
				paramMap.put(ICimaCommonConstants.ACCOUNT, (AccountCache.Account)user.get(KEY_ACCOUNT));
				paramMap.put(ICimaCommonConstants.USERATTR, (UserAttributesCache.Attribute)user.get(KEY_USER));
			} else {
				throw new IllegalStateException("This user is null or this user doesn't have either user account: " + 
						(user!=null?user.get(KEY_ACCOUNT):null) + "or user attribute: " + (user!=null?user.get(KEY_USER):null));
			}
		} else {
			throw new IllegalStateException("No user found. Filter: " + filter + " Environment: " + getCurrentEnvironment() + " SourceId: " + getClass().getName());
		}
		populateTestMap(TEST_PARAM_MAP, paramMap, true);		
	}
	
	
	@And("^user opens SuSi IP Address page in browser (.*?) over (.*?)$")
	public void userOpensSuSiIPAddressPage(String sauceBrowser, String saucePlatform) throws Exception {

		String pfId = PageNavigator.start("ValidateSuSiFeature_"+ System.currentTimeMillis());
		browser = getBrowserInstance(getClass().getSimpleName(), Platforms.valueOf(saucePlatform), Types.COMPUTER, sauceBrowser, false);
		String sessId = ((RemoteWebDriver)browser).getSessionId().toString();
		
		SuSiIPAddress susiIpAddress = new SuSiIPAddress(browser);
		susiIpAddress.setPageFlowId(pfId);
		susiIpAddress.setWindowHandle(this.browser.getWindowHandle());
		susiIpAddress.get();
		
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.BROWSER, browser, true);
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.PAGEFLOWID, pfId, true);
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.SESSIONID, sessId, true);
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.SUSIIPADDRESSPAGE, susiIpAddress, true);
	}
	

	@When("^user enters IP address (.*?) in SuSi and clicks Next button$")
	public void userEntersIPAddressAndNext(String ipAddress) throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		SuSiIPAddress susiIpAddress = (SuSiIPAddress) paramMap.get(ICimaCommonConstants.SUSIIPADDRESSPAGE);
		susiIpAddress.enterIpAddress(ipAddress);
		Object obj = susiIpAddress.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj, true);
	}
	

	@Then("^user successfully lands on SuSi entering mobile page$")
	public void userLandsOnSuSiWelcomeMobilePage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiWelcomeMobile)) {
			throw new IllegalStateException("Expected page not found after user enters IP Address and clicks Next button from SuSi IP page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
	}
	

	@When("^user provides valid mobile number, accepts terms, and clicks Next button$")
	public void userEntersMobileNumberAcceptsTermsAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiWelcomeMobile)) {
			throw new IllegalStateException("Expected page not found after user enters IP Address and clicks Next button from SuSi IP page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
		SuSiWelcomeMobile susiWelcomeMobile = (SuSiWelcomeMobile) obj;
		AccountCache.Account account = (AccountCache.Account) paramMap.get(ICimaCommonConstants.ACCOUNT);
		if(account!= null && account.getPhoneNumber() != null){
			susiWelcomeMobile.enterMobileNumber(account.getPhoneNumber());
			susiWelcomeMobile.acceptTerms();
			Object obj1 = susiWelcomeMobile.pageNext();
			populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
		} else {
			throw new IllegalStateException("account is not fetching from database or this account doesn't have phone number.");
		}
	}
	

	@When("^user prefers to sign in to SuSi account with username and password$")
	public void userSignInWithUsername() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiWelcomeMobile)) {
			throw new IllegalStateException("Expected page not found after user enters IP Address and clicks Next button from SuSi IP page. Page has been identified as " +
				(obj!=null?obj.getClass().getName():null));
		}
		SuSiWelcomeMobile susiWelcomeMobile = (SuSiWelcomeMobile) obj;
		Object obj1 = susiWelcomeMobile.signInWithUsernameAndPassword();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@Then("^user successfully lands on SuSi entering verification code page$")
	public void userLandsOnSuSiMobileSentPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiMobileSent)) {
			throw new IllegalStateException("Expected page not found after user enters valid mobile number accepts terms and " +
				"clicks Next button from SuSi welcome mobile page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
	}
	

	@When("^user enters verification code in SuSi and clicks Next button$")
	public void userEntersVerificationCodeAndNext() throws Exception {
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiMobileSent)) {
			throw new IllegalStateException("Expected page not found after user enters valid mobile number accepts terms and " + 
				"clicks Next button from SuSi welcome mobile page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiMobileSent susiMobileSent = (SuSiMobileSent) obj;
		AccountCache.Account account = (AccountCache.Account) paramMap.get(ICimaCommonConstants.ACCOUNT);
		if(account != null && account.getPhoneNumber() != null) {
			String vericode=readLog("SUSI_SMS_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, account.getPhoneNumber());	
			if (vericode == null) {
				throw new IllegalStateException("No SMS verification code found in server log for this mobile number: " + account.getPhoneNumber());
			}
			susiMobileSent.enterVerificationCode(vericode);
			Object obj1 = susiMobileSent.pageNext();
			populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
		} else {
			throw new IllegalStateException("account is not fetching from database or this account doesn't have phone number.");
		}
	}
	

	@Then("^user successfully lands on cannot find you page$")
	public void userLandsOnCannotFindYouPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiNotFindYou)) {
			throw new IllegalStateException("Expected page not found after user enters valid SMS code and clicks Next button " + 
				"from SuSi verification code page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
	}
	

	@When("^user clicks signIn button on cannot find you page in SuSi$")
	public void userClicksNextButton() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiNotFindYou)) {
			throw new IllegalStateException("Expected page not found after user enters valid SMS code and clicks Next button " + 
				"from SuSi verification code page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiNotFindYou susiNotFindYou = (SuSiNotFindYou) obj;
		Object obj1 = susiNotFindYou.signIn();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@Then("^user successfully lands on SuSi entering credential page$")
	public void userLandsOnWelcomeUsernamePage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiWelcomeUsername)) {
			throw new IllegalStateException("Expected page not found after user clicks Next button from cannot find you page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
	}
	

	@When("^user enters username and password, and clicks Sign In button to login SuSi account$")
	public void userEntersUsernamePasswdAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiWelcomeUsername)) {
			throw new IllegalStateException("Expected page not found after user clicks Next button from cannot find you page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
		SuSiWelcomeUsername susiWelcomeUsername = (SuSiWelcomeUsername) obj;
		UserAttributesCache.Attribute userAttributes = (UserAttributesCache.Attribute) paramMap.get(ICimaCommonConstants.USERATTR);
		susiWelcomeUsername.enterUsername(userAttributes.getUserId());
		susiWelcomeUsername.enterPassword(userAttributes.getPassword());
		Object obj1 = susiWelcomeUsername.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	
	
	@Then("^user successfully lands on SuSi your account page$")
	public void userLandsOnYourAccountPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiYourAccount)) {
			throw new IllegalStateException("Expected page not found after user enters verification code or enters username and password and " + 
				"clicks Next button from verification code page or welcome entering credential page. Page has been identified as " + 
					(obj!=null?obj.getClass().getName():null));
		}
	}
	
	
	@When("^user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button$")
	public void userAutenticatesAcceptsAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiYourAccount)) {
			throw new IllegalStateException("Expected page not found after user enters verification code or enters username and password and " + 
				"clicks Next button from verification code page or welcome entering credential page. Page has been identified as " + 
					(obj!=null?obj.getClass().getName():null));
		}
		SuSiYourAccount susiYourAccount = (SuSiYourAccount) obj;
		UserAttributesCache.Attribute userAttributes = (UserAttributesCache.Attribute) paramMap.get(ICimaCommonConstants.USERATTR);
		AccountCache.Account account = (AccountCache.Account) paramMap.get(ICimaCommonConstants.ACCOUNT);
		
		if(susiYourAccount.getName() != null) {
			if(!susiYourAccount.getName().equals(account.getFirstName() + " " + account.getLastName())) {
				throw new IllegalStateException("Account Name does not match, expected value: " + (account.getFirstName() + " " + 
					account.getLastName()) + ", actual value: " + susiYourAccount.getName());
			}
		} else {
			throw new IllegalStateException("Failed to get the user's name of UID: " + userAttributes.getUserId());
		}

		susiYourAccount.acceptTerms();
		Object obj1 = susiYourAccount.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@Then("^user successfully lands on SuSi set up WiFi page$")
	public void userLandsOnSetUpWiFiPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiSetUpWiFi)) {
			throw new IllegalStateException("Expected page not found after user authenticates himself accepts agreement and clicks Next button " + 
				"from SuSi your account page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
	}
	

	@When("^user selects Existing WiFi Settings and clicks Next button$")
	public void userSelectsExistingWiFiAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiSetUpWiFi)) {
			throw new IllegalStateException("Expected page not found after user authenticates himself accepts agreement and clicks Next button " + 
				"from SuSi your account page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiSetUpWiFi susiSetUpWiFi = (SuSiSetUpWiFi) obj;
		susiSetUpWiFi.selectExistingWifiOption();
		Object obj1 = susiSetUpWiFi.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@When("^user creates a New WiFi and Password, and clicks Next button$")
	public void userSelectsNewWiFiAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiSetUpWiFi)) {
			throw new IllegalStateException("Expected page not found after user authenticates himself accepts agreement and clicks Next button " +
				"from SuSi your account page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiSetUpWiFi susiSetUpWiFi = (SuSiSetUpWiFi) obj;
		susiSetUpWiFi.selectNewWifiOption();
		susiSetUpWiFi.enterWifi24Id(ICimaCommonConstants.WIFI_ID);
		susiSetUpWiFi.enterWifi24Passwd(ICimaCommonConstants.WIFI_PASSWD);
		Object obj1 = susiSetUpWiFi.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@When("^user selects Existing WiFi Settings, checks to receive message, and clicks Next button$")
	public void userSelectsExistingWiFiChecksMessageAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiSetUpWiFi)) {
			throw new IllegalStateException("Expected page not found after user authenticates himself accepts agreement and clicks Next button " +
				"from SuSi your account page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiSetUpWiFi susiSetUpWiFi = (SuSiSetUpWiFi) obj;
		susiSetUpWiFi.selectExistingWifiOption();
		susiSetUpWiFi.agreeReceivingMessage();
		Object obj1 = susiSetUpWiFi.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@When("^user creates a New WiFi and Password, checks to receive message, and clicks Next button$")
	public void userSelectsNewWiFiChecksMessageAndNext() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiSetUpWiFi)) {
			throw new IllegalStateException("Expected page not found after user authenticates himself accepts agreement and clicks Next button " + 
				"from SuSi your account page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiSetUpWiFi susiSetUpWiFi = (SuSiSetUpWiFi) obj;
		susiSetUpWiFi.selectNewWifiOption();
		susiSetUpWiFi.enterWifi24Id(ICimaCommonConstants.WIFI_ID);
		susiSetUpWiFi.enterWifi24Passwd(ICimaCommonConstants.WIFI_PASSWD);
		susiSetUpWiFi.agreeReceivingMessage();
		Object obj1 = susiSetUpWiFi.pageNext();
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@Then("^user lands on updating your settings page$")
	public void userLandsOnPendingPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiUpdateSetting)) {
			throw new IllegalStateException("Expected page not found after user selects Existing WiFi and clicks Next button " + 
				"from SuSi set up WiFi page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		SuSiUpdateSetting susiUpdateSetting = (SuSiUpdateSetting) obj;
		if(!susiUpdateSetting.isUpdatingPresent()) {
			throw new IllegalStateException("Expected page not found after user selects Existing WiFi and clicks Next button " + 
				"from SuSi set up WiFi page. Page has been identified as " + (obj!=null?obj.getClass().getName():null));
		}
		Object obj1 = susiUpdateSetting.pageNext(ICimaCommonConstants.SUSI_UPDATING_WIFI_TIME);
		populateTestMap(TEST_PARAM_MAP, ICimaCommonConstants.NEXTPAGE, obj1, true);
	}
	

	@And("^user should get the All Set page$")
	public void userLandsOnAllSetPage() throws Exception {
		
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		Object obj = paramMap.get(ICimaCommonConstants.NEXTPAGE);
		if (!(obj instanceof SuSiAllSet)) {
			throw new IllegalStateException("Expected page not found after WiFi updating setting is done from SuSi updating your settings page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
		SuSiAllSet susiAllSet = (SuSiAllSet) obj;
		if(!susiAllSet.isAllSetPresent()) {
			throw new IllegalStateException("Expected page not found after WiFi updating setting is done from SuSi updating your settings page. Page has been identified as " + 
				(obj!=null?obj.getClass().getName():null));
		}
	}
	

	@And("^user should receive a text message about WiFi detail$")
	public void userReceiveTextMessage() throws Exception {
		Map<String, Object> paramMap = getTestMap(TEST_PARAM_MAP);
		AccountCache.Account account = (AccountCache.Account) paramMap.get(ICimaCommonConstants.ACCOUNT);
		if(account != null && account.getPhoneNumber() != null) {
			String message=readLog("SUSI_MESSAGE_VERIFICATION", LogReader.RegexArgument.PHONE_NUMBER, account.getPhoneNumber());
			if (message == null) {
				throw new IllegalStateException("No verification message found in server log for this mobile number: " + account.getPhoneNumber());
			}
		} else {
			throw new IllegalStateException("account is not fetching from database or this account doesn't have phone number.");
		}
	}

	
	private void purgeConfig(String serviceAccountId){
		//http://stackoverflow.com/questions/3021602/http-basic-auth-via-url-in-firefox-does-not-work
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("network.http.phishy-userpass-length", 255);
		profile.setPreference("network.automatic-ntlm-auth.trusted-uris","yourDomain");
		browser = new FirefoxDriver(profile);
		browser.get(ICimaCommonConstants.DELETE_TOKEN_GEN_URL + serviceAccountId);
		String token = browser.findElement(By.xpath("//*[/html/body]")).getText();
		if(token==null) {
			throw new IllegalStateException("token is empty for Url " + (ICimaCommonConstants.DELETE_TOKEN_GEN_URL + serviceAccountId));
		}
		browser.quit();
		profile = new FirefoxProfile();
		try {
			File modifyHeaders = new ClassPathResource("modify_headers-0.7.1.1-fx.xpi").getFile();
			profile.setEnableNativeEvents(false); 
			profile.addExtension(modifyHeaders);
		} catch (Exception e) {
	    	throw new IllegalStateException("Exception occured while adding Extension to firefox profile:", e);
		}
		profile.setPreference("modifyheaders.headers.count", 1);
		profile.setPreference("modifyheaders.headers.action0", "Add");
		profile.setPreference("modifyheaders.headers.name0", "SATAuthorization");
		profile.setPreference("modifyheaders.headers.value0", token);
		profile.setPreference("modifyheaders.headers.enabled0", true);
		profile.setPreference("modifyheaders.config.active", true);
		profile.setPreference("modifyheaders.config.alwaysOn", true);
		DesiredCapabilities capabilities = new DesiredCapabilities();
	    capabilities.setBrowserName("firefox");
	    capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
	    capabilities.setCapability(FirefoxDriver.PROFILE, profile);
	    try {
		    browser = new FirefoxDriver(capabilities);
		    browser.get(ICimaCommonConstants.DELETE_CONFIG_URL);
		    browser.findElement(By.xpath("//*[local-name() = 'a' and text() = 'accountLink']	")).click();
	    	browser.findElement(By.xpath("//*[local-name() = 'li' and text() = 'XB3 device']"));
	    } catch(Exception e) {
			browser.quit();
			throw new IllegalStateException("Failed to connect to XB3 device",e);
	    }
	    try {
	    	browser.findElement(By.xpath("//*[local-name() = 'a' and text() = 'deleteconfig']")).click();
	    } catch(Exception e) {
	    	//ignore this exception, because for some test cases, it is expected not to find this "deleteconfig" link
	    }
		browser.quit();
	}
}
