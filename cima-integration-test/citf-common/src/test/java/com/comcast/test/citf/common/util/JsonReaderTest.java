package com.comcast.test.citf.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.comcast.test.citf.common.reader.JsonReader;

public class JsonReaderTest{

	private final String scenario1="To validate the Graffiti Instance reporting1";
	private final String scenario2="To validate the Graffiti Instance reporting2";


	@Test
	public void testGenerateMergerdCucumberReport() {	
		try{
			String targetRootDirectory = "src/test/resources/";
			String fileContent1 = FileUtility.getFileContentAsString(targetRootDirectory + "cucumber1.json");
			String fileContent2 = FileUtility.getFileContentAsString(targetRootDirectory + "cucumber2.json");

			String MergedReport=JsonReader.generateMergerdCucumberReport(fileContent1,fileContent2);

			assertTrue(MergedReport.contains(scenario1));
			assertTrue(MergedReport.contains(scenario2));
			assertFalse(MergedReport.contains("notvalidscenario"));
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}

	}	


}
