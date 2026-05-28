package com.gbmoraes.todo.adapters.outbound.persistence.issue

import com.gbmoraes.todo.domain.issues.Issue
import com.gbmoraes.todo.domain.issues.IssueRepository
import org.springframework.stereotype.Component

@Component
open class IssueRepositoryImpl(
    private val jpa: IssueJpaRepository,
) : IssueRepository {

    override fun save(issue: Issue): Issue =
        jpa.save(IssueJpaEntity.fromDomain(issue)).toDomain()

    override fun findById(id: String): Issue? =
        jpa.findById(id).orElse(null)?.toDomain()

    override fun findAllByUserId(
        userId: String,
        completed: Boolean?,
        cursor: String?,
        limit: Int,
    ): List<Issue> =
        jpa.findAllByUserIdWithCursor(userId, completed, cursor, limit)
            .map { it.toDomain() }

    override fun delete(id: String) =
        jpa.deleteById(id)
}