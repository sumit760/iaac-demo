package com.comcast.test.citf.core.setup.cima;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.cima.persistence.AccountsDAO;
import com.comcast.test.citf.common.cima.persistence.UserAccountDAO;
import com.comcast.test.citf.common.cima.persistence.UserAttributesDAO;
import com.comcast.test.citf.common.cima.persistence.UserChannelDAO;
import com.comcast.test.citf.common.cima.persistence.UserDAO;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.Channels;
import com.comcast.test.citf.common.cima.persistence.beans.Users;
import com.comcast.test.citf.common.exception.InvalidUserException;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.controller.AbstractExecutionController;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.setup.IDataLoderEnums;

/**
 * This class populates Users, User Attributes and its mapping tables in database after fetching the data from corresponding CSV files.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

public class UserSetupExcelLoader implements IDataLoderEnums{
	
	/**
	 * Populates data in Users, User Attributes and its mapping tables in database after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 * @param environment
	 * 			Test environment
	 * @param category
	 * 			The category to decide in which tests this data going to be used, i.e. IDM, LOGIN, TVE.
	 */
	public static int populateUserAndMaps(String inputFile, String environment, String category){
		
		int noOfEntriesLoaded = 0;
		try{
			if(inputFile==null || AbstractExecutionController.isInEnvironment(environment)==null || (!ICimaCommonConstants.USER_CATEGORY_LOG_IN.equals(category) && 
					!ICimaCommonConstants.USER_CATEGORY_TVE.equals(category)  && !ICimaCommonConstants.USER_CATEGORY_IDM.equals(category))){
				throw new IllegalArgumentException(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);			
			}
			
			usrDao = ObjectInitializer.getUserDAO();
			accDao = ObjectInitializer.getAccountsDAO();
			usrAttrDao = ObjectInitializer.getUserAttributeDAO();
			usrAccDao = ObjectInitializer.getUserAccountDAO();
			
			switch(category){
			
				case ICimaCommonConstants.USER_CATEGORY_TVE:
					usrChnlDao = ObjectInitializer.getUserChannelDAO();
					noOfEntriesLoaded = loadTveUsers(inputFile, environment);
					break;
					
				case ICimaCommonConstants.USER_CATEGORY_LOG_IN:
					noOfEntriesLoaded = loadLoginUsers(inputFile, environment);
					break;
					
				case ICimaCommonConstants.USER_CATEGORY_IDM:
					noOfEntriesLoaded = loadIdmUsers(inputFile, environment);
					break;	
			}
			
			
		}
		catch(Exception t){
			LOGGER.error("Error occurred while initialing database setup : ", t);
		}
		
		return noOfEntriesLoaded;
	}
	
	
	/**
	 * Populates data in Users (belong to LOGIN), User Attributes and User Accounts mapping tables in database after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 * @param environment
	 * 			Test environment
	 */
	private static int loadLoginUsers(String inputFile, String environment) {
		List<Map<String, Object>> userMaps;
		int rowCount = 1;
			
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(UserExcelColumns.USER_ID.getValue());
		columnNames.add(UserExcelColumns.PASSWORD.getValue());
		columnNames.add(UserExcelColumns.PRIMARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.SECONDARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.LOGIN_STATUS.getValue());
		
		addUserAttributeColumnNames(columnNames);
		
		userMaps = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
		
		if(userMaps!=null && !userMaps.isEmpty()){
			
			for(Map<String, Object> userData : userMaps){
				
				if(userData==null || userData.get(UserExcelColumns.USER_ID.getValue())==null || 
						 userData.get(UserExcelColumns.PASSWORD.getValue())==null){
					LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
					continue;
				}
				
				//User
				checkUserInExcel(userData);
				//User attributes...
				checkUserAttributesInExcel(userData, userId);
				
				try{
					Users user = usrDao.populateAndGetUser(userId, password, ICimaCommonConstants.USER_CATEGORY_LOG_IN, loginStatus, environment);
                    populateUserAttributesAndAccountUserMap(user);
				}
				catch(InvalidUserException e){
					LOGGER.error("User cannot be inserted due to : ", e);
				}	
				
				rowCount++;
			}
			LOGGER.info("User data population is over. The input file {} found with {} rows for {} environment.", 
					inputFile, (rowCount-1), environment);
		}
		return rowCount;
	}
		
	
	/**
	 * Populates data in Users (belong to TVE), User Attributes and User Accounts mapping tables in database after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 * @param environment
	 * 			Test environment
	 */
	private static int loadTveUsers(String inputFile, String environment){
		List<Map<String, Object>> userChannelMaps;
		
		int rowCount = 1;
		List<Channels> channelList = ObjectInitializer.getChannelDAO().findAllChannels();
		if(channelList==null || channelList.isEmpty()){
			throw new IllegalStateException("Channel list not found from database!!!");
		}
	
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(UserExcelColumns.USER_ID.getValue());
		columnNames.add(UserExcelColumns.PASSWORD.getValue());
		columnNames.add(UserExcelColumns.PRIMARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.SECONDARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.TV_RATING.getValue());
		columnNames.add(UserExcelColumns.MOVIE_RATING.getValue());
		for(Channels channel : channelList){
			columnNames.add(channel.getName());
		}
	
		addUserAttributeColumnNames(columnNames);
	
		userChannelMaps = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
		if(userChannelMaps!=null && !userChannelMaps.isEmpty()){
		
			for(Map<String, Object> userData : userChannelMaps){
			
				if(userData==null || userData.get(UserExcelColumns.USER_ID.getValue())==null || 
					 userData.get(UserExcelColumns.PASSWORD.getValue())==null){
					LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
					continue;
				}
				
				//User
				checkUserInExcel(userData);
				String tvRating = userData.get(UserExcelColumns.TV_RATING.getValue())!=null ? userData.get(UserExcelColumns.TV_RATING.getValue()).toString() : null;
				String movieRating = userData.get(UserExcelColumns.MOVIE_RATING.getValue())!=null ? userData.get(UserExcelColumns.MOVIE_RATING.getValue()).toString() : null;
				
				//User attributes...
				checkUserAttributesInExcel(userData, userId);
			
				try{
					Users user = usrDao.populateAndGetUser(userId, password, ICimaCommonConstants.USER_CATEGORY_TVE, null, tvRating, movieRating, environment);
			
					Set<String> keys = userData.keySet();
					int channelCount = keys.size() - 4;
			
					if(channelCount>0){
						int columnIndex = 1;
				
						for(Channels channel : channelList){
							if(keys.contains(channel.getName())){
								usrChnlDao.populateMap(user, channel, userData.get(channel.getName()).toString());
								columnIndex++;
							}
					
							if(columnIndex>channelCount){
								break;
							}
						}
					}
                    populateUserAttributesAndAccountUserMap(user);
				}
				catch(InvalidUserException e){
					LOGGER.error("User's details cannot be inserted due to : ", e);
				}
			
				rowCount++;
			}
			LOGGER.info("User data population is over. The input file {} found with {} rows for {} environment.", 
					inputFile, (rowCount-1), environment);
		}
		
		return rowCount;
	}
	
	
	/**
	 * Populates data in Users (belong to IDM), User Attributes and User Accounts mapping tables in database after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 * @param environment
	 * 			Test environment
	 */
	private static int loadIdmUsers(String inputFile, String environment) {
		List<Map<String, Object>> userServiceMaps;
			
		int rowCount = 1;
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(UserExcelColumns.USER_ID.getValue());
		columnNames.add(UserExcelColumns.PASSWORD.getValue());
		columnNames.add(UserExcelColumns.PRIMARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.SECONDARY_BILLING_ACCOUNTS.getValue());
		columnNames.add(UserExcelColumns.LOGIN_STATUS.getValue());
	
		addUserAttributeColumnNames(columnNames);
		
		userServiceMaps = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
		
		if(userServiceMaps!=null && !userServiceMaps.isEmpty()){
			
			for(Map<String, Object> userData : userServiceMaps){
				
				if(userData==null || userData.get(UserExcelColumns.USER_ID.getValue())==null || 
						 userData.get(UserExcelColumns.PASSWORD.getValue())==null){
					LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
					continue;
				}
				
				//User
				checkUserInExcel(userData);
				//User attributes...
				checkUserAttributesInExcel(userData, userId);
				
				try{
					Users user = usrDao.populateAndGetUser(userId, password, ICimaCommonConstants.USER_CATEGORY_IDM, loginStatus, environment);
                    populateUserAttributesAndAccountUserMap(user);
				}
				catch(InvalidUserException e){
					LOGGER.error("User cannot be inserted due to : ", e);
				}	
				
				rowCount++;
			}
			LOGGER.info("User data population is over. The input file {} found with {} rows for {} environment.", 
					inputFile, (rowCount-1), environment);
		}
		
		return rowCount;
	}
	
	
	
	
	
	/********************************************** Private *******************************************************/
	
	private static UserDAO usrDao = null;
	private static AccountsDAO accDao = null;
	private static UserAttributesDAO usrAttrDao = null;
	private static UserChannelDAO usrChnlDao = null;
	private static UserAccountDAO usrAccDao = null;
	
	//Common User columns
	private static String userId = null;
	private static Set<String> primaryAccountIds = null;
	private static Set<String> secondaryAccountIds = null;
	private static String loginStatus = null;
	private static String password = null;
	
	//User attributes columns
	private static boolean isUserAttrExist = false;	
	private static String guid = null;
	private static String email = null;
	private static String altEmailId = null;
	private static String altEmailPswd = null;
	private static String fbId = null;
	private static String fbPswd = null;
	private static String secrtQues = null;
	private static String secrtAns = null;
	private static java.sql.Date dob = null;
	private static String ssn = null;
	
	/**
	 * Utility method to check users data fetched from CSV file and populate a few common variables.
	 * 
	 * @param userData
	 * 			User data map fetched from the CSV file
	 */
	private static void checkUserInExcel(Map<String, Object> userData){
		userId = userData.get(UserExcelColumns.USER_ID.getValue()).toString();
		loginStatus = userData.get(UserExcelColumns.LOGIN_STATUS.getValue())!=null ? userData.get(UserExcelColumns.LOGIN_STATUS.getValue()).toString() : ICommonConstants.DB_STATUS_ACTIVE;
		password = userData.get(UserExcelColumns.PASSWORD.getValue())!=null ? userData.get(UserExcelColumns.PASSWORD.getValue()).toString() : null;
		primaryAccountIds = userData.get(UserExcelColumns.PRIMARY_BILLING_ACCOUNTS.getValue())!=null ?
				StringUtility.getTokensFromString(userData.get(UserExcelColumns.PRIMARY_BILLING_ACCOUNTS.getValue()).toString(), ICommonConstants.COMMA) : null;
		secondaryAccountIds = userData.get(UserExcelColumns.SECONDARY_BILLING_ACCOUNTS.getValue())!=null ?
				StringUtility.getTokensFromString(userData.get(UserExcelColumns.SECONDARY_BILLING_ACCOUNTS.getValue()).toString(), ICommonConstants.COMMA) : null;
	}
	
	/**
	 * Utility method populate User Attribute column headers
	 * 
	 * @param columnNames
	 * 			Name of column headers
	 */
	private static void addUserAttributeColumnNames(Set<String> columnNames){
		columnNames.add(UserExcelColumns.EMAIL.getValue());
		columnNames.add(UserExcelColumns.ALTERNATE_EMAIL.getValue());
		columnNames.add(UserExcelColumns.ALTERNATE_EMAIL_PASSWORD.getValue());
		columnNames.add(UserExcelColumns.FACEBOOK_ID.getValue());
		columnNames.add(UserExcelColumns.FACEBOOK_PASSWORD.getValue());
		columnNames.add(UserExcelColumns.DATE_OF_BIRTH.getValue());
		columnNames.add(UserExcelColumns.SSN.getValue());
		columnNames.add(UserExcelColumns.CUSTOMER_GUID.getValue());
		columnNames.add(UserExcelColumns.SECRET_QUESTION.getValue());
		columnNames.add(UserExcelColumns.SECRET_ANSWER.getValue());
	}
	
	/**
	 * Utility method to check user attribute data fetched from CSV file and populate a few common variables.
	 * 
	 * @param userData
	 * 			User attribute data map fetched from the CSV
	 * @param userId
	 * 			User Id
	 * @return true if operation succeeded, else false
	 */
	private static boolean checkUserAttributesInExcel(Map<String, Object> userData, String userId){
		isUserAttrExist = false;	
		guid = null;
		email = null;
		altEmailId = null;
		altEmailPswd = null;
		fbId = null;
		fbPswd = null;
		secrtQues = null;
		secrtAns = null;
		ssn = null;
		dob = null;
		
		guid = userData.get(UserExcelColumns.CUSTOMER_GUID.getValue())!=null ? userData.get(UserExcelColumns.CUSTOMER_GUID.getValue()).toString() : null;
		
		if(!StringUtility.isStringEmpty(guid)){
			email = userData.get(UserExcelColumns.EMAIL.getValue())!=null ? userData.get(UserExcelColumns.EMAIL.getValue()).toString() : null;
			altEmailId = userData.get(UserExcelColumns.ALTERNATE_EMAIL.getValue())!=null ? userData.get(UserExcelColumns.ALTERNATE_EMAIL.getValue()).toString() : null;
			altEmailPswd = userData.get(UserExcelColumns.ALTERNATE_EMAIL_PASSWORD.getValue())!=null ? userData.get(UserExcelColumns.ALTERNATE_EMAIL_PASSWORD.getValue()).toString() : null;
			fbId = userData.get(UserExcelColumns.FACEBOOK_ID.getValue())!=null ? userData.get(UserExcelColumns.FACEBOOK_ID.getValue()).toString() : null;
			fbPswd = userData.get(UserExcelColumns.FACEBOOK_PASSWORD.getValue())!=null ? userData.get(UserExcelColumns.FACEBOOK_PASSWORD.getValue()).toString() : null;
			secrtQues = userData.get(UserExcelColumns.SECRET_QUESTION.getValue())!=null ? userData.get(UserExcelColumns.SECRET_QUESTION.getValue()).toString() : null;			
			secrtAns = userData.get(UserExcelColumns.SECRET_ANSWER.getValue())!=null ? userData.get(UserExcelColumns.SECRET_ANSWER.getValue()).toString() : null;	
			ssn = userData.get(UserExcelColumns.SSN.getValue())!=null ? userData.get(UserExcelColumns.SSN.getValue()).toString() : null;	
			
			if(userData.get(UserExcelColumns.DATE_OF_BIRTH.getValue())!=null){
				String dobStr = null;
				try{
					dobStr = userData.get(UserExcelColumns.DATE_OF_BIRTH.getValue()).toString();
					dob = MiscUtility.getSqlDate(dobStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME);
				}catch(Exception parseException){
					LOGGER.warn(StringUtility.appendStrings("Not able to insert/update Date of Birth {} as it is not in the expected date format {} for userId ", 
							dobStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME, userId));
				}
			}
			
			isUserAttrExist = true;
		}
		else {
			LOGGER.warn("User attribute will not be inserted/updated as mandatory data[guid, email & first name] is not found/invalid for user {}", userId);
		}
		return isUserAttrExist;
	}

    private static void populateUserAttributesAndAccountUserMap(Users user) throws InvalidUserException{
        //Inserting/updating user attributes
        if(isUserAttrExist && user!=null){
            usrAttrDao.populateAndGetUserAttributes(guid, user, email, altEmailId, altEmailPswd, secrtQues, secrtAns, fbId, fbPswd, ssn, dob);
        }

        //Inserting primary account user map
        if(primaryAccountIds!=null){
            for(String accId : primaryAccountIds){
                Accounts acc = accDao.findAccountById(accId);
                if(acc!=null){
                    usrAccDao.populateAccountUserMap(user, acc, ICimaCommonConstants.UserRoles.PRIMARY);
                }
            }
        }

        //Inserting secondary account user map
        if(secondaryAccountIds!=null){
            for(String accId : secondaryAccountIds){
                Accounts acc = accDao.findAccountById(accId);
                if(acc!=null){
                    usrAccDao.populateAccountUserMap(user, acc, ICimaCommonConstants.UserRoles.SECONDARY);
                }
            }
        }
    }
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserSetupExcelLoader.class);
}
