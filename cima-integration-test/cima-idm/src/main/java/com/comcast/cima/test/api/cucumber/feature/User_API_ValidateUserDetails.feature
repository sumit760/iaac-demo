Feature: Validate User API

	@Integration
	Scenario Outline: Validate unavailability of registered userId using User API
	
		# Request for an active user and OAuth client
		Given an active user 
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unavailability of userId by authenticating through access token
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to validate unavailability of the userId
		Then the application receives a successful response
		And response validates unavailability of the userId 
		
		Examples: 
        |scopeKey |
        |UID_AVAILBILITY|
	
		
	@Integration
	Scenario Outline: Validate availability of unregistered userId using User API
	
		# Request for a unregistered user and OAuth client
		Given an unregistered user
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates availability of userId by authenticating through access token
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to validate availability of the userId
		Then the application receives a successful response
		And response validates availability of the userId 
		
		Examples: 
        |scopeKey |
        |UID_AVAILBILITY|
	
	
	@Integration
	Scenario Outline: Validate unauthorized access of registered userId with invalid scope
	
		# Request for a unregistered user and OAuth client
		Given an active user 
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unauthorized of userId by authenticating through access token
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to validate unavailability of the userId
		Then the application receives an unauthorized access response
		
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        

	@Integration
	Scenario Outline: Validate unauthorized access of registered userId with expired access token
	
		# Request for a unregistered user and OAuth client
		Given an active user
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint

		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates unauthorized of userId by authenticating through access token
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to validate unavailability of the userId
		Then the application receives an unauthorized access response
		
		Examples: 
        |scopeKey |
        |UID_AVAILBILITY|
        
        
	@Integration
	Scenario Outline: Validate unavailability of registered alternateEmail using User API
	
		# Request for an active user and OAuth client
		Given an active user with alternate email
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unavailability of alternateEmail by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the alternateEmail
		Then the application receives a successful response
		And response validates unavailability of the alternateEmail 
	
		Examples: 
        |scopeKey |
        |ALTERNATIVE_EMAIL_AVAILBILITY|
        
	
	@Integration
	Scenario Outline: Validate availability of unregistered alternateEmail using User API

		# Request for an unregistered user and OAuth client
		Given an unregistered user with alternate email
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates availability of alternateEmail by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate availability of the alternateEmail
		Then the application receives a successful response
		And response validates availability of the alternateEmail 
	
		Examples: 
	    |scopeKey |
        |ALTERNATIVE_EMAIL_AVAILBILITY|
	    
	    
	@Integration
	Scenario Outline: Validate unavailability of invalid alternateEmail using User API
	
		# Request for an unregistered user and OAuth client
		Given an unregistered user with invalid alternate email
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unavailability of alternateEmail by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the invalid alternateEmail
		Then the application receives a successful response
		And response validates unavailability of the invalid alternateEmail 
	
		Examples: 
        |scopeKey |
        |ALTERNATIVE_EMAIL_AVAILBILITY|
		
		
	@Integration
	Scenario Outline: Validate unauthorized access of registered alternateEmail with invalid scope
	
		# Request for an active user and OAuth client
		Given an active user with alternate email
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unauthorized of alternateEmail by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the alternateEmail
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
        
    @Integration
	Scenario Outline: Validate unauthorized access of registered alternateEmail with expired access token
	
		# Request for an active user and OAuth client
		Given an active user with alternate email
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates unauthorized of alternateEmail by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the alternateEmail
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |ALTERNATIVE_EMAIL_AVAILBILITY|
        
	
	@Integration
	Scenario Outline: Validate unavailability of registered mobileNumber using User API
	
		# Request for an active user and OAuth client
		Given an active user with mobile phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unavailability of mobileNumber by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the mobileNumber
		Then the application receives a successful response
		And response validates unavailability of the mobileNumber 
	
		Examples: 
        |scopeKey |
        |MOBILE_PHONE_NUMBER_AVAILBILITY|
	
		
	@Integration
	Scenario Outline: Validate availability of unregistered mobileNumber using User API
	
		# Request for an unregistered user and OAuth client
		Given an unregistered user with mobile phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates availability of mobileNumber by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate availability of the mobileNumber
		Then the application receives a successful response
		And response validates availability of the mobileNumber 
	
		Examples: 
	    |scopeKey |
	    |MOBILE_PHONE_NUMBER_AVAILBILITY|
	
	
	@Integration
	Scenario Outline: Validate unavailability of invalid mobileNumber using User API
	
		# Request for an unregistered user and OAuth client
		Given an unregistered user with invalid mobile phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unavailability of mobileNumber by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the invalid mobileNumber
		Then the application receives a successful response
		And response validates unavailability of the invalid mobileNumber 
	
		Examples: 
        |scopeKey |
        |MOBILE_PHONE_NUMBER_AVAILBILITY|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of registered mobileNumber with invalid scope
		# Request for an active user and OAuth client
		Given an active user with mobile phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unauthorized of mobileNumber by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the mobileNumber
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
        
    @Integration
	Scenario Outline: Validate unauthorized access of registered mobileNumber with expired access token
		# Request for an active user and OAuth client
		Given an active user with mobile phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates unauthorized of mobileNumber by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate unavailability of the mobileNumber
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |MOBILE_PHONE_NUMBER_AVAILBILITY|
        

	@Integration
	Scenario Outline: Validate availability of secret questions using User API
	
		# Request for an active user and OAuth client
		Given an active user with secret questions
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates availability of secret questions by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate availability of secret questions
		Then the application receives a successful response
		And response validates availability of secret questions 
	
		Examples: 
        |scopeKey |
        |SECRET_QUESTIONS|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of secret questions with invalid scope
	
		# Request for an active user and OAuth client
		Given an active user with secret questions
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates unauthorized of secret questions by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate availability of secret questions
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of secret questions with expired access token
	
		# Request for an active user and OAuth client
		Given an active user with secret questions
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates unauthorized of secret questions by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate availability of secret questions
		Then the application receives an unauthorized access response
	
		Examples: 
        |scopeKey |
        |SECRET_QUESTIONS|
      
        
	@Integration
	Scenario Outline: Validate one billing account associated with homePhone using User API
     
		# Request for an active homePhone and OAuth client
		Given an active homePhone
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates one billing account associated with homePhone by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with homePhone
		Then the application receives a successful response
		And response validates billing accounts associated with homePhone
     	
     	Examples: 
        |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
        
        
	@Integration
	Scenario Outline: Validate multiple billing accounts associated with homePhone using User API
	
		# Request for an active homePhone, an active user and OAuth client
		Given an active homePhone
		And an active user with the same home phone number
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates multiple billing accounts associated with homePhone by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with homePhone
		Then the application receives a successful response
		And response validates billing accounts associated with homePhone
		
		
		Examples: 
        |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of billing accounts associated with homePhone with invalid scope

		# Request for an active homePhone and OAuth client
		Given an active homePhone
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application validates one billing account associated with homePhone by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with homePhone
		Then the application receives an unauthorized access response
     	
     	Examples: 
        |scopeKey|
        |REGISTER_USER|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of billing accounts associated with homePhone with expired access token

		# Request for an active homePhone and OAuth client
		Given an active homePhone
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates one billing account associated with homePhone by authenticating through access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with homePhone
		Then the application receives an unauthorized access response
     	
     	Examples: 
        |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
       
        
	@Integration
	Scenario Outline: Validate a billing account with valid user-level OAuth access token

		# Request for an active user, OAuth client and web browser
		Given an active user
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
		And a web browser
		And the service endpoint
		
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in and get auth response url
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests and recieves access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives successful response
		And OAuth access token in response is valid
		
		# Application validates billing accounts by authenticating through valid user-level OAuth access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with a user via roles
		Then the application receives a successful response
		And response validates billing accounts associated with a user via roles
		
		Examples: 
	    |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
        
        
	@WIP
	Scenario Outline: Validate a billing account and associated service account with valid user-level Oauth access token

		# Request for an active user with xbo service account, OAuth client and web browser
		Given an active user with xbo service account
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
		And a web browser
		And the service endpoint
		
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in and get auth response url
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests and recieves access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives successful response
		And OAuth access token in response is valid
		
		# Application validates billing accounts and service accounts by authenticating through valid user-level OAuth access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with a user via roles
		Then the application receives a successful response
		And response validates billing accounts and xbo service accounts associated with a user via roles
		
		Examples: 
	    |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
        
        
	@WIP
	Scenario Outline: Validate multiple billing accounts and associated service accounts with valid user-level Oauth access token

		# Request for an active user with xbo service account, OAuth client and web browser
		Given an active user with multiple billing accounts with X1 service
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
		And a web browser
		And the service endpoint
		
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in and get auth response url
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests and recieves access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives successful response
		And OAuth access token in response is valid
		
		# Application validates billing accounts and service accounts by authenticating through valid user-level OAuth access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with a user via roles
		Then the application receives a successful response
		And response validates billing accounts and xbo service accounts associated with a user via roles
		
		Examples: 
	    |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of billing accounts with valid user-level OAuth access token with invalid scope
	
		# Request for an active user, OAuth client and web browser
		Given an active user
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
		And a web browser
		And the service endpoint
		
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in and get auth response url
		Then OAuth client not able to receive an authorization code in the authorization response
		
		Examples: 
	    |scopeKey|
        |UID_AVAILBILITY|
        
        
	@Integration
	Scenario Outline: Validate unauthorized access of billing accounts with valid user-level OAuth access token with expired access token
	
		# Request for an active user, OAuth client and web browser
		Given an active user
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
		And a web browser
		And the service endpoint
		
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in and get auth response url
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests and receives expired access token using client_credential flow
		When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application validates billing accounts by authenticating through valid user-level OAuth access token
		When OAuth access token is set in a HTTP request header
		And the application sends the request to validate billing accounts associated with a user via roles
		Then the application receives an unauthorized access response
		
		Examples: 
	    |scopeKey|
        |ACCOUNT_BASIC_PROFILE|
		

	@Integration
	Scenario Outline: Create one time reset code using User API
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKey>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
       Examples:

        | scopeKey |
        |RESET_CODE_CREATE|   
        
        
    @Integration
	Scenario Outline: Create one time reset code using User API V2 endpoint
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
        And the service v2 endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKey>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
       Examples:

        | scopeKey |
        |RESET_CODE_CREATE|   
        
    @Integration
	Scenario Outline: Create one time reset code using User API with invalid scope
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKey>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives unauthorized access response
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then the application receives an unauthorized access response
        
       Examples:

        | scopeKey |
        |RESET_CODE_DELETE|     
        
    @Integration
	Scenario Outline: Create one time reset code using User API with expired access token
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response   
 
       # Care agent requests and receives unauthorized access response
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then the application receives an unauthorized access response
        
       Examples:

        | scopeKey |
        |RESET_CODE_CREATE|   
        
        
    @Integration
	Scenario Outline: Read one time reset code using User API
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
    
    	# Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
        # Care agent reads reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to read reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
    
        Examples:

        | scopeKeys |
        |RESET_CODE_READ RESET_CODE_CREATE|
        
        
    @Integration
	Scenario Outline: Read one time reset code using User API V2 endpoint
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service v2 endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
    
    	# Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
        # Care agent reads reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to read reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
    
        Examples:

        | scopeKeys |
        |RESET_CODE_READ RESET_CODE_CREATE|
        
     @Integration
	Scenario Outline: Read one time reset code using User API for user who doesnot have reset code  
        
        # Request for an active user and OAuth client
        Given an unregistered user
        And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKey>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
    
        # Care agent reads reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to read reset code for the user who does not have Reset Code
        Then care agent receives a successful response
        And Response contains reset code not found for the user
    
        Examples:

        | scopeKey |
        | RESET_CODE_READ|
        
        
    @Integration
	Scenario Outline: Read expired reset code using User API   
        
        # Request for an active user and OAuth client
        Given an unregistered new user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
        
        # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the unregistered user
        Then care agent receives a successful response
        And response contains a valid reset code
    
        # Care agent reads reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
     	And Wait to get Reset Code expired
		And care agent sends the request to read expired reset code 
        Then care agent receives a successful response
        And Response contains message as reset code is expired for the user
    
        Examples:

        | scopeKeys |
        |RESET_CODE_READ RESET_CODE_CREATE|
        
        
    @Integration
	Scenario Outline: Read one time reset code using User API with expired access token
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response 
           
        # Care agent reads unauthorized access response
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to read reset code for the user
		Then the application receives an unauthorized access response
       
        Examples:

        | scopeKeys |
        |RESET_CODE_DELETE|
        
    @Integration
	Scenario Outline: Read one time reset code using User API with invalid scope
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
    
    	# Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
        # Care agent reads unauthorized access response
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to read reset code for the user
		Then the application receives an unauthorized access response
       
        Examples:

        | scopeKeys |
        |RESET_CODE_DELETE RESET_CODE_CREATE|
        
        
     @Integration
     Scenario Outline: Delete one time reset code using User API
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
           
        # Care agent deletes the reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to delete a reset code for the user
        Then care agent receives a successful response
        And response contains deleted reset code
        When care agent sends the request to delete a reset code for the user 
        Then care agent receives a successful response
        And Response contains reset code not found
    
        Examples:

         | scopeKeys|
         |RESET_CODE_CREATE RESET_CODE_DELETE|
         
         
     @Integration
     Scenario Outline: Delete one time reset code using User API V2 endpoint
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service v2 endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
           
        # Care agent deletes the reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to delete a reset code for the user
        Then care agent receives a successful response
        And response contains deleted reset code
        When care agent sends the request to delete a reset code for the user 
        Then care agent receives a successful response
        And Response contains reset code not found
    
        Examples:

         | scopeKeys|
         |RESET_CODE_CREATE RESET_CODE_DELETE|
         
                 
     @Integration
     Scenario Outline: Delete one time reset code using User API for invalid reset code
        
        # Request for an active user and OAuth client
        Given an unregistered new user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
        # Care agent deletes the reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to delete an invalid reset code
        Then care agent receives a successful response
        And Response contains reset code not found
       
        Examples:

         | scopeKeys|
         |RESET_CODE_CREATE RESET_CODE_DELETE|
         
         
    @Integration
	Scenario Outline: Delete expired reset code using User API   
        
        # Request for an active user and OAuth client
        Given an unregistered new user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid
        
        # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the unregistered user
        Then care agent receives a successful response
        And response contains a valid reset code
    
        # Care agent reads reset code by authenticating through access token
     	When OAuth access token is set in a HTTP request header
     	And Wait to get Reset Code expired
		And care agent sends the request to Delete expired reset code 
        Then care agent receives a successful response
        And Response contains message as reset code is expired for the user
    
        Examples:

        | scopeKeys |
        |RESET_CODE_CREATE RESET_CODE_DELETE|
        
             
     @Integration
     Scenario Outline: Delete one time reset code using User API with invalid scope
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
           
        # Care agent gets unauthorized access response
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to delete a reset code for the user
        Then the application receives an unauthorized access response    
       
        Examples:

        | scopeKeys|
        |RESET_CODE_CREATE RESET_CODE_READ|
        
        
      @Integration
     Scenario Outline: Delete one time reset code using User API with expired access token
        
        # Request for an active user and OAuth client
        Given an active user
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Care agent requests and receives reset code by authenticating through access token
        When OAuth access token is set in a HTTP request header
		And care agent sends the request to create a reset code for the user
        Then care agent receives a successful response
        And response contains a valid reset code
        
        # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response 
           
        # Care agent gets unauthorized access response
     	When OAuth access token is set in a HTTP request header
		And care agent sends the request to delete a reset code for the user
        Then the application receives an unauthorized access response    
        
        Examples:

         | scopeKeys|
         |RESET_CODE_CREATE RESET_CODE_READ|
         
        
         

 	@Integration
	Scenario Outline: Send verification email and validate verification email
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user
        Then the application receives a successful response
        And response confirms message submission
        And email verification link is sent to the log

        # Application executes validateverificationemail by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And email verification token is extracted from the log
        And application sends the request appending the verification token to validate verification email
        Then the application receives a successful response
        And response contains valid account details of user  
        
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL VALIDATE_VERIFICATION_URL|  
        
        
    @Integration
	Scenario Outline: Validation of sendverification email for user without billing accountid
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without billing account ID
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user without GUID parameter
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without GUID parameter
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL|
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user without First Name
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without First Name
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration 
	Scenario Outline: Validation of sendverification email for user without Last Name
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without Last Name
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user without Username
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without Username
        Then the application receives a successful response
        And response confirms invalid input receieved
        
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user without email
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without email
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user without flow URL
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user without Flow URL
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
        
        
	@Integration
	Scenario Outline: Validation of sendverification email for user with invalid email
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application executes sendverificationemail API by authenticating through access token
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user with invalid email
        Then the application receives a successful response
        And response confirms invalid input receieved
      
       Examples:

        |scopeKeys |
        |SEND_VERIFICATION_URL SEND_VERIFICATION_URL| 
   
	@Integration
	Scenario Outline: Send verification email and validate verification email with invalid scope
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests access token for scope "<scopeKeys>" using client_credential flow
        Then OAuth client receives successful response
        And OAuth access token in response is valid       
 
       # Application gets unauthorized access response
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user
        Then the application receives an unauthorized access response    
        
       Examples:

        |scopeKeys |
        |RESET_CODE_DELETE|  
        
        
    @Integration
	Scenario Outline: Send verification email and validate verification email with expired access token
        
        # Request for an active user having alternate email and OAuth client details to send verification email
        Given an active user with alternate email
        And OAuth client with registered scope keys "<scopeKeys>" and HTTP redirect url
        And the service endpoint
    
       # OAuth client requests and receives access token using client_credential flow
        When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response      
 
       # Application gets unauthorized access response
        When OAuth access token is set in a HTTP request header
        And application sends the request to send verification email for the user
        Then the application receives an unauthorized access response    
        
       Examples:

        |scopeKeys |
        |RESET_CODE_DELETE|  
        
        
	@Integration
	Scenario Outline: Register user thourgh User API
	
		# Request for an fresh user and OAuth client
		Given a fresh user with alternative email and mobile 
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application register userid and also deltes it from LDAP
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to register a fresh user
		Then the application receives a successful response
		And response validates successful registration of fresh user 
		When application deletes registerd user from LDAP
		Then register user gets deleted successfully
		
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
        
	@Integration
	Scenario Outline: Register user thourgh User API with invalid account details
	
		# Request for an existing user and OAuth client
		Given an active user with alternate email and phone
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application try register user with invalid account details
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to register user with invalid account details
		Then the application receives a successful response
		And response validates unsuccessful registration of user with existing account 

		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
        
        
	@Integration
	Scenario Outline: Register user thourgh User API with invalid Scope
	
		# Request for an existing user and OAuth client
		Given a fresh user with alternative email and mobile 
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
		When OAuth client requests access token for scope "<scopeKey>" using client_credential flow 
		Then OAuth client receives successful response 
		And OAuth access token in response is valid 
		
		# Application try register user with invalid scope details and gets unauthorized access response
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to register a fresh user
		Then the application receives an unauthorized access response
		

		Examples: 
        |scopeKey |
        |RESET_CODE_DELETE|  

   
           
	@Integration
	Scenario Outline: Register user thourgh User API with expired access token
	
		# Request for an existing user and OAuth client
		Given a fresh user with alternative email and mobile
		And OAuth client with registered scope keys "<scopeKey>" and HTTP redirect url 
		And the service endpoint
		
		# OAuth client requests and receives access token using client_credential flow
        When OAuth client requests expired access token for scope "<scopeKey>" using client_credential flow
		Then OAuth client receives successful response
		
		# Application try register user and gets unauthorized access response
		When OAuth access token is set in a HTTP request header 
		And the application sends the request to register a fresh user
		Then the application receives an unauthorized access response
		
		Examples: 
        |scopeKey |
        |REGISTER_USER|
        
