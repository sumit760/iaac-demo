Feature: Validate password reset flows in IDM with invalid data


@Integration
Scenario Outline: Validate password reset for an invalid user 
Given the user is on xfinity SignIn page
When the invalid user: <userID> continue to recover password in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity displays error message for the user

Examples:

|userID|environment|platform|type|browser|
|~invaliduser~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~   ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset for invalid SQA and invalid ZIP
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides wrong answer: <wrongAnswer> and wrong zipcode: <wrongZipCode> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity displays error message for invalid SQA  and Invalid ZIP


Examples:
|RecoveryOption|wrongAnswer|wrongZipCode|environment|platform|type|browser|
|~SQA~|~coke123~|~00000~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with empty SQA and empty ZIP
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides empty answer: <wrongAnswer> and empty zipcode: <wrongZipCode> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity display error message for empty SQA and empty ZIP


Examples:
|RecoveryOption|wrongAnswer|wrongZipCode|environment|platform|type|browser|
|~SQA~|~  ~|~  ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~wrong~|~  ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~  ~|~99999~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with empty New Password
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides empty password in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity display empty password error message


Examples:
|RecoveryOption|environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with Password and confirm password not matching
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides non matching <password> and <retypepassword> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity display error message for invalid confirm passwords


Examples:
|RecoveryOption|password|retypepassword|environment|platform|type|browser|
|~SQA~|~Comcast123~|~Cocast123~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with Password less than 8 char or more than 16 char 
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides password: <password> less than eight or more than sixteen chars in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity display invalid password error message


Examples:
|RecoveryOption|password|environment|platform|type|browser|
|~SQA~|~less8~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~passwordmorethansixteencharacters~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|




@Integration
Scenario Outline: Validate password reset with Password same as username or last name or first name
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides password as: <passwordType> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser> 
Then Xinfity displays invalid password error message stating password can not be username or last name or first name


Examples:
|RecoveryOption|passwordType|environment|platform|type|browser|
|~SQA~|~USERNAME~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~FIRSTNAME~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~LASTNAME~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with invalid Password format
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides invalid password of type: <passwordType> as: <password> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity displays invalid password format error message


Examples:
|RecoveryOption|passwordType|password|environment|platform|type|browser|
|~SQA~|~NOLETTER~|~40057789211~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~NONUMBER~|~MyComcast~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~NOLETTER~|~@!$%&<?([}~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~WITHSPACE~|~My Comcast~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~EASYTOGUESS~|~123456789~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with invalid Reset Code from the agents
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides invalid reset codde: <resetcode> from the agent in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity displays invalid reset code error message


Examples:
|RecoveryOption|resetcode|environment|platform|type|browser|
|~SQA~|~123456~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~  ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with invalid SMS code
Given the user is on xfinity SignIn page
When the user with RecoveryOption: <RecoveryOption> provides invalid SMS codde: <smscode> in Environment : <environment> , Platform <platform> , device type : <type> and Browser : <browser>
Then Xinfity displays invalid SMS code error message


Examples:
|RecoveryOption|smscode|environment|platform|type|browser|
|~SMS~|~123456~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SMS~|~  ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



