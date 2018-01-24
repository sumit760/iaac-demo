package com.comcast.test.citf.common.cima.persistence.beans;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import com.comcast.test.citf.common.cima.persistence.beans.FreshUsers;


public class FreshUsersTest {

	FreshUsers objFreshUsers;
	private  String sValid="VALID";
	private String sNull=null;
	private String sBlank="";
	private int ivalid=12;

	Calendar calendar = Calendar.getInstance();
	java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());

	@Before
	public void setup() {
		objFreshUsers=new FreshUsers();
	}


	@Test
	public void testPrimaryKey() {
		objFreshUsers.setPrimaryKey(ivalid);
		assertEquals(ivalid,objFreshUsers.getPrimaryKey());
	}

	@Test
	public void testAlternativeEmail() {

		objFreshUsers.setAlternativeEmail(sValid);
		assertEquals(sValid,objFreshUsers.getAlternativeEmail());

		objFreshUsers.setAlternativeEmail(sNull);
		assertEquals(sNull,objFreshUsers.getAlternativeEmail());

		objFreshUsers.setAlternativeEmail(sBlank);
		assertEquals(sBlank,objFreshUsers.getAlternativeEmail());
	}
	
	@Test
	public void testAlterEmailPassword() {

		objFreshUsers.setAlterEmailPassword(sValid);
		assertEquals(sValid,objFreshUsers.getAlterEmailPassword());

		objFreshUsers.setAlterEmailPassword(sNull);
		assertEquals(sNull,objFreshUsers.getAlterEmailPassword());

		objFreshUsers.setAlterEmailPassword(sBlank);
		assertEquals(sBlank,objFreshUsers.getAlterEmailPassword());
	}

	@Test
	public void testFbID() {

		objFreshUsers.setFbId(sValid);
		assertEquals(sValid,objFreshUsers.getFbId());

		objFreshUsers.setFbId(sNull);
		assertEquals(sNull,objFreshUsers.getFbId());

		objFreshUsers.setFbId(sBlank);
		assertEquals(sBlank,objFreshUsers.getFbId());
	}

	@Test
	public void testFbPassword() {

		objFreshUsers.setFbPassword(sValid);
		assertEquals(sValid,objFreshUsers.getFbPassword());

		objFreshUsers.setFbPassword(sNull);
		assertEquals(sNull,objFreshUsers.getFbPassword());

		objFreshUsers.setFbPassword(sBlank);
		assertEquals(sBlank,objFreshUsers.getFbPassword());
	}


	@Test
	public void testLastUserId() {

		objFreshUsers.setLastUserId(sValid);
		assertEquals(sValid,objFreshUsers.getLastUserId());

		objFreshUsers.setLastUserId(sNull);
		assertEquals(sNull,objFreshUsers.getLastUserId());

		objFreshUsers.setLastUserId(sBlank);
		assertEquals(sBlank,objFreshUsers.getLastUserId());
	}


	@Test
	public void testLastModifiedOn() {

		objFreshUsers.setLastModifiedOn(currentTimestamp);
		assertEquals(currentTimestamp,objFreshUsers.getLastModifiedOn());
	}

	@Test
	public void testLockStatus() {

		objFreshUsers.setLockStatus(sValid);
		assertEquals(sValid,objFreshUsers.getLockStatus());

		objFreshUsers.setLockStatus(sNull);
		assertEquals(sNull,objFreshUsers.getLockStatus());

		objFreshUsers.setLockStatus(sBlank);
		assertEquals(sBlank,objFreshUsers.getLockStatus());
	}
}
