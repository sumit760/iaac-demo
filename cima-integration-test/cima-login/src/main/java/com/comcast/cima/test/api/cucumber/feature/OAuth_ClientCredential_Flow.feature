Feature: Obtain OAuth access token using client credential flow
			
	# Client Credential flow is used for web clients
	#
	# This is an one-step process 
	# - the application requests and receives access token 
	#   with grant type set to 'client credentials'
	
	@Integration @Smoke
	Scenario Outline: Obtain OAuth access token in client credential flow	
		
		# Request for an active user, OAuth client and web browser
		Given an active customer
		And OAuth client with registered scope "<scopes>"
	
		# OAuth client requests and receives access token
        When OAuth client requests access token in client credential flow
        Then OAuth client receives an access token response
	    And response contains a valid OAuth access token
 	
		Examples:
		| scopes                              | 
		| https://login.comcast.net/api/login | 
