package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.application.issue.dto.IssueOutput
import com.gbmoraes.todo.application.issue.dto.toOutput
import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.issues.IssueRepository

class GetIssueUseCase(
    val issueRepository: IssueRepository,
) {
    fun execute(id: String, userId: String): IssueOutput {
        val todo = issueRepository.findById(id) ?: throw IssueException.NotFound(id)

        if (todo.userId != userId) {
            throw IssueException.Unauthorized(id)
        }

        return todo.toOutput()
    }
}