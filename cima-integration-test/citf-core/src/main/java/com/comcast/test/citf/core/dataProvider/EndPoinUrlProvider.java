package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide end point URLs.
*
*/
@Service("endPointUrlDP")
public class EndPoinUrlProvider extends AbstractDataProvider{
	
	public enum LoginUrlPropKeys{
		XFINITY_SIGNIN("test_data.login_url.xfinity_signin"),
		XFINITY_HOME("test_data.login_url.xfinity_home"),
		EMAIL("test_data.login_url.email"),
		VOICE_AND_TEXT("test_data.login_url.voice_and_text"),
		HOME_SECURITY("test_data.login_url.home_security"),
		DVR_MANAGER("test_data.login_url.dvr_manager"),
		XFINITY_ON_DEMAND("test_data.login_url.xfinity_on_demand"),
		TV_LISTINGS("test_data.login_url.tv_listings"),
		CONSTANT_GUARD("test_data.login_url.constant_guard"),
		HELP_AND_SUPPORT("test_data.login_url.help_and_support"),
		SHOP_OR_UPGRADE("test_data.login_url.shop_or_upgrade"),
		MY_ACCOUNT("test_data.login_url.my_account"),
		TV("test_data.login_url.tv"),
		PRIVACY_POLICY("test_data.login_url.privacy_policy"),
		XFINITY_INSIDER("test_data.login_url.xfinity_insider"),
		XFINITY_LITHIUM("test_data.login_url.xfinity_lithium"),
		XFINITY_STORE("test_data.login_url.xfinity_store"),
		XFINITY_SYMANTEC("test_data.login_url.xfinity_symantec"),
		XFINITY_PUBLISH("test_data.login_url.xfinity_publish"),
		XFINITY_REFER_A_FRIEND("test_data.login_url.xfinity_refer_a_friend"),
		XFINITY_TERMS_OF_SERVICE("test_data.login_url.xfinity_terms_of_service"),
		XFINITY_LOGIN_STATUS("test_data.login_url.xfinity_login_status"),
		XFINITY_CREATEUSERNAME_URL("test_Data.idm_url_createusername");
		
		private final String value;
        LoginUrlPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}	
	
	public enum IDMUrlPropKeys{		
		SECRET_QUESTIONS("test_data.idm_url.secret_questions"),
        ALTERNATIVE_EMAIL_AVAILBILITY("test_data.idm_url.alternative_email_availability"),
        UID_AVAILBILITY("test_data.idm_url.uid_availability"),
        MOBILE_PHONE_NUMBER_AVAILBILITY("test_data.idm_url.mobile_phone_number_availability"),
        RESET_CODE("test_data.idm_url.reset_code"),
        SEND_VERIFICATION_URL("test_data.idm_url.send_verification_url"),
        VALIDATE_VERIFICATION_URL("test_data.idm_url.validate_verification_url"),
        REGISTER_USER("test_data.idm_url.register_user"),
        RESET_CODE_UI("test_data.idm_url.reset_code_ui"),
        TOKEN_GENERATOR("test_data.idm_url.token_generator"),
        SUSI_IP("test_data.idm_url.susi_ip"),
        IDMBASEURL("test_data.idm_url.api_base_url"),
		IDMBASEURL_V2("test_data.idm_url.api_base_url_v2"),
		FB_XFINITY_URL("test_data.idm_url.fb_xfinity_url"),
		SIGN_IN_XFINITY_RABI_URL("test_data.idm_url.xfinity_sign_in_rabi");
		
		private final String value;
        IDMUrlPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
		
	public enum ServiceEndpointPropKeys{
		ACCESS("test_data.service_endpoint.access"),
		LOGIN("test_data.service_endpoint.login");
		
		private final String value;
        ServiceEndpointPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public enum OtherUrlPropKeys{
		PARENTAL_CONTROL_SERVICE_URL("test_data.parental_control_service_url"),
		SAML_RESPONSE_URL("test_data.saml_response_url"),
		SAML_RESPONSE_REDIRECT_URL("test_data.saml_response_redirect_url"),
		TVE_DESTINATION_POX_URL("test_data.tve_destination_pox_url"),
		TVE_DESTINATION_SOAP_URL("test_data.tve_destination_soap_url");
		
		private final String value;
		OtherUrlPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	

	
	public String getLoginUrl(String key){
		String cacheKey = null;
		
		if(key!=null){
			try{
				cacheKey = LoginUrlPropKeys.valueOf(key.toUpperCase()).getValue();
			}catch(IllegalArgumentException iae){}
		}
		
		return cacheKey!=null ? getConfigString(cacheKey) : null;
	}
	
	public String getIdmUrl(String key){
		String cacheKey = null;
		
		if(key!=null){
			try{
				cacheKey = IDMUrlPropKeys.valueOf(key.toUpperCase()).getValue();
			}catch(IllegalArgumentException iae){}
		}
		
		return cacheKey!=null ? getConfigString(cacheKey) : null;
	}
		
	public String getServiceEndPointUrl(String key){
		String cacheKey = null;
		
		if(key!=null){
			try{
				cacheKey = ServiceEndpointPropKeys.valueOf(key.toUpperCase()).getValue();
			}catch(IllegalArgumentException iae){}
		}
		
		return cacheKey!=null ? getConfigString(cacheKey) : null;
	}
	
	public String getParentalControlServiceUrl(){
		return getConfigString(OtherUrlPropKeys.PARENTAL_CONTROL_SERVICE_URL.getValue());
	}
	
	public String getSamlResponseUrl(){
		return getConfigString(OtherUrlPropKeys.SAML_RESPONSE_URL.getValue());
	}
	
	public String getSamlResponseRedirectUrl(){
		return getConfigString(OtherUrlPropKeys.SAML_RESPONSE_REDIRECT_URL.getValue());
	}
	
	public String getTveDestinationPoxUrl(){
		return getConfigString(OtherUrlPropKeys.TVE_DESTINATION_POX_URL.getValue());
	}
	
	public String getTveDestinationSoapUrl(){
		return getConfigString(OtherUrlPropKeys.TVE_DESTINATION_SOAP_URL.getValue());
	}
}
