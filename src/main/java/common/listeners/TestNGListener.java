package listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import utils.WebDriverManager; // Ensure WebDriverManager is implemented

public class TestNGListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Reporter.log("Starting Test: " + result.getMethod().getMethodName(), true);
        attachTextToReport("Test Start", "Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.log("Test Passed: " + result.getMethod().getMethodName(), true);
        attachTextToReport("Test Passed", "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.log("Test Failed: " + result.getMethod().getMethodName(), true);

        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            attachTextToReport("Failure Reason", throwable.getMessage());
        }

        // Capture Screenshot only for UI tests
        WebDriver driver = WebDriverManager.getDriver();
        if (driver != null) {
            attachScreenshotToReport(driver);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Reporter.log("Test Skipped: " + result.getMethod().getMethodName(), true);
        attachTextToReport("Test Skipped", "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        Reporter.log("Test Suite Started: " + context.getName(), true);
    }

    @Override
    public void onFinish(ITestContext context) {
        Reporter.log("Test Suite Finished: " + context.getName(), true);
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
}
