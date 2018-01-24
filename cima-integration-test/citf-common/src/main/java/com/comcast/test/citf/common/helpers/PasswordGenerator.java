package com.comcast.test.citf.common.helpers;

import java.util.Random;

/**
 * Utility class for generating random password.
 * 
 * @author Sumit Pal
 *
 */
public class PasswordGenerator {
	
	private static final String ALPHA_CAPS 	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA 	= "abcdefghijklmnopqrstuvwxyz";
	private static final String NUM 	= "0123456789";
	private static final String SPL_CHARS	= "!@#$%^&*_=+-/";
	

	/**
	 * Generates a random password.
	 * 
	 * @param minLen
	 * 					Minimum length of the password.
	 * @param maxLen
	 * 					Maximum length of the password.
	 * @param noOfCAPSAlpha
	 * 					Number of alphabets in capital.
	 * @param noOfDigits
	 * 					Number of digits in the password.
	 * @param noOfSplChars
	 * 					Number of special characters in the password.
	 * @return
	 * 					The random password.
	 */
	public synchronized static String generatePassword(int minLen, int maxLen, int noOfCAPSAlpha, 
			int noOfDigits, int noOfSplChars) {
		
		if(minLen > maxLen){
			throw new IllegalArgumentException("Min. Length > Max. Length!");
		}
		if( (noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen ){
			throw new IllegalArgumentException("Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
		}
		
		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
		}
		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
		}
		for(int i = 0; i < len; i++) {
			if(pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
			}
		}
		return String.valueOf(pswd);

	}
	
	
	private static int getNextIndex(Random rnd, int len, char[] pswd) {
		return rnd.nextInt(len);
	}

}
