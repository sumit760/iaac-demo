package com.comcast.test.citf.core.dataProvider;

import org.springframework.stereotype.Service;


/**
*
* @author Sumit Pal
* @since April 2016
*
* This is data provider to provide configuration data.
*
*/
@Service("configDP")
public class ConfigurationDataProvider extends AbstractDataProvider{
	
	public enum ConfigurationPropKeys{
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
	
	public enum Browser {
		BROWSER_FF("config_data.browser.firefox"),
		BROWSER_CHROME("config_data.browser.chrome"),
		BROWSER_IE("config_data.browser.ie"),
		BROWSER_SAFARI("config_data.browser.safari");
		
		private final String value;
		Browser(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public enum Platform {
		PLATFORM_WINDOWS("config_data.browser.platform.windows"),
		PLATFORM_LINUX("config_data.browser.platform.linux"),
		PLATFORM_MAC("config_data.browser.platform.mac");
		
		private final String value;
		Platform(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public String getFireFoxBrowser() {
		return getConfigString(Browser.BROWSER_FF.getValue());
	}
	
	public String getChromeBrowser() {
		return getConfigString(Browser.BROWSER_CHROME.getValue());
	}
	
	public String getIEBrowser() {
		return getConfigString(Browser.BROWSER_IE.getValue());
	}
	
	public String getSafariBrowser() {
		return getConfigString(Browser.BROWSER_SAFARI.getValue());
	}
	
	public String getWindowsPlatform() {
		return getConfigString(Platform.PLATFORM_WINDOWS.getValue());	
	}
	
	public String getLinuxPlatform() {
		return getConfigString(Platform.PLATFORM_LINUX.getValue());	
	}
	
	public String getMacPlatform() {
		return getConfigString(Platform.PLATFORM_MAC.getValue());	
	}
	
	public String getCitfRunMode(){
		return getConfigString(ConfigurationPropKeys.CITF_RUN_MODE.getValue());
	}
	
	public long getMaxCitfExecutionTime() throws NumberFormatException{
		return Long.parseLong(getConfigString(ConfigurationPropKeys.MAX_CITF_EXECUTION_TIME.getValue()));
	}
	
	public int getMaxNumberOfParallelThreads() throws NumberFormatException{
		return Integer.parseInt(getConfigString(ConfigurationPropKeys.NUMBER_OF_MAX_PARALLEL_THREADS.getValue()));
	}
	
	public String getLogReadingMechanism(){
		return getConfigString(ConfigurationPropKeys.LOG_READING_MECHANISM.getValue());
	}
	
	public long getMaxWaitingTimeToAcquireLock() throws NumberFormatException{
		return Long.parseLong(getConfigString(ConfigurationPropKeys.MAX_WAITING_TIME_TO_ACQUIRE_LOCK.getValue()));
	}
	
	public String getIdmServerTimeZone() throws NumberFormatException{
		return getConfigString(ConfigurationPropKeys.IDM_SERVER_TIMEZONE.getValue());
	}
	
	public long getResetCodeExpirationTime(){
		return Long.parseLong(getConfigString(ConfigurationPropKeys.RESET_CODE_EXPIRATION_TIME.getValue()));
	}
	
	public JavaKeyStoreCredentials getCitfKeystoreCredentials(){
		return new JavaKeyStoreCredentials(	getConfigString(ConfigurationPropKeys.CITF_KEYSTORE_ALIAS.getValue()),
											getConfigString(ConfigurationPropKeys.CITF_KEYSTORE_ENTRY_PASSWORD.getValue()),
											getConfigString(ConfigurationPropKeys.CITF_KEYSTORE_KEYSTORE_PASSWORD.getValue()));
	}
	
	public JavaKeyStoreCredentials getTVEKeystoreCredentials(){
		return new JavaKeyStoreCredentials(	getConfigString(ConfigurationPropKeys.TVE_KEYSTORE_ALIAS.getValue()),
											getConfigString(ConfigurationPropKeys.TVE_KEYSTORE_ENTRY_PASSWORD.getValue()),
											getConfigString(ConfigurationPropKeys.TVE_KEYSTORE_KEYSTORE_PASSWORD.getValue()));
	}
	
	public SauceLabCredentials getSauceLabCredentials(){
		return new SauceLabCredentials(	getConfigString(ConfigurationPropKeys.SAUCELAB_USER_NAME.getValue()),
										getConfigString(ConfigurationPropKeys.SAUCELAB_ACCESS_KEY.getValue()),
										getConfigString(ConfigurationPropKeys.SAUCELAB_ON_DEMAND_URL_WITH_SAUCECONNECT.getValue()),
										getConfigString(ConfigurationPropKeys.SAUCELAB_ON_DEMAND_URL_WITHOUT_SAUCECONNECT.getValue()));
	}	
	
	public static class SauceLabCredentials{
		private String userName;
		private String accessKey;
		private String onDemandUrlWithSauceConnect;
		private String onDemandUrlWithoutSauceConnect;
		
		public SauceLabCredentials(String userName, String accessKey, String onDemandUrlWithSauceConnect, String onDemandUrlWithoutSauceConnect){
			this.userName = userName;
			this.accessKey = accessKey;
			this.onDemandUrlWithSauceConnect = onDemandUrlWithSauceConnect;
			this.onDemandUrlWithoutSauceConnect = onDemandUrlWithoutSauceConnect;
		}

		public String getUserName() {
			return userName;
		}

		public String getAccessKey() {
			return accessKey;
		}

		public String getOnDemandUrlWithSauceConnect() {
			return onDemandUrlWithSauceConnect;
		}

		public String getOnDemandUrlWithoutSauceConnect() {
			return onDemandUrlWithoutSauceConnect;
		}
	}
	
	public static class JavaKeyStoreCredentials{
		private String alias;
		private String entryPassword;
		private String keyStorePassword;
		
		public JavaKeyStoreCredentials(String alias, String entryPassword, String keyStorePassword){
			this.alias = alias;
			this.entryPassword = entryPassword;
			this.keyStorePassword = keyStorePassword;
		}

		public String getAlias() {
			return alias;
		}

		public String getEntryPassword() {
			return entryPassword;
		}

		public String getKeyStorePassword() {
			return keyStorePassword;
		}
	}
}
