package com.comcast.test.citf.common.cima.persistence;

import java.util.Map;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * DAO class for common operations in the database. 
 * 
 * @author Abhijit Rej (arej001c)
 * @since December 2015
 *
 */
@Repository("commonDao")
public class CommonDAO extends AbstractDAO{
	
	/**
	 * This method will be used to update single/multiple columns in any table with any number of conditions.
	 * 
	 * @param updateMap
	 * 			Map of column names and the values to be updated
	 * @param conditionMap
	 * 			Map of condition attributes to identify the row(s) to be updated
	 * @param tableName
	 * 			Name of the table
	 */
	@Transactional
	public int updateSingleTable(	Map<String, Object> updateMap, 
									Map<String, String> conditionMap, 
									String tableName){
		int updateQuantity=0;
		try{
			String qryString = this.buildQuery(updateMap, conditionMap, tableName);
			if(qryString!=null){
				Query query = getSession().createQuery(qryString);
			
				for(Map.Entry<String, Object> entry : updateMap.entrySet()){
					String key = entry.getKey();
					Object obj =  entry.getValue();
					if(obj instanceof String){
						query = query.setString(VARIRBLE_PREFIX + key, obj.toString());
					} else if(obj instanceof byte[]) {
						query = query.setBinary(VARIRBLE_PREFIX + key, (byte[])obj);
					}
					
				}
			
				if(conditionMap!=null && conditionMap.size()>0) {
					for(Map.Entry<String, String> entry : conditionMap.entrySet()){
						String key = entry.getKey();
						query = query.setString(VARIRBLE_PREFIX + key, entry.getValue());
					}
				}
			
				updateQuantity = query.executeUpdate();
			
				if(updateQuantity>0) {
					LOGGER.info("Update is successful");
				}
			}
		}
		catch(Exception e){
			LOGGER.error("Update for table: {} with updateMap:{} and conditionMap: {} was failed due to : \n", tableName, updateMap, conditionMap, e);
		}
		return updateQuantity;
	}
	
	
	/**
	 * Builds HQL query
	 * 
	 * @param updateMap
	 * 			Map of column names and the values to be updated
	 * @param conditionMap
	 * 			Map of condition attributes to identify the row(s) to be updated
	 * @param tableName
	 * 			Name of the table
	 * @return build HSQL query
	 */
	private String buildQuery(Map<String, Object> updateMap, Map<String, String> conditionMap, String tableName){
		String query = null;
		if(updateMap!=null && updateMap.size()>0 && tableName!=null){
			StringBuilder sbf = new StringBuilder();
			sbf.append(QUERY_UPDATE);
			sbf.append(tableName);
			sbf.append(QUERY_SET);
			
			int maxSize = updateMap.size();
			int index = 0;
			for(String key : updateMap.keySet()){
				sbf.append(key);
				sbf.append(EQUALS_IN_QUERY+key);
				index++;
				
				if(index<maxSize) {
					sbf.append(ICommonConstants.COMMA);
				}
			}
			
			if(conditionMap!=null && conditionMap.size()>0){
				sbf.append(QUERY_WHERE);
			
				maxSize = conditionMap.size();
				index = 0;
				for(String key : conditionMap.keySet()){
					sbf.append(key);
					sbf.append(EQUALS_IN_QUERY+key);
					index++;
				
					if(index<maxSize) {
						sbf.append(QUERY_AND);
					}
				}
			}
			
			if(sbf.length()>0) {
				query = sbf.toString();
			}
		}
		
		LOGGER.info("Query built : ", query);
		return query;
	}
	
	private static final String VARIRBLE_PREFIX  = "var";
	private static final String EQUALS_IN_QUERY  = " = :var";
	private static final String QUERY_AND  = " AND ";
	private static final String QUERY_WHERE  = " WHERE ";
	private static final String QUERY_SET  = " SET ";
	private static final String QUERY_UPDATE  = "UPDATE ";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDAO.class);
}
