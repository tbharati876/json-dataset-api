
package com.assignment.jsondataset.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultGroupingStrategy implements GroupingStrategy {

    @Override
    public Map<String, List<JsonNode>> group(
            List<JsonNode> records,
            String fieldName) {

        Map<String, List<JsonNode>> groupedRecords = new LinkedHashMap<>();

        for (JsonNode record : records) {

            JsonNode fieldValue = record.path(fieldName);

            String groupKey;

            if (fieldValue.isMissingNode() || fieldValue.isNull()) {
                groupKey = "null";
            } else if (fieldValue.isValueNode()) {
                groupKey = fieldValue.asText();
            } else {
                groupKey = fieldValue.toString();
            }

            groupedRecords
                    .computeIfAbsent(groupKey, key -> new ArrayList<>())
                    .add(record);
        }

        return groupedRecords;
    }
}
