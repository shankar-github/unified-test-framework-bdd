//package steps;
//
//import config.ConfigManager;
//import helpers.HeaderManager;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.restassured.http.Headers;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import base.APIBase;
//
//public class PutStepDefinitions extends APIBase {
//
//    @Given("I send a PUT request to {string} with the following details:")
//    public void sendPutRequest(String path, DataTable dataTable) {
//        // Combine base URL with path
//        String endpoint = ConfigManager.get("baseUrl") + path;
//
//        // Convert DataTable to Map
//        Map<String, String> requestBody = dataTable.asMap(String.class, String.class);
//
//        // Prepare headers (if needed, use getDefaultHeaders or custom headers)
//        Headers headers = HeaderManager.getDefaultHeaders();
//
//        // Prepare parameters for the sendRequest method
//        Map<String, Object> params = new HashMap<>();
//        params.put("body", requestBody);
//
//        // Send PUT request
//        sendRequest("PUT", endpoint, headers, params);
//    }
//
//    @Then("I should receive a response with status code {int}")
//    public void verifyStatusCode(int expectedStatusCode) {
//        // Verify the response status code
//        verifyStatusCode(expectedStatusCode);
//    }
//
//    @Then("The response body should contain {string}")
//    public void verifyResponseBodyContains(String expectedContent) {
//        // Verify the response body contains the expected content
//        verifyResponseContains(expectedContent);
//    }
//}
