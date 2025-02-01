package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllureHooks {

    private WebDriver driver; // UI Testing WebDriver

    @Before
    public void beforeScenario(Scenario scenario) {
        logScenarioDetails("Starting Test Case", scenario);

        // Capture and log test data (from DataTable if applicable)
        Map<String, String> testData = scenario.getSourceTagNames().stream()
                .collect(Collectors.toMap(s -> s, s -> s));

        Optional.ofNullable(testData)
                .filter(data -> !data.isEmpty())
                .ifPresent(data -> attachTextToReport("Test Data", data.toString()));

        // Initialize WebDriver only if it's a UI Test
        if (isUITest(scenario)) {
            driver = WebDriverManager.getDriver(); // Assuming WebDriverManager is handling driver setup
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        logScenarioStatus(scenario.getName(), status);

        if (scenario.isFailed()) {
            String errorMessage = scenario.getStatus().name();
            attachTextToReport("Failure Reason", errorMessage);

            if (isUITest(scenario)) {
                attachScreenshotToReport(); // Capture UI screenshot if failed
            }
        }

        // Quit WebDriver only for UI tests
        if (driver != null) {
            driver.quit();
        }
    }

    private void logScenarioDetails(String message, Scenario scenario) {
        attachStepToReport(message + ": " + scenario.getName());
        attachStepToReport("Test Tags: " + scenario.getSourceTagNames());
    }

    @Step("Log Scenario Status: {0} - Status: {1}")
    public void logScenarioStatus(String scenarioName, String status) {
        attachStepToReport("Test Case: " + scenarioName + " - Status: " + status);
    }

    @Step("{0}")
    public void attachStepToReport(String message) {
        attachTextToReport("Step", message);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String attachTextToReport(String name, String content) {
        return content;
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotToReport() {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    private boolean isUITest(Scenario scenario) {
        return scenario.getSourceTagNames().stream().anyMatch(tag -> tag.equalsIgnoreCase("@UI"));
    }
}
