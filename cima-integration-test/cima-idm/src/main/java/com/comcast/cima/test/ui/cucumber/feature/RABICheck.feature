Feature: Validate RABI check flow in IDM

@Integration @Smoke
Scenario Outline: Validated RABI check flow by adding SMS as recovery option  
Given an existing user with only SQA as recovery option
And user opens Xfinity RABI Login page in browser <browser> over <platform> 
When user signs in Xfinity for RABI
Then user successfully lands on Update your password recovery options page
When user verifies a phone number and enters SMS code and clicks Continue button
Then user successfully lands on Review your password recovery options page
Then user verifies mobile number has set in LDAP for RABI check

Examples:

|platform|browser|
|WINDOWS|FF|

@Integration @Smoke
Scenario Outline: Validated RABI check flow by adding third party email as recovery option
Given an existing user with only SQA as recovery option
When third party email status is checked in the log before test
And user opens Xfinity RABI Login page in browser <browser> over <platform> 
When user signs in Xfinity for RABI
Then user successfully lands on Update your password recovery options page
When user enters third party email and clicks Continue button
Then user successfully lands on Review your password recovery options page
When third party email is verified in the log
Then user verifies third party email has set in LDAP for RABI check

Examples:

|platform|browser|
|WINDOWS|FF|

@Integration @Smoke
Scenario Outline: Validated RABI check flow by adding SMS and third party email as recovery options 
Given an existing user with only SQA as recovery option
When third party email status is checked in the log before test
And user opens Xfinity RABI Login page in browser <browser> over <platform> 
When user signs in Xfinity for RABI
Then user successfully lands on Update your password recovery options page
When user enters third party email and verifies a phone number and enters SMS code and clicks Continue button
Then user successfully lands on Review your password recovery options page
When third party email is verified in the log
Then user verifies mobile number and third party email have set in LDAP for RABI check

Examples:

|platform|browser|
|WINDOWS|FF|

@Integration
Scenario Outline: Validated RABI check flow by adding SMS and third party email also changing SQA as recovery options
Given an existing user with only SQA as recovery option
When third party email status is checked in the log before test
And user opens Xfinity RABI Login page in browser <browser> over <platform> 
When user signs in Xfinity for RABI
Then user successfully lands on Update your password recovery options page
When user enters third party email and verifies a phone number and enters SMS code also changes SQA and clicks Continue button
Then user successfully lands on Review your password recovery options page
When third party email is verified in the log
Then user verifies mobile number and third party email also updated SQA have set in LDAP for RABI check

Examples:

|platform|browser|
|WINDOWS|FF|
