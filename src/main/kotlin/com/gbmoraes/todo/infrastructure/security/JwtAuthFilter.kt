package com.gbmoraes.todo.infrastructure.security

import com.gbmoraes.todo.application.port.Token
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter

open class JwtAuthFilter(
    private val token: Token,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
    ) {
        val rawToken = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith("Bearer ") }
            ?.removePrefix("Bearer ")

        if (rawToken != null && token.validate(rawToken)) {
            val id = token.decode(rawToken)
            val userDetails = userDetailsService.loadUserByUsername(id)
            val auth = UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.authorities,
            )

            SecurityContextHolder.getContext().authentication = auth
        }

        chain.doFilter(request, response)
    }
}