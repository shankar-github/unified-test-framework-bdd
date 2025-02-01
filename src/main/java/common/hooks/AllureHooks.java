package common.hooks;

import common.utils.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Collection;

public class AllureHooks {
    private WebDriver driver;

    @Before
    public void beforeScenario(Scenario scenario) {
        logScenarioDetails("Starting Test Case", scenario);

        if (isUITest(scenario)) {
            String browser = System.getProperty("browser", "chrome"); // Get browser from system property, default to Chrome
            driver = DriverFactory.getDriver(browser);
        }
    }

    @After
    public void afterScenario(Scenario scenario) {
        logScenarioStatus(scenario);

        if (scenario.isFailed() && isUITest(scenario)) {
            attachScreenshotToReport();
        }

        // Quit WebDriver only if it was initialized
        if (isUITest(scenario)) {
            DriverFactory.quitDriver();
        }
    }

    private boolean isUITest(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames(); // Fixed type mismatch
        return tags.stream().anyMatch(tag -> tag.equalsIgnoreCase("@UI"));
    }

    private void logScenarioDetails(String message, Scenario scenario) {
        attachStepToReport(message + ": " + scenario.getName());
        attachStepToReport("Test Tags: " + scenario.getSourceTagNames());
    }

    @Step("Test Case: {0} - Status: {1}")
    private void logScenarioStatus(Scenario scenario) {
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        attachStepToReport(scenario.getName() + " - Status: " + status);
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotToReport() {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String attachStepToReport(String message) {
        return message;
    }
}
