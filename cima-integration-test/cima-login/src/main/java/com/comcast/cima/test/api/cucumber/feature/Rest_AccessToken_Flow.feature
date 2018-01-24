Feature: Obtain access token using REST API 
		 
	# This is a two-step process where in the first step
	# 	- the application requests and receives login token		 
	#
	# in the second step
	# 	- the application requests for access token using login token
	
	@Integration @Smoke
	Scenario Outline: Obtain access token from REST API
		
		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service requests and receives login token
        When rest service requests a login token
        Then rest service receives a login token response 
        And response contains a valid login token
        
        # Rest service requests and receives access token 
        # by passing the login token
        When rest service requests access token by passing login token
        Then rest service receives an access token response
	    And response contains a valid service token for smartzone service

		Examples:
		| service   | 
		| smartzone | 


	@Integration @Smoke
	Scenario Outline: Try to obtain access token from REST API by invalid appKey
		
		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service requests and receives login token
        When rest service requests a login token
        Then rest service receives a login token response 
        And response contains a valid login token
        
        # Rest service requests access token by passing the login token
        # with invalid appKey
        When rest service requests access token with invalid appkey passing the login token
        Then rest service receives a response with invalid application key message
	    And response does not contain any valid access token

		Examples:
		| service   | 
		| smartzone | 


	@Integration
	Scenario Outline: Try to obtain access token from REST API by invalid service
		
		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service requests and receives login token
        When rest service requests a login token
        Then rest service receives a login token response 
        And response contains a valid login token
        
        # Rest service requests access token by passing the login token
        # with invalid service
        When rest service requests access token with invalid service passing the login token
        Then rest service receives a response with invalid service message
	    And response does not contain any valid access token

		Examples:
		| service   | 
		| invalidservice | 


	@Integration @Smoke
	Scenario Outline: Try to obtain access token from REST API by invalid login token
		
		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
        # Rest service requests access token by passing invalid login token
        When rest service requests access token by passing invalid login token
        Then rest service receives a response with invalid login token message
	    And response does not contain any valid access token

		Examples:
		| service   | 
		| smartzone | 
 
