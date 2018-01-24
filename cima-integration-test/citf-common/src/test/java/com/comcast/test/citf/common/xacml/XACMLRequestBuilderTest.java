package com.comcast.test.citf.common.xacml;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.comcast.test.citf.common.crypto.XMLSignatureGenerator;
import com.comcast.test.citf.common.crypto.XMLSignatureGenerator.KeyStoreType;
import com.comcast.test.citf.common.crypto.XMLSignatureGenerator.SignatureType;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions;

public class XACMLRequestBuilderTest {
	
	@InjectMocks
	private XACMLRequestBuilder mockXACMLRequestBuilder;
	
	@Mock
	private XacmlHelper mockHelper;
	
	@Mock
	private XMLSignatureGenerator signatureGen;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Test to verify XACML channel request for OAuth. 
	 * 
	 * The API takes the channel name and the action attribute to generate the request.
	 * expectedResponse is the XACML request that is expected to be generated
	 * actualResponse is the XACML request that is generated with the API.
	 */
	@Test
	public void testCreateChannelRequestForOAUTH() {
		
		String expectedResponse = "<xacml-context:Request xmlns:xacml-context=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" "
								  + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:"
								  + "tc:xacml:2.0:context:schema:os http://docs.oasis-open.org/xacml/access_control-xacml-2.0-context-schema-os.xsd"
								  + "\">"
								  + "<xacml-context:Resource>"
								  + "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" "
								  + "DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue>HBO</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "</xacml-context:Resource>"
								  + "<xacml-context:Action><xacml-context:Attribute AttributeId=\"urn:oasis:"
								  + "names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue>VIEW</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "</xacml-context:Action>"
								  + "</xacml-context:Request>";
	
		String actualResponse = mockXACMLRequestBuilder.createChannelRequestForOAUTH("HBO", Actions.VIEW);
		
		assertThat(
				"Expected valid response",
				actualResponse, notNullValue());
		
		assertThat(actualResponse, is(expectedResponse));
	}
	
	
	/**
	 * Test to verify XACML asset request for OAuth. 
	 * 
	 * The API takes channel name, resourceIs, asset title, asset rating and action attribute.
	 * expectedResponse is the XACML request that is expected to be generated
	 * actualResponse is the XACML request that is generated with the API.
	 */
	@Test
	public void testCreateAssetRequestForOAUTH() {
		
		String expectedResponse = "<xacml-context:Request xmlns:xacml-context=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" "
								  + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:"
								  + "tc:xacml:2.0:context:schema:os http://docs.oasis-open.org/xacml/access_control-xacml-2.0-context-"
								  + "schema-os.xsd\">"
								  + "<xacml-context:Resource>"
								  + "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:tv-network\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:"
								  + "AttributeValueType\">Fox</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:"
								  + "AttributeValueType\">mockResourceId</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "<xacml-context:Attribute "
								  + "AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:id:namespace\" DataType=\"http://www.w3.org/2001/XMLSchema#string"
								  + "\">"
								  + "<xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:"
								  + "AttributeValueType\">mockNameSpace</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute><xacml-context:Attribute "
								  + "AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:rating:urn:v-chip\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">"
								  + "PG-13</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:"
								  + "attribute:content:title\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
								  + "<xacml-context:AttributeValue xmlns:"
								  + "xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">mock asset title"
								  + "</xacml-context:AttributeValue></xacml-context:Attribute>"
								  + "</xacml-context:Resource><xacml-context:Action>"
								  + "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/"
								  + "XMLSchema#string\">"
								  + "<xacml-context:AttributeValue>VIEW</xacml-context:AttributeValue>"
								  + "</xacml-context:Attribute>"
								  + "</xacml-context:Action>"
								  + "</xacml-context:Request>";
		
		String actualResponse = mockXACMLRequestBuilder.createAssetRequestForOAUTH(
				new XACMLRequestBuilder.TveAssetResource[]{
						XACMLRequestBuilder.TveAssetResource.newInstanceForVChip(
								"Fox", "mockResourceId", "mockNameSpace", "PG-13", "mock asset title")
								},
								Actions.VIEW);
		
		assertThat(
				"Expected valid response",
				actualResponse, notNullValue());
		
		assertThat(actualResponse, is(expectedResponse));
	}
	
	
	/**
	 * Test to verify XACML channel request for SAML.
	 * 
	 * The API takes channel name, action attribute and other attributes to generate SAML response which
	 * is needed to generate the XACML request.
	 */
	@Test
	public void testCreateChannelRequestForSAML() {
		
		String samlResponse = "PHNhbWxwOlJlc3BvbnNlIERlc3RpbmF0aW9uPSJodHRwOi8vbG9jYWxob3N0OjgwODAvIiBJc3N1ZUluc3RhbnQ9IjIwMTUtMDctMzBUMTg6MzA6NDk";
		String samlResponseUrl = "https://idp-qa4.comcast.net/idp/startSSO.ping?PartnerSpId=";
		String destinationURL = "http://localhost:8080/citf-ui/noReturn/samlResponse/";
		String channel = "ESPN";
		String clientId = "mockClientId";
		String userId = "mockUserId";
		String password = "mockPassword";
		String tveKeyStoreAlias = "mockAlias";
		String tveKeyStorePassword = "mockKeyStorePassword";
		String tveKeyStoreEntryPassword = "mockEntryPassword";
		String mockSignature = "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"
							   + "<ds:SignedInfo>"
							   + "<ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"
							   + "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>"
							   + "<ds:Reference URI=\"#jk8qU9HndhvYyfv5rWvxnJvPcZP\">"
							   + "<ds:Transforms>"
							   + "<ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>"
							   + "</ds:Transforms><ds:DigestMethod "
							   + "Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
							   + "<ds:DigestValue>+LcoKk3tV9hicALAL6ooah7e280=</ds:DigestValue>"
							   + "</ds:Reference>"
							   + "</ds:SignedInfo>"
							   + "<ds:SignatureValue>lbNfMGPw76s3abQew5ydyuMDD28jVYD9AWZd6/fmPCgAw4pmkJJauiCHVF5pyC85JmzV"
							   + "3YueFOO5apcVXJYu/LfkJtpvETLxWSwqhpurzHX7JGvWwwKOXXkyPDnDeyuZ5sI2PRlJyQIVpReyn75PX41HEhFRSLY0xwikzrE6frU=</ds:SignatureValue>"
							   + "<ds:KeyInfo>"
							   + "<ds:KeyValue>"
							   + "<ds:RSAKeyValue><ds:Modulus>q2alnYJnR3uI1oAIdxKyskw+A+UpzyrlzEgOEBQoU8lX3XS7InieXBZQ5J2/bhlrmkgDxutJ"
							   + "64K4d9heEptQ/if8LjN2KNlI2vJrYIW3VF0YzgW+B/NJZHbbk4UPuWpEW4HcYzEeU//HQrWsXmkN6gbkvqW7AzKo06ZefB1aY2M=</ds:Modulus><ds:Exponent>"
							   + "AQAB</ds:Exponent>"
							   + "</ds:RSAKeyValue>"
							   + "</ds:KeyValue>"
							   + "</ds:KeyInfo>"
							   + "</ds:Signature>";
		
		
		KeyPair kp = getKeyPair();

		//Stub
		Mockito
			   .when(mockHelper.fetchSAMLResponseForTVE(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			   .thenReturn(samlResponse);
		
		Mockito
		       .when(signatureGen.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, tveKeyStoreAlias, tveKeyStorePassword, tveKeyStoreEntryPassword, KeyStoreType.JKS))
		       .thenReturn(kp);
		
		Mockito
		       .when(signatureGen.generateXMLSignature(Mockito.anyString(), Mockito.anyString(), Mockito.any(KeyPair.class), Mockito.any(SignatureType.class)))
		       .thenReturn(mockSignature);
		
		String samlRequest = mockXACMLRequestBuilder.createChannelRequestForSAML(channel, 
		                    		  					   						 Actions.VIEW, 
		                    		  					   						 userId, 
		                    		  					   						 password, 
		                    		  					   						 samlResponseUrl, 
		                    		  					   						 destinationURL, 
		                    		  					   						 clientId, 
		                    		  					   						 tveKeyStoreAlias, 
		                    		  					   						 tveKeyStorePassword, 
		                    		  					   						 tveKeyStoreEntryPassword);
		
		assertThat(
				"Expected valid saml request",
				samlRequest, notNullValue());
		
		assertThat(samlRequest, containsString(samlResponse));
		
		assertThat(samlRequest, containsString(clientId));
		
		assertThat(samlRequest, containsString(destinationURL));
		
		assertThat(samlRequest, containsString(channel));
		
	}
	
	
	/**
	 * Test to verify XACML asset request for SAML.
	 * 
	 * The API takes channel name, action attribute, asset name and rating and other 
	 * attributes to generate SAML response which is needed to generate the XACML request.
	 */
	@Test
	public void testCreateAssetRequestForSAML() {
		
		String samlResponse = "PHNhbWxwOlJlc3BvbnNlIERlc3RpbmF0aW9uPSJodHRwOi8vbG9jYWxob3N0OjgwODAvIiBJc3N1ZUluc3RhbnQ9IjIwMTUtMDctMzBUMTg6MzA6NDk";
		String samlResponseUrl = "https://idp-qa4.comcast.net/idp/startSSO.ping?PartnerSpId=";
		String destinationURL = "http://localhost:8080/citf-ui/noReturn/samlResponse/";
		String tvNetwork = "FOX";
		String clientId = "mockClientId";
		String resourceId = "mockResourceId";
		String namespace = "mockNameSpace";
		String tveKeyStoreAlias = "mockAlias";
		String vChip = "TV-G";
		String assetTitle = "mockAssetTitle";
		String userId = "mockUserId";
		String password = "mockPassword";
		String tveKeyStorePassword = "mockKeyStorePassword";
		String tveKeyStoreEntryPassword = "mockEntryPassword";
		String mockSignature = "<ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">"
							   + "<ds:SignedInfo><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/>"
							   + "<ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/>"
							   + "<ds:Reference URI=\"#jk8qU9HndhvYyfv5rWvxnJvPcZP\">"
							   + "<ds:Transforms>"
							   + "<ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/>"
							   + "</ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/>"
							   + "<ds:DigestValue>+LcoKk3tV9hicALAL6ooah7e280=</ds:DigestValue>"
							   + "</ds:Reference>"
							   + "</ds:SignedInfo>"
							   + "<ds:SignatureValue>lbNfMGPw76s3abQew5ydyuMDD28jVYD9AWZd6/fmPCgAw4pmkJJauiCHVF5pyC85JmzV"
							   + "3YueFOO5apcVXJYu/LfkJtpvETLxWSwqhpurzHX7JGvWwwKOXXkyPDnDeyuZ5sI2PRlJyQIVpReyn75PX41HEhFRSLY0xwikzrE6frU=</ds:SignatureValue>"
							   + "<ds:KeyInfo>"
							   + "<ds:KeyValue>"
							   + "<ds:RSAKeyValue>"
							   + "<ds:Modulus>q2alnYJnR3uI1oAIdxKyskw+A+UpzyrlzEgOEBQoU8lX3XS7InieXBZQ5J2/bhlrmkgDxutJ"
							   + "64K4d9heEptQ/if8LjN2KNlI2vJrYIW3VF0YzgW+B/NJZHbbk4UPuWpEW4HcYzEeU//HQrWsXmkN6gbkvqW7AzKo06ZefB1aY2M=</ds:Modulus>"
							   + "<ds:Exponent>AQAB</ds:Exponent>"
							   + "</ds:RSAKeyValue>"
							   + "</ds:KeyValue>"
							   + "</ds:KeyInfo>"
							   + "</ds:Signature>";
		
		KeyPair kp = getKeyPair();

		//Stub
		Mockito
			   .when(mockHelper.fetchSAMLResponseForTVE(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
			   .thenReturn(samlResponse);
		
		Mockito
		       .when(signatureGen.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, tveKeyStoreAlias, tveKeyStorePassword, tveKeyStoreEntryPassword, KeyStoreType.JKS))
		       .thenReturn(kp);
		
		Mockito
		       .when(signatureGen.generateXMLSignature(Mockito.anyString(), Mockito.anyString(), Mockito.any(KeyPair.class), Mockito.any(SignatureType.class)))
		       .thenReturn(mockSignature);
		
		String samlRequest = mockXACMLRequestBuilder.createAssetRequestForSAML(tvNetwork, resourceId, namespace, vChip, assetTitle, Actions.VIEW, userId, password, samlResponseUrl, destinationURL, clientId, tveKeyStoreAlias, tveKeyStorePassword, tveKeyStoreEntryPassword);
		
		assertThat(
				"Expected valid saml request",
				samlRequest, notNullValue());
		
		assertThat(samlRequest, containsString(samlResponse));
		
		assertThat(samlRequest, containsString(clientId));
		
		assertThat(samlRequest, containsString(destinationURL));
		
		assertThat(samlRequest, containsString(tvNetwork));
		
		assertThat(samlRequest, containsString(clientId));
		
		assertThat(samlRequest, containsString(resourceId));
		
		assertThat(samlRequest, containsString(namespace));
		
		assertThat(samlRequest, containsString(vChip));
		
		assertThat(samlRequest, containsString(assetTitle));
		
	}
	
	
	private KeyPair getKeyPair()  {
		KeyPair kp = null;
		try{
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			kp = keyGen.generateKeyPair();
		}catch(NoSuchAlgorithmException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
		return kp;
	}

}
