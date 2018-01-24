package com.comcast.cima.test.ui.pageobject;
 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;

/**
 * This is Selenium Page Object for "Xfinity IDM Terms Of Service" web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */


public class XfinityIDMTermsOfService extends SeleniumPageObject<XfinityIDMTermsOfService>{

	@FindBy(how = How.XPATH, using = "//div[@id='header']/div/h2")
	public WebElement termsOfServiceHeader;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[2]")
	public WebElement termsOfService1;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[3]")
	public WebElement termsOfService2;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[4]")
	public WebElement termsOfService3;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[5]")
	public WebElement termsOfService4;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[6]")
	public WebElement termsOfService5;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[7]")
	public WebElement termsOfService6;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[8]")
	public WebElement termsOfService7;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[9]")
	public WebElement termsOfService8;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[10]")
	public WebElement termsOfService9;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[11]")
	public WebElement termsOfService10;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[12]")
	public WebElement termsOfService11;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[13]")
	public WebElement termsOfService12;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[14]")
	public WebElement termsOfService13;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[15]")
	public WebElement termsOfService14;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[16]")
	public WebElement termsOfService15;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[17]")
	public WebElement termsOfService16;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[18]")
	public WebElement termsOfService17;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[19]")
	public WebElement termsOfService18;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[20]")
	public WebElement termsOfService19;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[21]")
	public WebElement termsOfService20;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[22]")
	public WebElement termsOfService21;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[23]")
	public WebElement termsOfService22;
	
	@FindBy(how = How.XPATH, using = "//div[@id='terms']/div/div[@class='comes-from-legal']/h1[24]")
	public WebElement termsOfService23;
	
	
	public XfinityIDMTermsOfService(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@Override
	protected void load() {}


	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(termsOfServiceHeader));
	}
	/**
	 * To verify Xfinity Terms And Conditions
	 * 
	 * @return true or false based on validation status of terms conditions's page element
	 */
	
	public boolean verifyXfinityTermsAndConditions() {
		boolean validated = false;
		if (isElementPresent(termsOfServiceHeader) &&
			isElementPresent(termsOfService1) &&
			isElementPresent(termsOfService2) &&
			isElementPresent(termsOfService3) &&
			isElementPresent(termsOfService4) &&
			isElementPresent(termsOfService5) &&
			isElementPresent(termsOfService6) &&
			isElementPresent(termsOfService7) &&
			isElementPresent(termsOfService8) &&
			isElementPresent(termsOfService9) &&
			isElementPresent(termsOfService10) &&
			isElementPresent(termsOfService11) &&
			isElementPresent(termsOfService12) &&
			isElementPresent(termsOfService13) &&
			isElementPresent(termsOfService14) &&
			isElementPresent(termsOfService15) &&
			isElementPresent(termsOfService16) &&
			isElementPresent(termsOfService17) &&
			isElementPresent(termsOfService18) &&
			isElementPresent(termsOfService19) &&
			isElementPresent(termsOfService20) &&
			isElementPresent(termsOfService21) &&
			isElementPresent(termsOfService22) &&
			isElementPresent(termsOfService23)) {
			validated = true;
		}
			
		return validated;
	}

	
	private static Logger logger = LoggerFactory.getLogger(XfinityIDMTermsOfService.class);
}
