package com.comcast.test.citf.core.runtime;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.CodecUtility;
import com.comcast.test.citf.common.util.FileUtility;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;

/**
 * This class generates Cucumber runner class and load those classes at runtime.
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 * 
*/
public class ClassGenerator {

	/**
	 * Generates Cucumber Runner class (i.e. java file), compiles it and load the class at runtime.
	 * 
	 * @param className 
	 * 			Name of the class file need to generate
	 * @param fileDirectory 
	 * 			Path where the files needs to create
	 * @param threadName 
	 * 			Name of thread which will used in tags annotation
	 * @param features 
	 * 			Name of the features
	 * @param glues 
	 * 			Name of the glues
	 * @return Newly created and loaded runner class
	 */
	@SuppressWarnings("rawtypes")
	public static Class generateRunnerClass(String className, String fileDirectory, String threadName, List<String> features, List<String> glues) {
		Class runner = null;
		
		try{
			String fullClassPath = StringUtility.appendStrings(fileDirectory, className, ICommonConstants.EXTN_JAVA);
			
			LinkedList<String> content = new LinkedList<String>();
			content.add(RUNNER_CLASS_CONTENT_IMPORT_LINE1);
			content.add(RUNNER_CLASS_CONTENT_IMPORT_LINE2);
			content.add(RUNNER_CLASS_CONTENT_IMPORT_LINE3);
			content.add(RUNNER_CLASS_CONTENT_ANNO_RUNW);
			content.add(RUNNER_CLASS_CONTENT_ANNO_CUOPT_START);
			
			content.add(joinStringsToLine(RUNNER_CLASS_CONTENT_ANNO_CUOPT_FEATURE_START, features));
			content.add(joinStringsToLine(RUNNER_CLASS_CONTENT_ANNO_CUOPT_GLUE_START, glues));
			content.add(StringUtility.appendStrings(RUNNER_CLASS_CONTENT_ANNO_CUOPT_TAGS_START, ICommonConstants.SINGLE_DOUBLE_QUOTE, threadName, ICommonConstants.SINGLE_DOUBLE_QUOTE, ICommonConstants.END_CURLY_BRACE));
			
			content.add(StringUtility.appendStrings(RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART1, className, RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART2, className, RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART3));
			content.add(RUNNER_CLASS_CONTENT_ANNO_CUOPT_END);
			content.add(StringUtility.appendStrings(RUNNER_CLASS_CONTENT_CLASS_START, className, ICommonConstants.BLANK_SPACE_STRING, ICommonConstants.START_CURLY_BRACE));
			content.add(ICommonConstants.END_CURLY_BRACE);
			
			LOGGER.debug("{} is ready to create with content ::\n {}", fullClassPath, content);
			
			if(FileUtility.createFile(fullClassPath, content)){					//Creating java file
				LOGGER.info("{} created successfully for thread {}", fullClassPath, threadName);
				
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				
				URLClassLoader classLoader=(URLClassLoader)Thread.currentThread().getContextClassLoader();
				StringBuilder path=new StringBuilder();
				
				for(URL url : classLoader.getURLs()) {
				   if (path.length() > 0) {
				     path.append(File.pathSeparator);
				   }
				   
				   String decodedPath =  CodecUtility.decodeURL(url.getPath(), ICommonConstants.ENCODING_UTF8);
				   path.append(new File(decodedPath).getAbsolutePath());
				}
				
				List<String> options=Arrays.asList(CLASSPATH, path.toString(), DASH_S, fileDirectory, fullClassPath);
				int compilationResult=compiler.run(null,null,null,options.toArray(new String[options.size()]));
				
				if(compilationResult==0){
					addURLInClassLoader(classLoader, new File(fileDirectory).toURI().toURL());
					runner = Class.forName(className, true, classLoader);
					LOGGER.info("{} has been successfully compiled and added to classpath.", fullClassPath);
				}
			}
			
		}catch(IOException e){
			LOGGER.error("IOException occurred while generating dynamic class : ", e);
		}catch(IllegalAccessException e){
			LOGGER.error("IllegalAccessException occurred while generating dynamic class : ", e);
		}catch(InvocationTargetException e){
			LOGGER.error("InvocationTargetException occurred while generating dynamic class : ", e);
		}catch(NoSuchMethodException e){
			LOGGER.error("NoSuchMethodException occurred while generating dynamic class : ", e);
		}catch(ClassNotFoundException e){
			LOGGER.error("ClassNotFoundException occurred while generating dynamic class : ", e);
		}
		return runner;
	}
	
	/**
	 * Loads any compiled class file.
	 * 
	 * @param classDirectory 
	 * 			The folder/directory path which contains the class file
	 * @param className 
	 * 			Name of the class (excluding any extension). e.g. "Thread1Runner"
	 * @return the loaded class
	 * @throws IOException, ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public static Class loadClass(String classDirectory, String className) throws IOException, ClassNotFoundException{
		
		if(classLoader == null) {
			classLoader = URLClassLoader.newInstance(new URL[] { new File(classDirectory).toURI().toURL() });
		}
		
		return Class.forName(className, true, classLoader);
	}
	
	
	//************************** Private variables & methods **********************************************
	
	/**
	 * An utility method to join Cucumber runner annotation specific Strings in a single line.
	 * 
	 * @param startElement 
	 * 			Starting of the line
	 * @param elements 
	 * 			List of Strings which need to be joined
	 * @return accumulated String
	 */
	private static String joinStringsToLine(String startElement, List<String> elements){
		
		StringBuilder sbf = new StringBuilder();
		int limit = elements.size()-1;
		int currentIndex = 0;
		
		sbf.append(startElement);
		for(String element : elements){
			sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
			sbf.append(element);
			
			if(currentIndex<limit) {
				sbf.append(RUNNER_CLASS_CONTENT_DOUBLE_QUOTES_END_WITH_COMMA_SPACE);
			} else {
				sbf.append(ICommonConstants.SINGLE_DOUBLE_QUOTE);
			}
			currentIndex++;
		}
		sbf.append(ICommonConstants.END_CURLY_BRACE);
		
		return sbf.toString();
	}
	
	/**
	 * An utility method to add URL in URLClassLoader
	 * 
	 * @param classLoader 
	 * 			Instance of URLClassLoader  @see java.net.URLClassLoader
	 * @param url 
	 * 			URL of the provided file path  @see java.net.URL
	 * @throws NoSuchMethodException, InvocationTargetException, IllegalAccessException
	 */
	private static void addURLInClassLoader(URLClassLoader classLoader, URL url) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
		method.setAccessible(true);
	    method.invoke(classLoader, url);
	}
	
	private static URLClassLoader classLoader = null;
	
	private static final String RUNNER_CLASS_CONTENT_IMPORT_LINE1 					= "import org.junit.runner.RunWith;";
	private static final String RUNNER_CLASS_CONTENT_IMPORT_LINE2 					= "import cucumber.api.CucumberOptions;";
	private static final String RUNNER_CLASS_CONTENT_IMPORT_LINE3 					= "import cucumber.api.junit.Cucumber;";
	private static final String RUNNER_CLASS_CONTENT_ANNO_RUNW 						= "@RunWith(Cucumber.class)";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_START 				= "@CucumberOptions(";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_FEATURE_START 		= " features={";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_GLUE_START 			= " ,glue={";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_TAGS_START			= " ,tags={";
	
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART1 		= " ,plugin = {\"pretty\", \"html:target/cucumber";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART2  		= "\", \"json:target/cucumber";
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_PLUGIN_PART3  		= "/cucumber-result.json\"}";
	
	private static final String RUNNER_CLASS_CONTENT_ANNO_CUOPT_END 				= " ,strict=true)";
	private static final String RUNNER_CLASS_CONTENT_CLASS_START 					= "public class ";
	
	private static final String RUNNER_CLASS_CONTENT_DOUBLE_QUOTES_END_WITH_COMMA_SPACE	= "\", ";
	
	private static final String CLASSPATH = "-classpath";
	private static final String DASH_S = "-s";
	
	@SuppressWarnings("rawtypes")
	private static final Class[] parameters = new Class[]{URL.class};
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassGenerator.class);
}
