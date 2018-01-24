package com.comcast.test.citf.common.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Bean class for ParentalControl.
 * 
 * @author arej001c
 *
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class ParentalControl {
	
	public String parentalPin;
	public List<String> protectedRatings;
	public List<String> protectedChannels;
	public List<String> protectedNetworks;
	
	/**
	 * Returns  parentalPin
	 * @return parentalPin
	 */
	public String getParentalPin() {
		return parentalPin;
	}
	/**
	 * Sets parentalPin
	 * @param parentalPin
	 * 			Parental pin
	 */
	public void setParentalPin(String parentalPin) {
		this.parentalPin = parentalPin;
	}
	/**
	 * Returns protectedRatings
	 * @return List of protectedRating
	 */
	public List<String> getProtectedRatings() {
		return protectedRatings;
	}
	
	/**
	 * Sets protectedRatings
	 * @param protectedRatings
	 * 			List of protectedRating
	 */
	public void setProtectedRatings(List<String> protectedRatings) {
		this.protectedRatings = protectedRatings;
	}
	/**
	 * Returns protectedChannels
	 * @return List of protectedChannel
	 */
	public List<String> getProtectedChannels() {
		return protectedChannels;
	}
	
	/**
	 * Sets protectedChannels 
	 * @param protectedChannels
	 * 			List of protectedChannel
	 */
	public void setProtectedChannels(List<String> protectedChannels) {
		this.protectedChannels = protectedChannels;
	}
	
	/**
	 * Returns protectedNetworks
	 * @return List of protectedNetwork
	 */
	public List<String> getProtectedNetworks() {
		return protectedNetworks;
	}
	
	/**
	 * Sets protectedNetworks
	 * @param protectedNetworks
	 * 			List of protectedNetwork
	 */
	public void setProtectedNetworks(List<String> protectedNetworks) {
		this.protectedNetworks = protectedNetworks;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb=new StringBuilder();
        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                Method m = propertyDescriptor.getReadMethod();
                if (m!=null && !m.getName().equals("getClass")) {
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
