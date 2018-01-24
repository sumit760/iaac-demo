package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide key data.
*
*/
@Service("keysDP")
public class KeysDataProvider extends AbstractDataProvider{
	
	public enum Keys{
		APP_KEY("test_data.app_key"),
		SIGN_KEY("test_data.sign_key");
		
		private final String value;
		Keys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	

	public String getAppKey(){
		return getConfigString(Keys.APP_KEY.getValue());
	}
	
	public String getSignKey(){
		return getConfigString(Keys.SIGN_KEY.getValue());
	}
	
}
