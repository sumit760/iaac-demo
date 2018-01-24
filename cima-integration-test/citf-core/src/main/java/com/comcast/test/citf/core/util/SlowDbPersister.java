package com.comcast.test.citf.core.util;

import java.util.StringTokenizer;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;

import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.mq.CommonMessageConsumer;
import com.comcast.test.citf.core.mq.MQConnectionHandler.QueueNames;
import com.comcast.test.citf.core.ui.pom.ResetPassword;
import com.comcast.test.citf.core.ui.pom.ResetPasswordByEmail;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySMS;
import com.comcast.test.citf.core.ui.pom.ResetPasswordBySQA;
import com.comcast.test.citf.core.ui.pom.ResetPasswordGateway;
import com.comcast.test.citf.core.ui.pom.ResetPasswordMethods;
import com.comcast.test.citf.core.ui.pom.SecurityCheck;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * This class provides method to persist password in database when password of any user gets change during test execution.  
 * 
 * @author Abhijit Rej (arej001c)
 * @since December 2015
 *
 */

public class SlowDbPersister extends CommonMessageConsumer{

	private static final long THREAD_WAITING_TIME = 10000; //1 min
	private boolean isON = true;
	
	/**
	 * Persists changed password in database when password of any user gets change during test execution
	 */
	@Async("executor")
	public void persistNewPasswords(){
		WebDriver browser = null;

		while(isON){
			try{
				String userId = null;
				String password = null;
				String alternateEmail = null;
				String mobile = null;
				String secretAnswer = null;
				String zipCode = null;
				String currentTestMethod = null;
				String saucePlatform = null;
				String sauceDeviceType = null;
				String sauceBrowser = null;
				String pfId = null;
				SignInToXfinity signInToXfinity;
				ResetPasswordBySQA resetPasswordBySQAobj = null;
				ResetPasswordBySMS resetPasswordBySMSobj;
				ResetPasswordByEmail resetPasswordByEmailobj;
				ResetPasswordMethods resetPasswordMethodsobj;
				Object message = receiveMessage(QueueNames.Persist_User_Password_In_Database);
				if(message!=null){
					StringTokenizer tokens = new StringTokenizer(message.toString(), ICommonConstants.COMMA);
					if(tokens.countTokens()==11){
						userId = tokens.nextToken();
						password = tokens.nextToken();
						alternateEmail = tokens.nextToken();
						mobile = tokens.nextToken();
						secretAnswer = tokens.nextToken();
						zipCode = tokens.nextToken();
						currentTestMethod = tokens.nextToken();
						saucePlatform = tokens.nextToken();
						sauceDeviceType = tokens.nextToken();
						sauceBrowser = tokens.nextToken();
						pfId = tokens.nextToken();
					}
				}
				
				if(userId!=null && password!=null){
					LOGGER.info("Strating to persist new password for user : {}", userId);
					
					browser = testInitializer.getBrowserInstance(currentTestMethod, Platforms.valueOf(saucePlatform), Types.valueOf(sauceDeviceType), sauceBrowser, true);
					signInToXfinity = new SignInToXfinity(browser);
					signInToXfinity.setPageFlowId(pfId);
					signInToXfinity.setWindowHandle(browser.getWindowHandle());
					signInToXfinity.get();
					ResetPasswordGateway resetPasswordGatewayobj = (ResetPasswordGateway)signInToXfinity.getPageResetPasswordGateway();
					resetPasswordGatewayobj.enterUsername(userId);
					SecurityCheck SecurityCheckobj = (SecurityCheck)resetPasswordGatewayobj.continueResettingPassword();
					if(alternateEmail.equals("null") && mobile.equals("null")) {
						resetPasswordBySQAobj=(ResetPasswordBySQA)SecurityCheckobj.provideSecurityCodeAndContinue("answer");
					} else if(alternateEmail.equals("null") && !mobile.equals("null")) {
						resetPasswordBySMSobj=(ResetPasswordBySMS)SecurityCheckobj.provideSecurityCodeAndContinue("answer");
						resetPasswordBySQAobj=(ResetPasswordBySQA)resetPasswordBySMSobj.noPhoneAccess();
					} else if(!alternateEmail.equals("null") && mobile.equals("null")) {
						resetPasswordByEmailobj=(ResetPasswordByEmail)SecurityCheckobj.provideSecurityCodeAndContinue("answer");
						resetPasswordBySQAobj=(ResetPasswordBySQA)resetPasswordByEmailobj.noEmailAccess();
					} else if(!alternateEmail.equals("null") && !mobile.equals("null")) {
						resetPasswordMethodsobj=(ResetPasswordMethods)SecurityCheckobj.provideSecurityCodeAndContinue("answer");
						resetPasswordBySQAobj=(ResetPasswordBySQA)resetPasswordMethodsobj.continueResettingPasswordByOtherWay();
					}
					if(resetPasswordBySQAobj ==null){
						LOGGER.error("Not able reset password due to ResetPasswordBySQA page not populated! ");
						return;
					}
					resetPasswordBySQAobj.answerSecretQuestion(secretAnswer);
					resetPasswordBySQAobj.enterZipCode(zipCode);
					ResetPassword resetPasswordobj=(ResetPassword)resetPasswordBySQAobj.continueResettingPassword();
					if(resetPasswordobj ==null){
						LOGGER.error("Not able reset password due to ResetPassword page not populated! ");
						return;
					}
					resetPasswordobj.enterPassword(password);
					resetPasswordobj.reEnterPassword(password);
				    resetPasswordobj.continuePage();
				}
			
			}catch(Exception e){
				LOGGER.error("Error occurred while checking No of messages in queue : ", e);
			}
			finally{
				if(browser!=null){
					browser.quit();
				}
			}
		}
	}
	
	public void switchOff() throws InterruptedException{
		isON = false;
		Thread.sleep(THREAD_WAITING_TIME);
	}
	
	@Autowired
	@Qualifier("citfTestInitializer")
	private CitfTestInitializer testInitializer;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SlowDbPersister.class);
}
