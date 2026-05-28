package com.gbmoraes.todo.unit.auth

import com.gbmoraes.todo.application.auth.SignInUseCase
import com.gbmoraes.todo.application.auth.dto.SignInInput
import com.gbmoraes.todo.application.port.Encoder
import com.gbmoraes.todo.application.port.Token
import com.gbmoraes.todo.domain.user.User
import com.gbmoraes.todo.domain.user.UserException
import com.gbmoraes.todo.domain.user.UserRepository
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SignInUseCaseTest {

    @MockK lateinit var userRepository: UserRepository
    @MockK lateinit var encoder: Encoder
    @MockK lateinit var token: Token

    @InjectMockKs lateinit var useCase: SignInUseCase

    private val user = User(
        id = "user-id",
        name = "John Doe",
        email = "johndoe@example.com",
        password = "hashed",
    )

    @Test
    fun `should return token with valid credentials`() {
        every { userRepository.findByEmail("johndoe@example.com") } returns user
        every { encoder.matches("secret123", "hashed") } returns true
        every { token.generate("user-id") } returns "jwt-token"

        val result = useCase.execute(SignInInput("johndoe@example.com", "secret123"))

        assertThat(result.token).isEqualTo("jwt-token")
    }

    @Test
    fun `should throw InvalidCredentials when user not found`() {
        every { userRepository.findByEmail(any()) } returns null

        assertThrows<UserException.InvalidCredentials> {
            useCase.execute(SignInInput("janedoe@example.com", "secret123"))
        }
    }

    @Test
    fun `should throw InvalidCredentials when password is wrong`() {
        every { userRepository.findByEmail(any()) } returns user
        every { encoder.matches("wrong", "hashed") } returns false

        assertThrows<UserException.InvalidCredentials> {
            useCase.execute(SignInInput("johndoe@example.com", "wrong"))
        }
    }
}