package com.comcast.test.citf.core.setup;


/**
 * This interface contains enumerations of CSV headers used in setup CSV files.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */
public interface IDataLoderEnums {
	
	/**
	 * Column headers of Accounts setup CSV file
	 */
	enum AccountExcelColumns{
		BILL_ACCOUNT_ID("Billing Account Id"),
		SERVICE_ACCOUNT_ID("Service Account Id"),
		BILLING_SYSTEM("Billing System"),
		LOB("LOB"),
		ACCOUNT_TYPE("Account Type"),
		AUTH_GUID("Auth Guid"),
		FIRST_NAME("First Name"),
		LAST_NAME("Last Name"),
		ADDRESS("Address"),
		PHONE_NUMBER("Phone Number"),
		ZIP("Zip"),
		ACCOUNT_STATUS("Account Status"),
		TRANSFER_FLAG("Transfer Flag"),
		PHYSICAL_RESOURCE_LINK("Physical Resource Link"),
		FRESH_SSN("Fresh SSN"),
		FRESH_DOB("Fresh Date of Birth"),
		SSN_CREATE_DATE("SSN Create Date");
		
		private final String value;
        private AccountExcelColumns(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Column headers of Users setup CSV file
	 */
	enum UserExcelColumns{
		USER_ID("User Id"),
		PASSWORD("Password"),
		PRIMARY_BILLING_ACCOUNTS("Primary Billing Accounts"),
		SECONDARY_BILLING_ACCOUNTS("Secondary Billing Accounts"),
		CATEGORY("Category"),
		TV_RATING("TV Rating"),
		MOVIE_RATING("Movie rating"),
		LOGIN_STATUS("Login Status"),
		EMAIL("Email"),
		ALTERNATE_EMAIL("Alternate Email"),
		ALTERNATE_EMAIL_PASSWORD("Alternate Email Password"),
		FACEBOOK_ID("Facebook Id"),
		FACEBOOK_PASSWORD("Facebook Password"),
		DATE_OF_BIRTH("Date of Birth"),
		SSN("SSN"),
		CUSTOMER_GUID("Customer Guid"),
		SECRET_QUESTION("Secret Question"),
		SECRET_ANSWER("Secret Answer");
		
		private final String value;
        private UserExcelColumns(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Column headers of Channels setup CSV file
	 */
	enum ChannelDetails{
		CHANNEL_ID("Channel Id"),
		CHANNEL_NAME("Channel Name"),
		DESCRIPTION("Description"),
		STATION_ID("Station Id"),
		CONTENT_TYPE("Content Type");
		
		private final String value;
        private ChannelDetails(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
	
	/**
	 * Column headers of Ratings setup CSV file
	 */
	enum Ratings{
		RATING_NAME("Rating Name"),
		RATING_TYPE("Rating Type"),
		PRIORITY("Priority");
		
		private final String value;
        private Ratings(final String value) {
            this.value = value;
        }
        
        public String getValue(){
        	return this.value;
        }
	}
}
