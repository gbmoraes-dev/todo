package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.UpdateIssueUseCase
import com.gbmoraes.todo.application.issue.dto.UpdateIssueInput
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
class UpdateIssueUseCaseTest {
    @MockK lateinit var issueRepository: IssueRepository

    @InjectMockKs lateinit var useCase: UpdateIssueUseCase

    private val issue = Issue(
        id = "issue-id",
        userId = "user-id",
        title = "Old title",
        description = "Old description",
        completed = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    @Test
    fun `should update title when provided`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(UpdateIssueInput("issue-id", "user-id", "New title", null))

        assertThat(result.title).isEqualTo("New title")
        assertThat(result.description).isEqualTo("Old description")
    }

    @Test
    fun `should keep existing values when fields are null`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(UpdateIssueInput("issue-id", "user-id", null, null))

        assertThat(result.title).isEqualTo("Old title")
        assertThat(result.description).isEqualTo("Old description")
    }

    @Test
    fun `should throw NotFound when issue does not exist`() {
        every { issueRepository.findById("issue-id") } returns null

        assertThrows<IssueException.NotFound> {
            useCase.execute(UpdateIssueInput("issue-id", "user-id", "New title", null))
        }
    }

    @Test
    fun `should throw Unauthorized when userId does not match`() {
        every { issueRepository.findById("issue-id") } returns issue

        assertThrows<IssueException.Unauthorized> {
            useCase.execute(UpdateIssueInput("issue-id", "other-user-id", "New title", null))
        }
    }

    @Test
    fun `should keep existing title when new title is blank`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(UpdateIssueInput("issue-id", "user-id", "   ", null))

        assertThat(result.title).isEqualTo("Old title")
    }

    @Test
    fun `should update description when provided`() {
        every { issueRepository.findById("issue-id") } returns issue
        every { issueRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(UpdateIssueInput("issue-id", "user-id", null, "New description"))

        assertThat(result.description).isEqualTo("New description")
    }
}