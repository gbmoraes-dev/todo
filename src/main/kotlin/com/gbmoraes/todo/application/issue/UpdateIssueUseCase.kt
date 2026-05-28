package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.application.issue.dto.IssueOutput
import com.gbmoraes.todo.application.issue.dto.UpdateIssueInput
import com.gbmoraes.todo.application.issue.dto.toOutput
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository
import java.time.Instant

class UpdateIssueUseCase(
    private val issueRepository: IssueRepository,
) {
    fun execute(input: UpdateIssueInput): IssueOutput {
        val todo = issueRepository.findById(input.id) ?: throw IssueException.NotFound(input.id)

        if (todo.userId != input.userId) {
            throw IssueException.Unauthorized(input.id)
        }

        val updated = todo.copy(
            title = input.title?.takeIf { it.isNotBlank() } ?: todo.title,
            description = input.description ?: todo.description,
            updatedAt = Instant.now(),
        )

        return issueRepository.save(updated).toOutput()
    }
}