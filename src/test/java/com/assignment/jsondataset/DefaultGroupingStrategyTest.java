
package com.assignment.jsondataset;

import com.assignment.jsondataset.strategy.DefaultGroupingStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DefaultGroupingStrategyTest {

    private DefaultGroupingStrategy groupingStrategy;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        groupingStrategy = new DefaultGroupingStrategy();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGroupRecordsByCategory() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree(
                        "{\"name\":\"Laptop\",\"category\":\"Electronics\"}"
                ),
                objectMapper.readTree(
                        "{\"name\":\"Phone\",\"category\":\"Electronics\"}"
                ),
                objectMapper.readTree(
                        "{\"name\":\"Chair\",\"category\":\"Furniture\"}"
                )
        );

        Map<String, List<JsonNode>> result =
                groupingStrategy.group(records, "category");

        assertEquals(2, result.size());
        assertEquals(2, result.get("Electronics").size());
        assertEquals(1, result.get("Furniture").size());
    }

    @Test
    void shouldPlaceMissingFieldRecordsInNullGroup() throws Exception {

        List<JsonNode> records = List.of(
                objectMapper.readTree("{\"name\":\"Laptop\"}"),
                objectMapper.readTree("{\"name\":\"Phone\",\"category\":\"Electronics\"}")
        );

        Map<String, List<JsonNode>> result =
                groupingStrategy.group(records, "category");

        assertTrue(result.containsKey("null"));
        assertEquals(1, result.get("null").size());
        assertEquals(1, result.get("Electronics").size());
    }

    @Test
    void shouldReturnEmptyMapForEmptyRecords() {

        Map<String, List<JsonNode>> result =
                groupingStrategy.group(List.of(), "category");

        assertTrue(result.isEmpty());
    }
}
