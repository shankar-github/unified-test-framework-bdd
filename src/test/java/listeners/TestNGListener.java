
package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestNGListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        Reporter.log("Starting Test: " + result.getMethod().getMethodName(), true);
        Reporter.log("Start Time: " + result.getStartMillis(), true);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Reporter.log("Test Passed: " + result.getMethod().getMethodName(), true);
        Reporter.log("End Time: " + result.getEndMillis(), true);
        Reporter.log("Duration: " + (result.getEndMillis() - result.getStartMillis()) + " ms", true);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Reporter.log("Test Failed: " + result.getMethod().getMethodName(), true);
        Reporter.log("End Time: " + result.getEndMillis(), true);
        Reporter.log("Duration: " + (result.getEndMillis() - result.getStartMillis()) + " ms", true);
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            Reporter.log("Failure Reason: " + throwable.getMessage(), true);
        } else {
            Reporter.log("Failure Reason: Unknown", true);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Reporter.log("Test Skipped: " + result.getMethod().getMethodName(), true);
        Reporter.log("End Time: " + result.getEndMillis(), true);
    }

    @Override
    public void onStart(ITestContext context) {
        Reporter.log("Test Suite Started: " + context.getName(), true);
    }

    @Override
    public void onFinish(ITestContext context) {
        Reporter.log("Test Suite Finished: " + context.getName(), true);
    }
}
