package com.gbmoraes.todo.adapters.inbound.http.dto.auth

import com.gbmoraes.todo.application.auth.dto.SignInInput
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:Email val email: String,
    @field:NotBlank val password: String,
) {
    fun toInput() = SignInInput(email = email, password = password)
}