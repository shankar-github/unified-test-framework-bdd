
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.ConfigManager;

/**
 * The MSSQLConnector class is responsible for establishing and managing a connection to a Microsoft SQL Server database.
 * It provides methods to retrieve the connection instance and close the connection.
 * The connection is established lazily and only once, ensuring thread safety with double-checked locking.
 * The class also loads the MSSQL JDBC driver during class initialization to ensure the driver is available before any connection attempts.
 */
public class MSSQLConnector {

    // Logger for logging information and errors related to MSSQL connection
    private static final Logger logger = LogManager.getLogger(MSSQLConnector.class);

    // MSSQL connection instance, using volatile for thread safety
    private static volatile Connection connection;

    // Static block to load the MSSQL JDBC driver when the class is loaded
    static {
        try {
            // Load MSSQL JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            logger.error("MSSQL JDBC Driver not found", e);  // Log error if JDBC driver is not found
        }
    }

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This ensures that the class can only be accessed through static methods.
     */
    private MSSQLConnector() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the MSSQL connection instance.
     * This method ensures that the connection is established only once using double-checked locking.
     * If the connection is not already established, it initializes the connection using the JDBC DriverManager.
     * The MSSQL connection details (URL, username, and password) are retrieved from the configuration manager.
     *
     * @return The Connection instance.
     * @throws RuntimeException If the MSSQL connection fails.
     */
    public static Connection getConnection() {
        if (connection == null) {
            synchronized (MSSQLConnector.class) {
                if (connection == null) {
                    initializeConnection();
                }
            }
        }
        return connection;
    }

    /**
     * Initializes the MSSQL connection using the JDBC DriverManager.
     * This method is synchronized to ensure thread safety during initialization.
     */
    private static synchronized void initializeConnection() {
        try {
            // Retrieve MSSQL connection details from configuration
            String url = ConfigManager.get("mssql.url");
            String username = ConfigManager.get("mssql.username");
            String password = ConfigManager.get("mssql.password");

            // Validate configuration values
            if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty()) {
                throw new IllegalStateException("MSSQL connection details are not properly configured.");
            }

            // Establish the connection using JDBC
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.error("Failed to connect to MSSQL database", e);  // Log error if connection fails
            throw new RuntimeException("MSSQL connection failed", e);  // Throw runtime exception if connection fails
        }
    }

    /**
     * Closes the MSSQL connection.
     * This method ensures that the MSSQL connection is properly closed, freeing up resources.
     * If the connection is already closed or an error occurs during closure, it is logged.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                // Check if the connection is still open before attempting to close
                if (!connection.isClosed()) {
                    connection.close();  // Close the connection
                    logger.info("MSSQL connection closed.");  // Log closure success
                }
            } catch (SQLException e) {
                logger.error("Failed to close MSSQL connection", e);  // Log error if closing the connection fails
            }
        }
    }

    // Add shutdown hook to close the connection on application exit
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeConnection();  // Ensure MSSQL connection is closed on application shutdown
        }));
    }
}
