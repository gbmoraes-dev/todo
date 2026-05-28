package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.application.issue.dto.IssueOutput
import com.gbmoraes.todo.application.issue.dto.toOutput
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository
import java.time.Instant

class CompleteIssueUseCase(
    private val issueRepository: IssueRepository,
) {
    fun execute(id: String, userId: String): IssueOutput {
        val todo = issueRepository.findById(id)  ?: throw IssueException.NotFound(id)

        if (todo.userId != userId) {
            throw IssueException.Unauthorized(id)
        }

        if (todo.completed) {
            throw IssueException.AlreadyCompleted(id)
        }

        val completed = todo.copy(
            completed = true,
            updatedAt = Instant.now(),
        )

        return issueRepository.save(completed).toOutput()
    }
}