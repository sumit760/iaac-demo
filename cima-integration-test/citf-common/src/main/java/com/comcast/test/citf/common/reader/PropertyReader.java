package com.comcast.test.citf.common.reader;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * Class to read properties file.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
public class PropertyReader {
	
	/**
	 * Load properties file from class path resource.
	 * 
	 * @param clsPathResource
	 * 				Class path resource.
	 * @return The properties object.
	 * @throws IOException
	 */
	public static Properties loadPropertyFile(String clsPathResource) throws IOException{
		Resource resource = new ClassPathResource(clsPathResource);
		return PropertiesLoaderUtils.loadProperties(resource);
	}

	/**
	 * Returns the property value given the prop file as string and the key.
	 * 
	 * @param propertyFileName
	 * 				The .prop file as string
	 * @param key
	 * 				The key
	 * @return The value.
	 * @throws IOException
	 */
	public static String getProperty(String propertyFileName, String key) throws IOException{
		return loadPropertyFile(propertyFileName).getProperty(key);		
	}	
	
	/**
	 * Returns the property value given the prop file as properties object and the key.
	 * 
	 * @param propertyFile
	 * 				The .prop file as properties object.
	 * @param key
	 * 				The key.
	 * @return The value.
	 */
	public static String getProperty(Properties propertyFile, String key) {
		return propertyFile.getProperty(key);		
	}

	/**
	 * Returns the key set of the properties file.
	 * 
	 * @param propertyFile
	 * 					The .prop file as Properties object.
	 * @return The set of keys.
	 */
	public static Set<Object> getPropertyKeySet(Properties propertyFile) {
		return propertyFile.keySet();		
	}
}