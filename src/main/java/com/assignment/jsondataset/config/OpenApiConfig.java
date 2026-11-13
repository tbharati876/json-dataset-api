
package com.assignment.jsondataset.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI datasetApiDocumentation() {

        return new OpenAPI()
                .info(new Info()
                        .title("JSON Dataset Management API")
                        .description(
                                "REST API for creating, storing, retrieving, "
                                + "grouping and sorting JSON datasets."
                        )
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("JSON Dataset API Support")
                        )
                );
    }
}
