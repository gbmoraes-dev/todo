package com.gbmoraes.todo.adapters.outbound.security

import com.gbmoraes.todo.application.port.Encoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
open class BCrypt(
    private val encoder: PasswordEncoder,
) : Encoder {
    override fun hash(raw: String): String = encoder.encode(raw)!!

    override fun matches(raw: String, hash: String): Boolean = encoder.matches(raw, hash)
}