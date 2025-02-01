
package listeners;

import io.qameta.allure.Attachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import testrunners.DriverFactory;

import java.util.Arrays;

/**
 * TestNGListener class to handle TestNG test events.
 * This class integrates with Allure for reporting and uses a logger for logging.
 */
public class TestNGListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestNGListener.class);

    /**
     * Method to be executed when a test starts.
     *
     * @param result the result of the test method that is starting
     */
    @Override
    public void onTestStart(ITestResult result) {
        Reporter.log("Starting Test: " + result.getMethod().getMethodName(), true);
        logger.info("Starting Test: {}", result.getMethod().getMethodName());
        attachTextToReport("Test Start", "Test Started: " + result.getMethod().getMethodName());
    }

    /**
     * Method to be executed when a test succeeds.
     *
     * @param result the result of the test method that succeeded
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.log("Test Passed: " + result.getMethod().getMethodName(), true);
        logger.info("Test Passed: {}", result.getMethod().getMethodName());
        attachTextToReport("Test Passed", "Test Passed: " + result.getMethod().getMethodName());
    }

    /**
     * Method to be executed when a test fails.
     *
     * @param result the result of the test method that failed
     */
    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.log("Test Failed: " + result.getMethod().getMethodName(), true);
        logger.error("Test Failed: {}", result.getMethod().getMethodName());

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            attachTextToReport("Failure Reason", throwable.getMessage());
            logger.error("Failure Reason: {}", throwable.getMessage());
        }

        // Capture Screenshot only for UI tests
        if (isUITest(result)) {
            WebDriver driver = DriverFactory.getDriver();
            if (driver != null) {
                attachScreenshotToReport(driver);
                logger.info("Screenshot attached for test: {}", result.getMethod().getMethodName());
            }
        }
    }

    /**
     * Method to be executed when a test is skipped.
     *
     * @param result the result of the test method that was skipped
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        Reporter.log("Test Skipped: " + result.getMethod().getMethodName(), true);
        logger.warn("Test Skipped: {}", result.getMethod().getMethodName());
        attachTextToReport("Test Skipped", "Test Skipped: " + result.getMethod().getMethodName());
    }

    /**
     * Method to be executed when a test context starts.
     *
     * @param context the test context that is starting
     */
    @Override
    public void onStart(ITestContext context) {
        Reporter.log("Test Suite Started: " + context.getName(), true);
        logger.info("Test Suite Started: {}", context.getName());
    }

    /**
     * Method to be executed when a test context finishes.
     *
     * @param context the test context that is finishing
     */
    @Override
    public void onFinish(ITestContext context) {
        Reporter.log("Test Suite Finished: " + context.getName(), true);
        logger.info("Test Suite Finished: {}", context.getName());
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String attachTextToReport(String name, String content) {
        return content;
    }

    @Attachment(value = "Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshotToReport(WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        return new byte[0];
    }

    /**
     * Helper method to check if the test is a UI test based on its tags.
     *
     * @param result the result of the test method
     * @return true if the test is a UI test, false otherwise
     */
    private boolean isUITest(ITestResult result) {
        return Arrays.asList(result.getMethod().getGroups()).contains("UI");
    }
}
