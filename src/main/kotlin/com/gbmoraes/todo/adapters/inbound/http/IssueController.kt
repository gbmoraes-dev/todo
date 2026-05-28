package com.gbmoraes.todo.adapters.inbound.http

import com.gbmoraes.todo.adapters.inbound.http.dto.issue.CreateIssueRequest
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.IssueResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.ListIssueResponse
import com.gbmoraes.todo.adapters.inbound.http.dto.issue.UpdateIssueRequest
import com.gbmoraes.todo.application.issue.CompleteIssueUseCase
import com.gbmoraes.todo.application.issue.CreateIssueUseCase
import com.gbmoraes.todo.application.issue.DeleteIssueUseCase
import com.gbmoraes.todo.application.issue.GetIssueUseCase
import com.gbmoraes.todo.application.issue.ListIssuesUseCase
import com.gbmoraes.todo.application.issue.UpdateIssueUseCase
import com.gbmoraes.todo.application.issue.dto.ListIssueInput
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/issues")
class IssueController(
    private val createIssueUseCase: CreateIssueUseCase,
    private val getIssueUseCase: GetIssueUseCase,
    private val listIssuesUseCase: ListIssuesUseCase,
    private val updateIssueUseCase: UpdateIssueUseCase,
    private val completeIssueUseCase: CompleteIssueUseCase,
    private val deleteIssueUseCase: DeleteIssueUseCase,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateIssueRequest,
        @AuthenticationPrincipal user: UserDetails,
    ): IssueResponse =
        createIssueUseCase.execute(request.toInput(user.username))
            .let { IssueResponse.fromOutput(it) }

    @GetMapping
    fun list(
        @AuthenticationPrincipal user: UserDetails,
        @RequestParam completed: Boolean? = null,
        @RequestParam cursor: String? = null,
        @RequestParam(defaultValue = "20") limit: Int,
    ): ListIssueResponse =
        listIssuesUseCase.execute(
            ListIssueInput(
                userId = user.username,
                completed = completed,
                cursor = cursor,
                limit = limit,
            )
        ).let { ListIssueResponse.fromOutput(it) }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: String,
        @AuthenticationPrincipal user: UserDetails,
    ): IssueResponse =
        getIssueUseCase.execute(id, user.username)
            .let { IssueResponse.fromOutput(it) }

    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateIssueRequest,
        @AuthenticationPrincipal user: UserDetails,
    ): IssueResponse =
        updateIssueUseCase.execute(request.toInput(id, user.username))
            .let { IssueResponse.fromOutput(it) }

    @PatchMapping("/{id}/complete")
    fun complete(
        @PathVariable id: String,
        @AuthenticationPrincipal user: UserDetails,
    ): IssueResponse =
        completeIssueUseCase.execute(id, user.username)
            .let { IssueResponse.fromOutput(it) }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: String,
        @AuthenticationPrincipal user: UserDetails,
    ) = deleteIssueUseCase.execute(id, user.username)
}