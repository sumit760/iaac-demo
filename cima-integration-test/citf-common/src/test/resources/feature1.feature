Feature: Obtain access token through OAuth Enabled REST API for valid data

@Integration
Scenario Outline: Obtain access token through OAuth Enabled REST API for valid data
Given client id, password, response type, scope and user id for generating access token for service: <serviceID> entitlement: <entitlement> authorizationStatus: <authorizationStatus> authenticationStatus: <authenticationStatus>
When request for authorization token is made for valid data
Then Generate authorization token for valid data
Given client id, client secret, grant type, scope and generated authorization token for valid data
When request for access token is made for valid data
Then Generate OAuth access token for valid data


Examples:

| serviceID | entitlement |authorizationStatus|authenticationStatus|
|~REST_API_LOGIN_SMARTZONE~|~HSD~|~Success~|~Success~|