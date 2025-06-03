package com.joo.miruni.domain.usecase.task.todo

import java.time.LocalDateTime

interface DelayAllTodoItemUseCase {
    suspend operator fun invoke(itemIds: List<Long>, delayDateTime: LocalDateTime)
}
