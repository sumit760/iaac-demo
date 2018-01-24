package com.comcast.cima.test.api.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;

import com.comcast.cima.test.dataProvider.IdmTestDataProvider;
import com.comcast.test.citf.common.cima.jsonObjs.IDMUserCredentialValidationResponseJSON;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.exception.TestExecutionException;
import com.comcast.test.citf.common.helpers.PasswordGenerator;
import com.comcast.test.citf.common.http.RestHandler;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.parser.JSONParserHelper;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.AccountCache;
import com.comcast.test.citf.core.cache.UserAttributesCache;
import com.comcast.test.citf.core.init.OAuthInitializer;
import com.comcast.test.citf.core.init.OAuthInitializer.OauthResultMapKeys;
import com.comcast.test.citf.core.init.ObjectInitializer;

public class ITestIDM_API_ValidateUserDetails  extends IdmTestDataProvider{
	
	@BeforeTest(alwaysRun=true)
	public void init(ITestContext context){

			try{
				Map<String, String> localParams = ((XmlClass)context.getCurrentXmlTest().getXmlClasses().get(0)).getLocalParameters();
				sourceId = localParams.get(ICimaCommonConstants.TEST_PARAMETER_CLASS_ID);
				idmCredType = localParams.get(ICommonConstants.TEST_PARAMETER_IDM_CRED_TYPE_KEY);
				idmCredValue = localParams.get(ICommonConstants.TEST_PARAMETER_IDM_CRED_TYPE_VALUE);
				scopeFilterVal = localParams.get(ICimaCommonConstants.TEST_PARAMETER_IDM_SCOPE_KEY);
				String filterAlterEmailid = localParams.get(IdmFilterKeys.ALTERNATE_MAIL.getValue());
				String filterMobileNo = localParams.get(IdmFilterKeys.PHONE.getValue());
				String filterSecretQuestion= localParams.get(IdmFilterKeys.SECRET_QUESTION.getValue());
				filter =  new HashMap<String, Object>();
				if(filterAlterEmailid!=null)
						filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterEmailid);
				if(filterMobileNo!=null)
						filter.put(IdmFilterKeys.PHONE.getValue(), filterMobileNo);
				if(filterSecretQuestion!=null)
						filter.put(IdmFilterKeys.SECRET_QUESTION.getValue(), filterSecretQuestion);
				executionEnvironment = getCurrentEnvironment();
				if(scopeFilterVal.contains("REGISTER_USER")){
					if (filterMobileNo!=null) {
						keySet.add(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
						filter.put(IdmFilterKeys.PHONE.getValue(), filterMobileNo);
					}
					if (filterAlterEmailid!=null) {
						keySet.add(IDMGeneralKeys.FRESH_USER.getValue());
						filter.put(IdmFilterKeys.ALTERNATE_MAIL.getValue(), filterAlterEmailid);
					}
				}
				filter.put(IDMGeneralKeys.SCOPE.getValue(), scopeFilterVal);
				
				oauth= ObjectInitializer.getOAuthService();
				this.getTestData();
				
				if (scopeFilterVal.contains("REGISTER_USER")) {
					LDAPInterface ldap = ObjectInitializer.getLdapService();
					LDAPAuthorization auth = ldap.getAuthorizationData(ldap.new LDAPInstruction(LDAPInterface.SearchType.EQUAL_TO, 
							LDAPInterface.LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID, this.billingAccountId));
					
					if (auth.getDigitalVoiceStatus() != null && (auth.getDigitalVoiceStatus().equalsIgnoreCase("C") ||
																 auth.getDigitalVoiceStatus().equalsIgnoreCase("A")))
						this.cdvStatus = "CDV";
					
					if (auth.getHsdStatus() != null && (auth.getHsdStatus().equalsIgnoreCase("C") ||
							 auth.getHsdStatus().equalsIgnoreCase("A")))
						this.hsdStatus = "HSD";
					
					if (auth.getVidStatus() != null && (auth.getVidStatus().equalsIgnoreCase("C") ||
							 auth.getVidStatus().equalsIgnoreCase("A")))
						this.vidStatus = "VID";
					
					UserNameGeneratorService userNameGen = ObjectInitializer.getUserNameGeneratorService();
					this.userId = userNameGen.next();
					this.password = PasswordGenerator.generatePassword(8, 10, 1, 1, 1);
				}

			}catch(Exception e){
				produceError(sourceId, new TestExecutionException("Error occured while initializing test data :"+e.getMessage(), ITestIDM_API_ValidateUserDetails.class));
			}
		}

	
	
	@Test
	public void checkUserCredForValidData() {
		logger.info("Starting to test OAuth Access token generation for Smart zone service with source Id:"+sourceId);
		
		try{
			if(this.clientId==null || this.scope==null||this.userAttributes==null || this.clientSecret==null || this.account==null)
				throw new Exception(INPUT_DATA_NOT_FOUND_FROM_DP_EXCEPTION);
			
			Map<OauthResultMapKeys, String> resMap = oauth.fetchAccessTokenWithClientCredential(this.clientId, this.scope, ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS, this.clientSecret, true);
			if(resMap!=null){
				if(ICommonConstants.OPERATION_STATUS_SUCCESS.equals(resMap.get(OauthResultMapKeys.STATUS)) && resMap.get(OauthResultMapKeys.ACCESS_TOKEN)!=null){
					accessToken = resMap.get(OauthResultMapKeys.ACCESS_TOKEN);
					
					Map<String, String> headerMap = new HashMap<String, String>();
					Map<String, String> paramMap = new HashMap<String, String>();
					headerMap.put("Authorization", "Bearer "+accessToken);
					String userIdStringArray[];
					
					 if(scope.contains("/sendverificationemail") || scope.contains("/validateverificationemail")){	
						 paramMap.put("billingAccountId", this.account.getAccountId());
						 paramMap.put("customerGuid", this.userAttributes.getGuid());
						 paramMap.put("firstName", this.account.getFirstName());
						 paramMap.put("lastName", this.account.getLastName());
						 if(this.userAttributes.getUserId().contains("@")){
								userIdStringArray=this.userAttributes.getUserId().split("@");
								paramMap.put("userName", userIdStringArray[0]);
							}
						 else {
							 paramMap.put("userName", this.userAttributes.getUserId()); 
						 }
						 paramMap.put("contactEmail", this.userAttributes.getAlterEmail());
						 paramMap.put("prefferedEmail", "true");
						 paramMap.put("flowUrl","https://login-qa4.comcast.net/myaccount/create-uid" );
						 paramMap.put("validationUrlTTLHours", "72");
					 }
					 
					 if(scope.contains("/registeruser")){	
						 paramMap.put("firstName", this.firstName);
						 paramMap.put("lastName", this.lastName); 
						 paramMap.put("displayName",this.userId.split(ICommonConstants.UNDER_SCORE)[0] +" "+this.userId.split(ICommonConstants.UNDER_SCORE)[1]);
						 paramMap.put("phone", this.phoneNumber); 
						 paramMap.put("mobilePhone", "");
						 paramMap.put("uid", this.userId);
						 paramMap.put("password", this.password);
						 paramMap.put("secretQuestion", secretQuestion);
						 paramMap.put("secretAnswer", secretAnswer);
						 paramMap.put("account", this.billingAccountId);
						 paramMap.put("linesOfBusiness", getLineofBusiness());
						 paramMap.put("createEmailFlag", "true");
						 paramMap.put("isPrimary","true" );
						 paramMap.put("parentGUID", "");
						 
					 }
					
					String response="";
					
					if(scope.contains("resetcode#create")){
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), null, headerMap, RestHandler.WriteRequestMethod.POST);
					}
					else if(scope.contains("resetcode#read")){
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), null, headerMap, RestHandler.WriteRequestMethod.GET);
					}
					else if(scope.contains("resetcode#delete")){
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), null, headerMap, RestHandler.WriteRequestMethod.DELETE);
					}
					else if(scope.contains("sendverificationemail") || scope.contains("validateverificationemail")){
						RestHandler handler = ObjectInitializer.getRestHandler();
						handler.setRequestType(MediaType.APPLICATION_JSON);
						response = handler.executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), paramMap, headerMap, RestHandler.WriteRequestMethod.POST);
					}
					else if(scope.contains("credentials#write")){
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), null, headerMap, RestHandler.WriteRequestMethod.GET);
					}
					else if(scope.contains("registeruser")){
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), paramMap, headerMap, RestHandler.WriteRequestMethod.POST);
					}
					else {
						response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmCredType), null, headerMap, RestHandler.WriteRequestMethod.GET);
					}
									
					Object paresdObject = null;
					try{
						 
						 if(response.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)){
							 this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
						 }
						 try{
							 paresdObject = JSONParserHelper.parseJSON(response, IDMUserCredentialValidationResponseJSON.class);
						 }
						 catch(Exception e){
							 this.jsonParsingError = e.getMessage();
						 }
						 
						 if(paresdObject!=null){
							 IDMUserCredentialValidationResponseJSON parsedResponse = (IDMUserCredentialValidationResponseJSON)paresdObject;
							 
							  if(parsedResponse!=null){
								  
								 if(scope.contains("/username#write")){	
									 if(parsedResponse.getUid()!=null){
										 if(parsedResponse.getAvailable().contains("true")){
											logger.info("User id available");
										 }
										 else {
										 logger.info("User id is not available");
									     }
									 } 
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/contactinfo#write") && scopeFilterVal.contains("IDM_CONTACT_EMAIL_AVAILABILITY")){						 
									 if(parsedResponse.getAlternateEmail()!=null){
										 if(parsedResponse.getAvailable().contains("true")){
											logger.info("alternate email available.");
										 }
										 else if(parsedResponse.getAvailable().contains("false") && (parsedResponse.getErrorCode()==null || parsedResponse.getErrorCode()=="")) {
											 logger.info("email id not available");	 
									     }
										 else if(parsedResponse.getAvailable().contains("false") && parsedResponse.getErrorCode().contains("1000") && parsedResponse.getErrorDescription().contains("Invalid email address")){
											 logger.info("Invalid Email address"); 
										 }
										 else {
											 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
										 }
									 } 
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/contactinfo#write") && scopeFilterVal.contains("IDM_MOBILE_NO_AVAILABILITY")){						 
									 if(parsedResponse.getMobilePhoneNumber()!=null){
										 if(parsedResponse.getAvailable().contains("true")){
											logger.info("mobile no available.");
										 }
										 else if(parsedResponse.getAvailable().contains("false") && (parsedResponse.getErrorCode()==null || parsedResponse.getErrorCode()=="")) {
											 logger.info("mobile no not available");	 
									     }
										 else if(parsedResponse.getAvailable().contains("false") && parsedResponse.getErrorCode().contains("2000") && parsedResponse.getErrorDescription().contains("Invalid mobile phone number")){
											 logger.info("invalid mobile no."); 
										 }
										 else {
											 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
										 }
									 } 
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/resetcode#create")){				 
									 if(parsedResponse.getResetCode()!=null){
										 logger.info("reset code generated successully " + parsedResponse.getResetCode());
									 } 
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/resetcode#read")){				 
									if(parsedResponse.getResetCode()!=null){
										 logger.info("reset code generated successully " + parsedResponse.getResetCode());
									 } 
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/sendverificationemail")){				 
									 if(parsedResponse.getStatus().equalsIgnoreCase("success") && parsedResponse.getMessage().equalsIgnoreCase("Message submitted")){
										String verificationLink=readLog("SEND_VERIFICATION_EMAIL", null, "");
										
										 if(verificationLink!=null){
											 logger.info("verfication mail sent successfully");
										 }
										 else {
											 produceError(sourceId, new TestExecutionException("Test case failed as mail not sent: ", ITestIDM_API_ValidateUserDetails.class));
										 }
										 
									 } 
									 else if(parsedResponse.getStatus().equalsIgnoreCase("invalid_request") && parsedResponse.getMessage().equalsIgnoreCase("Invalid input received")){
										 logger.info("invalid input received");
									 }
									 else {
									 produceError(sourceId, new TestExecutionException("Test case failed : ", ITestIDM_API_ValidateUserDetails.class));
									 }
								 }
								 else if(scope.contains("/validateverificationemail")) {
									 if(parsedResponse.getStatus().equalsIgnoreCase("success") && parsedResponse.getMessage().equalsIgnoreCase("Message submitted")){
										 String verificationLink=readLog("SEND_VERIFICATION_EMAIL", null, "");
										 String[] parts = verificationLink.split("\\=");
										 String idmEmailVerificationKey = parts[0];
										 response = ObjectInitializer.getRestHandler().executeWriteRequest(getIDMUserCredUrl(this.scope, idmEmailVerificationKey), null, headerMap, RestHandler.WriteRequestMethod.POST);
										 
										 if(response.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)){
											 this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
										 }
										 try{
											 paresdObject = JSONParserHelper.parseJSON(response, IDMUserCredentialValidationResponseJSON.class);
										 }
										 catch(Exception e){
											 this.jsonParsingError = e.getMessage();
										 }
										 if(paresdObject!=null){
											 IDMUserCredentialValidationResponseJSON parsedResponseVerificationEmail = (IDMUserCredentialValidationResponseJSON)paresdObject;
											  if(parsedResponse!=null){
												  
											  }
											  }
									 }
								 }
								 else if(scope.contains("/registeruser")){
									 if(parsedResponse.getStatus().equalsIgnoreCase("success") && (parsedResponse.getCustGuid()!=null && parsedResponse.getCustGuid()!="")){
										 logger.info("user registered successfully");
										 LDAPInterface ldap = ObjectInitializer.getLdapService(); 
										 List<String> users = new ArrayList<String>(); 
										 users.add("citf005"); 
										 ldap.purgeUsers(users);
									 }
									 else{
										 produceError(sourceId, new TestExecutionException("user registration failed ", ITestIDM_API_ValidateUserDetails.class));
										 }
									 }

								 
						 }
						 }
						 else {
							 if(idmCredType.equalsIgnoreCase(ICommonConstants.IDM_CRED_TYPE_VALUE_NULL)) {
									if((scope.contains("username#write") || scope.contains("/contactinfo#write")) && (this.errorDescription == ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE)){
										logger.info("HTTP response code 404 returned successfully for no input credential");
										}
									else if(scope.contains("resetcode#delete") && response.contains("Request method 'DELETE' not supported")) {
											logger.info("Test case for Reset delete returned 'DELETE not supported' successfully");
										}
									
							 }
							 else if(scope.contains("resetcode#delete") && response.contains("Request method 'DELETE' not supported")) {
										logger.info("Test case for Reset delete returned 'DELETE not supported' successfully");	
							 }
							 else if(scope.contains("credentials#write") && response!=null) {
								 Map sequrityQuestions=parseIDMSecurityQuestionResponse(response);
								 Iterator entries = sequrityQuestions.entrySet().iterator();
								 
								 if(!sequrityQuestions.isEmpty()){
								 while (entries.hasNext()) {
								     Map.Entry entry = (Map.Entry) entries.next();
								     String key = (String)entry.getKey();
								     String value = (String)entry.getValue();
								     if((key.contains("FAVORITE_BEVERAGE") || key.contains("FIRST_CAR") || key.contains("FIRST_JOB") || 
								    		 key.contains("NIECE_NEPHEW") || key.contains("HONEYMOON")|| key.contains("SPORTS_TEAM")|| 
								    		 key.contains("BEST_FRIEND")|| key.contains("FAVORITE_TEACHER")|| key.contains("PET_NAME")|| 
								    		 key.contains("FAVORITE_MOVIE")) && (value==null || value=="")){
								    	 produceError(sourceId, new TestExecutionException("Test case failed due to no value presence against a Security question. ", ITestIDM_API_ValidateUserDetails.class));
								     }
								     else {
								    	 logger.info("Security Question validation Test case executed successfully.");
								     }
								     
								 }
								 }
								 else {
									 logger.debug("Test case for Security questionm validation failed as no security question set returned.");	
									 produceError(sourceId, new TestExecutionException("Test case for Security questionm validation failed as no security question set returned.: ", ITestIDM_API_ValidateUserDetails.class));
								 }	
						  } 
							 
							 
						 } 
					 }
					 catch (Exception e) {
						 produceError(sourceId, new TestExecutionException("Error occured while testing IDM User credential validation method : "+e.getMessage(), ITestIDM_API_ValidateUserDetails.class));
					 }	 
				}
			}
		}
		catch(Exception e){
			produceError(sourceId, new TestExecutionException("Error occured while testing IDM User credential validation method : "+e.getMessage(), ITestIDM_API_ValidateUserDetails.class));
		}
	}
	
	
	public Map parseIDMSecurityQuestionResponse(String response){
		 Map sequrityQuestions = new HashMap();
		 response= response.replace("[", "");
		 response= response.replace("]", "");
		 ArrayList aList= new ArrayList(Arrays.asList(response.split(",")));
		 for(int i=0;i<aList.size();i++)
		 {
			 ArrayList bList= new ArrayList(Arrays.asList(aList.get(i).toString().split(",")));
			 for(int j=0;j<bList.size();j++)
			 {
				 String[] myList = bList.get(j).toString().split(":");
				 sequrityQuestions.put(myList[0],myList[1]);
			 }
		 }
		return sequrityQuestions;	
	}
	
	
	public String getIDMUserCredUrl(String scope, String idmCredType){
		String url="";
		
		if(idmCredType.equalsIgnoreCase(ICommonConstants.IDM_CRED_TYPE_VALUE_EXISTING)){
			if(scope.contains("username#write")){
			url=IDMBASEURL+"uid/"+this.userAttributes.getUserId();
			}
			else if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_CONTACT_EMAIL_AVAILABILITY")){
			url=IDMBASEURL+"alternateEmail/"+this.userAttributes.getAlterEmail();
			}
			else if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_MOBILE_NO_AVAILABILITY")){
			url=IDMBASEURL+"mobilePhoneNumber/"+this.account.getPhoneNumber();
			}
			else if(scope.contains("credentials#write")){
				url=IDMBASEURL+"secretquestions";
			}
			else if(scope.contains("resetcode")){
			url=IDMBASEURL+"resetCode/userName:"+this.userAttributes.getUserId();
			}
			else if(scope.contains("sendverificationemail")){
			url=IDMBASEURL+"sendVerificationEmail";
			}
			else if(scope.contains("validateverificationemail")){
				url=IDMBASEURL+"verify="+idmCredType;
			}
			else if(scope.contains("registeruser")){
				url=IDMBASEURL+"registerUser";
			}
		}
		else if(idmCredType.equalsIgnoreCase(ICommonConstants.IDM_CRED_TYPE_VALUE_NEW)) {
			if(scope.contains("username#write")){
				url=IDMBASEURL+"uid/"+idmCredValue;
				}
				if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_CONTACT_EMAIL_AVAILABILITY")){
				url=IDMBASEURL+"alternateEmail/"+idmCredValue;
				}
				else if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_MOBILE_NO_AVAILABILITY")){
				url=IDMBASEURL+"mobilePhoneNumber/"+idmCredValue;
				}
				else if(scope.contains("resetcode")){
				url=IDMBASEURL+"resetCode/userName:"+idmCredValue;
				}
				else if(scope.contains("credentials#write")){
					url=IDMBASEURL+"secretquestions";
				}
				else if(scope.contains("sendverificationemail")){
				url=IDMBASEURL+"sendverificationemail";
				}
				else if(scope.contains("validateverificationemail")){
					url=IDMBASEURL+"validateverificationemail";
					}
				else if(scope.contains("registeruser")){
					url=IDMBASEURL+"registerUser";
				}
			
		}
		else if(idmCredType.equalsIgnoreCase(ICommonConstants.IDM_CRED_TYPE_VALUE_NULL)) {
			if(scope.contains("username#write")){
				url=IDMBASEURL+"uid/";
				}
				if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_CONTACT_EMAIL_AVAILABILITY")){
				url=IDMBASEURL+"alternateEmail/";
				}
				else if(scope.contains("contactinfo#write") && scopeFilterVal.contains("IDM_MOBILE_NO_AVAILABILITY")){
				url=IDMBASEURL+"mobilePhoneNumber/";
				}
				else if(scope.contains("resetcode")){
				url=IDMBASEURL+"resetCode/userName:";
				}
				else if(scope.contains("credentials#write")){
					url=IDMBASEURL+"secretquestions";
				}
				else if(scope.contains("sendverificationemail")){
				url=IDMBASEURL+"sendverificationemail";
				}
				else if(scope.contains("validateverificationemail")){
					url=IDMBASEURL+"validateverificationemail";
					}
				else if(scope.contains("registeruser")){
					url=IDMBASEURL+"registerUser";
				}
		}
		
		
		
		return url;
		
	}
	
	
	
	
	//******************************** private methods & variables **********************************//*
	
	private String sourceId = null;
	private UserAttributesCache.Attribute userAttributes = null;
	private AccountCache.Account account = null;
	private String userId = null;
	private String password = null;
	private String billingAccountId = null;
	private String firstName = null;
	private String lastName = null;
	private String phoneNumber = null;
	private String cdvStatus = null;
	private String hsdStatus = null;
	private String vidStatus = null;
	private List<String> lob = null;
	private WebDriver browser;
	private String executionEnvironment = null;
	private String errorDetails = null;
	private Map<String, Object> testData = null;
	private Map<String, Object> filter = null;
	private String scopeFilterVal=null;
	OAuthInitializer oauth;
	private String scope=null;
	private String clientId=null;
	private String clientSecret=null;
	private String accessToken=null;
	
	
	private String IDMBASEURL="https://xdn-qa4.xfinity.com/user/v1/";
	private String idmCredType="";
	private String idmCredValue="";
	private String errorDescription="";
	private String jsonParsingError="";
	Set<String> keySet = null;
	private FreshUsers user = null;
	String secretQuestion="What is your favorite beverage?";
	String secretAnswer="COKE";
	
	
	private void getTestData() throws Exception {
		
		if(testData==null){
			if(!scopeFilterVal.contains("REGISTER")){
			
			keySet = new HashSet<String>(Arrays.asList(	IDMGeneralKeys.USER_DETAILS.getValue(),
														IDMGeneralKeys.CLIENT_ID_CLIENT_SECRET.getValue(),
														IDMGeneralKeys.SCOPE.getValue()));
			logger.debug("Calling data provider with keySet = "+keySet.toString());
			testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
			if(testData!=null && testData.get(IDMGeneralKeys.USER_DETAILS.getValue())!=null){
				List<Map<String, Object>> lst = (List<Map<String, Object>>)testData.get(IDMGeneralKeys.USER_DETAILS.getValue());
					if(lst!=null && lst.get(0)!=null){
						Map<String, Object> map = lst.get(0);
						if(map!=null && map.get(KEY_USER)!=null && map.get(KEY_ACCOUNT)!=null){
							this.account = (AccountCache.Account)map.get(KEY_ACCOUNT);
							this.userAttributes = (UserAttributesCache.Attribute)map.get(KEY_USER);	
						}
						
					if(testData.get(IDMGeneralKeys.SCOPE.getValue())!=null)
			                scope = testData.get(IDMGeneralKeys.SCOPE.getValue()).toString();
						}
					if(testData.get(KEY_CLIENT_ID)!=null)
						clientId = testData.get(KEY_CLIENT_ID).toString();
				
					if(testData.get(KEY_CLIENT_SECRET)!=null)
						clientSecret = testData.get(KEY_CLIENT_SECRET).toString();
					}	
			}
			
			else {
				logger.debug("Calling data provider with keySet = "+keySet.toString());
				 testData = getTestData(keySet, filter, getCurrentEnvironment(), sourceId);
				 if(testData!=null && testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue())!=null){
                     AccountCache.Account account = (AccountCache.Account)testData.get(IDMGeneralKeys.FRESH_ACCOUNT.getValue());
					 if(account!=null) {
                          phoneNumber = account.getPhoneNumber();
                          billingAccountId = account.getAccountId();
                          firstName = account.getFirstName();
                          lastName = account.getLastName();
					 }
				 }        
				 if(testData!=null && testData.get(IDMGeneralKeys.FRESH_USER.getValue())!=null){
                     user = (FreshUsers)testData.get(IDMGeneralKeys.FRESH_USER.getValue());
                     String alternateEmail;
					if (user!=null)
                     alternateEmail = user.getAlternativeEmail();
				 }
			}
			logger.info("Getting data from data provider :: userAttributes: "+ this.userAttributes);
			logger.info("Data provider has been called for fetching test data");
		}
		}
	
	
	private String getLineofBusiness() {
		StringBuilder sb = new StringBuilder();
		sb.append("[\"");
		if (this.cdvStatus!=null)
			sb.append(this.cdvStatus).append("\"");
		if (this.hsdStatus!=null)
			sb.append(",\"").append(this.hsdStatus).append("\"");
		if (this.vidStatus!=null)
			sb.append(",\"").append(this.vidStatus).append("\"");
		
		sb.append("]");
		
		return sb.toString();
	}
	
	private static Logger logger = LoggerFactory.getLogger(ITestIDM_API_ValidateUserDetails.class);
}