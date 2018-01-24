package com.comcast.test.citf.common.ldap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.runners.MockitoJUnitRunner;

import com.comcast.test.citf.common.ldap.LDAPInterface.LDAPInstruction;
import com.comcast.test.citf.common.ldap.LDAPInterface.LdapAttribute;
import com.comcast.test.citf.common.ldap.LDAPInterface.SearchType;
import com.comcast.test.citf.common.ldap.model.LDAPAuthorization;
import com.comcast.test.citf.common.ldap.model.LDAPCustomer;
import com.comcast.test.citf.common.util.cima.ICimaCommonConstants;

@RunWith(MockitoJUnitRunner.class)
public class LDAPInterfaceTest {
	
	private LDAPCustomer mockCustomer;
	private LDAPAuthorization mockAuthorization;
	private LDAPInterface ldap;
	private DirContext mockCtx;
	private SearchControls mockSearchControls;
	@SuppressWarnings("rawtypes")
	private NamingEnumeration mockResults;
	private SearchResult mockSearchResults;
	private Attributes mockAttributes;
	private Attribute mockAttribute;
	@SuppressWarnings("rawtypes")
	private NamingEnumeration mockEnumeration;
	private Binding mockBinding;
	private LdapName mockLdapName;
	private ModificationItem mockModificationItem;
	private LDAPInstruction mockLDAPInstruction;
	
	@Before
	public void init() {
		ldap = Mockito.spy(new LDAPInterface());
		mockCustomer = Mockito.mock(LDAPCustomer.class,Mockito.RETURNS_DEEP_STUBS);
		mockAuthorization = Mockito.mock(LDAPAuthorization.class,Mockito.RETURNS_DEEP_STUBS);
		mockCtx = Mockito.mock(DirContext.class,Mockito.RETURNS_DEEP_STUBS); 
		mockSearchControls = Mockito.mock(SearchControls.class,Mockito.RETURNS_DEEP_STUBS); 
		mockResults = Mockito.mock(NamingEnumeration.class,Mockito.RETURNS_DEEP_STUBS); 
		mockSearchResults = Mockito.mock(SearchResult.class,Mockito.RETURNS_DEEP_STUBS);
		mockAttributes = Mockito.mock(Attributes.class,Mockito.RETURNS_DEEP_STUBS);
		mockAttribute = Mockito.mock(Attribute.class,Mockito.RETURNS_DEEP_STUBS);
		mockEnumeration = Mockito.mock(NamingEnumeration.class,Mockito.RETURNS_DEEP_STUBS);
		mockBinding = Mockito.mock(Binding.class,Mockito.RETURNS_DEEP_STUBS);
		mockLdapName = Mockito.mock(LdapName.class,Mockito.RETURNS_DEEP_STUBS);
		mockModificationItem = Mockito.mock(ModificationItem.class,Mockito.RETURNS_DEEP_STUBS);
		mockLDAPInstruction = Mockito.mock(LDAPInstruction.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetCustomerData() {
		
		String custGuid = "mockCustGuid";
		String authGuid = "mockAuthGuid";
		String contactEmail = "mockContactEmail";
		String contactEmailStatus = "mockContactEmailStatus";
		String preferredCommunicationEmail = "mockPreferredCommunicationEmail";
		String pwdHint = "mockPwdHint";
		String pwdHintAnswer = "mockPwdHintAnswer";
		String compromisedFlag = "mockCompromisedFlag";
		List<String> cstManageeGuids;
		List<String> cstManagerGuids;
		String firstName = "mockFirstName";
		String lastName = "mockLastName";
		String role = "mockRole";
		String email = "mockEmail";
		String uid = "mockUid";
		
		cstManageeGuids = new ArrayList<>();
		cstManagerGuids = new ArrayList<>();
		
		cstManageeGuids.add("mockManageeGuid");
		cstManagerGuids.add("mockManagerGuid");
		
		try{
			//Define the Stub
			Mockito
			.doReturn(mockCustomer)
			.when(ldap)
			.getLDAPCustomer();

			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getAttributes())
			.thenReturn(mockAttributes);

			Mockito
			.when(mockAttributes.get(Mockito.anyString()))
			.thenReturn(mockAttribute);

			Mockito
			.when(mockAttribute.get())
			.thenReturn("mockAttrVal");

			Mockito
			.when(mockCustomer.getCstCustGuid())
			.thenReturn(custGuid);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstCustGuid(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstAuthGuid())
			.thenReturn(authGuid);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstAuthGuid(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstContactEmail())
			.thenReturn(contactEmail);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstContactEmail(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstContactEmailStatus())
			.thenReturn(contactEmailStatus);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstContactEmailStatus(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstPreferredCommunicationEmail())
			.thenReturn(preferredCommunicationEmail);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstPreferredCommunicationEmail(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstPwdHint())
			.thenReturn(pwdHint);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstPwdHint(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstPwdHintAnswer())
			.thenReturn(pwdHintAnswer);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstPwdHintAnswer(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstCompromisedFlag())
			.thenReturn(compromisedFlag);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstCompromisedFlag(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstManageeGuids())
			.thenReturn(cstManageeGuids);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstManageeGuids(Mockito.anyString());

			Mockito
			.when(mockCustomer.getCstManagerGuids())
			.thenReturn(cstManagerGuids);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setCstManagerGuids(Mockito.anyString());

			Mockito
			.when(mockCustomer.getFirstName())
			.thenReturn(firstName);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setFirstName(Mockito.anyString());

			Mockito
			.when(mockCustomer.getLastName())
			.thenReturn(lastName);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setLastName(Mockito.anyString());

			Mockito
			.when(mockCustomer.getRole())
			.thenReturn(role);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setRole(Mockito.anyString());

			Mockito
			.when(mockCustomer.getEmail())
			.thenReturn(email);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setEmail(Mockito.anyString());

			Mockito
			.when(mockCustomer.getUid())
			.thenReturn(uid);

			Mockito
			.doNothing()
			.when(mockCustomer)
			.setUid(Mockito.anyString());

			Mockito
			.doNothing()
			.when(mockResults)
			.close();

			Mockito
			.doNothing()
			.when(mockCtx)
			.close();
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
		
		LDAPCustomer ldapCust = ldap.getCustomerData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_GUID,"mock value"));
		Assert.assertEquals(custGuid,ldapCust.getCstCustGuid());
		Assert.assertEquals(authGuid,ldapCust.getCstAuthGuid());
		Assert.assertEquals(contactEmail,ldapCust.getCstContactEmail());
		Assert.assertEquals(contactEmailStatus,ldapCust.getCstContactEmailStatus());
		Assert.assertEquals(preferredCommunicationEmail,ldapCust.getCstPreferredCommunicationEmail());
		Assert.assertEquals(pwdHint,ldapCust.getCstPwdHint());
		Assert.assertEquals(pwdHintAnswer,ldapCust.getCstPwdHintAnswer());
		Assert.assertEquals(compromisedFlag,ldapCust.getCstCompromisedFlag());
		
		Assert.assertEquals(cstManageeGuids,ldapCust.getCstManageeGuids());
		Assert.assertEquals(cstManagerGuids,ldapCust.getCstManagerGuids());
		
		Assert.assertEquals(firstName,ldapCust.getFirstName());
		Assert.assertEquals(lastName,ldapCust.getLastName());
		Assert.assertEquals(role,ldapCust.getRole());
		Assert.assertEquals(email,ldapCust.getEmail());
		Assert.assertEquals(uid,ldapCust.getUid());
	}

	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetAuthorizationData() {
		
		String authGuid = "mockAuthGuid";
		String accountManagerGuid = "mockAccountManagerGuid";
		String accountPrimaryGuid = "mockAccountPrimaryGuid";
		String accountStatus = "mockAccountStatus";
		String billingId = "mockBillingId";
		String billingSystem = "mockbillingSystem";
		String firstName = "mockFirstName";
		String lastName = "mockLastName";
		String digitalVoiceStatus = "mockCDV";
		String hsdStatus = "mockHSD";
		String vidStatus = "mockVID";
		
		try{
			//Define the Stub
			Mockito
			.doReturn(mockAuthorization)
			.when(ldap)
			.getLDAPAuthorization();

			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getAttributes())
			.thenReturn(mockAttributes);

			Mockito
			.when(mockAttributes.get(Mockito.anyString()))
			.thenReturn(mockAttribute);

			Mockito
			.when(mockAttribute.get())
			.thenReturn("mockAttrVal");

			Mockito
			.when(mockAuthorization.getCstAuthGuid())
			.thenReturn(authGuid);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setCstAuthGuid(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getCstAccountManagerGuid())
			.thenReturn(accountManagerGuid);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setCstAuthGuid(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getCstAccountPrimaryGuid())
			.thenReturn(accountPrimaryGuid);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setCstAccountPrimaryGuid(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getAccountStatus())
			.thenReturn(accountStatus);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setAccountStatus(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getBillingId())
			.thenReturn(billingId);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setBillingId(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getBillingSystem())
			.thenReturn(billingSystem);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setBillingSystem(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getFirstName())
			.thenReturn(firstName);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setFirstName(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getLastName())
			.thenReturn(lastName);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setLastName(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getDigitalVoiceStatus())
			.thenReturn(digitalVoiceStatus);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setDigitalVoiceStatus(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getHsdStatus())
			.thenReturn(hsdStatus);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setHsdStatus(Mockito.anyString());

			Mockito
			.when(mockAuthorization.getVidStatus())
			.thenReturn(vidStatus);

			Mockito
			.doNothing()
			.when(mockAuthorization)
			.setVidStatus(Mockito.anyString());

			Mockito
			.doNothing()
			.when(mockResults)
			.close();

			Mockito
			.doNothing()
			.when(mockCtx)
			.close();
		
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
		LDAPAuthorization ldapAuth = ldap.getAuthorizationData(ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.AUTHORIZATION_BILLING_ACCOUNT_ID,"mock value"));
		Assert.assertEquals(authGuid,ldapAuth.getCstAuthGuid());
		Assert.assertEquals(accountManagerGuid,ldapAuth.getCstAccountManagerGuid());
		Assert.assertEquals(accountPrimaryGuid,ldapAuth.getCstAccountPrimaryGuid());
		Assert.assertEquals(accountStatus,ldapAuth.getAccountStatus());
		Assert.assertEquals(billingId,ldapAuth.getBillingId());
		Assert.assertEquals(billingSystem,ldapAuth.getBillingSystem());
		Assert.assertEquals(firstName,ldapAuth.getFirstName());
		Assert.assertEquals(lastName,ldapAuth.getLastName());
		Assert.assertEquals(digitalVoiceStatus,ldapAuth.getDigitalVoiceStatus());
		Assert.assertEquals(hsdStatus,ldapAuth.getHsdStatus());
		Assert.assertEquals(vidStatus,ldapAuth.getVidStatus());
	}
	
	
	@Test
	public void testPurgeUsers() {
		
		List<String> users = new ArrayList<String>();
		users.add("mockUser1");
		users.add("mockUser2");
		users.add("mockUser3");
		try{
			Mockito
			.doNothing()
			.when(ldap)
			.purgeUser(Mockito.anyString());

			ldap.purgeUsers(users);
			Mockito.verify(ldap, VerificationModeFactory.times(1)).purgeUsers(users);
			Mockito.verify(ldap, VerificationModeFactory.times(1)).purgeUser("mockUser1"+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);
			Mockito.verify(ldap, VerificationModeFactory.times(1)).purgeUser("mockUser2"+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);
			Mockito.verify(ldap, VerificationModeFactory.times(1)).purgeUser("mockUser3"+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	
	@Test
	public void testPurgeUser() {
		
		LDAPCustomer cust = new LDAPCustomer();
		cust.setCstCustGuid("1234");
		cust.setCstManageeGuids("1234");
		
		Mockito
		       .doReturn(mockAuthorization)
		       .when(ldap)
		       .getLDAPAuthorization();
		
		Mockito
	       .doReturn(mockLDAPInstruction)
	       .when(ldap)
	       .getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_MAIL, "mockUser");
		
		Mockito
		       .doReturn(cust)
		       .when(ldap)
		       .getCustomerData(Mockito.any(LDAPInstruction.class));
		
		Mockito
	       .doReturn(mockAuthorization)
	       .when(ldap)
	       .getAuthorizationData(Mockito.any(LDAPInstruction.class));
		
		try{
			Mockito
			.doNothing()
			.when(ldap)
			.deleteUserAssociations(Mockito.anyString());

			Mockito
			.doNothing()
			.when(ldap)
			.resetAuthorization(Mockito.anyString());

			ldap.purgeUser("mockUser"+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);

			Mockito
			.verify(ldap, VerificationModeFactory.times(1))
			.purgeUser("mockUser"+ICimaCommonConstants.COMCAST_EMAIL_DOMAIN);

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getLDAPAuthorization();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).
			getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_MAIL, "mockUser@comcast.net");
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getCustomerData(Mockito.any(LDAPInstruction.class));
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getAuthorizationData(Mockito.any(LDAPInstruction.class));
			Mockito.verify(ldap, VerificationModeFactory.times(1)).deleteUserAssociations(Mockito.anyString());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).resetAuthorization(Mockito.anyString());
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteUserAssociations() {
		
		String name = "mockNames";
		String childEntry = "mockChildEntry";
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getName())
			.thenReturn(name);

			Mockito
			.when(mockCtx.listBindings(Mockito.anyString()))
			.thenReturn(mockEnumeration);

			Mockito
			.when(mockEnumeration.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockEnumeration.next())
			.thenReturn(mockBinding);

			Mockito
			.when(mockBinding.getName())
			.thenReturn(childEntry);

			Mockito
			.doReturn(mockLdapName)
			.when(ldap)
			.getLdapName(name);

			Mockito
			.doNothing()
			.when(mockCtx)
			.destroySubcontext(mockLdapName);

			ldap.deleteUserAssociations("mockCstCustGuid");
			Mockito.verify(ldap, VerificationModeFactory.times(1)).deleteUserAssociations("mockCstCustGuid");

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(mockResults, VerificationModeFactory.times(2)).hasMore();
			Mockito.verify(mockResults, VerificationModeFactory.times(1)).next();
			Mockito.verify(mockSearchResults, VerificationModeFactory.times(1)).getName();
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).listBindings(Mockito.anyString());
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(2)).hasMore();
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).next();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getLdapName(name);
			Mockito.verify(mockLdapName, VerificationModeFactory.times(1)).add(Mockito.anyString());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).destroySubcontext(mockLdapName);
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testResetAuthorization() {
		
		String NameSpaceToUpdate = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getNameInNamespace())
			.thenReturn(NameSpaceToUpdate);

			Mockito
			.doReturn(new ModificationItem[2])
			.when(ldap)
			.getModificationItem(2);

			Mockito
			.doReturn(mockAttribute)
			.when(ldap)
			.getAttribute(Mockito.anyString(), Mockito.anyString());

			Mockito
			.doReturn(mockModificationItem)
			.when(ldap)
			.getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));

			Mockito
			.doNothing()
			.when(mockCtx)
			.modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));

			ldap.resetAuthorization("mockCstAuthGuid");
			Mockito.verify(ldap, VerificationModeFactory.times(1)).resetAuthorization("mockCstAuthGuid");

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(mockResults, VerificationModeFactory.times(2)).hasMore();
			Mockito.verify(mockResults, VerificationModeFactory.times(1)).next();
			Mockito.verify(mockSearchResults, VerificationModeFactory.times(1)).getNameInNamespace();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getModificationItem(Mockito.anyInt());
			Mockito.verify(ldap, VerificationModeFactory.times(2)).getAttribute(Mockito.anyString(), Mockito.anyString());
			Mockito.verify(ldap, VerificationModeFactory.times(2)).getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testupdateAttributeOfCustomerObject() {
		String NameSpaceToUpdate = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.doReturn(mockLDAPInstruction)
			.when(ldap)
			.getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_UID, "mockUserId");

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockEnumeration);

			Mockito
			.when(mockEnumeration.hasMore())
			.thenReturn(true);

			Mockito
			.when(mockEnumeration.next())
			.thenReturn(mockSearchResults);

			Mockito
			.doReturn(new ModificationItem[1])
			.when(ldap)
			.getModificationItem(1);

			Mockito
			.doReturn(mockAttribute)
			.when(ldap)
			.getAttribute(Mockito.anyString(), Mockito.anyString());

			Mockito
			.doReturn(mockModificationItem)
			.when(ldap)
			.getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));

			Mockito
			.doNothing()
			.when(mockCtx)
			.modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));

			Mockito
			.when(mockSearchResults.getNameInNamespace())
			.thenReturn(NameSpaceToUpdate);

			ldap.updateAttributeOfCustomerObject("mockUserId", "mockAttrName", "mockAttrValue");
			Mockito.verify(ldap, VerificationModeFactory.times(1)).updateAttributeOfCustomerObject("mockUserId", "mockAttrName", "mockAttrValue");

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_UID, "mockUserId");
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).hasMore();
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).next();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getModificationItem(Mockito.anyInt());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getAttribute(Mockito.anyString(), Mockito.anyString());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	@Test
	public void testUpdate() {
		
		String NameSpaceToUpdate = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		Map<LdapAttribute, String> modifyList = new HashMap<LdapAttribute, String>();
		modifyList.put(LdapAttribute.CUSTOMER_UID, "attrValue");
		
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(false)
			.thenReturn(true);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getName())
			.thenReturn("cn=mock,ou=W7,ou=ACC,o=Comcast");

			Mockito
			.when(mockSearchResults.getNameInNamespace())
			.thenReturn(NameSpaceToUpdate);

			Mockito
			.when(mockLDAPInstruction.getModifyList())
			.thenReturn(modifyList);

			Mockito
			.doReturn(new ModificationItem[1])
			.when(ldap)
			.getModificationItem(Mockito.anyInt());

			Mockito
			.doReturn(mockAttribute)
			.when(ldap)
			.getAttribute(Mockito.anyString(), Mockito.anyString());

			Mockito
			.doReturn(mockModificationItem)
			.when(ldap)
			.getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));

			Mockito
			.doNothing()
			.when(mockCtx)
			.modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));

			List<LDAPInstruction> instructions = new ArrayList<LDAPInstruction>();
			LDAPInstruction instruction = ldap.new LDAPInstruction(SearchType.EQUAL_TO,LdapAttribute.CUSTOMER_UID,"mockUID");
			instructions.add(instruction);

			ldap.update(instructions);
			Mockito.verify(ldap, VerificationModeFactory.times(1)).update(instructions);

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockResults, VerificationModeFactory.times(1)).hasMore();
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}
	
	@Test
	public void testDeleteUserLoginObject() {
		String NameSpaceToDelete = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockResults);

			Mockito
			.when(mockResults.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockResults.next())
			.thenReturn(mockSearchResults);

			Mockito
			.when(mockSearchResults.getName())
			.thenReturn(NameSpaceToDelete);

			Mockito
			.when(mockCtx.listBindings(Mockito.anyString()))
			.thenReturn(mockEnumeration);

			Mockito
			.when(mockEnumeration.hasMore())
			.thenReturn(true)
			.thenReturn(false);

			Mockito
			.when(mockEnumeration.next())
			.thenReturn(mockBinding);

			Mockito
			.doReturn(mockLdapName)
			.when(ldap)
			.getLdapName(Mockito.anyString());

			Mockito
			.when(mockLdapName.add(Mockito.anyString()))
			.thenReturn(mockLdapName);

			Mockito
			.doNothing()
			.when(mockCtx)
			.destroySubcontext(Mockito.any(LdapName.class));


			ldap.deleteUserLoginObject("mockcstContactEmail");
			Mockito.verify(ldap, VerificationModeFactory.times(1)).deleteUserLoginObject(Mockito.anyString());

			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(mockResults, VerificationModeFactory.times(2)).hasMore();
			Mockito.verify(mockResults, VerificationModeFactory.times(1)).next();
			Mockito.verify(mockSearchResults, VerificationModeFactory.times(1)).getName();
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).listBindings(Mockito.anyString());
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(2)).hasMore();
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).next();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getLdapName(Mockito.anyString());
			Mockito.verify(mockLdapName, VerificationModeFactory.times(1)).add(Mockito.anyString());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).destroySubcontext(Mockito.any(LdapName.class));
		}catch(NamingException e){
			Assert.fail("Error occured in test "+e.getMessage());
		}
	}
	
	
	@Test
	public void testUpdateAttributeOfCustomerObject() {
		String nameSpace = "cn=sittesters,ou=W7,ou=ACC,o=Comcast";
		
		try{
			Mockito
			.doReturn(mockCtx)
			.when(ldap)
			.getInitialDirContext();

			Mockito
			.doReturn(mockSearchControls)
			.when(ldap)
			.getSearchControls();

			Mockito
			.doNothing()
			.when(mockSearchControls)
			.setSearchScope(Mockito.anyInt());

			Mockito
			.doReturn(mockLDAPInstruction)
			.when(ldap)
			.getLDAPInstruction(SearchType.EQUAL_TO, LdapAttribute.CUSTOMER_UID, "mockUserId");

			Mockito
			.when(mockCtx.search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class)))
			.thenReturn(mockEnumeration);

			Mockito
			.when(mockEnumeration.hasMore())
			.thenReturn(true);

			Mockito
			.when(mockEnumeration.next())
			.thenReturn(mockSearchResults);

			Mockito
			.doReturn(new ModificationItem[1])
			.when(ldap)
			.getModificationItem(Mockito.anyInt());

			Mockito
			.doReturn(mockAttribute)
			.when(ldap)
			.getAttribute(Mockito.anyString(), Mockito.anyString());

			Mockito
			.doReturn(mockModificationItem)
			.when(ldap)
			.getModificationItem(Mockito.anyInt(), Mockito.any(Attribute.class));

			Mockito
			.when(mockSearchResults.getNameInNamespace())
			.thenReturn(nameSpace);

			Mockito
			.doNothing()
			.when(mockCtx)
			.modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));

			ldap.updateAttributeOfCustomerObject("mockUserId", "mockAttrName", "mockAttrValue");
			Mockito.verify(ldap, VerificationModeFactory.times(1))
			.updateAttributeOfCustomerObject(Mockito.anyString(),Mockito.anyString(),Mockito.anyString());


			Mockito.verify(ldap, VerificationModeFactory.times(1)).getInitialDirContext();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getSearchControls();
			Mockito.verify(mockSearchControls, VerificationModeFactory.times(1)).setSearchScope(Mockito.anyInt());
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).search(Mockito.anyString(), Mockito.anyString(), Mockito.any(SearchControls.class));
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).hasMore();
			Mockito.verify(mockEnumeration, VerificationModeFactory.times(1)).next();
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getModificationItem(Mockito.anyInt());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getAttribute(Mockito.anyString(), Mockito.anyString());
			Mockito.verify(ldap, VerificationModeFactory.times(1)).getModificationItem(Mockito.anyInt(),Mockito.any(Attribute.class));
			Mockito.verify(mockCtx, VerificationModeFactory.times(1)).modifyAttributes(Mockito.anyString(), Mockito.any(ModificationItem[].class));
		}catch(NamingException e){
			Assert.fail("Error occurred in test "+e.getMessage());
		}
	}

}
