package com.gbmoraes.todo.adapters.outbound.persistence.user

import com.gbmoraes.todo.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserJpaEntity(
    @Id
    val id: String = "",

    @Column(unique = true, nullable = false)
    val email: String = "",

    @Column(nullable = false)
    val name: String = "",

    @Column(nullable = false)
    val password: String = "",
) {
    fun toDomain() = User(
        id = id,
        email = email,
        name = name,
        password = password,
    )

    companion object {
        fun fromDomain(user: User) = UserJpaEntity(
            id = user.id,
            email = user.email,
            name = user.name,
            password = user.password,
        )
    }
}