package steps;

import base.APIBase;
import config.ConfigManager;
import helpers.HeaderManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PutStepDefinitions {

    private static final Logger logger = LogManager.getLogger(PutStepDefinitions.class);
    private final APIBase apiBase = new APIBase();

    @Given("I send a PUT request with the following details:")
    public void sendPutRequest(DataTable dataTable) {
        // Convert DataTable to a map
        Map<String, String> requestData = dataTable.asMap(String.class, String.class);

        // Extract details from the table
        int userId = Integer.parseInt(requestData.get("userId"));
        String username = requestData.get("username");
        String email = requestData.get("email");

        // Construct the endpoint
        String endpoint = ConfigManager.get("baseUrl") + "/users/" + userId;

        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("email", email);
        Headers headers = HeaderManager.getDefaultHeaders();
        Map<String, Object> params = new HashMap<>();

        try {
            logger.info("Sending PUT request to endpoint: {}", endpoint);
            apiBase.sendRequest("PUT", endpoint, headers, params);
        } catch (Exception e) {
            logger.error("Error sending PUT request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("I should receive a PUT response with status code {int}")
    public void verifyPutResponseStatusCode(int expectedStatusCode) {
        try {
            logger.info("Verifying status code: {}", expectedStatusCode);
            apiBase.verifyStatusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Status code verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("The PUT response body should contain {string}")
    public void verifyPutResponseBodyContains(String expectedContent) {
        try {
            logger.info("Verifying response body contains: {}", expectedContent);
            apiBase.verifyResponseContains(expectedContent);
        } catch (AssertionError e) {
            logger.error("Response body verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
