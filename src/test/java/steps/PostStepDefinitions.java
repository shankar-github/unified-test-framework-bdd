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
import java.util.Map.Entry;

public class PostStepDefinitions {

    private static final Logger logger = LogManager.getLogger(PostStepDefinitions.class);
    private final APIBase apiBase = new APIBase(); // Assuming ApiBase is your base API class

    @Given("I send a POST request with the JSON data:")
    public void sendPostRequestWithJson(String jsonData) {
        logger.info("Sending POST request with JSON data: {}", jsonData);
        apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonData));
    }

 
    @Given("I send a POST request with the following details:")
    public void sendPostRequestWithDetails(DataTable table) throws JsonProcessingException {
        // Convert the DataTable to a List of Maps to handle the key-value pairs per row
        List<Map<String, String>> dataList = table.asMaps(String.class, String.class);

        // Iterate through the list and log the data or send it as JSON
        for (Map<String, String> data : dataList) {
            logger.info("Data: {}", data);

            // Convert the data map to JSON format before sending the request
            String jsonBody = new ObjectMapper().writeValueAsString(data);

            // Send the POST request with the JSON body
            apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
        }
    }



    @Given("I send a POST request with user data from CSV file {string}")
    public void sendPostRequestWithCsvFile(String csvFileName) throws IOException {
        // Read data from CSV
        List<Map<String, String>> csvData = CsvReader.readCsv(csvFileName);

        // Iterate through the data and send a POST request for each user
        for (Map<String, String> userDetails : csvData) {
            // Wrap user data in a Map with the "body" key (required format)
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("body", userDetails);

            // Convert the request body map to a JSON string
            String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

            logger.info("Sending POST request with JSON data: {}", jsonBody);

            // Send the POST request
            apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
        }
    }


    @Given("I send a POST request with user data from JSON file {string}")
    public void sendPostRequestWithJsonFile(String jsonFileName) throws IOException {
        // Read data from JSON file
        Map<String, String> jsonData = JsonReader.readJson(jsonFileName);

        // Convert map to JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(jsonData);

        logger.info("Sending POST request with JSON data: {}", jsonBody);
        apiBase.sendRequest("POST", "/users", HeaderManager.getHeaders(), Map.of("body", jsonBody));
    }

    @Given("I send a POST request with user data from Excel file {string}")
    public void sendPostRequestWithExcelFile(String excelFileName) throws IOException {
        // Read data from Excel
        List<Map<String, String>> excelData = ExcelReader.readExcel(excelFileName);

        // List to hold all user JSON objects
        List<Map<String, String>> usersList = new ArrayList<>();

        for (Map<String, String> userDetails : excelData) {
            // Add each user's data to the list
            usersList.add(userDetails);
        }

        // Convert the list of users to a JSON array
        String jsonBody = new ObjectMapper().writeValueAsString(usersList);

        logger.info("Sending bulk POST request with JSON data: {}", jsonBody);

        // Send a single API request with all users in one go
        apiBase.sendRequest("POST", "/users/bulk", HeaderManager.getHeaders(), Map.of("body", jsonBody));
    }

    @Then("I should receive a response with status code {int}")
    public void validateStatusCode(int expectedStatusCode) {
    	apiBase.verifyStatusCode(expectedStatusCode);
    }

    @Then("The response body should contain {string}")
    public void validateResponseBodyContains(String expectedContent) {
    	apiBase.verifyResponseContains(expectedContent);
    }
}
