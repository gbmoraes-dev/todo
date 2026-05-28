package com.gbmoraes.todo.domain.user

import java.util.UUID

data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
) {
    companion object {
        fun create(id: String, name: String, email: String, password: String) = User(
            id = id,
            name = name,
            email = email,
            password = password
        )
    }
}
