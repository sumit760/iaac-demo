package com.comcast.test.citf.common.ldap.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean class for LDAP Customer object.
 * 
 * @author spal004c
 * @since 7/16/2015
 *
 */
public class LDAPCustomer {
	
	private String cstCustGuid;
	private String cstAuthGuid;
	private String cstContactEmail;
	private String cstContactEmailStatus;
	private String cstPreferredCommunicationEmail;
	private String cstPwdHint;
	private String cstPwdHintAnswer;
	private String cstCompromisedFlag;
	private List<String> cstManageeGuids;
	private List<String> cstManagerGuids;
	private String cstLoginStatus;
	private String firstName;
	private String lastName;
	private String role;
	private String email;
	private String uid;
	private String mobile;
	
	/**
	 * Returns the cstCustGuid of the customer.
	 * 
	 * @return cstCustGuid.
	 */
	public String getCstCustGuid() {
		return cstCustGuid;
	}
	
	/**
	 * Sets cstCustGuid of customer.
	 * 
	 * @param cstCustGuid 
	 * 			cstCustGuid to set.
	 */
	public void setCstCustGuid(String cstCustGuid) {
		this.cstCustGuid = cstCustGuid;
	}
	
	/**
	 * Returns cstAuthGuid of customer.
	 * 
	 * @return cstAuthGuid.
	 */
	public String getCstAuthGuid() {
		return cstAuthGuid;
	}
	
	/**
	 * Sets cstAuthGuid of customer.
	 * 
	 * @param cstAuthGuid 
	 * 			cstAuthGuid to set.
	 */
	public void setCstAuthGuid(String cstAuthGuid) {
		this.cstAuthGuid = cstAuthGuid;
	}
	
	/**
	 * Returns cstContactEmail of customer.
	 * 
	 * @return cstContactEmail.
	 */
	public String getCstContactEmail() {
		return cstContactEmail;
	}
	
	/**
	 * Sets cstContactEmail of customer.
	 * 
	 * @param cstContactEmail 
	 * 			cstContactEmail to set.
	 */
	public void setCstContactEmail(String cstContactEmail) {
		this.cstContactEmail = cstContactEmail;
	}
	
	/**
	 * Returns cstContactEmailStatus of customer.
	 *  
	 * @return cstContactEmailStatus.
	 */
	public String getCstContactEmailStatus() {
		return cstContactEmailStatus;
	}
	
	/**
	 * Sets cstContactEmailStatus of customer.
	 * 
	 * @param cstContactEmailStatus 
	 * 			cstContactEmailStatus to set.
	 */
	public void setCstContactEmailStatus(String cstContactEmailStatus) {
		this.cstContactEmailStatus = cstContactEmailStatus;
	}
	
	/**
	 * Returns cstPreferredCommunicationEmail of customer.
	 * 
	 * @return cstPreferredCommunicationEmail.
	 */
	public String getCstPreferredCommunicationEmail() {
		return cstPreferredCommunicationEmail;
	}
	
	/**
	 * Sets cstPreferredCommunicationEmail of customer.
	 * 
	 * @param cstPreferredCommunicationEmail 
	 * 			cstPreferredCommunicationEmail to set.
	 */
	public void setCstPreferredCommunicationEmail(
			String cstPreferredCommunicationEmail) {
		this.cstPreferredCommunicationEmail = cstPreferredCommunicationEmail;
	}
	
	/**
	 * Returns cstPwdHint of customer.
	 * 
	 * @return passwordHint of customer.
	 */
	public String getCstPwdHint() {
		return cstPwdHint;
	}
	
	/**
	 * Sets cstPwdHint of customer.
	 * 
	 * @param cstPwdHint 
	 * 			cstPwdHint to set.
	 */
	public void setCstPwdHint(String cstPwdHint) {
		this.cstPwdHint = cstPwdHint;
	}
	
	/**
	 * Returns cstPwdHintAnswer of customer.
	 * 
	 * @return cstPwdHintAnswer.
	 */
	public String getCstPwdHintAnswer() {
		return cstPwdHintAnswer;
	}
	
	/**
	 * Sets cstPwdHintAnswer of customer.
	 * 
	 * @param cstPwdHintAnswer 
	 * 			cstPwdHintAnswer to set.
	 */
	public void setCstPwdHintAnswer(String cstPwdHintAnswer) {
		this.cstPwdHintAnswer = cstPwdHintAnswer;
	}
	
	/**
	 * Returns cstCompromisedFlag of customer.
	 * 
	 * @return cstCompromisedFlag.
	 */
	public String getCstCompromisedFlag() {
		return cstCompromisedFlag;
	}
	
	/**
	 * Sets cstCompromisedFlag of customer.
	 * 
	 * @param cstCompromisedFlag 
	 * 			cstCompromisedFlag to set.
	 */
	public void setCstCompromisedFlag(String cstCompromisedFlag) {
		this.cstCompromisedFlag = cstCompromisedFlag;
	}
	
	/**
	 * Returns cstManageeGuids of customer.
	 * 
	 * @return The list of cstManageeGuids.
	 */
	public List<String> getCstManageeGuids() {
		return cstManageeGuids;
	}
	
	/**
	 * Sets cstManageeGuids of customer.
	 * 
	 * @param cstManageeGuid 
	 * 			The list of cstManageeGuids.
	 */
	public void setCstManageeGuids(String cstManageeGuid) {
		if (this.cstManageeGuids == null) {
			cstManageeGuids = new ArrayList<String>();
		}
		this.cstManageeGuids.add(cstManageeGuid);
	}
	
	/**
	 * Returns cstManagerGuids of customer.
	 * 
	 * @return The list of cstManagerGuids.
	 */
	public List<String> getCstManagerGuids() {
		return cstManagerGuids;
	}
	
	/**
	 * Sets cstManagerGuid of customer.
	 * 
	 * @param cstManagerGuid 
	 * 			The list of cstManagerGuids.
	 */
	public void setCstManagerGuids(String cstManagerGuid) {
		if (this.cstManagerGuids == null) {
			cstManagerGuids = new ArrayList<String>();
		}
		this.cstManagerGuids.add(cstManagerGuid);
	}

	
	public String getCstLoginStatus() {
		return cstLoginStatus;
	}
	
	public void setCstLoginStatus(String cstLoginStatus) {
		this.cstLoginStatus = cstLoginStatus;
	}

	/**
	 * Returns firstName of customer.
	 * 
	 * @return Firstname.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Sets the firstName of customer.
	 * 
	 * @param firstName 
	 * 			First name to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * Returns the lastName of customer.
	 * 
	 * @return Lastname.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Sets lastName of customer.
	 * 
	 * @param lastName 
	 * 			Last name to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * Returns the role of customer.
	 * 
	 * @return Customer role.
	 */
	public String getRole() {
		return role;
	}
	
	/** 
	 * Sets the role of customer.
	 * 
	 * @param role 
	 * 			Customer role.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Returns the email of customer.
	 * 
	 * @return Email of customer.
	 */
	public String getMobile() {
		return mobile;
	}
	
	/** 
	 * Sets mobile of customer.
	 * 
	 * @param mobile
	 * 			Mobile Number
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * Returns mobile of customer.
	 * 
	 * @return mobile
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email of customer.
	 * 
	 * @param email 
	 * 			Email of customer.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns uid of the user.
	 * 
	 * @return UID
	 */
	public String getUid() {
		return uid;
	}
	
	/**
	 * Sets the uid of the user.
	 * 
	 * @param uid 
	 * 			UID
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method m = propertyDescriptor.getReadMethod();
                if (m!=null && !m.getName().equals("getClass") ) {
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
