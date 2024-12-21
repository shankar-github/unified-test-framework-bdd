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

public class PatchStepDefinitions {

    private static final Logger logger = LogManager.getLogger(PatchStepDefinitions.class);
    private final APIBase apiBase = new APIBase();

    @Given("I send a PATCH request with the following details:")
    public void sendPatchRequest(String path, DataTable dataTable) {
        String endpoint = ConfigManager.get("baseUrl") + path;
        Map<String, String> requestBody = dataTable.asMap(String.class, String.class);
        Headers headers = HeaderManager.getDefaultHeaders();
        Map<String, Object> params = new HashMap<>();
        params.put("body", requestBody);

        try {
            logger.info("Sending PATCH request to endpoint: {}", endpoint);
            apiBase.sendRequest("PATCH", endpoint, headers, params);
        } catch (Exception e) {
            logger.error("Error sending PATCH request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("I should receive a PATCH response with status code {int}")
    public void verifyPatchResponseStatusCode(int expectedStatusCode) {
        try {
            logger.info("Verifying status code: {}", expectedStatusCode);
            apiBase.verifyStatusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Status code verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("The PATCH response body should contain {string}")
    public void verifyPatchResponseBodyContains(String expectedContent) {
        try {
            logger.info("Verifying response body contains: {}", expectedContent);
            apiBase.verifyResponseContains(expectedContent);
        } catch (AssertionError e) {
            logger.error("Response body verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}