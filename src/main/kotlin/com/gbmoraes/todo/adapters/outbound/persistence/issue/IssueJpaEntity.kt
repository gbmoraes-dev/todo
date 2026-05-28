package com.gbmoraes.todo.adapters.outbound.persistence.issue

import com.gbmoraes.todo.domain.issues.Issue
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "issues")
class IssueJpaEntity(
    @Id
    val id: String = "",

    @Column(nullable = false)
    val userId: String = "",

    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = true)
    val description: String = "",

    @Column(nullable = false)
    val completed: Boolean = false,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    val updatedAt: Instant = Instant.now(),
) {
    fun toDomain() = Issue(
        id = id,
        userId = userId,
        title = title,
        description = description,
        completed = completed,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun fromDomain(issue: Issue) = IssueJpaEntity(
            id = issue.id,
            userId = issue.userId,
            title = issue.title,
            description = issue.description,
            completed = issue.completed,
            createdAt = issue.createdAt,
            updatedAt = issue.updatedAt,
        )
    }
}