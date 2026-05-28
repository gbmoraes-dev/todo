package com.gbmoraes.todo.adapters.outbound.persistence.issue

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IssueJpaRepository : JpaRepository<IssueJpaEntity, String> {
    @Query(
        """
        SELECT t FROM IssueJpaEntity t
        WHERE t.userId = :userId
        AND (:completed IS NULL OR t.completed = :completed)
        AND (:cursor IS NULL OR t.id > :cursor)
        ORDER BY t.id ASC
        LIMIT :limit
    """
    )
    fun findAllByUserIdWithCursor(
        userId: String,
        completed: Boolean?,
        cursor: String?,
        limit: Int,
    ): List<IssueJpaEntity>
}