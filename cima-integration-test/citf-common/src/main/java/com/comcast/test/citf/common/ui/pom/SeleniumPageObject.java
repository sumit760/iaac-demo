package com.comcast.test.citf.common.ui.pom;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
 * Abstract class to support the Page Object Model feature in CITF. This uses LoadableComponent
 * to ensure that the page loads completely before it returns the page handle.
 * 
 * @author spal004c and shaileshs362
 *
 * @param <T> The Page Object Model class
 */
public abstract class SeleniumPageObject<T extends SeleniumPageObject<T>> extends LoadableComponent<T> {
	
	public WebDriver driver;
	
	
	/**
	 * Unique identifier for a page flow.
	 */
	public String pageFlowId;
	
	/**
	 * Method to get the page flowId.
	 * 
	 * @return The page flowId.
	 */
    public String getPageFlowId() {
		return pageFlowId;
	}
    
    /**
     * Method to set the page flowId.
     * 
     * @param pageFlowId The page flowId to set.
     * 
     */
	public void setPageFlowId(String pageFlowId) {
		this.pageFlowId = pageFlowId;
	}
	
	/**
	 * The unique window handle associated with a UI page.
	 */
	public String windowHandle;
	
	/**
	 * Method to get the window handle of a UI window.
	 * 
	 * @return The UI window handle.
	 */
	public String getWindowHandle() {
		return windowHandle;
	}

	/**
	 * Method to set the window handle associated with a UI page.
	 * 
	 * @param windowHandle The window handle to set.
	 * 
	 */
	public void setWindowHandle(String windowHandle) {
		this.windowHandle = windowHandle;
	}


	/**
	 * Method to get list of page errors.
	 * This method must need to be implemented by all page object classes.
	 * 
	 * @return The list of page errors.
	 */
	public List<String> getPageError(){
		return null;
	}

	/**
     * Method to select the check box by ID.
     * 
     * @param  field The check box ID.
     */
	protected void selectCheckboxByID(WebElement field) {
    	if (!field.isSelected()){
    		field.click();
    	}
    }
    
    
    /**
     * Method to select radio button by ID.
     * 
     * @param  field The radio button ID. 
     */
	protected void selectRadioByID(WebElement field) {
    	if (!field.isSelected()){
    		field.click();
    	}
    }
    
    
    /**
     * Method to check whether an element is present or not.
     * This method checks the presence and enabled status of an element.
     * 
     * @param  field   The element to check for.
     * 
     * @return True or false True when the element is present/False otherwise
     */
	protected boolean isElementPresent(WebElement field) {
       boolean status = false;
    	
       try{
    	  if (field.isDisplayed()){
    		if (field.isEnabled()){
    			status =true;
    		}
    	  }    	
    	}catch (Exception e){ 
		  status =false;
    	}
    	return status;
    }
    
    
	/**
	 * Method to select radio button by the text associated with it.
	 *   
	 * @param radio The list of radio buttons.
	 * @param attachedText The text attached.
	 * 
	 */
	protected void selectRadioButtonByText(List<WebElement> radio,String attachedText){
		  int count=radio.size();
		  boolean isSelected=true;
		  for (int i = 0; i < count-1; i++) {
			
			String radioLabel=radio.get(i).getAttribute("value");
			if (radioLabel.equalsIgnoreCase(attachedText)){
				if (!radio.get(i).isSelected()){
					radio.get(i).click();
				  isSelected=false;
				  break;
				}
				isSelected=false;
			}
		 }	  		  
	  }
	  
	/**
	 * Method to select check box by the text associated with.
	 *   
	 * @param checkbox List of check box web elements.
	 * @param attachedText The text attached.
	 * 
	 */
	protected void selectCheckboxByText(List<WebElement> checkbox, String attachedText){
		  int count = checkbox.size();
		  boolean isSelected=true;
		  for (int i = 0; i < count-1; i++) {
			  			  
			    String radioLabel = checkbox.get(i).getAttribute("value");
			    if (radioLabel.equalsIgnoreCase(attachedText))  {
			    	if (!checkbox.get(i).isSelected())	{
			    		checkbox.get(i).click();
			    		isSelected=false;
			    		break;
			    	}
			    	isSelected=false;
			    }
			}	  		  
	  }
	  
	/**
	 * Method to select a drop down by the text.
	 * 
	 * @param dropdown The drop down element.
	 * @param Value The text associated with.
	 * 
	 */
	protected void selectDropDownOptionByVisibleText(WebElement dropdown, String Value){					
	  new Select(dropdown).selectByVisibleText(Value);
	}
	
	/**
	 * Method to select drop down by web element.
	 * 
	 * @param dropdown The drop down element.
	 * @param element The element to select in the drop down.
	 * 
	 */
	protected void selectDropDownOptionByElement(WebElement dropdown, WebElement element){					
		  if (isElementPresent(dropdown)) {
			  dropdown.click();
		  }
		  waitForElementPresent(element,ICommonConstants.WAIT_TIMEOUT);
		  if (isElementPresent(element)) {
			  element.click();
		  }
	}
	
	
	/**
	 * Method to wait for a page to load for the web element.
	 * 
	 * @param element The web element for which the page is expected 
	 *                to load.
	 * @param maxWait The maximum amount of time for the wait.
	 * @param minWait The minimum amount of time for the wait.
	 * 
	 */
	protected void waitForPagetoLoad(WebElement element, int maxWait, int minWait) {
	  WebDriverWait wait = new WebDriverWait(driver, 10);
	  wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	
	/**
	 * Method to wait for an element in a page.
	 * 
	 * @param element The web element to load for.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	protected void waitForElementPresent(WebElement element,long timeOutInSec) {
		  WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		  wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	/**
	 * Method to wait for an element to become visible.
	 * 
	 * @param element The web element to wait for.
	 * @param timeOutInSec The time out in seconds.
	 * 
	 */
	protected void waitForElementVisible(WebElement element,long timeOutInSec) {
		  WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		  wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Method to wait for page title.
	 * 
	 * @param title Page title to wait for.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	protected void waitForPageTitle(String title,long timeOutInSec) {
		  WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		  wait.until(ExpectedConditions.titleContains(title));
	}
	
	/**
	 * Method to wait for linked text to appear on page.
	 * 
	 * @param text The linked text to wait for.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	public void waitForLinkedText(String text,long timeOutInSec) {
		String title = driver.getTitle();
		if (title.startsWith("Watch TV Online, Pay Your Bill, Control & Secure Your Home") && text.equalsIgnoreCase("Sign Out")) {
			Actions actions = new Actions(driver);
			actions.moveToElement(driver.findElement(new By.ByXPath("//a[@id='polaris-header-main-navigation-profile']/div/span[4]"))).perform();
		}
		if (!title.equalsIgnoreCase("Please Confirm Access")
				&& !title.equalsIgnoreCase("Unauthorized use of your Comcast ID has been detected")
				&& !title.equalsIgnoreCase("Recovery Options")
				&& !title.equalsIgnoreCase("Sign in to Comcast")) {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
			wait.until(ExpectedConditions.presenceOfElementLocated(new By.ByLinkText(text)));
		}
		
	}
	
	
	/**
	 * Method to wait for a linked text within an iframe.
	 * 
	 * @param text The linked text to wait for.
	 * @param frame The frameId.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	public void waitForLinkedTextInFrame(String text,String frame,long timeOutInSec) {
		waitForFrame(frame,timeOutInSec);
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.presenceOfElementLocated(new By.ByLinkText(text)));
	}
	
	/**
	 * Method to wait for a frame to appear.
	 * 
	 * @param frameId The frameId
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	public void waitForFrame(String frameId,long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameId));
	}
	
	/**
	 * Method to wait for an element by its XAPTH
	 * 
	 * @param xpath The Xpath of the element to waitr for.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	public void waitForElementByXPATH(String xpath,long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.presenceOfElementLocated(new By.ByXPath(xpath)));
	}
	
	
	/**
	 * Method to wait for a text in a web page by XPath
	 * 
	 * @param xpath The XPath of the element.
	 * @param text The text to wait for.
	 * @param timeOutInSec Time out in seconds.
	 * 
	 */
	public void waitForTextPresent(String xpath, String text,long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(new By.ByXPath(xpath), text));
	}
	
	
	/**
	 * Method to wait for a text present in the page source.
	 * 
	 * @param texts String of texts to wait for.
	 * 
	 * @return True if found.
	 *         False otherwise.
	 */
	public boolean waitForTextPresentinPageSource(String ...texts ) {
		boolean present = false;
		long increment = 0;
		long maxWaitTime = ICommonConstants.WAIT_TIMEOUT;
		
		String pageSource = driver.getPageSource();
		pageSource = pageSource.replaceAll("\\s+", ICommonConstants.BLANK_SPACE_STRING);
		while (increment <= maxWaitTime) {
			for (String text : texts) {
				if (pageSource.contains(text)) {
					present = true;
					break;
				}
			}
			if (present) break;
			try {
				Thread.sleep(ICommonConstants.SLEEP_TIME);
				pageSource = driver.getPageSource();
				pageSource = pageSource.replaceAll("\\s+", ICommonConstants.BLANK_SPACE_STRING);
				increment = increment + 5;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return present;
	}
	
	
	public void waitForPageLoaded(WebDriver driver)
	{
	    ExpectedCondition<Boolean> expectation = new
	    ExpectedCondition<Boolean>() {
	        public Boolean apply(WebDriver driver) {
	            return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
	        }
	    };
	    WebDriverWait wait = new WebDriverWait(driver,ICimaCommonConstants.WAIT_TIMEOUT);
	    wait.until(expectation);
	}
	
	/**
	 * Method to clear and type in a text box.
	 * 
	 * @param field The Text box element
	 * @param text The text to inout.
	 * 
	 */
	protected void clearAndType(WebElement field, String text) {
        field.clear();
        field.click();
        field.sendKeys(text);
    }

	/**
	 * Method to get page title.
	 * 
	 * @return The page title.
	 * 
	 */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    
    /**
     * Method to switch to the child window.
     * 
     * @param browser The current browser.
     * 
     * @return The child window handle.
     */
    public WebDriver switchToChildWindow(WebDriver browser) {
    	for (String handle : browser.getWindowHandles()) 
		{
			 browser.switchTo().window(handle);		
			 browser.manage().window().maximize();
		}
		return browser;		
	}
	
	/**
	 * Method to switch back to the parent window.
	 * 
	 * @param browser The current browser.
	 * @param parentwindow The parent window handle.
	 * 
	 * @return The parent window handle.
	 */
    public WebDriver switchToParentWindow(WebDriver browser,String parentwindow){
		 browser.switchTo().window(parentwindow);	
		 return browser;	
	}
	
	public WebDriver switchToFrame(WebDriver browser,String frameID){
		 browser.switchTo().frame(frameID);	
		 return browser;	
	}
	
	/**
	 * Method to switch to the default farme.
	 * 
	 * @param browser The current browser.
	 * 
	 * @return The deafult frameId.
	 * 
	 */
	public WebDriver switchToDefaultFrame(WebDriver browser){
		 browser.switchTo().defaultContent();
		 return browser;	
	}
	
	/**
	 * Method to clear all the cookies associated with the browser session.
	 */
	public void clearAllCookies() {
		this.driver.manage().deleteAllCookies();
	}
	
	/**
	 * Method to get all the cookies associated with a browser session.
	 * 
	 * @return Set of cookies.
	 */
	public Set<Cookie> getAllCookies() {
		return this.driver.manage().getCookies();
	}
	
	
	/**
	 * Method to set all the cookies in a browser session.
	 * 
	 * @param cookies Set of cookies to set.
	 * 
	 */
	public void setAllCookies(Set<Cookie> cookies) {
		if (cookies != null) {
			for (Cookie c : cookies) {
				this.driver.manage().addCookie(new Cookie(c.getName(),c.getValue()));
			}
		}
	}

}
