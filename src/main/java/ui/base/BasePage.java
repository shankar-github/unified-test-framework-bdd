package ui.base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // Interact with WebElement (send keys)
    public void sendKeys(WebElement element, String data) {
        if (element != null) {
            element.sendKeys(data);
            logger.info("Entered data: '" + data + "' into element.");
        } else {
            logger.error("Element not found to send keys.");
        }
    }

    // Interact with WebElement (click)
    public void click(WebElement element) {
        if (element != null) {
            element.click();
            logger.info("Clicked on the element.");
        } else {
            logger.error("Element not found to click.");
        }
    }

    // Select from Dropdown
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
                        logger.error("Invalid selection method: " + selectBy);
                }
                logger.info("Selected value '" + selectValue + "' from dropdown.");
            } else {
                logger.error("Dropdown element not found.");
            }
        } catch (Exception e) {
            logger.error("Failed to select value from dropdown.");
        }
    }

    // Wait for element to be clickable
    public WebElement waitForElement(WebElement element, Duration timeOutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            logger.error("Timed out waiting for element to be clickable.");
            return null;
        }
    }

    // Get the text from WebElement
    public String getElementText(WebElement element) {
        return (element != null) ? element.getText().trim() : "";
    }

    // Take a screenshot
    public void takeScreenshot(String testCaseName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File(System.getProperty("user.dir") + "/screenshots/" + testCaseName + ".png");
            FileUtils.copyFile(screenshot, dest);
            logger.info("Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to capture screenshot.", e);
        }
    }

    // Upload a file to a file input field
    public void uploadFile(String filePath, WebElement uploadElement) {
        if (uploadElement != null) {
            uploadElement.sendKeys(filePath);
            logger.info("File uploaded: " + filePath);
        } else {
            logger.error("Upload element not found.");
        }
    }
}
