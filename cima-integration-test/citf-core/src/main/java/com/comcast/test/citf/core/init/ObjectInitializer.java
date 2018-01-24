package com.comcast.test.citf.core.init;

import com.comcast.test.citf.common.cima.persistence.AccountsDAO;
import com.comcast.test.citf.common.cima.persistence.AssetDAO;
import com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO;
import com.comcast.test.citf.common.cima.persistence.ChannelDAO;
import com.comcast.test.citf.common.cima.persistence.CommonDAO;
import com.comcast.test.citf.common.cima.persistence.FreshUserDAO;
import com.comcast.test.citf.common.cima.persistence.LogFinderDAO;
import com.comcast.test.citf.common.cima.persistence.RatingDAO;
import com.comcast.test.citf.common.cima.persistence.ServersDAO;
import com.comcast.test.citf.common.cima.persistence.UserAccountDAO;
import com.comcast.test.citf.common.cima.persistence.UserAttributesDAO;
import com.comcast.test.citf.common.cima.persistence.UserChannelDAO;
import com.comcast.test.citf.common.cima.persistence.UserDAO;
import com.comcast.test.citf.common.crypto.EncryptionHandler;
import com.comcast.test.citf.common.http.RestHandler;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.parser.SpreadSheetReader;
import com.comcast.test.citf.common.reader.LogReader;
import com.comcast.test.citf.common.service.ParentalControlService;
import com.comcast.test.citf.common.service.UserNameGeneratorService;
import com.comcast.test.citf.common.ui.router.AbstractPageRouter;
import com.comcast.test.citf.common.ui.router.PageNavigator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.validator.ICommonValidator;
import com.comcast.test.citf.core.cache.ICitfCache;

/**
 * This class initializes spring beans which have been heavily used across CITF 
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
public abstract class ObjectInitializer {
	
	/**
	 * @return UserDAO @see com.comcast.test.citf.common.cima.persistence.UserDAO
	 */
	public static UserDAO getUserDAO(){
		return (UserDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_USERS);
	}
	
	
	/**
	 * @return ChannelDAO @see com.comcast.test.citf.common.cima.persistence.ChannelDAO
	 */
	public static ChannelDAO getChannelDAO(){
		return (ChannelDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_CHANNELS);
	}
	
	/**
	 * @return UserChannelDAO @see com.comcast.test.citf.common.cima.persistence.UserChannelDAO
	 */
	public static UserChannelDAO getUserChannelDAO(){
		return (UserChannelDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_USER_CHANNEL_MAP);
	}
	
	/**
	 * @return RatingDAO @see com.comcast.test.citf.common.cima.persistence.RatingDAO
	 */
	public static RatingDAO getRatingDAO(){
		return (RatingDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_RATINGS);
	}
	
	/**
	 * @return AssetDAO @see com.comcast.test.citf.common.cima.persistence.AssetDAO
	 */
	public static AssetDAO getAssetDAO(){
		return (AssetDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_ASSETS);
	}
	
	/**
	 * @return BrowserCapabilityDAO @see com.comcast.test.citf.common.cima.persistence.BrowserCapabilityDAO
	 */
	public static BrowserCapabilityDAO getBrowserCapabilityDAO(){
		return (BrowserCapabilityDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_BROWSER_CAPABILITIES);
	}
	
	/**
	 * @return ServersDAO @see com.comcast.test.citf.common.cima.persistence.ServersDAO
	 */
	public static ServersDAO getServersDAO(){
		return (ServersDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_SERVER);
	}
	
	/**
	 * @return UserAttributesDAO @see com.comcast.test.citf.common.cima.persistence.UserAttributesDAO
	 */
	public static UserAttributesDAO getUserAttributeDAO(){
		return (UserAttributesDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_USER_ATTRIBUTES);
	}
	
	/**
	 * @return LogFinderDAO @see com.comcast.test.citf.common.cima.persistence.LogFinderDAO
	 */
	public static LogFinderDAO getLogFinderDAO(){
		return (LogFinderDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_LOG_FINDER);
	}
	
	/**
	 * @return AccountsDAO @see com.comcast.test.citf.common.cima.persistence.AccountsDAO
	 */
	public static AccountsDAO getAccountsDAO(){
		return (AccountsDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_ACCOUNTS);
	}
	
	/**
	 * @return UserAccountDAO @see com.comcast.test.citf.common.cima.persistence.UserAccountDAO
	 */
	public static UserAccountDAO getUserAccountDAO(){
		return (UserAccountDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_USER_ACCOUNT_MAP);
	}
	
	/**
	 * @return FreshUserDAO @see com.comcast.test.citf.common.cima.persistence.FreshUserDAO
	 */
	public static FreshUserDAO getFreshUserDAO(){
		return (FreshUserDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_FRESH_USER);
	}
	
	/**
	 * @return CommonDAO @see com.comcast.test.citf.common.cima.persistence.CommonDAO
	 */
	public static CommonDAO getCommonDAO(){
		return (CommonDAO)CoreContextInitilizer.getBean(ICommonConstants.DAO_COMMON);
	}
	
	/**
	 * @return ICitfCache @see com.comcast.test.citf.core.cache.ICitfCache
	 */
	public static ICitfCache getCache(String cache){
		return (ICitfCache)CoreContextInitilizer.getBean(cache);
	}
	
	/**
	 * Instantiate, initializes and provides EncryptionHandler
	 * 
	 * @return EncryptionHandler @see com.comcast.test.citf.common.crypto.EncryptionHandler
	 */
	public static EncryptionHandler getEncryptionHandler(String fileName, String alias, String storePassword, String entryPassword){
		EncryptionHandler handler = (EncryptionHandler)CoreContextInitilizer.getBean("encryptionHandler");
		handler.initializeEncrypter(fileName, alias, storePassword, entryPassword);
		return handler;
	}
	
	/**
	 * @return LDAPInterface @see com.comcast.test.citf.common.ldap.LDAPInterface
	 */
	public static LDAPInterface getLdapService(){
		return (LDAPInterface)CoreContextInitilizer.getBean("ldapService");
	}
	
	/**
	 * @return ICommonValidator @see com.comcast.test.citf.common.validator.ICommonValidator
	 */
	public static ICommonValidator getValidator(String validator){
		return (ICommonValidator)CoreContextInitilizer.getBean(validator);
	}
	
	/**
	 * @return SpreadSheetReader @see com.comcast.test.citf.common.parser.SpreadSheetReader
	 */
	public static SpreadSheetReader getExcelReader(){
		return (SpreadSheetReader)CoreContextInitilizer.getBean("spreadSheetReader");
	}
	
	/**
	 * @return ParentalControlService @see com.comcast.test.citf.common.util.ParentalControlService
	 */
	public static ParentalControlService getParentalControlService(){
		return (ParentalControlService)CoreContextInitilizer.getBean("parentalControlService");
	}
	
	/**
	 * @return OAuthInitializer @see com.comcast.test.citf.core.init.OAuthInitializer
	 * 
	 * @deprecated will remove this class once every OAuth test will be switched to use Google OAuth API
	 */
	@Deprecated
	public static OAuthInitializer getOAuthService(){
		return (OAuthInitializer)CoreContextInitilizer.getBean("oauthInit");
	}
	
	/**
	 * @return PageNavigator @see com.comcast.test.citf.common.ui.router.PageNavigator
	 */
	public static PageNavigator getPageNavigator(){
		AbstractPageRouter router = (AbstractPageRouter)CoreContextInitilizer.getBean(ICommonConstants.BEAN_NAME_UI_PAGE_ROUTER);
		PageNavigator navigator = (PageNavigator)CoreContextInitilizer.getBean(ICommonConstants.BEAN_NAME_UI_PAGE_NAVIGATOR);
		navigator.setIdentifier(router);
		return navigator;
	}
	
	/**
	 * @return LogReader @see com.comcast.test.citf.common.util.LogReader
	 */
	public static LogReader getLogReader(){
		return (LogReader)CoreContextInitilizer.getBean("logReader");
	}
	
	/**
	 * @return UserNameGeneratorService @see com.comcast.test.citf.common.util.UserNameGeneratorService
	 */
	public static UserNameGeneratorService getUserNameGeneratorService(){
		return (UserNameGeneratorService)CoreContextInitilizer.getBean("usernameGenerator");
	}
	
	
	/**
	 * @return CitfTestInitializer @see com.comcast.test.citf.core.init.CitfTestInitializer
	 */
	public static CitfTestInitializer getCitfTestInitializer(){
		return (CitfTestInitializer)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_TEST_INITIALIZER);
	}
	
	/**
	 * Provides instance of RestHandler. @see com.comcast.test.citf.common.http.RestHandler
	 * 
	 * @return instance of RestHandler
	 */
	public static RestHandler getRestHandler(){
        return (RestHandler)CoreContextInitilizer.getBean(ICommonConstants.SPRING_BEAN_REST_HANDLER);
    }
}
