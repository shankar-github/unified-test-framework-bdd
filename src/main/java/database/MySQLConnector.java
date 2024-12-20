package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.ConfigManager;

/**
 * The MySQLConnector class is responsible for establishing and managing a connection to a MySQL database.
 * It provides methods to retrieve the connection instance and close the connection.
 * The connection is established lazily and only once, ensuring thread safety with double-checked locking.
 * The class also loads the MySQL JDBC driver during class initialization to ensure the driver is available before any connection attempts.
 */
public class MySQLConnector {

    // Logger for logging information and errors related to MySQL connection
    private static final Logger logger = LogManager.getLogger(MySQLConnector.class);

    // MySQL connection instance, using volatile for thread safety
    private static volatile Connection connection;

    // Static block to load the MySQL JDBC driver when the class is loaded
    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC Driver not found", e); // Log error if JDBC driver is not found
        }
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This ensures that the class can only be accessed through static methods.
     */
    private MySQLConnector() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the MySQL connection instance.
     * This method ensures that the connection is established only once using double-checked locking.
     * If the connection is not already established, it initializes the connection using the JDBC DriverManager.
     * The MySQL connection details (URL, username, and password) are retrieved from the configuration manager.
     *
     * @return The Connection instance.
     * @throws RuntimeException If the MySQL connection fails.
     */
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (MySQLConnector.class) {
                if (connection == null) {
                    try {
                        // Retrieve MySQL connection details from configuration
                        String url = ConfigManager.get("mysql.url");
                        String username = ConfigManager.get("mysql.username");
                        String password = ConfigManager.get("mysql.password");

                        // Validate configuration values
                        if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty()) {
                            throw new IllegalStateException("MySQL connection details are not properly configured.");
                        }

                        // Establish the connection using JDBC
                        connection = DriverManager.getConnection(url, username, password);
                        logger.info("Connected to MySQL database successfully."); // Log success
                    } catch (SQLException e) {
                        logger.error("Failed to connect to MySQL database", e); // Log error if connection fails
                        throw new RuntimeException("MySQL connection failed", e); // Throw runtime exception if connection fails
                    }
                }
            }
        }
        return connection; // Return the established connection
    }

    /**
     * Closes the MySQL connection.
     * This method ensures that the MySQL connection is properly closed, freeing up resources.
     * If the connection is already closed or an error occurs during closure, it is logged.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                // Check if the connection is still open before attempting to close
                if (!connection.isClosed()) {
                    connection.close(); // Close the connection
                    logger.info("MySQL connection closed."); // Log closure success
                }
            } catch (SQLException e) {
                logger.error("Failed to close MySQL connection", e); // Log error if closing the connection fails
            }
        }
    }

    // Add shutdown hook to ensure the MySQL connection is closed properly when the application exits
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeConnection(); // Ensure MySQL connection is closed on application shutdown
        }));
    }
}
