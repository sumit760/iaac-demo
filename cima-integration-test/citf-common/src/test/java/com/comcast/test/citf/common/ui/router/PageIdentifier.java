package com.comcast.test.citf.common.ui.router;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.ui.router.AbstractPageRouter;


/**
* @author Abhijit Rej (arej001c)
* @since October 2015
* 
*/
@Service("pageIdentifier")
@Scope("singleton")
public class PageIdentifier extends AbstractPageRouter{
	
	public PageIdentifier(){
		titleIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		titleIdentityMap.put("Sign in to Comcast", 												   											 	SignInToXfinity.class);
		titleIdentityMap.put("Sign in to XFINITY TV", 											   			  								 	SignInToXfinity.class);
		titleIdentityMap.put("XFINITY", 											   			  								 				SignInToXfinity.class);
		
		contentIdentityMap = new HashMap<String, Class<? extends SeleniumPageObject>>();
		contentIdentityMap.put("http://www.xfinity.com/upgrade-center/customer-deals", 															SignInToXfinity.class);
		
		super.logger = this.logger;
	}
	
	private static Logger logger = LoggerFactory.getLogger(PageIdentifier.class);
}
