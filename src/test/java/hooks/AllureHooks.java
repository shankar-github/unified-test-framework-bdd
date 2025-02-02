
package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import testrunners.DriverFactory;

import org.apache.commons.io.output.WriterOutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class AllureHooks {

    private static final ThreadLocal<StringWriter> requestWriter = new ThreadLocal<>();
    private static final ThreadLocal<StringWriter> responseWriter = new ThreadLocal<>();
    private static final ThreadLocal<Response> apiResponse = new ThreadLocal<>();
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<String> testData = new ThreadLocal<>();

    @Before
    public void beforeScenario(Scenario scenario) {
        requestWriter.set(new StringWriter());
        responseWriter.set(new StringWriter());

        // Automatically log API requests/responses
        RestAssured.filters(
                new RequestLoggingFilter(new PrintStream(new WriterOutputStream(requestWriter.get(), StandardCharsets.UTF_8))),
                new ResponseLoggingFilter(new PrintStream(new WriterOutputStream(responseWriter.get(), StandardCharsets.UTF_8)))
        );

        // Initialize WebDriver for UI tests
        if (scenario.getSourceTagNames().contains("@UI")) {
            driver.set(DriverFactory.getDriver());
        }

        // Capture test data dynamically from the scenario's step arguments
        captureTestData(scenario);
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@api")) {
            attachApiDetails();
        }

        if (scenario.getSourceTagNames().contains("@UI")) {
            attachUiDetails(scenario);
        }

        // Cleanup WebDriver after UI test
        if (driver.get() != null) {
            driver.get().quit();
        }
    }

    // Capture test data dynamically from the scenario's step arguments
    private void captureTestData(Scenario scenario) {
        StringBuilder testDataBuilder = new StringBuilder("Test Data:\n");

        // Capture scenario tags (like @UI or @api)
        scenario.getSourceTagNames().forEach(tag -> testDataBuilder.append(tag).append("\n"));

        // Save this collected data to a ThreadLocal variable for later use
        testData.set(testDataBuilder.toString());
    }

    // Attach API request & response details
    private void attachApiDetails() {
        Allure.addAttachment("API Request", requestWriter.get().toString());
        Allure.addAttachment("API Response", responseWriter.get().toString());
    }

    // Attach UI test details & failure screenshot
    private void attachUiDetails(Scenario scenario) {
        // Attach the test data (captured from the scenario's steps)
        if (testData.get() != null) {
            Allure.addAttachment("Test Data", testData.get());
        }

        // Capture and attach screenshot on failure
        if (scenario.isFailed() && driver.get() != null) {
            byte[] screenshot = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("UI Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), "png");
        }
    }
}
