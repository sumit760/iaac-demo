package com.comcast.test.citf.common.ui.router;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

import com.comcast.test.citf.common.init.CommonContextInitializer;
import com.comcast.test.citf.common.ui.pom.SeleniumPageObject;
import com.comcast.test.citf.common.util.ICommonConstants;


public class PageNavigatorTest {
	
	private static final String LOGIN_CONTEXT = "Cima-Login-Spring-Context.xml";
	
	private PageNavigator navigator;
	private WebDriver mockDriver;
	private SeleniumPageObject mockPageObject;
	
	@Before
	public void init() {
		navigator = Mockito.spy(new PageNavigator());
		mockDriver = Mockito.mock(WebDriver.class,Mockito.RETURNS_DEEP_STUBS);
		mockPageObject = Mockito.mock(SeleniumPageObject.class,Mockito.RETURNS_DEEP_STUBS);
		try{
			CommonContextInitializer.initializeTestContexts(LOGIN_CONTEXT);
		}catch(Exception e){ Assert.fail(e.getMessage());}
	}
	
	
	@Test
	public void testGetNextPageWithBrowserNull() {
		
		String pageFlowId = "mockPageFlowId";
		
		PageNavigator navigator = new PageNavigator();
		Object obj = navigator.getNextPage(null, mockPageObject, pageFlowId);
		
		assertThat(
				obj, notNullValue());
		
		assertThat(
				obj, instanceOf(PageError.class));
		
		PageError err = (PageError) obj;
		
		assertThat(
				err.getError(), is("System Error"));
		
	}

	
	@Test
	public void testGetNextPageWithPageObjectNull() {
		
		String pageFlowId = "mockPageFlowId";
		
		PageNavigator navigator = new PageNavigator();
		Object obj = navigator.getNextPage(mockDriver, null, pageFlowId);
		
		assertThat(
				obj, notNullValue());
		
		assertThat(
				obj, instanceOf(PageError.class));
		
		PageError err = (PageError) obj;
		
		assertThat(
				err.getError(), is("System Error"));
		
	}

	
	@Test
	public void testGetNextPage() {
		
		String pageFlowId = "mockPageFlowId";
		String title = "Sign in to Comcast";
				
		String pageSource = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html lang=\"en\"><head>"
							+ "<title>"+title+"</title><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/styles.min.css?v=f6ce72c\">"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"><meta name=\"description\" content=\"Comcast.net has "
							+ "more to offer when you sign in to your mySIGN-IN Account. Use your account to access other Comcast sites and services like "
							+ "Comcast.net, Plaxo, and more.\"></head><body><div id=\"bd\" class=\"signin clearfix\"><div id=\"left\"><style type=\"text/css\">"
							+ "#ad-block{width:300px;margin:0 0 0 auto}#ad-wrapper{width:300px}#ads-info{font-size:10px;margin:9px 0 0 0;text-align:center}"
							+ "#ads-info a{margin-left:20px}#ads-info a.first{margin-left:0}</style></body></html>";
		
		
		Mockito
		       .when(mockDriver.getPageSource())
		       .thenReturn(pageSource);
		
		AbstractPageRouter identifier = (AbstractPageRouter)CommonContextInitializer.getBean(ICommonConstants.BEAN_NAME_UI_PAGE_ROUTER);
		navigator = new PageNavigator();
		navigator.setIdentifier(identifier);
		Object obj = navigator.getNextPage(mockDriver, mockPageObject, pageFlowId);
			
		assertThat(
				obj, notNullValue());
			
		assertThat(
				obj, instanceOf(SignInToXfinity.class));
			
	}

	
	@Test
	public void testGetNextPageWithUnIdentifiedPageSource() {
		
		String pageFlowId = "mockPageFlowId";
		
		String pageSource = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html lang=\"en\"><head>"
							+ "<title>Mock Page Title</title><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/styles.min.css?v=f6ce72c\">"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"><meta name=\"description\" content=\"Comcast.net has "
							+ "more to offer when you sign in to your mySIGN-IN Account. Use your account to access other Comcast sites and services like "
							+ "Comcast.net, Plaxo, and more.\"></head><body><div id=\"bd\" class=\"signin clearfix\"><div id=\"left\"><style type=\"text/css\">"
							+ "#ad-block{width:300px;margin:0 0 0 auto}#ad-wrapper{width:300px}#ads-info{font-size:10px;margin:9px 0 0 0;text-align:center}"
							+ "#ads-info a{margin-left:20px}#ads-info a.first{margin-left:0}</style></body></html>";
		
		
		Mockito
		       .when(mockDriver.getPageSource())
		       .thenReturn(pageSource);
		
		AbstractPageRouter identifier = (AbstractPageRouter)CommonContextInitializer.getBean(ICommonConstants.BEAN_NAME_UI_PAGE_ROUTER);
		navigator = new PageNavigator();
		navigator.setIdentifier(identifier);
		Object obj = navigator.getNextPage(mockDriver, mockPageObject, pageFlowId);
			
		assertThat(
				obj, notNullValue());
		
		assertThat(
				obj, instanceOf(PageError.class));
			
		PageError err = (PageError) obj;
			
		assertThat(
				err.getError(), is("System Error"));
		
	}

	
	/**
	 * This test class verifies the page routing is forced to the destination page 
	 * when the destination page is well known and not supposed to be changed.
	 */
	@Test
	public void testGetNextPageWithForcedDestination() {
		
		String pageFlowId = "mockPageFlowId";
		
		String pageSource = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html lang=\"en\"><head>"
							+ "<title>Sign in to Comcast</title><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css/styles.min.css?v=f6ce72c\">"
							+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\"><meta name=\"description\" content=\"Comcast.net has "
							+ "more to offer when you sign in to your mySIGN-IN Account. Use your account to access other Comcast sites and services like "
							+ "Comcast.net, Plaxo, and more.\"></head><body><div id=\"bd\" class=\"signin clearfix\"><div id=\"left\"><style type=\"text/css\">"
							+ "#ad-block{width:300px;margin:0 0 0 auto}#ad-wrapper{width:300px}#ads-info{font-size:10px;margin:9px 0 0 0;text-align:center}"
							+ "#ads-info a{margin-left:20px}#ads-info a.first{margin-left:0}</style></body></html>";
		
		
		Mockito
		       .when(mockDriver.getPageSource())
		       .thenReturn(pageSource);
		
		AbstractPageRouter identifier = (AbstractPageRouter)CommonContextInitializer.getBean(ICommonConstants.BEAN_NAME_UI_PAGE_ROUTER);
		navigator = new PageNavigator();
		navigator.setIdentifier(identifier);
		Object obj = navigator.getNextPage(mockDriver, mockPageObject, pageFlowId, PageError.class);
			
		assertThat(
				obj, notNullValue());
			
		assertThat(
				obj, instanceOf(PageError.class));
			
		PageError err = (PageError) obj;
			
		assertThat(
				err.getError(), is("System Error"));
			
	}
	
	
	@After
	public void tearDown() {
		CommonContextInitializer.destroyContext();
	}

}
