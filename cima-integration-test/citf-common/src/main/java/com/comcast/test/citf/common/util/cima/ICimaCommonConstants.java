package com.comcast.test.citf.common.util.cima;

import com.comcast.test.citf.common.util.ICommonConstants;

public interface ICimaCommonConstants extends ICommonConstants{

	String OAUTH_REQUEST_ID_REGEX = "reqId=[0-9a-z\\-]+";
	String OAUTH_AUTH_CODE_PREFIX = "?code=";

	//String SAML_RESPONSE_REDIRECT_URL = "http://localhost:8080/citf-ui/noReturn/samlResponse/";

	String EXTENSION_JAVA_KEY_STORE_JKS = "jks";

	String JAVA_KEY_STORE_CITF_COMMON = "classpath:citf-keystore.jks";
	String JAVA_KEY_STORE_TVE = "classpath:tve-keystore.jks";
	String FILE_PATH_CIMA_SERVER_ACCESS_PRIVATE_KEY = "citf-core/src/main/resources/CIMA_Server_Private_Key.ppk";

	//Feature and Step definition files pacakge names --------------------------- Start
	String CUCUMBER_FEATURE_PACKAGE_API_PATH = "src/main/java/com/comcast/cima/test/api/cucumber/feature";
	String CUCUMBER_FEATURE_PACKAGE_API_OLD_PATH = "src/com/comcast/cima/test/api/cucumber/feature";
	String CUCUMBER_STEP_DEFINATION_PACKAGE_API = "com/comcast/cima/test/api/cucumber/steps";

	String CUCUMBER_FEATURE_PACKAGE_UI_PATH = "src/main/java/com/comcast/cima/test/ui/cucumber/feature";
	String CUCUMBER_FEATURE_PACKAGE_UI_OLD_PATH = "src/com/comcast/cima/test/ui/cucumber/feature";
	String CUCUMBER_STEP_DEFINATION_PACKAGE_UI = "com/comcast/cima/test/ui/cucumber/steps";

	String CUCUMBER_FEATURE_PACKAGE_COMMON_PATH = "src/main/java/com/comcast/cima/test/common/cucumber/feature";
	String CUCUMBER_STEP_DEFINATION_PACKAGE_COMMON = "com/comcast/test/citf/core/cucumber/steps";
	//Feature and Step definition files pacakge names --------------------------- End

	
	//Properties files to keep configuration data and test data --------------------------- Start
	String PROPERTY_FILE_CITF_CORE_CONFIGURATION = "citf-core-config.properties";
	
	String PROPERTY_FILE_CITF_GENERAL_QA_CONFIGURATION = "citf-config-QA.properties";
	String PROPERTY_FILE_CITF_COMCAST_QA_CONFIGURATION = "citf-config-QA-COMCAST.properties";
	String PROPERTY_FILE_CITF_XFINITY_QA_CONFIGURATION = "citf-config-QA-XFINITY.properties";
	
	String PROPERTY_FILE_CITF_GENERAL_STAGE_CONFIGURATION = "citf-config-STAGE.properties";
	String PROPERTY_FILE_CITF_COMCAST_STAGE_CONFIGURATION = "citf-config-STAGE-COMCAST.properties";
	String PROPERTY_FILE_CITF_XFINITY_STAGE_CONFIGURATION = "citf-config-STAGE-XFINITY.properties";
	
	String PROPERTY_FILE_CITF_GENERAL_PROD_CONFIGURATION = "citf-config-PROD.properties";
	String PROPERTY_FILE_CITF_COMCAST_PROD_CONFIGURATION = "citf-config-PROD-COMCAST.properties";
	String PROPERTY_FILE_CIT_XFINITYF_PROD_CONFIGURATION = "citf-config-PROD-XFINITY.properties";
	//Properties files to keep configuration data and test data --------------------------- End
	
	//CITF Run modes -- Start
		String CITF_RUN_MODE_LOCAL = "LOCAL";
		String CITF_RUN_MODE_REMOTE = "REMOTE";
	//CITF Run modes -- End

	//Database table users user_role values -- START
		String DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY = "P";
		String DB_TAB_USERS_COLUMN_USER_ROLE_SECONDARY = "S";
	//Database table users user_role values -- END

	//LDAP connection credentials -- start
		String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
		String LDAP_HOSTNAME = "esdvip-east.ula.comcast.net";
		String LDAP_PORT = "389";
		String LDAP_ORG = "comcast";
		String LDAP_SECURITY_AUTHENTICATION = "simple";
		String LDAP_SECURITY_PRINCIPAL = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		String LDAP_SECURITY_CREDENTIALS = "GDSpass3245";
	//LDAP connection credentials -- end

	//Validation Map keys -- start
		String VALIDATION_KEY_FIRST_NAME = "VK_FIRST_NAME";
		String VALIDATION_KEY_LAST_NAME = "VK_LAST_NAME";
		String VALIDATION_KEY_EMAIL = "VK_EMAIL";
		String VALIDATION_KEY_CUSTOMER_ID = "VK_CUSTOMER_ID";

	//Validation Map keys -- end

	//String cache common keys -- start
		String STRING_CACHE_KEY_TEST_ENVIRONMENT = "SCK_TEST_ENV";
		String STRING_CACHE_KEY_TEST_CATEGORY = "SCK_TEST_CAT";
		String STRING_CACHE_KEY_UI_TEST_EXEC_ENV = "SCK_TEST_UI_EXEC_ENV";
		String STRING_CACHE_KEY_TEST_DOMAIN = "SCK_TEST_DOMAIN";
	//String cache common keys -- end

	//User Category -- start
		String USER_CATEGORY_IDM = "IDM";
		String USER_CATEGORY_LOG_IN = "LOGIN";
		String USER_CATEGORY_TVE = "TVE";

		String USER_CATEGORY_ALL = "ALL";
	//User Category -- end

	//LOBs
		enum LOB{
			VID,
			CDV,
			HSD
		}		

	//Account Type
        enum AccountType {
            RESIDENTIAL,
            BUSINESS
            
        }

	// XML Signatire namespace -- start
		String TVE_XML_SIG_NAMESPACE = "ds";
	// XML Signatire namespace -- end

	//Temp Parameter table keys -- start
		String DB_TAB_TEMP_PARAMETERS_KEY_OAUTH_CODE_PREFIX = "OAUTH_CODE_";
		String DB_TAB_TEMP_PARAMETERS_KEY_SAML_RESPONSE = "SAML_RESPONSE";

	//Temp Parameter table keys -- end

	String ID_PREFIX_XACML_AUTH_DECESION_QUERY = "XID";

	//Service Map keys used in UesrCache/UserServiceDAO
	enum UserServiceMapKeys{
		USER_ID,
		SERVICE_NAME,
		ENTITLEMENTS,
		AUTHORIZATION_STATUS,
		AUTHENTICATION_STATUS
	}

	String STATUS_USER_SERVICES_SUCCESS = "Success";

	enum AssetRatingTypes{
		TV_RATING,
		MOVIE_RATING
	}

	enum UserRoles{
		PRIMARY(ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_PRIMARY),
		SECONDARY(ICimaCommonConstants.DB_TAB_USERS_COLUMN_USER_ROLE_SECONDARY);

		private final String value;
        UserRoles(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}
	
	String COMCAST_EMAIL_DOMAIN = "@comcast.net";

	//Server Types -- Start
		String SERVER_TYPE_CIMA_LOG = "CIMA_LOG";
		String SERVER_TYPE_SPLUNK = "SPLUNK";
	//Server Types -- End

	//Browser Capabilities -- Start
		String SAUCELABS_EXECUTION_PLATFORM = "PLATFORM";
		String SAUCELABS_EXECUTION_DEVICE_TYPE = "TYPE";
		String SAUCELABS_EXECUTION_BROWSER = "BROWSER";
	//Browser Capabilities -- end

	String LOG_FILE_NAME_CIMA_LOGIN = "cima-login";
	String LOG_FILE_NAME_CIMA_IDM = "cima-idm";
	String LOG_FILE_NAME_CIMA_TVE = "cima-tve";

	//Create User parameters -- Start
		String USER_SECURITY_QUESTION_DEFAULT = "What is your favorite beverage?";
		String USER_SECURITY_QUESTION_ANSWER_DEFAULT = "coke";
		String IDM_USER_CREATE_STATUS = "USER_CREATE_STATUS";
	//Create User parameters -- end

	//Splunk parameter Domain Values
		String SPLUNK_PARAM_NAME_DOMAIN = "cima_domain";
		String SPLUNK_PARAM_VALUE_DOMAIN_COMCAST = "comcast.net";
		String SPLUNK_PARAM_VALUE_DOMAIN_XFINITY = "xfinity.net";

	//IDM Page Attributes...
		String PAGE_ATTRIBUTE_ADDRESS = "ADDRESS";
		String PAGE_ATTRIBUTE_PHONE = "PHONE";
		
	String ESD_FLAG_CST_LOGIN_STATUS = "ESD_FLAG_CST_LOGIN_STATUS";
	String TEST_CASE_USERNAME = "TEST_CASE_USERNAME";
		
	//State idenfiers for cucumber steps -- START
		String STATE_IDENTIFIER_OAUTH = "OAUTH";
		String STATE_IDENTIFIER_IDM = "IDM";
	//State idenfiers for cucumber steps -- END

	//IDM Secret Questions & Answers
		String SECOND_SECRET_ANSWER = "BMW";
		String SECOND_SECRET_QUESTION = "What was your first car (make and model)?";
				
	//	SuSi Test cases
		String SCENARIO_NAME = "SCENARIO_NAME";
		String BROWSER = "BROWSER";
		String SESSIONID = "SESSIONID";
		String ACCOUNT = "ACCOUNT";
		String USERATTR = "USERATTR";
		String PAGEFLOWID = "PAGEFLOWID";
		String SUSIIPADDRESSPAGE = "SUSIIPADDRESSPAGE";
		String ENVIRONMENT = "ENVIRONMENT";
		String NEXTPAGE = "NEXTPAGE";
		String DELETE_TOKEN_GEN_URL = "http://satuser:password123@tvxwmg-c1-a00006-g.ch.tvx.comcast.com:5555/getSatToken?serviceAccountId=";
		String DELETE_CONFIG_URL = "https://siorc.xfinity.com/";
		String SERVICE_ID = "5906883917096467165";
		String WIFI_ID = "jimmy1234";
		String WIFI_PASSWD = "Susisusi";
		
	// Invalid access token
		final String EXPIRED_ACCESS_TOKEN = "CgNPQVQQARgCIoABrBYITUz-XlGeANa8YLDZo1NgJ-jviuDY"
				+ "C5WcLo9gT00SKSmEvN9Ruj4Vue5SO0pCj7QPd_31CwrSMXg5n0XwEUP9mJ51CFRBZmbIQ48jlY"
				+ "AU2rrw4KwA4_1qib6OWS3sYACD_yEp8tB99M0wnL22VwiLAZe7B6p5_gxdFf0ofx8qEIeyrX7t"
				+ "IliN4FVkilEZgzQyUQoodXJuOmNpbWE6b2F1dGg6djI6YWNjZXNzLXRva2VuOmVuY3J5cHQ6MRI"
				+ "ldXJuOmNpbWE6b2F1dGg6djI6YWNjZXNzLXRva2VuOnNpZ246MTqQAlpPnpONTMKd9ra7iz3Jb3"
				+ "7MqXrCjDep9Z6IdP1Fyx2LIpkp1nwQFL1dJFDNRd6zQP2TfCwLqXQrsLSjfq-8rL6IleS0QP8Zz-"
				+ "gHD1QTMVEm4GCi8rE0avnuwjYW2rE1uxkGiUzQdpsYojOEwOs3hzab5mnMEf8o4Eit5hNGugMku"
				+ "2LBSvvMaRTPaZBA35kLQOubE8TT0F_1AHnD07FyqKEOK_xmOqZAm31hmNyXG2Cn7WLG_fegMgcS"
				+ "6J86gqUs9wqHkN_IcM1D99I5dqFDDUlwxTShUlxw7PA_nV_xH9nLABo6VR0yLJVnpyEq5W4_DAC"
				+ "uWqT-Aj6dSXkWkqYlWSV3E7Ewkc_kWKqf24kmhshi";

	String SUSI_UPDATING_WIFI_TIME = "50";
		
	// User API
		String UNREGISTERED_USERID = "citfuser123123213123";
		String UNREGISTERED_ALTEREMAIL = "citf@newEmail.com";
		String UNREGISTERED_INVALID_ALTEREMAIL = "abc";
		String UNREGISTERED_MOBILE = "9888998988";
		String UNREGISTERED_INVALID_MOBILE = "988899898981989";
		String RESET_CODE_NOT_FOUND="Reset_Code_Not_Found";
		String SUCCESS_MESSAGE="Message submitted";
		String INVALID_INPUT_MESSAGE="Invalid input received";
	
	// Invalid device activation attributes
		String INVALID_CLIENT_ID = "invalidclientid";
		String INVALID_USER_CODE = "invalidusercode";
		String INVALID_DEVICE_CODE = "invaliddevicecode";
		String INVALID_REFRESH_TOKEN = "0eZ2nBl-q79sN4cpUb5xlnOYKhep*";
		String INVALID_GRANT_TYPE = "invalidgranttype";
		String INVALID_CLIENT_SECRET = "invalidclientsecret";
		String INVALID_SCOPE = "https://login123.comcast.net/api/login";
		String INVALID_REDIRECT_URL = "https://localhost:9000/";
		String INVALID_RESPONSE_TYPE = "invalidresponsetype";
		String INVALID_AUTH_CODE = "0.ac.tPku8h";
				
	// XML namespace
		String LOGIN_TOKEN_XML_NAMESPACE = "xmlns=\"urn:comcast:login:api:v1.0\"";
				
	// Invalid REST Api attributes
		String INVALID_APP_KEY = "invalidappkey";
		String INVALID_LOGIN_TOKEN = "cxddDPkmm12xbiBdc";
		String INVALID_USERID = "invaliduserId";
		String INVALID_USER_PASSWORD = "invaliduserpassword";
		String INVALID_DIGITAL_SIGNATURE = "aXPqJGiN0Bp6GUZkx9gJZcrTd6s%3D";
}