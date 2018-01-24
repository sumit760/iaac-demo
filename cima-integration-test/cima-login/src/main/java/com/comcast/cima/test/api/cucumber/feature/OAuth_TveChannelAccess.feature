Feature: TVE_Automated Tests - OAuth-based Channel Access (https://www.teamccp.com/jira/browse/XCIMA-2550)

	@Integration
	Scenario: Get valid Oauth authorization token for TVE scope
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
	@Integration
	Scenario: Get valid Oauth access token for TVE scope with the authorization token
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
	@Integration
	Scenario: Request channel access with invalid access token
		
		Given an invalid TVE OAuth access token
		When oauth client prepares and sends a XACML view channel request for 'TestChannel1' by passing the access token
		Then confirm that the status code of the last TVE call is 401
	
	@Integration
	Scenario: Request channel access with expired access token
		
		Given an expired TVE OAuth access token
		When oauth client prepares and sends a XACML view channel request for 'TestChannel1' by passing the access token
		Then confirm that the status code of the last TVE call is 401
	
	@Integration
	Scenario: Request channel access with a renewed access token
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
		And renew OAuth access token using refresh token
	
		When oauth client prepares and sends a XACML view channel request for 'TestChannel1' by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request single channel access with the XACML request and Oauth access token
	
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view channel request for 'TestChannel1' by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request multiple channel access, Validate channel response for PERMIT/DENY and other parameters
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view channel request for channels 'TestChannel1' and 'TestChannel2' by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
		And confirm that the 2nd TVE 'Result' has a 'Decision' of 'Deny'

	@Integration
	Scenario: Request channel access with malformed XACML request
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When prepare a XACML view channel request for 'TestChannel1'
		And malform existing XACML request
		Then execute TVE request and read response
		And confirm that the status code of the last TVE call is 500
	
	@Integration
	Scenario: Request channel access with invalid ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When prepare a XACML view channel request for 'TestChannel1'
		And change the XACML request by substituting a view action with an invalid one
		Then execute TVE request and read response
		And confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request channel access with empty ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When prepare a XACML view channel request for 'TestChannel1'
		And change the XACML request by substituting a view action with an empty one
		Then execute TVE request and read response
		And confirm that the status code of the last TVE call is 500
	
	@Integration
	Scenario: Request channel access with null ACTION attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML (no action) channel request for 'TestChannel1' by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'Deny'
	
	@Integration
	Scenario: Request channel access with invalid RESOURCE attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view channel request for 'SomeNonexistentChannel' by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'NotApplicable'
	
	@Integration
	Scenario: Request channel access with empty RESOURCE attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view channel request for '' by passing the access token
		Then confirm that the status code of the last TVE call is 500
	
	@Integration
	Scenario: Request channel access with null RESOURCE attribute
		
		Given a TVE customer who cannot view channels 'TestChannel1' and 'TestChannel2'
		And OAuth client with registered scope "https://login.comcast.net/pdp/tve" and HTTP redirect url for TVE
		And a web browser
	
		When OAuth client requests authorization code in web browser
		And user is redirected after sign in
		Then OAuth client receives an authorization code in the authorization response
	
		When OAuth client requests an access token by passing the authorization code
		Then OAuth client receives an access token response
		And response contains a valid OAuth access token
	
		When oauth client prepares and sends a XACML view channel request for no channels by passing the access token
		Then confirm that the 1st TVE 'Result' has a 'Decision' of 'NotApplicable'
	