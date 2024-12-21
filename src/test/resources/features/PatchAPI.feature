#
#Feature: Patch User API
#
  #Scenario: Partially update an existing user's details
    #Given I send a PATCH request with the following details:
      #| email          |
      #| jane.doe@test.com |
    #Then I should receive a response with status code 200
    #And The response body should contain "jane.doe@test.com"
