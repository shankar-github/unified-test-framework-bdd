package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.ConfigManager;

/**
 * The MongoDBConnector class is responsible for establishing and managing a connection to a MongoDB database.
 * It provides methods to retrieve the database instance and close the connection.
 * The connection is established lazily and only once, ensuring thread safety with double-checked locking.
 * This class ensures that resources are properly managed and that errors during connection or disconnection are logged.
 */
public class MongoDBConnector {

    // Logger for logging information and errors related to MongoDB connection
    private static final Logger logger = LogManager.getLogger(MongoDBConnector.class);
    
    // MongoDB client and database instances, using volatile for thread safety
    private static volatile MongoClient mongoClient;
    private static volatile MongoDatabase database;

    /**
     * Private constructor to prevent instantiation of this utility class.
     * This ensures that the class can only be accessed through static methods.
     */
    private MongoDBConnector() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the MongoDatabase instance.
     * This method ensures that the connection is established only once using double-checked locking.
     * If the database is not already connected, it initializes the connection and retrieves the database.
     * The MongoDB URI and database name are retrieved from the configuration manager.
     * 
     * @return The MongoDatabase instance.
     * @throws RuntimeException If the MongoDB connection fails.
     */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            synchronized (MongoDBConnector.class) {
                if (database == null) {
                    try {
                        // Retrieve MongoDB URI and database name from configuration
                        String uri = ConfigManager.get("mongodb.uri");
                        String dbName = ConfigManager.get("mongodb.database");

                        if (uri == null || uri.isEmpty() || dbName == null || dbName.isEmpty()) {
                            throw new IllegalStateException("MongoDB URI or Database name is not configured.");
                        }

                        mongoClient = MongoClients.create(uri);  // Create MongoClient with the URI
                        database = mongoClient.getDatabase(dbName);  // Get the database instance
                        logger.info("Connected to MongoDB successfully.");  // Log success
                    } catch (Exception e) {
                        logger.error("Failed to connect to MongoDB", e);  // Log error if connection fails
                        throw new RuntimeException("MongoDB connection failed", e);  // Throw runtime exception if connection fails
                    }
                }
            }
        }
        return database;  // Return the MongoDatabase instance
    }

    /**
     * Closes the MongoDB connection.
     * This method ensures that the MongoDB client is closed properly, freeing up resources.
     * If an error occurs during the closing process, it is logged.
     */
    public static void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();  // Close the MongoDB client
                logger.info("MongoDB connection closed.");  // Log closure success
            } catch (Exception e) {
                logger.error("Error occurred while closing MongoDB connection", e);  // Log any error during closure
            }
        }
    }

    // Add shutdown hook to close the connection on application exit
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            closeConnection();  // Ensure MongoDB connection is closed on application shutdown
        }));
    }
}
