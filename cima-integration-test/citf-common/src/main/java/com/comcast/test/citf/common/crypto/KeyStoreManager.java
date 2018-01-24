package com.comcast.test.citf.common.crypto;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.init.CommonContextInitializer;

/**
 * This class will be used to retrieve public key while decrypting and retrieve primary key while encrypting. Both the keys will be kept on keystore file 
 * inside the implementation project's resources directory.
 * The keystore certificate has default expiry time of 20 years (if it has not been changed), so these keys along with the certificate need
 * to be re-generated before it got expired.
 * To generate the this the below command may be used 
 * 
 * keytool -genkey -keyalg RSA -alias <ALIAS_NAME> -keystore <KEYSTORE_FILE_PATH>/keystore.jks -storepass <STORE_PASSWORD> -validity <VALIDITY_IN_DAYS> -keysize 2048 -keypass <ENTRY_PASSWORD>
 *
 * This will ask for a few more information.
 * 
 * After generating the .jks file, the file needs to kept on same location replacing the existing one. In case the arguments has been changed during 
 * generation, then those need to be updated in database.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 * 
 */
@Service("keyStoreManager")
public class KeyStoreManager {
	
	/**
	 * Retrieves the private key from the key store. The key store and all related parameters 
	 * needs to be set prior to using this method.
	 * 
	 * @return The private key from the key store.
	 */
	public PrivateKey retrievePrivateKey() {
		PrivateKey key = null;
		try{
			LOGGER.debug("Inputs fileName - {}; alias - {}; storePassword - {} and entryPassword - {}", 
						fileName, alias, storePassword, entryPassword);
		
			if(fileName == null || alias == null || storePassword == null || entryPassword == null){
				throw new IllegalArgumentException("[retrievePrivateKey] Inputs cannot be empty!!!");
			}
			
			KeyStore keyStore = createKeyStore(fileName, storePassword);
			PasswordProtection keyPassword = new PasswordProtection(entryPassword.toCharArray());
			KeyStore.Entry entry = keyStore.getEntry(alias, keyPassword);
			key = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
		}
		catch(Exception e){
			LOGGER.error("Error occurred while retrieving private key: ", e);
		}
		LOGGER.debug("Private Key has been retrieved.");
		return key;
	}
	
	/**
	 * Retrieves the public key from the key store. The key store and all related parameters 
	 * needs to be set prior to using this method.
	 * 
	 * @return The public key from the key store.
	 */
	public PublicKey retrievePublicKey(){
		PublicKey key = null;
		try{
			LOGGER.debug("Inputs fileName - {}; alias - {}; storePassword - {} and entryPassword - {}", 
					fileName, alias, storePassword, entryPassword);

			if(fileName == null || alias == null || storePassword == null || entryPassword == null){
				throw new IllegalArgumentException("[retrievePublicKey] Inputs cannot be empty!!!");
			}

			KeyStore keyStore = createKeyStore(fileName, storePassword);
			PasswordProtection keyPassword = new PasswordProtection(entryPassword.toCharArray());
			KeyStore.Entry entry = keyStore.getEntry(alias, keyPassword);
			key = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
		}
		catch(Exception e){
			LOGGER.error("Error occurred while retrieving public key: ", e);
		}
		LOGGER.debug("Public Key has been retrieved.");
		return key;
	}
	
	/**
	 * Retrieves the certificate from the key store. The key store and all related parameters 
	 * needs to be set prior to using this method.
	 * 
	 * @return The key store certificate.
	 */
	public Certificate retrieveCertificate() {
		Certificate cert = null;
		try{
			if(fileName == null || alias == null || storePassword == null || entryPassword == null){
				throw new IllegalArgumentException("[retrieveCertificate] Inputs cannot be empty!!!");
			}

			KeyStore keyStore = createKeyStore(fileName, storePassword);
			PasswordProtection keyPassword = new PasswordProtection(entryPassword.toCharArray());
			KeyStore.Entry entry = keyStore.getEntry(alias, keyPassword);
			cert = ((KeyStore.PrivateKeyEntry) entry).getCertificate();
		}
		catch(Exception e){
			LOGGER.error("Error occurred while retrieving certificate: ", e);
		}
		LOGGER.info("Certificate has been retrieved.");
		return cert;
	}
	
	/**
	 * Sets key store filename.
	 * 
	 * @param fileName The key store filename to set.
	 * 
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the key store alias.
	 * 
	 * @param alias The key store alias to set.
	 * 
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Sets the key store password.
	 * 
	 * @param storePassword The key store password.
	 */
	public void setStorePassword(String storePassword) {
		this.storePassword = storePassword;
	}

	/**
	 * Sets the key store password.
	 * 
	 * @param entryPassword The key store password to set.
	 */
	public void setEntryPassword(String entryPassword) {
		this.entryPassword = entryPassword;
	}
	
	
	
	
	/************************************ Private methods & variables ********************************************/
	
	private KeyStore createKeyStore(String fileName, String pw) {
		KeyStore keyStore = null;
		try{
			InputStream is = CommonContextInitializer.getResource(fileName);

			keyStore = KeyStore.getInstance(KEY_STORE_FORMAT_JCEKS);
			if (is!=null) {
				keyStore.load(is, pw.toCharArray());
				LOGGER.debug("KeyStore file {} has been loaded successfully.", fileName);
			}
			else{
				throw new IllegalStateException("KeyStore file cannot be loaded!!!");
			}
		}
		catch(Exception e){
			LOGGER.error("Error occurred while creating KeyStore: ", e);
		}
	    return keyStore;
	}

	
	private String fileName = null;			
	private String alias = null; 			
	private String storePassword = null; 	
	private String entryPassword = null; 	
	
	private static final String KEY_STORE_FORMAT_JCEKS = "JKS";
	private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreManager.class);
}