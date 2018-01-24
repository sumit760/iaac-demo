package com.pwc.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Property {

	public HashMap<String,String> readProperty(){
		
		Properties prop=new Properties();
		FileInputStream file;
		String path="./connection.property";
		HashMap<String,String> propertyMap=new HashMap<String, String>();
		try {
			file=new FileInputStream(path);
			
			prop.load(file);
			file.close();
			propertyMap.put("Name", prop.getProperty("Name"));
			propertyMap.put("Password", prop.getProperty("Password"));
			propertyMap.put("Url", prop.getProperty("Url"));
			propertyMap.put("ClassName",prop.getProperty("ClassName"));
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return propertyMap;
	}
	
}
