package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;


/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide client id and client secrets.
*
*/
@Service("clientDetailsDP")
public class ClientDetailsProvider extends AbstractDataProvider{

	public enum ClientDetailsPropKeys{
		TVE_CLIENT_ID("test_data.client_id.tve"),
		TVE_CLIENT_SECRET("test_data.client_secret.tve"),
		LOGIN_CLIENT_ID("test_data.client_id.login"),
		LOGIN_CLIENT_SECRET("test_data.client_secret.login"),
		OAUTH_TEST_CLIENT_ID("test_data.client_id.oauth_test"),
		OAUTH_TEST_CLIENT_SECRET("test_data.client_secret.oauth_test"),
		XFINITY_CLIENT_ID("test_data.client_id.xfinity"),
		XFINITY_CLIENT_SECRET("test_data.client_secret.xfinity");
		
		private final String value;
        ClientDetailsPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public enum ClientType{
		TVE,
		LOGIN,
		OAUTH,
		XFINITY
	}
	
	public ClientDetails getClientDetails(ClientType type){
		ClientDetails result = null;
		switch(type){
			case TVE:
				result = new ClientDetails(getConfigString(ClientDetailsPropKeys.TVE_CLIENT_ID.getValue()),
											getConfigString(ClientDetailsPropKeys.TVE_CLIENT_SECRET.getValue()));
				break;
			
			case LOGIN:
				result = new ClientDetails(getConfigString(ClientDetailsPropKeys.LOGIN_CLIENT_ID.getValue()),
											getConfigString(ClientDetailsPropKeys.LOGIN_CLIENT_SECRET.getValue()));
				break;
			
			case OAUTH:
				result = new ClientDetails(getConfigString(ClientDetailsPropKeys.OAUTH_TEST_CLIENT_ID.getValue()),
											getConfigString(ClientDetailsPropKeys.OAUTH_TEST_CLIENT_SECRET.getValue()));
				break;
				
			case XFINITY:
				result = new ClientDetails(getConfigString(ClientDetailsPropKeys.XFINITY_CLIENT_ID.getValue()),
											getConfigString(ClientDetailsPropKeys.XFINITY_CLIENT_SECRET.getValue()));
		}
		
		return result;
	}
	
	public static class ClientDetails{
		private String clientId;
		private String clientSecret;
		
		public ClientDetails(String clientId, String clientSecret){
			this.clientId = clientId;
			this.clientSecret = clientSecret;
		}

		public String getClientId() {
			return clientId;
		}

		public String getClientSecret() {
			return clientSecret;
		}
	}
}
