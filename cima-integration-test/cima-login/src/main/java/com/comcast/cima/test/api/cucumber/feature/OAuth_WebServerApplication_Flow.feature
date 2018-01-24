Feature: Obtain OAuth access token in Web Server Application Flow
			
	# Web Server flow is used for web applications where server-side code
	# needs to interact with propected APIs or resources.
	#
	# This is a two step process where in the first step
	# - the application requests for an authorization code to the OAuth server
	# - the OAuth server redirects the application for user authentication
	# - once user authenticates, OAuth server returns an authorization code
	#
	# In the second step
	# - the application requests for access token with the authrorization code 

	@Integration @Smoke
	Scenario Outline: Obtain OAuth access token from the web server application flow
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code with state "<state>" in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
		And state "<state>" is present in the authorization response
	
		# OAuth client requests and recieves access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		Examples:
		| scopes                              | state |
		| https://login.comcast.net/api/login | a+b=% |
		
		
	# Try to obtain OAuth access token with invalid client Id
    @Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid client id
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
			
		# OAuth client requests access token by passing invalid client id
		When OAuth client requests an access token with invalid client id by passing the authorization code
		Then OAuth client receives client not found in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 


	# Try to obtain OAuth access token with invalid client Secret
    @Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid client secret
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
			
		# OAuth client requests access token by passing invalid client secret
		When OAuth client requests an access token with invalid client secret by passing the authorization code
		Then OAuth client receives bad client credentials in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 


	# Try to obtain OAuth access token with invalid scope
    @Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid scope
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
			
		# OAuth client requests access token by passing invalid scope
		When OAuth client requests an access token with invalid scope by passing the authorization code
		Then OAuth client receives invalid scope in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 

		
	# Try to obtain OAuth access token with invalid grant type
	@Integration
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid grant type
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests access token by passing invalid grant type
		When OAuth client requests an access token with invalid grant type by passing the authorization code
		Then OAuth client receives unsupported grant type in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
	

	# Try to obtain OAuth access token with invalid redirect URL
	@Integration
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid redirect URL
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
		
		# OAuth client requests access token by passing invalid redirect URL
		When OAuth client requests an access token with invalid redirect URL by passing the authorization code
		Then OAuth client receives redirect url mismatch in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 


	# Try to obtain OAuth access token with invalid authorization code
	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth access token from the web server application flow using invalid authorization code
		
		# Request for an active user and OAuth client
		Given an active customer
		And OAuth client with registered scope "<scopes>"
	
		# OAuth client requests access token by passing invalid authorization code
		When OAuth client requests an access token with an invalid authorization code
		Then OAuth client receives invalid authorization code in the response
		And response does not contain any valid OAuth access token
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 

		
	# Try to obtain OAuth authorization code with invalid scope
	@Integration
	Scenario Outline: Try to obtain OAuth authorization code from the web server application flow using invalid scope
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser

		# OAuth client requests authorization code by passing invalid scope
		When OAuth client requests authorization code with invalid scope in web browser
		And user is redirected after sign in
		Then OAuth client receives an unrecognized scope message
		
		Examples:
		| scopes                              	 | 
		| https://login123.comcast.net/api/login | 
	

	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth authorization code from the web server application flow using invalid clientId
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser

		# OAuth client requests authorization code by passing invalid clientId
		When OAuth client requests authorization code with invalid clientId in web browser
		And user is redirected after sign in
		Then OAuth client receives an invalid client message
		
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 



	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth authorization code from the web server application flow using invalid response type
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser

		# OAuth client requests authorization code by passing invalid response type
		When OAuth client requests authorization code with invalid response type in web browser
		And user is redirected after sign in
		Then OAuth client receives unsupported response type message
		
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 


	@Integration @Smoke
	Scenario Outline: Try to obtain OAuth authorization code from the web server application flow using invalid redirect URL
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser

		# OAuth client requests authorization code by passing invalid redirect URL
		When OAuth client requests authorization code with invalid redirect URL in web browser
		And user is redirected after sign in
		Then OAuth client receives redirect URI mismatch message
		
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
		

	@Integration @Smoke
	Scenario Outline: Obtain service token from the web server application flow
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a service access Url
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and recieves access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
		
		# OAuth client requests service token by redeeming access code
		When OAuth client requests a service token for "<service>" by passing the access token
		Then OAuth client receives an service token response
		And response contains a valid service token
		
	
		Examples:
		| scopes                              			  | service   |
		| 'https://login.comcast.net/api/login#smartzone' | smartzone |



	@Integration @Smoke
	Scenario Outline: Try to obtain service token from the web server application flow by expired access token
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a service access Url
		And a web browser
	
		# OAuth client requests service token by redeeming expired access code
		When OAuth client requests a service token for "<service>" by passing an expired access token
		Then OAuth client receives an service token response with error
		And response does not contain a valid service token
		
	
		Examples:
		| scopes                              			  | service   |
		| 'https://login.comcast.net/api/login#smartzone' | smartzone |

	