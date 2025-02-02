
package ui.pages;

import ui.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SignInPage class to provide interactions with the sign-in page.
 * This class uses WebDriver to interact with web elements and includes logging for actions.
 */
public class SignInPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(SignInPage.class);

    // Locators using Page Factory annotations
    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(id = "password")
    private WebElement passwordElement;

    @FindBy(id = "user-name")
    private WebElement userNameElement;

    @FindBy(xpath = "//h3[@data-test='error']")
    private WebElement loginErrorMessage;

    /**
     * Constructor to initialize the WebDriver and PageFactory.
     *
     * @param driver the WebDriver instance
     */
    public SignInPage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
        logger.info("Initialized SignInPage");
    }

    /**
     * Enters the username into the username field.
     *
     * @param userName the username to enter
     */
    public void enterUserName(String userName) {
        sendKeys(userNameElement, userName);
        logger.info("Entered username: {}", userName);
    }

    /**
     * Enters the password into the password field.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
    	sendKeys(passwordElement, password);
    }
    public void clickLoginButton() {
        click(loginButton);
        logger.info("Clicked login button");
    }

    /**
     * Performs login and returns the result.
     *
     * @param userName the username to enter
     * @param password the password to enter
     * @return the DashboardPage if login is successful, otherwise the error message
     */
    public DashBoardPage doValidLogin(String userName, String password) {
        enterUserName(userName);
        enterPassword(password);
        clickLoginButton();
        System.out.println("Login successful");
        return new DashBoardPage(driver);
    }

    /**
     * Validates invalid login by checking for the error message.
     *
     * @return true if login failed, false otherwise
     */
    public SignInPage doInvalidLogin(String userName, String password) {
        enterUserName(userName);
        enterPassword(password);
        clickLoginButton();
        System.out.println("Login failed");
        return new SignInPage(driver);
    }

    /**
     * Gets the login error message.
     *
     * @return the login error message
     */
    public String getLoginErrorMessage() {
        String errorMessage = loginErrorMessage.getText();
        logger.info("Login error message: {}", errorMessage);
        return errorMessage;
    }
}
