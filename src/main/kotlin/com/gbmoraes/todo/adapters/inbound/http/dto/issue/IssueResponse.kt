package com.gbmoraes.todo.adapters.inbound.http.dto.issue

import com.gbmoraes.todo.application.issue.dto.IssueOutput
import java.time.Instant

data class IssueResponse(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromOutput(output: IssueOutput) = IssueResponse(
            id = output.id,
            userId = output.userId,
            title = output.title,
            description = output.description,
            completed = output.completed,
            createdAt = output.createdAt,
            updatedAt = output.updatedAt,
        )
    }
}