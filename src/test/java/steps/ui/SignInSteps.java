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
    private DashBoardPage dashBoardPage;
    private String loginErrorMessage;

    public SignInSteps() {
        // Get the driver from DriverFactory (initialized in Hooks)
        this.driver = DriverFactory.getDriver();
    }

    @Given("the user navigates to the sign-in page")
    public void the_user_navigates_to_the_sign_in_page() {
        signInPage = new SignInPage(driver);
    }

    @When("the user enters valid credentials with username {string} and password {string}")
    
    public void the_user_logs_in_with_valid_username_and_password(String username, String password) {
        // Perform login with valid or invalid credentials
        dashBoardPage = signInPage.doValidLogin(username, password);
    }
    
   @When("the user enters invalid credentials with username {string} and password {string}")
    
    public void the_user_logs_in_with_invalid_username_and_password(String username, String password) {
        // Perform login with valid or invalid credentials
        signInPage = signInPage.doInvalidLogin(username, password);
    }

   @Then("the user should be redirected to the dashboard with message {string}")
   public void the_user_should_see_dashboard_text(String expectedText) {
       Assert.assertNotNull(dashBoardPage, "Dashboard page is null. Login might have failed.");
       Assert.assertTrue(dashBoardPage.getSwagText().contains(expectedText),
               "User is not redirected to the dashboard.");
   }


    @Then("the user should see an error message {string}")
    public void the_user_should_see_an_error_message(String expectedErrorMessage) {
        // Get the error message from the SignInPage
        loginErrorMessage = signInPage.getLoginErrorMessage();

        // Assert if the error message matches the expected one
        Assert.assertTrue(loginErrorMessage.equals(expectedErrorMessage),
                "Error message mismatch. Expected: " + expectedErrorMessage + " but got: " + loginErrorMessage);
    }
}
