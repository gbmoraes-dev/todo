package com.gbmoraes.todo.adapters.inbound.http

import com.gbmoraes.todo.domain.issues.IssueException
import com.gbmoraes.todo.domain.user.UserException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
open class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ProblemDetail> {
        val fieldErrors = ex.bindingResult.fieldErrors
            .groupBy { it.field }
            .mapValues { (_, fieldList) -> fieldList.map { it.defaultMessage } }

        val problem = ProblemDetail
            .forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed")
            .also { problem -> problem.setProperty("errors", fieldErrors) }

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem)
    }

    @ExceptionHandler(UserException.EmailAlreadyInUse::class)
    fun handleEmailInUse(ex: UserException.EmailAlreadyInUse) =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Conflict")

    @ExceptionHandler(UserException.InvalidCredentials::class)
    fun handleInvalidCredentials(ex: UserException.InvalidCredentials) =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.message ?: "Unauthorized")

    @ExceptionHandler(IssueException.NotFound::class)
    fun handleIssueNotFound(ex: IssueException.NotFound) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message ?: "Not found")

    @ExceptionHandler(IssueException.Unauthorized::class)
    fun handleIssueUnauthorized(ex: IssueException.Unauthorized) =
        ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.message ?: "Forbidden")

    @ExceptionHandler(IssueException.AlreadyCompleted::class)
    fun handleIssueAlreadyCompleted(ex: IssueException.AlreadyCompleted) =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message ?: "Conflict")

    @ExceptionHandler(IssueException.EmptyTitle::class)
    fun handleIssueEmptyTitle(ex: IssueException.EmptyTitle) =
        ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.message ?: "Unprocessable")
}