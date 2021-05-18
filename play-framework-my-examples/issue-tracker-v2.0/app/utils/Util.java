package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Util {
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

    public static JsonNode createJsonNode(Object node) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonData = mapper.convertValue(node, JsonNode.class);
        return jsonData;
    }

    public static <T> T mapToObject(JsonNode node, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(node, tClass);
    }
}
