package com.comcast.cima.test.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;


/**
* @author Abhijit Rej (arej001c)
* @since September 2015
* 
*/
public class TveXmlParser{

	public static enum XmlType{
		SAML_RESPONSE,
		OAUTH_RESPONSE,
	}
	
	public static enum ResultMapKeys{
		DECISION,
		TV_RATING,
		MOVIE_RATING,
		STATUS,
		REASON,
		REASON_CODE
	}
	
	public static Map<ResultMapKeys, String> parse(XmlType type, String xml){
		Map<ResultMapKeys, String> resultMap = null; 
		logger.info(StringUtility.appendObjects("Start parsing xml response for ", (type!=null)? type.name() : "null", " xml with input ", xml));
		String decision = null;
		String temp = null;
		
		try{
			if(type == null || StringUtility.isStringEmpty(xml))
				throw new Exception(ICommonConstants.EXCEPTION_INVALID_INPUT);
			
			resultMap =  new HashMap<TveXmlParser.ResultMapKeys, String>();
			if(XmlType.SAML_RESPONSE.equals(type) || XmlType.OAUTH_RESPONSE.equals(type)){
				
				if(XmlType.SAML_RESPONSE.equals(type)){
					temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_STATUS);
					if(temp!=null && temp.length()>116)
						resultMap.put(ResultMapKeys.STATUS, temp.substring(61, temp.length()-55));
				}
				else{
					temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_OAUTH_STATUS);
					if(temp!=null && temp.length()>121)
						resultMap.put(ResultMapKeys.STATUS, temp.substring(59, temp.length()-62));
				}
				
				
				temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_OAUTH_DECISION);
				if(temp!=null && temp.length()>90){
					decision = temp.substring(73, temp.length()-15);
					resultMap.put(ResultMapKeys.DECISION, decision);
						
					if(DECISION_PERMIT.equalsIgnoreCase(decision)){
						temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_OAUTH_MOVIE_RATING);
						if(temp!=null && temp.length()>231)
							resultMap.put(ResultMapKeys.MOVIE_RATING, temp.substring(205, temp.length()-26));
							
						temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_OAUTH_TV_RATING);
						if(temp!=null && temp.length()>232)
							resultMap.put(ResultMapKeys.TV_RATING, temp.substring(206, temp.length()-26));
					}
					else{
						temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_OAUTH_REASON);
						if(temp!=null && temp.length()>235)
							resultMap.put(ResultMapKeys.REASON, temp.substring(209, temp.length()-26));
						
						temp = StringUtility.regularExpressionChecker(xml, REGEX_RESPONSE_SAML_OAUTH_REASON_CODE);
						if(temp!=null && temp.length()>251)
							resultMap.put(ResultMapKeys.REASON_CODE, temp.substring(225, temp.length()-26));
					}
				}
			}
			
			if(resultMap.isEmpty())
				resultMap = null;
		}
		catch(Exception e){
			logger.error("Error occured while parsing XML response : "+MiscUtility.getStackTrace(e));
		}
		
		logger.info("Exit parsing XML response with result "+resultMap);
		return resultMap;
	}
	
	
	
	public static Map<String, Map<ResultMapKeys, String>> parseMultiChannel(String xml, List<String> channels){
		Map<String, Map<ResultMapKeys, String>> resultMap = null; 
		logger.info(StringUtility.appendObjects("Start parsing Multi Channel xml response for ", channels, " channels with input ", xml));
		String decision = null;
		String temp = null;
		
		try{
			if(StringUtility.isStringEmpty(xml) || channels == null)
				throw new Exception(ICommonConstants.EXCEPTION_INVALID_INPUT);
			
			for(String channel : channels){
				if(!StringUtility.isStringEmpty(channel)){
					String resultEle = StringUtility.regularExpressionChecker(xml, StringUtility.appendStrings(REGEX_RESPONSE_OAUTH_MULTICHANNEL_RESULT_START_PART1, channel, REGEX_RESPONSE_OAUTH_MULTICHANNEL_RESULT_START_PART2));
					if(resultEle!=null){
						Map<ResultMapKeys, String> map = new HashMap<ResultMapKeys, String>();
						temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_OAUTH_STATUS);
						if(temp!=null && temp.length()>121)
							map.put(ResultMapKeys.STATUS, temp.substring(59, temp.length()-62));
						
						temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_SAML_OAUTH_DECISION);
						if(temp!=null && temp.length()>90){
							decision = temp.substring(73, temp.length()-15);
							map.put(ResultMapKeys.DECISION, decision);
								
							if(DECISION_PERMIT.equalsIgnoreCase(decision)){
								temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_SAML_OAUTH_MOVIE_RATING);
								if(temp!=null && temp.length()>231)
									map.put(ResultMapKeys.MOVIE_RATING, temp.substring(205, temp.length()-26));
									
								temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_SAML_OAUTH_TV_RATING);
								if(temp!=null && temp.length()>232)
									map.put(ResultMapKeys.TV_RATING, temp.substring(206, temp.length()-26));
							}
							else{
								temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_SAML_OAUTH_REASON);
								if(temp!=null && temp.length()>235)
									map.put(ResultMapKeys.REASON, temp.substring(209, temp.length()-26));
								
								temp = StringUtility.regularExpressionChecker(resultEle, REGEX_RESPONSE_SAML_OAUTH_REASON_CODE);
								if(temp!=null && temp.length()>251)
									map.put(ResultMapKeys.REASON_CODE, temp.substring(225, temp.length()-26));
							}
						}
						
						if(!map.isEmpty()){
							if(resultMap == null)
								resultMap = new HashMap<String, Map<ResultMapKeys,String>>();
							
							resultMap.put(channel, map);
						}
					}
				}
			}
		}
		catch(Exception e){
			logger.error("Error occured while parsing MultiChannel XML response : "+MiscUtility.getStackTrace(e));
		}
		
		logger.info("Exit parsing MultiChannel XML response with result "+resultMap);
		return resultMap;
	}
	
	
	private static final String REGEX_RESPONSE_SAML_OAUTH_DECISION = "<ns3:Decision xmlns:ns3=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\">.*?</ns3:Decision>";
	private static final String REGEX_RESPONSE_SAML_OAUTH_MOVIE_RATING = "<ns2:AttributeAssignment AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:ratings:maxMPAA\" DataType=\"http://www.w3.org/2001/XMLSchema#string\" xmlns:ns2=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\">.*?</ns2:AttributeAssignment>";
	private static final String REGEX_RESPONSE_SAML_OAUTH_TV_RATING = "<ns2:AttributeAssignment AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:ratings:maxVCHIP\" DataType=\"http://www.w3.org/2001/XMLSchema#string\" xmlns:ns2=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\">.*?</ns2:AttributeAssignment>";
	private static final String REGEX_RESPONSE_SAML_OAUTH_REASON = "<ns2:AttributeAssignment AttributeId=\"urn:cablelabs:ocla:1.0:attribute:enviroment:buy-flow:message\" DataType=\"http://www.w3.org/2001/XMLSchema#string\" xmlns:ns2=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\">.*?</ns2:AttributeAssignment>";
	private static final String REGEX_RESPONSE_SAML_OAUTH_REASON_CODE = "<ns2:AttributeAssignment AttributeId=\"urn:cablelabs:olca:1.0:attribute:content:subscription:tv-network:reason-code\" DataType=\"http://www.w3.org/2001/XMLSchema#string\" xmlns:ns2=\"urn:oasis:names:tc:xacml:2.0:policy:schema:os\">.*?</ns2:AttributeAssignment>";
	private static final String REGEX_RESPONSE_SAML_STATUS = "<saml2p:StatusCode Value=\"urn:oasis:names:tc:SAML:2.0:status:.*?\" xmlns:saml2p=\"urn:oasis:names:tc:SAML:2.0:protocol\"/>";
	private static final String REGEX_RESPONSE_OAUTH_STATUS = "<ns3:StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:.*?\" xmlns:ns3=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\"/>";
	
	private static final String REGEX_RESPONSE_OAUTH_MULTICHANNEL_RESULT_START_PART1 = "<ns3:Result ResourceId=\"";
	private static final String REGEX_RESPONSE_OAUTH_MULTICHANNEL_RESULT_START_PART2 = "\" xmlns:ns3=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\">.*?</ns2:Obligations>";
	
	
	private static final String DECISION_PERMIT = "Permit";
	
	private static Logger logger = LoggerFactory.getLogger(TveXmlParser.class);
}
