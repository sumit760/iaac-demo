package com.comcast.test.citf.core.setup.cima;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.setup.IDataLoderEnums;

/**
 * This class populates Channels and Asset Rating tables in database after fetching the data from corresponding CSV files.
 * 
 * @author Abhijit Rej (arej001c)
 * @since July 2015
 *
 */
public class ChannelAndRatingExcelLoader implements IDataLoderEnums{
	
	/**
	 * Populates data in Channel table after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 */
	public static void loadChannels(String inputFile) {
		List<Map<String, Object>> channels;
		
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(ChannelDetails.CHANNEL_ID.getValue());
		columnNames.add(ChannelDetails.CHANNEL_NAME.getValue());
		columnNames.add(ChannelDetails.DESCRIPTION.getValue());
		columnNames.add(ChannelDetails.STATION_ID.getValue());
		columnNames.add(ChannelDetails.CONTENT_TYPE.getValue());
		
		try{
			channels = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
			
			if(channels!=null && !channels.isEmpty()){
				int rowCount = 1;
				
				for(Map<String, Object> channel : channels){
					if(channel==null || channel.get(ChannelDetails.CHANNEL_ID.getValue())==null || 
							channel.get(ChannelDetails.CHANNEL_NAME.getValue())==null || channel.get(ChannelDetails.STATION_ID.getValue())==null){
						LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
						continue;
					}
				
					String desc = channel.get(ChannelDetails.DESCRIPTION.getValue())!=null ? channel.get(ChannelDetails.DESCRIPTION.getValue()).toString() : null;
					String contentType = channel.get(ChannelDetails.CONTENT_TYPE.getValue())!=null ? channel.get(ChannelDetails.CONTENT_TYPE.getValue()).toString() : null;
					ObjectInitializer.getChannelDAO().populateChannel(	channel.get(ChannelDetails.CHANNEL_ID.getValue()).toString(), 
																		channel.get(ChannelDetails.CHANNEL_NAME.getValue()).toString(), 
																		desc, 
																		channel.get(ChannelDetails.STATION_ID.getValue()).toString(), 
																		contentType, 
																		null);
				}
				
				rowCount++;
				LOGGER.info("Channel data population is over. The input file {} found with {} rows.", inputFile, (rowCount-1));
			}
		}
		catch(Exception t){
			LOGGER.error("Error occurred while populating channels in database ", t);
		}
	}
	
	/**
	 * Populates data in Asset Ratings table after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 */
	public static void loadRatings(String inputFile) {
		List<Map<String, Object>> ratings;
		
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(Ratings.RATING_NAME.getValue());
		columnNames.add(Ratings.RATING_TYPE.getValue());
		columnNames.add(Ratings.PRIORITY.getValue());
		
		try{
			ratings = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
			
			if(ratings!=null && !ratings.isEmpty()){
				int rowCount = 1;
				
				for(Map<String, Object> rating : ratings){
					if(rating==null || rating.get(Ratings.RATING_NAME.getValue())==null || 
							rating.get(Ratings.RATING_TYPE.getValue())==null || rating.get(Ratings.PRIORITY.getValue())==null){
						LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
						continue;
					}
					
					ObjectInitializer.getRatingDAO().populateAssetRating(	rating.get(Ratings.RATING_NAME.getValue()).toString(), 
																			rating.get(Ratings.RATING_TYPE.getValue()).toString(), 
																			Integer.parseInt(rating.get(Ratings.PRIORITY.getValue()).toString()));
					
				}
				
				rowCount++;
				LOGGER.info("Rating data population is over. The input file {} found with {} rows.", inputFile, (rowCount-1));
			}
		}
		catch(Exception t){
			LOGGER.error("Error occurred populating ratings in database ", t);
		}
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ChannelAndRatingExcelLoader.class);
}
