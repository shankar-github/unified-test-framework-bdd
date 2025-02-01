package steps.ui;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import testrunners.DriverFactory;
import ui.pages.SignInPage;
import ui.pages.DashBoardPage;

public class SignInSteps {

    private WebDriver driver;
    private SignInPage signInPage;
    private Object loginResult;
    private DashBoardPage dashBoardPage;

    public SignInSteps() {
        // Get the driver from DriverFactory (initialized in Hooks)
        this.driver = DriverFactory.getDriver();
    }

    @Given("the user navigates to the sign-in page")
    public void the_user_navigates_to_the_sign_in_page() {
        signInPage = new SignInPage(driver);
    }

    @When("the user enters valid credentials with username {string} and password {string} for {string}")
    public void the_user_logs_in_with_username_and_password(String username, String password, String testCase) {
    	testCase = testCase.toLowerCase().trim();
    	if (testCase.equals("valid")) {
    		dashBoardPage = signInPage.doValidLogin(username, password);
    		}else {
    			signInPage.doInvalidLogin(username, password);
    		}
    }

    @Then("the user should be redirected to the dashboard")
    public void the_user_should_be_redirected_to_the_dashboard() {
		Assert.assertTrue(dashBoardPage.getSwagText().contains("Swag Labs"),
				"User is not redirected to the dashboard.");
    }

    @Then("the user should see an error message {string}")
    public void the_user_should_see_an_error_message(String expectedErrorMessage) {
		Assert.assertTrue(signInPage.getLoginErrorMessage().contains(expectedErrorMessage),
				"Error message is not displayed.");
        
       
    }
}
