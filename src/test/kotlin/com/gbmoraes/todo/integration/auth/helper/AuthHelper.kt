package com.gbmoraes.todo.integration.auth.helper

import com.gbmoraes.todo.adapters.inbound.http.dto.auth.AuthResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignUpRequest
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class AuthHelper(private val restTemplate: TestRestTemplate) {
    fun signUp(email: String = "johndoe@test.com", password: String = "secret123"): String {
        val response = restTemplate.postForEntity(
            "/auth/sign-up",
            SignUpRequest(email = email, name = "John Doe", password = password),
            AuthResponse::class.java,
        )
        return response.body!!.token
    }

    fun authHeaders(token: String): HttpHeaders = HttpHeaders().apply {
        setBearerAuth(token)
        contentType = MediaType.APPLICATION_JSON
    }
}