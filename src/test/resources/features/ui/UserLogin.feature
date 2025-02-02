Feature: User Login

 Feature: User Login

  @UI
  Scenario: Valid login with standard user credentials
    Given the user navigates to the sign-in page
    When the user enters valid credentials with username "standard_user" and password "secret_sauce"
    Then the user should be redirected to the dashboard with message "Swag Labs"

  @UI
  Scenario: Invalid login with incorrect credentials
    Given the user navigates to the sign-in page
    When the user enters invalid credentials with username "invalidUser" and password "invalidPassword"
    Then the user should see an error message "Epic sadface: Username and password do not match any user in this servic"
