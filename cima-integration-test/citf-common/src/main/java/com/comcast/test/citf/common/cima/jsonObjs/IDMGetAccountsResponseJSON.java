package com.comcast.test.citf.common.cima.jsonObjs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
/*@JsonPropertyOrder({
        "username",
        "custGuid", 
        "authorizationRoles",
        "homePhone",
        "authorizations", 
})*/

@JsonIgnoreProperties(ignoreUnknown=true)
public class IDMGetAccountsResponseJSON {
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("custGuid")
	private String custGuid;
	
	@JsonProperty("authorizationRoles")
	private List<AuthorizationRole> authorizationRoles = new ArrayList<AuthorizationRole>();

	@JsonProperty("homePhone")
	private String homePhone;
	
	@JsonProperty("authorizations")
	private List<Authorization> authorizations = new ArrayList<Authorization>();
	
	
	@JsonProperty("username")
	public String getUsername() {
		return username;
	}

	@JsonProperty("username")
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty("custGuid")
	public String getCustGuid() {
		return custGuid;
	}

	@JsonProperty("custGuid")
	public void setCustGuid(String custGuid) {
		this.custGuid = custGuid;
	}

	@JsonProperty("authorizationRoles")
	public List<AuthorizationRole> getAuthorizationRoles() {
		return authorizationRoles;
	}

	@JsonProperty("authorizationRoles")
	public void setAuthorizationRoles(List<AuthorizationRole> authorizationRoles) {
		this.authorizationRoles = authorizationRoles;
	}

	@JsonProperty("homePhone")
	public String getHomePhone() {
		return homePhone;
	}

	@JsonProperty("homePhone")
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@JsonProperty("authorizations")
	public List<Authorization> getAuthorizations() {
		return authorizations;
	}

	@JsonProperty("authorizations")
	public void setAuthorizations(List<Authorization> authorizations) {
		this.authorizations = authorizations;
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class AuthorizationRole {
		
		@JsonProperty("roleType")
		private String roleType;
		
		@JsonProperty("xboServiceAccountId")
		private String xboServiceAccountId;
		
		@JsonProperty("authorization")
		private Authorization authorization;

		@JsonProperty("roleType")
		public String getRoleType() {
			return roleType;
		}

		@JsonProperty("roleType")
		public void setRoleType(String roleType) {
			this.roleType = roleType;
		}

		@JsonProperty("xboServiceAccountId")
		public String getXboServiceAccountId() {
			return xboServiceAccountId;
		}

		@JsonProperty("xboServiceAccountId")
		public void setXboServiceAccountId(String xboServiceAccountId) {
			this.xboServiceAccountId = xboServiceAccountId;
		}

		@JsonProperty("authorization")
		public Authorization getAuthorization() {
			return authorization;
		}

		@JsonProperty("authorization")
		public void setAuthorization(Authorization authorization) {
			this.authorization = authorization;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class Authorization {
		
		@JsonProperty("accountManagerGuid")
	    private String accountManagerGuid;
		
		@JsonProperty("accountStatus")
	    private String accountStatus;
		
		@JsonProperty("billingAccountId")
	    private String billingAccountId;
		
		
		@JsonProperty("accountManagerGuid")
		public String getAccountManagerGuid() {
			return accountManagerGuid;
		}

		@JsonProperty("accountManagerGuid")
		public void setAccountManagerGuid(String accountManagerGuid) {
			this.accountManagerGuid = accountManagerGuid;
		}

		@JsonProperty("accountStatus")
		public String getAccountStatus() {
			return accountStatus;
		}

		@JsonProperty("accountStatus")
		public void setAccountStatus(String accountStatus) {
			this.accountStatus = accountStatus;
		}

		@JsonProperty("billingAccountId")
		public String getBillingAccountId() {
			return billingAccountId;
		}

		@JsonProperty("billingAccountId")
		public void setBillingAccountId(String billingAccountId) {
			this.billingAccountId = billingAccountId;
		}
	}
	
}
