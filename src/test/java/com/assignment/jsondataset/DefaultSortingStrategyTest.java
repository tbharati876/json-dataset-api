
package com.assignment.jsondataset;

import com.assignment.jsondataset.strategy.DefaultSortingStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSortingStrategyTest {

    private DefaultSortingStrategy sortingStrategy;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        sortingStrategy = new DefaultSortingStrategy();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldSortNumbersInAscendingOrder() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"name\":\"Laptop\",\"price\":50000}"),
                objectMapper.readTree("{\"name\":\"Phone\",\"price\":20000}"),
                objectMapper.readTree("{\"name\":\"Tablet\",\"price\":30000}")
        );

        List<JsonNode> result =
                sortingStrategy.sort(records, "price", "ASC");

        assertEquals(20000, result.get(0).get("price").asInt());
        assertEquals(30000, result.get(1).get("price").asInt());
        assertEquals(50000, result.get(2).get("price").asInt());
    }

    @Test
    void shouldSortNumbersInDescendingOrder() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"price\":20000}"),
                objectMapper.readTree("{\"price\":50000}"),
                objectMapper.readTree("{\"price\":30000}")
        );

        List<JsonNode> result =
                sortingStrategy.sort(records, "price", "DESC");

        assertEquals(50000, result.get(0).get("price").asInt());
        assertEquals(30000, result.get(1).get("price").asInt());
        assertEquals(20000, result.get(2).get("price").asInt());
    }

    @Test
    void shouldSortTextInAscendingOrder() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"name\":\"Phone\"}"),
                objectMapper.readTree("{\"name\":\"Laptop\"}"),
                objectMapper.readTree("{\"name\":\"Tablet\"}")
        );

        List<JsonNode> result =
                sortingStrategy.sort(records, "name", "ASC");

        assertEquals("Laptop", result.get(0).get("name").asText());
        assertEquals("Phone", result.get(1).get("name").asText());
        assertEquals("Tablet", result.get(2).get("name").asText());
    }

    @Test
    void shouldPlaceMissingValuesAtTheEnd() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"name\":\"Unknown\"}"),
                objectMapper.readTree("{\"name\":\"Laptop\",\"price\":50000}"),
                objectMapper.readTree("{\"name\":\"Phone\",\"price\":20000}")
        );

        List<JsonNode> result =
                sortingStrategy.sort(records, "price", "ASC");

        assertEquals(20000, result.get(0).get("price").asInt());
        assertEquals(50000, result.get(1).get("price").asInt());
        assertFalse(result.get(2).has("price"));
    }

    @Test
    void shouldRejectInvalidSortOrder() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"price\":100}")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> sortingStrategy.sort(records, "price", "INVALID")
        );
    }

    @Test
    void shouldNotModifyOriginalRecords() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"price\":500}"),
                objectMapper.readTree("{\"price\":100}")
        );

        sortingStrategy.sort(records, "price", "ASC");

        assertEquals(500, records.get(0).get("price").asInt());
        assertEquals(100, records.get(1).get("price").asInt());
    }
}
