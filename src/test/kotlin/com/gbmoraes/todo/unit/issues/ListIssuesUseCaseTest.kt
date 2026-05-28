package com.gbmoraes.todo.unit.issues

import com.gbmoraes.todo.application.issue.ListIssuesUseCase
import com.gbmoraes.todo.application.issue.dto.ListIssueInput
import com.gbmoraes.todo.domain.issues.Issue
import com.gbmoraes.todo.domain.issues.IssueRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant

@ExtendWith(MockKExtension::class)
class ListIssuesUseCaseTest {
    @MockK lateinit var issueRepository: IssueRepository

    @InjectMockKs lateinit var useCase: ListIssuesUseCase

    private fun makeIssue(id: String, completed: Boolean = false) = Issue(
        id = id,
        userId = "user-id",
        title = "Issue $id",
        description = "",
        completed = completed,
        createdAt = Instant.now(),
        updatedAt = Instant.now(),
    )

    @Test
    fun `should return paginated issues`() {
        val issues = (1..3).map { makeIssue("issue-$it") }

        every {
            issueRepository.findAllByUserId("user-id", null, null, 21)
        } returns issues

        val result = useCase.execute(ListIssueInput("user-id", limit = 20))

        assertThat(result.items).hasSize(3)
        assertThat(result.hasMore).isFalse()
        assertThat(result.nextCursor).isNull()
    }

    @Test
    fun `should return hasMore true when there are more items`() {
        val issues = (1..21).map { makeIssue("issue-$it") }

        every {
            issueRepository.findAllByUserId("user-id", null, null, 21)
        } returns issues

        val result = useCase.execute(ListIssueInput("user-id", limit = 20))

        assertThat(result.items).hasSize(20)
        assertThat(result.hasMore).isTrue()
        assertThat(result.nextCursor).isEqualTo("issue-20")
    }

    @Test
    fun `should filter by completed`() {
        val issues = listOf(makeIssue("issue-1", completed = true))

        every {
            issueRepository.findAllByUserId("user-id", true, null, 21)
        } returns issues

        val result = useCase.execute(ListIssueInput("user-id", completed = true, limit = 20))

        assertThat(result.items).hasSize(1)
        assertThat(result.items.first().completed).isTrue()
    }

    @Test
    fun `should return empty list when user has no issues`() {
        every {
            issueRepository.findAllByUserId("user-id", null, null, 21)
        } returns emptyList()

        val result = useCase.execute(ListIssueInput("user-id", limit = 20))

        assertThat(result.items).isEmpty()
        assertThat(result.hasMore).isFalse()
    }
}