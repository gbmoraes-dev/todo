package com.gbmoraes.todo.adapters.inbound.http.dto.auth

import com.gbmoraes.todo.application.auth.dto.SignUpInput
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:Email val email: String,
    @field:NotBlank val name: String,
    @field:Size(min = 8) val password: String,
) {
    fun toInput() = SignUpInput(email = email, name = name, password = password)
}