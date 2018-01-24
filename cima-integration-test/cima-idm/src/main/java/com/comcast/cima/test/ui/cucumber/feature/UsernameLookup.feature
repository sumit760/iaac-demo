Feature: Validate Username lookup flow in IDM

@Integration @Smoke
Scenario Outline: Validate username lookup using valid account number and street address/phone number  
Given account number <alternateEmailStatus> alternative email and <secondAttribute> after clicking the link in sign in page and selecting account number in method selection page
When the user is going to lookup his username with account number and another attribute in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out his email using account number and continues by selecting email

Examples:

| secondAttribute |alternateEmailStatus|environment|platform|type|browser|
|~ADDRESS~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~ADDRESS~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration @Smoke
Scenario Outline: Validate username lookup using valid phone number  
Given phone number <alternateEmailStatus> alternative email after clicking the link in sign in page and selecting phone number in method selection page
When the user is going to lookup his username with phone number in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out his email using phone number and continues by selecting email

Examples:

| alternateEmailStatus |environment|platform|type|browser|
|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration @Smoke
Scenario Outline: Validate username lookup using valid fresh account number and street address/phone without any user 
Given account number and <secondAttribute> after clicking the link in sign in page and selecting account number in method selection page
When the non-exist user is going to lookup his username with account number and another attribute in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user has been redirected to create user page using account number

Examples:

| secondAttribute |environment|platform|type|browser|
|~ADDRESS~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate username lookup using valid fresh phone number without any user
Given phone number after clicking the link in sign in page and selecting phone number in method selection page
When the non-exist user is going to lookup his username with phone number in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user has been redirected to create user page using phone number

Examples:

|environment|platform|type|browser|
|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration @Smoke
Scenario Outline: Validate username lookup in create UID using valid account number and street address/phone number 
Given account number <alternateEmailStatus> alternative email and <secondAttribute> after clicking the create username in sign in page and selecting account number in method selection page
When the user is going to create username with account number and another attribute in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out username and email using account number and continues by selecting email

Examples:

| secondAttribute |alternateEmailStatus|environment|platform|type|browser|
|~ADDRESS~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~ADDRESS~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate username lookup in create UID using valid phone number  
Given phone number <alternateEmailStatus> alternative email after clicking the create username in sign in page and selecting phone number in method selection page
When the user is going to create username with phone number in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out username and email using phone number and continues by selecting email

Examples:

| alternateEmailStatus |environment|platform|type|browser|
|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration @Smoke
Scenario Outline: Validate username lookup through password reset flow using valid account number and street address/phone number
Given account number <alternateEmailStatus> alternative email and <secondAttribute> after clicking the forgot password in sign in page and selecting account number in method selection page
When the user is going to forgot password with account number and another attribute in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out username with or without email through password reset flow

Examples:

| secondAttribute |alternateEmailStatus|environment|platform|type|browser|
|~ADDRESS~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~ADDRESS~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate username lookup in forgot password using valid phone number  
Given phone number <alternateEmailStatus> alternative email after clicking the forgot password in sign in page and selecting phone number in method selection page
When the user is going to forgot password with phone number in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out username and email in forgot password using phone number and continues by selecting email

Examples:

| alternateEmailStatus |environment|platform|type|browser|
|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration @Smoke
Scenario Outline: Validate username lookup using valid business account number and street address/phone number 
Given business account number <alternateEmailStatus> alternative email and <secondAttribute> after clicking the link in sign in page and selecting account number in method selection page
When the user is going to lookup his username with business account number and another attribute in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out his email using business account number and continues by selecting email

Examples:

| secondAttribute |alternateEmailStatus|environment|platform|type|browser|
|~ADDRESS~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~ADDRESS~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Integration
Scenario Outline: Validate username lookup using valid business user phone number  
Given business user phone number <alternateEmailStatus> alternative email after clicking the link in sign in page and selecting phone number in method selection page
When the user is going to lookup his username with business user phone number in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser>
Then the user is able to find out his email using business user phone number and continues by selecting email

Examples:

| alternateEmailStatus |environment|platform|type|browser|
|~WITH~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~WITHOUT~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|