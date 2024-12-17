Feature: Put User API

  Scenario: Update an existing user's details
    Given I send a PUT request with the following details:
      | name         | username | email             |
      | Jane Smith   | janes    | janesmith@test.com|
    Then I should receive a response with status code 200
    And The response body should contain "Jane Smith"
