package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;

/**
*
* @author Abhijit Rej
* @since April 2016
*
* This is data provider to provide scope values both for IDM and Login.
*
*/
@Service("scopeDP")
public class ScopeValueProvider extends AbstractDataProvider{

	public enum LoginScopePropKeys{
		MCDV("test_data.login_scope.mcdv"),
		MOBILE_CDV("test_data.login_scope.mobile_cdv"),
		PLAXO("test_data.login_scope.plaxo"),
		SMARTZONE("test_data.login_scope.smartzone"),
		PLAXOSHORTSESSION("test_data.login_scope.plaxo_short_session"),
		MOXIE("test_data.login_scope.moxie"),
		XSS("test_data.login_scope.xss"),
		CEMP("test_data.login_scope.cemp"),
		CODEBIG("test_data.login_scope.codebig"),
		XHS("test_data.login_scope.xhs"),
		ASTRO("test_data.login_scope.astro");
		
		private final String value;
        LoginScopePropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public enum IdmScopePropKeys{
		SECRET_QUESTIONS("test_data.idm_scope.secret_question"),
		ALTERNATIVE_EMAIL_AVAILBILITY("test_data.idm_scope.email_availability"),
		UID_AVAILBILITY("test_data.idm_scope.uid_availability"),
		MOBILE_PHONE_NUMBER_AVAILBILITY("test_data.idm_scope.mobile_availability"),
		RESET_CODE_CREATE("test_data.idm_scope.reset_code_create"),
		RESET_CODE_READ("test_data.idm_scope.reset_code_read"),
		RESET_CODE_DELETE("test_data.idm_scope.reset_code_delete"),
		SEND_VERIFICATION_URL("test_data.idm_scope.send_verification_email"),
		VALIDATE_VERIFICATION_URL("test_data.idm_scope.validate_verification_email"),
		REGISTER_USER("test_data.idm_scope.register_user"),
		ACCOUNT_BASIC_PROFILE("test_data.idm_scope.account_basic_profile");
		
		private final String value;
        IdmScopePropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public enum OtherScopePropKeys{
		MY_ACCOUNT("test_data.other_scope.myaccount"),
		TVE("test_data.other_scope.tve");
		
		private final String value;
        OtherScopePropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public String getLoginScopeValue(String key){
		String cacheKey = null;
		
		if(key!=null){
			try{
				cacheKey = LoginScopePropKeys.valueOf(key.toUpperCase()).getValue();
			}catch(IllegalArgumentException iae){}
		}
		
		return cacheKey!=null ? getConfigString(cacheKey) : null;
	}
	
	
	public String getIdmScopeValue(String key){
		String cacheKey = null;
		
		if(key!=null){
			try{
				cacheKey = IdmScopePropKeys.valueOf(key.toUpperCase()).getValue();
			}catch(IllegalArgumentException iae){}
		}
		
		return cacheKey!=null ? getConfigString(cacheKey) : null;
	}
}
