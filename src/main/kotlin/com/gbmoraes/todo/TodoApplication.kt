package com.gbmoraes.todo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TodoApplication

fun main(args: Array<String>) {
	runApplication<TodoApplication>(*args)
}
