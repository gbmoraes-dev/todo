package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository

class DeleteIssueUseCase(
    private val issueRepository: IssueRepository,
) {
    fun execute(id: String, userId: String) {
        val todo = issueRepository.findById(id) ?: throw IssueException.NotFound(id)

        if (todo.userId != userId) {
            throw IssueException.Unauthorized(id)
        }

        issueRepository.delete(id)
    }
}