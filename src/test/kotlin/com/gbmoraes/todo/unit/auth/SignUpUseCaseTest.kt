package com.gbmoraes.todo.unit.auth

import com.gbmoraes.todo.application.auth.SignUpUseCase
import com.gbmoraes.todo.application.auth.dto.SignUpInput
import com.gbmoraes.todo.application.port.Encoder
import com.gbmoraes.todo.application.port.Id
import com.gbmoraes.todo.application.port.Token
import com.gbmoraes.todo.domain.user.UserException
import com.gbmoraes.todo.domain.user.UserRepository
import io.mockk.every
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SignUpUseCaseTest {
    @MockK lateinit var userRepository: UserRepository
    @MockK lateinit var encoder: Encoder
    @MockK lateinit var token: Token
    @MockK lateinit var id: Id

    @InjectMockKs lateinit var useCase: SignUpUseCase

    @Test
    fun `should create user and return token`() {
        every { userRepository.emailAlreadyTaken(any()) } returns false
        every { id.generate() } returns "user-id"
        every { encoder.hash(any()) } returns "hashed"
        every { userRepository.save(any()) } returnsArgument 0
        every { token.generate("user-id") } returns "jwt-token"

        val result = useCase.execute(SignUpInput("John Doe", "johndoe@example.com", "secret123"))

        assertThat(result.token).isEqualTo("jwt-token")
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `should throw EmailAlreadyInUse when email is taken`() {
        every { userRepository.emailAlreadyTaken("johndoe@example.com") } returns true

        assertThrows<UserException.EmailAlreadyInUse> {
            useCase.execute(SignUpInput("John Doe", "johndoe@example.com", "secret123"))
        }
    }
}