package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide OAuth related data.
*
*/
@Service("oAuthDP")
public class OAuthDataProvider extends AbstractDataProvider{
	
	public enum OAuthUrlPropKeys{
		OOB_URL_FOR_CLIENT_ID_LOGIN("test_data.http_oob_url.cleint_id_login"),
		REQUEST_TOKEN_BASE_URL("test_data.oauth_request_token_base_url"),
		HTTP_REDIRECT_URL_FOR_CLIENT_ID_LOGIN("test_data.http_redirect_url.cleint_id_login"),
		REQUEST_TOKEN_REDIRECT_URL("test_data.oauth_request_token_redirect_url"),
		AUTHORIZATION_URL("test_data.authorization_url"),
		ACCESS_TOKEN_URL("test_data.access_token_url"),
		OOB_URL("test_data.oauth_oob_url"),
		TOKEN_INFO_URL("test_data.oauth_token_info_url"),
		DEVICE_ACTIVATION_REQUEST_URL("test_data.oauth_device_activation_request_url"),
		INTERNAL_ACCOUNT_ID_GRANT_SIGN_KEY("test_data.oauth_internal_account_id_grant.sign_key");
		private final String value;
        OAuthUrlPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}

	public String getAccessTokenUrl(){
		return getConfigString(OAuthUrlPropKeys.ACCESS_TOKEN_URL.getValue());
	}

	public String getAuthorizationUrl(){
		return getConfigString(OAuthUrlPropKeys.AUTHORIZATION_URL.getValue());
	}

	public String getOobUrl(){
		return getConfigString(OAuthUrlPropKeys.OOB_URL.getValue());
	}

	public String getTokenInfoUrl(){
		return getConfigString(OAuthUrlPropKeys.TOKEN_INFO_URL.getValue());
	}
	
	public String getOobUrlForLogin(){
		return getConfigString(OAuthUrlPropKeys.OOB_URL_FOR_CLIENT_ID_LOGIN.getValue());
	}
	
	public String getRequestTokenBaseUrl(){
		return getConfigString(OAuthUrlPropKeys.REQUEST_TOKEN_BASE_URL.getValue());
	}
	
	public String getHttepRedirectUrlForLogin(){
		return getConfigString(OAuthUrlPropKeys.HTTP_REDIRECT_URL_FOR_CLIENT_ID_LOGIN.getValue());
	}
	
	public String getRequestTokenRedirectUrl(){
		return getConfigString(OAuthUrlPropKeys.REQUEST_TOKEN_REDIRECT_URL.getValue());
	}
	
	public String getDeviceActivationRequestUrl(){
		return getConfigString(OAuthUrlPropKeys.DEVICE_ACTIVATION_REQUEST_URL.getValue());
	}
	
	public String getInternalAccountIdGrantSignKey(){
		return getConfigString(OAuthUrlPropKeys.INTERNAL_ACCOUNT_ID_GRANT_SIGN_KEY.getValue());
	}
}
