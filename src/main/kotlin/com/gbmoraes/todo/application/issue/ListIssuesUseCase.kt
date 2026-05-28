package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.application.issue.dto.ListIssueInput
import com.gbmoraes.todo.application.issue.dto.ListIssueOutput
import com.gbmoraes.todo.application.issue.dto.toOutput
import com.gbmoraes.todo.domain.issues.IssueRepository

class ListIssuesUseCase(
    private val issueRepository: IssueRepository,
) {
    fun execute(input: ListIssueInput): ListIssueOutput {
        val items = issueRepository.findAllByUserId(
            userId = input.userId,
            completed = input.completed,
            cursor = input.cursor,
            limit = input.limit + 1,
        )

        val hasMore = items.size > input.limit
        val page = if (hasMore) items.dropLast(1) else items

        return ListIssueOutput(
            items = page.map { it.toOutput() },
            nextCursor = if (hasMore) page.last().id else null,
            hasMore = hasMore,
        )
    }
}