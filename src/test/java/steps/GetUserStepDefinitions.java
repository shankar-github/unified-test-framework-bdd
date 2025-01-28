
package steps;

import java.util.HashMap;
import java.util.Map;

import base.APIBase;
import helpers.HeaderManager;
import io.cucumber.java.en.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Step definitions for handling GET requests to retrieve user information in Cucumber tests.
 */
public class GetUserStepDefinitions {

    private static final Logger logger = LogManager.getLogger(GetUserStepDefinitions.class);
    private final APIBase apiBase = new APIBase();
    private String endpoint;
    private Map<String, Object> params = new HashMap<>();
    

    /**
     * Sets the API endpoint to retrieve user information.
     *
     * @param userId The ID of the user.
     * @return The constructed endpoint.
     */
    @Given("the API endpoint is set to \\/users\\/{string}")
    public String setApiEndpoint(String userId) {
        this.endpoint = "/users/" + userId;
        logger.info("API endpoint set to: {}", endpoint);
        return endpoint;
    }

    /**
     * Sends a GET request to the previously set endpoint.
     */
    @When("a GET request is sent to the endpoint")
    public void sendGetRequest() {
    	params.put("name", "John Doe");
        try {
            logger.info("Sending GET request to endpoint: {}", endpoint);
            apiBase.sendRequest("GET", endpoint, HeaderManager.getHeaders(), params);
        } catch (Exception e) {
            logger.error("Error sending GET request: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Verifies that the response status code matches the expected status code.
     *
     * @param expectedStatusCode The expected status code.
     */
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

    /**
     * Verifies that the response body contains the expected content.
     *
     * @param expectedMessage The expected content in the response body.
     */
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
