package com.comcast.test.citf.core.init;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.comcast.test.citf.core.cache.ConfigCache;
import com.comcast.test.citf.core.cache.StringCache;
import com.comcast.test.citf.core.cache.UserCache;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CoreContextInitilizerTest {
	
	@Before
	public void setup() {
		try{
			CoreContextInitilizer.initializeContext();
		}catch(Exception e){ Assert.fail(e.getMessage());}
	}
	
	@Test
	public void testGetBean()  {
		
		Object bean;
		
		bean = CoreContextInitilizer.getBean("stringCache");
		
		assertThat(
			bean,
			instanceOf(StringCache.class));
		

		bean = CoreContextInitilizer.getBean("configCache");
		
		assertThat(
			bean,
			instanceOf(ConfigCache.class));

		
		bean = CoreContextInitilizer.getBean("userCache");
		
		assertThat(
			bean,
			instanceOf(UserCache.class));
	}
	
	
	
	@Test
	public void testGetResource() {
		
		 InputStream is = CoreContextInitilizer.getResource("active-mq-config.properties");
		 
		 String content = null;
		 try{
			 content = IOUtils.toString(is);
		 }catch(IOException e){
				Assert.fail("Exception occurred while geeting content from InputStream " + e);
		 }	 
		 
		 assertThat(
					content,
					notNullValue());
		 
		 assertThat(
					content,
					containsString("activemq.data"));
		 
		 assertThat(
					content,
					containsString("activemq.kahadb"));
		 
	}
	
	@After
	public void tearDown() {
		try{
			CoreContextInitilizer.destroyContext();
		}catch(Exception e){}
	}

}