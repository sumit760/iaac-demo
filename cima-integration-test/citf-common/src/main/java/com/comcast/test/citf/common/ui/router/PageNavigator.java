package com.comcast.test.citf.common.ui.router;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;

/**
* Spring service class to facilitate UI page navigation.
* 
* @author Abhijit Rej (arej001c)
* @since October 2015
* 
*/
@Service("pageNavigator")
@Scope("singleton")
public class PageNavigator {
	
	
	/**
	 * Method to identify the next page in the UI flow and get the object instance of the page.
	 * <p>
	 * This method determines the next page object, checks in the object pool whether the object
	 * already exists and if not returns a page object instance of the page. In case, the object
	 * already exists in the pool, the same object instance is returned.
	 * <p>
	 * Note that this method is to be used along with CITF Page Object Model in UI.
	 * 
	 * @param driver Selenium web driver
	 * @param thisPage The current UI page object
	 * @param pageFlowId The page flowId associated with the flow
	 * 
	 * @return The object instance of the next UI page.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public Object getNextPage(	WebDriver driver, SeleniumPageObject thisPage, String pageFlowId){
		return this.getNextPage(driver, thisPage, pageFlowId, null);
	}
	
	
	/**
	 * Method to identify the next page in the UI flow and get the object instance of the page.
	 * The forceDestination param is used to override the routing logic if the destination page
	 * is already known. In that case, the destination page object class is directly provided to 
	 * skip the routing logic that otherwise happens if the next UI page is uniquely known beforehand.
	 * 	 * <p>
	 * This method determines the next page object, checks in the object pool whether the object
	 * already exists and if not returns a page object instance of the page. In case, the object
	 * already exists in the pool, the same object instance is returned.
	 * <p>
	 * Note that this method is to be used along with CITF Page Object Model in UI.
	 * 
	 * @param driver Selenium web driver.
	 * @param thisPage The current page object.
	 * @param pageFlowId The page flowId associated with the flow.
	 * @param forcedDestination The destination page object class.
	 *  
	 * @return The object instance of the next UI page.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public Object getNextPage(	WebDriver driver, SeleniumPageObject thisPage, String pageFlowId, Class forcedDestination){
		LOGGER.info("Entering to fetch next page with driver: {} requester page: {}, pageFlowId: {} and forcedDestination: {}", 
						driver, thisPage, pageFlowId, forcedDestination!=null ? forcedDestination.getClass().getName() : ICommonConstants.BLANK_STRING);
		
		SeleniumPageObject resultPage = null;
		boolean isAlreadyExist = false;
		List<Object> instanceResult = null;
		PageError error = null;
		
		try{
			if(thisPage == null || driver == null){
				error = new PageError(PageError.ERROR_SYSTEM_ERROR, PageError.ERROR_DESCRIPTION_INVALID_NULL_INPUT, thisPage!=null? thisPage.getClass().getName():MESSAGE_NOT_AVAILABLE);
				return error;
			}
		
			String pageSource = driver.getPageSource();
			Class nextClass = identifier.identifyClass(pageSource);
			if(identifier.getPageError()!=null || (nextClass == null && forcedDestination == null)){
				String err = "Not able to identify next Page Object";
				if(identifier.getPageError()!=null){
					err = identifier.getPageError();
				}
				
				error = new PageError(PageError.ERROR_SYSTEM_ERROR, err, thisPage.getClass().getName());
				return error;
			}

			if(forcedDestination == null && thisPage.getClass().getName().equals(nextClass.getName())){
				return new PageError(PageError.ERROR_PAGE_ERROR, "Unknown current page error", thisPage.getClass().getName());
			}
		
			if(pageFlowId==null){
				pageFlowId = PAGE_FLOW_ID_PREFIX + MiscUtility.generateUniqueId();
				LOGGER.warn("Page Flow not provided while trying to get next page from PageNavigator with {}", thisPage.getClass().getName());
			}
		
			if(pageFlows==null){
				pageFlows = new HashMap<String, PageFlow>();
				LOGGER.warn("Page Flow Map is NULL while trying to get next page from PageNavigator with PageFlowId: {}", pageFlowId);
			}
		
			PageFlow pf = pageFlows.get(pageFlowId)!=null ? pageFlows.get(pageFlowId) : 
							new PageFlow(new LinkedList<SeleniumPageObject>(), PAGE_FLOW_DEFAULT_TEST_NAME + getDeafultTestCunt());
		
			instanceResult = forcedDestination!=null ? instantiate(driver, pf, forcedDestination) : instantiate(driver, pf, nextClass);
    	
			if(instanceResult!=null && instanceResult.size()==2){
				if(true==(Boolean)instanceResult.get(0)) {
					isAlreadyExist = true;
				}
				if(instanceResult.get(1)!=null) {
					resultPage = (SeleniumPageObject)instanceResult.get(1);
				}
			}
    	
			if(!isAlreadyExist && resultPage!=null){
				resultPage.setPageFlowId(pageFlowId);
				resultPage.setWindowHandle(driver.getWindowHandle());
				pf.addObject(resultPage);
				pageFlows.put(pageFlowId, pf);
    		
				LOGGER.info("A new object[{}] has been added in PageFlow. {}", resultPage.getClass().getName(), pf.toString());
			}
			
			pf.updatePage(thisPage);
			
		}catch(Exception e){
			LOGGER.error("Error occurred while navigating to next page : ", e);
			return new PageError(PageError.ERROR_SYSTEM_ERROR, e.getMessage(), thisPage!=null? thisPage.getClass().getName():MESSAGE_NOT_AVAILABLE);
		}
    	
		return resultPage;
    }
	
		
	/**
	 * Method to initiate the page flow.
	 * 
	 * @param testName The unique test name.
	 * 
	 * @return The page flowId that is used to identify the page object pool
	 *         associated with a UI flow.
	 */
	@SuppressWarnings("rawtypes")
	public static String start(String testName){
		String id = PAGE_FLOW_ID_PREFIX + MiscUtility.generateUniqueId();
		
		if(pageFlows==null) {
			pageFlows = new HashMap<String, PageFlow>();
		}
		
		PageFlow pf = new PageFlow(new LinkedList<SeleniumPageObject>(), testName);
		pageFlows.put(id, pf);
		
		LOGGER.info("PageNavigator successfully started for PF Id: {} and name: {}", id, testName);
		return id;
	}
	
	/*
	 * This method MUST be called from UI test cases which will internally using PageNavigator AFTER executing test
	 */
	
	
	/**
	 * Method to close the page flow and destroy the object pool.
	 * 
	 * @param id The page flowId.
	 */
	@SuppressWarnings("rawtypes")
	public static void close(String id){
		if(id!=null && pageFlows!=null && pageFlows.get(id)!=null){
			PageFlow pf = pageFlows.get(id);
			String pfDtls = pf.toString();
			
			List<SeleniumPageObject> objects = pf.getObjects();
			if(objects!=null){
				for(SeleniumPageObject obj : objects) {
					obj = null;
				}
			}
			pageFlows.remove(id);
			LOGGER.info("PageNavigator successfully closed for PF which was [{}].", pfDtls);
		}
	}
	
	/**
	 * Method to set the page navigator identifier.
	 * 
	 * @param identifier The abstract page router bean object
	 */
	public void setIdentifier(AbstractPageRouter identifier) {
		this.identifier = identifier;
	}



	/****************************** Private stuff ************************************/
	private AbstractPageRouter identifier;
	
	private static Map<String, PageFlow> pageFlows = new HashMap<String, PageFlow>();
	
	private static class PageFlow{
		@SuppressWarnings("rawtypes")
		private List<SeleniumPageObject> objects;
		private String testName;
		
		@SuppressWarnings("rawtypes")
		public PageFlow(List<SeleniumPageObject> objects, String testName){
			this.objects = objects;
			this.testName = testName;
		}

		@SuppressWarnings("rawtypes")
		public List<SeleniumPageObject> getObjects() {
			return objects;
		}
		
		public String getTestName() {
			return testName;
		}

		public void setTestName(String testName) {
			this.testName = testName;
		}

		@SuppressWarnings("rawtypes")
		public SeleniumPageObject fetchObject(String className){
			SeleniumPageObject result = null;
			
			if(objects!=null){
				for(SeleniumPageObject obj : objects){
					if(obj.getClass().getName().equals(className)){
						result = obj;
					}
				}
			}
			return result;
		}
		
		@SuppressWarnings("rawtypes")
		public void addObject(SeleniumPageObject obj){
			this.objects.add(obj);
		}
		
		@SuppressWarnings("rawtypes")
		public void updatePage(SeleniumPageObject upObj){
			if(objects!=null && upObj!=null){
				for(SeleniumPageObject obj : objects){
					if(obj.getClass().getName().equals(upObj.getClass().getName())){
						objects.add(upObj);
						break;
					}
				}
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			
			if(obj instanceof PageFlow){
				PageFlow pf = (PageFlow)obj;
				int localListSize = this.objects!=null? this.objects.size() : 0;
				int remoteListSize = pf.getObjects()!=null? pf.getObjects().size() : 0;
				
				if(this.testName.equals(pf.getTestName()) && localListSize == remoteListSize) {
					return true;
				}
			}
			return false;
		}
        
		@Override
		public int hashCode() {
			return this.getTestName().hashCode() + (this.getObjects()!=null? this.getObjects().hashCode():0);
		}
		
		@Override
        public String toString() {
            return String.format( "Page Flow with test name %s has %s objects", this.testName, this.objects!=null?this.objects.size():"0");
        }
	}
	
	
	@SuppressWarnings("rawtypes")
	private static List<Object> instantiate(WebDriver driver, PageFlow pf, Class<? extends SeleniumPageObject> cls){
		List<Object> results = new LinkedList<Object>();
		
		if(pf.fetchObject(cls.getName())!=null){
			results.add(true);
			results.add(pf.fetchObject(cls.getName()));
    	}
    	else{
    		results.add(false);
    		results.add(PageFactory.initElements(driver, cls));
    	}
    	
		return results;
	}
	
	private static int getDeafultTestCunt(){
		pageFlowDefaultTestCount++;
		return pageFlowDefaultTestCount;
	}
	
	private static int pageFlowDefaultTestCount = 0;
	
	private static final String PAGE_FLOW_ID_PREFIX = "PF_";
	private static final String PAGE_FLOW_DEFAULT_TEST_NAME = "System generated ";
	private static final String MESSAGE_NOT_AVAILABLE = "Not available";
	
	private static Logger LOGGER = LoggerFactory.getLogger(PageNavigator.class);
}
