Feature: Obtain OAuth access token using limited input device Flow
			
	# Limited Input Device flow is used in set-top box, game system and other 
	# web enabled “limited input devices”.
	#
	# This is a three-step process where in the first step
	# - the application requests device activation code 
	# - the application performs successful device activation
	# - the application requests for access token with the device activation code
	
	@Integration @Smoke
	Scenario Outline: Obtain OAuth access token in limited input device flow
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests for device activation code
        When OAuth client requests a device activation code
        Then OAuth client receives a device activation response 
        And response contains device code user code and verification url
           
		#User activates device
		When user opens the verification url
		And provides the user code
        And user signs in at Login Page
        And permission is granted by user
        Then user receives the device activation success message
          
        # OAuth client requests and receives access token by passing the device code
        When OAuth client requests access token by passing device code
        Then OAuth client receives an access token response
	    And response contains a valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
		
		
	
	@Integration
	Scenario Outline: Try to obtain OAuth access token in limited input device flow using invalid client id
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests device activation code by 
		# passing invalid client id
        When OAuth client requests a device activation code using invalid client id
        Then OAuth client receives an error message
		
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
	

	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the limited input device flow using invalid user code
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests for device activation code
        When OAuth client requests a device activation code
        Then OAuth client receives a device activation response 
        And response contains device code user code and verification url
           
		#User tries to activate device
		When user opens the verification url
		And provides an invalid user code
        Then user receives unrecognized device code message

		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
		
		
	
	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the limited input device flow using invalid device code
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests for device activation code
        When OAuth client requests a device activation code
        Then OAuth client receives a device activation response 
        And response contains device code user code and verification url
           
		#User activates device
		When user opens the verification url
		And provides the user code
        And user signs in at Login Page
        And permission is granted by user
        Then user receives the device activation success message
          
        # OAuth client tries to obtain access token by passing invalid device code
        When OAuth client requests access token by passing invalid device code
        Then OAuth client receives invalid authorization code response
        And response does not contain any valid OAuth access token
           
        Examples:

		| scopes                              | 
		| https://login.comcast.net/api/login | 
	