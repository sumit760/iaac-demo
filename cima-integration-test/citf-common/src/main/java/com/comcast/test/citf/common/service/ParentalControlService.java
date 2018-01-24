package com.comcast.test.citf.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comcast.test.citf.common.init.CommonContextInitializer;
import com.comcast.test.citf.common.ldap.LDAPInterface;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.parser.JSONParserHelper;
import com.comcast.test.citf.common.util.ICommonConstants;

/**
 * Service class for handling user parental control.
 * 
 * @author spal004c
 *
 */
@Service("parentalControlService")
public class ParentalControlService {
	
	/**
	 * Returns the parental control details of provided user id for the end url.
	 * 
	 * @param uid
	 * 								User id.
	 * @param prntlCntrlServiceURL
	 * 								parental control service URL.
	 * @return
	 * 								The parental control object.
	 * @see com.comcast.test.citf.common.service.ParentalControl
	 */	
	public ParentalControl getParentalControlDetails(String uid, String prntlCntrlServiceURL) {
		
		ParentalControl pc = null;
		
		try {
			LDAPInterface ldap = getLdapService();
			String mail = uid + "@comcast.net";
			LDAPCustomer customer = ldap.getCustomerData(ldap.new LDAPInstruction(LDAPInterface.SearchType.EQUAL_TO, LDAPInterface.LdapAttribute.CUSTOMER_MAIL, mail));
			String cstAuthGuid = customer.getCstAuthGuid();
			if (cstAuthGuid != null && !cstAuthGuid.isEmpty()) {
				String url = prntlCntrlServiceURL.replace("<cstAuthGuid>", cstAuthGuid);
				String resp = execute(ICommonConstants.PARENTAL_CONTROL_USER, ICommonConstants.PARENTAL_CONTROL_USER_PWD , url);
				if (resp != null && !resp.isEmpty()) {
					pc = (ParentalControl)JSONParserHelper.parseJSON(resp, ParentalControl.class);
				}
			}
		} catch(Exception e) {
			LOGGER.debug("Failure in processing parental control >> ", e);
		}
		
		return pc;
	}
	
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParentalControlService.class);
		
	public LDAPInterface getLdapService() {
		return (LDAPInterface) CommonContextInitializer.getBean("ldapService");
	}
	
	private String execute(String u, String p, String url) throws IOException {
		BufferedReader rd = null;
		HttpResponse response;
		StringBuffer result = null;
				
		try{
			HttpClient client = getHttpClient();
			HttpGet request = getHttpGetRequest(url);
		
			String unhashedString = u + ":" + p;
			String auth = new String(Base64.encodeBase64(unhashedString.getBytes()));
			request.setHeader("Authorization", "Basic " + auth);
		
			response = client.execute(request);
			rd = getBufferedReader(response);

			result = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}finally{
			try{
				if(rd!=null){
					rd.close();
				}
			} catch(Exception e) {
				LOGGER.warn("Error occured while doing clean up: ", e);
			}
		}
		
		return result.toString();
	}
	
	public HttpGet getHttpGetRequest(String url) {
		
		return new HttpGet(url);
	}
	
	public HttpClient getHttpClient() {
		return HttpClientBuilder.create().build();
	}

	public BufferedReader getBufferedReader(HttpResponse response) throws IOException {
		return new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
	}
}