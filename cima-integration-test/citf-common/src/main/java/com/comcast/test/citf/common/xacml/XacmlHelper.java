package com.comcast.test.citf.common.xacml;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.cima.persistence.TempParameterDAO;
import com.comcast.test.citf.common.cima.persistence.beans.TempParameters;
import com.comcast.test.citf.common.http.SeleniumHelper;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * This class will be used by XACMLRequestBuilder.java to fetch SAML Response after authentication.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 * 
 */
@Service("xacmlHelper")
public class XacmlHelper {
	
	
	/**
	 * Method to fetch SAML response and hence the subjectId to be used in framing XACML request for SAML.
	 * 
	 * @param url IDP Url such as https://idp-qa4.comcast.net/idp/startSSO.ping?PartnerSpId=
	 * @param userId The user name for authentication
	 * @param password The user password for authentication
	 * 
	 * @return The SAML response string
	 */
	protected String fetchSAMLResponseForTVE(String url, String userId, String password) {
		
		String samlResponse = null;
		WebDriver browser = null;
		TempParameters dbParams = null;
		LOGGER.info("Start fetching SAML response");
		
		try{
			if(StringUtility.isStringEmpty(url) || StringUtility.isStringEmpty(userId) || StringUtility.isStringEmpty(password)){
				throw new IllegalArgumentException(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
			}
			
			browser = SeleniumHelper.createHtmlUnitDriver(true);
			browser.get(url);
			SeleniumHelper.waitForButtonElement(browser, TVE_SAML_RESPONSE_FIELD_USER_ID);
			WebElement userEle =browser.findElement(new By.ById(TVE_SAML_RESPONSE_FIELD_USER_ID));
			userEle.sendKeys(userId);
		
			WebElement pswdEle =browser.findElement(new By.ById(TVE_SAML_RESPONSE_FIELD_PASSWORD));
			pswdEle.sendKeys(password);
		
			WebElement signInbutton = browser.findElement(new By.ById(TVE_SAML_RESPONSE_BUTTON_SIGN_IN));
			signInbutton.click();
		
			int loopCount = 0;
			while((dbParams==null || !ICimaCommonConstants.DB_STATUS_ACTIVE.equals(dbParams.getStatus())) && loopCount<DATABASE_OPERATION_FAILURE_LOOP_COUNT){
				TimeUnit.MILLISECONDS.sleep(DATABASE_OPERATION_WAITING_TIME);
				dbParams = tpDAO.findParameterByKey(ICimaCommonConstants.DB_TAB_TEMP_PARAMETERS_KEY_SAML_RESPONSE);
				loopCount++;
			}
		
			if(dbParams==null || !ICimaCommonConstants.DB_STATUS_ACTIVE.equals(dbParams.getStatus())){
				throw new IllegalStateException(ICimaCommonConstants.DB_TAB_TEMP_PARAMETERS_KEY_SAML_RESPONSE+" not found in database with ACTIVE status!!!");
			}
			
			String response = dbParams.getValue();
			response = StringUtility.regularExpressionChecker(response.replace(TVE_SAML_RESPONSE_REPLACEABLE_STRING, TVE_SAML_RESPONSE_REPLACING_STRING), TVE_SAML_RESPONSE_REGEX);
			
			if(response!=null && response.length()>37){
				samlResponse = response.substring(15, response.length()-22);
			}
		}
		catch(Exception e){
			LOGGER.info("Error occurred while fetching SAML response: ", e);
		}
		finally{
			if(browser!=null){
				browser.close();
			}
			tpDAO.deactivateParameter(ICimaCommonConstants.DB_TAB_TEMP_PARAMETERS_KEY_SAML_RESPONSE);
		}
		
		LOGGER.info("Existing fetchSAMLResponseForTVE with SAML response : {}", samlResponse);
		return samlResponse;
	}
	
	

	@Autowired
	private TempParameterDAO tpDAO;
	
	private static final int DATABASE_OPERATION_FAILURE_LOOP_COUNT = 2;
	private static final long DATABASE_OPERATION_WAITING_TIME = 1000;
	
	
	private static final String TVE_SAML_RESPONSE_FIELD_USER_ID = "user";
	private static final String TVE_SAML_RESPONSE_FIELD_PASSWORD = "passwd";
	private static final String TVE_SAML_RESPONSE_BUTTON_SIGN_IN = "sign_in";
	
	private static final String TVE_SAML_RESPONSE_REPLACEABLE_STRING = "<saml:Attribute NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\" Name=\"urn:cablelabs:olca:1.0:attribute:subscriber:identifier\"><saml:AttributeValue xsi:type=\"xs:string\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
	private static final String TVE_SAML_RESPONSE_REPLACING_STRING = "<SAML_Response>";
	private static final String TVE_SAML_RESPONSE_REGEX = "<SAML_Response>.*?</saml:AttributeValue>";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XacmlHelper.class);
}
