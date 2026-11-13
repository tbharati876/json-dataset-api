
package com.assignment.jsondataset.service.impl;

import com.assignment.jsondataset.dto.request.CreateDatasetRequest;
import com.assignment.jsondataset.dto.response.CreateDatasetResponse;
import com.assignment.jsondataset.dto.response.QueryDatasetResponse;
import com.assignment.jsondataset.entity.DatasetRecord;
import com.assignment.jsondataset.exception.ResourceNotFoundException;
import com.assignment.jsondataset.repository.DatasetRecordRepository;
import com.assignment.jsondataset.service.DatasetRecordService;
import com.assignment.jsondataset.strategy.GroupingStrategy;
import com.assignment.jsondataset.strategy.SortingStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DatasetRecordServiceImpl implements DatasetRecordService {

    private final DatasetRecordRepository repository;
    private final GroupingStrategy groupingStrategy;
    private final SortingStrategy sortingStrategy;

    public DatasetRecordServiceImpl(
            DatasetRecordRepository repository,
            GroupingStrategy groupingStrategy,
            SortingStrategy sortingStrategy) {

        this.repository = repository;
        this.groupingStrategy = groupingStrategy;
        this.sortingStrategy = sortingStrategy;
    }

    @Override
    @Transactional
    public CreateDatasetResponse createDataset(CreateDatasetRequest request) {

        String datasetName = request.getDatasetName().trim();

        if (datasetName.isEmpty()) {
            throw new IllegalArgumentException(
                    "Dataset name cannot be empty.");
        }

        List<JsonNode> records = request.getRecords();

        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one JSON record is required.");
        }

        for (JsonNode record : records) {
            if (record == null || !record.isObject()) {
                throw new IllegalArgumentException(
                        "Every dataset record must be a valid JSON object.");
            }
        }

        LocalDateTime createdAt = LocalDateTime.now();

        List<DatasetRecord> datasetRecords = records.stream()
                .map(record -> new DatasetRecord(datasetName, record))
                .toList();

        List<DatasetRecord> savedRecords =
                repository.saveAll(datasetRecords);

        return new CreateDatasetResponse(
                "Dataset records created successfully.",
                datasetName,
                savedRecords.size(),
                createdAt
        );
    }

    @Override
    @Transactional(readOnly = true)
    public QueryDatasetResponse queryDataset(
            String datasetName,
            String groupBy,
            String sortBy,
            String sortOrder) {

        if (datasetName == null || datasetName.isBlank()) {
            throw new IllegalArgumentException(
                    "Dataset name cannot be empty.");
        }

        String normalizedDatasetName = datasetName.trim();

        List<DatasetRecord> datasetRecords =
                repository.findByDatasetNameOrderByIdAsc(
                        normalizedDatasetName);

        if (datasetRecords.isEmpty()) {
            throw new ResourceNotFoundException(
                    "Dataset not found: " + normalizedDatasetName);
        }

        List<JsonNode> records = datasetRecords.stream()
                .map(DatasetRecord::getRecordData)
                .toList();

        String normalizedSortBy = normalizeField(sortBy);
        String normalizedGroupBy = normalizeField(groupBy);

        String normalizedSortOrder = sortOrder == null
                ? "ASC"
                : sortOrder.trim().toUpperCase(Locale.ROOT);

        if (!normalizedSortOrder.equals("ASC")
                && !normalizedSortOrder.equals("DESC")) {
            throw new IllegalArgumentException(
                    "Sort order must be either ASC or DESC.");
        }

        if (normalizedSortBy != null) {
            records = sortingStrategy.sort(
                    records,
                    normalizedSortBy,
                    normalizedSortOrder
            );
        }

        Object resultData;

        if (normalizedGroupBy != null) {
            Map<String, List<JsonNode>> groupedRecords =
                    groupingStrategy.group(records, normalizedGroupBy);

            resultData = groupedRecords;
        } else {
            resultData = records;
        }

        return new QueryDatasetResponse(
                normalizedDatasetName,
                records.size(),
                normalizedGroupBy,
                normalizedSortBy,
                normalizedSortBy == null ? null : normalizedSortOrder,
                resultData
        );
    }

    private String normalizeField(String fieldName) {

        if (fieldName == null || fieldName.isBlank()) {
            return null;
        }

        return fieldName.trim();
    }
}
