package utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * Utility class to perform repetitive operations. In this case, building HTTP responses
 */
public class Util {

    /**
     * This method, we'll be creating standard JSON responses with a boolean isSuccessful key and the response body
     * @param response
     * @param ok
     * @return
     */
    public static ObjectNode createResponse(Object response, boolean ok) {
        ObjectNode result = Json.newObject();
        result.put("isSuccessful", ok);
        if (response instanceof String) {
            result.put("body", (String) response);
        } else {
            result.putPOJO("body", response);
        }
        return result;
    }
}