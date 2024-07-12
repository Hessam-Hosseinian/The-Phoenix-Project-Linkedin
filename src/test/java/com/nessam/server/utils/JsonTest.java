package com.nessam.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTest {

    @Test
    public void testParseValidJson() {
        String jsonString = "{\"name\":\"John\", \"age\":30}";

        try {
            JsonNode jsonNode = Json.parse(jsonString);
            assertNotNull(jsonNode);
            assertEquals("John", jsonNode.get("name").asText());
            assertEquals(30, jsonNode.get("age").asInt());
        } catch (JsonProcessingException e) {
            fail("JsonProcessingException should not have been thrown for valid JSON string.");
        }
    }

    @Test
    public void testParseInvalidJson() {
        String jsonString = "{name:\"John\", \"age\":30}"; // Invalid JSON (missing quotes around name key)

        assertThrows(JsonProcessingException.class, () -> {
            Json.parse(jsonString);
        });
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"name\":\"John\", \"age\":30}";

        try {
            JsonNode jsonNode = Json.parse(jsonString);
            Person person = Json.fromJson(jsonNode, Person.class);
            assertNotNull(person);
            assertEquals("John", person.getName());
            assertEquals(30, person.getAge());
        } catch (JsonProcessingException e) {
            fail("JsonProcessingException should not have been thrown for valid JSON string.");
        }
    }

    @Test
    public void testJsonModify() {
        String jsonString = "[{\"id\":\"12345\", \"name\":\"Post1\"}, {\"id\":\"67890\", \"name\":\"Post2\"}]";
        String extractedField = Json.jsonModify(jsonString, "id");
        assertEquals("12345", extractedField);

        String invalidJsonString = "{\"id\":\"12345\", \"name\":\"Post1\"}"; // Not an array
        String result = Json.jsonModify(invalidJsonString, "id");
        assertEquals("The JSON is not an array or is empty.", result);
    }

    // Helper class for the fromJson test
    static class Person {
        private String name;
        private int age;

        // Getters and setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
