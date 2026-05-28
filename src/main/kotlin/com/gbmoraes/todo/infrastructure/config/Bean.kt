package com.gbmoraes.todo.infrastructure.config

import com.gbmoraes.todo.application.auth.SignInUseCase
import com.gbmoraes.todo.application.auth.SignUpUseCase
import com.gbmoraes.todo.application.port.Encoder
import com.gbmoraes.todo.application.port.Id
import com.gbmoraes.todo.application.port.Token
import com.gbmoraes.todo.application.issue.CompleteIssueUseCase
import com.gbmoraes.todo.application.issue.CreateIssueUseCase
import com.gbmoraes.todo.application.issue.DeleteIssueUseCase
import com.gbmoraes.todo.application.issue.GetIssueUseCase
import com.gbmoraes.todo.application.issue.ListIssuesUseCase
import com.gbmoraes.todo.application.issue.UpdateIssueUseCase
import com.gbmoraes.todo.domain.issues.IssueRepository
import com.gbmoraes.todo.domain.user.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Bean {
    @Bean
    open fun signUpUseCase(
        userRepository: UserRepository,
        encoder: Encoder,
        token: Token,
        id: Id,
    ): SignUpUseCase = SignUpUseCase(
        userRepository = userRepository,
        id = id,
        encoder = encoder,
        token = token,
    )

    @Bean
    open fun signInUseCase(
        userRepository: UserRepository,
        encoder: Encoder,
        token: Token,
    ): SignInUseCase = SignInUseCase(
        userRepository = userRepository,
        encoder = encoder,
        token = token,
    )

    @Bean
    open fun createTodoUseCase(
        issueRepository: IssueRepository, id: Id
    ): CreateIssueUseCase = CreateIssueUseCase(
        issueRepository = issueRepository,
        id = id,
    )

    @Bean
    open fun getTodoUseCase(
        issueRepository: IssueRepository
    ): GetIssueUseCase = GetIssueUseCase(
        issueRepository = issueRepository,
    )

    @Bean
    open fun listTodosUseCase(
        issueRepository: IssueRepository
    ): ListIssuesUseCase = ListIssuesUseCase(
        issueRepository = issueRepository,
    )

    @Bean
    open fun updateTodoUseCase(
        issueRepository: IssueRepository
    ): UpdateIssueUseCase = UpdateIssueUseCase(
        issueRepository = issueRepository,
    )

    @Bean
    open fun completeTodoUseCase(
        issueRepository: IssueRepository
    ): CompleteIssueUseCase = CompleteIssueUseCase(
        issueRepository = issueRepository,
    )

    @Bean
    open fun deleteTodoUseCase(
        issueRepository: IssueRepository
    ): DeleteIssueUseCase = DeleteIssueUseCase(
        issueRepository = issueRepository,
    )
}