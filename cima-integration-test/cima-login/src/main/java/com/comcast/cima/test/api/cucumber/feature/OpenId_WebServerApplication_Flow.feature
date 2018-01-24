Feature: Obtain OpenId access token in Web Server Application Flow
			
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
	Scenario Outline: Obtain OpenId access token from the web server application flow
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
		And a web browser
	
		# OAuth client requests and receives authorization code
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		# OAuth client requests and recieves access token and id token by redeeming 
		# authorization code
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
		And response contains a valid OAuth Id token
	
		Examples:

		| scopes |
		| openid |