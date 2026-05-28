package com.gbmoraes.todo.integration.issue.helper

import com.gbmoraes.todo.adapters.inbound.http.dto.issue.CreateIssueRequest
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.IssueResponse
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod

class IssueHelper(private val restTemplate: TestRestTemplate) {
    fun createIssue(headers: HttpHeaders, title: String = "Buy milk"): IssueResponse {
        return restTemplate.exchange(
            "/issues",
            HttpMethod.POST,
            HttpEntity(CreateIssueRequest(title, "From the store"), headers),
            IssueResponse::class.java,
        ).body!!
    }

    fun completeIssue(headers: HttpHeaders, id: String): IssueResponse {
        return restTemplate.exchange(
            "/issues/$id/complete",
            HttpMethod.PATCH,
            HttpEntity<Unit>(headers),
            IssueResponse::class.java,
        ).body!!
    }
}