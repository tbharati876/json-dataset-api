
package com.assignment.jsondataset;

import com.assignment.jsondataset.dto.request.CreateDatasetRequest;
import com.assignment.jsondataset.dto.response.CreateDatasetResponse;
import com.assignment.jsondataset.dto.response.QueryDatasetResponse;
import com.assignment.jsondataset.entity.DatasetRecord;
import com.assignment.jsondataset.exception.ResourceNotFoundException;
import com.assignment.jsondataset.repository.DatasetRecordRepository;
import com.assignment.jsondataset.service.impl.DatasetRecordServiceImpl;
import com.assignment.jsondataset.strategy.GroupingStrategy;
import com.assignment.jsondataset.strategy.SortingStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetRecordServiceImplTest {

    @Mock
    private DatasetRecordRepository repository;

    @Mock
    private GroupingStrategy groupingStrategy;

    @Mock
    private SortingStrategy sortingStrategy;

    @InjectMocks
    private DatasetRecordServiceImpl service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private JsonNode record1;
    private JsonNode record2;

    @BeforeEach
    void setUp() throws Exception {
        record1 = objectMapper.readTree(
                "{\"name\":\"Laptop\",\"category\":\"Electronics\",\"price\":50000}"
        );

        record2 = objectMapper.readTree(
                "{\"name\":\"Phone\",\"category\":\"Electronics\",\"price\":20000}"
        );
    }

    @Test
    void createDatasetShouldSaveRecordsSuccessfully() {

        CreateDatasetRequest request = new CreateDatasetRequest(
                "products",
                List.of(record1, record2)
        );

        when(repository.saveAll(anyList())).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        CreateDatasetResponse response = service.createDataset(request);

        assertEquals("products", response.getDatasetName());
        assertEquals(2, response.getRecordsInserted());
        assertNotNull(response.getCreatedAt());

        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    void createDatasetShouldRejectEmptyDatasetName() {

        CreateDatasetRequest request = new CreateDatasetRequest(
                "   ",
                List.of(record1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createDataset(request)
        );

        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void createDatasetShouldRejectEmptyRecords() {

        CreateDatasetRequest request = new CreateDatasetRequest(
                "products",
                List.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createDataset(request)
        );

        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void queryDatasetShouldReturnAllRecords() {

        List<DatasetRecord> databaseRecords = List.of(
                new DatasetRecord("products", record1),
                new DatasetRecord("products", record2)
        );

        when(repository.findByDatasetNameOrderByIdAsc("products"))
                .thenReturn(databaseRecords);

        QueryDatasetResponse response = service.queryDataset(
                "products",
                null,
                null,
                "ASC"
        );

        assertEquals("products", response.getDatasetName());
        assertEquals(2, response.getTotalRecords());
        assertNull(response.getGroupBy());
        assertNull(response.getSortBy());
        assertInstanceOf(List.class, response.getData());

        verify(repository).findByDatasetNameOrderByIdAsc("products");
    }

    @Test
    void queryDatasetShouldApplySorting() {

        List<DatasetRecord> databaseRecords = List.of(
                new DatasetRecord("products", record1),
                new DatasetRecord("products", record2)
        );

        when(repository.findByDatasetNameOrderByIdAsc("products"))
                .thenReturn(databaseRecords);

        when(sortingStrategy.sort(
                anyList(),
                eq("price"),
                eq("DESC")
        )).thenReturn(List.of(record1, record2));

        QueryDatasetResponse response = service.queryDataset(
                "products",
                null,
                "price",
                "DESC"
        );

        assertEquals("price", response.getSortBy());
        assertEquals("DESC", response.getSortOrder());

        verify(sortingStrategy).sort(anyList(), eq("price"), eq("DESC"));
    }

    @Test
    void queryDatasetShouldApplyGrouping() {

        List<DatasetRecord> databaseRecords = List.of(
                new DatasetRecord("products", record1),
                new DatasetRecord("products", record2)
        );

        when(repository.findByDatasetNameOrderByIdAsc("products"))
                .thenReturn(databaseRecords);

        when(groupingStrategy.group(anyList(), eq("category")))
                .thenReturn(Map.of(
                        "Electronics",
                        List.of(record1, record2)
                ));

        QueryDatasetResponse response = service.queryDataset(
                "products",
                "category",
                null,
                "ASC"
        );

        assertEquals("category", response.getGroupBy());
        assertInstanceOf(Map.class, response.getData());

        verify(groupingStrategy).group(anyList(), eq("category"));
    }

    @Test
    void queryDatasetShouldRejectInvalidSortOrder() {

        List<DatasetRecord> databaseRecords = List.of(
                new DatasetRecord("products", record1)
        );

        when(repository.findByDatasetNameOrderByIdAsc("products"))
                .thenReturn(databaseRecords);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.queryDataset(
                        "products",
                        null,
                        "price",
                        "INVALID"
                )
        );

        verifyNoInteractions(sortingStrategy);
    }

    @Test
    void queryDatasetShouldThrowExceptionWhenDatasetDoesNotExist() {

        when(repository.findByDatasetNameOrderByIdAsc("unknown"))
                .thenReturn(List.of());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.queryDataset(
                        "unknown",
                        null,
                        null,
                        "ASC"
                )
        );
    }
}
