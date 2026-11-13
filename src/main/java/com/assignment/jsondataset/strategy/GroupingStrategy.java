
package com.assignment.jsondataset.strategy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public interface GroupingStrategy {

    Map<String, List<JsonNode>> group(
            List<JsonNode> records,
            String fieldName
    );
}
