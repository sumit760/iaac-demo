Feature: Validate the negative scenarios in the username lookup flow in IDM

@Integration
Scenario Outline: Provide invalid phone number when verifying user identity in username lookup
Given the user is on xfinity Login page and clicks username lookup link
When the user provides phone number type: <phoneNumberType> with value: <invalidPhoneNumber> when verifying his identity for username lookup in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid phone number error message for username lookup

Examples:

| phoneNumberType |invalidPhoneNumber|environment|platform|type|browser|
|~INVALIDLENGTH~|~11111111~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALID~|~2152006312~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Provide invalid sms code when verifying user identity in username lookup
Given the user is on xfinity Login page and clicks username lookup link
When the user provides smscode of type: <smsCodeType> with value: <invalidSMSCode> when verifying his identity for username lookup in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user gets the invalid sms code error message for username lookup

Examples:

| smsCodeType |invalidSMSCode|environment|platform|type|browser|
|~EMPTY~|~~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALID~|~000000~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~INVALIDTHRICE~|~000000,000001,000010~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|