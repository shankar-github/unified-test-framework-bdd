package common.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;

public class DriverFactory {
	
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver(String browser) {
        if (driverThreadLocal.get() == null) {
            driverThreadLocal.set(createDriver(browser));
        }
        return driverThreadLocal.get();
    }

    private static WebDriver createDriver(String browser) {
        WebDriver driver;
        String os = System.getProperty("os.name").toLowerCase(); // Get OS Name

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
        return driver;
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
