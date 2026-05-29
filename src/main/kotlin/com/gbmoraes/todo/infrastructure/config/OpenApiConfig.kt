package com.gbmoraes.todo.infrastructure.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "bearer-token",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
)
open class OpenApiConfig {
    @Bean
    open fun openAPI(): OpenAPI = OpenAPI().apply {
        servers = listOf(
            Server().apply {
                url = "/"
                description = "Current server"
            }
        )
    }
}