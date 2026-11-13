
package com.assignment.jsondataset.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateDatasetRequest {

    @NotBlank(message = "Dataset name cannot be empty")
    private String datasetName;

    @NotEmpty(message = "At least one JSON record is required")
    private List<@NotNull(message = "JSON record cannot be null") JsonNode> records;

    public CreateDatasetRequest() {
    }

    public CreateDatasetRequest(String datasetName, List<JsonNode> records) {
        this.datasetName = datasetName;
        this.records = records;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public List<JsonNode> getRecords() {
        return records;
    }

    public void setRecords(List<JsonNode> records) {
        this.records = records;
    }
}
