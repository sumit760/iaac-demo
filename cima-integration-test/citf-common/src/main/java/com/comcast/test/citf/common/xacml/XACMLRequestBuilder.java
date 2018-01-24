package com.comcast.test.citf.common.xacml;

import java.security.KeyPair;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.crypto.XMLSignatureGenerator;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.StringUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;


/**
 * Spring service class to build XACML request for channels and assets.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 * 
 */

@Service("xacmlBuilder")
public class XACMLRequestBuilder {
	
	public enum Actions{
		VIEW("VIEW");
		
		private final String value;
        Actions(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	public static final class TveAssetResource {
		
		private final String tvNetwork;
		private final String resourceId;
		private final String namespace;
		private final String vChip;
		private final String mpaa;
		private final String assetTitle;
		
		private TveAssetResource(
				String inTvNetwork, String inResourceId, String inNamespace,
				String inVChip, String inMpaa, String inAssetTitle) {
			this.tvNetwork = inTvNetwork;
			this.resourceId = inResourceId;
			this.namespace = inNamespace;
			this.vChip = inVChip;
			this.mpaa = inMpaa;
			this.assetTitle = inAssetTitle;
		}
		protected String getTvNetwork() {
			return this.tvNetwork;
		}
		protected String getResourceId() {
			return this.resourceId;
		}
		protected String getNamespace() {
			return this.namespace;
		}
		protected String getVChip() {
			return this.vChip;
		}
		protected String getMpaa() {
			return this.mpaa;
		}
		protected String getAssetTitle() {
			return this.assetTitle;
		}
		public static TveAssetResource newInstanceForVChip(
				String inTvNetwork, String inResourceId, String inNamespace,
				String inVChip, String inAssetTitle) {
			checkTvNetworkResourceIdNamespace(inTvNetwork, inResourceId, inNamespace);
			if(StringUtility.isStringEmpty(inVChip) || StringUtility.isStringEmpty(inAssetTitle)){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			return new TveAssetResource(
					inTvNetwork, inResourceId, inNamespace, inVChip, null, inAssetTitle);
		}
		
		public static TveAssetResource newInstanceForMpaa(
				String inTvNetwork, String inResourceId, String inNamespace,
				String inMpaa, String inAssetTitle) {
			checkTvNetworkResourceIdNamespace(inTvNetwork, inResourceId, inNamespace);
			if(StringUtility.isStringEmpty(inMpaa) || StringUtility.isStringEmpty(inAssetTitle)){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			return new TveAssetResource(
					inTvNetwork, inResourceId, inNamespace, null, inMpaa, inAssetTitle);
		}
		
		private static void checkTvNetworkResourceIdNamespace(String inTvNetwork, String inResourceId, String inNamespace) {
			//this function was added to reduce 'cyclomatic complexity' (detected by Sonar in Jenkins)
			if(StringUtility.isStringEmpty(inTvNetwork)
					|| StringUtility.isStringEmpty(inResourceId)
					|| StringUtility.isStringEmpty(inNamespace)){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
		}
	}
	/**
	 * Method to create XACML channel request to be used with OAuth.
	 * 
	 * <br>Note that for OAuth the XACML request does not require subjectId</br>
	 * 
	 * @param channelName The name of the channel for which XACML request is to be made
	 * @param action Enum for Action to be performed over the channel
	 * 
	 * @return The XACML channel request for OAuth
	 * @see com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions
	 */
	public String createChannelRequestForOAUTH(String channelName, Actions action){
		LOGGER.info("OAUTH Channel Request cretaion request comes with assetTitle: {}", channelName);
		
		if(StringUtility.isStringEmpty(channelName) || action==null){
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}
		String reqStr = XacmlStaticBuilder.getChannelRequest(null, channelName, action.getValue());
		
		LOGGER.info("OAUTH Channel Request cretaion completed with generated request : {}", reqStr);
		return reqStr;
	}
	
	
	
	/**
	 * Method to create XACML channel request to be used with OAuth.
	 * 
	 * <br>Note that for OAuth the XACML request does not require subjectId</br>
	 * 
	 * @param channelNames The names of the channel for which XACML request is to be made
	 * @param action Enum for Action to be performed over the channel
	 * 
	 * @return The XACML channel request for OAuth
	 * @see com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions
	 */
	public String createChannelRequestForOAUTH(String[] channelNames, Actions action){
		LOGGER.info("OAUTH Channel Request creation request comes with assetTitle: {}", Arrays.toString(channelNames));
		
		if (channelNames == null) {
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}
		String actionValue = ((action != null)?(action.getValue()):null);
		String reqStr = XacmlStaticBuilder.getChannelRequest(null, channelNames, actionValue);
		
		LOGGER.info("OAUTH Channel Request creation completed with generated request : {}", reqStr);
		return reqStr;
	}
	
	
	
	/**
	 * Method to create XACML asset request to be used with OAuth (Movie)
	 * 
	 * <br>Note that for OAuth the XACML request does not require subjectId</br>
	 * 
	 * @param inTarArray The array containing Resource requests
	 * @param action Enum for Action to be performed over the asset
	 * 
	 * @return The XACML asset request for OAuth
	 * @see com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions
	 */
	public String createAssetRequestForOAUTH(TveAssetResource[] inTarArray, Actions action) {
		if (inTarArray == null) {
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		} else {
			for (int i = 0; i < inTarArray.length; i++) {
				TveAssetResource nextTar = inTarArray[i];
				if (nextTar != null) {
				LOGGER.info("OAUTH Asset Request creation request comes with Resource(" + i
						+ ") - tvNetwork: {}, resource id: {}, namespace: {}, vChip: {}, mpaa: {}, and asset title: {}", 
						nextTar.getTvNetwork(), nextTar.getResourceId(), nextTar.getNamespace(),
						nextTar.getVChip(), nextTar.getMpaa(), nextTar.getAssetTitle());
				} else {
					LOGGER.info("OAUTH Asset Request creation request comes with Resource(" + i + ") - null");
				}
			}
		}
		String actionValue = ((action != null)?(action.getValue()):null);
		String reqStr = XacmlStaticBuilder.getAssetRequest(null, inTarArray, actionValue);
		
		LOGGER.info("OAUTH Asset Request creation completed with generated request : {}", reqStr);
		return reqStr;
	}
	
	/**
	 * Method to create SAML signed XACML channel request
	 * 
	 * @param channelName The name of the channel
	 * @param action Enum for Action to be performed over the asset
	 * @param userId User name for authentication
	 * @param password User password for authentication
	 * @param samlResponseUrl The SAML request URL to the IDP such as 
	 *        https://idp-qa4.comcast.net/idp/startSSO.ping?PartnerSpId=<clientId>
	 * @param destinationUrl The XACML TVE end-point such as 
	 *        https://login-qa4.comcast.net/api/xacml/soap/tve 
	 * @param clientId The clientId
	 * @param tveKeyStoreAlias TVE key store alias to retrieve client secret
	 * @param tveKeyStorePassword TVE key password to retrieve client secret
	 * @param tveKeyStoreEntryPassword TVE key store entry password to retrieve client secret
	 * 
	 * @return SAML signed XACML request for channel
	 * @see com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions
	 */
	public String createChannelRequestForSAML(	String channelName, 
												Actions action, 
												String userId, 
												String password, 
												String samlResponseUrl, 
												String destinationUrl, 
												String clientId,
												String tveKeyStoreAlias,
												String tveKeyStorePassword,
												String tveKeyStoreEntryPassword) {
		LOGGER.info("SAML Channel Request creation request comes with channel name: {}, user id: {}, samlResponseUrl: {}, destinationUrl: {}, "
				+ "clientId: {} and tveKeyStoreAlias: {}", channelName, userId, samlResponseUrl, destinationUrl, clientId, tveKeyStoreAlias);

		if(StringUtility.isStringEmpty(channelName) || action==null){
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}
		String subjectId = helper.fetchSAMLResponseForTVE(samlResponseUrl+clientId, userId, password);
		if(StringUtility.isStringEmpty(subjectId)){
			throw new IllegalStateException("Unable to create Subject id!!!");
		}
		String sigId = ICimaCommonConstants.ID_PREFIX_XACML_AUTH_DECESION_QUERY + MiscUtility.generateUniqueId();
		String sigBody = XacmlStaticBuilder.generateXACMLAuthzDecisionQuery(XacmlStaticBuilder.getChannelRequest(subjectId, channelName, action.getValue()), sigId, destinationUrl, clientId);
		
		KeyPair keyPair = signatureGen.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, tveKeyStoreAlias, tveKeyStorePassword, tveKeyStoreEntryPassword, XMLSignatureGenerator.KeyStoreType.JKS);
		if(keyPair == null){
			throw new IllegalStateException("KeyPair not found from TVE keystore!!!");
		}
		String signature = signatureGen.generateXMLSignature(sigBody, sigId, keyPair, XMLSignatureGenerator.SignatureType.TVE);
		if(signature == null){
			throw new IllegalStateException("Signature not generated!!!");
		}
		signature = signature.replaceAll(SAML_SIGNATURE_REPLACEABLE_REGEX, SAML_SIGNATURE_REPLACING);
		String reqStr = XacmlStaticBuilder.generateXacmlFinalRequestForSAML(sigBody, signature);
		
		LOGGER.info("SAML Channel Request cretaion completed with generated request : {}", reqStr);
		return reqStr;
	}
	
	
	/**
	 * Method to create SAML signed XACML asset request
	 * 
	 * @param tvNetwork The TV Network
	 * @param resourceId The resourceId
	 * @param namespace  The namespace
	 * @param vChip The asset rating
	 * @param assetTitle The asset title
	 * @param action Enum for Action to be performed over the asset
	 * @param userId User name for authentication
	 * @param password User password for authentication
	 * @param samlResponseUrl he SAML request URL to the IDP such as 
	 *        https://idp-qa4.comcast.net/idp/startSSO.ping?PartnerSpId=<clientId>
	 * @param destinationUrl The XACML TVE end-point such as 
	 *        https://login-qa4.comcast.net/api/xacml/soap/tve 
	 * @param clientId The clientId
	 * @param tveKeyStoreAlias TVE key store alias to retrieve client secret
	 * @param tveKeyStorePassword TVE key password to retrieve client secret
	 * @param tveKeyStoreEntryPassword TVE key store entry password to retrieve client secret
	 * 
	 * @return SAML signed XACML request for asset
	 * @see com.comcast.test.citf.common.xacml.XACMLRequestBuilder.Actions
	 */
	public String createAssetRequestForSAML(	String tvNetwork, 
												String resourceId, 
												String namespace, 
												String vChip, 
												String assetTitle,
												Actions action, 
												String userId, 
												String password, 
												String samlResponseUrl, 
												String destinationUrl, 
												String clientId,
												String tveKeyStoreAlias,
												String tveKeyStorePassword,
												String tveKeyStoreEntryPassword) {
		LOGGER.info("SAML Asset Request creation request comes with tvNetwork: {}, resource id: {}, namespace: {}, vChip: {}, asset title: {}, "
				+ "user id: {}, samlResponseUrl: {}, destinationUrl: {}, clientId: {} and tveKeyStoreAlias: {}", 
				tvNetwork, resourceId, namespace, vChip, assetTitle, userId, samlResponseUrl, destinationUrl, clientId, tveKeyStoreAlias);

		if(StringUtility.isStringEmpty(tvNetwork) || StringUtility.isStringEmpty(resourceId) || StringUtility.isStringEmpty(namespace) || 
				StringUtility.isStringEmpty(vChip) || StringUtility.isStringEmpty(assetTitle) || action==null){
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		}
		String subjectId = helper.fetchSAMLResponseForTVE(samlResponseUrl+clientId, userId, password);
		if(StringUtility.isStringEmpty(subjectId)){
			throw new IllegalStateException("Unable to create Subject id!!!");
		}
		String sigId = ICimaCommonConstants.ID_PREFIX_XACML_AUTH_DECESION_QUERY + MiscUtility.generateUniqueId();

		String sigBody = XacmlStaticBuilder.generateXACMLAuthzDecisionQuery(
				XacmlStaticBuilder.getAssetRequest(
						subjectId,
						new TveAssetResource[]{TveAssetResource.newInstanceForVChip(tvNetwork, resourceId, namespace, vChip, assetTitle)},
						action.getValue()),
				sigId, destinationUrl, clientId);

		KeyPair keyPair = signatureGen.getKeyPairFromKeyStore(ICimaCommonConstants.JAVA_KEY_STORE_TVE, tveKeyStoreAlias, tveKeyStorePassword, tveKeyStoreEntryPassword, XMLSignatureGenerator.KeyStoreType.JKS);
		if(keyPair == null){
			throw new IllegalStateException("KeyPair not found from TVE keystore!!!");
		}
		String signature = signatureGen.generateXMLSignature(sigBody, sigId, keyPair, XMLSignatureGenerator.SignatureType.TVE);
		if(signature == null){
			throw new IllegalStateException("Signature not generated!!!");
		}
		signature = signature.replaceAll(SAML_SIGNATURE_REPLACEABLE_REGEX, SAML_SIGNATURE_REPLACING);

		String reqStr = XacmlStaticBuilder.generateXacmlFinalRequestForSAML(sigBody, signature);

		LOGGER.info("SAML Asset Request creation completed with generated request : {}", reqStr);
		return reqStr;
	}
	
	
	@Autowired
	private XacmlHelper helper;
	
	@Autowired
	private XMLSignatureGenerator signatureGen;
	
	private static final String SAML_SIGNATURE_REPLACEABLE_REGEX = ">(\n|\\s+)<";
	private static final String SAML_SIGNATURE_REPLACING = "><";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XACMLRequestBuilder.class);
}
