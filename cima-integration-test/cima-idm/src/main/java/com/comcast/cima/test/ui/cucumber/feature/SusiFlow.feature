#SuSi Flow is only valid in Staging
Feature: Validate SuSi flows

#@Integration @Smoke
Scenario Outline: Existing wifi flow setting- Without SMS confirmation
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user provides valid mobile number, accepts terms, and clicks Next button
Then user successfully lands on SuSi entering verification code page
When user enters verification code in SuSi and clicks Next button
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page
When user selects Existing WiFi Settings and clicks Next button
Then user lands on updating your settings page
And user should get the All Set page

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|


#@Integration @Smoke
Scenario Outline: Existing wifi flow setting-With SMS confirmation
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user provides valid mobile number, accepts terms, and clicks Next button
Then user successfully lands on SuSi entering verification code page
When user enters verification code in SuSi and clicks Next button
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page
When user selects Existing WiFi Settings, checks to receive message, and clicks Next button
Then user lands on updating your settings page
And user should get the All Set page
And user should receive a text message about WiFi detail

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|


#@Integration @Smoke
Scenario Outline: New wifi setting flow- Without SMS confirmation
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user provides valid mobile number, accepts terms, and clicks Next button
Then user successfully lands on SuSi entering verification code page
When user enters verification code in SuSi and clicks Next button
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page
When user creates a New WiFi and Password, and clicks Next button
Then user lands on updating your settings page
And user should get the All Set page

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|


#@Integration @Smoke
Scenario Outline: New wifi setting flow- With SMS confirmation
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user provides valid mobile number, accepts terms, and clicks Next button
Then user successfully lands on SuSi entering verification code page
When user enters verification code in SuSi and clicks Next button
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page
When user creates a New WiFi and Password, checks to receive message, and clicks Next button
Then user lands on updating your settings page
And user should get the All Set page
And user should receive a text message about WiFi detail

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|


#@Integration @Smoke
Scenario Outline: Login with username & Password
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user prefers to sign in to SuSi account with username and password
Then user successfully lands on SuSi entering credential page
When user enters username and password, and clicks Sign In button to login SuSi account
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|


#@Integration @Smoke
Scenario Outline: Login with Phone number
Given an existing user with phone number
And user opens SuSi IP Address page in browser <browser> over <platform> 
When user enters IP address <IP address> in SuSi and clicks Next button
Then user successfully lands on SuSi entering mobile page
When user provides valid mobile number, accepts terms, and clicks Next button
Then user successfully lands on SuSi entering verification code page
When user enters verification code in SuSi and clicks Next button
Then user successfully lands on SuSi your account page
When user authenticates himself in SuSi your account page, accepts agreement, and clicks Next button
Then user successfully lands on SuSi set up WiFi page

Examples:

|IP address|platform|browser|
|73.5.240.226|WINDOWS|FF|