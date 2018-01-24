package com.comcast.test.citf.common.cima.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap;
import com.comcast.test.citf.common.cima.persistence.beans.AccountUserMap.AccountUserId;
import com.comcast.test.citf.common.cima.persistence.beans.Accounts;
import com.comcast.test.citf.common.cima.persistence.beans.Users;

public class AccountsDAOTest {
	
	private AccountsDAO mockAccountsDao;
	private Session mockSession;
	private Criteria mockCriteria;
	private Accounts mockAccounts;
	
	@Before
	public void init() {
		mockAccountsDao = Mockito.spy(new AccountsDAO());
		mockSession = Mockito.mock(Session.class, Mockito.RETURNS_DEEP_STUBS);
		mockCriteria = Mockito.mock(Criteria.class, Mockito.RETURNS_DEEP_STUBS);
		mockAccounts = Mockito.mock(Accounts.class, Mockito.RETURNS_DEEP_STUBS);
	}
	
	@Test
	public void testFindAccountById() {
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockAccountsDao)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Accounts.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
		       .when(mockCriteria.uniqueResult())
		       .thenReturn(mockAccounts);
		
		Accounts acc = mockAccountsDao.findAccountById("mockAccountId");
		
		assertThat(
				acc, notNullValue());
		
		assertThat(
				acc, is(mockAccounts));
	}
	
	
	@Test
	public void testFindAccountByUser() {
		
		String role = "mockRole";
		String accountStatus = "A";
		String accountType = "Residential";
		String loginStatus = "A";
		
		List<AccountUserMap> resultset = new ArrayList<AccountUserMap>();
		AccountUserMap accUser = new AccountUserMap();
		accUser.setRole(role);
		AccountUserId accUserId = new AccountUserId();
		Accounts acc = new Accounts();
		acc.setAccountStatus(accountStatus);
		acc.setAccountType(accountType);
		Users users = new Users();
		users.setLoginStatus(loginStatus);
		accUserId.setAccount(acc);
		accUserId.setUser(users);
		accUser.setUaMapPrimaryKey(accUserId);
		resultset.add(accUser);
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockAccountsDao)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(AccountUserMap.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
			   .when(mockCriteria.list())
			   .thenReturn(resultset);
		
		Accounts accounts = mockAccountsDao.findAccountByUser(users);
		
		assertThat(
				accounts, notNullValue());
		
		assertThat(
				accounts.getAccountStatus(), is(accountStatus));
		
		assertThat(
				accounts.getAccountType(), is(accountType));

	}

	
	@Test
	public void testFindAccountsWithDtlsByEnvironment() {
		
		String accountStatus = "A";
		String address = "mockAddress";
		String authGuid = "mockAuthGuid";
		String billingAccountId = "mockBillingAccountId";
		String billingSystem = "mockBillingSystem";
		String dob = "mockDOB";
		String environment = "mockEnv";
		String firstName = "mockFirstName";
		String lastName = "mockLastName";
		String phoneNumber = "mockPhoneNumber";
		String zip = "mockZip";
		
		
		List<Accounts> mockAccList = new ArrayList<Accounts>();		
		Accounts acc = new Accounts();
		acc.setAccountStatus(accountStatus);
		acc.setAddress(address);
		acc.setAuthGuid(authGuid);
		acc.setBillingAccountId(billingAccountId);
		acc.setBillingSystem(billingSystem);
		acc.setDob(dob);
		acc.setEnvironment(environment);
		acc.setFirstName(firstName);
		acc.setLastName(lastName);
		acc.setPhoneNumber(phoneNumber);
		acc.setZip(zip);
		mockAccList.add(acc);
		
		//Stub
		Mockito
		       .doReturn(mockSession)
		       .when(mockAccountsDao)
		       .getSession();
		
		Mockito
		       .when(mockSession.createCriteria(Mockito.eq(Accounts.class)))
		       .thenReturn(mockCriteria);
		
		Mockito
		       .when(mockCriteria.add(Mockito.any(Criterion.class)))
		       .thenReturn(mockCriteria);

		Mockito
		       .when(mockCriteria.list())
		       .thenReturn(mockAccList);
		
		mockAccList = mockAccountsDao.findAccountsWithDtlsByEnvironment(environment);
		
		assertThat(
				mockAccList, notNullValue());
		
		assertThat(
				mockAccList.size(), is(1));
		
		assertThat(
				mockAccList.get(0).getAccountStatus(), is(accountStatus));
		
		assertThat(
				mockAccList.get(0).getAddress(), is(address));
		
		assertThat(
				mockAccList.get(0).getAuthGuid(), is(authGuid));
		
		assertThat(
				mockAccList.get(0).getBillingAccountId(), is(billingAccountId));
		
		assertThat(
				mockAccList.get(0).getBillingSystem(), is(billingSystem));
		
		assertThat(
				mockAccList.get(0).getDob(), is(dob));
		
		assertThat(
				mockAccList.get(0).getEnvironment(), is(environment));
		
		assertThat(
				mockAccList.get(0).getFirstName(), is(firstName));
		
		assertThat(
				mockAccList.get(0).getLastName(), is(lastName));
		
		assertThat(
				mockAccList.get(0).getPhoneNumber(), is(phoneNumber));
		
		assertThat(
				mockAccList.get(0).getZip(), is(zip));
	}
	
	
	
	@Test
	public void testPopulateAccount() throws ParseException{
		
		String billingAccountId = "mockBillingAccountId";
		String serviceAccountId = "mockServiceAccountId";
		String environment = "mockEnv";
		String billingSystem = "mockBillingSystem";
		String authGuid = "mockauthGuid";
		String accountStatus = "mockAccountStatus";
		String firstName = "mockFirstName";
		String lastName = "mockLastName";
		String phoneNumber = "mockPhoneNumber";
		String address = "mockAddress";
		String zip = "mockZip";
		String transferFlag = "mockTransferFlag";
		String physicalResourceLink = "mockPhysicalResourceLink";
		String freshSSN = "mockFreshSSN";
		Date freshDob;
		Date ssnCreationDate;
		String lob = "VID,HSD,CDV";
		String accountType = "RESIDENTIAL";
		
		freshDob = getDate("1990-12-20 00:00:00");
		ssnCreationDate = getDate("2010-12-20 00:00:00");
		
		Accounts accounts = new Accounts();
		
		//Stub
		Mockito
		       .doReturn(accounts)
		       .when(mockAccountsDao)
		       .findEntityByRequiredField(Mockito.any(Class.class), Mockito.anyString(), Mockito.anyString());
		
		Mockito
		       .doReturn(mockSession)
		       .when(mockAccountsDao)
		       .getSession();
		
		Mockito
		       .when(mockSession.merge(Mockito.any(Accounts.class)))
		       .thenReturn(mockSession);
		
		mockAccountsDao.populateAccount(billingAccountId,
										serviceAccountId,
										environment, 
										billingSystem, 
										authGuid, 
										accountStatus, 
										firstName, 
										lastName, 
										phoneNumber, 
										address, 
										zip, 
										transferFlag, 
										physicalResourceLink, 
										freshSSN, 
										freshDob, 
										ssnCreationDate,
										lob, 
										accountType);
		
		Mockito.verify(mockAccountsDao, VerificationModeFactory.times(1))
							.findEntityByRequiredField(Mockito.any(Class.class), Mockito.anyString(), Mockito.anyString());
		
		Mockito.verify(mockAccountsDao, VerificationModeFactory.times(1)).getSession();
		
		Mockito.verify(mockSession, VerificationModeFactory.times(1)).merge(Mockito.any(Accounts.class));

	}
	
	
	private Date getDate(String strDate) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        java.util.Date cdate = sdf.parse(strDate);
       
        return new Date(cdate.getTime());
 	}
	
}
