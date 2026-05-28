package com.gbmoraes.todo.adapters.outbound.id

import com.gbmoraes.todo.application.port.Id
import org.springframework.stereotype.Component
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Component
class UUIDv7 : Id {
    override fun generate(): String = Uuid.generateV7().toString()
}