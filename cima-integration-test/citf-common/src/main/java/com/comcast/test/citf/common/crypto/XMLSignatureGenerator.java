package com.comcast.test.citf.common.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Collections;
import java.util.Iterator;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.comcast.test.citf.common.init.CommonContextInitializer;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * Class to generate XML signature over the XML string.
 * <p>
 * This class will be used to generate any XML Signature (Currently it supports 'DOM' only).
 * The input certificate may comes in '.p12' format, in that case this needs to be changed in '.jks' format using the below query
 * 		keytool -importkeystore -srckeystore <P12_FILE_NAME> -srcstoretype pkcs12 -srcalias "1" -destkeystore <JKS_FILE_NAME_WHICH_NEED_TO_GENARTE> 
 * 				-deststoretype jks -deststorepass <KEYSTORE_PASSWORD> -destalias "<ALIAS_NAME>"  
 * 
 * @author Sumit Pal and Abhijit Rej
 * @since July 2015
 * 
 */
@Service("xmlSignatureGenerator")
public class XMLSignatureGenerator {
	
	/**
	 * The XML signature type.
	 */
	public enum SignatureType{
		TVE,
		OTHERS
	}
	
	/**
	 * The java key store type.
	 */
	public enum KeyStoreType {
		JKS
	}
	
	/**
	 * Gets the Key pair from java key store of type "jks".
	 * 
	 * @param keystoreFileName The key store filename.
	 * @param alias The key store alias.
	 * @param keystorePassword The key store password.
	 * @param entryPassword The key store entry password.
	 * @param ksType The key store type.
	 * 
	 * @return KeyPair The key pair associated with the keystore.
	 * @see KeyStoreType
	 */
	public KeyPair getKeyPairFromKeyStore(String keystoreFileName, String alias, String keystorePassword, String entryPassword, KeyStoreType ksType) {
		PrivateKey privatekey = null;
		PublicKey publickey = null;
		KeyStore ks;
		
		try{
			if (keystoreFileName==null || alias==null || keystorePassword==null || entryPassword==null || ksType==null) {
				throw new IllegalArgumentException(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
			}

			if (KeyStoreType.JKS.equals(ksType)){
				ks = KeyStore.getInstance(ICimaCommonConstants.EXTENSION_JAVA_KEY_STORE_JKS);

				ks.load(CommonContextInitializer.getResource(keystoreFileName), keystorePassword.toCharArray());
				KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(entryPassword.toCharArray()));

				privatekey = keyEntry.getPrivateKey();
				Certificate cert = ks.getCertificate(alias);
				publickey = cert.getPublicKey();

				if(privatekey!=null && publickey!=null){
					LOGGER.debug("Public & private keys fetched from key store.");
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Error occurred while fetching KeyPair from KeyStore: ", e);
		}
		return (privatekey!=null && publickey!=null) ? new KeyPair(publickey, privatekey) : null;
	}
	
	/**
	 * Genrerate XML signature over an element with ID=id with the private key provided within the keystore.
	 * 
	 * @param xmlString The XML string over which the signature will be generated.
	 * @param ID The ID field of the XML string.
	 * @param kp The Key Pair obtained from the key store.
	 * @param sigType Signature type.
	 * 
	 * @return The Signature element string.
	 * @see KeyPair, SignatureType
	 */
	public String generateXMLSignature(String xmlString, String ID, KeyPair kp, SignatureType sigType) {
		InputStream xmlIs = null;
		XMLSignatureFactory fac;
		DocumentBuilderFactory dbf;
    	Document doc;
    	ByteArrayOutputStream baos = null;
    	String resultStr = null;
    	
    	try
    	{
    		LOGGER.info("Staring to generate XMLSignature for xml: {}, Id: {} and Signature type: {}", xmlString, ID, sigType);	

    		if (StringUtility.isStringEmpty(xmlString) || kp == null || sigType == null){
    			throw new IllegalArgumentException(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
    		}

    		xmlIs = new ByteArrayInputStream(xmlString.getBytes(Charset.forName(ICimaCommonConstants.ENCODING_UTF8)));
    		fac = XMLSignatureFactory.getInstance(SIGNATURE_MECHANISM_DOM);
    		KeyInfoFactory kif = fac.getKeyInfoFactory();
    		KeyValue kv = kif.newKeyValue(kp.getPublic());
    		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));

    		dbf = DocumentBuilderFactory.newInstance();
    		dbf.setNamespaceAware(true);
    		doc = dbf.newDocumentBuilder().parse(xmlIs);

    		XPath xpath = XPathFactory.newInstance().newXPath();
    		Node signatureInfoNode = (Node) xpath.evaluate("//*[@ID=\""+ID+"\"]", doc, XPathConstants.NODE);
    		Element e = (Element) signatureInfoNode;
    		e.setAttributeNS(null, "ID", ID);
    		e.setIdAttributeNode(e.getAttributeNode("ID"), true);
    		String referenceURI = "#" + ID;

    		Reference ref = fac.newReference(referenceURI, fac.newDigestMethod(DigestMethod.SHA1, null),
    				Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null, null);

    		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,(C14NMethodParameterSpec) null),
    				fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

    		DOMSignContext dsc = new DOMSignContext(kp.getPrivate(), e);
    		dsc.setDefaultNamespacePrefix(ICimaCommonConstants.TVE_XML_SIG_NAMESPACE);

    		XMLSignature signature = fac.newXMLSignature(si, ki);
    		signature.sign(dsc);

    		baos = new ByteArrayOutputStream();
    		TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer trans = tf.newTransformer();

    		trans.transform(new DOMSource(doc), new StreamResult(baos));

    		if(baos.size()>0){
    			resultStr = new String(baos.toByteArray(), ICimaCommonConstants.ENCODING_UTF8);
    			LOGGER.debug("Original generated signature : {}", new String(baos.toByteArray(), ICimaCommonConstants.ENCODING_UTF8));
    		}
        
		} catch (NoSuchAlgorithmException e) {
        	LOGGER.error("NoSuchAlgorithmException occurred while generating xml signature : ", e);
        } 
    	catch (InvalidAlgorithmParameterException e) {
        	LOGGER.error("InvalidAlgorithmParameterException occurred while generating xml signature : ", e);
        } 
        catch (FileNotFoundException e) {
        	LOGGER.error("FileNotFoundException occurred while generating xml signature : ", e);
        } 
        catch (IOException e) {
        	LOGGER.error("IOException occurred while generating xml signature : ", e);
        } 
        catch (SAXException e) {
        	LOGGER.error("SAXException occurred while generating xml signature : ", e);
        } 
        catch (ParserConfigurationException e) {
        	LOGGER.error("ParserConfigurationException occurred while generating xml signature : ", e);
        } 
        catch (MarshalException e) {
        	LOGGER.error("MarshalException occurred while generating xml signature : ", e);
        } 
        catch (XMLSignatureException e) {
        	LOGGER.error("XMLSignatureException occurred while generating xml signature : ", e);
        } 
        catch (TransformerConfigurationException e) {
        	LOGGER.error("TransformerConfigurationException occurred while generating xml signature : ", e);
        } 
        catch (TransformerException e) {
        	LOGGER.error("TransformerException occurred while generating xml signature : ", e);
        }
    	catch (Exception e) {
        	LOGGER.error("General Exception occurred while generating xml signature : ", e);
        }
        finally{
    		try{
    			if(xmlIs!=null){
    				xmlIs.close();
    			}
    			if(baos!=null){
    				baos.close();
    			}
    		}catch (IOException e) {
    	       	LOGGER.warn("IOException occurred while doing cleanup activity : ", e);
    	    }
    	}
        
    	LOGGER.info("Exiting generateXMLSignature with signature : {}", resultStr);
        return resultStr;
	}
	
	public String getSignatureElement(String input, SignatureType sigType) {
    	String output = input;
    	org.jdom2.input.SAXBuilder builder;
    	org.jdom2.Document document;
    	org.jdom2.output.XMLOutputter outputter;
    	InputStream xmlIs = null;
    	
    	try{
    		xmlIs = new ByteArrayInputStream(input.getBytes(Charset.forName(ICimaCommonConstants.ENCODING_UTF8)));
    		builder = new org.jdom2.input.SAXBuilder();
    		document = builder.build(xmlIs);
    		org.jdom2.Element root = document.getRootElement();
    		
    		org.jdom2.filter.ElementFilter filter=new org.jdom2.filter.ElementFilter(ELEMENT_NAME_SIGNATURE);
    		if(root!=null && root.getDescendants(filter)!=null){
    			org.jdom2.Element signature = root.getDescendants(filter).next();
    			outputter = new org.jdom2.output.XMLOutputter(org.jdom2.output.Format.getPrettyFormat());
    			output = outputter.outputString(signature);
    			
    			LOGGER.debug("The signature after parsing : {}", output);		
    		}    		
    	}
    	catch (JDOMException e) {
    		LOGGER.error("JDOMException occurred while parsing xml signature : ", e);
    	}
    	catch (IOException e) {
    		LOGGER.error("IOException occurred while generating xml signature : ", e);
    	}
    	finally{
    		try{
    			if(xmlIs!=null){
    				xmlIs.close();
    			}
    		}catch (IOException e) {
    	       	LOGGER.error("IO Exception occurred while doing cleanup activity : ", e);
    	    }
    	}
    	
    	return output;
    }
	
	
	public boolean validateXMLSignature(String signedContent,KeyPair kp) {
		XMLSignatureFactory fac;
		Document doc;
		DocumentBuilderFactory dbf;
		boolean coreValidity = false;
		
		try {
			if (StringUtility.isStringEmpty(signedContent) || kp == null) {
				throw new IllegalStateException(ICimaCommonConstants.EXCEPTION_INVALID_INPUT);
			}
		
			InputStream xmlIs = new ByteArrayInputStream(signedContent.getBytes(Charset.forName(ICimaCommonConstants.ENCODING_UTF8)));
			dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			doc = dbf.newDocumentBuilder().parse(xmlIs);
			
			// Find Signature element.
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
			if (nl.getLength() == 0) {
			    throw new IllegalStateException("Cannot find Signature element");
			}
			
			NodeList el = doc.getElementsByTagNameNS("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", "XACMLAuthzDecisionQuery");
			Attr id = ((Element)el.item(0)).getAttributeNode("ID");
		    Element e = (Element)el.item(0);
		    e.setIdAttributeNode(id, true);
		    // Create a DOMValidateContext and specify the public key
			DOMValidateContext valContext = new DOMValidateContext(kp.getPublic(), nl.item(0));
			
			fac = XMLSignatureFactory.getInstance(SIGNATURE_MECHANISM_DOM);
			
			// Unmarshal the XMLSignature.
			XMLSignature sig = fac.unmarshalXMLSignature(valContext);
			// Validate the XMLSignature.
			coreValidity = sig.validate(valContext);
			
			// Check core validation status.
			if (!coreValidity) {
				LOGGER.debug("Signature failed core validation");
			    boolean sv = sig.getSignatureValue().validate(valContext);
			    LOGGER.debug("signature validation status: {}", sv);
			    if (!sv) {
			        
			    	// Check the validation status of each Reference.
			        Iterator i = sig.getSignedInfo().getReferences().iterator();
			        for (int j=0; i.hasNext(); j++) {
			            boolean refValid = ((Reference) i.next()).validate(valContext);
			            LOGGER.debug("ref[{}] validity status: {}", j, refValid);
			        }
			    }
			} else {
				LOGGER.debug("Signature passed core validation");
			}
		} 
		catch (MarshalException me) {
			LOGGER.error("MarshalException occurred while unmarshalling xml signature: ", me);
		} 
		catch (XMLSignatureException xe) {
			LOGGER.error("XMLSignatureException occurred while generating xml signature: ", xe);
		} 
		catch (IOException e) {
			LOGGER.error("IOException occurred while generating xml signature: ", e);
        } 
		catch (SAXException e) {
			LOGGER.error("SAXException occurred while generating xml signature: ", e);
        } 
		catch (ParserConfigurationException e) {
			LOGGER.error("ParserConfigurationException occurred while generating xml signature: ", e);
        } 
		catch (IllegalStateException e) {
			LOGGER.error("IllegalStateException occurred while generating xml signature: ", e);
        } 
		
		return coreValidity;
	}
	
    private static final String SIGNATURE_MECHANISM_DOM = "DOM";
	private static final String ELEMENT_NAME_SIGNATURE = "Signature";
	private static final Logger LOGGER = LoggerFactory.getLogger(XMLSignatureGenerator.class);
}