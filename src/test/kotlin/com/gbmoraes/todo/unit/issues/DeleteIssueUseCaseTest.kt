package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.DeleteIssueUseCase
import com.gbmoraes.todo.domain.issues.Issue
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
class DeleteIssueUseCaseTest {
    @MockK lateinit var issueRepository: IssueRepository

    @InjectMockKs lateinit var useCase: DeleteIssueUseCase

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
    fun `should delete issue`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.delete("issue-id") } just runs

        useCase.execute("issue-id", "user-id")

        verify(exactly = 1) { issueRepository.delete("issue-id") }
    }

    @Test
    fun `should throw NotFound when issue does not exist`() {
        every { issueRepository.findById("issue-id") } returns null

        assertThrows<IssueException.NotFound> {
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
}