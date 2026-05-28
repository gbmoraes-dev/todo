package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.CompleteIssueUseCase
import com.gbmoraes.todo.domain.issues.Issue
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
class CompleteIssueUseCaseTest {
    @MockK lateinit var issueRepository: IssueRepository

    @InjectMockKs lateinit var useCase: CompleteIssueUseCase

    private val issue = Issue(
        id = "issue-id",
        userId = "user-id",
        title = "Buy milk",
        description = "",
        completed = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    @Test
    fun `should complete issue`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute("issue-id", "user-id")

        assertThat(result.completed).isTrue()
    }

    @Test
    fun `should throw AlreadyCompleted when issue is already completed`() {
        every { issueRepository.findById("issue-id") } returns issue.copy(completed = true)

        assertThrows<IssueException.AlreadyCompleted> {
            useCase.execute("issue-id", "user-id")
        }
    }

    @Test
    fun `should throw Unauthorized when userId does not match`() {
        every { issueRepository.findById("issue-id") } returns issue

        assertThrows<IssueException.Unauthorized> {
            useCase.execute("issue-id", "other-user-id")
        }
    }

    @Test
    fun `should throw NotFound when issue does not exist`() {
        every { issueRepository.findById("issue-id") } returns null

        assertThrows<IssueException.NotFound> {
            useCase.execute("issue-id", "user-id")
        }
    }
}