 package steps.api;
 /* 
 * import base.APIBase; import config.ConfigManager; import
 * helpers.HeaderManager; import io.cucumber.datatable.DataTable; import
 * io.cucumber.java.en.Given; import io.cucumber.java.en.Then; import
 * io.restassured.http.Headers; import org.apache.logging.log4j.LogManager;
 * import org.apache.logging.log4j.Logger;
 * 
 * import java.util.HashMap; import java.util.Map;
 * 
 *//**
	 * Step definitions for handling PATCH requests in Cucumber tests.
	 */
/*
 * public class PatchStepDefinitions {
 * 
 * private static final Logger logger =
 * LogManager.getLogger(PatchStepDefinitions.class); private final APIBase
 * apiBase = new APIBase();
 * 
 *//**
	 * Sends a PATCH request with the given details.
	 *
	 * @param path      The endpoint path.
	 * @param dataTable DataTable containing the request details.
	 */
/*
 * @Given("I send a PATCH request with the following details:") public void
 * sendPatchRequest(String path, DataTable dataTable) { String endpoint =
 * ConfigManager.get("baseUrl") + path; Map<String, String> requestBody =
 * dataTable.asMap(String.class, String.class); Headers headers =
 * HeaderManager.getDefaultHeaders(); Map<String, Object> params = new
 * HashMap<>(); params.put("body", requestBody);
 * 
 * try { logger.info("Sending PATCH request to endpoint: {}", endpoint);
 * apiBase.sendRequest("PATCH", endpoint, headers, params); } catch (Exception
 * e) { logger.error("Error sending PATCH request: {}", e.getMessage(), e);
 * throw e; } }
 * 
 *//**
	 * Verifies that the response status code matches the expected status code.
	 *
	 * @param expectedStatusCode The expected status code.
	 */
/*
 * @Then("I should receive a PATCH response with status code {int}") public void
 * verifyPatchResponseStatusCode(int expectedStatusCode) { try {
 * logger.info("Verifying status code: {}", expectedStatusCode);
 * apiBase.verifyStatusCode(expectedStatusCode); } catch (AssertionError e) {
 * logger.error("Status code verification failed: {}", e.getMessage(), e); throw
 * e; } }
 * 
 *//**
	 * Verifies that the response body contains the expected content.
	 *
	 * @param expectedContent The expected content in the response body.
	 *//*
		 * @Then("The PATCH response body should contain {string}") public void
		 * verifyPatchResponseBodyContains(String expectedContent) { try {
		 * logger.info("Verifying response body contains: {}", expectedContent);
		 * apiBase.verifyResponseContains(expectedContent); } catch (AssertionError e) {
		 * logger.error("Response body verification failed: {}", e.getMessage(), e);
		 * throw e; } } }
		 */


