package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since May 2016
*
* This is data provider to provide required data for TVE tests.
*
*/
@Service("tveDP")
public class TveDataProvider extends AbstractDataProvider{
	
	public enum TvePropKeys {
		REQUEST_URL("test_data.tve.request_url"),
		USER1_USERNAME("test_data.tve.user1.username"),
		USER1_PASSWORD("test_data.tve.user1.password"),
		USER2_USERNAME("test_data.tve.user2.username"),
		USER2_PASSWORD("test_data.tve.user2.password"),
		OAUTH_REQUESTED_SCOPES("test_data.tve.oauth.requested_scopes"),
		OAUTH_REDIRECT_URL("test_data.tve.oauth.redirect_url"),
		OAUTH_CLIENT_ID("test_data.tve.oauth.client_id"),
		OAUTH_CLIENT_SECRET("test_data.tve.oauth.client_secret"),
		OAUTH_INVALID_ACCESS_TOKEN("test_data.tve.oauth.invalid_access_token"),
		OAUTH_EXPIRED_ACCESS_TOKEN("test_data.tve.oauth.expired_access_token");
		
		private final String value;
		TvePropKeys(final String value) {
			this.value = value;
		}
		public String getValue(){
			return this.value;
		}
	}
	
	
	public String getRequestUrl(){
		return getConfigString(TvePropKeys.REQUEST_URL.getValue());
	}
	
	public String getUser1Username(){
		return getConfigString(TvePropKeys.USER1_USERNAME.getValue());
	}
	
	public String getUser1Password(){
		return getConfigString(TvePropKeys.USER1_PASSWORD.getValue());
	}
	
	public String getUser2Username(){
		return getConfigString(TvePropKeys.USER2_USERNAME.getValue());
	}
	
	public String getUser2Password(){
		return getConfigString(TvePropKeys.USER2_PASSWORD.getValue());
	}
	
	public String getOauthRequestedScopes(){
		return getConfigString(TvePropKeys.OAUTH_REQUESTED_SCOPES.getValue());
	}
	
	public String getOauthRedirectUrl(){
		return getConfigString(TvePropKeys.OAUTH_REDIRECT_URL.getValue());
	}
	
	public String getOauthClientId(){
		return getConfigString(TvePropKeys.OAUTH_CLIENT_ID.getValue());
	}

	public String getOauthClientSecret(){
		return getConfigString(TvePropKeys.OAUTH_CLIENT_SECRET.getValue());
	}
	
	public String getOauthInvalidAccessToken(){
		return getConfigString(TvePropKeys.OAUTH_INVALID_ACCESS_TOKEN.getValue());
	}
	
	public String getOauthExpiredAccessToken(){
		return getConfigString(TvePropKeys.OAUTH_EXPIRED_ACCESS_TOKEN.getValue());
	}
}
