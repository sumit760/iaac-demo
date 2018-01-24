package com.comcast.test.citf.common.cima.persistence;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.comcast.test.citf.common.cima.persistence.beans.TempParameters;
import com.comcast.test.citf.common.orm.AbstractDAO;
import com.comcast.test.citf.common.util.ICommonConstants;
import com.comcast.test.citf.common.util.MiscUtility;

/**
 * DAO class for temporary parameters. This class is accountable for
 * all the DB bound temporary operations.
 * 
 * @author Abhijit Rej (arej001c)
 * @since August 2015
 *
 */
@Repository("tempParameterDAO")
public class TempParameterDAO extends AbstractDAO{
	
	/**
	 * Gets the temporary parameter value by its key.
	 * 
	 * @param key The key of the temporary parameter.
	 *  
	 * @return The temporary parameter object.
	 */
	@Transactional(readOnly=true)
	public TempParameters findParameterByKey(String key){
        Criteria criteria = getSession().createCriteria(TempParameters.class);
        criteria.add(Restrictions.eq("key",key));
        return (TempParameters) criteria.uniqueResult();
    }

	/**
	 * Populates temporary parameters in the database.
	 * 
	 * @param key The Key of the parameter.
	 * @param value The Value of the parameter.
	 * @param addVal1 Any additional value associated with.
	 * @param addVal2 Any additional value associated with.
	 */
	@Transactional
	public void populateResponse(String key, String value, String addVal1, String addVal2){
		
		TempParameters dbParam = this.findParameterByKey(key);
		if(dbParam!=null && dbParam.getStatus()!=null && !ICommonConstants.DB_STATUS_DELETED.equals(dbParam.getStatus()))
			logger.error("Previous Temp Parameter[key = "+key+"] is still not consumed!!! Reseting the value with new result.");
		
		if(dbParam==null)
			dbParam = new TempParameters(key, value, addVal1, addVal2, MiscUtility.getCurrentSqlDate(), LAST_MODIFIED_BY_CITF_UI_SERVER, ICommonConstants.DB_STATUS_ACTIVE);
		else{
			dbParam.setValue(value);
			dbParam.setStatus(ICommonConstants.DB_STATUS_ACTIVE);
			dbParam.setAddVal1(addVal1);
			dbParam.setAddVal2(addVal2);
			dbParam.setModifiedBy(LAST_MODIFIED_BY_CITF_UI_SERVER);
			dbParam.setModifiedOn(MiscUtility.getCurrentSqlDate());
		}
		
		getSession().merge(dbParam);
    }
	
	
	/**
	 * Deactivates the temporary parameter.
	 * 
	 * @param key The temporary parameter key to deactivate.
	 */
	@Transactional
	public void deactivateParameter(String key){
		
		TempParameters dbParam = this.findParameterByKey(key);
		if(dbParam!=null && dbParam.getStatus()!=null && !ICommonConstants.DB_STATUS_DELETED.equals(dbParam.getStatus())){
			dbParam.setStatus(ICommonConstants.DB_STATUS_DELETED);
			dbParam.setModifiedBy(LAST_MODIFIED_BY_KEY_USER);
			dbParam.setModifiedOn(MiscUtility.getCurrentSqlDate());
			getSession().merge(dbParam);
		}
    }
	
	
	private static final String LAST_MODIFIED_BY_KEY_USER = "KEY-USER";
	private static final String LAST_MODIFIED_BY_CITF_UI_SERVER = "CITF-UI-SERVER";
	
	private static final Logger logger = LoggerFactory.getLogger(TempParameterDAO.class);
}
