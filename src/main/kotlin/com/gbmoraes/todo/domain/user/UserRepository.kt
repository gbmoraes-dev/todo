package com.gbmoraes.todo.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findByEmail(email: String): User?
    fun emailAlreadyTaken(email: String): Boolean
}