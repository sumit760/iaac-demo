package com.comcast.test.citf.common.xacml;

import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * This class will be used by XACMLRequestBuilder.java only to build the static 
 * component of the request.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 * 
 * 
 */
public class XacmlStaticBuilder {


	/**
	 * Method to get the XACML channel request (multiple channel).
	 *  
	 * @param subjectId The subjectId to be used. 
	 * 		  This is a mandatory field for SAML, but it should be null for OAuth
	 * @param resourceIds resource Ids
	 * @param actionId The action to perform over the channel (VIEW etc.)
	 * 
	 * @return The XACML channel request
	 */
	static String getChannelRequest(String subjectId, String[] resourceIds, String actionId){
		StringBuilder sbf = new StringBuilder();
		sbf.append(XML_HEADER);
		
		if(!StringUtility.isStringEmpty(subjectId)){
			sbf.append(SUBJECT_PREFIX);
			sbf.append(subjectId);
			sbf.append(SUBJECT_SUFFIX);
		}

		for (String resourceId : resourceIds) {
			sbf.append(RESOURCE_HEADER);
			sbf.append(CHANNEL_RESOURCE_RESOURCE_ID_PREFIX);
			if (resourceId != null) {
				sbf.append(resourceId);
			}
			sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
			sbf.append(RESOURCE_FOOTER);
		}
		
		if (actionId != null) {
			sbf.append(ACTION_PREFIX);
			sbf.append(actionId);
			sbf.append(ACTION_SUFFIX);
		}
		
		sbf.append(XML_FOOTER);
		
		return sbf.toString();
	}
	/**
	 * Method to get the XACML channel request (single channel).
	 *  
	 * @param subjectId The subjectId to be used. 
	 * 		  This is a mandatory field for SAML, but it should be null for OAuth
	 * @param resourceId The resourceId
	 * @param actionId The action to perform over the channel (VIEW etc.)
	 * 
	 * @return The XACML channel request
	 */
	static String getChannelRequest(String subjectId, String resourceId, String actionId){
		return getChannelRequest(subjectId, new String[]{resourceId}, actionId);
	}
	
	/**
	 * Method to get the XACML asset request.
	 * 
	 * @param subjectId The subjectId to be used. 
	 * 		  This is a mandatory field for SAML, but it should be null for OAuth
	 * @param inTarArray The array containing Resource requests
	 * @param actionId The action to perform over the asset (VIEW etc.)
	 * 
	 * @return The XACML asset request
	 */
	public static String getAssetRequest(
			String subjectId, XACMLRequestBuilder.TveAssetResource[] inTarArray, String actionId) {
		StringBuilder sbf = new StringBuilder();
		sbf.append(XML_HEADER);
		
		if(!StringUtility.isStringEmpty(subjectId)){
			sbf.append(SUBJECT_PREFIX);
			sbf.append(subjectId);
			sbf.append(SUBJECT_SUFFIX);
		}
		
		for (XACMLRequestBuilder.TveAssetResource nextTar : inTarArray) {
			sbf.append(RESOURCE_HEADER);
			
			if (nextTar != null) {
				if(!StringUtility.isStringEmpty(nextTar.getTvNetwork())){
					sbf.append(ASSET_RESOURCE_TV_NETWORK_PREFIX);
					sbf.append(nextTar.getTvNetwork());
					sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				}
		
				sbf.append(ASSET_RESOURCE_RESOURCE_ID_PREFIX);
				sbf.append(nextTar.getResourceId());
				sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				
				if(!StringUtility.isStringEmpty(nextTar.getNamespace())){
					sbf.append(ASSET_RESOURCE_NAMESPACE_PREFIX);
					sbf.append(nextTar.getNamespace());
					sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				}
				
				if(!StringUtility.isStringEmpty(nextTar.getVChip())){
					sbf.append(ASSET_RESOURCE_V_CHIP_PREFIX);
					sbf.append(nextTar.getVChip());
					sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				}
				
				if(!StringUtility.isStringEmpty(nextTar.getMpaa())){
					sbf.append(ASSET_RESOURCE_MPAA_PREFIX);
					sbf.append(nextTar.getMpaa());
					sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				}
				
				if(!StringUtility.isStringEmpty(nextTar.getAssetTitle())){
					sbf.append(ASSET_RESOURCE_TITLE_PREFIX);
					sbf.append(nextTar.getAssetTitle());
					sbf.append(COMMON_RESOURCE_ATTRIBUTE_SUFFIX);
				}
			}
			
			sbf.append(RESOURCE_FOOTER);
		}
		
		if (actionId != null) {
			sbf.append(ACTION_PREFIX);
			sbf.append(actionId);
			sbf.append(ACTION_SUFFIX);
		}
		
		sbf.append(XML_FOOTER);
		
		return sbf.toString();
	}
	
	
	/**
	 * Method to get the XACML AuthzdecisionQuery request to be used in generating the SAML signature.
	 * <p>
	 * Use XMLSignatureGenerator.generateXMLSignature() to generate the signature on XACML AuthzdecisionQuery request
	 * 
	 * @param xacmlSubjectId The XACML subject Id
	 * @param sigId XACML The Signature Id
	 * @param destinationUrl The destination URL or the CRACr end-point
	 * @param clientId The clientId
	 * 
	 * @return The XACML AuthzdecisionQuery request
	 */
	static String generateXACMLAuthzDecisionQuery(String xacmlSubjectId, String sigId, String destinationUrl, String clientId){
		StringBuilder sbf = new StringBuilder();
		sbf.append(SAML_REPONSE_WRPR_PREFIX_PART1_START);
		sbf.append(destinationUrl);
		sbf.append(SAML_REPONSE_WRPR_PREFIX_PART2_BFORE_ID);
		sbf.append(sigId);
		sbf.append(SAML_REPONSE_WRPR_PREFIX_PART3_BFORE_ISSUE_INSTANT);
		sbf.append(MiscUtility.getFormattedDate(ICimaCommonConstants.DATE_FORMAT_DETAILED_WITH_TZ));
		sbf.append(SAML_REPONSE_WRPR_PREFIX_PART4_BFORE_CLIENT_ID);
		sbf.append(clientId);
		sbf.append(SAML_REPONSE_WRPR_PREFIX_PART5_END);
		sbf.append(xacmlSubjectId);
		sbf.append(SAML_REPONSE_WRPR_SUFFIX);
		
		return sbf.toString();
	}
	
	
	/**
	 * Method to assemble the signed XACML request
	 * 
	 * @param request The XACML AuthzdecisionQuery request
	 * @param signature The SAML signature
	 * 
	 * @return The signed XACML request
	 */
	static String generateXacmlFinalRequestForSAML(String request, String signature){
		request = request.replace(SAML_XACML_FINAL_WRPR_REQUEST_REPLACEABLE, SAML_XACML_FINAL_WRPR_REQUEST_REPLACING_FIRST_PART+signature+SAML_XACML_FINAL_WRPR_REQUEST_REPLACING_LAST_PART);
		
		StringBuilder sbf = new StringBuilder();
		sbf.append(SAML_XACML_FINAL_WRPR_SOAP_ENV_HEADER);
		sbf.append(request);
		sbf.append(SAML_XACML_FINAL_WRPR_SOAP_ENV_FOOTER);
		
		return sbf.toString();
	}
	
	//************************************* Private constants **************************************
	
	private static final String XML_HEADER = "<xacml-context:Request xmlns:xacml-context=\"urn:oasis:names:tc:xacml:2.0:context:schema:os\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:oasis:names:tc:xacml:2.0:context:schema:os http://docs.oasis-open.org/xacml/access_control-xacml-2.0-context-schema-os.xsd\">";
	private static final String XML_FOOTER = "</xacml-context:Request>";
	
	private static final String SUBJECT_PREFIX = "<xacml-context:Subject SubjectCategory=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"><xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue>";
	private static final String SUBJECT_SUFFIX = "</xacml-context:AttributeValue></xacml-context:Attribute></xacml-context:Subject>";
	
	private static final String ACTION_PREFIX = "<xacml-context:Action><xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue>";
	private static final String ACTION_SUFFIX = "</xacml-context:AttributeValue></xacml-context:Attribute></xacml-context:Action>";
	
	
	private static final String RESOURCE_HEADER = "<xacml-context:Resource>";
	private static final String RESOURCE_FOOTER = "</xacml-context:Resource>";
	private static final String COMMON_RESOURCE_ATTRIBUTE_SUFFIX = "</xacml-context:AttributeValue></xacml-context:Attribute>";
	
	private static final String CHANNEL_RESOURCE_RESOURCE_ID_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue>";
	
	private static final String ASSET_RESOURCE_TV_NETWORK_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:tv-network\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	private static final String ASSET_RESOURCE_RESOURCE_ID_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	private static final String ASSET_RESOURCE_NAMESPACE_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:id:namespace\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	private static final String ASSET_RESOURCE_V_CHIP_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:rating:urn:v-chip\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	private static final String ASSET_RESOURCE_MPAA_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:rating:urn:mpaa\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	private static final String ASSET_RESOURCE_TITLE_PREFIX = "<xacml-context:Attribute AttributeId=\"urn:cablelabs:ocla:1.0:attribute:content:title\" DataType=\"http://www.w3.org/2001/XMLSchema#string\"><xacml-context:AttributeValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xacml-context:AttributeValueType\">";
	
	
	private static final String SAML_REPONSE_WRPR_PREFIX_PART1_START = "<xacml-samlp:XACMLAuthzDecisionQuery xmlns:xacml-samlp=\"urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol\"  InputContextOnly=\"true\" Destination=\"";
	private static final String SAML_REPONSE_WRPR_PREFIX_PART2_BFORE_ID = "\" ID=\"";
	private static final String SAML_REPONSE_WRPR_PREFIX_PART3_BFORE_ISSUE_INSTANT = "\" IssueInstant=\"";
	private static final String SAML_REPONSE_WRPR_PREFIX_PART4_BFORE_CLIENT_ID = "\" Version=\"2.0\" Consent=\"consent-uri\">\n<saml2:Issuer xmlns:saml2=\"urn:oasis:names:tc:SAML:2.0:assertion\">";
	private static final String SAML_REPONSE_WRPR_PREFIX_PART5_END = "</saml2:Issuer>\n";
	private static final String SAML_REPONSE_WRPR_SUFFIX = "</xacml-samlp:XACMLAuthzDecisionQuery>";
	
	private static final String SAML_XACML_FINAL_WRPR_REQUEST_REPLACEABLE = "</saml2:Issuer>\n<xacml-context:Request";
	private static final String SAML_XACML_FINAL_WRPR_REQUEST_REPLACING_FIRST_PART = "</saml2:Issuer>\n";
	private static final String SAML_XACML_FINAL_WRPR_REQUEST_REPLACING_LAST_PART = "<xacml-context:Request";
	
	private static final String SAML_XACML_FINAL_WRPR_SOAP_ENV_HEADER = "<soap-env:Envelope xmlns:soap-env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">\n<soap-env:Header/>\n<soap-env:Body>\n";
	private static final String SAML_XACML_FINAL_WRPR_SOAP_ENV_FOOTER =	"</soap-env:Body></soap-env:Envelope>";
}
