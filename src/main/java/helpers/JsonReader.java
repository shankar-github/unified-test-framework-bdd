
package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Reads data from a JSON file using Jackson.
 * The file path is dynamically built using the ConfigManager to get the base directory and the file name passed by the Step Definitions (SDs).
 */
public class JsonReader {

    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, String> readJson(String fileName) throws IOException {
        // Get the base directory path for the test data folder
        String basePath = ConfigManager.get("testDataFolderLocation");
        if (basePath == null || basePath.isEmpty()) {
            String errorMsg = "Base directory for test data folder is not defined in config.properties.";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }

        // Append the file name to the base path to form the complete file path
        String filePath = basePath + fileName;
        logger.info("Reading JSON file from: {}", filePath);

        File jsonFile = new File(filePath);
        if (!jsonFile.exists() || !jsonFile.canRead()) {
            String errorMsg = "JSON file not found or not readable at path: " + filePath;
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }

        try {
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            return jsonNodeToMap(rootNode);
        } catch (IOException e) {
            String errorMsg = "Error reading or parsing JSON file: " + filePath;
            logger.error(errorMsg, e);
            throw e;
        }
    }

    private static Map<String, String> jsonNodeToMap(JsonNode jsonNode) {
        Map<String, String> map = new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            String value = entry.getValue().asText();
            map.put(key, value);
        }

        return map;
    }
}
