Feature: Obtain OAuth access tokens using Implicit Flow

  @Integration @Smoke
  Scenario Outline: Obtain access token without id_token for valid OAuth client using HTTP url
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And OAuth client with registered scopes "<scopes>" and HTTP redirect url
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow
    And User signs-in at Login Page
    Then Browser navigates to OAuth client's redirect url
    And OAuth access token is present in the url fragment
    And OAuth access token in the url fragment is valid
    And id_token is not present in the url fragment

    Examples:
      | browser name | browser platform | scopes                              |
      | FF           | WINDOWS          | https://login.comcast.net/api/login |

  @Integration @Smoke
  Scenario Outline: Obtain access token with id_token for valid OAuth client using HTTP url
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And OAuth client with registered scopes "<scopes>" and HTTP redirect url
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow
    And User signs-in at Login Page
    Then Browser navigates to OAuth client's redirect url
    And OAuth access token is present in the url fragment
    And OAuth access token in the url fragment is valid
    And id_token is present in the url fragment
    And id_token in the url fragment is valid

    Examples:
      | browser name | browser platform | scopes         |
      | FF           | WINDOWS          | openid profile |

  @Integration @Smoke
  Scenario Outline: Obtain access token without id_token for valid OAuth client using out-of-bounds url
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And OAuth client with registered scopes "<scopes>" and out-of-bounds redirect url
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow
    And User signs-in at Login Page
    Then Browser navigates to OAuth OOB URL
    And OAuth access token is present in the url fragment
    And OAuth access token in the url fragment is valid
    And OAuth access token is present in the OOB Page
    And id_token is not present in the url fragment
    And id_token is not present in the OOB Page

    Examples:
      | browser name | browser platform | scopes                              |
      | FF           | WINDOWS          | https://login.comcast.net/api/login |

  @Integration @Smoke
  Scenario Outline: Obtain access token with id_token for valid OAuth client using out-of-bounds url
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And OAuth client with registered scopes "<scopes>" and out-of-bounds redirect url
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow
    And User signs-in at Login Page
    Then Browser navigates to OAuth OOB URL
    And OAuth access token is present in the url fragment
    And OAuth access token in the url fragment is valid
    And id_token is present in the url fragment
    And id_token in the url fragment is valid
    And id_token is present in the OOB Page

    Examples:
      | browser name | browser platform | scopes         |
      | FF           | WINDOWS          | openid profile |

  @Integration @Smoke
  Scenario Outline: Requesting implicit authorization with invalid OAuth client ID results in error
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And Unregistered OAuth Client
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow
    And User signs-in at Login Page
    Then "invalid_client" OAuth error page is displayed

    Examples:
      | browser name | browser platform | scopes |
      | FF           | WINDOWS          | openid |

  @Integration @Smoke
  Scenario Outline: Requesting implicit authorization with passive authentication without logged in user results in error response
    Given Web browser named "<browser name>" for platform "<browser platform>"
    And OAuth client with registered scopes "<scopes>" and out-of-bounds redirect url
    And Active user
    When OAuth client requests access token for scopes "<scopes>" using implicit grant flow and passive authentication
    Then Browser navigates to OAuth OOB URL
    And "login_required" OAuth error is present in the url fragment

    Examples:
      | browser name | browser platform | scopes         |
      | FF           | WINDOWS          | openid profile |
