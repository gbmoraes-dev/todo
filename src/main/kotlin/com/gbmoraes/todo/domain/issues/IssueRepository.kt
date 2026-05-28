package com.gbmoraes.todo.domain.issues

interface IssueRepository {
    fun save(issue: Issue): Issue
    fun findById(id: String): Issue?
    fun findAllByUserId(
        userId: String,
        completed: Boolean? = null,
        cursor: String? = null,
        limit: Int = 20,
    ): List<Issue>
    fun delete(id: String)
}