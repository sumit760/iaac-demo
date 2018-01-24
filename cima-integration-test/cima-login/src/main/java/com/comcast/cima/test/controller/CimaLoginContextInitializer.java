package com.comcast.cima.test.controller;

import com.comcast.test.citf.core.init.CoreContextInitilizer;

/**
 * Contains context file name of cima-login and will be used during context initialization
 * 
 * @author Abhijit Rej (arej001c)
 * @since June 2015
 *
 */
public class CimaLoginContextInitializer extends CoreContextInitilizer{

	private static final String CONTEXT_FILE_NAME = "Cima-Login-Spring-Context.xml";
	
	public static void initializeContext(){
		getContextList().add(CONTEXT_FILE_NAME);
		CoreContextInitilizer.initializeContext();
	}

}
