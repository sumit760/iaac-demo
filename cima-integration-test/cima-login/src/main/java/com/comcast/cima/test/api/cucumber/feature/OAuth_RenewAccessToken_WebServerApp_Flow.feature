Feature: Renew OAuth access token using Web Server Application Flow
			
	# Web Server flow is used for web applications where server-side code
	# needs to interact with prospected APIs or resources.
	#
	# This is a three step process where in the first step
	# - the application requests for an authorization code to the OAuth server
	# - the OAuth server redirects the application for user authentication
	# - once user authenticates, OAuth server returns an authorization code
	#
	# In the second step
	# - the application requests and receives access token and refresh token 
	#   by passing authorization code 
    #
    # In the third step
    # -the application requests for the renewal of access token using the refresh token

	@Integration @Smoke
	Scenario Outline: Renew OAuth access token in web server application flow
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and receives access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token with refresh token

        # OAuth client requests for the new access token using refresh token
		When OAuth client requests for new access token by passing the refresh token
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		Examples:
		| scopes                              |
		| https://login.comcast.net/api/login |
		
	
	

	@Integration @Smoke
	Scenario Outline: Try to renew OAuth access token from the web server application flow using invalid refresh token
		
		# Request for OAuth client 
		Given OAuth client with registered scope "<scopes>"
		
		# OAuth client requests for access token using invalid refresh token
		When OAuth client requests for access token using invalid refresh token
		Then OAuth client receives invalid refresh token response
		And response does not contain any valid OAuth access token 
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
	

	
	@Integration
	Scenario Outline: Try to renew OAuth access token from the web server application flow using invalid grant type
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and receives access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token with refresh token
		
		# OAuth client requests for access token using invalid grant type
		When OAuth client requests for access token by passing refresh token with invalid grant type
		Then OAuth client receives invalid grant type response
		And response does not contain any valid OAuth access token 
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
		
		
	@Integration @Smoke
	Scenario Outline: Try to renew OAuth access token from the web server application flow using invalid clientId
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and receives access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token with refresh token
		
		# OAuth client requests for access token using invalid clientId
		When OAuth client requests for access token by passing refresh token with invalid clientId
		Then OAuth client receives unauthorized client response
		And response does not contain any valid OAuth access token 
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 



	@Integration @Smoke
	Scenario Outline: Try to renew OAuth access token from the web server application flow using invalid client secret
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and receives access token by redeeming authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token with refresh token
		
		# OAuth client requests for access token using invalid client secret
		When OAuth client requests for access token by passing refresh token with invalid client secret
		Then OAuth client receives bad client credentials response
		And response does not contain any valid OAuth access token 
	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
	