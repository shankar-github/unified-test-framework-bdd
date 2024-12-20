package steps;

import java.util.HashMap;
import java.util.Map;

import base.APIBase;
import helpers.HeaderManager;
import io.cucumber.java.en.*;


public class GetUserStepDefinitions {

    private final APIBase apiBase = new APIBase();
    private String endpoint;
    private Map<String, Object> params = new HashMap<>();
   
    @Given("the API endpoint is set to \\/users\\/{string}")
    public String setApiEndpoint(String userId) {
        this.endpoint = "/users/" + userId;
        return endpoint;
    }

    @When("a GET request is sent to the endpoint")
    public void sendGetRequest() {
//    	params.put("queryParams", new HashMap<String, String>());  // Empty query params
//        params.put("body", null);  // No body for a GET request
        apiBase.sendRequest("GET", endpoint,HeaderManager.getHeaders(), params);
    }

    @Then("the status code should be {int}")
    public void verifyStatusCode(int expectedStatusCode) {
        apiBase.verifyStatusCode(expectedStatusCode);
    }

    @And("the response body should contain the string {string}")
    public void verifyResponseBody(String expectedMessage) {
        apiBase.verifyResponseContains(expectedMessage);
    }
}