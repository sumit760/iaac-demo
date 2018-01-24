package com.comcast.cima.test.ui.helpers;

import java.net.SocketException;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivation;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationConfirmation;
import com.comcast.cima.test.ui.pageobject.XfinityDeviceActivationSuccess;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.ui.router.PageError;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;

/**
 * Activate device  to get access token through device for valid and invalid data
 * 
 * <br> Note that device is limited input device like set-top box ,game system and other web enabled "limited input device". </br>
 * 
 * @author arej001c
 */
@Service("loginTestUtility")
public class LoginTestUtility {

	/**
	 * Method to activate device using user code, user id and password.This device code is to get oAuth access token.
	 * 
	 * @param userCode
	 *        	UserCode
	 * @param verficationUrl
	 *        	The url which is to be used to activate device (eg. https://login-qa4.comcast.net/oauth/device/activate)
	 * @param userId
	 *        	Xfinity user id.
	 * @param password
	 *        	Xfinity password.
	 * 
	 * @return return device activation status (Success or failed)
	 * 
	 * @throws Exception
	 */
	public synchronized String activateDevice( String userCode,
										  String verficationUrl,
										  String userId,
										  String password) throws Exception{
		LOGGER.info("Starting to activate device with userCode: {}, verficationUrl: {}, userId: and {} password: {}", 
				userCode, verficationUrl, userId, (password!=null?"not null.":"null."));
		
		String result = ICommonConstants.OPERATION_STATUS_ERROR;
		WebDriver browser = null;
		String pfId = null;
		try{
			browser = testInitializer.getBrowserInstance("LoginTestUtility", Platforms.WINDOWS, Types.COMPUTER, BrowserCapabilityDAO.BROWSER_NAME_FIREFOX, true);
			
			pfId = PageNavigator.start("LoginTestUtility.activateDevice");
			XfinityDeviceActivation activationPage = new XfinityDeviceActivation(browser, verficationUrl);
			activationPage.setPageFlowId(pfId);
			activationPage.get();
			
			Object res = activationPage.activateDevice(userCode);
			if(res instanceof SignInToXfinity){
				SignInToXfinity signInPage = (SignInToXfinity)res;
				
				Object obj = signInPage.signinForDeviceActivation(userId, password);
				if(obj!=null){
					if(obj instanceof XfinityDeviceActivationConfirmation){
						XfinityDeviceActivationConfirmation confirmation = (XfinityDeviceActivationConfirmation)obj;
						Object confirmResp = confirmation.goToApp();
						if(confirmResp instanceof XfinityDeviceActivationSuccess){
							result = ICommonConstants.OPERATION_STATUS_SUCCESS;
						} else if(obj instanceof PageError) {
							this.errorDetails = MiscUtility.getPageErrorMessage((PageError)obj);
						}
					} 
					else if(obj instanceof XfinityDeviceActivationSuccess){
						result = ICommonConstants.OPERATION_STATUS_SUCCESS;
					} else if(obj instanceof PageError){
						this.errorDetails = MiscUtility.getPageErrorMessage((PageError)obj);
					}
				}
			}
			else if(res instanceof String){
				this.errorDetails = res.toString();
			} else if(res==null){
				this.errorDetails = "Not able to create Device activation page.";
			}
		}catch(SocketException se){
			throw new Exception(StringUtility.appendStrings("Could not able to connect ", verficationUrl, ":", se.getMessage()));
		}catch(Exception e){
			LOGGER.error("Error occurred while activating device: ", e);
			this.errorDetails = e.getMessage();
		}
		finally{
			if(browser!=null){
				browser.quit();
			}
			PageNavigator.close(pfId);
		}
		
		LOGGER.info("Exiting activate device with result: {}", result);
		return result;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}
	
	
	@Autowired
    @Qualifier("citfTestInitializer")
    private CitfTestInitializer testInitializer;

	private String errorDetails = null;	
	private static Logger LOGGER = LoggerFactory.getLogger(LoginTestUtility.class);
}
