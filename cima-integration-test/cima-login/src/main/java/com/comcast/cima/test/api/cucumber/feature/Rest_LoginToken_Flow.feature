Feature: Try to obtain REST Login token by passing invalid parameters 

			
	@Integration @Smoke
	Scenario Outline: Try to obtain REST login token by passing invalid AppKey		

		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service tries to receive login token using invalid appkey
        When rest service requests a login token by passing invalid appkey
        Then rest service receives response with invalid application key
        And response does not contain any valid Login token

		Examples:
		| service	| 
		| smartzone |
		

	@Integration @Smoke
	Scenario Outline: Try to obtain REST login token by passing invalid userId		

		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service tries to receive login token using invalid userId
        When rest service requests a login token by passing invalid userId
        Then rest service receives response with invalid user credentials
        And response does not contain any valid Login token

		Examples:
		| service	| 
		| smartzone |


	@Integration @Smoke
	Scenario Outline: Try to obtain REST login token by passing invalid user password	

		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service tries to receive login token using invalid user password
        When rest service requests a login token by passing invalid user password
        Then rest service receives response with invalid user credentials
        And response does not contain any valid Login token

		Examples:
		| service	| 
		| smartzone |


	@Integration @Smoke
	Scenario Outline: Try to obtain REST login token by passing invalid digital signature	

		# Request for an active user and REST service
		Given an active customer
		And a REST service "<service>"
	
		# Rest service tries to receive login token using invalid digital signature
        When rest service requests a login token by passing invalid digital signature
        Then rest service receives response with invalid digital signature
        And response does not contain any valid Login token

		Examples:
		| service	| 
		| smartzone |
		
		