
package com.assignment.jsondataset.dto.response;

import java.time.LocalDateTime;

public class CreateDatasetResponse {

    private String message;
    private String datasetName;
    private int recordsInserted;
    private LocalDateTime createdAt;

    public CreateDatasetResponse() {
    }

    public CreateDatasetResponse(
            String message,
            String datasetName,
            int recordsInserted,
            LocalDateTime createdAt) {
        this.message = message;
        this.datasetName = datasetName;
        this.recordsInserted = recordsInserted;
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public int getRecordsInserted() {
        return recordsInserted;
    }

    public void setRecordsInserted(int recordsInserted) {
        this.recordsInserted = recordsInserted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
