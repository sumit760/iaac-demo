package com.comcast.test.citf.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class for Coding/Decoding
 * @author arej001c, spal004c
 */
public class CodecUtility {
	
	/**
	 * Enumeration for algorithm type to be used in signature
	 */
	public enum AlgorithmType{
		HMAC_SHA1_ALGORITHM("HmacSHA1"),
		HMAC_SHA256_ALGORITHM("HmacSHA256");
		
		private final String value;

        AlgorithmType(final String value) {
            this.value = value;
        }
	}
	
	/**
	 * Returns the digital signature of the payload based on the environment.
	 * 
	 * @param payLoad
	 * 					The payload to sign.
	 * @param key
	 * 					The key to be used in signature.
	 * @param environment
	 * 					The environment (QA, STAGE etc.)
	 * @return
	 * 					Signed payload.
	 * @throws NoSuchAlgorithmException, InvalidKeyException
	 */
	public static String getDigitalSignature(String payLoad, String key, String environment) throws NoSuchAlgorithmException, InvalidKeyException{
		byte[] b = ICommonConstants.ENVIRONMENT_STAGE.equals(environment) ?
				CodecUtility.generateHMACSHA1Signature(payLoad, key, CodecUtility.AlgorithmType.HMAC_SHA1_ALGORITHM, true) : 
					CodecUtility.generateHMACSHA1Signature(payLoad, key, CodecUtility.AlgorithmType.HMAC_SHA1_ALGORITHM, false);
		
		return CodecUtility.encodeURL(new String(Base64.encodeBase64(b)), ICommonConstants.ENCODING_UTF8);
	}
	

	/**
	 * Returns the HMACSHA1 signature of the payload.
	 * 
	 * @param data
	 * 				Data to sign.
	 * @param key
	 * 				key to be used in signature.
	 * @param algoType
	 * 				The signing algorithm type.
	 * @param isByteArrayNeeded
	 * 				boolean flag. 
	 * 				True means the signing key is Base64 decoded and used.
	 *              False means signing key is used directly.
	 * @return	
	 * 				Byte array of the signature.
	 * @throws NoSuchAlgorithmException, InvalidKeyException
	 */
	public static byte[] generateHMACSHA1Signature(String data, String key, AlgorithmType algoType, boolean isByteArrayNeeded) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signingKey = isByteArrayNeeded ? new SecretKeySpec(Base64.decodeBase64(key), algoType.value) : new SecretKeySpec(key.getBytes(), algoType.value);
		Mac mac = Mac.getInstance(algoType.value);
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		  
		LOGGER.info("Signature has been generated for input[data: {}, key: {} and algoType: {}] and result: {}", 
				  					data, key, algoType, rawHmac);
		return rawHmac;
	}
	
	/**
	 * Returns the URL in encoded format.
	 * 
	 * @param data
	 * 				URL to encode.
	 * @param encodingType
	 * 				The encoding type.
	 * @return
	 * 				Encoded URL.
	 */
	public static String encodeURL(String data, String encodingType) {
		String res=null;
		try {
			res = URLEncoder.encode(data, encodingType);
		} 
		catch (UnsupportedEncodingException e) {
			LOGGER.error("Error occurred while encoding URL: {}", e);
		}
		return res;
	}
	
	/**
	 * Returns the decoded URL.
	 * 
	 * @param data 
	 * 				Encoded URL.
	 * @param encodingType
	 * 				Encoding type.
	 * @return
	 * 				Decoded URL.
	 */
	public static String decodeURL(String data, String encodingType) {
		String res=null;
		try {
			res = URLDecoder.decode(data, encodingType);
		} 
		catch (UnsupportedEncodingException e) {
			LOGGER.error("Error occurred while decoding URL: {}", e);
		}
		return res;
	}
	
	/**
	 * Returns the Base64 encoded byte array.
	 * 
	 * @param input
	 * 				The byte array.
	 * @return
	 * 				The base64 encoded byte array.
	 */
	public static byte[] getBase64EncodedByteArray(byte[] input) {
		return Base64.encodeBase64(input);
	}
	
	/**
	 * Returns the Base64 decoded string.
	 * 
	 * @param input	
	 * 				Base64 encoded string.
	 * @param encodingType
	 * 				Encoding type.
	 * @return
	 * 				The base64 decoded string.	
	 * @throws UnsupportedEncodingException
	 */
	public static String getBase64DecodedString(String input, String encodingType) throws UnsupportedEncodingException{
		return new String(Base64.decodeBase64(input), encodingType);
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodecUtility.class);
}
