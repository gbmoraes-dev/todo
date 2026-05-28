package com.gbmoraes.todo.application.auth.dto

data class SignUpInput(val name: String, val email: String, val password: String)
data class SignInInput(val email: String, val password: String)
data class AuthOutput(val token: String)
