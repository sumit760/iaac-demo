package com.comcast.test.citf.core.init;

import com.comcast.test.citf.common.init.CommonContextInitializer;

/**
 * Contains context file name of citf-core and will be used during context initialization
 * 
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 *
 */

public class CoreContextInitilizer extends CommonContextInitializer{

	private static final String CONTEXT_FILE_NAME = "Core-Spring-Context.xml";
	
	public static void initializeContext(){
		getContextList().add(CONTEXT_FILE_NAME);
		CommonContextInitializer.initializeContext();
	}

}
