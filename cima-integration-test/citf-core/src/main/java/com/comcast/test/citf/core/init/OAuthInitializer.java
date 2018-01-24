package com.comcast.test.citf.core.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Platforms;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO.Types;
import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.http.Oauth2Handler;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.CodecUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.cache.ICitfCache;
import com.comcast.test.citf.core.ui.pom.OAuthRequestLink;
import com.comcast.test.citf.core.ui.pom.SignInToXfinity;
import com.google.common.base.Joiner;

//Deprecated: will remove this class once every OAuth test will be switched to use Google OAuth API
@Deprecated
@Service("oauthInit")
public class OAuthInitializer extends CitfTestInitializer {
	
	public static enum OauthResultMapKeys{
		AUTH_TOKEN,
		ACCESS_TOKEN,
		STATUS,
		ERROR_DESCRIPTION,
		REFRESH_TOKEN,
		EXPIRES_IN,
		SCOPE,
		DEVICE_CODE,
		USER_CODE,
		VERIFICATION_URI,
		ID_TOKEN
	}
	
	
	public Oauth2Handler getOauth2Handler() throws Exception{
		Oauth2Handler handler = (Oauth2Handler)CoreContextInitilizer.getBean(OAUTH2_HANDLER_BEAN_NAME);
		handler.setClientId(null);
		handler.setScope(null);
		handler.setUserId(null);
		handler.setPassword(null);
		handler.setGrantType(null);
		handler.setClientSecret(null);
		handler.setReqTokenRedirectURL(null);
		
		return handler;
	}
	
	
	public Map<OauthResultMapKeys, String> fetchOauth2AuthToken(	String clientId,
																	String responseType,
																	String scope,
																	String userId,
																	String password,
																	String redirectURL,
																	boolean validationEnabled,
																	boolean reUseAuthToken) throws Exception{
		return this.fetchOauth2AuthToken(clientId, responseType, scope, userId, password, redirectURL, Oauth2Handler.INAVLID_FIELD_VALUE, validationEnabled, reUseAuthToken);
	}
	

	public Map<OauthResultMapKeys, String> fetchOauth2AuthToken(	String clientId,
																	String responseType,
																	String scope,
																	String userId,
																	String password,
																	String redirectURL,
																	String idTokenHint,
																	boolean validationEnabled,
																	boolean reUseAuthToken) throws Exception{
		
		logger.info(StringUtility.appendObjects("Start generating Oauth2/OpenID Auth Token with client id: ", clientId, ", responseType: ", responseType,
				", scope: ", scope, " and userId: ", userId));
		
		if(validationEnabled && (clientId == null || responseType == null || scope == null || userId == null || password == null))
			throw new Exception(Oauth2Handler.AUTH_URL_PARAM_MISSING_EXCEPTION);
		
		String authToken = null;	
		String error = null;
		String errDtls = null;
		WebDriver sauceBrowser = null;
		scope = scope!=null? CodecUtility.encodeURL(scope, ICimaCommonConstants.ENCODING_UTF8) : null;
		ICitfCache mapCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_MAP);
		Map<String, Object> tokensMap = null;
		if(mapCache.getObject(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP)!=null)
			tokensMap = (Map<String, Object>)mapCache.getObject(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP);
		
		if (reUseAuthToken) {
			Map scopeMap = null;
			if(tokensMap != null && tokensMap.get(scope)!=null) {
				scopeMap = (Map<String, String>)tokensMap.get(scope);
				if (scopeMap != null)
					authToken = (String)scopeMap.get(Oauth2Handler.MAP_KEY_AUTH_TOKEN);
			}
		}
		
		if(authToken == null){
			
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			String environment = getCurrentEnvironment();

			if(redirectURL==null)
				redirectURL = configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.REQUEST_TOKEN_REDIRECT_URL.getValue(), environment);
			else if(redirectURL.equalsIgnoreCase("nullValue"))
				redirectURL=null;
			String reqTokenBaseUrl = configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.REQUEST_TOKEN_BASE_URL.getValue(), environment);
		
			if(reqTokenBaseUrl == null || environment == null)
				throw new Exception(Oauth2Handler.AUTH_URL_PARAM_MISSING_EXCEPTION);

			String pfId = null;
			try{
				sauceBrowser = getBrowserInstance("OAuthInitializer.fetchOauth2AuthToken", Platforms.WINDOWS, Types.COMPUTER, BrowserCapabilityDAO.BROWSER_NAME_FIREFOX, false);
				pfId = PageNavigator.start("OAuthInitializer.fetchOauth2AuthToken");
				logger.info(StringUtility.appendObjects("Starting to fetch auth token with saucelab browser: ", sauceBrowser," and PageFlow Id: ",pfId));
				
				OAuthRequestLink linkPage = new OAuthRequestLink(sauceBrowser);
				linkPage.setPageFlowId(pfId);
				String authTokenUrl = Oauth2Handler.getAuthTokenURL(reqTokenBaseUrl, clientId, responseType, redirectURL, scope, idTokenHint);
				Object obj = linkPage.open(authTokenUrl);
				if(obj!=null && obj instanceof SignInToXfinity){
					SignInToXfinity signInPage = (SignInToXfinity)obj;
					String url = signInPage.signInToGetResponseURL(userId, password);
					if(url!=null){
						url = CodecUtility.decodeURL(url, ICimaCommonConstants.ENCODING_UTF8);
						logger.info("OAuth result Page URL :"+url);
						
						authToken = StringUtility.regularExpressionChecker(url, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_CODE, 1);
						if(authToken == null){
							error = StringUtility.regularExpressionChecker(url, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_ERROR, 1);
							errDtls = StringUtility.regularExpressionChecker(url, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_SAUCEBROWSER_ERROR_DESCRIPTION, 1);
						}
					}
					else
						error = "Page[SignInToXfinity] population error ocuured while trying to request on Auth Token URL: "+authTokenUrl;
					
					if(authToken==null && error == null && errDtls == null){
						String responseSrc = sauceBrowser.getPageSource();
						if(!StringUtility.isStringEmpty(responseSrc)){
							logger.debug("OAuth errornous Page source :"+responseSrc);
							error = StringUtility.regularExpressionChecker(responseSrc, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR, 1);
							errDtls = StringUtility.regularExpressionChecker(responseSrc, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR_DESCRIPTION, 1);
						}
					}
					
				}
				else{
					String responseSrc = sauceBrowser.getPageSource();
					if(!StringUtility.isStringEmpty(responseSrc)){
						logger.debug("OAuth errornous Page source : "+responseSrc);
						error = StringUtility.regularExpressionChecker(responseSrc, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR);
						errDtls = StringUtility.regularExpressionChecker(responseSrc, Oauth2Handler.REGEX_OAUTH_AUTH_TOKEN_PAGESOURCE_ERROR_DESCRIPTION);
					}
					else
						error = "Page source not found while trying to request on Auth Token URL: "+authTokenUrl;
				}
				logger.info(StringUtility.appendStrings("OAuth Token fetch result authToken : ", authToken, ", error: ", error, " and error description : ", errDtls));	
				
				if(tokensMap!=null)
					mapCache.put(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP, tokensMap);
			}catch(Exception e){
				logger.error(MiscUtility.getStackTrace(e));
				throw new Exception("Error occured while trying to fetch OAuth token :"+e.getMessage());
			}finally{
				if(sauceBrowser!=null)
					sauceBrowser.quit();

				PageNavigator.close(pfId);
			}
		}
		else
			logger.info("Resusing existing OAuth Token : " + authToken);	
	
		Map<OAuthInitializer.OauthResultMapKeys, String> result = new HashMap<OAuthInitializer.OauthResultMapKeys, String>();			
		if(authToken!=null){
			result.put(OauthResultMapKeys.AUTH_TOKEN, authToken);
			result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
		}
		else{
			result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
			if(errDtls!=null)
				result.put(OauthResultMapKeys.ERROR_DESCRIPTION, errDtls);
			if(error!=null)
				result.put(OauthResultMapKeys.STATUS, error);
		}

		return result;
	}
	
	
	public Map<OauthResultMapKeys, String> fetchOpenIdAuthToken(	String clientId,
																	String responseType,
																	String scope,
																	String userId,
																	String password,
																	String redirectURL,
																	boolean validationEnabled,
																	boolean reUseAuthToken) throws Exception{
		scope = OPENID_SCOPE_PREFIX + scope;
		return this.fetchOauth2AuthToken(clientId, responseType, scope, userId, password, redirectURL, validationEnabled, reUseAuthToken);
	}
	
	public Map<OauthResultMapKeys, String> fetchOpenIdAuthToken(	String clientId,
																	String responseType,
																	String scope,
																	String userId,
																	String password,
																	String redirectURL,
																	String idToken,
																	boolean validationEnabled,
																	boolean reUseAuthToken) throws Exception{
		scope = OPENID_SCOPE_PREFIX + scope;
		return this.fetchOauth2AuthToken(clientId, responseType, scope, userId, password, redirectURL, idToken, validationEnabled, reUseAuthToken);
	}
	
	
	public Map<OauthResultMapKeys, String> fetchOauth2AccessToken(		String clientId,
																		List<String> scopes,
																		String grantType,
																		String clientSecret,
																		String authToken) throws Exception{
		
		Joiner joiner = Joiner.on(ICommonConstants.BLANK_SPACE_STRING).skipNulls();
		
		return fetchOauth2AccessToken(clientId,
									  joiner.join(scopes),
									  grantType,
									  clientSecret,
									  authToken,
									  false,
									  false);
		
	}
	
	
	public Map<OauthResultMapKeys, String> fetchOauth2AccessToken(		String clientId,
																		String scope,
																		String grantType,
																		String clientSecret,
																		String invalidAuthToken,
																		boolean validationEnabled,
																		boolean reuseOldToken) throws Exception{
		
		Map<OauthResultMapKeys, String> result = null;
		String accessToken = null;
			
		Oauth2Handler handler =  getOauth2Handler();
		ICitfCache mapCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_MAP);

		if(handler != null){
			handler.setClientId(clientId);
			handler.setScope(scope);
			handler.setGrantType(grantType);
			handler.setClientSecret(clientSecret);
			
			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			String environment = getCurrentEnvironment();

			initializeOauthHandler(	handler, 
						configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.ACCESS_TOKEN_URL.getValue(), environment), 
						configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.REQUEST_TOKEN_REDIRECT_URL.getValue(), environment),
						mapCache);

			if(ICommonConstants.GRANT_TYPE_CLIENT_CREDENTIALS.equals(grantType))	//without auth token
				accessToken = handler.fetchAccessToken(validationEnabled, null, false, environment);
			else{
				if(invalidAuthToken==null)
					accessToken = this.fetchAccessTokenUsingExistingToken(	validationEnabled, 
																			true, 
																			environment, 
																			clientId, 
																			clientSecret, 
																			grantType, 
																			scope, 
																			handler.getReqTokenRedirectURL(), 
																			null,
																			null,
																			null,
																			handler);
				
				else if(invalidAuthToken.equalsIgnoreCase("nullAuthCode"))
					accessToken = handler.fetchAccessToken(validationEnabled, null, false, getCurrentEnvironment());
				else
					accessToken = handler.fetchAccessToken(validationEnabled, invalidAuthToken, false, getCurrentEnvironment());
			}
			
			result = new HashMap<OAuthInitializer.OauthResultMapKeys, String>();
			if(accessToken!=null){
				result.put(OauthResultMapKeys.ACCESS_TOKEN, accessToken);
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
				
				if(handler.getRefreshToken()!=null)
					result.put(OauthResultMapKeys.REFRESH_TOKEN, handler.getRefreshToken());
				if(String.valueOf(handler.getExpiresIn())!=null)
					result.put(OauthResultMapKeys.EXPIRES_IN, String.valueOf(handler.getExpiresIn()));
				if(handler.getIdToken()!=null)
					result.put(OauthResultMapKeys.ID_TOKEN, handler.getIdToken());
			
				if(handler.getTokensMap()!=null)
					mapCache.put(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP, handler.getTokensMap());
				
				if(handler.getScope()!=null)
					 result.put(OauthResultMapKeys.SCOPE, handler.getScope());
			}
			else if(handler.getErrorDescription()!=null || handler.getStatus()!=null){
				result.put(OauthResultMapKeys.STATUS, handler.getStatus());
				result.put(OauthResultMapKeys.ERROR_DESCRIPTION, handler.getErrorDescription());
			}
			else
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
		}
		
		return result;
	}	
	
	
	public Map<OauthResultMapKeys, String> fetchOauth2AccessToken(	String clientId,
																	String scope,
																	String responseType,
																	String grantType,
																	String clientSecret,
																	String userId,
																	String password,
																	boolean validationEnabled,
																	boolean reuseOldToken) throws Exception{		

		Map<OauthResultMapKeys, String> result = null;
		String accessToken = null;
			
		Oauth2Handler handler =  getOauth2Handler();
		ICitfCache mapCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_MAP);

		if(handler != null){
			handler.setClientId(clientId);
			handler.setScope(scope);
			handler.setGrantType(grantType);
			handler.setClientSecret(clientSecret);
			handler.setUserId(userId);
			handler.setPassword(password);

			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			String environment = getCurrentEnvironment();

			initializeOauthHandler(	handler, 
				configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.ACCESS_TOKEN_URL.getValue(), environment), 
				configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.REQUEST_TOKEN_REDIRECT_URL.getValue(), environment),
				mapCache);

			accessToken = this.fetchAccessTokenUsingExistingToken(			validationEnabled, 
																			true, 
																			environment, 
																			clientId, 
																			clientSecret, 
																			grantType, 
																			scope, 
																			handler.getReqTokenRedirectURL(), 
																			userId,
																			password,
																			responseType,
																			handler);
			result = new HashMap<OAuthInitializer.OauthResultMapKeys, String>();
			if(accessToken!=null){
				result.put(OauthResultMapKeys.ACCESS_TOKEN, accessToken);
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
			
				if(handler.getRefreshToken()!=null)
					result.put(OauthResultMapKeys.REFRESH_TOKEN, handler.getRefreshToken());
				if(String.valueOf(handler.getExpiresIn())!=null)
					result.put(OauthResultMapKeys.EXPIRES_IN, String.valueOf(handler.getExpiresIn()));
				if(handler.getIdToken()!=null)
					result.put(OauthResultMapKeys.ID_TOKEN, handler.getIdToken());
			
				if(handler.getTokensMap()!=null)
					mapCache.put(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP, handler.getTokensMap());
			}
			else if(handler.getErrorDescription()!=null || handler.getStatus()!=null){
				result.put(OauthResultMapKeys.STATUS, handler.getStatus());
				result.put(OauthResultMapKeys.ERROR_DESCRIPTION, handler.getErrorDescription());
			}
			else
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
		}
			
		return result;
	}
	
	
	public Map<OauthResultMapKeys, String> fetchOpenIdAccessToken (  	String clientId,
																		String scope,
																		String grantType,
																		String clientSecret,
																		String invalidAuthToken,
																		boolean validationEnabled,
																		boolean reuseOldToken) throws Exception{
		scope = OPENID_SCOPE_PREFIX + scope;
		return this.fetchOauth2AccessToken(clientId, scope, grantType, clientSecret, invalidAuthToken, validationEnabled, reuseOldToken);
	}
	
	
	public Map<OauthResultMapKeys, String> fetchAccessTokenWithClientCredential(  	String clientId,
																					String scope,
																					String grantType,
																					String clientSecret,
																					boolean validationEnabled) throws Exception{
		return this.fetchOauth2AccessToken(clientId, scope, grantType, clientSecret, null, validationEnabled, false);
	}

	
	public Map<OauthResultMapKeys, String> fetchOauth2AccessTokenUsingRefreshToken(	String refreshToken, 
																					String clientId, 
																					String clientSecret, 
																					String grantType,
																					String userId,
																					String password,
																					String scope,
																					boolean validationEnabled) throws Exception{

		Map<OauthResultMapKeys, String> result = null;
		String accessToken = null;

		Oauth2Handler handler =  getOauth2Handler();
		ICitfCache mapCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_MAP);

		if(handler != null){
			handler.setClientId(clientId);
			handler.setGrantType(grantType);
			handler.setClientSecret(clientSecret);
			handler.setUserId(userId);
			handler.setPassword(password);
			handler.setScope(scope);

			ICitfCache configCache = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS);
			String environment = getCurrentEnvironment();
		
				initializeOauthHandler(	handler, 
										configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.ACCESS_TOKEN_URL.getValue(), environment), 
										configCache.getString(IDataProviderEnums.OAuthUrlPropKeys.REQUEST_TOKEN_REDIRECT_URL.getValue(), environment),
										mapCache);
		
			accessToken = handler.fetchAccessToken(refreshToken, validationEnabled);
		
			result = new HashMap<OAuthInitializer.OauthResultMapKeys, String>();
			if(accessToken!=null){
				result.put(OauthResultMapKeys.ACCESS_TOKEN, accessToken);
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
		
				if(handler.getRefreshToken()!=null)
					result.put(OauthResultMapKeys.REFRESH_TOKEN, handler.getRefreshToken());
				if(String.valueOf(handler.getExpiresIn())!=null)
					result.put(OauthResultMapKeys.EXPIRES_IN, String.valueOf(handler.getExpiresIn()));
		
				if(handler.getTokensMap()!=null)
					mapCache.put(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP, handler.getTokensMap());
			}
			else if(handler.getErrorDescription()!=null || handler.getStatus()!=null){
				result.put(OauthResultMapKeys.STATUS, handler.getStatus());
				result.put(OauthResultMapKeys.ERROR_DESCRIPTION, handler.getErrorDescription());
			}
			else
				result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
		}
		
		return result;
	}
	
	/*
	 * This will fetch OAuth device activation response with the client id provided.
	 * For invalid cases -
	 * 	I. 	Provide ICommonConstants.BLANK_STRING as clientId when the URL needs to have 'client_id' param with no value
	 *  II.	Provide Oauth2Handler.INAVLID_FIELD_VALUE as clientId when the URL needs not to have 'client_id' param at all
	 */
	public Map<OauthResultMapKeys, String> fetchDeviceActivationResponse(String clientId) throws Exception{
		Map<OauthResultMapKeys, String> result = null;
	
		Oauth2Handler handler =  getOauth2Handler();
		String url = ObjectInitializer.getCache(ICimaCommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.OAuthUrlPropKeys.DEVICE_ACTIVATION_REQUEST_URL.getValue(), getCurrentEnvironment());
		
		Map<String, String> response= handler.fetchDeviceActivationResponse(url, clientId);
		result = new HashMap<OauthResultMapKeys, String>();
		if(response==null){
			result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_ERROR);
			
			if(handler.getErrorDescription()!=null)
				result.put(OauthResultMapKeys.ERROR_DESCRIPTION, handler.getErrorDescription());
		}
		else{
			if(response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_DEVICE_CODE)!=null)
				result.put(OauthResultMapKeys.DEVICE_CODE, response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_DEVICE_CODE));
			
			if(response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_USER_CODE)!=null)
				result.put(OauthResultMapKeys.USER_CODE, response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_USER_CODE));
			
			if(response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_VERIFICATION_URI)!=null)
				result.put(OauthResultMapKeys.VERIFICATION_URI, response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_VERIFICATION_URI));
			
			if(response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_EXPIRES_IN)!=null)
				result.put(OauthResultMapKeys.EXPIRES_IN, response.get(Oauth2Handler.DEVICE_ACT_RESP_MAP_KEY_EXPIRES_IN));
			
			result.put(OauthResultMapKeys.STATUS, ICommonConstants.OPERATION_STATUS_SUCCESS);
		}
		
		return result;
	}
	
	
	
	private String fetchAccessTokenUsingExistingToken(	boolean validationEnabled, 
														boolean useExisting, 
														String environment, 
														String clientId,
														String clientSecret,
														String grantType,
														String scope,
														String reqTokenRedirectURL,
														String userId,
														String password,
														String responseType,
														Oauth2Handler handler) throws Exception{
		String accessToken = null;
		String authCode = null;
		
		logger.info(StringUtility.appendStrings("Start generating Oauth2/OpenID Access Token with clientId: ",clientId,", clientSecret: ",clientSecret,
				", grantType: ",grantType," and scope: ",scope));
		
		if(validationEnabled && (clientId == null || clientSecret == null || grantType == null || scope == null  || reqTokenRedirectURL == null))
			throw new Exception(Oauth2Handler.ACCESS_TOKEN_PARAM_MISSING_EXCEPTION);
		
		
		String existingToken = Oauth2Handler.NOT_FOUND;
		Map<String, Object> tokensMap = handler.getTokensMap();
		
		if(useExisting)
			existingToken = handler.checkExistingToken(tokensMap, handler.getScope(), Oauth2Handler.MAP_KEY_ACCESS_TOKEN);
		
		if(Oauth2Handler.NOT_FOUND.equals(existingToken)){
			
			if(handler.getTokensMap().get(scope)!=null && ((Map<String, String>)tokensMap.get(scope)).get(Oauth2Handler.MAP_KEY_AUTH_TOKEN)!=null)
				authCode = (String)((Map<String, String>)tokensMap.get(scope)).get(Oauth2Handler.MAP_KEY_AUTH_TOKEN);
			else{
				Map<OauthResultMapKeys, String> authRes = this.fetchOauth2AuthToken(clientId, responseType, scope, userId, password, reqTokenRedirectURL, validationEnabled, useExisting);
				if(authRes!=null && authRes.get(OauthResultMapKeys.AUTH_TOKEN)!=null)
					authCode = authRes.get(OauthResultMapKeys.AUTH_TOKEN);
			}
			
			accessToken = handler.fetchAccessToken(false, authCode, true, environment);
		}
		else
			accessToken = existingToken;
		
		logger.info("Exiting fetchAuthToken with Oauth2 Access Token : "+accessToken);
		return accessToken;
	}
	
	private void initializeOauthHandler(Oauth2Handler handler, String accessTokenUrl, String reqTokenRedirectUrl, ICitfCache mapCache) throws Exception{

		if(accessTokenUrl!=null)
			handler.setAccessTokenUrl(accessTokenUrl);
		
		if(reqTokenRedirectUrl!=null)
			handler.setReqTokenRedirectURL(reqTokenRedirectUrl);
		
		Map<String, Object> tokensMap = null;
		if(mapCache.getObject(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP)!=null)
			tokensMap = (Map<String, Object>)mapCache.getObject(Oauth2Handler.CACHE_KEY_OAUTH_TOKEN_MAP);
		
		handler.setTokensMap(tokensMap);
	}
	
	private static final String OPENID_SCOPE_PREFIX = "openid ";
		
	private static final String OAUTH2_HANDLER_BEAN_NAME = "oauth2Handler";
	public Logger logger = LoggerFactory.getLogger(OAuthInitializer.class);

}
