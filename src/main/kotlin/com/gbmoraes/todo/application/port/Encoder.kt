package com.gbmoraes.todo.application.port

interface Encoder {
    fun hash(raw: String): String
    fun matches(raw: String, hash: String): Boolean
}