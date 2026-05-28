package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.CreateIssueUseCase
import com.gbmoraes.todo.application.issue.dto.CreateIssueInput
import com.gbmoraes.todo.application.port.Id
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateIssueUseCaseTest {

    @MockK(relaxed = true) lateinit var issueRepository: IssueRepository
    @MockK(relaxed = true) lateinit var id: Id

    @InjectMockKs lateinit var useCase: CreateIssueUseCase

    @Test
    fun `should create issue and return output`() {
        every { id.generate() } returns "issue-id"
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(CreateIssueInput("user-id", "Buy milk", "From the store"))

        assertThat(result.id).isEqualTo("issue-id")
        assertThat(result.title).isEqualTo("Buy milk")
        assertThat(result.completed).isFalse()
        verify(exactly = 1) { issueRepository.save(any()) }
    }

    @Test
    fun `should throw EmptyTitle when title is blank`() {
        assertThrows<IssueException.EmptyTitle> {
            useCase.execute(CreateIssueInput("user-id", "", "description"))
        }
    }
}