package com.gbmoraes.todo.application.issue

import com.gbmoraes.todo.application.issue.dto.CreateIssueInput
import com.gbmoraes.todo.application.issue.dto.IssueOutput
import com.gbmoraes.todo.application.port.Id
import com.gbmoraes.todo.domain.issues.Issue
import com.gbmoraes.todo.domain.issues.IssueRepository
import com.gbmoraes.todo.application.issue.dto.toOutput

class CreateIssueUseCase(
    private val issueRepository: IssueRepository,
    private val id: Id,
) {
    fun execute(input: CreateIssueInput): IssueOutput {
        val issue = Issue.create(
            id = id.generate(),
            userId = input.userId,
            title = input.title,
            description = input.description,
        )

        return issueRepository.save(issue).toOutput()
    }
}