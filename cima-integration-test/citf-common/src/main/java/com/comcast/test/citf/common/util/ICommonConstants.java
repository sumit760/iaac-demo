package com.comcast.test.citf.common.util;


public interface ICommonConstants {
	
	int QUANTITY_UNLIMITED = -1;

	String BLANK_STRING = "";
	String BLANK_SPACE_STRING = " ";
	String COLON = ":";
	String PIPE = "|";
	String EQUALS_SIGN = "=";
	String AMPERSAND_SIGN = "&";
	String LESSER_THAN = "<";
	String XML_TAG_CLOSE = "</";
	String GREATER_THAN = ">";
	String COMMA_SPACE = ", ";
	String AT_THE_RATE = "@";
	String HASH_SIGN = "#";
	String DOLLAR_SIGN = "$";
	String COMMA = ",";
	String DOT = ".";
	String SLASH = "/";
	String BACKSLASH = "\\";
	String START_CURLY_BRACE = "{";
	String END_CURLY_BRACE = "}";
	String START_ROUND_BRACE = "(";
	String END_ROUND_BRACE = ")";
	String SINGLE_DOUBLE_QUOTE = "\"";
	String NEW_LINE = "\n";
	String START_SQUARE_BRACKET = "[";
	String END_SQUARE_BRACKET = "]";
	String UNDER_SCORE = "_";
	String DASH = "-";
	String NOT_SIGN = "!";
	String ASTERIX = "*";
	
	String BOOLEAN_VALUE_TRUE = "TRUE";
	String BOOLEAN_VALUE_FALSE = "FALSE";
	
	String URL_HTTP_PREFIX = "http://";
	
	String SPLUNK_SCHEME_HTTP = "http";
	String SPLUNK_SCHEME_HTTPS = "https";
	
	String GET_METHOD = "GET";
	String POST_METHOD = "POST";
	String HTTP_1P1 = "HTTP/1.1";
	String ENCODING_UTF8 = "UTF-8";
	
	String REGEX_BLANK_SPACE ="\\s";
	String REGEX_LESS_THAN_TAG ="\\>";
	
	String DB_STATUS_ACTIVE = "ACTIVE";
	String DB_STATUS_DELETED = "DELETED";
	
	String OPERATION_STATUS_SUCCESS = "Success";
	String OPERATION_STATUS_ERROR = "ERROR";
	
	String EXTN_JAVA = ".java";
	String EXTN_CLASS = ".class";
	
	String SYSTEM_PROPERTY_USER_DIRECTORY = "user.dir";
	String SYSTEM_PROPERTY_TEMP_DIRECTORY = "java.io.tmpdir";
	String SYSTEM_PROPERTY_OS_NAME = "os.name";
	
	String USER_GENERATED_SYSTEM_PROPERTY_LOG_PATH = "ugsp.log";
	String USER_GENERATED_SYSTEM_PROPERTY_TEMP_FILE_PATH_CITF = "ugsp.tmpdir.citf";
	
	String DIRECTORY_NAME_TARGET = "/target/";
	
	//Cucumber scope used by spring factory
	String CUCUMBER_SCOPE_NAME = "cucumber-thread";
	
	//List of environments -- Start
		String ENVIRONMENT_ALL = "ALL";
		String ENVIRONMENT_QA = "QA";
		String ENVIRONMENT_DEV = "DEV";
		String ENVIRONMENT_STAGE = "STAGE";
		String ENVIRONMENT_PERF = "PERF";
		String ENVIRONMENT_PROD = "PROD";
	//List of environments -- End
		
	//List of Test Types -- Start	
		String TEST_TYPE_API = "API";
		String TEST_TYPE_UI = "UI";
		String TEST_TYPE_ALL = "ALL";	
	//List of Test Types -- End	
		
	//List of Test Category -- Start			(NOTE: This values should the same as in the tags of feature files )
		String TEST_CATEGORY_SMOKE = "Smoke";
		String TEST_CATEGORY_INTEGRATION = "Integration";
		String TEST_CATEGORY_WIP = "WIP";
		String TEST_CATEGORY_TEST = "Test";
		
	//List of Test Category -- Start	
		
	//To avoid parallel execution of any test scenario, the scenario must need to have the TEST_EXECUTION_TYPE_SEQUENTIAL annotation
	String TEST_EXECUTION_TYPE_SEQUENTIAL = "@Sequential";
		
	//List of UI Execution Environment -- Start			
		String ENVIRONMENT_UI_LOCAL = "LOCAL";
		String ENVIRONMENT_UI_REMOTE = "REMOTE";
	//List of UI Execution Environment -- Start
		
	//URI Domains
		String URI_DOMAIN_COMCAST = "COMCAST";
		String URI_DOMAIN_XFINITY = "XFINITY";
		String URI_DOMAIN_UNILEVER = "UNILEVER";
			
	//List of Cache -- Start
		String CACHE_CONFIGURARTION_PARAMS = "configCache";
		String CACHE_USERS = "userCache";
		String CACHE_STRING = "stringCache";
		String CACHE_TEST_ERROR = "testErrorCache";
		String CACHE_MAP = "mapCache";
		String CACHE_USER_ATTRIBUTES = "userAttributesCache";
		String CACHE_ACCOUNT = "accountCache";
	//List of Cache -- End
		
	//List of DAO -- Start
		String DAO_USERS = "userDao";
		String DAO_USER_ATTRIBUTES = "userAttributes";
		String DAO_CHANNELS = "channelDao";
		String DAO_USER_CHANNEL_MAP = "userChannelDao";
		String DAO_USER_SERVICE_MAP = "userServiceDao";
		String DAO_RATINGS = "ratingDao";
		String DAO_ASSETS = "assetDao";
		String DAO_BROWSER_CAPABILITIES = "browserCapabilityDAO";
		String DAO_SERVER = "serversDao";
		String DAO_LOG_FINDER = "logFinderDao";
		String DAO_ACCOUNTS = "accountDao";
		String DAO_USER_ACCOUNT_MAP = "userAccountDao";
		String DAO_FRESH_USER = "freshUserDao";
		String DAO_COMMON = "commonDao";
	//List of DAO -- End	
		
	//Few important Spring bean names
		String SPRING_BEAN_ASYNC_EXECUTER = "asyncExecutor";
		String SPRING_BEAN_EXECUTION_ENGINE = "engine";
		String SPRING_BEAN_FILE_MODIFIER = "fileModifier";
		String SPRING_BEAN_CACHE_INITIALIZER = "cacheInitializer";
		String SPRING_BEAN_TEST_INITIALIZER = "citfTestInitializer";
		String SPRING_BEAN_REST_HANDLER = "restHandler";
	
	//List of Data provider beans -- Start	
		String SPRING_BEAN_DATA_PROVIDER_USER = "userDP";
		String SPRING_BEAN_DATA_PROVIDER_CONFIGURATION = "configDP";
		String SPRING_BEAN_DATA_PROVIDER_CLIENT_DETAILS = "clientDetailsDP";
		String SPRING_BEAN_DATA_PROVIDER_URLS = "endPointUrlDP";
		String SPRING_BEAN_DATA_PROVIDER_KEYS = "keysDP";
		String SPRING_BEAN_DATA_PROVIDER_OAUTH_DATA = "oAuthDP";
		String SPRING_BEAN_DATA_PROVIDER_SCOPE_VALUES = "scopeDP";
		String SPRING_BEAN_DATA_PROVIDER_XTV_PIL = "xtvPilDP";
		String SPRING_BEAN_DATA_PROVIDER_TVE = "tveDP";
		String SPRING_BEAN_DATA_PROVIDER_FRESH_ACCOUNT = "freshAccountDP";
		String SPRING_BEAN_DATA_PROVIDER_FRESH_USER_DATA = "freshUserDP";
	//List of Data provider beans -- End	
		
	
	//Cache Filter condition keys -- Start
	
		//For common...
		String CACHE_FLTR_CONDTN_ENVIRONMENT = "CFC_ALL.ENVIRONMENT";
		String CACHE_FLTR_CONDTN_QUANTITY = "CFC_ALL.QUANTITY";
		String CACHE_FLTR_CONDTN_USER_IDS_CSV = "CFC_ALL.USER_IDS";
		String CACHE_FLTR_CONDTN_UNLOCKED = "CFC_ALL.UNLOCKED";
		String CACHE_FLTR_CONDTN_LOBS_CSV = "CFC_ALL.LOBS_CSV";						//This value will be like VID,HSD
	
		//For user cache...
		String CACHE_FLTR_CONDTN_LOGIN_STATUS = "CFC_USER.LOGIN_STATUS";
		String CACHE_FLTR_CONDTN_CATEGORY = "CFC_USER.CATEGORY";
		String CACHE_FLTR_CONDTN_CHANNEL_NAME = "CFC_USER.CHANNEL_NAME";
		String CACHE_FLTR_CONDTN_CHANNEL_SUBSCRIPTION = "CFC_USER.CHANNEL_SUBSCRIPTION";
		String CACHE_FLTR_CONDTN_TV_RATING = "CFC_USER.TV_RATING";
		String CACHE_FLTR_CONDTN_MOVIE_RATING = "CFC_USER.MOVIE_RATING";
		String CACHE_FLTR_CONDTN_PRIMARY_ACCOUNT_ID = "CFC_USER.PRIMARY_ACCOUNT_ID";
		String CACHE_FLTR_CONDTN_SECONDARY_ACCOUNT_ID = "CFC_USER.SECONDARY_ACCOUNT_ID";
		String CACHE_FLTR_CONDTN_HAS_MULTIPLE_PRIMARY_ACCOUNTS = "CFC_USER.HAS_MULTIPLE_PRIMARY_ACCOUNTS";
		String CACHE_FLTR_CONDTN_HAS_MULTIPLE_SECONDARY_ACCOUNTS = "CFC_USER.HAS_MULTIPLE_SECONDARY_ACCOUNTS";
		
		//User Attribute filter condition
		String CACHE_FLTR_CONDTN_USER_ATTR_EMAIL = "CFC_USERATTR.USER_ATTR_EMAIL";
		String CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL = "CFC_USERATTR.USER_ATTR_ALTERNATE_MAIL";
		String CACHE_FLTR_CONDTN_USER_ATTR_SECRET_QUESTION = "CFC_USERATTR.USER_ATTR_SECRET_QUESTION";
		String CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID = "CFC_USERATTR.USER_ATTR_FACE_BOOK_ID";
		String CACHE_FLTR_CONDTN_USER_ATTR_SSN = "CFC_USERATTR.USER_ATTR_SSN";
		String CACHE_FLTR_CONDTN_USER_ATTR_DOB = "CFC_USERATTR.USER_ATTR_DOB";
		String CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_SAME_AS_ALTER_EMAIL = "CFC_USERATTR.USER_ATTR_FB_ID_SAME_AS_ALTER_EMAIL";
        String CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID_NOT_SAME_AS_ALTER_EMAIL = "CFC_USERATTR.USER_ATTR_FB_ID_NOT_SAME_AS_ALTER_EMAIL";
		
		//Account filter condition
		String CACHE_FLTR_CONDTN_PHONE = "CFC_ACCOUNT.PHONE_NUMBER";
		String CACHE_FLTR_CONDTN_ACCOUNT_STATUS = "CFC_ACCOUNT.ACCOUNT_STATUS";
		String CACHE_FLTR_CONDTN_USER_ROLE = "CFC_ACCOUNT.USER_ROLE";
		String CACHE_FLTR_CONDTN_TRANSFER_FLAG = "CFC_ACCOUNT.TRANSFER_FLAG";
		String CACHE_FLTR_CONDTN_ADDRESS = "CFC_ACCOUNT.ADDRESS";
		String CACHE_FLTR_CONDTN_ACCOUNT_TYPE = "CFC_ACCOUNT.ACCOUNT_TYPE";
		String CACHE_FLTR_CONDTN_FRESH_ACCOUNT_SSN = "CFC_ACCOUNT.FRESH_ACCOUNT_SSN";
		String CACHE_FLTR_CONDTN_SERVICE_ACCOUNT_ID = "CFC_ACCOUNT.SERVICE_ACCOUNT_ID";
		
		String CACHE_FLTR_VALUE_NOT_NULL = "CFV_NOT_NULL";
		String CACHE_FLTR_VALUE_NULL = "CFV_NULL";
	//Cache Filter condition keys -- End
	
		String USER_STATUS_ACTIVE = "ACTIVE";
		String USER_STATUS_SUSPENDED = "SUSPENDED";
		String USER_STATUS_DELETED = "DELETED";
		
	//grant types....
		String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
		String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
		String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
		String GRANT_TYPE_DEVICE_CODE = "device_code";
	
	//response_types...
	String RESPONSE_TYPE_CODE = "code";
	
	//List of Validators -- Start
		String VALIDATOR_CIMA_LOGIN_TEST = "cimaLoginApiTestValidator";
		String VALIDATOR_CIMA_IDM_TEST = "cimaIdmTestValidator";
		String VALIDATOR_CIMA_TVE_TEST = "cimaTveTestValidator";
	//List of Validators -- End
		
		
	//LDAP Authorization...
	String LDAP_AUTHORIZATION_STATUS_ACTIVE = "A";
	String LDAP_AUTHORIZATION_STATUS_CANCELLED = "C";
	
	
	//Common Exception messages -- Start
		String EXCEPTION_INVALID_INPUT = "Input(s) is(are) null or invalid!";
		String EXCEPTION_UI_SERVER_UNAVAILABLE = "Server seems to be unavaialble!";
		String EXCEPTION_UI_INTERNAL_SERVER_ERROR = "Page is showing internal server error(HTTP 500)!";
	//Common Exception messages -- End
		
	//Common UI page errors -- Start
		String UI_PAGE_ERROR_PAGE_NOT_FOUND = "Error 404--Not Found";
		String UI_PAGE_ERROR_INTERNAL_ERROR = "Error 500 - Internal Error";
	//Common UI page errors -- End	
		
	// Parental Control configuration details -- Start
		String PARENTAL_CONTROL_USER = "cimauser";
		String PARENTAL_CONTROL_USER_PWD = "Xee7ZaiL";
	// Parental Control configuration details -- End
		
	//Common Test method parameters -- start
		String TEST_PARAMETER_CLASS_ID = "TESTP_CLASS_ID";
		
		String TEST_PARAMETER_SERVICE_ID = "TESTP_SERVICE_ID";
		String TEST_PARAMETER_ENTITLEMENT = "TESTP_ENTITLEMENT";
		String TEST_PARAMETER_AUTHORIZATION_STATUS = "TESTP_AUTHORIZATION_STATUS";
		String TEST_PARAMETER_AUTHENTICATION_STATUS = "TESTP_AUTHENTICATION_STATUS";
		
		String TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTIONS_NOT_NULL_CSV = "TESTP_PWD_RECOVERY_OPTIONS_NOT_NULL_CSV";
		String TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTIONS_NULL_CSV = "TESTP_PWD_RECOVERY_OPTIONS_NULL_CSV";
		String TEST_PARAMETER_IDM_PASSWORD_RECOVERY_OPTION_CHOSEN = "TESTP_PWD_RECOVERY_OPTION_CHOSEN";
		String TEST_PARAMETER_IDM_SCOPE_KEY="idm_Scope_Key";
		String TEST_PARAMETER_IDM_INVALID_USERID = "TESTP_IDM_INVALID_USERID";
		String TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER = "TESTP_IDM_INVALID_SECURITY_ANSWER";
		String TEST_PARAMETER_IDM_INVALID_SECURITY_ANSWER_TYPE = "TESTP_IDM_INVALID_SECURITY_ANSWER_TYPE";
		String TEST_PARAMETER_IDM_INVALID_ZIPCODE = "TESTP_IDM_INVALID_ZIPCODE";
		String TEST_PARAMETER_IDM_USER_PASSWORD = "TESTP_IDM_USER_PASSWORD";
		String TEST_PARAMETER_IDM_USER_RETYPE_PASSWORD = "TESTP_IDM_USER_RETYPE_PASSWORD";
		String TEST_PARAMETER_IDM_USER_PASSWORD_TYPE = "TESTP_IDM_USER_PASSWORD_TYPE";
		String TEST_PARAMETER_IDM_AGENT_RESET_CODE = "TESTP_IDM_AGENT_RESET_CODE";
		String TEST_PARAMETER_IDM_SMS_CODE = "TESTP_IDM_SMS_CODE";
		String TEST_PARAMETER_IDM_SMS_CODE_TYPE = "TESTP_IDM_SMS_CODE_TYPE";
		String TEST_PARAMETER_IDM_NEW_USER_ID = "TESTP_IDM_NEW_USER_ID";
		String TEST_PARAMETER_IDM_NEW_USER_KEEP_SIGNEDIN = "TESTP_IDM_NEW_USER_KEEP_SIGNEDIN";
		String TEST_PARAMETER_IDM_USER_IDENTIFIER_TYPE = "TESTP_IDM_USER_IDENTIFIER_TYPE";
		String TEST_PARAMETER_IDM_USER_PHONE = "TESTP_IDM_USER_PHONE";
		String TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION = "TESTP_IDM_UL_PAGE_OPTION";
		String TEST_PARAMETER_IDM_USER_LOOKUP_PAGE_OPTION_TWO = "TESTP_IDM_UL_PAGE_OPTION_TWO";
		String TEST_PARAMETER_IDM_USER_PHONE_TYPE = "TESTP_IDM_USER_PHONE_TYPE";
		String TEST_PARAMETER_IDM_USER_NAME = "TESTP_IDM_USR_NAME";
		String TEST_PARAMETER_IDM_USER_NAME_TYPE = "TESTP_IDM_USR_NAME_TYPE";
		String TEST_PARAMETER_IDM_USER_ALT_EMAIL = "TESTP_IDM_USR_ALT_EMAIL";
		String TEST_PARAMETER_IDM_IDENTITY_TYPE = "TESTP_IDM_IDENTITY_TYPE";
		String TEST_PARAMETER_IDM_IDENTITY_VALUE = "TESTP_IDM_IDENTITY_VALUE";
		
		String TEST_PARAMETER_IDM_CRED_TYPE_KEY="idm_cred_Type";
		String TEST_PARAMETER_IDM_CRED_TYPE_VALUE="idm_cred_Value";
		String IDM_CRED_TYPE_VALUE_NEW="new";
		String IDM_CRED_TYPE_VALUE_EXISTING="existing";
		String IDM_CRED_TYPE_VALUE_INVALID="invalid";
		String IDM_CRED_TYPE_VALUE_NULL="null";
		String IDM_API_RESPONSE_TYPE_UNAUTHORIZED="Unauthorized Access";
				
	//Common Test method parameters -- end	
		
	//Date formats
		String DATE_FORMAT_DETAILED_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
		String DATE_FORMAT_DETAILED_WITH_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
		String DATE_FORMAT_US_EN_WITHOUT_TIME = "MM/dd/yyyy";

	String BEAN_NAME_UI_PAGE_NAVIGATOR = "pageNavigator";
	String BEAN_NAME_UI_PAGE_ROUTER = "pageIdentifier";

	String CURRENT_TEST_METHOD = "CURRENT_TEST";
	
	//Selenium waitfor timeout *
	long WAIT_TIMEOUT = 180;
	long SLEEP_TIME = 5000;
	long WAIT_TIME_UI = 2000;
	
	//Test Execution Status -- Start
		String TEST_EXECUTION_STATUS_PASSED = "Passed";
		String TEST_EXECUTION_STATUS_FAILED = "Failed";
	//Test Execution Status -- End
	
}
