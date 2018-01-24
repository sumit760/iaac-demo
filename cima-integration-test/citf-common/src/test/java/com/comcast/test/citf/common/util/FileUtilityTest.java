package com.comcast.test.citf.common.util;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class FileUtilityTest {

	private final String identifiers="@Integration";
	private final String currentDir = System.getProperty("user.dir")+"/target";


	@Test
	public void testGetFileContentAsString() {
		try{
			String sysPath=currentDir+"/Test1.txt";
			FileUtility.createFile(sysPath, "abcd");
			String s=FileUtility.getFileContentAsString(sysPath);
			assertEquals(s,"abcd");
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}


	@Test
	public void testCountOccuranceInFile() {
		String sysPath=currentDir+"/Test2.txt";
		LinkedList<String> lines=new LinkedList<String>();
		lines.add("@Integration Feature1");
		lines.add("@Integration Feature2");
		lines.add("@Smoke Feature3");

		try{
			FileUtility.createFile(sysPath, lines);
			int i=FileUtility.countOccuranceInFile(sysPath, true, identifiers);
			assertEquals(i,2);
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

	@Test
	public void testFindFileNamesInDirectory() {
		try{
			Set<String> s = FileUtility.findFileNamesInDirectory(currentDir);
			assertTrue(s.size()>0);
			assertThat(s, hasItems("Test2.txt","Directory2","Test4.txt"));
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}


	@Test
	public void testCreateFile() {
		String sysPath=currentDir+"/Test3.txt";
		
		try{
			assertTrue(FileUtility.createFile(sysPath, "New File with valid data"));
			String fileContent = FileUtils.readFileToString(new File(sysPath), StandardCharsets.UTF_8);
			assertThat(fileContent, containsString("New File with valid data"));
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

	@Test
	public void testCreateFilewithLines() {
		LinkedList<String> lines = new LinkedList<String>();
		String sysPath=currentDir+"/Test4.txt";
		lines.add("Hi hello how r u??");
		lines.add("I m not fine");

		try{
			assertTrue(FileUtility.createFile(sysPath, lines));
			String fileContent = FileUtils.readFileToString(new File(sysPath), StandardCharsets.UTF_8);
			assertThat(fileContent, containsString("Hi hello how r u??"));
			assertThat(fileContent, containsString("I m not fine"));
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}


	@Test
	public void testCreateDirectory() {
		String dirPath=currentDir+"/Directory1";
		
		try{
			assertTrue(FileUtility.createDirectory(dirPath));
			assertThat(Files.exists(Paths.get(dirPath), LinkOption.NOFOLLOW_LINKS), is(true));
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

	@Test
	public void testDeleteAll() {
		String dirPath=currentDir+"/Directory2";
		
		try{
			FileUtility.createDirectory(dirPath);
			String sysPath1=dirPath+"/Test1.txt";
			FileUtility.createFile(sysPath1, "abcd");
			String sysPath2=dirPath+"/Test2.txt";
			FileUtility.createFile(sysPath2, "abcd1");
			FileUtility.deleteAll(sysPath1);
			Set<String>  fileNames=FileUtility.findFileNamesInDirectory(dirPath);
			for (String file : fileNames) {
				assertTrue(!file.startsWith("Test1"));
			}
		}catch(IOException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

}
