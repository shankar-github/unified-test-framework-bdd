package ui.steps;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import common.testrunners.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;

import ui.pages.SignInPage;
import ui.pages.DashBoardPage;

public class SignInSteps {

    private WebDriver driver;
    private SignInPage signInPage;
    private Object loginResult;

    @Before
    public void setUp() {
        // Initialize the driver using DriverFactory
        driver = DriverFactory.getDriver();
    }

    @Given("the user navigates to the sign-in page")
    public void the_user_navigates_to_the_sign_in_page() {
        // Initialize the SignInPage with the WebDriver
        signInPage = new SignInPage(driver);
    }

    @When("the user enters valid credentials with username {string} and password {string}")
    public void the_user_enters_valid_credentials(String username, String password) {
        // Perform the login with valid credentials
        loginResult = signInPage.doLogin(username, password);
    }

    @When("the user enters invalid credentials with username {string} and password {string}")
    public void the_user_enters_invalid_credentials(String username, String password) {
        // Perform the login with invalid credentials
        loginResult = signInPage.doLogin(username, password);
    }

    @Then("the user should be redirected to the dashboard")
    public void the_user_should_be_redirected_to_the_dashboard() {
        // Verify that the login was successful and the user is redirected to the dashboard
        Assert.assertEquals("Login failed, dashboard not reached.", loginResult instanceof DashBoardPage);
    }

    @Then("the user should see an error message {string}")
    public void the_user_should_see_an_error_message(String expectedErrorMessage) {
        // Verify that the correct error message is displayed
        Assert.assertEquals("Error message not displayed", loginResult instanceof String);
        String errorMessage = (String) loginResult;
        Assert.assertEquals("Error message mismatch.", expectedErrorMessage, errorMessage);
    }

    // Optionally, you can also include a @After hook to quit the driver after each scenario
}
