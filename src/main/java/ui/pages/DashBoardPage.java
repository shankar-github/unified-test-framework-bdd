
package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.base.BasePage;

/**
 * DashBoardPage class to provide interactions with the dashboard page.
 * This class uses WebDriver to interact with web elements and includes logging for actions.
 */
public class DashBoardPage extends BasePage {

    private static final Logger logger = LoggerFactory.getLogger(DashBoardPage.class);
    
    @FindBy(className = "app_logo")
    private WebElement appLogo;

    
    /**
     * Constructor to initialize the WebDriver and PageFactory.
     *
     * @param driver the WebDriver instance
     */
    public DashBoardPage(WebDriver driver) {
        super(driver);  // Call the constructor of BasePage
        PageFactory.initElements(driver, this);  // Initialize Page Factory elements
        logger.info("Initialized DashBoardPage");
    }

    // Add methods for interactions with the dashboard page here
    // Example method:
    /**
     * Example method to demonstrate logging.
     */
    
    public String getSwagText() {
        logger.info("Executing example method in DashBoardPage");
        return getElementText(appLogo);
    }
}
