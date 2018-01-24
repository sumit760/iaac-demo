package com.comcast.test.citf.common.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * Class to parse CSV as well as excel file content and returns a list of maps for each row.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */

@Service("spreadSheetReader")
public class SpreadSheetReader {
	
	/**
	 * Reads any CSV file and list down all data which belong to the provided set of column names.
	 * It will generate a map for each row which contains same key name which are present in the input set.
	 * 
	 * @param fileName
	 * 					CSV file name.
	 * @param columnNames
	 * 					Column names.
	 * @return
	 * 					List of map for each row of CSV data.
	 */
	public List<Map<String, Object>> readCSV(String fileName, Set<String> columnNames){
		resultList = null;
		Reader reader = null;
		CSVParser parser = null;
		
		try{			
			if(fileName==null || fileName.length()<5 || columnNames==null || columnNames.isEmpty()){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}

			URL url = new ClassPathResource(fileName).getURI().toURL();
			reader = new InputStreamReader(new BOMInputStream(url.openStream()), ICommonConstants.ENCODING_UTF8);
			parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
			resultList = new LinkedList<Map<String,Object>>();
			
			for (CSVRecord record : parser) {
				
				if(record!=null && record.size()>0){
					Map<String, Object> resultMap = new HashMap<String, Object>();
					
					for(String inputCol : columnNames){
						try{
							String value = record.get(inputCol);
							if(value!=null && !value.isEmpty()){
								resultMap.put(inputCol, value.contains(REPLACEMENT_OF_COMMA_IN_CSV) ? value.replace(REPLACEMENT_OF_COMMA_IN_CSV, ICommonConstants.COMMA) : value);
							}
						}catch(Exception e){
							LOGGER.debug("Column not found in {} CSV file, exception : ", fileName, e);
						}
					}
					
					if(!resultMap.isEmpty()){
			    		resultList.add(resultMap);
		    		}
				}
			}
		
		}
		catch(Exception e){
			LOGGER.error("Error occurred while trying to read {} CSV file ", fileName, e);
		}
		finally{
			try{
				if(parser!=null) {
					parser.close();
				}
				if(reader!=null) {
					reader.close();
				}
			}catch (IOException e) {
    	       	LOGGER.warn("IOException occurred while doing cleanup activity : ", e);
    	    }
		}
		
		return resultList;
	}

	/**
	 * Method read any excel file and list down all data which belong to the provided set of column names.
	 * <p>
	 * It will generate a map for each row which contains same key name which are present in the input set.
	 * sheetNumbers is a optional field. It only loads the first sheet (by default) in case sheetNames is null.
	 * 
	 * @param fileName
	 * 					The excel file name.
	 * @param columnNames
	 * 					The column names.
	 * @param sheetNames
	 * 					The sheet name.
	 * @return
	 * 					List of map for each row of data.
	 */
	public List<Map<String, Object>> readExcel(String fileName, Set<String> columnNames, Set<String> sheetNames){
		resultList = null;
		InputStream fis = null;
		Workbook excel = null;
		
		try{
			if(fileName==null || fileName.length()<5 || columnNames==null || columnNames.isEmpty()){
				throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
			}
			
			fis = new ClassPathResource(fileName).getInputStream();
			
			if(fileName.contains(EXCEL_EXTN_XLSX)){
				excel = new XSSFWorkbook(fis);
			}	
			else if(fileName.contains(EXCEL_EXTN_XLS)){
				excel = new HSSFWorkbook(fis);
			}
			
			if(excel==null || excel.getNumberOfSheets()==0){
				throw new IllegalStateException("Input excel is either corrupted or does not contain any data!");
			}
			
			resultList = new LinkedList<Map<String,Object>>();
			
			if(sheetNames!=null){
				for(String sheetName : sheetNames){
					this.readSheet(excel, columnNames, sheetName, -1);
				}
			}
			else{
				this.readSheet(excel, columnNames, null, DEFAULT_SHEET_NO);
			}
			
			if(resultList!=null && resultList.size()==0){
				resultList = null;
			}
		}
		catch(Exception e){
			LOGGER.error("Error occurred while trying to read {} excel file ", excel, e);
		}
		finally{
			if(fis!=null){
				try{
					fis.close();
				}catch (IOException e) {
	    	       	LOGGER.warn("IOException occurred while doing cleanup activity : ", e);
	    	    }
			}
		}
		
		return resultList;
	}
	
	
	
	
	
	/********************************* Private variables & methods ************************************/
	
	private void readSheet(Workbook excel, Set<String> columnNames, String sheetName, int sheetNo) throws IllegalStateException{
		Sheet sheet = null;
		Row header;
		Map<String, Object> resultMap;
		String sheetInfo = "Sheet "+(sheetName!=null?("name "+sheetName):("No. "+sheetNo));
		
		if(sheetName!=null){
			sheet = excel.getSheet(sheetName);
		}
		else if(sheetNo>=0){
			sheet = excel.getSheetAt(sheetNo);
		}
		
		if(sheet==null){
			throw new IllegalStateException(sheetInfo+" is not found or invalid!!!");
		}
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		//filtering the column names
		if(rowIterator.hasNext()){
			header = rowIterator.next();
		}
		else{
			throw new IllegalStateException(sheetInfo+" is not found or invalid!!!");
		}
		
		Map<Integer, String> columnMap = new HashMap<Integer, String>();
		int columnNo = 0;
		
		Iterator<Cell> headerIterator = header.cellIterator();
	    while(headerIterator.hasNext()) {
			String column = headerIterator.next().getStringCellValue();
	        
			for(String inputCol : columnNames){
	        	if(column!=null && column.trim().equalsIgnoreCase(inputCol)){
	        		columnMap.put(columnNo, inputCol);
	        		break;
	        	}
	        }
			columnNo++;
		}
		
	    if(!columnMap.isEmpty()){
	    	while(rowIterator.hasNext()) {
	    		resultMap = new HashMap<String, Object>();
	    		columnNo = 0;
	    		Row row = rowIterator.next();
	         
	    		Iterator<Cell> cellIterator = row.cellIterator();
	    		while(cellIterator.hasNext()) {
	    			Cell cell = cellIterator.next();
	             
	    			if(columnMap.get(columnNo)!=null){
	    				switch(cell.getCellType()) {
	    					case Cell.CELL_TYPE_BOOLEAN:
	    						resultMap.put(columnMap.get(columnNo), cell.getBooleanCellValue());
	    						break;
	    					case Cell.CELL_TYPE_NUMERIC:
	    						resultMap.put(columnMap.get(columnNo), cell.getNumericCellValue());
		    					break;
	    					case Cell.CELL_TYPE_STRING:
	    						String val = cell.getStringCellValue();
	    						if(val!=null && !ICommonConstants.BLANK_STRING.equals(val.trim())){
	    							resultMap.put(columnMap.get(columnNo), val);
	    						}
		    					break;
	    				}
	    			}
	    			columnNo++;
	    		}
	    		
	    		if(!resultMap.isEmpty()){
		    		resultList.add(resultMap);
	    		}
	    	}
	    }
	    else
	    	LOGGER.warn("No matching column names found in {}", sheetInfo);
	}

	private static final String REPLACEMENT_OF_COMMA_IN_CSV = ";";
	
	private List<Map<String, Object>> resultList = null;
	private static final int DEFAULT_SHEET_NO = 0;
	
	private static final String EXCEL_EXTN_XLS = ".xls";
	private static final String EXCEL_EXTN_XLSX = ".xlsx";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpreadSheetReader.class);
}