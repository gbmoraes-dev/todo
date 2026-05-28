package com.gbmoraes.todo.adapters.inbound.http.dto.issue

import com.gbmoraes.todo.application.issue.dto.UpdateIssueInput
import jakarta.validation.constraints.Size

data class UpdateIssueRequest(
    @field:Size(max = 100)
    val title: String? = null,

    val description: String? = null,
) {
    fun toInput(id: String, userId: String) = UpdateIssueInput(
        id = id,
        userId = userId,
        title = title,
        description = description,
    )
}