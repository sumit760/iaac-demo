package com.comcast.test.citf.common.cima.persistence.beans;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class UserAttributesTest {

	private UserAttributes objUserAttributes;
	private static final String sValid="VALID";
	private static final String sNull = null;
	private static final String sBlank="";
	private Users objUsers;

	Calendar calendar = Calendar.getInstance();

	private static final java.sql.Date date = new java.sql.Date(System.currentTimeMillis());

	@Before
	public void setup() {
		objUserAttributes=new UserAttributes();
	}


	@Test
	public void testGuID() {

		objUserAttributes.setGuid(sValid);
		assertEquals(sValid,objUserAttributes.getGuid());

		objUserAttributes.setGuid(sNull);
		assertEquals(sNull,objUserAttributes.getGuid());

		objUserAttributes.setGuid(sBlank);
		assertEquals(sBlank,objUserAttributes.getGuid());
	}

	@Test
	public void testUser() {

		objUsers=new Users("abcd","testPassword","valid","123", "a", "a","valid");
		
		objUserAttributes.setUser(objUsers);
		assertEquals(objUsers,objUserAttributes.getUser());
	}


	@Test
	public void testEmail() {
		objUserAttributes.setEmail(sValid);
		assertEquals(sValid,objUserAttributes.getEmail());

		objUserAttributes.setEmail(sNull);
		assertEquals(sNull,objUserAttributes.getEmail());

		objUserAttributes.setEmail(sBlank);
		assertEquals(sBlank,objUserAttributes.getEmail());
	}



	@Test
	public void testSecretQuestion() {
		objUserAttributes.setSecretQuestion(sValid);
		assertEquals(sValid,objUserAttributes.getSecretQuestion());

		objUserAttributes.setSecretQuestion(sNull);
		assertEquals(sNull,objUserAttributes.getSecretQuestion());

		objUserAttributes.setSecretQuestion(sBlank);
		assertEquals(sBlank,objUserAttributes.getSecretQuestion());

	}


	@Test
	public void testSecretAnswer() {
		objUserAttributes.setSecretAnswer(sValid);
		assertEquals(sValid,objUserAttributes.getSecretAnswer());

		objUserAttributes.setSecretAnswer(sNull);
		assertEquals(sNull,objUserAttributes.getSecretAnswer());

		objUserAttributes.setSecretAnswer(sBlank);
		assertEquals(sBlank,objUserAttributes.getSecretAnswer());
	}



	@Test
	public void testAlterEmail() {

		objUserAttributes.setAlterEmail(sValid);
		assertEquals(sValid,objUserAttributes.getAlterEmail());

		objUserAttributes.setAlterEmail(sNull);
		assertEquals(sNull,objUserAttributes.getAlterEmail());

		objUserAttributes.setAlterEmail(sBlank);
		assertEquals(sBlank,objUserAttributes.getAlterEmail());
	}

	@Test
	public void testAlterEmailPassword() {
		objUserAttributes.setAlterEmailPassword(sValid);
		assertEquals(sValid,objUserAttributes.getAlterEmailPassword());

		objUserAttributes.setAlterEmailPassword(sNull);
		assertEquals(sNull,objUserAttributes.getAlterEmailPassword());

		objUserAttributes.setAlterEmailPassword(sBlank);
		assertEquals(sBlank,objUserAttributes.getAlterEmailPassword());
	}

	@Test
	public void testFbId() {

		objUserAttributes.setFbId(sValid);
		assertEquals(sValid,objUserAttributes.getFbId());

		objUserAttributes.setFbId(sNull);
		assertEquals(sNull,objUserAttributes.getFbId());

		objUserAttributes.setFbId(sBlank);
		assertEquals(sBlank,objUserAttributes.getFbId());
	}


	@Test
	public void testDob() {

		objUserAttributes.setDob(date);
		assertEquals(date,objUserAttributes.getDob());
	}


	@Test
	public void testSsn() {

		objUserAttributes.setSsn(sValid);
		assertEquals(sValid,objUserAttributes.getSsn());

		objUserAttributes.setSsn(sNull);
		assertEquals(sNull,objUserAttributes.getSsn());

		objUserAttributes.setSsn(sBlank);
		assertEquals(sBlank,objUserAttributes.getSsn());
	}


	@Test
	public void testFbPassword() {

		objUserAttributes.setFbPassword(sValid);
		assertEquals(sValid,objUserAttributes.getFbPassword());

		objUserAttributes.setFbPassword(sNull);
		assertEquals(sNull,objUserAttributes.getFbPassword());

		objUserAttributes.setFbPassword(sBlank);
		assertEquals(sBlank,objUserAttributes.getFbPassword());

	}


}
