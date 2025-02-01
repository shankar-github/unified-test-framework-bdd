#Feature: User API Tests with Various Data Sources
#
  #Scenario: Create a new user with JSON data from the feature file
    #Given I send a POST request with the JSON data:
      #"""
      #{
        #"name": "Jane Doe",
        #"username": "janed",
        #"email": "janedoe@test.com"
      #}
      #"""
    #Then I should receive a response with status code 201
    #And The response body should contain "Jane Doe"
#
  #Scenario: Create a new user with table data from the feature file
    #Given I send a POST request with the following details:
      #| name         | username | email             |
      #| John Smith   | johns    | johnsmith@test.com|
    #Then I should receive a response with status code 201
    #And The response body should contain "John Smith"
#
  #Scenario: Create a new user from CSV file
    #Given I send a POST request with user data from CSV file "users.csv"
    #Then I should receive a response with status code 201
    #And The response body should contain "abcd"
#
  #Scenario: Create a new user from an external JSON file
    #Given I send a POST request with user data from JSON file "users.json"
    #Then I should receive a response with status code 201
    #And The response body should contain "Ram Ram"
#
  #Scenario: Create a new user from an Excel file
    #Given I send a POST request with user data from Excel file "users.xlsx"
    #Then I should receive a response with status code 201
    #And The response body should contain "Bob Smith"
