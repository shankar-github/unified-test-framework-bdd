
package ui.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

/**
 * BasePage class to provide common web page interactions.
 * This class uses WebDriver to interact with web elements and includes logging for actions.
 */
public class BasePage {

    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    private static final int TIMEOUT = 10;

    /**
     * Constructor to initialize the WebDriver and PageFactory.
     *
     * @param driver the WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        logger.info("Initialized BasePage with timeout of {} seconds", TIMEOUT);
    }

    /**
     * Sends keys to a web element.
     *
     * @param element the web element
     * @param data the data to send
     */
    public void sendKeys(WebElement element, String data) {
        if (element != null) {
            element.sendKeys(data);
            logger.info("Entered data: '{}' into element.", data);
        } else {
            logger.error("Element not found to send keys.");
        }
    }

    /**
     * Clicks on a web element.
     *
     * @param element the web element
     */
    public void click(WebElement element) {
        if (element != null) {
            element.click();
            logger.info("Clicked on the element.");
        } else {
            logger.error("Element not found to click.");
        }
    }

    /**
     * Selects a value from a dropdown.
     *
     * @param element the dropdown element
     * @param selectValue the value to select
     * @param selectBy the method to select by (val, index, text)
     */
    public void selectFromDropDown(WebElement element, String selectValue, String selectBy) {
        try {
            if (element != null) {
                Select select = new Select(element);
                switch (selectBy.toLowerCase()) {
                    case "val":
                        select.selectByValue(selectValue);
                        break;
                    case "index":
                        select.selectByIndex(Integer.parseInt(selectValue));
                        break;
                    case "text":
                        select.selectByVisibleText(selectValue);
                        break;
                    default:
                        logger.error("Invalid selection method: {}", selectBy);
                }
                logger.info("Selected value '{}' from dropdown.", selectValue);
            } else {
                logger.error("Dropdown element not found.");
            }
        } catch (Exception e) {
            logger.error("Failed to select value from dropdown.", e);
        }
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param element the web element
     * @param timeOutInSeconds the timeout duration
     * @return the clickable web element
     */
    public WebElement waitForElement(WebElement element, Duration timeOutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for element to be clickable.", e);
            return null;
        }
    }

    /**
     * Gets the text from a web element.
     *
     * @param element the web element
     * @return the text of the element
     */
    public String getElementText(WebElement element) {
        return (element != null) ? element.getText().trim() : "";
    }

    /**
     * Takes a screenshot of the current page.
     *
     * @param testCaseName the name of the test case
     */
    public void takeScreenshot(String testCaseName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir") + "/screenshots/" + testCaseName + ".png");
            FileUtils.copyFile(screenshot, dest);
            logger.info("Screenshot saved: {}", dest.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot.", e);
        }
    }

    /**
     * Uploads a file to a file input field.
     *
     * @param filePath the path of the file to upload
     * @param uploadElement the file input element
     */
    public void uploadFile(String filePath, WebElement uploadElement) {
        if (uploadElement != null) {
            uploadElement.sendKeys(filePath);
            logger.info("File uploaded: {}", filePath);
        } else {
            logger.error("Upload element not found.");
        }
    }
}
