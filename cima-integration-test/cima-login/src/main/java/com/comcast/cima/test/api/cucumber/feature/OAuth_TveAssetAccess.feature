Feature: TVE_Automated Tests - OAuth-based Asset Access (https://www.teamccp.com/jira/browse/XCIMA-2551)
	
	@Integration
	Scenario: Get valid Oauth authorization token for TVE scope
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
	@Integration
	Scenario: Get valid Oauth access token for TVE scope with the authorization token
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
	@Integration
	Scenario: Request single asset access with the XACML request and Oauth access token (expect 'Permit' results)
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-Y       |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Permit'
		
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |MPAA       |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |G          |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Permit'
	
	@Integration
	Scenario: Request single asset access with the XACML request and Oauth access token (expect 'Deny')
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
		
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |MPAA       |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |X          |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request multiple asset access, Request asset access that is blocked by parental control, Validate asset response for PERMIT/DENY and other parameters
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
			|TestChannel1   |oxygen         |ROVI       |TV-Y       |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
		And confirm that the 2nd TVE 'Result' has a 'Decision' of 'Permit'
		And confirm that the 2nd TVE 'Result' has an 'Attribute' with name-value pair ('maxMPAA','G')
		And confirm that the 2nd TVE 'Result' has an 'Attribute' with name-value pair ('maxVCHIP','TV-G')
	
	@Integration
	Scenario: Request asset access with malformed XACML request
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		Given prepare a XACML view asset request
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		And malform existing XACML request
		Then execute TVE request and read response
		And confirm that the status code of the last TVE call is 500
	
	@Integration
	Scenario: Request asset access with invalid access token
		
		Given an invalid TVE OAuth access token
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		Then confirm that the status code of the last TVE call is 401
	
	@Integration
	Scenario: Request asset access with expired access token
		
		Given an expired TVE OAuth access token
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		Then confirm that the status code of the last TVE call is 401
	
	@Integration
	Scenario: Request asset access with a renewed access token
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
		And renew OAuth access token using refresh token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request asset access with invalid ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		And change the XACML request by substituting a view action with an invalid one
		Then execute TVE request and read response
		And confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request asset access with empty ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		And change the XACML request by substituting a view action with an empty one
		Then execute TVE request and read response
		And confirm that the status code of the last TVE call is 500
	
	@Integration
	Scenario: Request asset access with null ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML (no action) asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |TV-14      |SportsCenter   |
		And confirm that the status code of the last TVE call is 200
		And confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request asset access with invalid RESOURCE attribute
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
			|Invalid        |Invalid        |Invalid    |Invalid    |Invalid        |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'NotApplicable'
	
	@Integration
	Scenario: Request asset access with empty RESOURCE attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request with empty resource by passing the access token
		And confirm that the status code of the last TVE call is 200
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'NotApplicable'
	
	@Integration
	Scenario: Request asset access with null RESOURCE attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip     |Asset Title    |
		Then confirm that the status code of the last TVE call is 200
		And confirm that the 1st TVE 'Result' has a 'Decision' of 'NotApplicable'
	
	@Integration
	Scenario: Request asset access with invalid CONTENT rating
		
		Given a TVE customer with parental control thresholds 'G' and 'TV-G'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view asset request by passing the access token
			|TV Network     |Resource ID    |Namespace  |V-Chip         |Asset Title    |
			|TestChannel1   |oxygen         |ROVI       |InvalidRating  |SportsCenter   |
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Permit'
	
