package database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import config.ConfigManager;

public class MongoDBConnector {

    private static final Logger logger = LogManager.getLogger(MongoDBConnector.class);
    private static volatile MongoClient mongoClient;
    private static volatile MongoDatabase database;

    private MongoDBConnector() {
        // Private constructor to prevent instantiation
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            synchronized (MongoDBConnector.class) {
                if (database == null) {
                    try {
                        // Retrieve MongoDB URI and database name from configuration
                        String uri = ConfigManager.get("mongodb.uri");
                        mongoClient = MongoClients.create(uri);
                        String dbName = ConfigManager.get("mongodb.database");
                        database = mongoClient.getDatabase(dbName);
                        logger.info("Connected to MongoDB successfully.");
                    } catch (Exception e) {
                        logger.error("Failed to connect to MongoDB", e);
                        throw new RuntimeException("MongoDB connection failed", e);
                    }
                }
            }
        }
        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            try {
                mongoClient.close();  // Close the MongoDB client
                logger.info("MongoDB connection closed.");
            } catch (Exception e) {
                logger.error("Error occurred while closing MongoDB connection", e);
            }
        }
    }
}
