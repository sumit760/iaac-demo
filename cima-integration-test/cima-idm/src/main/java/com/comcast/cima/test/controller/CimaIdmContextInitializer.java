package com.comcast.cima.test.controller;

import com.comcast.test.citf.core.init.CoreContextInitilizer;

/**
 * Contains context file name of cima-idm and will be used during context initialization
 * 
 * @author Abhijit Rej (arej001c)
 * @since September 2015
 *
 */
public class CimaIdmContextInitializer extends CoreContextInitilizer{

	private static final String CONTEXT_FILE_NAME = "Cima-IDM-Spring-Context.xml";

	public static void initializeContext(){
		getContextList().add(CONTEXT_FILE_NAME);
		CoreContextInitilizer.initializeContext();
	}
}
