package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.dataProvider.cima.IDataProviderEnums;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.init.CitfTestInitializer;
import com.comcast.test.citf.core.init.ObjectInitializer;

/**
 * This is Selenium Page Object for Xfinity My Account web page.
 * 
 * @author Sumit Pal & Shailesh Suman
 *
 */
public class XfinityMyAccount extends SeleniumPageObject<XfinityMyAccount>  {

	/*Header elements and links */
	@FindBy(how = How.XPATH, using = "//div[@id='polaris-header']/div[1]/ul[2]/li[4]/a")
	public WebElement SignIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Sign Out")
	public WebElement Signout;
	
	private final static String frameId = "myxfnnavbar";
	
	/* My account related elements*/
	
	@FindBy(how = How.XPATH, using = "//a[@id='DoitLater']/span")
	private WebElement confirmVoiceSecurityPINLater1; 
	
	@FindBy(how = How.XPATH, using = "//div[@id='main_0_dvSave']/a[@class='btn btn-gray overlay-cancel']/span")
	private WebElement confirmVoiceSecurityPINLater2; 
	
	@FindBy(how = How.XPATH, using = "//span[@id='overlay-home-network-messaging_descriptor']")
	private WebElement confirmVoiceSecurityPINText; 
	
	@FindBy(how = How.XPATH, using = "//div[@id='overlay-cpni']/div[1]/a")
	private WebElement confirmVoiceSecurityPINClose; 
	
	@FindBy(how = How.XPATH, using = "//span[@data-sel='users-and-preferences-link']")
	private WebElement userandPreferenceLink; 	
	
	@FindBy(how = How.XPATH, using = "//a[@class='xfinityParentalControls']")
	private WebElement ParentalControlLink; 
	
	@FindBy(how = How.ID, using = "add-user-first-name")
	private WebElement UserPin;
	
	@FindBy(how = How.ID, using = "btnExistUser")
	private WebElement OkButton;
	
	@FindBy(how = How.ID, using = "changeparentpin")
	private WebElement ChangeParentalPin;
	
	
	/*Parental control elements*/
	
	@FindBy(how = How.ID, using = "parental-control-option-on")
	private WebElement ParentalControlON;
	
	@FindBy(how = How.ID, using = "parental-control-option-off")
	private WebElement ParentalControlOFF;
	
	@FindBy(how = How.ID, using = "parental-control-option-off")
	private WebElement HelpTextOfTvMovieLock;
	
	@FindBy(how = How.ID, using = "btnSetRestriction")
	private WebElement SaveButton;
	
	@FindBy(how = How.ID, using = "Undo")
	private WebElement UndoButton;
	
	@FindBy(how = How.ID, using = "unlock-channels")
	private WebElement unlockAllChannel;
	
	
	/* TV Ratings elements*/
	@FindBy(how = How.ID, using = "unlock-tv-movie")
	private WebElement unlockAllTVMovieRating;
	
	@FindBy(how = How.XPATH, using = "//div[@class='TVRestrictionsTooltip']/div[@class='tooltip-container question']")
	private WebElement TVLockHelpIcon;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[3]/div/div/span/span")
	private WebElement ratingTV_Y7;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[2]/div/div/span/span")
	private WebElement ratingTV_Y;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[4]/div/div/span/span")
	private WebElement ratingTV_FV;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[5]/div/div/span/span")
	private WebElement ratingTV_G;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[6]/div/div/span/span")
	private WebElement ratingTV_PG;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[7]/div/div/span/span")
	private WebElement ratingTV_TV14;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[8]/div/div/span/span")
	private WebElement ratingTV_MA;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[9]/div/div/span/span")
	private WebElement ratingTV_AO;
	
	@FindBy(how = How.XPATH, using = "//*[@id='tv-slider']/div[10]/div/div/span/span")
	private WebElement ratingTV_NR;
	
	
	
	/* Movie Rating elements */
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[2]/div/div/span/span")
	private WebElement ratingMovie_G;
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[3]/div/div/span/span")
	private WebElement ratingMovie_PG;
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[4]/div/div/span/span")
	private WebElement ratingMovie_PG13;
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[5]/div/div/span/span")
	private WebElement ratingMovie_R;
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[6]/div/div/span/span")
	private WebElement ratingMovie_NC17;
	
	@FindBy(how = How.XPATH, using = "//*[@id='movie-slider']/div[7]/div/div/span/span")
	private WebElement ratingMovie_X;	
	
	
	
	/* Xfinity Channel elements*/
	
	@FindBy(how = How.ID, using = "Unlocked_1")
	private WebElement  chnlTwentythCenturyFoxON;
	
	@FindBy(how = How.ID, using = "locked_1")
	private WebElement  chnlTwentythCenturyFoxOFF;
	
	@FindBy(how = How.ID, using = "Unlocked_2")
	private WebElement  chnlForKidON;
	
	@FindBy(how = How.ID, using = "locked_2")
	private WebElement  chnlForKidOFF;
	
	@FindBy(how = How.ID, using = "Unlocked_3")
	private WebElement  chnlAandEON;
	
	@FindBy(how = How.ID, using = "locked_3")
	private WebElement  chnlAandEOFF;
	
	@FindBy(how = How.ID, using = "Unlocked_4")
	private WebElement  chnlABCON;
	
	@FindBy(how = How.ID, using = "locked_4")
	private WebElement  chnlABCOFF;
	
	@FindBy(how = How.ID, using = "Unlocked_5")
	private WebElement  chnlABCFamilyON;
	
	@FindBy(how = How.ID, using = "locked_5")
	private WebElement  chnlFamilyOFF;
	
	@FindBy(how = How.ID, using = "Unlocked_6")
	private WebElement  chnlAMCON;
	
	@FindBy(how = How.ID, using = "locked_6")
	private WebElement  chnlAMCOFF;
	
	
	
	public XfinityMyAccount(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void load() {
		try{
			this.driver.get(getURLToLoad());
		} catch (Exception e){
			logger.error("Error occurred while loading xfinity My Account Page. {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void isLoaded() throws Error {
		String title = driver.getTitle();
		Assert.assertTrue(title.startsWith("Sign in") || title.startsWith("Comcast"));
	}
	
	/* Methods to check presence of element*/
	/**
	 * Checks whether Sign Out link present
	 * 
	 * @return TRUE if page contains Sign Out link, else FALSE
	 */
	public boolean isSignoutLinkPresent() {
		switchToDefaultFrame(driver);
		switchToFrame(driver, frameId);
		return this.isElementPresent(Signout);
	}
	
	/**
	 * Checks whether Sign Out link present
	 * 
	 * @return TRUE if page contains Sign Out link, else FALSE
	 */
	public boolean isSignInLinkPresent() {
		switchToFrame(driver, frameId);
		return this.isElementPresent(SignIn);
	}
	
	/**
	 * Checks whether user signed in
	 * 
	 * @return TRUE if page contains Sign out link, else FALSE
	 */
	public boolean isUserSignedIn(){
		return this.isSignoutLinkPresent();
	}
	
	/**
	 * Checks whether user signed out
	 * 
	 * @return TRUE if page contains Sign In link, else FALSE
	 */
	public boolean isUserSignedOut(){
		return this.isSignInLinkPresent();
	}
	
	/**
	 * Checks whether user signed out
	 * 
	 * @return TRUE if page contains Sign In link, else FALSE
	 */
	public boolean isuserandPreferenceLinkPresent() {
		return this.isElementPresent(userandPreferenceLink);
	}
	
	/**
	 * Checks whether Parental Control link present
	 * 
	 * @return TRUE if page contains Parental Control link, else FALSE
	 */
	public boolean isParentalControlLinkPresent() {
		return this.isElementPresent(ParentalControlLink);
	}
	
	/**
	 * Checks whether User Pin field present
	 * 
	 * @return TRUE if page contains User Pin field, else FALSE
	 */
	public boolean isUserPinFieldPresent() {
		return this.isElementPresent(UserPin);
	}
	
	/**
	 * Checks whether Parental Control ON field present
	 * 
	 * @return TRUE if page contains Parental Control ON field, else FALSE
	 */
	public boolean isParentalControlONPresent() {
		return this.isElementPresent(ParentalControlON);
	}
	
	/**
	 * Checks whether OK button present
	 * 
	 * @return TRUE if page contains OK button, else FALSE
	 */
	public boolean isOkButtonPresent() {
		return this.isElementPresent(OkButton);
	}
	
	/**
	 * Checks whether Parental Control OFF field present
	 * 
	 * @return TRUE if page contains Parental Control OFF field, else FALSE
	 */
	public boolean isParentalControlOFFPresent() {
		return this.isElementPresent(ParentalControlOFF);
	}
	
	private boolean isVoiceSecurityPINDialog1Present() {
		return isElementPresent(this.confirmVoiceSecurityPINLater1);
	}
	
	private boolean isVoiceSecurityPINDialog2Present() {
		return isElementPresent(this.confirmVoiceSecurityPINLater1);
	}
	
	private boolean isVoiceSecurityPINDialogClosePresent() {
		return isElementPresent(this.confirmVoiceSecurityPINClose);
	}
	
	
	/*Method to perfrom actions*/
	
	public void closeVoiceSecurityPINDialogWindow() {
		if (isVoiceSecurityPINDialog1Present() ||
				isVoiceSecurityPINDialog2Present()	) {
			if (isVoiceSecurityPINDialogClosePresent()) {
				this.confirmVoiceSecurityPINClose.click();
			}
		}
	}
	
	
	
	/**
	 * Unlocks all TV and Movie ratings
	 */
	public void unlockAllTVAndMovieRating() {
		this.unlockAllTVMovieRating.click();
	}
	
	/**
	 * Sets Parrental Control ON
	 */
	public void setParrentalControlON() {
		this.ParentalControlON.click();
	}
	
	/**
	 * Sets Parental Control OFF
	 */
	public void setParrentalControlOFF() {
		this.ParentalControlOFF.click();
	}
	
	/**
	 * Saves changes
	 */
	public void saveChanges() {
		this.SaveButton.click();
	}
	
	
	/* Methods to set rating for TV*/
	
	/**
	 * Sets TV Rating to TV Y
	 */
	public void setTVRatingTVY() {
		this.ratingTV_Y.click();
	}
	
	/**
	 * Sets TV Rating to TV FV
	 */
	public void setTVRatingTVFV() {
		this.ratingTV_FV.click();
	}
	
	/**
	 * Sets TV Rating to TV Y7
	 */
	public void setTVRatingTVY7() {
		this.ratingTV_Y7.click();
	}
	
	/**
	 * Sets TV Rating to TV G
	 */
	public void setTVRatingTVG() {
		this.ratingTV_G.click();
	}
	
	/**
	 * Sets TV Rating to TV PG
	 */
	public void setTVRatingTVPG() {
		this.ratingTV_PG.click();
	}
	
	/**
	 * Sets TV Rating to TV 14
	 */
	public void setTVRatingTV14() {
		this.ratingTV_TV14.click();
	}
	
	/**
	 * Sets TV Rating to TV MA
	 */
	public void setTVRatingTVMA() {
		this.ratingTV_MA.click();
	}
	
	/**
	 * Sets TV Rating to TV AO
	 */
	public void setTVRatingTVAO() {
		this.ratingTV_AO.click();
	}
	
	/**
	 * Sets TV Rating to TV NR
	 */
	public void setTVRatingTVNR() {
		this.ratingTV_NR.click();
	}
	
	
	/* Methods to set rating for Movie*/
	
	/**
	 * Sets Movie Rating to G
	 */
	public void setMovieRatingG() {
		this.ratingMovie_G.click();
	}
	
	/**
	 * Sets Movie Rating to PG
	 */
	public void setMovieRatingPG() {
		this.ratingMovie_PG.click();
	}
	
	/**
	 * Sets Movie Rating to PG 13
	 */
	public void setMovieRatingPG13() {
		this.ratingMovie_PG13.click();
	}
	
	/**
	 * Sets Movie Rating to R
	 */
	public void setMovieRatingR() {
		this.ratingMovie_R.click();
	}
	
	/**
	 * Sets Movie Rating to NC 17
	 */
	public void setMovieRatingNC17() {
		this.ratingMovie_NC17.click();
	}
	
	/**
	 * Sets Movie Rating to X
	 */
	public void setMovieRatingX() {
		this.ratingMovie_X.click();
	}
	
	
	/* Methods to lock and unlock channel*/
	
	/**
	 * Unlocks all channels
	 */
	public void unlockAllChannel() {
		this.unlockAllChannel.click();
	}
	
	/**
	 * Unlocks Channel Century FOX
	 */
	public void unlockChannelCentruryFOX() {
		this.chnlTwentythCenturyFoxON.click();
		
	}
	
	/**
	 * Locks Channel Century FOX
	 */
	public void lockChannelCentruryFOX() {
		this.chnlTwentythCenturyFoxOFF.click();
		
	}
	
	/**
	 * Unlocks Channel for Kid
	 */
	public void unlockChannel4Kid() {
		this.chnlForKidON.click();
		
	}
	
	/**
	 * Locks Channel for Kid
	 */
	public void lockChannel4Kid() {
		this.chnlForKidOFF.click();
		
	}
	
	/**
	 * Unlocks Channel A&E
	 */
	public void unlockChannelAE() {
		this.chnlAandEON.click();
		
	}
	
	/**
	 *Locks Channel A&E
	 */
	public void lockChannelAE() {
		this.chnlAandEOFF.click();
		
	}
	
	/**
	 *Unlocks Channel ABC
	 */
	public void unlockChannelABC() {
		this.chnlABCON.click();
		
	}
	
	/**
	 *Locks Channel ABC
	 */
	public void lockChannelABC() {
		this.chnlABCOFF.click();
		
	}
	
	/**
	 *Unlocks Channel ABC Family
	 */
	public void unlockChannelABCFamily() {
		this.chnlABCFamilyON.click();
		
	}
	
	/**
	 *Locks Channel ABC Family
	 */
	public void lockChannelABCFamily() {
		this.chnlFamilyOFF.click();
	}
	
	/**
	 *Unlocks Channel AMC
	 */
	public void unlockChannelAM() {
		this.chnlAMCON.click();
	}
	
	/**
	 *Locks Channel AMC
	 */
	public void lockChannelAM() {
		this.chnlAMCOFF.click();
	}
	
	/**
	 * Signed out
	 */
	public void signOut() {
		if (isUserSignedIn()) {
			this.Signout.click();
			waitForElementPresent(this.SignIn,ICommonConstants.WAIT_TIMEOUT);
		}
	}
	
	/**
	 * Navigates to back page
	 * @return Back page object
	 */
	public Object backBrowser() {
		this.driver.navigate().back();
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
	}
	
	/**
	 * Refreshes page
	 * @return same refreshed page object
	 */
	public Object refreshPage() {
		this.driver.navigate().refresh();
		waitForPageLoaded(this.driver);
		return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.getPageFlowId());
	}
	
	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(IDataProviderEnums.LoginUrlPropKeys.MY_ACCOUNT.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	/**
	 * Provides User Preference page
	 * 
	 * @return page object for User Preference page
	 */
	public Object getUserPreferncePage() {
		if (isElementPresent(userandPreferenceLink)){
			this.userandPreferenceLink.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	private static Logger logger = LoggerFactory.getLogger(XfinityMyAccount.class);
}
