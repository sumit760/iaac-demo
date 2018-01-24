package com.comcast.test.citf.common.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for String handling.
 * 
 * @author arej001c
 *
 */

public class StringUtility {
	
	/**
	 * Checks whether the input string is null or blank and returns boolean depending on that.
	 * 
	 * @param str
	 * 				The string which has to be checked.
	 * @return
	 * 				true, if the string is empty.
	 * 				false, otherwise.
	 */
	public static boolean isStringEmpty(String str){
		return str == null || ICommonConstants.BLANK_STRING.equals(str.trim());
	}
	
	/**
	 * Checks whether the regular expression is matched with the input string 
	 * and returns the matched expression.
	 * 
	 * @param input 
	 * 				The input string.
	 * @param regex 
	 * 				The regular expression.
	 * @return
	 * 				The matched content.
	 */
	public static String regularExpressionChecker(String input, String regex){
		
		String matchedExpr = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		if(matcher.find()){
			matchedExpr = matcher.group();
		}
		LOGGER.debug("Regular expression checked with input[string: {} and regex: {}] and matched Expression found : {}", 
							input, regex, matchedExpr);
		return matchedExpr;
	}
	
	
	/**
	 * Checks whether the regular expression matched with the input string and returns 
	 * the matched expression from specified element number.
	 * 
	 * @param input 
	 * 					The input string.
	 * @param regex 
	 * 					The regular expression.
	 * @param elementNo 
	 * 					The index of the matched content.
	 * @return
	 * 					The matched content.
	 */
	public static String regularExpressionChecker(String input, String regex, int elementNo){
		
		String matchedExpr = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		
		if(matcher.find()){
			matchedExpr = matcher.group(elementNo);
		}
		LOGGER.debug("Regular expression checked with input[string: {} and regex: {}] and matched Expression found : {}", 
						input, regex, matchedExpr);
		return matchedExpr;
	}
	
	
	/**
	 * Method to remove all name spaces from any xml.
	 * 
	 * @param xml	
	 * 				The input xml.
	 * @param nameSpaceEndChars 
	 * 				Name space characters e.g. \\s, \\>
	 * @return
	 * 				XML with no name space.
	 */
	public static String removeAllNameSpaces(String xml, String... nameSpaceEndChars){
		
		boolean more = false;
		StringBuilder regex = new StringBuilder();
		regex.append(REGEX_NAME_SAPCE_PREFIX);
		
		for(String nseChar : nameSpaceEndChars){
			if(more){
				regex.append(ICommonConstants.PIPE);
			}
			regex.append(nseChar);
			
			if(!more){
				more = true;
			}
		}
		regex.append(REGEX_NAME_SAPCE_SUFFIX);
		String result = xml.replaceAll(regex.toString(), ICommonConstants.BLANK_STRING);
		
		LOGGER.debug("Remove All NameSpaces with input[xml: {} and attributes: {}] and output : {}", 
						xml, Arrays.toString(nameSpaceEndChars), result);
		return result;
	}
	
	/**
	 * Converts a list to a String type array.
	 * 
	 * @param list 
	 * 				The input List of String type.
	 * @return
	 * 				The String type array.
	 */
	public static String[] ListToStringArray(List<String> list){
		String[] result = null;
		
		if(list!=null && !list.isEmpty()){
			result = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				result[i] = list.get(i);
			}
		}
		
		return result;
	}
	
	/**
	 * Splits the input string around matches of given delimiter and 
	 * returns the set computed by splitting the input string.
	 * 
	 * @param input 
	 * 				The input string.
	 * @param delimeter 
	 * 				The token delimiter.
	 * @return
	 * 				The set of tokens.
	 */
	public static Set<String> getTokensFromString(String input, String delimeter){
		Set<String> result = null;
		
		if(input!=null){
			StringTokenizer tokens = new StringTokenizer(input, delimeter);
			
			while(tokens.hasMoreTokens()){
				if(result == null){
					result = new LinkedHashSet<String>();
				}
				result.add(tokens.nextToken());
			}
		}
		
		return result;
	}
	
	public static String getFirstTokenFromString(String input, String delimeter){
		Set<String> tokens = getTokensFromString(input, delimeter);
		return tokens!=null? tokens.iterator().next() : null;
	}
	

	/**
	 * Splits the input string around matches of given delimiter and 
	 * returns the last string computed by splitting the input string.
	 * 
	 * @param input
	 * 				The input string.
	 * @param delimeter
	 * 				The delimiter
	 * @return
	 * 				The last token of the input string.
	 */
	public static String getLastTokenFromString(String input, String delimeter){
		String result = null;
		
		if(input!=null){
			StringTokenizer tokens = new StringTokenizer(input, delimeter);
			while(tokens.hasMoreTokens()){
				result = tokens.nextToken();
			}
		}
		
		return result;
	}
	
	/**
	 * Appends more than one string inputs and returns that.
	 * 
	 * @param inputs 
	 * 					The input strings.
	 * @return
	 * 					The appended string.
	 */
	public static String appendStrings(String... inputs){
		if(inputs==null){
			return null;
		}
		StringBuilder sbf = new StringBuilder(inputs.length);
		for(String input: inputs){
			sbf.append(input);
		}
		return sbf.toString();
	}
	
	/**
	 * Appends more than one Objects and returns that as String.
	 * 
	 * @param inputs 
	 * 				The input objects.
	 * @return
	 * 				The appended objects as string.
	 */
	public static String appendObjects(Object... inputs){
		if(inputs==null){
			return null;
		}
		StringBuilder sbf = new StringBuilder(inputs.length);
		for(Object input: inputs){
			sbf.append(input);
		}
		return sbf.toString();
	}
	
	
	/**
	 * Returns a String derived from the input String by replacing separatorToReplace by changedSeparator.
	 * 
	 * @param separatorToReplace
	 * 							String which has to be replaced. 
	 * @param changedSeparator
	 * 							String which will replace the old one.
	 * @param inputString
	 * 							The input string.
	 * @return
	 * 							The string with the modified separator.
	 */
	public static String replaceSeparatorsInString(String separatorToReplace, String changedSeparator, String inputString){
		String result = null;
		Set<String> phrases = getTokensFromString(inputString, separatorToReplace);
		
		if(phrases!=null){
			StringBuilder sbf = new StringBuilder(phrases.size());
			int index = 0;
			for(String phrase : phrases){
				sbf.append(phrase);
				
				if(index<phrases.size()-1){
					sbf.append(changedSeparator);
				}
				index++;
			}
			
			if(sbf.length()>0){
				result = sbf.toString();
			}
		}
		
		return result;
	}
	
	private static final String REGEX_NAME_SAPCE_PREFIX = "xmlns[^";
	private static final String REGEX_NAME_SAPCE_SUFFIX = "]+";
	
	private static Logger LOGGER = LoggerFactory.getLogger(StringUtility.class);
}
