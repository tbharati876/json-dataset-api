
package com.assignment.jsondataset;

import com.assignment.jsondataset.controller.DatasetRecordController;
import com.assignment.jsondataset.exception.GlobalExceptionHandler;
import com.assignment.jsondataset.exception.ResourceNotFoundException;
import com.assignment.jsondataset.service.DatasetRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private DatasetRecordService datasetRecordService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        DatasetRecordController controller =
                new DatasetRecordController(datasetRecordService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnNotFoundWhenDatasetDoesNotExist() throws Exception {

        when(datasetRecordService.queryDataset(
                "unknown",
                null,
                null,
                "ASC"
        )).thenThrow(
                new ResourceNotFoundException("Dataset not found: unknown")
        );

        mockMvc.perform(get("/api/datasets/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Dataset not found: unknown"));
    }

    @Test
    void shouldReturnBadRequestForInvalidJson() throws Exception {

        mockMvc.perform(post("/api/datasets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Invalid or missing JSON request body."));

        verifyNoInteractions(datasetRecordService);
    }

    @Test
    void shouldReturnBadRequestForInvalidSortOrder() throws Exception {

        when(datasetRecordService.queryDataset(
                "products",
                null,
                "price",
                "INVALID"
        )).thenThrow(
                new IllegalArgumentException(
                        "Sort order must be either ASC or DESC."
                )
        );

        mockMvc.perform(get("/api/datasets/products")
                        .param("sortBy", "price")
                        .param("sortOrder", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Sort order must be either ASC or DESC."));
    }
}
