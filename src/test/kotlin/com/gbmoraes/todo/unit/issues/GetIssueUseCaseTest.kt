package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.GetIssueUseCase
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
class GetIssueUseCaseTest {
    @MockK lateinit var issueRepository: IssueRepository

    @InjectMockKs lateinit var useCase: GetIssueUseCase

    private val issue = Issue(
        id = "issue-id",
        userId = "user-id",
        title = "Buy milk",
        description = "From the store",
        completed = false,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    @Test
    fun `should return issue when found`() {
        every { issueRepository.findById("issue-id") } returns issue

        val result = useCase.execute("issue-id", "user-id")

        assertThat(result.id).isEqualTo("issue-id")
        assertThat(result.title).isEqualTo("Buy milk")
    }

    @Test
    fun `should return issue when userId matches`() {
        every { issueRepository.findById("issue-id") } returns issue

        val result = useCase.execute("issue-id", "user-id")

        assertThat(result.id).isEqualTo("issue-id")
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