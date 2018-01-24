package com.comcast.test.citf.core.setup.cima;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;
import com.comcast.test.citf.core.init.ObjectInitializer;
import com.comcast.test.citf.core.setup.IDataLoderEnums;

/**
 * This class populates Accounts table in database after fetching the data from Account CSV file.
 * 
 * @author Abhijit Rej (arej001c)
 * @since November 2015
 *
 */
public class AccountExcelLoader implements IDataLoderEnums{
	
	/**
	 * Populates data in Accounts table after fetching it from CSV file.
	 * 
	 * @param inputFile
	 * 			The CSV file from which data will be fetched
	 * @param environment
	 * 			Execution environment
	 */
	public static int loadAccounts(String inputFile, String environment) {
		List<Map<String, Object>> accounts;
		int rowCount = 1;
			
		Set<String> columnNames = new HashSet<String>();
		columnNames.add(AccountExcelColumns.BILL_ACCOUNT_ID.getValue());
		columnNames.add(AccountExcelColumns.SERVICE_ACCOUNT_ID.getValue());
		columnNames.add(AccountExcelColumns.BILLING_SYSTEM.getValue());
		columnNames.add(AccountExcelColumns.LOB.getValue());
		columnNames.add(AccountExcelColumns.ACCOUNT_TYPE.getValue());
		columnNames.add(AccountExcelColumns.AUTH_GUID.getValue());
		columnNames.add(AccountExcelColumns.FIRST_NAME.getValue());
		columnNames.add(AccountExcelColumns.LAST_NAME.getValue());
		columnNames.add(AccountExcelColumns.ADDRESS.getValue());
		columnNames.add(AccountExcelColumns.PHONE_NUMBER.getValue());
		columnNames.add(AccountExcelColumns.ZIP.getValue());
		columnNames.add(AccountExcelColumns.ACCOUNT_STATUS.getValue());
		columnNames.add(AccountExcelColumns.TRANSFER_FLAG.getValue());
		columnNames.add(AccountExcelColumns.PHYSICAL_RESOURCE_LINK.getValue());
		columnNames.add(AccountExcelColumns.FRESH_SSN.getValue());
		columnNames.add(AccountExcelColumns.FRESH_DOB.getValue());
		columnNames.add(AccountExcelColumns.SSN_CREATE_DATE.getValue());
		
		
		accounts = ObjectInitializer.getExcelReader().readCSV(inputFile, columnNames);
		
		if(accounts!=null && !accounts.isEmpty()){
			
			for(Map<String, Object> accountData : accounts){
				
				if(accountData==null || accountData.get(AccountExcelColumns.BILL_ACCOUNT_ID.getValue())==null || 
						 accountData.get(AccountExcelColumns.AUTH_GUID.getValue())==null){
					LOGGER.warn("Mandatory data is missing in row[{}]. This row will be ignored!", rowCount);
					continue;
				}
				
				try{
					String accountId = accountData.get(AccountExcelColumns.BILL_ACCOUNT_ID.getValue()).toString();
					String authGuid = accountData.get(AccountExcelColumns.AUTH_GUID.getValue()).toString();

                    String serviceAccountId = accountData.get(AccountExcelColumns.SERVICE_ACCOUNT_ID.getValue())!=null ? accountData.get(AccountExcelColumns.SERVICE_ACCOUNT_ID.getValue()).toString() : null;
					String billingSystem = accountData.get(AccountExcelColumns.BILLING_SYSTEM.getValue())!=null ? accountData.get(AccountExcelColumns.BILLING_SYSTEM.getValue()).toString() : null;
					String fName = accountData.get(AccountExcelColumns.FIRST_NAME.getValue())!=null ? accountData.get(AccountExcelColumns.FIRST_NAME.getValue()).toString() : null;
					String lName = accountData.get(AccountExcelColumns.LAST_NAME.getValue())!=null ? accountData.get(AccountExcelColumns.LAST_NAME.getValue()).toString() : null;
					String address = accountData.get(AccountExcelColumns.ADDRESS.getValue())!=null ? accountData.get(AccountExcelColumns.ADDRESS.getValue()).toString() : null;
					String phone = accountData.get(AccountExcelColumns.PHONE_NUMBER.getValue())!=null ? accountData.get(AccountExcelColumns.PHONE_NUMBER.getValue()).toString() : null;
					String zip = accountData.get(AccountExcelColumns.ZIP.getValue())!=null ? accountData.get(AccountExcelColumns.ZIP.getValue()).toString() : null;
					String tFlag = accountData.get(AccountExcelColumns.TRANSFER_FLAG.getValue())!=null ? accountData.get(AccountExcelColumns.TRANSFER_FLAG.getValue()).toString() : null;
					String prLink = accountData.get(AccountExcelColumns.PHYSICAL_RESOURCE_LINK.getValue())!=null ? accountData.get(AccountExcelColumns.PHYSICAL_RESOURCE_LINK.getValue()).toString() : null;
					String acStatus = accountData.get(AccountExcelColumns.ACCOUNT_STATUS.getValue())!=null ? accountData.get(AccountExcelColumns.ACCOUNT_STATUS.getValue()).toString() : ICimaCommonConstants.DB_STATUS_ACTIVE;
					String ssn = accountData.get(AccountExcelColumns.FRESH_SSN.getValue())!=null ? accountData.get(AccountExcelColumns.FRESH_SSN.getValue()).toString() : null;
					String lob = accountData.get(AccountExcelColumns.LOB.getValue())!=null ? accountData.get(AccountExcelColumns.LOB.getValue()).toString() : null;
					String accountType = accountData.get(AccountExcelColumns.ACCOUNT_TYPE.getValue())!=null ? accountData.get(AccountExcelColumns.ACCOUNT_TYPE.getValue()).toString() : null;

					
					java.sql.Date dob = null;
					if(accountData.get(AccountExcelColumns.FRESH_DOB.getValue())!=null){
						String dobStr = null;
						try{
							dobStr = accountData.get(AccountExcelColumns.FRESH_DOB.getValue()).toString();
							dob = MiscUtility.getSqlDate(dobStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME);
						}catch(Exception parseException){
							LOGGER.warn("Not able to insert/update Fresh Date of Birth [{}] as it is not in the expected date format [{}] for accountId {}", 
									dobStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME, accountId);
						}
					}
					
					java.sql.Date ssnCreateDate = null;
					if(accountData.get(AccountExcelColumns.SSN_CREATE_DATE.getValue())!=null){
						String scStr = null;
						try{
							scStr = accountData.get(AccountExcelColumns.SSN_CREATE_DATE.getValue()).toString();
							ssnCreateDate = MiscUtility.getSqlDate(scStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME);
						}catch(Exception parseException){
							LOGGER.warn("Not able to insert/update Fresh SSN Create Date [{}] as it is not in the expected date format [{}] for accountId {}", 
									scStr, ICommonConstants.DATE_FORMAT_US_EN_WITHOUT_TIME, accountId);
						}
					}
					
					ObjectInitializer.getAccountsDAO().populateAccount(accountId, serviceAccountId, environment, billingSystem, authGuid, acStatus, fName, lName, phone, address,
                                                                        zip, tFlag, prLink, ssn, dob, ssnCreateDate, lob, accountType);
				}
				catch(Exception e){
					LOGGER.error("Account cannot be inserted due to ", e);
				}	
				
				rowCount++;
			}
			LOGGER.info("Account data population is over. The input file {} found with {} rows for {} environment.", inputFile, (rowCount-1), environment);
		}
		return rowCount;
	}
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountExcelLoader.class);
}