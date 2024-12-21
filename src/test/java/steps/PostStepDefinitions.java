package steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import base.APIBase;
import helpers.CsvReader;
import helpers.ExcelReader;
import helpers.HeaderManager;
import helpers.JsonReader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostStepDefinitions {

    private static final Logger logger = LogManager.getLogger(PostStepDefinitions.class);
    private final APIBase apiBase = new APIBase();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Given("I send a POST request with the JSON data:")
    public void sendPostRequestWithJson(String jsonData) {
        try {
            logger.info("Sending POST request with JSON data: {}", jsonData);
            apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonData));
        } catch (Exception e) {
            logger.error("Error sending POST request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Given("I send a POST request with the following details:")
    public void sendPostRequestWithDetails(DataTable table) {
        List<Map<String, String>> dataList = table.asMaps(String.class, String.class);

        for (Map<String, String> data : dataList) {
            try {
                logger.info("Data: {}", data);
                String jsonBody = objectMapper.writeValueAsString(data);
                apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
            } catch (JsonProcessingException e) {
                logger.error("Error processing JSON: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to process JSON", e);
            }
        }
    }

    @Given("I send a POST request with user data from CSV file {string}")
    public void sendPostRequestWithCsvFile(String csvFileName) {
        try {
            List<Map<String, String>> csvData = CsvReader.readCsv(csvFileName);

            for (Map<String, String> userDetails : csvData) {
                String jsonBody = objectMapper.writeValueAsString(userDetails);
                logger.info("Sending POST request with JSON data: {}", jsonBody);
                apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
            }
        } catch (IOException e) {
            logger.error("Error reading CSV file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }

    @Given("I send a POST request with user data from JSON file {string}")
    public void sendPostRequestWithJsonFile(String jsonFileName) {
        try {
            Map<String, String> jsonData = JsonReader.readJson(jsonFileName);
            String jsonBody = objectMapper.writeValueAsString(jsonData);
            logger.info("Sending POST request with JSON data: {}", jsonBody);
            apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
        } catch (IOException e) {
            logger.error("Error reading JSON file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read JSON file", e);
        }
    }

    @Given("I send a POST request with user data from Excel file {string}")
    public void sendPostRequestWithExcelFile(String excelFileName) {
        try {
            List<Map<String, String>> excelData = ExcelReader.readExcel(excelFileName);
            List<Map<String, String>> usersList = new ArrayList<>(excelData);
            String jsonBody = objectMapper.writeValueAsString(usersList);
            logger.info("Sending bulk POST request with JSON data: {}", jsonBody);
            apiBase.sendRequest("POST", "/users/bulk", HeaderManager.getHeaders(), Map.of("body", jsonBody));
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    @Then("I should receive a response with status code {int}")
    public void validatePostResponseStatusCode(int expectedStatusCode) {
        try {
            apiBase.verifyStatusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Status code verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("The response body should contain {string}")
    public void validatePostResponseBodyContains(String expectedContent) {
        try {
            apiBase.verifyResponseContains(expectedContent);
        } catch (AssertionError e) {
            logger.error("Response body verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}