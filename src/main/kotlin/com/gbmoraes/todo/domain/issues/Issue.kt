package com.gbmoraes.todo.domain.issues

import java.time.Instant

data class Issue(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val completed: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun create(id: String, userId: String, title: String, description: String): Issue {
            if (title.isEmpty()) {
                throw IssueException.EmptyTitle()
            }

            return Issue(
                id = id,
                userId = userId,
                title = title,
                description = description,
                completed = false,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
            )
        }
    }
}