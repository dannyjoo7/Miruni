package com.joo.miruni.domain.usecase

import com.joo.miruni.domain.model.TodoItemsEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


interface GetTodoItemsForAlarmUseCase {
    suspend operator fun invoke(
        selectDate: LocalDateTime,
        lastDeadLine: LocalDateTime?,
    ): Flow<TodoItemsEntity>
}
