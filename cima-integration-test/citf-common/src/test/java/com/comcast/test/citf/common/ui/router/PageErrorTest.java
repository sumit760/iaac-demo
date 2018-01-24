package com.comcast.test.citf.common.ui.router;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;



public class PageErrorTest {

	@Test
	public void testGetError() {
		
		PageError pageErr = new PageError("Mock Page Error", "Mock Page Error Description", "Mock Source Page");
		
		String err = pageErr.getError();
		
		assertThat(
				"Expected not null value",
				err, notNullValue());
		
		assertThat(err, is("Mock Page Error"));
	}
	
	
	@Test
	public void testSetPageError() {
		
		PageError pageErr = new PageError("Mock Page Error", "Mock Page Error Description", "Mock Source Page");
		
		pageErr.setError("Mock Page Error altered");
		
		assertThat(pageErr.getError(), is("Mock Page Error altered"));
		
	}
	
	
	@Test
	public void testGetErrorDescriptions() {
		
		List<String> errorDescriptions = Arrays.asList("description1","description2","description3");
		PageError pageErr = new PageError("Mock Page Error", errorDescriptions, "Mock Source Page");
		
		List<String> errs = pageErr.getErrorDescriptions();
		
		assertThat(
				"Expected not null value",
				errs, notNullValue());
		
		assertThat(errs, hasItems("description1","description2","description3"));
	}
	
	@Test
	public void testSetErrorDescriptions() {
		
		List<String> errorDescriptions = Arrays.asList("description1");
		PageError pageErr = new PageError("Mock Page Error", errorDescriptions, "Mock Source Page");
		
		pageErr.setErrorDescription(Arrays.asList("description2","description3"));
		
		assertThat(pageErr.getErrorDescriptions(), hasItems("description2","description3"));
	}
	
	
	@Test
	public void testGetSourcePage() {
		
		PageError pageErr = new PageError("Mock Page Error", "Mock Page Error Description", "Mock Source Page");
		
		String sourcePage = pageErr.getSourcePage();
		
		assertThat(
				"Expected not null value",
				sourcePage, notNullValue());
		
		assertThat(sourcePage, is("Mock Source Page"));
	}
	
	
	@Test
	public void testSetSourcePage() {
		
		PageError pageErr = new PageError("Mock Page Error", "Mock Page Error Description", "Mock Source Page");
		
		pageErr.setSourcePage("Mock Source Page altered");
		
		assertThat(pageErr.getSourcePage(), is("Mock Source Page altered"));
	}
}
