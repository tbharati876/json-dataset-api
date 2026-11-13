
package com.assignment.jsondataset.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class DefaultSortingStrategy implements SortingStrategy {

    @Override
    public List<JsonNode> sort(
            List<JsonNode> records,
            String fieldName,
            String sortOrder) {

        String order = sortOrder == null
                ? "ASC"
                : sortOrder.trim().toUpperCase(Locale.ROOT);

        if (!order.equals("ASC") && !order.equals("DESC")) {
            throw new IllegalArgumentException(
                    "Sort order must be either ASC or DESC.");
        }

        List<JsonNode> sortedRecords = new ArrayList<>(records);

        Comparator<JsonNode> comparator = (first, second) -> {

            JsonNode firstValue = first.path(fieldName);
            JsonNode secondValue = second.path(fieldName);

            boolean firstMissing = firstValue.isMissingNode()
                    || firstValue.isNull();

            boolean secondMissing = secondValue.isMissingNode()
                    || secondValue.isNull();

            if (firstMissing && secondMissing) {
                return 0;
            }

            if (firstMissing) {
                return 1;
            }

            if (secondMissing) {
                return -1;
            }

            int result = compareValues(firstValue, secondValue);

            return order.equals("DESC") ? -result : result;
        };

        sortedRecords.sort(comparator);

        return sortedRecords;
    }

    private int compareValues(JsonNode first, JsonNode second) {

        if (first.isNumber() && second.isNumber()) {
            return first.decimalValue().compareTo(second.decimalValue());
        }

        if (first.isBoolean() && second.isBoolean()) {
            return Boolean.compare(
                    first.booleanValue(),
                    second.booleanValue());
        }

        String firstText = first.asText();
        String secondText = second.asText();

        int result = firstText.compareToIgnoreCase(secondText);

        if (result == 0) {
            result = firstText.compareTo(secondText);
        }

        return result;
    }
}
