Feature: UID Lookup Flow in IDM
			
	@Integration @Smoke
	Scenario: UID lookup by account number and street address
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides account details and gets username
		When user provides account details by account number and street address 
		Then user receives his Xfinity username
		And user validates the username
		
		# 5. User continues with the username
		When user continues with the username received
		Then user is redirected to sign in with username populated

		
	@Integration @Smoke
	Scenario: UID lookup by account number and phone number
		
		# 1. Request for user with phone and no alternate email 
		#    and web browser
		Given an active user with phone and no alternate email
		And billing account of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides account details and gets username
		When user provides account details by account number and phone number 
		Then user receives his Xfinity username
		And user validates the username
		
		# 5. User continues with the username
		When user continues with the username received
		Then user is redirected to sign in with username populated


	@Integration @Smoke
	Scenario: UID lookup by account number for user with alternate email
		
		# 1. Request for user with alternate email and web browser
		Given an active user with an alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides account details and gets username
		When user provides account details by account number and street address 
		Then user receives multiple Xfinity usernames
		And user validates all the usernames
		
		# 5. User continues with the username
		When user continues with the username received
		Then user is redirected to sign in with username populated


	@Integration @Smoke
	Scenario: UID lookup by SMS/text
		
		# 1. Request for user with phone number and web browser
		Given an active user with registered phone number on billing account
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects phone number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
	
		# 4. User confirms phone number and gets username
		When user provides the phone number
		And user verifies the number by applying the verification code  
		Then user receives his Xfinity username
		And user validates the username
		
		# 5. User continues with the username
		When user continues with the username received
		Then user is redirected to sign in with username populated


	@Integration @Smoke
	Scenario: UID lookup by Social Security Number
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides SSN, date of birth and phone number
		When user provides the social security number
		And user provides the date of birth
		And user provides the phone number associated with the account
		Then user receives his Xfinity username
		And user validates the username
		
		# 5. User continues with the username
		When user continues with the username received
		Then user is redirected to sign in with username populated


	@Integration @Smoke
	Scenario: UID lookup by phone number for a fresh account
		
		# 1. Request for fresh account with mobile number and web browser
		Given a fresh account with mobile number
		And a web browser
		
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects phone number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
		
		# 4. User provides phone number and asked to create username
		When user provides the phone number
		And user verifies the number by applying the verification code  
		Then user is asked to create username
		


	@Integration @Smoke
	Scenario: UID lookup by account number for a fresh account
		
		# 1. Request for fresh user account and web browser
		Given a fresh user account
		And a web browser
		
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# 4. User provides phone number and asked to create username
		When user provides account details by account number and street address
		Then user is asked to create username
		
	
	@Integration
	Scenario: UID lookup by account number from reset password for user having no alternate email
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
		
		# 2. User opens reset password
		When user opens Xfinity Login page in a browser
		And user chooses to reset his password but forgots username
		And user tries to retrieve his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides account details and gets username
		When user provides account details by account number and street address 
		Then user receives his Xfinity username
		And user validates the username
		
		# 5. User continues with the username and prompted
		#    to reset his password by security question
		When user continues with the username received
		Then user is redirected to reset his password by security questions
		


	@Integration
	Scenario: UID lookup by account number from reset password for user with alternate email
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with an alternate email
		And billing account and address of the user
		And a web browser
		
		# 2. User opens reset password
		When user opens Xfinity Login page in a browser
		And user chooses to reset his password but forgots username
		And user tries to retrieve his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides account details and gets username
		When user provides account details by account number and street address 
		Then user receives multiple Xfinity usernames
		And user validates all the usernames
		
		# 5. User continues with the username and prompted
		#    to reset his password by email
		When user continues with the username received
		Then user is redirected to reset his password by alternate email
	

	@Integration
	Scenario: UID lookup by account number for a fresh facebook account
		
		# 1. Request for fresh facebook and user account
		Given a fresh facebook account
		And a fresh account with mobile number
		And a web browser
		
		# 2. User logs in by facebook
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user is prompted to provide xfinity user name
		
		# 3. User proceeds to lookup username
		When user chooses to lookup his username
		Then user is asked to verify his identity
		
		# 4. User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# 5. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# 6. User provides account details and gets username
		When user provides account details by account number and street address 
		Then user is asked to create username
		
		
	@Integration
	Scenario: UID lookup by phone number for a fresh facebook account
		
		# 1. Request for fresh facebook and user account
		Given a fresh facebook account
		And a fresh account with mobile number
		And a web browser
		
		# 2. User logs in by facebook
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user is prompted to provide xfinity user name
		
		# 3. User proceeds to lookup username
		When user chooses to lookup his username
		Then user is asked to verify his identity
		
		# 4. User verifies his identity
		When user verifies his identity by phone number
		Then user is challenged with a security check
		
		# 5. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
		
		# 6. User confirms phone number and asked to create username
		When user provides the phone number
		And user verifies the number by applying the verification code  
		Then user is asked to create username
		

	@Integration
	Scenario: Create UID after lookup by phone number for a fresh account
		
		# 1. Request for fresh account with mobile number and web browser
		Given a fresh account with mobile number
		And a web browser
		
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects phone number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
		
		# 4. User provides phone number and asked to create username
		When user provides the phone number
		And user verifies the number by applying the verification code  
		Then user is asked to create username
		
		# 5. User creates user name
		When user continues creating username
		And user provides the username and password
		And user sets security question and answer
		And user agrees to the terms of service
		Then username should be created for the user
		
		
	# Negative Scenarios
		
	@Integration @Smoke
	Scenario: UID lookup by invalid account number and street address
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides invalid account details and gets error message
		When user provides account details by invalid account number and street address 
		Then user receives information mismatch error
		

	@Integration @Smoke
	Scenario: UID lookup by invalid account number and phone number
		
		# 1. Request for user with phone and no alternate email 
		#    and web browser
		Given an active user with phone and no alternate email
		And billing account of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides invalid account details and gets error message
		When user provides account details by invalid account number and phone number 
		Then user receives information mismatch error


	@Integration @Smoke
	Scenario: UID lookup by account number and invalid street address
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides invalid account details and gets error message
		When user provides account details by account number and invalid address 
		Then user receives information mismatch error
		

	@Integration
	Scenario: Try to lookup UID by street address only
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides invalid account details and gets error message
		When user provides account details by street address only
		Then user is prompted to provide the account number
		

	@Integration
	Scenario: Try to lookup UID by account number only
		
		# 1. Request for user with no alternate email and web browser
		Given an active user with no alternate email
		And billing account and address of the user
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects account number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
	
		# 4. User provides invalid account details and gets error message
		When user provides account details by account number only
		Then user is prompted to provide street address or phone number
		
		
	@Integration @Smoke
	Scenario: Try to lookup UID by incorrect phone number
		
		# 1. Request for user with phone number and web browser
		Given an active user with registered phone number on billing account
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects phone number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
	
		# 4. User provides incorrect phone number
		When user provides an incorrect phone number
		Then user receives information mismatch error		
		

	@Integration
	Scenario: Try to lookup UID by invalid phone number
		
		# 1. Request for user with phone number and web browser
		Given an active user with registered phone number on billing account
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects phone number to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide the mobile phone number to verify identity
	
		# 4. User provides invalid phone number
		When user provides an invalid phone number
		Then user is prompted to provide a valid phone number	
		
		
	@Integration @Smoke
	Scenario: Try to lookup UID by incorrect Social Security Number
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides incorrect SSN, valid date of birth and phone number
		When user provides incorrect social security number
		And user provides the date of birth
		And user provides the phone number associated with the account
		Then user receives information mismatch error
		
		
	@Integration
	Scenario: Try to lookup UID by invalid Social Security Number
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides invalid SSN, valid date of birth and phone number
		When user provides invalid social security number
		And user provides the date of birth
		And user provides the phone number associated with the account
		Then user is prompted to provide valid SSN
		

	@Integration @Smoke
	Scenario: Try to lookup UID by incorrect date of birth and valid SSN
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides SSN, incorrect date of birth and TN
		When user provides the social security number
		And user provides incorrect date of birth
		And user provides the phone number associated with the account
		Then user receives information mismatch error


	@Integration
	Scenario: Try to lookup UID by invalid date of birth and valid SSN
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides SSN, invalis date of birth and TN
		When user provides the social security number
		And user provides invalid date of birth
		And user provides the phone number associated with the account
		Then user is prompted to provide valid date of birth
		
		
	@Integration @Smoke
	Scenario: Try to lookup UID by incorrect TN valid SSN and date of birth
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides SSN, date of birth and incorrect TN
		When user provides the social security number
		And user provides the date of birth
		And user provides incorrect TN
		Then user receives information mismatch error
		
		
	@Integration
	Scenario: Try to lookup UID by invalid TN valid SSN and date of birth
		
		# 1. Request for user with SSN and web browser
		Given an active user with social security number
		And a web browser
	
		# 2. User tries to access username
		When user opens Xfinity Login page in a browser
		And user tries to access his username
		And user selects SSN to verify his identity
		Then user is challenged with a security check
		
		# 3. User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN
	
		# 4. User provides SSN, date of birth and invalid TN
		When user provides the social security number
		And user provides the date of birth
		And user provides invalid TN
		Then user is prompted to provide valid TN

		