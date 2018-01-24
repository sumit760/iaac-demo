package com.comcast.test.citf.common.init;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

/**
 * Class to initialize the context of the common module in CITF. 
 * 
 * @author Abhijit Rej
 *
 */
public class CommonContextInitializer {

	private static final String CONTEXT_FILE_NAME = "Common-Spring-Context.xml";
	
	/**
	 * Initializes spring contexts
	 */
	protected static void initializeContext(){
		getContextList().add(CONTEXT_FILE_NAME);
		factory = new ClassPathXmlApplicationContext(contextFileNames.toArray(new String[contextFileNames.size()]));
		((ClassPathXmlApplicationContext)factory).registerShutdownHook();
		
		LOGGER.info("Spring contexts[{}] have been successfully initialized", Arrays.toString(contextFileNames.toArray()));		
	}
	
	/**
	 * This is created for unit test
	 */
	public static void initializeTestContexts(String otherContext){
		if(otherContext!=null){
			getContextList().add(otherContext);
		}
		initializeContext();
	}
	
	/**
	 * Returns the bean object from the context.
	 * 
	 * @param beanName
	 * 					The name of the bean.
	 * @return Object
	 * 					The bean object.
	 */
	public static Object getBean(String beanName){
		return factory.getBean(beanName);
	}
	
	/**
	 * Loads the files present in resource folder from context and returns as a stream.
	 * 
	 * @param classPathFileName 
	 * 							File name in class path.
	 * @return InputStream	
	 *                          The file content as stream.
	 */
	public static InputStream getResource(String classPathFileName){
		Resource resource;
		InputStream resultIS = null;
		
		try{
			resource = factory.getResource(classPathFileName);
			if(resource!=null && resource.exists()){
				resultIS = resource.getInputStream();
			}
		}catch(Exception e){
			LOGGER.error("Error occurred while loading Resource {} : ", classPathFileName, e);
		}		
		return resultIS;
	}
	
	/**
	 * Destroys the spring context xml.
	 */
	public static void destroyContext(){
		try{
			ConfigurableApplicationContext con = (ConfigurableApplicationContext)factory;
			con.close();
			factory = null;
		}
		catch(Exception e){
			LOGGER.error("Spring context closing error - ", e);
		}
		contextFileNames = null;
	}
	
	
	protected static Set<String> getContextList(){
		if(contextFileNames == null){
			contextFileNames = new HashSet<String>();
		}
		return contextFileNames;
	}
	
	private static Set<String> contextFileNames = null;
	private static ApplicationContext factory = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonContextInitializer.class);
}
