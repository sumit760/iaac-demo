Feature: Validate password reset flows in IDM


@Integration @Smoke
Scenario Outline: Validate inactive users cannot login
    Given the user is on xfinity Login page
    When the compromised user attempts to login in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
    Then the inactive user ~<username>~ is not able to login because this user has a ~<cstLoginStatus>~ value for the 'cstLoginStatus' attribute in ESD
  Examples:
    |environment    |platform   |type       |   browser|username                       |cstLoginStatus |
    |           ~QA~|  ~WINDOWS~| ~COMPUTER~|      ~FF~|  xcima_test_account_inactive_1|              S|
    |           ~QA~|  ~WINDOWS~| ~COMPUTER~|      ~FF~|  xcima_test_account_inactive_2|              D|

@Integration @Smoke
Scenario Outline: Validate password reset for compromised user
    Given the user is on xfinity Login page
    When the compromised user attempts to login in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
    Then the compromised user is able to reset the password
  Examples:
    |environment    |platform   |type       |   browser|
    |           ~QA~|  ~WINDOWS~| ~COMPUTER~|      ~FF~|

@Integration @Smoke
Scenario Outline: Validate password reset with SQA recovery options
Given the user is on xfinity Login page
When the user with recovery option as SQA chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using SQA recovery options method

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Validate password reset with SMS recovery options
Given the user is on xfinity Login page
When the user with recovery option as SMS chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using SMS recovery options method

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SMS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|




@Integration @Smoke
Scenario Outline: Validate password reset with Email recovery options
Given the user is on xfinity Login page
When the user with recovery option as Email chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using Email recovery options method


Examples:

| RecoveryOption |environment|platform|type|browser|
|~EMAIL~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with SMS and SQA recovery options
Given the user is on xfinity Login page
When the user with recovery option as SMS and SQA chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using SMS and SQA recovery options method

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SMS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with Email and SQA recovery options
Given the user is on xfinity Login page
When the user with recovery option as Email and SQA chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using Email and SQA recovery options method

Examples:

| RecoveryOption |environment|platform|type|browser|
|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Validate password reset with Email, SMS and SQA recovery options
Given the user is on xfinity Login page
When the user with recovery option as Email, SMS and SQA chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password using Email and SMS recovery options method

Examples:

| RecoveryOption |environment|platform|type|browser|
|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SMS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration @Smoke
Scenario Outline: Validate password reset with the help of field agents
Given the user is on xfinity Login page
When the user forgots his recovery option as Email, SMS and SQA chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password with the help of the agents

Examples:

| RecoveryOption |environment|platform|type|browser|
|~Agent~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



@Integration
Scenario Outline: Validate password reset with SQA or SMS after selecting Email
Given the user is on xfinity Login page
When the user does not have email access after selecting email and chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password with either of SQA or SMS

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SMS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with SQA and Email after selecting SMS
Given the user is on xfinity Login page
When the user does not have phone access after selecting SMS and chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password with either of SQA or Email

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with SQA or SMS after generating recovery link using Email
Given the user is on xfinity Login page
When the user does not have email access after generating recovery link by email and chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password with either of SQA or SMS even after generating the recovery link by email

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~SMS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate password reset with SQA or Email after generating recovery code using SMS
Given the user is on xfinity Login page
When the user does not have phone access after generating recovery code by SMS and chooses <RecoveryOption> as recovery option in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to reset the password with either of SQA or Email even after generating the recovery code by SMS

Examples:

| RecoveryOption |environment|platform|type|browser|
|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



