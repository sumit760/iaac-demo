package com.comcast.test.citf.common.http;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class for Selenium based operations.
 * 
 * @author arej001c, spal004c
 *
 */
public class SeleniumHelper {

	/**
	 * Returns headless browser instance.
	 * 
	 * @param jsEnabled
	 * 					Boolean to indicate whether the browser will support javascript.
	 * @return
	 * 					Headless browser instance.
	 */
	public static WebDriver createHtmlUnitDriver(boolean jsEnabled){
		HtmlUnitDriver browser = new HtmlUnitDriver(jsEnabled);
		if(jsEnabled)
			browser.setJavascriptEnabled(true);
		
		return browser;
	}
	
	/**
	 * Returns FirefoxDriver instance.
	 * 
	 * @return WebDriver instance.
	 */
	public static WebDriver createFirefoxDriver(){
		return new FirefoxDriver();
	}
	
	/**
	 * Waits for an WebElement of specified Id.
	 * 
	 * @param driver 
	 * 				WebDriver instance.
	 * @param Id 
	 * 				Id of the element to wait for.
	 */
	public static void waitForButtonElement(WebDriver driver, final String Id) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(Id)));
	}
	
	/**
	 * Waits for button element identified by the specified Id till the timeOutInSec.
	 * 
	 * @param driver 
	 *               WebDriver instance.
	 * @param Id
	 * 				 Id of the WebElement.
	 * @param timeOutInSec
	 * 				 Time out in seconds.
	 */
	public static void waitForButtonElement(WebDriver driver, final String Id, long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.elementToBeClickable(By.id(Id)));
	}
	
	
	/**
	 * HtmlUnitDriver Waits for an element identified by the specified xpath till the timeOutInSec.
	 * 
	 * @param driver
	 * 				HtmlUnitDriver instance.
	 * @param xPath 
	 * 				xPath of the WebElement.
	 * @param timeOutInSec
	 * 				Time out in seconds.
	 */
	public static void waitForElementByXpath(HtmlUnitDriver driver, final String xPath, long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
	}
	
	/**
	 * Webdriver Waits for an element identified by the specified xpath till the timeOutInSec.
	 * 
	 * @param driver
	 * 				 WebDriver instance.
	 * @param xPath
	 * 				xPath of the WebElement.
	 * @param timeOutInSec
	 * 				Time out in seconds.
	 */
	public static void waitForElementByXpath(WebDriver driver, final String xPath, long timeOutInSec) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSec);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xPath)));
	}
}
