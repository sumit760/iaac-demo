package com.comcast.test.citf.common.ui.router;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.google.common.base.CharMatcher;

/**
* Abstract class for UI page routing.
* 
* @author Abhijit Rej (arej001c)
* @since October 2015
* 
*/
public abstract class AbstractPageRouter {

	protected Map<String, Class<? extends SeleniumPageObject>> titleIdentityMap; 
	protected Map<String, Class<? extends SeleniumPageObject>> contentIdentityMap;
	
	protected String pageError = null;
	
	
	/**
	 * Method to identify the next UI page on scanning the HTML page content.
	 * <p>
	 * Note that this method has to be used with CITF Page Object Model to uniquely
	 * identify the next UI page or the page object class.
	 * 
	 * @param pageSource The HTML page source
	 * 
	 * @return The Page Object Model class to be responsible for the page under consideration.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends SeleniumPageObject> identifyClass(String pageSource){
		this.pageError = null;
		
		if(pageSource.contains(ICommonConstants.UI_PAGE_ERROR_INTERNAL_ERROR)){
			this.pageError=ICommonConstants.EXCEPTION_UI_INTERNAL_SERVER_ERROR;
			return null;
		}
		else if(pageSource.contains(ICommonConstants.UI_PAGE_ERROR_PAGE_NOT_FOUND)){
			this.pageError=ICommonConstants.EXCEPTION_UI_SERVER_UNAVAILABLE;
			return null;
		}
		
		pageSource = pageSource.replaceAll("\\s+", ICommonConstants.BLANK_SPACE_STRING);
		
		if(titleIdentityMap != null){
			String title =  StringUtility.regularExpressionChecker(pageSource, REGEX_TITLE, 1);
			if (title != null) {
				title = title.trim();
				title = CharMatcher.ASCII.retainFrom(title);
				if (titleIdentityMap.containsKey(title)) {
					logger.info("Match found with title = {}", title);
					return titleIdentityMap.get(title);
				} else {
					logger.info("Match not found with title = {}", title);
				}
			}
		}
		
		if(contentIdentityMap != null){
			for(Map.Entry<String, Class<? extends SeleniumPageObject>> entry : contentIdentityMap.entrySet()){
				String identity = entry.getKey();
				Class<? extends SeleniumPageObject> value = entry.getValue();
				
				if(!identity.contains(SEPARATOR)){
					if(pageSource.contains(identity)){
						logger.info("Match found with content {}", identity);
						return value;
					} 
				}
				else{
					Set<String> iTokens = StringUtility.getTokensFromString(identity, SEPARATOR);
					boolean isValid = true;
					for(String iToken : iTokens){
						if(!pageSource.contains(iToken)){
							isValid = false;
							break;		
						}
					}
					
					if(isValid){
						logger.info("Match found with multi-content {}", identity);
						return value;
					}
						
				}
			}
		}
		
		return null;
	}

	public String getPageError() {
		return pageError;
	}
	
	
	private static final String REGEX_TITLE = "<title>(.*?)</title>";
	protected static final String SEPARATOR = "#~$^";
	protected Logger logger;
}
