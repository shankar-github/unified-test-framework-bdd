#
#Feature: Put User API
#
  #Scenario: Update an existing user detail
    #Given I send a PUT request with the following details:
      #| userId         | username | email             |
      #| 1						   | janes    | janesmith@test.com|
    #Then I should receive a PUT response with status code 200
    #And The PUT response body should contain "Jane Smith"
