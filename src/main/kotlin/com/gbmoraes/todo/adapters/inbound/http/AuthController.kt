package com.gbmoraes.todo.adapters.inbound.http

import com.gbmoraes.todo.adapters.inbound.http.dto.auth.AuthResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignInRequest
import com.gbmoraes.todo.adapters.inbound.http.dto.auth.SignUpRequest
import com.gbmoraes.todo.application.auth.SignInUseCase
import com.gbmoraes.todo.application.auth.SignUpUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
open class AuthController(
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
) {

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody request: SignUpRequest): AuthResponse =
        signUpUseCase.execute(request.toInput())
            .let { AuthResponse(it.token) }

    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody request: SignInRequest): AuthResponse =
        signInUseCase.execute(request.toInput())
            .let { AuthResponse(it.token) }
}