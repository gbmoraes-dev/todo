package com.gbmoraes.todo.integration.auth

import com.gbmoraes.todo.adapters.inbound.http.dto.auth.AuthResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignInRequest
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignUpRequest
import com.gbmoraes.todo.config.IntegrationTestBase
import com.gbmoraes.todo.integration.auth.helper.AuthHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class AuthControllerIntegrationTest : IntegrationTestBase() {
    private lateinit var authHelper: AuthHelper

    @BeforeEach
    fun setup() {
        authHelper = AuthHelper(restTemplate)
    }

    // ── sign up ───────────────────────────────────────────────────────────────

    @Test
    fun `POST sign-up should return 201 and token`() {
        val response = restTemplate.postForEntity(
            "/auth/sign-up",
            SignUpRequest("johndoe@test.com", "John Doe", "secret123"),
            AuthResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.token).isNotBlank()
    }

    @Test
    fun `POST sign-up should return 409 when email already exists`() {
        authHelper.signUp("johndoe@test.com", "secret123")

        val response = restTemplate.postForEntity(
            "/auth/sign-up",
            SignUpRequest("johndoe@test.com", "John Doe", "secret123"),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CONFLICT)
    }

    @Test
    fun `POST sign-up should return 422 when body is invalid`() {
        val response = restTemplate.postForEntity(
            "/auth/sign-up",
            SignUpRequest("not-an-email", "John Doe", "123"),
            String::class.java,
        )

        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    // ── sign in ───────────────────────────────────────────────────────────────

    @Test
    fun `POST sign-in should return 200 and token`() {
        authHelper.signUp("johndoe@test.com", "secret123")

        val response = restTemplate.postForEntity(
            "/auth/sign-in",
            SignInRequest("johndoe@test.com", "secret123"),
            AuthResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.token).isNotBlank()
    }

    @Test
    fun `POST sign-in should return 401 with wrong password`() {
        authHelper.signUp("johndoe@test.com", "secret123")

        val response = restTemplate.postForEntity(
            "/auth/sign-in",
            SignInRequest("johndoe@test.com", "wrongpassword"),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `POST sign-in should return 401 for non-existent user`() {
        val response = restTemplate.postForEntity(
            "/auth/sign-in",
            SignInRequest("ghost@test.com", "secret123"),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }
}