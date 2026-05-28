package com.gbmoraes.todo.infrastructure.security

import com.gbmoraes.todo.adapters.outbound.persistence.user.UserJpaRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailService(
    private val userJpaRepository: UserJpaRepository,
) : UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val user = userJpaRepository.findById(id)
            .orElseThrow { UsernameNotFoundException("User not found: $id") }

        return org.springframework.security.core.userdetails.User
            .withUsername(user.id)
            .password(user.password)
            .roles("USER")
            .build()
    }
}