Feature: Get User API

  Scenario: Get user details
    Given I send a GET request
    Then I should receive a response with status code 200
    And The response body should contain "Leanne Graham"
