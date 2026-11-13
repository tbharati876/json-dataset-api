
package com.assignment.jsondataset.service;

import com.assignment.jsondataset.dto.request.CreateDatasetRequest;
import com.assignment.jsondataset.dto.response.CreateDatasetResponse;
import com.assignment.jsondataset.dto.response.QueryDatasetResponse;

public interface DatasetRecordService {

    CreateDatasetResponse createDataset(CreateDatasetRequest request);

    QueryDatasetResponse queryDataset(
            String datasetName,
            String groupBy,
            String sortBy,
            String sortOrder
    );
}
