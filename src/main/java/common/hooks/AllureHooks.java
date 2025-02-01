
package common.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import common.testrunners.DriverFactory;
import java.util.Collection;

/**
 * AllureHooks class to handle before and after scenario hooks for UI tests.
 * This class integrates with Allure for reporting and uses a logger for logging.
 */
public class AllureHooks {
    private WebDriver driver;
    private static final Logger logger = LogManager.getLogger(AllureHooks.class);

    /**
     * Method to be executed before each scenario.
     * Initializes the WebDriver if the scenario is a UI test.
     *
     * @param scenario the current scenario
     */
    @Before
    public void beforeScenario(Scenario scenario) {
        logScenarioDetails("Starting Test Case", scenario);
        logger.info("Starting scenario: {}", scenario.getName());

        if (isUITest(scenario)) {
            driver = DriverFactory.getDriver();
            logger.info("WebDriver initialized for scenario: {}", scenario.getName());
        }
    }

    /**
     * Method to be executed after each scenario.
     * Logs the scenario status and attaches a screenshot if the scenario failed.
     * Quits the WebDriver if it was initialized.
     *
     * @param scenario the current scenario
     */
    @After
    public void afterScenario(Scenario scenario) {
        logScenarioStatus(scenario);
        logger.info("Finished scenario: {}", scenario.getName());

        if (scenario.isFailed() && isUITest(scenario)) {
            attachScreenshotToReport();
            logger.error("Scenario failed: {}. Screenshot attached.", scenario.getName());
        }

        if (isUITest(scenario)) {
            DriverFactory.quitDriver();
            logger.info("WebDriver quit for scenario: {}", scenario.getName());
        }
    }

    /**
     * Checks if the scenario is a UI test based on its tags.
     *
     * @param scenario the current scenario
     * @return true if the scenario is a UI test, false otherwise
     */
    private boolean isUITest(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        return tags.stream().anyMatch(tag -> tag.equalsIgnoreCase("@UI"));
    }

    /**
     * Logs the details of the scenario.
     *
     * @param message  the message to log
     * @param scenario the current scenario
     */
    private void logScenarioDetails(String message, Scenario scenario) {
        attachStepToReport(message + ": " + scenario.getName());
        attachStepToReport("Test Tags: " + scenario.getSourceTagNames());
        logger.info("{}: {}", message, scenario.getName());
    }

    /**
     * Logs the status of the scenario.
     *
     * @param scenario the current scenario
     */
    @Step("Test Case: {0} - Status: {1}")
    private void logScenarioStatus(Scenario scenario) {
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        attachStepToReport(scenario.getName() + " - Status: " + status);
        logger.info("Scenario {} - Status: {}", scenario.getName(), status);
    }

    /**
     * Attaches a screenshot to the Allure report if the scenario failed.
     *
     * @return the screenshot as a byte array
     */
    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotToReport() {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Attaches a step to the Allure report.
     *
     * @param message the message to attach
     * @return the message
     */
    @Attachment(value = "{0}", type = "text/plain")
    public String attachStepToReport(String message) {
        return message;
    }
}
