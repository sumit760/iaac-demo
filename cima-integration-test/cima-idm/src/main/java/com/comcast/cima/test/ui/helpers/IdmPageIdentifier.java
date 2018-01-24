package com.comcast.cima.test.ui.helpers;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.comcast.cima.test.ui.pageobject.AWSManagementConsole;
import com.comcast.cima.test.ui.pageobject.BusinessAccount;
import com.comcast.cima.test.ui.pageobject.CompromisedUserAccount;
import com.comcast.cima.test.ui.pageobject.CreateUser;
import com.comcast.cima.test.ui.pageobject.CreateUserConfirmation;
import com.comcast.cima.test.ui.pageobject.EmailValidationSuccess;
import com.comcast.cima.test.ui.pageobject.FacebookLoginPopUp;
import com.comcast.cima.test.ui.pageobject.InformationMismatch;
import com.comcast.cima.test.ui.pageobject.LastStepBeforeFBConnect;
import com.comcast.cima.test.ui.pageobject.MyAccountChangePassword;
import com.comcast.cima.test.ui.pageobject.OpenAWSConsole;
import com.comcast.cima.test.ui.pageobject.RABICheckConfirmation;
import com.comcast.cima.test.ui.pageobject.RABICheckOptions;
import com.comcast.cima.test.ui.pageobject.RecoveryOptions;
import com.comcast.cima.test.ui.pageobject.ResetPasswordSuccess;
import com.comcast.cima.test.ui.pageobject.SMSExpired;
import com.comcast.cima.test.ui.pageobject.SuSiAllSet;
import com.comcast.cima.test.ui.pageobject.SuSiIPAddress;
import com.comcast.cima.test.ui.pageobject.SuSiMobileSent;
import com.comcast.cima.test.ui.pageobject.SuSiNotFindYou;
import com.comcast.cima.test.ui.pageobject.SuSiPageExpire;
import com.comcast.cima.test.ui.pageobject.SuSiSetUpWiFi;
import com.comcast.cima.test.ui.pageobject.SuSiTryAgain;
import com.comcast.cima.test.ui.pageobject.SuSiTryAgainLast;
import com.comcast.cima.test.ui.pageobject.SuSiUpdateSetting;
import com.comcast.cima.test.ui.pageobject.SuSiWelcomeMobile;
import com.comcast.cima.test.ui.pageobject.SuSiWelcomeUsername;
import com.comcast.cima.test.ui.pageobject.SuSiYourAccount;
import com.comcast.cima.test.ui.pageobject.UserByAccountNumber;
import com.comcast.cima.test.ui.pageobject.UserByMobile;
import com.comcast.cima.test.ui.pageobject.UserBySSN;
import com.comcast.cima.test.ui.pageobject.UserLookupSignUp;
import com.comcast.cima.test.ui.pageobject.UserLookupSuccess;
import com.comcast.cima.test.ui.pageobject.UserSMSConfirmation;
import com.comcast.cima.test.ui.pageobject.UsernameLookupConfirmtaion;
import com.comcast.cima.test.ui.pageobject.VerifyIdentity;
import com.comcast.cima.test.ui.pageobject.VerifyUserAfterCreation;
import com.comcast.cima.test.ui.pageobject.Welcome;
import com.comcast.cima.test.ui.pageobject.XfinityUserprofile;
import com.comcast.cima.test.ui.pageobject.XfinityUserprofileSpanish;
import com.comcast.cima.test.ui.pageobject.openDemoApp;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.ui.router.AbstractPageRouter;
import com.comcast.test.citf.core.ui.pom.MyAccountUserPreference;
import com.comcast.test.citf.core.ui.pom.MyAccountWelcome;
import com.comcast.test.citf.core.ui.pom.ResetPassword;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByAgent;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByEmail;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySMS;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySQA;
import com.comcast.test.citf.core.ui.pom.ResetPasswordEmailConfirmation;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.ResetPasswordMethods;
import com.comcast.test.citf.core.ui.pom.ResetPasswordSMSConfirmation;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.comcast.test.citf.core.ui.pom.SignInToXfinitySpanish;
import com.comcast.test.citf.core.ui.pom.XfinityHome;


/**
 * Identifies and initializes the next page object of navigation in the test flow for cima-idm. 
 * <br> Note that any new page object introduction in test flow needs to be added here. </br>
 * 
* @author Abhijit Rej (arej001c)
* @since October 2015
* 
*/
@Service("pageIdentifier")
@Scope("singleton")
public class IdmPageIdentifier extends AbstractPageRouter{
	
	/**
	 * Constructor method
	 * 
	 * This constructor defines the unique title phrases inside titleIdentityMap and unique identity phrases inside contentIdentityMap
	 */
	@SuppressWarnings("rawtypes")
	public IdmPageIdentifier(){
		titleIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		titleIdentityMap.put("Access Email &amp; Voicemail | Get News Online | My XFINITY",									XfinityHome.class);
		titleIdentityMap.put("Amazon.com Sign In",																			OpenAWSConsole.class);
		titleIdentityMap.put("EC2 Management Console",																		AWSManagementConsole.class);
		titleIdentityMap.put("Login",																						openDemoApp.class);
		titleIdentityMap.put("Welcome",																						Welcome.class);
		
		contentIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		
		contentIdentityMap.put("Connect using Facebook", 																	SignInToXfinity.class);
		
		//Password Reset
		contentIdentityMap.put("Unauthorized use of your Comcast ID has been detected", 									CompromisedUserAccount.class);
		contentIdentityMap.put("Recovery Options", 																			RecoveryOptions.class);
		contentIdentityMap.put("Please enter your XFINITY username", 														ResetPasswordGateway.class);
		contentIdentityMap.put("Please type the letters below, to make sure you're a human", 								SecurityCheck.class);
		contentIdentityMap.put("We'll send an email to", 																	ResetPasswordByEmail.class);
		contentIdentityMap.put("Click the link to reset your password",														ResetPasswordEmailConfirmation.class);
		contentIdentityMap.put("Please answer your secret question to reset your password", 								ResetPasswordBySQA.class);
		contentIdentityMap.put("Please select how you would like to recover your password", 								ResetPasswordMethods.class);
		contentIdentityMap.put("We'll send a text message", 																ResetPasswordBySMS.class);
		contentIdentityMap.put("We've just sent a text message", 															ResetPasswordSMSConfirmation.class);
		contentIdentityMap.put("Please create a new password to access your XFINITY account", 								ResetPassword.class);
		contentIdentityMap.put("call 1-800-XFINITY and one of our agents will give you a reset code for password recovery", ResetPasswordByAgent.class);
		contentIdentityMap.put("All set", 																					ResetPasswordSuccess.class);
		contentIdentityMap.put("We've updated your password and you can now sign in to your account",						ResetPasswordSuccess.class);
		
		//Create UID
		contentIdentityMap.put("Please choose a method to verify your identity",											VerifyIdentity.class);
		contentIdentityMap.put("Elija un método para verificar su identidad",												VerifyIdentity.class);
		contentIdentityMap.put("Please complete a security check by typing the letters below",								SecurityCheck.class);
		contentIdentityMap.put("Encontremos su nombre de usuario",															SecurityCheck.class);
		contentIdentityMap.put("Enter the mobile phone number on your account",												UserByMobile.class);
		contentIdentityMap.put("We've just sent a text to",																	UserSMSConfirmation.class);
		contentIdentityMap.put("Let�s set up your XFINITY username",														CreateUser.class);
		contentIdentityMap.put("We just need a little more information to get you started",									CreateUser.class);
		contentIdentityMap.put("Welcome to XFINITY",																		VerifyUserAfterCreation.class);
		contentIdentityMap.put("One last step! Please verify your email address",											VerifyUserAfterCreation.class);
		contentIdentityMap.put("Enter your information below",																UserByAccountNumber.class);
		contentIdentityMap.put("We need some additional information",														UserByAccountNumber.class);
		contentIdentityMap.put("Introduzca la información abajo",															UserByAccountNumber.class);
		contentIdentityMap.put("Social Security number (last four digits)",													UserBySSN.class);
		contentIdentityMap.put("Let's try that again. Please enter the verification code sent to",							SMSExpired.class);
		contentIdentityMap.put("Your alternate email address has been successfully validated",							    EmailValidationSuccess.class);
		contentIdentityMap.put("You already have a username set up", 														CreateUserConfirmation.class);
		contentIdentityMap.put("It's easier than ever to sign in to XFINITY with your preferred email address and password",CreateUserConfirmation.class);
		
		contentIdentityMap.put("The details you've entered are for a Comcast Business account", 							BusinessAccount.class);		
		
		
		//Username lookup
		contentIdentityMap.put("We found a username associated with your account",											UserLookupSuccess.class);
		contentIdentityMap.put("This information doesn't match our records",												InformationMismatch.class);
		contentIdentityMap.put("Let's try that again",																		InformationMismatch.class);
		contentIdentityMap.put("Let's find your username",																	SecurityCheck.class);
		contentIdentityMap.put("We found multiple usernames associated with your account",									UsernameLookupConfirmtaion.class);
		contentIdentityMap.put("We found a username associated with your account",											UsernameLookupConfirmtaion.class);
		contentIdentityMap.put("You don't have a username set-up",															UserLookupSignUp.class);
		contentIdentityMap.put("Usted no tiene un nombre de usuario configuración",											UserLookupSignUp.class);
		
		//facebook connect
		contentIdentityMap.put("Log in to use your Facebook account",														FacebookLoginPopUp.class);
		contentIdentityMap.put("Do you have a Comcast Username and password? Enter it below to connect with your Facebook account",	LastStepBeforeFBConnect.class);
		contentIdentityMap.put("Security Settings",												                            XfinityUserprofile.class);
		contentIdentityMap.put("Configuraciones de seguridad",												                XfinityUserprofileSpanish.class);
		contentIdentityMap.put("Inicia sesi�n en Comcast",											                        SignInToXfinitySpanish.class);
		
		//my account
		contentIdentityMap.put("Users & Preferences"+SEPARATOR+"Rename or link another account"+SEPARATOR+"My profile",		MyAccountUserPreference.class);
		contentIdentityMap.put("Change password",																			MyAccountChangePassword.class);
		contentIdentityMap.put("Welcome to My Account!",																	MyAccountWelcome.class);
		
		//RABI check
		contentIdentityMap.put("Update your password recovery options", 													RABICheckOptions.class);		
		contentIdentityMap.put("Review your password recovery options", 													RABICheckConfirmation.class);	
		
		//SuSi
		contentIdentityMap.put("Oops, we missed some information",															SuSiIPAddress.class);
		contentIdentityMap.put("Enter the mobile number on your account, and we'll text you a verification code",			SuSiWelcomeMobile.class);
		contentIdentityMap.put("We've just sent you a text with a verification code",										SuSiMobileSent.class);
		contentIdentityMap.put("We Can't Find You" + SEPARATOR + "This number isn't associated with your account", 			SuSiNotFindYou.class);
		contentIdentityMap.put("Sign in with your XFINITY username and password to verify your account",				 	SuSiWelcomeUsername.class);
		contentIdentityMap.put("Your Account Info", 																		SuSiYourAccount.class);
		contentIdentityMap.put("<h1 id=\"mainHeading\">Sorry, we're having some trouble</h1>",								SuSiTryAgain.class);
		contentIdentityMap.put("Set Up Your WiFi", 																			SuSiSetUpWiFi.class);
		contentIdentityMap.put("<h1 id=\"mainHeading\">All Set</h1>"+ SEPARATOR + "Your settings have been saved.", 		SuSiAllSet.class);
		contentIdentityMap.put("<h1 id=\"mainHeading\">Let's Try That Again</h1>" + SEPARATOR + 
				"It looks like something went wrong on our end. Please try again to continue your setup.", 					SuSiTryAgainLast.class);
		contentIdentityMap.put("Warning: Page has Expired", 																SuSiPageExpire.class);
		contentIdentityMap.put("Hang Tight", 																				SuSiUpdateSetting.class);
		
		super.logger = this.logger;
	}
	
	private Logger logger = LoggerFactory.getLogger(IdmPageIdentifier.class);
}
