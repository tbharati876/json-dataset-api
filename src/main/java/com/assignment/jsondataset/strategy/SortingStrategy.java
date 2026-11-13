
package com.assignment.jsondataset.strategy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface SortingStrategy {

    List<JsonNode> sort(
            List<JsonNode> records,
            String fieldName,
            String sortOrder
    );
}
