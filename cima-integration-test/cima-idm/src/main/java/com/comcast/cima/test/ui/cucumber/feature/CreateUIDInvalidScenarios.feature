Feature: Validate the negative scenarios in the create UID flow in IDM

@Integration @Smoke
Scenario Outline: Provide invalid phone number when verifying user identity
Given the user is on xfinity Login page and clicks create userID link
When the user provides phone number type: <phoneNumberType> with value: <invalidPhoneNumber> when verifying his identity in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid phone number error message

Examples:

| phoneNumberType |invalidPhoneNumber|environment|platform|type|browser|
|~INVALIDLENGTH~|~11111111~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALID~|~2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Provide invalid sms code when verifying user identity
Given the user is on xfinity Login page and clicks create userID link
When the user provides smscode of type: <smsCodeType> with value: <invalidSMSCode> when verifying his identity in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid sms code error message

Examples:

| smsCodeType |invalidSMSCode|environment|platform|type|browser|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALID~|~000000~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDTHRICE~|~000000,000001,000010~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Provide invalid username when creating userID
Given the user is on xfinity Login page and clicks create userID link
When the user provides username of type: <userNameType> of value: <invalidUsername> when creating userID in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid username error message

Examples:

| userNameType |invalidUsername|environment|platform|type|browser|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SHORT~|~ab~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~LONG~|~averylongusernameofsizemorethanthirtytwocharacters~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WTHSPACE~|~userId with space~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Provide invalid password when creating userID
Given the user is on xfinity Login page and clicks create userID link
When the user provides invalid password of type: <passwordType> as: <password> when creating userID in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid password error message

Examples:

| passwordType |password|environment|platform|type|browser|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~LESSTHAN8~|~less8~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~MORETHAN16~|~passwordmorethansixteencharacters~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~USERNAME~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~FIRSTNAME~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~LASTNAME~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~NOLETTER~|~40057789211~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~NONUMBER~|~MyComcast~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~NOLETTER~|~@!$%&<?([}~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WITHSPACE~|~My Comcast~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~EASYTOGUESS~|~123456789~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~MISMATCH~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Provide invalid alternate email when creating userID
Given the user is on xfinity Login page and clicks create userID link
When the user provides invalid alternate email: <invalidAltEmail> when creating userID in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid email error message

Examples:

| invalidAltEmail |environment|platform|type|browser|
|~abcd~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~www.mail.com~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~@gmail.com~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Provide invalid Mobile Phone when creating userID
Given the user is on xfinity Login page and clicks create userID link
When the user provides mobile phone number type: <phoneNumberType> with value: <invalidPhoneNumber> in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid mobile phone error message

Examples:

| phoneNumberType |invalidPhoneNumber|environment|platform|type|browser|
|~INVALIDLENGTH~|~11111111~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALID~|~2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|




@Integration @Smoke
Scenario Outline: Provide invalid answer to security question when creating userID
Given the user is on xfinity Login page and clicks create userID link
When the user provides invalid SQA type: <SQAType> of value: <SQAValue> in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid answer error message

Examples:

| SQAType |SQAValue|environment|platform|type|browser|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~LESSTHAN3~|~ab~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~MORETHAN25~|~averylongsecurityquestionanswerforpasswordrecovery~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|




@Integration @Smoke
Scenario Outline: Provide invalid account number,street address and phone number when verifying user identity
Given the user is on xfinity Login page and clicks create userID link
When the user provides invalid identity type <identityType> of value <identityValue> when verifying identity in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid identity parameter error message

Examples:

| identityType |identityValue|environment|platform|type|browser|
|~EMPTYACCNOSTADDRPHONE~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~EMPTYSTADDR~|~ ~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~EMPTYPHONE~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDPHONE~|~1111111~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDACCNO,VALIDSTRADDR~|~1111111111,~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDACCNO,VALIDPHONENO~|~1111111111,~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~VALIDACCNO,INVALIDSTRADDR~|~,invalid street address~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~VALIDACCNO,INVALIDPHONENO~|~,2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDACCNO,INVALIDSTRADDR~|~1111111111,invalid street address~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDACCNO,INVALIDPHONE~|~1111111111,2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Provide invalid SSN DOB and Phone number when verifying user identity
Given the user is on xfinity Login page and clicks create userID link
When the user provides invalid identity type <identityType> of value <invalidValue> when verifying identity in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid identity parameter error message

Examples:

| identityType |invalidValue|environment|platform|type|browser|
|~EMPTYSSNDOBPHONE~|~0000~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDSSN~|~0000~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDDOB~|~28/12/1976~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~DOB~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDPHONELENGTH~|~11111111~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDSSN,VALIDDOB,VALIDPHONE~|~0001,,~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~VALIDSSN,INVALIDDOB,INVALIDPHONE~|~,12/22/2000,2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~VALIDSSN,VALIDDOB,INVALIDPHONE~|~,,2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~VALIDSSN,INVALIDDOB,VALIDPHONE~|~,12/22/2000,~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|

