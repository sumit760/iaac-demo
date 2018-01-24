package com.comcast.test.citf.common.http;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.cima.jsonObjs.AccessTokenResponseJSON;
import com.comcast.test.citf.common.cima.jsonObjs.DeviceActivationResponseJSON;
import com.comcast.test.citf.common.parser.JSONParserHelper;
import com.comcast.test.citf.common.util.CodecUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;


/**
 * Class to handle OAuth operations like getting authorization token, access token and 
 * Id token. This class is out dated and soon be deprecated. 
 * 
 * CITF recommends to use Google OAuth 2.0 library for all OAuth operations.
 * 
 * @author arej001c, spal004c
 *
 */
@Deprecated
@Service("oauth2Handler")
public class Oauth2Handler {
	
	
	/**
	 * Returns Access token for OAuth2 using Authorization Token
	 * 
	 * @param validationEnabled 
	 * 					Depending upon this parameter's value(True or False), validity of all parameters are being checked
	 * @param authToken
	 * 					The authToken
	 * @param noLog  
	 * 				    Boolean value depending on the use of Execution log 
	 * @param environment 
	 *                  The environment
	 *                  
	 * @return The OAuth access token.
	 * @throws Exception
	 */
	public String fetchAccessToken(boolean validationEnabled, String authToken, boolean noLog, String environment) throws Exception{
		String accessToken = null;
		
		if(!noLog){
			logger.info(StringUtility.appendStrings("Start generating Oauth2/OpenID Access Token with clientId: ",this.clientId,", clientSecret: ",this.clientSecret,
					", grantType: ",this.grantType,", scope: ",this.scope," and authToken: ",authToken));
		}
		if(validationEnabled && (this.clientId == null || this.clientSecret == null || this.grantType == null || this.scope == null || this.accessTokenUrl == null 
				|| this.reqTokenRedirectURL == null || (authToken == null && !ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS.equals(this.grantType)))){
			throw new Exception(ACCESS_TOKEN_PARAM_MISSING_EXCEPTION);
		}
		try{
			Map<String, String> parameters = new HashMap<String, String>();
			
			if(!INAVLID_FIELD_VALUE.equals(this.grantType)){
				parameters.put(GRANT_TYPE, this.grantType);
			}
			if(!INAVLID_FIELD_VALUE.equals(this.reqTokenRedirectURL)){
				parameters.put(REDIRECT_URI2, this.reqTokenRedirectURL);
			}
			if(!INAVLID_FIELD_VALUE.equals(this.scope)){
				parameters.put(SCOPE2, this.scope);
			}
			if(!INAVLID_FIELD_VALUE.equals(authToken) && !ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS.equals(this.grantType)){
				parameters.put(CODE, authToken);
			}
			parameters.put(CLIENT_ID, this.clientId);
			parameters.put(CLIENT_SECRET, this.clientSecret);
			
			String output = httpCaller.executeWriteRequest(this.accessTokenUrl, parameters, null, RestHandler.WriteRequestMethod.POST);
			logger.info(StringUtility.appendObjects("Oauth2 Access token fetched with request: ",parameters," and the response before parsing : ",output));
			 
			if(!StringUtility.isStringEmpty(output) && output.contains(ICommonConstants.START_CURLY_BRACE) && output.contains(ICommonConstants.END_CURLY_BRACE)){
				accessToken = output.substring(output.indexOf(ICommonConstants.START_CURLY_BRACE), output.indexOf(ICommonConstants.END_CURLY_BRACE)+1);
				 
				 Object paresdObject = null;
				 try{
					 paresdObject = JSONParserHelper.parseJSON(accessToken, AccessTokenResponseJSON.class);
				 }
				 catch(Exception e){
					 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 this.errorDescription = e.getMessage();
				 }
						 
				 if(paresdObject!=null){
					 AccessTokenResponseJSON parsedResponse = (AccessTokenResponseJSON)paresdObject;
					 accessToken = parsedResponse.getAccess_token();
					 
					 if(accessToken!=null){
						 this.refreshToken = parsedResponse.getRefresh_token();
						 this.expiresIn = parsedResponse.getExpires_in();
						 this.idToken = parsedResponse.getId_token();
						 
						 if (this.scope!=null && tokensMap!=null && !this.scope.equalsIgnoreCase(INAVLID_FIELD_VALUE) && tokensMap.get(this.scope)!=null) {
							 Map scopeMap = (Map<String, String>)tokensMap.get(this.scope);
							 scopeMap.put(MAP_KEY_ACCESS_TOKEN, accessToken);
							 scopeMap.put(MAP_KEY_REFRESH_TOKEN, this.refreshToken);
							 scopeMap.put(MAP_KEY_GENERATED_ON, MiscUtility.getCurrentTimeInMillis());
							 tokensMap.put(this.scope, scopeMap);
						 }

						 this.status = ICommonConstants.OPERATION_STATUS_SUCCESS;
					 }
					 else{
						 if(parsedResponse.getError_description()!=null)
							 this.errorDescription = parsedResponse.getError_description();
						 
						 if(parsedResponse.getError()!=null)
							 this.status = parsedResponse.getError();
						 else
							 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 }
				 }
			}
			else if(!StringUtility.isStringEmpty(output) && 
					 (output.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND) || output.contains(ICommonConstants.UI_PAGE_ERROR_INTERNAL_ERROR))){
				 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
				 this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
			}
			else
				 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 
		}catch(Exception e){
			logger.error(MiscUtility.getStackTrace(e));
			throw new Exception(e.getMessage());
		} finally {
			
		}
		
		if(!noLog)
			logger.info("Exiting fetchAuthToken with Oauth2 Access Token : "+accessToken);
		
		return accessToken;
	}
	
	
	/**
	 * Returns Access token for OAuth2 using refresh Token.
	 * 
	 * @param refreshToken 
	 * 						The refreshToken.
	 * @param validationEnabled 
	 * 						Depending upon this parameter's value (True or False), validity of 
	 * 						all parameters are being checked.
	 * 
	 * @return OAuth access token.
	 * @throws Exception
	 */
	public String fetchAccessToken(String refreshToken, boolean validationEnabled) throws Exception{
		String accessToken = null;
		logger.info("Start generating Oauth2 Access Token using refresh token : "+refreshToken);
		
		if(validationEnabled && (this.clientId == null || this.clientSecret == null || this.grantType == null || this.accessTokenUrl == null 
				|| refreshToken == null || this.userId == null || this.password == null))
			throw new Exception(ACCESS_TOKEN_PARAM_MISSING_EXCEPTION);
		
		try{
			Map<String, String> parameters = new HashMap<String, String>();
			
			if(!INAVLID_FIELD_VALUE.equals(refreshToken))
				parameters.put(REFRESH_TOKEN, refreshToken);
			
			if(!INAVLID_FIELD_VALUE.equals(this.grantType))
				parameters.put(GRANT_TYPE, this.grantType);
			
			if(!INAVLID_FIELD_VALUE.equals(this.clientId))
				parameters.put(CLIENT_ID, this.clientId);
			
			if(!INAVLID_FIELD_VALUE.equals(this.clientSecret))
				parameters.put(CLIENT_SECRET, this.clientSecret);
			
			
			String output = httpCaller.executeWriteRequest(this.accessTokenUrl, parameters, null, RestHandler.WriteRequestMethod.POST);
			logger.info(StringUtility.appendObjects("Oauth2 Access token fetched with request: ",parameters," and the response before parsing : ",output));
			 
			if(!StringUtility.isStringEmpty(output) && output.contains(ICommonConstants.START_CURLY_BRACE) && output.contains(ICommonConstants.END_CURLY_BRACE)){
				 accessToken = output.substring(output.indexOf(ICommonConstants.START_CURLY_BRACE), output.indexOf(ICommonConstants.END_CURLY_BRACE)+1);
				 
				 Object paresdObject = null;
				 try{
					 paresdObject = JSONParserHelper.parseJSON(accessToken, AccessTokenResponseJSON.class);
				 }
				 catch(Exception e){
					 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 this.errorDescription = e.getMessage();
				 }
						 
				 if(paresdObject!=null){
					 AccessTokenResponseJSON parsedResponse = (AccessTokenResponseJSON)paresdObject;
					 accessToken = parsedResponse.getAccess_token();
					 
					 if(accessToken!=null){
						 this.refreshToken = parsedResponse.getRefresh_token();
						 this.expiresIn = parsedResponse.getExpires_in();
						 this.idToken = parsedResponse.getId_token();
						 
						 if (this.scope!=null && !this.scope.equalsIgnoreCase(INAVLID_FIELD_VALUE) && tokensMap.get(this.scope)!=null) {
							 Map scopeMap = (Map<String, String>)tokensMap.get(this.scope);
							 scopeMap.put(MAP_KEY_ACCESS_TOKEN, accessToken);
							 scopeMap.put(MAP_KEY_REFRESH_TOKEN, this.refreshToken);
							 scopeMap.put(MAP_KEY_GENERATED_ON, MiscUtility.getCurrentTimeInMillis());
						 }

						 this.status = ICommonConstants.OPERATION_STATUS_SUCCESS;
					 }
					 else{
						 if(parsedResponse.getError_description()!=null)
							 this.errorDescription = parsedResponse.getError_description();
						 
						 if(parsedResponse.getError()!=null)
							 this.status = parsedResponse.getError();
						 else
							 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 }
				 }
			}
			else if(!StringUtility.isStringEmpty(output) && 
					 (output.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND) || output.contains(ICommonConstants.UI_PAGE_ERROR_INTERNAL_ERROR))){
				 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
				 this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
			}
			else
				 this.status = ICommonConstants.OPERATION_STATUS_ERROR;
					 
		}catch(Exception e){
			logger.error(MiscUtility.getStackTrace(e));
			throw new Exception(e.getMessage());
		}finally {
		}
		
		logger.info("Exiting fetchAuthToken with Oauth2 Access Token using refresh token: "+accessToken);
		return accessToken;
	}
	
	
	
	/**
	 * Returns Device Activation Response details.
	 * 
	 * @param url 
	 * 				Device activation url.
	 * @param clientId 
	 * 				The client id.
	 * 
	 * @return Device activation response map.
	 * 
	 * @throws Exception
	 */
	public Map<String, String> fetchDeviceActivationResponse(String url, String clientId) throws Exception{
		Map<String, String> resMap = null;
		logger.info("Start generating Device Activation Response using url : "+url);
		this.status = ICommonConstants.OPERATION_STATUS_ERROR;
		
		if(StringUtility.isStringEmpty(url))
			throw new Exception(ACCESS_TOKEN_PARAM_MISSING_EXCEPTION);
		
		try{
			 Map<String, String> parameters = new HashMap<String, String>();
			 if(!INAVLID_FIELD_VALUE.equalsIgnoreCase(clientId))
				 parameters.put(CLIENT_ID, clientId);
			 else
				 parameters.put("invalid_client_id", clientId);
			 
			 String output = httpCaller.executeWriteRequest(url, parameters, null, RestHandler.WriteRequestMethod.POST);
			 logger.info("Device Activation response fetched before parsing : "+output);
			 
			 if(StringUtility.isStringEmpty(output))				 
				 this.errorDescription = "Server seems to be unavaialble";
			 else{
				 if(output.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND))
					 this.errorDescription = ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
				 else if(output.contains(ICommonConstants.UI_PAGE_ERROR_INTERNAL_ERROR))
					 this.errorDescription = "Internal error occured probably due to invalid client id";
				 else{
					 if(!StringUtility.isStringEmpty(output) && output.contains(ICommonConstants.START_CURLY_BRACE) && output.contains(ICommonConstants.END_CURLY_BRACE)){
						 output = output.substring(output.indexOf(ICommonConstants.START_CURLY_BRACE), output.indexOf(ICommonConstants.END_CURLY_BRACE)+1);
						 Object paresdObject = null;
						 try{
							 paresdObject = JSONParserHelper.parseJSON(output, DeviceActivationResponseJSON.class);
						 }
						 catch(Exception e){
							 this.errorDescription = e.getMessage();
						 }
						 
						 if(paresdObject!=null){
							 DeviceActivationResponseJSON parsedResponse = (DeviceActivationResponseJSON)paresdObject;
							 
							 if(parsedResponse!=null){ 
								 if(parsedResponse.getDevice_code()!=null && parsedResponse.getUser_code()!=null && parsedResponse.getVerification_uri()!=null){
									 resMap = new HashMap<String, String>();
									 resMap.put(DEVICE_ACT_RESP_MAP_KEY_DEVICE_CODE, parsedResponse.getDevice_code());
									 resMap.put(DEVICE_ACT_RESP_MAP_KEY_USER_CODE, parsedResponse.getUser_code());
									 resMap.put(DEVICE_ACT_RESP_MAP_KEY_VERIFICATION_URI, parsedResponse.getVerification_uri());
									 resMap.put(DEVICE_ACT_RESP_MAP_KEY_EXPIRES_IN, String.valueOf(parsedResponse.getExpires_in()));
								 
									 this.status = ICommonConstants.OPERATION_STATUS_SUCCESS;
								 }
								 else if(parsedResponse.getError()!=null || parsedResponse.getError_description()!=null){
									 if(parsedResponse.getError_description()!=null)
										 this.errorDescription = parsedResponse.getError_description();
									 else
										 this.errorDescription = parsedResponse.getError();
								 }
								 else
									 this.errorDescription = "Parsed response is empty/invalid.";
							 }
						 }
					 }
				 }
			 }
					 
		}catch(Exception e){
			logger.error(MiscUtility.getStackTrace(e));
			throw new Exception(e.getMessage());
		}
		
		logger.info("Exiting fetchDeviceActivationResponse with device activation response : "+resMap);
		return resMap;
	}
	
	/**
	 * Returns status.
	 * 
	 * @return Status.
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * Returns errorDescription.
	 * 
	 * @return Error description.
	 */
	public String getErrorDescription() {
		return this.errorDescription;
	}

	//This has been kept in case it needs to reset from outside
	public static void setTokenExpiryTime(long tokenExpiryTime) {
		Oauth2Handler.tokenExpiryTime = tokenExpiryTime;
	}
	
	/**
	 * Sets clientId.
	 * 
	 * @param clientId ClientId to set.
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	/**
	 * Sets scope.
	 * 
	 * @param scope Scope to set.
	 * 
	 * @throws Exception
	 */
	public void setScope(String scope) throws Exception{
		if(scope!=null)
			this.scope = CodecUtility.encodeURL(scope, ICimaCommonConstants.ENCODING_UTF8);
		else
			this.scope = null;
	}
	
	/**
	 * Returns scope.
	 * 
	 * @return The scope.
	 */
	public String getScope() {
		return scope;
	}
	
	/**
	 * Sets userId.
	 * 
	 * @param userId userId to set.
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * Sets password.
	 * 
	 * @param password Password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Sets grantType.
	 * 
	 * @param grantType GrantType to set.
	 */
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	
	/**
	 * Sets clientSecret.
	 * 
	 * @param clientSecret Client secret to set.
	 */
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	/**
	 * Sets token Map.
	 * 
	 * @param tokensMap Token Map to set.
	 */
	public void setTokensMap(Map<String, Object> tokensMap) {
		if(tokensMap!=null)
			this.tokensMap = tokensMap;
		else
			this.tokensMap = new HashMap<String, Object>();
	}
	
	/**
	 * Returns tokeMap.
	 * 
	 * @return The token map.
	 */
	public Map<String, Object> getTokensMap() {
		return this.tokensMap;
	}
	
	/**
	 * Sets request token redirect URL.
	 * 
	 * @param reqTokenRedirectURL Request token redirect URL to set.
	 */
	public void setReqTokenRedirectURL(String reqTokenRedirectURL) {
		this.reqTokenRedirectURL = reqTokenRedirectURL;
	}
	
	/**
	 * Returns request token redirect URL.
	 * 
	 * @return request token redirect URL.
	 */
	public String getReqTokenRedirectURL() {
		return this.reqTokenRedirectURL;
	}
	
	/**
	 * Sets access token URL.
	 * 
	 * @param accessTokenUrl The access token URL to set.
	 */
	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}
	
	/**
	 * Returns the refreshToken.
	 * 
	 * @return Refresh token.
	 */
	
	public String getRefreshToken() {
		return this.refreshToken;
	}
	
	/**
	 * Returns expiresIn.
	 * 
	 * @return expiresIn.
	 */

	public long getExpiresIn() {
		return this.expiresIn;
	}
	
	/**
	 * Returns idToken.
	 * 
	 * @return idToken.
	 */
	public String getIdToken() {
		return idToken;
	}

	public static final String CACHE_KEY_OAUTH_TOKEN_MAP = "OAuth2Handler_Key_Token_Map";
	
	public static final String DEVICE_ACT_RESP_MAP_KEY_DEVICE_CODE = "DARM_DEVICE_CODE";
	public static final String DEVICE_ACT_RESP_MAP_KEY_USER_CODE = "DARM_USER_CODE";
	public static final String DEVICE_ACT_RESP_MAP_KEY_VERIFICATION_URI = "DARM_VERIFICATION_URI";
	public static final String DEVICE_ACT_RESP_MAP_KEY_EXPIRES_IN = "DARM_EXPIRES_IN";
	
	/**
	 * Generates the authorization token url based on the scope and other parameters.
	 * 
	 * @param reqTokenBaseURL 
	 * 						  The authorization token base url. 
	 * @param clientId 
	 * 						  The registered client id.
	 * @param responseType 
	 * 						  The response type (code).
	 * @param reqTokenRedirectURL 
	 * 						  Application redirect url.
	 * @param scope 
	 * 						  The scope of the authorization code.
	 * @param idToken
	 * 					      The idToken
	 * @return
	 * 						  The Authorization code URL.
	 * @throws Exception
	 */
	public static String getAuthTokenURL(String reqTokenBaseURL, String clientId, String responseType, String reqTokenRedirectURL, String scope, String idToken) throws Exception{
		String url = null;

		StringBuilder sbf = new StringBuilder();
		if(!INAVLID_FIELD_VALUE.equals(reqTokenBaseURL)){
			sbf.append(reqTokenBaseURL);
		}
		if(!INAVLID_FIELD_VALUE.equals(clientId)){
			sbf.append(URL_FIELD_CLIENT_ID);
			sbf.append(clientId);
		}
		if(!INAVLID_FIELD_VALUE.equals(responseType)){
			sbf.append(URL_FIELD_RESPONSE_TYPE);
			sbf.append(responseType);
		}
		if(!INAVLID_FIELD_VALUE.equals(reqTokenRedirectURL)){
			sbf.append(URL_FIELD_REDIRECT_URI);
			sbf.append(reqTokenRedirectURL);
		}
		if(!INAVLID_FIELD_VALUE.equals(scope)){
			sbf.append(URL_FIELD_SCOPE);
			sbf.append(scope);
		}
		if(!INAVLID_FIELD_VALUE.equals(idToken)){
			sbf.append(URL_FIELD_ID_TOKEN_HINT);
			sbf.append(idToken);
		}
			
		url = sbf.toString();
		return url;	
	}
	
	/**
	 * Checks whether any existing authorization token presents in the token map.
	 * 
	 * @param tokensMap
	 *                   The tokenMap to check.
	 * @param scope 
	 * 					 The scope of the request.
	 * @param tokenKey 
	 * 					 The token key to search
	 * @return
	 * 					 The token if found.
	 */
	
	public String checkExistingToken(Map<String, Object> tokensMap, String scope, String tokenKey){
		String token = NOT_FOUND;
		
		if(tokensMap.get(scope)!=null){
			Map scopeMap = (Map<String, String>)tokensMap.get(scope);
			if(scopeMap.get(tokenKey)!=null && scopeMap.get(MAP_KEY_GENERATED_ON)!=null){
				if(!MiscUtility.isExpired((Long)scopeMap.get(MAP_KEY_GENERATED_ON), tokenExpiryTime))
					token = (String)scopeMap.get(tokenKey);
			}
		}
		else{
			Map scopeMap = new HashMap<String, String>();
			tokensMap.put(scope, scopeMap);
		}	
		
		logger.debug(NOT_FOUND.equals(token)?"Need to generate new Oauth2 Access Token.":"There is an existing valid Oauth2 Access Token, which can be reused.");
		return token;
	}
	
	
	private String clientId = null;
	private String scope = null;
	private String userId = null;
	private String password = null;
	private String grantType = null;
	private String clientSecret = null;
	private String reqTokenRedirectURL = null;
	private String accessTokenUrl = null;
	private String status = null;
	private String errorDescription = null;
	private String refreshToken = null;
	private long expiresIn;
	private String idToken = null;
	private Map<String, Object> tokensMap = null;
	
	public static long tokenExpiryTime	= 3594000;	//bufferTime = 5000 ms
	
	@Autowired
	@Qualifier("restHandler")
	private RestHandler httpCaller;
	
	private static final String URL_FIELD_CLIENT_ID = "client_id=";
	private static final String URL_FIELD_RESPONSE_TYPE = "&response_type=";
	private static final String URL_FIELD_REDIRECT_URI = "&redirect_uri=";
	private static final String URL_FIELD_SCOPE = "&scope=";
	private static final String REDIRECT_URI2 = "redirect_uri";
	private static final String SCOPE2 = "scope";
	private static final String URL_FIELD_ID_TOKEN_HINT = "&id_token_hint=";
	
	private static final String GRANT_TYPE = "grant_type";
	private static final String CODE = "code";
	private static final String CLIENT_SECRET = "client_secret";
	private static final String CLIENT_ID = "client_id";
	private static final String REFRESH_TOKEN = "refresh_token";
	
	public static final String AUTH_URL_PARAM_MISSING_EXCEPTION = "AuthToken Fetch Error: Mandatory parameter(s) [clientId/responseType/scope/userId/password] is(are) missing";
	public static final String ACCESS_TOKEN_PARAM_MISSING_EXCEPTION = "AccessToken Fetch Error: Mandatory parameter(s) [grantType/scope/authCode] is(are) missing";
	
	public static final String NOT_FOUND = "NOT_FOUND";
	public static final String MAP_KEY_AUTH_TOKEN = "MAP_KEY_AUTH_TOKEN";
	public static final String MAP_KEY_ACCESS_TOKEN = "MAP_KEY_ACCESS_TOKEN";
	private static final String MAP_KEY_REFRESH_TOKEN = "MAP_KEY_REFRESH_TOKEN";
	private static final String MAP_KEY_GENERATED_ON = "MAP_KEY_GENERATED_ON";
	public static final String MAP_AUTH_KEY_GENERATED_ON = "MAP_AUTH_KEY_GENERATED_ON";
	
	//Regular expressions 
	public static final String REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR = "error=\"(.*?)\",";
	public static final String REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR_DESCRIPTION = ".*error_description=\"(.*?)\"";
	public static final String REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_CODE = "[a-zA-Z0-9\\:\\/\\?\\-]+code=([a-zA-Z0-9\\.]+)";
	public static final String REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_ERROR = "[a-zA-Z0-9\\:\\/\\?\\-]+error=(.*?)&";
	public static final String REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_ERROR_DESCRIPTION = "[a-zA-Z0-9\\:\\/\\?\\-=]+&error_description=([a-zA-Z0-9\\.:\\s\\[\\]]+)";
	
	public static final String INAVLID_FIELD_VALUE = "null";
	
	private static Logger logger = LoggerFactory.getLogger(Oauth2Handler.class);
}
