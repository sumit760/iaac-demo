Feature: Validate Facebook Connect out of home flows:

	@Integration
	Scenario: Using XfinityURL FacebookConnect & FacebookDisconnect
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# User opens facebook xfinity portal and starts facebook connect flow
		When user opens FB XfinityURL page in a browser
		And user logs in at Facebook Xfinity URL page
		Then user is disconnected from Facebook
		
		# Steps to connect facebook using XfinityURL
		When user connects facebook account
		Then user is connected from Facebook
		
		# Steps to disconnect facebook using XfinityURL
		When user disconnects facebook account
		Then user is disconnected from Facebook
	
	
	@Integration
	Scenario: Using XfinityURL FacebookBlock & FacebookUnBlock
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# User opens facebook xfinity portal and starts facebook block flow
		When user opens FB XfinityURL page in a browser
		And user logs in at Facebook Xfinity URL page
		Then user is unblocked from Facebook
		
		# Steps to block facebook using XfinityURL
		When user blocks facebook account
		Then user is blocked from Facebook and close browser
		
		# Steps to verify the facebook has been blocked
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user should be blocked by facebook connect
		
		# User starts open facebook xfinity portal and starts facebook unblock flow
		When user opens FB XfinityURL page in a browser
		And user logs in at Facebook Xfinity URL page
		Then user is blocked from Facebook
		
		# Steps to unblock facebook using XfinityURL
		When user unblocks facebook account
		Then user is unblocked from Facebook and close browser
		
		# Steps to verify the facebook has been unblocked
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user lands on Xfnity portal page
	
	
	@Integration
	Scenario: Username lookup using FB connect by account number - Business Account
	
		# Request for a facebook account, an business account
		Given a fresh facebook account
		And an business account
		
		# User logs in by facebook and starts user name lookup flow
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user is prompted to provide xfinity user name
		
        # User proceeds to user name lookup		
		When user proceeds with username lookup
		Then user is asked to verify his identity
		
        # User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
        # User is challenged with a security check		
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details
		When user provides account details by account number and phone number
		Then user is able to lookup his username
	
	
	@Integration
	Scenario: Verify CIMA App Access UsingFacebook_Connect
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# Steps to access Xfinity portal by facebook connect
		When user opens Xfinity Login page in a browser
		And user logs in by using facebook account
		Then user lands on Xfnity portal page
		
		# User opens facebook xfinity portal and starts facebook disconnect flow
		When user opens FB XfinityURL page in a browser
		And user logs in at Facebook Xfinity URL page
		Then user is connected from Facebook
		
		# Steps to disconnect facebook using XfinityURL
		When user disconnects facebook account
		Then user is disconnected from Facebook
	
	
	@Integration
	Scenario: Using XfinityURL FacebookConnect & FacebookDisconnect in Spanish
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# User opens facebook xfinity portal and starts facebook connect flow
		When user opens FB XfinityURL page in a Spanish browser 
		And user logs in at Facebook Xfinity URL page
		Then user is disconnected from Facebook in Spanish
		
		# Steps to connect facebook using XfinityURL		
		When user connects facebook account in Spanish
		Then user is connected from Facebook in Spanish
		
		# Steps to disconnect facebook using XfinityURL
		When user disconnects facebook account in Spanish
		Then user is disconnected from Facebook in Spanish
	
	
	@Integration
	Scenario: Using XfinityURL FacebookBlock & FacebookUnBlock in Spanish
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# User opens facebook xfinity portal and starts facebook block flow
		When user opens FB XfinityURL page in a Spanish browser 
		And user logs in at Facebook Xfinity URL page
		Then user is unblocked from Facebook in Spanish
		
		# Steps to block facebook using XfinityURL		
		When user blocks facebook account in Spanish
		Then user is blocked from Facebook in Spanish and close browser
		
		# Steps to verify the facebook has been blocked
		When user opens Xfinity Login page in a Spanish browser
		And user logs in by using facebook account
		Then user should be blocked by facebook connect
		
		# User starts open facebook xfinity portal and starts facebook unblock flow
		And user opens FB XfinityURL page in a Spanish browser 
		And user logs in at Facebook Xfinity URL page
		Then user is blocked from Facebook in Spanish
		
		# Steps to unblock facebook using XfinityURL		
		When user unblocks facebook account in Spanish
		Then user is unblocked from Facebook in Spanish and close browser
		
		# Steps to verify the facebook has been unblocked
		And user opens Xfinity Login page in a Spanish browser
		And user logs in by using facebook account
		Then user lands on Xfnity portal page
	
	
	@Integration
	Scenario: Username lookup using FB connect by account number - Business Account in Spanish
	
		# Request for a facebook account, an business account
		Given a fresh facebook account
		And an business account
		
		# User logs in by facebook and starts user name lookup flow
		When user opens Xfinity Login page in a Spanish browser
		And user logs in by using facebook account
		Then user is prompted to provide xfinity user name
		
        # User proceeds to user name lookup				
		When user proceeds with username lookup
		Then user is asked to verify his identity
		
        # User verifies his identity
		When user verifies his identity by account number
		Then user is challenged with a security check
		
		# User is challenged with a security check	
		When user clears the security check by providing the CAPTCHA
		Then user is asked to provide his account details
		
		# User provides account details	
		When user provides account details by account number and phone number
		Then user is able to lookup his username
	
	
	@Integration
	Scenario: Verify CIMA App Access UsingFacebook_Connect in Spanish
	
		# Request for a user with facebook account
		Given an existing user with facebook Id same as alternate email Id
		
		# Steps to access Xfinity portal by facebook connect
		When user opens Xfinity Login page in a Spanish browser
		And user logs in by using facebook account
		Then user lands on Xfnity portal page
		
		# User opens facebook xfinity portal and starts facebook disconnect flow
		When user opens FB XfinityURL page in a Spanish browser 
		And user logs in at Facebook Xfinity URL page
		Then user is connected from Facebook in Spanish
		
		# Steps to disconnect facebook using XfinityURL
		When user disconnects facebook account in Spanish
		Then user is disconnected from Facebook in Spanish
	
	
