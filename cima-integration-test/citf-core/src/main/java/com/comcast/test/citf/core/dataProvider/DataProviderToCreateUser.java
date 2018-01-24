package com.comcast.test.citf.core.dataProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.cima.persistence.FreshUserDAO;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

/**
*
* @author Abhijit Rej
* @since May 2016
*
* This is data provider to provide required data to create a new user.
*
*/
@Service("freshUserDP")
public class DataProviderToCreateUser extends AbstractDataProvider{

	 public enum Filter{
	   	ALTERNATE_MAIL(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL),
	   	DOB(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB),
	   	FACE_BOOK_ID(ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID);

	    private final String value;
	    Filter(final String value) {
	       this.value = value;
	    }
	    public String getValue(){
	        return this.value;
	    }
	 }
	 
	 /**
	  * provides fresh user
	  * 
	  * @param filters
	  * 		Filters for selecting user
	  * @return FreshUsers instance
	  * @see com.comcast.test.citf.common.cima.persistence.beans.FreshUsers
	  */
	 public FreshUsers getFreshUser(Map<Filter, String> filters){
		 LOGGER.debug("Starting to fetch fresh user details with filter {}", filters!=null?Arrays.toString(filters.entrySet().toArray()):null);
		 FreshUsers user;
		 
		 List<FreshUserDAO.Query> queries = populateQueries(filters);
		 user = getFreshUserDAO().findUser(queries);
		 
		 LOGGER.debug("Fresh user fetched: {}", user);
		 return user;
	 }
	 
	 /**
	  * unlocks fresh user
	  * @param primaryKey
	  * 		primary key of fresh user
	  */
	 public void unlockFreshUser(int primaryKey){
		 getFreshUserDAO().unlockUser(primaryKey);
	 }
	 
	 
	 private List<FreshUserDAO.Query> populateQueries(Map<Filter, String> filters){
		 List<FreshUserDAO.Query> queries = null;
		 if(filters != null){
			 for(Map.Entry<Filter, String> filer : filters.entrySet()){
				 if(queries==null){
					 queries = new ArrayList<FreshUserDAO.Query>();
				 }
				 queries.add(prepareQueryToFetchFreshUser(filer.getKey().getValue(), filer.getValue()));
	         }
	     }
		 return queries;
	 }
	 
	private FreshUserDAO.Query prepareQueryToFetchFreshUser(String filterKey, String filterValue) {
		FreshUserDAO.Conditions condition = FreshUserDAO.Conditions.EXACT_VALUE;
		FreshUserDAO.Columns column = null;
		String value = null;

		if (ICimaCommonConstants.CACHE_FLTR_VALUE_NULL.equals(filterValue)) {
			condition = FreshUserDAO.Conditions.IS_NULL;
		} else if (ICimaCommonConstants.CACHE_FLTR_VALUE_NOT_NULL.equals(filterValue)) {
			condition = FreshUserDAO.Conditions.IS_NOT_NULL;
		} else {
			value = filterValue;
		}

		if (ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_FACE_BOOK_ID.equals(filterKey)) {
			column = FreshUserDAO.Columns.FACEBOOK;
		} else if (ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_DOB.equals(filterKey)) {
			column = FreshUserDAO.Columns.DOB;
		} else if (ICimaCommonConstants.CACHE_FLTR_CONDTN_USER_ATTR_ALTERNATE_MAIL.equals(filterKey)) {
			column = FreshUserDAO.Columns.ALTERNATE_EMAIL;
		}
		return new FreshUserDAO.Query(column, condition, value);
	}
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderToCreateUser.class);
}
