
package steps;

import java.util.HashMap;
import java.util.Map;

import base.APIBase;
import helpers.HeaderManager;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GetUserStepDefinitions {

    private static final Logger logger = LogManager.getLogger(GetUserStepDefinitions.class);
    private final APIBase apiBase = new APIBase();
    private String endpoint;
    private Map<String, Object> params = new HashMap<>();

    @Given("the API endpoint is set to \\/users\\/{string}")
    public String setApiEndpoint(String userId) {
        this.endpoint = "/users/" + userId;
        logger.info("API endpoint set to: {}", endpoint);
        return endpoint;
    }

    @When("a GET request is sent to the endpoint")
    public void sendGetRequest() {
        try {
            logger.info("Sending GET request to endpoint: {}", endpoint);
            apiBase.sendRequest("GET", endpoint, HeaderManager.getHeaders(), params);
        } catch (Exception e) {
            logger.error("Error sending GET request: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Then("the status code should be {int}")
    public void verifyGetResponseStatusCode(int expectedStatusCode) {
        try {
            logger.info("Verifying status code: {}", expectedStatusCode);
            apiBase.verifyStatusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Status code verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @And("the response body should contain the string {string}")
    public void verifyGetResponseBody(String expectedMessage) {
        try {
            logger.info("Verifying response body contains: {}", expectedMessage);
            apiBase.verifyResponseContains(expectedMessage);
        } catch (AssertionError e) {
            logger.error("Response body verification failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
