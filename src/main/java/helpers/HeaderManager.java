package helpers;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HeaderManager {

    private static final Logger logger = LogManager.getLogger(HeaderManager.class);

    // Default headers set
    private static final Map<String, String> DEFAULT_HEADERS = new HashMap<>();

    static {
        // Set up default headers
        DEFAULT_HEADERS.put("Content-Type", "application/json");
    }

    /**
     * Adds custom headers to the default set of headers.
     *
     * @param customHeaders A map of custom headers to be added.
     * @return Headers object containing both default and custom headers.
     */
    public static Headers getHeaders(Map<String, String> customHeaders) {
        logger.info("Creating headers with custom values.");
        Map<String, String> allHeaders = new HashMap<>(DEFAULT_HEADERS);

        if (customHeaders != null) {
            allHeaders.putAll(customHeaders);  // Add custom headers
        }

        // Log the headers being added
        logger.debug("Headers being added: " + allHeaders);

        return createHeadersFromMap(allHeaders);
    }

    /**
     * Overloaded method to get default headers without custom headers.
     *
     * @return Default headers.
     */
    public static Headers getHeaders() {
        return getDefaultHeaders(); // Return default headers without any modifications.
    }

    /**
     * Adds a single header to the default set of headers.
     *
     * @param key   The name of the header.
     * @param value The value of the header.
     * @return Headers object containing both default and the provided header.
     */
    public static Headers addHeader(String key, String value) {
        logger.info("Adding header: " + key + " = " + value);
        Map<String, String> allHeaders = new HashMap<>(DEFAULT_HEADERS);
        allHeaders.put(key, value); // Add the custom header

        // Log the header being added
        logger.debug("Header added: " + key + " = " + value);

        return createHeadersFromMap(allHeaders);
    }

    /**
     * Creates a Headers object from a map of header key-value pairs.
     *
     * @param headersMap A map containing header names and their corresponding values.
     * @return Headers object containing the provided headers.
     */
    private static Headers createHeadersFromMap(Map<String, String> headersMap) {
        Header[] headers = headersMap.entrySet().stream()
            .map(entry -> new Header(entry.getKey(), entry.getValue()))
            .toArray(Header[]::new);

        return new Headers(headers);
    }

    /**
     * Retrieves the default headers.
     *
     * @return Default headers.
     */
    public static Headers getDefaultHeaders() {
        logger.info("Getting default headers.");
        return createHeadersFromMap(DEFAULT_HEADERS);
    }
}
