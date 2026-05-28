package com.gbmoraes.todo.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.AuthResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignUpRequest
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
abstract class IntegrationTestBase {
    companion object {
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:17-alpine").apply {
            withDatabaseName("todo_test")
            withUsername("test")
            withPassword("test")
            start()
        }

        @DynamicPropertySource
        @JvmStatic
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.flyway.enabled") { "true" }
            registry.add("jwt.secret") { "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970" }
        }
    }

    @Autowired lateinit var restTemplate: TestRestTemplate
    @Autowired lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun cleanUp() {
        jdbcTemplate.execute("DELETE FROM issues")
        jdbcTemplate.execute("DELETE FROM users")
    }
}