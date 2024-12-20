package helpers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import config.ConfigManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Reads data from a CSV file using OpenCSV.
 * The file path is dynamically built using the ConfigManager to get the base directory and the file name passed by the Step Definitions (SDs).
 */
public class CsvReader {

    private static final Logger logger = LogManager.getLogger(CsvReader.class);

    public static List<Map<String, String>> readCsv(String fileName) throws IOException {
        // Get the base directory path for the test data folder
        String basePath = ConfigManager.get("testDataFolderLocation");
        if (basePath == null) {
            logger.error("Base directory for test data folder is not defined in config.properties.");
            throw new IOException("Base directory for test data folder is not defined.");
        }

        // Append the file name to the base path to form the complete file path
        String filePath = basePath + fileName;

        logger.info("Reading CSV file from: {}", filePath);

        List<Map<String, String>> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] headers = reader.readNext();
            if (headers == null) {
                throw new IOException("CSV file is empty.");
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length != headers.length) {
                    logger.warn("Row length does not match header length: {}", String.join(",", row));
                    continue;
                }

                Map<String, String> rowData = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    rowData.put(headers[i], row[i]);
                }
                data.add(rowData);
            }
        } catch (CsvValidationException e) {
            logger.error("Error validating CSV file: {}", e.getMessage());
            throw new IOException("Error validating CSV file: " + e.getMessage(), e);
        }

        logger.info("CSV file successfully read with {} rows.", data.size());
        return data;
    }
}
