package com.gbmoraes.todo.domain.user

sealed class UserException(message: String) : RuntimeException(message) {
    class EmailAlreadyInUse(email: String) : UserException("Email already in use: $email")
    class InvalidCredentials : UserException("Invalid credentials")
}