package com.comcast.test.citf.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Utility class for File handling operations.
 * 
 * @author arej001c, spal004c
 * @since June 2015
 *
 */
public class FileUtility {

	/**
	 * Reads an InputStream and returns the string representation of the streams.
	 * 
	 * @see InputStream 
	 * 					The specified InputStream.
	 * @return result
	 * 					The string representation of the stream.
	 * @throws IOException
	 */
	public static String readInputStream(InputStream is) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		String result = null;
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));  
			String str;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			
			if(sb.length()>0){
				result = sb.toString();
			}
			
		}finally{
			if(br!=null){
				br.close();
			}
		}
		return result;
	}
	
	/**
	 * Gets the content of a file as String.
	 * 
	 * @param filePath 
	 * 					The path of the file
	 * @return output	
	 * 					The file content as string.
	 * @throws IOException
	 */
	public static String getFileContentAsString(String filePath) throws IOException{
		String output = null;
		FileInputStream fis = null;
		
		try{
			File file = new File(filePath);
			fis = new FileInputStream(file);
			output = readInputStream(fis);
		}finally{
			if(fis!=null) {
				fis.close();
			}
		}
		return output;
	}
	
	/**
	 * Loads files from classpath resource
	 * 
	 * @param fileName
	 * 					Classpath of the file
	 * @return Content of the file
	 * @throws IOException
	 */
	public static String loadFileFromClassPathAndGetContent(String fileName) throws IOException{
		String output = null;
		InputStream is = null;		
		try{
			is = new ClassPathResource(fileName).getInputStream();
			output = readInputStream(is);
		}finally{
			if(is!=null){
				is.close();
			}
		}
		return output;
	}
	
	/**
	 * Deletes all directories and files from the mentioned filePath.
	 * 
	 * @param filePath 
	 * 					The path of the file.
	 */
	public static void deleteAll(String filePath){
		try{
			if(filePath!=null){
				File file = new File(filePath);
				deleteAll(file);
				LOGGER.info("All directories/files have been deleted from {}",filePath);
			}
		}
		catch(Exception e){
			LOGGER.warn("Error occurred while deleting file {} : ", filePath, e);
		}
	}
	
	/**
	 * Counts the occurrence of the identifiers in a file .
	 * 
	 * @param filePath
	 * 						Path of the file.
	 * @param isAllIdentifiersMandatory
	 * 						Specifies whether all identifiers are mandatory.
	 * @param identifiers 
	 * 						The identifier whose count is being found.
	 * @return
	 * 						The count of occurrences.
	 * @throws IOException
	 */
	public static int countOccuranceInFile(String filePath, boolean isAllIdentifiersMandatory, String... identifiers) throws IOException{
		int count = 0;
		BufferedReader reader = null;

		try{
			Resource srcPR = new FileSystemResource(filePath);
			reader = new BufferedReader(new FileReader(srcPR.getFile()));
			int	identifiersCount = identifiers.length;
			String line;
			
			if(identifiersCount>1 && isAllIdentifiersMandatory){						//filtering using all the parameters with logical AND
				while ((line = reader.readLine()) != null) {
					int index = 0;
					
					for(String identifier: identifiers){
						if(line.contains(identifier)){
							index++;
							if(index == identifiersCount){
								count++;
							}
						}
						else{
							break;
						}
					}
				}
				
			}
			
			else{																		//filtering using all the parameters with logical OR
				while ((line = reader.readLine()) != null) {
					for(String identifier: identifiers){
						if(line.contains(identifier)){
							count++;
							break;
						}
					}
				}
			}
			
			LOGGER.info("{} contains {} {} times.", filePath, Arrays.toString(identifiers), String.valueOf(count));
		}
		finally{
			if(reader!=null){
				reader.close();
			}
		}
		
		return count;
	}
	
	/**
	 * Returns name of all files present in a directory.
	 * 
	 * @param directoryPath 
	 * 						The path of the directory
	 * @return
	 * 						The set of files present in the directory.
	 * @throws IOException
	 */
	public static Set<String> findFileNamesInDirectory(String directoryPath) throws IOException{
		 Set<String> fileNames = null;
		 String[] strFiles = null;
		
		 Resource srcPR = new FileSystemResource(directoryPath);
		 File directory = new File(srcPR.getURI());
		 if(directory.exists() && directory.isDirectory()){
			 strFiles = directory.list();
		 }
		 
		 if(strFiles!=null && strFiles.length>0){
			 for(String fileName : strFiles){				 
				 if(fileName!=null){
					 if(fileNames==null){
						 fileNames = new HashSet<String>();
					 }
					 fileNames.add(fileName);
				 }
			 }
		 }
		 
		 return fileNames;
	}
	
	/**
	 * Creates a new file.
	 * 
	 * @param fileName
	 * 					Name and path of the file.
	 * @param content
	 * 					The string content of the file.
	 * @return
	 * 					True - created successfully.
	 * 					False - otherwise.
	 * @throws IOException
	 */
	public static boolean createFile(String fileName, String content) throws IOException{
		boolean fileCreated = false;
		BufferedWriter writer = null;
		
		try{
			if(content == null || fileName == null){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			
			Path tempfilePath = Paths.get(fileName);
			writer = Files.newBufferedWriter(tempfilePath, StandardCharsets.UTF_8);
			writeLine(writer, content);
			
			fileCreated = true;
		}
		finally{
			if(writer!=null){
				writer.close();
			}
		}
		
		return fileCreated;
	}
	
	/**
	 * Creates a new file with the lines provided.
	 * 
	 * @param fileName
	 * 						Name and path of the file
	 * @param lines 
	 * 						List of lines.
	 * @return
	 * 						True - created successfully.
	 * 						False - otherwise.
	 * @throws IOException
	 */
	public static boolean createFile(String fileName, LinkedList<String> lines) throws IOException{
		boolean fileCreated = false;
		BufferedWriter writer = null;
		
		try{
			if(fileName==null || lines == null || lines.size()<=0){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			
			Path tempfilePath = Paths.get(fileName);
			writer = Files.newBufferedWriter(tempfilePath, StandardCharsets.UTF_8);
			
			for(String line: lines){
				writeLine(writer, line);
			}
			
			fileCreated = true;
		}
		finally{
			if(writer!=null){
				writer.close();
			}
		}
		
		return fileCreated;
	}
	
	/**
	 * Creates a directory in the mentioned path.
	 * 
	 * @param dirPath
	 * 					The path of the directory.
	 * @return
	 * 					True - created successfully.
	 * 					False - otherwise.
	 * @throws IOException
	 */
	public static boolean createDirectory(String dirPath) throws IOException{
		boolean isCreated = false;
		
		Path path = Paths.get(dirPath);
		Path returnPath = Files.createDirectories(path);
		
		if(returnPath!=null){
			isCreated = true;
		}
		
		return isCreated;
	}
	
	/************************* Private fields & methods **********************************************/
	
	private static void deleteAll(File file){

		if(file!=null && file.exists()) {
            if(file.isDirectory() && !ArrayUtils.isEmpty(file.list())) {
            	String[] strFiles = file.list();

            	for(String strFilename: strFiles) {
            		File fileToDelete = new File(file, strFilename);
            		deleteAll(fileToDelete);
            	}
            }
            file.delete();
		}
    }
	
	private static void writeLine(BufferedWriter writer, String line) throws IOException{
		writer.write(line, 0, line.length());
		writer.newLine();
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtility.class);
}