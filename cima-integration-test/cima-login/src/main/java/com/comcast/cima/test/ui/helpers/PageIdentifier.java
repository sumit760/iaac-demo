package com.comcast.cima.test.ui.helpers;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.comcast.cima.test.ui.pageobject.XfinityConstantGuard;
import com.comcast.cima.test.ui.pageobject.XfinityContactCustomerSupport;
import com.comcast.cima.test.ui.pageobject.XfinityCustomerAgreement;
import com.comcast.cima.test.ui.pageobject.XfinityDVRManager;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationConfirmation;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationSuccess;
import com.comcast.cima.test.ui.pageobject.XfinityEmailAndVoice;
import com.comcast.cima.test.ui.pageobject.XfinityGetApps;
import com.comcast.cima.test.ui.pageobject.XfinityHelpSupport;
import com.comcast.cima.test.ui.pageobject.XfinityHomeSecurity;
import com.comcast.cima.test.ui.pageobject.XfinityOnDemand;
import com.comcast.cima.test.ui.pageobject.XfinityPrivacyPolicy;
import com.comcast.cima.test.ui.pageobject.XfinityShopAndUpgrade;
import com.comcast.cima.test.ui.pageobject.XfinityStore;
import com.comcast.cima.test.ui.pageobject.XfinityStoreLocator;
import com.comcast.cima.test.ui.pageobject.XfinitySymantec;
import com.comcast.cima.test.ui.pageobject.XfinityTVListing;
import com.comcast.cima.test.ui.pageobject.XfinityTermsOfService;
import com.comcast.cima.test.ui.pageobject.XfinityToolbar;
import com.comcast.cima.test.ui.pageobject.XfinityUpgrade;
import com.comcast.cima.test.ui.pageobject.XfinityWatchTVOnline;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.ui.router.AbstractPageRouter;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.XfinityHome;
import com.comcast.test.citf.core.ui.pom.XfinityMyAccount;


/**
 *Identifies and initializes the next page object of navigation in the test flow for cima-login. 
 * <br> Note that any new page object introduction in test flow needs to be added here. </br>
 * 
 * @author Abhijit Rej (arej001c)
 * @since October 2015
 * 
 */
@Service("pageIdentifier")
@Scope("singleton")
public class PageIdentifier extends AbstractPageRouter{
	
	/**
	 * Constructor method
	 * 
	 * This constructor defines the unique title phrases inside titleIdentityMap and unique identity phrases inside contentIdentityMap
	 */
	@SuppressWarnings("rawtypes")
	public PageIdentifier(){
		titleIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		titleIdentityMap.put("Access My Account | Email | Online News | My XFINITY", 									 						XfinityHome.class);
		titleIdentityMap.put("Please Confirm Access", 								    		   											 	XfinityDeviceActivationConfirmation.class);
		titleIdentityMap.put("Sign in to Comcast", 												   											 	SignInToXfinity.class);
		titleIdentityMap.put("Sign in to XFINITY TV", 											   			  								 	SignInToXfinity.class);
		titleIdentityMap.put("XFINITY", 											   			  								 				SignInToXfinity.class);
		titleIdentityMap.put("Comcast Deals, Offers, Specials and Promotions | XFINITY",					   								 	XfinityShopAndUpgrade.class);
		titleIdentityMap.put("Constant Guard - Antivirus and security tools for Xfinity customers", 											XfinityConstantGuard.class);
		titleIdentityMap.put("Comcast Phone Number &amp; Customer Service Contact Info",  			   										 	XfinityContactCustomerSupport.class);
		titleIdentityMap.put("Customer Agreements, Policies &amp; Service Disclosures",			   											 	XfinityCustomerAgreement.class);
		titleIdentityMap.put("DVR Manager | Watch TV Anywhere | Schedule Recordings | Program | XFINITY TV", 								 	XfinityDVRManager.class);
		titleIdentityMap.put("Comcast Customer Service - XFINITY Technical Support", 														 	XfinityHelpSupport.class);
		titleIdentityMap.put("Comcast Customer Service - XFINITY&#174; Technical Support", 													 	XfinityHelpSupport.class);
		titleIdentityMap.put("Comcast | My Account | Ecobill Online Bill Pay | Help &amp; Support",		 									    XfinityMyAccount.class);
		titleIdentityMap.put("Watch TV Online, Stream Full Episodes &amp; Movies | XFINITY TV Go", 												XfinityWatchTVOnline.class);
		titleIdentityMap.put("XFINITY Connect", 																								XfinityEmailAndVoice.class);
		titleIdentityMap.put("XFINITY Connect: Inbox", 																							XfinityEmailAndVoice.class);
		titleIdentityMap.put("XFINITY - Email currently unavailable", 																			XfinityEmailAndVoice.class);
		titleIdentityMap.put("XFINITY Home Security Systems, Alarm Systems from Comcast",		 											 	XfinityHomeSecurity.class);
		titleIdentityMap.put("Watch On Demand | Movies | TV | Watchlist | New | Kids | Premium | Browse | Search | Most Viewed | XFINITY TV", 	XfinityOnDemand.class);
		titleIdentityMap.put("TV Guide, TV Listings, Air Dates and Showtimes from XFINITY TV Go", 											 	XfinityTVListing.class);
		titleIdentityMap.put("XFINITY Privacy Policy",										  												 	XfinityPrivacyPolicy.class);
		titleIdentityMap.put("XFINITY Terms Of Service", 																					 	XfinityTermsOfService.class);
		titleIdentityMap.put("XFINITY TV Store", 																							 	XfinityStore.class);
		titleIdentityMap.put("Norton Download Manager", 																						XfinitySymantec.class);
		titleIdentityMap.put("http://www.xfinity.com/upgrade-center/customer-deals",															XfinityUpgrade.class);
		titleIdentityMap.put("Download XFINITY Toolbar | Search Google | Email Alerts | Spyware Protection | Mac | Windows | XFINITY",		 	XfinityToolbar.class);
		titleIdentityMap.put("Find Locations, Service and Payment Centers - XFINITY",		 													XfinityStoreLocator.class);
		titleIdentityMap.put("TV On The Go, Access to Email and More With XFINITY TV Go Mobile Apps",		 								 	XfinityGetApps.class);
		titleIdentityMap.put("Congratulations", 																 				 				XfinityDeviceActivationSuccess.class);
		
		contentIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		contentIdentityMap.put("http://www.xfinity.com/upgrade-center/customer-deals", 															XfinityUpgrade.class);
		contentIdentityMap.put("Get answers from Comcast agents and customers like you",														XfinityContactCustomerSupport.class);
		
		super.logger = this.logger;
	}
	
	private Logger logger = LoggerFactory.getLogger(PageIdentifier.class);
}
