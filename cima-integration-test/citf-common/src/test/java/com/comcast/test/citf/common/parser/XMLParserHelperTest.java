package com.comcast.test.citf.common.parser;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;

public class XMLParserHelperTest {
	
	@Test
	public void testGetXMLValueMapInvalidInput() {
		
		try {
			XMLParserHelper.
            			   getXMLValueMap("", XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);
			
		} catch(Exception e) {
			assertThat(
					e.getMessage(), 
					containsString("Input XML String is empty"));
		}
		
	}
	
	
	@Test
	public void testGetXMLValueMapForZimbraResponse() {
		
		String stringToParse = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
							   + "<ServiceToken xmlns=\"urn:comcast:login:api:v1.0\">"
							   + "<AuthResponse xmlns=\"urn:zimbraAccount\">"
							   + "<authToken>0_e10efc5653d363a7a696d6272613b</authToken>"
							   + "<lifetime>86399802</lifetime>"
							   + "<skin>serenity</skin>"
							   + "</AuthResponse>"
							   + "</ServiceToken>"
							   + "<Status xmlns=\"urn:comcast:login:api:v1.0\" "
							   + "Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/>"
							   + "</cima:AccessTokenResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
                getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);

		assertThat(
				parsedMap, notNullValue());

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_AUTH_TOKEN), 
					is("0_e10efc5653d363a7a696d6272613b"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS), 
				is("urn:comcast:login:api:rest:1.0:status:Success"));

	}

	
	@Test
	public void testGetXMLValueMapForMoxieResponse() {
		
		String stringToParse = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
					+ "<ServiceToken xmlns=\"urn:comcast:login:api:v1.0\" "
					+ "ExpiresOn=\"2015-09-24T16:03:49.774Z\">"
					+ "<AccessToken xmlns=\"urn:comcast:login:api:oauth:1.0\">"
					+ "<token><![CDATA[U1ZDAQECAIBPH47Ml]]>"
					+ "</token>"
					+ "<token_secret><![CDATA[zX5QHz7t5ayJuMDZQ45iNvLPRlM=]]></token_secret>"
					+ "</AccessToken>"
					+ "</ServiceToken><Status xmlns=\"urn:comcast:login:api:v1.0\" Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/>"
					+ "</cima:AccessTokenResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
                getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);

		assertThat(
				parsedMap, notNullValue());

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN), 
					is("U1ZDAQECAIBPH47Ml"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_TOKEN_SECRET), 
				is("zX5QHz7t5ayJuMDZQ45iNvLPRlM="));
		
		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS), 
				is("urn:comcast:login:api:rest:1.0:status:Success"));

	}

	
	@Test
	public void testGetXMLValueMapForPlaxoResponse() {
		
		String stringToParse = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
							 + "<cima:ServiceToken>"
				             + "<ns1:GetSessionResponse xmlns:ns1=\"Plaxo\"><response>"
				             + "<code>200</code>"
				             + "<message>OK</message>"
				             + "<session>IApJRecZACKdDsY</session>"
				             + "<userId>244815829419</userId>"
				             + "</response>"
				             + "</ns1:GetSessionResponse>"
				             + "</cima:ServiceToken>"
				             + "<Status xmlns=\"urn:comcast:login:api:v1.0\" Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/>"
				             + "</cima:AccessTokenResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
				                                               getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);
		
		assertThat(
				"Expected valid response",
				parsedMap, notNullValue());
		
		assertThat(
				"Expected valid userId",
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_USERID), is("244815829419"));
		
		assertThat(
				"Expected valid status",
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_STATUS), is("Success"));
		
		assertThat(
				"Expected valid session",
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_SESSION), is("IApJRecZACKdDsY"));
	}

	
	@Test
	public void testGetXMLValueMapForErrorResponse() {
		
		String stringToParse = "<cima:AccessTokenResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
					+ "<cima:Status Code=\"urn:comcast:login:api:rest:1.0:status:UnknownFailure\" "
					+ "Message=\"Failed to acquire Plaxo token\" />"
					+ "</cima:AccessTokenResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
				                                               getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_SERVICE_ACCESS_TOKEN_RESPONSE);
		
		assertThat(
				"Expected valid response",
				parsedMap, notNullValue());
		
		assertThat(
				"Expected valid userId",
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_SRVC_ACS_TKN_RSPNS_ERR_MSG), is("Failed to acquire Plaxo token"));
		
	}

	
	@Test
	public void testGetXMLValueMapForLoginTokenResponse() {
		
		String stringToParse = "<AuthnResponse xmlns=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
				+ "<LoginToken ExpiresOn=\"2015-06-12T01:16:55.798Z\">PHNhbWFtbDpBc3NlcnRpb24+</LoginToken>"
				+ "<Status Code=\"urn:comcast:login:api:rest:1.0:status:Success\"/>"
				+ "</AuthnResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
				                                               getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_LOGIN_TOKEN_RESPONSE);
		
		assertThat(
				parsedMap, notNullValue());
		
		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_LOGIN_TOKEN), 
				is("PHNhbWFtbDpBc3NlcnRpb24+"));
		
		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_STATUS), 
				is("urn:comcast:login:api:rest:1.0:status:Success"));

		
	}

	
	@Test
	public void testGetXMLValueMapForLoginTokenErrorResponse() {
		
		String stringToParse = "<cima:AuthnResponse xmlns:cima=\"urn:comcast:login:api:v1.0\" version=\"1.0\">"
						+ "<cima:Status Code=\"urn:comcast:login:api:rest:1.0:status:SigNotValid\" "
						+ "Message=\"Invalid request signature\" /></cima:AuthnResponse>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
				                                               getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_LOGIN_TOKEN_RESPONSE);
		
		assertThat(
				parsedMap, notNullValue());
		
		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_RSPNS_ERR_MSG), 
				is("Invalid request signature"));
		
	}

	
	@Test
	public void testGetXMLValueMapForLoginTokenSAMLAssertion() {
		
		String stringToParse = "<saml:Assertion xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\" "
				+ "xmlns:ns4=\"http://www.w3.org/2000/09/xmldsig#\" xmlns:xi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" ID=\"ID_2015-06-11T21.16.55.798Z\" IssueInstant=\"2015-06-11T21:16:55.801Z\" "
				+ "Version=\"2.0\">"
				+ "<saml:Issuer>https://login.comcast.net/api/login</saml:Issuer>"
				+ "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"
				+ "<ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"
				+ "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig" 
				+ "#rsa-sha1\"/>"
				+ "<ds:Reference URI=\"#ID_2015-06-11T21.16.55.798Z\">"
				+ "<ds:Transforms>"
				+ "<ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#"
				+ "enveloped-signature\"/>"
				+ "<ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#WithComments\"/></ds:Transforms>"
				+ "<ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
				+ "<ds:DigestValue>bYW/eQN0k7HV93FT7AJCe94tH/g=</ds:DigestValue>"
				+ "</ds:Reference>"
				+ "</ds:SignedInfo>"
				+ "<ds:SignatureValue>fdukrdXUP5uOES+v3h5SXopCz+1HWgKsip2kDy6myNvN9/JkRSJIDIBsQC2PBmLBWnwMzLCAdQL1XKi/gkOW2/qf2w8"
				+ "ftAe0G0r3ZWcVgviNs=</ds:SignatureValue>"
				+ "</ds:Signature>"
				+ "<saml:Subject>"
				+ "<saml:NameID Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\">043527441729102010Comcast.USR4JR</saml:NameID>"
				+ "<saml:SubjectConfirmation Method=\"urn:oasis:names:tc:SAML:2.0:cm:holder-of-key\">"
				+ "<saml:SubjectConfirmationData NotBefore=\"2015-06-11T21:16:55.798Z\" NotOnOrAfter=\"2015-06-12T01:16:55.798Z\" "
				+ "Recipient=\"https://login.comcast.net/api/login\"/></saml:SubjectConfirmation>"
				+ "</saml:Subject>"
				+ "<saml:Conditions NotBefore=\"2015-06-11T21:16:55.798Z\" NotOnOrAfter=\"2015-06-12T01:16:55.798Z\">"
				+ "<saml:AudienceRestriction>"
				+ "<saml:Audience>https://login.comcast.net/api/login</saml:Audience>"
				+ "</saml:AudienceRestriction>"
				+ "</saml:Conditions>"
				+ "<saml:AuthnStatement AuthnInstant=\"2015-06-11T21:16:55.798Z\" SessionNotOnOrAfter=\"2015-06-12T01:16:55.798Z\">"
				+ "<saml:AuthnContext>"
				+ "<saml:AuthnContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport</saml:AuthnContextClassRef>"
				+ "<saml:AuthnContextDeclRef>urn:comcast:cima:login:rest:ac:password</saml:AuthnContextDeclRef>"
				+ "</saml:AuthnContext>"
				+ "</saml:AuthnStatement>"
				+ "<saml:AttributeStatement>"
				+ "<saml:Attribute Name=\"firstname\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">"
				+ "<saml:AttributeValue xi:type=\"xs:string\">TROY</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"lastname\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">"
				+ "<saml:AttributeValue xi:type=\"xs:string\">CIMA</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"emailAddress\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">"
				+ "<saml:AttributeValue xi:type=\"xs:string\">B_CIMA_TRIPLE_06@comcast.net</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"appKey\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">"
				+ "<saml:AttributeValue xi:type=\"xs:string\">testApp</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"cid\"><saml:AttributeValue xi:type=\"xs:base64Binary\">c+yco2e9gD186PTljLk2mr2E9gk=</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"cos\" NameFormat=\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\">"
				+ "<saml:AttributeValue xi:type=\"xs:string\">hsi</saml:AttributeValue>"
				+ "<saml:AttributeValue xi:type=\"xs:string\">cdv</saml:AttributeValue>"
				+ "<saml:AttributeValue xi:type=\"xs:string\">video</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "<saml:Attribute Name=\"subject-id\" NameFormat="
				+ "\"urn:oasis:names:tc:SAML:2.0:attrname-format:basic\"><saml:AttributeValue xi:type=\"xs:string\">043527441729102010Comcast.USR4JR</saml:AttributeValue>"
				+ "</saml:Attribute>"
				+ "</saml:AttributeStatement>"
				+ "</saml:Assertion>";
		
		Map<XMLParserHelper.ParsedMapKeys, Object> parsedMap = XMLParserHelper.
				                                               getXMLValueMap(stringToParse, XMLParserHelper.XmlParsingObjectTypes.CIMA_LOGIN_TOKEN_SAML_ASSERTION);
		
		assertThat(
				parsedMap, notNullValue());
		
		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_APP_KEY), 
				is("testApp"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_CID), 
				is("c+yco2e9gD186PTljLk2mr2E9gk="));

		assertThat(
				parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_COS),
				(Matcher) hasItems("hsi","cdv","video"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_EMAIL), 
				is("B_CIMA_TRIPLE_06@comcast.net"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_FIRST_NAME), 
				is("TROY"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_LAST_NAME), 
				is("CIMA"));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_SIGNATURE_INFO), 
				is("fdukrdXUP5uOES+v3h5SXopCz+1HWgKsip2kDy6myNvN9/JkRSJIDIBsQC2PBmLBWnwMz"
						+ "LCAdQL1XKi/gkOW2/qf2w8ftAe0G0r3ZWcVgviNs="));

		assertThat(
				(String)parsedMap.get(XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_SUBJECT_ID), 
				is("043527441729102010Comcast.USR4JR"));

	}

}
	