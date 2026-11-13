
package com.assignment.jsondataset;

import com.assignment.jsondataset.controller.DatasetRecordController;
import com.assignment.jsondataset.dto.request.CreateDatasetRequest;
import com.assignment.jsondataset.dto.response.CreateDatasetResponse;
import com.assignment.jsondataset.dto.response.QueryDatasetResponse;
import com.assignment.jsondataset.service.DatasetRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DatasetRecordControllerTest {

    @Mock
    private DatasetRecordService datasetRecordService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {

        DatasetRecordController controller =
                new DatasetRecordController(datasetRecordService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(
                        new MappingJackson2HttpMessageConverter(objectMapper)
                )
                .build();
    }

    @Test
    void createDatasetShouldReturnCreatedResponse() throws Exception {

        CreateDatasetResponse response = new CreateDatasetResponse(
                "Dataset records created successfully.",
                "products",
                2,
                LocalDateTime.now()
        );

        when(datasetRecordService.createDataset(any(CreateDatasetRequest.class)))
                .thenReturn(response);

        String requestBody = """
                {
                    "datasetName": "products",
                    "records": [
                        {"name": "Laptop", "price": 50000},
                        {"name": "Phone", "price": 20000}
                    ]
                }
                """;

        mockMvc.perform(post("/api/datasets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.datasetName").value("products"))
                .andExpect(jsonPath("$.recordsInserted").value(2));

        verify(datasetRecordService).createDataset(
                any(CreateDatasetRequest.class)
        );
    }

    @Test
    void queryDatasetShouldReturnRecords() throws Exception {

        QueryDatasetResponse response = new QueryDatasetResponse(
                "products",
                2,
                null,
                null,
                null,
                List.of(
                        Map.of("name", "Laptop", "price", 50000),
                        Map.of("name", "Phone", "price", 20000)
                )
        );

        when(datasetRecordService.queryDataset(
                eq("products"),
                isNull(),
                isNull(),
                eq("ASC")
        )).thenReturn(response);

        mockMvc.perform(get("/api/datasets/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.datasetName").value("products"))
                .andExpect(jsonPath("$.totalRecords").value(2));

        verify(datasetRecordService).queryDataset(
                "products",
                null,
                null,
                "ASC"
        );
    }

    @Test
    void queryDatasetShouldAcceptGroupingAndSortingParameters()
            throws Exception {

        QueryDatasetResponse response = new QueryDatasetResponse(
                "products",
                2,
                "category",
                "price",
                "DESC",
                Map.of(
                        "Electronics",
                        List.of(
                                Map.of("name", "Laptop", "price", 50000),
                                Map.of("name", "Phone", "price", 20000)
                        )
                )
        );

        when(datasetRecordService.queryDataset(
                "products",
                "category",
                "price",
                "DESC"
        )).thenReturn(response);

        mockMvc.perform(get("/api/datasets/products")
                        .param("groupBy", "category")
                        .param("sortBy", "price")
                        .param("sortOrder", "DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupBy").value("category"))
                .andExpect(jsonPath("$.sortBy").value("price"))
                .andExpect(jsonPath("$.sortOrder").value("DESC"));

        verify(datasetRecordService).queryDataset(
                "products",
                "category",
                "price",
                "DESC"
        );
    }

    @Test
    void createDatasetShouldRejectInvalidRequest() throws Exception {

        String requestBody = """
                {
                    "datasetName": "",
                    "records": []
                }
                """;

        mockMvc.perform(post("/api/datasets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(datasetRecordService);
    }
}
