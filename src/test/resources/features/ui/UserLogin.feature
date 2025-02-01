Feature: User Login

  @UI
  Scenario Outline: User login with different credentials
    Given the user navigates to the sign-in page
    When the user enters credentials with username "<username>" and password "<password>" for "<test-case>"
    Then the user should <result>

    Examples: 
      | username      | password        | test-case | result                                                                    |
      | standard_user | secret_sauce    | positive | Swag Labs                                                                 |
      | invalidUser   | invalidPassword | negative | Epic sadface: Username and password do not match any user in this service |
