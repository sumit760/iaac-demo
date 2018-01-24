package com.comcast.test.citf.common.cima.persistence.beans;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Bean class for Account User mapping.
 * 
 * @author arej001c
 *
 */
@Entity
@Table(name="user_account_map")
@AssociationOverrides({
    @AssociationOverride(name = "uaMapPrimaryKey.user",
        joinColumns = @JoinColumn(name = "user_id")),
    @AssociationOverride(name = "uaMapPrimaryKey.account",
        joinColumns = @JoinColumn(name = "account_id")) })
public class AccountUserMap {
	
	/**
	 * Default Constructor 
	 */
	public AccountUserMap(){
		
	}
	
	/**
	 * Constructor class specifying uaMapPrimaryKey and role
	 * 
	 * @param uaMapPrimaryKey
	 * 			AccountUserMap primary key
	 * @param role
	 * 			Role of the user
	 */
	public AccountUserMap(AccountUserId uaMapPrimaryKey, String role){
		this.uaMapPrimaryKey = uaMapPrimaryKey;
		this.role = role;
	}
	
	@EmbeddedId
	private AccountUserId uaMapPrimaryKey;
	
	@Column(name = "user_role", nullable = false, length = 1)
	private String role;
	
	/**
	 * Returns uaMapPrimaryKey of AccountUserMap
	 * @return uaMapPrimaryKey
	 * @see com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap.AccountUserId
	 */
	public AccountUserId getUaMapPrimaryKey() {
		return uaMapPrimaryKey;
	}

	/**
	 * Sets uaMapPrimaryKey of AccountUserMap
	 */
	public void setUaMapPrimaryKey(AccountUserId uaMapPrimaryKey) {
		this.uaMapPrimaryKey = uaMapPrimaryKey;
	}
	
	/**
	 * Returns role of AccountUserMap
	 * @return role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets role of AccountUserMap
	 * @param role
	 * 			Role of user
	 */
	public void setRole(String role) {
		this.role = role;
	}


	@Embeddable
	public static class AccountUserId implements Serializable { 
		
		private static final long serialVersionUID = 1L;

		@ManyToOne(cascade = CascadeType.ALL)
	    private Users user;
		
		@ManyToOne(cascade = CascadeType.ALL)
	    private Accounts account;
		
		/**
		 * Default constructor
		 */
		public AccountUserId(){
			
		}
	    
		/**
		 * Class constructor specifying Users and Accounts
		 * @param user
		 * 			Users @see com.comcast.test.citf.common.cima.persistence.beans.Users
		 * @param account
		 * 			Accounts @see com.comcast.test.citf.common.cima.persistence.beans.Accounts
		 */
	    public AccountUserId(Users user, Accounts account){
	    	this.user = user;
	    	this.account = account;
	    }
	   
	    /**
	     * Returns Users of AccountUserId
	     * @return Users
	     * 
	     * @see com.comcast.test.citf.common.cima.persistence.beans.Users
	     */
	    public Users getUser() {
	        return user;
	    }
	 
	    /**
	     * Sets Users of AccountUserId
	     * @param user	
	     * 			Users @see com.comcast.test.citf.common.cima.persistence.beans.Users
	     */
	    public void setUser(Users user) {
	        this.user = user;
	    }

	    /**
	     * Returns Accounts of AccountUserId
	     * @return account
	     * 
	     * @see com.comcast.test.citf.common.cima.persistence.beans.Accounts
	     */
		public Accounts getAccount() {
			return account;
		}

		/**
		 * Sets Accounts of AccountUserId
		 * @param account
		 * 			Accounts @see com.comcast.test.citf.common.cima.persistence.beans.Accounts
		 */
		public void setAccount(Accounts account) {
			this.account = account;
		}
	}
}