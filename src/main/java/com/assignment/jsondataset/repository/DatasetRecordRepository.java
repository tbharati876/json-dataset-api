
package com.assignment.jsondataset.repository;

import com.assignment.jsondataset.entity.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasetRecordRepository
        extends JpaRepository<DatasetRecord, Long> {

    List<DatasetRecord> findByDatasetNameOrderByIdAsc(String datasetName);

    boolean existsByDatasetName(String datasetName);

    long countByDatasetName(String datasetName);
}
