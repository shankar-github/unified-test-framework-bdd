# API Test Automation Framework

This framework provides production-level API test automation capabilities.

## Features:
- **Modular Design**: The framework is designed in a modular way to ensure easy maintenance and scalability. Each component is separated into different packages.
- **Logging with SLF4J**: Comprehensive logging using SLF4J for better traceability and debugging. Configurable logging levels and output formats.
- **RestAssured for API Testing**: Utilizes RestAssured for efficient and effective API testing, supporting various HTTP methods and validation mechanisms.
- **TestNG for Test Execution**: Leverages TestNG for organizing and running tests, providing features like parallel execution, data-driven testing, and detailed reports.
- **Database Connections**: Supports connections to MySQL and MongoDB databases, allowing for setup and teardown of test data.
- **Reusable Utilities**: Includes a set of reusable utilities to simplify common tasks such as reading configurations, handling JSON, and managing headers.

## Directory Structure:
api-test-framework/ ├── pom.xml ├── README.md ├── src │ ├── main │ │ ├── java │ │ │ ├── base # Base classes and utilities │ │ │ ├── config # Configuration management │ │ │ ├── database # Database connection and operations │ │ │ ├── helpers # Helper classes for various tasks │ │ │ └── api # API request and response handling │ │ └── resources │ │ └── test-data # Test data files │ └── test │ ├── java │ │ ├── runners # Test runners for executing tests │ │ └── tests # Test cases and step definitions │ └── resources │ └── features # Cucumber feature files


## Prerequisites:
- **Java**: Ensure Java is installed on your system. The framework is compatible with Java 8 and above.
- **Maven**: Install Maven for managing dependencies and running tests. Ensure `mvn` is available in your system's PATH.

## Running Tests:
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd api-test-framework
   ```
2. Install dependencies and run tests:
   ```bash
   mvn clean test
   ```

## Configuration:
- **Database Configuration**: Configure database connections in `src/main/resources/config/database.properties`. Example:
  ```properties
  db.url=jdbc:mysql://localhost:3306/testdb
  db.password=password
  ```
- **API Configuration**: Configure API endpoints and other settings in `src/main/resources/config/api.properties`. Example:
  ```properties
  api.base.url=https://api.example.com
  api.timeout=5000
  ```

## Logging:
- Logs are managed using SLF4J and can be configured in `src/main/resources/log4j2.xml`. Example configuration:
  ```xml
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
  ```

## Adding New Tests:
1. **Create Feature Files**: Add new feature files in `src/test/resources/features`. Example:
   ```gherkin
   Feature: User API

   Scenario: Create a new user
       Given I have a valid user payload
       When I send a POST request to "/users"
       Then the response status should be 201
       And the response body should contain the user details
   ```
2. **Implement Step Definitions**: Implement step definitions in `src/test/java/steps`. Example:
   ```java
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
   ```
3. **Add Test Runners**: Add test runners in `src/test/java/runners`. Example:
   ```java
   @RunWith(Cucumber.class)
   @CucumberOptions(
       features = "src/test/resources/features",
       glue = "steps",
       plugin = {"pretty", "html:target/cucumber-reports"}
   )
   public class TestRunner {
   }
   ```

## Dependencies:
- Ensure all dependencies are listed in `pom.xml`. Example:
  ```xml
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
      <!-- Add other dependencies here -->
  </dependencies>
  ```

## Contributing:
- **Fork the Repository**: Fork the repository to your GitHub account.
- **Create a Branch**: Create a new branch for your feature or bugfix.
- **Submit a Pull Request**: Submit a pull request with a detailed description of your changes.

## License:
This project is licensed under the MIT License.