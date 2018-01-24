package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.core.dataProvider.EndPoinUrlProvider;
import com.comcast.test.citf.core.init.ObjectInitializer;

//This is a test page for Spanish, will restructure this page/content
public class SignInToXfinitySpanish extends SeleniumPageObject<SignInToXfinitySpanish> {

	/*Sign in related elements*/
	
	@FindBy(how = How.ID, using = "user")
	private WebElement username;
	
	@FindBy(how = How.ID, using = "passwd")
	private WebElement password;
	
	@FindBy(how = How.ID, using = "sign_in")
	private WebElement signIn;	
	
	@FindBy(how = How.NAME, using = "nucaptcha-answer")
	private WebElement captchaField;
	
	/* Page Header*/
	@FindBy(how = How.XPATH, using = "//*[@id='signin']/div[1]/h2")
	private WebElement pageHeader;
	
	/*Links of sign in page*/
	
	@FindBy(how = How.ID, using = "sign_in_fb")
	private WebElement connectUsingFacebook;
	
	@FindBy(how = How.ID, using = "rm_label_learn_more")
	private WebElement learnMore;
	
	@FindBy(how = How.ID, using = "fb_learn_more_text")
	private WebElement LearnMoreLinkFb;
	
	@FindBy(how = How.XPATH, using = "//*[@id='needaccess']/p/a")
	private WebElement createUsername;
	
	@FindBy(how = How.ID, using = "forgotPwdLink")
	private WebElement forgotPassword;
	
	@FindBy(how = How.LINK_TEXT, using = "�Olvidaste tu nombre de usuario o email?")
	private WebElement lookUp;
	
	@FindBy(how = How.ID, using = "remember_me")
	private WebElement keepMeSignedIn;
	
	@FindBy(how = How.LINK_TEXT, using = "Mapa del sitio")
	private WebElement siteMapLink;
	
	@FindBy(how = How.LINK_TEXT, using = "Pol�tica de privacidad")
	private WebElement privacyPolicy;
	
	@FindBy(how = How.LINK_TEXT, using = "Condiciones del servicio")
	private WebElement termsOfService;
	
	@FindBy(how = How.LINK_TEXT, using = "Comun�cate con nosotros")
	private WebElement contactUs;
	
	
	
	
	/* English to spanish conversion*/
	private String spanishTextOfsignInToComcast="Inicia sesi�n en Comcast";
	
	private String spanishTextofLogin="INICIAR SESI�N";
	
	private String spanishTextOflearnMore="M�s informaci�n";
	
	private String spanishTextDontKnowUsernameOrEmail="�Olvidaste tu nombre de usuario o email?";
	
	private String spanishTextofForgotPassword="�Olvidaste tu contrase�a?";
	
	private String spanishTextofCreateUsername="Crear nombre de usuario";
	
	
	
	public SignInToXfinitySpanish(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	@Override
	protected void isLoaded() throws Error {
		Assert.assertTrue(isElementPresent(signIn));
	}

	@Override
	protected void load() {
		try	{
			this.driver.get(getURLToLoad());
			this.driver.manage().window().maximize();
		} catch (Exception e) {
			LOGGER.error("Error occurred while loading Xfinity SignIn Page in spanish. ", e);
		}
	}
	
	public boolean isXfinityPageHeaderinSpanish() {
		if (isElementPresent(pageHeader)) {
			if(pageHeader.getText().equalsIgnoreCase(spanishTextOfsignInToComcast))
			{
				return true;
			}
		
	    }
		return false;
	}
	
	/*
	 * This methods will verify whether footer section of xfinity page is displayed in spanish or not
	 */
	
	public boolean isXfinityLoginFooterDisplayedInSpanish() {
		if (isElementPresent(siteMapLink) && isElementPresent(privacyPolicy) && isElementPresent(termsOfService) && isElementPresent(contactUs)  )
		{
			return true;
	    }
		return false;
	}
	
	
	/*
	 * Methods to verify whetherelemntes of xfinity page is displayed in spanish or not
	 */
	
	public boolean isXfinityLoginButtonDisplayedInSpanish() {
		if (isElementPresent(signIn)) {
			if(signIn.getText().equalsIgnoreCase(spanishTextofLogin))
			{
				return true;
			}	
			return false;
	    }
		return false;
	}
	
	
	public boolean isXfinityCreateUsernameLinkDisplayedInSpanish() {
		if (isElementPresent(createUsername)) {
			if(createUsername.getText().equalsIgnoreCase(spanishTextofCreateUsername))
			{
				return true;
			}	
			return false;
	    }
		return false;
	}
	
	
	public boolean isXfinityForgotPasswordLinkDisplayedInSpanish() {
		if (isElementPresent(forgotPassword)) {
			if(forgotPassword.getText().equalsIgnoreCase(spanishTextofForgotPassword))
			{
				return true;
			}	
			return false;
	    }
		return false;
	}
	
	
	public boolean isXfinityLearnMoreLinkDisplayedInSpanish() {
		if (isElementPresent(learnMore)) {
			if(learnMore.getText().equalsIgnoreCase(spanishTextOflearnMore))
			{
				return true;
			}	
			return false;
	    }
		return false;
	}
	
	
	public boolean isXfinityUserNameLookupLinkDisplayedInSpanish() {
		if (isElementPresent(lookUp)) {
			if(lookUp.getText().equalsIgnoreCase(spanishTextDontKnowUsernameOrEmail))
			{
				return true;
			}	
			return false;
	    }
		return false;
	}
	

	/*Transition Methods*/
	
	public Object getFacebookLoginPopup() {
		if (isElementPresent(connectUsingFacebook)) {
			this.connectUsingFacebook.click();
			return ObjectInitializer.getPageNavigator().getNextPage(driver, this, this.pageFlowId);
		}
		return null;
	}
	
	
	
	
	private String getURLToLoad() {
		return ObjectInitializer.getCache(ICommonConstants.CACHE_CONFIGURARTION_PARAMS).getString(EndPoinUrlProvider.LoginUrlPropKeys.XFINITY_SIGNIN.getValue(), 
											ObjectInitializer.getCitfTestInitializer().getCurrentEnvironment());
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SignInToXfinitySpanish.class);
	

}
