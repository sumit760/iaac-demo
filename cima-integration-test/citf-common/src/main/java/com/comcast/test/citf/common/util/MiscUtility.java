package com.comcast.test.citf.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;

import com.comcast.test.citf.common.cucumber.CucumberSpringFactory;
import com.comcast.test.citf.common.ui.router.PageError;

/**
 * Miscellaneous utility class.
 * 
 * @author arej001c
 *
 */
public class MiscUtility {
	
	/**
	 * Returns error stack trace.
	 * 
	 * @param throwable
	 * 					The throwable object.
	 * @return
	 * 					The error stack trace.
	 */
	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw,true);
		throwable.printStackTrace(pw);
		
		return sw.getBuffer().toString();
	}

	/**
	 * Returns current system time in milliseconds.
	 * 
	 * @return Current system time in milliseconds
	 */
	public static long getCurrentTimeInMillis(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal.getTimeInMillis();
	}
	
	/**
	 * Checks whether timeInMillis is expired based on the expire time and current time.
	 * 
	 * @param timeInMillis
	 * 						Time in milliseconds.
	 * @param expiry	
	 * 						Expire time.
	 * @return
	 * 						true, if expired.
	 * 						false, otherwise.
	 */
	public static boolean isExpired(long timeInMillis, long expiry){
		boolean expired = false;
		
		if(timeInMillis+expiry < getCurrentTimeInMillis()){
			expired = true;
		}
		return expired;
	}
	
	/**
	 * Returns randomly generated unique id.
	 * 
	 * @return
	 * 			A random unique Id.
	 */
	public static synchronized String generateUniqueId(){
		String id = UUID.randomUUID().toString();
		return id.substring(id.length()-12);
	}
	
	/**
	 * Returns current sql time stamp.
	 * 
	 * @return
	 * 		   Current sql time stamp.
	 */
	public static Timestamp getCurrentSqlDate(){
		return new Timestamp(getCurrentTimeInMillis());
	}
	
	/**
	 * Returns the formatted date.
	 * 
	 * @param dateFormat
	 * 					The desired date format.
	 * @return
	 * 					The formatted date.
	 */
	public static String getFormattedDate(String dateFormat){
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		return formatter.format(new Date());
	}
	
	/**
	 * Updates the given date string into java.sql.Date type.
	 * 
	 * @param dateStr 
	 * 					The date string.
	 * @param dateFormat
	 * 					The date Format.
	 * @return
	 * 					The Sql date.
	 * @throws ParseException
	 */
	public static java.sql.Date getSqlDate(String dateStr, String dateFormat) throws ParseException{
		return new java.sql.Date(getDateInMillis(dateStr, dateFormat));
	}
	
	/**
	 * Returns the provided date string in milliseconds.
	 * 
	 * @param dateStr
	 * 					The date string.
	 * @param dateFormat
	 * 					The date format.
	 * @return
	 * 					The date in milliseconds.
	 * @throws ParseException
	 */
	public static long getDateInMillis(String dateStr, String dateFormat) throws ParseException{
		if(dateStr!=null && dateFormat!=null){
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			Date utilDate = formatter.parse(dateStr);
			return utilDate.getTime();
		}
		return 0;
	}
	
	
	/**
	 * Gets the difference between current time and provided dateStr till the difference is greater than toleranceTimeInHour.
	 * 
	 * @param dateStr 
	 * 					The date string.
	 * @param dateFormat 
	 * 					The date format.
	 * @param isItPastTime 
	 * 					boolean specifies whether dateStr is a past date or not.
	 * @param toleranceTimeInHour 
	 * 					The tolerance time.
	 * @return
	 * 					The time zone difference in milliseconds.
	 * @throws ParseException
	 */
	public static long getTimeZoneDifferenceInMillis(String dateStr, String dateFormat, boolean isItPastTime, int toleranceTimeInHour) throws ParseException{
		long time = getDateInMillis(dateStr, dateFormat);
		long currentTime = getCurrentTimeInMillis();
		
		float diff =  currentTime - time;
		float diffInHour = diff/(60*60*1000);
		int roundDiffInHour = Math.round(diffInHour);
		if(roundDiffInHour<0){
			roundDiffInHour = 0 - roundDiffInHour;
		}
		if(roundDiffInHour<toleranceTimeInHour){
			return 0;
		}
		long tzDiff = roundDiffInHour*60*60*1000L;
		
		if( (isItPastTime && (time+tzDiff)>currentTime) || (!isItPastTime && (time+tzDiff)<currentTime)){
			tzDiff = 0 - tzDiff;
		}
		return tzDiff;
	}
	
	
	/**
	 * Returns the difference between provided time zone and GMT in minutes.
	 * 
	 * @param timezone e.g. EST, PST etc.
	 * @return The difference from GMT.
	 */
	public static int getDiffFromGMTInMinutes(String timezone){
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
		long offInMili = cal.getTimeZone().getOffset(0);
		return (int) offInMili/(60*1000);
	}
	
	/**
	 * Returns the combined dateStr and deficitTime in milliseconds.
	 * 
	 * @param dateStr 
	 * 					The date string.
	 * @param dateFormat 
	 * 					The date format.
	 * @param deficitTime 
	 * 					The deficit time.
	 * @return
	 * 					The time in milliseconds.
	 * @throws ParseException
	 */
	public static long getDateInMillis(String dateStr, String dateFormat, long deficitTime) throws ParseException{
		long time = getDateInMillis(dateStr, dateFormat);
		return time>0 ? time+deficitTime : time;
	}
	
	/**
	 * 
	 * Sets the log4j log path as as system variable and 
	 * sets the temporary file path for dynamically generated
	 * cucumber feature files and runner classes.
	 * 
	 * @param logFileName 
	 * 						The log file name.
	 * @return
	 * 						The system temporary path.
	 */
	public static String setEnvironmentVariables(String logFileName) {
		String systemTempDir = null;
		
		if(System.getProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_LOG_PATH)==null || 
				System.getProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_TEMP_FILE_PATH_CITF)==null){
			
			systemTempDir = System.getProperty(ICommonConstants.SYSTEM_PROPERTY_TEMP_DIRECTORY);
			if(systemTempDir==null){
				throw new IllegalStateException("System properties not available. Not able run CITF !!!!!");
			}
			systemTempDir = systemTempDir.replaceAll(Matcher.quoteReplacement(ICommonConstants.BACKSLASH), ICommonConstants.SLASH);
			if(!ICommonConstants.SLASH.equals(systemTempDir.substring(systemTempDir.length()-1))){
				systemTempDir = systemTempDir + ICommonConstants.SLASH;
			}
			String currentDate = new SimpleDateFormat("_MM_dd_yyyy_HH_mm").format(Calendar.getInstance().getTime());
			logFileName = StringUtility.appendStrings(System.getProperty(ICommonConstants.SYSTEM_PROPERTY_USER_DIRECTORY), ICommonConstants.DIRECTORY_NAME_TARGET, 
														LOG_SLASH, logFileName, currentDate, DOT_LOG);

			System.setProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_LOG_PATH, logFileName);
			System.out.println(MSG_LOG_FILE_PATH + logFileName);
			
			System.setProperty(ICommonConstants.USER_GENERATED_SYSTEM_PROPERTY_TEMP_FILE_PATH_CITF, systemTempDir + TEMP_FILE_PATH_SUFFIX);
		}
		
		System.setProperty(CUCUMBER_OBJECT_FACTORY_CLASS_KEY, CucumberSpringFactory.class.getName());
		return systemTempDir;
	}
	
	/**
	 * Appends all the error message of a page and returns that as String.
	 * 
	 * @param err 
	 * 				Input errors in the form of PageError object
	 * @return
	 * 				The error message.
	 * @see com.comcast.test.citf.common.ui.router.PageError
	 */
	public static String getPageErrorMessage(PageError err){
		String errRes = null;
		
		if(err.getErrorDescriptions()!=null && err.getErrorDescriptions().size()==1){
			errRes = err.getErrorDescriptions().get(0);
		}
		else if(err.getErrorDescriptions()!=null && err.getErrorDescriptions().size()>1){
			List<String> erStrList = err.getErrorDescriptions();
			int index = 1;
			StringBuilder sbf = new StringBuilder();
			for(String erStr : erStrList){
				if(index<erStrList.size()){
					sbf.append(ICommonConstants.COMMA_SPACE);
				}
				sbf.append(erStr);
				index++;
			}
			errRes = sbf.toString();
		}
		else if(err.getError()!=null){
			errRes = err.getError();
		}
		
		return errRes;
	}
	
	/**
	 * Returns the new date of birth of the user
	 * <p>
	 * The month of DOB for a particular user should be incremented every month to match with SSN.
	 * 
	 * @param dob 
	 * 				The date of birth.
	 * @param createDate 
	 * 				Create date of SSN.
	 * @return
	 * 				The new date of birth to be associated with the SSN.
	 */
	public static String getDateOfBirthForSSN(java.sql.Date dob, java.sql.Date createDate){
		Calendar calCurrent = Calendar.getInstance();
		calCurrent.setTime(new Date());
		
		Calendar calCreateDate = Calendar.getInstance();
		calCreateDate.setTimeInMillis(createDate.getTime());
		
		int monthDifference = (calCurrent.get(Calendar.YEAR) * 12 + calCurrent.get(Calendar.MONTH)) - 
								(calCreateDate.get(Calendar.YEAR) * 12 + calCreateDate.get(Calendar.MONTH));
		
		if(monthDifference>=0){
			Calendar calDob = Calendar.getInstance();
			calDob.setTimeInMillis(dob.getTime());
			SimpleDateFormat formatter = new SimpleDateFormat(ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME);
			
			if(monthDifference>0){
				calDob.add(Calendar.MONTH, monthDifference);
			}
			return formatter.format(calDob.getTime());
		}
		
		return null;
	}
	
	public static String getFormattedDate(String dateFormat, Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }
	
	private static final String DOT_LOG = ".log";
	private static final String LOG_SLASH = "logs/";
	private static final String MSG_LOG_FILE_PATH = "\n\n\n######### Log file path: ";
	private static final String TEMP_FILE_PATH_SUFFIX = "citf-temp/citf/";
	private static final String CUCUMBER_OBJECT_FACTORY_CLASS_KEY = "cucumber.api.java.ObjectFactory";
}
