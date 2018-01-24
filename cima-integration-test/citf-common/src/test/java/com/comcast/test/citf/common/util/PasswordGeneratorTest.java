package com.comcast.test.citf.common.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.comcast.test.citf.common.helpers.PasswordGenerator;

public class PasswordGeneratorTest {

	public PasswordGenerator objPasswordGenerator;

	@Before
	public void setup() {
		objPasswordGenerator = new PasswordGenerator();
	}

	@Test
	public void testGeneratePassword()
	{
		String generatedPassword;
		
		/**
		 * Minimum length = 6
		 * Maximum length = 20
		 * noOfCAPSAlpha = 1
		 * noOfDigit = 1
		 * noOfSplChars = 1
		 */
		generatedPassword = PasswordGenerator.generatePassword(6, 20, 1, 1, 1);
		verifyPassword(generatedPassword);
		
		
		/**
		 * Minimum length = 7
		 * Maximum length = 10
		 * noOfCAPSAlpha = 3
		 * noOfDigit = 2
		 * noOfSplChars = 2
		 */
		generatedPassword = PasswordGenerator.generatePassword(7, 10, 3, 2, 2);
		verifyPassword(generatedPassword);
		
		
		/**
		 * Minimum length = 8
		 * Maximum length = 15
		 * noOfCAPSAlpha = 3
		 * noOfDigit = 2
		 * noOfSplChars = 3
		 */
		generatedPassword = PasswordGenerator.generatePassword(8, 15, 3, 2, 3);
		verifyPassword(generatedPassword);
		
	}
	
	private void verifyPassword(String password) {
		assertTrue(password.matches(".*[\\d]*.*"));
		assertTrue(password.length() >= 6);
		assertTrue(password.length() <= 20);
		assertTrue(password.matches(".*[A-Za-z]+.*"));
		assertTrue(password.matches(".*[!+-/@#$%^&*()_=]+.*"));
	}


}
