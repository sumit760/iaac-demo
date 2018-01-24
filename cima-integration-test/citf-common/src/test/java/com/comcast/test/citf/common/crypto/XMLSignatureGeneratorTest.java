package com.comcast.test.citf.common.crypto;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.security.KeyPair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.crypto.XMLSignatureGenerator.SignatureType;
import com.comcast.test.citf.common.init.CommonContextInitializer;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

public class XMLSignatureGeneratorTest {
	
	private XMLSignatureGenerator objXMLSignatureGenerator;
	private static final String xmlString="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<xacml-samlp:XACMLAuthzDecisionQuery "
			+ "xmlns:xacml-samlp=\"urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol\" "
			+ "InputContextOnly=\"true\" Destination=\"https://login-qa4.comcast.net/api/xacml/soap/tve\" "
			+ "ID=\"jk8qU9HndhvYyfv5rWvxnJvPcZP\" "
			+ "IssueInstant=\"2015-08-06T14:31:28.215Z\" "
			+ "Version=\"2.0\" "
			+ "Consent=\"consent-uri\">"
			+ "<saml2:Issuer xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">cimatest</saml2:Issuer>"
			+ "<xacml-context:Request xmlns:xacml-context=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" "
			+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"   xsi:schemaLocation=\"urn:oasis:names:tc:xacml:2.0:context:schema:os "
			+ "http://docs.oasis-open.org/xacml/access_control-xacml-2.0-context-schema-os.xsd\">   "
			+ "<xacml-context:Subject  SubjectCategory=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">"
			+ "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
			+ "<xacml-context:AttributeValue>VFZTAQEAWpVGS8FHyA**</xacml-context:AttributeValue></xacml-context:Attribute></xacml-context:Subject>"
			+ "<xacml-context:Resource>"
			+ "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" "
			+ "DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue>TestChannel1</xacml-context:AttributeValue>"
			+ "</xacml-context:Attribute>"
			+ "</xacml-context:Resource>"
			+ "<xacml-context:Action>"
			+ "<xacml-context:Attribute "
			+ "AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\">"
			+ "<xacml-context:AttributeValue>VIEW</xacml-context:AttributeValue>"
			+ "</xacml-context:Attribute>"
			+ "</xacml-context:Action>"
			+ "</xacml-context:Request>"
			+ "</xacml-samlp:XACMLAuthzDecisionQuery>";
	
	private static final String ID="jk8qU9HndhvYyfv5rWvxnJvPcZP";

	
	@Before
	public void setup(){
		CommonContextInitializer.initializeTestContexts(null);
		objXMLSignatureGenerator = new XMLSignatureGenerator();
	}
	

	
	@Test
	public void testGetKeyPairFromKeyStore(){
		
		KeyPair keyPair = objXMLSignatureGenerator.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, 
														                  "citf_tve", 
														                  "Comcast1234", 
														                  "password", 
														                  XMLSignatureGenerator.KeyStoreType.JKS);
		assertThat(keyPair, 
				  notNullValue());
		
		assertThat(keyPair.getPrivate(), 
				  notNullValue());
		
		assertThat(keyPair.getPublic(), 
				  notNullValue());
	}
	
	
	@Test
	public void testGenerateXMLSignature() {
		
		KeyPair keyPair = objXMLSignatureGenerator.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, 
				                                  "citf_tve", 
				                                  "Comcast1234", 
				                                  "password", 
				                                  XMLSignatureGenerator.KeyStoreType.JKS);
		
		String signature = objXMLSignatureGenerator.generateXMLSignature(xmlString, 
				                                		                 ID, 
				                                		                 keyPair,
				                                		                 SignatureType.TVE );
		
		assertThat(signature, containsString(ID));
		
		boolean validity = objXMLSignatureGenerator.validateXMLSignature(signature, keyPair);
		
		assertThat(validity,is(true));
		
	}
	
	
	@Test
	public void testGetSignatureElement(){
		
		KeyPair keyPair = objXMLSignatureGenerator.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, 
                									"citf_tve", 
                									"Comcast1234", 
                									"password", 
                									XMLSignatureGenerator.KeyStoreType.JKS);

		String signature = objXMLSignatureGenerator.generateXMLSignature(xmlString, 
              		                 ID, 
              		                 keyPair,
              		                 SignatureType.TVE );
		
		assertThat(signature, notNullValue());
		
		String signatureElement = objXMLSignatureGenerator.getSignatureElement(signature, SignatureType.TVE);

		assertThat(signatureElement, notNullValue());
		
		assertThat(signatureElement, containsString(ID));
	}
	
	
	@After
	public void tearDown() {
		CommonContextInitializer.destroyContext();
	}
}
