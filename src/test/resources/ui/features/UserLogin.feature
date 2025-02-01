Feature: User Login

  Scenario: Valid login with correct credentials
    Given the user navigates to the sign-in page
    When the user enters valid credentials with username "validUser" and password "validPassword"
    Then the user should be redirected to the dashboard

  Scenario: Invalid login with incorrect credentials
    Given the user navigates to the sign-in page
    When the user enters invalid credentials with username "invalidUser" and password "invalidPassword"
    Then the user should see an error message "Invalid credentials"
