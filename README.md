# Unified Test Framework BDD

This framework provides production-level API and UI test automation capabilities.

## Features:
- **Modular Design**: The framework is designed in a modular way to ensure easy maintenance and scalability. Each component is separated into different packages.
- **Logging with SLF4J**: Comprehensive logging using SLF4J for better traceability and debugging. Configurable logging levels and output formats.
- **RestAssured for API Testing**: Utilizes RestAssured for efficient and effective API testing, supporting various HTTP methods and validation mechanisms.
- **Selenium for UI Testing**: Integrates Selenium for robust UI testing across different browsers.
- **TestNG for Test Execution**: Leverages TestNG for organizing and running tests, providing features like parallel execution, data-driven testing, and detailed reports.
- **Database Connections**: Supports connections to MySQL and MongoDB databases, allowing for setup and teardown of test data.
- **Reusable Utilities**: Includes a set of reusable utilities to simplify common tasks such as reading configurations, handling JSON, and managing headers.

## Directory Structure:
```plaintext
unified-test-framework-bdd/
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── api         # API-related classes
│   │   │   ├── common      # Common utilities and base classes
│   │   │   ├── ui          # UI-related classes
│   │   │   ├── utils       # Utility classes
│   ├── test
│       ├── java
│       │   ├── api         # API test classes
│       │   ├── ui          # UI test classes
│       │   ├── steps       # Step definitions for BDD
│       │   ├── runners     # Test runners
│       ├── resources
│           ├── features    # Feature files for BDD
│           ├── config      # Configuration files
│           ├── logs        # Log files

Prerequisites:
Java: Ensure Java is installed on your system. The framework is compatible with Java 8 and above.
Maven: Install Maven for managing dependencies and running tests. Ensure mvn is available in your system's PATH.

Running Tests:
Clone the repository:
bash
Copy
Edit
git clone https://github.com/shankar-github/unified-test-framework-bdd.git
cd unified-test-framework-bdd
Install dependencies and run tests:
bash
Copy
Edit
mvn clean test
Running API Tests:
To run API tests, use the following command:
bash
Copy
Edit
mvn test -Dgroups="api"
Running UI Tests:
To run UI tests, use the following command:
bash
Copy
Edit
mvn test -Dgroups="ui"
Running Tagged Tests:
To run tests with specific tags, use the following command:
bash
Copy
Edit
mvn test -Dgroups="your-tag"
Configuration:
Database Configuration: Configure database connections in src/main/resources/config/database.properties. Example:
properties
Copy
Edit
db.url=jdbc:mysql://localhost:3306/testdb
db.password=password
API Configuration: Configure API endpoints and other settings in src/main/resources/config/api.properties. Example:
properties
Copy
Edit
api.base.url=https://api.example.com
api.timeout=5000
Logging:
Logs are managed using SLF4J and can be configured in src/main/resources/log4j2.xml. Example configuration:
xml
Copy
Edit
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
Adding New Tests:
Adding API Tests:
Create Feature Files: Add new feature files in src/test/resources/features/api. Example:
gherkin
Copy
Edit
Feature: User API
Scenario: Create a new user
    Given I have a valid user payload
    When I send a POST request to "/users"
    Then the response status should be 201
    And the response body should contain the user details
Implement Step Definitions: Implement step definitions in src/test/java/steps/api. Example:
java
Copy
Edit
@Given("I have a valid user payload")
public void iHaveAValidUserPayload() {
    // Implementation here
}

@When("I send a POST request to {string}")
public void iSendAPostRequestTo(String endpoint) {
    // Implementation here
}

@Then("the response status should be {int}")
public void theResponseStatusShouldBe(int statusCode) {
    // Implementation here
}

@Then("the response body should contain the user details")
public void theResponseBodyShouldContainTheUserDetails() {
    // Implementation here
}
Add Test Runners: Add test runners in src/test/java/runners/api. Example:
java
Copy
Edit
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/api",
    glue = "steps/api",
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class ApiTestRunner {
}
Adding UI Tests:
Create Feature Files: Add new feature files in src/test/resources/features/ui. Example:
gherkin
Copy
Edit
Feature: User Login UI
Scenario: User logs in successfully
    Given I am on the login page
    When I enter valid credentials
    Then I should be redirected to the dashboard
Implement Step Definitions: Implement step definitions in src/test/java/steps/ui. Example:
java
Copy
Edit
@Given("I am on the login page")
public void iAmOnTheLoginPage() {
    // Implementation here
}

@When("I enter valid credentials")
public void iEnterValidCredentials() {
    // Implementation here
}

@Then("I should be redirected to the dashboard")
public void iShouldBeRedirectedToTheDashboard() {
    // Implementation here
}
Add Test Runners: Add test runners in src/test/java/runners/ui. Example:
java
Copy
Edit
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = "steps/ui",
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class UiTestRunner {
}
Dependencies:
Ensure all dependencies are listed in pom.xml. Example:
xml
Copy
Edit
<dependencies>
    <dependency>
        <groupId>io.rest-assured</groupId>
        <artifactId>rest-assured</artifactId>
        <version>4.3.3</version>
    </dependency>
    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.3.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>3.141.59</version>
    </dependency>
    <!-- Add other dependencies here -->
</dependencies>
Contributing:
Fork the Repository: Fork the repository to your GitHub account.
Create a Branch: Create a new branch for your feature or bugfix.
Submit a Pull Request: Submit a pull request with a detailed description of your changes.
License:
This project is licensed under the MIT License.
