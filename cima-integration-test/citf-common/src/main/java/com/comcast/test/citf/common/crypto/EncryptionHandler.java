package com.comcast.test.citf.common.crypto;

import java.security.Key;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * Handler class to encrypt data based on 'RSA' encryption algorithm.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */

@Service("encryptionHandler")
public class EncryptionHandler {
	
	@Autowired
	@Qualifier("keyStoreManager")
	private KeyStoreManager keyManager = null;
	

	/**
	 * Initialize the encryption handler.
	 * <p>
	 * This method should be called before doing any encryption/decryption using this handler.
	 * 
	 * @param fileName 
	 * 			The java key store file name normally in .jks format.
	 * @param alias 
	 * 			The file alias.
	 * @param storePassword 
	 * 			The key store password.
	 * @param entryPassword 
	 * 			The key store entry password.
	 */
	public void initializeEncrypter(String fileName, String alias, String storePassword, String entryPassword){
		getKeyManager().setAlias(alias);
		getKeyManager().setFileName(fileName);
		getKeyManager().setStorePassword(storePassword);
		getKeyManager().setEntryPassword(entryPassword);
	}
	
	
	/**
	 * Encrypts the input data and returns the encrypted data.
	 * 
	 * @param input 
	 * 			The data to encrypt.
	 * @return The encrypted data
	 */
	public byte[] encryptData(String input){
		byte[] encryptedResult = null;
		
		try{
			if(StringUtility.isStringEmpty(input)){
				throw new IllegalArgumentException("[encryptData] input cannot be empty!!!");
			}
			byte[] intext = input.getBytes();
			encryptedResult = getCipher(Cipher.ENCRYPT_MODE, getKeyManager().retrievePublicKey()).doFinal(intext);
			 
		}catch(Exception e){
			LOGGER.error("Error occurred while encrypting data: ", e);
		}
		
		LOGGER.debug("Data has been encrypyed from input: {} to output: {}", input, encryptedResult!=null?"Not null":"null");
		return encryptedResult;
	}
	
	
	/**
	 * Decryptes the data and returns the decrypted data.
	 * 
	 * @param input 
	 * 			The encrypted data.
	 * @return The decrypted data.
	 */
	public String decryptData(byte[] input) {	
		return this.decryptData(input, getKeyManager().retrievePrivateKey());
	}
	
	
	/**
	 * Decrypt data with the given key.
	 * 
	 * @param input 
	 * 			Encrypted data
	 * @param key 
	 * 			The key to be used for decryption.
	 * @return The decrypted data
	 * 
	 */
	public String decryptData(byte[] input, PrivateKey key) {
		String decryptedResult = null;
		
		try{
			if(input == null || key == null){
				throw new IllegalArgumentException("[decryptData] input cannot be empty!!!");
			}
			byte[] ciphertext = getCipher(Cipher.DECRYPT_MODE, key).doFinal(input);
			decryptedResult = new String(ciphertext, ICommonConstants.ENCODING_UTF8);
			
		}catch(Exception e){
			LOGGER.error("Error occurred while decrypting data: ", e);
		}
		
		LOGGER.debug("Data has been decrypyed from input: {} to output: {}", input, decryptedResult);
		return decryptedResult;
	}
	
	
	/**
	 * Returns the key store manager.
	 * 
	 * @return The Key Store manager instance.
	 * 
	 */
	public KeyStoreManager getKeyManager() {
		return keyManager;
	}

	/**
	 * Sets the key store manager.
	 * 
	 * @param keyManager 
	 * 			The key store manager to set.
	 * 
	 */
	public void setKeyManager(KeyStoreManager keyManager) {
		this.keyManager = keyManager;
	}
	
	/**
	 * Provides Cipher
	 * 
	 * @param mode
	 * 			Mode value
	 * @param key
	 * 			Key 	@see java.security.Key
	 * @return Cipher	@see javax.crypto.Cipher
	 */
	private Cipher getCipher(int mode, Key key) {
		Cipher cipher = null;
		try{
			cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM_RSA);
			cipher.init(mode, key);
		}catch(Exception e){
			LOGGER.error("Error occurred while getting Ciphar : ", e);
		}
		return cipher;
	}
		
	private static final String ENCRYPTION_ALGORITHM_RSA = "RSA";
	private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionHandler.class);
}
