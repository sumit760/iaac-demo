package com.comcast.test.citf.common.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;


/**
 * Class for XML Parsing. This class parses any XML string and returns a predefined map of objects.
 * 
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 *
 */
public class XMLParserHelper {
	
	/**
	 * Enum for object types to be used to call 'getXMLValueMap' method to 
	 * select the type of XML Parsing operation.
	 * 
	 * @author arej001c
	 *
	 */
	public enum XmlParsingObjectTypes{
		CIMA_SERVICE_ACCESS_TOKEN_RESPONSE,
		CIMA_LOGIN_TOKEN_RESPONSE,
		CIMA_LOGIN_TOKEN_SAML_ASSERTION
	}
	
	/**
	 * Enum for the key list for fetching Map values which have been generated after parsing the xml.
	 * 
	 * @author arej001c
	 *
	 */
	public enum ParsedMapKeys{
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_AUTH_TOKEN,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN_SECRET,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_ERR_MSG,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_SESSION,
		KEY_CIMA_SRVC_ACS_TKN_RSPNS_USERID,
		
		KEY_CIMA_LGN_TKN_RSPNS_LOGIN_TOKEN,
		KEY_CIMA_LGN_TKN_RSPNS_STATUS,
		KEY_CIMA_LGN_TKN_RSPNS_ERR_MSG,
		
		KEY_CIMA_LGN_TKN_SAML_SIGNATURE_INFO,
		KEY_CIMA_LGN_TKN_SAML_FIRST_NAME,
		KEY_CIMA_LGN_TKN_SAML_LAST_NAME,
		KEY_CIMA_LGN_TKN_SAML_EMAIL,
		KEY_CIMA_LGN_TKN_SAML_CID,
		KEY_CIMA_LGN_TKN_SAML_COS,
		KEY_CIMA_LGN_TKN_SAML_APP_KEY,
		KEY_CIMA_LGN_TKN_SAML_SUBJECT_ID
	}
	
	
	/**
	 * Method to parse a predefined XML string. 
	 * It returns a Map of all key values fetched from the XML during parsing.
	 * 
	 * @param xmlString The XML string to parse.
	 * @param objectType The parsing object type.
	 * 
	 * @return The map of parsed tokens and their values.
	 */
	public static Map<XMLParserHelper.ParsedMapKeys, Object> getXMLValueMap(String xmlString, XMLParserHelper.XmlParsingObjectTypes objectType) {
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedXmlMap = null;
		
		try{
			if(StringUtility.isStringEmpty(xmlString)){
				throw new IllegalArgumentException("Input XML String is empty");
			}

			switch(objectType){
			case CIMA_SERVICE_ACCESS_TOKEN_RESPONSE:
				//For success case
				if(xmlString.contains(AUTH_TOKEN)){		//Type 1
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_BLANK_SPACE, ICommonConstants.REGEX_LESS_THAN_TAG);
					xmlString = xmlString.replaceAll(CIMA_COLON, ICommonConstants.BLANK_STRING);
					com.comcast.test.citf.common.cima.satrXmlSuccessType1.AccessTokenResponse response = 
							(com.comcast.test.citf.common.cima.satrXmlSuccessType1.AccessTokenResponse)parseXML(xmlString, com.comcast.test.citf.common.cima.satrXmlSuccessType1.AccessTokenResponse.class, CIMA_SERVICE_ACCESS_TOKEN_RESPONSE_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getServiceToken()!=null && response.getServiceToken().getAuthResponse()!=null) {
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_AUTH_TOKEN, response.getServiceToken().getAuthResponse().getAuthToken());
						}
						if(response.getStatus()!=null) {
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS, response.getStatus().getCode());
						}
					}
				}
				else if(xmlString.contains(TOKEN)){		//Type 2
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_BLANK_SPACE, ICommonConstants.REGEX_LESS_THAN_TAG);
					xmlString = xmlString.replaceAll(CIMA_COLON, ICommonConstants.BLANK_STRING);
					com.comcast.test.citf.common.cima.satrXmlSuccessType2.AccessTokenResponse response = 
							(com.comcast.test.citf.common.cima.satrXmlSuccessType2.AccessTokenResponse)parseXML(xmlString, com.comcast.test.citf.common.cima.satrXmlSuccessType2.AccessTokenResponse.class, CIMA_SERVICE_ACCESS_TOKEN_RESPONSE_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getServiceToken()!=null && response.getServiceToken().getAccessToken()!=null 
								&& response.getServiceToken().getAccessToken().getToken()!=null && response.getServiceToken().getAccessToken().getTokenSecret()!=null){
							String token = response.getServiceToken().getAccessToken().getToken();
							String tokenSecret = response.getServiceToken().getAccessToken().getTokenSecret();

							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN, (token.contains(CDATA_PREFIX) ? token.substring(9, token.length()-3) : token));
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN_SECRET, (tokenSecret.contains(CDATA_PREFIX) ? tokenSecret.substring(9, tokenSecret.length()-3) : tokenSecret));
						}

						if(response.getStatus()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS, response.getStatus().getCode());
						}
					}
				}
				else if(xmlString.contains(SESSION) || xmlString.contains(USERID)){		//Type 3 (e.g. for PLAXO)
					String session = null;
					String userId = null;
					String status = null;
					String temp = StringUtility.regularExpressionChecker(xmlString, REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_SESSION);
					if(temp!=null && temp.length()>19){
						session = temp.substring(9, temp.length()-10);
					}
					temp = StringUtility.regularExpressionChecker(xmlString, REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_USERID);
					if(temp!=null && temp.length()>17){
						userId = temp.substring(8, temp.length()-9);
					}
					temp = StringUtility.regularExpressionChecker(xmlString, REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_STATUS);
					if(temp!=null && temp.length()>90){
						status = temp.substring(87, temp.length()-3);
					}
					if(session!=null || userId!=null || status!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();

						if(session!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_SESSION, session);
						}
						if(userId!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_USERID, userId);
						}
						if(status!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS, status);
						}
					}

				}

				//For failed case
				else{
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_BLANK_SPACE);
					xmlString = xmlString.replaceAll(CIMA_COLON, ICommonConstants.BLANK_STRING);
					com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError response = 
							(com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError)parseXML(xmlString, com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError.class, CIMA_SERVICE_ACCESS_TOKEN_RESPONSE_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getStatus()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS, response.getStatus().getCode());
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_ERR_MSG, response.getStatus().getMessage());
						}
					}
				}

				break;

			case CIMA_LOGIN_TOKEN_RESPONSE:
				//For success case
				if(xmlString.contains(LOGIN_TOKEN)){
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_LESS_THAN_TAG);
					com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse response = 
							(com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse)parseXML(xmlString, com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse.class, CIMA_LOGIN_TOKEN_RESPONSE_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getLoginToken()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_LOGIN_TOKEN, response.getLoginToken().getValue());
						}
						if(response.getStatus()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_STATUS, response.getStatus().getCode());
						}
					}
				}

				//For failed case
				else{
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_LESS_THAN_TAG);
					xmlString = xmlString.replaceAll(CIMA_COLON, ICommonConstants.BLANK_STRING);
					com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError response = 
							(com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError)parseXML(xmlString, com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError.class, CIMA_LOGIN_TOKEN_RESPONSE_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getStatus()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_STATUS, response.getStatus().getCode());
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_ERR_MSG, response.getStatus().getMessage());
						}
					}
				}

				break;

			case CIMA_LOGIN_TOKEN_SAML_ASSERTION:
				//For success case
				if(xmlString.contains("SignatureValue")){
					xmlString = StringUtility.removeAllNameSpaces(xmlString, ICommonConstants.REGEX_BLANK_SPACE, ICommonConstants.REGEX_LESS_THAN_TAG);
					xmlString = xmlString.replaceAll(SAML_COLON_START, ICommonConstants.LESSER_THAN);
					xmlString = xmlString.replaceAll(SAML_COLON_END, ICommonConstants.XML_TAG_CLOSE);
					xmlString = xmlString.replaceAll(DS_COLON_START, ICommonConstants.LESSER_THAN);
					xmlString = xmlString.replaceAll(DS_COLON_END, ICommonConstants.XML_TAG_CLOSE);
					xmlString = xmlString.replaceAll(XI_COLON, ICommonConstants.BLANK_STRING);

					com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion response = 
							(com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion)parseXML(xmlString, com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion.class, CIMA_LOGIN_TOKEN_SAML_ASSERTION_ROOT_NODE);

					if(response!=null){
						parsedXmlMap = new HashMap<XMLParserHelper.ParsedMapKeys, Object>();
						if(response.getSignature()!=null){
							parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_SIGNATURE_INFO, response.getSignature().getSignatureValue());
						}
						if(response.getAttributeStatement()!=null){
							for(com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion.AttributeStatement.Attribute attribute : response.getAttributeStatement().getAttribute()){
								if(attribute!=null && attribute.getAttributeValue()!=null && !attribute.getAttributeValue().isEmpty() && attribute.getAttributeValue().get(0)!=null){

									if(CIMA_LGN_TKN_SAML_FIRST_NAME.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_FIRST_NAME, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_LAST_NAME.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_LAST_NAME, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_EMAIL.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_EMAIL, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_APP_KEY.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_APP_KEY, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_CID.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_CID, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_SUB_ID.equals(attribute.getName())){
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_SUBJECT_ID, attribute.getAttributeValue().get(0).getValue());
									}
									if(CIMA_LGN_TKN_SAML_COS.equals(attribute.getName())){

										List<String> cosList =  new ArrayList<String>();
										for(com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion.AttributeStatement.Attribute.AttributeValue attrVal : attribute.getAttributeValue()){
											cosList.add(attrVal.getValue());
										}
										parsedXmlMap.put(ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_COS, cosList);
									}

								}
							}
						}
					}
				}					
			}
		}catch(JAXBException e){
			LOGGER.error("Error occurred while prasing XML data: ", e);
		}
		LOGGER.debug("XML parsing done for input [xml string :{}, parse type: {}] with output map : {}", xmlString, objectType, parsedXmlMap);
		return parsedXmlMap;
	}
	
	
	
	
	/*********************************** Private methods **********************************************************/
	
	private static Object parseXML(String xmlString, Class<?> ObjectClassName, String rootNode) throws JAXBException {
		
		Object parsedXmlObj = null;
		JAXBContext jaxbContext = null;
		Unmarshaller jaxbUnmarshaller;

		if(ObjectClassName == com.comcast.test.citf.common.cima.satrXmlSuccessType1.AccessTokenResponse.class)	{
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.satrXmlSuccessType1.AccessTokenResponse.class);
		}
		else if(ObjectClassName == com.comcast.test.citf.common.cima.satrXmlSuccessType2.AccessTokenResponse.class)	{
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.satrXmlSuccessType2.AccessTokenResponse.class);
		}
		else if(ObjectClassName == com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError.class)	{
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.satrXmlFail.AccessTokenResponseError.class);
		}
		else if(ObjectClassName == com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse.class)	{
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.ltrXmlSuccess.AuthnResponse.class);
		}
		else if(ObjectClassName == com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError.class) { 
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.ltrXmlFail.AuthnResponseError.class);
		}
		else if(ObjectClassName == com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion.class) {	
			jaxbContext = JAXBContext.newInstance(com.comcast.test.citf.common.cima.ltsaXmlSuccess.Assertion.class);
		}	
		
		//Unmarshalling
		if(jaxbContext!=null){
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			parsedXmlObj = jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
		}
		return parsedXmlObj;
	}


	private static final String CIMA_SERVICE_ACCESS_TOKEN_RESPONSE_ROOT_NODE = "AccessTokenResponse";
	private static final String CIMA_LOGIN_TOKEN_RESPONSE_ROOT_NODE = "AuthnResponse";
	private static final String CIMA_LOGIN_TOKEN_SAML_ASSERTION_ROOT_NODE = "Assertion";
	private static final String AUTH_TOKEN = "<authToken>";
	private static final String TOKEN = "<token>";
	private static final String SESSION = "<session>";
	private static final String USERID = "<userId>";
	private static final String CIMA_COLON = "cima:";
	private static final String LOGIN_TOKEN = "LoginToken";
	private static final String SAML_COLON_START = "<saml:";
	private static final String SAML_COLON_END = "</saml:";
	private static final String DS_COLON_START = "<ds:";
	private static final String DS_COLON_END = "</ds:";
	private static final String XI_COLON = "xi:";
	private static final String CIMA_LGN_TKN_SAML_FIRST_NAME = "firstname";
	private static final String CIMA_LGN_TKN_SAML_LAST_NAME = "lastname";
	private static final String CIMA_LGN_TKN_SAML_EMAIL = "emailAddress";
	private static final String CIMA_LGN_TKN_SAML_APP_KEY = "appKey";
	private static final String CIMA_LGN_TKN_SAML_CID = "cid";
	private static final String CIMA_LGN_TKN_SAML_SUB_ID = "subject-id";
	private static final String CIMA_LGN_TKN_SAML_COS = "cos";
	private static final String CDATA_PREFIX = "<![CDATA[";

	private static final String REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_SESSION = "<session>.*?</session>";
	private static final String REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_USERID = "<userId>.*?</userId>";
	private static final String REGEX_SERVICE_ACCESS_TOKEN_SUCCESS_RESPONSE_TYPE3_STATUS = "<Status xmlns=\"urn:comcast:login:api:v1.0\" Code=\".*?/>";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLParserHelper.class);
}
