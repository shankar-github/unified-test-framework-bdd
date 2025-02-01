
package helpers;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads data from an Excel file using Apache POI.
 * The file path is dynamically built using the ConfigManager to get the base directory and the file name passed by the Step Definitions (SDs).
 */
public class ExcelReader {

    private static final Logger logger = LogManager.getLogger(ExcelReader.class);

    public static List<Map<String, String>> readExcel(String fileName) throws IOException {
        // Get the base directory path for the test data folder
        String basePath = ConfigManager.get("testDataFolderLocation");
        if (basePath == null || basePath.isEmpty()) {
            String errorMsg = "Base directory for test data folder is not defined in config.properties.";
            logger.error(errorMsg);
            throw new IOException(errorMsg);
        }

        // Append the file name to the base path to form the complete file path
        String filePath = basePath + fileName;
        logger.info("Reading Excel file from: {}", filePath);

        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                String errorMsg = "The Excel file does not contain any sheets.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                String errorMsg = "The Excel file's first sheet does not contain a header row.";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    logger.warn("Skipping empty row at index: {}", i);
                    continue;
                }

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String cellValue = (cell != null) ? getCellValueAsString(cell) : "";
                    rowData.put(headers.get(j), cellValue);
                }
                data.add(rowData);
            }

            logger.info("Excel file successfully read. Total rows processed: {}", data.size());
        } catch (IOException e) {
            String errorMsg = "Error reading Excel file: " + e.getMessage();
            logger.error(errorMsg, e);
            throw e;
        }

        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
