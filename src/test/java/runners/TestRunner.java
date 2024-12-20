package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",  // Path to feature files
    glue = {"steps"},                 // Path to step definitions and hooks
    plugin = {
        "pretty",                              // Console output
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure plugin for Cucumber 7
    },
    monochrome = true                         // Clean console output
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
