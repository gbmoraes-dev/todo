package com.gbmoraes.todo.adapters.inbound.http.dto.issue

import com.gbmoraes.todo.application.issue.dto.CreateIssueInput
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateIssueRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val title: String,

    val description: String? = null,
) {
    fun toInput(userId: String) = CreateIssueInput(
        userId = userId,
        title = title,
        description = description ?: "",
    )
}