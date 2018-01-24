package com.comcast.test.citf.common.dataProvider.cima;

import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 * This interface will be implemented by all Data Provider abstract classes.
 *
 * The enumeration contains keys, which will be used to call data provider from test classes. The data provider class returns same key [String/List<Map>] (where 
 * returning single element per key, e.g. Scopes.REST_API_LOGIN or for RestAPIGeneralKeys.USERID_PASSWOORD the return key will be RestAPIGeneralKeys.USERID_PASSWOORD 
 * which contains List of Maps or a single Map contains KEY_USER_ID and KEY_PASSWORD) or different keys (where returning multiple elements per single key, 
 * e.g. RestAPIGeneralKeys.CLIENT_ID_CLIENT_SECRET returns KEY_CLIENT_ID and KEY_CLIENT_SECRET)  
 * 
 * @deprecated will be removed while implementing data providers
 */
@Deprecated
public interface IDataProviderEnums {
	
	//************************** Property file keys -- Start *********************************************
	
	enum ConfigurationPropKeys{
		CITF_RUN_MODE("config_data.run_mode"),
		MAX_CITF_EXECUTION_TIME("config_data.max_execution_time_in_millis"),
		NUMBER_OF_MAX_PARALLEL_THREADS("config_data.max_parallel_thread_count"),
		LOG_READING_MECHANISM("config_data.log_read_mechanism"),
		SAUCELAB_USER_NAME("config_data.saucelabs.user_name"),
		SAUCELAB_ACCESS_KEY("config_data.saucelabs.access_key"),
		SAUCELAB_ON_DEMAND_URL_WITH_SAUCECONNECT("config_data.saucelabs.on_demand_url_with_sauce_connect"),
		SAUCELAB_ON_DEMAND_URL_WITHOUT_SAUCECONNECT("config_data.saucelabs.on_demand_url_without_sauce_connect"),
		CITF_KEYSTORE_ALIAS("config_data.keystore_citf.alias"),
		CITF_KEYSTORE_ENTRY_PASSWORD("config_data.keystore_citf.entry_password"),
		CITF_KEYSTORE_KEYSTORE_PASSWORD("config_data.keystore_citf.keystore_password"),
		TVE_KEYSTORE_ALIAS("config_data.keystore_tve.alias"),
		TVE_KEYSTORE_ENTRY_PASSWORD("config_data.keystore_tve.entry_password"),
		TVE_KEYSTORE_KEYSTORE_PASSWORD("config_data.keystore_tve.keystore_password"),
		MAX_WAITING_TIME_TO_ACQUIRE_LOCK("config_data.max_waiting_time_to_acquire_lock_in_millis"),
		IDM_SERVER_TIMEZONE("config_data.idm_server_timezone"),
		RESET_CODE_EXPIRATION_TIME("test_data.reset_code_expiration_time");
		
		private final String value;
        ConfigurationPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	enum LoginScopePropKeys{
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
	
	enum IdmScopePropKeys{
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
	
	enum OtherScopePropKeys{
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
	
	enum TvePropKeys {
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
	
	enum ClientDetailsPropKeys{
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
	
	enum ServiceEndpointPropKeys{
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
	
	
	enum OAuthUrlPropKeys{
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
	
	
	enum XfinityTVPartnerIntegrationLayerPropKeys {
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
	
	
	enum LoginUrlPropKeys{
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
	
	
	enum IDMUrlPropKeys{		
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
		FB_XFINITY_URL("test_data.idm_url.fb_xfinity_url");
		
		private final String value;
        IDMUrlPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	enum OtherPropKeys{
		APP_KEY("test_data.app_key"),
		SIGN_KEY("test_data.sign_key"),
		PARENTAL_CONTROL_SERVICE_URL("test_data.parental_control_service_url"),
		SAML_RESPONSE_URL("test_data.saml_response_url"),
		SAML_RESPONSE_REDIRECT_URL("test_data.saml_response_redirect_url"),
		TVE_DESTINATION_POX_URL("test_data.tve_destination_pox_url"),
		TVE_DESTINATION_SOAP_URL("test_data.tve_destination_soap_url");
		
		private final String value;
        OtherPropKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	//************************** Property file keys -- End *********************************************
	
	/**
	 * @deprecated Will remove this enum while implementing new data providers.
	 */
	@Deprecated
	enum RestAPIGeneralKeys{
		SCOPE("SCOPE"),
		USERID_PASSWOORD_LOGIN("USER_PASS_LOGIN"),
		CLIENT_ID_CLIENT_SECRET_LOGIN("CLIENT_ID_CLIENT_SECRET_LOGIN"),
		CLIENT_ID_CLIENT_SECRET_XFINITY("CLIENT_ID_CLIENT_SECRET_XFINITY"),
		CLIENT_ID_CLIENT_SECRET_TVE("CLIENT_ID_CLIENT_SECRET_TVE"),
		APP_KEY_SIGN_KEY("APP_KEY_SIGN_KEY"),
		SERVICE_ACCESS_END_POINT(ServiceEndpointPropKeys.ACCESS.getValue()),
		SERVICE_LOGIN_END_POINT(ServiceEndpointPropKeys.LOGIN.getValue()),
		KEY_SYSTEM_IP("KEY_SYSTEM_IP"),
		KEY_PIN("KEY_PIN");
		
		private final String value;
        RestAPIGeneralKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * @deprecated Will remove this enum while implementing new data providers.
	 */
	@Deprecated
	enum IDMGeneralKeys{
		SCOPE("SCOPE"),
		USER_DETAILS("USR_ACC_USRATTR"),
		CLIENT_ID_CLIENT_SECRET("CLIENT_ID_CLIENT_SECRET_LOGIN"),
		FRESH_ACCOUNT("FRESH_ACCOUNT"),
		FRESH_USER("FRESH_USER");
		
		private final String value;
        IDMGeneralKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	
	enum FilterPrefix{
		GENERAL("CFC_ALL."),
		USER("CFC_USER."),
		USER_ATTR("CFC_USERATTR."),
		ACCOUNT("CFC_ACCOUNT."),
		SERVICE("CFC_SERVICE.");
		
		private final String value;
        FilterPrefix(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	enum LockableResource{
		USER,
		ACCOUNT,
		FRESH_USER
	}
	
	//*********************************************** Filter Keys ***************************************************
	
	
	/**
	 * This enum is only containing those keys which will be needed as key for Login
	 * 
	 * @deprecated Will remove this enum while implementing new data providers.
	 */
	@Deprecated
	enum LoginFilterKeys{
		LOBS_CSV(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOBS_CSV),
		LOGIN_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS),
		USER_IDS_CSV(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV);
		
		private final String value;
        LoginFilterKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	
	/**
	 * This enum is only containing those keys which will be needed as key for IDM
	 * 
	 * @deprecated Will remove this enum while implementing new data providers.
	 */
	@Deprecated
	enum IdmFilterKeys{
		EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_EMAIL),
		ALTERNATE_MAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL),
		PHONE(ICimaCommonConstants.CACHE_FLTR_CONDTN_PHONE),
		SECRET_QUESTION(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION),
		FACE_BOOK_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID),
		SSN(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_SSN),
		DOB(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB),
		LOGIN_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_LOGIN_STATUS),
		ACCOUNT_STATUS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_STATUS),
		USER_IDS_CSV(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_IDS_CSV),
		PRIMARY_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_PRIMARY_ACCOUNT_ID),
		SECONDARY_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_SECONDARY_ACCOUNT_ID),
		USER_ROLE(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ROLE),
        ADDRESS(ICimaCommonConstants.CACHE_FLTR_CONDTN_ADDRESS),
        ACCOUNT_TYPE(ICimaCommonConstants.CACHE_FLTR_CONDTN_ACCOUNT_TYPE),
        FRESH_ACCOUNT_SSN(ICimaCommonConstants.CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN),
        FACE_BOOK_ID_SAME_AS_ALTER_EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL),
        FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL),
		HAS_MULTIPLE_PRIMARY_ACCOUNTS(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS),
		HAS_MULTIPLE_SECONDARY_ACCOUNTS(ICimaCommonConstants.CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS),
		SERVICE_ACCOUNT_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID);
		
		private final String value;
        IdmFilterKeys(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	
	//**************************** Other Keys *******************************************
	
	String SCOPE_PREFIX = "SCOPE_PREFIX_";
	String SERVICE_NAME_PREFIX = "SN_PREFIX_";
	String IDM_SCOPE_PREFIX = "SCOPE_PREFIX_REST_API_IDM_";
	
	//Used to fetch User Id for Login
	String KEY_USER_ID = "IL_USER_ID";
	
	//Used to fetch Password for Login
	String KEY_PASSWORD = "IL_PASSWORD";
	
	//Used to fetch User/User attributes and Account details for IDM
	String KEY_USER = "IK_USER";
	String KEY_ACCOUNT = "IK_ACCOUNT";
	
	//Used to fetch CLIENT_ID for CLIENT_ID_CLIENT_SECRET
	String KEY_CLIENT_ID = "KEY_CLIENT_ID";
	
	//Used to fetch CLIENT_SECRET for CLIENT_ID_CLIENT_SECRET
	String KEY_CLIENT_SECRET = "KEY_CLIENT_SECRET";
	
	//Used to fetch APP_KEY for APP_KEY_SIGN_KEY
	String KEY_APP_KEY = "APP_KEY";
	
	//Used to fetch SIGN_KEY for APP_KEY_SIGN_KEY
	String KEY_SIGN_KEY = "SIGN_KEY";
}