package com.gbmoraes.todo.application.auth

import com.gbmoraes.todo.application.auth.dto.AuthOutput
import com.gbmoraes.todo.application.auth.dto.SignUpInput
import com.gbmoraes.todo.application.port.Encoder
import com.gbmoraes.todo.application.port.Id
import com.gbmoraes.todo.application.port.Token
import com.gbmoraes.todo.domain.user.User
import com.gbmoraes.todo.domain.user.UserException
import com.gbmoraes.todo.domain.user.UserRepository

class SignUpUseCase(
    private val userRepository: UserRepository,
    private val id: Id,
    private val encoder: Encoder,
    private val token: Token,
) {
    fun execute(input: SignUpInput): AuthOutput {
        if (userRepository.emailAlreadyTaken((input.email))) {
            throw UserException.EmailAlreadyInUse(input.email)
        }

        val hash = encoder.hash(input.password)

        val user = User.create(
            id = id.generate(),
            name = input.name,
            email = input.email,
            password = hash,
        )

        val saved = userRepository.save(user)

        return AuthOutput(token = token.generate(saved.id))
    }
}