
package com.assignment.jsondataset.controller;

import com.assignment.jsondataset.dto.request.CreateDatasetRequest;
import com.assignment.jsondataset.dto.response.CreateDatasetResponse;
import com.assignment.jsondataset.dto.response.QueryDatasetResponse;
import com.assignment.jsondataset.service.DatasetRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datasets")
public class DatasetRecordController {

    private final DatasetRecordService datasetRecordService;

    public DatasetRecordController(
            DatasetRecordService datasetRecordService) {
        this.datasetRecordService = datasetRecordService;
    }

    @PostMapping
    public ResponseEntity<CreateDatasetResponse> createDataset(
            @Valid @RequestBody CreateDatasetRequest request) {

        CreateDatasetResponse response =
                datasetRecordService.createDataset(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{datasetName}")
    public ResponseEntity<QueryDatasetResponse> queryDataset(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC")
            String sortOrder) {

        QueryDatasetResponse response =
                datasetRecordService.queryDataset(
                        datasetName,
                        groupBy,
                        sortBy,
                        sortOrder
                );

        return ResponseEntity.ok(response);
    }
}
