# API Test Automation Framework

This framework provides production-level API test automation capabilities.

## Features:
- Modular design
- Logging with SLF4J
- RestAssured for API Testing
- TestNG for Test Execution
- Database Connections (MySQL, MongoDB)
- Reusable Utilities

## Directory Structure:
```
api-test-framework/
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── base
│   │   │   ├── config
│   │   │   ├── database
│   │   │   ├── helpers
│   │   │   └── api
│   │   └── resources
│   │       └── test-data
│   └── test
│       ├── java
│       │   ├── runners
│       │   └── tests
│       └── resources
│           └── features
```

## Running Tests:
1. Install Maven.
2. Run:
   ```bash
   mvn clean test
   ```
