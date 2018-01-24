package com.comcast.test.citf.core.setup.cima;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * Populate database after reading all the setup CSV files.
 * 
 * @author spal004c
 * @since January 2016
 *
 */
public class InitialDataLoader {
	
	private static final String FILE_NAME_CHANNELS = "cima-setup/Test_Channels.csv";
	private static final String FILE_NAME_RATINGS = "cima-setup/Channel_Ratings.csv";
	private static final String FILE_NAME_ACCOUNTS_FOR_QA = "cima-setup/Test_Accounts_QA.csv";
	private static final String FILE_NAME_LOGIN_USERS_FOR_QA = "cima-setup/Test_Users_Login_QA.csv";
	private static final String FILE_NAME_IDM_USERS_FOR_QA = "cima-setup/Test_Users_Idm_QA.csv";
	private static final String FILE_NAME_TVE_USERS_FOR_QA = "cima-setup/Test_Users_TVE_QA.csv";
	private static final String FILE_NAME_ACCOUNTS_FOR_STAGE = "cima-setup/Test_Accounts_STAGE.csv";
	private static final String FILE_NAME_LOGIN_USERS_FOR_STAGE = "cima-setup/Test_Users_Login_STAGE.csv";
	private static final String FILE_NAME_IDM_USERS_FOR_STAGE = "cima-setup/Test_Users_Idm_STAGE.csv";
	
	/**
	 * Populate database after reading all the setup CSV files.
	 * 
	 */
	public static void load() {
		
		//NOTE: This may need to change to load only those artifacts which belong to the execution environment.	
		ChannelAndRatingExcelLoader.loadChannels(FILE_NAME_CHANNELS);
		AccountExcelLoader.loadAccounts(FILE_NAME_ACCOUNTS_FOR_QA, ICommonConstants.ENVIRONMENT_QA);
		UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_LOGIN_USERS_FOR_QA, ICommonConstants.ENVIRONMENT_QA, ICimaCommonConstants.USER_CATEGORY_LOG_IN);
		UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_IDM_USERS_FOR_QA, ICommonConstants.ENVIRONMENT_QA, ICimaCommonConstants.USER_CATEGORY_IDM);
		UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_TVE_USERS_FOR_QA, ICommonConstants.ENVIRONMENT_QA, ICimaCommonConstants.USER_CATEGORY_TVE);
		AccountExcelLoader.loadAccounts(FILE_NAME_ACCOUNTS_FOR_STAGE, ICommonConstants.ENVIRONMENT_STAGE);
		UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_LOGIN_USERS_FOR_STAGE, ICommonConstants.ENVIRONMENT_STAGE, ICimaCommonConstants.USER_CATEGORY_LOG_IN);
		UserSetupExcelLoader.populateUserAndMaps(FILE_NAME_IDM_USERS_FOR_STAGE, ICommonConstants.ENVIRONMENT_STAGE, ICimaCommonConstants.USER_CATEGORY_IDM);
		
		//NOTE: This is may not need to be loaded for all projects
		ChannelAndRatingExcelLoader.loadRatings(FILE_NAME_RATINGS);
	}
}
