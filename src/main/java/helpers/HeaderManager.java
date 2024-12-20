package helpers;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for managing HTTP headers in API requests.
 * Provides methods to retrieve default headers, add custom headers, and combine headers dynamically.
 */
public class HeaderManager {

    // Logger to log information and errors related to header management
    private static final Logger logger = LogManager.getLogger(HeaderManager.class);

    // Default headers, initialized with the "Content-Type" header
    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<>();

    static {
        // Set up default headers
        DEFAULT_HEADERS.put("Content-Type", "application/json");
    }

    /**
     * Combines the default headers with the provided custom headers.
     *
     * @param customHeaders A map of custom headers to be added to the default headers.
     * @return A Headers object containing both default and custom headers.
     */
    public static Headers getHeaders(Map<String, String> customHeaders) {
        logger.info("Creating headers with custom values."); // Log the action
        Map<String, String> allHeaders = new HashMap<>(DEFAULT_HEADERS);

        // Add custom headers to the default headers if provided
        if (customHeaders != null) {
            allHeaders.putAll(customHeaders);
        }

        logger.debug("Combined headers: {}", allHeaders); // Log the combined headers
        return createHeadersFromMap(allHeaders); // Convert to Headers object and return
    }

    /**
     * Retrieves the default headers without adding any custom headers.
     *
     * @return A Headers object containing only the default headers.
     */
    public static Headers getHeaders() {
        logger.info("Retrieving default headers."); // Log the action
        return getDefaultHeaders(); // Return the default headers
    }

    /**
     * Adds a single custom header to the default set of headers and returns the combined headers.
     *
     * @param key   The name of the header to be added.
     * @param value The value of the header to be added.
     * @return A Headers object containing both default and the provided custom header.
     */
    public static Headers addHeader(String key, String value) {
        logger.info("Adding a single custom header: {} = {}", key, value); // Log the custom header being added
        Map<String, String> allHeaders = new HashMap<>(DEFAULT_HEADERS);

        // Add the custom header
        allHeaders.put(key, value);
        logger.debug("Updated headers with new header: {}", allHeaders); // Log the updated headers
        return createHeadersFromMap(allHeaders); // Convert to Headers object and return
    }

    /**
     * Converts a map of header key-value pairs into a Headers object.
     *
     * @param headersMap A map containing header names as keys and their corresponding values.
     * @return A Headers object containing the provided header key-value pairs.
     */
    private static Headers createHeadersFromMap(Map<String, String> headersMap) {
        // Convert map entries into Header objects and collect them into an array
        Header[] headers = headersMap.entrySet().stream()
            .map(entry -> new Header(entry.getKey(), entry.getValue()))
            .toArray(Header[]::new);

        return new Headers(headers); // Return the Headers object
    }

    /**
     * Retrieves the default headers as a Headers object.
     *
     * @return A Headers object containing the default headers.
     */
    public static Headers getDefaultHeaders() {
        logger.info("Getting default headers."); // Log the action
        return createHeadersFromMap(DEFAULT_HEADERS); // Convert default headers map to Headers object
    }
}
