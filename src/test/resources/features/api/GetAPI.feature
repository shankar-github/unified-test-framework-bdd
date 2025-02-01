
Feature: User  API Testing
@api
  Scenario Outline: Validate user details and error cases
    Given the API endpoint is set to /users/"<userId>"
    When a GET request is sent to the endpoint
    Then the status code should be <statusCode>
    And the response body should contain the string "<responseMessage>"

  Examples:
    | userId	| statusCode | responseMessage          |
    | 1       | 200        | Leanne Graham            |
    |	2       | 200        | Ervin Howell             |
    | 3       | 200        | Clementine Bauch         |
    | 999     | 404        | 								          |
    | abc     | 404        | 												  |
    |       	| 200        | 										      |

 