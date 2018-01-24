package com.comcast.cima.test.ui.cucumber.steps;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.comcast.cima.test.ui.pageobject.XfinityConstantGuard;
import com.comcast.cima.test.ui.pageobject.XfinityContactCustomerSupport;
import com.comcast.cima.test.ui.pageobject.XfinityDVRManager;
import com.comcast.cima.test.ui.pageobject.XfinityEmailAndVoice;
import com.comcast.cima.test.ui.pageobject.XfinityGetApps;
import com.comcast.cima.test.ui.pageobject.XfinityHelpSupport;
import com.comcast.cima.test.ui.pageobject.XfinityHomeSecurity;
import com.comcast.cima.test.ui.pageobject.XfinityOnDemand;
import com.comcast.cima.test.ui.pageobject.XfinityPrivacyPolicy;
import com.comcast.cima.test.ui.pageobject.XfinityStore;
import com.comcast.cima.test.ui.pageobject.XfinityStoreLocator;
import com.comcast.cima.test.ui.pageobject.XfinityTVListing;
import com.comcast.cima.test.ui.pageobject.XfinityTermsOfService;
import com.comcast.cima.test.ui.pageobject.XfinityUpgrade;
import com.comcast.cima.test.ui.pageobject.XfinityWatchTVOnline;
import com.comcast.test.citf.core.cucumber.steps.CoreCucumberSteps;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityHome;
import com.comcast.test.citf.core.ui.pom.XfinityMyAccount;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Validate_XfinityPortal {
	
	/** Log */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private XfinityHome xfinityHome;
	private XfinityUpgrade xfinityUpgrade;
	private XfinityHelpSupport xfinityHelp;
	private XfinityHelpSupport xfinitySupport;
	private XfinityMyAccount xfinityMyAccount;
	private XfinityMyAccount xfinityFindMyAccount;
	private XfinityMyAccount xfinityManageMyAccount;
	private XfinityMyAccount parentalControl;
	private XfinityWatchTVOnline xfinityTVGo;
	private XfinityEmailAndVoice email;
	private XfinityTVListing xfinityGuide;
	private XfinityDVRManager xfinitySaved;
	private XfinityOnDemand xfinityOnDemand;
	private XfinityConstantGuard xfinityCG;
	private XfinityStoreLocator xfinitySL;
	private XfinityWatchTVOnline xfinityWatchTVOnline;
	private XfinityDVRManager xfinityDVRManager;
	private XfinityContactCustomerSupport xfinityCustSupport;
	private XfinityHomeSecurity xfinityHomeSecurity;
	private XfinityGetApps xfinityApps;
	private XfinityTVListing xfinityTVListing;
	private XfinityStore purchaseMovie;
	private XfinityPrivacyPolicy xfinityPrivacyPolicy;
	private XfinityTermsOfService xfinityTermsofService;
	private Object pageObj;
	
	@Autowired
	private CoreCucumberSteps coreCucumberSteps;
	
	@Autowired
	private UICommonSteps uiCommonSteps;
	
	@Autowired
	private CitfTestInitializer citfTestInitializer;
	
	
	@Then("^xfinity top navigational menu is loaded successfully$")
	public void topNavigationMenuLoad() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"Xfinity portal top navigation menu is not loaded successfully",
				xfinityHome.isTopNavigationLoadedSuccessfully(),
				is(true));
		
		logger.info("xfinity top navigational menu is loaded successfully");
	}
	
	@And("^xfinity help & support menu is loaded successfully$")
	public void helpAndSupportMenuLoad() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"Xfinity portal help & support section is not loaded successfully",
				xfinityHome.isHelpAndSupportLoadedSuccessfully(),
				is(true));
		
		logger.info("xfinity help & support menu is loaded successfully");
	}
	
	@And("^xfinity I Want To section is loaded successfully$")
	public void iWantToSectionMenuLoad() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"Xfinity portal I Want To section is not loaded successfully",
				xfinityHome.isIWantToSectionLoadedSuccessfully(),
				is(true));
		
		logger.info("xfinity I Want To section is loaded successfully");
	}
	
	@And("^xfinity footer menu is loaded successfully$")
	public void footerMenuLoad() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"Xfinity portal footer menu is not loaded successfully",
				xfinityHome.isFooterSectionLoadedSuccessfully(),
				is(true));
		
		logger.info("xfinity footer menu is loaded successfully");
	}
	
	//Validate SSO SLO of Xfinity Shop/Upgrade   ----- Start
	
	@When("^customer opens shop/upgrade from xfinity portal$")
	public void customerOpensShopAndUpgrade() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageUpgradeFromNav();	
		if (obj instanceof XfinityUpgrade) {
			xfinityUpgrade = (XfinityUpgrade) obj;

			logger.info("Shop & upgrade is opened from xfinity portal");
		}
	}
	
	
	@Then("^customer gets signed in to Xfinity shop/upgrade by SSO$")
	public void customerSignsInToShopAndUpgradeBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityUpgrade.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity shop/upgrade by SSO",
				xfinityUpgrade.isUserSignedIn(),
				is(true));
		logger.info("customer gets signed in to Xfinity shop/upgrade by SSO");
	}
	
	@When("^customer reloads Xfinity Shop/Upgrade page$")
	public void customerReloadsShopAndUpgrade() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityUpgrade.getWindowHandle());
		pageObj = xfinityUpgrade.refreshPage();	
		logger.info("Xfinity Shop/Upgrade page is refreshed");
	}
	
	@Then("^customer gets signed out from Xfinity shop/upgrade by SLO$")
	public void customerGetsSignedOutFromShopAndUpgrade() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity shop/upgrade by SLO",
				pageObj,
				instanceOf(SignInToXfinity.class));
		logger.info("customer is signed out from Xfinity shop/upgrade by SLO");
	}
	
	//Validate SSO SLO of Xfinity Shop/Upgrade   ----- End
	
	//Validate SSO SLO of Xfinity Support ------- Start
	
	@When("^customer opens support from xfinity portal$")
	public void customerOpensSupport() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageSupportFromNav();	
		if (obj instanceof XfinityHelpSupport) {
			xfinitySupport = (XfinityHelpSupport) obj;
			
			logger.info("Xfinity Support is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Xfinity support by SSO$")
	public void customerSignsInToSupportBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySupport.getWindowHandle());
		xfinitySupport.refreshPage();
		assertThat(
				"User is not signed in to Xfinity support by SSO",
				xfinitySupport.isUserSignedIn(),
				is(true));
		logger.info("User is signed to Xfinity Support by SSO");
	}
	
	@When("^customer reloads Xfinity support page$")
	public void customerReloadsSupport() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySupport.getWindowHandle());
		xfinitySupport.refreshPage();	
		logger.info("Xfinity Support page is refreshed");
	}
	
	@Then("^customer gets signed out from Xfinity support by SLO$")
	public void customerGetsSignedOutFromSupport() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity support by SLO",
				xfinitySupport.isUserSignedOut(),
				is(true));
		logger.info("User signs out from Xfinity Support by SLO");
	}
	
	//Validate SSO SLO of Xfinity Support ------- End
	
	
	//Validate SSO SLO of My Account ------- Start
	
	@When("^customer opens MyAccount from xfinity portal$")
	public void customerOpensMyAccount() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageMyaccount();
		if (obj instanceof XfinityMyAccount) {
			xfinityMyAccount = (XfinityMyAccount) obj;
			xfinityMyAccount.closeVoiceSecurityPINDialogWindow();
			
			logger.info("MyAccount is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to MyAccount by SSO$")
	public void customerSignsInToMyAccountBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityMyAccount.getWindowHandle());
		xfinityMyAccount.refreshPage();
		assertThat(
				"User is not signed in to My Account by SSO",
				xfinityMyAccount.isUserSignedIn(),
				is(true));
		logger.info("User is signed into MyAccount by SSO");
	}
	
	@When("^customer reloads MyAccount page$")
	public void customerReloadsMyAccount() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityMyAccount.getWindowHandle());
		pageObj = xfinityMyAccount.refreshPage();	
		
		logger.info("MyAccount is refreshed");
	}
	
	@Then("^customer is redirected to Xfinity Sign In$")
	public void customerGetsSignedOutFromMyAccount() throws Exception {
		
		assertThat(
				"Customer is expected to be redirected to Sign in",
				pageObj,
				instanceOf(SignInToXfinity.class));
		
		logger.info("User is redirected to Xfinity Sign In from MyAccount");
	}

	//Validate SSO SLO of My Account ------- Start
	
	
	//Validate SSO SLO of Xfinity TVGo ------- Start
	
	@When("^customer opens TVGo from xfinity portal$")
	public void customerOpensTVGo() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageTVGo();
		if (obj instanceof XfinityWatchTVOnline) {
			xfinityTVGo = (XfinityWatchTVOnline) obj;
			
			logger.info("TVGo is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Xfinity TVGo by SSO$")
	public void customerSignsInToTVGoBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTVGo.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity TVGo by SSO",
				xfinityTVGo.isUserSignedIn(),
				is(true));
		logger.info("User is signed into Xfinity TVGo by SSO");
	}
	
	@When("^customer reloads Xfinity TVGo$")
	public void customerReloadsTVGo() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTVGo.getWindowHandle());
		xfinityTVGo.refreshPage();	
		
		logger.info("Xfinity TVGo page is refreshed");
	}
	
	@Then("^customer gets signed out from Xfinity TVGo by SLO$")
	public void customerGetsSignedOutFromTVGo() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity TVGo by SLO",
				xfinityTVGo.isUserSignedOut(),
				is(true));
		logger.info("User is signed out from Xfinity TVGo by SLO");
	}
	
	//Validate SSO SLO of Xfinity TVGo ------- End

	
	//Validate SSO SLO of Xfinity email ------- Start
	
	@When("^customer opens Email from xfinity portal$")
	public void customerOpensemail() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageEmailNav();
		if (obj instanceof XfinityEmailAndVoice) {
			email = (XfinityEmailAndVoice) obj;
			
			logger.info("Email is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Xfinity Email by SSO$")
	public void customerSignsInToEmailBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),email.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Email by SSO",
				email.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to email by SSO");
	}
	
	@When("^customer reloads Email page$")
	public void customerReloadsEmail() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),email.getWindowHandle());
		this.pageObj = email.refreshPage();	
		
		logger.info("Email page is refreshed");
	}
	
	@Then("^customer gets signed out from Email page by SLO$")
	public void customerGetsSignedOutFromEmail() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Email by SLO",
				this.pageObj,
				instanceOf(SignInToXfinity.class));
		
		logger.info("User is signed out from email by SLO");
	}
	
	//Validate SSO SLO of Xfinity Email ------- End


	//Validate SSO SLO of Xfinity Guide ------- Start
	
	@When("^customer opens Guide from xfinity portal$")
	public void customerOpensGuide() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageGuideFromMoreInNav();
		if (obj instanceof XfinityTVListing) {
			xfinityGuide = (XfinityTVListing) obj;
			
			logger.info("Guide is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Guide by SSO$")
	public void customerSignsInToGuideBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityGuide.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Guide by SSO",
				xfinityGuide.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to guide by SSO");
	}
	
	@When("^customer reloads Guide page$")
	public void customerReloadsGuide() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityGuide.getWindowHandle());
		xfinityGuide.refreshPage();	
		
		logger.info("Guide page is refreshed");
	}
	
	@Then("^customer gets signed out from Guide page by SLO$")
	public void customerGetsSignedOutFromGuide() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Guide by SLO",
				xfinityGuide.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from guide by SLO");
	}
	
	//Validate SSO SLO of Xfinity Guide ------- End


	
	//Validate SSO SLO of Xfinity Saved ------- Start
	
	@When("^customer opens Saved from xfinity portal$")
	public void customerOpensSaved() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageSavedFromMoreInNav();
		if (obj instanceof XfinityDVRManager) {
			xfinitySaved = (XfinityDVRManager) obj;
			
			logger.info("Saved is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Saved by SSO$")
	public void customerSignsInToSavedBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySaved.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Saved by SSO",
				xfinitySaved.isUserSignedIn(),
				is(true));
		logger.info("User is signed in to saved by SSO");
	}
	
	@When("^customer reloads Saved page$")
	public void customerReloadsSaved() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySaved.getWindowHandle());
		xfinitySaved.refreshPage();	
		
		logger.info("Saved page is refreshed");
	}
	
	@Then("^customer gets signed out from Saved page by SLO$")
	public void customerGetsSignedOutFromSaved() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Saved by SLO",
				xfinitySaved.isUserSignedOut(),
				is(true));
		logger.info("User is signed out from Saved by SLO");
	}
	
	//Validate SSO SLO of Xfinity Saved ------- End
	

	
	//Validate SSO SLO of Xfinity OnDemand ------- Start
	
	@When("^customer opens OnDemand from xfinity portal$")
	public void customerOpensOnDemand() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageOnDemandFromMoreInNav();
		if (obj instanceof XfinityOnDemand) {
			xfinityOnDemand = (XfinityOnDemand) obj;
			
			logger.info("OnDemand is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to OnDemand by SSO$")
	public void customerSignsInToOnDemandBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityOnDemand.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity OnDemand by SSO",
				xfinityOnDemand.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to OnDemand by SSO");
	}
	
	@When("^customer reloads OnDemand page$")
	public void customerReloadsOnDemand() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityOnDemand.getWindowHandle());
		xfinityOnDemand.refreshPage();	
		
		logger.info("OnDemand page is refreshed");
	}
	
	@Then("^customer gets signed out from OnDemand page by SLO$")
	public void customerGetsSignedOutFromOnDemand() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity OnDemand by SLO",
				xfinityOnDemand.isUserSignedOut(),
				is(true));
		logger.info("User is signed out from OnDemand by SLO");
	}
	
	//Validate SSO SLO of Xfinity OnDemand ------- End


	//Validate SSO SLO of Xfinity Constant Guard ------- Start
	
	@When("^customer opens Constant Guard from xfinity portal$")
	public void customerOpensConstantGuard() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageConstantGuardFromMoreInNav();
		if (obj instanceof XfinityConstantGuard) {
			xfinityCG = (XfinityConstantGuard) obj;
			
			logger.info("Constant Guard is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Constant Guard by SSO$")
	public void customerSignsInToConstantGuardBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityCG.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Constant Guard by SSO",
				xfinityCG.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Constant Guard by SSO");
	}
	
	@When("^customer reloads Constant Guard page$")
	public void customerReloadsConstantGuard() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityCG.getWindowHandle());
		xfinityCG.refreshPage();	
		
		logger.info("Constant Guard page is refreshed");
	}
	
	@Then("^customer gets signed out from Constant Guard page by SLO$")
	public void customerGetsSignedOutFromConstantGuard() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Constant Guard by SLO",
				xfinityCG.isUserSignedOut(),
				is(true));
		logger.info("User is signed out from Constant Guard by SLO");
	}
	
	//Validate SSO SLO of Xfinity Constant Guard ------- End
	

	//Validate SSO SLO of Xfinity Store Locator ------- Start
	
	@When("^customer opens Store Locator from xfinity portal$")
	public void customerOpensStoreLocator() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageStoreLocator();
		if (obj instanceof XfinityStoreLocator) {
			xfinitySL = (XfinityStoreLocator) obj;
			
			logger.info("Store Locator is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Store Locator by SSO$")
	public void customerSignsInToStoreLocatorBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySL.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Store Locator by SSO",
				xfinitySL.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Store Locator by SSO");
	}
	
	@When("^customer reloads Store Locator page$")
	public void customerReloadsStoreLocator() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinitySL.getWindowHandle());
		xfinitySL.refreshPage();	
		
		logger.info("Store Locator page is refreshed");
	}
	
	@Then("^customer gets signed out from Store Locator page by SLO$")
	public void customerGetsSignedOutFromStoreLocator() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Store Locator by SLO",
				xfinitySL.isUserSignedOut(),
				is(true));
		logger.info("User is signed out from Store Locator by SLO");
	}
	
	//Validate SSO SLO of Xfinity Store Locator ------- End

	
	//Validate SSO SLO of Xfinity watch TV online ------- Start
	
	@When("^customer opens watch TV online from xfinity portal$")
	public void customerOpensWatchTVOnline() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageWatchTVOnline();
		if (obj instanceof XfinityWatchTVOnline) {
			xfinityWatchTVOnline = (XfinityWatchTVOnline) obj;
			
			logger.info("Watch TV online is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to watch TV online by SSO$")
	public void customerSignsInToWatchTVOnlineBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityWatchTVOnline.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity watch TV online by SSO",
				xfinityWatchTVOnline.isUserSignedIn(),
				is(true));
		logger.info("User is signed in to watch TV online by SSO");
	}
	
	@When("^customer reloads watch TV online page$")
	public void customerReloadsWatchTVOnline() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityWatchTVOnline.getWindowHandle());
		xfinityWatchTVOnline.refreshPage();	
		
		logger.info("Watch TV online is refreshed");
	}
	
	@Then("^customer gets signed out from watch TV online page by SLO$")
	public void customerGetsSignedOutFromWatchTVOnline() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity watch TV online by SLO",
				xfinityWatchTVOnline.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from watch TV online by SLO");
	}
	
	//Validate SSO SLO of Xfinity watch TV online ------- End
	

	//Validate SSO SLO of Xfinity parental control ------- Start
	
	@When("^customer opens parental control from xfinity portal$")
	public void customerOpensParentalControl() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageParentalControl();
		if (obj instanceof XfinityMyAccount) {
			parentalControl = (XfinityMyAccount) obj;
			parentalControl.closeVoiceSecurityPINDialogWindow();
			
			logger.info("Parental Control is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to parental control by SSO$")
	public void customerSignsInToParentalControlBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),parentalControl.getWindowHandle());
		parentalControl.refreshPage();
		assertThat(
				"User is not signed in to Xfinity parental control by SSO",
				parentalControl.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to parental control by SSO");
	}
	
	@When("^customer reloads parental control page$")
	public void customerReloadsParentalControl() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),parentalControl.getWindowHandle());
		pageObj = parentalControl.refreshPage();	
		
		logger.info("Parental Control page is reloaded");
	}
	
	//Validate SSO SLO of Xfinity parental control ------- End


	
	//Validate SSO SLO of Xfinity Find My Account Number ------- Start
	
	@When("^customer opens Find My Account Number from xfinity portal$")
	public void customerOpensFindMyAccountNumber() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageFindAccountNumber();
		if (obj instanceof XfinityMyAccount) {
			xfinityFindMyAccount = (XfinityMyAccount) obj;
			xfinityFindMyAccount.closeVoiceSecurityPINDialogWindow();
			
			logger.info("Find My Account Number is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Find My Account Number by SSO$")
	public void customerSignsInToFindMyAccountNumberBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityFindMyAccount.getWindowHandle());
		xfinityFindMyAccount.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Find My Account Number by SSO",
				xfinityFindMyAccount.isUserSignedIn(),
				is(true));
		logger.info("User is signed in to Find My Account Number by SSO");
	}
	
	@When("^customer reloads Find My Account Number page$")
	public void customerReloadsFindMyAccountNumber() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityFindMyAccount.getWindowHandle());
		pageObj = xfinityFindMyAccount.refreshPage();	
		
		logger.info("Find My Account Number page is reloaded");
	}
	
	//Validate SSO SLO of Xfinity Find My Account Number ------- End
	

	//Validate SSO SLO of Xfinity Manage My DVR ------- Start
	
	@When("^customer opens Manage My DVR from xfinity portal$")
	public void customerOpensManageMyDVR() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageManageDVR();
		if (obj instanceof XfinityDVRManager) {
			xfinityDVRManager = (XfinityDVRManager) obj;
			
			logger.info("Manage My DVR is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Manage My DVR by SSO$")
	public void customerSignsInToManageMyDVRBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityDVRManager.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Manage My DVR by SSO",
				xfinityDVRManager.isUserSignedIn(),
				is(true));
		logger.info("User is signed in to Manage My DVR by SSO");
	}
	
	@When("^customer reloads Manage My DVR page$")
	public void customerReloadsManageMyDVR() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityDVRManager.getWindowHandle());
		xfinityDVRManager.refreshPage();	
		
		logger.info("Manage My DVR page is reloaded");
	}
	
	@Then("^customer gets signed out from Manage My DVR page by SLO$")
	public void customerGetsSignedOutFromManageMyDVR() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Manage My DVR by SLO",
				xfinityDVRManager.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Manage My DVR page");
	}
	
	//Validate SSO SLO of Xfinity Manage My DVR ------- End
	

	//Validate SSO SLO of Xfinity Contact Customer Support ------- Start
	
	@When("^customer opens Contact Customer Support from xfinity portal$")
	public void customerOpensContactCustomerSupport() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageCustomerSupport();
		if (obj instanceof XfinityContactCustomerSupport) {
			xfinityCustSupport = (XfinityContactCustomerSupport) obj;
			
			logger.info("Contact Customer Support is opended from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Contact Customer Support by SSO$")
	public void customerSignsInToContactCustomerSupportBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityCustSupport.getWindowHandle());
		xfinityCustSupport.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Contact Customer Support by SSO",
				xfinityCustSupport.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Contact Customer Support by SSO");
	}
	
	@When("^customer reloads Contact Customer Support page$")
	public void customerReloadsContactCustomerSupport() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityCustSupport.getWindowHandle());
		xfinityCustSupport.refreshPage();	
		
		logger.info("Contact Customer Support page is reloaded");
	}
	
	@Then("^customer gets signed out from Contact Customer Support page by SLO$")
	public void customerGetsSignedOutFromContactCustomerSupport() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Contact Customer Support by SLO",
				xfinityCustSupport.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Contact Customer Support by SLO");
	}
	
	//Validate SSO SLO of Xfinity Contact Customer Support ------- End
	
	
	//Validate SSO SLO of Xfinity Home Security and Automation ------- Start
	
	@When("^customer opens Home Security and Automation from xfinity portal$")
	public void customerOpensHomeSecurityAndAutomation() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageHomeSecurityAndControl();
		coreCucumberSteps.setPageObject(obj);
		
		logger.info("Home Security and Automation is opened from Xfinity portal");
	}
	
	@Then("^customer gets signed in to Home Security and Automation by SSO$")
	public void customerSignsInToHomeSecurityAndAutomationBySSO() throws Exception {
		
		XfinityHomeSecurity xfinityHomeSecurity = (XfinityHomeSecurity) coreCucumberSteps.getPageObject();
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityHomeSecurity.getWindowHandle());
		xfinityHomeSecurity.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Home Security and Automation by SSO",
				xfinityHomeSecurity.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Home Security and Automation by SSO");
	}
	
	@When("^customer reloads Home Security and Automation page$")
	public void customerReloadsHomeSecurityAndAutomation() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityHomeSecurity.getWindowHandle());
		xfinityHomeSecurity.refreshPage();	
		
		logger.info("Home Security and Automation page is reloaded");
	}
	
	@Then("^customer gets signed out from Home Security and Automation page by SLO$")
	public void customerGetsSignedOutFromHomeSecurityAndAutomation() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Home Security and Automation by SLO",
				xfinityHomeSecurity.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Home Security and Automation page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Home Security and Automation ------- End

	
	
	//Validate SSO SLO of Xfinity Upgrade My Service ------- Start
	
	@When("^customer opens Upgrade My Service from xfinity portal$")
	public void customerOpensUpgradeMyService() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageUpgradeMyService();
		if (obj instanceof XfinityUpgrade) {
			xfinityUpgrade = (XfinityUpgrade) obj;
			
			logger.info("Upgrade My Service is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Upgrade My Service by SSO$")
	public void customerSignsInToUpgradeMyServiceBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityUpgrade.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Upgrade My Service by SSO",
				xfinityUpgrade.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Upgrade My Service by SSO");
	}
	
	@When("^customer reloads Upgrade My Service page$")
	public void customerReloadsUpgradeMyService() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityUpgrade.getWindowHandle());
		this.pageObj = xfinityUpgrade.refreshPage();	
		
		logger.info("Upgrade My Service page is reloaded");
	}
	
	@Then("^customer gets signed out from Upgrade My Service page by SLO$")
	public void customerGetsSignedOutFromUpgradeMyService() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Upgrade My Service by SLO",
				this.pageObj,
				instanceOf(SignInToXfinity.class));
		
		logger.info("User is signed out from Upgrade My Service page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Upgrade My Service ------- End
	

	//Validate SSO SLO of Xfinity Manage My Account ------- Start
	
	@When("^customer opens Manage My Account from xfinity portal$")
	public void customerOpensManageMyAccount() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageManageMyAccount();
		if (obj instanceof XfinityMyAccount) {
			xfinityManageMyAccount = (XfinityMyAccount) obj;
			xfinityManageMyAccount.closeVoiceSecurityPINDialogWindow();
			
			logger.info("Manage My Account is opended from xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Manage My Account by SSO$")
	public void customerSignsInToManageMyAccountBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityManageMyAccount.getWindowHandle());
		xfinityManageMyAccount.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Manage My Account by SSO",
				xfinityManageMyAccount.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Manage My Account by SSO");
	}
	
	@When("^customer reloads Manage My Account page$")
	public void customerReloadsManageMyAccount() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityManageMyAccount.getWindowHandle());
		pageObj = xfinityManageMyAccount.refreshPage();	
		
		logger.info("Manage My Account page is reloaded");
	}
	
	//Validate SSO SLO of Xfinity Manage My Account ------- End

	
	//Validate SSO SLO of Xfinity Get Apps ------- Start
	
	@When("^customer opens Get Apps from xfinity portal$")
	public void customerOpensGetApps() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageApps();
		if (obj instanceof XfinityGetApps) {
			xfinityApps = (XfinityGetApps) obj;
			
			logger.info("Get Apps is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Get Apps by SSO$")
	public void customerSignsInToGetAppsBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityApps.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Get Apps by SSO",
				xfinityApps.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Get Apps by SSO");
	}
	
	@When("^customer reloads Get Apps page$")
	public void customerReloadsGetApps() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityApps.getWindowHandle());
		xfinityApps.refreshPage();	
		
		logger.info("Get Apps page is reloaded");
	}
	
	@Then("^customer gets signed out from Get Apps page by SLO$")
	public void customerGetsSignedOutFromGetApps() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Get Apps by SLO",
				xfinityApps.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Get Apps page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Get Apps ------- End

	
	//Validate SSO SLO of Xfinity Manage Users and Alerts ------- Start
	
	@When("^customer opens Manage Users and Alerts from xfinity portal$")
	public void customerOpensManageUsersAndAlerts() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageUsersAndAlerts();
		if (obj instanceof XfinityMyAccount) {
			xfinityMyAccount = (XfinityMyAccount) obj;
			xfinityMyAccount.closeVoiceSecurityPINDialogWindow();
			
			logger.info("Manage Users and Alerts is opened from Xfinity home");
		}
	}
	
	@Then("^customer gets signed in to Manage Users and Alerts by SSO$")
	public void customerSignsInToManageUsersAndAlertsBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityMyAccount.getWindowHandle());
		xfinityMyAccount.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Manage Users And Alerts by SSO",
				xfinityMyAccount.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Manage Users and Alerts by SSO");
	}
	
	@When("^customer reloads Manage Users and Alerts page$")
	public void customerReloadsManageUsersAndAlerts() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityMyAccount.getWindowHandle());
		pageObj = xfinityMyAccount.refreshPage();	
		
		logger.info("Manage Users and Alerts page is reloaded");
	}
	
	//Validate SSO SLO of Xfinity Manage My Account ------- End


	//Validate SSO SLO of Xfinity TV Listing ------- Start
	
	@When("^customer opens TV Listing from xfinity portal$")
	public void customerOpensTVListing() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageTVListings();
		if (obj instanceof XfinityTVListing) {
			xfinityTVListing = (XfinityTVListing) obj;
			
			logger.info("TV Listing is opened from xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to TV Listing by SSO$")
	public void customerSignsInToTVListingBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTVListing.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity TV Listing by SSO",
				xfinityTVListing.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to TV Listing by SSO");
	}
	
	@When("^customer reloads TV Listing page$")
	public void customerReloadsTVListing() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTVListing.getWindowHandle());
		xfinityTVListing.refreshPage();	
		
		logger.info("TV Listing page is reloaded");
	}
	
	@Then("^customer gets signed out from TV Listing page by SLO$")
	public void customerGetsSignedOutFromTVListing() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity TV Listing by SLO",
				xfinityTVListing.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from TV Listing page by SLO");
	}
	
	//Validate SSO SLO of Xfinity TV Listing ------- End


	
	//Validate SSO SLO of Xfinity Purchase a Movie ------- Start
	
	@When("^customer opens Purchase a Movie from xfinity portal$")
	public void customerOpensPurchaseAMovie() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPagePurchaseMovie();
		if (obj instanceof XfinityStore) {
			purchaseMovie = (XfinityStore) obj;
			
			logger.info("Purchase a Movie page is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Purchase a Movie by SSO$")
	public void customerSignsInToPurchaseAMovieBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),purchaseMovie.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Purchase a Movie by SSO",
				purchaseMovie.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Purchase a Movie by SSO");
	}
	
	@When("^customer reloads Purchase a Movie page$")
	public void customerReloadsPurchaseAMovie() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),purchaseMovie.getWindowHandle());
		purchaseMovie.refreshPage();	
		
		logger.info("Purchase a Movie page is reloaded");
	}
	
	@Then("^customer gets signed out from Purchase a Movie page by SLO$")
	public void customerGetsSignedOutFromPurchaseAMovie() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Purchase a Movie by SLO",
				purchaseMovie.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Purchase a Movie page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Purchase a Movie ------- End


	
	//Validate SSO SLO of Xfinity Help & Support ------- Start
	
	@When("^customer opens Help & Support from xfinity portal$")
	public void customerOpensHelpAndSupport() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageHelpAndSupport();
		if (obj instanceof XfinityHelpSupport) {
			xfinityHelp = (XfinityHelpSupport) obj;
			
			logger.info("Help & Support is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Help & Support by SSO$")
	public void customerSignsInToHelpAndSupportBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityHelp.getWindowHandle());
		xfinityHelp.refreshPage();
		assertThat(
				"User is not signed in to Xfinity Help & Support by SSO",
				xfinityHelp.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Help & Support by SSO");
	}
	
	@When("^customer reloads Help & Support page$")
	public void customerReloadsHelpAndSupport() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityHelp.getWindowHandle());
		xfinityHelp.refreshPage();	
		
		logger.info("Help & Support page is reloaded");
	}
	
	@Then("^customer gets signed out from Help & Support page by SLO$")
	public void customerGetsSignedOutFromHelpAndSupport() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Help & Support by SLO",
				xfinityHelp.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Help & Support page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Help & Support ------- End


	
	//Validate SSO SLO of Xfinity Privacy Policy ------- Start
	
	@When("^customer opens Privacy Policy from xfinity portal$")
	public void customerOpensPrivacyPolicy() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPagePrivacyPolicy();
		if (obj instanceof XfinityPrivacyPolicy) {
			xfinityPrivacyPolicy = (XfinityPrivacyPolicy) obj;
			
			logger.info("Privacy Policy is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Privacy Policy by SSO$")
	public void customerSignsInToPrivacyPolicyBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityPrivacyPolicy.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Privacy Policy by SSO",
				xfinityPrivacyPolicy.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Privacy Policy by SSO");
	}
	
	@When("^customer reloads Privacy Policy page$")
	public void customerReloadsPrivacyPolicy() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityPrivacyPolicy.getWindowHandle());
		xfinityPrivacyPolicy.refreshPage();	
		
		logger.info("Privacy Policy page is reloaded");
	}
	
	@Then("^customer gets signed out from Privacy Policy page by SLO$")
	public void customerGetsSignedOutFromPrivacyPolicy() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Privacy Policy by SLO",
				xfinityPrivacyPolicy.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Privacy Policy page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Privacy Policy ------- End

	
	//Validate SSO SLO of Xfinity Terms of Service ------- Start
	
	@When("^customer opens Terms of Service from xfinity portal$")
	public void customerOpensTermsOfService() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		Object obj = xfinityHome.getPageTermsOfService();
		if (obj instanceof XfinityTermsOfService) {
			xfinityTermsofService = (XfinityTermsOfService) obj;
			
			logger.info("Terms of Service is opened from Xfinity portal");
		}
	}
	
	@Then("^customer gets signed in to Terms of Service by SSO$")
	public void customerSignsInToTermsOfServiceBySSO() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTermsofService.getWindowHandle());
		assertThat(
				"User is not signed in to Xfinity Terms of Service by SSO",
				xfinityTermsofService.isUserSignedIn(),
				is(true));
		
		logger.info("User is signed in to Terms of Service by SSO");
	}
	
	@When("^customer reloads Terms of Service page$")
	public void customerReloadsTermsOfService() throws Exception {
		
		citfTestInitializer.switchWindow(uiCommonSteps.getBrowser(),xfinityTermsofService.getWindowHandle());
		xfinityTermsofService.refreshPage();	
		
		logger.info("Terms of Service page is reloaded");
	}
	
	@Then("^customer gets signed out from Terms of Service page by SLO$")
	public void customerGetsSignedOutFromTermsOfService() throws Exception {
		
		assertThat(
				"User is not signed out from Xfinity Terms of Service by SLO",
				xfinityTermsofService.isUserSignedOut(),
				is(true));
		
		logger.info("User is signed out from Terms of Service page by SLO");
	}
	
	//Validate SSO SLO of Xfinity Terms of Service ------- End

	
	//Validate Remember Me in Xfinity Portal -- Start

	@Then("^customer is signed in automatically$")
	public void customerSignsInAutomatically() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"User is not signed in to Xfinity Portal automatically with Remember Me",
				xfinityHome.isUserSignedIn(),
				is(true));
	}


	@Then("^customer is not signed in automatically$")
	public void customerNotSignsInAutomatically() throws Exception {
		
		xfinityHome = uiCommonSteps.getXfinityHome();
		assertThat(
				"User is signed in to Xfinity Portal automatically "
				+ "after Remember Me period is expired",
				xfinityHome.isUserSignedIn(),
				is(false));
	}
	
}
