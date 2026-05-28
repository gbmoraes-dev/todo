package com.gbmoraes.todo.domain.issues

sealed class IssueException(message: String) : RuntimeException(message) {
    class NotFound(id: String) : IssueException("Not found: $id")
    class AlreadyCompleted(id: String) : IssueException("Todo is already completed: $id")
    class EmptyTitle : IssueException("Title cannot be empty")
    class Unauthorized(id: String) : IssueException("You don't have permission to access todo: $id")
}