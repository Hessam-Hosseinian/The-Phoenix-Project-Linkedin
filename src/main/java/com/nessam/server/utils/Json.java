package com.nessam.server.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
    private static final ObjectMapper myObjectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return myObjectMapper.readTree(jsonSrc);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node, clazz);
    }

    public static String jsonModify(String jsonString, String field) {
        try {
            // Example


            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Parse the JSON string into a JsonNode
            JsonNode rootNode = objectMapper.readTree(jsonString);

            // Extract a specific value (e.g., extract the value of "id" from the first post)
            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstPost = rootNode.get(0);
                String idValue = firstPost.get(field).asText();  // Adjust the field name as needed

                // Print the extracted value
                return idValue;
            } else {
                return "The JSON is not an array or is empty.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "The JSON is not an array or is empty.";
        }
    }
}
//this is a test comment