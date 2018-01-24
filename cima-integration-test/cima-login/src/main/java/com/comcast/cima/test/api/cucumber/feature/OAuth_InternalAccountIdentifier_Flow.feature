Feature: Obtain OAuth Access token using Internal Account Identifier Flow

  @Integration @Smoke
  Scenario Outline: Obtain account-level OAuth access token for a valid account
    Given OAuth client with registered scopes "<scopes>" and internal account identifier flow
    And active billing account
    And valid internal account grant token signing key
    When OAuth client requests access token for scopes "<scopes>" using internal account identifier flow
    Then OAuth client receives successful internal account Id response
    And OAuth access token in internal account Id response is valid
    Examples:
      | scopes                                           |
      | https://secure.api.comcast.net/sig/account/basic |

  @Integration @Smoke
  Scenario Outline: Receive an error response for invalid account
    Given OAuth client with registered scopes "<scopes>" and internal account identifier flow
    And invalid billing account
    And valid internal account grant token signing key
    When OAuth client requests access token for scopes "<scopes>" using internal account identifier flow
    Then OAuth client receives "invalid_request" error response
    Examples:
      | scopes                                           |
      | https://secure.api.comcast.net/sig/account/basic |

  @Integration
  Scenario Outline: Receive an error response for incorrectly signed request
    Given OAuth client with registered scopes "<scopes>" and internal account identifier flow
    And active billing account
    And invalid internal account grant token signing key
    When OAuth client requests access token for scopes "<scopes>" using internal account identifier flow
    Then OAuth client receives "invalid_request" error response
    Examples:
      | scopes                                           |
      | https://secure.api.comcast.net/sig/account/basic |

  @Integration
  Scenario: Receive an error response for invalid client id
    Given invalid OAuth client for internal account identifier flow
    And active billing account
    And valid internal account grant token signing key
    When OAuth client requests access token for scopes "" using internal account identifier flow
    Then OAuth client receives "unauthorized" error response
