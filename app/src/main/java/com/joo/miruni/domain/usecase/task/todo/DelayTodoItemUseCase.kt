package com.joo.miruni.domain.usecase.task.todo

import java.time.LocalDateTime

interface DelayTodoItemUseCase {
    suspend operator fun invoke(id: Long, delayDateTime: LocalDateTime)
}
