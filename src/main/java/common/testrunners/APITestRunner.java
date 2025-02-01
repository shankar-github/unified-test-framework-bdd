package common.testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CucumberOptions(
    features = "src/test/resources/features",  // Path to feature files
    glue = {"steps","hooks"},                 // Path to step definitions and hooks
    plugin = {
        "pretty",                              // Console output
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure plugin for Cucumber 7
    },
    monochrome = true                         // Clean console output
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LogManager.getLogger(APITestRunner.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        try {
            logger.info("Fetching scenarios for parallel execution");
            return super.scenarios();
        } catch (Exception e) {
            logger.error("Error fetching scenarios: {}", e.getMessage(), e);
            throw e;
        }
    }
}