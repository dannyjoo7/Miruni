package com.joo.miruni.domain.usecase

import com.joo.miruni.presentation.home.ThingsTodo
import java.time.LocalDateTime

interface DelayTodoItemUseCase {
    suspend operator fun invoke(id: Long, delayDateTime: LocalDateTime)
}
