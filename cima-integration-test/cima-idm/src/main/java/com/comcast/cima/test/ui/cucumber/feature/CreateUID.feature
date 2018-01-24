Feature: Validate create UID flow in IDM

@Smoke
Scenario Outline: Create userId verified by mobile and various password recovery options  
Given the user is on xfinity Login page and wants to create his userID
When the user identifies himself with phone number and provides userId: <uid> and password recovery option as: <recoveryOption> in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to create his Comcast userID verified by mobile

Examples:

| uid |recoveryOption|environment|platform|type|browser|
|~GENERATE~|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~GENERATE~|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~NA~|~UIDEmail~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|


@Smoke
Scenario Outline: Create userId verified by account number and various password recovery options  
Given the user is on xfinity Login page and wants to create his userID
When the user identifies himself with account number and identifier: <identifierType> and provides userId: <uid> and password recovery option as: <recoveryOption> in Environment : <environment> , Platform <platform> , device type : <type> and Browser :<browser> .
Then the user is able to create his Comcast userID verified by account number

Examples:

| identifierType |uid|recoveryOption|environment|platform|type|browser|
|~STADDRESS~|~GENERATE~|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~STADDRESS~|~GENERATE~|~Phone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~STADDRESS~|~GENERATE~|~EmailPhone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~STADDRESS~|~GENERATE~|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~STADDRESS~|~NA~|~UIDEmail~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~STADDRESS~|~NA~|~UIDEmailPhone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~GENERATE~|~Email~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~GENERATE~|~Phone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~GENERATE~|~EmailPhone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~GENERATE~|~SQA~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~NA~|~UIDEmail~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|
|~PHONE~|~NA~|~UIDEmailPhone~|~QA~|~WINDOWS~|~COMPUTER~|~FF~|



