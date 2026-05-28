package com.gbmoraes.todo.application.auth

import com.gbmoraes.todo.application.auth.dto.AuthOutput
import com.gbmoraes.todo.application.auth.dto.SignInInput
import com.gbmoraes.todo.application.port.Encoder
import com.gbmoraes.todo.application.port.Token
import com.gbmoraes.todo.domain.user.UserException
import com.gbmoraes.todo.domain.user.UserRepository

class SignInUseCase(
    private val userRepository: UserRepository,
    private val encoder: Encoder,
    private val token: Token,
) {
    fun execute(input: SignInInput): AuthOutput {
        val user = userRepository.findByEmail(input.email) ?: throw UserException.InvalidCredentials()

        if (!encoder.matches(input.password, user.password)) {
            throw UserException.InvalidCredentials()
        }

        return AuthOutput(token = token.generate(user.id))
    }
}