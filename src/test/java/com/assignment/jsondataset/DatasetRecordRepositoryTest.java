
package com.assignment.jsondataset;

import com.assignment.jsondataset.entity.DatasetRecord;
import com.assignment.jsondataset.repository.DatasetRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class DatasetRecordRepositoryTest {

    @Autowired
    private DatasetRecordRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSaveAndRetrieveJsonDatasetRecords() throws Exception {

        JsonNode record = objectMapper.readTree(
                "{\"name\":\"Laptop\",\"category\":\"Electronics\",\"price\":50000}"
        );

        DatasetRecord savedRecord = repository.save(
                new DatasetRecord("products", record)
        );

        assertNotNull(savedRecord.getId());

        List<DatasetRecord> results =
                repository.findByDatasetNameOrderByIdAsc("products");

        assertEquals(1, results.size());
        assertEquals("products", results.get(0).getDatasetName());

        assertEquals(
                "Laptop",
                results.get(0).getRecordData().get("name").asText()
        );

        assertEquals(
                50000,
                results.get(0).getRecordData().get("price").asInt()
        );
    }

    @Test
    void shouldCountRecordsByDatasetName() throws Exception {

        JsonNode firstRecord = objectMapper.readTree(
                "{\"name\":\"Laptop\"}"
        );

        JsonNode secondRecord = objectMapper.readTree(
                "{\"name\":\"Phone\"}"
        );

        repository.saveAll(List.of(
                new DatasetRecord("products", firstRecord),
                new DatasetRecord("products", secondRecord)
        ));

        assertEquals(2, repository.countByDatasetName("products"));
        assertEquals(0, repository.countByDatasetName("unknown"));
    }
}
