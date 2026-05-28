package com.gbmoraes.todo.adapters.inbound.http.dto.issue

import com.gbmoraes.todo.application.issue.dto.ListIssueOutput

data class ListIssueResponse(
    val items: List<IssueResponse>,
    val nextCursor: String?,
    val hasMore: Boolean,
) {
    companion object {
        fun fromOutput(output: ListIssueOutput) = ListIssueResponse(
            items = output.items.map { IssueResponse.fromOutput(it) },
            nextCursor = output.nextCursor,
            hasMore = output.hasMore,
        )
    }
}
