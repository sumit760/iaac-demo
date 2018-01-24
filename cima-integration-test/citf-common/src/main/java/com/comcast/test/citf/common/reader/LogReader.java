package com.comcast.test.citf.common.reader;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Properties;
import java.util.Stack;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Utility class for extracting application logs. This supports
 * two ways of extracting logs as
 * 
 * 1. Extracting from application log files in server.
 * 2. Extracting from Splunk.
 * 
 * @author arej001c, spal004c
 *
 */
@Service("logReader")
public class LogReader {

	public static final int DEFAULT_FILE_PORT = 22;
	public static final int DEFAULT_SPLUNK_PORT = 8089;

	private static final long DEFAULT_LATENCY_TIME_IN_MILLIS = 60000;
	private static final int DEFAULT_LATENCY_TIME_IN_MINUTES = 1;

	private static final int DEFAULT_MAX_LINE_BUFFER = 500;
	private static final long DEFAULT_NETWORK_CONN_WAITING_TIME = 1000;

	public static final String LOG_READER_SPLUNK = "SPLUNK";
	public static final String LOG_READER_FILE = "FILE";

	/**
	 * Enum RegexArgument to be used 
	 *
	 */
	public enum RegexArgument{
		PHONE_NUMBER("$PHONE_NUMBER"),
		EMAIL("$EMAIL"),
		EARLIEST("$EARLIEST"),
		ENVIRONMENT("$ENVIRONMENT"),
		DOMAIN("$DOMAIN");

		private final String value;
        RegexArgument(final String value) {
            this.value = value;
        }

        public String getValue(){
        	return this.value;
        }
	}

	/**
	 * Reads from a file and returns the content of the file.
	 * 
	 * @param remoteFile
	 * 						The file name.
	 * @param host 
	 * 						Host name of the remote server.
	 * @param port 
	 * 						Port of the remote server.
	 * @param user 
	 * 						User id for remote server.
	 * @param password 
	 * 						Password for remote server.
	 * @param regex
	 * 						The regular expression to apply on the log content.
	 * @param privateKeyPath
	 * 						Server .pk file for SSH authentication.
	 * @return
	 * 						Matched content from the log.
	 */
	public String readFromFile(	String remoteFile,
								String host,
								int port,
								String user,
								String password,
								String regex,
								String privateKeyPath){
		if(port<=0) {
			port = DEFAULT_FILE_PORT;
		}

		return readFile(remoteFile, host, port, user, password, regex, privateKeyPath, DEFAULT_LATENCY_TIME_IN_MILLIS, DEFAULT_MAX_LINE_BUFFER);
	}

	/**
	 * Queries Splunk and return the result.
	 * 
	 * @param host 
	 * 					Host name of the splunk server.
	 * @param port 
	 * 					Port of splunk server.
	 * @param user 
	 * 					Userid for splunk server.
	 * @param password 
	 * 					Password for splunk server.
	 * @param query 
	 * 					The specified Splunk query.
	 * @param searchKey 
	 * 					The key element searched for in the query as an example sms code ,url link etc.
	 * @param splunkTimeZone
	 * 					Splunk time zone.
	 * @return
	 * 					Matched content from Splunk.
	 */
	public String readFromSplunk(	String host,
									int port,
									String user,
									String password,
									String query,
									String searchKey,
									String splunkTimeZone){

		LOGGER.info("Starting to read splunk log from {}:{} using {} user+ query {} and timezone {}. It'll search for key {} with max latency time " + DEFAULT_LATENCY_TIME_IN_MINUTES + " min.",
		            host, port, user, query, splunkTimeZone, searchKey);

		com.splunk.Service service = splunk.initializeSplunk(host, port, user, password, ICommonConstants.SPLUNK_SCHEME_HTTPS);
		int earliest = 0 - DEFAULT_LATENCY_TIME_IN_MINUTES;

		if(splunkTimeZone!=null && !SplunkReader.SPLUNK_SERVER_DEFAULT_TIMEZONE.equalsIgnoreCase(splunkTimeZone.toUpperCase().trim())) {
			earliest = MiscUtility.getDiffFromGMTInMinutes(splunkTimeZone) + earliest;
		}

		query = query.replace(RegexArgument.EARLIEST.getValue(), String.valueOf(earliest) + SplunkReader.SPLUNK_TIME_SUFFIX_MINUTE);
		LOGGER.debug("Final query for searching in splunk : {}", query);

		return splunk.read(service, query, searchKey);
	}


	/**
	 * Reads log file from server
	 * 
	 * @param remoteFile
	 * 			File path
	 * @param host
	 * 			Host name of the log server.
	 * @param port
	 * 			Port of log server.
	 * @param user
	 * 			Userid for log server.
	 * @param password
	 * 			Password for log server.
	 * @param regex
	 * 			Regular expression to fetch the required value 
	 * @param privateKeyPath
	 * 			Path of private key
	 * @param latencyTimeInMillis
	 * 			Latency time to read log
	 * @param maxLineBuffer
	 * 			Maximum number of lines to fetch at a time
	 * @return value to be fetched
	 */
	private String readFile(	String remoteFile,
								String host,
								int port,
								String user,
								String password,
								String regex,
								String privateKeyPath,
								long latencyTimeInMillis,
								int maxLineBuffer){
	  String result = null;
	  Session session = null;
	  boolean isCheckNeeded = true;
	  Channel channel = null;
	  deficitTimeToAdd = 0;

		LOGGER.info("Starting to read {} file from {}:{} using {} user and regex {}. It'll search for regular expression {} with max latency time {}",
		            remoteFile, host, port, user, regex, regex, latencyTimeInMillis / (60 * 1000));

	  try{
		  if(StringUtility.isStringEmpty(remoteFile) || StringUtility.isStringEmpty(regex)) {
			  throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		  }

		  session = getSession(host, port, user, password, privateKeyPath);
		  if(session==null) {
			  throw new IllegalStateException("Session cannot be created");
		  }

		  String command = new MessageFormat("tail -{1}l {2}").format(new String[] {String.valueOf(maxLineBuffer), remoteFile});
		  while(isCheckNeeded){
			  ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 channel = getChannel(session, baos);
			 if(channel==null) {
				 throw new IllegalStateException("Channel cannot be opened");
			 }

			 ((ChannelExec)channel).setCommand(command);
			 channel.connect();
			 Thread.sleep(DEFAULT_NETWORK_CONN_WAITING_TIME);
			 Stack<String> lines = splitInLines(new String(baos.toByteArray(), StandardCharsets.UTF_8));

			 LOGGER.debug("Fetched log content: {}", lines);
			 LOGGER.info("Fetched log size: {}", lines.size());

			 while(!lines.isEmpty()) {
				 String res = checkLine(lines.pop(), regex, latencyTimeInMillis);
				 LOGGER.debug("In loop[{}] result found : {}", 500 - lines.size(), res);

				 if (res != null && !CHECK_RESULT_NOT_MATCHED.equals(res)) {
					 if (!CHECK_RESULT_LATENCY_TIME_OVER.equals(res)) {
						 result = res;
					 }

					 isCheckNeeded = false;
					 break;
				 }
			 }

			 if(!channel.isClosed()) {
				 channel.disconnect();
			 }
		  }

	  }catch(Exception e){
		  LOGGER.error("Error occurred while reading log file", e);
		  result = null;
	  }
	  finally{
		  if(channel!=null && !channel.isClosed()) {
			  channel.disconnect();
		  }
		  if(session!=null) {
			  session.disconnect();
		  }
	  }

	  LOGGER.info("Log reading is over with result: {}", result);
	  return result;
	}

	/**
	 * Returns Java secure channel session.
	 * 
	 * @param host 
	 * 					Host name of the server.
	 * @param port
	 * 					Port of the server.
	 * @param user
	 * 					Username to login to server.
	 * @param password
	 * 					Password to login to server.
	 * 					No password is required if the authentication is done
	 * 					through SSH authentication via a .pk file.
	 * @param privateKeyPath
	 * 					The server .pk file.
	 * @return
	 * 					The Java secure channel session.
	 * @throws JSchException
	 */
	private Session getSession(String host, int port, String user, String password, String privateKeyPath) throws JSchException{
		if(StringUtility.isStringEmpty(host) || StringUtility.isStringEmpty(user) || (StringUtility.isStringEmpty(privateKeyPath) && StringUtility.isStringEmpty(password)))
			  throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);

		JSch jsch = new JSch();
		if(!StringUtility.isStringEmpty(privateKeyPath))
		  jsch.addIdentity(privateKeyPath);

	    Session session = jsch.getSession(user, host, port);
	    LOGGER.debug("Session created.");

	    if(password!=null)
	    	session.setPassword(password);

	    Properties config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
	    session.connect();
		LOGGER.debug("Connection established with {} server.", host);

	    return session;
	}

	/**
	 * Returns a java secure channel.
	 * 
	 * @param session
	 * 					The java secure channel session.
	 * @param os 
	 * 					The outputStream to set.
	 * @return
	 * 					The java secure channel session.
	 * @throws JSchException
	 */
	private Channel getChannel(Session session, OutputStream os) throws JSchException{
		Channel channel = session.openChannel( "exec" );
		channel.setInputStream( null );
		channel.setOutputStream( os );

		return channel;
	}


	/*********************************************** Private **************************************************/

	private long deficitTimeToAdd = 0;

	@Autowired
	@Qualifier("splunkReader")
	private SplunkReader splunk;

	/**
	 * Utility method to split string in lines
	 * 
	 * @param input
	 * 			Input line to split
	 * @return Stack of split strings
	 */
	private Stack<String> splitInLines(String input) {
		final Stack<String> resultList = new Stack<>();
		if(StringUtility.isStringEmpty(input)) {
			return resultList;
		}

		String line;
		String minus_one_line = null;
		String minus_two_line = null;

		StringTokenizer tokens = new StringTokenizer(input, ICommonConstants.NEW_LINE);
		while(tokens.hasMoreTokens()){
			line = tokens.nextToken();
			if(StringUtility.isStringEmpty(line))
				continue;

			StringBuilder linesToCheck = new StringBuilder(5);
			if(minus_two_line!=null){
				linesToCheck.append(minus_two_line);
				linesToCheck.append(ICommonConstants.NEW_LINE);
			}
			if(minus_one_line!=null){
				 linesToCheck.append(minus_one_line);
				 linesToCheck.append(ICommonConstants.NEW_LINE);
				 minus_two_line = minus_one_line;
			}
			linesToCheck.append(line);
			minus_one_line = line;
			resultList.push(linesToCheck.toString());
		}
		return resultList;
	}

	/**
	 * Utility method to check line
	 * 
	 * @param input
	 * 			Line string to check
	 * @param regex
	 * 			Regular expression
	 * @param latencyTime
	 * 			Maximum log time allowed 
	 * @return result
	 */
	private String checkLine(String input, String regex, long latencyTime){
		LOGGER.debug("Starting to check LINE {} with REGEX {}", input, regex);
		String result = CHECK_RESULT_NOT_MATCHED;

		if(input == null || input.length() < 20) {
			return null;
		}

		try{
			String dateStr = input.substring(0, 19);
			LOGGER.debug("Extracted date string: {}", dateStr);

			if(deficitTimeToAdd == 0){
				deficitTimeToAdd = MiscUtility.getTimeZoneDifferenceInMillis(dateStr, ICommonConstants.DATE_FORMAT_DETAILED_FORMAT1, true, 1);
				LOGGER.info("Deficit Time calculated(in hr.): {}", deficitTimeToAdd / 60 * 60 * 1000);
			}

			if(MiscUtility.isExpired(MiscUtility.getDateInMillis(dateStr, ICommonConstants.DATE_FORMAT_DETAILED_FORMAT1, deficitTimeToAdd), latencyTime))
				return CHECK_RESULT_LATENCY_TIME_OVER;
		}catch(ParseException pe){
			LOGGER.error("Parsing Exception occurred while checking lines", pe);
		}
		catch(Exception e){
			LOGGER.error("Exception occurred while checking lines", e);
		}
		result = StringUtility.regularExpressionChecker(input, regex, 1);
		return result;
	}

	private static final String CHECK_RESULT_LATENCY_TIME_OVER = "CR_LATENCY_TIME_OVER";
	private static final String CHECK_RESULT_NOT_MATCHED = "CR_NOT_MATCHED";

	private static final Logger LOGGER = LoggerFactory.getLogger(LogReader.class);
}