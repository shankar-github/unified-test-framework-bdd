package common.testrunners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CucumberOptions(
        features = "src/test/resources/features/ui",  // Path to UI feature files
        glue = {"steps.ui", "hooks"},                // UI step definitions and hooks
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure plugin for Cucumber 7
        },
        monochrome = true
)
public class UITestRunner extends AbstractTestNGCucumberTests {

    private static final Logger logger = LogManager.getLogger(UITestRunner.class);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        try {
            logger.info("Fetching UI scenarios for parallel execution");
            return super.scenarios();
        } catch (Exception e) {
            logger.error("Error fetching UI scenarios: {}", e.getMessage(), e);
            throw e;
        }
    }
}
