package com.gbmoraes.todo.application.port

interface Token {
    fun generate(id: String): String
    fun validate(token: String): Boolean
    fun decode(token: String): String
}