
package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Base class for API testing, providing utility methods for sending HTTP
 * requests, validating responses, and logging.
 */
public class APIBase {

    protected RequestSpecification requestSpec;
    protected Response response;
    private static final Logger logger = LogManager.getLogger(APIBase.class);
    private String baseUri;

    public APIBase() {
        logger.info("Initializing API Base Class");

        // Fetch base URI from ConfigManager
        baseUri = ConfigManager.get("baseUri");
        if (baseUri == null || baseUri.isEmpty()) {
            baseUri = "http://localhost"; // Default fallback URI
            logger.warn("Base URI not found in configuration. Using default: {}", baseUri);
        }

        logger.info("Base URI: {}", baseUri);
        initializeRequestSpec(baseUri);
    }

    /**
     * Initializes or reinitializes the RequestSpecification.
     *
     * @param baseUri The base URI to set in the RequestSpecification.
     */
    private void initializeRequestSpec(String baseUri) {
        try {
            requestSpec = new RequestSpecBuilder().setBaseUri(baseUri)
                    .addFilter(new RequestLoggingFilter())
                    .addFilter(new ResponseLoggingFilter())
                    .build();
            logger.info("RequestSpecification initialized successfully with Base URI: {}", baseUri);
        } catch (Exception e) {
            logger.error("Failed to initialize RequestSpecification: {}", e.getMessage(), e);
            throw new RuntimeException("Error initializing RequestSpecification", e);
        }
    }

    /**
     * Sets or updates the base URI.
     *
     * @param baseURI The new base URI to use.
     */
    public void setBaseURI(String baseURI) {
        this.baseUri = baseURI;
        initializeRequestSpec(baseURI);
        logger.info("Base URI updated to: {}", baseURI);
    }

    /**
     * Sends an HTTP request based on the provided method, endpoint, headers, and
     * parameters.
     *
     * @param method   The HTTP method (GET, POST, PUT, etc.).
     * @param endpoint The endpoint to hit.
     * @param headers  The headers to include in the request.
     * @param params   A map containing query parameters, body, or multipart data.
     * @return The Response object.
     */
    public Response sendRequest(String method, String endpoint, Headers headers, Map<String, Object> params) {
        if (requestSpec == null) {
            logger.error("RequestSpecification is null. Please initialize it before sending a request.");
            throw new IllegalStateException("RequestSpecification is not initialized.");
        }

        logger.info("Sending {} request to endpoint: {}", method.toUpperCase(), endpoint);
        logger.info("Full URL: {}", baseUri + endpoint);

        if (headers != null && headers.size() > 0) {
            requestSpec.headers(headers);
        } else {
            logger.warn("No headers provided for request.");
        }

        Map<String, String> queryParams = (Map<String, String>) params.getOrDefault("queryParams", new HashMap<>());
        if (!queryParams.isEmpty()) {
            requestSpec.queryParams(queryParams);
        } else {
            logger.info("No query parameters provided.");
        }

        Object body = params.get("body");
        if (body != null) {
            requestSpec.body(body);
        } else {
            logger.info("No body content provided for request.");
        }

        String filePath = (String) params.get("filePath");
        String paramName = (String) params.get("paramName");
        if (filePath != null && paramName != null) {
            File file = new File(filePath);
            if (file.exists()) {
                requestSpec.multiPart(paramName, file);
            } else {
                logger.error("File does not exist at path: {}", filePath);
                throw new IllegalArgumentException("File does not exist: " + filePath);
            }
        }

        try {
            response = sendHttpRequest(method, endpoint);
            logRequestAndResponse(method, endpoint, headers, params.toString());
            logger.info("Response received: Status Code: {}, Body: {}", response.statusCode(), response.asString());
        } catch (Exception e) {
            logger.error("Exception while sending request: {}", e.getMessage(), e);
            throw e;
        }

        return response;
    }

    private Response sendHttpRequest(String method, String endpoint) {
        switch (method.toUpperCase()) {
            case "GET":
                return given().spec(requestSpec).get(endpoint);
            case "POST":
                return given().spec(requestSpec).post(endpoint);
            case "PUT":
                return given().spec(requestSpec).put(endpoint);
            case "PATCH":
                return given().spec(requestSpec).patch(endpoint);
            case "DELETE":
                return given().spec(requestSpec).delete(endpoint);
            case "HEAD":
                return given().spec(requestSpec).head(endpoint);
            case "OPTIONS":
                return given().spec(requestSpec).options(endpoint);
            default:
                logger.error("Unsupported HTTP method: {}", method);
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }

    /**
     * Converts a map of key-value pairs to a JSON string.
     *
     * @param data The map to convert.
     * @return The resulting JSON string.
     */
    public static String convertMapToJson(Map<String, String> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    /**
     * Logs request and response details to Allure.
     *
     * @param method   The HTTP method.
     * @param endpoint The endpoint.
     * @param headers  The request headers.
     * @param body     The request body.
     */
    public void logRequestAndResponse(String method, String endpoint, Headers headers, String body) {
        logger.info("Headers: {}", headers);
        logger.info("Body: {}", body);
        Allure.addAttachment("Request Headers", "application/json", headers.toString(), "json");
        Allure.addAttachment("Request Body", "application/json", body, "json");
        logger.info("Response: {} {}", response.statusCode(), response.getBody().asString());
        Allure.addAttachment("Response Body", "application/json", response.getBody().asString(), "json");
    }

    /**
     * Verifies the status code of the response.
     *
     * @param expectedStatusCode The expected status code.
     */
    public void verifyStatusCode(int expectedStatusCode) {
        logger.info("Verifying status code: {}", expectedStatusCode);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Status Code Mismatch");
    }

    /**
     * Verifies the existence of a specific key in the JSON response body.
     *
     * @param key The key to check for.
     */
    public void verifyJsonResponseKeyExists(String key) {
        logger.info("Verifying if key exists in response body: {}", key);
        Assert.assertNotNull(response.jsonPath().getString(key), "Key " + key + " does not exist in response");
    }

    /**
     * Verifies if the response body contains a specific substring.
     * It checks whether the expected substring is present in the response body
     * and logs the verification process.
     *
     * @param expectedSubstring The substring that is expected to be present in the response body.
     * @return void This method does not return any value. It throws an AssertionError if the response body does not contain the expected substring.
     */
    public void verifyResponseContains(String expectedSubstring) {
        logger.info("Verifying response contains: " + expectedSubstring);
        Assert.assertTrue(response.getBody().asString().contains(expectedSubstring),
                "Response body does not contain the expected substring: " + expectedSubstring);
    }

    /**
     * Method for extracting a list of values from the JSON response based on the
     * provided key. It uses JsonPath to extract the list corresponding to the key
     * from the response.
     *
     * @param key The key whose associated list of values is to be extracted from
     *            the JSON response.
     * @return A list of strings containing the values corresponding to the key in
     *         the JSON response.
     */
    public List<String> getJsonResponseList(String key) {
        return response.jsonPath().getList(key); // Extracts and returns the list
    }

    /**
     * Method for handling response failure by checking the status code. If the
     * status code is 400 or greater, it logs an error and throws an AssertionError
     * to signal that the request has failed.
     *
     * @return void This method does not return any value. It throws an
     *         AssertionError on failure.
     */
    public void handleResponseFailure() {
        if (response.statusCode() >= 400) { // Checks if status code indicates failure
            logger.error("Request failed with status code: " + response.statusCode()); // Logs the error message
            throw new AssertionError("Request failed with status code: " + response.statusCode()); // Throws exception
        }
    }
}
