package com.comcast.test.citf.common.ldap.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Bean class for LDAP Authorization object.
 * 
 * @author spal004c
 * @since 7/16/2015
 *
 */
public class LDAPAuthorization {
	
	private String cstAuthGuid;
	private String cstAccountManagerGuid;
	private String cstAccountPrimaryGuid;
	private String accountStatus;
	private String billingId;
	private String billingSystem;
	private String firstName;
	private String lastName;
	private String digitalVoiceStatus;		//SAML cos list value (XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_COS) "cdv" mapped here
	private String hsdStatus;				//SAML cos list value (XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_COS) "hsi" mapped here
	private String vidStatus;				//SAML cos list value (XMLParserHelper.ParsedMapKeys.KEY_CIMA_LGN_TKN_SAML_COS) "video" mapped here
	
	/**
	 * Gets cstAuthGuid of the account.
	 * 
	 * @return The cstAuthGuid of the account.
	 * 
	 */
	public String getCstAuthGuid() {
		return cstAuthGuid;
	}
	
	/**
	 * Sets the cstAuthGuid of the account.
	 * 
	 * @param cstAuthGuid The cstAuthGuid to set.
	 * 
	 */
	public void setCstAuthGuid(String cstAuthGuid) {
		this.cstAuthGuid = cstAuthGuid;
	}
	
	/**
	 * Gets the cstAccountManagerGuid.
	 *  
	 * @return The cstAccountManagerGuid.
	 * 
	 */
	public String getCstAccountManagerGuid() {
		return cstAccountManagerGuid;
	}
	
	/**
	 * Sets the cstAccountManagerGuid.
	 * 
	 * @param cstAccountManagerGuid The cstAccountManagerGuid.
	 */
	public void setCstAccountManagerGuid(String cstAccountManagerGuid) {
		this.cstAccountManagerGuid = cstAccountManagerGuid;
	}
	
	/**
	 * Gets the cstAccountPrimaryGuid.
	 * 
	 * @return The cstAccountPrimaryGuid.
	 * 
	 */
	public String getCstAccountPrimaryGuid() {
		return cstAccountPrimaryGuid;
	}
	
	/**
	 * Sets the cstAccountPrimaryGuid.
	 * 
	 * @param cstAccountPrimaryGuid The cstAccountPrimaryGuid to set.
	 */
	public void setCstAccountPrimaryGuid(String cstAccountPrimaryGuid) {
		this.cstAccountPrimaryGuid = cstAccountPrimaryGuid;
	}
	
	/**
	 * Gets the account status.
	 * 
	 * @return The account status.
	 */
	public String getAccountStatus() {
		return accountStatus;
	}
	
	/**
	 * Sets the account status.
	 * 
	 * @param accountStatus The account status to set for.
	 */
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
	
	/**
	 * Gets the account billingId.
	 * 
	 * @return The account billing Id.
	 */
	public String getBillingId() {
		return billingId;
	}
	
	/**
	 * Sets the account billing Id.
	 * 
	 * @param billingId The billingId to set for.
	 */
	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}
	
	/**
	 * Gets the account billing system like DST, CSG etc.
	 * 
	 * @return The Account billing system.
	 * 
	 */
	public String getBillingSystem() {
		return billingSystem;
	}
	
	/**
	 * Sets the account billing system.
	 * 
	 * @param billingSystem The account billing system to set for.
	 * 
	 */
	public void setBillingSystem(String billingSystem) {
		this.billingSystem = billingSystem;
	}
	
	/**
	 * Gets the account holders first name.
	 * 
	 * @return The first name of the account holder.
	 * 
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the account holders first name.
	 * 
	 * @param firstName The first name of the account holder.
	 * 
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name of the account holder.
	 * 
	 * @return The last name of the account holder.
	 * 
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets the last name of the account holder.
	 * 
	 * @param lastName The last name of the account holder.
	 * 
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Gets the Digital Voice Status of the account i.e. whether CDV is active/non-active/suspended
	 * for the account.
	 * 
	 * @return The digital voice (CDV) status.
	 */
	public String getDigitalVoiceStatus() {
		return digitalVoiceStatus;
	}
	
	/**
	 * Sets the Digital Voice Status of the account.
	 * 
	 * @param digitalVoiceStatus The Digital Voice Status to set.
	 * 
	 */
	public void setDigitalVoiceStatus(String digitalVoiceStatus) {
		this.digitalVoiceStatus = digitalVoiceStatus;
	}
	
	/**
	 * Gets the High Speed Data (HSD) Status of the account i.e. whether HSD is active/non-active/suspended
	 * for the account.
	 * 
	 * @return HSD status of the account.
	 * 
	 */
	public String getHsdStatus() {
		return hsdStatus;
	}
	
	/**
	 * Sets the Digital Voice Status of the account.
	 * 
	 * @param hsdStatus The HSD status to set for.
	 * 
	 */
	public void setHsdStatus(String hsdStatus) {
		this.hsdStatus = hsdStatus;
	}
	
	/**
	 * Gets the Video status (VID) of the account i.e. whether VID is active/non-active/suspended
	 * for the account.
	 * 
	 * @return The VID status of then account.
	 * 
	 */
	public String getVidStatus() {
		return vidStatus;
	}
	
	/**
	 * Sets the Video status (VID) of the account.
	 * 
	 * @param vidStatus The VID status to set for.
	 * 
	 */
	public void setVidStatus(String vidStatus) {
		this.vidStatus = vidStatus;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method m = propertyDescriptor.getReadMethod();
                if ((m != null)&&(!m.getName().equals("getClass"))) {
                    Object res = m.invoke(this);
                    if (res != null) {
                        sb.append(propertyDescriptor.getName()+"="+ res.toString()+"|");
                    }
                }
            }
            //remove trailing "|"
            sb.deleteCharAt(sb.length() - 1);

        }catch(Exception e){
        }
        return sb.toString();
		
	}

}
