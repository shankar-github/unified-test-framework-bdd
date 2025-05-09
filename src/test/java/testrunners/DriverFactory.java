package testrunners;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;

import common.config.ConfigManager;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DriverFactory class to manage WebDriver instances.
 * This class initializes WebDriver based on the browser specified in the configuration file.
 * It also handles the setup and teardown of WebDriver instances.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static String baseURL = ConfigManager.get("baseURL");
    private static String browser = ConfigManager.get("browser");

    // Initialize Logger
    private static final Logger logger = LogManager.getLogger(DriverFactory.class);

    /**
     * Gets the WebDriver instance for the current thread.
     * Initializes the WebDriver if it is not already initialized.
     *
     * @return the WebDriver instance
     */
    public static WebDriver getDriver() {
        // Validate configuration
        validateConfiguration();

        if (driverThreadLocal.get() == null) {
            logger.info("Initializing driver...");
            driverThreadLocal.set(createDriver(browser, baseURL));  // Initialize driver based on browser
        }
        return driverThreadLocal.get();
    }

    /**
     * Validates the configuration to ensure necessary values are provided.
     */
    private static void validateConfiguration() {
        if (browser == null || browser.isEmpty()) {
            throw new IllegalArgumentException("Browser configuration is missing or empty.");
        }

        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("Base URL configuration is missing or empty.");
        }
    }

    /**
     * Creates a WebDriver instance based on the specified browser and baseURL.
     *
     * @param browser the browser to use (chrome, firefox, edge, safari)
     * @param baseURL the base URL to navigate to
     * @return the WebDriver instance
     */
    private static WebDriver createDriver(String browser, String baseURL) {
        WebDriver driver;
        String os = System.getProperty("os.name").toLowerCase(); // Get OS Name

        logger.info("Creating driver for browser: " + browser);

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized"); // Open in maximized mode
                chromeOptions.addArguments("--disable-notifications"); // Disable notifications
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--width=1920", "--height=1080"); // Set window size
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                if (!os.contains("win")) { // Edge is only supported on Windows officially
                    throw new UnsupportedOperationException("Edge browser is only supported on Windows.");
                }
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;

            case "safari":
                if (!os.contains("mac")) { // Safari only runs on macOS
                    throw new UnsupportedOperationException("Safari is only supported on macOS.");
                }
                driver = new SafariDriver();
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseURL);
        logger.info("Driver initialized and navigated to base URL: " + baseURL);
        return driver;
    }

    /**
     * Quits the WebDriver instance for the current thread.
     * Removes the WebDriver instance from the ThreadLocal storage.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting driver...");
            driver.quit();
            driverThreadLocal.remove();
        } else {
            logger.warn("Driver is already null. Nothing to quit.");
        }
    }
}
