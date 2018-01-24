Feature: Validate create UID flow in IDM

	@Integration
	Scenario: IDM_CreateUserID: AccountNo_ViaFBConnect_Accept_AllOptions
	
		# Request for a facebook account and fresh account
		Given a fresh facebook account
		And a fresh account with mobile number
		
		# User logs in by facebook and starts creating user id flow
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user is prompted to provide xfinity user name
		
		# User proceeds to create username
		When user proceeds with creating username
		Then user is asked to verify his identity
		
		# User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details
		When user provides account details by account number and street address 
		Then user is prompted to create his userId
		
		# User creates userId
		When user provides password while creating userId
		And user provides his phone number
		And user provides secret question and answers
		And user agrees to the terms and condition before verifying mobile number
		And user verifies his phone number
		Then userId should be created successfully for the user
		And user email should be marked as verified
		
        # User continues the flow and verifies LDAP for updated email 
        # and mobile number
		When user confirms his userId
		Then user lands on Xfnity portal page
		And user mobile number and third party email is verified in the back office 
	
	
	@Integration
    Scenario: IDM_CreateUserID: Mobile_viaFBConnect_Accept_AllOptions
    
        # Request for a facebook account and fresh account
        Given a fresh facebook account
        And a fresh account with mobile number
        
        # User logs in by facebook and starts creating user id flow
        When user opens Xfinity Login page in a browser
        And user logs in by using facebook account
        Then user is prompted to provide xfinity user name
        
        # User proceeds to create username
        When user proceeds with creating username
        Then user is asked to verify his identity
        
        # User verifies his identity
        When user verifies his identity by mobile number
        Then user is challenged with a security check
        
        # User is challenged with a security check
        When user clears the security check by providing the CAPTCHA
        Then user is asked to provide his mobile details
        
        # User provides mobile details and verifies it from splunk
		When user provides the mobile number
		And user is prompted for one time verification code
		Then user provides the one time verification code
		And user is prompted to create his userId

        
        # User creates userId
        When user provides password while creating userId
        And user provides secret question and answers
        And user agrees to the terms and conditions
        Then userId should be created successfully for the user
        And user email should be marked as verified
    
        # User continues the flow and verifies LDAP for updated email 
        # and mobile number
        When user confirms his userId
        Then user lands on Xfnity portal page
        And user mobile number and third party email is verified in the back office 
    
    
	@Integration
    Scenario: IDM_CreateUserID: SSN_ViaFBConnect_Accept_AllOptions
    
        # Request for a facebook account, a fresh SSN account
        Given a fresh facebook account
        And a fresh SSN account with mobile number
        
        # User logs in by facebook and starts creating user id flow
        When user opens Xfinity Login page in a browser
        And user logs in by using facebook account
        Then user is prompted to provide xfinity user name
        
        # User proceeds to create username
        When user proceeds with creating username
        Then user is asked to verify his identity
        
        # User verifies his identity
        When user verifies his identity by SSN
        Then user is challenged with a security check
        
        # User is challenged with a security check
        When user clears the security check by providing the CAPTCHA
        Then user is asked to provide his SSN details
        
        # User provides SSN details verifies his usernamae already exists
        When user provides fresh SSN details
        Then user is prompted to create his userId
        
        # User creates userId
        When user provides password while creating userId
		And user provides his phone number
        And user provides secret question and answers
        And user agrees to the terms and condition before verifying mobile number
		And user verifies his phone number
        Then userId should be created successfully for the user
        And user email should be marked as verified
        
        # User continues the flow and verifies LDAP for updated email 
        # and mobile number
        When user confirms his userId
        Then user lands on Xfnity portal page
        And user mobile number and third party email is verified in the back office
	
	
	@Integration
    Scenario: IDM_CreateUserID: ViaFBConnect_Multiple_Accounts_Accept_AllOptions
    
        # Request for a facebook account, two fresh accounts in IDM
        Given a fresh facebook account
        And a fresh account with mobile number
        And another fresh account with the same mobile number
        
        # User logs in by facebook and starts creating user id flow
        When user opens Xfinity Login page in a browser
        And user logs in by using facebook account
        Then user is prompted to provide xfinity user name
        
        # User proceeds to create username
        When user proceeds with creating username
        Then user is asked to verify his identity
        
        # User verifies his identity
        When user verifies his identity by mobile number
        Then user is challenged with a security check
        
        # User is challenged with a security check
        When user clears the security check by providing the CAPTCHA
        Then user is asked to provide his mobile details
        
        # User provides mobile details and verifies it from splunk
        When user provides the mobile number
        Then user is asked to provide his account details
        
        # User provides account details
        When user provides account details by account number and street address 
        Then user is prompted to create his userId
        
        # User creates userId
        When user provides password while creating userId
        And user provides his phone number
        And user provides secret question and answers
        And user agrees to the terms and condition before verifying mobile number
        And user verifies his phone number
        Then userId should be created successfully for the user
        And user email should be marked as verified
        
        # User continues the flow and verifies LDAP for updated email 
        # and mobile number
        When user confirms his userId
        Then user lands on Xfnity portal page
        And user mobile number and third party email is verified in the back office 
    
    
	@Integration
    Scenario: IDM_CreateUserID: SSN_ViaUsername_AllOptions
    
        # Request for a fresh user, a fresh SSN account in IDM
        Given a fresh user with alternate email
        And a fresh SSN account with mobile number
        
        #User starts opens xfinity portal and starts user id creation flow
        When user opens Xfinity Login page in a browser 
        And user starts user id creation flow
        Then user is asked to verify his identity        
        
        #User verifies his identity
        When user verifies his identity by SSN
        Then user is challenged with a security check
        
        # User is challenged with a security check
        When user clears the security check by providing the CAPTCHA
        Then user is asked to provide his SSN details
        
        # User provides SSN details verifies his usernamae already exists
        When user provides fresh SSN details
        Then user is prompted to create his userId
        
        # Steps to create user by accepting all options
        When user provides username
        And user provides password while creating userId
        And user provides alternative email
        And user provides his phone number
        And user provides secret question and answers
        And user agrees to the terms and condition before verifying mobile number
		And user verifies his phone number
        Then userId should be created successfully for the user
        
        # User continues the flow and verifies LDAP for updated email 
        # and mobile number
        When user confirms his userId
        Then user lands on Xfnity portal page
        When third party email is verified in the log
        Then user mobile number and third party email is verified in the back office


	@Integration
	Scenario: Create username by using mobile with existing Comcast Username  
	
		#Request for an existing user with mobile
		Given an existing user with mobile number
		
		#User starts opens xfinity portal and starts user id creation flow
		When user opens Xfinity Login page in a browser 
		And user starts user id creation flow
		Then user is asked to verify his identity
		
		#User verifies his identity
		When user verifies his identity by mobile number
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his mobile details
		
		# User provides mobile details and verifies it from splunk
		When user provides the mobile number
		And user is prompted for one time verification code
		Then user provides the one time verification code
				
		# User verifies his usernamae already exists
		Then user gets user id existense confirmation
	
	
	@Integration
	Scenario: Create username by using SSN with existing Comcast Username  
	
		#Request for an existing user with SSN
		Given an existing user with SSN
		
		#User starts opens xfinity portal and starts user id creation flow
		When user opens Xfinity Login page in a browser 
		And user starts user id creation flow
		Then user is asked to verify his identity		
		
		#User verifies his identity
		When user verifies his identity by SSN
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his SSN details
		
		# User provides SSN details verifies his usernamae already exists
		When user provides SSN details
		Then user gets user id existense confirmation
				
	
	@Integration
	Scenario: Create username business account validation 
	
		#Request for a business account
		Given a business account
		
		#User starts opens xfinity portal and starts user id creation flow
		When user opens Xfinity Login page in a browser 
		And user starts user id creation flow
		Then user is asked to verify his identity	
		
		# User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details and verify business account error message
		When user provides account details by account number and street address 
		Then user gets business account error message
	

	@Integration
	Scenario: Create username by using email token as verfied true 
	
		#Request for a fresh user with alternate email id and mobile number
		Given a fresh user with alternate email and mobile number
		
		#user generates email token
		When user opens token generation tool in a browser 
		Then user sucessfully lands on token generation tool page
		When user requests to generate an email token with true
		Then user gets email token
		
		#User starts opens user creation flow with generated email token
		When user requests to create an username by passing the email token
		Then user is asked to verify his identity
		
		# User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details
		When user provides account details by account number and street address 
		Then user is prompted to create his userId
		
		#user provides password and SQA and proceed
		When user provides password and security question answer
		Then userId should be created successfully for the user
		And user email should be marked as verified
		When user confirms his userId
		Then user lands on Xfnity portal page
		And third party email is verified in back office
		
	
	@Integration
	Scenario: Create username by using email token as verfied False 
	
		#Request for a fresh user with alternate email id and mobile number
		Given a fresh user with alternate email and mobile number
		
		#user generates email token
		When user opens token generation tool in a browser 
		Then user sucessfully lands on token generation tool page
		When user requests to generate an email token with false
		Then user gets email token
		
		#User starts opens user creation flow with generated email token
		When user requests to create an username by passing the email token
		Then user is asked to verify his identity
		
		# User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# User is challenged with a security check
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details
		When user provides account details by account number and street address 
		Then user is prompted to create his userId
		
		#user provides password and SQA and proceed and verify third party email from splunk
		When user provides password and security question answer
		Then userId should be created successfully for the user
		And third party email is verified in the log
		And third party email is verified in back office
