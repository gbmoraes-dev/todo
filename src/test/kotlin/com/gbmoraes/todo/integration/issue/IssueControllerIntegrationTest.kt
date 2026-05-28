package com.gbmoraes.todo.integration.issue

import com.gbmoraes.todo.adapters.inbound.http.dto.issue.CreateIssueRequest
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.IssueResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.ListIssueResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.UpdateIssueRequest
import com.gbmoraes.todo.config.IntegrationTestBase
import com.gbmoraes.todo.integration.auth.helper.AuthHelper
import com.gbmoraes.todo.integration.issue.helper.IssueHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class IssueControllerIntegrationTest : IntegrationTestBase() {
    private lateinit var authHelper: AuthHelper
    private lateinit var issueHelper: IssueHelper

    @BeforeEach
    fun setup() {
        authHelper = AuthHelper(restTemplate)
        issueHelper = IssueHelper(restTemplate)
    }

    // ── create ───────────────────────────────────────────────────────────────

    @Test
    fun `POST issues should return 201 and issue`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        val response = restTemplate.exchange(
            "/issues",
            HttpMethod.POST,
            HttpEntity(CreateIssueRequest("Buy milk", "From the store"), headers),
            IssueResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.title).isEqualTo("Buy milk")
        assertThat(response.body?.completed).isFalse()
    }

    @Test
    fun `POST issues should return 401 without token`() {
        val response = restTemplate.postForEntity(
            "/issues",
            CreateIssueRequest("Buy milk", null),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `POST issues should return 422 when title is blank`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        val response = restTemplate.exchange(
            "/issues",
            HttpMethod.POST,
            HttpEntity(CreateIssueRequest("", null), headers),
            String::class.java,
        )

        assertThat(response.statusCode.value()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    // ── list ─────────────────────────────────────────────────────────────────

    @Test
    fun `GET issues should return paginated issues`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        issueHelper.createIssue(headers, "Issue 1")
        issueHelper.createIssue(headers, "Issue 2")

        val response = restTemplate.exchange(
            "/issues",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            ListIssueResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.items).hasSize(2)
        assertThat(response.body?.hasMore).isFalse()
    }

    @Test
    fun `GET issues should filter by completed`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        val issue = issueHelper.createIssue(headers)
        issueHelper.completeIssue(headers, issue.id)
        issueHelper.createIssue(headers, "Incomplete issue")

        val response = restTemplate.exchange(
            "/issues?completed=true",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            ListIssueResponse::class.java,
        )

        assertThat(response.body?.items).hasSize(1)
        assertThat(response.body?.items?.first()?.completed).isTrue()
    }

    @Test
    fun `GET issues should not return issues from other users`() {
        val token1 = authHelper.signUp("user1@test.com")
        val headers1 = authHelper.authHeaders(token1)

        val token2 = authHelper.signUp("user2@test.com")
        val headers2 = authHelper.authHeaders(token2)

        issueHelper.createIssue(headers1)

        val response = restTemplate.exchange(
            "/issues",
            HttpMethod.GET,
            HttpEntity<Unit>(headers2),
            ListIssueResponse::class.java,
        )

        assertThat(response.body?.items).isEmpty()
    }

    @Test
    fun `GET issues should paginate correctly`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        repeat(5) { issueHelper.createIssue(headers, "Issue $it") }

        val firstPage = restTemplate.exchange(
            "/issues?limit=3",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            ListIssueResponse::class.java,
        ).body!!

        assertThat(firstPage.items).hasSize(3)
        assertThat(firstPage.hasMore).isTrue()
        assertThat(firstPage.nextCursor).isNotNull()

        val secondPage = restTemplate.exchange(
            "/issues?limit=3&cursor=${firstPage.nextCursor}",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            ListIssueResponse::class.java,
        ).body!!

        assertThat(secondPage.items).hasSize(2)
        assertThat(secondPage.hasMore).isFalse()
    }

    // ── get ──────────────────────────────────────────────────────────────────

    @Test
    fun `GET issues id should return issue`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            IssueResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.id).isEqualTo(issue.id)
    }

    @Test
    fun `GET issues id should return 404 when not found`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)

        val response = restTemplate.exchange(
            "/issues/non-existent-id",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `GET issues id should return 403 when issue belongs to another user`() {
        val token1 = authHelper.signUp("user1@test.com")
        val headers1 = authHelper.authHeaders(token1)

        val token2 = authHelper.signUp("user2@test.com")
        val headers2 = authHelper.authHeaders(token2)

        val issue = issueHelper.createIssue(headers1)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.GET,
            HttpEntity<Unit>(headers2),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    // ── update ───────────────────────────────────────────────────────────────

    @Test
    fun `PATCH issues id should update issue`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.PATCH,
            HttpEntity(UpdateIssueRequest("New title", null), headers),
            IssueResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.title).isEqualTo("New title")
        assertThat(response.body?.description).isEqualTo(issue.description)
    }

    @Test
    fun `PATCH issues id should return 403 when issue belongs to another user`() {
        val token1 = authHelper.signUp("user1@test.com")
        val headers1 = authHelper.authHeaders(token1)

        val token2 = authHelper.signUp("user2@test.com")
        val headers2 = authHelper.authHeaders(token2)

        val issue = issueHelper.createIssue(headers1)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.PATCH,
            HttpEntity(UpdateIssueRequest("New title", null), headers2),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    // ── complete ─────────────────────────────────────────────────────────────

    @Test
    fun `PATCH issues id complete should complete issue`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        val response = restTemplate.exchange(
            "/issues/${issue.id}/complete",
            HttpMethod.PATCH,
            HttpEntity<Unit>(headers),
            IssueResponse::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.completed).isTrue()
    }

    @Test
    fun `PATCH issues id complete should return 409 when already completed`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        issueHelper.completeIssue(headers, issue.id)

        val response = restTemplate.exchange(
            "/issues/${issue.id}/complete",
            HttpMethod.PATCH,
            HttpEntity<Unit>(headers),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.CONFLICT)
    }

    // ── delete ───────────────────────────────────────────────────────────────

    @Test
    fun `DELETE issues id should delete issue`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.DELETE,
            HttpEntity<Unit>(headers),
            Unit::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @Test
    fun `DELETE issues id should return 404 after deletion`() {
        val token = authHelper.signUp()
        val headers = authHelper.authHeaders(token)
        val issue = issueHelper.createIssue(headers)

        restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.DELETE,
            HttpEntity<Unit>(headers),
            Unit::class.java,
        )

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.GET,
            HttpEntity<Unit>(headers),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `DELETE issues id should return 403 when issue belongs to another user`() {
        val token1 = authHelper.signUp("user1@test.com")
        val headers1 = authHelper.authHeaders(token1)

        val token2 = authHelper.signUp("user2@test.com")
        val headers2 = authHelper.authHeaders(token2)

        val issue = issueHelper.createIssue(headers1)

        val response = restTemplate.exchange(
            "/issues/${issue.id}",
            HttpMethod.DELETE,
            HttpEntity<Unit>(headers2),
            String::class.java,
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }
}