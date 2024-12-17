package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static Properties props;

    // Static block for lazy initialization of the Properties object
    static {
        loadProperties();
    }

    // Private method to load properties from the config file
    private static synchronized void loadProperties() {
        if (props == null) {
            try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
                props = new Properties();
                props.load(input);
                logger.info("Configuration loaded successfully.");
            } catch (IOException e) {
                logger.error("Failed to load configuration file", e);
            }
        }
    }

    // Public method to get the property value by key
    public static String get(String key) {
        if (props == null) {
            logger.warn("Properties have not been loaded yet. Attempting to load.");
            loadProperties();  // Ensure properties are loaded if not already
        }
        return props != null ? props.getProperty(key) : null;
    }
}
