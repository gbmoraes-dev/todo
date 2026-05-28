package com.gbmoraes.todo.adapters.outbound.persistence.user

import com.gbmoraes.todo.domain.user.User
import com.gbmoraes.todo.domain.user.UserRepository
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val jpa: UserJpaRepository,
) : UserRepository {
    override fun save(user: User): User =
        jpa.save(UserJpaEntity.fromDomain(user)).toDomain()

    override fun findByEmail(email: String): User? =
        jpa.findByEmail(email)?.toDomain()

    override fun emailAlreadyTaken(email: String): Boolean =
        jpa.existsByEmail(email)
}