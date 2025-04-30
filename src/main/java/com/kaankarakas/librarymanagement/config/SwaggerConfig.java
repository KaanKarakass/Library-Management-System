package com.kaankarakas.librarymanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Library Management API")
                        .version("v1")
                        .description("API documentation for Library Management System"));
    }

    @Bean
    public GroupedOpenApi apiDocs() {
        return GroupedOpenApi.builder()
                .group("library-api")
                .pathsToMatch("/api/**")
                .build();
    }
}
