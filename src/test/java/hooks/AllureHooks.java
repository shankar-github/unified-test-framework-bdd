package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllureHooks {

    @Before
    public void beforeScenario(Scenario scenario) {
        logScenarioDetails("Starting Test Case", scenario);

        // Capture and log test data (from DataTable if applicable)
        Map<String, String> testData = scenario.getSourceTagNames().stream()
                                              .collect(Collectors.toMap(s -> s, s -> s));

        Optional.ofNullable(testData)
                .filter(data -> !data.isEmpty())
                .ifPresent(data -> {
                    attachTextToReport("Test Data", data.toString());
                });
    }

    @After
    public void afterScenario(Scenario scenario) {
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        logScenarioStatus(scenario.getName(), status);

        // If the test failed, log the reason for failure
        if (scenario.isFailed()) {
            String errorMessage = scenario.getStatus().name();
            attachTextToReport("Failure Reason", errorMessage);
        }
    }

    private void logScenarioDetails(String message, Scenario scenario) {
        // Log details of the scenario directly to Allure
        attachStepToReport(message + ": " + scenario.getName());
        attachStepToReport("Test Tags: " + scenario.getSourceTagNames());
    }

    @Step("Log Scenario Status: {0} - Status: {1}")
    public void logScenarioStatus(String scenarioName, String status) {
        attachStepToReport("Test Case: " + scenarioName + " - Status: " + status);
    }

    @Step("Log Failure Reason: {0}")
    public void logFailureReason(String reason) {
        attachStepToReport("Failure Reason: " + reason);
    }

    @Step("{0}")
    public void attachStepToReport(String message) {
        // This is a helper method to add the message as an Allure step
        attachTextToReport("Step", message);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String attachTextToReport(String name, String content) {
        // This will attach the test data to the Allure report
        return content;
    }
}
