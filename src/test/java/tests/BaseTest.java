package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigManager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected Response response;

    // Set up the base URI for RestAssured from the config file
    public static void setup() {
        String baseUrl = ConfigManager.get("base.url");
        RestAssured.baseURI = baseUrl;
    }

    // Get the full URL for a given endpoint
    public static String getFullUrl(String endpoint) {
        return RestAssured.baseURI + endpoint;
    }

    // Convert Map<String, String> to JSON String
    public static String convertMapToJson(Map<String, String> data) {
        try {
            // Create an instance of ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    // Common method to send HTTP requests
    public void sendRequest(String method, String endpoint, Headers headers, Map<String, Object> params) {
        logger.info("Sending " + method + " request to: " + endpoint);
        
        RequestSpecification request = RestAssured.given().headers(headers);

        // Add query parameters if present
        Map<String, String> queryParams = (Map<String, String>) params.getOrDefault("queryParams", new HashMap<>());
        if (!queryParams.isEmpty()) {
            request.queryParams(queryParams);
        }

        // Add body if present
        Object body = params.get("body");
        if (body != null) {
            request.body(body);
        }

        // Add multipart data if file path and param name are provided
        String filePath = (String) params.get("filePath");
        String paramName = (String) params.get("paramName");
        if (filePath != null && paramName != null) {
            File file = new File(filePath);
            request.multiPart(paramName, file);
        }

        // Send the request based on the HTTP method
        switch (method.toUpperCase()) {
            case "GET":
                response = request.when().get(endpoint);
                break;
            case "POST":
                response = request.when().post(endpoint);
                break;
            case "PUT":
                response = request.when().put(endpoint);
                break;
            case "PATCH":
                response = request.when().patch(endpoint);
                break;
            case "DELETE":
                response = request.when().delete(endpoint);
                break;
            case "HEAD":
                response = request.when().head(endpoint);
                break;
            case "OPTIONS":
                response = request.when().options(endpoint);
                break;
            default:
                logger.error("Unsupported HTTP method: " + method);
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        // Log the response for debugging
        logResponse();
    }


    // Log response details for debugging
    private void logResponse() {
        logger.info("Response Status Code: " + response.statusCode());
        logger.info("Response Body: " + response.getBody().asString());
    }

    // Verify the status code of the response
    public void verifyStatusCode(int expectedStatusCode) {
        logger.info("Verifying status code: " + expectedStatusCode);
        Assert.assertEquals(response.statusCode(), expectedStatusCode, "Status Code Mismatch");
    }

    // Verify a specific response header value
    public void verifyHeader(String headerName, String expectedValue) {
        logger.info("Verifying header: " + headerName + " with value: " + expectedValue);
        Assert.assertEquals(response.getHeader(headerName), expectedValue, "Header value mismatch for " + headerName);
    }

    // Verify if a specific key exists in the response body
    public void verifyJsonResponseKey(String key, String expectedValue) {
        logger.info("Verifying key: " + key + " with expected value: " + expectedValue);
        Assert.assertEquals(response.jsonPath().getString(key), expectedValue, "Response body does not match for key " + key);
    }

    // Check if a specific key exists in the response body
    public void verifyJsonResponseKeyExists(String key) {
        logger.info("Verifying if key exists in response body: " + key);
        Assert.assertNotNull(response.jsonPath().getString(key), "Key " + key + " does not exist in response");
    }

    // Fetch a specific value from the response JSON body
    public String getJsonResponseKey(String key) {
        return response.jsonPath().getString(key);
    }

    // Verify if the response contains a specific string
    public void verifyResponseContains(String expectedSubstring) {
        logger.info("Verifying response contains: " + expectedSubstring);
        Assert.assertTrue(response.getBody().asString().contains(expectedSubstring),
                "Response body does not contain the expected substring: " + expectedSubstring);
    }

    // Verify if the response does not contain a specific string
    public void verifyResponseNotContains(String unexpectedSubstring) {
        logger.info("Verifying response does not contain: " + unexpectedSubstring);
        Assert.assertFalse(response.getBody().asString().contains(unexpectedSubstring),
                "Response body contains the unexpected substring: " + unexpectedSubstring);
    }

    // Verify the JSON response against a schema
    public void verifyJsonSchema(String schema) {
        logger.info("Verifying response against schema: " + schema);
        response.then().assertThat().body(matchesJsonSchemaInClasspath(schema));
    }

    // Verify that a specific value exists in an array in the response body
    public void verifyJsonArrayContains(String arrayKey, String value) {
        logger.info("Verifying that the array under key " + arrayKey + " contains value: " + value);
        Assert.assertTrue(response.jsonPath().getList(arrayKey).contains(value),
                "Array does not contain the expected value: " + value);
    }

    // Verify the response time is under a certain threshold
    public void verifyResponseTimeLessThan(long timeInMillis) {
        logger.info("Verifying response time is less than: " + timeInMillis + " ms");
        Assert.assertTrue(response.getTime() < timeInMillis, "Response time is too long");
    }

    // Verify the response content type
    public void verifyContentType(String expectedContentType) {
        logger.info("Verifying content type: " + expectedContentType);
        Assert.assertEquals(response.getContentType(), expectedContentType, "Content Type Mismatch");
    }

    // Verify the existence of a cookie in the response
    public void verifyCookieExists(String cookieName) {
        logger.info("Verifying cookie exists: " + cookieName);
        Assert.assertNotNull(response.getCookie(cookieName), "Cookie " + cookieName + " does not exist");
    }

    // Convert response body to a Java object
    public <T> T getResponseAsObject(Class<T> clazz) {
        return response.getBody().as(clazz);
    }

    // Method for validating JSON response path
    public void validateJsonResponsePath(String jsonPath, Object expectedValue) {
        logger.info("Validating JSON path: " + jsonPath + " with value: " + expectedValue);
        Object actualValue = response.jsonPath().get(jsonPath);
        Assert.assertEquals(actualValue, expectedValue, "JSON Path " + jsonPath + " does not match the expected value.");
    }

    // Method for verifying multiple headers
    public void verifyHeaders(Map<String, String> expectedHeaders) {
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            String actualValue = response.getHeader(entry.getKey());
            Assert.assertEquals(actualValue, entry.getValue(), "Header value mismatch for " + entry.getKey());
        }
    }

    // Method for verifying status code is in a range
    public void verifyStatusCodeInRange(int minStatusCode, int maxStatusCode) {
        logger.info("Verifying status code is between " + minStatusCode + " and " + maxStatusCode);
        int actualStatusCode = response.statusCode();
        Assert.assertTrue(actualStatusCode >= minStatusCode && actualStatusCode <= maxStatusCode,
                "Status code " + actualStatusCode + " is not within the range " + minStatusCode + " - " + maxStatusCode);
    }

    // Method for verifying response time in a range
    public void verifyResponseTimeInRange(long minTime, long maxTime) {
        logger.info("Verifying response time is between " + minTime + " ms and " + maxTime + " ms");
        long actualTime = response.getTime();
        Assert.assertTrue(actualTime >= minTime && actualTime <= maxTime,
                "Response time " + actualTime + " is not within the range " + minTime + " - " + maxTime);
    }

    // Method for verifying cookie value
    public void verifyCookieValue(String cookieName, String expectedValue) {
        logger.info("Verifying cookie " + cookieName + " with value: " + expectedValue);
        String actualValue = response.getCookie(cookieName);
        Assert.assertEquals(actualValue, expectedValue, "Cookie value mismatch for " + cookieName);
    }

    // Method for logging request and response
    public void logRequestAndResponse(String method, String endpoint, Headers headers, String body) {
        logger.info("Request: " + method + " " + endpoint);
        logger.info("Headers: " + headers);
        logger.info("Body: " + body);
        logger.info("Response: " + response.statusCode() + " " + response.getBody().asString());
    }

    

    // Method for extracting list of values from response
    public List<String> getJsonResponseList(String key) {
        return response.jsonPath().getList(key);
    }

    // Method for handling response failure
    public void handleResponseFailure() {
        if (response.statusCode() >= 400) {
            logger.error("Request failed with status code: " + response.statusCode());
            Assert.fail("Request failed with status code: " + response.statusCode());
        }
    }
}
