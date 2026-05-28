package com.gbmoraes.todo.application.issue.dto

import com.gbmoraes.todo.domain.issues.Issue
import java.time.Instant

data class CreateIssueInput(val userId: String, val title: String, val description: String)
data class UpdateIssueInput(val id: String, val userId: String, val title: String?, val description: String?)
data class ListIssueInput(
    val userId: String,
    val completed: Boolean? = null,
    val cursor: String? = null,
    val limit: Int = 20,
)
data class ListIssueOutput(
    val items: List<IssueOutput>,
    val nextCursor: String?,
    val hasMore: Boolean,
)
data class IssueOutput(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun Issue.toOutput() = IssueOutput(
    id = id,
    userId = userId,
    title = title,
    description = description,
    completed = completed,
    createdAt = createdAt,
    updatedAt = updatedAt,
)