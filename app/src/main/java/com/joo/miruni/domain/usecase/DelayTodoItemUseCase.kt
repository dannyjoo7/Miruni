package com.joo.miruni.domain.usecase

import java.time.LocalDateTime

interface DelayTodoItemUseCase {
    suspend operator fun invoke(id: Long, delayDateTime: LocalDateTime)
}
