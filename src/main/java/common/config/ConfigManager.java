
package common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The ConfigManager class is responsible for loading and managing the application's configuration properties.
 * It loads properties from a configuration file located at "src/main/resources/config.properties".
 * The class utilizes lazy loading to load properties only when they are needed.
 * It also ensures thread safety while loading the properties.
 * This class provides a method to retrieve the configuration values by key.
 */
public class ConfigManager {

    // Logger for logging information, warnings, and errors related to configuration loading
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);

    // Property object that holds the loaded configuration properties
    private static volatile Properties props;

    /**
     * Static block for lazy initialization of the Properties object.
     * This is executed when the class is loaded to ensure properties are loaded before use.
     * The properties are loaded by calling the loadProperties method.
     */
    static {
        loadProperties(); // Load properties as soon as the class is loaded
    }

    /**
     * Loads properties from the config file.
     * This method is synchronized to ensure thread safety during property loading.
     * It will read the properties from the classpath resource "config.properties".
     *
     * If the properties file is loaded successfully, an info log is generated.
     * If an error occurs during loading, an error log is generated.
     *
     * @throws RuntimeException if the properties file cannot be loaded.
     */
    private static synchronized void loadProperties() {
        if (props == null) {
            try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("Configuration file not found");
                }
                Properties tempProps = new Properties();
                tempProps.load(input);  // Load properties from the classpath
                props = tempProps;
                logger.info("Configuration loaded successfully.");
            } catch (IOException e) {
                logger.error("Failed to load configuration file", e);
                throw new RuntimeException("Configuration loading failed", e);  // Throw exception for critical failure
            }
        }
    }

    /**
     * Retrieves the value of a configuration property by its key.
     * If the properties are not loaded yet, it will load them before fetching the value.
     *
     * @param key The key for the configuration property.
     * @return The value of the configuration property, or null if the key doesn't exist.
     *         If the properties file is not loaded, it will load the properties first before retrieving the value.
     */
    public static String get(String key) {
        ensurePropertiesLoaded();
        return props != null ? props.getProperty(key) : null;
    }

    /**
     * Retrieves the value of a configuration property by its key, with a default value if the key is not found.
     *
     * @param key The key for the configuration property.
     * @param defaultValue The value to return if the key doesn't exist.
     * @return The value of the configuration property, or the default value if the key doesn't exist.
     */
    public static String get(String key, String defaultValue) {
        ensurePropertiesLoaded();
        return props != null ? props.getProperty(key, defaultValue) : defaultValue;
    }

    /**
     * Ensures that properties are loaded.
     */
    private static void ensurePropertiesLoaded() {
        if (props == null) {
            logger.info("Properties have not been loaded yet. Loading now.");
            loadProperties();  // Ensure properties are loaded if not already
        }
    }

    /**
     * Optional method to reload properties in case of changes.
     */
    public static synchronized void reloadProperties() {
        logger.info("Reloading configuration properties.");
        props = null;
        loadProperties();  // Reload properties
    }
}
