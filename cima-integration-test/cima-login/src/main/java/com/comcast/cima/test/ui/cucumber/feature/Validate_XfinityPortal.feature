Feature: Validate Single Sign On and Single logout of Xfinity Portal links
			
	@Integration @Smoke
	Scenario: Validate all the links are present in Xfinity Portal
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer verifies Xfinity portal links
		When customer opens Xfinity portal in browser
		Then xfinity top navigational menu is loaded successfully
		And xfinity help & support menu is loaded successfully
		And xfinity I Want To section is loaded successfully 
		And xfinity footer menu is loaded successfully

	
	@Integration @Smoke
	Scenario: Validate Sign in/Sign out in Xfinity Portal
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in from xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		
	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Shop/Upgrade
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Shop/Upgrade and gets signed
		# in by SSO
		When customer opens shop/upgrade from xfinity portal
		Then customer gets signed in to Xfinity shop/upgrade by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Shop/Upgrade
		# by SLO
		When customer reloads Xfinity Shop/Upgrade page
		Then customer gets signed out from Xfinity shop/upgrade by SLO
		
	
	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Support
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Support and gets signed
		# in by SSO
		When customer opens support from xfinity portal
		Then customer gets signed in to Xfinity support by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Shop/Upgrade
		# by SLO
		When customer reloads Xfinity support page
		Then customer gets signed out from Xfinity support by SLO
		
		
	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity My Account
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens My Account and gets signed
		# in by SSO
		When customer opens MyAccount from xfinity portal
		Then customer gets signed in to MyAccount by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from MyAccount
		# by SLO and redirected to Xfinity Sign In
		When customer reloads MyAccount page
		Then customer is redirected to Xfinity Sign In

			
	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity TVGo
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity TVGo and gets signed
		# in by SSO
		When customer opens TVGo from xfinity portal
		Then customer gets signed in to Xfinity TVGo by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity TVGo
		# by SLO
		When customer reloads Xfinity TVGo
		Then customer gets signed out from Xfinity TVGo by SLO
		
		
	@Integration @Smoke
	Scenario Outline: Validate SSO SLO of Xfinity Email
		
		# Request for an active HSD customer and web browser
		Given an active "<lob>" customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Email and gets signed
		# in by SSO
		When customer opens Email from xfinity portal
		Then customer gets signed in to Xfinity Email by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Email
		# by SLO
		When customer reloads Email page
		Then customer gets signed out from Email page by SLO
		
		Examples:

		|lob   	|
		|HSD    |


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Guide
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Guide and gets signed
		# in by SSO
		When customer opens Guide from xfinity portal
		Then customer gets signed in to Guide by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Guide
		# by SLO
		When customer reloads Guide page
		Then customer gets signed out from Guide page by SLO
	

	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Saved
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Saved and gets signed
		# in by SSO
		When customer opens Saved from xfinity portal
		Then customer gets signed in to Saved by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Saved
		# by SLO
		When customer reloads Saved page
		Then customer gets signed out from Saved page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity OnDemand
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity OnDemand and gets signed
		# in by SSO
		When customer opens OnDemand from xfinity portal
		Then customer gets signed in to OnDemand by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity OnDemand
		# by SLO
		When customer reloads OnDemand page
		Then customer gets signed out from OnDemand page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Constant Guard
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Constant Guard and gets signed
		# in by SSO
		When customer opens Constant Guard from xfinity portal
		Then customer gets signed in to Constant Guard by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Constant Guard
		# by SLO
		When customer reloads Constant Guard page
		Then customer gets signed out from Constant Guard page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Store Locator
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Store Locator and gets signed
		# in by SSO
		When customer opens Store Locator from xfinity portal
		Then customer gets signed in to Store Locator by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Store Locator
		# by SLO
		When customer reloads Store Locator page
		Then customer gets signed out from Store Locator page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Watch TV Online
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity watch TV online and gets signed
		# in by SSO
		When customer opens watch TV online from xfinity portal
		Then customer gets signed in to watch TV online by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity watch TV online
		# by SLO
		When customer reloads watch TV online page
		Then customer gets signed out from watch TV online page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity parental control
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity parental control and 
		# gets signed in by SSO
		When customer opens parental control from xfinity portal
		Then customer gets signed in to parental control by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity parental control
		# by SLO
		When customer reloads parental control page
		Then customer is redirected to Xfinity Sign In


	@Integration
	Scenario: Validate SSO SLO of Xfinity Find My Account Number
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Find My Account Number and 
		# gets signed in by SSO
		When customer opens Find My Account Number from xfinity portal
		Then customer gets signed in to Find My Account Number by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Find My Account Number
		# by SLO
		When customer reloads Find My Account Number page
		Then customer is redirected to Xfinity Sign In


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Manage My DVR
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Manage My DVR and gets signed
		# in by SSO
		When customer opens Manage My DVR from xfinity portal
		Then customer gets signed in to Manage My DVR by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Manage My DVR
		# by SLO
		When customer reloads Manage My DVR page
		Then customer gets signed out from Manage My DVR page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Contact Customer Support
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Contact Customer Support and gets signed
		# in by SSO
		When customer opens Contact Customer Support from xfinity portal
		Then customer gets signed in to Contact Customer Support by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Contact Customer Support
		# by SLO
		When customer reloads Contact Customer Support page
		Then customer gets signed out from Contact Customer Support page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Home Security and Automation
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Home Security and Automation
		# and gets signed in by SSO
		When customer opens Home Security and Automation from xfinity portal
		And user signs in at Login Page with just a password
		Then customer gets signed in to Home Security and Automation by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Contact Customer Support
		# by SLO
		When customer reloads Home Security and Automation page
		Then customer gets signed out from Home Security and Automation page by SLO


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity Upgrade My Service
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Upgrade My Service
		# and gets signed in by SSO
		When customer opens Upgrade My Service from xfinity portal
		Then customer gets signed in to Upgrade My Service by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Upgrade My Service
		# by SLO
		When customer reloads Upgrade My Service page
		Then customer gets signed out from Upgrade My Service page by SLO



	@Integration
	Scenario: Validate SSO SLO of Xfinity Manage My Account
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Manage My Account
		# and gets signed in by SSO
		When customer opens Manage My Account from xfinity portal
		Then customer gets signed in to Manage My Account by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Manage My Account
		# by SLO
		When customer reloads Manage My Account page
		Then customer is redirected to Xfinity Sign In


	@Integration
	Scenario: Validate SSO SLO of Xfinity Get Apps
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Get Apps
		# and gets signed in by SSO
		When customer opens Get Apps from xfinity portal
		Then customer gets signed in to Get Apps by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Get Apps
		# by SLO
		When customer reloads Get Apps page
		Then customer gets signed out from Get Apps page by SLO


	@Integration
	Scenario: Validate SSO SLO of Xfinity Manage Users and Alerts
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Manage Users and Alerts
		# and gets signed in by SSO
		When customer opens Manage Users and Alerts from xfinity portal
		Then customer gets signed in to Manage Users and Alerts by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Get Apps
		# by SLO
		When customer reloads Manage Users and Alerts page
		Then customer is redirected to Xfinity Sign In


	@Integration @Smoke
	Scenario: Validate SSO SLO of Xfinity TV Listing
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity TV Listing
		# and gets signed in by SSO
		When customer opens TV Listing from xfinity portal
		Then customer gets signed in to TV Listing by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity TV Listing
		# by SLO
		When customer reloads TV Listing page
		Then customer gets signed out from TV Listing page by SLO


	@Integration
	Scenario: Validate SSO SLO of Xfinity Purchase a Movie
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity TV Listing
		# and gets signed in by SSO
		When customer opens Purchase a Movie from xfinity portal
		Then customer gets signed in to Purchase a Movie by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity TV Listing
		# by SLO
		When customer reloads Purchase a Movie page
		Then customer gets signed out from Purchase a Movie page by SLO
		
		
	@Integration
	Scenario: Validate SSO SLO of Xfinity Help & Support
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Help & Support
		# and gets signed in by SSO
		When customer opens Help & Support from xfinity portal
		Then customer gets signed in to Help & Support by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Help & Support
		# by SLO
		When customer reloads Help & Support page
		Then customer gets signed out from Help & Support page by SLO		


	@Integration
	Scenario: Validate SSO SLO of Xfinity Privacy Policy
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Privacy Policy
		# and gets signed in by SSO
		When customer opens Privacy Policy from xfinity portal
		Then customer gets signed in to Privacy Policy by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Privacy Policy
		# by SLO
		When customer reloads Privacy Policy page
		Then customer gets signed out from Privacy Policy page by SLO	
		
		
	@Integration
	Scenario: Validate SSO SLO of Xfinity Terms of Service
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal
		Then sign out link is present in xfinity portal
		
		# customer opens Xfinity Terms of Service
		# and gets signed in by SSO
		When customer opens Terms of Service from xfinity portal
		Then customer gets signed in to Terms of Service by SSO
		
		# customer signs out from xfinity portal
		When customer signs out from Xfinity portal
		Then sign in link is present in xfinity portal
		
		# customer gets signed out from Xfinity Terms of Service
		# by SLO
		When customer reloads Terms of Service page
		Then customer gets signed out from Terms of Service page by SLO	


	@Integration @Smoke
	Scenario: Validate Remember Me in Xfinity Portal 
		
		# Request for an active customer and web browser
		Given an active customer
		And a web browser
	
		# customer signs in to xfinity portal with 'Remember Me'
		When customer opens Xfinity portal in browser
		And customer signs in from xfinity portal with remember me
		Then sign out link is present in xfinity portal
		
		# customer closes and reopens xfinity portal
		When customer closes the browser
		And reopens Xfinity portal within a month
		Then customer is signed in automatically
		
		# customer signs in Xfinity portal after a month
		When customer opens Xfinity portal in browser after a month
		Then customer is not signed in automatically

		