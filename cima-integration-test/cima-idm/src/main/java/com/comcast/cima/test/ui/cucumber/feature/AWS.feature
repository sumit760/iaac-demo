Feature: Validate user can logon to the AWS console:

	@Smoke
	Scenario: AWS Console login
	
		# Request for a user and AWS
		Given AWS URL and user credentials
		
		# User opens AWS console
		When user opens AWS console
		And user provides username and password
		Then user should be able to login to AWS Console		
		
		When user clicks logout from AWS Console
		Then user should be able to logout from AWS Console
		