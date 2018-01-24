package com.comcast.test.citf.common.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.test.citf.common.reader.PropertyReader;

public class PropertyReaderTest {

	@Test
	public void testGetProperty()
	{
		try{
			String path="prop.properties";
			assertEquals(PropertyReader.getProperty(path,"prop.TestName"),"mockTestName");
			assertEquals(PropertyReader.getProperty(path,"prop.TestPath"),"mockTestPath");
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}


}
