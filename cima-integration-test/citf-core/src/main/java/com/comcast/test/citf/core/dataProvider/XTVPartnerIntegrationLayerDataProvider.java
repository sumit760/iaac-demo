package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide XTV PIL required data.
*
*/
@Service("xtvPilDP")
public class XTVPartnerIntegrationLayerDataProvider extends AbstractDataProvider{

	public enum XfinityTVPartnerIntegrationLayerPropKeys {
		XTV_PIL_AUTHN_SELECT_ACCOUNT("test_data.xtv_pil_authn_select_account"),
		XTV_PIL_AUTHN_LOGOUT("test_data.xtv_pil_authn_logout"),
		XTV_PIL_UNIVERSITY_ACCOUNT1_USERNAME("test_data.xtv_pil.university.account1.username"),
		XTV_PIL_UNIVERSITY_ACCOUNT1_PASSWORD("test_data.xtv_pil.university.account1.password");
		
		private final String value;
		XfinityTVPartnerIntegrationLayerPropKeys(final String value) {
			this.value = value;
		}
		public String getValue() {
			return this.value;
		}
	}
	
	public String getAuthSelectAccount(){
		return getConfigString(XfinityTVPartnerIntegrationLayerPropKeys.XTV_PIL_AUTHN_SELECT_ACCOUNT.getValue());
	}
	
	public String getAuthLogOut(){
		return getConfigString(XfinityTVPartnerIntegrationLayerPropKeys.XTV_PIL_AUTHN_LOGOUT.getValue());
	}
	
	public String getUniversityAccount1Username(){
		return getConfigString(XfinityTVPartnerIntegrationLayerPropKeys.XTV_PIL_UNIVERSITY_ACCOUNT1_USERNAME.getValue());
	}
	
	public String getUniversityAccount1Password(){
		return getConfigString(XfinityTVPartnerIntegrationLayerPropKeys.XTV_PIL_UNIVERSITY_ACCOUNT1_PASSWORD.getValue());
	}
}
