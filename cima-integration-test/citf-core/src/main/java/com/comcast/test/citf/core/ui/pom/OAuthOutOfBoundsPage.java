package com.comcast.test.citf.core.ui.pom;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;

/**
 * This is Selenium Page Object for OAuth Out of Bound page.
 * 
 * @author Valdas Sevelis
 */
public class OAuthOutOfBoundsPage extends SeleniumPageObject<OAuthOutOfBoundsPage> {

	@FindBy(how = How.ID, using = "implicit_grant_response")
	private WebElement implicitGrantResponseContainer;

	@FindBy(how = How.ID, using = "access_token")
	private WebElement accessTokenText;

	@FindBy(how = How.ID, using = "token_type")
	private WebElement tokenTypeText;

	@FindBy(how = How.ID, using = "expires_in")
	private WebElement expiresInText;

	@FindBy(how = How.ID, using = "id_token")
	private WebElement idTokenText;

	public OAuthOutOfBoundsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/**
	 * Provides Optional embedded access token
	 * @see com.google.common.base.Optional
	 *  
	 * @return Embedded access token
	 */
	public Optional<String> getEmbeddedAccessToken() {
		return getText(accessTokenText);
	}

	/**
	 * Provides Optional embedded access token type
	 * @see com.google.common.base.Optional
	 * 
	 * @return Embedded access token type
	 */
	public Optional<String> getEmbeddedAccessTokenType() {
		return getText(tokenTypeText);
	}

	/**
	 * Provides Optional embedded expires in
	 * @see com.google.common.base.Optional
	 * 
	 * @return Embedded expires in value
	 */
	public Optional<Integer> getEmbeddedExpiresIn() {
		return getText(expiresInText).transform(
				new Function<String, Integer>() {
					@Override
					public Integer apply(String s) {
						return Integer.parseInt(s);
					}
				});
	}

	/**
	 * Provides Optional embedded id token
	 * @see com.google.common.base.Optional
	 * 
	 * @return Embedded id token
	 */
	public Optional<String> getEmbeddedIdToken() {
		return getText(idTokenText);
	}

	/**
	 * Provides Optional text
	 * 
	 * @param e
	 * 			Selenium WebElement
	 * @return Text
	 * 
	 * @see com.google.common.base.Optional
	 * @see org.openqa.selenium.WebElement
	 */
	protected Optional<String> getText(WebElement e) {
		try {
			return (e == null) ? Optional.<String>absent() : Optional.fromNullable(e.getText());
		} catch (NoSuchElementException exc) {
			return Optional.absent();
		}
	}

	@Override
	protected void isLoaded() throws Error {
		String url = driver.getCurrentUrl();
		Assert.assertTrue(url.endsWith("/oob"));
		Assert.assertNotNull(implicitGrantResponseContainer);
	}

	@Override
	protected void load() {
		this.driver.get(getURLToLoad());
	}

	/**
	 * Provides URL to load. 
	 * This method has not been implemented.
	 * 
	 * @return URL to load
	 */
	private String getURLToLoad() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
