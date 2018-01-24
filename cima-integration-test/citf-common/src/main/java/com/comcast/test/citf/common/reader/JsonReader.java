package com.comcast.test.citf.common.reader;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.StringUtility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Utility class for merging cucumber JSON reports.
 * 
 * @author arej001c
 *
 */
public class JsonReader {
	
	/**
	 * Merged more than one cucumber reports and generate the merged one.
	 * 
	 * @param individualReportAsString
	 * 									Individual cucumber reports as String.
	 * @return 
	 * 									The merged report.
	 * @throws IOException
	 */
	public static String generateMergerdCucumberReport(String... individualReportAsString) throws IOException{
		String fullReport = null;
		LOGGER.info("Starting merging cucumber report files with total No. of individual reports : {}", individualReportAsString!=null ? individualReportAsString.length : "0");
		
		if(individualReportAsString == null)
			throw new IllegalArgumentException(ICommonConstants.EXCEPTION_INVALID_INPUT);
		
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder sbf = new StringBuilder(individualReportAsString.length);
		
		sbf.append(ICommonConstants.START_SQUARE_BRACKET);
		for(String report: individualReportAsString){
			if(report!=null){
				JsonNode rootNode = mapper.readTree(report);
				if(rootNode!=null && rootNode.iterator().hasNext()){
					if(sbf.length()>1){
						sbf.append(ICommonConstants.COMMA);
						sbf.append(ICommonConstants.NEW_LINE);
					}
					
					JsonNode rootEle = rootNode.iterator().next();
					if(rootEle.get(CUCUMBER_REPORT_KEY_NAME)!=null && rootEle.get(CUCUMBER_REPORT_KEY_ELEMENTS)!=null){
						String oldName = rootEle.get(CUCUMBER_REPORT_KEY_NAME).textValue();
						
						LinkedHashSet<String> scenarioNames = getScenarioNames(rootEle);
						if(scenarioNames == null || scenarioNames.isEmpty()){
							continue;
						}
						 
						//Single scenario
						if(scenarioNames.size()==1){
							String newName = scenarioNames.iterator().next();
							 
							String uri = StringUtility.appendStrings(StringUtility.replaceSeparatorsInString(ICommonConstants.BLANK_SPACE_STRING, ICommonConstants.UNDER_SCORE, newName), ".scenario");
							((ObjectNode)rootEle).put(CUCUMBER_REPORT_KEY_NAME, newName);
							((ObjectNode)rootEle).put(CUCUMBER_REPORT_KEY_KEYWORD, CUCUMBER_REPORT_FEATURE_VALUE_OF_KEYWORD);
							((ObjectNode)rootEle).put(CUCUMBER_REPORT_KEY_URI, uri);
							
							sbf.append(rootEle.toString());
							LOGGER.info("Changing feature element name from '{}' to '{}' and keyword to '{}'", oldName, newName, CUCUMBER_REPORT_FEATURE_VALUE_OF_KEYWORD);
						}
						 
						//For report with multiple different scenarios
						else{
							boolean isFirst = true;
							for(String scenarioName : scenarioNames){
								JsonNode clone = rootEle.deepCopy();
								Iterator<JsonNode> elementsIterator = clone.get(CUCUMBER_REPORT_KEY_ELEMENTS).iterator();
								while(elementsIterator.hasNext()){
									JsonNode element = elementsIterator.next();
									if(!scenarioName.equalsIgnoreCase(element.get(CUCUMBER_REPORT_KEY_NAME).textValue())){
										elementsIterator.remove();
									}
								}
								 
								String uri = StringUtility.appendStrings(StringUtility.replaceSeparatorsInString(ICommonConstants.BLANK_SPACE_STRING, ICommonConstants.UNDER_SCORE, scenarioName), ".scenario");
								((ObjectNode)clone).put(CUCUMBER_REPORT_KEY_NAME, scenarioName);
								((ObjectNode)clone).put(CUCUMBER_REPORT_KEY_KEYWORD, CUCUMBER_REPORT_FEATURE_VALUE_OF_KEYWORD);
								((ObjectNode)clone).put(CUCUMBER_REPORT_KEY_URI, uri);
								
								if(!isFirst){
									sbf.append(ICommonConstants.COMMA);
									sbf.append(ICommonConstants.NEW_LINE);
								}
								else {
									isFirst = false;
								}
								sbf.append(clone.toString());
								LOGGER.info("Splitting and creating new scenario with name '{}' and keyword '{}'", scenarioName, CUCUMBER_REPORT_FEATURE_VALUE_OF_KEYWORD);
							}
						}
					}
				}
			}
		}
		sbf.append(ICommonConstants.END_SQUARE_BRACKET);
		
		if(sbf.length()>2){
			fullReport = sbf.toString();
			LOGGER.info("Merging of cucumber report files is successfully done");
		}
				
		return fullReport;
	}
	
	
	private static LinkedHashSet<String> getScenarioNames(JsonNode rootElement){
		LinkedHashSet<String> scenarioNames = null;
		
		if(rootElement!=null && rootElement.get(CUCUMBER_REPORT_KEY_ELEMENTS).iterator()!=null){
			scenarioNames = new LinkedHashSet<String>();
			
			Iterator<JsonNode> elementsIterator = rootElement.get(CUCUMBER_REPORT_KEY_ELEMENTS).iterator();
			while(elementsIterator.hasNext()){
				JsonNode element = elementsIterator.next();
				if(element.get(CUCUMBER_REPORT_KEY_NAME)!=null){
					scenarioNames.add(element.get(CUCUMBER_REPORT_KEY_NAME).textValue());
				}
			}
		}
		
		return scenarioNames;
	}
	
	private static final String CUCUMBER_REPORT_KEY_NAME = "name";
	private static final String CUCUMBER_REPORT_KEY_URI = "uri";
	private static final String CUCUMBER_REPORT_KEY_ELEMENTS = "elements";
	private static final String CUCUMBER_REPORT_KEY_KEYWORD = "keyword";
	private static final String CUCUMBER_REPORT_FEATURE_VALUE_OF_KEYWORD = "Scenario";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonReader.class);
}
